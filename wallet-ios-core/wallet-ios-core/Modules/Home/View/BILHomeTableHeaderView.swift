//
//  BILHomeTableHeaderView.swift
//  wallet-ios-core
//
//  Created by 仇弘扬 on 2017/11/24.
//  Copyright © 2017年 BitBill. All rights reserved.
//

import UIKit
import SocketIO

protocol BILHomeTableHeaderViewDelegate: NSObjectProtocol {
	func actionButtonTapped(headerView: BILHomeTableHeaderView)
}

class BILHomeTableHeaderView: UITableViewHeaderFooterView {

	@IBOutlet weak var titleLabel: UILabel!
	@IBOutlet weak var subTitleLabel: UILabel!
	@IBOutlet weak var actionButton: UIButton!
	@IBOutlet weak var bgImageView: UIImageView!
    @IBOutlet weak var networkIndicator: UIView!
    
	weak var delegate: BILHomeTableHeaderViewDelegate?
	
	var buttonImage: UIImage? {
		didSet {
			if let bi = buttonImage {
				actionButton.setImage(bi, for: .normal)
			}
			actionButton.isHidden = buttonImage == nil
		}
	}
	
	override func awakeFromNib() {
		super.awakeFromNib()
        NotificationCenter.default.addObserver(self, selector: #selector(networkDidChanged(notification:)), name: .networkStatusDidChanged, object: nil)
	}
    deinit {
        NotificationCenter.default.removeObserver(self, name: .networkStatusDidChanged, object: nil)
    }
    
    @objc
    func networkDidChanged(notification: Notification) {
        loadNetworkIndicator()
    }
    
	@IBAction func actionButtonTapped(_ sender: Any) {
		delegate?.actionButtonTapped(headerView: self)
	}
    
    func hideNetworkIndicator() {
        networkIndicator.isHidden = true
    }
    
    func showNetworkIndicator() {
        networkIndicator.isHidden = false
        loadNetworkIndicator()
    }
    
    func loadNetworkIndicator() {
        if let socket = BILSokectManager.manager.socket {
            let isConnected = socket.status == SocketIOStatus.connected
            networkIndicator.backgroundColor = isConnected ? UIColor(hex: 0xABE64D) : UIColor(white: 1.0, alpha: 0.3)
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
