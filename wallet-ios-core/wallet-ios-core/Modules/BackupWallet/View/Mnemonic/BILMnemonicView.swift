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

class BILMnemonicView: UIView, UICollectionViewDelegateFlowLayout, UICollectionViewDataSource, DZNEmptyDataSetSource {
	
	@IBOutlet weak var collectionView: UICollectionView!
	@IBOutlet weak var delegate: BILMnemonicViewDelegate?
	
	var emptyTitle: String? {
		didSet {
			collectionView.reloadData()
		}
	}
	
	var dataArray = [String]() {
		didSet {
			collectionView.reloadData()
            selectedArray.removeAll()
		}
	}
	
	var selectedArray = [String]()
	
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
			collectionView.allowsSelection = false
			if delegate != nil {
				collectionView.allowsMultipleSelection = true
			}
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
		size.width += 6
		size.height = 30
		return size
	}
	
	func collectionView(_ collectionView: UICollectionView, didSelectItemAt indexPath: IndexPath) {
		selectedArray.append(dataArray[indexPath.item])
		if let d = delegate {
			d.selectedMnemonicArrayDidChange(mnemonicView: self, currentArray: selectedArray)
		}
	}
	
	func collectionView(_ collectionView: UICollectionView, didDeselectItemAt indexPath: IndexPath) {
		if let index = selectedArray.index(of: dataArray[indexPath.item]) {
			selectedArray.remove(at: index)
		}
		if let d = delegate {
			d.selectedMnemonicArrayDidChange(mnemonicView: self, currentArray: selectedArray)
		}
	}
	
	func title(forEmptyDataSet scrollView: UIScrollView!) -> NSAttributedString! {
		
		let title = NSAttributedString(string: emptyTitle ?? "", attributes: [NSAttributedStringKey.foregroundColor : UIColor.bil_white_40_color, NSAttributedStringKey.font: UIFont.systemFont(ofSize: 21)])
		
		return title
	}

    /*
    // Only override draw() if you perform custom drawing.
    // An empty implementation adversely affects performance during animation.
    override func draw(_ rect: CGRect) {
        // Drawing code
    }
    */

}
