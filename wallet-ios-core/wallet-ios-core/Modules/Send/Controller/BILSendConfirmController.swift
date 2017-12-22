//
//  BILSendConfirmController.swift
//  wallet-ios-core
//
//  Created by 仇弘扬 on 2017/12/12.
//  Copyright © 2017年 BitBill. All rights reserved.
//

import UIKit
import SwiftyJSON

struct BTCFee {
    var isBest = false
    var fee = 0
    var timeInMinute = 0
    init(jsonData: JSON) {
        self.fee = jsonData["fee"].intValue
        self.timeInMinute = jsonData["time"].intValue
        self.isBest = jsonData["best"].boolValue
    }
    init(fee: Int, timeInMinute: Int, isBest: Bool = false) {
        self.fee = fee
        self.timeInMinute = timeInMinute
        self.isBest = isBest
    }
    
    var timeString: String {
        get {
            if timeInMinute < 60 {
                return " \(timeInMinute) 分钟"
            }
            let minuteInDay = 60 * 24
            if timeInMinute < minuteInDay {
                return " \(timeInMinute / 60) 小时"
            }
            
            return " \(timeInMinute / minuteInDay) 天"
        }
    }
}

class BILSendConfirmController: BILBaseViewController {
    @IBOutlet weak var addressLabel: UILabel!
    @IBOutlet weak var amountLabel: UILabel!
    @IBOutlet weak var walletIDLabel: UILabel!
    @IBOutlet weak var feeTipeLabel: UILabel!
    @IBOutlet weak var feeSlider: UISlider!
    
    @IBOutlet weak var remarkInputView: BILInputView!
    
    var sendModel: BILSendModel?
    var txBuilder: BTCTransactionBuilder? {
        didSet {
            sliderValueChanged(feeSlider)
        }
    }
    
    var fees = [BTCFee]()
    var maxFeePerByte = 0
    var bestFee = 0
	
	var isNotEnougnBalance = false
    
    override func viewDidLoad() {
        super.viewDidLoad()

        // Do any additional setup after loading the view.
        if let model = sendModel {
            walletIDLabel.text = model.wallet?.id
            addressLabel.text = model.address
            amountLabel.text = "\(model.bitcoinAmount) BTC"
        }
        
        loadTransactionBuildConfiguration()
    }
    
    @IBAction func sliderValueChanged(_ sender: UISlider) {
        print(sender.value)
        let currentFee = Int(sender.value)
        guard var nearFee = fees.last else { return }
        for i in 0..<fees.count - 1 {
            let bigFee = fees[i]
            let smallFee = fees[i + 1]
            print("\(sender.value), \(smallFee.fee), \(bigFee.fee)")
            if smallFee.fee <= currentFee, bigFee.fee >= currentFee {
                if (currentFee - smallFee.fee) >= (bigFee.fee - currentFee) {
                    nearFee = bigFee
                }
                else
                {
                    nearFee = smallFee
                }
                break
            }
        }
        guard let builder = txBuilder else { return }
        builder.feePerByte = currentFee
        do {
            let fee = Int64(try builder.fee(perByte: currentFee))
            
            if let model = sendModel, model.isSendAll {
                let remainAmount = Int64(model.bitcoinSatoshiAmount) - fee
                amountLabel.text = "\(BTCFormatString(btc: remainAmount)) BTC"
            }
            feeTipeLabel.text = "平均确认时间\(nearFee.timeString)，需耗费 \(BTCFormatString(btc: fee)) BTC"
			isNotEnougnBalance = false
        } catch {
            feeTipeLabel.text = "余额不足以支付手续费"
			isNotEnougnBalance = true
        }
    }
    
    func setFees(fees: [BTCFee], best:BTCFee?) {
        self.fees = fees
        guard let minFee = fees.last else { return }
        guard let maxFee = fees.first else { return }
        guard let bFee = best else { return }
        feeSlider.minimumValue = Float(minFee.fee)
        feeSlider.maximumValue = Float(maxFee.fee)
        feeSlider.value = Float(bFee.fee)
        maxFeePerByte = maxFee.fee
        bestFee = bFee.fee
        sliderValueChanged(feeSlider)
    }
    
