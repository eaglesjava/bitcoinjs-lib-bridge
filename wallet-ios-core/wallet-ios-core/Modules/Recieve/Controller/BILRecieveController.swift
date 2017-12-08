//
//  BILRecieveController.swift
//  wallet-ios-core
//
//  Created by 仇弘扬 on 2017/12/7.
//  Copyright © 2017年 BitBill. All rights reserved.
//

import UIKit

class BILRecieveController: BILBaseViewController {

	@IBOutlet weak var backupViewHeight: NSLayoutConstraint!
	@IBOutlet weak var qrCodeImageView: UIImageView!
    @IBOutlet weak var addressLabel: UILabel!
    var currentWallet: WalletModel?
    @IBOutlet weak var currentWalletIDLabel: UILabel!
    @IBOutlet weak var currentWalletBalanceLabel: UILabel!
    @IBOutlet weak var currentWalletShortIDLabel: UILabel!
	
	var recieveModel: BILRecieveModel?
    
    override func viewDidLoad() {
        super.viewDidLoad()

        // Do any additional setup after loading the view.
        currentWallet = BILWalletManager.shared.wallets.first
        
        if let w = currentWallet {
            currentWalletIDLabel.text = w.id
            currentWalletBalanceLabel.text = w.btc_balanceString + " btc"
            currentWalletShortIDLabel.text = "\(w.id?.first ?? "B")"
			
			backupViewHeight.constant = w.isNeedBackup ? 40 : 0
            w.lastBTCAddress(success: { (address) in
                self.setAddress(address: address)
            }, failure: { (errorMsg) in
                debugPrint(errorMsg)
            })
        }
    }
	
	func setAddress(address: String) {
		recieveModel = BILRecieveModel(address: address, volume: "")
		let scale = UIScreen.main.scale
		let size = qrCodeImageView.frame.size.applying(CGAffineTransform(scaleX: scale, y: scale))
		qrCodeImageView.image = BILQRCodeHelper.generateQRCode(msg: "bitcoin:\(address)", targetSize: size)
		addressLabel.text = address
	}
    
    override func viewDidAppear(_ animated: Bool) {
        super.viewDidAppear(animated)
        showTipAlert(title: "友情提醒", msg: "为保护您的隐私，每次转入操作时，都将使用新地址，已使用的旧地址仍然可用")
    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
	
	@IBAction func generateNewAddress(_ sender: Any) {
		currentWallet?.getNewBTCAddress(success: { (address) in
			self.setAddress(address: address)
		}, failure: { (errorMsg) in
			debugPrint(errorMsg)
		})
	}
	// MARK: - Navigation
	
	override func shouldPerformSegue(withIdentifier identifier: String, sender: Any?) -> Bool {
		guard let _ = currentWallet else { return false }
		return true
	}

    // In a storyboard-based application, you will often want to do a little preparation before navigation
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        // Get the new view controller using segue.destinationViewController.
        // Pass the selected object to the new view controller.
		if segue.identifier == "BILRecieveToBackUpWallet" {
			
		}
		guard let id = segue.identifier else { return }
		switch id {
		case "BILRecieveToBackUpWallet":
			if let cont = (segue.destination as? UINavigationController)?.viewControllers.first as? BILBackupWalletMnemonicController {
				if let wallet = currentWallet {
					cont.mnemonicHash = wallet.mnemonicHash
				}
			}
		case "BILToSpecificRecieveSegue":
			let cont = segue.destination as! BILSpecificVolumeRecieveInputController
			cont.recieveModel = recieveModel
		default:
			()
		}
    }

}
