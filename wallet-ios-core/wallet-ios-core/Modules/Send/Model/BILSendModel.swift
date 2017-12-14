//
//  BILSendModel.swift
//  wallet-ios-core
//
//  Created by 仇弘扬 on 2017/12/12.
//  Copyright © 2017年 BitBill. All rights reserved.
//

import UIKit
import SwiftyJSON

class BILSendModel: BILRecieveModel {
    
    var wallet: WalletModel?
    
    var isSendAll = false
    
    var bitcoinAmount: String {
        get {
            guard let amount = Double(self.amount) else {
                return "0.00"
            }
            guard let w = wallet else {
                return "0.00"
            }
            return isSendAll ? (w.btc_balanceString) : String(amount)
        }
    }
    
    var bitcoinSatoshiAmount: Int {
        get {
            guard let amount = Double(self.amount) else { return 0 }
            let satoshi = Int(amount * Double(BTC_SATOSHI))
            return satoshi
        }
    }
    
    func sendTransactionr(success: @escaping ([String: JSON]) -> Void, failure: @escaping (_ message: String, _ code: Int) -> Void) {
        
    }
}
