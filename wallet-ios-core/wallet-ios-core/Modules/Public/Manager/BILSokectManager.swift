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
//            var ids = [String]()
//            for wallet in BILWalletManager.shared.wallets {
//                ids.append(wallet.id!)
//            }
//            ack.with(["clientID": BILDeviceManager.shared.deviceID, "walletID": ids.joined(separator: "|")])
            self.postWallets()
        }
        socket.on(clientEvent: .reconnect) { (data, ack) in
            self.postWallets()
        }
        socket.on("message") { (data, emitter) in
            debugPrint(data)
        }
    }
    
    func postWallets() {
        var ids = [String]()
        for wallet in BILWalletManager.shared.wallets {
            ids.append(wallet.id!)
        }
        
        guard let jsonString = JSON(["clientID": BILDeviceManager.shared.deviceID, "walletID": ids.joined(separator: "|")]).rawString() else { return }
        socket?.emitWithAck("message", jsonString).timingOut(after: 3, callback: { (data) in
            debugPrint(data)
        })
        
    }
    
}
