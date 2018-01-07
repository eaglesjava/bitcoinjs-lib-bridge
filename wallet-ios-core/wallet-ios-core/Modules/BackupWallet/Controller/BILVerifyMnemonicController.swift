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
	@IBOutlet weak var verifyMnemonicView: BILMnemonicView!
	
	var wallet: WalletModel?
	
	var randomArray = [String]()
	var dataArray = [String]() {
		didSet {
			var arr = [String]()
			var temp = [String](dataArray)
			for _ in 0..<temp.count {
				arr.append(temp.remove(at: Int(arc4random()) % temp.count))
			}
			randomArray = arr
		}
	}
	
	override func viewDidLoad() {
        super.viewDidLoad()

        // Do any additional setup after loading the view.
		randomMnemonicView.dataArray = randomArray
		verifyMnemonicView.emptyTitle = .backupWallet_verify_emptyTitle
        verifyMnemonicView.collectionView.allowsSelection = false
    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
	
	func selectedMnemonicArrayDidChange(mnemonicView: BILMnemonicView, currentArray: [String]) {
		verifyMnemonicView.dataArray = currentArray
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
