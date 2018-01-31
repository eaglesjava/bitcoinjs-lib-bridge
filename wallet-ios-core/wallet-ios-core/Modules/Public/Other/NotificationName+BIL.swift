//
//  NotificationName+BIL.swift
//  wallet-ios-core
//
//  Created by 仇弘扬 on 2017/11/28.
//  Copyright © 2017年 BitBill. All rights reserved.
//

import Foundation

extension Notification.Name {
	static let walletDidChanged = NSNotification.Name("walletDidChanged")
    static let walletCountDidChanged = NSNotification.Name("walletCountDidChanged")
    static let balanceDidChanged = NSNotification.Name("balanceDidChanged")
	static let languageDidChanged = NSNotification.Name("languageDidChanged")
    static let transactionSended = NSNotification.Name("transactionSended")
    static let transactionDidChanged = NSNotification.Name("transactionDidChanged")
    static let localUTXODidChanged = NSNotification.Name("localUTXODidChanged")
    static let receivedUnconfirmTransaction = NSNotification.Name("receivedUnconfirmTransaction")
    static let unconfirmTransactionBeenConfirmed = NSNotification.Name("unconfirmTransactionBeenConfirmed")
    static let receivePageCurrentWallet = NSNotification.Name("receivePageCurrentWallet")
    static let sendBTCToContact = NSNotification.Name("sendBTCToContact")
    static let contactDidChanged = NSNotification.Name("contactDidChanged")
    static let networkStatusDidChanged = NSNotification.Name("networkStatusDidChanged")
	static let shortcutScanQRCode = NSNotification.Name("shortcutScanQRCode")
	static let shortcutAddContact = NSNotification.Name("shortcutAddContact")
    static let transactionReceived = NSNotification.Name("transactionReceived")
    static let exchangeRateDidChanged = NSNotification.Name("exchangeRateDidChanged")
}
