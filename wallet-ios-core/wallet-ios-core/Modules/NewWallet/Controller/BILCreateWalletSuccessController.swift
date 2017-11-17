//
//  BILCreateWalletSuccessController.swift
//  wallet-ios-core
//
//  Created by 仇弘扬 on 2017/11/17.
//  Copyright © 2017年 BitBill. All rights reserved.
//

import UIKit

class BILCreateWalletSuccessController: BILBaseViewController {

	var mnemonicHash: String?
	
    override func viewDidLoad() {
        super.viewDidLoad()

        // Do any additional setup after loading the view.
    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
	
	@IBAction func backupLaterAction(_ sender: Any) {
		BILControllerManager.shared.showMainTabBarController()
	}
	// MARK: - Navigation

    // In a storyboard-based application, you will often want to do a little preparation before navigation
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        // Get the new view controller using segue.destinationViewController.
        // Pass the selected object to the new view controller.
		if segue.identifier == "BILToBackupWalletSegue" {
			if let cont = (segue.destination as? UINavigationController)?.viewControllers.first as? BILBackupWalletMnemonicController {
				cont.mnemonicHash = mnemonicHash
			}
		}
    }
    

}
