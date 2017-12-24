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
    
    override init() {
        super.init()
        loadWalletBalancesFromServer()
    }
	
	weak var appDelegate: AppDelegate?
    
    var coreDataContext = {
        (UIApplication.shared.delegate as! AppDelegate).persistentContainer.viewContext
    }()
    
    var wallets: [WalletModel] {
        get {
            var results = [WalletModel]()
            guard let delegate = appDelegate else { return results }
            do {
                let context = delegate.persistentContainer.viewContext
                let request: NSFetchRequest<WalletModel> = WalletModel.fetchRequest()
                results.append(contentsOf: try context.fetch(request))
            } catch {
                debugPrint("查询钱包失败")
                UIApplication.shared.keyWindow?.makeToast("查询钱包失败")
            }
            return results
        }
    }
    
	func newWallet() -> WalletModel {
		let context = coreDataContext
		let wallet = NSEntityDescription.insertNewObject(forEntityName: "WalletModel", into: context) as! WalletModel
		return wallet
	}
    
    func remove(wallet: WalletModel) throws {
        let context = coreDataContext
        context.delete(wallet)
		do {
			try context.save()
			NotificationCenter.default.post(name: .walletDidChanged, object: nil)
		} catch {
			throw error
		}
    }
	
	func saveWallets() throws {
		let context = coreDataContext
		guard context.hasChanges else {
			return
		}
		NotificationCenter.default.post(name: .walletDidChanged, object: nil)
		try context.save()
	}
	
    func loadWalletBalancesFromServer() {
        for wallet in wallets {
            wallet.getBalanceFromServer(success: { (w) in
                
            }, failure: { (msg, code) in
                
            })
        }
    }
	
}
