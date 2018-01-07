//
//  BILContactEditController.swift
//  wallet-ios-core
//
//  Created by 仇弘扬 on 2017/12/27.
//  Copyright © 2017年 BitBill. All rights reserved.
//

import UIKit
import SVProgressHUD

class BILContactEditController: BILLightBlueBaseController {

    var contact: ContactModel?
    
    @IBOutlet weak var nameInputView: BILInputView!
    @IBOutlet weak var remarkInputView: BILInputView!
    @IBOutlet weak var contactTypeLabel: UILabel!
    @IBOutlet weak var contactTypeStringLabel: BILCopyLabel!
    
    override func viewDidLoad() {
        super.viewDidLoad()

        // Do any additional setup after loading the view.
        nameInputView.delegate = self
        remarkInputView.delegate = self
        guard let c = contact else { return }
        contactTypeLabel.text = c.additionType == .walletID ? .contact_detail_walletID : (.contact_detail_walletAddress + " (\(c.coinType.name))")
        contactTypeStringLabel.valueTitle = c.additionType == .walletID ? .contact_detail_id : .contact_detail_address
        contactTypeStringLabel.text = c.detail
        remarkInputView.textField.text = c.remark
        nameInputView.textField.text = c.name
    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    
    @IBAction func saveAction(_ sender: Any) {
        updateContact()
    }
    @IBAction func deleteAction(_ sender: Any) {
        let alert = UIAlertController(title: "提示", message: "确定要删除该联系人吗？", preferredStyle: .alert)
        
        alert.addAction(UIAlertAction(title: "删除", style: .destructive, handler: { (action) in
            self.deleteContact()
        }))
        
        alert.addAction(UIAlertAction(title: "取消", style: .cancel, handler: nil))
        
        present(alert, animated: true, completion: nil)
    }
    
    func checkName() -> String? {
        return check(str: nameInputView.textField.text, minLength: 1, maxLength: 30, key: "名称")
    }
    
    func checkRemark() -> String? {
        return check(str: remarkInputView.textField.text, minLength: 1, maxLength: 100, key: "备注", required: false)
    }
    
    func check(str: String?, minLength: Int, maxLength: Int, key: String, required: Bool = true) -> String? {
        guard let s = str, !s.isEmpty else {
            if required {
                return "请输入\(key)"
            }
            return nil
        }
        var toReturn: String? = nil
        switch s.count {
        case minLength - 1:
            toReturn = "请输入\(key)"
        case let i where i > maxLength:
            toReturn = "\(key)支持\(minLength)-\(maxLength)位"
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
            nameInputView.show(tip: "名称不能为空", type: .error)
            return
        }
        
        guard name != contact?.name || remarkInputView.textField.text != contact?.remark else {
            showTipAlert(msg: "您没有做任何修改")
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
            self.bil_showSuccess(status: "更新成功")
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
