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
    var amount: Int64
    var txHash: String
    var inputAddressString: String
    
    var remark: String?
    
    init(hex: String, address: String, inputAddressString: String, amount: Int64) {
        self.hexString = hex
        let data = hex.ck_mnemonicData()
        bytesCount = data.count
        self.address = address
        self.inputAddressString = inputAddressString
        self.amount = amount
        
        let bytes = hexString.ck_mnemonicData().bytes
        let hashBytes = bytes.sha256().sha256()
        let reverseData = Data(hashBytes.reversed())
        self.txHash = reverseData.toHexString()
    }
}

struct BTCInput {
    var txHash: String
    var index: Int
    var bip39Index: Int
    var satoshi: Int64
    var address: String
    var isChange: Bool
    init(txHash: String, index: Int, bip39Index: Int, satoshi: Int64, address: String, isChange: Bool) {
        self.txHash = txHash
        self.index = index
        self.bip39Index = bip39Index
        self.satoshi = satoshi
        self.address = address
        self.isChange = isChange
    }
    
    func toDictionary() -> [String: Any] {
        var dic = [String: Any]()
        dic["txHash"] = txHash
        dic["index"] = index
        dic["bip39Index"] = bip39Index
        dic["isChange"] = isChange
        return dic
    }
}

struct BTCOutput {
    var address: String
    var amount: Int64
    init(address: String, amount: Int64) {
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
    
    enum TransactionBuildError: LocalizedError {
        case noInput
        case noOutput
        case jsonError
        case notEnoughBalance
        case jsBuildTransactionFailed
        case buildError
        
        var localizedDescription: String {
            switch self {
            case .noInput:
                return NSLocalizedString("TransactionBuildError.noInput", comment: "")
            case .noOutput:
                return NSLocalizedString("TransactionBuildError.noOutput", comment: "")
            case .jsonError:
                return NSLocalizedString("TransactionBuildError.jsonError", comment: "")
            case .notEnoughBalance:
                return NSLocalizedString("TransactionBuildError.notEnoughBalance", comment: "")
            case .jsBuildTransactionFailed:
                return NSLocalizedString("TransactionBuildError.jsBuildTransactionFailed", comment: "")
            case .buildError:
                return NSLocalizedString("TransactionBuildError.buildError", comment: "")
            }
        }
        
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
        self.utxos = utxos.sorted()
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
    
    private func byteCountFor(inCount: Int, outCount: Int) -> Int64 {
        return Int64(inCount * TX_INPUT_SIZE + outCount * TX_OUTPUT_SIZE)
    }
    
    private func feeFor(inCount: Int, outCount: Int, feePerByte: Int = 0) -> Int64 {
        return byteCountFor(inCount:inCount, outCount:outCount) * Int64(feePerByte)
    }
    
    func fee(perByte: Int) throws -> Int64 {
        let minFee = Int64(0.00001 * Double(BTC_SATOSHI))
        if isSendAll {
			var totalSatoshis: Int64 = 0
			for utxo in utxos {
				totalSatoshis += utxo.satoshiAmount
			}
			let fee = feeFor(inCount: utxos.count, outCount: 1, feePerByte: perByte)
			if fee >= totalSatoshis
			{
				throw TransactionBuildError.notEnoughBalance
			}
            return max(minFee, fee)
        }
        
		var outputSatoshiSum: Int64 = 0
        for output in outputs {
            outputSatoshiSum += output.amount
        }
        
		var inputSatoshiSum: Int64 = 0
        var inputCount = 0
        var enough = false
        for utxo in utxos {
            inputSatoshiSum += utxo.satoshiAmount
            inputCount += 1
            if (inputSatoshiSum < outputSatoshiSum + feeFor(inCount: inputCount, outCount: outputCount, feePerByte: perByte)) {
                continue
            }
            enough = true
            break
        }
        if !enough {
            throw TransactionBuildError.notEnoughBalance
        }
        return max(feeFor(inCount: inputCount, outCount: outputCount, feePerByte: perByte), minFee)
    }
    
    private func maxFeeFor(inCount: Int, outCount: Int) -> Int64 {
		return feeFor(inCount:inCount, outCount:outCount, feePerByte: maxFeePerByte)
    }
    
    private var outputCount: Int {
        get {
            return outputs.count + (hasChangeOutput() ? 1 : 0)
        }
    }
    
    private var outputSatoshiWithoutChange: Int64 {
		var outputSatoshiSum: Int64 = 0
        for output in outputs {
            outputSatoshiSum += output.amount
        }
        return outputSatoshiSum
    }
    
    func canFeedOutputs() -> Bool {
        do {
            try chooseInput()
        } catch {
            return false
        }
        return true
    }
    
    private func chooseInput() throws {
        
        inputs.removeAll()
        
        if isSendAll {
            for utxo in utxos {
                addInput(input: utxo.toInput())
            }
            return
        }
        
        let outputSatoshiSum = outputSatoshiWithoutChange
        
        switch bulidTactics {
        case .clearSmallBalance:
			var inputSatoshiSum: Int64 = 0
            var enough = false
            for utxo in utxos {
                inputSatoshiSum += utxo.satoshiAmount
                addInput(input: utxo.toInput())
                if (inputSatoshiSum < outputSatoshiSum + feeFor(inCount: inputs.count, outCount: outputCount, feePerByte: feePerByte)) {
                    continue
                }
                enough = true
                break
            }
            if !enough {
                throw TransactionBuildError.notEnoughBalance
            }
        default:
            ()
        }
        
    }
    
    func build(success: @escaping (_ tx: BTCTransaction) -> Void, failure: @escaping (_ error: TransactionBuildError) -> Void) {
        
        guard outputs.count > 0 else {
            failure(TransactionBuildError.noOutput)
            return
        }
        
        do {
            try chooseInput()
        } catch {
            failure(TransactionBuildError.notEnoughBalance)
            return
        }
        
        guard inputs.count > 0 else {
            failure(TransactionBuildError.noInput)
            return
        }
        
		var totalFee: Int64 = 0
        do {
            totalFee = try fee(perByte: feePerByte)
        } catch {
            failure(TransactionBuildError.notEnoughBalance)
            return
        }
        
        
        guard totalFee > 0 else {
            failure(BTCTransactionBuilder.TransactionBuildError.jsBuildTransactionFailed)
            return
        }
        
        var txDatas = [String: Any]()
        
        var inputsJ = [[String: Any]]()
		var inputSatoshiSum: Int64 = 0
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
			if target.amount <= 0 {
				failure(TransactionBuildError.notEnoughBalance)
				return
			}
            outputsJ.append(target.toDictionary())
        }
        else
        {
			var outputSatoshiSum: Int64 = 0
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
                guard let str = result as? String else {
                    return
                }
                guard let targetOutput = self.outputs.first else {
                    return
                }
                let tx = BTCTransaction(hex: str, address:targetOutput.address, inputAddressString: inputAddresses.joined(separator: "|"), amount: targetOutput.amount)
                success(tx)
                
            }, failure: { (error) in
                failure(TransactionBuildError.jsBuildTransactionFailed)
            })
        } catch {
            failure(TransactionBuildError.jsonError)
        }
    }
}
