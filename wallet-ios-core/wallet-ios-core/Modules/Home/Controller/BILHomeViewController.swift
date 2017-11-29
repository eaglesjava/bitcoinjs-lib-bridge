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
			return "进行中的交易"
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
		return numberOfRows() == 0 ? 0 : 80
	}
	
	var rowHeight: CGFloat {
		switch self {
		case .asset:
			return 66
		case .recentRecord:
			return 56
		case .wallet:
			return 91
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
	
	override func viewDidLoad() {
        super.viewDidLoad()

        // Do any additional setup after loading the view.
		tableView.register(UINib(nibName: headerViewID, bundle: nil), forHeaderFooterViewReuseIdentifier: headerViewID)
		tableView.register(UINib(nibName: transactionCellID, bundle: nil), forCellReuseIdentifier: transactionCellID)
		
		NotificationCenter.default.addObserver(self, selector: #selector(walletDidChanged(notification:)), name: .walletDidChanged, object: nil)
		
		if #available(iOS 11.0, *) {
			navigationController?.navigationBar.prefersLargeTitles = false
		} else {
			// Fallback on earlier versions
		}
    }
	
	deinit {
		NotificationCenter.default.removeObserver(self, name: .walletDidChanged, object: nil)
	}

	override func viewWillAppear(_ animated: Bool) {
		super.viewWillAppear(animated)
		navigationController?.setNavigationBarHidden(true, animated: false)
	}
	
	override func viewWillDisappear(_ animated: Bool) {
		super.viewWillAppear(animated)
		navigationController?.setNavigationBarHidden(false, animated: false)
	}
	
	// MARK: - Notifications
	func balanceDidChanged(notification: Notification) {
		var sum: Int64 = 0
		for wallet in wallets {
			sum += wallet.btcBalance
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
			let alert = UIAlertController(title: nil, message: nil, preferredStyle: .actionSheet)
			let createAction = UIAlertAction(title: "创建钱包", style: .default, handler: { (action) in
				self.performSegue(withIdentifier: "BILHomeToCreateWalletSegue", sender: nil)
			})
			let importAction = UIAlertAction(title: "导入钱包", style: .default, handler: { (action) in
				self.performSegue(withIdentifier: "BILHomeToImportWalletSegue", sender: nil)
			})
			let cancelAction = UIAlertAction(title: "取消", style: .cancel, handler: nil)
			alert.addAction(createAction)
			alert.addAction(importAction)
			alert.addAction(cancelAction)
			present(alert, animated: true, completion: nil)
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
			()
		case .recentRecord:
			let c = cell as! BILTransactionCell
			c.transaction = type.dataArray()[indexPath.row] as? BILTransaction
		case .wallet:
			let c = cell as! BILWalletCell
			c.wallet = type.dataArray()[indexPath.row] as? WalletModel
		}
		return cell!
	}
	
    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    

    // MARK: - Navigation

    // In a storyboard-based application, you will often want to do a little preparation before navigation
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        // Get the new view controller using segue.destinationViewController.
        // Pass the selected object to the new view controller.
		if segue.identifier == "BILHomeToWalletSegue" {
			let cont = segue.destination as! BILWalletController
			cont.wallet = wallets[(tableView.indexPathForSelectedRow?.row)!]
		}
    }

}
