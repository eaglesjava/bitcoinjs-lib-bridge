//
//  BILGuideItem.swift
//  wallet-ios-core
//
//  Created by 仇弘扬 on 2017/11/20.
//  Copyright © 2017年 BitBill. All rights reserved.
//

import UIKit

class BILGuideItem {
	var endPoint: CGPoint
	var startPoint: CGPoint
	
	var startAlpha: CGFloat = 1
	var endAlpha: CGFloat = 1
	
	var updown = true
	
	enum LayoutDirection {
		case top
		case bottom
	}
	
	var layoutDirection = LayoutDirection.bottom
	
	init(startPoint: CGPoint, endPoint: CGPoint) {
		self.startPoint = startPoint
		self.endPoint = endPoint
	}
	
	func adjust(percent: CGFloat) {
		
	}
	
	func center(in frame: CGRect, percent: CGFloat) -> CGPoint {
		var p = CGPoint.zero
		p.x = startPoint.x + (endPoint.x - startPoint.x) * percent
		p.y = startPoint.y + (endPoint.y - startPoint.y) * percent
		if layoutDirection == .bottom {
			p.y = frame.height - p.y
		}
		return p
	}
	
	func alpha(percent: CGFloat) -> CGFloat {
		return startAlpha == endAlpha ? startAlpha : percent * (abs(endAlpha - startAlpha))
	}
}

class BILGuideImageItem: BILGuideItem {
	var image: UIImage
	lazy var imageView: UIImageView = {
		let v = UIImageView(image: image)
		return v
	}()
	
	init(startPoint: CGPoint, endPoint: CGPoint, image: UIImage) {
		self.image = image
		super.init(startPoint: startPoint, endPoint: endPoint)
	}
}
