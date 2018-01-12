//
//  BILAddContactByAddressController.swift
//  wallet-ios-core
//
//  Created by 仇弘扬 on 2017/12/20.
//  Copyright © 2017年 BitBill. All rights reserved.
//

import UIKit
import SVProgressHUD

class BILAddContactByAddressController: BILLightBlueBaseController {

    @IBOutlet weak var nameInputView: BILInputView!
    @IBOutlet weak var addressInputView: BILInputView!
    @IBOutlet weak var remarkInputView: BILInputView!
    
    var address: String?
    
    override func viewDidLoad() {
        super.viewDidLoad()

        // Do any additional setup after loading the view.
        nameInputView.delegate = self
        addressInputView.delegate = self
        remarkInputView.delegate = self
        
        addressInputView.textField.text = address
    }
    
    override func languageDidChanged() {
        super.languageDidChanged()
        title = "Add via address".bil_ui_localized
        nameInputView.updateTitleString("Name".bil_ui_localized)
        addressInputView.updateTitleString("Address of digital asset".bil_ui_localized)
        remarkInputView.updateTitleString("Remarks".bil_ui_localized)
        nameInputView.textField.placeholder = "Please input".bil_ui_localized
        addressInputView.textField.placeholder = "Please input".bil_ui_localized
        remarkInputView.textField.placeholder = "Please input".bil_ui_localized
    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    
    @IBAction func addAction(_ sender: Any) {
        addContact()
    }
    
    @IBAction func scanQRCodeAction(_ sender: Any) {
        unowned let unownedSelf = self
        let cont = BILQRCodeScanViewController.controller { (qrString) in
            if let result = BILURLHelper.transferBitCoinURL(urlString: qrString)?.address {
                unownedSelf.navigationController?.popViewController(animated: true)
                guard !ContactModel.isAddressExits(address: result) else {
                    unownedSelf.bil_makeToast(msg: .contact_contact_addressExits)
                    return
                }
                unownedSelf.addressInputView.textField.text = result
            }
        }
        show(cont, sender: sender)
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

extension BILAddContactByAddressController {
    
    override func viewDidAppear(_ animated: Bool) {
        super.viewDidAppear(true)
        NotificationCenter.default.addObserver(self, selector: #selector(self.textFieldValueDidChange(notification:))
            , name: .UITextFieldTextDidChange, object: nil)
    }
    
    override func viewDidDisappear(_ animated: Bool) {
        super.viewDidDisappear(true)
        NotificationCenter.default.removeObserver(self, name: .UITextFieldTextDidChange, object: nil)
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
        
        guard let address = addressInputView.textField.text else {
            showTipAlert(title: nil, msg: .contact_contact_addressInvalid, dismissed: {
                self.addressInputView.textField.becomeFirstResponder()
            })
            return
        }
        
        guard !ContactModel.isAddressExits(address: address) else {
            showTipAlert(msg: .contact_contact_addressExits)
            return
        }
        
        BitcoinJSBridge.shared.validateAddress(address: address, success: { (result) in
            debugPrint(result)
            let isValidate = result as! Bool
            if isValidate {
                self.bil_showLoading(status: nil)
                ContactModel.addContactToServer(id: "", address: address, name: name, remark: remark, success: { (contact) in
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
            } else {
                self.bil_dismissHUD()
                self.addressInputView.show(tip: .contact_contact_addressInvalid, type: .error)
            }
        }) { (error) in
            debugPrint(error)
            self.showTipAlert(msg: error.localizedDescription)
        }
    }
}

extension BILAddContactByAddressController: BILInputViewDelegate {
    func textFieldShouldReturn(_ textField: UITextField) -> Bool {
        switch textField {
        case nameInputView.textField:
            guard let msg = checkName() else
            {
                addressInputView.textField.becomeFirstResponder()
                return false
            }
            nameInputView.show(tip: msg, type: .error)
        case addressInputView.textField:
            guard let address = addressInputView.textField.text else {
                addressInputView.show(tip: .contact_contact_addressInvalid, type: .error)
                return false
            }
            guard !ContactModel.isAddressExits(address: address) else {
                addressInputView.show(tip: .contact_contact_addressExits, type: .error)
                return false
            }
            BitcoinJSBridge.shared.validateAddress(address: address, success: { (result) in
                debugPrint(result)
                let isValidate = result as! Bool
                if isValidate {
                    self.remarkInputView.textField.becomeFirstResponder()
                } else {
                    self.addressInputView.show(tip: .contact_contact_addressInvalid, type: .error)
                }
            }) { (error) in
                debugPrint(error)
            }
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
            case addressInputView.textField:
                addressInputView.show(tip: "Address of digital asset".bil_ui_localized, type: .normal)
            case remarkInputView.textField:
                remarkInputView.show(tip: "Remarks".bil_ui_localized, type: .normal)
            default: ()
            }
        }
    }
}
