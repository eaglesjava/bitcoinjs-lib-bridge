//
//  Contact+Network.swift
//  wallet-ios-core
//
//  Created by 仇弘扬 on 2017/12/20.
//  Copyright © 2017年 BitBill. All rights reserved.
//

import Foundation
import SwiftyJSON

extension Contact {
    func getContactsFromServer(success: @escaping ([Contact]) -> Void, failure: @escaping (_ message: String, _ code: Int) -> Void) {
        BILNetworkManager.request(request: .getContactLastAddress(walletID: walletID), success: { (data) in
            
        }, failure: failure)
    }
    static func addContactToServer(id: String = "", address: String = "", name: String, remark: String = "", success: @escaping (Contact) -> Void, failure: @escaping (_ message: String, _ code: Int) -> Void) {
        BILNetworkManager.request(request: .addContact(walletID: id, name: name, remark: remark, address: address), success: { (data) in
            let contact = Contact(name: name, walletID: id, address: address, remark: remark)
            success(contact)
            NotificationCenter.default.post(name: .contactDidChanged, object: contact)
        }) { (msg, code) in
            
        }
    }
    static func getContactFromServer(by id: String, success: @escaping (String) -> Void, failure: @escaping (_ message: String, _ code: Int) -> Void) {
        BILNetworkManager.request(request: .searchWalletID(walletID: id), success: { (data) in
            success(id)
        }, failure: failure)
    }
    static func getContactsFromServer(success: @escaping ([Contact]) -> Void, failure: @escaping (_ message: String, _ code: Int) -> Void) {
        BILNetworkManager.request(request: .getContacts, success: { (data) in
            let json = JSON(data)
            var contacts = [Contact]()
            for subJson in json["rows"].arrayValue {
                let contact = Contact(jsonData: subJson)
                contacts.append(contact)
            }
            success(contacts)
        }, failure: failure)
    }
}
