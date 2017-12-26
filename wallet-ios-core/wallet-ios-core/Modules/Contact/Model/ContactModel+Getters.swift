//
//  Contact+Lazy.swift
//  wallet-ios-core
//
//  Created by 仇弘扬 on 2017/12/26.
//  Copyright © 2017年 BitBill. All rights reserved.
//

import Foundation

enum ContactAdditionType: Int16 {
	case address = 0
	case walletID
}

extension ContactModel: Comparable {
	public static func <(lhs: ContactModel, rhs: ContactModel) -> Bool {
		return lhs.name! < rhs.name!
	}
	
	var firstNameWord: String {
		get {
			return String(name!.first ?? "B")
		}
	}
	
	var firstNameLetter: String {
		guard let letter = name!.firstUpperLetter().first else {
			return "#"
		}
		return (letter <= "Z" && letter >= "A") ? String(letter) : "#"
	}
	
	var additionType: ContactAdditionType {
		get { return ContactAdditionType(rawValue: additionTypeRawValue) ?? .address }
		set { additionTypeRawValue = newValue.rawValue }
	}
	
	var coinType: CoinType {
		get { return CoinType(rawValue: coinTypeRawValue) ?? .btc }
		set { coinTypeRawValue = newValue.rawValue }
	}
	
	var detail: String {
		return additionType == .address ? address! : walletID!
	}
	
	var remarkString: String {
		get {
			return remark!.isEmpty ? "无" : remark!
		}
	}
}
