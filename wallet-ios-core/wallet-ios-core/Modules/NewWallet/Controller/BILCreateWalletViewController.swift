//
//  BILNewWalletViewController.swift
//  wallet-ios-core
//
//  Created by 仇弘扬 on 2017/11/13.
//  Copyright © 2017年 BitBill. All rights reserved.
//

import UIKit
import CoreData
import CryptoSwift
import SwiftyJSON

enum CreateWalletType {
    case new
    case recover
    case resetPassword
    
    func titleString() -> String {
        switch self {
        case .new:
            return .newWallet_create_typeNew
        case .recover:
            return .newWallet_create_typeRecover
        case .resetPassword:
            return .newWallet_create_typeResetPassword
        }
    }
}

class BILCreateWalletViewController: BILBaseViewController, BILInputViewDelegate {
	
	@IBOutlet weak var inputsView: UIView!
	@IBOutlet weak var passwordStrengthView: BILPasswordStrengthView!
	@IBOutlet weak var passwordTextField: ASKPlaceHolderColorTextField!
	@IBOutlet weak var confirmPasswordTextField: ASKPlaceHolderColorTextField!
	
	@IBOutlet weak var createButton: BILGradientButton!
    @IBOutlet weak var agreenmentButton: UIButton!
    
	@IBOutlet weak var passwordInputView: BILInputView!
	@IBOutlet weak var confirmPasswordInputView: BILInputView!
	
	@IBOutlet weak var tipLabel: UILabel!
	@IBOutlet weak var agreeLabel: UILabel!
    
    var createWalletType: CreateWalletType = .new
	var mnemonic: String? {
		didSet {
            guard let m = mnemonic else { return }
            createWalletType = WalletModel.checkMnemonicIsExists(m: m) ? .resetPassword : .recover
		}
	}
	var mnemonicHash: String?
    var walletID: String?
	
    override func viewDidLoad() {
        super.viewDidLoad()

        // Do any additional setup after loading the view.
    }
	
	override func languageDidChanged() {
		super.languageDidChanged()
		let titleString = createWalletType.titleString()
		title = "\(titleString.capitalized)\(String.newWallet_create_wallet)"
		let to = BILSettingManager.currentLanguage == .en ? " to " : ""
		createButton.setTitle("\(String.newWallet_create_begin)\(to)\(titleString)", for: .normal)
		
		let font = BILSettingManager.currentLanguage == .en ? UIFont.italicSystemFont(ofSize: 14) : UIFont.systemFont(ofSize: 14, weight: .medium)
		agreenmentButton.setAttributedTitle(NSAttributedString(string: .newWallet_create_agreementTitle, attributes: [.font: font, .underlineStyle: NSUnderlineStyle.styleSingle.rawValue, .foregroundColor: UIColor.white]), for: .normal)
		
		passwordInputView.updateTitleString("Create wallet password".bil_ui_localized)
		passwordInputView.textField.placeholder = "Please input 6-20 characters".bil_ui_localized
		
		confirmPasswordInputView.updateTitleString("Confirm the wallet password".bil_ui_localized)
		confirmPasswordInputView.textField.placeholder = "Please input".bil_ui_localized
		tipLabel.text = "Password tip".bil_ui_localized
		createButton.setTitle("Complete".bil_ui_localized, for: .normal)
		agreeLabel.text = "Complete means agreed".bil_ui_localized
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
		performSegue(withIdentifier: "BILCreateWalletSuccessSegue", sender: nil)
	}
	
	func checkPassword() -> String? {
        
        guard let pwd = passwordTextField.text else {
            return .newWallet_create_pwdInput
        }
        
        var toReturn: String? = nil
        switch pwd.count {
        case 0:
            toReturn = .newWallet_create_pwdInput
        case let i where i > 20: fallthrough
        case 1...5:
            toReturn = .newWallet_create_pwdRange
        default: ()
        }
        
		return toReturn
	}
	
	func checkConfirmPassword() -> Bool {
		if let confirmPwd = confirmPasswordTextField.text, confirmPwd.count <= 20 && confirmPwd.count >= 6, confirmPwd == passwordTextField.text {
			return true
		}
		return false
	}
    
