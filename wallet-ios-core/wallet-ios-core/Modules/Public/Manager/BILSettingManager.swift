//
//  BILSettingManager.swift
//  wallet-ios-core
//
//  Created by 仇弘扬 on 2017/12/27.
//  Copyright © 2017年 BitBill. All rights reserved.
//

import UIKit

extension String {
    static let bil_UserDefaultsKey_isHomeShortcutEnabled = "bil_isHomeShortcutEnabled"
}

class BILSettingManager: NSObject {
    static var isHomeShortcutEnabled: Bool {
        set {
            UserDefaults.standard.set(newValue, forKey: .bil_UserDefaultsKey_isHomeShortcutEnabled)
        }
        get {
            if UserDefaults.standard.object(forKey: .bil_UserDefaultsKey_isHomeShortcutEnabled) == nil
            {
                self.isHomeShortcutEnabled = true
            }
            return UserDefaults.standard.bool(forKey: .bil_UserDefaultsKey_isHomeShortcutEnabled)
        }
    }
}
