//
//  BILPopMenu.swift
//  wallet-ios-core
//
//  Created by 仇弘扬 on 2017/12/6.
//  Copyright © 2017年 BitBill. All rights reserved.
//

import UIKit

class BILPopMenu: UIView, BILPopMenuItemViewDelegate {
    
    typealias BILPopMenuDismissedClosure = () -> Void
    
    enum LayoutDirection {
        case top
        case bottom
    }
    
    var layoutDirection = LayoutDirection.top
    
    let itemSpace = 10
    let itemViewHeight = 40
    let itemViewWidth = 100
    let containerBottomSpace = 20
    
    let animateSpace: CGFloat = 20
    
    var dismissedClosure: BILPopMenuDismissedClosure?
    var willDismissClosure: BILPopMenuDismissedClosure?
    
    var containerView = UIView()
    
    var items = [BILPopMenuItem]()
    
    override init(frame: CGRect) {
        super.init(frame: frame)
        let tap = UITapGestureRecognizer(target: self, action: #selector(tapped(sender:)))
        addGestureRecognizer(tap)
    }
    
    required init?(coder aDecoder: NSCoder) {
        fatalError("init(coder:) has not been implemented")
    }
    
    var containerHeight: Int {
        get { return itemViewHeight * items.count + itemSpace * (items.count - 1) }
    }
    
    @objc
    func tapped(sender: UITapGestureRecognizer) {
        switch sender.state {
        case .ended:
            dismissAnimate {
                
            }
        default:
            ()
        }
    }
    
    func itemDidTapped(itemView: BILPopMenuItemView) {
        if let item = itemView.item {
            dismissAnimate {
                item.tappedClosure?()
            }
        }
    }
    
    func addItem(item: BILPopMenuItem) {
        items.append(item)
    }
    
    func addItems(items: BILPopMenuItem ...) {
        self.items.append(contentsOf: items)
    }
    
    func show(in view: UIView, focusPoint: CGPoint, willDismiss: @escaping BILPopMenuDismissedClosure, dismissed: @escaping BILPopMenuDismissedClosure = { () -> Void in }) {
        willDismissClosure = willDismiss
        dismissedClosure = dismissed
        frame = view.bounds
        view.addSubview(self)
        if focusPoint.y - CGFloat(containerHeight) < 0 {
            layoutDirection = .bottom
        }
        setupItemViews(focusPoint: focusPoint)
        showAnimate()
    }
    
    func setupItemViews(focusPoint: CGPoint) {
        let count = items.count
        for index in 0..<count {
            let item = items[index]
            if let view = Bundle.main.loadNibNamed("BILPopMenuItemView", owner: nil, options: nil)?.first as? BILPopMenuItemView {
                view.titleLabel.text = item.title
                view.frame = CGRect(x: 0, y: (itemViewHeight + itemSpace) * index, width: itemViewWidth, height: itemViewHeight)
                view.delegate = self
                view.item = item
                containerView.addSubview(view)
            }
        }
        let containerHeight = self.containerHeight
        
        let y = Int(focusPoint.y) - (layoutDirection == .top ? containerHeight : 0) - (layoutDirection == .top ? containerBottomSpace : -containerBottomSpace)
        
        containerView.frame = CGRect(x: Int(focusPoint.x) - itemViewWidth, y: y, width: itemViewWidth, height: containerHeight)
        if containerView.superview == nil {
            addSubview(containerView)
        }
    }
    
    func showAnimate() {
        containerView.alpha = 0
        let frame = containerView.frame
        containerView.frame.origin.y += (layoutDirection == .top ? animateSpace : -animateSpace)
        UIView.animate(withDuration: 0.35) {
            self.containerView.alpha = 1
            self.containerView.frame = frame
        }
    }
    func dismissAnimate(complete: @escaping BILPopMenuDismissedClosure) {
        
        if let willDismiss = willDismissClosure {
            willDismiss()
        }
        
        var frame = containerView.frame
        frame.origin.y += (layoutDirection == .top ? animateSpace : -animateSpace)
        UIView.animate(withDuration: 0.35, animations: {
            self.containerView.alpha = 0
            self.containerView.frame = frame
        }) { (finished) in
            let itemViews = self.containerView.subviews
            for _ in 0..<itemViews.count {
                itemViews.last?.removeFromSuperview()
            }
            self.dismissedClosure = nil
            self.removeFromSuperview()
            
            self.dismissedClosure?()
            complete()
        }
    }
}
