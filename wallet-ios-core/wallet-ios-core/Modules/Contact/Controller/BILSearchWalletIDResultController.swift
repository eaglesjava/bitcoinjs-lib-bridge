//
//  BILSearchWalletIDResultController.swift
//  wallet-ios-core
//
//  Created by 仇弘扬 on 2017/12/20.
//  Copyright © 2017年 BitBill. All rights reserved.
//

import UIKit

class BILSearchWalletIDResultController: BILLightBlueBaseController {

    var walletID: String?
    
    @IBOutlet weak var nameInputView: BILInputView!
    @IBOutlet weak var remarkInputView: BILInputView!
    @IBOutlet weak var addButton: BILWhiteBorderButton!
    
    override func viewDidLoad() {
        super.viewDidLoad()

        // Do any additional setup after loading the view.
        navigationItem.title = walletID
        nameInputView.delegate = self
        remarkInputView.delegate = self
    }
    
    override func languageDidChanged() {
        super.languageDidChanged()
        nameInputView.updateTitleString("Name".bil_ui_localized)
        remarkInputView.updateTitleString("Remarks".bil_ui_localized)
        nameInputView.textField.placeholder = "Please input".bil_ui_localized
        remarkInputView.textField.placeholder = "Please input".bil_ui_localized
        addButton.setTitle("Add to contact".bil_ui_localized, for: .normal)
    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    
    @IBAction func addAction(_ sender: Any) {
        addContact()
    }
    
    func checkName() -> String? {
        return check(str: nameInputView.textField.text, minLength: 1, maxLength: 30, key: .contact_searchResult_name)
    }
    
    func checkRemark() -> String? {
        return check(str: remarkInputView.textField.text, minLength: 1, maxLength: 100, key: .contact_searchResult_remark, required: false)
    }
    
    func check(str: String?, minLength: Int, maxLength: Int, key: String, required: Bool = true) -> String? {
        guard let s = str, !s.isEmpty else {
            if required {
                return "\(String.contact_searchResult_input)\(key)"
            }
            return nil
        }
        var toReturn: String? = nil
        switch s.count {
        case minLength - 1:
            toReturn = "\(String.contact_searchResult_input)\(key)"
        case let i where i > maxLength:
            toReturn = "\(key) \(String.contact_searchResult_surport) \(minLength)-\(maxLength) \(String.contact_searchResult_wei)"
        default: ()
        }
        
        return toReturn
    }
    
    func addContact() {
        guard let id = walletID else { return }
        guard let name = nameInputView.textField.text else {
            nameInputView.show(tip: .contact_searchResult_nameEmpty, type: .error)
            return
        }
        
        let nameError = checkName()
        guard nameError == nil else {
            nameInputView.textField.becomeFirstResponder()
            nameInputView.show(tip: nameError!, type: .error)
            return
        }
        
        let remark = remarkInputView.textField.text ?? ""
        addContact(id: id, name: name, remark: remark)
    }
    
    func addContact(id: String, name: String, remark: String = "") {
        bil_showLoading(status: nil)
        ContactModel.addContactToServer(id: id, name: name, remark: remark, success: { (contact) in
            self.bil_showSuccess(status: .contact_searchResult_success)
            self.bil_dismissHUD(delay: 1.5, complete: {
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
            self.bil_dismissHUD()
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

extension BILSearchWalletIDResultController: BILInputViewDelegate {
    func textFieldShouldReturn(_ textField: UITextField) -> Bool {
        switch textField {
        case nameInputView.textField:
            guard let msg = checkName() else
            {
                remarkInputView.textField.becomeFirstResponder()
                return false
            }
            nameInputView.show(tip: msg, type: .error)
        case remarkInputView.textField:
            addContact()
        default:
            ()
        }
        return true
    }
    @objc
    func textFieldValueDidChange(notification: Notification) {
        if let textField: UITextField = notification.object as? UITextField {
            switch textField {
            case nameInputView.textField:
                nameInputView.show(tip: "Name".bil_ui_localized, type: .normal)
            case remarkInputView.textField:
                remarkInputView.show(tip: "Remarks".bil_ui_localized, type: .normal)
            default: ()
            }
        }
    }
}
