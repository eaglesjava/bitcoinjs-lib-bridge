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
		return BILQRCodeHelper.generateQRCode(msg: self, targetSize: targetSize)
	}
}
