//
//  BILHomeViewController.swift
//  wallet-ios-core
//
//  Created by 仇弘扬 on 2017/11/24.
//  Copyright © 2017年 BitBill. All rights reserved.
//

import UIKit

enum BILHomeSectionType: Int {
	case asset = 0
	case recentRecord
	case wallet
	
	var sectionTitle: String {
		switch self {
		case .asset:
			return "总资产"
		case .recentRecord:
			return "未确认交易"
		case .wallet:
			return "我的钱包"
		}
	}
	
	var sectionSubTitle: String {
		switch self {
		case .asset:
			return "当前币种 1 个"
		case .recentRecord:
			return "正在进行中 \(numberOfRows()) 个"
		case .wallet:
			return "当前钱包 \(numberOfRows()) 个"
		}
	}
	
	var sectionViewHeight: CGFloat {
		if numberOfRows() == 0 {
			return 0
		}
		switch self {
		case .asset:
			return 95
        case .wallet:
            return 101
		default:
			return 95
		}
	}
	
	var rowHeight: CGFloat {
		switch self {
		case .asset:
			return 66
		case .recentRecord:
			return 56
		case .wallet:
			return 89
		}
	}
	
	var headerActionButtonImage: UIImage? {
		switch self {
		case .asset: fallthrough
		case .recentRecord:
			return nil
		case .wallet:
			return UIImage(named: "btn_add")
		}
	}
	
	func numberOfRows() -> Int {
		return dataArray().count
	}
	
	func dataArray() -> [Any] {
		switch self {
		case .asset:
			return [NSObject()]
		case .recentRecord:
			return BILTransactionManager.shared.recnetRecords
		case .wallet:
			return BILWalletManager.shared.wallets
		}
	}
	
	func cellID() -> String {
		switch self {
		case .asset:
			return "BILHomeAssetCell"
		case .recentRecord:
			return "BILTransactionCell"
		case .wallet:
			return "BILWalletCell"
		}
	}
	
}

class BILHomeViewController: BILBaseViewController, UITableViewDelegate, UITableViewDataSource, BILHomeTableHeaderViewDelegate {

	@IBOutlet weak var tableView: UITableView!
	
	var wallets: [WalletModel] {
		get {
			return BILWalletManager.shared.wallets
		}
	}
	
	var dataArray: [BILHomeSectionType] = [.asset, .recentRecord, .wallet]
	
	let headerViewID = "BILHomeTableHeaderView"
	let walletCellID = BILHomeSectionType.wallet.cellID()
	let transactionCellID = BILHomeSectionType.recentRecord.cellID()
	
	var headerBGImage: UIImage?
    
    var totalBTCBalance = "0.00"
	
