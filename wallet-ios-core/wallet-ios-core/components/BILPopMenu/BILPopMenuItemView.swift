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
    
    override func touchesBegan(_ touches: Set<UITouch>, with event: UIEvent?) {
        titleLabel.alpha = 0.5
    }
    
    override func touchesCancelled(_ touches: Set<UITouch>, with event: UIEvent?) {
        titleLabel.alpha = 1
    }
    
    override func touchesEnded(_ touches: Set<UITouch>, with event: UIEvent?) {
        titleLabel.alpha = 1
    }
    
    @objc
    func tapped(sender: UITapGestureRecognizer) {
        print(sender.state)
        switch sender.state {
        case .ended:
            delegate?.itemDidTapped(itemView: self)
        default: ()
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
