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
	
	var recnetRecords = [BTCTransactionModel]()
	
    private var observationAddresses = [String]()
    
    func addObserveAddress(address: String) {
        if observationAddresses.contains(address) {
            return
        }
        observationAddresses.append(address)
    }
    
    func removeObserveAddress(address: String) {
        guard let index = observationAddresses.index(of: address) else { return }
        observationAddresses.remove(at: index)
    }
    
    func addressWillAdd(address: String, in transaction: BTCTransactionModel) {
        guard let _ = observationAddresses.index(of: address) else { return }
        NotificationCenter.default.post(name: .transactionReceived, object: transaction)
        removeObserveAddress(address: address)
    }
    
}
