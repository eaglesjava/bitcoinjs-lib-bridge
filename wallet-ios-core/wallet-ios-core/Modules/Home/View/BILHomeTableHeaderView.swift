//
//  BILHomeTableHeaderView.swift
//  wallet-ios-core
//
//  Created by 仇弘扬 on 2017/11/24.
//  Copyright © 2017年 BitBill. All rights reserved.
//

import UIKit

protocol BILHomeTableHeaderViewDelegate: NSObjectProtocol {
	func actionButtonTapped(headerView: BILHomeTableHeaderView)
}

class BILHomeTableHeaderView: UITableViewHeaderFooterView {

	@IBOutlet weak var titleLabel: UILabel!
	@IBOutlet weak var subTitleLabel: UILabel!
	@IBOutlet weak var actionButton: UIButton!
	@IBOutlet weak var bgImageView: UIImageView!
	
	weak var delegate: BILHomeTableHeaderViewDelegate?
	
	var buttonImage: UIImage? {
		didSet {
			if let bi = buttonImage {
				actionButton.setImage(bi, for: .normal)
			}
			actionButton.isHidden = buttonImage == nil
		}
	}
	
	override func awakeFromNib() {
		super.awakeFromNib()
	}
	@IBAction func actionButtonTapped(_ sender: Any) {
		delegate?.actionButtonTapped(headerView: self)
	}
	
	/*
    // Only override draw() if you perform custom drawing.
    // An empty implementation adversely affects performance during animation.
    override func draw(_ rect: CGRect) {
        // Drawing code
    }
    */

}
