//
//  Contact+Network.swift
//  wallet-ios-core
//
//  Created by 仇弘扬 on 2017/12/20.
//  Copyright © 2017年 BitBill. All rights reserved.
//

import Foundation
import SwiftyJSON

extension ContactModel {
    func getContactAddressFromServer(success: @escaping (String) -> Void, failure: @escaping (_ message: String, _ code: Int) -> Void) {
        BILNetworkManager.request(request: .getContactLastAddress(walletID: walletID!), success: { (data) in
            let json = JSON(data)
            success(json["address"].stringValue)
        }, failure: failure)
    }
    
    func updateToServer(name: String, remark: String = "", success: @escaping (ContactModel) -> Void, failure: @escaping (_ message: String, _ code: Int) -> Void) {
        BILNetworkManager.request(request: .updateContact(walletID: walletID!, name: name, remark: remark, address: address!), success: { (data) in
            do {
                self.name = name
                self.remark = remark
                try bil_contactManager.saveModels()
                success(self)
            } catch {
                failure(error.localizedDescription, -2)
            }
        }, failure: failure)
    }
    
    func deleteFromServer(success: @escaping () -> Void, failure: @escaping (_ message: String, _ code: Int) -> Void) {
        BILNetworkManager.request(request: .deleteContact(walletID: walletID!, address: address!), success: { (data) in
            do {
                try bil_contactManager.remove(model: self)
                success()
            } catch {
                failure(error.localizedDescription, -2)
            }
        }, failure: failure)
    }
    
    static func addContactToServer(id: String = "", address: String = "", name: String, remark: String = "", coinType: CoinType = .btc, success: @escaping (ContactModel) -> Void, failure: @escaping (_ message: String, _ code: Int) -> Void) {
        BILNetworkManager.request(request: .addContact(walletID: id, name: name, remark: remark, address: address, coinType: coinType), success: { (data) in
            let contact = bil_contactManager.newModel()
			contact.setupProperties(name: name, walletID: id, address: address, remark: remark, coinType: coinType.rawValue)
			do {
				try bil_contactManager.saveModels()
				success(contact)
			} catch {
				failure(error.localizedDescription, -1)
			}
        }, failure: failure)
    }
    static func getContactFromServer(by id: String, success: @escaping (String) -> Void, failure: @escaping (_ message: String, _ code: Int) -> Void) {
        BILNetworkManager.request(request: .searchWalletID(walletID: id), success: { (data) in
            success(id)
        }, failure: failure)
    }
    
    static func saveServerContacts(data: [String: JSON], success: @escaping ([ContactModel]) -> Void, failure: @escaping (_ message: String, _ code: Int) -> Void) {
        let json = JSON(data)
        var contacts = [ContactModel]()
        for subJson in json["rows"].arrayValue {
            let contact = bil_contactManager.newModel()
            contact.setupProperties(jsonData: subJson)
            contacts.append(contact)
        }
        do {
            try bil_contactManager.saveModels()
            success(contacts)
        } catch {
            failure(error.localizedDescription, -1)
        }
    }
    
    static func recoverContactsFromServer(recoverKey: String, success: @escaping ([ContactModel]) -> Void, failure: @escaping (_ message: String, _ code: Int) -> Void) {
        BILNetworkManager.request(request: .recoverContacts(recoverKey: recoverKey), success: { (data) in
            ContactModel.saveServerContacts(data: data, success: success, failure: failure)
        }, failure: failure)
    }
    
    static func getContactsFromServer(success: @escaping ([ContactModel]) -> Void, failure: @escaping (_ message: String, _ code: Int) -> Void) {
        BILNetworkManager.request(request: .getContacts, success: { (data) in
            ContactModel.saveServerContacts(data: data, success: success, failure: failure)
        }, failure: failure)
    }
	
	func setupProperties(jsonData: JSON) {
		setupProperties(name: jsonData["contactName"].stringValue,
						walletID: jsonData["walletContact"].stringValue,
						address: jsonData["walletAddress"].stringValue,
						remark: jsonData["remark"].stringValue,
						coinType: jsonData["coinType"].int16Value)
	}
	
	func setupProperties(name: String, walletID: String = "", address: String = "", remark: String = "", coinType: Int16 = CoinType.btc.rawValue) {
		self.name = name
		self.walletID = walletID
		self.address = address
		self.remark = remark
		self.coinType = CoinType(rawValue: coinType) ?? .btc
		additionType = !walletID.isEmpty ? .walletID : .address
	}
}
