//
//  UIViewController+BIL.swift
//  wallet-ios-core
//
//  Created by 仇弘扬 on 2017/11/13.
//  Copyright © 2017年 BitBill. All rights reserved.
//

import Foundation
import UIKit

extension UIViewController {
	func bil_setBackgroudColor() {
		_ = view.setupGradient(colors: [UIColor.bil_deep_blue_start_bgcolor.cgColor, UIColor.bil_deep_blue_end_bgcolor.cgColor], startPoint: CGPoint(x: 0.5, y: 0), endPoint: CGPoint(x: 0.5, y: 1))
		view.backgroundColor = UIColor.clear
	}
	
	@IBAction
	func bil_dismissSelfModalViewController() {
		view.endEditing(true)
		dismiss(animated: true, completion: nil)
	}
	
	func bil_addBackButton() {
		
	}
	
	@IBAction func bil_pop() {
		navigationController?.popViewController(animated: true)
	}
}
