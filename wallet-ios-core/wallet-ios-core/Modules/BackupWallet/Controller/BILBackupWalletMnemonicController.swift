//
//  BILBackupWalletController.swift
//  wallet-ios-core
//
//  Created by 仇弘扬 on 2017/11/16.
//  Copyright © 2017年 BitBill. All rights reserved.
//

import UIKit

class BILBackupWalletMnemonicController: UIViewController {

	@IBOutlet weak var mnemonicView: BILMnemonicView!
	var mnemonic: String?
	
	override func viewDidLoad() {
        super.viewDidLoad()

        // Do any additional setup after loading the view.
		mnemonic = "请 输 入 密 码 以 确 认 码 以 确 认"
		mnemonicView.mnemonic = mnemonic!
    }
	
	override func viewDidLayoutSubviews() {
		super.viewDidLayoutSubviews()
		bil_setBackgroudColor()
	}
	
	override func viewDidAppear(_ animated: Bool) {
		super.viewDidAppear(animated)
		
		if mnemonic == nil {
			showAlertForPassword()
		}
		
	}
	
	func showAlertForPassword() {
		let alert = UIAlertController(title: "备份钱包", message: nil, preferredStyle: .alert)
		
		let ok = UIAlertAction(title: "确认", style: .default, handler: nil)
		let cancel = UIAlertAction(title: "取消", style: .cancel, handler: nil)
		alert.addAction(ok)
		alert.addAction(cancel)
		
		alert.addTextField { (textField) in
			textField.placeholder = "请输入密码以确认"
		}
		
		present(alert, animated: true, completion: nil)
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
		}
    }
	

}
