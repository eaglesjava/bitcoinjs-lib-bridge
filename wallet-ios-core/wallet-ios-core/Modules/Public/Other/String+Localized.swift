//
//  String+Localized.swift
//  wallet-ios-core
//
//  Created by 仇弘扬 on 2018/1/5.
//  Copyright © 2018年 BitBill. All rights reserved.
//

import Foundation

extension String {
    static let publicCurrencyCNYName = "Public.currency.name.cny".bil_localized
    static let publicCurrencyUSDName = "Public.currency.name.usd".bil_localized
    static let publicKeyboardHide = "Public.keyboard.hide".bil_localized
    static let publicNetworkConnectServerFaild = "Public.network.connect.sever.failed".bil_localized
    static let publicWalletFetchError = "Public.wallet.fetch.failed".bil_localized
    static let publicWalletExtKeyError = "Public.wallet.error.extKey".bil_localized
    static let publicWalletIDError = "Public.wallet.error.id".bil_localized
    static let publicWalletDataError = "Public.wallet.error.data".bil_localized
    static let publicWalletGetAddressError = "Public.wallet.error.address".bil_localized
    static let publicWalletNotRefresh = "Public.wallet.error.address.dont.need.refresh".bil_localized
    static let publicWalletNoMoreAddress = "Public.wallet.error.address.nomore".bil_localized
    static let publicWalletGenerateAddressError = "Public.wallet.error.address.generate".bil_localized
    static let publicWalletIndexError = "Public.wallet.error.address.index".bil_localized
    static let publicTransactionUnconfirm = "Public.transaction.status.unconfirm".bil_localized
    static let publicTransactionSuccess = "Public.transaction.status.success".bil_localized
    static let publicTransactionFailure = "Public.transaction.status.failure".bil_localized
    static let publicTransactionConfirmed = "Public.transaction.status.confirmed".bil_localized
    static let publicTransactionNoRemark = "Public.transaction.remark.none".bil_localized
    static let publicAlertActionTitle = "Public.alert.action.title".bil_localized
//    static let publicCurrencyCnyName = "Public.transaction.status.failure".bil_localized
//    static let publicCurrencyCnyName = "Public.transaction.status.failure".bil_localized
//    static let publicCurrencyCnyName = "Public.transaction.status.failure".bil_localized
}

extension String {
    var bil_localized: String {
        get {
            guard let tableName = components(separatedBy: ".").first else {
                return self
            }
            
            return NSLocalizedString(self, tableName: tableName.capitalized, comment: "")
        }
    }
}
