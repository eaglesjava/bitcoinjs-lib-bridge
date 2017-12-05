//
//  WalletModel+BTC.swift
//  wallet-ios-core
//
//  Created by 仇弘扬 on 2017/12/5.
//  Copyright © 2017年 BitBill. All rights reserved.
//

import Foundation

extension WalletModel {
    var btc_balanceString: String {
        get {
            return String(format: "%.6f", Double(btcBalance) / 100000000.0)
        }
    }
    var btc_unconfirm_balanceString: String {
        get {
            return String(format: "%.6f", Double(btcUnconfirmBalance) / 100000000.0)
        }
    }
}
