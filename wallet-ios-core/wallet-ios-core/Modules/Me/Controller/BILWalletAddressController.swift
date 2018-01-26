//
//  BILWalletAddressController.swift
//  wallet-ios-core
//
//  Created by 仇弘扬 on 2017/12/25.
//  Copyright © 2017年 BitBill. All rights reserved.
//

import UIKit
import SVProgressHUD

class BILWalletAddressController: BILLightBlueBaseController {

    var wallet: WalletModel?
    
    @IBOutlet weak var idLabel: UILabel!
    @IBOutlet weak var tableView: UITableView!
    
	@IBOutlet weak var idTitleLabel: UILabel!
	@IBOutlet weak var scanButton: UIButton!
	var addresses = [BTCWalletAddressModel]()
    var changeAddresses = [BTCWalletAddressModel]()
    let sectionTitles = [.meWalletAddress_addressAndBalance, "Address of change".bil_ui_localized]
    
    override func viewDidLoad() {
        super.viewDidLoad()

        // Do any additional setup after loading the view.
        tableView.register(UINib(nibName: "BILTableViewHeaderFooterView", bundle: nil), forHeaderFooterViewReuseIdentifier: "BILTableViewHeaderFooterView")
        refreshUI()
        
        NotificationCenter.default.addObserver(self, selector: #selector(localUTXODidChanged(notification:)), name: .localUTXODidChanged, object: nil)
        wallet?.getUTXOFromServer(success: { (utxos) in
            self.refreshUI()
            self.bil_dismissHUD()
        }, failure: { (msg, code) in
            self.bil_showError(status: msg)
        })
    }
    
    deinit {
        NotificationCenter.default.removeObserver(self, name: .localUTXODidChanged, object: nil)
    }
	
	override func languageDidChanged() {
		super.languageDidChanged()
		title = "Wallet address".bil_ui_localized
		idTitleLabel.text = "Wallet ID".bil_ui_localized
		scanButton.setTitle("    Scan address    ".bil_ui_localized, for: .normal)
	}
    
    @objc
    func localUTXODidChanged(notification: Notification) {
        if let w = wallet {
            addresses = w.btc_addressModels
            changeAddresses = w.btc_changeAddressModels
            tableView.reloadData()
        }
    }
    
    func refreshUI() {
        if let w = wallet {
            idLabel.text = w.id
            addresses = w.btc_addressModels
            changeAddresses = w.btc_changeAddressModels
            tableView.reloadData()
        }
    }
    @IBAction func scanMoreAddressAction(_ sender: Any) {
        guard let w = wallet else { return }
        bil_showLoading()
        let targetIndex = w.lastBTCAddressIndex + 10
        let targetChangeIndex = w.lastBTCChangeAddressIndex + 10
        w.refreshAddressToSever(index: targetIndex, changeIndex: targetChangeIndex, success: { (addresses, changeAddresses) in
            BILWalletManager.shared.loadBlockHeightAndWalletVersion()
            w.getTransactionHistoryFromSever(page: 0, size: w.btc_transactionArray.count + 2000, success: { (txs) in
			}, failure: { (msg, code) in
			})
            w.getUTXOFromServer(success: { (utxos) in
                self.refreshUI()
                self.bil_showSuccess()
            }, failure: { (msg, code) in
                self.bil_showError(status: msg)
            })
        }) { (msg) in
            self.bil_showError(status: msg)
        }
    }
    
    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
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

extension BILWalletAddressController: UITableViewDelegate, UITableViewDataSource {
    
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
        return changeAddresses.count > 0 ? sectionTitles.count : 1
    }
    
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return section == 0 ? addresses.count : changeAddresses.count
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let cell = tableView.dequeueReusableCell(withIdentifier: "BILMeUnspentBalanceCell", for: indexPath) as! BILMeUnspentBalanceCell
        let utxo = indexPath.section == 0 ? addresses[indexPath.row] : changeAddresses[indexPath.row]
        cell.titleLabel.text = utxo.address
        cell.subTitleLabel.text = BTCFormatString(btc: utxo.satoshi) + " BTC"
        cell.subTitleLabel.textColor = UIColor(white: 1.0, alpha: utxo.satoshi == 0 ? 0.3 : 1.0) 
        return cell
    }
    
    func tableView(_ tableView: UITableView, viewForHeaderInSection section: Int) -> UIView? {
        let headerView = tableView.dequeueReusableHeaderFooterView(withIdentifier: "BILTableViewHeaderFooterView") as! BILTableViewHeaderFooterView
        headerView.titleLabel.text = sectionTitles[section]
        headerView.bgImageView.image = backgroundImage?.snapshotSubImage(rect: view.convert(headerView.frame, from: tableView))
        return headerView
    }
    
    func tableView(_ tableView: UITableView, heightForHeaderInSection section: Int) -> CGFloat {
        return 36
    }
    
}
