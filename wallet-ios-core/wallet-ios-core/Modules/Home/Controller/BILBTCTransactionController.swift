//
//  BILBTCTransactionController.swift
//  wallet-ios-core
//
//  Created by 仇弘扬 on 2017/12/30.
//  Copyright © 2017年 BitBill. All rights reserved.
//

import UIKit

class BILBTCTransactionController: BILLightBlueBaseController {
    
    var transaction: BTCTransactionModel?
    @IBOutlet weak var tableView: UITableView!
    
    @IBOutlet weak var targetAmountLabel: UILabel!
    @IBOutlet weak var statusLabel: UILabel!
    @IBOutlet weak var statusImageView: UIImageView!
    
	var datas = [(key: String, value: String, cellID: String)]()
    
    override func viewDidLoad() {
        super.viewDidLoad()

        // Do any additional setup after loading the view.
        tableView.estimatedRowHeight = 100
        tableView.rowHeight = UITableViewAutomaticDimension
        refreshUI()
    }
    
    func refreshUI() {
        guard let tx = transaction else { return }
        targetAmountLabel.text = tx.volumeString
        statusLabel.text = tx.status.description
        statusImageView.image = tx.status.image
        
		datas.append(("交易 hash", value: tx.txHash!, cellID: "BILTXDetailCell"))
        datas.append(("发送地址", value: "", cellID: "BILTXTitleCell"))
		for addModel in tx.inputAddressModels {
			datas.append((addModel.address!, value: BTCFormatString(btc: addModel.satoshi) + " BTC", cellID: "BILAddressCell"))
		}
        datas.append(("接收地址", value: "", cellID: "BILTXTitleCell"))
		for addModel in tx.outputAddressModels {
			datas.append((addModel.address!, value: BTCFormatString(btc: addModel.satoshi) + " BTC", cellID: "BILAddressCell"))
		}
        if tx.status == .success {
            datas.append(("确认", value: "\(tx.confirmCount)", cellID: "BILTXDetailCell"))
        }
        datas.append(("备注", value: tx.remarkString, cellID: "BILTXDetailCell"))
        datas.append(("交易时间", value: tx.dateSring, cellID: "BILTXDetailCell"))
    }
    
    override func viewWillAppear(_ animated: Bool) {
        super.viewWillAppear(animated)
        navigationController?.setNavigationBarHidden(false, animated: false)
    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    
    @IBAction func checkTXAction(_ sender: Any) {
        guard let txHash = transaction?.txHash else { return }
        if let url = URL(string: "\(String.bil_extenal_blockchain_transaction)\(txHash)") {
            UIApplication.shared.open(url, options: [:], completionHandler: nil)
        }
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

extension BILBTCTransactionController: UITableViewDelegate, UITableViewDataSource {
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return datas.count
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
		let element = datas[indexPath.row]
        let cell = tableView.dequeueReusableCell(withIdentifier: element.cellID, for: indexPath)
		
		switch element.cellID {
		case "BILTXDetailCell", "BILTXTitleCell":
			let c = cell as! BILTXDetailCell
			c.keyLabel?.text = element.key
			c.valueLabel?.text = element.value
			c.valueLabel?.valueTitle = element.key
		case "BILAddressCell":
			let c = cell as! BILAddressCell
			c.addressLabel.text = element.key
			c.amountLabel.text = element.value
		default:
			()
		}
		
        return cell
    }
}
