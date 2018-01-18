//
//  BILSupportedCoinsPopupController.swift
//  wallet-ios-core
//
//  Created by 仇弘扬 on 2018/1/18.
//  Copyright © 2018年 BitBill. All rights reserved.
//

import UIKit

class BILSupportedCoinsPopupController: UIViewController {
    @IBOutlet weak var titleLabel: UILabel!
    
    override func viewDidLoad() {
        super.viewDidLoad()

        // Do any additional setup after loading the view.
        languageDidChanged()
    }
    
    override func languageDidChanged() {
        super.languageDidChanged()
        titleLabel.text = "Currencies supported currently".bil_ui_localized
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
