//
//  BILGradientView.swift
//  wallet-ios-core
//
//  Created by 仇弘扬 on 2017/11/24.
//  Copyright © 2017年 BitBill. All rights reserved.
//

import UIKit

class BILGradientView: UIView {
	
	var gradientLayer: CAGradientLayer?
	
	override func awakeFromNib() {
		super.awakeFromNib()
		gradientLayer = setupGradient(startPoint: CGPoint(x: 0, y: 0.5),
									  endPoint: CGPoint(x: 1, y: 0.5))
		
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
