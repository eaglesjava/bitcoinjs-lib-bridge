//
//  BILWalletCell.swift
//  wallet-ios-core
//
//  Created by 仇弘扬 on 2017/11/24.
//  Copyright © 2017年 BitBill. All rights reserved.
//

import UIKit

class BILWalletCell: UITableViewCell {

	@IBOutlet weak var bil_contentView: BILGradientView!
	@IBOutlet weak var idLabel: UILabel!
	@IBOutlet weak var subIDLabel: UILabel!
	@IBOutlet weak var btcBalanceLabel: UILabel!
	@IBOutlet weak var needBackupButton: UIButton!
	
	var wallet: WalletModel? {
		didSet {
			if let w = wallet {
				idLabel.text = w.id
				subIDLabel.text = "\(w.id?.first ?? "B")"
				btcBalanceLabel.text = w.btc_balanceString + " BTC"
				needBackupButton.isHidden = !w.isNeedBackup
			}
		}
	}
	
	override func awakeFromNib() {
        super.awakeFromNib()
        // Initialization code
		languageDidChanged()
    }
    
    override func languageDidChanged() {
        needBackupButton.setTitle("Back up".bil_ui_localized, for: .normal)
    }

    override func setSelected(_ selected: Bool, animated: Bool) {
        super.setSelected(selected, animated: animated)

        // Configure the view for the selected state
    }

}
