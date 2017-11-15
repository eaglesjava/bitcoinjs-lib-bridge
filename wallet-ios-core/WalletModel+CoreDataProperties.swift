//
//  WalletModel+CoreDataProperties.swift
//  
//
//  Created by 仇弘扬 on 2017/11/14.
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
    @NSManaged public var supportedCoinTypes: NSOrderedSet?

}

// MARK: Generated accessors for supportedCoinTypes
extension WalletModel {

    @objc(insertObject:inSupportedCoinTypesAtIndex:)
    @NSManaged public func insertIntoSupportedCoinTypes(_ value: CoinModel, at idx: Int)

    @objc(removeObjectFromSupportedCoinTypesAtIndex:)
    @NSManaged public func removeFromSupportedCoinTypes(at idx: Int)

    @objc(insertSupportedCoinTypes:atIndexes:)
    @NSManaged public func insertIntoSupportedCoinTypes(_ values: [CoinModel], at indexes: NSIndexSet)

    @objc(removeSupportedCoinTypesAtIndexes:)
    @NSManaged public func removeFromSupportedCoinTypes(at indexes: NSIndexSet)

    @objc(replaceObjectInSupportedCoinTypesAtIndex:withObject:)
    @NSManaged public func replaceSupportedCoinTypes(at idx: Int, with value: CoinModel)

    @objc(replaceSupportedCoinTypesAtIndexes:withSupportedCoinTypes:)
    @NSManaged public func replaceSupportedCoinTypes(at indexes: NSIndexSet, with values: [CoinModel])

    @objc(addSupportedCoinTypesObject:)
    @NSManaged public func addToSupportedCoinTypes(_ value: CoinModel)

    @objc(removeSupportedCoinTypesObject:)
    @NSManaged public func removeFromSupportedCoinTypes(_ value: CoinModel)

    @objc(addSupportedCoinTypes:)
    @NSManaged public func addToSupportedCoinTypes(_ values: NSOrderedSet)

    @objc(removeSupportedCoinTypes:)
    @NSManaged public func removeFromSupportedCoinTypes(_ values: NSOrderedSet)

}
