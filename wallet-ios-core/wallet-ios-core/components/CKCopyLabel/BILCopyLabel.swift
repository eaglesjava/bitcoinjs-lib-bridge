//
//  BILCopyLabel.swift
//  wallet-ios-core
//
//  Created by 仇弘扬 on 2017/12/11.
//  Copyright © 2017年 BitBill. All rights reserved.
//

import UIKit
import Toast_Swift

extension UIViewController {
    func bil_makeToast(msg: String?, completion: ((_ didTap: Bool) -> Void)? = nil) {
        view.makeToast(msg, completion: completion)
    }
}

extension UIView {
    func bil_makeToast(msg: String?, completion: ((_ didTap: Bool) -> Void)? = nil) {
		if let cont = viewController() {
			cont.bil_makeToast(msg: msg, completion: completion)
		}
		else
		{
			window?.makeToast(msg, completion: completion)
		}
    }
}

class BILCopyLabel: UILabel {
    
    @IBInspectable
    var valueTitle: String?

    override func awakeFromNib() {
        super.awakeFromNib()
        setupGesture()
    }
    
    override init(frame: CGRect) {
        super.init(frame: frame)
        setupGesture()
    }
    
    required init?(coder aDecoder: NSCoder) {
        super.init(coder: aDecoder)
        setupGesture()
    }
	
	func textForCopy() -> String {
		return text ?? ""
	}
    
    func setupGesture() {
        isUserInteractionEnabled = true
        
        let tap = UITapGestureRecognizer(target: self, action: #selector(tapAction(gesture:)))
        addGestureRecognizer(tap)
    }
    
    @objc
    func tapAction(gesture: UITapGestureRecognizer) {
        UIPasteboard.general.string = textForCopy()
        bil_makeToast(msg: "\(valueTitle ?? "")\(String.contact_detail_copied)")
    }
    
    /*
    // Only override draw() if you perform custom drawing.
    // An empty implementation adversely affects performance during animation.
    override func draw(_ rect: CGRect) {
        // Drawing code
    }
    */

}

final class BILAddressLabel: BILCopyLabel {
    override var valueTitle: String? {
        get {
            return String.contact_detail_address.capitalized
        }
        set {}
    }
	
	override func textForCopy() -> String {
		return super.textForCopy().replacingOccurrences(of: "My address".bil_ui_localized, with: "")
	}
}

final class BILTXHashLabel: BILCopyLabel {
    override var valueTitle: String? {
        get {
            return String.homeTxDetailHash
        }
        set {}
    }
}
