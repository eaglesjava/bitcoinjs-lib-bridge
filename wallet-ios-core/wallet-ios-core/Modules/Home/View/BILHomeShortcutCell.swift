//
//  BILHomeShortcutCell.swift
//  wallet-ios-core
//
//  Created by 仇弘扬 on 2017/12/27.
//  Copyright © 2017年 BitBill. All rights reserved.
//

import UIKit

class BILHomeShortcutCell: UITableViewCell {

    override func awakeFromNib() {
        super.awakeFromNib()
        // Initialization code
    }

	@IBAction func scanAction(_ sender: Any) {
		BILControllerManager.shared.mainTabBarController?.selectedIndex = 3
		postNotification(name: .shortcutScanQRCode, after: 100)
	}
	@IBAction func addContactAction(_ sender: Any) {
		BILControllerManager.shared.mainTabBarController?.selectedIndex = 1
		postNotification(name: .shortcutAddContact, after: 100)
	}
	
	func postNotification(name: Notification.Name, after milliseconds: Int) {
		DispatchQueue.main.asyncAfter(deadline: .now() + DispatchTimeInterval.milliseconds(milliseconds)) {
			NotificationCenter.default.post(name: name, object: nil)
		}
	}
	
	override func setSelected(_ selected: Bool, animated: Bool) {
        super.setSelected(selected, animated: animated)

        // Configure the view for the selected state
    }

}
