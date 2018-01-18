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
            let red = UIColor(hex: 0xF64048)
			let white = UIColor.white
			let yellow = UIColor.yellow
			let green = UIColor(hex: 0xABE64D)
			switch strength {
			case .low:
				colors = [red, white, white]
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
	
	static func caculatePasswordStrength(pwd: String) -> PasswordStrength{
		
		guard !pwd.isEmpty else {
			return .none
		}
		
		var sum = 0
		let max = 10000
		
		// 长度
		switch pwd.count {
		case 0...4:
			sum += 5
		case 5...7:
			sum += 10
		case 8...max:
			sum += 25
		default: ()
		}
		
		func count(string: String, pattern: String) throws -> Int {
			let exp = try NSRegularExpression(pattern: pattern, options: .allowCommentsAndWhitespace)
			let count = exp.numberOfMatches(in: string, options: .reportCompletion, range: NSMakeRange(0, string.count))
			return count
		}
		
		do {
			
			let numCount = try count(string: pwd, pattern: "[0-9]")
			let lowerCount = try count(string: pwd, pattern: "[a-z]")
			let upperCount = try count(string: pwd, pattern: "[A-Z]")
			let otherCount = pwd.count - numCount - lowerCount - upperCount
			
			// 字母
			if lowerCount + upperCount != 0 {
				if lowerCount == 0 || upperCount == 0 {
					sum += 10
				}
				else
				{
					sum += 20
				}
			}
			
			// 数字
			switch numCount {
			case 1:
				sum += 10
			case 2...max:
				sum += 20
			case 0: fallthrough
			default:
				sum += 0
			}
			
			// 奖励
			if numCount != 0 && lowerCount + upperCount != 0 {
				sum += 2
				if otherCount != 0 {
					sum += 1
					if lowerCount != 0 && upperCount != 0 {
						sum += 2
					}
				}
			}
			
		} catch {
			debugPrint(error)
		}
		
		switch sum {
		case 0..<50:
			return .low
		case 50..<60:
			return .medium
		case 60..<max:
			return .high
		default:
			return .none
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
