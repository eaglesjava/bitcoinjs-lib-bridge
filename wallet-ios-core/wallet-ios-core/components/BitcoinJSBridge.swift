//
//  BitcoinJSBridge.swift
//  wallet-ios-core
//
//  Created by 仇弘扬 on 2017/11/7.
//  Copyright © 2017年 BitBill. All rights reserved.
//

import Foundation
import WebKit

class BitcoinJSBridge: NSObject, WKNavigationDelegate {
	let webview = WKWebView()
	
	var canCallJSFunction = false
	
	enum Language {
		case chinese
		case english
		func languageParameter() -> String {
			var toReturn = ""
			switch self {
			case .chinese:
				toReturn = "chinese_simplified"
			default:
				toReturn = "english"
			}
			return "bridge.bip39.wordlists." + toReturn
		}
	}
	
	enum JSError: Error {
		case JSDidNotLoaded
        case resultError
	}
	
	static let shared: BitcoinJSBridge = {
		BitcoinJSBridge()
	}()
	
	func loadBitcoinJS() {
		webview.navigationDelegate = self
		let re = URLRequest(url: Bundle.main.url(forResource: "index", withExtension: "html")!)
		webview.load(re)
	}
	
	func generateMnemonic(entropy: Int = 128, language: Language = .english, success: @escaping (_ object: Any) -> Void, failure: @escaping (_ error: Error) -> Void) {
		let method = "bridge.generateMnemonicRandom(\(entropy), \(language.languageParameter()))"
		callJS(method: method, success: success, failure: failure)
	}
	
	func mnemonicToSeedHex(mnemonic: String, password: String = "", success: @escaping (_ object: Any) -> Void, failure: @escaping (_ error: Error) -> Void) {
		let method = "bridge.mnemonicToSeedHex('\(mnemonic)', '\(password)')"
		callJS(method: method, success: success, failure: failure)
	}
	
	func validateMnemonic(mnemonic: String, success: @escaping (_ object: Any) -> Void, failure: @escaping (_ error: Error) -> Void) {
		let method = "bridge.validateMnemonic('\(mnemonic)')"
		callJS(method: method, success: success, failure: failure)
	}
    
    func validateAddress(address: String, success: @escaping (_ object: Any) -> Void, failure: @escaping (_ error: Error) -> Void) {
        let method = "bridge.validateAddress('\(address)')"
        callJS(method: method, success: success, failure: failure)
    }
	
	func getAddress(seed: String, index: Int = 0, success: @escaping (_ object: Any) -> Void, failure: @escaping (_ error: Error) -> Void) {
		let method = "bridge.getBitcoinAddressBySeedHex('\(seed)', \(index))"
		callJS(method: method, success: success, failure: failure)
	}
	
	func getAddress(xpub: String, index: Int = 0, success: @escaping (_ object: Any) -> Void, failure: @escaping (_ error: Error) -> Void) {
		let method = "bridge.getBitcoinAddressByMasterXPublicKey('\(xpub)', \(index))"
		callJS(method: method, success: success, failure: failure)
	}
    
    func getAddresses(xpub: String, fromIndex: Int64 = 0, toIndex: Int64, success: @escaping (_ object: Any) -> Void, failure: @escaping (_ error: Error) -> Void) {
        let method = "bridge.getBitcoinContinuousAddressByMasterXPublicKey('\(xpub)', \(fromIndex), \(toIndex))"
        callJS(method: method, success: success, failure: failure)
    }
    
    func getXPublicKeys(seed: String, success: @escaping (_ object: (mainPubkey: String, changePubkey: String)) -> Void, failure: @escaping (_ error: Error) -> Void) {
        let method = "bridge.getBitcoinXPublicKeys('\(seed)')"
        callJS(method: method, success: { (result) in
            guard let arr = result as? Array<String>, arr.count == 2 else {
                failure(JSError.resultError)
                return
            }
            success((arr[0], arr[1]))
        }, failure: failure)
    }
	
	func getMasterXPublicKey(seed: String, success: @escaping (_ object: Any) -> Void, failure: @escaping (_ error: Error) -> Void) {
		let method = "bridge.getBitcoinMasterXPublicKey('\(seed)')"
		callJS(method: method, success: success, failure: failure)
	}
    
    func getChangeXPublicKey(seed: String, success: @escaping (_ object: Any) -> Void, failure: @escaping (_ error: Error) -> Void) {
        let method = "bridge.getBitcoinChangeXPublicKey('\(seed)')"
        callJS(method: method, success: success, failure: failure)
    }
    
    func getMasterXPublicKey(mnemonic: String, success: @escaping (_ object: Any) -> Void, failure: @escaping (_ error: Error) -> Void) {
        mnemonicToSeedHex(mnemonic: mnemonic, success: { (seedHex) in
            self.getMasterXPublicKey(seed: seedHex as! String, success: success, failure: failure)
        }, failure: failure)
    }
    
    func buildTransaction(seedHex: String, inputsOutputs: String, success: @escaping (_ object: Any) -> Void, failure: @escaping (_ error: Error) -> Void) {
        let method = "bridge.buildTransaction('\(seedHex)', '\(inputsOutputs)')"
        callJS(method: method, success: success, failure: failure)
    }
	
	func callJS(method: String, success: @escaping (_ object: Any) -> Void, failure: @escaping (_ error: Error) -> Void) {
		if !canCallJSFunction {
			failure(JSError.JSDidNotLoaded)
			return
		}
		webview.evaluateJavaScript(method) { (object, error) in
			if let obj = object {
				debugPrint("call js method succeed: \(method), \(obj)")
				success(obj)
			}
			if let err = error {
				debugPrint("call js method failed: \(method), \(err)")
				failure(err)
			}
		}
	}
	
	func webView(_ webView: WKWebView, didFinish navigation: WKNavigation!) {
		canCallJSFunction = true
        BILWalletManager.shared.loadBlockHeightAndWalletVersion()
	}
    
    func webView(_ webView: WKWebView, didFail navigation: WKNavigation!, withError error: Error) {
        canCallJSFunction = false
    }
	
}
