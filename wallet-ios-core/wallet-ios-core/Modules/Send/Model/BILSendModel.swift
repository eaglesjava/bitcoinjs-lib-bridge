//
//  BILSendModel.swift
//  wallet-ios-core
//
//  Created by 仇弘扬 on 2017/12/12.
//  Copyright © 2017年 BitBill. All rights reserved.
//

import UIKit

class BILSendModel: BILRecieveModel {
    
    var wallet: WalletModel?
    
    var bitcoinSatoshiAmount: Int {
        get {
            guard let amount = Double(self.amount) else { return 0 }
            let satoshi = Int(amount * Double(BTC_SATOSH))
            return satoshi
        }
    }
}
