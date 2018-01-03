//
//  BILExchangeRateLabel.swift
//  wallet-ios-core
//
//  Created by 仇弘扬 on 2018/1/2.
//  Copyright © 2018年 BitBill. All rights reserved.
//

import UIKit

class BILExchangeRateLabel: UILabel {
    
    var btcValue: Int64 = 0 {
        didSet {
            rateDidchange()
        }
    }
    
    override init(frame: CGRect) {
        super.init(frame: frame)
        setupNotifation()
    }
    
    required init?(coder aDecoder: NSCoder) {
        super.init(coder: aDecoder)
        setupNotifation()
    }
    
    deinit {
        NotificationCenter.default.removeObserver(self, name: .exchangeRateDidChanged, object: nil)
    }
    
    func setupNotifation() {
        NotificationCenter.default.addObserver(self, selector: #selector(rateDidchange), name: .exchangeRateDidChanged, object: nil)
    }
    
    @objc
    func rateDidchange() {
        let price = Double(btcValue) * BILWalletManager.shared.cnyExchangeRate / Double(BTC_SATOSHI)
        text = String(format: "%.2f", price) + " CNY"
    }
}
