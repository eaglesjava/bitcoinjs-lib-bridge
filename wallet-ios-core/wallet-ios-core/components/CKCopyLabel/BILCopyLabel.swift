//
//  BILCopyLabel.swift
//  wallet-ios-core
//
//  Created by 仇弘扬 on 2017/12/11.
//  Copyright © 2017年 BitBill. All rights reserved.
//

import UIKit
import Toast_Swift

extension UIView {
    func bil_makeToast(msg: String?) {
        window?.makeToast(msg)
    }
}

class BILCopyLabel: UILabel {

    override func awakeFromNib() {
        super.awakeFromNib()
        setupGesture()
    }
    
    override init(frame: CGRect) {
        super.init(frame: frame)
        setupGesture()
    }
    
    required init?(coder aDecoder: NSCoder) {
        super.init(coder: aDecoder)
        setupGesture()
    }
    
    func setupGesture() {
        isUserInteractionEnabled = true
        
        let tap = UITapGestureRecognizer(target: self, action: #selector(tapAction(gesture:)))
        addGestureRecognizer(tap)
    }
    
    @objc
    func tapAction(gesture: UITapGestureRecognizer) {
        UIPasteboard.general.string = text
        bil_makeToast(msg: "已复制")
    }
    
    /*
    // Only override draw() if you perform custom drawing.
    // An empty implementation adversely affects performance during animation.
    override func draw(_ rect: CGRect) {
        // Drawing code
    }
    */

}
