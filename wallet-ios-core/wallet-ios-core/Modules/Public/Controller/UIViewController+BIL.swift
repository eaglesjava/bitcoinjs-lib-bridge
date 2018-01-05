//
//  UIViewController+BIL.swift
//  wallet-ios-core
//
//  Created by 仇弘扬 on 2017/11/13.
//  Copyright © 2017年 BitBill. All rights reserved.
//

import Foundation
import UIKit
import SVProgressHUD

extension UIViewController {
	
	@IBAction
	func bil_dismissSelfModalViewController() {
		view.endEditing(true)
		dismiss(animated: true, completion: nil)
	}
	
	func bil_addBackButton() {
		
	}
    
    func bil_showLoading(status: String? = nil) {
        SVProgressHUD.show(withStatus: status)
    }
    
    func bil_showSuccess(status: String) {
        SVProgressHUD.showSuccess(withStatus: status)
    }
    
    func bil_showError(status: String) {
        SVProgressHUD.showError(withStatus: status)
    }
    
    func bil_dismissHUD(delay: Double = 0, complete: SVProgressHUDDismissCompletion? = nil) {
        SVProgressHUD.dismiss(withDelay: delay, completion: complete)
    }
    
    func showTipAlert(title: String? = nil, msg: String? = nil, actionTitle: String = .publicAlertActionTitle, dismissed: (() -> Void)? = nil) {
        let alert = UIAlertController(title: title, message: msg, preferredStyle: .alert)
        
        alert.addAction(UIAlertAction(title: actionTitle, style: .default, handler: { (action) in
            dismissed?()
        }))
        
        present(alert, animated: true, completion: nil)
    }
	
	@IBAction func bil_pop() {
		navigationController?.popViewController(animated: true)
	}
}
