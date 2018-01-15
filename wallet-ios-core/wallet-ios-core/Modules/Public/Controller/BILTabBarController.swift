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
        
        languageDidChanged()
		NotificationCenter.default.addObserver(self, selector: #selector(languageDidChanged), name: .languageDidChanged, object: nil)
	}
	
	deinit {
		NotificationCenter.default.removeObserver(self, name: .languageDidChanged, object: nil)
	}
    
    override func viewWillAppear(_ animated: Bool) {
        super.viewWillAppear(animated)
        languageDidChanged()
    }
    
    override func languageDidChanged() {
		super.languageDidChanged()
        guard let items = tabBar.items else { return }
        let titles = ["Assets".bil_ui_localized, "Contacts".bil_ui_localized, "Receive".bil_ui_localized, "Send".bil_ui_localized, "Me".bil_ui_localized]
        for i in 0..<items.count {
            print(items[i].title!)
            items[i].title = titles[i]
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
