//
//  BILBILMnemonicCell.swift
//  wallet-ios-core
//
//  Created by 仇弘扬 on 2017/11/16.
//  Copyright © 2017年 BitBill. All rights reserved.
//

import UIKit

class BILMnemonicCell: UICollectionViewCell {
    
	@IBOutlet weak var title: UILabel!
	
	override var isSelected: Bool {
		didSet {
			title.textColor = isSelected ? UIColor.bil_white_40_color : UIColor.white
		}
	}
	
}
