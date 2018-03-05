//
//  BaseNavigationController.swift
//  wallet-ios-core
//
//  Created by 仇弘扬 on 2018/2/28.
//  Copyright © 2018年 BitBill. All rights reserved.
//

import UIKit

class BaseNavigationController: UINavigationController {

    override func viewDidLoad() {
        super.viewDidLoad()

        // Do any additional setup after loading the view.
        let appearance = navigationBar
        appearance.setBackgroundImage(UIImage(), for: .default)
        appearance.shadowImage = UIImage()
        appearance.titleTextAttributes = [.foregroundColor: UIColor.white]
        appearance.tintColor = UIColor.white
        if #available(iOS 11.0, *) {
            appearance.prefersLargeTitles = UIScreen.main.bounds.height > 568
            appearance.largeTitleTextAttributes = [.foregroundColor: UIColor.white]
        }
    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    
    override var preferredStatusBarStyle: UIStatusBarStyle {
        get {
            return .lightContent
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
