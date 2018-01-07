//
//  BILSpecificVolumeRecieveInputController.swift
//  wallet-ios-core
//
//  Created by 仇弘扬 on 2017/12/8.
//  Copyright © 2017年 BitBill. All rights reserved.
//

import UIKit

class BILSpecificVolumeRecieveInputController: BILBaseViewController, UITextFieldDelegate {
	
	var recieveModel: BILRecieveModel?
    @IBOutlet weak var amountTextField: UITextField!
    @IBOutlet weak var cnyLabel: BILExchangeRateLabel!
    @IBOutlet weak var coinNameLabel: UILabel!
    @IBOutlet weak var nextButtonBottomSpace: NSLayoutConstraint!
	
    override func viewDidLoad() {
        super.viewDidLoad()

        // Do any additional setup after loading the view.
		if let r = recieveModel {
			coinNameLabel.text  = r.coinType.name
		}
        
        NotificationCenter.default.addObserver(self, selector: #selector(keyboardWillShow(_:)), name: .UIKeyboardWillShow, object: nil)
        NotificationCenter.default.addObserver(self, selector: #selector(keyboardWillHide(_:)), name: .UIKeyboardWillHide, object: nil)
        amountTextField.becomeFirstResponder()
    }
    
    @objc internal func keyboardWillShow(_ notification : Notification?) {
        guard let info = notification?.userInfo else { return }
        var animationCurve = UIViewAnimationOptions.curveEaseOut
        if let curve = info[UIKeyboardAnimationCurveUserInfoKey] as? UInt {
            animationCurve = UIViewAnimationOptions(rawValue: curve)
        }
        
        var animationDuration = 0.25
        //  Getting keyboard animation duration
        if let duration = info[UIKeyboardAnimationDurationUserInfoKey] as? TimeInterval {
            //Saving animation duration
            if duration != 0.0 {
                animationDuration = duration
            }
        }
        
        if let kbFrame = info[UIKeyboardFrameEndUserInfoKey] as? CGRect {
            UIView.animate(withDuration: animationDuration, delay: 0, options: animationCurve, animations: {
                self.nextButtonBottomSpace.constant = kbFrame.height + 30
            }, completion: { (finished) in
                
            })
        }
        
    }
    
    @objc internal func keyboardWillHide(_ notification : Notification?) {
        guard let info = notification?.userInfo else { return }
        var animationCurve = UIViewAnimationOptions.curveEaseOut
        if let curve = info[UIKeyboardAnimationCurveUserInfoKey] as? UInt {
            animationCurve = UIViewAnimationOptions(rawValue: curve)
        }
        
        var animationDuration = 0.25
        //  Getting keyboard animation duration
        if let duration = info[UIKeyboardAnimationDurationUserInfoKey] as? TimeInterval {
            //Saving animation duration
            if duration != 0.0 {
                animationDuration = duration
            }
        }
        
        UIView.animate(withDuration: animationDuration, delay: 0, options: animationCurve, animations: {
            self.nextButtonBottomSpace.constant = 30
        }, completion: { (finished) in
            
        })
    }
	
    func textField(_ textField: UITextField, shouldChangeCharactersIn range: NSRange, replacementString string: String) -> Bool {
        guard var text = textField.text else { return true }
        print(text)
        let rangeLocation =  text.index(text.startIndex, offsetBy: range.location)
        if range.length == 0 {
            text.insert(contentsOf: string, at:rangeLocation)
        } else {
            let upperLocation = text.index(rangeLocation, offsetBy: range.length)
            text.removeSubrange(rangeLocation..<upperLocation)
        }
        debugPrint("\(text)  \(range)    \(string)")
        
        if text.count > 30 {
            return false
        }
        
        if let coinAmount = Double(text) {
            cnyLabel.btcValue = coinAmount
        }
        
        if text.contains(".") {
            let array = text.components(separatedBy: ".")
            if array.count > 2 {
                return false
            }
            let decimalPlace = array[1]
            return decimalPlace.count <= "\(BTC_SATOSHI)".count - 1
        }
        
        return true
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
			guard let text = amountTextField.text, !text.isEmpty else {
				showTipAlert(title: .recieveSpecificCheckAmountTipTitle, msg: .recieveSpecificCheckAmountTipMessageEmpty)
				return false
			}
			
			guard let btcValue = Double(text), btcValue > 0 else {
				showTipAlert(title: .recieveSpecificCheckAmountTipTitle, msg: .recieveSpecificCheckAmountTipMessageZero)
				return false
			}
			recieveModel?.amount = text
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