	func createWallet() {
        
        let pwdError = checkPassword()
        guard pwdError == nil else {
            passwordTextField.becomeFirstResponder()
            passwordInputView.show(tip: pwdError!, type: .error)
            return
        }
		
		guard checkConfirmPassword() else {
            confirmPasswordTextField.becomeFirstResponder()
			confirmPasswordInputView.show(tip: .newWallet_create_pwdNotEqual, type: .error)
			return
		}
		
        view.endEditing(true)
        self.bil_showLoading()
        
		getMnemonic { (m) in
            func cleanUp(wallet: WalletModel?, error: String) {
                self.bil_showError(status: error)
                debugPrint(error)
                if let w = wallet {
                    do {
                        try BILWalletManager.shared.remove(wallet: w)
                    } catch {
                        self.bil_showError(status: .newWallet_create_failed)
                    }
                }
            }
            guard let pwd = self.passwordTextField.text else {
                self.bil_showError(status: .newWallet_create_pwdEmpty)
                return
            }
            
            var localWallet = WalletModel.fetch(mnemonicHash: m.md5())
            
            if localWallet == nil {
                localWallet = BILWalletManager.shared.newWallet()
            }
            
            guard let wallet = localWallet else {
                return
            }
            
            wallet.id = self.walletID
            wallet.resetProperties(m: m, pwd: pwd, needSave:false, success: { (w) in
                func successFromSever(result: [String: Any]) {
                    do {
                        try BILWalletManager.shared.saveWallets()
						BILWalletManager.shared.loadBlockHeightAndWalletVersion()
                        NotificationCenter.default.post(name: .walletCountDidChanged, object: nil)
                        self.mnemonicHash = wallet.mnemonicHash
                        self.createSuccess()
                        self.bil_dismissHUD()
                    } catch {
                        cleanUp(wallet: wallet, error: error.localizedDescription)
                    }
                }
                switch self.createWalletType {
                case .new:
                    wallet.createWalletToServer(success: { (result) in
                        successFromSever(result: result)
                    }, failure: { (msg, code) in
                        cleanUp(wallet: wallet, error: msg)
                    })
                case .recover:
                    wallet.importWalletToServer(success: { (result) in
                        wallet.isNeedBackup = false
                        successFromSever(result: result)
                    }, failure: { (msg, code) in
                        cleanUp(wallet: wallet, error: msg)
                    })
                case .resetPassword:
                    successFromSever(result: [:])
                }
            }, failure: { (errorMsg) in
                self.bil_showError(status: errorMsg)
            })
		}
		
	}
	
	func getMnemonic(complete: @escaping (_ mnemoic: String) -> Void) {
		if let m = mnemonic {
			complete(m)
		}
		else {
            let lan: BitcoinJSBridge.Language = (BILSettingManager.currentLanguage == .zh_cn) ? .chinese : .english
			BitcoinJSBridge.shared.generateMnemonic(language: lan, success: { (mnemonic) in
                let str = mnemonic as! String
                self.mnemonic = str
                self.createWalletType = .new
				complete(str)
			}) { (error) in
				self.bil_showError(status: error.localizedDescription)
				debugPrint(error)
			}
		}
	}
	
	// MARK: - Delegates
	
	func textFieldShouldReturn(_ textField: UITextField) -> Bool {
		switch textField {
		case passwordTextField:
            let pwdError = checkPassword()
            guard pwdError == nil else {
                passwordInputView.show(tip: pwdError!, type: .error)
                return false
            }
			confirmPasswordTextField.becomeFirstResponder()
		case confirmPasswordTextField:
			createWallet()
		default: ()
		}
		return true
	}
	
	// MARK: - Actions
	@IBAction func createWalletAction(_ sender: Any) {
		createWallet()
	}
	
	@objc
    func textFieldValueDidChange(notification: Notification) {
		if let textField: UITextField = notification.object as? UITextField {
			switch textField {
			case passwordTextField:
				passwordStrengthView.strength = BILPasswordStrengthView.caculatePasswordStrength(pwd: textField.text ?? "")
				passwordInputView.show(tip: .newWallet_create_createPwd, type: .normal)
			case confirmPasswordTextField:
				confirmPasswordInputView.show(tip: .newWallet_create_confirmPwd, type: .normal)
			default: ()
			}
		}
	}
	
    // MARK: - Navigation

    // In a storyboard-based application, you will often want to do a little preparation before navigation
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        // Get the new view controller using segue.destinationViewController.
        // Pass the selected object to the new view controller.
		if segue.identifier == "BILCreateWalletSuccessSegue" {
			let cont = segue.destination as! BILCreateWalletSuccessController
            cont.createWalletType = createWalletType
			cont.mnemonicHash = mnemonicHash
		}
    }

}
