//
//  BILAuthenticator.swift
//  wallet-ios-core
//
//  Created by 仇弘扬 on 2017/12/26.
//  Copyright © 2017年 BitBill. All rights reserved.
//

import UIKit
import LocalAuthentication

/// ****************  Face ID  ****************** ///
let kFaceIdAuthenticationReason = NSLocalizedString("kFaceIdAuthenticationReason", tableName: "BILAuthenticator", comment: "")
let kFaceIdPasscodeAuthenticationReason = NSLocalizedString("kFaceIdPasscodeAuthenticationReason", tableName: "BILAuthenticator", comment: "")

/// ****************  Touch ID  ****************** ///
let kTouchIdAuthenticationReason = NSLocalizedString("kTouchIdAuthenticationReason", tableName: "BILAuthenticator", comment: "")
let kTouchIdPasscodeAuthenticationReason = NSLocalizedString("kTouchIdPasscodeAuthenticationReason", tableName: "BILAuthenticator", comment: "")

/// ****************  Passcode  ****************** ///
let kPasscodeAuthenticationReason = NSLocalizedString("kPasscodeAuthenticationReason", tableName: "BILAuthenticator", comment: "")


/// success block
public typealias AuthenticationSuccess = (() -> ())

/// failure block
public typealias AuthenticationFailure = (() -> ())

class BILAuthenticator {
    public static let shared = BILAuthenticator()
}

extension BILAuthenticator {
    
    /// Check for biometric authentication
    func autherizeWithTouchIdOrFaceId(reason: String, success successBlock:@escaping AuthenticationSuccess, failure failureBlock:@escaping AuthenticationFailure) {
        
        let reasonString = reason.isEmpty ? BILAuthenticator.shared.defaultBiometricAuthenticationReason() : reason
        
        let context = LAContext()
        
        // authenticate
        BILAuthenticator.shared.evaluate(policy: LAPolicy.deviceOwnerAuthentication, with: context, reason: reasonString, success: successBlock, failure: failureBlock)
    }
    
    
    /// checks if face id is avaiable on device
    public func faceIDAvailable() -> Bool {
        if #available(iOS 11.0, *) {
            return (LAContext().biometryType == .faceID)
        }
        return false
    }
    public func touchIDAvailable() -> Bool {
        if #available(iOS 10.0, *) {
            return (LAContext().biometryType == .touchID)
        }
        return false
    }
}

// MARK:- Private
extension BILAuthenticator {
    
    /// get authentication reason to show while authentication
    func defaultBiometricAuthenticationReason() -> String {
        return faceIDAvailable() ? kFaceIdAuthenticationReason : (touchIDAvailable() ? kTouchIdAuthenticationReason : kPasscodeAuthenticationReason)
    }
    
    /// get passcode authentication reason to show while entering device passcode after multiple failed attempts.
    func defaultPasscodeAuthenticationReason() -> String {
        return faceIDAvailable() ? kFaceIdPasscodeAuthenticationReason : kTouchIdPasscodeAuthenticationReason
    }
    
    /// evaluate policy
    func evaluate(policy: LAPolicy, with context: LAContext, reason: String, success successBlock: @escaping AuthenticationSuccess, failure failureBlock: @escaping AuthenticationFailure) {
        
        context.evaluatePolicy(policy, localizedReason: reason) { (success, err) in
            DispatchQueue.main.async {
                if success {
                    successBlock()
                } else {
                    failureBlock()
                }
            }
        }
    }
}
