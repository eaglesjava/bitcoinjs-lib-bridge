//
//  BILBackupWalletSuccessController.swift
//  wallet-ios-core
//
//  Created by 仇弘扬 on 2017/11/17.
//  Copyright © 2017年 BitBill. All rights reserved.
//

import UIKit

class BILBackupWalletSuccessController: BILBaseViewController {

	@IBOutlet weak var titleLabel: UILabel!
	@IBOutlet weak var resultLabel: UILabel!
	@IBOutlet weak var tipLabel: UILabel!
	@IBOutlet weak var doneButton: BILGradientButton!
	override func viewDidLoad() {
        super.viewDidLoad()

        // Do any additional setup after loading the view.
    }

	override func languageDidChanged() {
		super.languageDidChanged()
		title = "Congratulations".bil_ui_localized
		titleLabel.text = "Back up wallet".bil_ui_localized
		resultLabel.text = "Succeed to back up".bil_ui_localized
		tipLabel.text = "Please keep your mnemonic words carefully".bil_ui_localized
		doneButton.setTitle("Complete".bil_ui_localized, for: .normal)
	}
	
    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
	
	@IBAction func doneAction(_ sender: Any) {
		BILControllerManager.shared.showMainTabBarController()
	}

    /*
    // MARK: - Navigation

    // In a storyboard-based application, you will often want to do a little preparation before navigation
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        // Get the new view controller using segue.destinationViewController.
        // Pass the selected object to the new view controller.
    }
    */

}
