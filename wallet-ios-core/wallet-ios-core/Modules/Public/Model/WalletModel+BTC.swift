//
//  WalletModel+BTC.swift
//  wallet-ios-core
//
//  Created by 仇弘扬 on 2017/12/5.
//  Copyright © 2017年 BitBill. All rights reserved.
//

import Foundation
import SwiftyJSON
import DateToolsSwift

let BTC_SATOSHI: Int64 = 100000000

func BTCFormatString(btc: Int64) -> String {
    if btc == 0 {
        return "0.00"
    }
    let d = Double(btc) / Double(BTC_SATOSHI)
    var str = String(format: "%.8f", d)
	while str.hasSuffix("0") {
		str.removeLast()
	}
    return str
}

enum BitcoinAddressType {
    case normal
    case change
}

extension WalletModel {
    var btcBalance: Int64 {
        set {
            bitcoinWallet?.satoshi = newValue
        }
        get {
            return bitcoinWallet?.satoshi ?? 0
        }
    }
    var btcUnconfirmBalance: Int64 {
        set {
            bitcoinWallet?.unconfirmSatoshi = newValue
        }
        get {
            return bitcoinWallet?.unconfirmSatoshi ?? 0
        }
    }
    var lastBTCAddressIndex: Int64 {
        set {
            bitcoinWallet?.lastAddressIndex = newValue
        }
        get {
            return bitcoinWallet?.lastAddressIndex ?? 0
        }
    }
    var lastBTCChangeAddressIndex: Int64 {
        set {
            bitcoinWallet?.lastChangeAddressIndex = newValue
        }
        get {
            return bitcoinWallet?.lastChangeAddressIndex ?? 0
        }
    }
}

extension WalletModel {
    
    static func fetch(by addresses: [String], isAll: Bool = true) -> WalletModel? {
        for wallet in BILWalletManager.shared.wallets {
            if wallet.contain(btcAddresses: addresses, isAll: isAll) {
                return wallet
            }
        }
        return nil
    }
    
    static func fetchWallets(by addresses: [String]) -> [WalletModel] {
        var wallets = [WalletModel]()
        for wallet in BILWalletManager.shared.wallets {
            if wallet.contain(btcAddresses: addresses, isAll: false) {
                wallets.append(wallet)
            }
        }
        return wallets
    }
    
    func contain(btcAddress: String) -> Bool {
        return setContain(addreessSet: bitcoinWallet?.addresses, address: btcAddress) || setContain(addreessSet: bitcoinWallet?.changeAddresses, address: btcAddress)
    }
    
    func setContain(addreessSet: NSOrderedSet?, address: String) -> Bool {
        guard let set = addreessSet else { return false }
        let count = set.filter({
            ($0 as? BTCWalletAddressModel)?.address == address
        }).count
        return count > 0
    }
    
    func contain(btcAddresses: [String], isAll: Bool = true) -> Bool {
        var count = 0
        for address in btcAddresses {
            if contain(btcAddress: address) {
                count += 1
            }
            if !isAll, count >= 1 {
                return true
            }
        }
        return count > 0 && count == btcAddresses.count
    }
	
	func randomAddress() -> BTCWalletAddressModel {
		let count = bitcoinWallet!.addresses!.count
		return bitcoinWallet!.addresses![Int(arc4random()) % count] as! BTCWalletAddressModel
	}
    
    func randomChangeAddress() -> BTCWalletAddressModel? {
        guard bitcoinWallet!.changeAddresses!.count > 0 else {
            return nil
        }
        let count = bitcoinWallet!.changeAddresses!.count
        return bitcoinWallet!.changeAddresses![Int(arc4random()) % count] as? BTCWalletAddressModel
    }
    
    func unconfirmTxs() -> [BTCTransactionModel] {
        let txs = btc_transactionArray
        return txs.filter({
            return $0.height == -1
        })
    }
	
	static func allUnconfirmBTCTransactions() -> [BTCTransactionModel] {
		var txs: [BTCTransactionModel] = []
		for wallet in BILWalletManager.shared.wallets {
			txs.append(contentsOf: wallet.unconfirmTxs())
		}
		return txs
	}
    
