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

class BILBackupWalletMnemonicController: UIViewController {

	@IBOutlet weak var mnemonicView: BILMnemonicView!
	var wallet: WalletModel?
	var mnemonicHash: String? {
		didSet {
			wallet = WalletModel.fetch(by: mnemonicHash)
		}
	}
	
	var mnemonic = ""
	
	override func viewDidLoad() {
        super.viewDidLoad()

        // Do any additional setup after loading the view.
		mnemonicView.emptyTitle = "请输入密码解锁助记词"
    }
	
	override func viewDidLayoutSubviews() {
		super.viewDidLayoutSubviews()
		bil_setBackgroudColor()
	}
	
	override func viewDidAppear(_ animated: Bool) {
		super.viewDidAppear(animated)
		
		if let _ = wallet {
			showAlertForPassword()
		}
	}
	
	func showAlertForFail() {
		let alert = UIAlertController(title: "备份失败", message: "请稍后再试", preferredStyle: .alert)
		
		let ok = UIAlertAction(title: "确认", style: .default) { (action) in
			BILControllerManager.shared.showMainTabBarController()
		}
		alert.addAction(ok)
		
		present(alert, animated: true, completion: nil)
	}
	
	func showAlertForPassword() {
		let alert = UIAlertController(title: "备份钱包", message: nil, preferredStyle: .alert)
		
		let ok = UIAlertAction(title: "确认", style: .default) { (action) in
			guard let pwd = alert.textFields?.first?.text else {
				self.showAlertForFail()
				return
			}
			self.decryptMnemonic(pwd: pwd)
		}
		let cancel = UIAlertAction(title: "取消", style: .cancel) { (action) in
			BILControllerManager.shared.showMainTabBarController()
		}
		alert.addAction(ok)
		alert.addAction(cancel)
		
		alert.addTextField { (textField) in
			textField.placeholder = "请输入密码以确认"
			textField.isSecureTextEntry = true
		}
		
		present(alert, animated: true, completion: nil)
	}
	
	func decryptMnemonic(pwd: String) {
		do {
			guard let w = self.wallet else {
				self.showAlertForFail()
				return
			}
			let key = String(pwd.sha256().prefix(32))
			let aes = try AES(key: key, iv: String(key.reversed().prefix(16)))
			
			if let mnemonic = String(bytes: try aes.decrypt((w.encryptedMnemonic?.ck_mnemonicData().bytes)!), encoding: .utf8), mnemonic.md5() == self.mnemonicHash {
				self.mnemonic = mnemonic
				self.mnemonicView.mnemonic = mnemonic
			}
			else
			{
				self.showAlertForFail()
			}
		} catch {
			print(error)
			self.showAlertForFail()
		}
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
		}
    }
	

}
