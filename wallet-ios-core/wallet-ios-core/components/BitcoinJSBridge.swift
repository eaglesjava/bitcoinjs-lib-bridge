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
	
	func getAddress(seed: String, index: Int = 0, success: @escaping (_ object: Any) -> Void, failure: @escaping (_ error: Error) -> Void) {
		let method = "bridge.getBitcoinAddressBySeedHex('\(seed)', \(index))"
		callJS(method: method, success: success, failure: failure)
	}
	
	func getAddress(xpub: String, index: Int = 0, success: @escaping (_ object: Any) -> Void, failure: @escaping (_ error: Error) -> Void) {
		let method = "bridge.getBitcoinAddressByMasterXPublicKey('\(xpub)', \(index))"
		callJS(method: method, success: success, failure: failure)
	}
	
	func getMasterXPublicKey(seed: String, success: @escaping (_ object: Any) -> Void, failure: @escaping (_ error: Error) -> Void) {
		let method = "bridge.getBitcoinMasterXPublicKey('\(seed)')"
		callJS(method: method, success: success, failure: failure)
	}
	
	func callJS(method: String, success: @escaping (_ object: Any) -> Void, failure: @escaping (_ error: Error) -> Void) {
		if !canCallJSFunction {
			failure(JSError.JSDidNotLoaded)
			return
		}
		webview.evaluateJavaScript(method) { (object, error) in
			if let obj = object {
				print("call js method succeed: \(method), \(obj)")
				success(obj)
			}
			if let err = error {
				print("call js method failed: \(method), \(err)")
				failure(err)
			}
		}
	}
	
	func webView(_ webView: WKWebView, didFinish navigation: WKNavigation!) {
		canCallJSFunction = true
	}
	
}
