//
//  BILSendResultController.swift
//  wallet-ios-core
//
//  Created by 仇弘扬 on 2017/12/13.
//  Copyright © 2017年 BitBill. All rights reserved.
//

import UIKit
import SafariServices

class BILSendResultController: BILBaseViewController {

    @IBOutlet weak var addressLabel: UILabel!
    @IBOutlet weak var amountLabel: UILabel!
    
    var sendModel: BILSendModel?
    
    override func viewDidLoad() {
        super.viewDidLoad()

        // Do any additional setup after loading the view.
        
        if let model = sendModel {
            addressLabel.text = model.address
            amountLabel.text = "\(model.bitcoinAmount) BTC"
        }
    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    
    @IBAction func done(_ sender: Any) {
        dismiss(animated: true, completion: nil)
    }
    
    @IBAction func checkTransaction(_ sender: Any) {
        let cont = SFSafariViewController(url: URL(string: "https://blockchain.info")!)
        present(cont, animated: true, completion: nil)
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
