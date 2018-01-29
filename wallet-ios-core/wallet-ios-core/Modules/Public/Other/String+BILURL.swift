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
            case .createWallet(let wallet):
                return (.bil_wallet_create, ["walletId": wallet.id!, "extendedKeys": wallet.mainExtPublicKey!, "extendedChangeKeys": wallet.changeExtPublicKey!, "clientId": BILDeviceManager.shared.deviceID, "deviceToken": BILAppStartUpManager.shared.deviceToken ?? ""])
            case .importWallet(let wallet):
                return (.bil_wallet_import, ["walletId": wallet.id!, "extendedKeys": wallet.mainExtPublicKey!, "extendedChangeKeys": wallet.changeExtPublicKey!, "clientId": BILDeviceManager.shared.deviceID, "deviceToken": BILAppStartUpManager.shared.deviceToken ?? ""])
            case .deleteWallet(let extendedKeyHash):
                return (.bil_wallet_delete, ["extendedKeysHash": extendedKeyHash, "clientId": BILDeviceManager.shared.deviceID])
            case .checkWalletID(let walletID):
                return (.bil_wallet_check_id, ["walletId": walletID])
            case .getWalletID(let extendedKeyHash):
                return (.bil_wallet_get_id, ["extendedKeysHash": extendedKeyHash])
            case .getTransactionHistory(let extendedKeyHash, let id,let page, let size):
                return (.bil_wallet_transaction_history, ["extendedKeysHash": extendedKeyHash, "id": id, "page": page, "size": size])
            case .getBalance(let extendedKeyHash):
                return (.bil_wallet_get_balance, ["extendedKeysHash": extendedKeyHash])
            case .getUTXO(let extendedKeyHash):
                return (.bil_wallet_get_UTXO, ["extendedKeysHash": extendedKeyHash])
            case .getTransactionBuildConfig(let extendedKeyHash):
                return (.bil_wallet_get_transaction_build_config, ["extendedKeysHash": extendedKeyHash])
            case .refreshAddress(let extendedKeyHash, let indexNum, let changIndex):
                return (.bil_wallet_refresh_address, ["extendedKeysHash": extendedKeyHash, "indexNo": indexNum, "changeIndexNo": changIndex])
            case .sendTransaction(let extendedKeyHash, let address, let inAddress, let amount, let txHash, let txHex, let remark):
                return (.bil_wallet_send_transaction, ["extendedKeysHash" : extendedKeyHash,
                                                       "inAddress": inAddress,
                                                       "outAddress" : address,
                                                       "outAmount" : amount,
                                                       "txHash" : txHash,
                                                       "hexTx" : txHex,
                                                       "remark": remark])
            case .getUnconfirmTransaction(let wallets):
                let hashes = WalletModel.getKeyHash(wallets: wallets)
                return (.bil_wallet_get_unconfirm_transaction, ["extendedKeysHash": hashes])
            case .getContactLastAddress(let walletID):
                return (.bil_contact_get_last_address, ["walletId": walletID])
            case .getContacts:
                return (.bil_contact_get_all, ["walletKey": BILDeviceManager.shared.contactKey])
            case .searchWalletID(let walletID):
                return (.bil_contact_search_id, ["walletId": walletID])
            case .addContact(let walletID, let name, let remark, let address, let coinType):
                return (.bil_contact_add, ["walletKey": BILDeviceManager.shared.contactKey, "contactName": name, "remark": remark, "walletId": walletID, "address": address, "coinType": coinType.name])
            case .updateContact(let walletID, let name, let remark, let address):
                return (.bil_contact_update, ["walletKey": BILDeviceManager.shared.contactKey, "contactName": name, "remark": remark, "walletId": walletID, "address": address])
            case .deleteContact(let walletID, let address):
                return (.bil_contact_delete, ["walletKey": BILDeviceManager.shared.contactKey, "walletId": walletID, "address": address])
            case .recoverContacts(let recoverKey):
                return (.bil_contact_recover, ["walletKey": BILDeviceManager.shared.contactKey, "recoverKey": recoverKey])
            case .getExchangeRate:
                return (.bil_get_exchange_Rate, [:])
            case .getBlockHeightAndWalletVersion(let hashes):
                return (.bil_get_blockHeight_WalletVersion, ["extendedKeysHash": hashes])
            case .feedback(let content, let contact):
                return (.bil_feedback, ["context": content, "contact": contact])
            }
        }()
        
        let url = NSURL(string: .bil_base_url)
        var request = URLRequest(url: url!.appendingPathComponent(result.path)!)
        let encoding = JSONEncoding()
        
        debugPrint("load \(result.path) with \(result.parameters ?? [:])")
        
        request.httpMethod = HTTPMethod.post.rawValue
        
        request.setValue("iOS", forHTTPHeaderField: "platform")
        request.timeoutInterval = 30.0
        return try encoding.encode(request, with: result.parameters)
    }
    
    case root
    case createWallet(wallet: WalletModel)
    case importWallet(wallet: WalletModel)
    case deleteWallet(extendedKeyHash: String)
    case checkWalletID(walletID: String)
    case getWalletID(extendedKeyHash: String)
    case getBalance(extendedKeyHash: String)
    case getUTXO(extendedKeyHash: String)
    case getTransactionHistory(extendedKeyHash: String, id: String, page: Int, size: Int)
    case getTransactionBuildConfig(extendedKeyHash: String)
    case refreshAddress(extendedKeyHash: String, index: Int64, changeIndex: Int64)
    case sendTransaction(extendedKeyHash: String, address: String, inAddress: String, amount: Int64, txHash: String, txHex: String, remark: String)
    case getUnconfirmTransaction(wallets: [WalletModel])

    case getContactLastAddress(walletID: String)
    case getContacts
    case searchWalletID(walletID: String)
    case addContact(walletID: String, name: String, remark: String, address: String, coinType: CoinType)
    case updateContact(walletID: String, name: String, remark: String, address: String)
    case deleteContact(walletID: String, address: String)
    case recoverContacts(recoverKey: String)
    
    case getExchangeRate
    case getBlockHeightAndWalletVersion(hashes: String)
    
    case feedback(content: String, contact: String)
    
}

