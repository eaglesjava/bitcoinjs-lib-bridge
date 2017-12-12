//
//  BILScanQRCodeResultController.swift
//  wallet-ios-core
//
//  Created by 仇弘扬 on 2017/12/12.
//  Copyright © 2017年 BitBill. All rights reserved.
//

import UIKit

class BILScanQRCodeResultController: BILBaseViewController {

    @IBOutlet weak var amountTextField: UITextField!
    @IBOutlet weak var coinNameLabel: UILabel!
    @IBOutlet weak var cnyLabel: UILabel!
    @IBOutlet weak var addressLabel: UILabel!
    
    var sendModel: BILSendModel?
    
    override func viewDidLoad() {
        super.viewDidLoad()

        // Do any additional setup after loading the view.
        addressLabel.text = sendModel?.address
        coinNameLabel.text = sendModel?.coinType.name
        amountTextField.text = sendModel?.amount
    }
    
    override func viewDidAppear(_ animated: Bool) {
        super.viewDidAppear(animated)
        guard let viewControllers = navigationController?.viewControllers else { return }
        for cont in viewControllers.reversed() {
            if cont is BILQRCodeScanViewController {
                navigationController?.viewControllers.remove(at: viewControllers.count - 2)
                navigationController?.view.setNeedsLayout()
                navigationController?.view.layoutIfNeeded()
                break
            }
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
