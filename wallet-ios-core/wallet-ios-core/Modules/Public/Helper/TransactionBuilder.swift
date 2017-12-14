//
//  BILTransactionBuilder.swift
//  wallet-ios-core
//
//  Created by 仇弘扬 on 2017/12/14.
//  Copyright © 2017年 BitBill. All rights reserved.
//

import UIKit
import SwiftyJSON

enum TransactionBuildTactics {
    case minSize
    case clearSmallBalance
}

class Transaction: NSObject {
    var address: String?
    var bytesCount: Int = 0
    var hexString: String
    init(hex: String) {
        self.hexString = hex
        let data = hex.ck_mnemonicData()
        bytesCount = data.count
    }
}

struct BTCInput {
    var txHash: String
    var index: Int
    var bip39Index: Int
    init(txHash: String, index: Int, bip39Index: Int) {
        self.txHash = txHash
        self.index = index
        self.bip39Index = bip39Index
    }
    
    func toDictionary() -> [String: Any] {
        var dic = [String: Any]()
        dic["txHash"] = txHash
        dic["index"] = index
        dic["bip39Index"] = bip39Index
        return dic
    }
}

struct BTCOutput {
    var address: String
    var amount: Int
    init(address: String, amount: Int) {
        self.address = address
        self.amount = amount
    }
    
    func toDictionary() -> [String: Any] {
        var dic = [String: Any]()
        dic["address"] = address
        dic["amount"] = amount
        return dic
    }
}

class TransactionBuilder: NSObject {
    
    enum TransactionBuildError: Error {
        case noInput
        case noOutput
        case jsonError
    }
    
    var seedHex: String
    
    var inputs = [BTCInput]()
    var outputs = [BTCOutput]()
    
    init(seedHex: String = "") {
        self.seedHex = seedHex
    }
    
    func addInput(input: BTCInput) {
        inputs.append(input)
    }
    
    func addOutput(output: BTCOutput) {
        outputs.append(output)
    }
    
    func build(success: @escaping (_ tx: Transaction) -> Void, failure: @escaping (_ error: Error) -> Void) {
        
        guard inputs.count > 0 else {
            failure(TransactionBuildError.noInput)
            return
        }
        
        guard outputs.count > 0 else {
            failure(TransactionBuildError.noOutput)
            return
        }
        
        var txDatas = [String: Any]()
        
        var inputsJ = [[String: Any]]()
        for input in inputs {
            inputsJ.append(input.toDictionary())
        }
        txDatas["inputs"] = inputsJ
        
        var outputsJ = [[String: Any]]()
        for output in outputs {
            outputsJ.append(output.toDictionary())
        }
        txDatas["outputs"] = outputsJ
        
        do {
            guard let jsonString = String(data: try JSONSerialization.data(withJSONObject: txDatas, options: .prettyPrinted), encoding: .utf8) else {
                throw TransactionBuildError.jsonError
            }
            debugPrint(jsonString)
            let noWhiteSpaceJson = jsonString.replacingOccurrences(of: "\n", with: "").replacingOccurrences(of: " ", with: "")
            BitcoinJSBridge.shared.buildTransaction(seedHex: seedHex, inputsOutputs: noWhiteSpaceJson, success: { (result) in
                debugPrint(result)
                guard let str = result as? String else {
                    return
                }
                let tx = Transaction(hex: str)
                success(tx)
            }, failure: { (error) in
                failure(error)
            })
        } catch {
            failure(error)
        }
    }
}
