//
//  BILAllInOneView.swift
//  wallet-ios-core
//
//  Created by 仇弘扬 on 2017/11/17.
//  Copyright © 2017年 BitBill. All rights reserved.
//

import UIKit

class BILAllInOneView: BILGuideBaseView {

	override func awakeFromNib() {
		super.awakeFromNib()
		backgroundColor = UIColor.clear
	}
	
	override func setupItems() {
		let screenCenter = Int(UIScreen.main.bounds.width / 2)
		let screenWidth = UIScreen.main.bounds.width
		var startPoints = [CGPoint(x: screenCenter - 116, y: 208), CGPoint(x: screenCenter - 94, y: 124), CGPoint(x: screenCenter - 22, y: 248), CGPoint(x: screenCenter + 70, y: 230), CGPoint(x: screenCenter + 90, y: 92), CGPoint(x: screenCenter + 120, y: 162)]
		var endPoints = [CGPoint]()
		var a: [CGFloat] = [2.4, 2.1, 1.9, 1.5, 1.7, 1.2]
        for i in 0..<startPoints.count {
            var point = startPoints[i]
            point.y -= BILAppStartUpManager.shared.isSmallScreen ? 60 : 0
            startPoints[i] = point
            var pp = point
            pp.x = (point.x - screenWidth) * a[i]
            endPoints.append(pp)
        }
		for i in 0...5 {
			items.append(BILGuideImageItem(startPoint: startPoints[i], endPoint: endPoints[i], image: UIImage(named: "icon_guide_\(i + 1)")!))
		}
		
		var screenCenterPoint = UIApplication.shared.keyWindow!.center
		screenCenterPoint.y = 126 - (BILAppStartUpManager.shared.isSmallScreen ? 36 : 0)
		let item = BILGuideImageItem(startPoint: screenCenterPoint, endPoint: CGPoint(x: screenCenterPoint.x + screenWidth, y: screenCenterPoint.y), image: UIImage(named: "icon_guide_phone_bg")!)
		item.endAlpha = 0.0
		items.append(item)
	}
    
    override func didMoveToWindow() {
        if window != nil {
            for index in 0..<(items.count - 1) {
                let item = items[index]
                if item is BILGuideImageItem {
                    let i = item as! BILGuideImageItem
                    i.imageView.transform = CGAffineTransform(scaleX: 0, y: 0)
                    UIView.animate(withDuration: 0.25, delay: 0.08 * Double(index), options: .curveEaseInOut, animations: {
                        i.imageView.transform = CGAffineTransform(scaleX: 1, y: 1)
                    }, completion: nil)
                }
            }
        }
    }
	
    /*
    // Only override draw() if you perform custom drawing.
    // An empty implementation adversely affects performance during animation.
    override func draw(_ rect: CGRect) {
        // Drawing code
    }
    */

}
