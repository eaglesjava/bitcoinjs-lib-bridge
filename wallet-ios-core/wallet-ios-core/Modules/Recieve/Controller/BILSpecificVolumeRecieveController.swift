//
//  BILSpecificVolumeRecieveController.swift
//  wallet-ios-core
//
//  Created by 仇弘扬 on 2017/12/8.
//  Copyright © 2017年 BitBill. All rights reserved.
//

import UIKit

class BILSpecificVolumeRecieveController: BILBaseViewController {

	var recieveModel: BILRecieveModel?
	
	@IBOutlet weak var addressLabel: UILabel!
	@IBOutlet weak var qrCodeImageView: UIImageView!
	@IBOutlet weak var amountLabel: UILabel!
	override func viewDidLoad() {
        super.viewDidLoad()

        // Do any additional setup after loading the view.
		if let r = recieveModel {
			addressLabel.text = r.address
			qrCodeImageView.image = BILQRCodeHelper.generateQRCode(msg: r.schemeString)
			amountLabel.text = "\(r.volume) \(r.coinType.name)"
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
