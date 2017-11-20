//
//  BILWelcomeViewController.swift
//  wallet-ios-core
//
//  Created by 仇弘扬 on 2017/11/13.
//  Copyright © 2017年 BitBill. All rights reserved.
//

import UIKit

class BILWelcomeViewController: BILBaseViewController, UIScrollViewDelegate {

	@IBOutlet weak var newWalletButton: BILGradientButton!
	@IBOutlet weak var guideScrollView: UIScrollView!
	@IBOutlet weak var pageControl: UIPageControl!
	
	var guideViews = [BILGuideBaseView]()
	
	override func viewDidLoad() {
        super.viewDidLoad()

        // Do any additional setup after loading the view.
		let allInOneView = Bundle.main.loadNibNamed("BILAllInOneView", owner: nil, options: nil)?.first as! BILAllInOneView
		let keyView = Bundle.main.loadNibNamed("BILGuideKeyView", owner: nil, options: nil)?.first as! BILGuideKeyView
		guideScrollView.addSubview(allInOneView)
		guideScrollView.addSubview(keyView)
		guideViews.append(contentsOf: [allInOneView, keyView])
    }
	
	override func viewDidLayoutSubviews() {
		super.viewDidLayoutSubviews()
		
		let frame = guideScrollView.bounds
		for gView in guideViews {
			gView.adjust(frame: frame, index: guideViews.index(of: gView)!)
		}
		
		guideScrollView.contentSize = CGSize(width: frame.width * CGFloat(guideViews.count), height: frame.height)
		layoutGuideView()
	}
	
	func scrollViewDidScroll(_ scrollView: UIScrollView) {
		layoutGuideView()
	}
	
	func scrollViewDidEndDecelerating(_ scrollView: UIScrollView) {
		let index = Int(scrollView.contentOffset.x) / Int(scrollView.frame.width)
		pageControl.currentPage = index
	}
	
	func layoutGuideView() {
		for gView in guideViews {
			gView.adjust(contentOffset: guideScrollView.contentOffset)
		}
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
		if segue.identifier == "BILRecoverWallet" { // For test
			let vc = segue.destination as! BILCreateWalletViewController
			vc.mnemonic = "Haha"
		}
    }

}
