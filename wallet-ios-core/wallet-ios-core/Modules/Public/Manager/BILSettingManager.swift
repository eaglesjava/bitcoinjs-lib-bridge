//
//  BILSettingManager.swift
//  wallet-ios-core
//
//  Created by 仇弘扬 on 2017/12/27.
//  Copyright © 2017年 BitBill. All rights reserved.
//

import UIKit

extension String {
    static let bil_UserDefaultsKey_isHomeShortcutEnabled    = "bil_isHomeShortcutEnabled"
    static let bil_UserDefaultsKey_isSoundEnabled           = "bil_isSoundEnabled"
    static let bil_UserDefaultsKey_currencyType             = "bil_currencyType"
}

enum CurrencyType: Int {
    case cny
    case usd
    
    var symbol: String {
        get {
            switch self {
            case .cny:
                return "¥"
            case .usd:
                return "$"
            }
        }
    }
    
    var symbolName: String {
        get {
            switch self {
            case .cny:
                return "CNY"
            case .usd:
                return "USD"
            }
        }
    }
    
    var rate: Double {
        get {
            switch self {
            case .cny:
                return BILWalletManager.shared.cnyExchangeRate
            case .usd:
                return BILWalletManager.shared.usdExchangeRate
            }
        }
    }
    
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
    
    static var isSoundEnabled: Bool {
        set {
            UserDefaults.standard.set(newValue, forKey: .bil_UserDefaultsKey_isSoundEnabled)
        }
        get {
            if UserDefaults.standard.object(forKey: .bil_UserDefaultsKey_isSoundEnabled) == nil
            {
                self.isSoundEnabled = true
            }
            return UserDefaults.standard.bool(forKey: .bil_UserDefaultsKey_isSoundEnabled)
        }
    }
    
    static var currencyType: CurrencyType {
        set {
            UserDefaults.standard.set(newValue.rawValue, forKey: .bil_UserDefaultsKey_currencyType)
        }
        get {
            if UserDefaults.standard.object(forKey: .bil_UserDefaultsKey_currencyType) == nil
            {
                self.currencyType = .usd
            }
            return CurrencyType(rawValue: UserDefaults.standard.integer(forKey: .bil_UserDefaultsKey_currencyType)) ?? .usd
        }
    }
}
