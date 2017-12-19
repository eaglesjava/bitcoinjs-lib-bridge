//
//  BILSokectManager.swift
//  wallet-ios-core
//
//  Created by 仇弘扬 on 2017/12/16.
//  Copyright © 2017年 BitBill. All rights reserved.
//

import UIKit
import SocketIO
import SwiftyJSON

class BILSokectManager: NSObject {
    
    var socket: SocketIOClient?
    var manager: SocketManager?
    
    static let manager = {
        return BILSokectManager()
    }()
    
    override init() {
        super.init()
        NotificationCenter.default.addObserver(self, selector: #selector(postWallets), name: NSNotification.Name.walletDidChanged, object: nil)
    }
    
    deinit {
        NotificationCenter.default.removeObserver(self, name: NSNotification.Name.walletDidChanged, object: nil)
    }
    
    func startConnect() {
        connect()
        socket?.connect()
    }
    
    func stopConnect() {
        socket?.disconnect()
    }
    func reconnect() {
        manager?.reconnect()
    }
    
    func connect(urlString: String = .bil_socket_base_url) {
        guard let url = URL(string: urlString) else { return }
        let manager = SocketManager(socketURL: url, config: [.log(true), .compress])
        let socket = manager.defaultSocket
        self.manager = manager
        self.socket = socket
        socket.on(clientEvent: .connect) { (data, ack) in
            debugPrint(data)
            self.postWallets()
        }
        socket.on(clientEvent: .reconnect) { (data, ack) in
            self.postWallets()
        }
        socket.on("message") { (data, emitter) in
            debugPrint(data)
        }
        socket.on(.bil_socket_event_unconfirom) { (data, emitter) in
            debugPrint(data)
        }
        socket.on(.bil_socket_event_confirom) { (data, emitter) in
            debugPrint(data)
        }
    }
    
    @objc
    func postWallets() {
        var ids = [String]()
        for wallet in BILWalletManager.shared.wallets {
            ids.append(wallet.id!)
        }
        
        let data = ["clientId": BILDeviceManager.shared.deviceID,
                    "walletId": ids.joined(separator: "|"),
                    "deviceToken": BILAppStartUpManager.shared.deviceToken ?? "",
                    "platform": "iOS"]
        
        guard let jsonString = JSON(data).rawString() else { return }
        socket?.emitWithAck(.bil_socket_event_register, jsonString).timingOut(after: 3, callback: { (data) in
            debugPrint(data)
        })
        
    }
    
}
