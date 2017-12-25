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
        
        return msg.qrCodeImage(targetSize: targetSize)
    }
}
