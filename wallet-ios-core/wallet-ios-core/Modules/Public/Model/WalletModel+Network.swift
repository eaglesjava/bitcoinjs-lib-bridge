//
//  WalletModel+Network.swift
//  wallet-ios-core
//
//  Created by 仇弘扬 on 2017/12/5.
//  Copyright © 2017年 BitBill. All rights reserved.
//

import Foundation
import SwiftyJSON

extension WalletModel {
    
    func syncWallet(json: JSON) {
		
		func loadTXs(version: Int64) {
            getTransactionHistoryFromSever(page: 0, size: (bitcoinWallet?.transactions?.count)! + 1000, success: { (txs) in
				self.bitcoinWallet?.version = version
				do {
					try BILWalletManager.shared.saveWallets()
					self.bitcoinWallet?.needLoadServer = false
				} catch {
					debugPrint(error)
				}
			}, failure: { (msg, code) in
				debugPrint(msg)
			})
		}
        
        func handleChangeAddress(changeIndex: Int64) {
            if lastBTCChangeAddressIndex < changeIndex {
                generateAddresses(pubkey: changeExtPublicKey!, from: lastBTCChangeAddressIndex, to: changeIndex, success: { (addresses) in
                    self.bitcoinWallet?.needLoadServer = true
                    loadTXs(version: (self.bitcoinWallet?.version)!)
                }, failure: { (msg, code) in
                    debugPrint(msg)
                })
            }
        }
		
        let addressIndex = json["indexNo"].int64Value
        let changeIndex = json["changeIndexNo"].int64Value
        if lastBTCAddressIndex < addressIndex {
            generateAddresses(pubkey: mainExtPublicKey!, from: lastBTCAddressIndex, to: addressIndex, success: { (addresses) in
                if self.lastBTCChangeAddressIndex < changeIndex {
                    handleChangeAddress(changeIndex: changeIndex)
                }
                else
                {
                    self.bitcoinWallet?.needLoadServer = true
                    loadTXs(version: (self.bitcoinWallet?.version)!)
                }
            }, failure: { (msg, code) in
                debugPrint(msg)
            })
        }
        else
        {
            handleChangeAddress(changeIndex: changeIndex)
        }
        
        let serverVersion = json["version"].int64Value
        bitcoinWallet?.needLoadServer = (bitcoinWallet?.version)! < serverVersion
        if (bitcoinWallet?.needLoadServer)! {
			loadTXs(version: serverVersion)
        }
    }
    
    func createWalletToServer(success: @escaping ([String: JSON]) -> Void, failure: @escaping (_ message: String, _ code: Int) -> Void) {
        guard let _ = id else {
            failure(.publicWalletIDError, -1)
            return
        }
        guard let _ = mainExtPublicKey else {
            failure(.publicWalletExtKeyError, -2)
            return
        }
        guard let _ = changeExtPublicKey else {
            failure(.publicWalletExtKeyError, -3)
            return
        }
        BILNetworkManager.request(request: .createWallet(wallet: self), success: success, failure: failure)
    }
    
    func deleteWalletInSever(success: @escaping () -> Void, failure: @escaping (_ message: String, _ code: Int) -> Void) {
        guard let extKey = mainExtPublicKey else {
            failure(.publicWalletExtKeyError, -1)
            return
        }
        BILNetworkManager.request(request: .deleteWallet(extendedKeyHash: extKey.md5()), success: { (result) in
            do {
                try BILWalletManager.shared.remove(wallet: self)
                NotificationCenter.default.post(name: .walletCountDidChanged, object: nil)
                success()
            } catch {
                failure(error.localizedDescription, -1)
            }
        }, failure: failure)
    }
    
