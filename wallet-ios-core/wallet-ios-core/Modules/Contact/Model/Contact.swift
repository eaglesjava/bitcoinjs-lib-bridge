//
//  BILContact.swift
//  wallet-ios-core
//
//  Created by 仇弘扬 on 2017/12/18.
//  Copyright © 2017年 BitBill. All rights reserved.
//

import UIKit
import SwiftyJSON

enum ContactAdditionType {
    case address
    case walletID
}

class Contact: NSObject, Comparable {
    static func <(lhs: Contact, rhs: Contact) -> Bool {
        return lhs.name < rhs.name
    }
    
    var name: String
    var walletID: String
    var address: String
    var remark: String
    
    var additionType: ContactAdditionType = .address
    
    lazy var firstNameWord: String = {
       return String(name.first ?? "B")
    }()
    
    lazy var firstNameLetter: String = {
        guard let letter = name.firstUpperLetter().first else {
            return "#"
        }
        return (letter <= "Z" && letter >= "A") ? String(letter) : "#"
    }()
    
    lazy var detail: String = {
        return additionType == .address ? address : walletID
    }()
    
    var remarkString: String {
        get {
            return remark.isEmpty ? "无" : remark
        }
    }
    
    init(jsonData: JSON) {
        name = jsonData["contactName"].stringValue
        walletID = jsonData["walletContact"].stringValue
        address = jsonData["walletAddress"].stringValue
        remark = jsonData["remark"].stringValue
        additionType = walletID.isEmpty ? .address : .walletID
    }
    
    init(name: String, walletID: String = "", address: String = "", remark: String = "") {
        self.name = name
        self.walletID = walletID
        self.address = address
        additionType = walletID.isEmpty ? .address : .walletID
        self.remark = remark
    }
    
}
