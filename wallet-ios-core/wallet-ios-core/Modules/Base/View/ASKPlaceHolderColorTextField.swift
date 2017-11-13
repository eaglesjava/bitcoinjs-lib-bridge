//
//  ASKPlaceHolderColorTextField.swift
//  Wallet
//
//  Created by 仇弘扬 on 2017/9/11.
//  Copyright © 2017年 askcoin. All rights reserved.
//

import UIKit

class ASKPlaceHolderColorTextField: UITextField {

	@IBInspectable var placeholderColor: UIColor?
	{
		didSet {
			if let c = placeholderColor {
				setValue(c, forKeyPath: "_placeholderLabel.textColor")
			}
		}
	}
	
    /*
    // Only override draw() if you perform custom drawing.
    // An empty implementation adversely affects performance during animation.
    override func draw(_ rect: CGRect) {
        // Drawing code
    }
    */

}
