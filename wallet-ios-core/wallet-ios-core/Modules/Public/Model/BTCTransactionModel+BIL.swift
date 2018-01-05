//
//  BTCTransactionModel+BIL.swift
//  wallet-ios-core
//
//  Created by 仇弘扬 on 2017/12/29.
//  Copyright © 2017年 BitBill. All rights reserved.
//

import Foundation
import SwiftyJSON
import Timepiece

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
            str = "icon_record_transfer"
        }
        return UIImage(named: str)
    }
}

enum BILTransactionStatus {
    case unconfirm
    case success
    case failure
    
    var image: UIImage? {
        var str = ""
        switch self {
        case .unconfirm:
            str = "pic_transaction_unconfirm"
        case .success:
            str = "pic_transaction_success"
        case .failure:
            str = "pic_transaction_failed"
        }
        return UIImage(named: str)
    }
    
    var description: String {
        var str = ""
        switch self {
        case .unconfirm:
            str = .publicTransactionUnconfirm
        case .success:
            str = .publicTransactionSuccess
        case .failure:
            str = .publicTransactionFailure
        }
        return str
    }
}

extension BTCTransactionModel {
    
    var confirmCount: Int64 {
        get {
            return Int64(BILWalletManager.shared.btcBlockHeight) - height + 1
        }
    }
    
    var feeSatoshi: Int64 { get { return inSatoshi - outSatoshi } }
    
    var type: BILTransactionType {
        get { return BILTransactionType(rawValue: typeRawValue) ?? .recieve }
        set { typeRawValue = newValue.rawValue }
    }
    
    var status: BILTransactionStatus {
        get { return height > 0 ? .success : .unconfirm }
    }
    
    var dateSring: String {
        get {
            guard let d: Date = createdDate else { return "" }
            return d.stringIn(dateStyle: .medium, timeStyle: .medium)
        }
    }
    
    var volumeString: String {
        get {
            var symbol = ""
            switch type {
            case .recieve:
                symbol = "+"
            case .send:
                symbol = "-"
            default:
                ()
            }
            return String(format: "\(symbol)\(BTCFormatString(btc: Int64(targetSatoshi))) BTC")
        }
    }
    
    var remarkString: String {
        get { return remark!.isEmpty ? "无" : remark! }
    }
    
    var confirmString: String {
        get { return height == -1 ? .publicTransactionUnconfirm : ((confirmCount > 1000 ? "1000+" : "\(confirmCount) ") + .publicTransactionConfirmed) }
    }
	
	var inputAddressModels: [BTCTXAddressModel] {
		get { return inputs?.array as! [BTCTXAddressModel] }
	}
	
	var outputAddressModels: [BTCTXAddressModel] {
		get { return outputs?.array as! [BTCTXAddressModel] }
	}
    
    var firstTargetAddress: BTCTXAddressModel? {
        get {
            guard let model = targets?.firstObject as? BTCTXAddressModel else {
                return nil
            }
            return model
        }
    }
    
    var inputAddresses: [String] {
        get {
            return getAddressStringArray(from: inputs!)
        }
    }
    
    var outputAddresses: [String] {
        get {
            return getAddressStringArray(from: outputs!)
        }
    }
    
    var targetAddresses: [String] {
        get {
            return getAddressStringArray(from: targets!)
        }
    }
    
    func getAddressStringArray(from set: NSOrderedSet) -> [String] {
        var toReturn = [String]()
        
        for addModel in set {
            let add = addModel as! BTCTXAddressModel
            toReturn.append(add.address! + "\t" + BTCFormatString(btc: add.satoshi) + " BTC")
        }
        
        return toReturn
    }
    
    func clearSatoshi() {
        inSatoshi = 0
        outSatoshi = 0
        targetSatoshi = 0
    }
    
    func newAddressModelIfNeeded(_ address: String, isInput: Bool = true) -> BTCTXAddressModel {
        let array = (isInput ? inputs : outputs)!.array
        for add in array {
            let tx = add as! BTCTXAddressModel
            if tx.address == address {
                return tx
            }
        }
        let tx = bil_btc_tx_addressManager.newModel()
        return tx
    }
    
    func setProperties(json: JSON) {
        clearSatoshi()
        height = json["height"].int64Value
        serverID = json["id"].int64Value
        var inAddresses = [String]()
        for j in json["inputs"].arrayValue {
            let add = j["address"].stringValue
            let tx = newAddressModelIfNeeded(add)
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
            let tx = newAddressModelIfNeeded(add, isInput: false)
            tx.address = add
            tx.satoshi = j["value"].int64Value
            outAddresses.append(add)
            outSatoshi += tx.satoshi
            self.addToOutputs(tx)
        }
        remark = json["remark"].stringValue
        txHash = json["txHash"].stringValue
        createdDate = Date(dateString: json["createdTime"].stringValue, format: "yyyy-MM-dd HH:mm:ss")
        
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
            do {
                try bil_btc_transactionManager.remove(model: self)
            } catch {
                debugPrint(error.localizedDescription)
            }
            return
        }
        
        for add in outputs! {
            let tx = add as! BTCTXAddressModel
            switch type {
            case .send:
                if !w.contain(btcAddress: tx.address!) {
                    self.addToTargets(tx)
                    targetSatoshi += tx.satoshi
                }
            case .transfer: fallthrough
            case .recieve:
                if w.contain(btcAddress: tx.address!) {
                    self.addToTargets(tx)
                    targetSatoshi += tx.satoshi
                }
            }
        }
        
        w.addToBtcTransactions(self)
    }
}
