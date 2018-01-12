//
//  BILNetworkManager.swift
//  wallet-ios-core
//
//  Created by 仇弘扬 on 2017/11/30.
//  Copyright © 2017年 BitBill. All rights reserved.
//

import UIKit
import Alamofire
import SwiftyJSON

class BILNetworkManager {
    
    var sessionManager: SessionManager!
    
    static let manager = {
        return BILNetworkManager()
    }()
    
    init() {
        let serverTrustPolicy = ServerTrustPolicy.pinCertificates(
            certificates: ServerTrustPolicy.certificates(),
            validateCertificateChain: true,
            validateHost: true
        )
        
        let serverTrustPolicies: [String: ServerTrustPolicy] = [
            "*.bitbill.com": serverTrustPolicy
        ]
        sessionManager = SessionManager(
            serverTrustPolicyManager: ServerTrustPolicyManager(policies: serverTrustPolicies)
        )
        sessionManager.delegate.taskDidReceiveChallenge = { (session, task, challenge) in
            return (.cancelAuthenticationChallenge, nil)
        }
        sessionManager.delegate.sessionDidReceiveChallenge = { (session, challenge) in
            var disposition: URLSession.AuthChallengeDisposition = .performDefaultHandling
            var credential: URLCredential?
            switch challenge.protectionSpace.authenticationMethod {
            case NSURLAuthenticationMethodServerTrust:
                //认证服务器证书
                let host = challenge.protectionSpace.host
                if
                    let serverTrustPolicy = serverTrustPolicies[host],
                    let serverTrust = challenge.protectionSpace.serverTrust
                {
                    //session对对应的host有相应的Policy alamofire默认下session.serverTrustPolicyManager就为nil
                    if serverTrustPolicy.evaluate(serverTrust, forHost: host) {
                        //认证
                        disposition = .useCredential
                        credential = URLCredential(trust: serverTrust)
                    } else {
                        //取消
                        disposition = .cancelAuthenticationChallenge
                    }
                }
                return (disposition, credential)
            default:
                return (.cancelAuthenticationChallenge, nil)
            }
        }
    }
    
    static func request(request: Router, success: @escaping ([String: JSON]) -> Void, failure: @escaping (_ message: String, _ code: Int) -> Void) {
        
        BILNetworkManager.manager.sessionManager.request(request).responseJSON { (response) in
            debugPrint(request)
            debugPrint(response)
            if let json = response.result.value as? [String : Any] {
                debugPrint("JSON: \(json)") // serialized json response
                let j = JSON(json)
                let status = j["status"].intValue
                if status < 0 {
                    failure(j["message"].stringValue, j["status"].intValue)
                }
                else
                {
                    success(j["data"].dictionaryValue)
                }
            }
            else
            {
                guard let error = response.error else {
                    failure(.publicNetworkConnectServerFaild, -1)
                    return
                }
                failure("\(error.localizedDescription)", -1)
            }
        }
    }
}
