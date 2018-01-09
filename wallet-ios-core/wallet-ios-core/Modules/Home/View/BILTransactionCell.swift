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
	@IBOutlet weak var titleLabel: UILabel!
	@IBOutlet weak var dateLabel: UILabel!
	@IBOutlet weak var volumeLabel: UILabel!
    
	var transaction: BTCTransactionModel? {
		didSet {
			if let t = transaction {
                typeImageView.image = t.height == -1 ? UIImage(named: "icon_record_unconfirm") : t.type.image
                dateLabel.text = t.dateSring
                volumeLabel.text = t.volumeString
                titleLabel.text = t.height == -1 ? t.status.description : t.typeString
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
