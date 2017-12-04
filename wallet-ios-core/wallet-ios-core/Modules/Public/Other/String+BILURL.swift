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
                return (.bil_wallet_create, ["walletID": walletID, "extendedKeys": extendedKey, "clientId": BILDeviceManager.shared.deviceID, "deviceToken": BILAppStartUpManager.shared.deviceToken ?? ""])
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
}
extension String {
    static var bil_base_url: String { get { return "http://192.168.1.10:8086/" } }
    static var bil_wallet_create: String { get { return "bitbill/bitcoin/wallet/create" } }
}
