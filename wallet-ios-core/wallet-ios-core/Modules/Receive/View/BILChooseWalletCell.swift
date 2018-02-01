//
//  BILChooseWalletCell.swift
//  wallet-ios-core
//
//  Created by 仇弘扬 on 2017/12/9.
//  Copyright © 2017年 BitBill. All rights reserved.
//

import UIKit

class BILChooseWalletCell: UITableViewCell {
	
	@IBOutlet weak var idLabel: UILabel!
	@IBOutlet weak var subIDLabel: UILabel!
	@IBOutlet weak var btcBalanceLabel: UILabel!
	@IBOutlet weak var button: UIButton!

	var wallet: WalletModel? {
		didSet {
			if let w = wallet {
				idLabel.text = w.id
				subIDLabel.text = "\(w.id?.first ?? "B")".uppercased()
				btcBalanceLabel.text = w.btc_balanceString + " BTC"
			}
		}
	}
	
    override func awakeFromNib() {
        super.awakeFromNib()
        // Initialization code
    }

    override func setSelected(_ selected: Bool, animated: Bool) {
        super.setSelected(selected, animated: animated)
		button.setImage(UIImage(named: selected ? "btn_circle_selected" : "btn_circle_unselected"), for: .normal)
        // Configure the view for the selected state
    }

}
