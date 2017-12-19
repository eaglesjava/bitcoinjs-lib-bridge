//
//  BILTableViewHeaderFooterView.swift
//  wallet-ios-core
//
//  Created by 仇弘扬 on 2017/12/18.
//  Copyright © 2017年 BitBill. All rights reserved.
//

import UIKit

class BILTableViewHeaderFooterView: UITableViewHeaderFooterView {
    
    @IBOutlet weak var bgImageView: UIImageView!
    @IBOutlet weak var titleLabel: UILabel!
    
    override func awakeFromNib() {
        super.awakeFromNib()
    }
    
    override func prepareForReuse() {
        super.prepareForReuse()
        bgImageView.image = nil
        titleLabel.text = ""
    }
    
}
