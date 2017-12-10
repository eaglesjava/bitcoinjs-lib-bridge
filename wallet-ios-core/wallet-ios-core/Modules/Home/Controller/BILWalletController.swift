//
//  BILWalletController.swift
//  wallet-ios-core
//
//  Created by 仇弘扬 on 2017/11/29.
//  Copyright © 2017年 BitBill. All rights reserved.
//

import UIKit

class BILWalletController: BILBaseViewController {

	var wallet: WalletModel?
	@IBOutlet weak var btcWalletView: BILBTCWalletView!
	
    override func viewDidLoad() {
        super.viewDidLoad()

        // Do any additional setup after loading the view.
		title = wallet?.id
		
		btcWalletView.wallet = wallet
    }
	
	override func viewWillAppear(_ animated: Bool) {
		super.viewWillAppear(animated)
		navigationController?.setNavigationBarHidden(false, animated: false)
	}
	
	override func bil_setBackgroudColor() {
        if gradientLayer == nil {
            gradientLayer = view.setupGradient(colors: [UIColor.bil_gradient_start_color.cgColor, UIColor.bil_gradient_end_color.cgColor], startPoint: CGPoint(x: 0, y: 0), endPoint: CGPoint(x: 1, y: 1))
        }
        gradientLayer?.frame = UIScreen.main.bounds
		view.backgroundColor = UIColor.clear
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
