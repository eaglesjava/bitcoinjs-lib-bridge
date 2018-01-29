//
//  BILAboutUsViewController.swift
//  wallet-ios-core
//
//  Created by 仇弘扬 on 2017/12/26.
//  Copyright © 2017年 BitBill. All rights reserved.
//

import UIKit

class BILAboutUsViewController: BILBaseViewController {

    @IBOutlet weak var versionLabel: UILabel!
    
    let titles = [String.meAboutUs_agreement, String.meAboutUs_contactUs, "Feedback".bil_ui_localized]
	let subTitles = ["", "hi@bitbill.com", ""]
    let segues = ["BILAboutUsToAgreementSegue", "", "BILAboutUsToFeedbackSegue"]
    
    override func viewDidLoad() {
        super.viewDidLoad()

        // Do any additional setup after loading the view.
        if let version = Bundle.main.object(forInfoDictionaryKey:"CFBundleShortVersionString") as? String {
            versionLabel.text = version
        }
    }
	
	override func languageDidChanged() {
		super.languageDidChanged()
		title = "About us".bil_ui_localized
	}

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    
    @IBAction func emailAction(_ sender: Any?) {
        UIApplication.shared.open(URL(string: "mailto:hi@bitbill.com")!, options: [:], completionHandler: nil)
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

extension BILAboutUsViewController: UITableViewDelegate, UITableViewDataSource {
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return titles.count
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let cell = tableView.dequeueReusableCell(withIdentifier: "BILMeCell", for: indexPath)
        if let c = cell as? BILMeCell {
            c.titleLabel.text = titles[indexPath.row]
			c.subTitleLabel.text = subTitles[indexPath.row]
			c.subTitleLabel.textColor = UIColor.white
        }
        return cell
    }
    
    func tableView(_ tableView: UITableView, heightForRowAt indexPath: IndexPath) -> CGFloat {
        return 60
    }
    
    func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
        switch indexPath.row {
        case 1:
            emailAction(nil)
        default:
            let segue = segues[indexPath.row]
            guard !segue.isEmpty else { return }
            performSegue(withIdentifier: segue, sender: nil)
        }
    }
    
}
