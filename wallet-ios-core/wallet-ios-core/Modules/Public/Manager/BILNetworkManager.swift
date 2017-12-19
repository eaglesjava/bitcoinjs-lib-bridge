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
    static func request(request: Router, success: @escaping ([String: JSON]) -> Void, failure: @escaping (_ message: String, _ code: Int) -> Void) {
        Alamofire.request(request).responseJSON { (response) in
            debugPrint(request)
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
                    success(j["data"].dictionaryValue)
                }
            }
            else
            {
                failure("数据解析失败，\(response)", -1)
            }
        }
    }
	
}
