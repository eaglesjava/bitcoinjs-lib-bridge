//
//  UIImage+QRCode.swift
//  wallet-ios-core
//
//  Created by 仇弘扬 on 2018/2/1.
//  Copyright © 2018年 BitBill. All rights reserved.
//

import Foundation
import UIKit

extension UIImage {
    func toQRCodeString() -> String? {
        guard let ciimage = CIImage(image: self) else {
            return nil
        }
        let context = CIContext(options: [kCIContextUseSoftwareRenderer: true])
        guard let detector = CIDetector(ofType: CIDetectorTypeQRCode, context: context, options: [CIDetectorAccuracy: CIDetectorAccuracyHigh]) else {
            return nil
        }
        let results = detector.features(in: ciimage)
        guard let r = results.first as? CIQRCodeFeature else {
            return nil
        }
        
        let msg = r.messageString
        return msg
    }
}
