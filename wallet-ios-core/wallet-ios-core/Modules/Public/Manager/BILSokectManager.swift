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
        NotificationCenter.default.addObserver(self, selector: #selector(postWallets), name: .walletCountDidChanged, object: nil)
    }
    
    deinit {
        NotificationCenter.default.removeObserver(self, name: .walletCountDidChanged, object: nil)
    }
    
    func startConnect() {
        if let s = socket {
            guard s.status != .connected else { return }
        }
        connect()
        socket?.connect()
    }
    
    func stopConnect() {
        guard let s = socket else { return }
        guard s.status == .connected else { return }
        socket?.disconnect()
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
            NotificationCenter.default.post(name: .networkStatusDidChanged, object: nil)
        }
        socket.on(clientEvent: .disconnect) { (data, ack) in
            debugPrint("socket disconnect")
            NotificationCenter.default.post(name: .networkStatusDidChanged, object: nil)
        }
        socket.on(clientEvent: .reconnect) { (data, ack) in
            debugPrint("socket disconnect, will try reconnect")
            NotificationCenter.default.post(name: .networkStatusDidChanged, object: nil)
        }
        socket.on("message") { (data, emitter) in
            debugPrint(data)
        }
        socket.on(.bil_socket_event_unconfirm) { (data, emitter) in
            debugPrint(data)
            let jsonStr = data.first as? String ?? ""
            var json = JSON(parseJSON: jsonStr)["context"]
            let amount = json["amount"].int64Value
            BILAudioPlayer.playRecieveMoney(isBig: amount > (BTC_SATOSHI / 2))
            let wallet = WalletModel.fetch(id: json["walletId"].string)
            wallet?.syncWallet(json: json)
            BILWalletManager.shared.btcBlockHeight = json["height"].intValue
            NotificationCenter.default.post(name: .recievedUnconfirmTransaction, object: nil)
        }
        socket.on(.bil_socket_event_confirm) { (data, emitter) in
            debugPrint(data)
            NotificationCenter.default.post(name: .unconfirmTransactionBeenConfirmed, object: nil)
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
        
        guard let jsonString = JSON(data).rawString()?.replacingOccurrences(of: "\n", with: "").replacingOccurrences(of: " ", with: "") else { return }
        debugPrint("json : \(jsonString)")
        socket?.emitWithAck(.bil_socket_event_register, jsonString).timingOut(after: 3, callback: { (data) in
            debugPrint(data)
        })
    }
    
}
