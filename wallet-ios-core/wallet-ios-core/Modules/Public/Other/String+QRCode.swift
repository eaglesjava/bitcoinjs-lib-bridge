//
//  String+QRCode.swift
//  wallet-ios-core
//
//  Created by 仇弘扬 on 2017/12/22.
//  Copyright © 2017年 BitBill. All rights reserved.
//

import Foundation
import UIKit

extension String {
	func qrCodeImage(targetSize: CGSize) -> UIImage? {
        guard let filter = CIFilter(name: "CIQRCodeGenerator") else {
            return nil
        }
        
        let data = self.data(using: .utf8)
        
        filter.setValue(data, forKey: "inputMessage")
        
        guard let ciImage = filter.outputImage else {
            return nil
        }
        let scale = targetSize.width / ciImage.extent.size.width
        let magnifiedImage = ciImage.transformed(by: CGAffineTransform(scaleX: scale, y: scale))
        
        return UIImage(ciImage: magnifiedImage)
	}
}
