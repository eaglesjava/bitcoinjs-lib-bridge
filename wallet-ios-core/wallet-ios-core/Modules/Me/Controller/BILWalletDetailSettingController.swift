//
//  BILWalletDetailSettingController.swift
//  wallet-ios-core
//
//  Created by 仇弘扬 on 2017/12/22.
//  Copyright © 2017年 BitBill. All rights reserved.
//

import UIKit
import DateToolsSwift

class BILWalletDetailSettingController: BILLightBlueBaseController {

	var wallet: WalletModel?
	
	@IBOutlet weak var idLabel: UILabel!
	@IBOutlet weak var dateLabel: UILabel!
	@IBOutlet weak var qrImageView: UIImageView!
	
	override func viewDidLoad() {
        super.viewDidLoad()

        // Do any additional setup after loading the view.
		if let w = wallet {
			navigationItem.title = w.id
			idLabel.text = w.id
			dateLabel.text = w.createDate?.format(with: "yyyy.MM.dd")
			let width = qrImageView.frame.width
			qrImageView.image = w.id_qrString.qrCodeImage(targetSize: CGSize(width: width, height: width))
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
