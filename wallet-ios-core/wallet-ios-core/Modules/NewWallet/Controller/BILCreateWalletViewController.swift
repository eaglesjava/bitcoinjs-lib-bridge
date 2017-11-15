//
//  BILNewWalletViewController.swift
//  wallet-ios-core
//
//  Created by 仇弘扬 on 2017/11/13.
//  Copyright © 2017年 BitBill. All rights reserved.
//

import UIKit
import SVProgressHUD
import CoreData
import CryptoSwift
import PopupDialog

class BILCreateWalletViewController: UIViewController, UITextFieldDelegate {
	
	enum CreateWalletType {
		case new
		case recover
		
		func titleString() -> String {
			return self == .new ? "新建" : "导入"
		}
		
	}

	@IBOutlet var sucessView: BILCreateWalletSucessView!
	
	@IBOutlet weak var titleLabel: UILabel!
	@IBOutlet weak var inputsView: UIView!
	@IBOutlet weak var passwordStrengthView: BILPasswordStrengthView!
	@IBOutlet weak var passwordTextField: ASKPlaceHolderColorTextField!
	@IBOutlet weak var walletNameTextField: ASKPlaceHolderColorTextField!
	@IBOutlet weak var confirmPasswordTextField: ASKPlaceHolderColorTextField!
	
	@IBOutlet weak var createButton: BILGradientButton!
	
	@IBOutlet weak var walletNameInputView: BILInputView!
	@IBOutlet weak var passwordInputView: BILInputView!
	@IBOutlet weak var confirmPasswordInputView: BILInputView!
	
	var createWalletType: CreateWalletType = .new
	var mnemonic: String? {
		didSet {
			createWalletType = .recover
		}
	}
	
    override func viewDidLoad() {
        super.viewDidLoad()

        // Do any additional setup after loading the view.
		let titleString = createWalletType.titleString()
		titleLabel.text = "\(titleString)钱包"
		createButton.setTitle("开始\(titleString)", for: .normal)
    }
	
