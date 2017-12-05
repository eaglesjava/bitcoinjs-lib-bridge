//
//  WalletModel+Network.swift
//  wallet-ios-core
//
//  Created by 仇弘扬 on 2017/12/5.
//  Copyright © 2017年 BitBill. All rights reserved.
//

import Foundation
import SwiftyJSON

extension WalletModel {
    func createWalletToServer(success: @escaping ([String: JSON]) -> Void, failure: @escaping (_ message: String, _ code: Int) -> Void) {
        guard let walletID = id else {
            failure("ID不能为空", -1)
            return
        }
        guard let extKey = mainExtPublicKey else {
            failure("extKey不能为空", -1)
            return
        }
        BILNetworkManager.request(request: .createWallet(walletID: walletID, extendedKey: extKey), success: success, failure: failure)
    }
    
    func importWalletToServer(success: @escaping ([String: JSON]) -> Void, failure: @escaping (_ message: String, _ code: Int) -> Void) {
        guard let walletID = id else {
            failure("ID不能为空", -1)
            return
        }
        guard let extKey = mainExtPublicKey else {
            failure("extKey不能为空", -1)
            return
        }
        BILNetworkManager.request(request: .importWallet(walletID: walletID, extendedKey: extKey), success: success, failure: failure)
    }
    
    
    /// 从服务器获取余额
    ///
    /// - Parameters:
    ///   - success: 可能会有两次调用，第一次为当前余额，第二次为服务器返回
    ///   - failure: 失败
    func getBalanceFromServer(success: @escaping (_ wallet: WalletModel) -> Void, failure: @escaping (_ message: String, _ code: Int) -> Void) {
        guard let extKey = mainExtPublicKey else {
            failure("extKey不能为空", -1)
            return
        }
        success(self)
        WalletModel.getBalanceFromServer(mainExtPublicKey: extKey, success: { (satoshBalance, unconfirmBalance) in
            self.btcBalance = Int64(satoshBalance)
            self.btcUnconfirmBalance = Int64(unconfirmBalance)
            do {
                try BILWalletManager.shared.saveWallets()
            } catch {
                debugPrint(error)
            }
            success(self)
        }, failure: failure)
    }
    
    static func getBalanceFromServer(mainExtPublicKey: String, success: @escaping (_ satoshBalance: Int, _ unconfirmBalance: Int) -> Void, failure: @escaping (_ message: String, _ code: Int) -> Void) {
        BILNetworkManager.request(request: .getWalletID(extendedKey: mainExtPublicKey.md5()), success: { (result) in
            debugPrint(result)
            let json = JSON(result)
            guard let balance = json["balance"].int, let unconfirm = json["balance"].int else {
                failure("获取到错误的数据", -1)
                return
            }
            success(balance, unconfirm)
        }, failure: failure)
    }
    
    static func getWalletIDFromSever(mainExtPublicKey: String, success: @escaping (_ id: String) -> Void, failure: @escaping (_ message: String, _ code: Int) -> Void) {
        BILNetworkManager.request(request: .getWalletID(extendedKey: mainExtPublicKey.md5()), success: { (result) in
            debugPrint(result)
            let json = JSON(result)
            guard let id = json["walletId"].string else {
                failure("获取到错误的数据", -1)
                return
            }
            success(id)
        }, failure: failure)
    }
}
