//
//  BILContactManager.swift
//  wallet-ios-core
//
//  Created by 仇弘扬 on 2017/12/26.
//  Copyright © 2017年 BitBill. All rights reserved.
//

import Foundation
import UIKit
import CoreData

let bil_contactManager = BILCoreDataModelManager<ContactModel>(modelName: "ContactModel", notificationName: .contactDidChanged)
let bil_btc_addressManager = BILCoreDataModelManager<BTCAddressModel>(modelName: "BTCAddressModel", notificationName: nil)

class BILCoreDataModelManager<T: NSManagedObject>: NSObject {
	
	weak var appDelegate: AppDelegate?
	
	lazy var coreDataContext = {
		(UIApplication.shared.delegate as! AppDelegate).persistentContainer.viewContext
	}()
	
	var modelName: String
	var notificationName: Notification.Name?
	
	init(modelName: String, notificationName: Notification.Name? = nil) {
		self.modelName = modelName
		self.notificationName = notificationName
	}
	
	var models: [T] {
		get {
			var results = [T]()
			do {
				let context = coreDataContext
				let request: NSFetchRequest<T> = T.fetchRequest() as! NSFetchRequest<T>
				results.append(contentsOf: try context.fetch(request))
			} catch {
				debugPrint("查询\(T.self)失败")
				UIApplication.shared.keyWindow?.makeToast("查询\(T.self)失败")
			}
			return results
		}
	}
	
	func newModel() -> T {
		let context = coreDataContext
		let contact = NSEntityDescription.insertNewObject(forEntityName: modelName, into: context) as! T
		return contact
	}
	
	func saveModels() throws {
		let context = coreDataContext
		guard context.hasChanges else {
			return
		}
		try context.save()
		postNotication()
	}
	
	func remove(model: T) throws {
		let context = coreDataContext
		context.delete(model)
		try context.save()
		postNotication()
	}
	
	func postNotication() {
		guard let n = notificationName else { return }
		NotificationCenter.default.post(name: n, object: nil)
	}
    
    func fetch(key: String, value: String) -> T? {
        var model: T? = nil
        do {
            let context = (UIApplication.shared.delegate as! AppDelegate).persistentContainer.viewContext
            let request: NSFetchRequest<T> = T.fetchRequest() as! NSFetchRequest<T>
            request.predicate = NSPredicate(format: "\(key)=%@", value)
            let results = try context.fetch(request)
            model = results.first
        } catch {
            return model
        }
        return model
    }
	
}
