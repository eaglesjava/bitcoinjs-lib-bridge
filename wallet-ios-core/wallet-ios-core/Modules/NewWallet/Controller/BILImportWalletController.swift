//
//  BILImportWalletController.swift
//  wallet-ios-core
//
//  Created by 仇弘扬 on 2017/11/21.
//  Copyright © 2017年 BitBill. All rights reserved.
//

import UIKit
import SVProgressHUD

class BILImportWalletController: BILBaseViewController, UITextViewDelegate {

	@IBOutlet weak var textView: UITextView!
	@IBOutlet weak var mnemonicView: BILMnemonicView!
	
	let emptyTitle = "点击以输入"
	let sugueID = "BILMnemonicToNewWalletSegue"
	
	override func viewDidLoad() {
        super.viewDidLoad()

        // Do any additional setup after loading the view.
		self.mnemonicView.emptyTitle = emptyTitle
//		textView.text = "seed sock milk update focus rotate barely fade car face mechanic mercy"
    }
	
	override func viewWillAppear(_ animated: Bool) {
		super.viewWillAppear(animated)
		if presentationController == nil {
			navigationItem.rightBarButtonItem = nil
		}
	}
	
	override func viewDidAppear(_ animated: Bool) {
		super.viewDidAppear(animated)
//        textView.text = "两 览 藏 微 储 继 料 叶 历 跳 语 握"
		textView.becomeFirstResponder()
	}

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    
    func normalized(mnemonic: String) -> String? {
        var trimmedString = mnemonic.trimmingCharacters(in: .whitespaces)
        do {
            let regex = try NSRegularExpression(pattern: "  +", options: .caseInsensitive)
            let str = NSMutableString(string: trimmedString)
            _ = regex.replaceMatches(in: str, options: .reportCompletion, range: NSMakeRange(0, trimmedString.count), withTemplate: " ")
            trimmedString = String(str)
        } catch {
            debugPrint(error)
        }
        let words = trimmedString.components(separatedBy: " ")
        let lengths = [12, 15, 18, 21, 24]
        guard lengths.contains(words.count) else {
            debugPrint("长度不符合")
            return nil
        }
        
        return words.joined(separator: " ")
    }
    
	@IBAction func nextAction(_ sender: Any) {
		view.endEditing(true)
        
        guard let text = textView.text, !text.isEmpty else {
            SVProgressHUD.showInfo(withStatus: "请输入助记词")
            SVProgressHUD.dismiss(withDelay: 1.2, completion: {
                self.textView.becomeFirstResponder()
            })
            return
        }
        guard let mnemonic = normalized(mnemonic: textView.text) else {
            SVProgressHUD.showError(withStatus: "助记词格式化失败")
            return
        }
        
        SVProgressHUD.show(withStatus: "校验助记词。。。")
        
        DispatchQueue.main.asyncAfter(deadline: .now() + DispatchTimeInterval.seconds(1)) {
            
            func segueSender(mnemonic: String, walletID: String?) -> (mnemonic: String, walletID: String?) {
                return (mnemonic, walletID)
            }
            // TODO: 本地校验是否存在该助记词，服务器校验
            BitcoinJSBridge.shared.validateMnemonic(mnemonic: mnemonic, success: { (result) in
                let isValidate = result as! Bool
                if isValidate {
                    if let wallet = WalletModel.fetch(mnemonicHash: mnemonic.md5()) {
                        debugPrint("钱包已存在在本地")
                        let alert = UIAlertController(title: "钱包已存在", message: "是否重置密码", preferredStyle: .alert)
                        alert.addAction(UIAlertAction(title: "取消", style: .cancel, handler: { (action) in
                            self.bil_dismissSelfModalViewController()
                        }))
                        alert.addAction(UIAlertAction(title: "重置", style: .destructive, handler: { (action) in
                            self.performSegue(withIdentifier: self.sugueID, sender: segueSender(mnemonic: mnemonic, walletID: wallet.id))
                        }))
                        self.present(alert, animated: true, completion: nil)
                        SVProgressHUD.dismiss()
                    }
                    else
                    {
                        BitcoinJSBridge.shared.getMasterXPublicKey(mnemonic: mnemonic, success: { (pubKey) in
                            WalletModel.getWalletIDFromSever(mainExtPublicKey: pubKey as! String, success: { (id) in
                                self.performSegue(withIdentifier: self.sugueID, sender: segueSender(mnemonic: mnemonic, walletID: id))
                                SVProgressHUD.dismiss()
                            }, failure: { (errorMsg, code) in
                                SVProgressHUD.showError(withStatus: "请求失败，请稍后再试")
                            })
                        }, failure: { (error) in
                            SVProgressHUD.dismiss()
                            self.performSegue(withIdentifier: self.sugueID, sender: segueSender(mnemonic: mnemonic, walletID: nil))
                        })
                    }
                }
                else
                {
                    print("不是合法的助记词")
                    SVProgressHUD.showError(withStatus: "不是合法的助记词")
                }
            }) { (error) in
                print("校验助记词失败")
                SVProgressHUD.showError(withStatus: "校验助记词失败")
            }
        }
        
	}
	
	public func textViewDidBeginEditing(_ textView: UITextView) {
		mnemonicView.emptyTitle = nil
	}
	
	public func textViewDidEndEditing(_ textView: UITextView) {
		guard let text = textView.text, text.count == 0 else {
			return
		}
		mnemonicView.emptyTitle = emptyTitle
	}
	
    // MARK: - Navigation

    // In a storyboard-based application, you will often want to do a little preparation before navigation
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        // Get the new view controller using segue.destinationViewController.
        // Pass the selected object to the new view controller.
		if segue.identifier == sugueID {
			let cont = segue.destination as! BILCreateWalletViewController
            cont.navigationItem.rightBarButtonItem = nil
            guard let s = sender as? (mnemonic: String, walletID: String?) else {
                return
            }
			cont.mnemonic = s.mnemonic
            cont.walletID = s.walletID
		}
    }

}
