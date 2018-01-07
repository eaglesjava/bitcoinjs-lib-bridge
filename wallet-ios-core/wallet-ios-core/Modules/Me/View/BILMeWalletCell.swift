//
//  BILMeWalletCell.swift
//  wallet-ios-core
//
//  Created by 仇弘扬 on 2017/12/21.
//  Copyright © 2017年 BitBill. All rights reserved.
//

import UIKit

class BILMeWalletCell: BILMeCell {

    var wallet: WalletModel? {
        didSet {
            guard let w = wallet else { return }
            titleLabel.text = w.id
            actionButton.isHidden = !w.isNeedBackup
            actionButton.setTitle(.meWallet_backupNow, for: .normal)
        }
    }
    
    @IBOutlet weak var actionButton: UIButton!
    
    @IBAction func actionButtonTouched(_ sender: Any) {
        guard let meCont = viewController() as? BILMeController else { return }
        meCont.performSegue(withIdentifier: .bil_meToBackupWalletSegue, sender: wallet)
    }
    override func awakeFromNib() {
        super.awakeFromNib()
        // Initialization code
    }

    override func setSelected(_ selected: Bool, animated: Bool) {
        super.setSelected(selected, animated: animated)

        // Configure the view for the selected state
    }

}
