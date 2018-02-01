//
//  BILSendResultController.swift
//  wallet-ios-core
//
//  Created by 仇弘扬 on 2017/12/13.
//  Copyright © 2017年 BitBill. All rights reserved.
//

import UIKit
import SafariServices

class BILSendResultController: BILBaseViewController {

    @IBOutlet weak var addressLabel: UILabel!
    @IBOutlet weak var amountLabel: UILabel!
    @IBOutlet weak var txHashLabel: BILCopyLabel!
    @IBOutlet weak var addContactButton: BILGradientButton!
    @IBOutlet weak var blockchainButton: UIButton!
    @IBOutlet weak var contactRecommendLabel: UILabel!
    
	@IBOutlet weak var resultLabel: UILabel!
	@IBOutlet weak var doneButton: UIButton!
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
	
	override func languageDidChanged() {
		super.languageDidChanged()
		blockchainButton.setAttributedTitle(NSAttributedString(string: .homeTxDetailBlockchain, attributes: [.font: UIFont.systemFont(ofSize: 15), .underlineStyle: NSUnderlineStyle.styleSingle.rawValue, .foregroundColor: UIColor.white]), for: .normal)
		doneButton.setTitle("Complete".bil_ui_localized, for: .normal)
		resultLabel.text = "Send successfully".bil_ui_localized
		contactRecommendLabel.text = "Add the address as the frequent contact for the next transaction".bil_ui_localized
		addContactButton.setTitle("Quickly create contacts".bil_ui_localized, for: .normal)
	}
	
    @IBAction func addContactAction(_ sender: Any) {
        if let tx = sendModel?.transaction {
            showAddContactAlert(address: tx.address)
        }
    }
    
    func showAddContactAlert(address: String) {
        let alert = UIAlertController(title: "\(String.sendResultInput) \(address) \(String.sendResultInputName)", message: nil, preferredStyle: .alert)
        
        alert.addAction(UIAlertAction(title: .sendConfirmConfirm, style: .default, handler: { (action) in
            guard let name = alert.textFields?.first?.text else {
                return
            }
            self.addContact(address: address, name: name)
        }))
        
        alert.addAction(UIAlertAction(title: .sendConfirmCancel, style: .cancel, handler: { (action) in
            
        }))
        
        alert.addTextField { (textField) in
            textField.placeholder = .sendResultInputNamePlaceHolder
        }
        
        present(alert, animated: true, completion: nil)
    }
    
    func addContact(address: String, name: String) {
        ContactModel.addContactToServer(address: address, name: name, success: { (contact) in
            self.bil_showSuccess(status: .sendResultAddSuccess)
            self.bil_dismissHUD(delay: 1.5, complete: {
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
        guard let txHash = sendModel?.transaction?.txHash else { return }
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
