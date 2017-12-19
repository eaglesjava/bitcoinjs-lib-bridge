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
	
	var transaction: BILTransactionHistoryModel? {
		didSet {
			if let t = transaction {
				typeImageView.image = UIImage(named: t.type == .recieve ? "icon_record_recieve" : "icon_record_send")
				addressLabel.text = t.firstAddress
				dateLabel.text = t.dateSring
				volumeLabel.text = t.volumeString
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
