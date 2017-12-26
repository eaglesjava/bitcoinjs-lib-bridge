//
//  BILContactController.swift
//  wallet-ios-core
//
//  Created by 仇弘扬 on 2017/12/13.
//  Copyright © 2017年 BitBill. All rights reserved.
//

import UIKit
import DZNEmptyDataSet
import SVProgressHUD

extension String {
    static var bil_contactsToAddByIDSegue: String { return "BILContactsToAddByIDSegue" }
    static var bil_showContactDetailSegue: String { return "BILShowContactDetailSegue" }
    static var bil_contactsToAddByAddressSegue: String { return "bil_contactsToAddByAddressSegue" }
	static var bil_contactsToResultSegue: String { return "BILContactsToResultSegue" }
}

class BILContactController: BILLightBlueBaseController {

    typealias DidSelectContactClosure = (Contact) -> Void
    
    @IBOutlet weak var tableView: UITableView!
    var contacts = [String: [Contact]]()
    var firstLetters = [String]()
    var showTableViewIndexes: Bool = false
    
    var didSelectContactClosure: DidSelectContactClosure?
    
    var emptyTitle: String?
    var emptyDescription: String?
    
    override func viewDidLoad() {
        super.viewDidLoad()

        // Do any additional setup after loading the view.
        tableView.register(UINib(nibName: "BILContactCell", bundle: nil), forCellReuseIdentifier: "BILContactCell")
        tableView.register(UINib(nibName: "BILTableViewHeaderFooterView", bundle: nil), forHeaderFooterViewReuseIdentifier: "BILTableViewHeaderFooterView")
        tableView.emptyDataSetSource = self
        loadContacts()
        
        setupRefresh()
        
        NotificationCenter.default.addObserver(self, selector: #selector(loadContacts), name: .contactDidChanged, object: nil)
        
        firstLetters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ#".map{ String($0) }
    }
    
    override func viewDidLayoutSubviews() {
        super.viewDidLayoutSubviews()
        navigationController?.navigationBar.setBackgroundImage(backgroundImage, for: .any, barMetrics: .default)
    }
    
    func setupRefresh() {
        let refreshControl = UIRefreshControl()
        refreshControl.addTarget(self, action: #selector(loadContacts), for: .valueChanged)
        refreshControl.tintColor = UIColor.white
        tableView.refreshControl = refreshControl
    }
    
    deinit {
        NotificationCenter.default.removeObserver(self, name: .contactDidChanged, object: nil)
    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }

    @IBAction func newContactAction(_ sender: Any) {
        let sheet = UIAlertController(title: nil, message: nil, preferredStyle: .actionSheet)
        sheet.addAction(UIAlertAction(title: "通过 ID 添加", style: .default, handler: { (action) in
            self.performSegue(withIdentifier: .bil_contactsToAddByIDSegue, sender: sender)
        }))
        sheet.addAction(UIAlertAction(title: "通过 地址 添加", style: .default, handler: { (action) in
            self.performSegue(withIdentifier: .bil_contactsToAddByAddressSegue, sender: sender)
        }))
        sheet.addAction(UIAlertAction(title: "扫码添加", style: .default, handler: { (action) in
			self.addContactByScanQRCode()
        }))
        
        sheet.addAction(UIAlertAction(title: "取消", style: .cancel, handler: { (action) in
            
        }))
        present(sheet, animated: true, completion: nil)
    }
	
	func addContactByScanQRCode() {
		unowned let unownedSelf = self
		let cont = BILQRCodeScanViewController.controller { (qrString) in
			if let result = BILURLHelper.transferContactURL(urlString: qrString) {
				unownedSelf.navigationController?.popViewController(animated: true)
				unownedSelf.checkID(id: result)
			}
		}
		show(cont, sender: nil)
	}
	
	func checkID(id: String) {
		SVProgressHUD.show()
		Contact.getContactFromServer(by: id, success: { (id) in
			DispatchQueue.main.asyncAfter(deadline: .now() + DispatchTimeInterval.milliseconds(350), execute: {
				SVProgressHUD.dismiss()
				self.showResult(id: id)
			})
		}) { (msg, code) in
			self.bil_makeToast(msg: msg)
			SVProgressHUD.dismiss()
		}
		
	}
	
	func showResult(id: String) {
		performSegue(withIdentifier: .bil_contactsToResultSegue, sender: id)
	}
	
    // MARK: - Navigation

    override func shouldPerformSegue(withIdentifier identifier: String, sender: Any?) -> Bool {
        if identifier == .bil_showContactDetailSegue {
            guard (sender as? Contact) != nil else { return false }
        }
        return true
    }
    
    // In a storyboard-based application, you will often want to do a little preparation before navigation
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        // Get the new view controller using segue.destinationViewController.
        // Pass the selected object to the new view controller.
        guard let id = segue.identifier else {
            return
        }
        switch id {
        case String.bil_showContactDetailSegue:
            let cont = segue.destination as! BILContactDetailController
            if let contact = sender as? Contact {
                cont.contact = contact
            }
		case String.bil_contactsToResultSegue:
			guard let walletID = sender as? String else { return }
			let cont = segue.destination as! BILSearchWalletIDResultController
			cont.walletID = walletID
        default:
            ()
        }
    }

}

extension BILContactController {
    @objc
    func loadContacts() {
        func loadEnd() {
            self.tableView.reloadData()
        }
        
        Contact.getContactsFromServer(success: { (contacts) in
            self.handleContacts(datas: contacts)
            loadEnd()
        }) { (msg, code) in
            self.emptyTitle = "获取联系人失败"
            self.emptyDescription = "请检查您的网络，或稍后再试"
            self.bil_makeToast(msg: msg)
            loadEnd()
        }
    }
    
