//
//  BILExchangeRateLabel.swift
//  wallet-ios-core
//
//  Created by 仇弘扬 on 2018/1/2.
//  Copyright © 2018年 BitBill. All rights reserved.
//

import UIKit

func getCurrency(btcValue: Double) -> String {
    let currencyType = BILSettingManager.currencyType
    let price = Double(btcValue) * currencyType.rate
	var formatString = NumberFormatter.localizedString(from: NSNumber(value: price), number: .currency)
	formatString.removeFirst()
    return formatString + " " + currencyType.symbolName
}

func getCurrency(btcValue: Int64) -> String {
    return getCurrency(btcValue: Double(btcValue) / Double(BTC_SATOSHI))
}

class BILExchangeRateLabel: UILabel {
    
    var btcValue: Double? = 0 {
        didSet {
            rateDidChanged()
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
    
    override func awakeFromNib() {
        super.awakeFromNib()
        rateDidChanged()
    }
    
    deinit {
        NotificationCenter.default.removeObserver(self, name: .exchangeRateDidChanged, object: nil)
    }
    
    func setupNotifation() {
        NotificationCenter.default.addObserver(self, selector: #selector(rateDidChanged), name: .exchangeRateDidChanged, object: nil)
    }
    
    @objc
    func rateDidChanged() {
        guard let value = btcValue else { return }
        text = getCurrency(btcValue: value)
    }
}
