//
//  BILQRCodeHelper.swift
//  wallet-ios-core
//
//  Created by 仇弘扬 on 2017/12/7.
//  Copyright © 2017年 BitBill. All rights reserved.
//

import UIKit

class BILQRCodeHelper: NSObject {
    static func generateQRCode(msg: String, targetSize: CGSize = CGSize(width: 270, height: 270)) -> UIImage? {
        
        guard let filter = CIFilter(name: "CIQRCodeGenerator") else {
            return nil
        }
        
        let data = msg.data(using: .utf8)
        
        filter.setValue(data, forKey: "inputMessage")
        
        guard let ciImage = filter.outputImage else {
            return nil
        }
        let scale = targetSize.width / ciImage.extent.size.width
        let magnifiedImage = ciImage.transformed(by: CGAffineTransform(scaleX: scale, y: scale))
        
        return UIImage(ciImage: magnifiedImage)
    }
}
