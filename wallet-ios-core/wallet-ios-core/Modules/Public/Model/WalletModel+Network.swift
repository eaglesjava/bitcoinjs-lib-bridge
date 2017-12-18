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
    func createWalletToServer(success: @escaping ([String: JSON]) -> Void, failure: @escaping (_ message: String, _ code: Int) -> Void) {
        guard let walletID = id else {
            failure("ID不能为空", -1)
            return
        }
        guard let extKey = mainExtPublicKey else {
            failure("extKey不能为空", -1)
            return
        }
//		success([:])
        BILNetworkManager.request(request: .createWallet(walletID: walletID, extendedKey: extKey), success: success, failure: failure)
    }
    
    func importWalletToServer(success: @escaping ([String: JSON]) -> Void, failure: @escaping (_ message: String, _ code: Int) -> Void) {
        guard let walletID = id else {
            failure("ID不能为空", -1)
            return
        }
        guard let extKey = mainExtPublicKey else {
            failure("extKey不能为空", -1)
            return
        }
        BILNetworkManager.request(request: .importWallet(walletID: walletID, extendedKey: extKey), success: success, failure: failure)
    }
    
    
    /// 从服务器获取余额
    ///
    /// - Parameters:
    ///   - success: 可能会有两次调用，第一次为当前余额，第二次为服务器返回
    ///   - failure: 失败
    func getBalanceFromServer(success: @escaping (_ wallet: WalletModel) -> Void, failure: @escaping (_ message: String, _ code: Int) -> Void) {
        guard let extKey = mainExtPublicKey else {
            failure("extKey不能为空", -1)
            return
        }
        success(self)
        WalletModel.getBalanceFromServer(mainExtPublicKey: extKey, success: { (satoshiBalance, unconfirmBalance) in
            self.btcBalance = Int64(satoshiBalance)
            self.btcUnconfirmBalance = Int64(unconfirmBalance)
            do {
                try BILWalletManager.shared.saveWallets()
                NotificationCenter.default.post(name: .balanceDidChanged, object: nil)
                success(self)
            } catch {
                debugPrint(error)
                failure(error.localizedDescription, -1)
            }
        }, failure: failure)
    }
    
    func getTransactionHistoryFromSever(page: Int, size: Int, success: @escaping (_ txs: [BILTransactionHistoryModel]) -> Void, failure: @escaping (_ message: String, _ code: Int) -> Void) {
        guard let extKey = mainExtPublicKey else {
            failure("extKey不能为空", -1)
            return
        }
        BILNetworkManager.request(request: .getTransactionHistory(extendedKeyHash: extKey.md5(), page: page, size: size), success: { (result) in
            print(result)
            let json = JSON(result)
            let datas = json["history"].arrayValue
            var models = [BILTransactionHistoryModel]()
            for json in datas {
                models.append(BILTransactionHistoryModel(jsonData: json))
            }
            success(models)
        }, failure: failure)
    }
    
    func getUTXOFromServer(success: @escaping (_ utxo: [BitcoinUTXOModel]) -> Void, failure: @escaping (_ message: String, _ code: Int) -> Void) {
        guard let extKey = mainExtPublicKey else {
            failure("extKey不能为空", -1)
            return
        }
        BILNetworkManager.request(request: .getUTXO(extendedKeyHash: extKey.md5()), success: { (result) in
            print(result)
            let json = JSON(result)
            let utxoDatas = json["utxo"].arrayValue
            var utxoModels = [BitcoinUTXOModel]()
            for json in utxoDatas {
                utxoModels.append(BitcoinUTXOModel(jsonData: json))
            }
            success(utxoModels)
        }, failure: failure)
    }
    
    
    
    func getTXBuildConfigurationFromServer(success: @escaping (_ utxo: [BitcoinUTXOModel], _ fee: [BTCFee], _ bestFee: BTCFee?) -> Void, failure: @escaping (_ message: String, _ code: Int) -> Void) {
        guard let extKey = mainExtPublicKey else {
            failure("extKey不能为空", -1)
            return
        }
        BILNetworkManager.request(request: .getTransactionBuildConfig(extendedKeyHash: extKey.md5()), success: { (result) in
            print(result)
            let json = JSON(result)
            let utxoDatas = json["utxo"].arrayValue
            var utxoModels = [BitcoinUTXOModel]()
            for json in utxoDatas {
                utxoModels.append(BitcoinUTXOModel(jsonData: json))
            }
            
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
            failure("extKey不能为空", -1)
            return
        }
        let tx = transaction
        BILNetworkManager.request(request: .sendTransaction(extendedKeyHash: extKey.md5(), address: tx.address, inAddress: tx.inputAddressString, amount: tx.amount, txHash: tx.txHash, txHex: tx.hexString), success: { (result) in
            success(result)
        }, failure: failure)
    }
    
    static func getBalanceFromServer(mainExtPublicKey: String, success: @escaping (_ satoshiBalance: Int, _ unconfirmBalance: Int) -> Void, failure: @escaping (_ message: String, _ code: Int) -> Void) {
        BILNetworkManager.request(request: .getBalance(extendedKeyHash: mainExtPublicKey.md5()), success: { (result) in
            debugPrint(result)
            let json = JSON(result)
            guard let balance = json["balance"].int, let unconfirm = json["unconfirm"].int else {
                failure("获取到错误的数据", -1)
                return
            }
            success(balance, unconfirm)
        }, failure: failure)
    }
    
    static func getWalletIDFromSever(mainExtPublicKey: String, success: @escaping (_ id: String?) -> Void, failure: @escaping (_ message: String, _ code: Int) -> Void) {
        BILNetworkManager.request(request: .getWalletID(extendedKeyHash: mainExtPublicKey.md5()), success: { (result) in
            debugPrint(result)
            let json = JSON(result)
            guard let id = json["walletId"].string else {
                failure("获取到错误的数据", -1)
                return
            }
            success(id.isEmpty ? nil : id)
        }, failure: failure)
    }
}
