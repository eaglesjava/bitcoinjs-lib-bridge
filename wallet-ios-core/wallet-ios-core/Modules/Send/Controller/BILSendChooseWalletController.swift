//
//  BILSendChooseWalletController.swift
//  wallet-ios-core
//
//  Created by 仇弘扬 on 2017/12/12.
//  Copyright © 2017年 BitBill. All rights reserved.
//

import UIKit

class BILSendChooseWalletController: BILBaseViewController {
    let chooseWalletSegue = "BILSendChooseWalletSegue"
    let confirmSegue = "BILChooseWalletToConfirmSegue"
    
    var sendModel: BILSendModel?
    var wallet: WalletModel? {
        didSet {
            guard let w = wallet else { return }
            if let isSendAll = sendModel?.isSendAll, isSendAll {
                sendModel?.amount = BTCFormatString(btc: w.btcBalance)
            }
        }
    }
    
    var targetAmount = 0
    
    override func viewDidLoad() {
        super.viewDidLoad()

        // Do any additional setup after loading the view.
        wallet = BILWalletManager.shared.wallets.first
    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    
    @IBAction func nextAction(_ sender: Any) {
        guard let w = wallet else {
            return
        }
        guard let model = sendModel else {
            return
        }
        if model.isSendAll, w.btcBalance == 0 {
            showTipAlert(title: "提示", msg: "余额不足")
            return
        }
        
        if w.btcBalance >= Int64(model.bitcoinSatoshiAmount) {
            self.sendModel?.wallet = self.wallet
            self.performSegue(withIdentifier: self.confirmSegue, sender: sender)
        }
        else
        {
            showTipAlert(title: "提示", msg: "余额不足")
        }
    }
    
    // MARK: - Navigation

    // In a storyboard-based application, you will often want to do a little preparation before navigation
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        // Get the new view controller using segue.destinationViewController.
        // Pass the selected object to the new view controller.
        guard let id = segue.identifier else { return }
        switch id {
        case chooseWalletSegue:
            let cont = segue.destination as! BILChooseWalletController
            unowned let s = self
            cont.setDidSelecteWalletClosure(onSelected: { (wallet) in
                s.wallet = wallet
            })
        case confirmSegue:
            let cont = segue.destination as! BILSendConfirmController
            cont.sendModel = sendModel
        default:
            ()
        }
    }

}