extension String {
//    static let bil_base_url = "http://192.168.1.31:8086/"
    static let bil_base_host = "walletservice.bitbill.com"
    static let bil_base_url = "https://" + bil_base_host + "/"
    static let bil_path = "bitbill/bitcoin/"
    static let bil_wallet_path = bil_path + "wallet/"
    static let bil_wallet_create = bil_wallet_path + "create"
    static let bil_wallet_delete = bil_wallet_path + "deleteWallet"
    static let bil_wallet_import = bil_wallet_path + "import"
    static let bil_wallet_check_id = bil_wallet_path + "checkWalletId"
    static let bil_wallet_get_id = bil_wallet_path + "getWalletId"
    static let bil_wallet_get_balance = bil_wallet_path + "getBalance"
    static let bil_wallet_get_UTXO = bil_wallet_path + "listUnspent"
    static let bil_wallet_get_transaction_build_config = bil_wallet_path + "getTxElement"
    static let bil_wallet_refresh_address = bil_wallet_path + "refreshAddress"
    static let bil_wallet_send_transaction = bil_wallet_path + "sendTransaction"
    static let bil_wallet_transaction_history = bil_wallet_path + "getTxList"
    static let bil_wallet_get_unconfirm_transaction = bil_wallet_path + "listUnconfirmTx"
    
    static let bil_contact_get_all = bil_wallet_path + "getContacts"
    static let bil_contact_get_last_address = bil_wallet_path + "getLastAddress"
    static let bil_contact_search_id = bil_wallet_path + "searchWalletId"
    static let bil_contact_add = bil_wallet_path + "addContacts"
    static let bil_contact_update = bil_wallet_path + "updateContacts"
    static let bil_contact_delete = bil_wallet_path + "deleteContacts"
    static let bil_contact_recover = bil_wallet_path + "recoverContacts"
    
    static let bil_get_exchange_Rate = bil_path + "get_exchange_rate"
    static let bil_get_blockHeight_WalletVersion = bil_wallet_path + "getCacheVersion"
    
    static let bil_feedback = bil_path + "feed_back"
}

extension String {
//    static let bil_socket_base_url = "http://192.168.1.15:8088/"
    static let bil_socket_base_url = "http://walletsocket.bitbill.com:8088/"
}

extension String {
    static let bil_socket_event_register = "register"
    static let bil_socket_event_unconfirm = "unconfirm"
    static let bil_socket_event_confirm = "confirm"
}

extension String {
    static let bil_extenal_blockchain_transaction = "https://blockchain.info/tx/"
}
