//
//  NSDecimalNumber+BIL.swift
//  wallet-ios-core
//
//  Created by 仇弘扬 on 2018/2/3.
//  Copyright © 2018年 BitBill. All rights reserved.
//

import Foundation

extension NSDecimalNumber {
	static let bitcoinSatoshiNumber = NSDecimalNumber(value: BTC_SATOSHI)
	static func convertBTCSatoshi(satoshi: Int64) -> Double {
		return convertBTCSatoshi(satoshi: satoshi).doubleValue
	}
	static func convertBTCSatoshi(satoshi: Int64) -> NSDecimalNumber {
		return NSDecimalNumber(value: satoshi).dividing(by: .bitcoinSatoshiNumber)
	}
	static func convertBTCAmount(amount: Double) -> Int64 {
		return NSDecimalNumber(value: amount).dividing(by: .bitcoinSatoshiNumber).int64Value
	}
}
