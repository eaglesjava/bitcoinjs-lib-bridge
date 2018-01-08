//
//  UIColor+bilHex.swift
//  Wallet
//
//  Created by 仇弘扬 on 2017/9/5.
//  Copyright © 2017年 bilcoin. All rights reserved.
//

import Foundation
import UIKit

extension UIColor {
	convenience init(bil_r: Int, bil_g: Int, bil_b: Int, bil_a: CGFloat = 1.0) {
		self.init(red: CGFloat(bil_r) / 255.0, green: CGFloat(bil_g) / 255.0, blue: CGFloat(bil_b) / 255.0, alpha: bil_a)
	}
	
	convenience init(hex: Int, bil_a: CGFloat = 1.0) {
		self.init(bil_r: (hex & 0xFF0000) >> 16, bil_g: (hex & 0xFF00) >> 8, bil_b: (hex & 0xFF), bil_a: bil_a)
	}
}

extension UIColor {
	static var bil_gradient_start_color: UIColor { get { return UIColor(hex: 0x2991DD) } }
	static var bil_gradient_end_color: UIColor { get { return UIColor(hex: 0x113CA6) } }
	static var bil_gradient_start_red_color: UIColor { get { return UIColor(hex: 0xFF626C) } }
	static var bil_gradient_end_red_color: UIColor { get { return UIColor(hex: 0xFF454D) } }
	static var bil_gradient_shadow_color: UIColor { get { return UIColor(hex: 0xB4EC51) } }
	static var bil_black_30_color: UIColor { get { return self.bil_black_color(alpha: 0.3) } }
	static var bil_black_70_color: UIColor { get { return self.bil_black_color(alpha: 0.7) } }
	static var bil_deep_blue_start_bgcolor: UIColor{ get { return UIColor(hex: 0x1C1C1C) } }
	static var bil_deep_blue_end_bgcolor: UIColor{ get { return UIColor(hex: 0x070707) } }
	
	static var bil_white_40_color: UIColor { get { return UIColor(white: 1.0, alpha: 0.4) } }
	static var bil_white_60_color: UIColor { get { return UIColor(white: 1.0, alpha: 0.4) } }
	
	static func bil_black_color(alpha: CGFloat) -> UIColor {
		return UIColor(hex: 0x000000, bil_a: alpha)
	}
}