	override func viewDidLoad() {
        super.viewDidLoad()

        // Do any additional setup after loading the view.
		tableView.register(UINib(nibName: headerViewID, bundle: nil), forHeaderFooterViewReuseIdentifier: headerViewID)
		tableView.register(UINib(nibName: transactionCellID, bundle: nil), forCellReuseIdentifier: transactionCellID)
        
        setupRefresh()
		
		NotificationCenter.default.addObserver(self, selector: #selector(walletDidChanged(notification:)), name: .walletDidChanged, object: nil)
        NotificationCenter.default.addObserver(self, selector: #selector(balanceDidChanged(notification:)), name: .walletDidChanged, object: nil)
        NotificationCenter.default.addObserver(self, selector: #selector(uncofirmTransactionDidChanged(notification:)), name: .recievedUnconfirmTransaction, object: nil)
        NotificationCenter.default.addObserver(self, selector: #selector(uncofirmTransactionDidChanged(notification:)), name: .unconfirmTransactionBeenConfirmed, object: nil)
		
		if #available(iOS 11.0, *) {
			navigationController?.navigationBar.prefersLargeTitles = false
		} else {
			// Fallback on earlier versions
		}
        refresh(sender: nil)
        balanceDidChanged(notification: nil)
    }
    
    func setupRefresh() {
        let refreshControl = UIRefreshControl()
        refreshControl.addTarget(self, action: #selector(refresh(sender:)), for: .valueChanged)
        refreshControl.tintColor = UIColor.white
        tableView.refreshControl = refreshControl
    }
	
	deinit {
		NotificationCenter.default.removeObserver(self, name: .walletDidChanged, object: nil)
        NotificationCenter.default.removeObserver(self, name: .recievedUnconfirmTransaction, object: nil)
        NotificationCenter.default.removeObserver(self, name: .unconfirmTransactionBeenConfirmed, object: nil)
	}

	override func viewWillAppear(_ animated: Bool) {
		super.viewWillAppear(animated)
		navigationController?.setNavigationBarHidden(true, animated: false)
		tableView.reloadData()
	}
	
	override func viewWillDisappear(_ animated: Bool) {
		super.viewWillAppear(animated)
//		navigationController?.setNavigationBarHidden(false, animated: false)
	}
	
	// MARK: - Notifications
    @objc
    func refresh(sender: Any?) {
        uncofirmTransactionDidChanged(notification: nil)
        WalletModel.getBalanceFromServer(wallets: wallets, success: {
            self.balanceDidChanged(notification: nil)
        }) { (msg, code) in
            
        }
    }
    
    @objc
	func balanceDidChanged(notification: Notification?) {
		var sum: Int64 = 0
		for wallet in wallets {
			sum += wallet.btcBalance
            sum += wallet.btcUnconfirmBalance
		}
        totalBTCBalance = BTCFormatString(btc: sum)
        tableView.reloadData()
	}
    
    @objc
    func uncofirmTransactionDidChanged(notification: Notification?) {
        func loadEnd() {
            self.tableView.reloadData()
            self.tableView.refreshControl?.endRefreshing()
        }
        WalletModel.getUnconfirmTransactionFromSever(wallets: wallets, success: { (txs)  in
            BILTransactionManager.shared.recnetRecords = txs
            loadEnd()
        }) { (msg, code) in
            loadEnd()
        }
    }
	
	@objc
	func walletDidChanged(notification: Notification) {
		tableView.reloadData()
	}
	
	// MARK: - Header view delegate
	func actionButtonTapped(headerView: BILHomeTableHeaderView) {
		guard let type = BILHomeSectionType(rawValue: headerView.tag) else { return }
		switch type {
		case .asset: ()
		case .recentRecord: ()
		case .wallet:
            
            let button = headerView.actionButton!
            var point = view.window!.convert(button.center, from: headerView)
            point.x += 8
            
            UIView.animate(withDuration: 0.35, animations: {
                button.transform = CGAffineTransform(rotationAngle: CGFloat(Double.pi / 4))
            })
            
            let menu = BILPopMenu()
            menu.addItem(item: BILPopMenuItem(title: "创建钱包", tapped: {
                self.performSegue(withIdentifier: "BILHomeToCreateWalletSegue", sender: nil)
            }))
            menu.addItem(item: BILPopMenuItem(title: "导入钱包", tapped: {
                self.performSegue(withIdentifier: "BILHomeToImportWalletSegue", sender: nil)
            }))
            menu.show(in: view.window!, focusPoint: point, willDismiss: {
                UIView.animate(withDuration: 0.35, animations: {
                    button.transform = CGAffineTransform(rotationAngle: 0)
                })
            })
		}
	}
	
	// MARK: - ScrollView delegate
	func scrollViewDidScroll(_ scrollView: UIScrollView) {
		guard let section = tableView.indexPathsForVisibleRows?.first?.section else { return }

		for i in 0...numberOfSections(in: tableView) {
			guard let header = tableView.headerView(forSection: i) as? BILHomeTableHeaderView  else { continue }
			if i == section {
				let headerRect = view.convert(header.frame, from: tableView)
				
				headerBGImage = BILAppStartUpManager.shared.snapshotNavBackgroundImage(rect: headerRect)
				let image = headerBGImage
				header.bgImageView.image = image
			}
			else
			{
				header.bgImageView.image = nil
			}
		}
		
	}
	
	// MARK: - TableView delegate & dataSource
	func numberOfSections(in tableView: UITableView) -> Int {
		return dataArray.count
	}
	
	func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
		guard let type = BILHomeSectionType(rawValue: section) else { return 0 }
		return type.numberOfRows()
	}
	
	func tableView(_ tableView: UITableView, heightForHeaderInSection section: Int) -> CGFloat {
		guard let type = BILHomeSectionType(rawValue: section) else { return 0 }
		return type.sectionViewHeight
	}
	
	func tableView(_ tableView: UITableView, viewForHeaderInSection section: Int) -> UIView? {
		guard let type = BILHomeSectionType(rawValue: section) else { return nil }
		
		let header = tableView.dequeueReusableHeaderFooterView(withIdentifier: headerViewID) as! BILHomeTableHeaderView
		header.tag = section
		header.subTitleLabel.text = type.sectionSubTitle
		header.titleLabel.text = type.sectionTitle
		header.buttonImage = type.headerActionButtonImage
		header.delegate = self
		
		if section == 0 && headerBGImage == nil {
			headerBGImage = BILAppStartUpManager.shared.snapshotNavBackgroundImage(rect: view.convert(header.frame, from: tableView))
		}
        header.bgImageView.image = nil
        
        if let firstVisibleSection = tableView.indexPathsForVisibleRows?.first?.section, firstVisibleSection == section {
            header.bgImageView.image = headerBGImage
        }
		
		return header
	}
	
	func tableView(_ tableView: UITableView, heightForRowAt indexPath: IndexPath) -> CGFloat {
		guard let type = BILHomeSectionType(rawValue: indexPath.section) else { return 0 }
		return type.rowHeight
	}
	
	func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
		guard let type = BILHomeSectionType(rawValue: indexPath.section) else { return UITableViewCell() }
		let cell = tableView.dequeueReusableCell(withIdentifier: type.cellID())
		switch type {
		case .asset:
            let c = cell as! BILHomeAssetCell
            c.btcBanlanceLabel.text = totalBTCBalance
		case .recentRecord:
			let c = cell as! BILTransactionCell
			c.transaction = type.dataArray()[indexPath.row] as? BILTransactionHistoryModel
		case .wallet:
			let c = cell as! BILWalletCell
            c.needBackupButton.tag = indexPath.row
			c.wallet = type.dataArray()[indexPath.row] as? WalletModel
		}
		return cell!
	}
	
    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    

    // MARK: - Navigation
    override func shouldPerformSegue(withIdentifier identifier: String, sender: Any?) -> Bool {
        if identifier == "BILHomeToWalletSegue" {
            return sender is UITableViewCell
        }
        return true
    }
    // In a storyboard-based application, you will often want to do a little preparation before navigation
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        // Get the new view controller using segue.destinationViewController.
        // Pass the selected object to the new view controller.
		if segue.identifier == "BILHomeToWalletSegue" {
			
		}
        
        guard let id = segue.identifier else { return }
        
        switch id {
        case "BILHomeToWalletSegue":
            let cont = segue.destination as! BILWalletController
            cont.wallet = wallets[(tableView.indexPath(for: sender as! UITableViewCell))!.row]
        case "BILHomeToBackUpWallet":
            if let cont = (segue.destination as? UINavigationController)?.viewControllers.first as? BILBackupWalletMnemonicController {
                let wallet = wallets[(sender as! UIView).tag]
                cont.mnemonicHash = wallet.mnemonicHash
            }
        default:
            ()
        }
    }

}
