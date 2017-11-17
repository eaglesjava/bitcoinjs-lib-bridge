//
//  BILControllerManager.swift
//  wallet-ios-core
//
//  Created by 仇弘扬 on 2017/11/17.
//  Copyright © 2017年 BitBill. All rights reserved.
//

import UIKit

class BILControllerManager: NSObject {
	var mainTabBarController: UITabBarController?
	static let shared = {
		return BILControllerManager()
	}()
	
	func showMainTabBarController() {
		UIApplication.shared.delegate?.window??.rootViewController?.dismiss(animated: true, completion: nil)
	}
}
