//
//  BILGuideKeyView.swift
//  wallet-ios-core
//
//  Created by 仇弘扬 on 2017/11/20.
//  Copyright © 2017年 BitBill. All rights reserved.
//

import UIKit

class BILGuideKeyView: BILGuideBaseView {

    @IBOutlet weak var keyImageViewBottomSpace: NSLayoutConstraint!
	
	override func awakeFromNib() {
		super.awakeFromNib()
		titleLabel.text = "Own your private keys".bil_ui_localized
	}
    
    override func adjust(frame: CGRect, index: Int) {
        super.adjust(frame: frame, index: index)
        keyImageViewBottomSpace.constant = BILAppStartUpManager.shared.isSmallScreen ? 80 : 100
    }
    
    /*
    // Only override draw() if you perform custom drawing.
    // An empty implementation adversely affects performance during animation.
    override func draw(_ rect: CGRect) {
        // Drawing code
    }
    */

}
