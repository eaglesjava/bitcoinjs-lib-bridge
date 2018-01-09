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
            transactions.append(contentsOf: w.btc_transactionArray)
            w.getBalanceFromServer(success: { (wallet) in
                var frame = self.tableView.tableHeaderView!.frame
                frame.size.height = w.btcUnconfirmBalance == 0 ? 113 : 201
                self.tableView.tableHeaderView?.frame = frame
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
        tableView.register(UINib(nibName: "BILTableViewHeaderFooterView", bundle: nil), forHeaderFooterViewReuseIdentifier: "BILTableViewHeaderFooterView")
        
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
            self.transactions.removeAll()
            self.transactions.append(contentsOf: w.btc_transactionArray)
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
		return 74
	}
	
	func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
		let cell = tableView.dequeueReusableCell(withIdentifier: "BILTransactionCell") as! BILTransactionCell
		cell.transaction = transactions[indexPath.row]
		cell.separatorInset = UIEdgeInsetsMake(0, 25, 0, 25)
		return cell
	}
    
    func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
        let tx = transactions[indexPath.row]
        viewController()?.performSegue(withIdentifier: .bil_walletToBTCTXDetailSegue, sender: tx)
    }
    
    func tableView(_ tableView: UITableView, heightForHeaderInSection section: Int) -> CGFloat {
        return 36
    }
    
    func tableView(_ tableView: UITableView, viewForHeaderInSection section: Int) -> UIView? {
        let headerView = tableView.dequeueReusableHeaderFooterView(withIdentifier: "BILTableViewHeaderFooterView") as! BILTableViewHeaderFooterView
        headerView.titleLabel.text = .homeTxDetailTx
        headerView.titleLabel.textColor = UIColor(white: 1.0, alpha: 0.6)
        headerView.bil_backgroundView.backgroundColor = UIColor(white: 1.0, alpha: 0.1)
        return headerView
    }
    
    func numberOfSections(in tableView: UITableView) -> Int {
        return 1
    }
    
    func scrollViewDidScroll(_ scrollView: UIScrollView) {
        guard let section = tableView.indexPathsForVisibleRows?.first?.section else { return }
        
        for i in 0...numberOfSections(in: tableView) {
            guard let header = tableView.headerView(forSection: i) as? BILTableViewHeaderFooterView  else { continue }
            if i == section {
                let headerRect = viewController()!.view.convert(header.frame, from: tableView)
                
                header.bgImageView.image = (viewController() as? BILBaseViewController)?.backgroundImage?.snapshotSubImage(rect: headerRect)
            }
            else
            {
                header.bgImageView.image = nil
            }
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

extension BILBTCWalletView: DZNEmptyDataSetSource {
    func title(forEmptyDataSet scrollView: UIScrollView!) -> NSAttributedString! {
        return NSAttributedString(string: .homeTxDetailNoTx, attributes: [NSAttributedStringKey.font: UIFont.systemFont(ofSize: 15), NSAttributedStringKey.foregroundColor: UIColor(white: 1.0, alpha: 0.3)])
    }
}
