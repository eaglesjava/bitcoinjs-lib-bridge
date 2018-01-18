//
//  AppDelegate.swift
//  wallet-ios-core
//
//  Created by 仇弘扬 on 2017/11/7.
//  Copyright © 2017年 BitBill. All rights reserved.
//

import UIKit
import CoreData
import IQKeyboardManagerSwift
import CryptoSwift
import UserNotifications

enum BILApplicationShortcutItemType: String {
    
    case contact
    case scanQRCode
    case send
    case receive
    
}

@UIApplicationMain
class AppDelegate: UIResponder, UIApplicationDelegate {

	var window: UIWindow?


	func application(_ application: UIApplication, didFinishLaunchingWithOptions launchOptions: [UIApplicationLaunchOptionsKey: Any]?) -> Bool {
		// Override point for customization after application launch.
        
        print(BILSettingManager.currentLanguage)
//        BILSettingManager.currentLanguage = .zh_cn
//        print(BILSettingManager.currentLanguage)
		
		BILAppStartUpManager.shared.startSetup()
		BILWalletManager.shared.appDelegate = self
        
        UNUserNotificationCenter.current().delegate = self
		
		let results = BILWalletManager.shared.wallets
		debugPrint(results)
		
		if results.count == 0 {
			let cont = UIStoryboard(name: "NewWallet", bundle: nil).instantiateInitialViewController()
			window?.makeKeyAndVisible()
			window?.rootViewController?.present(cont!, animated: false, completion: nil)
            UIApplication.shared.shortcutItems = nil
		}
        else
        {
            createShortcuts()
        }
        
        NotificationCenter.default.addObserver(forName: .walletCountDidChanged, object: nil, queue: OperationQueue.main) { (_) in
            if BILWalletManager.shared.wallets.count == 0 {
                UIApplication.shared.shortcutItems = nil
            }
            else
            {
                self.createShortcuts()
            }
        }
        
        if let shortcutItem = launchOptions?[UIApplicationLaunchOptionsKey.shortcutItem] as? UIApplicationShortcutItem {
            handle(shortcutItem: shortcutItem)
        }
		
		return true
	}
    
    func createShortcuts() {
        let contactItem = UIApplicationShortcutItem(type: BILApplicationShortcutItemType.contact.rawValue, localizedTitle: "Contacts".bil_ui_localized, localizedSubtitle: nil, icon: UIApplicationShortcutIcon(templateImageName: "f3"), userInfo: nil)
        let scanItem = UIApplicationShortcutItem(type: BILApplicationShortcutItemType.scanQRCode.rawValue, localizedTitle: "Scan".bil_ui_localized, localizedSubtitle: nil, icon: UIApplicationShortcutIcon(templateImageName: "f4"), userInfo: nil)
        let sendItem = UIApplicationShortcutItem(type: BILApplicationShortcutItemType.send.rawValue, localizedTitle: "Send".bil_ui_localized, localizedSubtitle: nil, icon: UIApplicationShortcutIcon(templateImageName: "f2"), userInfo: nil)
        let receiveItem = UIApplicationShortcutItem(type: BILApplicationShortcutItemType.receive.rawValue, localizedTitle: "Receive".bil_ui_localized, localizedSubtitle: nil, icon: UIApplicationShortcutIcon(templateImageName: "f1"), userInfo: nil)
        
        UIApplication.shared.shortcutItems = [contactItem, scanItem, sendItem, receiveItem].reversed()
    }
    
    func application(_ application: UIApplication, performActionFor shortcutItem: UIApplicationShortcutItem, completionHandler: @escaping (Bool) -> Void) {
        handle(shortcutItem: shortcutItem)
        completionHandler(true)
    }
    
    func handle(shortcutItem: UIApplicationShortcutItem) {
        guard let type = BILApplicationShortcutItemType(rawValue: shortcutItem.type) else { return }
        switch type {
        case .contact:
            BILControllerManager.shared.mainTabBarController?.selectedIndex = 1
        case .scanQRCode:
            BILControllerManager.shared.mainTabBarController?.selectedIndex = 3
            if let cont = (BILControllerManager.shared.mainTabBarController?.selectedViewController as? UINavigationController)?.viewControllers.first as? BILSendController {
                cont.scanQRCodeAction(shortcutItem)
            }
        case .send:
            BILControllerManager.shared.mainTabBarController?.selectedIndex = 3
        case .receive:
            BILControllerManager.shared.mainTabBarController?.selectedIndex = 2
        }
    }

