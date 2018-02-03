//
//  BILReceiveModel.swift
//  wallet-ios-core
//
//  Created by 仇弘扬 on 2017/12/8.
//  Copyright © 2017年 BitBill. All rights reserved.
//

import UIKit

enum CoinType: Int16 {
    case btc = 0
    
    var name: String {
        get {
            switch self {
            case .btc:
                return "BTC"
            }
        }
    }
    
    var scheme: String {
        get {
            switch self {
            case .btc:
                return "bitcoin"
            }
        }
    }
}

class BILReceiveModel: NSObject {
	
	var address: String
	var amount: String
	var coinType: CoinType
	
	init(address: String = "", amount: String = "0", coinType: CoinType = .btc) {
		self.address = address
		self.amount = amount
		self.coinType = coinType
	}
	
	var urlString: String {
		get {
			return "\(coinType.scheme):\(address)" + (amount.isEmpty ? "" : "?amount=\(amount)")
		}
	}
	
	var bitcoinAmount: String {
		get {
			let amount = NSDecimalNumber(string: self.amount)
			
			return BTCFormatString(btc: amount.multiplying(by: .bitcoinSatoshiNumber).int64Value)
		}
	}
	
	var bitcoinSatoshiAmount: Int64 {
		get {
			let amount = NSDecimalNumber(string: self.bitcoinAmount)
			let satoshi = amount.multiplying(by: .bitcoinSatoshiNumber).int64Value
			return satoshi
		}
	}
}
