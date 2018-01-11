//
//  BILCoinHeaderView.swift
//  wallet-ios-core
//
//  Created by 仇弘扬 on 2018/1/9.
//  Copyright © 2018年 BitBill. All rights reserved.
//

import UIKit

class BILCoinHeaderView: UIView {
    
    var labels = [UILabel]()
    
    override func awakeFromNib() {
        super.awakeFromNib()
        debugPrint(subviews)
        for view in subviews {
            if view is UILabel {
                labels.append(view as! UILabel)
            }
        }
        
        for label in labels {
            label.isUserInteractionEnabled = true
            let tap = UITapGestureRecognizer(target: self, action: #selector(tapped(tap:)))
            label.addGestureRecognizer(tap)
        }
    }
    @objc
    func tapped(tap: UITapGestureRecognizer) {
        let label = tap.view as! UILabel
        if !(label.text ?? "").contains("BTC") {
            viewController()?.bil_makeToast(msg: .publicToastComingSoon)
        }
    }

    /*
    // Only override draw() if you perform custom drawing.
    // An empty implementation adversely affects performance during animation.
    override func draw(_ rect: CGRect) {
        // Drawing code
    }
    */

}
