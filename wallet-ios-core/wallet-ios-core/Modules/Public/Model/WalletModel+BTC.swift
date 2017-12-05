//
//  WalletModel+BTC.swift
//  wallet-ios-core
//
//  Created by 仇弘扬 on 2017/12/5.
//  Copyright © 2017年 BitBill. All rights reserved.
//

import Foundation

let BTC_SATOSH = 100000000

extension WalletModel {
    var btc_balanceString: String {
        get {
            return String(format: "%.6f", Double(btcBalance) / Double(BTC_SATOSH))
        }
    }
    var btc_unconfirm_balanceString: String {
        get {
            return String(format: "%.6f", Double(btcUnconfirmBalance) / Double(BTC_SATOSH))
        }
    }
}
