//
//  BILAudioPlayer.swift
//  wallet-ios-core
//
//  Created by 仇弘扬 on 2017/12/21.
//  Copyright © 2017年 BitBill. All rights reserved.
//

import UIKit
import AVFoundation

class BILAudioPlayer: NSObject {
    static func play(filePath: String?) {
        guard let path = filePath else {
            return
        }
        guard FileManager.default.fileExists(atPath: path) else {
            return
        }
        let url = URL(fileURLWithPath: path)
        
        let urlCF = url as CFURL
        
        var soundID:SystemSoundID = 0
        AudioServicesCreateSystemSoundID(urlCF, &soundID)
        AudioServicesPlaySystemSoundWithCompletion(soundID) {
            AudioServicesDisposeSystemSoundID(soundID)
        }
    }
    
    static func playRecieveMoney(isBig: Bool = false){
        play(filePath: Bundle.main.path(forResource: isBig ? "diaoluo_da" : "diaoluo_xiao", ofType: "mp3"))
    }
}