    func importWalletToServer(success: @escaping ([String: JSON]) -> Void, failure: @escaping (_ message: String, _ code: Int) -> Void) {
        guard let _ = id else {
            failure(.publicWalletIDError, -1)
            return
        }
        guard let _ = mainExtPublicKey else {
            failure(.publicWalletExtKeyError, -2)
            return
        }
        guard let _ = changeExtPublicKey else {
            failure(.publicWalletExtKeyError, -3)
            return
        }
        BILNetworkManager.request(request: .importWallet(wallet: self), success: { (result) in
            success(result)
        }, failure: failure)
    }
    
    
    /// 从服务器获取余额
    ///
    /// - Parameters:
    ///   - success: 可能会有两次调用，第一次为当前余额，第二次为服务器返回
    ///   - failure: 失败
    func getBalanceFromServer(success: @escaping (_ wallet: WalletModel) -> Void, failure: @escaping (_ message: String, _ code: Int) -> Void) {
        success(self)
        WalletModel.getBalanceFromServer(wallets: [self], success: { () in
            success(self)
        }, failure: failure)
    }
    
    func lastConfirmedTranscationServerID() -> String {
        var id = "0"
        for tx in btc_transactionArray.reversed() {
            debugPrint("\(tx.height), \(tx.serverID)")
            if tx.height > 0 {
                id = "\(tx.serverID)"
            }
            else
            {
                break
            }
        }
        return id
    }
    
    func getTransactionHistoryFromSever(page: Int, size: Int, success: @escaping (_ txs: [BTCTransactionModel]) -> Void, failure: @escaping (_ message: String, _ code: Int) -> Void) {
        guard let extKey = mainExtPublicKey else {
            failure(.publicWalletExtKeyError, -1)
            return
        }
        success(btc_transactionArray)
        BILNetworkManager.request(request: .getTransactionHistory(extendedKeyHash: extKey.md5(), id: lastConfirmedTranscationServerID(), page: page, size: size), success: { (result) in
            debugPrint(result)
            let json = JSON(result)
            let datas = json["list"].arrayValue
            for json in datas {
                var model = bil_btc_transactionManager.newModelIfNeeded(keyValues: (key: "txHash", value: json["txHash"].stringValue))
                if model.wallet != self {
                    model = bil_btc_transactionManager.newModel()
                }
                model.setProperties(json: json, inWallet: self)
            }
            do {
                try BILWalletManager.shared.saveWallets()
                success(self.btc_transactionArray)
            } catch {
                failure(error.localizedDescription, -2)
            }
        }, failure: failure)
    }
    
    func getUTXOFromServer(success: @escaping (_ utxo: [BitcoinUTXOModel]) -> Void, failure: @escaping (_ message: String, _ code: Int) -> Void) {
        getTXBuildConfigurationFromServer(success: { (utxos, fees, fee) in
            self.updateUTXO(utxos: utxos)
            success(utxos)
        }, failure: failure)
    }
    
    func getTXBuildConfigurationFromServer(success: @escaping (_ utxo: [BitcoinUTXOModel], _ fee: [BTCFee], _ bestFee: BTCFee?) -> Void, failure: @escaping (_ message: String, _ code: Int) -> Void) {
        guard let extKey = mainExtPublicKey else {
            failure(.publicWalletExtKeyError, -1)
            return
        }
        BILNetworkManager.request(request: .getTransactionBuildConfig(extendedKeyHash: extKey.md5()), success: { (result) in
            debugPrint(result)
            let json = JSON(result)
            let utxoDatas = json["utxo"].arrayValue
            var utxoModels = [BitcoinUTXOModel]()
			var satoshisSum: Int64 = 0
            for json in utxoDatas {
				let utxo = BitcoinUTXOModel(jsonData: json)
                var isContain = false
                for u in utxoModels {
                    if u == utxo {
                        isContain = true
                        break
                    }
                }
                if !isContain {
                    satoshisSum += utxo.satoshiAmount
                    utxoModels.append(utxo)
                }
            }
            self.btcBalance = satoshisSum
            let feeDatas = json["fees"].arrayValue
            var fees = [BTCFee]()
            var bestFee: BTCFee? = nil
            for json in feeDatas {
                let fee = BTCFee(jsonData: json)
                if fee.isBest {
                    bestFee = fee
                }
                fees.append(fee)
            }
            
            success(utxoModels, fees, bestFee)
        }, failure: failure)
    }
    
