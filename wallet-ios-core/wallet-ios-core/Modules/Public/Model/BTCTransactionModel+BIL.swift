//
//  BTCTransactionModel+BIL.swift
//  wallet-ios-core
//
//  Created by 仇弘扬 on 2017/12/29.
//  Copyright © 2017年 BitBill. All rights reserved.
//

import Foundation
import SwiftyJSON

enum BILTransactionType: Int16 {
    case recieve = 0
    case send
    case transfer
    
    var image: UIImage? {
        var str = ""
        switch self {
        case .recieve:
            str = "icon_record_recieve"
        case .send:
            str = "icon_record_send"
        case .transfer:
            str = "icon_home_h"
        }
        return UIImage(named: str)
    }
}

extension BTCTransactionModel {
    
    var confirmCount: Int64 {
        get {
            return height
        }
    }
    
    var feeSatoshi: Int64 { get { return inSatoshi - outSatoshi } }
    
    var type: BILTransactionType {
        get { return BILTransactionType(rawValue: typeRawValue) ?? .recieve }
        set { typeRawValue = newValue.rawValue }
    }
    
    var dateSring: String {
        get {
            guard let d: Date = createdDate else { return "" }
            return d.stringIn(dateStyle: .medium, timeStyle: .medium)
        }
    }
    
    var volumeString: String {
        get {
            return String(format: "\(type == .recieve ? "+" : "-")\(BTCFormatString(btc: Int64(targetSatoshi))) BTC")
        }
    }
    
    var firstTargetAddress: BTCAddressModel? {
        get {
            guard let model = targets?.firstObject as? BTCAddressModel else {
                return nil
            }
            return model
        }
    }
    
    func setProperties(json: JSON) {
        height = json["height"].int64Value
        var inAddresses = [String]()
        for j in json["inputs"].arrayValue {
            let add = j["address"].stringValue
            let tx = bil_btc_addressManager.newModelIfNeeded(key: "address", value: add)
            tx.address = add
            tx.satoshi = j["value"].int64Value
            inAddresses.append(add)
            inSatoshi += tx.satoshi
            self.addToInputs(tx)
        }
        var outAddresses = [String]()
        for j in json["outputs"].arrayValue {
            debugPrint(j["address"])
            let add = j["address"].stringValue
            let tx = bil_btc_addressManager.newModelIfNeeded(key: "address", value: add)
            tx.address = add
            tx.satoshi = j["value"].int64Value
            outAddresses.append(add)
            outSatoshi += tx.satoshi
            self.addToOutputs(tx)
        }
        remark = json["remark"].stringValue
        txHash = json["txHash"].stringValue
        createdDate = Date(dateString: json["createdTime"].stringValue, format: "yyyy:MM:dd HH:mm:ss")
        
        var wallet: WalletModel?
        if let inw = WalletModel.fetch(by: inAddresses, isAll: false) {
            if let outAllw = WalletModel.fetch(by: outAddresses, isAll: true) {
                typeRawValue = BILTransactionType.transfer.rawValue
                wallet = outAllw
            }
            else
            {
                wallet = inw
                typeRawValue = BILTransactionType.send.rawValue
            }
        }
        else
        {
            if let outw = WalletModel.fetch(by: outAddresses, isAll: false) {
                wallet = outw
                typeRawValue = BILTransactionType.recieve.rawValue
            }
        }
        
        guard let w = wallet else {
            return
        }
        
        for add in outAddresses {
            let tx = bil_btc_addressManager.newModelIfNeeded(key: "address", value: add)
            switch type {
            case .send:
                if !w.contain(btcAddress: add) {
                    self.addToTargets(tx)
                    targetSatoshi += tx.satoshi
                }
            case .transfer: fallthrough
            case .recieve:
                if w.contain(btcAddress: add) {
                    self.addToTargets(tx)
                    targetSatoshi += tx.satoshi
                }
            }
        }
        
        w.addToBtcTransactions(self)
    }
}
