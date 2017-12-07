//
//  BILTransaction.swift
//  wallet-ios-core
//
//  Created by 仇弘扬 on 2017/11/27.
//  Copyright © 2017年 BitBill. All rights reserved.
//

import UIKit
import Foundation
import Timepiece

class BILTransaction: NSObject {
	
	enum BILTransactionType {
		case recieve
		case send
	}
	
	var address: String?
	var type = BILTransactionType.recieve
	var volume = 0
	lazy var volumeString: String = {
		return String(format: "\(type == .recieve ? "+" : "-")\(BTCFormatString(btc: volume)) btc")
	}()
	
	var date: Date?
	lazy var dateSring: String = {
//        guard let d = self.date else { return "" }
//        let formatter = DateFormatter()
//        formatter.dateFormat = "MM.dd.yyyy"
//        return formatter.string(from: d)
        guard let d: Date = self.date else { return "" }
        
        return d.stringIn(dateStyle: .medium, timeStyle: .medium)
	}()
}
