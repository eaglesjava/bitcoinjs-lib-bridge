//
//  CoinModel+CoreDataProperties.swift
//  
//
//  Created by 仇弘扬 on 2017/11/14.
//
//

import Foundation
import CoreData


extension CoinModel {

    @nonobjc public class func fetchRequest() -> NSFetchRequest<CoinModel> {
        return NSFetchRequest<CoinModel>(entityName: "CoinModel")
    }

    @NSManaged public var icon: String?
    @NSManaged public var name: String?

}