    func lastBTCAddress(success: @escaping (String) -> Void, failure: @escaping (String) -> Void) {
        guard let xpub = mainExtPublicKey else {
            failure(.publicWalletIDError)
            return
        }
        let index = lastBTCAddressIndex
        if index == bitcoinWallet!.addresses!.count - 1 {
            guard let address = (bitcoinWallet?.addresses?.lastObject as? BTCWalletAddressModel)?.address else {
                failure(.publicWalletGetAddressError)
                return
            }
            success(address)
        }
        else
        {
            BitcoinJSBridge.shared.getAddress(xpub: xpub, index: Int(index), success: { (address) in
                if let add = address as? String {
                    let addModel = bil_btc_wallet_addressManager.newModelIfNeeded(key: "address", value: add)
                    addModel.address = add
                    addModel.index = index
                    addModel.satoshi = 0
                    self.bitcoinWallet?.addToAddresses(addModel)
                    success(add)
                } else {
                    failure(.publicWalletGetAddressError)
                }
            }) { (error) in
                failure(error.localizedDescription)
            }
        }
    }
    
    func refreshAddressToSever(index: Int64, changeIndex: Int64 = 0, success: @escaping ([BTCWalletAddressModel], [BTCWalletAddressModel]) -> Void, failure: @escaping (String) -> Void) {
        guard let extPub = mainExtPublicKey else {
            failure(.publicWalletExtKeyError)
            return
        }
        
        guard index > lastBTCAddressIndex || changeIndex > lastBTCChangeAddressIndex else {
            failure("index 小于或等于当前地址，不需要刷新")
            return
        }

        BILNetworkManager.request(request: .refreshAddress(extendedKeyHash: extPub.md5(), index: index, changeIndex: changeIndex), success: { (result) in
            debugPrint(result)
            let json = JSON(result)
            let serverIndex = json["indexNo"].int64Value
            let serverChangeIndex = json["changeIndexNo"].int64Value
            var localIndex = self.lastBTCAddressIndex
            var localChangeIndex = self.lastBTCChangeAddressIndex
            localIndex = min(localIndex, serverIndex)
            localChangeIndex = min(localChangeIndex, serverChangeIndex)
            self.lastBTCAddressIndex = serverIndex
            self.lastBTCChangeAddressIndex = serverChangeIndex
            if index > serverIndex {
                failure(.publicWalletNoMoreAddress)
                return
            }
            self.generateAddresses(from: localIndex, to: self.lastBTCAddressIndex, success: { (models) in
                if self.lastBTCChangeAddressIndex == -1 {
                    success(models, [])
                    return
                }
                self.generateAddresses(type: .change, from: localChangeIndex, to: self.lastBTCChangeAddressIndex, success: { (changeModels) in
                    success(models, changeModels)
                }, failure: { (msg, code) in
                    failure("change: \(msg)")
                })
            }, failure: { (msg, code) in
                failure(msg)
            })
            
        }, failure: { (msg, code) in
            debugPrint(msg)
            failure(msg)
        })
    }
	
    func getNewBTCAddress(success: @escaping (String) -> Void, failure: @escaping (String) -> Void) {
        let tempIndex = lastBTCAddressIndex + 1
        refreshAddressToSever(index: tempIndex, changeIndex: lastBTCChangeAddressIndex, success: { (models, changeModels) in
            guard let address = models.last?.address else {
                failure(.publicWalletGenerateAddressError)
                return
            }
            success(address)
        }, failure: failure)
	}
    
    func getNewBTCChangeAddress(success: @escaping (String) -> Void, failure: @escaping (String) -> Void) {
        let tempIndex = lastBTCChangeAddressIndex + 1
        refreshAddressToSever(index: lastBTCAddressIndex, changeIndex: tempIndex, success: { (models, changeModels) in
            guard let address = changeModels.last?.address else {
                failure(.publicWalletGenerateAddressError)
                return
            }
            success(address)
        }, failure: failure)
    }
    
    func isTooMoreUnusedBTCAddress() -> Bool {
        let limitCount = 20
        let count = btc_addressModels.count
        guard count >= limitCount else {
            return false
        }
        let last20Addresses = btc_addressModels[(count - limitCount + 1)...(count - 1)]
        let usedCount = last20Addresses.filter { $0.isUsed }.count
        return usedCount == 0
    }
    
