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
    var createWalletType: CreateWalletType = .new
	
    @IBOutlet weak var titleLabel: UILabel!
    @IBOutlet weak var resultTitleLabel: UILabel!
    @IBOutlet weak var msgLabel: UILabel!
    @IBOutlet weak var backupButton: BILGradientButton!
    @IBOutlet weak var backupLaterButton: UIButton!
    @IBOutlet weak var okButton: BILGradientButton!
    
    override func viewDidLoad() {
        super.viewDidLoad()

        // Do any additional setup after loading the view.
    }
	
	override func languageDidChanged() {
		super.languageDidChanged()
		titleLabel.text = "\(createWalletType.titleString().capitalized)\(String.newWallet_create_wallet)"
		resultTitleLabel.text = "\(createWalletType.titleString().capitalized)\(String.newWallet_createSuccess)"
		switch createWalletType {
		case .new:
			msgLabel.text = .newWallet_createSuccess_new
		case .recover:
			msgLabel.text = .newWallet_createSuccess_recover
			backupButton.isHidden = true
			backupLaterButton.isHidden = true
			okButton.isHidden = false
		case .resetPassword:
			msgLabel.text = .newWallet_createSuccess_reset
			backupButton.isHidden = true
			backupLaterButton.isHidden = true
			okButton.isHidden = false
		}
		backupButton.setTitle("Back up wallet".bil_ui_localized, for: .normal)
		backupLaterButton.setTitle("Back up later".bil_ui_localized, for: .normal)
		okButton.setTitle("Confirm".bil_ui_localized, for: .normal)
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
