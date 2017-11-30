//
//  BILBaseViewController.swift
//  wallet-ios-core
//
//  Created by 仇弘扬 on 2017/11/17.
//  Copyright © 2017年 BitBill. All rights reserved.
//

import UIKit

class BILBaseViewController: UIViewController {
	
    override func viewDidLoad() {
        super.viewDidLoad()

        // Do any additional setup after loading the view.
    }
	
	func bil_setBackgroudColor() {
		_ = view.setupGradient(colors: [UIColor.bil_deep_blue_start_bgcolor.cgColor, UIColor.bil_deep_blue_end_bgcolor.cgColor], startPoint: CGPoint(x: 0.3, y: 0), endPoint: CGPoint(x: 0.7, y: 1))
		view.backgroundColor = UIColor.clear
	}

	override func viewDidLayoutSubviews() {
		super.viewDidLayoutSubviews()
		bil_setBackgroudColor()
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
