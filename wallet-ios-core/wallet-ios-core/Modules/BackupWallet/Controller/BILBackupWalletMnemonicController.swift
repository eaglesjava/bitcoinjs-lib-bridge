//
//  BILBackupWalletController.swift
//  wallet-ios-core
//
//  Created by 仇弘扬 on 2017/11/16.
//  Copyright © 2017年 BitBill. All rights reserved.
//

import UIKit
import CoreData
import CryptoSwift

class BILBackupWalletMnemonicController: BILBaseViewController {

	@IBOutlet weak var secureLabel: UILabel!
	@IBOutlet weak var snapshotTipLabel: UILabel!
	@IBOutlet weak var nextButton: BILGradientButton!
	@IBOutlet weak var mnemonicView: BILMnemonicView!
	var wallet: WalletModel?
	var mnemonicHash: String? {
		didSet {
			wallet = WalletModel.fetch(mnemonicHash: mnemonicHash)
		}
	}
	
    var mnemonic = "" {
        didSet {
            mnemonicView.mnemonic = mnemonic
        }
    }
	
	override func viewDidLoad() {
        super.viewDidLoad()

        // Do any additional setup after loading the view.
		mnemonicView.emptyTitle = .backupWallet_mnemonic_emptyTitle
        NotificationCenter.default.addObserver(self, selector: #selector(showAlertForSnapshot), name: .UIApplicationUserDidTakeScreenshot, object: nil)
    }
	
	override func languageDidChanged() {
		super.languageDidChanged()
		title = "Save words".bil_ui_localized
		secureLabel.text = "Mnemonic tip".bil_ui_localized
		snapshotTipLabel.text = "Snapshot tip".bil_ui_localized
		nextButton.setTitle("I have saved them orderly".bil_ui_localized, for: .normal)
	}
    
    deinit {
        NotificationCenter.default.removeObserver(self, name: .UIApplicationUserDidTakeScreenshot, object: nil)
    }
    
	@IBAction func cancelAction(_ sender: Any) {
		dismiss(animated: true, completion: nil)
	}
	@objc
    func showAlertForSnapshot() {
        showTipAlert(msg: .backupWallet_mnemonic_snapshotTip)
    }
	
	override func viewDidAppear(_ animated: Bool) {
		super.viewDidAppear(animated)
		
		if let _ = wallet {
			showAlertForPassword()
		}
	}
	
    func showAlertForFail(_ msg: String = .backupWallet_mnemonic_tryLater) {
		let alert = UIAlertController(title: .backupWallet_mnemonic_failed, message: msg, preferredStyle: .alert)
		
		let ok = UIAlertAction(title: .backupWallet_mnemonic_confirm, style: .default) { (action) in
			BILControllerManager.shared.showMainTabBarController()
		}
		alert.addAction(ok)
		
		present(alert, animated: true, completion: nil)
	}
	
	func showAlertForPassword() {
		let alert = UIAlertController(title: .backupWallet_mnemonic_backupWallet, message: nil, preferredStyle: .alert)
		
		let ok = UIAlertAction(title: .backupWallet_mnemonic_confirm, style: .default) { (action) in
			guard let pwd = alert.textFields?.first?.text else {
				self.showAlertForFail()
				return
			}
			self.decryptMnemonic(pwd: pwd)
		}
		let cancel = UIAlertAction(title: .backupWallet_mnemonic_cancel, style: .cancel) { (action) in
			BILControllerManager.shared.showMainTabBarController()
		}
		alert.addAction(ok)
		alert.addAction(cancel)
		
		alert.addTextField { (textField) in
			textField.placeholder = .backupWallet_mnemonic_inputPwd
			textField.isSecureTextEntry = true
		}
		
		present(alert, animated: true, completion: nil)
	}
	
	func decryptMnemonic(pwd: String) {
        guard let w = self.wallet else {
            self.showAlertForFail(.backupWallet_mnemonic_getWalletFailed)
            return
        }
        guard let m = w.decryptMnemonic(pwd: pwd) else {
            self.showAlertForFail(.backupWallet_mnemonic_decodeWalletFailed)
            return
        }
        mnemonic = m
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
		if segue.identifier == "BILVerifyMnemonic" {
			let cont = segue.destination as! BILVerifyMnemonicController
			cont.dataArray = mnemonicView.dataArray
			cont.wallet = wallet
            mnemonic = ""
		}
    }
	

}
