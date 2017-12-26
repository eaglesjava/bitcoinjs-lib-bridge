//
//  BILHomeShortcutButton.swift
//  wallet-ios-core
//
//  Created by 仇弘扬 on 2017/12/27.
//  Copyright © 2017年 BitBill. All rights reserved.
//

import UIKit

class BILHomeShortcutButton: UIButton {
	
	override func layoutSubviews() {
		super.layoutSubviews()
		let imageSize:CGSize = imageView!.frame.size
		let titleSize:CGSize = titleLabel!.frame.size
		let space: CGFloat = 12
		titleEdgeInsets = UIEdgeInsets(top: 0, left:-imageSize.width, bottom: -imageSize.height - space, right: 0)
		imageEdgeInsets = UIEdgeInsets(top: -titleSize.height - space, left: 0, bottom: 0, right: -titleSize.width)
	}
	
    /*
    // Only override draw() if you perform custom drawing.
    // An empty implementation adversely affects performance during animation.
    override func draw(_ rect: CGRect) {
        // Drawing code
    }
    */

}
