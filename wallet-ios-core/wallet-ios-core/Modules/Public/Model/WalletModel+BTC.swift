//
//  WalletModel+BTC.swift
//  wallet-ios-core
//
//  Created by 仇弘扬 on 2017/12/5.
//  Copyright © 2017年 BitBill. All rights reserved.
//

import Foundation

let BTC_SATOSHI = 100000000

func BTCFormatString(btc: Int64) -> String {
    return String(format: "%.6f", Double(btc) / Double(BTC_SATOSHI))
}

extension WalletModel {
    func lastBTCAddress(success: @escaping (String) -> Void, failure: @escaping (String) -> Void) {
        guard let xpub = mainExtPublicKey else {
            failure("主扩展公钥为空")
            return
        }
        let index = lastAddressIndex
        BitcoinJSBridge.shared.getAddress(xpub: xpub, index: Int(index), success: { (address) in
            if address is String {
                success(address as! String)
            } else {
                failure("获取地址失败")
            }
        }) { (error) in
            failure(error.localizedDescription)
        }
    }
	
	func getNewBTCAddress(success: @escaping (String) -> Void, failure: @escaping (String) -> Void) {
        guard let extPub = mainExtPublicKey else {
            failure("主扩展公钥为空")
            return
        }
		lastAddressIndex += 1
		lastBTCAddress(success: { (address) in
			do {
				try BILWalletManager.shared.saveWallets()
                success(address)
                BILNetworkManager.request(request: .refreshAddress(extendedKeyHash: extPub.md5(), index: self.lastAddressIndex), success: { (result) in
                    debugPrint(result)
                }, failure: { (msg, code) in
                    debugPrint(msg)
                })
			} catch {
				self.lastAddressIndex -= 1
				failure("新地址保存失败")
			}
		}) { (msg) in
			self.lastAddressIndex -= 1
			failure(msg)
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
