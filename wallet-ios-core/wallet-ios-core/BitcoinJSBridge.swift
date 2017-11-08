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
	
	static let shared: BitcoinJSBridge = {
		BitcoinJSBridge()
	}()
	
	func loadBitcoinJS() {
		webview.navigationDelegate = self
		let re = URLRequest(url: Bundle.main.url(forResource: "index", withExtension: "html")!)
		webview.load(re)
	}
	
	func webView(_ webView: WKWebView, didFinish navigation: WKNavigation!) {
		webview.evaluateJavaScript("bridge.myFunction()") { (obj, err) in
			if let j = obj {
				print(j)
			}
		}
		
		webview.evaluateJavaScript("bridge.bip39Test()") { (obj, err) in
			if let j = obj {
				print(j)
			}
		}
		
		webview.evaluateJavaScript("bridge.paramsTest(5)") { (obj, err) in
			if let j = obj {
				print(j)
			}
		}
	}
}
