//
//  BILPopMenuItem.swift
//  wallet-ios-core
//
//  Created by 仇弘扬 on 2017/11/29.
//  Copyright © 2017年 BitBill. All rights reserved.
//

import UIKit

class BILPopMenuItem: NSObject {
    typealias BILPopMenuItemTappedClosure = () -> Void
    var title = ""
    var imageName = ""
    var tappedClosure: BILPopMenuItemTappedClosure?
    init(title: String, imageName: String = "", tapped: @escaping BILPopMenuItemTappedClosure) {
        self.title = title
        self.imageName = imageName
        tappedClosure = tapped
    }
}
