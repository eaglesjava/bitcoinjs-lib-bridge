//
//  BILAppSettingController.swift
//  wallet-ios-core
//
//  Created by 仇弘扬 on 2018/1/3.
//  Copyright © 2018年 BitBill. All rights reserved.
//

import UIKit

class BILAppSettingController: BILBaseViewController, UITableViewDelegate, UITableViewDataSource {

    enum BILSettingSectionType: Int {
        case sound = 0
        case currency
        
        var sectionTitle: String {
            switch self {
            case .sound:
                return "音效"
            case .currency:
                return "货币"
            }
        }
        
        var sectionViewHeight: CGFloat {
            if numberOfRows() == 0 {
                return 0
            }
            return 36
        }
        
        var rowHeight: CGFloat {
            return 60
        }
        
        func numberOfRows() -> Int {
            return dataArray().count
        }
        
        func dataArray() -> [Any] {
            switch self {
            case .sound:
                return ["音效"]
            case .currency:
                return ["当前币种"]
            }
        }
        
        func cellID() -> String {
            switch self {
            case .sound:
                return "BILMeSwitchCell"
            default:
                return "BILMeCell"
            }
        }
    }
    
    var sections: [BILSettingSectionType] = [.sound, .currency]
    
    override func viewDidLoad() {
        super.viewDidLoad()

        // Do any additional setup after loading the view.
    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    
    func numberOfSections(in tableView: UITableView) -> Int {
        return sections.count
    }
    
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return sections[section].dataArray().count
    }
    
    func tableView(_ tableView: UITableView, heightForRowAt indexPath: IndexPath) -> CGFloat {
        let sectionType = sections[indexPath.section]
        return sectionType.rowHeight
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let sectionType = sections[indexPath.section]
        let cell = tableView.dequeueReusableCell(withIdentifier: sectionType.cellID(), for: indexPath)
        let model = sectionType.dataArray()[indexPath.row]
        switch sectionType {
        case .sound:
            let c = cell as! BILMeSwitchCell
            c.bil_switch.isOn = BILSettingManager.isSoundEnabled
            c.bil_switch.onTintColor = UIColor(patternImage: UIImage(named: "pic_switch_background")!)
            c.switchChangedClosure = { (isOn) in
                BILSettingManager.isSoundEnabled = isOn
            }
            c.titleLabel.text = model as? String
        case .currency:
            let c = cell as! BILMeCell
            c.titleLabel.text = model as? String
			c.subTitleLabel.text = BILSettingManager.currencyType.localizedName
			c.subTitleLabel.textColor = UIColor(white: 1.0, alpha: 0.6)
        }
        
        return cell
    }
    
    func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
        let sectionType = sections[indexPath.section]
        switch sectionType {
        case .currency:
            chooseCurrencyType()
        default:
            ()
        }
    }
    
    func chooseCurrencyType() {
        let sheet = UIAlertController(title: nil, message: nil, preferredStyle: .actionSheet)
        sheet.addAction(UIAlertAction(title: "CNY", style: .default, handler: { (action) in
            BILSettingManager.currencyType = .cny
            self.bil_makeToast(msg: "已保存")
        }))
        sheet.addAction(UIAlertAction(title: "USD", style: .default, handler: { (action) in
            BILSettingManager.currencyType = .usd
            self.bil_makeToast(msg: "已保存")
        }))
        
        sheet.addAction(UIAlertAction(title: "取消", style: .cancel, handler: { (action) in
            
        }))
        present(sheet, animated: true, completion: nil)
    }

    /*
    // MARK: - Navigation

    // In a storyboard-based application, you will often want to do a little preparation before navigation
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        // Get the new view controller using segue.destinationViewController.
        // Pass the selected object to the new view controller.
    }
    */

}
