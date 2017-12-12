//
//  BILSendInputAmountController.swift
//  wallet-ios-core
//
//  Created by 仇弘扬 on 2017/12/12.
//  Copyright © 2017年 BitBill. All rights reserved.
//

import UIKit

class BILSendInputAmountController: BILBaseViewController {
    
    let chooseWalletSegue = "BILInputToChooseWalletSegue"
    
    @IBOutlet weak var amountTextField: UITextField!
    @IBOutlet weak var cnyLabel: UILabel!
    @IBOutlet weak var coinNameLabel: UILabel!
    
    var sendModel: BILSendModel?
    
    override func viewDidLoad() {
        super.viewDidLoad()

        // Do any additional setup after loading the view.
        coinNameLabel.text = sendModel?.coinType.name
        amountTextField.becomeFirstResponder()
    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    

    // MARK: - Navigation

    override func shouldPerformSegue(withIdentifier identifier: String, sender: Any?) -> Bool {
        if identifier == chooseWalletSegue {
            guard let amount = amountTextField.text, !(amount.isEmpty) else {
                return false
            }
            sendModel?.amount = amount
        }
        return true
    }
    
    // In a storyboard-based application, you will often want to do a little preparation before navigation
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        // Get the new view controller using segue.destinationViewController.
        // Pass the selected object to the new view controller.
        if segue.identifier == chooseWalletSegue {
            let cont = segue.destination as! BILSendChooseWalletController
            cont.sendModel = sendModel
        }
    }

}
