//
//  BILRecieveModel.swift
//  wallet-ios-core
//
//  Created by 仇弘扬 on 2017/12/8.
//  Copyright © 2017年 BitBill. All rights reserved.
//

import UIKit

struct BILRecieveModel {
	enum CoinType {
		case btc
		
		var name: String {
			get {
				switch self {
				case .btc:
					return "BTC"
				}
			}
		}
		
		var scheme: String {
			get {
				switch self {
				case .btc:
					return "bitcoin"
				}
			}
		}
		
	}
	
	var address: String
	var volume: String
	var coinType: CoinType
	
	init(address: String, volume: String, coinType: CoinType = .btc) {
		self.address = address
		self.volume = volume
		self.coinType = coinType
	}
	
	var schemeString: String {
		get {
			return "\(coinType.scheme):\(address)" + (volume.isEmpty ? "" : "?amount=\(volume)")
		}
	}
}
