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
    var wallet: WalletModel?
    
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
        guard let w = wallet else { return }
        guard let model = sendModel else { return }
        w.getUTXOFromServer(success: { (utxos) in
            let builder = TransactionBuilder(seedHex: "4c70975c6b4f3b33aefdec2eeee8e5576616c8ef0616df50d302d2380d16683bedd89de385e98f5373c1ffab06be1f6f8a84f3df047f39b1718179ff1b0ed313")
            for utxo in utxos {
                builder.addInput(input: utxo.toInput())
            }
            builder.addOutput(output: BTCOutput(address: model.address, amount: model.bitcoinSatoshiAmount))
            builder.addOutput(output: BTCOutput(address: "1AXUGwG2fom3payUa29KDDvHSsJ8cNpCd9", amount: model.bitcoinSatoshiAmount))
            builder.build(success: { (tx) in
                debugPrint(tx.bytesCount)
            }, failure: { (error) in
                debugPrint(error.localizedDescription)
            })
            self.sendModel?.wallet = self.wallet
            self.performSegue(withIdentifier: self.confirmSegue, sender: sender)
        }) { (msg, code) in
            self.showTipAlert(title: nil, msg: msg)
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
