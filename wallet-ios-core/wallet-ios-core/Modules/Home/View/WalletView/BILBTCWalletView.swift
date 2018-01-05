//
//  BILBTCWalletView.swift
//  wallet-ios-core
//
//  Created by 仇弘扬 on 2017/11/29.
//  Copyright © 2017年 BitBill. All rights reserved.
//

import UIKit
import DZNEmptyDataSet

class BILBTCWalletView: UIView, UITableViewDelegate, UITableViewDataSource {

	@IBOutlet weak var tableView: UITableView!
	@IBOutlet weak var balanceLabel: UILabel!
    @IBOutlet weak var currencyLabel: BILExchangeRateLabel!
	@IBOutlet weak var unconfirmBalanceLabel: UILabel!
	@IBOutlet weak var heightOfBalanceView: NSLayoutConstraint!
    @IBOutlet weak var unconfirmContainerView: UIView!
    
    private var page: Int = 0
    private var size: Int = 2000
    var isLoading = false
    var isLoadingMore = false
	
	var transactions = [BTCTransactionModel]()
	
	weak var wallet: WalletModel? {
		didSet {
            guard let w = wallet else { return }
            transactions.removeAll()
            let txs = w.btcTransactions?.sortedArray(comparator: { (lhs, rhs) -> ComparisonResult in
                let l = lhs as! BTCTransactionModel
                let r = rhs as! BTCTransactionModel
                return r.createdDate!.compare(l.createdDate!)
            }) as! [BTCTransactionModel]
            transactions.append(contentsOf: txs)
            w.getBalanceFromServer(success: { (wallet) in
                self.heightOfBalanceView.constant = w.btcUnconfirmBalance == 0 ? 122 : 213
                self.balanceLabel.text = w.btc_balanceString
                self.unconfirmBalanceLabel.text = w.btc_unconfirm_balanceString + " BTC "
                self.currencyLabel.btcValue = Double(w.btc_balanceString)
            }) { (msg, code) in
                debugPrint(msg)
            }
            reloadData()
		}
	}
	
	override func awakeFromNib() {
		super.awakeFromNib()
        
        tableView.emptyDataSetSource = self
        
		tableView.register(UINib(nibName: "BILTransactionCell", bundle: nil), forCellReuseIdentifier: "BILTransactionCell")
        
        setupRefresh()
        
        unconfirmContainerView.layer.borderWidth = 1
        unconfirmContainerView.layer.borderColor = UIColor(white: 1.0, alpha: 0.3).cgColor
        unconfirmContainerView.layer.cornerRadius = 5
	}
    
    func reloadData() {
        page = 0
        loadTransactionHistory()
    }
    
    func loadMore() {
        guard !isLoading, !isLoadingMore else { return }
        page += 1
        isLoadingMore = true
        loadTransactionHistory()
    }
    
    func loadEnd() {
        self.isLoading = false
        self.isLoadingMore = false
        self.tableView.reloadData()
    }
    
    func loadTransactionHistory() {
        guard let w = wallet else { return }
        guard !isLoading else { return }
        guard w.needLoadServer else { return }
        isLoading = true
        w.getTransactionHistoryFromSever(page: page, size: size, success: { (txs) in
            if !self.isLoadingMore {
                self.transactions.removeAll()
            }
            let txsa = w.btcTransactions?.sortedArray(comparator: { (lhs, rhs) -> ComparisonResult in
                let l = lhs as! BTCTransactionModel
                let r = rhs as! BTCTransactionModel
                return r.createdDate!.compare(l.createdDate!)
            }) as! [BTCTransactionModel]
            self.transactions.append(contentsOf: txsa)
            w.needLoadServer = false
            self.loadEnd()
        }) { (msg, code) in
            self.loadEnd()
            if self.isLoadingMore {
                self.page -= 1
            }
            self.bil_makeToast(msg: msg)
        }
    }
    
    func setupRefresh() {
        let refreshControl = UIRefreshControl()
        refreshControl.addTarget(self, action: #selector(refresh(sender:)), for: .valueChanged)
        refreshControl.tintColor = UIColor.white
        tableView.refreshControl = refreshControl
    }
    
    @objc
    func refresh(sender: Any?) {
        wallet?.needLoadServer = true
        DispatchQueue.main.asyncAfter(deadline: .now() + DispatchTimeInterval.seconds(1)) {
            self.loadTransactionHistory()
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
        cell.confirmLabel.isHidden = false
		cell.separatorInset = UIEdgeInsetsMake(0, 25, 0, 25)
		return cell
	}
    
    func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
        let tx = transactions[indexPath.row]
        viewController()?.performSegue(withIdentifier: .bil_walletToBTCTXDetailSegue, sender: tx)
    }
	
    /*
    // Only override draw() if you perform custom drawing.
    // An empty implementation adversely affects performance during animation.
    override func draw(_ rect: CGRect) {
        // Drawing code
    }
    */

}

extension BILBTCWalletView: DZNEmptyDataSetSource {
    func title(forEmptyDataSet scrollView: UIScrollView!) -> NSAttributedString! {
        return NSAttributedString(string: "无交易记录", attributes: [NSAttributedStringKey.font: UIFont.systemFont(ofSize: 15), NSAttributedStringKey.foregroundColor: UIColor(white: 1.0, alpha: 0.3)])
    }
}
