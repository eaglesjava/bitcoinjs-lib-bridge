//
//  UIImage+Snapshot.swift
//  wallet-ios-core
//
//  Created by 仇弘扬 on 2017/12/18.
//  Copyright © 2017年 BitBill. All rights reserved.
//

import Foundation
import UIKit

extension UIImage {
    func snapshotSubImage(rect: CGRect) -> UIImage? {
        guard let cgimage = cgImage else { return nil }
        let piexlRect = CGRect(x: rect.origin.x * scale, y: rect.origin.y * scale, width: rect.width * scale, height: rect.height * scale)
        if let imageRef = cgimage.cropping(to: piexlRect) {
            let toReturn = UIImage(cgImage: imageRef)
            return toReturn
        }
        return nil
    }
}
