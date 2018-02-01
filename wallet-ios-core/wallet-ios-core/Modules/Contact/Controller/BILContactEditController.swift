//
//  BILContactEditController.swift
//  wallet-ios-core
//
//  Created by 仇弘扬 on 2017/12/27.
//  Copyright © 2017年 BitBill. All rights reserved.
//

import UIKit

class BILContactEditController: BILLightBlueBaseController {

    var contact: ContactModel?
    
    @IBOutlet weak var nameInputView: BILInputView!
    @IBOutlet weak var remarkInputView: BILInputView!
    @IBOutlet weak var contactTypeLabel: UILabel!
    @IBOutlet weak var contactTypeStringLabel: BILCopyLabel!
    @IBOutlet weak var deleteButton: UIButton!
    
    override func viewDidLoad() {
        super.viewDidLoad()

        // Do any additional setup after loading the view.
        nameInputView.delegate = self
        remarkInputView.delegate = self
        guard let c = contact else { return }
        contactTypeStringLabel.valueTitle = c.additionType == .walletID ? .contact_detail_id : .contact_detail_address
        contactTypeStringLabel.text = c.detail
        remarkInputView.textField.text = c.remark
        nameInputView.textField.text = c.name
    }
    
    override func languageDidChanged() {
        super.languageDidChanged()
        title = "Edit contact".bil_ui_localized
        nameInputView.updateTitleString("Name".bil_ui_localized)
        remarkInputView.updateTitleString("Remarks".bil_ui_localized)
        nameInputView.textField.placeholder = "Please input".bil_ui_localized
        remarkInputView.textField.placeholder = "Please input".bil_ui_localized
        guard let c = contact else { return }
        contactTypeLabel.text = c.additionType == .walletID ? .contact_detail_walletID : (.contact_detail_walletAddress + " (\(c.coinType.name))")
        deleteButton.setTitle("Delete contact".bil_ui_localized, for: .normal)
    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    
    @IBAction func saveAction(_ sender: Any) {
        updateContact()
    }
    @IBAction func deleteAction(_ sender: Any) {
        let alert = UIAlertController(title: "Tip".bil_ui_localized, message: "Are you sure to delete this contact?".bil_ui_localized, preferredStyle: .alert)
        
        alert.addAction(UIAlertAction(title: "Delete".bil_ui_localized, style: .destructive, handler: { (action) in
            self.deleteContact()
        }))
        
        alert.addAction(UIAlertAction(title: "Cancel".bil_ui_localized, style: .cancel, handler: nil))
        
        present(alert, animated: true, completion: nil)
    }
    
    func checkName() -> String? {
        return check(str: nameInputView.textField.text, minLength: 1, maxLength: 30, key: "Name".bil_ui_localized)
    }
    
    func checkRemark() -> String? {
        return check(str: remarkInputView.textField.text, minLength: 1, maxLength: 100, key: "Remarks".bil_ui_localized, required: false)
    }
    
    func check(str: String?, minLength: Int, maxLength: Int, key: String, required: Bool = true) -> String? {
        guard let s = str, !s.isEmpty else {
            if required {
                return "Please input".bil_ui_localized + "\(key)"
            }
            return nil
        }
        var toReturn: String? = nil
        switch s.count {
        case minLength - 1:
            toReturn = "Please input".bil_ui_localized + "\(key)"
        case let i where i > maxLength:
            toReturn = "\(key) \(String.contact_searchResult_surport) \(minLength)-\(maxLength) \(String.contact_searchResult_wei)"
        default: ()
        }
        
        return toReturn
    }
    
    func deleteContact() {
        bil_showLoading(status: nil)
        contact?.deleteFromServer(success: {
            self.navigationController?.popToRootViewController(animated: true)
            self.bil_dismissHUD()
        }, failure: { (msg, code) in
            self.bil_makeToast(msg: msg)
            self.bil_dismissHUD()
        })
    }
    
    func updateContact() {
        guard let name = nameInputView.textField.text else {
            nameInputView.show(tip: .contact_searchResult_nameEmpty, type: .error)
            return
        }
        
        guard name != contact?.name || remarkInputView.textField.text != contact?.remark else {
            showTipAlert(msg: "Nothing has been changed".bil_ui_localized)
            return
        }
        
        let nameError = checkName()
        guard nameError == nil else {
            nameInputView.textField.becomeFirstResponder()
            nameInputView.show(tip: nameError!, type: .error)
            return
        }
        
        let remark = remarkInputView.textField.text ?? ""
        bil_showLoading(status: nil)
        contact?.updateToServer(name: name, remark: remark, success: { (contact) in
            self.bil_showSuccess(status: "Updated successfully".bil_ui_localized)
            self.bil_dismissHUD(delay: 1.5, complete: {
                self.navigationController?.popViewController(animated: true)
            })
        }) { (msg, code) in
            self.bil_dismissHUD()
            self.showTipAlert(msg: msg)
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

extension BILContactEditController: BILInputViewDelegate {
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
            updateContact()
        default:
            ()
        }
        return true
    }
}
