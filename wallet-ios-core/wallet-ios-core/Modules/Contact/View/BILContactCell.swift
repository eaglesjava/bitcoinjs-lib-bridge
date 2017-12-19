//
//  BILContactCell.swift
//  wallet-ios-core
//
//  Created by 仇弘扬 on 2017/12/18.
//  Copyright © 2017年 BitBill. All rights reserved.
//

import UIKit

class BILContactCell: UITableViewCell {

    @IBOutlet weak var firstWordLabel: UILabel!
    @IBOutlet weak var nameLabel: UILabel!
    @IBOutlet weak var detailLabel: UILabel!
    @IBOutlet weak var seletedButton: UIButton!
    
    var contact: Contact? {
        didSet {
            guard let c = contact else { return }
            nameLabel.text = c.name
            firstWordLabel.text = c.firstNameWord
            detailLabel.text = c.detail
        }
    }
    
    override func awakeFromNib() {
        super.awakeFromNib()
        // Initialization code
        backgroundColor = UIColor.clear
    }

    override func setSelected(_ selected: Bool, animated: Bool) {
        super.setSelected(selected, animated: animated)
        seletedButton.isSelected = selected
        // Configure the view for the selected state
    }
    
}
