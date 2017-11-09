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
		let method = "bridge.generateMnemonicRandom(128, \(language.languageParameter()))"
		callJS(method: method, success: success, failure: failure)
	}
	
	func callJS(method: String, success: @escaping (_ object: Any) -> Void, failure: @escaping (_ error: Error) -> Void) {
		if !canCallJSFunction {
			failure(JSError.JSDidNotLoaded)
			return
		}
		webview.evaluateJavaScript(method) { (object, error) in
			if let obj = object {
				success(obj)
			}
			if let err = error {
				failure(err)
			}
		}
	}
	
	func webView(_ webView: WKWebView, didFinish navigation: WKNavigation!) {
		canCallJSFunction = true
	}
	
}
