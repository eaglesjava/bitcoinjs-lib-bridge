//
//  BILNetworkManager.swift
//  wallet-ios-core
//
//  Created by 仇弘扬 on 2017/11/30.
//  Copyright © 2017年 BitBill. All rights reserved.
//

import UIKit
import Alamofire

class BILNetworkManager: NSObject {
	static let shared = {
		return BILNetworkManager()
	}()
	
	func request() {
		Alamofire.request("http://192.168.1.10:8086/bitbill/bitcoin/wallet/create", method: .post, parameters: ["walletId": "Tesat", "extendedKeys": "xpub6EvXuejgrwbSQAk3YaaMfmXsoMEx7CgSLw4P7UjYKd8hbbZ2n4jp1LrVrbNMEK1qBzbmb6FeJVEHUXzDqYSPucHu5Yqc95r7YuasYyyB91N", "clientId": "abcdefghijklmn"], encoding: URLEncoding.default).responseJSON(queue: nil, options: .allowFragments) { (response) in
			print(response)
//			JSONEncoding.default
		}
	}
	
}
