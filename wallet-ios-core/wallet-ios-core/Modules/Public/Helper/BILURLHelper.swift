//
//  BILURLHelper.swift
//  wallet-ios-core
//
//  Created by 仇弘扬 on 2017/12/12.
//  Copyright © 2017年 BitBill. All rights reserved.
//

import UIKit
import Foundation

class BILURLHelper: NSObject {
	static func transferContactURL(urlString: String) -> String? {
		guard let url = URL(string: urlString) else {
			return nil
		}
		
		guard let components = URLComponents(url: url, resolvingAgainstBaseURL: false) else {
			return nil
		}
		guard components.scheme == "bitbill", components.host == "www.bitbill.com", components.path == "/contact", let items = components.queryItems else {
			return nil
		}
		
		for item in items {
			debugPrint(item)
			if item.name == "id" {
				return item.value
			}
		}
		
		return nil
	}
    static func transferBitCoinURL(urlString: String) -> (address: String, amount: Decimal)? {
        let coinType = CoinType.btc
        if urlString.lowercased().starts(with: coinType.scheme) {
            let arr = urlString.components(separatedBy: ":")
            guard let addressString = arr.last else {
                return nil
            }
            
            if addressString.contains("?") {
                let infoArr = addressString.components(separatedBy: "?")
                if infoArr.count >= 2 {
					let amount = infoArr[1].replacingOccurrences(of: "amount=", with: "")
					return (infoArr[0], Decimal(string: amount) ?? -1)
                }
            }
            else
            {
                return (addressString, -1)
            }
        }
        
        return (urlString, -1)
    }
}
