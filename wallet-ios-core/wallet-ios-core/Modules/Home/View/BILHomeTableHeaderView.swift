//
//  BILHomeTableHeaderView.swift
//  wallet-ios-core
//
//  Created by 仇弘扬 on 2017/11/24.
//  Copyright © 2017年 BitBill. All rights reserved.
//

import UIKit

class BILHomeTableHeaderView: UITableViewHeaderFooterView {

	@IBOutlet weak var titleLabel: UILabel!
	@IBOutlet weak var subTitleLabel: UILabel!
	@IBOutlet weak var actionButton: UIButton!
	
	var buttonImage: UIImage?
	
	override func awakeFromNib() {
		super.awakeFromNib()
		if let bi = buttonImage {
			actionButton.setImage(bi, for: .normal)
		}
		actionButton.isHidden = buttonImage != nil
	}
	
	/*
    // Only override draw() if you perform custom drawing.
    // An empty implementation adversely affects performance during animation.
    override func draw(_ rect: CGRect) {
        // Drawing code
    }
    */

}
