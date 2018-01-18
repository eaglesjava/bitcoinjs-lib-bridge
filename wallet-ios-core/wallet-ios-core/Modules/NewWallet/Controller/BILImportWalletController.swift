//
//  BILImportWalletController.swift
//  wallet-ios-core
//
//  Created by 仇弘扬 on 2017/11/21.
//  Copyright © 2017年 BitBill. All rights reserved.
//

import UIKit
import SVProgressHUD
import IQKeyboardManagerSwift

class BILImportWalletController: BILBaseViewController, UITextViewDelegate {

	@IBOutlet weak var textView: IQTextView!
	@IBOutlet weak var mnemonicView: BILMnemonicView!
	
	let sugueID = "BILMnemonicToWalletIDSegue"
    let resetSegueID = "BILMnemonicToResetPasswordSegue"
	@IBOutlet weak var tipLabel: UILabel!
	@IBOutlet weak var nextButton: BILGradientButton!
    @IBOutlet weak var cancelItem: UIBarButtonItem?
    
	override func viewDidLoad() {
        super.viewDidLoad()

        // Do any additional setup after loading the view.
    }
	
	override func languageDidChanged() {
		super.languageDidChanged()
		title = "Input mnemonic words".bil_ui_localized
		tipLabel.text = "Supports BIP39 mnemonic words only".bil_ui_localized
		self.mnemonicView.emptyTitle = .newWallet_import_emptyTitle
		nextButton.setTitle("Next".bil_ui_localized, for: .normal)
        cancelItem?.title = "Cancel".bil_ui_localized
	}
	
	override func viewWillAppear(_ animated: Bool) {
		super.viewWillAppear(animated)
		if presentationController == nil {
			navigationItem.rightBarButtonItem = nil
		}
	}
	
	override func viewDidAppear(_ animated: Bool) {
		super.viewDidAppear(animated)
//        textView.text = "两 览 藏 微 储 继 料 叶 历 跳 语 握"
		textView.becomeFirstResponder()
	}

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    
    func normalized(mnemonic: String) -> String? {
        var trimmedString = mnemonic.trimmingCharacters(in: .whitespaces)
        do {
            let regex = try NSRegularExpression(pattern: "  +", options: .caseInsensitive)
            let str = NSMutableString(string: trimmedString)
            _ = regex.replaceMatches(in: str, options: .reportCompletion, range: NSMakeRange(0, trimmedString.count), withTemplate: " ")
            trimmedString = String(str)
        } catch {
            debugPrint(error)
            return nil
        }
        let words = trimmedString.components(separatedBy: " ")
        
        return words.joined(separator: " ")
    }
    
	@IBAction func nextAction(_ sender: Any) {
        
        let alertTitle = String.newWallet_import_failed
        let alertMsg = String.newWallet_import_checkAgain
        
        guard let text = textView.text, !text.isEmpty else {
            self.textView.becomeFirstResponder()
            self.mnemonicView.layer.borderColor = UIColor(hex: 0xFD6D73).cgColor
            return
        }
        
        guard textView.text.contains(" ") else {
            showTipAlert(title: alertTitle, msg: .newWallet_import_spaceTip, dismissed: nil)
            return
        }
        
        guard let mnemonic = normalized(mnemonic: textView.text) else {
            showTipAlert(title: alertTitle, msg: alertMsg, dismissed: nil)
            return
        }
        
        func segueSender(mnemonic: String, walletID: String?) -> (mnemonic: String, walletID: String?) {
            return (mnemonic, walletID)
        }
        
        BitcoinJSBridge.shared.validateMnemonic(mnemonic: mnemonic, success: { (result) in
            let isValidate = result as! Bool
            if isValidate {
                if let wallet = WalletModel.fetch(mnemonicHash: mnemonic.md5()) {
                    debugPrint("钱包已存在在本地")
                    let alert = UIAlertController(title: .newWallet_import_exits, message: .newWallet_import_resetOrNot, preferredStyle: .alert)
                    alert.addAction(UIAlertAction(title: .meMe_cancel, style: .cancel, handler: { (action) in
                        self.bil_dismissSelfModalViewController()
                    }))
                    alert.addAction(UIAlertAction(title: .newWallet_import_reset, style: .destructive, handler: { (action) in
                        self.performSegue(withIdentifier: self.resetSegueID, sender: segueSender(mnemonic: mnemonic, walletID: wallet.id))
                    }))
                    self.present(alert, animated: true)
                }
                else
                {
                    self.bil_showLoading()
                    BitcoinJSBridge.shared.getMasterXPublicKey(mnemonic: mnemonic, success: { (pubKey) in
                        WalletModel.getWalletIDFromSever(mainExtPublicKey: pubKey as! String, success: { (id) in
                            self.performSegue(withIdentifier: self.sugueID, sender: segueSender(mnemonic: mnemonic, walletID: id))
                            self.bil_dismissHUD()
                        }, failure: { (errorMsg, code) in
                            self.bil_dismissHUD()
                            self.showTipAlert(title: alertTitle, msg: errorMsg)
                        })
                    }, failure: { (error) in
                        self.bil_dismissHUD()
                        self.performSegue(withIdentifier: self.sugueID, sender: segueSender(mnemonic: mnemonic, walletID: nil))
                    })
                }
            }
            else
            {
                print("不是合法的助记词")
                self.showTipAlert(title: alertTitle, msg: alertMsg)
            }
        }) { (error) in
            print("校验助记词失败")
            self.showTipAlert(title: alertTitle, msg: alertMsg)
        }
        
	}
	
    func textViewDidChange(_ textView: UITextView) {
        mnemonicView.layer.borderColor = UIColor(white: 1.0, alpha: 1.0).cgColor
    }
    
	public func textViewDidBeginEditing(_ textView: UITextView) {
		mnemonicView.emptyTitle = nil
        mnemonicView.layer.borderColor = UIColor(white: 1.0, alpha: 1.0).cgColor
        self.textView.placeholder = String.newWallet_import_12WordsTip
	}
	
	public func textViewDidEndEditing(_ textView: UITextView) {
		guard let text = textView.text, text.count == 0 else {
			return
		}
		mnemonicView.emptyTitle = .newWallet_import_emptyTitle
        self.textView.placeholder = nil
        resetMnemonicViewBorderColor()
	}
	
    func resetMnemonicViewBorderColor() {
        mnemonicView.layer.borderColor = UIColor(white: 1.0, alpha: 0.3).cgColor
    }
    
    // MARK: - Navigation

    // In a storyboard-based application, you will often want to do a little preparation before navigation
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        // Get the new view controller using segue.destinationViewController.
        // Pass the selected object to the new view controller.
		if segue.identifier == sugueID {
			let cont = segue.destination as! BILInputWalletIDController
            cont.navigationItem.rightBarButtonItem = nil
            guard let s = sender as? (mnemonic: String, walletID: String?) else {
                return
            }
			cont.mnemonic = s.mnemonic
            cont.walletID = s.walletID
		}
        
        if segue.identifier == resetSegueID {
            let cont = segue.destination as! BILCreateWalletViewController
            cont.navigationItem.rightBarButtonItem = nil
            guard let s = sender as? (mnemonic: String, walletID: String?) else {
                return
            }
            cont.mnemonic = s.mnemonic
            cont.walletID = s.walletID
        }
    }

}
