//
//  BILURLHelper.swift
//  wallet-ios-core
//
//  Created by 仇弘扬 on 2017/12/12.
//  Copyright © 2017年 BitBill. All rights reserved.
//

import UIKit

class BILURLHelper: NSObject {
    static func transferBitCoinURL(urlString: String) -> (address: String, amount: Double)? {
        let coinType = CoinType.btc
        if urlString.lowercased().starts(with: coinType.scheme) {
            let arr = urlString.components(separatedBy: ":")
            guard let addressString = arr.last else {
                return nil
            }
            
            if addressString.contains("?") {
                let infoArr = addressString.components(separatedBy: "?")
                if infoArr.count >= 2, let amount = Double(infoArr[1].replacingOccurrences(of: "amount=", with: "")) {
                    return (infoArr[0], amount)
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
