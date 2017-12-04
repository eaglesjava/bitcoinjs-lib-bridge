//
//  BILWalletManager.swift
//  wallet-ios-core
//
//  Created by 仇弘扬 on 2017/11/24.
//  Copyright © 2017年 BitBill. All rights reserved.
//

import UIKit
import CoreData

class BILWalletManager: NSObject {
	static let shared = {
		return BILWalletManager()
	}()
	
	weak var appDelegate: AppDelegate?
    
    var coreDataContext = {
        (UIApplication.shared.delegate as! AppDelegate).persistentContainer.viewContext
    }()
    
	
	func newWallet() -> WalletModel {
		let context = coreDataContext
		let wallet = NSEntityDescription.insertNewObject(forEntityName: "WalletModel", into: context) as! WalletModel
		return wallet
	}
    
    func remove(wallet: WalletModel) {
        let context = coreDataContext
        context.delete(wallet)
    }
	
	func saveWallets() throws {
		let context = coreDataContext
		guard context.hasChanges else {
			return
		}
		NotificationCenter.default.post(name: .walletDidChanged, object: nil)
		try context.save()
	}
	
	var wallets: [WalletModel] {
		get {
			var results = [WalletModel]()
			guard let delegate = appDelegate else { return results }
			do {
				let context = delegate.persistentContainer.viewContext
				let request: NSFetchRequest<WalletModel> = WalletModel.fetchRequest()
				results.append(contentsOf: try context.fetch(request))
			} catch {
				
			}
			return results
		}
	}
	
}
