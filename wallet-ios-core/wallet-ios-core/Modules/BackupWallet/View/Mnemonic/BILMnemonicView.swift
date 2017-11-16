//
//  BILMnemonicView.swift
//  wallet-ios-core
//
//  Created by 仇弘扬 on 2017/11/16.
//  Copyright © 2017年 BitBill. All rights reserved.
//

import UIKit
import SnapKit
import DZNEmptyDataSet

class BILMnemonicView: UIView, UICollectionViewDelegateFlowLayout, UICollectionViewDataSource {
	
	@IBOutlet weak var collectionView: UICollectionView!
	
	var dataArray = [String]() {
		didSet {
			collectionView.reloadData()
		}
	}
	
	var mnemonic = "" {
		didSet {
			dataArray = mnemonic.components(separatedBy: " ")
		}
	}
	
	override func awakeFromNib() {
		super.awakeFromNib()
		
		if let contentView = Bundle.main.loadNibNamed("BILMnemonicView", owner: self, options: nil)?.first as? UIView {
			
			addSubview(contentView)
			
			unowned let unownedSelf = self
			contentView.snp.makeConstraints({ (maker) in
				maker.edges.equalTo(unownedSelf).inset(UIEdgeInsets.zero)
			})
			
			collectionView.register(UINib(nibName: "BILMnemonicCell", bundle: nil), forCellWithReuseIdentifier: "BILMnemonicCell")
		}
		
		layer.borderColor = UIColor(white: 1.0, alpha: 0.3).cgColor
		layer.borderWidth = 1
		layer.cornerRadius = 2
		
	}
	
	func collectionView(_ collectionView: UICollectionView, numberOfItemsInSection section: Int) -> Int {
		return dataArray.count
	}
	
	func collectionView(_ collectionView: UICollectionView, cellForItemAt indexPath: IndexPath) -> UICollectionViewCell {
		let cell = collectionView.dequeueReusableCell(withReuseIdentifier: "BILMnemonicCell", for: indexPath) as! BILMnemonicCell
		cell.title.text = dataArray[indexPath.item]
		return cell
	}
	
	func collectionView(_ collectionView: UICollectionView, layout collectionViewLayout: UICollectionViewLayout, sizeForItemAt indexPath: IndexPath) -> CGSize {
		let text = dataArray[indexPath.item]
		var size = text.size(withAttributes: [NSAttributedStringKey.font : UIFont.systemFont(ofSize: 21)])
		size.width += 18
		size.height = 30
		return size
	}

    /*
    // Only override draw() if you perform custom drawing.
    // An empty implementation adversely affects performance during animation.
    override func draw(_ rect: CGRect) {
        // Drawing code
    }
    */

}
