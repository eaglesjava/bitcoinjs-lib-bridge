//
//  BILContactController.swift
//  wallet-ios-core
//
//  Created by 仇弘扬 on 2017/12/13.
//  Copyright © 2017年 BitBill. All rights reserved.
//

import UIKit

class BILContactController: BILLightBlueBaseController {

    @IBOutlet weak var tableView: UITableView!
    var contacts = [String: [Contact]]()
    var firstLetters = [String]()
    
    override func viewDidLoad() {
        super.viewDidLoad()

        // Do any additional setup after loading the view.
        tableView.register(UINib(nibName: "BILContactCell", bundle: nil), forCellReuseIdentifier: "BILContactCell")
        tableView.register(UINib(nibName: "BILTableViewHeaderFooterView", bundle: nil), forHeaderFooterViewReuseIdentifier: "BILTableViewHeaderFooterView")
        loadContacts()
    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }

    @IBAction func newContactAction(_ sender: Any) {
        let sheet = UIAlertController(title: nil, message: nil, preferredStyle: .actionSheet)
        sheet.addAction(UIAlertAction(title: "通过 ID 添加", style: .default, handler: { (action) in
            
        }))
        sheet.addAction(UIAlertAction(title: "通过 地址 添加", style: .default, handler: { (action) in
            
        }))
        sheet.addAction(UIAlertAction(title: "扫码添加", style: .default, handler: { (action) in
            
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

extension BILContactController {
    func loadContacts() {
        var datas = [Contact]()
        datas.append(Contact(name: "Asdf", walletID: "asdfjkrhgewkjrg", address: "143D88FBFCTVMM1iF9TWmrhPDjKK5aBAKK", additionType: .walletID))
        datas.append(Contact(name: "Asdf", walletID: "asdfjkrhgewkjrg", address: "143D88FBFCTVMM1iF9TWmrhPDjKK5aBAKK", additionType: .address))
        datas.append(Contact(name: "谷歌", walletID: "asdfjkrhgewkjrg", address: "143D88FBFCTVMM1iF9TWmrhPDjKK5aBAKK", additionType: .address))
        datas.append(Contact(name: "Asdf", walletID: "asdfjkrhgewkjrg", address: "143D88FBFCTVMM1iF9TWmrhPDjKK5aBAKK", additionType: .walletID))
        datas.append(Contact(name: "bsdf", walletID: "asdfjkrhgewkjrg", address: "143D88FBFCTVMM1iF9TWmrhPDjKK5aBAKK", additionType: .walletID))
        datas.append(Contact(name: "bsdf", walletID: "asdfjkrhgewkjrg", address: "143D88FBFCTVMM1iF9TWmrhPDjKK5aBAKK", additionType: .address))
        datas.append(Contact(name: "苹果", walletID: "asdfjkrhgewkjrg", address: "143D88FBFCTVMM1iF9TWmrhPDjKK5aBAKK", additionType: .walletID))
        datas.append(Contact(name: "bsdf", walletID: "asdfjkrhgewkjrg", address: "143D88FBFCTVMM1iF9TWmrhPDjKK5aBAKK", additionType: .address))
        datas.append(Contact(name: "iMac", walletID: "asdfjkrhgewkjrg", address: "143D88FBFCTVMM1iF9TWmrhPDjKK5aBAKK", additionType: .walletID))
        datas.append(Contact(name: "bsdf", walletID: "asdfjkrhgewkjrg", address: "143D88FBFCTVMM1iF9TWmrhPDjKK5aBAKK", additionType: .walletID))
        datas.append(Contact(name: "今天不想吃饭", walletID: "asdfjkrhgewkjrg", address: "143D88FBFCTVMM1iF9TWmrhPDjKK5aBAKK", additionType: .address))
        datas.append(Contact(name: "dsdf", walletID: "asdfjkrhgewkjrg", address: "143D88FBFCTVMM1iF9TWmrhPDjKK5aBAKK", additionType: .address))
        datas.append(Contact(name: "肉夹馍", walletID: "asdfjkrhgewkjrg", address: "143D88FBFCTVMM1iF9TWmrhPDjKK5aBAKK", additionType: .walletID))
        datas.append(Contact(name: "dsdf", walletID: "asdfjkrhgewkjrg", address: "143D88FBFCTVMM1iF9TWmrhPDjKK5aBAKK", additionType: .walletID))
        datas.append(Contact(name: "大吉大利", walletID: "asdfjkrhgewkjrg", address: "143D88FBFCTVMM1iF9TWmrhPDjKK5aBAKK", additionType: .address))
        datas.append(Contact(name: "gsdf", walletID: "asdfjkrhgewkjrg", address: "143D88FBFCTVMM1iF9TWmrhPDjKK5aBAKK", additionType: .walletID))
        datas.append(Contact(name: "凉皮", walletID: "asdfjkrhgewkjrg", address: "143D88FBFCTVMM1iF9TWmrhPDjKK5aBAKK", additionType: .walletID))
        datas.append(Contact(name: "gggsdf", walletID: "asdfjkrhgewkjrg", address: "143D88FBFCTVMM1iF9TWmrhPDjKK5aBAKK", additionType: .walletID))
        datas.append(Contact(name: "HHsdf", walletID: "asdfjkrhgewkjrg", address: "143D88FBFCTVMM1iF9TWmrhPDjKK5aBAKK", additionType: .address))
        datas.append(Contact(name: "iisdf", walletID: "asdfjkrhgewkjrg", address: "143D88FBFCTVMM1iF9TWmrhPDjKK5aBAKK", additionType: .walletID))
        datas.append(Contact(name: "jjjsdf", walletID: "asdfjkrhgewkjrg", address: "143D88FBFCTVMM1iF9TWmrhPDjKK5aBAKK", additionType: .address))
        datas.append(Contact(name: "油泼面", walletID: "asdfjkrhgewkjrg", address: "143D88FBFCTVMM1iF9TWmrhPDjKK5aBAKK", additionType: .walletID))
        datas.append(Contact(name: "jjjsdf", walletID: "asdfjkrhgewkjrg", address: "143D88FBFCTVMM1iF9TWmrhPDjKK5aBAKK", additionType: .walletID))
        datas.append(Contact(name: "kkksdf", walletID: "asdfjkrhgewkjrg", address: "143D88FBFCTVMM1iF9TWmrhPDjKK5aBAKK", additionType: .address))
        datas.append(Contact(name: "今晚吃鸡", walletID: "asdfjkrhgewkjrg", address: "143D88FBFCTVMM1iF9TWmrhPDjKK5aBAKK", additionType: .walletID))
        datas.append(Contact(name: "dghjsdf", walletID: "asdfjkrhgewkjrg", address: "143D88FBFCTVMM1iF9TWmrhPDjKK5aBAKK", additionType: .address))
        datas.append(Contact(name: "dfghsdf", walletID: "asdfjkrhgewkjrg", address: "143D88FBFCTVMM1iF9TWmrhPDjKK5aBAKK", additionType: .walletID))
        datas.append(Contact(name: "*sdf", walletID: "asdfjkrhgewkjrg", address: "143D88FBFCTVMM1iF9TWmrhPDjKK5aBAKK", additionType: .walletID))
        datas.append(Contact(name: "/sdf", walletID: "asdfjkrhgewkjrg", address: "143D88FBFCTVMM1iF9TWmrhPDjKK5aBAKK", additionType: .address))
        datas.append(Contact(name: "-sdf", walletID: "asdfjkrhgewkjrg", address: "143D88FBFCTVMM1iF9TWmrhPDjKK5aBAKK", additionType: .walletID))
        datas.append(Contact(name: "!@sdf", walletID: "asdfjkrhgewkjrg", address: "143D88FBFCTVMM1iF9TWmrhPDjKK5aBAKK", additionType: .walletID))
        datas.append(Contact(name: "bdrtbsdf", walletID: "asdfjkrhgewkjrg", address: "143D88FBFCTVMM1iF9TWmrhPDjKK5aBAKK", additionType: .address))
        datas.append(Contact(name: "e5tjy7sdf", walletID: "asdfjkrhgewkjrg", address: "143D88FBFCTVMM1iF9TWmrhPDjKK5aBAKK", additionType: .walletID))
        datas.append(Contact(name: "vbmsdf", walletID: "asdfjkrhgewkjrg", address: "143D88FBFCTVMM1iF9TWmrhPDjKK5aBAKK", additionType: .address))
        datas.append(Contact(name: "xzcvbsdf", walletID: "asdfjkrhgewkjrg", address: "143D88FBFCTVMM1iF9TWmrhPDjKK5aBAKK", additionType: .walletID))
        datas.append(Contact(name: "cvbnsdf", walletID: "asdfjkrhgewkjrg", address: "143D88FBFCTVMM1iF9TWmrhPDjKK5aBAKK", additionType: .address))
        datas.append(Contact(name: "cmnsdf", walletID: "asdfjkrhgewkjrg", address: "143D88FBFCTVMM1iF9TWmrhPDjKK5aBAKK", additionType: .walletID))
        datas.append(Contact(name: "gh,jusdf", walletID: "asdfjkrhgewkjrg", address: "143D88FBFCTVMM1iF9TWmrhPDjKK5aBAKK", additionType: .walletID))
        datas.append(Contact(name: "vbmnsdf", walletID: "asdfjkrhgewkjrg", address: "143D88FBFCTVMM1iF9TWmrhPDjKK5aBAKK", additionType: .address))
        datas.append(Contact(name: "xcvbsdf", walletID: "asdfjkrhgewkjrg", address: "143D88FBFCTVMM1iF9TWmrhPDjKK5aBAKK", additionType: .address))
        datas.append(Contact(name: "rtnyrtsdf", walletID: "asdfjkrhgewkjrg", address: "143D88FBFCTVMM1iF9TWmrhPDjKK5aBAKK", additionType: .address))
        datas.append(Contact(name: "cvbndrsdf", walletID: "asdfjkrhgewkjrg", address: "143D88FBFCTVMM1iF9TWmrhPDjKK5aBAKK", additionType: .walletID))
        datas.append(Contact(name: "reyjsdf", walletID: "asdfjkrhgewkjrg", address: "143D88FBFCTVMM1iF9TWmrhPDjKK5aBAKK", additionType: .walletID))
        datas.append(Contact(name: "iurtuisdf", walletID: "asdfjkrhgewkjrg", address: "143D88FBFCTVMM1iF9TWmrhPDjKK5aBAKK", additionType: .walletID))
        datas.append(Contact(name: "rtykjrtmsdf", walletID: "asdfjkrhgewkjrg", address: "143D88FBFCTVMM1iF9TWmrhPDjKK5aBAKK", additionType: .address))
        datas.append(Contact(name: "mrtmsdf", walletID: "asdfjkrhgewkjrg", address: "143D88FBFCTVMM1iF9TWmrhPDjKK5aBAKK", additionType: .walletID))
        datas.append(Contact(name: "阿猫", walletID: "asdfjkrhgewkjrg", address: "143D88FBFCTVMM1iF9TWmrhPDjKK5aBAKK", additionType: .walletID))
        datas.append(Contact(name: "大咪", walletID: "asdfjkrhgewkjrg", address: "143D88FBFCTVMM1iF9TWmrhPDjKK5aBAKK", additionType: .address))
        datas.append(Contact(name: "虎子", walletID: "asdfjkrhgewkjrg", address: "143D88FBFCTVMM1iF9TWmrhPDjKK5aBAKK", additionType: .walletID))
        datas.append(Contact(name: "小橘子", walletID: "asdfjkrhgewkjrg", address: "143D88FBFCTVMM1iF9TWmrhPDjKK5aBAKK", additionType: .walletID))
        datas.append(Contact(name: "咪宝", walletID: "asdfjkrhgewkjrg", address: "143D88FBFCTVMM1iF9TWmrhPDjKK5aBAKK", additionType: .address))
        datas.append(Contact(name: "豆浆", walletID: "asdfjkrhgewkjrg", address: "143D88FBFCTVMM1iF9TWmrhPDjKK5aBAKK", additionType: .walletID))
        datas.append(Contact(name: "油条", walletID: "asdfjkrhgewkjrg", address: "143D88FBFCTVMM1iF9TWmrhPDjKK5aBAKK", additionType: .address))
        datas.append(Contact(name: "饭团", walletID: "asdfjkrhgewkjrg", address: "143D88FBFCTVMM1iF9TWmrhPDjKK5aBAKK", additionType: .address))
        datas.append(Contact(name: "花卷", walletID: "asdfjkrhgewkjrg", address: "143D88FBFCTVMM1iF9TWmrhPDjKK5aBAKK", additionType: .walletID))
        handleContacts(datas: datas)
    }
    
    func handleContacts(datas: [Contact]) {
        
        for contact in datas {
            let firstLetter = contact.firstNameLetter
            debugPrint("\(firstLetter), \(contact.name)")
            if !firstLetters.contains(firstLetter) {
                firstLetters.append(firstLetter)
            }
            var array = contacts[firstLetter]
            if array == nil {
                array = [Contact]()
            }
            array?.append(contact)
            contacts[firstLetter] = array
        }
        for letter in firstLetters {
            contacts[letter] = contacts[letter]?.sorted()
        }
        firstLetters = firstLetters.sorted()
        tableView.reloadData()
    }
    
    func letter(of section: Int) -> String {
        return firstLetters[section]
    }
}

extension BILContactController {
    
}

extension BILContactController: UITableViewDelegate, UITableViewDataSource {
    
    func scrollViewDidScroll(_ scrollView: UIScrollView) {
        guard let section = tableView.indexPathsForVisibleRows?.first?.section else { return }
        for i in 0...numberOfSections(in: tableView) {
            guard let header = tableView.headerView(forSection: i) as? BILTableViewHeaderFooterView  else { continue }
            if i == section {
                let headerRect = view.convert(header.frame, from: tableView)
                
                header.bgImageView.image = backgroundImage?.snapshotSubImage(rect: headerRect)
            }
            else
            {
                header.bgImageView.image = nil
            }
        }
    }
    // MARK: - TableView
    func numberOfSections(in tableView: UITableView) -> Int {
        return firstLetters.count
    }
    
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return contacts[letter(of: section)]?.count ?? 0
    }
    
    func sectionIndexTitles(for tableView: UITableView) -> [String]? {
        return firstLetters
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let cell = tableView.dequeueReusableCell(withIdentifier: "BILContactCell", for: indexPath) as! BILContactCell
        cell.contact = contacts[letter(of: indexPath.section)]![indexPath.row]
        return cell
    }
    
    func tableView(_ tableView: UITableView, viewForHeaderInSection section: Int) -> UIView? {
        let headerView = tableView.dequeueReusableHeaderFooterView(withIdentifier: "BILTableViewHeaderFooterView") as! BILTableViewHeaderFooterView
        headerView.titleLabel.text = letter(of: section)
        headerView.bgImageView.image = backgroundImage?.snapshotSubImage(rect: view.convert(headerView.frame, from: tableView))
        return headerView
    }
    
    func tableView(_ tableView: UITableView, heightForHeaderInSection section: Int) -> CGFloat {
        return 32
    }
    
    func tableView(_ tableView: UITableView, heightForRowAt indexPath: IndexPath) -> CGFloat {
        return 72
    }
}
