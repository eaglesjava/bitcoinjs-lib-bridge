//
//  BILRecieveController.swift
//  wallet-ios-core
//
//  Created by 仇弘扬 on 2017/12/7.
//  Copyright © 2017年 BitBill. All rights reserved.
//

import UIKit

class BILRecieveController: BILBaseViewController {

    @IBOutlet weak var qrCodeImageView: UIImageView!
    @IBOutlet weak var addressLabel: UILabel!
    var currentWallet: WalletModel?
    @IBOutlet weak var currentWalletIDLabel: UILabel!
    @IBOutlet weak var currentWalletBalanceLabel: UILabel!
    @IBOutlet weak var currentWalletShortIDLabel: UILabel!
    
    override func viewDidLoad() {
        super.viewDidLoad()

        // Do any additional setup after loading the view.
        currentWallet = BILWalletManager.shared.wallets.first
        
        if let w = currentWallet {
            currentWalletIDLabel.text = w.id
            currentWalletBalanceLabel.text = w.btc_balanceString + " btc"
            currentWalletShortIDLabel.text = "\(w.id?.first ?? "B")"
            
            let scale = UIScreen.main.scale
            let size = qrCodeImageView.frame.size.applying(CGAffineTransform(scaleX: scale, y: scale))
            w.lastBTCAddress(success: { (address) in
                self.qrCodeImageView.image = BILQRCodeHelper.generateQRCode(msg: "bitcoin:\(address)", targetSize: size)
                self.addressLabel.text = address
            }, failure: { (errorMsg) in
                debugPrint(errorMsg)
            })
        }
    }
    
    override func viewDidAppear(_ animated: Bool) {
        super.viewDidAppear(animated)
        showTipAlert(title: "友情提醒", msg: "为保护您的隐私，每次转入操作时，都将使用新地址，已使用的旧地址仍然可用")
    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
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
