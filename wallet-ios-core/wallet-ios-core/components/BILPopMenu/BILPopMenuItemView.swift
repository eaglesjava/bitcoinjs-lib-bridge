//
//  BILPopMenuItemView.swift
//  wallet-ios-core
//
//  Created by 仇弘扬 on 2017/12/6.
//  Copyright © 2017年 BitBill. All rights reserved.
//

import UIKit

protocol BILPopMenuItemViewDelegate: NSObjectProtocol {
    func itemDidTapped(itemView: BILPopMenuItemView)
}

class BILPopMenuItemView: UIView {
    
    @IBOutlet weak var titleLabel: UILabel!
    
    weak var item: BILPopMenuItem?
    weak var delegate: BILPopMenuItemViewDelegate?
    
    override func awakeFromNib() {
        super.awakeFromNib()
        layer.shadowColor = UIColor.black.cgColor
        layer.shadowOffset = CGSize(width: 1, height: 1)
        layer.shadowRadius = 10
        layer.shadowOpacity = 0.5
        layer.cornerRadius = 20
        
        let tap = UITapGestureRecognizer(target: self, action: #selector(tapped(sender:)))
        addGestureRecognizer(tap)
    }
    
    @objc
    func tapped(sender: UITapGestureRecognizer) {
        switch sender.state {
        case .ended:
            delegate?.itemDidTapped(itemView: self)
        default:
            ()
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
