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
    static var transactionSended: Notification.Name { get { return NSNotification.Name("transactionSended") } }
}
