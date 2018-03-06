//
//  BILFeedbackViewController.swift
//  wallet-ios-core
//
//  Created by 仇弘扬 on 2018/1/29.
//  Copyright © 2018年 BitBill. All rights reserved.
//

import UIKit
import IQKeyboardManagerSwift

class BILFeedbackViewController: BILBaseViewController, UITextViewDelegate, BILInputViewDelegate {

    @IBOutlet weak var containerView: UIView!
    @IBOutlet weak var contactInputView: BILInputView!
    @IBOutlet weak var sendItem: UIBarButtonItem!
    @IBOutlet weak var contentTextView: IQTextView!
    
    override func viewDidLoad() {
        super.viewDidLoad()

        // Do any additional setup after loading the view.
        containerView.layer.borderWidth = 1
        resetMnemonicViewBorderColor()
        containerView.layer.cornerRadius = 2.0
        
        contactInputView.delegate = self
    }
    
    override func viewDidAppear(_ animated: Bool) {
        super.viewDidAppear(animated)
        contentTextView.becomeFirstResponder()
    }
    
    override func languageDidChanged() {
        super.languageDidChanged()
        title = "Feedback".bil_ui_localized
        contactInputView.updateTitleString("ContactTypeTitle".bil_ui_localized)
        contactInputView.textField.placeholder = "ContactTypePlaceHolder".bil_ui_localized
        contentTextView.placeholder = "Please input".bil_ui_localized
        sendItem.title = "Send".bil_ui_localized
    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    
    func textViewDidChange(_ textView: UITextView) {
        resetMnemonicViewBorderColor(alpha: 1.0)
    }
    
    public func textViewDidBeginEditing(_ textView: UITextView) {
        resetMnemonicViewBorderColor(alpha: 1.0)
    }
    
    public func textViewDidEndEditing(_ textView: UITextView) {
        guard let text = textView.text, text.count == 0 else {
            return
        }
        resetMnemonicViewBorderColor()
    }
    
    func resetMnemonicViewBorderColor(alpha: CGFloat = 0.3) {
        containerView.layer.borderColor = UIColor(white: 1.0, alpha: alpha).cgColor
    }
    
    func textFieldShouldReturn(_ textField: UITextField) -> Bool {
        commitAction(nil)
        return true
    }
    
    @IBAction func commitAction(_ sender: Any?) {
        let contact = contactInputView.textField.text ?? ""
        guard let content = contentTextView.text, !content.isEmpty else {
            showTipAlert(msg: "Can not be empty".bil_ui_localized)
            return
        }
        guard content.count <= 200 else {
            showTipAlert(msg: "Too much words".bil_ui_localized)
            return
        }
        bil_showLoading()
        BILNetworkManager.request(request: .feedback(content: content, contact: contact), success: { (result) in
            self.bil_showSuccess(status: nil, complete: {
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
