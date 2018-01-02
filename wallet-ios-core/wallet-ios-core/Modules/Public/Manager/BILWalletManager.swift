//
//  BILWalletManager.swift
//  wallet-ios-core
//
//  Created by 仇弘扬 on 2017/11/24.
//  Copyright © 2017年 BitBill. All rights reserved.
//

import UIKit
import CoreData
import SwiftyJSON

class BILWalletManager {
	static let shared = {
		return BILWalletManager()
	}()
    
    weak var appDelegate: AppDelegate?
    
    var cnyExchangeRate: Double = 0
    var usdExchangeRate: Double = 0
    var loadExchangeRateTimer = Timer()
    
    var wallets: [WalletModel] {
        get {
            var results = [WalletModel]()
            do {
                let context = coreDataContext
                let request: NSFetchRequest<WalletModel> = WalletModel.fetchRequest()
                let date = NSSortDescriptor(key: "createDate", ascending: false)
                request.sortDescriptors = [date]
                results.append(contentsOf: try context.fetch(request))
            } catch {
                debugPrint("查询钱包失败")
                UIApplication.shared.keyWindow?.makeToast("查询钱包失败")
            }
            return results
        }
    }
    
    var coreDataContext = {
        (UIApplication.shared.delegate as! AppDelegate).persistentContainer.viewContext
    }()
    
    init() {
        loadExchangeRateTimer = Timer(timeInterval: 30, target: self, selector: #selector(loadExchangeRate), userInfo: nil, repeats: true)
        applicationDidBecomeActive()
        
        NotificationCenter.default.addObserver(self, selector: #selector(applicationDidBecomeActive), name: .UIApplicationDidBecomeActive, object: nil)
        NotificationCenter.default.addObserver(self, selector: #selector(applicationWillResignActive), name: .UIApplicationWillResignActive, object: nil)
    }
    
    deinit {
        NotificationCenter.default.removeObserver(self, name: .UIApplicationDidBecomeActive, object: nil)
        NotificationCenter.default.removeObserver(self, name: .UIApplicationWillResignActive, object: nil)
    }
    
    @objc
    func applicationDidBecomeActive() {
        RunLoop.current.add(loadExchangeRateTimer, forMode: .defaultRunLoopMode)
        loadExchangeRateTimer.fire()
    }
    
    @objc
    func applicationWillResignActive() {
        loadExchangeRateTimer.invalidate()
    }
    
    @objc
    func loadExchangeRate() {
        BILNetworkManager.request(request: .getExchangeRate, success: { (result) in
            let json = JSON(result)
            self.cnyExchangeRate = json["cnyrate"].doubleValue
            self.usdExchangeRate = json["usdrate"].doubleValue
            NotificationCenter.default.post(name: .exchangeRateDidChanged, object: nil)
        }) { (msg, code) in
            debugPrint(msg)
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
	
}
