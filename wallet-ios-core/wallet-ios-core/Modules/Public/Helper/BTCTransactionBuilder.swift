//
//  BILTransactionBuilder.swift
//  wallet-ios-core
//
//  Created by 仇弘扬 on 2017/12/14.
//  Copyright © 2017年 BitBill. All rights reserved.
//

import UIKit
import SwiftyJSON
import CryptoSwift

let TX_OUTPUT_SIZE = 35
let TX_INPUT_SIZE = 150

enum TransactionBuildTactics {
    case minSize
    case clearSmallBalance
}

class BTCTransaction: NSObject {
    var address: String
    var bytesCount: Int = 0
    var hexString: String
    var amount: Int
    var txHash: String
    var inputAddressString: String
    
    init(hex: String, address: String, inputAddressString: String, amount: Int) {
        self.hexString = hex
        let data = hex.ck_mnemonicData()
        bytesCount = data.count
        self.address = address
        self.inputAddressString = inputAddressString
        self.amount = amount
        let bytes = hexString.ck_mnemonicData().bytes
        
        let hashData = Data(bytes.sha256().sha256())
        let reverseData = Data(hashData.bytes.reversed())
        self.txHash = reverseData.toHexString()
    }
}

struct BTCInput {
    var txHash: String
    var index: Int
    var bip39Index: Int
    var satoshi: Int
    var address: String
    init(txHash: String, index: Int, bip39Index: Int, satoshi: Int, address: String) {
        self.txHash = txHash
        self.index = index
        self.bip39Index = bip39Index
        self.satoshi = satoshi
        self.address = address
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

class BTCTransactionBuilder: NSObject {
    
    enum TransactionBuildError: Error {
        case noInput
        case noOutput
        case jsonError
    }
    
    var seedHex: String
    var bulidTactics: TransactionBuildTactics
    var changeAddress: String?
    
    var utxos = [BitcoinUTXOModel]()
    var feePerByte = 0
    var maxFeePerByte = 0
    var fee = 0
    
    var inputs = [BTCInput]()
    var outputs = [BTCOutput]()
    
    private var changeOutput: BTCOutput?
    private var targetOutput: BTCOutput?
    
    var isSendAll: Bool
    
    init(seedHex: String = "",
         bulidTactics: TransactionBuildTactics = .clearSmallBalance,
         utxos: [BitcoinUTXOModel],
         changeAddress: String? = nil,
         feePerByte: Int,
         maxFeePerByte: Int = 0,
         isSendAll: Bool = false) {
        self.seedHex = seedHex
        self.bulidTactics = bulidTactics
        self.utxos = utxos
        self.changeAddress = changeAddress
        self.feePerByte = feePerByte
        self.maxFeePerByte = maxFeePerByte
        self.isSendAll = isSendAll
        super.init()
        self.addChangeOutput()
    }
    
    func addInput(input: BTCInput) {
        guard let first = inputs.first, input.satoshi < first.satoshi else {
            inputs.append(input)
            return
        }
        inputs.insert(input, at: 0)
    }
    
    func addOutput(output: BTCOutput) {
        outputs.append(output)
    }
    
    func addTargetOutput(output: BTCOutput) -> Bool {
        if isSendAll {
            if outputs.count > 1 {
                return false
            }
            else
            {
                targetOutput = output
            }
        }
        outputs.append(output)
        return true
    }
    
    private func addChangeOutput() {
        guard let address = changeAddress else { return }
        
        let output = BTCOutput(address: address, amount: 0)
        changeOutput = output
    }
    
    func hasChangeOutput() -> Bool {
        return changeOutput != nil
    }
    
    private func byteCountFor(inCount: Int, outCount: Int) -> Int {
        return (inCount * TX_INPUT_SIZE + outCount * TX_OUTPUT_SIZE)
    }
    
    private func feeFor(inCount: Int, outCount: Int, feePerByte: Int = 0) -> Int {
        return byteCountFor(inCount:inCount, outCount:outCount) * feePerByte
    }
    
    func fee(perByte: Int) -> Int {
        
        var outputSatoshiSum = 0
        for output in outputs {
            outputSatoshiSum += output.amount
        }
        
        var inputSatoshiSum = 0
        var inputCount = 0
        for utxo in utxos {
            inputSatoshiSum += utxo.satoshiAmount
            inputCount += 1
            if (inputSatoshiSum < outputSatoshiSum + maxFeeFor(inCount: inputs.count, outCount: outputCount)) {
                continue
            }
            break
        }
        return byteCountFor(inCount:inputCount, outCount: outputCount) * perByte
    }
    
    private func maxFeeFor(inCount: Int, outCount: Int) -> Int {
        return feeFor(inCount:inCount, outCount:outCount, feePerByte: maxFeePerByte)
    }
    
    private var outputCount: Int {
        get {
            return outputs.count + (hasChangeOutput() ? 1 : 0)
        }
    }
    
    private var outputSatoshiWithoutChange: Int {
        var outputSatoshiSum = 0
        for output in outputs {
            outputSatoshiSum += output.amount
        }
        return outputSatoshiSum
    }
    
    private func chooseInput() {
        
        if isSendAll {
            for utxo in utxos {
                addInput(input: utxo.toInput())
            }
            return
        }
        
        let outputSatoshiSum = outputSatoshiWithoutChange
        
        switch bulidTactics {
        case .clearSmallBalance:
            var inputSatoshiSum = 0
            for utxo in utxos {
                inputSatoshiSum += utxo.satoshiAmount
                addInput(input: utxo.toInput())
                if (inputSatoshiSum < outputSatoshiSum + maxFeeFor(inCount: inputs.count, outCount: outputCount)) {
                    continue
                }
                break
            }
        default:
            ()
        }
        
    }
    
    func build(success: @escaping (_ tx: BTCTransaction) -> Void, failure: @escaping (_ error: Error) -> Void) {
        
        guard outputs.count > 0 else {
            failure(TransactionBuildError.noOutput)
            return
        }
        
        chooseInput()
        
        guard inputs.count > 0 else {
            failure(TransactionBuildError.noInput)
            return
        }
        
        let totalFee = fee(perByte: feePerByte)
        
        var txDatas = [String: Any]()
        
        var inputsJ = [[String: Any]]()
        var inputSatoshiSum = 0
        var inputAddresses = [String]()
        for input in inputs {
            inputsJ.append(input.toDictionary())
            inputSatoshiSum += input.satoshi
            inputAddresses.append(input.address)
        }
        txDatas["inputs"] = inputsJ
        
        var outputsJ = [[String: Any]]()
        if isSendAll {
            guard var target = targetOutput else {
                failure(TransactionBuildError.noOutput)
                return
            }
            target.amount = inputSatoshiSum - totalFee
            outputsJ.append(target.toDictionary())
        }
        else
        {
            var outputSatoshiSum = 0
            for output in outputs {
                outputsJ.append(output.toDictionary())
                outputSatoshiSum += output.amount
            }
            
            if var cOutput = changeOutput {
                cOutput.amount = inputSatoshiSum - outputSatoshiSum - totalFee
                outputsJ.append(cOutput.toDictionary())
            }
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
                guard let targetOutput = self.outputs.first else {
                    return
                }
                let tx = BTCTransaction(hex: str, address:targetOutput.address, inputAddressString: inputAddresses.joined(separator: "|"), amount: targetOutput.amount)
                success(tx)
                
            }, failure: { (error) in
                failure(error)
            })
        } catch {
            failure(error)
        }
    }
}
