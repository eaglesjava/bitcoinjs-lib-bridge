//
//  BILHomeShortcutCell.swift
//  wallet-ios-core
//
//  Created by 仇弘扬 on 2017/12/27.
//  Copyright © 2017年 BitBill. All rights reserved.
//

import UIKit

class BILHomeShortcutCell: UITableViewCell {
    @IBOutlet weak var contactButton: BILHomeShortcutButton!
    @IBOutlet weak var scanButton: BILHomeShortcutButton!
    
    override func awakeFromNib() {
        super.awakeFromNib()
        // Initialization code
        languageDidChanged()
    }

    override func languageDidChanged() {
		super.languageDidChanged()
        contactButton.setTitle("Add contact".bil_ui_localized, for: .normal)
        scanButton.setTitle("Scan".bil_ui_localized, for: .normal)
    }
    
	@IBAction func scanAction(_ sender: Any) {
		BILControllerManager.shared.mainTabBarController?.selectedIndex = 3
		postNotification(name: .shortcutScanQRCode, after: 10)
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
