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
                    unownedSelf.bil_makeToast(msg: "地址已存在")
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
        return check(str: nameInputView.textField.text, minLength: 1, maxLength: 30, key: "名称")
    }
    
    func checkRemark() -> String? {
        return check(str: remarkInputView.textField.text, minLength: 1, maxLength: 100, key: "名称", required: false)
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
    
    func addContact() {
        guard let name = nameInputView.textField.text else {
            nameInputView.show(tip: "名称不能为空", type: .error)
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
            showTipAlert(title: nil, msg: "地址不能为空", dismissed: {
                self.addressInputView.textField.becomeFirstResponder()
            })
            return
        }
        
        guard !ContactModel.isAddressExits(address: address) else {
            showTipAlert(msg: "地址已存在")
            return
        }
        
        BitcoinJSBridge.shared.validateAddress(address: address, success: { (result) in
            debugPrint(result)
            let isValidate = result as! Bool
            if isValidate {
                self.bil_showLoading(status: nil)
                ContactModel.addContactToServer(id: "", address: address, name: name, remark: remark, success: { (contact) in
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
                    self.bil_dismissHUD()
                    self.bil_makeToast(msg: msg)
                }
            } else {
                self.bil_dismissHUD()
                self.addressInputView.show(tip: "不是合法的地址", type: .error)
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
                addressInputView.show(tip: "地址不能为空", type: .error)
                return false
            }
            guard !ContactModel.isAddressExits(address: address) else {
                addressInputView.show(tip: "地址已存在", type: .error)
                return false
            }
            BitcoinJSBridge.shared.validateAddress(address: address, success: { (result) in
                debugPrint(result)
                let isValidate = result as! Bool
                if isValidate {
                    self.remarkInputView.textField.becomeFirstResponder()
                } else {
                    self.addressInputView.show(tip: "不是合法的地址", type: .error)
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
                nameInputView.show(tip: "名称", type: .normal)
            case addressInputView.textField:
                addressInputView.show(tip: "地址", type: .normal)
            case remarkInputView.textField:
                remarkInputView.show(tip: "备注", type: .normal)
            default: ()
            }
        }
    }
}
