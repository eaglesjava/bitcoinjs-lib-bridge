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
            tableView.reloadData()
        }
    }
    
    func refreshUI() {
        if let w = wallet {
            idLabel.text = w.id
            addresses = w.btc_addressModels
            tableView.reloadData()
        }
    }
    @IBAction func scanMoreAddressAction(_ sender: Any) {
        guard let w = wallet else { return }
        bil_showLoading()
        let targetIndex = w.lastBTCAddressIndex + 10
        w.refreshAddressToSever(index: targetIndex, success: { (addresses) in
            BILWalletManager.shared.loadBlockHeightAndWalletVersion()
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
    
    func numberOfSections(in tableView: UITableView) -> Int {
        return 1
    }
    
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return addresses.count
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let cell = tableView.dequeueReusableCell(withIdentifier: "BILMeUnspentBalanceCell", for: indexPath) as! BILMeUnspentBalanceCell
        let utxo = addresses[indexPath.row]
        cell.titleLabel.text = utxo.address
        cell.subTitleLabel.text = BTCFormatString(btc: utxo.satoshi) + " BTC"
        cell.subTitleLabel.textColor = UIColor(white: 1.0, alpha: utxo.satoshi == 0 ? 0.3 : 1.0) 
        return cell
    }
    
    func tableView(_ tableView: UITableView, viewForHeaderInSection section: Int) -> UIView? {
        let headerView = tableView.dequeueReusableHeaderFooterView(withIdentifier: "BILTableViewHeaderFooterView") as! BILTableViewHeaderFooterView
        headerView.titleLabel.text = .meWalletAddress_addressAndBalance
        headerView.bgImageView.image = backgroundImage?.snapshotSubImage(rect: view.convert(headerView.frame, from: tableView))
        return headerView
    }
    
    func tableView(_ tableView: UITableView, heightForHeaderInSection section: Int) -> CGFloat {
        return 36
    }
    
}
