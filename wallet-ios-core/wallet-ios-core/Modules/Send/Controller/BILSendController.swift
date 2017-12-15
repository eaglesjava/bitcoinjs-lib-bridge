//
//  BILSendController.swift
//  wallet-ios-core
//
//  Created by 仇弘扬 on 2017/12/11.
//  Copyright © 2017年 BitBill. All rights reserved.
//

import UIKit

class BILSendController: BILBaseViewController, UITextFieldDelegate {

    @IBOutlet weak var addressInputView: BILInputView!
    
    let addressToAmountSegue = "BILAddressToAmountSegue"
    let scanResultSegue = "BILSendToScanResult"
    
    var sendModel: BILSendModel?
    
    override func viewDidLoad() {
        super.viewDidLoad()

        // Do any additional setup after loading the view.
        addressInputView.textField.text = "17fLtpDmu7GhMgFyVBCrNySSanodr3toXP"
        NotificationCenter.default.addObserver(self, selector: #selector(transactionDidSend(notification:)), name: .transactionSended, object: nil)
    }
    
    deinit {
        NotificationCenter.default.removeObserver(self, name: .transactionSended, object: nil)
    }
    
    @objc
    func transactionDidSend(notification: Notification) {
        addressInputView.textField.text = ""
    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    
    func setAddress(address: String) {
        BitcoinJSBridge.shared.validateAddress(address: address, success: { (result) in
            debugPrint(result)
            let isValidate = result as! Bool
            if isValidate {
                self.addressInputView.textField.text = address
            } else {
                self.showTipAlert(title: nil, msg: "不是合法的地址")
            }
        }) { (error) in
            debugPrint(error)
        }
    }
    
    // MARK: - Actions
    @IBAction func scanQRCodeAction(_ sender: Any) {
        unowned let unownedSelf = self
        let cont = BILQRCodeScanViewController.controller { (qrString) in
            if let result = BILURLHelper.transferBitCoinURL(urlString: qrString) {
                debugPrint(result)
                unownedSelf.sendModel = BILSendModel(address: result.address, amount: String(result.amount))
                if result.amount < 0 {
                    unownedSelf.setAddress(address: result.address)
                    unownedSelf.navigationController?.popViewController(animated: true)
                }
                else
                {
                    unownedSelf.performSegue(withIdentifier: unownedSelf.scanResultSegue, sender: sender)
                }
            }
        }
        show(cont, sender: sender)
    }
    
    func textFieldShouldReturn(_ textField: UITextField) -> Bool {
        nextAction(textField)
        return true
    }
    
    @IBAction func nextAction(_ sender: Any) {
        guard let address = addressInputView.textField.text, !(address.isEmpty) else {
            bil_makeToast(msg: NSLocalizedString("地址不能为空", comment: ""))
            return
        }
        
        BitcoinJSBridge.shared.validateAddress(address: address, success: { (result) in
            let isValidate = result as! Bool
            if isValidate {
                self.sendModel = BILSendModel(address: address)
                self.performSegue(withIdentifier: self.addressToAmountSegue, sender: nil)
            }
            else
            {
                self.showTipAlert(title: "地址不正确", msg: "您输入的不是合法的地址")
            }
        }) { (error) in
            self.showTipAlert(title: "出现了错误", msg: error.localizedDescription)
        }
        
    }
    // MARK: - Navigation

    // In a storyboard-based application, you will often want to do a little preparation before navigation
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        // Get the new view controller using segue.destinationViewController.
        // Pass the selected object to the new view controller.
        guard let id = segue.identifier else { return }
        switch id {
        case addressToAmountSegue:
            let cont = segue.destination as! BILSendInputAmountController
            cont.sendModel = sendModel
        case scanResultSegue:
            let cont = segue.destination as! BILScanQRCodeResultController
            cont.sendModel = sendModel
        default:
            ()
        }
    }

}
