//
//  BILVerifyMnemonicController.swift
//  wallet-ios-core
//
//  Created by 仇弘扬 on 2017/11/16.
//  Copyright © 2017年 BitBill. All rights reserved.
//

import UIKit

class BILVerifyMnemonicController: BILBaseViewController, BILMnemonicViewDelegate {

	@IBOutlet weak var randomMnemonicView: BILMnemonicView!
    @IBOutlet weak var randomHeight: NSLayoutConstraint!
    @IBOutlet weak var verifyMnemonicView: BILMnemonicView!
    @IBOutlet weak var verifyHeight: NSLayoutConstraint!
    @IBOutlet weak var tipLabel: UILabel!
	@IBOutlet weak var clearItem: UIBarButtonItem!
	@IBOutlet weak var confirmButton: BILGradientButton!
	
	var wallet: WalletModel?
	
	var randomArray = [String]()
	var dataArray = [String]() {
		didSet {
			let temp = [String](dataArray)
			randomArray = temp.shuffle()
		}
	}
	
	override func viewDidLoad() {
        super.viewDidLoad()

        // Do any additional setup after loading the view.
		randomMnemonicView.dataArray = randomArray
		verifyMnemonicView.emptyTitle = .backupWallet_verify_emptyTitle
        verifyMnemonicView.collectionView.allowsSelection = false
        DispatchQueue.main.async {
            let height = self.randomMnemonicView.collectionView.contentSize.height
            self.randomHeight.constant = height + 50.0
            self.view.layoutIfNeeded()
        }
    }
	
	override func languageDidChanged() {
		super.languageDidChanged()
		title = "Verify words".bil_ui_localized
		tipLabel.text = "Click on the mnemonic words orderly to confirm whether your backup is right.".bil_ui_localized
		clearItem.title = "Clear".bil_ui_localized
		confirmButton.setTitle("Confirm".bil_ui_localized, for: .normal)
	}

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
	
	func selectedMnemonicArrayDidChange(mnemonicView: BILMnemonicView, currentArray: [String]) {
        if mnemonicView == randomMnemonicView {
            debugPrint(self.verifyMnemonicView.collectionView.contentSize.height)
            verifyMnemonicView.dataArray = currentArray
            DispatchQueue.main.asyncAfter(deadline: .now() + DispatchTimeInterval.milliseconds(1), execute: {
                debugPrint(self.verifyMnemonicView.collectionView.contentSize.height)
                let height = self.verifyMnemonicView.collectionView.contentSize.height
                self.verifyHeight.constant = max(height + 50.0, 125.0)
                self.view.layoutIfNeeded()
            })
        }
	}

    @IBAction func clearAction(_ sender: Any) {
        randomMnemonicView.dataArray = randomArray
        verifyMnemonicView.dataArray = []
    }
    
    @IBAction func verifyMnemonicAction(_ sender: Any) {
		if verifyMnemonicView.dataArray == dataArray {
			performSegue(withIdentifier: "BILBackUpSuccessSugue", sender: nil)
			wallet?.isNeedBackup = false
			wallet?.save()
		}
		else
		{
			showAlertForFail()
		}
	}
	
	func showAlertForFail() {
		let alert = UIAlertController(title: .backupWallet_mnemonic_failed, message: .newWallet_import_checkAgain, preferredStyle: .alert)
		
		let ok = UIAlertAction(title: .backupWallet_mnemonic_confirm, style: .default, handler: nil)
		let cancel = UIAlertAction(title: .backupWallet_mnemonic_cancel, style: .cancel, handler: nil)
		alert.addAction(ok)
		alert.addAction(cancel)
		
		present(alert, animated: true, completion: nil)
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
