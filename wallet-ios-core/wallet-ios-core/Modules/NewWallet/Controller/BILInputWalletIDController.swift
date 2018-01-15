//
//  BILInputWalletIDController.swift
//  wallet-ios-core
//
//  Created by 仇弘扬 on 2018/1/3.
//  Copyright © 2018年 BitBill. All rights reserved.
//

import UIKit
import PopupDialog

class BILInputWalletIDController: BILBaseViewController, BILInputViewDelegate {
    
    @IBOutlet weak var walletNameTextField: ASKPlaceHolderColorTextField!
    @IBOutlet weak var walletNameInputView: BILInputView!
	@IBOutlet weak var nextButton: BILGradientButton!
	@IBOutlet weak var cancelItem: UIBarButtonItem?
	
    var createWalletType: CreateWalletType = .new
    var mnemonic: String? {
        didSet {
            guard let m = mnemonic else { return }
            createWalletType = WalletModel.checkMnemonicIsExists(m: m) ? .resetPassword : .recover
        }
    }
    
    var mnemonicHash: String?
    var walletID: String?
    
    var hasShownAlert = false

    override func viewDidLoad() {
        super.viewDidLoad()

        // Do any additional setup after loading the view.
        walletNameInputView.delegate = self
        if let w = walletID, !w.isEmpty {
            walletNameTextField.isEnabled = false
            walletNameTextField.text = w
            walletNameTextField.textColor = UIColor.bil_white_60_color
        }
    }
	
	override func languageDidChanged() {
		super.languageDidChanged()
		title = "Input wallet ID".bil_ui_localized
		walletNameInputView.updateTitleString("Wallet ID".bil_ui_localized)
		walletNameInputView.tipLabel?.text = "Wallet ID tip".bil_ui_localized
		walletNameTextField.placeholder = "6-20 characters, begin with letter.".bil_ui_localized
		nextButton.setTitle("Next".bil_ui_localized, for: .normal)
		cancelItem?.title = "Cancel".bil_ui_localized
	}
    
    override func viewWillAppear(_ animated: Bool) {
        super.viewWillAppear(animated)
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
	
	@IBAction func cancelAction(_ sender: Any) {
		dismiss(animated: true, completion: nil)
	}
    
    func showAlertForSupportedCoins() {
        guard !hasShownAlert else {
            return
        }
        hasShownAlert = true
        let buttonTitle = String.newWallet_inputID_IKnow
        
        let vc = UIViewController(nibName: "BILSupportedCoinsPopupController", bundle: nil)
        let popup = PopupDialog(viewController: vc, transitionStyle: .fadeIn, gestureDismissal: false, hideStatusBar: false) {
            
        }
        
        let button = DefaultButton(title: buttonTitle, height: 50, dismissOnTap: true) {
            
        }
        popup.addButton(button)
        
        present(popup, animated: true) {
            
        }
    }
    
    func checkWalletID() -> String? {
        let emptyTip = String.newWallet_inputID_title
        guard let walletID = walletNameTextField.text else {
            return emptyTip
        }
        var toReturn: String? = nil
        switch walletID.count {
        case 0:
            toReturn = emptyTip
            return toReturn
        case let i where i > 20: fallthrough
        case 1...5:
            toReturn = .newWallet_inputID_range
        default: ()
        }
        
        func count(string: String, pattern: String) throws -> Int {
            let exp = try NSRegularExpression(pattern: pattern, options: .allowCommentsAndWhitespace)
            let count = exp.numberOfMatches(in: string, options: .reportCompletion, range: NSMakeRange(0, string.count))
            return count
        }
        
        do {
            let numCount = try count(string: walletID, pattern: "[0-9]")
            let lowerCount = try count(string: walletID, pattern: "[a-z]")
            let upperCount = try count(string: walletID, pattern: "[A-Z]")
            let underlineCount = try count(string: walletID, pattern: "[_]")
            let otherCount = walletID.count - numCount - lowerCount - upperCount - underlineCount
            if otherCount > 0 {
                toReturn = .newWallet_inputID_format
            }
            
            if try count(string: String(walletID.first!), pattern: "[a-zA-Z]") == 0 {
                toReturn = .newWallet_inputID_prefix
            }
        } catch {
            
        }
        
        if createWalletType == .new, WalletModel.checkIDIsExists(id: walletID) {
            toReturn = .newWallet_inputID_exits
        }
        
        return toReturn
    }
    
    @objc
    func textFieldValueDidChange(notification: Notification) {
        if let textField: UITextField = notification.object as? UITextField {
            switch textField {
            case walletNameTextField:
                walletNameInputView.show(tip: .newWallet_inputID_title, type: .normal)
            default: ()
            }
        }
    }
    
    func textFieldShouldReturn(_ textField: UITextField) -> Bool {
        switch textField {
        case walletNameTextField:
            let walletIDError = checkWalletID()
            guard walletIDError == nil else {
                walletNameInputView.show(tip: walletIDError!, type: .error)
                return false
            }
            next()
        default: ()
        }
        return true
    }
    
    func next() {
        let walletIDError = checkWalletID()
        guard walletIDError == nil else {
            walletNameTextField.becomeFirstResponder()
            walletNameInputView.show(tip: walletIDError!, type: .error)
            return
        }
        if walletID != nil && !walletNameTextField.isEnabled {
            self.performSegue(withIdentifier: "BILWalletIDToPasswordSegue", sender: nil)
            return
        }
        walletID = walletNameTextField.text
        BILNetworkManager.request(request: .checkWalletID(walletID: walletID!), success: { (result) in
            self.performSegue(withIdentifier: "BILWalletIDToPasswordSegue", sender: nil)
        }) { (msg, code) in
            self.walletNameInputView.show(tip: msg, type: .error)
        }
    }
    
    @IBAction func nextAction(_ sender: Any) {
        next()
    }
    
    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    

    // MARK: - Navigation

    // In a storyboard-based application, you will often want to do a little preparation before navigation
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        // Get the new view controller using segue.destinationViewController.
        // Pass the selected object to the new view controller.
        if segue.identifier == "BILWalletIDToPasswordSegue" {
            let cont = segue.destination as! BILCreateWalletViewController
            cont.navigationItem.rightBarButtonItem = nil
            cont.mnemonic = mnemonic
            cont.walletID = walletID
        }
    }

}
