//
//  BILContactController.swift
//  wallet-ios-core
//
//  Created by 仇弘扬 on 2017/12/13.
//  Copyright © 2017年 BitBill. All rights reserved.
//

import UIKit

extension String {
    static var bil_contactsToAddByIDSegue: String { return "BILContactsToAddByIDSegue" }
    static var bil_showContactDetailSegue: String { return "BILShowContactDetailSegue" }
    static var bil_contactsToAddByAddressSegue: String { return "bil_contactsToAddByAddressSegue" }
    static var bil_contactsToResultSegue: String { return "BILContactsToResultSegue" }
}

class BILContactController: BILLightBlueBaseController {

    typealias DidSelectContactClosure = (ContactModel) -> Void
    
    @IBOutlet weak var tableView: UITableView!
    var contacts = [String: [ContactModel]]()
    var firstLetters = [String]()
    var showTableViewIndexes: Bool = false
    
    var didSelectContactClosure: DidSelectContactClosure?
    
    @IBOutlet weak var emptyLabel: UILabel!
    @IBOutlet weak var emptyView: UIView!
    @IBOutlet weak var newItem: UIBarButtonItem!
    @IBOutlet weak var newButton: BILWhiteBorderButton!
    
    override func viewDidLoad() {
        super.viewDidLoad()

        // Do any additional setup after loading the view.
        tableView.register(UINib(nibName: "BILContactCell", bundle: nil), forCellReuseIdentifier: "BILContactCell")
        tableView.register(UINib(nibName: "BILTableViewHeaderFooterView", bundle: nil), forHeaderFooterViewReuseIdentifier: "BILTableViewHeaderFooterView")
        
        NotificationCenter.default.addObserver(self, selector: #selector(loadContacts), name: .contactDidChanged, object: nil)
        
        firstLetters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ#".map{ String($0) }
        loadContacts()
    }
    
    override func languageDidChanged() {
        super.languageDidChanged()
        title = "Contacts".bil_ui_localized
        emptyLabel.text = "No contacts".bil_ui_localized
        newItem.title = "Add".bil_ui_localized
        newButton.setTitle("Add now".bil_ui_localized, for: .normal)
        tableView.reloadData()
    }
    
    override func viewDidAppear(_ animated: Bool) {
        super.viewDidAppear(animated)
        NotificationCenter.default.addObserver(self, selector: #selector(newContactAction(_:)), name: .shortcutAddContact, object: nil)
    }
    
    override func viewDidDisappear(_ animated: Bool) {
        super.viewDidDisappear(animated)
        NotificationCenter.default.removeObserver(self, name: .shortcutAddContact, object: nil)
    }
    
    deinit {
        NotificationCenter.default.removeObserver(self, name: .contactDidChanged, object: nil)
    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }

    @objc
    @IBAction func newContactAction(_ sender: Any) {
        let sheet = UIAlertController(title: nil, message: nil, preferredStyle: .actionSheet)
        sheet.addAction(UIAlertAction(title: .contact_contact_addByID, style: .default, handler: { (action) in
            self.performSegue(withIdentifier: .bil_contactsToAddByIDSegue, sender: sender)
        }))
        sheet.addAction(UIAlertAction(title: .contact_contact_addByAddress, style: .default, handler: { (action) in
            self.performSegue(withIdentifier: .bil_contactsToAddByAddressSegue, sender: sender)
        }))
        sheet.addAction(UIAlertAction(title: .contact_contact_scanCode, style: .default, handler: { (action) in
            self.addContactByScanQRCode()
        }))
        
        sheet.addAction(UIAlertAction(title: .contact_contact_cancel, style: .cancel, handler: { (action) in
            
        }))
        present(sheet, animated: true, completion: nil)
    }
    
    func addContactByScanQRCode() {
        unowned let unownedSelf = self
        let cont = BILQRCodeScanViewController.controller { (qrString) in
            if let result = BILURLHelper.transferContactURL(urlString: qrString) {
                unownedSelf.navigationController?.popViewController(animated: true)
                unownedSelf.checkID(id: result)
                return
            }
            if let address = BILURLHelper.transferBitCoinURL(urlString: qrString)?.address {
                debugPrint(address)
                unownedSelf.navigationController?.popViewController(animated: true)
                guard !ContactModel.isAddressExits(address: address) else {
                    unownedSelf.bil_makeToast(msg: .contact_contact_addressExits)
                    return
                }
                self.bil_showLoading()
                BitcoinJSBridge.shared.validateAddress(address: address, success: { (result) in
                    let isValidate = result as! Bool
                    if isValidate {
                        DispatchQueue.main.asyncAfter(deadline: .now() + DispatchTimeInterval.milliseconds(350), execute: {
                            self.bil_dismissHUD()
                            unownedSelf.performSegue(withIdentifier: .bil_contactsToAddByAddressSegue, sender: address)
                        })
                    } else {
                        unownedSelf.showTipAlert(msg: .contact_contact_addressInvalid)
                        self.bil_dismissHUD()
                    }
                }, failure: { (error) in
                    unownedSelf.showTipAlert(msg: error.localizedDescription)
                })
            }
        }
        show(cont, sender: nil)
    }
    
    func checkID(id: String) {
        guard !ContactModel.isWalletIDExits(walletID: id) else {
            bil_makeToast(msg: "ID 已存在")
            return
        }
        self.bil_showLoading()
        ContactModel.getContactFromServer(by: id, success: { (id) in
            DispatchQueue.main.asyncAfter(deadline: .now() + DispatchTimeInterval.milliseconds(350), execute: {
                self.bil_dismissHUD()
                self.showResult(id: id)
            })
        }) { (msg, code) in
            self.bil_makeToast(msg: msg)
            self.bil_dismissHUD()
        }
    }
    
    func showResult(id: String) {
        performSegue(withIdentifier: .bil_contactsToResultSegue, sender: id)
    }
    
    // MARK: - Navigation

    override func shouldPerformSegue(withIdentifier identifier: String, sender: Any?) -> Bool {
        if identifier == .bil_showContactDetailSegue {
            guard (sender as? ContactModel) != nil else { return false }
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
            if let contact = sender as? ContactModel {
                cont.contact = contact
            }
        case String.bil_contactsToResultSegue:
            guard let walletID = sender as? String else { return }
            let cont = segue.destination as! BILSearchWalletIDResultController
            cont.walletID = walletID
        case String.bil_contactsToAddByAddressSegue:
            guard let address = sender as? String else { return }
            let cont = segue.destination as! BILAddContactByAddressController
            cont.address = address
            
        default:
            ()
        }
    }

}

extension BILContactController {

    @objc
    func loadContacts() {
        let models = bil_contactManager.models
        let isEmpty = models.count == 0
        tableView.isHidden = isEmpty
        emptyView.isHidden = !isEmpty
        handleContacts(datas: models)
        tableView.reloadData()
    }
    
    func handleContacts(datas: [ContactModel]) {
        contacts.removeAll()
        for contact in datas {
            let firstLetter = contact.firstNameLetter
            debugPrint("\(firstLetter), \(contact.name ?? "")")
            var array = contacts[firstLetter]
            if array == nil {
                array = [ContactModel]()
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
//        cell.seletedButton.isHidden = didSelectContactClosure == nil
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
