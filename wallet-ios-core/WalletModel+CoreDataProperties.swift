//
//  WalletModel+CoreDataProperties.swift
//  
//
//  Created by 仇弘扬 on 2017/11/17.
//
//

import Foundation
import CoreData


extension WalletModel {

    @nonobjc public class func fetchRequest() -> NSFetchRequest<WalletModel> {
        return NSFetchRequest<WalletModel>(entityName: "WalletModel")
    }

    @NSManaged public var createDate: NSDate?
    @NSManaged public var encryptedMnemonic: String?
    @NSManaged public var encryptedSeed: String?
    @NSManaged public var isNeedBackup: Bool
    @NSManaged public var lastAddressIndex: Int64
    @NSManaged public var name: String?
    @NSManaged public var seedHash: String?
    @NSManaged public var mnemonicHash: String?

}
