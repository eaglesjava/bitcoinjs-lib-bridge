//
//  BILSendController.swift
//  wallet-ios-core
//
//  Created by 仇弘扬 on 2017/12/11.
//  Copyright © 2017年 BitBill. All rights reserved.
//

import UIKit

class BILSendController: BILBaseViewController {

    @IBOutlet weak var addressInputView: BILInputView!
    
    let addressToAmountSegue = "BILAddressToAmountSegue"
    let scanResultSegue = "BILSendToScanResult"
    
    var sendModel: BILSendModel?
    
    override func viewDidLoad() {
        super.viewDidLoad()

        // Do any additional setup after loading the view.
        addressInputView.textField.text = "17XLRQJX97DZajzH7kZBrFzs7qHYBvWV1F"
    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    
    func setAddress(address: String) {
        addressInputView.textField.text = address
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
    
    // MARK: - Navigation
    
    override func shouldPerformSegue(withIdentifier identifier: String, sender: Any?) -> Bool {
        if identifier == addressToAmountSegue {
            guard let address = addressInputView.textField.text, !(address.isEmpty) else {
                bil_makeToast(msg: NSLocalizedString("地址不能为空", comment: ""))
                return false
            }
            sendModel = BILSendModel(address: address)
        }
        return true
    }

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
