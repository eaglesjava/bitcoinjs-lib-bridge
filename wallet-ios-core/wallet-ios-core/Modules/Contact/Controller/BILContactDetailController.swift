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
    
    var contact: Contact?
    
    override func viewDidLoad() {
        super.viewDidLoad()

        // Do any additional setup after loading the view.
        guard let c = contact else { return }
        nameFirstWordLabel.text = c.firstNameWord
        nameLabel.text = c.name
        contactTypeLabel.text = (c.additionType == .walletID ? "钱包ID" : "钱包地址") + " (\(c.coinType.name))"
        contactTypeStringLabel.valueTitle = c.additionType == .walletID ? "ID" : "地址"
        contactTypeStringLabel.text = c.detail
        remarkLabel.text = c.remarkString
    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    
    @IBAction func sendAction(_ sender: Any) {
        guard let c = contact else {
            bil_makeToast(msg: "数据错误")
            return
        }
        tabBarController?.selectedIndex = 3
        NotificationCenter.default.post(name: .sendBTCToContact, object: c)
        navigationController?.popToRootViewController(animated: true)
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