	override func viewDidAppear(_ animated: Bool) {
		super.viewDidAppear(true)
		NotificationCenter.default.addObserver(self, selector: #selector(self.textFieldValueDidChange(notification:))
			, name: .UITextFieldTextDidChange, object: nil)
		showAlertForSupportedCoins()
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
	
	func showAlertForSupportedCoins() {
		let buttonTitle = "我知道了"
		
		let vc = UIViewController(nibName: "BILSupportedCoinsPopupController", bundle: nil)
		let popup = PopupDialog(viewController: vc, transitionStyle: .fadeIn, gestureDismissal: true, hideStatusBar: true) {
			print("popup")
		}
		
		let button = DefaultButton(title: buttonTitle, dismissOnTap: true) {
			
		}
		popup.addButton(button)
		
		present(popup, animated: true) {
			print("present")
		}
	}
	
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
	
	func checkPassword() -> Bool {
		if let pwd = passwordTextField.text, pwd.count <= 20 && pwd.count >= 6 {
			return true
		}
		return false
	}
	
	func checkConfirmPassword() -> Bool {
		if let confirmPwd = confirmPasswordTextField.text, confirmPwd.count <= 20 && confirmPwd.count >= 6, confirmPwd == passwordTextField.text {
			return true
		}
		return false
	}
	
	func checkWalletName() -> Bool {
		if let walletName = walletNameTextField.text, walletName.count <= 20 && walletName.count >= 1 {
			return true
		}
		return false
	}
	
	func createWallet() {
		guard checkWalletName() else {
			walletNameInputView.show(tip: "名称限制在20位以内字符", type: .error)
			return
		}
		guard checkPassword() else {
			passwordInputView.show(tip: "密码长度限制在6-20位字符", type: .error)
			return
		}
		guard checkConfirmPassword() else {
			confirmPasswordInputView.show(tip: "密码不一致", type: .error)
			return
		}
		
		getMnemonic { (m) in
			BitcoinJSBridge.shared.mnemonicToSeedHex(mnemonic: m, password: "", success: { (seedHex) in
				let s = seedHex as! String
				let context = (UIApplication.shared.delegate as! AppDelegate).persistentContainer.viewContext
				let wallet = NSEntityDescription.insertNewObject(forEntityName: "WalletModel", into: context) as! WalletModel
				wallet.name = self.walletNameTextField.text!
				wallet.createDate = Date()
				do {
					let pwd = self.passwordTextField.text!
					let key = String(pwd.sha256().prefix(32))
					let aes = try AES(key: key, iv: String(key.reversed().prefix(16)))
					wallet.encryptedMnemonic = try aes.encrypt(Array(m.bytes)).toHexString()
					wallet.encryptedSeed = try aes.encrypt(Array(s.bytes)).toHexString()
					wallet.seedHash = s.md5()
					
					if let seed = String(bytes: try aes.decrypt((wallet.encryptedSeed?.ck_mnemonicData().bytes)!), encoding: .utf8), seed == s {
						try context.save()
						self.createSuccess()
					}
				} catch {
					SVProgressHUD.showError(withStatus: error.localizedDescription)
					print(error)
				}
			}, failure: { (error) in
				SVProgressHUD.showError(withStatus: error.localizedDescription)
				print(error)
			})
		}
		
	}
	
	func getMnemonic(complete: @escaping (_ mnemoic: String) -> Void) {
		if let m = mnemonic {
			complete(m)
		}
		else {
			BitcoinJSBridge.shared.generateMnemonic(language: .chinese, success: { (mnemonic) in
				complete(mnemonic as! String)
			}) { (error) in
				SVProgressHUD.showError(withStatus: error.localizedDescription)
				print(error)
			}
		}
	}
	
	// MARK: - Delegates
	
	func textFieldShouldReturn(_ textField: UITextField) -> Bool {
		switch textField {
		case walletNameTextField:
			if checkWalletName() {
				passwordTextField.becomeFirstResponder()
			}
			else
			{
				walletNameInputView.show(tip: "名称限制在20位以内字符", type: .error)
				return false
			}
		case passwordTextField:
			if checkPassword() {
				confirmPasswordTextField.becomeFirstResponder()
			}
			else
			{
				passwordInputView.show(tip: "密码长度限制在6-20位字符", type: .error)
				return false
			}
		case confirmPasswordTextField:
			if checkConfirmPassword() {
				createWallet()
				view.endEditing(true)
			}
			else
			{
				confirmPasswordInputView.show(tip: "密码不一致", type: .error)
				return false
			}
		default: ()
		}
		return true
	}
	
	// MARK: - Actions
	
	@IBAction func createWalletAction(_ sender: Any) {
		createWallet()
	}
	
	@objc func textFieldValueDidChange(notification: Notification) {
		if let textField: UITextField = notification.object as? UITextField {
			switch textField {
			case walletNameTextField:
				walletNameInputView.show(tip: "钱包名称", type: .normal)
			case passwordTextField:
				passwordStrengthView.strength = BILPasswordStrengthView.caculatePasswordStrength(pwd: textField.text ?? "")
				passwordInputView.show(tip: "创建交易密码", type: .normal)
			case confirmPasswordTextField:
				confirmPasswordInputView.show(tip: "确认交易密码", type: .normal)
			default: ()
			}
		}
//		if checkWalletName() {
//			walletNameInputView.show(tip: "钱包名称", type: .normal)
//		}
//		if checkPassword() {
//			passwordInputView.show(tip: "创建交易密码", type: .normal)
//		}
//		if checkConfirmPassword() {
//			confirmPasswordInputView.show(tip: "确认交易密码", type: .normal)
//		}
	}
	
    // MARK: - Navigation

    // In a storyboard-based application, you will often want to do a little preparation before navigation
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        // Get the new view controller using segue.destinationViewController.
        // Pass the selected object to the new view controller.
		
    }

}
