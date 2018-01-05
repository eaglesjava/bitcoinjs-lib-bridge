//
//  BILSearchWalletIDController.swift
//  wallet-ios-core
//
//  Created by 仇弘扬 on 2017/12/20.
//  Copyright © 2017年 BitBill. All rights reserved.
//

import UIKit

extension String {
    static var bil_searchIDToResultSegue: String { return "BILSearchIDToResultSegue" }
}

class BILSearchWalletIDController: BILLightBlueBaseController {
    
    @IBOutlet weak var idInputView: BILInputView!
    
    override func viewDidLoad() {
        super.viewDidLoad()

        // Do any additional setup after loading the view.
        idInputView.delegate = self
        idInputView.textField.becomeFirstResponder()
    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    @IBAction func searchAction(_ sender: Any) {
        view.endEditing(true)
        func showToast(msg: String) {
            self.bil_dismissHUD()
            showTipAlert(title: nil, msg: msg) {
                self.idInputView.textField.becomeFirstResponder()
            }
        }
        guard let id = idInputView.textField.text, !id.isEmpty else {
            showToast(msg: "ID 不能为空")
            return
        }
        guard id.count <= 20 else {
            showToast(msg: "ID 太长了")
            return
        }
        
        guard !ContactModel.isWalletIDExits(walletID: id) else {
            showToast(msg: "ID 已存在")
            return
        }
        
        bil_showLoading(status: nil)
        ContactModel.getContactFromServer(by: id, success: { (id) in
            self.showResult(id: id)
        }) { (msg, code) in
            showToast(msg: msg)
        }
        
    }
    
    func showResult(id: String) {
        self.bil_dismissHUD()
        performSegue(withIdentifier: .bil_searchIDToResultSegue, sender: id)
    }
    
    // MARK: - Navigation
    // In a storyboard-based application, you will often want to do a little preparation before navigation
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        // Get the new view controller using segue.destinationViewController.
        // Pass the selected object to the new view controller.
        guard let id = segue.identifier else { return }
        switch id {
        case .bil_searchIDToResultSegue:
            guard let walletID = sender as? String else { return }
            let cont = segue.destination as! BILSearchWalletIDResultController
            cont.walletID = walletID
        default:
            ()
        }
    }

}

extension BILSearchWalletIDController: BILInputViewDelegate {
    func textFieldShouldReturn(_ textField: UITextField) -> Bool {
        searchAction(textField)
        return true
    }
}
