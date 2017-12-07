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
import CryptoSwift

extension WalletModel {
    static func generateAES(pwd: String) -> AES? {
        do {
            let key = String(pwd.sha256().prefix(32))
            let aes = try AES(key: key, iv: String(key.reversed().prefix(16)))
            return aes
        } catch {
            print(error)
            return nil
        }
    }
    
    func decryptMnemonic(pwd: String) -> String? {
        do {
            guard let aes = WalletModel.generateAES(pwd: pwd) else { return nil }
            
            if let mnemonic = String(bytes: try aes.decrypt((encryptedMnemonic?.ck_mnemonicData().bytes)!), encoding: .utf8), mnemonic.md5() == self.mnemonicHash {
                return mnemonic
            }
            else
            {
                return nil
            }
        } catch {
            debugPrint(error)
            return nil
        }
    }
    
    func checkPassword(pwd: String) -> Bool {
        var toReturn = false
        
        do {
            guard let aes = WalletModel.generateAES(pwd: pwd) else { return false }
            
            if let s = String(bytes: try aes.decrypt((encryptedSeed?.ck_mnemonicData().bytes)!), encoding: .utf8), seedHash == s.md5() {
                toReturn = true
            }
        } catch {
            print(error)
        }
        
        return toReturn
    }
    
    static func checkMnemonicIsExists (m: String) -> Bool {
        var toReturn = false
        
        toReturn = fetch(mnemonicHash: m.md5()) != nil
        
        return toReturn
    }
    
    static func checkIDIsExists (id: String) -> Bool {
        var toReturn = false
        
        toReturn = fetch(id: id) != nil
        
        return toReturn
    }
}

extension WalletModel {
    func resetProperties(m: String, pwd: String, success: @escaping (_ wallet: WalletModel) -> Void, failure: @escaping (_ message: String) -> Void) {
        func cleanUp(wallet: WalletModel?, error: String) {
            debugPrint(error)
            if let w = wallet {
                BILWalletManager.shared.remove(wallet: w)
            }
            failure(error)
        }
        BitcoinJSBridge.shared.mnemonicToSeedHex(mnemonic: m, password: "", success: { (seedHex) in
            let s = seedHex as! String
            let wallet = self
            wallet.createDate = Date()
            do {
                guard let aes = WalletModel.generateAES(pwd: pwd) else { return }
                wallet.encryptedMnemonic = try aes.encrypt(Array(m.bytes)).toHexString()
                wallet.encryptedSeed = try aes.encrypt(Array(s.bytes)).toHexString()
                wallet.seedHash = s.md5()
                wallet.mnemonicHash = m.md5()
                
                BitcoinJSBridge.shared.getMasterXPublicKey(seed: s, success: { (pubKey) in
                    let extPubKey = pubKey as! String
                    wallet.mainExtPublicKey = extPubKey
                    if wallet.checkPassword(pwd: pwd) {
                        wallet.createWalletToServer(success: { (result) in
                            do {
                                try BILWalletManager.shared.saveWallets()
                                success(self)
                            } catch {
                                cleanUp(wallet: wallet, error: error.localizedDescription)
                            }
                        }, failure: { (msg, code) in
                            cleanUp(wallet: wallet, error: msg)
                        })
                    }
                }, failure: { (error) in
                    cleanUp(wallet: wallet, error: error.localizedDescription)
                })
            } catch {
                cleanUp(wallet: wallet, error: error.localizedDescription)
            }
        }, failure: { (error) in
            cleanUp(wallet: nil, error: error.localizedDescription)
        })
    }
	static func fetch(mnemonicHash: String?) -> WalletModel? {
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
    
    static func fetch(id: String?) -> WalletModel? {
        var wallet: WalletModel? = nil
        guard let idStr = id else {
            return wallet
        }
        do {
            let context = (UIApplication.shared.delegate as! AppDelegate).persistentContainer.viewContext
            let request: NSFetchRequest<WalletModel> = WalletModel.fetchRequest()
            request.predicate = NSPredicate(format: "id=%@", idStr)
            let results = try context.fetch(request)
            wallet = results.first
        } catch {
            return wallet
        }
        return wallet
    }
	
	func save() {
		do {
			try BILWalletManager.shared.saveWallets()
		} catch {
			debugPrint(error)
		}
	}
}
