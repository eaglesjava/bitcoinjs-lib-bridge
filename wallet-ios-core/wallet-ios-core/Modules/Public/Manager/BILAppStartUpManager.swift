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
	
	var navBackgroundImage: UIImage? = UIImage()
	
	func startSetup() {
		setupPopupDialog()
		setupIQKeyboard()
		loadJS()
		snapshotNavBackgroundImage()
//		setupNavigationBarAppearance()
	}
	
	private func snapshotNavBackgroundImage() {
		let v = UIView(frame: UIScreen.main.bounds)
		let layer = v.setupGradient(colors: [UIColor.bil_deep_blue_start_bgcolor.cgColor, UIColor.bil_deep_blue_end_bgcolor.cgColor], startPoint: CGPoint(x: 0.5, y: 0), endPoint: CGPoint(x: 0.5, y: 1))
		UIGraphicsBeginImageContextWithOptions(v.bounds.size, true, UIScreen.main.scale)
		if let currentContext = UIGraphicsGetCurrentContext() {
			layer.render(in: currentContext)
			let image = UIGraphicsGetImageFromCurrentImageContext()
			navBackgroundImage = image
		}
		UIGraphicsEndImageContext()
		if navBackgroundImage != nil {
			setupNavigationBarAppearance()
		}
	}
	
	private func setupNavigationBarAppearance() {
		let appearance = UINavigationBar.appearance()
		appearance.setBackgroundImage(UIImage(), for: .default)
		appearance.shadowImage = UIImage()
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
