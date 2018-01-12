//
//  ContactModel+BIL.swift
//  wallet-ios-core
//
//  Created by 仇弘扬 on 2017/12/27.
//  Copyright © 2017年 BitBill. All rights reserved.
//

import Foundation

extension ContactModel {
    static func isWalletIDExits(walletID: String) -> Bool {
        guard !walletID.isEmpty else {
            return false
        }
        return bil_contactManager.fetch(key: "walletID", value: walletID) != nil
    }
    static func isAddressExits(address: String) -> Bool {
        guard !address.isEmpty else {
            return false
        }
        return bil_contactManager.fetch(key: "address", value: address) != nil
    }
}
