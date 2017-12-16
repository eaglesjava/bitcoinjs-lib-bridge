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
}
