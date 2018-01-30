//
//  BILSendModel.swift
//  wallet-ios-core
//
//  Created by 仇弘扬 on 2017/12/12.
//  Copyright © 2017年 BitBill. All rights reserved.
//

import UIKit
import SwiftyJSON

class BILSendModel: BILReceiveModel {
    
    var wallet: WalletModel?
    
    var isSendAll = false
    var isContactAddress = false
    
    var transaction: BTCTransaction?
}
