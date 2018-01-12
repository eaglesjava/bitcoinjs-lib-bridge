//
//  UIView+Language.swift
//  wallet-ios-core
//
//  Created by 仇弘扬 on 2018/1/12.
//  Copyright © 2018年 BitBill. All rights reserved.
//

import UIKit

@objc
protocol LanguageChanged {
    @objc optional func languageDidChanged()
}

extension UIView: LanguageChanged {
    func languageDidChanged() {
        for view in subviews {
            view.languageDidChanged()
        }
    }
}
