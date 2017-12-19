//
//  BILWhiteBorderButton.swift
//  wallet-ios-core
//
//  Created by 仇弘扬 on 2017/12/19.
//  Copyright © 2017年 BitBill. All rights reserved.
//

import UIKit

class BILWhiteBorderButton: UIButton {

    override func awakeFromNib() {
        super.awakeFromNib()
        layer.borderColor = UIColor.white.cgColor
        layer.borderWidth = 1
        layer.cornerRadius = frame.height / 2
        setTitleColor(UIColor.white, for: .normal)
        titleLabel?.font = UIFont.systemFont(ofSize: 16)
    }
    
    /*
    // Only override draw() if you perform custom drawing.
    // An empty implementation adversely affects performance during animation.
    override func draw(_ rect: CGRect) {
        // Drawing code
    }
    */

}
