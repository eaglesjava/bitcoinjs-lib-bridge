//
//  BILSendInputAmountController.swift
//  wallet-ios-core
//
//  Created by 仇弘扬 on 2017/12/12.
//  Copyright © 2017年 BitBill. All rights reserved.
//

import UIKit

class BILSendInputAmountController: BILBaseViewController, UITextFieldDelegate {
    
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
    
    func textField(_ textField: UITextField, shouldChangeCharactersIn range: NSRange, replacementString string: String) -> Bool {
        guard var text = textField.text else { return true }
        print(text)
        let rangeLocation =  text.index(text.startIndex, offsetBy: range.location)
        if range.length == 0 {
            text.insert(contentsOf: string, at:rangeLocation)
        } else {
            let upperLocation = text.index(rangeLocation, offsetBy: range.length)
            text.removeSubrange(rangeLocation..<upperLocation)
        }
        print("\(text)  \(range)    \(string)")
        
        if text.contains(".") {
            let array = text.components(separatedBy: ".")
            if array.count > 2 {
                return false
            }
            let decimalPlace = array[1]
            return decimalPlace.count <= "\(BTC_SATOSH)".count - 1
        }
        
        return true
    }

    // MARK: - Navigation

    override func shouldPerformSegue(withIdentifier identifier: String, sender: Any?) -> Bool {
        if identifier == chooseWalletSegue {
            guard let amount = amountTextField.text, !(amount.isEmpty) else {
                bil_makeToast(msg: "金额不能为空")
                return false
            }
            guard let amountD = Double(amount), amountD > 0 else {
                bil_makeToast(msg: "金额必须大于 0")
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
