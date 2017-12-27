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
				failure(msg)
			})
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
