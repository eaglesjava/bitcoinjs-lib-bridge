//
//  BILSpecificVolumeRecieveInputController.swift
//  wallet-ios-core
//
//  Created by 仇弘扬 on 2017/12/8.
//  Copyright © 2017年 BitBill. All rights reserved.
//

import UIKit

class BILSpecificVolumeRecieveInputController: BILBaseViewController {
	
	var recieveModel: BILRecieveModel?
	@IBOutlet weak var coinNameLabel: UILabel!
	@IBOutlet weak var textField: ASKPlaceHolderColorTextField!
	
    override func viewDidLoad() {
        super.viewDidLoad()

        // Do any additional setup after loading the view.
		if let r = recieveModel {
			coinNameLabel.text  = r.coinType.name
		}
    }
	
	override func viewDidAppear(_ animated: Bool) {
		super.viewDidAppear(animated)
		textField.becomeFirstResponder()
	}

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    

    // MARK: - Navigation

	override func shouldPerformSegue(withIdentifier identifier: String, sender: Any?) -> Bool {
		switch identifier {
		case "BILVolumeToQRCodeSegue":
			guard let _ = recieveModel else {
				return false
			}
			guard let text = textField.text, !text.isEmpty else {
				showTipAlert(title: "提示", msg: "请输入金额")
				return false
			}
			
			guard let btcValue = Double(text), btcValue > 0 else {
				showTipAlert(title: "提示", msg: "请输入大于 0 的金额")
				return false
			}
			recieveModel?.volume = text
			return true
		default:
			return true
		}
	}
	
    // In a storyboard-based application, you will often want to do a little preparation before navigation
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        // Get the new view controller using segue.destinationViewController.
        // Pass the selected object to the new view controller.
		guard let id = segue.identifier else { return }
		switch id {
		case "BILVolumeToQRCodeSegue":
			let cont = segue.destination as! BILSpecificVolumeRecieveController
			cont.recieveModel = recieveModel
		default:
			()
		}
    }
	

}
