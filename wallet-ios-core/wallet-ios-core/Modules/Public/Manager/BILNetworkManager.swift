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
    static func request(request: Router, sucess: @escaping ([String: Any]?) -> Void, failure: @escaping (_ message: String, _ code: Int) -> Void) {
        Alamofire.request(request).responseJSON { (response) in
            print(response)
            sucess(nil)
        }
    }
	static func request() {
		Alamofire.request("http://192.168.1.10:8086/bitbill/bitcoin/wallet/create", method: .post, parameters: ["walletId": "Tesat", "extendedKeys": "xpub6EvXuejgrwbSQAk3YaaMfmXsoMEx7CgSLw4P7UjYKd8hbbZ2n4jp1LrVrbNMEK1qBzbmb6FeJVEHUXzDqYSPucHu5Yqc95r7YuasYyyB91N", "clientId": "abcdefghijklmn"], encoding: JSONEncoding.default).responseJSON(queue: nil, options: .allowFragments) { (response) in
//            print(response.result.value)
		}
	}
	
}
