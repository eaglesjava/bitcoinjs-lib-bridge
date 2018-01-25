//
//  BILReceiveController.swift
//  wallet-ios-core
//
//  Created by 仇弘扬 on 2017/12/7.
//  Copyright © 2017年 BitBill. All rights reserved.
//

import UIKit

class BILReceiveController: BILBaseViewController {
    @IBOutlet weak var qrCodeImageViewHeight: NSLayoutConstraint!
    
	@IBOutlet var chooseWalletContainerView: UIView!
	@IBOutlet weak var backupViewHeight: NSLayoutConstraint!
	@IBOutlet weak var qrCodeImageView: UIImageView!
    @IBOutlet weak var addressLabel: BILAddressLabel!
    @IBOutlet weak var currentWalletIDLabel: UILabel!
    @IBOutlet weak var currentWalletBalanceLabel: UILabel!
    @IBOutlet weak var currentWalletShortIDLabel: UILabel!
    @IBOutlet weak var specificButton: UIButton!
    @IBOutlet weak var backupButton: UIButton!
    
    var currentWallet: WalletModel? {
        didSet {
            refreshUI()
        }
    }
	
	var receiveModel: BILReceiveModel?
    
    lazy var qrCodeHeight: CGFloat = {
        return min(150, UIScreen.main.bounds.height - 388 - 88)
    }()
    
    override func viewDidLoad() {
        super.viewDidLoad()

        // Do any additional setup after loading the view.
        currentWallet = BILWalletManager.shared.wallets.first
		performSegue(withIdentifier: "BILReceiveChooseWalletSegue", sender: nil)
		
		let tap = UITapGestureRecognizer(target: self, action: #selector(tapped(sender:)))
		bgView.addGestureRecognizer(tap)
        
        qrCodeImageViewHeight.constant = qrCodeHeight
        
        NotificationCenter.default.addObserver(self, selector: #selector(walletCountDidChanged(notification:)), name: .walletCountDidChanged, object: nil)
        
        languageDidChanged()
    }
    
    override func languageDidChanged() {
		super.languageDidChanged()
        title = "Receive".bil_ui_localized
        navigationItem.rightBarButtonItem?.title = "Refresh address".bil_ui_localized
        specificButton.setAttributedTitle(NSAttributedString(string: .receiveSpecificButtonTitle, attributes: [.font: UIFont.systemFont(ofSize: 15), .underlineStyle: NSUnderlineStyle.styleSingle.rawValue, .foregroundColor: UIColor.white]), for: .normal)
        backupButton.setTitle("Back up wallet now".bil_ui_localized, for: .normal)
    }
    
    deinit {
        NotificationCenter.default.removeObserver(self, name: .walletCountDidChanged, object: nil)
    }
    
    override func viewWillAppear(_ animated: Bool) {
        super.viewWillAppear(animated)
        refreshUI()
    }
    
    @objc
    func walletCountDidChanged(notification: Notification) {
        if let walletID = currentWallet?.id, WalletModel.checkIDIsExists(id: walletID) {
            refreshUI()
        }
        else
        {
            currentWallet = BILWalletManager.shared.wallets.first
        }
    }
    
    func refreshUI() {
        if let w = currentWallet {
            currentWalletIDLabel.text = w.id
            currentWalletBalanceLabel.text = w.btc_balanceString + " BTC"
            currentWalletShortIDLabel.text = "\(w.id?.first ?? "B")"
            
            backupViewHeight.constant = w.isNeedBackup ? 40 : 0
			guard let add = w.btc_addressModels.last else {
				w.lastBTCAddress(success: { (address) in
					self.setAddress(address: address)
				}, failure: { (msg) in
					self.showTipAlert(msg: msg)
				})
				return
			}
			if add.isUsed {
				w.getNewBTCAddress(success: { (address) in
					self.setAddress(address: address)
				}, failure: { (msg) in
					self.setAddress(address: add.address!)
				})
			}
			else
			{
				setAddress(address: add.address!)
			}
        }
    }
	
	func setAddress(address: String) {
		guard let w = currentWallet, w.contain(btcAddress: address) else {		
			return
		}
		receiveModel = BILReceiveModel(address: address, amount: "")
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
			showTipAlert(title: .receiveAddressTipTitle, msg: .receiveAddressTipMessage)
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
        NotificationCenter.default.post(name: .receivePageCurrentWallet, object: currentWallet)
		showChooseWalletView()
	}
	
	private let container = UIView(frame: UIScreen.main.bounds)
	private let bgView = UIView(frame: UIScreen.main.bounds)
	
	func showChooseWalletView() {
		navigationItem.rightBarButtonItem?.isEnabled = false
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
        navigationItem.rightBarButtonItem?.isEnabled = true
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
        bil_showLoading(status: nil)
		currentWallet?.getNewBTCAddress(success: { (address) in
			self.setAddress(address: address)
            self.bil_makeToast(msg: .receiveAddressTipRefresh)
            self.bil_dismissHUD()
		}, failure: { (errorMsg) in
			debugPrint(errorMsg)
            self.bil_dismissHUD()
            self.bil_makeToast(msg: errorMsg)
		})
	}
    
    @IBAction func QRCodeTapped(_ sender: UITapGestureRecognizer) {
        addressLabel.tapAction(gesture: sender)
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
		case "BILReceiveToBackUpWallet":
			if let cont = (segue.destination as? UINavigationController)?.viewControllers.first as? BILBackupWalletMnemonicController {
				if let wallet = currentWallet {
					cont.mnemonicHash = wallet.mnemonicHash
				}
			}
		case "BILToSpecificReceiveSegue":
			let cont = segue.destination as! BILSpecificVolumeReceiveInputController
			cont.receiveModel = receiveModel
		case "BILReceiveChooseWalletSegue":
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