	func applicationWillResignActive(_ application: UIApplication) {
		// Sent when the application is about to move from active to inactive state. This can occur for certain types of temporary interruptions (such as an incoming phone call or SMS message) or when the user quits the application and it begins the transition to the background state.
		// Use this method to pause ongoing tasks, disable timers, and invalidate graphics rendering callbacks. Games should use this method to pause the game.
        BILSokectManager.manager.stopConnect()
	}

	func applicationDidEnterBackground(_ application: UIApplication) {
		// Use this method to release shared resources, save user data, invalidate timers, and store enough application state information to restore your application to its current state in case it is terminated later.
		// If your application supports background execution, this method is called instead of applicationWillTerminate: when the user quits.
        BILSokectManager.manager.stopConnect()
	}

	func applicationWillEnterForeground(_ application: UIApplication) {
		// Called as part of the transition from the background to the active state; here you can undo many of the changes made on entering the background.
	}

	func applicationDidBecomeActive(_ application: UIApplication) {
		// Restart any tasks that were paused (or not yet started) while the application was inactive. If the application was previously in the background, optionally refresh the user interface.
        debugPrint("applicationDidBecomeActive")
        BILSokectManager.manager.startConnect()
	}

	func applicationWillTerminate(_ application: UIApplication) {
		// Called when the application is about to terminate. Save data if appropriate. See also applicationDidEnterBackground:.
		// Saves changes in the application's managed object context before the application terminates.
        debugPrint("applicationWillTerminate")
        BILSokectManager.manager.stopConnect()
	}
	
    // MARK: - Push
    
    func application(_ application: UIApplication, didFailToRegisterForRemoteNotificationsWithError error: Error) {
        debugPrint(error)
    }
    func application(_ application: UIApplication, didRegisterForRemoteNotificationsWithDeviceToken deviceToken: Data) {
        let token = deviceToken.toHexString()
        BILAppStartUpManager.shared.deviceToken = token
        BILSokectManager.manager.postWallets()
        debugPrint("push token = \(token)")
    }
    
	// MARK: - Core Data stack
	
	lazy var persistentContainer: NSPersistentContainer = {
		/*
		The persistent container for the application. This implementation
		creates and returns a container, having loaded the store for the
		application to it. This property is optional since there are legitimate
		error conditions that could cause the creation of the store to fail.
		*/
		let container = NSPersistentContainer(name: "WalletModel")
		container.loadPersistentStores(completionHandler: { (storeDescription, error) in
			if let error = error as NSError? {
				// Replace this implementation with code to handle the error appropriately.
				// fatalError() causes the application to generate a crash log and terminate. You should not use this function in a shipping application, although it may be useful during development.
				
				/*
				Typical reasons for an er ror here include:
				* The parent directory does not exist, cannot be created, or disallows writing.
				* The persistent store is not accessible, due to permissions or data protection when the device is locked.
				* The device is out of space.
				* The store could not be migrated to the current model version.
				Check the error message to determine what the actual problem was.
				*/
				fatalError("Unresolved error \(error), \(error.userInfo)")
			}
		})
		return container
	}()
	
	// MARK: - Core Data Saving support
	
	func saveContext () {
		let context = persistentContainer.viewContext
		if context.hasChanges {
			do {
				try context.save()
			} catch {
				// Replace this implementation with code to handle the error appropriately.
				// fatalError() causes the application to generate a crash log and terminate. You should not use this function in a shipping application, although it may be useful during development.
				let nserror = error as NSError
				fatalError("Unresolved error \(nserror), \(nserror.userInfo)")
			}
		}
	}
}

extension AppDelegate: UNUserNotificationCenterDelegate {
    func userNotificationCenter(_ center: UNUserNotificationCenter, didReceive response: UNNotificationResponse, withCompletionHandler completionHandler: @escaping () -> Void) {
        
    }
    func userNotificationCenter(_ center: UNUserNotificationCenter, willPresent notification: UNNotification, withCompletionHandler completionHandler: @escaping (UNNotificationPresentationOptions) -> Void) {
        completionHandler([.alert, .badge, .sound])
    }
}

