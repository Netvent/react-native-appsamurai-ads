//
//  Alert.swift
//  RNBridgeTest
//
//  Created by Olcay Ay on 1.07.2019.
//  Copyright © 2019 Facebook. All rights reserved.
//

import Foundation
import UIKit

@objc(Alert)
class Alert: NSObject {
  
  @objc
  func show(
    _ title: String,
    message:String,
    positiveBtnText:String,
    negativeBtnText:String,
    onPositiveCallback: @escaping RCTResponseSenderBlock ,
    onNegativeCallback: @escaping RCTResponseSenderBlock ){
    let alertController = UIAlertController(title: title, message: message, preferredStyle: .alert)
    let positiveAction = UIAlertAction(title: positiveBtnText, style: .default) { (action:UIAlertAction) in
      onPositiveCallback([NSNull()])
    }
    
    let negativeAction = UIAlertAction(title: negativeBtnText, style: .cancel) { (action:UIAlertAction) in
      onNegativeCallback([NSNull()])
    }
    
    alertController.addAction(positiveAction)
    alertController.addAction(negativeAction)
    
    let rootViewController = UIApplication.shared.keyWindow!.rootViewController

    rootViewController?.present(alertController, animated: true, completion: nil)
  }

  @objc
  static func requiresMainQueueSetup() -> Bool {
    return true
  }
}
