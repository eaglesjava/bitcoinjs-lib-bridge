//
//  WalletSurpportedCoin+CoreDataProperties.swift
//  
//
//  Created by 仇弘扬 on 2017/11/14.
//
//

import Foundation
import CoreData


extension WalletSurpportedCoin {

    @nonobjc public class func fetchRequest() -> NSFetchRequest<WalletSurpportedCoin> {
        return NSFetchRequest<WalletSurpportedCoin>(entityName: "WalletSurpportedCoin")
    }

    @NSManaged public var walletModel: WalletModel?
    @NSManaged public var coin: CoinModel?

}
