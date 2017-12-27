//
//  BILMeSwitchCell.swift
//  wallet-ios-core
//
//  Created by 仇弘扬 on 2017/12/27.
//  Copyright © 2017年 BitBill. All rights reserved.
//

import UIKit

class BILMeSwitchCell: UITableViewCell {
    
    @IBOutlet weak var bil_switch: UISwitch!
    @IBOutlet weak var titleLabel: UILabel!
    
    var switchChangedClosure: ((Bool) -> Void)?
    
    @IBAction func switchChanged(_ sender: UISwitch) {
        switchChangedClosure?(sender.isOn)
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
