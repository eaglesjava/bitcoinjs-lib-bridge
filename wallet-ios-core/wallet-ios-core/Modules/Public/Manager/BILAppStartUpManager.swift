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
import Foundation
import SVProgressHUD
import CoreGraphics
import UserNotifications
import Toast_Swift

class BILAppStartUpManager: NSObject {
	
	static let shared = {
		return BILAppStartUpManager()
	}()
	
	var navBackgroundImage: UIImage? = UIImage()
    lazy var isSmallScreen: Bool = {
        return UIScreen.main.bounds.height <= 568
    }()
    
    var deviceToken: String?
    
	func startSetup() {
		setupPopupDialog()
		setupIQKeyboard()
		loadJS()
		snapshotNavBackgroundImage()
		setupTextFieldAppearance()
		setupTextViewAppearance()
		setupSVProgressHUD()
        setupPushService()
        setupToast()
        debugPrint(BILDeviceManager.shared.deviceID)
	}
    
    func setupToast() {
        var style = ToastStyle()
        style.backgroundColor = UIColor.white
        style.messageFont = UIFont.systemFont(ofSize: 15)
        style.cornerRadius = 20
        style.messageColor = UIColor.black
        style.horizontalPadding = 20
        style.verticalPadding = 11
        style.displayShadow = true
        style.shadowOffset = CGSize(width: 0, height: 0)
        style.shadowOpacity = 0.3
        ToastManager.shared.style = style
        ToastManager.shared.position = .bottom
    }
    
    func setupPushService() {
        let center = UNUserNotificationCenter.current()
        center.requestAuthorization(options: [.alert, .badge, .sound]) { (granted, error) in
            if granted {
                debugPrint("UNUserNotificationCenter success")
                DispatchQueue.main.async {
                    UIApplication.shared.registerForRemoteNotifications()
                }
            } else if error == nil {
                debugPrint("UNUserNotificationCenter failed")
            }
        }
    }
	
	func snapshotNavBackgroundImage(rect: CGRect) -> UIImage? {
		guard let image = navBackgroundImage else {
			return nil
		}
		return image.snapshotSubImage(rect: rect)
	}
	
	private func setupSVProgressHUD() {
        SVProgressHUD.setDefaultStyle(.dark)
		SVProgressHUD.setMinimumDismissTimeInterval(0.5)
        SVProgressHUD.setDefaultMaskType(.black)
//        SVProgressHUD.setMinimumSize(.init(width: 280, height: 128))
	}
	
	private func setupTextFieldAppearance() {
		let appearance = UITextField.appearance()
		appearance.tintColor = UIColor.white
	}
	
	private func setupTextViewAppearance() {
		let appearance = UITextView.appearance()
		appearance.tintColor = UIColor.white
	}
	
	private func snapshotNavBackgroundImage() {
		let v = UIView(frame: UIScreen.main.bounds)
		let layer = v.setupGradient(colors: [UIColor.bil_deep_blue_start_bgcolor.cgColor, UIColor.bil_deep_blue_end_bgcolor.cgColor], startPoint: CGPoint(x: 0.3, y: 0), endPoint: CGPoint(x: 0.7, y: 1))
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
		appearance.titleTextAttributes = [.foregroundColor: UIColor.white]
		appearance.tintColor = UIColor.white
		if #available(iOS 11.0, *) {
			appearance.prefersLargeTitles = !isSmallScreen
			appearance.largeTitleTextAttributes = [.foregroundColor: UIColor.white]
		}
	}
	
	private func setupPopupDialog() {
		let dialogAppearance = PopupDialogDefaultView.appearance()
		
		dialogAppearance.backgroundColor      = UIColor.white
		dialogAppearance.titleFont            = UIFont.systemFont(ofSize: 16, weight: .medium)
		dialogAppearance.titleColor           = UIColor.black
		
		let buttonAppearance = DefaultButton.appearance()
		buttonAppearance.titleColor = UIColor(hex: 0x318AF3)
		buttonAppearance.titleFont = UIFont.systemFont(ofSize: 16)
		
		let blurAppearance = PopupDialogOverlayView.appearance()
		blurAppearance.blurEnabled = false
		
		let containerViewAppearance = PopupDialogContainerView.appearance()
		containerViewAppearance.cornerRadius = 16
	}
	
	private func setupIQKeyboard() {
		IQKeyboardManager.sharedManager().enable = true
		IQKeyboardManager.sharedManager().shouldResignOnTouchOutside = true
		IQKeyboardManager.sharedManager().toolbarDoneBarButtonItemText = "收起键盘"
        
        IQKeyboardManager.sharedManager().disabledToolbarClasses.append(BILSendInputAmountController.self)
        IQKeyboardManager.sharedManager().disabledToolbarClasses.append(BILSpecificVolumeRecieveInputController.self)
        
        IQKeyboardManager.sharedManager().disabledTouchResignedClasses.append(BILSendInputAmountController.self)
        IQKeyboardManager.sharedManager().disabledTouchResignedClasses.append(BILSpecificVolumeRecieveInputController.self)
	}
	
	private func loadJS() {
		BitcoinJSBridge.shared.loadBitcoinJS()
	}
	
}
