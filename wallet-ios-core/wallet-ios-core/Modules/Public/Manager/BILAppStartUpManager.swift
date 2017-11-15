//
//  BILAppStartUpManager.swift
//  wallet-ios-core
//
//  Created by 仇弘扬 on 2017/11/15.
//  Copyright © 2017年 BitBill. All rights reserved.
//

import UIKit
import PopupDialog
import IQKeyboardManagerSwift

class BILAppStartUpManager: NSObject {
	static let shared = {
		return BILAppStartUpManager()
	}()
	
	func startSetup() {
		setupPopupDialog()
		setupIQKeyboard()
		loadJS()
	}
	
	private func setupPopupDialog() {
		let dialogAppearance = PopupDialogDefaultView.appearance()
		
		dialogAppearance.backgroundColor      = UIColor.white
		dialogAppearance.titleFont            = UIFont.systemFont(ofSize: 16, weight: .medium)
		dialogAppearance.titleColor           = UIColor.black
		
		let buttonAppearance = DefaultButton.appearance()
		buttonAppearance.titleColor = UIColor(hex: 0x318AF3)
		buttonAppearance.titleFont = UIFont.systemFont(ofSize: 16)
	}
	
	private func setupIQKeyboard() {
		IQKeyboardManager.sharedManager().enable = true
		IQKeyboardManager.sharedManager().shouldResignOnTouchOutside = true
		IQKeyboardManager.sharedManager().toolbarDoneBarButtonItemText = "收起键盘"
	}
	
	private func loadJS() {
		BitcoinJSBridge.shared.loadBitcoinJS()
	}
	
}
