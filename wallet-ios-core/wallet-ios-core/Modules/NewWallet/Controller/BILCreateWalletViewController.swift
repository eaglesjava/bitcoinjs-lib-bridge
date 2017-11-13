//
//  BILNewWalletViewController.swift
//  wallet-ios-core
//
//  Created by 仇弘扬 on 2017/11/13.
//  Copyright © 2017年 BitBill. All rights reserved.
//

import UIKit

class BILCreateWalletViewController: UIViewController, UITextFieldDelegate {

	@IBOutlet var sucessView: BILCreateWalletSucessView!
	
	@IBOutlet weak var inputsView: UIView!
	@IBOutlet weak var passwordStrengthView: BILPasswordStrengthView!
	@IBOutlet weak var passwordTextField: ASKPlaceHolderColorTextField!
	
	enum createWalletType {
		case new
		case recover
	}
	
    override func viewDidLoad() {
        super.viewDidLoad()

        // Do any additional setup after loading the view.
    }
	
	override func viewDidAppear(_ animated: Bool) {
		super.viewDidAppear(true)
		NotificationCenter.default.addObserver(self, selector: #selector(self.textFieldValueDidChange(notification:))
			, name: .UITextFieldTextDidChange, object: nil)
	}
	
	override func viewDidDisappear(_ animated: Bool) {
		super.viewDidDisappear(true)
		NotificationCenter.default.removeObserver(self, name: .UITextFieldTextDidChange, object: nil)
	}

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
	
	// MARK: - UI
	
	func createSuccess() {
		view.addSubview(sucessView)
		sucessView.alpha = 0
		sucessView.frame = inputsView.frame
		sucessView.backgroundColor = UIColor.clear
		UIView.animate(withDuration: 0.35) {
			self.sucessView.alpha = 1
			self.inputsView.alpha = 0
		}
	}
	
	// MARK: - Delegates
	
	
	
	// MARK: - Actions
	
	@IBAction func createWalletAction(_ sender: Any) {
		createSuccess()
	}
	
	@objc func textFieldValueDidChange(notification: Notification) {
		if let textField: UITextField = notification.object as? UITextField {
			switch textField {
			case passwordTextField:
				passwordStrengthView.strength = .medium
			default: ()
			}
		}
	}
	
    // MARK: - Navigation

    // In a storyboard-based application, you will often want to do a little preparation before navigation
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        // Get the new view controller using segue.destinationViewController.
        // Pass the selected object to the new view controller.
		
    }

}
