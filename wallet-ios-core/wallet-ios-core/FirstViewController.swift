//
//  FirstViewController.swift
//  wallet-ios-core
//
//  Created by 仇弘扬 on 2017/11/7.
//  Copyright © 2017年 BitBill. All rights reserved.
//

import UIKit

class FirstViewController: UIViewController {

	override func viewDidLoad() {
		super.viewDidLoad()
		// Do any additional setup after loading the view, typically from a nib.
		DispatchQueue.main.asyncAfter(deadline: .now() + .seconds(2)) {
			BitcoinJSBridge.shared.generateMnemonic(success: { (obj) in
				print(obj)
			}) { (error) in
				print(error)
			}
		}
	}

	override func didReceiveMemoryWarning() {
		super.didReceiveMemoryWarning()
		// Dispose of any resources that can be recreated.
	}


}

