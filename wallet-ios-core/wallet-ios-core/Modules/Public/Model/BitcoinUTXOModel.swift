//
//  BitcoinUTXOModel.swift
//  wallet-ios-core
//
//  Created by 仇弘扬 on 2017/12/14.
//  Copyright © 2017年 BitBill. All rights reserved.
//

import UIKit
import SwiftyJSON

class BitcoinUTXOModel: NSObject {
    var txHash: String
    var txOutputIndex: Int
    var bip39Index: Int
    var requiredSignatureCount: Int
    var satoshiAmount: Int
    var amount: Double
    var availableforspending: Bool
    var address: String
    init(jsonData: JSON) {
        txHash = jsonData["txHash"].stringValue
        txOutputIndex = jsonData["vIndex"].intValue
        bip39Index = jsonData["addressIndex"].intValue
        requiredSignatureCount = jsonData["reqSings"].intValue
        satoshiAmount = jsonData["sumOutAmount"].intValue
        amount = Double(satoshiAmount) / Double(BTC_SATOSHI)
        availableforspending = jsonData["availableforspending"].boolValue
        address = jsonData["addressTxt"].stringValue
    }
    
    func toInput() -> BTCInput {
        return BTCInput(txHash: txHash, index: txOutputIndex, bip39Index: bip39Index, satoshi: satoshiAmount, address: address)
    }
}
