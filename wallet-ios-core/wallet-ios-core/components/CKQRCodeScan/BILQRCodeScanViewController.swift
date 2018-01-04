//
//  BILQRCodeScanViewController.swift
//  wallet-ios-core
//
//  Created by 仇弘扬 on 2017/12/11.
//  Copyright © 2017年 BitBill. All rights reserved.
//

import UIKit
import AVFoundation

class BILQRCodeScanViewController: BILBaseViewController {
    
    fileprivate let scanKey = "QRCodeScan"
    fileprivate let scanDuration = 3.0
    
    typealias BILScanResultClosure = (String) -> Void
    
    var resultClosure: BILScanResultClosure?
    
    // MARK: - AV
    var scanSession: AVCaptureSession?
    var previewLayer: AVCaptureVideoPreviewLayer?
    var output: AVCaptureMetadataOutput?

    @IBOutlet weak var containerView: UIView!
    @IBOutlet weak var scanFrame: UIImageView!
    let scanLine = UIImageView(image: UIImage(named: "pic_scan_qr_line"))
    static func controller(complete: @escaping BILScanResultClosure) -> BILQRCodeScanViewController {
        let cont = BILQRCodeScanViewController(nibName: "BILQRCodeScanViewController", bundle: nil)
        cont.hidesBottomBarWhenPushed = true
        cont.resultClosure = complete
        return cont
    }
    
    override func viewDidLoad() {
        super.viewDidLoad()

        // Do any additional setup after loading the view.
        view.layoutIfNeeded()
        title = NSLocalizedString("扫一扫", comment: "")
        if #available(iOS 11.0, *) {
            navigationItem.largeTitleDisplayMode = .never
        }
        
        scanFrame.addSubview(scanLine)
    }
    
    override func viewDidAppear(_ animated: Bool) {
        super.viewDidAppear(animated)
		DispatchQueue.main.async {
			self.setupSession()
			self.scanSession?.startRunning()
			self.startScanAnimation()
		}
    }
    
    override func viewWillAppear(_ animated: Bool) {
        super.viewWillAppear(animated)
    }
    
    override func viewDidDisappear(_ animated: Bool) {
        super.viewDidDisappear(animated)
        scanSession?.stopRunning()
    }
    
    override func viewDidLayoutSubviews() {
        super.viewDidLayoutSubviews()
        setupLayers()
    }
    
    func setupLayers() {
        let maskLayer = CALayer()
        var frame = UIScreen.main.bounds
        frame.size.height += 200
        maskLayer.frame = frame
        maskLayer.backgroundColor = UIColor.bil_black_color(alpha: 0.5).cgColor
        
        debugPrint(scanFrame.frame)
        
        var scanFrameRect = scanFrame.frame
        scanFrameRect.origin.x = UIScreen.main.bounds.width / 2 - scanFrameRect.width / 2
        scanFrameRect = scanFrameRect.insetBy(dx: 0.5, dy: 0.5)
        
        let maskShapeLayer = CAShapeLayer()
        let path = UIBezierPath(rect: scanFrameRect)
        let p = UIBezierPath(rect: maskLayer.bounds)
        p.append(path.reversing())
        maskShapeLayer.path = p.cgPath
        maskLayer.mask = maskShapeLayer
        
        containerView.layer.insertSublayer(maskLayer, above: previewLayer)
        previewLayer?.frame = UIScreen.main.bounds
    }
    
    func setupSession() {
        guard scanSession == nil else {
            return
        }
        do {
            guard let device = AVCaptureDevice.default(for: .video) else { return }
            
            let input = try AVCaptureDeviceInput(device: device)
            
            let output = AVCaptureMetadataOutput()
            output.setMetadataObjectsDelegate(self, queue: .main)
            
            let scanSession = AVCaptureSession()
            if scanSession.canSetSessionPreset(.high) {
                
            }
            if scanSession.canAddInput(input) {
                scanSession.addInput(input)
            }
            if scanSession.canAddOutput(output) {
                scanSession.addOutput(output)
            }
            
            output.metadataObjectTypes = [.qr]
            
            let previewLayer = AVCaptureVideoPreviewLayer(session: scanSession)
            previewLayer.videoGravity = .resizeAspectFill
            containerView.layer.insertSublayer(previewLayer, at: 0)
            previewLayer.frame = containerView.bounds
            
            NotificationCenter.default.addObserver(self, selector: #selector(captureInputPortFormatDescriptionDidChange(notification:)), name: .AVCaptureInputPortFormatDescriptionDidChange, object: nil)
            
            self.scanSession = scanSession
            self.previewLayer = previewLayer
            self.output = output

        } catch {
            debugPrint(error)
        }
    }
    
    @objc
    func captureInputPortFormatDescriptionDidChange(notification: Notification) {
        let rect = previewLayer?.metadataOutputRectConverted(fromLayerRect: scanFrame.frame)
        debugPrint(rect ?? "no rect")
        output?.rectOfInterest = rect!
    }
    
    func startScanAnimation() {
        let x = scanFrame.bounds.width / 2
        let startPoint = CGPoint(x: x, y: 3)
        let endPoint = CGPoint(x: x, y: scanFrame.bounds.height - 3)
        
        let animate = CABasicAnimation(keyPath: "position")
        animate.timingFunction = CAMediaTimingFunction(name: kCAMediaTimingFunctionEaseInEaseOut)
        animate.fromValue = NSValue(cgPoint: startPoint)
        animate.toValue = NSValue(cgPoint: endPoint)
        animate.duration = scanDuration
        animate.repeatCount = .greatestFiniteMagnitude
        animate.autoreverses = true
        scanLine.layer.add(animate, forKey: scanKey)
    }
    
    func stopScanAnimation() {
        scanLine.layer.removeAllAnimations()
    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
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

//MARK: -
//MARK: AVCaptureMetadataOutputObjects Delegate

//扫描捕捉完成
extension BILQRCodeScanViewController : AVCaptureMetadataOutputObjectsDelegate
{
    
    func metadataOutput(_ output: AVCaptureMetadataOutput, didOutput metadataObjects: [AVMetadataObject], from connection: AVCaptureConnection) {
        if metadataObjects.count > 0
        {
            if let resultObj = metadataObjects.first as? AVMetadataMachineReadableCodeObject
            {
                debugPrint(resultObj.stringValue ?? "no result")
                if let qrStr = resultObj.stringValue {
                    self.scanSession!.stopRunning()
                    DispatchQueue.main.async {
                        self.resultClosure?(qrStr)
                        self.resultClosure = nil
                    }
                }
            }
        }
    }
}
