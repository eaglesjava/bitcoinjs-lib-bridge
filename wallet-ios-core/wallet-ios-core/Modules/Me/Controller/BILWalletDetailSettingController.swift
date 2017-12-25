//
//  BILWalletDetailSettingController.swift
//  wallet-ios-core
//
//  Created by 仇弘扬 on 2017/12/22.
//  Copyright © 2017年 BitBill. All rights reserved.
//

import UIKit
import DateToolsSwift

extension String {
    static var bil_meWalletDetailToBackupWalletSegue: String { return "BILMeWalletDetailToBackupSegue" }
    static var bil_meWalletDetailToAddressSegue: String { return "BILMeWalletDetailToAddressSegue" }
}

class BILWalletDetailSettingController: BILLightBlueBaseController {

	var wallet: WalletModel?
	
	@IBOutlet weak var idLabel: UILabel!
	@IBOutlet weak var dateLabel: UILabel!
	@IBOutlet weak var qrImageView: UIImageView!
	
    @IBOutlet weak var backupButton: BILWhiteBorderButton!
    @IBOutlet weak var walletAddressButton: BILWhiteBorderButton!
    override func viewDidLoad() {
        super.viewDidLoad()

        // Do any additional setup after loading the view.
		refreshUI()
        NotificationCenter.default.addObserver(self, selector: #selector(refreshUI), name: .walletDidChanged, object: nil)
    }
    
    deinit {
        NotificationCenter.default.removeObserver(self, name: .walletDidChanged, object: nil)
    }
    
    @objc
    func refreshUI() {
        if let w = wallet {
            navigationItem.title = w.id
            idLabel.text = w.id
            dateLabel.text = w.createDate?.format(with: "yyyy.MM.dd")
            let width = qrImageView.frame.width
            qrImageView.image = w.id_qrString.qrCodeImage(targetSize: CGSize(width: width, height: width))
            backupButton.isHidden = !w.isNeedBackup
        }
    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    
    func deleteWallet() {
        guard let wallet = self.wallet else { return }
        wallet.deleteWalletInSever(success: { (result) in
            
        }) { (msg, code) in
            self.showTipAlert(msg: "删除钱包失败")
        }
    }
    
    @IBAction func removeWalletAction(_ sender: Any) {
        guard let wallet = self.wallet else { return }
        let alert = UIAlertController(title: "删除钱包", message: "请谨慎使用此操作，删除前确认您的钱包已备份，否则数字资产将无法找回！", preferredStyle: .alert)
        
        let ok = UIAlertAction(title: "确认", style: .default) { (action) in
            guard let pwd = alert.textFields?.first?.text, !pwd.isEmpty else {
                self.showAlertForFail("密码不能为空")
                return
            }
            if !wallet.checkPassword(pwd: pwd) {
                self.showAlertForFail("密码输入错误")
                return
            }
            self.deleteWallet()
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
    
    func showAlertForFail(_ msg: String = "请稍后再试") {
        let alert = UIAlertController(title: "发送失败", message: msg, preferredStyle: .alert)
        
        let ok = UIAlertAction(title: "确认", style: .default) { (action) in
            BILControllerManager.shared.showMainTabBarController()
        }
        alert.addAction(ok)
        
        present(alert, animated: true, completion: nil)
    }
    
    // MARK: - Navigation

    // In a storyboard-based application, you will often want to do a little preparation before navigation
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        // Get the new view controller using segue.destinationViewController.
        // Pass the selected object to the new view controller.
        guard let id = segue.identifier else { return }
        switch id {
        case String.bil_meWalletDetailToBackupWalletSegue:
            guard let wallet = self.wallet else { return }
            if let cont = (segue.destination as? UINavigationController)?.viewControllers.first as? BILBackupWalletMnemonicController {
                cont.mnemonicHash = wallet.mnemonicHash
            }
        case .bil_meWalletDetailToAddressSegue:
            guard let wallet = self.wallet else { return }
            if let cont = segue.destination as? BILWalletAddressController {
                cont.wallet = wallet
            }
        default:
            ()
        }
    }

}
