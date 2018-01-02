//
//  BILTransactionCell.swift
//  wallet-ios-core
//
//  Created by 仇弘扬 on 2017/11/27.
//  Copyright © 2017年 BitBill. All rights reserved.
//

import UIKit

class BILTransactionCell: UITableViewCell {
	@IBOutlet weak var typeImageView: UIImageView!
	@IBOutlet weak var addressLabel: UILabel!
	@IBOutlet weak var dateLabel: UILabel!
	@IBOutlet weak var volumeLabel: UILabel!
    @IBOutlet weak var confirmLabel: UILabel!
    
	var transaction: BTCTransactionModel? {
		didSet {
			if let t = transaction {
                typeImageView.image = t.type.image
                addressLabel.text = t.firstTargetAddress?.address
                dateLabel.text = t.dateSring
                volumeLabel.text = t.volumeString
                confirmLabel.text = t.confirmString
                confirmLabel.textColor = UIColor(white: 1.0, alpha: t.height > 0 ? 1.0 : 0.6)
			}
		}
	}
	
    override func awakeFromNib() {
        super.awakeFromNib()
        // Initialization code
    }

    override func setSelected(_ selected: Bool, animated: Bool) {
        super.setSelected(selected, animated: animated)

        // Configure the view for the selected state
    }
    
}
