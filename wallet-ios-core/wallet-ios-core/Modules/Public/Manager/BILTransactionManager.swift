//
//  BILTransactionManager.swift
//  wallet-ios-core
//
//  Created by 仇弘扬 on 2017/11/27.
//  Copyright © 2017年 BitBill. All rights reserved.
//

import UIKit

class BILTransactionManager: NSObject {
	static let shared = {
		return BILTransactionManager()
	}()
	
	var recnetRecords: [BILTransactionHistoryModel] {
		let tt = BILTransactionHistoryModel()
		tt.address = "123456jWebYP9tanj2taQfNvRVMNGjCYAd"
		tt.date = Date()
		tt.volume = 10000
		return [BILTransactionHistoryModel](repeating: tt, count: 2)
	}
	
}
