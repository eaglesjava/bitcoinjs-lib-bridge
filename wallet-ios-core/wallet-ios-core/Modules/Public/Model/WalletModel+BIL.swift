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
