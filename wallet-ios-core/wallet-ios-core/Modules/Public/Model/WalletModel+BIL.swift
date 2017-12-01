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

extension WalletModel {
    func createWalletInServer(sucess: @escaping ([String: Any]?) -> Void, failure: @escaping (_ message: String, _ code: Int) -> Void) {
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
			print(error)
		}
	}
}