    func send(transaction: BTCTransaction, success: @escaping ([String: JSON]) -> Void, failure: @escaping (_ message: String, _ code: Int) -> Void) {
        guard let extKey = mainExtPublicKey else {
            failure(.publicWalletExtKeyError, -1)
            return
        }
        let tx = transaction
        BILNetworkManager.request(request: .sendTransaction(extendedKeyHash: extKey.md5(), address: tx.address, inAddress: tx.inputAddressString, amount: tx.amount, txHash: tx.txHash, txHex: tx.hexString, remark: tx.remark ?? ""), success: { (result) in
            success(result)
        }, failure: failure)
    }
    
    static func getUnconfirmTransactionFromSever(wallets: [WalletModel], success: @escaping (_ txs: [BTCTransactionModel]) -> Void, failure: @escaping (_ message: String, _ code: Int) -> Void) {
        guard wallets.count > 0 else {
            failure(.publicWalletDataError, -1)
            return
        }
        BILNetworkManager.request(request: .getUnconfirmTransaction(wallets: wallets), success: { (result) in
            debugPrint(result)
            let json = JSON(result)
            let txDatas = json["list"].arrayValue
            var utx = [BTCTransactionModel]()
            for json in txDatas {
                let model = bil_btc_transactionManager.newModelIfNeeded(key: "txHash", value: json["txHash"].stringValue)
                model.setProperties(json: json)
                if model.wallet != nil {
                    utx.append(model)
                }
            }
            do {
                try BILWalletManager.shared.saveWallets()
				utx.sort(by: { (lhs, rhs) -> Bool in
					rhs.createdDate!.isEarlier(than: lhs.createdDate!)
				})
                success(utx)
            } catch {
                failure(error.localizedDescription, -2)
            }
            
        }, failure: failure)
    }
    
    static func getKeyHash(wallets: [WalletModel]) -> String {
        var keys = [String]()
        for wallet in wallets {
            guard let key = wallet.mainExtPublicKey else { continue }
            keys.append(key.md5())
        }
        return keys.joined(separator: "|")
    }
    
    static func getBalanceFromServer(wallets: [WalletModel], success: @escaping () -> Void, failure: @escaping (_ message: String, _ code: Int) -> Void) {
        let hashes = getKeyHash(wallets: wallets)
        BILNetworkManager.request(request: .getBalance(extendedKeyHash: hashes), success: { (result) in
            debugPrint(result)
            
            let json = JSON(result)
            for wallet in wallets {
                guard let id = wallet.id else { continue }
                let subJson = json[id]
                wallet.btcBalance = subJson["balance"].int64Value
                wallet.btcUnconfirmBalance = subJson["unconfirm"].int64Value
                debugPrint(subJson)
            }
            do {
                try BILWalletManager.shared.saveWallets()
                NotificationCenter.default.post(name: .balanceDidChanged, object: nil)
                success()
            } catch {
                debugPrint(error)
                failure(error.localizedDescription, -1)
            }
        }, failure: failure)
    }
    
    static func getWalletIDFromSever(mainExtPublicKey: String, success: @escaping (_ id: String?) -> Void, failure: @escaping (_ message: String, _ code: Int) -> Void) {
        BILNetworkManager.request(request: .getWalletID(extendedKeyHash: mainExtPublicKey.md5()), success: { (result) in
            debugPrint(result)
            let json = JSON(result)
            guard let id = json["walletId"].string else {
                failure(.publicWalletDataError, -1)
                return
            }
            success(id.isEmpty ? nil : id)
        }, failure: failure)
    }
}
