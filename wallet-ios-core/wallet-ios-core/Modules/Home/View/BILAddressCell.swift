//
//  BILAddressCell.swift
//  wallet-ios-core
//
//  Created by 仇弘扬 on 2018/1/4.
//  Copyright © 2018年 BitBill. All rights reserved.
//

import UIKit

class BILAddressCell: UITableViewCell {

	@IBOutlet weak var addressLabel: BILAddressLabel!
	@IBOutlet weak var amountLabel: UILabel!
	override func awakeFromNib() {
        super.awakeFromNib()
        // Initialization code
    }

	override func setSelected(_ selected: Bool, animated: Bool) {
        super.setSelected(selected, animated: animated)

        // Configure the view for the selected state
    }

}
