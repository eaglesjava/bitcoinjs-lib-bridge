//
//  BILGuideBaseView.swift
//  wallet-ios-core
//
//  Created by 仇弘扬 on 2017/11/20.
//  Copyright © 2017年 BitBill. All rights reserved.
//

import UIKit

protocol BILGuideViewProtocol {
	func adjust(contentOffset: CGPoint)
	func adjust(frame: CGRect, index: Int)
}

class BILGuideBaseView: UIView, BILGuideViewProtocol {
	@IBOutlet weak var titleLabel: UILabel!
	private var index = 0
	var items = [BILGuideItem]()
	var itemsViews = [UIView]()
	
	override func awakeFromNib() {
		super.awakeFromNib()
		setupItems()
		addItemsViews()
		backgroundColor = UIColor.clear
		adjust(contentOffset: CGPoint.zero)
	}
	
	func setupItems() {
		
	}
	
	func addItemsViews() {
		for item in items {
			if item is BILGuideImageItem {
				let v = (item as! BILGuideImageItem).imageView
				v.center = item.center(in: bounds, percent: 1)
				v.alpha = item.alpha(percent: 0)
				addSubview(v)
			}
		}
	}
	
	func adjust(contentOffset: CGPoint) {
		let percent: CGFloat = (contentOffset.x - bounds.width * CGFloat(index)) / bounds.width
		for item in items {
			if item is BILGuideImageItem {
				let v = (item as! BILGuideImageItem).imageView
				v.alpha = item.alpha(percent: 1 - percent)
				v.center = item.center(in: bounds, percent: percent)
			}
		}
	}
	
	func adjust(frame: CGRect, index: Int) {
		var f = frame
		f.origin.x = CGFloat(index) * frame.width
		self.frame = f
		self.index = index
	}
	
    /*
    // Only override draw() if you perform custom drawing.
    // An empty implementation adversely affects performance during animation.
    override func draw(_ rect: CGRect) {
        // Drawing code
    }
    */

}
