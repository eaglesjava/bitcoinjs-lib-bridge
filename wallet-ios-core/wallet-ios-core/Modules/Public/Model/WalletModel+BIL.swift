//
//  WalletModel+BIL.swift
//  wallet-ios-core
//
//  Created by 仇弘扬 on 2017/11/17.
//  Copyright © 2017年 BitBill. All rights reserved.
//

import Foundation
import UIKit
import CoreData
import SwiftyJSON
import CryptoSwift

extension WalletModel {
    func createWalletInServer(sucess: @escaping ([String: JSON]) -> Void, failure: @escaping (_ message: String, _ code: Int) -> Void) {
        guard let walletID = id else {
            failure("ID不能为空", -1)
            return
        }
        guard let extKey = mainExtPublicKey else {
            failure("extKey不能为空", -1)
            return
        }
        BILNetworkManager.request(request: .createWallet(walletID: walletID, extendedKey: extKey), sucess: sucess, failure: failure)
    }
}

extension WalletModel {
    func checkPassword(pwd: String) -> Bool {
        var toReturn = false
        
        let key = String(pwd.sha256().prefix(32))
        
        do {
            let aes = try AES(key: key, iv: String(key.reversed().prefix(16)))
            
            if let s = String(bytes: try aes.decrypt((encryptedSeed?.ck_mnemonicData().bytes)!), encoding: .utf8), seedHash == s.md5() {
                toReturn = true
            }
        } catch {
            print(error)
        }
        
        return toReturn
    }
}

extension WalletModel {
	static func fetch(by mnemonicHash: String?) -> WalletModel? {
		var wallet: WalletModel? = nil
		guard let hash = mnemonicHash else {
			return wallet
		}
		do {
			let context = (UIApplication.shared.delegate as! AppDelegate).persistentContainer.viewContext
			let request: NSFetchRequest<WalletModel> = WalletModel.fetchRequest()
			request.predicate = NSPredicate(format: "mnemonicHash=%@", hash)
			let results = try context.fetch(request)
			wallet = results.first
		} catch {
			return wallet
		}
		return wallet
	}
	
	func save() {
		do {
			let context = (UIApplication.shared.delegate as! AppDelegate).persistentContainer.viewContext
			try context.save()
		} catch {
			debugPrint(error)
		}
	}
}
