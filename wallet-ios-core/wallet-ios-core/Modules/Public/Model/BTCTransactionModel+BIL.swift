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
    case receive = 0
    case send
    case transfer
    
    var image: UIImage? {
        var str = ""
        switch self {
        case .receive:
            str = "icon_record_receive"
        case .send:
            str = "icon_record_send"
        case .transfer:
            str = "icon_record_transfer"
        }
        return UIImage(named: str)
    }
    
    var description: String {
        var str = ""
        switch self {
        case .receive:
            str = .publicTransactionTypeReceive
        case .send:
            str = .publicTransactionTypeSend
        case .transfer:
            str = .publicTransactionTypeTransfer
        }
        return str
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
        get { return BILTransactionType(rawValue: typeRawValue) ?? .receive }
        set { typeRawValue = newValue.rawValue }
    }
    
    var status: BILTransactionStatus {
        get { return height > 0 ? .success : .unconfirm }
    }
    
    var dateSring: String {
        get {
            guard let d: Date = createdDate else { return "" }
            let dateFormatter = DateFormatter()
            dateFormatter.dateStyle = .medium
            dateFormatter.timeStyle = .medium
            dateFormatter.locale = Locale(identifier: BILSettingManager.currentLanguage.rawValue)
            return dateFormatter.string(from: d)
        }
    }
    
    var fee: Int64 {
        return inSatoshi - outSatoshi
    }
    
    var feeString: String {
        return BTCFormatString(btc: fee)
    }
    
    var volumeString: String {
        get {
            var symbol = ""
            switch type {
            case .receive:
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
        get { return remark!.isEmpty ? .publicTransactionNoRemark : remark! }
    }
    
    var typeString: String {
        get { return type.description }
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
    
    func newAddressModelIfNeeded(_ address: String, index: Int, isInput: Bool = true) -> BTCTXAddressModel {
        let array = (isInput ? inputs : outputs)!.array
        for add in array {
            let tx = add as! BTCTXAddressModel
            if tx.address == address && tx.index == Int64(index) {
                return tx
            }
        }
        let tx = bil_btc_tx_addressManager.newModel()
        tx.address = address
        tx.index = Int64(index)
		
		if let wAdd = bil_btc_wallet_addressManager.fetch(key: "address", value: address) {
			wAdd.isUsed = true
		}
		
        return tx
    }
    
    static func newTxIfNeeded(json: JSON, outWallet: WalletModel) -> BTCTransactionModel {
        let txs = bil_btc_transactionManager.fetchAll(keyValues: [(key: "txHash", value: json["txHash"].stringValue)])
        let filterd = txs.filter { $0.wallet?.wallet?.id == outWallet.id && ($0.type == .receive || $0.type == .transfer) }
        return filterd.first ?? bil_btc_transactionManager.newModel()
    }
    
    static func newTxIfNeeded(json: JSON, inWallet: WalletModel) -> BTCTransactionModel {
        let txs = bil_btc_transactionManager.fetchAll(keyValues: [(key: "txHash", value: json["txHash"].stringValue)])
        let filterd = txs.filter { $0.wallet?.wallet?.id == inWallet.id && ($0.type == .send || $0.type == .transfer) }
        return filterd.first ?? bil_btc_transactionManager.newModel()
    }
	
	static func newTxAfterDelete(txHash: String) -> BTCTransactionModel {
		let txs = bil_btc_transactionManager.fetchAll(keyValues: [(key: "txHash", value: txHash)])
		for tx in txs {
			tx.wallet?.removeFromTransactions(tx)
			do {
				try bil_btc_transactionManager.remove(model: tx)
			} catch {
				debugPrint(error.localizedDescription)
			}
		}
		return bil_btc_transactionManager.newModel()
	}
    
    static func handle(json: JSON) {
        let txHash = json["txHash"].stringValue
        var inSatoshi: Int64 = 0
        var outSatoshi: Int64 = 0
        var inAddresses = [String]()
        for j in json["inputs"].arrayValue {
            let add = j["address"].stringValue
            inAddresses.append(add)
            inSatoshi += j["value"].int64Value
        }
        var outAddresses = [String]()
        for j in json["outputs"].arrayValue {
            let add = j["address"].stringValue
            outAddresses.append(add)
            outSatoshi += j["value"].int64Value
        }
        
        let outWallets = WalletModel.fetchWallets(by: outAddresses)
        let inWallet = WalletModel.fetch(by: inAddresses, isAll: true)
        
        var type: BILTransactionType = .receive
        
        if inWallet != nil {
			
            if outWallets.count == 1, let oWallet = WalletModel.fetch(by: outAddresses, isAll: true), oWallet.id == inWallet?.id {
                type = .transfer
            }
            else
            {
                type = .send
            }
        }
        
        for oWallet in outWallets {
            if let inw = inWallet {
                guard inw.id != oWallet.id else {
                    continue
                }
            }
            let tx = newTxIfNeeded(json: json, outWallet: oWallet)
            tx.type = type == .transfer ? type : .receive
            let canAdd = oWallet.btc_transactionArray.filter({
                $0.txHash == txHash
            }).count == 0
            tx.setProperties(json: json, wallet: oWallet)
            if canAdd {
                oWallet.bitcoinWallet?.addToTransactions(tx)
            }
        }
        
        if let iWallet = inWallet {
            let tx = newTxIfNeeded(json: json, inWallet: iWallet)
            tx.type = type == .transfer ? type : .send
            let canAdd = iWallet.btc_transactionArray.filter({
                $0.txHash == txHash
            }).count == 0
            tx.setProperties(json: json, wallet: iWallet)
            if canAdd {
                iWallet.bitcoinWallet?.addToTransactions(tx)
            }
        }
    }
    
    func setProperties(json: JSON, wallet: WalletModel) {
		let beginDate = Date()
        clearSatoshi()
        height = json["height"].int64Value
        serverID = json["id"].int64Value
        txHash = json["txHash"].stringValue
        var inAddresses = [String]()
        for j in json["inputs"].arrayValue {
            let add = j["address"].stringValue
            let tx = newAddressModelIfNeeded(add, index: inAddresses.count)
            tx.txHash = txHash
            tx.satoshi = j["value"].int64Value
            inAddresses.append(add)
            inSatoshi += tx.satoshi
            self.addToInputs(tx)
        }
        var outAddresses = [String]()
        for j in json["outputs"].arrayValue {
            let add = j["address"].stringValue
            let tx = newAddressModelIfNeeded(add, index: outAddresses.count, isInput: false)
            tx.txHash = txHash
            tx.satoshi = j["value"].int64Value
            outAddresses.append(add)
            outSatoshi += tx.satoshi
            self.addToOutputs(tx)
        }
        remark = json["remark"].stringValue
        createdDate = Date(dateString: json["createdTime"].stringValue, format: "yyyy-MM-dd HH:mm:ss")
        
        let canAdd = wallet.btc_transactionArray.filter({
            $0.txHash == self.txHash
        }).count == 0
        
		debugPrint("--- 1 \(targetSatoshi)")
        for add in outputs! {
            let tx = add as! BTCTXAddressModel
			let isMine = wallet.contain(btcAddress: tx.address!)
            switch type {
            case .send:
                if !isMine {
                    self.addToTargets(tx)
                    targetSatoshi += tx.satoshi
					debugPrint("--- 2 \(targetSatoshi)")
                }
            case .transfer: fallthrough
            case .receive:
                if isMine {
                    self.addToTargets(tx)
                    targetSatoshi += tx.satoshi
					debugPrint("--- 3 \(targetSatoshi)")
                }
            }
            if canAdd {
                BILTransactionManager.shared.addressWillAdd(address: tx.address!, in: self)
            }
        }
		debugPrint("--- 4 \(targetSatoshi)")
		let endDate = Date()
		debugPrint("解析交易用时：\(endDate.seconds(from: beginDate))s")
    }
}
