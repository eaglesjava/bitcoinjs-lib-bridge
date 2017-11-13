//
//  BILPasswordStrengthView.swift
//  wallet-ios-core
//
//  Created by 仇弘扬 on 2017/11/13.
//  Copyright © 2017年 BitBill. All rights reserved.
//

import UIKit

class BILPasswordStrengthView: UIView {

	enum PasswordStrength {
		case none
		case low
		case medium
		case high
	}
	
	var strength: PasswordStrength = .none {
		didSet {
			var colors: [UIColor]
			let white = UIColor.white
			let yellow = UIColor.yellow
			let green = UIColor(hex: 0xABE64D)
			switch strength {
			case .low:
				colors = [yellow, white, white]
			case .medium:
				colors = [yellow, yellow, white]
			case .high:
				colors = Array(repeating: green, count: 3)
			case .none: fallthrough
			default:
				colors = Array(repeating: white, count: 3)
			}
			set(colors: colors)
		}
	}
	
	private func set(colors: [UIColor]) {
		guard colors.count == 3 else {
			return
		}
		
		for view in subviews {
			view.backgroundColor = colors[view.tag]
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
