//
//  BILWalletCell.swift
//  wallet-ios-core
//
//  Created by 仇弘扬 on 2017/11/24.
//  Copyright © 2017年 BitBill. All rights reserved.
//

import UIKit

class BILWalletCell: UITableViewCell {

	@IBOutlet weak var bil_contentView: UIView!
	@IBOutlet weak var idLabel: UILabel!
	@IBOutlet weak var subIDLabel: UILabel!
	@IBOutlet weak var btcBalanceLabel: UILabel!
	
	var wallet: WalletModel? {
		didSet {
			if let w = wallet {
				idLabel.text = w.name
				subIDLabel.text = "\(w.name?.first ?? "B")"
				btcBalanceLabel.text = "0.0000 btc"
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
