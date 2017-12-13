//
//  BILSendConfirmController.swift
//  wallet-ios-core
//
//  Created by 仇弘扬 on 2017/12/12.
//  Copyright © 2017年 BitBill. All rights reserved.
//

import UIKit

class BILSendConfirmController: BILBaseViewController {
    @IBOutlet weak var addressLabel: UILabel!
    @IBOutlet weak var amountLabel: UILabel!
    @IBOutlet weak var walletIDLabel: UILabel!
    
    var sendModel: BILSendModel?
    
    override func viewDidLoad() {
        super.viewDidLoad()

        // Do any additional setup after loading the view.
        if let model = sendModel {
            walletIDLabel.text = model.wallet?.id
            addressLabel.text = model.address
            amountLabel.text = "\(model.bitcoinAmount) BTC"
        }
    }

    @IBAction func nextAction(_ sender: Any) {
        guard let wallet = sendModel?.wallet else {
            showTipAlert(title: "发送失败", msg: "内部数据错误")
            return
        }
        
        let alert = UIAlertController(title: "输入钱包密码", message: nil, preferredStyle: .alert)
        
        let ok = UIAlertAction(title: "确认", style: .default) { (action) in
            guard let pwd = alert.textFields?.first?.text, !pwd.isEmpty else {
                self.showAlertForFail("密码不能为空")
                return
            }
            if !wallet.checkPassword(pwd: pwd) {
                self.showAlertForFail("密码输入错误")
                return
            }
            self.send(password: pwd)
        }
        let cancel = UIAlertAction(title: "取消", style: .cancel) { (action) in
            
        }
        alert.addAction(ok)
        alert.addAction(cancel)
        
        alert.addTextField { (textField) in
            textField.placeholder = "请输入密码以确认"
            textField.isSecureTextEntry = true
        }
        
        present(alert, animated: true, completion: nil)
    }
    
    func send(password: String) {
        sendSuccess()
    }
    
    func sendSuccess() {
        guard let cont = storyboard?.instantiateViewController(withIdentifier: "BILSendResultController") as? BILSendResultController else { return }
        cont.sendModel = sendModel
        present(cont, animated: true) {
            self.navigationController?.popToRootViewController(animated: false)
        }
    }
    
    func showAlertForFail(_ msg: String = "请稍后再试") {
        let alert = UIAlertController(title: "发送失败", message: msg, preferredStyle: .alert)
        
        let ok = UIAlertAction(title: "确认", style: .default) { (action) in
            BILControllerManager.shared.showMainTabBarController()
        }
        alert.addAction(ok)
        
        present(alert, animated: true, completion: nil)
    }
    
    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    

    // MARK: - Navigation

    // In a storyboard-based application, you will often want to do a little preparation before navigation
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        // Get the new view controller using segue.destinationViewController.
        // Pass the selected object to the new view controller.
        
    }

}
