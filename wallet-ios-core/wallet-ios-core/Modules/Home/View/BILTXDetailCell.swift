//
//  BILTXDetailCell.swift
//  wallet-ios-core
//
//  Created by 仇弘扬 on 2017/12/30.
//  Copyright © 2017年 BitBill. All rights reserved.
//

import UIKit

class BILTXDetailCell: UITableViewCell {

	@IBOutlet weak var keyLabel: UILabel?
    @IBOutlet weak var valueLabel: BILCopyLabel?
    override func awakeFromNib() {
        super.awakeFromNib()
        // Initialization code
    }

    override func setSelected(_ selected: Bool, animated: Bool) {
        super.setSelected(selected, animated: animated)

        // Configure the view for the selected state
    }

}
