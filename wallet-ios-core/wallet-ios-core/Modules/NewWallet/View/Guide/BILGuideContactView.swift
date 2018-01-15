//
//  BILGuideContactView.swift
//  wallet-ios-core
//
//  Created by 仇弘扬 on 2017/12/4.
//  Copyright © 2017年 BitBill. All rights reserved.
//

import UIKit

class BILGuideContactView: BILGuideBaseView {

    @IBOutlet weak var bottomSpace: NSLayoutConstraint!
	
	override func awakeFromNib() {
		super.awakeFromNib()
		titleLabel.text = "Send coins by contacts".bil_ui_localized
	}
    
    override func adjust(frame: CGRect, index: Int) {
        super.adjust(frame: frame, index: index)
        bottomSpace.constant = BILAppStartUpManager.shared.isSmallScreen ? 55 : 73.5
    }
    
    /*
    // Only override draw() if you perform custom drawing.
    // An empty implementation adversely affects performance during animation.
    override func draw(_ rect: CGRect) {
        // Drawing code
    }
    */

}
