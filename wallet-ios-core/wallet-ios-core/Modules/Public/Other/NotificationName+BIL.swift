//
//  NotificationName+BIL.swift
//  wallet-ios-core
//
//  Created by 仇弘扬 on 2017/11/28.
//  Copyright © 2017年 BitBill. All rights reserved.
//

import Foundation

extension Notification.Name {
	static var walletDidChanged: Notification.Name { get { return NSNotification.Name("walletDidChanged") } }
    static var walletCountDidChanged: Notification.Name { get { return NSNotification.Name("walletCountDidChanged") } }
    static var balanceDidChanged: Notification.Name { get { return NSNotification.Name("balanceDidChanged") } }
    static var transactionSended: Notification.Name { get { return NSNotification.Name("transactionSended") } }
    static var recievedUnconfirmTransaction: Notification.Name { get { return NSNotification.Name("recievedUnconfirmTransaction") } }
    static var unconfirmTransactionBeenConfirmed: Notification.Name { get { return NSNotification.Name("unconfirmTransactionBeenConfirmed") } }
    static var recievePageCurrentWallet: Notification.Name { get { return NSNotification.Name("recievePageCurrentWallet") } }
    static var sendBTCToContact: Notification.Name { get { return NSNotification.Name("sendBTCToContact") } }
    static var contactDidChanged: Notification.Name { get { return NSNotification.Name("contactDidChanged") } }
    static var networkStatusDidChanged: Notification.Name { get { return NSNotification.Name("networkStatusDidChanged") } }
	static var shortcutScanQRCode: Notification.Name { get { return NSNotification.Name("shortcutScanQRCode") } }
	static var shortcutAddContact: Notification.Name { get { return NSNotification.Name("shortcutAddContact") } }
}
