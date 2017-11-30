//
//  BILGradientView.swift
//  wallet-ios-core
//
//  Created by 仇弘扬 on 2017/11/24.
//  Copyright © 2017年 BitBill. All rights reserved.
//

import UIKit

class BILGradientView: UIView {
	
	override class var layerClass: AnyClass {
		get {
			return CAGradientLayer.self
		}
	}
	
	var gradientLayer: CAGradientLayer?
	
	override func awakeFromNib() {
		super.awakeFromNib()
		gradientLayer = bil_setupGradient(cornerRadius: layer.cornerRadius,
									  startPoint: CGPoint(x: 0, y: 0.5),
									  endPoint: CGPoint(x: 1, y: 0.5))
	}
	
	func bil_setupGradient(
		colors: [CGColor] = [UIColor.bil_gradient_start_color.cgColor, UIColor.bil_gradient_end_color.cgColor],
		shadowColor: UIColor = UIColor.bil_gradient_shadow_color,
		cornerRadius: CGFloat = 2.0,
		shadowOpacity: Float = 0.55,
		shadowRadius: CGFloat = 0,
		shadowOffset: CGSize = CGSize(width: 0, height: 0),
		startPoint: CGPoint = CGPoint(x: 0, y: 0),
		endPoint: CGPoint = CGPoint(x: 1, y: 1)
		) -> CAGradientLayer {
		let gr = layer as! CAGradientLayer
		gr.frame = bounds
		gr.startPoint = startPoint
		gr.endPoint = endPoint
		gr.colors = colors
		gr.cornerRadius = cornerRadius
		if shadowRadius > 0 {
			gr.shadowColor = shadowColor.cgColor
			gr.shadowOpacity = shadowOpacity
			gr.shadowRadius = shadowRadius
			gr.shadowOffset = shadowOffset
		}
		return gr
	}

    /*
    // Only override draw() if you perform custom drawing.
    // An empty implementation adversely affects performance during animation.
    override func draw(_ rect: CGRect) {
        // Drawing code
    }
    */

}
