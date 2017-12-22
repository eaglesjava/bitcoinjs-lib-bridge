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
    var isContactAddress = false
    
    var transaction: BTCTransaction?
    
    var bitcoinAmount: String {
        get {
            guard let amount = Double(self.amount) else {
                return "0.00"
            }
            return String(amount)
        }
    }
    
    var bitcoinSatoshiAmount: Int64 {
        get {
            guard let amount = Double(self.bitcoinAmount) else { return 0 }
            let satoshi = Int64(amount * Double(BTC_SATOSHI))
            return satoshi
        }
    }
}
