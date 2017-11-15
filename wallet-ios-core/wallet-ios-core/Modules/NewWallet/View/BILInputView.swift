//
//  BILInputView.swift
//  wallet-ios-core
//
//  Created by 仇弘扬 on 2017/11/14.
//  Copyright © 2017年 BitBill. All rights reserved.
//

import UIKit

class BILInputView: UIView {
	
	enum TipType {
		case normal
		case error
	}
	
	@IBOutlet weak var textField: UITextField!
	@IBOutlet weak var title: UILabel!
	@IBOutlet weak var line: UIView!

	func show(tip: String, type: TipType) {
		title.text = tip
		var color: UIColor
		var lineColor: UIColor
		switch type {
		case .error:
			color = UIColor(hex: 0xFD6D73)
			lineColor = color
		default:
			color = UIColor.white
			lineColor = UIColor(hex: 0xF2F2F2)
		}
		line.backgroundColor = color
		title.textColor = lineColor
	}
	
    /*
    // Only override draw() if you perform custom drawing.
    // An empty implementation adversely affects performance during animation.
    override func draw(_ rect: CGRect) {
        // Drawing code
    }
    */

}
