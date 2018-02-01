//
//  BILMeController.swift
//  wallet-ios-core
//
//  Created by 仇弘扬 on 2017/12/21.
//  Copyright © 2017年 BitBill. All rights reserved.
//

import UIKit

extension String {
    static var bil_meToBackupWalletSegue: String { return "BILMeToBackupWalletSegue" }
	static var bil_meToWalletDetailSegue: String { return "BILMeToWalletDetailSegue" }
    static var bil_meToSettingSegue: String { return "BILMeToSettingSegue" }
    static var bil_meToAboutUsSegue: String { return "BILMeToAboutUsSegue" }
    static var bil_meToFeedbackSegue: String { return "BILMeToFeedbackSegue" }
}

class BILMeController: BILBaseViewController {
    
    enum BILMeSectionType: Int {
        case preference = 0
		case contacts
        case wallet
        case system
        case other
        
        var sectionTitle: String {
            switch self {
            case .preference:
                return .meMe_meSection_preference
			case .contacts:
				return .meMe_meSection_contacts
            case .wallet:
                return .meMe_meSection_wallet
            case .system:
                return .meMe_meSection_system
            case .other:
                return .meMe_meSection_other
            }
        }
        
        var sectionViewHeight: CGFloat {
            if numberOfRows() == 0 {
                return 0
            }
            return 36
        }
        
        var rowHeight: CGFloat {
            return 60
        }
        
        func numberOfRows() -> Int {
            return dataArray().count
        }
        
        func dataArray() -> [Any] {
            switch self {
            case .preference:
                return [String.meMe_meCell_shortcut]
			case .contacts:
				return [String.meMe_meCell_contactBackup, String.meMe_meCell_contactRecover]
            case .wallet:
                return BILWalletManager.shared.wallets
            case .system:
                return [String.meMe_meCell_system]
            case .other:
                return [String.meMe_meCell_aboutUs, "Feedback".bil_ui_localized]
            }
        }
        
        func cellID() -> String {
            switch self {
            case .preference:
                return "BILMeSwitchCell"
            case .wallet:
                return "BILMeWalletCell"
            default:
                return "BILMeCell"
            }
        }
        
    }

    @IBOutlet weak var tableView: UITableView!
    var sections: [BILMeSectionType] = [.preference, .contacts, .wallet, .system, .other]
    
    override func viewDidLoad() {
        super.viewDidLoad()

        tableView.register(UINib(nibName: "BILTableViewHeaderFooterView", bundle: nil), forHeaderFooterViewReuseIdentifier: "BILTableViewHeaderFooterView")
		
		NotificationCenter.default.addObserver(self, selector: #selector(walletDidChanged(notification:)), name: .walletDidChanged, object: nil)
    }
	
	deinit {
		NotificationCenter.default.removeObserver(self, name: .walletDidChanged, object: nil)
	}
    
    override func viewDidAppear(_ animated: Bool) {
        super.viewDidAppear(animated)
    }
	
	override func languageDidChanged() {
		super.languageDidChanged()
		title = "Me".bil_ui_localized
		tableView.reloadData()
	}
	
	@objc
	func walletDidChanged(notification: Notification) {
		tableView.reloadData()
	}
	
	func backupContacts() {
		let key = BILDeviceManager.shared.contactKey
        BILAuthenticator.shared.autherizeWithTouchIdOrFaceId(reason: "", success: {
            self.showTipAlert(title: .meMe_contact_yourKey, msg: key, actionTitle: .meMe_contact_copyKey) {
                UIPasteboard.general.string = key
                self.bil_makeToast(msg: .meMe_contact_keyCopied)
            }
        }) {
            self.bil_makeToast(msg: .meMe_contact_authFailed)
        }
	}
	
	func recoverContacts() {
		let alert = UIAlertController(title: .meMe_meCell_contactRecover, message: nil, preferredStyle: .alert)
		
		alert.addTextField { (textField) in
			textField.placeholder = .meMe_contact_inputKey
		}
		
		alert.addAction(UIAlertAction(title: .meMe_confirm, style: .default, handler: { (action) in
            guard let key = alert.textFields?.first?.text, !key.isEmpty else {
                self.bil_makeToast(msg: .meMe_contact_keyEmpty)
                return
            }
            self.bil_showLoading(status: nil)
            ContactModel.recoverContactsFromServer(recoverKey: key, success: { (contacts) in
                let count = contacts.count
                if count == 0 {
                    self.bil_makeToast(msg: .meMe_contact_noMore)
                }
                else
                {
                    self.bil_makeToast(msg: "\(String.meMe_contact_recovered) \(contacts.count) \(String.meMe_contact_contact)")
                }
                self.bil_dismissHUD()
            }, failure: { (msg, code) in
                self.bil_makeToast(msg: msg)
                self.bil_dismissHUD()
            })
		}))
		
		alert.addAction(UIAlertAction(title: .meMe_cancel, style: .cancel, handler: { (action) in
			
		}))
		
		present(alert, animated: true, completion: nil)
	}

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }

