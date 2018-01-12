//
//  BILContactDetailController.swift
//  wallet-ios-core
//
//  Created by 仇弘扬 on 2017/12/19.
//  Copyright © 2017年 BitBill. All rights reserved.
//

import UIKit

class BILContactDetailController: BILLightBlueBaseController {

    @IBOutlet weak var nameFirstWordLabel: UILabel!
    @IBOutlet weak var nameLabel: UILabel!
    @IBOutlet weak var contactTypeLabel: UILabel!
    @IBOutlet weak var contactTypeStringLabel: BILCopyLabel!
    @IBOutlet weak var remarkLabel: UILabel!
    @IBOutlet weak var remarkTitleLabel: UILabel!
    @IBOutlet weak var sendButton: BILWhiteBorderButton!
    
    var contact: ContactModel?
    
    override func viewDidLoad() {
        super.viewDidLoad()

        // Do any additional setup after loading the view.
    }
    
    override func languageDidChanged() {
        title = "Contact details".bil_ui_localized
        remarkTitleLabel.text = "Remarks".bil_ui_localized
        guard let c = contact else { return }
        contactTypeLabel.text = c.additionType == .walletID ? .contact_detail_walletID : (.contact_detail_walletAddress + " (\(c.coinType.name))")
        sendButton.setTitle("Send".bil_ui_localized, for: .normal)
    }
    
    override func viewWillAppear(_ animated: Bool) {
        super.viewWillAppear(animated)
        guard let c = contact else { return }
        nameFirstWordLabel.text = c.firstNameWord
        nameLabel.text = c.name
        contactTypeStringLabel.valueTitle = c.additionType == .walletID ? .contact_detail_id : .contact_detail_address
        contactTypeStringLabel.text = c.detail
        remarkLabel.text = c.remarkString
    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    
    @IBAction func sendAction(_ sender: Any) {
        guard let c = contact else {
            bil_makeToast(msg: .contact_detail_error)
            return
        }
        tabBarController?.selectedIndex = 3
        NotificationCenter.default.post(name: .sendBTCToContact, object: c)
        navigationController?.popToRootViewController(animated: true)
    }
    
    // MARK: - Navigation

    // In a storyboard-based application, you will often want to do a little preparation before navigation
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        // Get the new view controller using segue.destinationViewController.
        // Pass the selected object to the new view controller.
        if let id = segue.identifier, id == "BILContactDetailToEditSegue" {
            let cont = segue.destination as? BILContactEditController
            cont?.contact = contact
        }
    }

}
