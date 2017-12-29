//
//  WalletModel+BTC.swift
//  wallet-ios-core
//
//  Created by 仇弘扬 on 2017/12/5.
//  Copyright © 2017年 BitBill. All rights reserved.
//

import Foundation

let BTC_SATOSHI: Int64 = 100000000

func BTCFormatString(btc: Int64) -> String {
    if btc == 0 {
        return "0.00"
    }
    let str = String(btc % BTC_SATOSHI)
    let d = Double(btc) / Double(BTC_SATOSHI)
    if str.count > 2 {
        return String(format: "%f", d)
    }
    var count = 0
    for char in str.reversed() {
        if char == "0" {
            count += 1
        }
        else
        {
            break
        }
    }
    return String(format: "%.\(8 - count)f", d)
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
    
    func contain(btcAddress: String) -> Bool {
        guard let set = addresses else { return false }
        let count = set.filter({
            ($0 as? BTCAddressModel)?.address == btcAddress
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
        return count == btcAddresses.count
    }
    
    func lastBTCAddress(success: @escaping (String) -> Void, failure: @escaping (String) -> Void) {
        guard let xpub = mainExtPublicKey else {
            failure("主扩展公钥为空")
            return
        }
        let index = lastAddressIndex
        if index == (addresses?.count)! - 1 {
            guard let address = (addresses?.lastObject as? BTCAddressModel)?.address else {
                failure("获取地址失败")
                return
            }
            success(address)
        }
        else
        {
            BitcoinJSBridge.shared.getAddress(xpub: xpub, index: Int(index), success: { (address) in
                if let add = address as? String {
                    let addModel = bil_btc_addressManager.newModel()
                    addModel.address = add
                    addModel.index = index
                    addModel.satoshi = 0
                    self.addToAddresses(addModel)
                    success(add)
                } else {
                    failure("获取地址失败")
                }
            }) { (error) in
                failure(error.localizedDescription)
            }
        }
    }
	
    func getNewBTCAddress(step: Int64 = 1, success: @escaping (String) -> Void, failure: @escaping (String) -> Void) {
        guard let extPub = mainExtPublicKey else {
            failure("主扩展公钥为空")
            return
        }
		lastAddressIndex += step
		lastBTCAddress(success: { (address) in
			BILNetworkManager.request(request: .refreshAddress(extendedKeyHash: extPub.md5(), index: self.lastAddressIndex), success: { (result) in
				debugPrint(result)
				do {
					try BILWalletManager.shared.saveWallets()
					success(address)
				} catch {
					self.lastAddressIndex -= step
					failure("新地址保存失败")
				}
			}, failure: { (msg, code) in
				debugPrint(msg)
                self.lastAddressIndex -= step
				failure(msg)
			})
		}) { (msg) in
			self.lastAddressIndex -= step
			failure(msg)
		}
	}
	
    var btc_transactionArray: [BTCTransactionModel] {
        get {
            return self.btcTransactions?.array as! [BTCTransactionModel]
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
    var btc_cnyString: String {
        get {
            return "0.00"
        }
    }
}
