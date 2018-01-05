//
//  BILAgreementController.swift
//  wallet-ios-core
//
//  Created by 仇弘扬 on 2017/12/26.
//  Copyright © 2017年 BitBill. All rights reserved.
//

import UIKit

class BILAgreementController: BILBaseViewController {
    
    @IBOutlet weak var textView: UITextView!
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        if #available(iOS 11.0, *) {
            navigationItem.largeTitleDisplayMode = .never
        } else {
            // Fallback on earlier versions
        }

        // Do any additional setup after loading the view.
    }
    
    override func viewDidAppear(_ animated: Bool) {
        super.viewDidAppear(true)
//        textView.scrollRectToVisible(CGRect.zero, animated: false)
    }
    
    override func viewDidLayoutSubviews() {
        super.viewDidLayoutSubviews()
        if textView.attributedText.string.isEmpty {
            textView.contentInset = UIEdgeInsets(top: 0, left: 25, bottom: 0, right: 25)
            guard let htmlURL = Bundle.main.url(forResource: "service_cn", withExtension: "html") else {
                return
            }
            do {
                let data = try Data(contentsOf: htmlURL)
                let att = try NSAttributedString(data: data, options: [NSAttributedString.DocumentReadingOptionKey.documentType: NSAttributedString.DocumentType.html], documentAttributes: nil)
                textView.attributedText = att
            } catch {
                showTipAlert(msg: error.localizedDescription, dismissed: {
                    self.navigationController?.popViewController(animated: true)
                })
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
