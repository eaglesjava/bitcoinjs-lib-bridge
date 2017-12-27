//
//  BILAgreementController.swift
//  wallet-ios-core
//
//  Created by 仇弘扬 on 2017/12/26.
//  Copyright © 2017年 BitBill. All rights reserved.
//

import UIKit

class BILAgreementController: BILWebViewController {
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        if #available(iOS 11.0, *) {
            navigationItem.largeTitleDisplayMode = .never
        } else {
            // Fallback on earlier versions
        }

        // Do any additional setup after loading the view.
        guard let htmlURL = Bundle.main.url(forResource: "service_cn", withExtension: "html") else {
            return
        }
        webView.loadFileURL(htmlURL, allowingReadAccessTo: htmlURL)
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
