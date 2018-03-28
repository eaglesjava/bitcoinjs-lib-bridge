//
//  String+CompareVersion.swift
//  wallet-ios-core
//
//  Created by 仇弘扬 on 2018/3/28.
//  Copyright © 2018年 BitBill. All rights reserved.
//

import Foundation
extension String
{
    func ck_compare(with version: String) -> ComparisonResult {
        return compare(version, options: .numeric, range: nil, locale: nil)
    }
    
    func isNewer(than aVersionString: String) -> Bool {
        return ck_compare(with: aVersionString) == .orderedDescending
    }
    
    func isOlder(than aVersionString: String) -> Bool {
        return ck_compare(with: aVersionString) == .orderedAscending
    }
    
    func isSame(to aVersionString: String) -> Bool {
        return ck_compare(with: aVersionString) == .orderedSame
    }
}
