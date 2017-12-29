//
//  BTCTransactionHandler.swift
//  wallet-ios-core
//
//  Created by 仇弘扬 on 2017/12/29.
//  Copyright © 2017年 BitBill. All rights reserved.
//

import UIKit
import SwiftyJSON

class BTCTransactionHandler: NSObject {
    static func handleBTCTransactionData(json: JSON, wallet: WalletModel? = nil) {
        let model = bil_btc_transactionManager.newModelIfNeeded(key: "txHash", value: json["txHash"].stringValue)
        var inAddresses = [String]()
        for j in json["inputs"].arrayValue {
            let add = j["address"].stringValue
            inAddresses.append(add)
        }
        var outAddresses = [String]()
        for j in json["outputs"].arrayValue {
            let add = j["address"].stringValue
            outAddresses.append(add)
        }
        
        if let inw = WalletModel.fetch(by: inAddresses, isAll: false) {
            inw.addToBtcTransactions(model)
            model.setProperties(json: json)
            if let outAllW = WalletModel.fetch(by: outAddresses, isAll: true) {
                var newModel = bil_btc_transactionManager.fetch(keyValues: (key: "txHash", value: json["txHash"].stringValue), ("typeRawValue", "1"))
                if newModel == nil {
                    newModel = bil_btc_transactionManager.newModel()
                }
                newModel = bil_btc_transactionManager.newModelIfNeeded(key: "txHash", value: json["txHash"].stringValue)
                outAllW.addToBtcTransactions(newModel!)
                newModel!.setProperties(json: json)
            }
        }
        else
        {
            if let outAllW = WalletModel.fetch(by: outAddresses, isAll: false) {
                outAllW.addToBtcTransactions(model)
                model.setProperties(json: json)
            }
        }
    }
}
