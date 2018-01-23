//
//  BILScanQRCodeResultController.swift
//  wallet-ios-core
//
//  Created by 仇弘扬 on 2017/12/12.
//  Copyright © 2017年 BitBill. All rights reserved.
//

import UIKit

class BILScanQRCodeResultController: BILBaseViewController {

    @IBOutlet weak var amountTextField: UITextField!
    @IBOutlet weak var coinNameLabel: UILabel!
    @IBOutlet weak var cnyLabel: BILExchangeRateLabel!
    @IBOutlet weak var addressLabel: UILabel!
	@IBOutlet weak var addreddTitleLabel: UILabel!
	
	@IBOutlet weak var nextButton: BILGradientButton!
	var sendModel: BILSendModel?
    
    override func viewDidLoad() {
        super.viewDidLoad()

        // Do any additional setup after loading the view.
        guard let model = sendModel else { return }
        addressLabel.text = model.address
        coinNameLabel.text = model.coinType.name
        amountTextField.text = model.amount
        cnyLabel.btcValue = Double(model.amount)
    }
	
	override func languageDidChanged() {
		super.languageDidChanged()
		title = "Scan results".bil_ui_localized
		nextButton.setTitle("Next".bil_ui_localized, for: .normal)
		addreddTitleLabel.text = "Address of receiver".bil_ui_localized
	}
    
    override func viewDidAppear(_ animated: Bool) {
        super.viewDidAppear(animated)
        guard let viewControllers = navigationController?.viewControllers else { return }
        for cont in viewControllers.reversed() {
            if cont is BILQRCodeScanViewController {
                navigationController?.viewControllers.remove(at: viewControllers.count - 2)
                navigationController?.view.setNeedsLayout()
                navigationController?.view.layoutIfNeeded()
                break
            }
        }
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
        if segue.identifier == "BILScanResultToChooseWallet" {
            let cont = segue.destination as! BILSendChooseWalletController
            cont.sendModel = sendModel
        }
    }

}
