//
//  BILSendResultController.swift
//  wallet-ios-core
//
//  Created by 仇弘扬 on 2017/12/13.
//  Copyright © 2017年 BitBill. All rights reserved.
//

import UIKit
import SafariServices
import SVProgressHUD

class BILSendResultController: BILBaseViewController {

    @IBOutlet weak var addressLabel: UILabel!
    @IBOutlet weak var amountLabel: UILabel!
    @IBOutlet weak var txHashLabel: BILCopyLabel!
    @IBOutlet weak var addContactButton: BILGradientButton!
    @IBOutlet weak var contactRecommendLabel: UILabel!
    
    var sendModel: BILSendModel?
    
    override func viewDidLoad() {
        super.viewDidLoad()

        // Do any additional setup after loading the view.
        
        if let tx = sendModel?.transaction {
            addressLabel.text = tx.address
            amountLabel.text = "\(BTCFormatString(btc: Int64(tx.amount))) BTC"
            txHashLabel.text = tx.txHash
        }
        
        if let model = sendModel {
            addContactButton.isHidden = model.isContactAddress
            contactRecommendLabel.isHidden = model.isContactAddress
        }
    }
    @IBAction func addContactAction(_ sender: Any) {
        if let tx = sendModel?.transaction {
            showAddContactAlert(address: tx.address)
        }
    }
    
    func showAddContactAlert(address: String) {
        let alert = UIAlertController(title: "输入 \(address) 的名称", message: nil, preferredStyle: .alert)
        
        alert.addAction(UIAlertAction(title: "确认", style: .default, handler: { (action) in
            guard let name = alert.textFields?.first?.text else {
                return
            }
            self.addContact(address: address, name: name)
        }))
        
        alert.addAction(UIAlertAction(title: "取消", style: .cancel, handler: { (action) in
            
        }))
        
        alert.addTextField { (textField) in
            textField.placeholder = "请输入联系人名称"
        }
        
        present(alert, animated: true, completion: nil)
    }
    
    func addContact(address: String, name: String) {
        ContactModel.addContactToServer(address: address, name: name, success: { (contact) in
            SVProgressHUD.showSuccess(withStatus: "添加成功")
            SVProgressHUD.dismiss(withDelay: 1.5, completion: {
                self.dismiss(animated: true, completion: nil)
            })
        }) { (msg, code) in
            self.bil_makeToast(msg: msg)
        }
    }
    
    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    
    @IBAction func done(_ sender: Any) {
        dismiss(animated: true, completion: nil)
    }
    
    @IBAction func checkTransaction(_ sender: Any) {
        guard let txHash = sendModel?.transaction else { return }
        if let url = URL(string: "\(String.bil_extenal_blockchain_transaction)\(txHash)") {
            UIApplication.shared.open(url, options: [:], completionHandler: nil)
        }
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
