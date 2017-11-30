//
//  BILBTCWalletView.swift
//  wallet-ios-core
//
//  Created by 仇弘扬 on 2017/11/29.
//  Copyright © 2017年 BitBill. All rights reserved.
//

import UIKit

class BILBTCWalletView: UIView, UITableViewDelegate, UITableViewDataSource {

	@IBOutlet weak var tableView: UITableView!
	@IBOutlet weak var balanceLabel: UILabel!
	@IBOutlet weak var unconfirmBalanceLabel: UILabel!
	@IBOutlet weak var heightOfBalanceView: NSLayoutConstraint!
	
	var transactions: [BILTransaction] {
		get {
			return BILTransactionManager.shared.recnetRecords
		}
	}
	
	weak var wallet: WalletModel? {
		didSet {
			heightOfBalanceView.constant = wallet?.btcUnconfirmBalance == 0 ? 108 : 142
			
			balanceLabel.text = "\(wallet?.btcBalance ?? 0)"
			unconfirmBalanceLabel.text = "\(wallet?.btcUnconfirmBalance ?? 0)btc"
		}
	}
	
	override func awakeFromNib() {
		super.awakeFromNib()
		tableView.register(UINib(nibName: "BILTransactionCell", bundle: nil), forCellReuseIdentifier: "BILTransactionCell")
	}
	
	func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
		return transactions.count
	}
	
	func tableView(_ tableView: UITableView, heightForRowAt indexPath: IndexPath) -> CGFloat {
		return 70
	}
	
	func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
		let cell = tableView.dequeueReusableCell(withIdentifier: "BILTransactionCell") as! BILTransactionCell
		cell.transaction = transactions[indexPath.row]
		cell.separatorInset = UIEdgeInsetsMake(0, 25, 0, 25)
		return cell
	}
	
    /*
    // Only override draw() if you perform custom drawing.
    // An empty implementation adversely affects performance during animation.
    override func draw(_ rect: CGRect) {
        // Drawing code
    }
    */

}
