//
//  BILRecieveController.swift
//  wallet-ios-core
//
//  Created by 仇弘扬 on 2017/12/7.
//  Copyright © 2017年 BitBill. All rights reserved.
//

import UIKit

class BILRecieveController: BILBaseViewController {
    @IBOutlet weak var qrCodeImageViewHeight: NSLayoutConstraint!
    
	@IBOutlet var chooseWalletContainerView: UIView!
	@IBOutlet weak var backupViewHeight: NSLayoutConstraint!
	@IBOutlet weak var qrCodeImageView: UIImageView!
    @IBOutlet weak var addressLabel: UILabel!
    @IBOutlet weak var currentWalletIDLabel: UILabel!
    @IBOutlet weak var currentWalletBalanceLabel: UILabel!
    @IBOutlet weak var currentWalletShortIDLabel: UILabel!
    
    var currentWallet: WalletModel? {
        didSet {
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
    }
	
	var recieveModel: BILRecieveModel?
    
    lazy var qrCodeHeight: CGFloat = {
        return min(180, UIScreen.main.bounds.height - 388 - 88)
    }()
    
    override func viewDidLoad() {
        super.viewDidLoad()

        // Do any additional setup after loading the view.
        currentWallet = BILWalletManager.shared.wallets.first
		performSegue(withIdentifier: "BILRecieveChooseWalletSegue", sender: nil)
		
		let tap = UITapGestureRecognizer(target: self, action: #selector(tapped(sender:)))
		bgView.addGestureRecognizer(tap)
        
        qrCodeImageViewHeight.constant = min(160, UIScreen.main.bounds.height - 388 - 88)
    }
	
	func setAddress(address: String) {
		recieveModel = BILRecieveModel(address: address, volume: "")
		let scale = UIScreen.main.scale
		let size = CGSize(width: qrCodeHeight, height: qrCodeHeight).applying(CGAffineTransform(scaleX: scale, y: scale))
		qrCodeImageView.image = BILQRCodeHelper.generateQRCode(msg: "bitcoin:\(address)", targetSize: size)
		addressLabel.text = address
	}
    
    override func viewDidAppear(_ animated: Bool) {
        super.viewDidAppear(animated)
		
		let key = "BILShowTipForWalletNewAddress"
		let shown = UserDefaults.standard.bool(forKey: key)
		if !shown {
			showTipAlert(title: "友情提醒", msg: "为保护您的隐私，每次接收操作时，都将使用新地址，已使用的旧地址仍然可用")
			UserDefaults.standard.set(true, forKey: key)
		}
    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
	
	@objc
	func tapped(sender: UITapGestureRecognizer) {
		print(sender.state)
		switch sender.state {
		case .ended:
			hideChooseWalletView()
		default: ()
		}
	}
	
	@IBAction func currentWalletViewTapped(_ sender: Any) {
		showChooseWalletView()
	}
	
	private let container = UIView(frame: UIScreen.main.bounds)
	private let bgView = UIView(frame: UIScreen.main.bounds)
	
	func showChooseWalletView() {
		
		let screenSize = UIScreen.main.bounds.size
		
		var bottom: CGFloat = 49.0
		if #available(iOS 11.0, *) {
			bottom = view.safeAreaInsets.bottom
		}
		container.frame.size.height = screenSize.height - bottom
		container.clipsToBounds = true
		
		bgView.backgroundColor = UIColor(white: 0, alpha: 0.7)
		bgView.alpha = 0
		bgView.frame = container.bounds
		container.addSubview(bgView)
		
		let height = min(BILWalletManager.shared.wallets.count, 3) * 85 + 50 - 1
		chooseWalletContainerView.frame = CGRect(x: 0, y: Int(container.bounds.height), width: Int(screenSize.width), height: height)
		container.addSubview(chooseWalletContainerView)
		view.addSubview(container)
		
		var targetFrame = chooseWalletContainerView.frame
		targetFrame.origin.y = screenSize.height - targetFrame.height - bottom
		
		UIView.animate(withDuration: 0.25) {
			self.chooseWalletContainerView.frame = targetFrame
			self.bgView.alpha = 1
		}
	}
	
	func hideChooseWalletView() {
		let screenSize = UIScreen.main.bounds.size
		var targetFrame = chooseWalletContainerView.frame
		targetFrame.origin.y = screenSize.height
		UIView.animate(withDuration: 0.25, animations: {
			self.chooseWalletContainerView.frame = targetFrame
			self.bgView.alpha = 0
		}) { (finished) in
			self.container.subviews.first?.removeFromSuperview()
			self.container.removeFromSuperview()
			self.bgView.removeFromSuperview()
			self.container.removeFromSuperview()
		}
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
		case "BILRecieveChooseWalletSegue":
			let cont = segue.destination as! BILChooseWalletController
			unowned let s = self
			cont.setDidSelecteWalletClosure(onSelected: { (wallet) in
				s.currentWallet = wallet
				s.hideChooseWalletView()
			})
		default:
			()
		}
    }

}
