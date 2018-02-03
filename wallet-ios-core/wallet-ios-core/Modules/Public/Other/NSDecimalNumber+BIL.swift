//
//  Decimal+BIL.swift
//  wallet-ios-core
//
//  Created by 仇弘扬 on 2018/2/3.
//  Copyright © 2018年 BitBill. All rights reserved.
//

import Foundation

extension Decimal {
	static let bitcoinSatoshiNumber = Decimal(BTC_SATOSHI)
	static func convertBTCSatoshi(satoshi: Int64) -> Decimal {
		return Decimal(satoshi) / .bitcoinSatoshiNumber
	}
	var doubleValue: Double {
		return (self as NSDecimalNumber).doubleValue
	}
}
