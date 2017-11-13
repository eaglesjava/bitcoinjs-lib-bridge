//
//  BILGradientButton.swift
//  wallet-ios-core
//
//  Created by 仇弘扬 on 2017/11/13.
//  Copyright © 2017年 BitBill. All rights reserved.
//

import UIKit

class BILGradientButton: UIButton {

	var gradientLayer: CAGradientLayer?
	
	override func awakeFromNib() {
		super.awakeFromNib()
		gradientLayer = setupGradient()
	}
	override func layoutSubviews() {
		super.layoutSubviews()
		gradientLayer?.frame = bounds
		gradientLayer?.cornerRadius = bounds.height / 2
	}
	
    /*
    // Only override draw() if you perform custom drawing.
    // An empty implementation adversely affects performance during animation.
    override func draw(_ rect: CGRect) {
        // Drawing code
    }
    */

}
