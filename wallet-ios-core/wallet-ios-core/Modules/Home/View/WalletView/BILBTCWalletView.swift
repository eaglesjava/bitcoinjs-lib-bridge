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
            guard let w = wallet else { return }
            w.getBalanceFromServer(success: { (wallet) in
                self.heightOfBalanceView.constant = w.btcUnconfirmBalance == 0 ? 116 : 142
                self.balanceLabel.text = w.btc_balanceString
                self.unconfirmBalanceLabel.text = w.btc_unconfirm_balanceString + "btc"
            }) { (msg, code) in
                debugPrint(msg)
            }
		}
	}
	
	override func awakeFromNib() {
		super.awakeFromNib()
		tableView.register(UINib(nibName: "BILTransactionCell", bundle: nil), forCellReuseIdentifier: "BILTransactionCell")
        
        setupRefresh()
	}
    
    func setupRefresh() {
        let refreshControl = UIRefreshControl()
        refreshControl.addTarget(self, action: #selector(refresh(sender:)), for: .valueChanged)
        refreshControl.tintColor = UIColor.white
        tableView.refreshControl = refreshControl
    }
    
    @objc
    func refresh(sender: Any?) {
        DispatchQueue.main.asyncAfter(deadline: .now() + DispatchTimeInterval.seconds(1)) {
            self.tableView.refreshControl?.endRefreshing()
        }
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
