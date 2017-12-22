//
//  String+BILURL.swift
//  wallet-ios-core
//
//  Created by 仇弘扬 on 2017/12/1.
//  Copyright © 2017年 BitBill. All rights reserved.
//

import Foundation
import Alamofire

enum Router: URLRequestConvertible {
    func asURLRequest() throws -> URLRequest {
        let result: (path: String, parameters: [String: Any]?) = {
            switch self {
            case .root:
                return (.bil_base_url, nil)
            case .createWallet(let walletID, let extendedKey):
                return (.bil_wallet_create, ["walletId": walletID, "extendedKeys": extendedKey, "clientId": BILDeviceManager.shared.deviceID, "deviceToken": BILAppStartUpManager.shared.deviceToken ?? ""])
            case .importWallet(let walletID, let extendedKey):
                return (.bil_wallet_import, ["walletId": walletID, "extendedKeys": extendedKey, "clientId": BILDeviceManager.shared.deviceID, "deviceToken": BILAppStartUpManager.shared.deviceToken ?? ""])
            case .checkWalletID(let walletID):
                return (.bil_wallet_check_id, ["walletId": walletID])
            case .getWalletID(let extendedKeyHash):
                return (.bil_wallet_get_id, ["extendedKeysHash": extendedKeyHash])
            case .getTransactionHistory(let extendedKeyHash, let page, let size):
                return (.bil_wallet_transaction_history, ["extendedKeysHash": extendedKeyHash, "page": page, "size": size])
            case .getBalance(let extendedKeyHash):
                return (.bil_wallet_get_balance, ["extendedKeysHash": extendedKeyHash])
            case .getUTXO(let extendedKeyHash):
                return (.bil_wallet_get_UTXO, ["extendedKeysHash": extendedKeyHash])
            case .getTransactionBuildConfig(let extendedKeyHash):
                return (.bil_wallet_get_transaction_build_config, ["extendedKeysHash": extendedKeyHash])
            case .refreshAddress(let extendedKeyHash, let indexNum):
                return (.bil_wallet_refresh_address, ["extendedKeysHash": extendedKeyHash, "indexNo": indexNum])
            case .sendTransaction(let extendedKeyHash, let address, let inAddress, let amount, let txHash, let txHex, let remark):
                return (.bil_wallet_send_transaction, ["extendedKeysHash" : extendedKeyHash,
                                                       "inAddress": inAddress,
                                                       "outAddress" : address,
                                                       "outAmount" : amount,
                                                       "txHash" : txHash,
                                                       "hexTx" : txHex,
                                                       "remark": remark])
            case .getUnconfirmTransaction(let wallets):
                var pubKeys = [String]()
                for wallet in wallets {
                    guard let pubkey = wallet.mainExtPublicKey else {
                        continue
                    }
                    pubKeys.append(pubkey.md5())
                }
                return (.bil_wallet_get_unconfirm_transaction, ["extendedKeysHash": pubKeys.joined(separator: "|")])
            case .getContactLastAddress(let walletID):
                return (.bil_contact_get_last_address, ["walletId": walletID])
            case .getContacts:
                return (.bil_contact_get_all, ["walletKey": BILDeviceManager.shared.contactKey])
            case .searchWalletID(let walletID):
                return (.bil_contact_search_id, ["walletId": walletID])
            case .addContact(let walletID, let name, let remark, let address):
                return (.bil_contact_add, ["walletKey": BILDeviceManager.shared.contactKey, "contactName": name, "remark": remark, "walletId": walletID, "address": address])
            }
        }()
        
        let url = NSURL(string: .bil_base_url)
        var request = URLRequest(url: url!.appendingPathComponent(result.path)!)
        let encoding = JSONEncoding()
        
        debugPrint("load \(result.path) with \(result.parameters ?? [:])")
        
        request.httpMethod = HTTPMethod.post.rawValue
        request.setValue("iOS", forHTTPHeaderField: "platform")
        request.timeoutInterval = 5.0
        return try encoding.encode(request, with: result.parameters)
    }
    
    case root
    case createWallet(walletID: String, extendedKey: String)
    case importWallet(walletID: String, extendedKey: String)
    case checkWalletID(walletID: String)
    case getWalletID(extendedKeyHash: String)
    case getBalance(extendedKeyHash: String)
    case getUTXO(extendedKeyHash: String)
    case getTransactionHistory(extendedKeyHash: String, page: Int, size: Int)
    case getTransactionBuildConfig(extendedKeyHash: String)
    case refreshAddress(extendedKeyHash: String, index: Int64)
    case sendTransaction(extendedKeyHash: String, address: String, inAddress: String, amount: Int, txHash: String, txHex: String, remark: String)
    case getUnconfirmTransaction(wallets: [WalletModel])

    case getContactLastAddress(walletID: String)
    case getContacts
    case searchWalletID(walletID: String)
    case addContact(walletID: String, name: String, remark: String, address: String)
    
}

extension String {
    static var bil_base_url: String { get { return "http://192.168.1.10:8086/" } }
//    static var bil_base_url: String { get { return "http://walletservice.bitbill.com:8086/" } }
    static var bil_wallet_path: String { get { return "bitbill/bitcoin/wallet/" } }
    static var bil_wallet_create: String { get { return bil_wallet_path + "create" } }
    static var bil_wallet_import: String { get { return bil_wallet_path + "import" } }
    static var bil_wallet_check_id:  String { get { return bil_wallet_path + "checkWalletId" } }
    static var bil_wallet_get_id:  String { get { return bil_wallet_path + "getWalletId" } }
    static var bil_wallet_get_balance:  String { get { return bil_wallet_path + "getBalance" } }
    static var bil_wallet_get_UTXO:  String { get { return bil_wallet_path + "listUnspent" } }
    static var bil_wallet_get_transaction_build_config:  String { get { return bil_wallet_path + "getTxElement" } }
    static var bil_wallet_refresh_address: String { get { return bil_wallet_path + "refreshAddress" } }
    static var bil_wallet_send_transaction: String { get { return bil_wallet_path + "sendTransaction" } }
    static var bil_wallet_transaction_history: String { get { return bil_wallet_path + "getTxHistory" } }
    static var bil_wallet_get_unconfirm_transaction: String { get { return bil_wallet_path + "listUnconfirm" } }
    
    static var bil_contact_get_all: String { get { return bil_wallet_path + "getContacts" } }
    static var bil_contact_get_last_address: String { get { return bil_wallet_path + "getLastAddress" } }
    static var bil_contact_search_id: String { get { return bil_wallet_path + "searchWalletId" } }
    static var bil_contact_add: String { get { return bil_wallet_path + "addContacts" } }
    
    
}

extension String {
//    static var bil_socket_base_url: String { get { return "http://walletservice.bitbill.com:8088/" } }
    static var bil_socket_base_url: String { get { return "http://192.168.1.10:8088/" } }
}

extension String {
    static var bil_socket_event_register: String { get { return "register" } }
    static var bil_socket_event_unconfirm: String { get { return "unconfirm" } }
    static var bil_socket_event_confirm: String { get { return "confirm" } }
}
