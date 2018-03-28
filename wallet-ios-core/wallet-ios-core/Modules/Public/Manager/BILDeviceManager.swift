//
//  BILDeviceManager.swift
//  wallet-ios-core
//
//  Created by 仇弘扬 on 2017/12/4.
//  Copyright © 2017年 BitBill. All rights reserved.
//

import UIKit

class BILDeviceManager: NSObject {
    
    var deviceID: String
    
    lazy var contactKey: String = {
        return String(deviceID.md5().prefix(10))
    }()
    
    override init() {
        let key = "bitbill.deviceID"
        if let uuid = UserDefaults.standard.string(forKey: key) {
            deviceID = uuid
        } else {
            deviceID = UUID().uuidString
            UserDefaults.standard.setValue(deviceID, forKey: key)
        }
        
    }
    
    static let shared = {
        return BILDeviceManager()
    }()
    
    lazy var appVersion: String = {
        if let version = Bundle.main.object(forInfoDictionaryKey:"CFBundleShortVersionString") as? String {
            return version
        }
        return ""
    }()
}