    func generateAddresses(type: BitcoinAddressType = .normal, from: Int64, to: Int64, success: @escaping ([BTCWalletAddressModel]) -> Void, failure: @escaping (_ message: String, _ code: Int) -> Void) {
        guard from <= to else {
            failure(.publicWalletIndexError, -1)
            return
        }
		let beginDate = Date()
        BitcoinJSBridge.shared.getAddresses(xpub: type == .normal ? mainExtPublicKey! : changeExtPublicKey!, fromIndex: from, toIndex: to, success: { (result) in
            debugPrint(result)
            guard let array = result as? [String] else {
                failure(.publicWalletGenerateAddressError, -1)
                return
            }
            var models = [BTCWalletAddressModel]()
            for address in array {
                let add = bil_btc_wallet_addressManager.newModelIfNeeded(key: "address", value: address)
                add.address = address
                add.satoshi = 0
                switch type {
                case .normal:
                    self.bitcoinWallet?.addToAddresses(add)
                case .change:
                    self.bitcoinWallet?.addToChangeAddresses(add)
                }
                models.append(add)
            }
            switch type {
            case .normal:
                self.lastBTCAddressIndex = to
            case .change:
                self.lastBTCChangeAddressIndex = to
            }
            do {
                try BILWalletManager.shared.saveWallets()
				
                success(models)
				let endDate = Date()
				debugPrint("生成 \(to - from + 1) 个地址，用时：\(endDate.seconds(from: beginDate))s")
            } catch {
                failure(error.localizedDescription, -2)
            }
        }) { (error) in
            debugPrint(error)
            failure(error.localizedDescription, -2)
        }
    }
	
    func updateUTXO(utxos: [BitcoinUTXOModel]) {
        for add in btc_addressModels {
            add.satoshi = 0
        }
        for utxo in utxos {
            let add = utxo.address
            let model = bil_btc_wallet_addressManager.newModelIfNeeded(key: "address", value: add)
			model.isUsed = true
			model.address = add
            model.satoshi += utxo.satoshiAmount
        }
        do {
            try BILWalletManager.shared.saveWallets()
            NotificationCenter.default.post(name: .localUTXODidChanged, object: nil)
        } catch {
            debugPrint("保存本地 UTXO 数据失败 \(error.localizedDescription)")
        }
    }
    
	func updateUTXOAtLocal() {
		for add in btc_addressModels {
			add.satoshi = 0
		}
		for tx in btc_transactionArray {
			debugPrint("------")
			if tx.height == -1 {
				continue
			}
			for add in tx.inputAddressModels {
				if let wAdd = bil_btc_wallet_addressManager.fetch(key: "address", value: add.address!) {
					debugPrint("add: \(add.address!) - \(add.satoshi)")
					wAdd.satoshi -= add.satoshi
					wAdd.isUsed = true
				}
			}
			for add in tx.outputAddressModels {
				if let wAdd = bil_btc_wallet_addressManager.fetch(key: "address", value: add.address!) {
					debugPrint("add: \(add.address!) + \(add.satoshi)")
					wAdd.satoshi += add.satoshi
					wAdd.isUsed = true
				}
			}
		}
		do {
			try BILWalletManager.shared.saveWallets()
            NotificationCenter.default.post(name: .localUTXODidChanged, object: nil)
		} catch {
			debugPrint("保存本地 UTXO 数据失败")
		}
	}
	
	var btc_addressModels: [BTCWalletAddressModel] {
		get {
			return self.bitcoinWallet?.addresses?.array as! [BTCWalletAddressModel]
		}
	}
    
    var btc_changeAddressModels: [BTCWalletAddressModel] {
        get {
            return self.bitcoinWallet?.changeAddresses?.array as! [BTCWalletAddressModel]
        }
    }
	
    var btc_transactionArray: [BTCTransactionModel] {
        get {
            return bitcoinWallet?.transactions!.sortedArray(comparator: { (lhs, rhs) -> ComparisonResult in
                let l = lhs as! BTCTransactionModel
                let r = rhs as! BTCTransactionModel
                return r.createdDate!.compare(l.createdDate!)
            }) as! [BTCTransactionModel]
        }
    }
    
    var btc_balanceString: String {
        get {
            return BTCFormatString(btc: btcBalance)
        }
    }
    var btc_unconfirm_balanceString: String {
        get {
            return BTCFormatString(btc: btcUnconfirmBalance)
        }
    }
    var btc_currencyString: String {
        get {
            return getCurrency(btcValue: btcBalance)
        }
    }
}