    func handleContacts(datas: [Contact]) {
        contacts.removeAll()
        for contact in datas {
            let firstLetter = contact.firstNameLetter
            debugPrint("\(firstLetter), \(contact.name)")
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
        showTableViewIndexes = datas.count > 0
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
    
    func scrollViewDidEndDragging(_ scrollView: UIScrollView, willDecelerate decelerate: Bool) {
        self.tableView.refreshControl?.endRefreshing()
    }
    
    // MARK: - TableView
    func numberOfSections(in tableView: UITableView) -> Int {
        return firstLetters.count
    }
    
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return contacts[letter(of: section)]?.count ?? 0
    }
    
    func sectionIndexTitles(for tableView: UITableView) -> [String]? {
        return showTableViewIndexes ? firstLetters : nil
    }
    
    func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
        guard let contact = contacts[letter(of: indexPath.section)]?[indexPath.row] else { return  }
        if let closure = didSelectContactClosure {
            closure(contact)
            didSelectContactClosure = nil
            navigationController?.popViewController(animated: true)
        }
        else
        {
            performSegue(withIdentifier: .bil_showContactDetailSegue, sender: contact)
        }
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let cell = tableView.dequeueReusableCell(withIdentifier: "BILContactCell", for: indexPath) as! BILContactCell
        cell.contact = contacts[letter(of: indexPath.section)]![indexPath.row]
        cell.seletedButton.isHidden = didSelectContactClosure == nil
        return cell
    }
    
    func tableView(_ tableView: UITableView, viewForHeaderInSection section: Int) -> UIView? {
        guard let count = contacts[letter(of: section)]?.count, count > 0 else {
            return nil
        }
        let headerView = tableView.dequeueReusableHeaderFooterView(withIdentifier: "BILTableViewHeaderFooterView") as! BILTableViewHeaderFooterView
        headerView.titleLabel.text = letter(of: section)
        headerView.bgImageView.image = backgroundImage?.snapshotSubImage(rect: view.convert(headerView.frame, from: tableView))
        return headerView
    }
    
    func tableView(_ tableView: UITableView, heightForHeaderInSection section: Int) -> CGFloat {
        guard let count = contacts[letter(of: section)]?.count, count > 0 else {
            return 0.0
        }
        return 32.0
    }
    
    func tableView(_ tableView: UITableView, heightForRowAt indexPath: IndexPath) -> CGFloat {
        return 72.0
    }
}

extension BILContactController: DZNEmptyDataSetSource {
    func title(forEmptyDataSet scrollView: UIScrollView!) -> NSAttributedString! {
        return NSAttributedString(string: emptyTitle ?? "您还没有联系人", attributes: [.foregroundColor : UIColor.white, .font: UIFont.systemFont(ofSize: 18)])
    }
    
    func description(forEmptyDataSet scrollView: UIScrollView!) -> NSAttributedString! {
        return NSAttributedString(string: emptyDescription ?? "可以点击右上角添加联系人", attributes: [.foregroundColor : UIColor.white, .font: UIFont.systemFont(ofSize: 14)])
    }
}
