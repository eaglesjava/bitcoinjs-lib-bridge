//
//  BILVerifyMnemonicController.swift
//  wallet-ios-core
//
//  Created by 仇弘扬 on 2017/11/16.
//  Copyright © 2017年 BitBill. All rights reserved.
//

import UIKit

class BILVerifyMnemonicController: UIViewController {

	@IBOutlet weak var randomMnemonicView: BILMnemonicView!
	
	var randomArray = [String]()
	var dataArray = [String]() {
		didSet {
			var arr = [String]()
			for _ in 0..<dataArray.count {
				arr.append(dataArray.remove(at: Int(arc4random()) % dataArray.count))
			}
			randomArray = arr
		}
	}
	
	var mnemonic = "" {
		didSet {
			dataArray = mnemonic.components(separatedBy: " ")
		}
	}
	
	override func viewDidLoad() {
        super.viewDidLoad()

        // Do any additional setup after loading the view.
		randomMnemonicView.dataArray = randomArray
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
