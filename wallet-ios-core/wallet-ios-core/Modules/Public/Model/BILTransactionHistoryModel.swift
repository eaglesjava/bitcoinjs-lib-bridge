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
import SwiftyJSON
import DateToolsSwift

class BILTransactionHistoryModel: NSObject {
	
    enum BILTransactionType {
		case recieve
		case send
	}
	
    var inAddress: [String]
    var outAddress: [String]
	var type = BILTransactionType.recieve
	var volume = 0
    var txHash: String
    lazy var address: String = {
        guard let add = type == .recieve ? inAddress.first : outAddress.first else {
            return ""
        }
        return add
    }()
	lazy var volumeString: String = {
		return String(format: "\(type == .recieve ? "+" : "-")\(BTCFormatString(btc: Int64(volume))) BTC")
	}()
	
	var date: Date?
	lazy var dateSring: String = {
        guard let d: Date = self.date else { return "" }
        
        return d.stringIn(dateStyle: .medium, timeStyle: .medium)
	}()
    
    override init() {
        self.inAddress = [String]()
        self.outAddress = [String]()
        txHash = ""
        super.init()
    }
    
    init(jsonData: JSON) {
        self.inAddress = jsonData["gatherAddressIn"].stringValue.components(separatedBy: "|")
        self.outAddress = jsonData["gatherAddressOut"].stringValue.components(separatedBy: "|")
        type = jsonData["inOut"].intValue == 1 ? .send : .recieve
        volume = jsonData["amount"].intValue
        date = Date(dateString: jsonData["createdTime"].stringValue, format: "yyyy-MM-dd hh:mm:ss")
        self.txHash = jsonData["txHash"].stringValue
    }
    
}
