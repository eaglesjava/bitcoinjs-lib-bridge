//
//  UIControl+ASKGradient.swift
//  Wallet
//
//  Created by 仇弘扬 on 2017/9/5.
//  Copyright © 2017年 askcoin. All rights reserved.
//

import Foundation
import UIKit

@IBDesignable
class ASKGradientView: UIView {

	let breathAnimationKey = "breathAnimationKey"
	
	@IBInspectable
	var startColor: UIColor = UIColor.bil_gradient_start_color
	@IBInspectable
	var endColor: UIColor = UIColor.bil_gradient_end_color
	@IBInspectable
	var startPoint: CGPoint = CGPoint(x: 0, y: 0.5)
	@IBInspectable
	var endPoint: CGPoint = CGPoint(x: 1, y: 0.5)
	
	var gradientLayer: CAGradientLayer?
	
	override func awakeFromNib() {
		super.awakeFromNib()
		gradientLayer = setupGradient(
			colors: [startColor.cgColor, endColor.cgColor],
			shadowOpacity: 0,
			shadowRadius: 0,
			shadowOffset: CGSize(width: 0, height: 0),
			startPoint: startPoint,
			endPoint: endPoint
		)
	}
	override func layoutSubviews() {
		super.layoutSubviews()
		gradientLayer?.frame = bounds
	}
}

class ASKGradientShadowView: ASKGradientView {
	override func awakeFromNib() {
		super.awakeFromNib()
		if let g = gradientLayer {
			g.removeFromSuperlayer()
		}
		gradientLayer = setupGradient(
			colors: [startColor.cgColor, endColor.cgColor],
			shadowOpacity: 0.6,
			shadowOffset: CGSize(width: 0, height: 4),
			startPoint: startPoint,
			endPoint: endPoint
		)
	}
	func startBreath() {
		guard gradientLayer != nil else {
			return
		}
		let animaiton = CABasicAnimation(keyPath: "shadowOpacity")
		animaiton.timingFunction = CAMediaTimingFunction(name: kCAMediaTimingFunctionEaseInEaseOut)
		animaiton.duration = 2;
		animaiton.fromValue = NSNumber(value: 0.0)
		animaiton.toValue = NSNumber(value: 0.6)
		animaiton.autoreverses = true
		animaiton.repeatCount = .greatestFiniteMagnitude
		gradientLayer!.add(animaiton, forKey: breathAnimationKey)
	}
	
	func stopBreath() {
		guard gradientLayer != nil else {
			return
		}
		gradientLayer!.removeAnimation(forKey: breathAnimationKey)
	}
}

extension UIView {
	func setupGradient(
		colors: [CGColor] = [UIColor.bil_gradient_start_color.cgColor, UIColor.bil_gradient_end_color.cgColor],
		shadowColor: UIColor = UIColor.bil_gradient_shadow_color,
		cornerRadius: CGFloat = 2.0,
		shadowOpacity: Float = 0.55,
		shadowRadius: CGFloat = 0,
		shadowOffset: CGSize = CGSize(width: 0, height: 0),
		startPoint: CGPoint = CGPoint(x: 0, y: 0),
		endPoint: CGPoint = CGPoint(x: 1, y: 1)
		) -> CAGradientLayer {
		let gr = CAGradientLayer()
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
		layer.insertSublayer(gr, at: 0)
		return gr
	}
}
