//
//  BILTabBarController.swift
//  wallet-ios-core
//
//  Created by 仇弘扬 on 2017/11/17.
//  Copyright © 2017年 BitBill. All rights reserved.
//

import UIKit

class BILTabBarController: UITabBarController {

    override func viewDidLoad() {
        super.viewDidLoad()

        // Do any additional setup after loading the view.
		BILControllerManager.shared.mainTabBarController = self
		
		tabBar.backgroundImage = UIImage()
		tabBar.shadowImage = UIImage()
		tabBar.tintColor = UIColor.white
        tabBar.unselectedItemTintColor = UIColor(white: 1.0, alpha: 0.3)
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
