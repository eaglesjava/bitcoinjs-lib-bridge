//
//  BILChooseWalletController.swift
//  wallet-ios-core
//
//  Created by 仇弘扬 on 2017/12/9.
//  Copyright © 2017年 BitBill. All rights reserved.
//

import UIKit

class BILChooseWalletController: BILBaseViewController, UITableViewDelegate, UITableViewDataSource {

	@IBOutlet weak var tableView: UITableView!
	fileprivate let cellID = "BILChooseWalletCell"
    
    var targetAmount = 0
	
    fileprivate var wallets: [WalletModel] {
        get {
            return BILWalletManager.shared.wallets
        }
    }
	fileprivate var currentSelectedIndex = 0
	
	fileprivate var didSelectClosure: ((WalletModel) -> Void)?
	
    @IBOutlet weak var titleLabel: UILabel?
    override func viewDidLoad() {
        super.viewDidLoad()

        // Do any additional setup after loading the view.
        NotificationCenter.default.addObserver(self, selector: #selector(walletDidChanged(notification:)), name: .receivePageCurrentWallet, object: nil)
        languageDidChanged()
    }
    
    override func viewWillAppear(_ animated: Bool) {
        super.viewWillAppear(animated)
        tableView.selectRow(at: IndexPath(row: currentSelectedIndex, section: 0), animated: false, scrollPosition: .top)
    }
    
    override func languageDidChanged() {
		super.languageDidChanged()
        titleLabel?.text = "Choose wallet".bil_ui_localized
		tableView.reloadData()
    }
    
    override func viewDidLayoutSubviews() {
        super.viewDidLayoutSubviews()
        gradientLayer?.removeFromSuperlayer()
    }
    
    @objc
    func walletDidChanged(notification: Notification) {
        tableView.reloadData()
        guard let wallet = notification.object as? WalletModel, let index = wallets.index(of: wallet) else {
            return
        }
        currentSelectedIndex = index
        tableView.selectRow(at: IndexPath(row: currentSelectedIndex, section: 0), animated: false, scrollPosition: .top)
    }
    
    deinit {
        NotificationCenter.default.removeObserver(self, name: .receivePageCurrentWallet, object: nil)
    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
	
	func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
		return wallets.count
	}
	
	func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
		let cell = tableView.dequeueReusableCell(withIdentifier: cellID, for: indexPath) as! BILChooseWalletCell
		cell.wallet = wallets[indexPath.row]
		if indexPath.row == wallets.count - 1 {
			cell.separatorInset = UIEdgeInsetsMake(0, 0, 0, UIScreen.main.bounds.width)
		}
        else
        {
            cell.separatorInset = UIEdgeInsetsMake(0, 25, 0, 25)
        }
		
		return cell
	}
	
	func tableView(_ tableView: UITableView, heightForRowAt indexPath: IndexPath) -> CGFloat {
		return 85.0
	}
	
	func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
		didSelectClosure?(wallets[indexPath.row])
        currentSelectedIndex = indexPath.row
	}
	
	func setDidSelecteWalletClosure(currentSelectedIndex: Int = 0, onSelected: @escaping (WalletModel) -> Void) {
		didSelectClosure = onSelected
		self.currentSelectedIndex = currentSelectedIndex
	}

    /*
    // MARK: - Navigation

    // In a storyboard-based application, you will often want to do a little preparation before navigation
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        // Get the new view controller using segue.destinationViewController.
        // Pass the selected object to the new view controller.
    }
    */

}
