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
	@IBOutlet weak var needBackupLabel: UILabel!
	
	var wallet: WalletModel? {
		didSet {
			if let w = wallet {
				idLabel.text = w.name
				subIDLabel.text = "\(w.name?.first ?? "B")"
				btcBalanceLabel.text = "0.0000 btc"
				needBackupLabel.isHidden = !w.isNeedBackup
				if w.isNeedBackup {
					bil_contentView.gradientLayer?.colors = [UIColor.bil_gradient_start_red_color.cgColor, UIColor.bil_gradient_end_red_color.cgColor]
					subIDLabel.textColor = UIColor(hex: 0xFF636B)
				}
				else
				{
					bil_contentView.gradientLayer?.colors = [UIColor.bil_gradient_start_color.cgColor, UIColor.bil_gradient_end_color.cgColor]
					subIDLabel.textColor = UIColor(hex: 0x428DCA)
				}
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
