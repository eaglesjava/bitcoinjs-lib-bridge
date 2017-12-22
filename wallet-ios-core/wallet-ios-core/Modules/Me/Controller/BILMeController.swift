//
//  BILMeController.swift
//  wallet-ios-core
//
//  Created by 仇弘扬 on 2017/12/21.
//  Copyright © 2017年 BitBill. All rights reserved.
//

import UIKit

extension String {
    static var bil_meToBackupWalletSegue: String { return "BILMeToBackupWalletSegue" }
	static var bil_meToWalletDetailSegue: String { return "BILMeToWalletDetailSegue" }
}

class BILMeController: BILBaseViewController {
    
    enum BILMeSectionType: Int {
        case wallet = 0
        case preference
        case other
        
        var sectionTitle: String {
            switch self {
            case .wallet:
                return "钱包"
            case .preference:
                return "偏好设置"
            case .other:
                return "其它"
            }
        }
        
        var sectionViewHeight: CGFloat {
            if numberOfRows() == 0 {
                return 0
            }
            return 36
        }
        
        var rowHeight: CGFloat {
            return 60
        }
        
        func numberOfRows() -> Int {
            return dataArray().count
        }
        
        func dataArray() -> [Any] {
            switch self {
            case .wallet:
                return BILWalletManager.shared.wallets
            case .preference:
                return[]
            case .other:
                return []
            }
        }
        
        func cellID() -> String {
            switch self {
            case .wallet:
                return "BILMeWalletCell"
            default:
                return "BILMeCell"
            }
        }
        
    }

    @IBOutlet weak var tableView: UITableView!
    var sections: [BILMeSectionType] = [.wallet, .preference, .other]
    
    override func viewDidLoad() {
        super.viewDidLoad()

        // Uncomment the following line to preserve selection between presentations
        // self.clearsSelectionOnViewWillAppear = false

        // Uncomment the following line to display an Edit button in the navigation bar for this view controller.
        // self.navigationItem.rightBarButtonItem = self.editButtonItem
        tableView.register(UINib(nibName: "BILTableViewHeaderFooterView", bundle: nil), forHeaderFooterViewReuseIdentifier: "BILTableViewHeaderFooterView")
        tableView.reloadData()
		
		NotificationCenter.default.addObserver(self, selector: #selector(walletDidChanged(notification:)), name: .walletDidChanged, object: nil)
    }
	
	deinit {
		NotificationCenter.default.removeObserver(self, name: .walletDidChanged, object: nil)
	}
	
	@objc
	func walletDidChanged(notification: Notification) {
		tableView.reloadData()
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
        guard let id = segue.identifier else { return }
        switch id {
        case String.bil_meToBackupWalletSegue:
            guard let wallet = sender as? WalletModel else { return }
            if let cont = (segue.destination as? UINavigationController)?.viewControllers.first as? BILBackupWalletMnemonicController {
                cont.mnemonicHash = wallet.mnemonicHash
            }
		case String.bil_meToWalletDetailSegue:
			guard let wallet = sender as? WalletModel else { return }
			let cont = segue.destination as! BILWalletDetailSettingController
			cont.wallet = wallet
        default:
            ()
        }
    }

}

// MARK: - Table view data source

extension BILMeController: UITableViewDataSource, UITableViewDelegate {
    
    func scrollViewDidScroll(_ scrollView: UIScrollView) {
        guard let section = tableView.indexPathsForVisibleRows?.first?.section else { return }
        for i in 0...numberOfSections(in: tableView) {
            guard let header = tableView.headerView(forSection: i) as? BILTableViewHeaderFooterView  else { continue }
            if i == section {
                let headerRect = view.convert(header.frame, from: tableView)
                
                header.bgImageView.image = backgroundImage?.snapshotSubImage(rect: headerRect)
            }
            else
            {
                header.bgImageView.image = nil
            }
        }
    }
    
    func numberOfSections(in tableView: UITableView) -> Int {
        return sections.count
    }
    
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return sections[section].dataArray().count
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let sectionType = sections[indexPath.section]
        let cell = tableView.dequeueReusableCell(withIdentifier: sectionType.cellID(), for: indexPath)
        switch sectionType {
        case .wallet:
            let c = cell as! BILMeWalletCell
            let wallet = sectionType.dataArray()[indexPath.row] as? WalletModel
            c.wallet = wallet
        default:
            ()
        }
        return cell
    }
    
    func tableView(_ tableView: UITableView, heightForRowAt indexPath: IndexPath) -> CGFloat {
        let sectionType = sections[indexPath.section]
        return sectionType.rowHeight
    }
    
    func tableView(_ tableView: UITableView, heightForHeaderInSection section: Int) -> CGFloat {
        let sectionType = sections[section]
        return sectionType.sectionViewHeight
    }
    
    func tableView(_ tableView: UITableView, viewForHeaderInSection section: Int) -> UIView? {
        let sectionType = sections[section]
        let headerView = tableView.dequeueReusableHeaderFooterView(withIdentifier: "BILTableViewHeaderFooterView") as! BILTableViewHeaderFooterView
        headerView.titleLabel.text = sectionType.sectionTitle
        headerView.bgImageView.image = backgroundImage?.snapshotSubImage(rect: view.convert(headerView.frame, from: tableView))
        return headerView
    }
	
	func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
		let sectionType = sections[indexPath.section]
		switch sectionType {
		case .wallet:
			guard let wallet = sectionType.dataArray()[indexPath.row] as? WalletModel else { return }
			performSegue(withIdentifier: .bil_meToWalletDetailSegue, sender: wallet)
		default:
			()
		}
	}
    
    /*
     // Override to support conditional editing of the table view.
     override func tableView(_ tableView: UITableView, canEditRowAt indexPath: IndexPath) -> Bool {
     // Return false if you do not want the specified item to be editable.
     return true
     }
     */
}
