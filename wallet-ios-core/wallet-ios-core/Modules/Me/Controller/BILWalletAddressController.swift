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
    
    var addresses = [BTCAddressModel]()
    
    override func viewDidLoad() {
        super.viewDidLoad()

        // Do any additional setup after loading the view.
        tableView.register(UINib(nibName: "BILTableViewHeaderFooterView", bundle: nil), forHeaderFooterViewReuseIdentifier: "BILTableViewHeaderFooterView")
        refreshUI()
    }
    
    func refreshUI() {
        if let w = wallet {
            idLabel.text = w.id
            addresses = w.addresses?.array as! [BTCAddressModel]
        }
    }
    @IBAction func scanMoreAddressAction(_ sender: Any) {
        guard let w = wallet else { return }
        SVProgressHUD.show(withStatus: "Scaning...")
        w.getNewBTCAddress(step: 10, success: { (address) in
            self.refreshUI()
            self.bil_dismissHUD()
        }) { (msg) in
            SVProgressHUD.showError(withStatus: msg)
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
        return cell
    }
    
    func tableView(_ tableView: UITableView, viewForHeaderInSection section: Int) -> UIView? {
        let headerView = tableView.dequeueReusableHeaderFooterView(withIdentifier: "BILTableViewHeaderFooterView") as! BILTableViewHeaderFooterView
        headerView.titleLabel.text = "地址与余额"
        headerView.bgImageView.image = backgroundImage?.snapshotSubImage(rect: view.convert(headerView.frame, from: tableView))
        return headerView
    }
    
    func tableView(_ tableView: UITableView, heightForHeaderInSection section: Int) -> CGFloat {
        return 36
    }
    
}