    // MARK: - Navigation

    // In a storyboard-based application, you will often want to do a little preparation before navigation
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        // Get the new view controller using segue.destinationViewController.
        // Pass the selected object to the new view controller.
        guard let id = segue.identifier else { return }
        switch id {
        case String.bil_meToBackupWalletSegue:
            guard let wallet = sender as? WalletModel else { return }
            if let cont = (segue.destination as? UINavigationController)?.viewControllers.first as? BILBackupWalletMnemonicController {
                cont.mnemonicHash = wallet.mnemonicHash
            }
		case String.bil_meToWalletDetailSegue:
			guard let wallet = sender as? WalletModel else { return }
			let cont = segue.destination as! BILWalletDetailSettingController
			cont.wallet = wallet
        default:
            ()
        }
    }

}

// MARK: - Table view data source

extension BILMeController: UITableViewDataSource, UITableViewDelegate {
    
    func numberOfSections(in tableView: UITableView) -> Int {
        return sections.count
    }
    
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return sections[section].dataArray().count
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let sectionType = sections[indexPath.section]
        let cell = tableView.dequeueReusableCell(withIdentifier: sectionType.cellID(), for: indexPath)
		let model = sectionType.dataArray()[indexPath.row]
        switch sectionType {
        case .preference:
            let c = cell as! BILMeSwitchCell
            c.bil_switch.isOn = BILSettingManager.isHomeShortcutEnabled
            c.bil_switch.onTintColor = UIColor(patternImage: UIImage(named: "pic_switch_background")!)
            c.switchChangedClosure = { (isOn) in
                BILSettingManager.isHomeShortcutEnabled = isOn
            }
            c.titleLabel.text = model as? String
        case .other: fallthrough
        case .system: fallthrough
		case .contacts:
			let c = cell as! BILMeCell
			c.titleLabel.text = model as? String
        case .wallet:
            let c = cell as! BILMeWalletCell
            let wallet = model as? WalletModel
            c.wallet = wallet
        }
        return cell
    }
    
    func tableView(_ tableView: UITableView, heightForRowAt indexPath: IndexPath) -> CGFloat {
        let sectionType = sections[indexPath.section]
        return sectionType.rowHeight
    }
    
    func tableView(_ tableView: UITableView, heightForHeaderInSection section: Int) -> CGFloat {
        let sectionType = sections[section]
        return sectionType.sectionViewHeight
    }
    
    func tableView(_ tableView: UITableView, viewForHeaderInSection section: Int) -> UIView? {
        let sectionType = sections[section]
        let headerView = tableView.dequeueReusableHeaderFooterView(withIdentifier: "BILTableViewHeaderFooterView") as! BILTableViewHeaderFooterView
        headerView.titleLabel.text = sectionType.sectionTitle
        headerView.titleLabel.textColor = UIColor(white: 1, alpha: 0.3)
        headerView.bil_backgroundView.backgroundColor = UIColor(hex: 0x0f0f0f)
        return headerView
    }
	
	func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
		let sectionType = sections[indexPath.section]
		switch sectionType {
		case .contacts:
			switch indexPath.row {
			case 0:
				backupContacts()
			case 1:
				recoverContacts()
			default:
				()
			}
		case .wallet:
			guard let wallet = sectionType.dataArray()[indexPath.row] as? WalletModel else { return }
			performSegue(withIdentifier: .bil_meToWalletDetailSegue, sender: wallet)
        case .system:
            performSegue(withIdentifier: .bil_meToSettingSegue, sender: nil)
        case .other:
            switch indexPath.row {
            case 0:
                performSegue(withIdentifier: .bil_meToAboutUsSegue, sender: nil)
            case 1:
                performSegue(withIdentifier: .bil_meToFeedbackSegue, sender: nil)
            default:
                ()
            }
		default:
			()
		}
	}
    
    /*
     // Override to support conditional editing of the table view.
     override func tableView(_ tableView: UITableView, canEditRowAt indexPath: IndexPath) -> Bool {
     // Return false if you do not want the specified item to be editable.
     return true
     }
     */
}
