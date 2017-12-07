//
//  WalletModel+BTC.swift
//  wallet-ios-core
//
//  Created by 仇弘扬 on 2017/12/5.
//  Copyright © 2017年 BitBill. All rights reserved.
//

import Foundation

let BTC_SATOSH = 100000000

func BTCFormatString(btc: Int) -> String {
    return String(format: "%.6f", Double(btc) / Double(BTC_SATOSH))
}

extension WalletModel {
    var btc_balanceString: String {
        get {
            return BTCFormatString(btc: Int(btcBalance))
        }
    }
    var btc_unconfirm_balanceString: String {
        get {
            return BTCFormatString(btc: Int(btcUnconfirmBalance))
        }
    }
    var btc_cnyString: String {
        get {
            return "0.00"
        }
    }
}
