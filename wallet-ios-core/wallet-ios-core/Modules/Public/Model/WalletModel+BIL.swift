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
	var id_qrString: String {
		get {
			return "bitbill://www.bitbill.com/contact?id=\(id ?? "")"
		}
	}
}

extension WalletModel {
    func generateAddresses(from: Int64, to: Int64, success: @escaping ([BTCAddressModel]) -> Void, failure: @escaping (_ message: String, _ code: Int) -> Void) {
        guard from < to else { return }
        BitcoinJSBridge.shared.getAddresses(xpub: mainExtPublicKey!, fromIndex: from, toIndex: from, success: { (result) in
            debugPrint(result)
            guard let array = result as? [String] else {
                failure("生成失败", -1)
                return
            }
            for address in array {
                let tx = bil_btc_addressManager.newModelIfNeeded(key: "address", value: address)
                tx.address = address
                tx.satoshi = 0
                self.addToAddresses(tx)
            }
            self.lastAddressIndex = to
            do {
                try BILWalletManager.shared.saveWallets()
            } catch {
                failure(error.localizedDescription, -2)
            }
        }) { (error) in
            debugPrint(error)
            failure(error.localizedDescription, -2)
        }
    }
}

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
    
    func decryptSeed(pwd: String) -> String? {
        do {
            guard let aes = WalletModel.generateAES(pwd: pwd) else { return nil }
            
            if let seed = String(bytes: try aes.decrypt((encryptedSeed?.ck_mnemonicData().bytes)!), encoding: .utf8), seed.md5() == self.seedHash {
                return seed
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
        return fetch(key: "mnemonicHash", value: m.md5()) > 0
    }
    
    static func checkIDIsExists (id: String) -> Bool {
        return fetch(key: "id", value: id) > 0
    }
}

extension WalletModel {
    func resetProperties(m: String, pwd: String, needSave: Bool = true, success: @escaping (_ wallet: WalletModel) -> Void, failure: @escaping (_ message: String) -> Void) {
        func cleanUp(wallet: WalletModel?, error: String) {
            debugPrint(error)
            if let w = wallet {
                do {
                    try BILWalletManager.shared.remove(wallet: w)
                } catch {
                    failure(error.localizedDescription)
                }
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
                        do {
                            if needSave {
                                try BILWalletManager.shared.saveWallets()
                            }
                            success(self)
                        } catch {
                            cleanUp(wallet: wallet, error: error.localizedDescription)
                        }
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
		return fetch(keyPath: "mnemonicHash", id: mnemonicHash)
	}
    
    static func fetch(id: String?) -> WalletModel? {
        return fetch(keyPath: "id", id: id)
    }
    
    static func fetch(keyPath: String, id: String?) -> WalletModel? {
        var wallet: WalletModel? = nil
        guard let idStr = id else {
            return wallet
        }
        do {
            let context = (UIApplication.shared.delegate as! AppDelegate).persistentContainer.viewContext
            let request: NSFetchRequest<WalletModel> = WalletModel.fetchRequest()
            request.predicate = NSPredicate(format: "\(keyPath)=%@", idStr)
            let results = try context.fetch(request)
            wallet = results.first
        } catch {
            return wallet
        }
        return wallet
    }
    
    static func fetch(key: String, value: String) -> Int {
        var count = 0
        do {
            let context = (UIApplication.shared.delegate as! AppDelegate).persistentContainer.viewContext
            let request: NSFetchRequest = WalletModel.fetchRequest()
            request.resultType = .countResultType
            request.predicate = NSPredicate(format: "\(key)=%@", value)
            count = try context.count(for: request)
            return count
        } catch {
            return count
        }
    }
	
	func save() {
		do {
			try BILWalletManager.shared.saveWallets()
		} catch {
			debugPrint(error)
		}
	}
}