    func loadTransactionBuildConfiguration() {
        guard let model = sendModel else { return }
        guard let wallet = model.wallet else { return }
        
        func errorHandler(msg: String) {
            self.showTipAlert(title: nil, msg: msg, dismissed: {
                self.navigationController?.popViewController(animated: true)
            })
            self.bil_dismissHUD()
        }
        
        func createTXBuilder(address: String?) {
            wallet.getTXBuildConfigurationFromServer(success: { (utxos, fees, bestFee)  in
                self.setFees(fees: fees, best: bestFee)
                self.amountLabel.text = wallet.btc_balanceString
                self.sendModel?.amount = wallet.btc_balanceString
                let builder = BTCTransactionBuilder(utxos: utxos, changeAddress: address, feePerByte: self.bestFee, maxFeePerByte: self.maxFeePerByte, isSendAll: model.isSendAll)
                _ = builder.addTargetOutput(output: BTCOutput(address: model.address, amount: model.bitcoinSatoshiAmount))
                self.bil_dismissHUD()
                if builder.canFeedOutputs() {
                    self.txBuilder = builder
                }
                else
                {
                    self.isNotEnougnBalance = true
                    self.showTipAlert(title: "提示", msg: "余额不足以支付手续费", dismissed: {
                        self.navigationController?.popViewController(animated: true)
                    })
                }
                
                
            }) { (msg, code) in
                errorHandler(msg: msg)
            }
        }
        
        bil_showLoading(status: "处理中...")
        
        if model.isSendAll {
            createTXBuilder(address: nil)
        }
        else
        {
            wallet.getNewBTCAddress(success: { (address) in
                createTXBuilder(address: address)
            }) { (msg) in
                errorHandler(msg: msg)
            }
        }
    }

    @IBAction func nextAction(_ sender: Any) {
		guard !isNotEnougnBalance else {
			showTipAlert(msg: "余额不足")
			return
		}
        guard (remarkInputView.textField.text ?? "").count <= 20 else {
            showTipAlert(title: nil, msg: "备注不能超过20个字")
            return
        }
        guard let wallet = sendModel?.wallet else {
            showTipAlert(title: "发送失败", msg: "请稍后再试")
            return
        }
        
        let alert = UIAlertController(title: "输入钱包密码", message: nil, preferredStyle: .alert)
        
        let ok = UIAlertAction(title: "确认", style: .default) { (action) in
            guard let pwd = alert.textFields?.first?.text, !pwd.isEmpty else {
                self.showAlertForFail("密码不能为空")
                return
            }
            if !wallet.checkPassword(pwd: pwd) {
                self.showAlertForFail("密码输入错误")
                return
            }
            self.send(password: pwd)
        }
        let cancel = UIAlertAction(title: "取消", style: .cancel) { (action) in
            
        }
        alert.addAction(ok)
        alert.addAction(cancel)
        
        alert.addTextField { (textField) in
            textField.placeholder = "请输入密码以确认"
            textField.isSecureTextEntry = true
        }
        
        present(alert, animated: true, completion: nil)
    }
    
    func send(password: String) {
        func errorHandler(msg: String) {
            self.showTipAlert(title: nil, msg: msg)
            self.bil_dismissHUD()
        }
        guard let builder = txBuilder else {
            errorHandler(msg: "创建交易失败")
            return
        }
        guard let wallet = sendModel?.wallet else { return }
        
        guard let seed = wallet.decryptSeed(pwd: password) else {
            errorHandler(msg: "解密 Seed 失败")
            return
        }
        bil_showLoading(status: "发送中...")
        builder.seedHex = seed
        builder.build(success: { (tx) in
            debugPrint(tx.bytesCount)
            debugPrint(tx.hexString)
            tx.remark = self.remarkInputView.textField.text
            wallet.send(transaction: tx, success: { (result) in
                debugPrint(result)
                self.sendSuccess(tx: tx)
            }, failure: { (msg, code) in
                debugPrint(msg)
                self.showTipAlert(title: "发送失败", msg: "\(msg), code = \(code)")
            })
            
        }, failure: { (error) in
            errorHandler(msg: error.localizedDescription)
        })
    }
    
    func sendSuccess(tx: BTCTransaction) {
        NotificationCenter.default.post(name: .transactionSended, object: nil)
        guard let cont = storyboard?.instantiateViewController(withIdentifier: "BILSendResultController") as? BILSendResultController else { return }
        sendModel?.transaction = tx
        cont.sendModel = sendModel
        self.bil_dismissHUD()
        present(cont, animated: true) {
            self.navigationController?.popToRootViewController(animated: false)
        }
    }
    
    func showAlertForFail(_ msg: String = "请稍后再试") {
        let alert = UIAlertController(title: "发送失败", message: msg, preferredStyle: .alert)
        
        let ok = UIAlertAction(title: "确认", style: .default) { (action) in
            BILControllerManager.shared.showMainTabBarController()
        }
        alert.addAction(ok)
        
        present(alert, animated: true, completion: nil)
    }
    
    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    

    // MARK: - Navigation

    // In a storyboard-based application, you will often want to do a little preparation before navigation
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        // Get the new view controller using segue.destinationViewController.
        // Pass the selected object to the new view controller.
        
    }

}
