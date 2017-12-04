//
//  BILNetworkManager.swift
//  wallet-ios-core
//
//  Created by 仇弘扬 on 2017/11/30.
//  Copyright © 2017年 BitBill. All rights reserved.
//

import UIKit
import Alamofire
import SwiftyJSON

class BILNetworkManager: NSObject {
    static func request(request: Router, sucess: @escaping ([String: JSON]) -> Void, failure: @escaping (_ message: String, _ code: Int) -> Void) {
        Alamofire.request(request).responseJSON { (response) in
            debugPrint(response)
            if let json = response.result.value as? [String : Any] {
                debugPrint("JSON: \(json)") // serialized json response
                let j = JSON(json)
                let status = j["status"].intValue
                if status < 0 {
                    failure(j["message"].stringValue, j["status"].intValue)
                }
                else
                {
                    sucess(j["data"].dictionaryValue)
                }
            }
            else
            {
                failure("数据解析失败，\(response)", -1)
            }
        }
    }
	static func request() {
//        Alamofire.request("http://192.168.1.11:8086/bitbill/bitcoin/cli", method: .post, parameters: ["walletId": "Tesata", "extendedKeys": "xpub6EvXuejgrwbSQAk3YaaMfmXsoMEx7CgSLw4P7UjYKd8hbbZ2n4jp1LrVrbNMEK1qBzbmb6FeJVEHUXzDqYSPucHu5Yqc95r7YuasYyyB91N", "clientId": "abcdefghijklmn"], encoding: JSONEncoding.default).responseJSON(queue: nil, options: .allowFragments) { (response) in
//            debugPrint(response.request ?? "request is nil")
//            debugPrint(response)
//        }
//        Alamofire.request("http://192.168.1.10:8086/bitbill/bitcoin/wallet/create", method: .post, parameters: ["walletId": "qwerqwer", "extendedKeys": "xpub6Dcfk6kxvgD7kP1T5eZk53GJQbFJxz2PBozNvMTeCSancKfgwpUrCkye5rbTfufBPRgQcqNc6yuBPpYo3siYfhbe1UCF7gNRknUSrhpbScS", "clientId": "61D568B3-8254-467C-A76C-320459C6BCDF"], encoding: JSONEncoding.default).responseJSON(queue: nil, options: .allowFragments) { (response) in
//            debugPrint(response.request ?? "request is nil")
//            debugPrint(response)
//        }
	}
	
}
