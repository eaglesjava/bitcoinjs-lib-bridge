//
//  BILInputView.swift
//  wallet-ios-core
//
//  Created by 仇弘扬 on 2017/11/14.
//  Copyright © 2017年 BitBill. All rights reserved.
//

import UIKit

@objc
protocol BILInputViewDelegate: UITextFieldDelegate {
	
}

class BILInputView: UIView, UITextFieldDelegate {
	
	enum TipType {
		case normal
		case error
	}
	
	@IBOutlet weak var textField: UITextField!
	@IBOutlet weak var title: UILabel?
    @IBOutlet weak var tipLabel: UILabel?
	@IBOutlet weak var line: UIView!
	@IBOutlet weak var functionTipHeight: NSLayoutConstraint?
    @IBOutlet weak var functionTipBottom: NSLayoutConstraint?
	
	@IBOutlet weak var delegate: BILInputViewDelegate?
    
    private var titleString: String?
	
	override func awakeFromNib() {
		super.awakeFromNib()
		line.backgroundColor = UIColor.bil_white_40_color
        titleString = title?.text
	}
    
    func updateTitleString(_ titleString: String) {
        self.titleString = titleString
        title?.text = titleString
    }
	
	func updateFunctionTipHeight(height: CGFloat, animate: Bool = false) {
		guard let h = functionTipHeight else { return }
		h.constant = height
        guard let b = functionTipBottom else { return }
        b.constant = height > 0 ? 8 : 0
		UIView.animate(withDuration: 0.35) {
			self.superview?.superview?.layoutIfNeeded()
		}
	}

	func show(tip: String, type: TipType) {
		title?.text = tip
		var color: UIColor
		var lineColor: UIColor
		switch type {
		case .error:
			color = UIColor(hex: 0xFD6D73)
			lineColor = color
		default:
			color = UIColor.white
			lineColor = UIColor(hex: 0xF2F2F2)
		}
		line.backgroundColor = color
		title?.textColor = lineColor
	}
	
	func textFieldDidBeginEditing(_ textField: UITextField) {
        delegate?.textFieldDidBeginEditing?(textField)
        show(tip: titleString ?? "", type: .normal)
		line.backgroundColor = UIColor.white
        guard let l = tipLabel else {
            return
        }
        updateFunctionTipHeight(height: l.bounds.height, animate: true)
	}
	
	func textFieldDidEndEditing(_ textField: UITextField) {
        show(tip: titleString ?? "", type: .normal)
		line.backgroundColor = UIColor.bil_white_40_color
		updateFunctionTipHeight(height: 0, animate: true)
	}
	
	func textFieldShouldReturn(_ textField: UITextField) -> Bool {
		if let r = delegate?.textFieldShouldReturn?(textField) {
			return r
		}
		return true
	}
	
    /*
    // Only override draw() if you perform custom drawing.
    // An empty implementation adversely affects performance during animation.
    override func draw(_ rect: CGRect) {
        // Drawing code
    }
    */

}
