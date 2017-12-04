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
    
	@IBAction func nextAction(_ sender: Any) {
		
		guard let input = textView.text else { return }
		var trimmedString = input.trimmingCharacters(in: .whitespaces)
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
			print("长度不符合")
			SVProgressHUD.showError(withStatus: "长度不符合")
			return
		}
		
		let mnemonic = words.joined(separator: " ")
		
		BitcoinJSBridge.shared.validateMnemonic(mnemonic: mnemonic, success: { (result) in
			let isValidate = result as! Bool
			if isValidate {
//				self.textView.isHidden = true
//				self.mnemonicView.mnemonic = mnemonic
				self.performSegue(withIdentifier: self.sugueID, sender: mnemonic)
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
			cont.mnemonic = sender as? String
		}
    }

}
