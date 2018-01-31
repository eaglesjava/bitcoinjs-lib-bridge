//
//  BILSpecificVolumeReceiveController.swift
//  wallet-ios-core
//
//  Created by 仇弘扬 on 2017/12/8.
//  Copyright © 2017年 BitBill. All rights reserved.
//

import UIKit

extension String {
    static let bil_specificToTxDetailSegue = "BILSpecificToTxDetailSegue"
}

class BILSpecificVolumeReceiveController: BILBaseViewController {

	var receiveModel: BILReceiveModel?
	
	@IBOutlet weak var addressLabel: UILabel!
	@IBOutlet weak var qrCodeImageView: UIImageView!
	@IBOutlet weak var amountLabel: UILabel!
    @IBOutlet weak var payLabel: UILabel!
    @IBOutlet weak var doneItem: UIBarButtonItem!
    override func viewDidLoad() {
        super.viewDidLoad()

        // Do any additional setup after loading the view.
		if let r = receiveModel {
			addressLabel.text = r.address
			qrCodeImageView.image = BILQRCodeHelper.generateQRCode(msg: r.urlString)
			amountLabel.text = "\(r.bitcoinAmount) \(r.coinType.name)"
            BILTransactionManager.shared.addObserveAddress(address: r.address)
		}
    }
    
    deinit {
        if let r = receiveModel {
            BILTransactionManager.shared.removeObserveAddress(address: r.address)
        }
    }
    
    override func viewDidAppear(_ animated: Bool) {
        super.viewDidAppear(animated)
        NotificationCenter.default.addObserver(self, selector: #selector(txReceived), name: .transactionReceived, object: nil)
    }
    
    override func viewDidDisappear(_ animated: Bool) {
        super.viewDidDisappear(animated)
        NotificationCenter.default.removeObserver(self, name: .transactionReceived, object: nil)
    }
    
    @objc func txReceived(notification: Notification) {
        guard let tx = notification.object as? BTCTransactionModel else { return }
        let cont = UIStoryboard(name: "Home", bundle: nil).instantiateViewController(withIdentifier: "BILBTCTransactionController") as! BILBTCTransactionController
        cont.transaction = tx
        let nav = UINavigationController(rootViewController: cont)
        cont.setupRightItemForPresent()
        BILControllerManager.shared.mainTabBarController?.present(nav, animated: true, completion: {
            self.navigationController?.popToRootViewController(animated: false)
            BILControllerManager.shared.mainTabBarController?.selectedIndex = 0
        })
    }
    
    override func languageDidChanged() {
        super.languageDidChanged()
        title = "Receive".bil_ui_localized
        payLabel.text = "Scan to pay".bil_ui_localized
        doneItem.title = "Done".bil_ui_localized
    }

    @IBAction func doneAction(_ sender: Any) {
        navigationController?.popToRootViewController(animated: true)
    }
    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    
    // MARK: - Navigation

    // In a storyboard-based application, you will often want to do a little preparation before navigation
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        // Get the new view controller using segue.destinationViewController.
        // Pass the selected object to the new view controller.
        guard let id = segue.identifier else { return }
        switch id {
        case String.bil_specificToTxDetailSegue:
            guard let tx = sender as? BTCTransactionModel else { return }
            let cont = segue.destination as! BILBTCTransactionController
            cont.transaction = tx
        default:
            ()
        }
    }

}
