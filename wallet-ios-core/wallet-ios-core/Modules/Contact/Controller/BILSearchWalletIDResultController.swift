//
//  BILSearchWalletIDResultController.swift
//  wallet-ios-core
//
//  Created by 仇弘扬 on 2017/12/20.
//  Copyright © 2017年 BitBill. All rights reserved.
//

import UIKit
import SVProgressHUD

class BILSearchWalletIDResultController: BILLightBlueBaseController {

    var walletID: String?
    
    @IBOutlet weak var idLabel: UILabel!
    @IBOutlet weak var idFirstWordLabel: UILabel!
    override func viewDidLoad() {
        super.viewDidLoad()

        // Do any additional setup after loading the view.
        idLabel.text = walletID
        guard let firstWord = walletID?.first else { return }
        idFirstWordLabel.text = String(firstWord)
    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    
    @IBAction func addAction(_ sender: Any) {
        guard let id = walletID else { return }
        showAddContactAlert(id: id)
    }
    func showAddContactAlert(id: String) {
        let alert = UIAlertController(title: "输入 \(id) 的名称", message: nil, preferredStyle: .alert)
        
        alert.addAction(UIAlertAction(title: "确认", style: .default, handler: { (action) in
            guard let name = alert.textFields?.first?.text else {
                return
            }
            self.addContact(id: id, name: name)
        }))
        
        alert.addAction(UIAlertAction(title: "取消", style: .cancel, handler: { (action) in
            
        }))
        
        alert.addTextField { (textField) in
            textField.placeholder = "请输入联系人名称"
        }
        
        present(alert, animated: true, completion: nil)
    }
    
    func addContact(id: String, name: String) {
        Contact.addContactToServer(id: id, name: name, success: { (contact) in
            SVProgressHUD.showSuccess(withStatus: "添加成功")
            SVProgressHUD.dismiss(withDelay: 1.5, completion: {
                guard let nav = self.navigationController else {
                    return
                }
                for cont in nav.viewControllers {
                    if cont is BILContactController {
                        nav.popToViewController(cont, animated: true)
                        return
                    }
                }
                nav.popToRootViewController(animated: true)
            })
        }) { (msg, code) in
            self.bil_makeToast(msg: msg)
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
