//
//  Array+Shuffle.swift
//  wallet-ios-core
//
//  Created by 仇弘扬 on 2018/1/16.
//  Copyright © 2018年 BitBill. All rights reserved.
//

import Foundation

extension Array {
    public func shuffle() -> Array {
        var list = self
        for index in 0..<list.count {
            let newIndex = Int(arc4random_uniform(UInt32(list.count-index))) + index
            if index != newIndex {
                list.swapAt(index, newIndex)
            }
        }
        return list
    }
}
