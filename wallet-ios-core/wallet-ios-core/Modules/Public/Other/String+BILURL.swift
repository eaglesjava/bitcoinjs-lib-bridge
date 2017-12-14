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
            case .getUTXO(let extendedKeyHash):
                return (.bil_wallet_get_UTXO, ["extendedKeysHash": extendedKeyHash])
            }
        }()
        
        let url = NSURL(string: .bil_base_url)
        var request = URLRequest(url: url!.appendingPathComponent(result.path)!)
        let encoding = JSONEncoding()
        
        request.httpMethod = HTTPMethod.post.rawValue
        return try encoding.encode(request, with: result.parameters)
    }
    
    case root
    case createWallet(walletID: String, extendedKey: String)
    case importWallet(walletID: String, extendedKey: String)
    case checkWalletID(walletID: String)
    case getWalletID(extendedKey: String)
    case getUTXO(extendedKey: String)
    
}

extension String {
    static var bil_base_url: String { get { return "http://192.168.1.10:8086/" } }
//    static var bil_base_url: String { get { return "http://walletservice.bitbill.com:8086/" } }
    static var bil_wallet_path: String { get { return "bitbill/bitcoin/wallet/" } }
    static var bil_wallet_create: String { get { return bil_wallet_path + "create" } }
    static var bil_wallet_import: String { get { return bil_wallet_path + "import" } }
    static var bil_wallet_check_id:  String { get { return bil_wallet_path + "checkWalletId" } }
    static var bil_wallet_get_id:  String { get { return bil_wallet_path + "getWalletId" } }
    static var bil_wallet_get_UTXO:  String { get { return bil_wallet_path + "listUnspent" } }
}
