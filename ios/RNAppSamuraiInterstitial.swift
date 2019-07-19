//
//  AppSamuraiInterstitial.swift
//  RNBridgeTest
//
//  Created by Olcay Ay on 8.07.2019.
//  Copyright © 2019 Facebook. All rights reserved.
//

import Foundation
import UIKit
import AppSamuraiAdSDK

@objc(RNAppSamuraiInterstitial)
class RNAppSamuraiInterstitial: RCTEventEmitter, ASInterstitialDelegate {
  
  private var asInterstitial: ASInterstitial?
  private var adUnitIDs: [String: String] = [:]
  private var testDevices: Array<String> = []
  private var requestAdResolve: RCTPromiseResolveBlock?
  private var requestAdReject: RCTPromiseRejectBlock?

  
  let kEventAdLoaded: String = "interstitialAdLoaded"
  let kEventAdFailedToLoad: String = "interstitialAdFailedToLoad"
  let kEventAdOpened: String = "interstitialAdOpened"
  let kEventAdFailedToOpen: String = "interstitialAdFailedToOpen"
  let kEventAdClosed: String = "interstitialAdClosed"
  let kEventAdLeftApplication: String = "interstitialAdLeftApplication"

  /// - Returns: all supported events
  @objc open override func supportedEvents() -> [String] {
    return [
      kEventAdLoaded,
      kEventAdFailedToLoad,
      kEventAdOpened,
      kEventAdFailedToOpen,
      kEventAdClosed,
      kEventAdLeftApplication
    ]
  }
  
  @objc
  func setAdUnitIDs(_ adUnitIDs: [String: String]) {
    // print("Setting AdUnitIDs \(adUnitIDs)")
    self.adUnitIDs = adUnitIDs
  }
  
  @objc
  func setTestDevices(_ testDevices: Array<String>) {
    // print("Setting test devices \(testDevices)")
    self.testDevices = testDevices
  }

  @objc
  func requestAd(_ resolve: @escaping RCTPromiseResolveBlock, rejecter reject: @escaping RCTPromiseRejectBlock ) {
    // print("Requesting Ad")
    
    self.requestAdResolve = nil
    self.requestAdReject = nil

    if ( asInterstitial == nil || asInterstitial!.hasBeenUsed ){
      self.requestAdResolve = resolve
      self.requestAdReject = reject
      
      var asadUnitID: String? = nil
      var gadAdUnitID: String? = nil
      for (adnetwork, adUnitID) in adUnitIDs {
        // print("\(adnetwork): \(adUnitID)")
        if ( adnetwork == AdNetwork.APPSAMURAI.rawValue ) {
          asadUnitID = adUnitID
        } else if ( adnetwork == AdNetwork.GOOGLE.rawValue ){
          gadAdUnitID = adUnitID
        }
      }

      // print("APPSAMURAI: \(asadUnitID) GOOGLE: \(gadAdUnitID)")

      if ( asadUnitID != nil ){
        asInterstitial = ASInterstitial(adUnitID: asadUnitID!, gadAdUnitID: gadAdUnitID)
        // delegate is used to receive ad events
        asInterstitial?.delegate = self
        
        let adRequest = ASAdRequest()
        if ( !testDevices.isEmpty ){
          adRequest.testDevices = testDevices
        }
        // Load ad with request
        asInterstitial?.loadAd(adRequest: adRequest)
      }
    } else {
      reject("E_AD_ALREADY_LOADED", "Ad is already loaded.", nil)
    }
  }

  @objc
  func showAd(_ resolve: @escaping RCTPromiseResolveBlock, rejecter reject: RCTPromiseRejectBlock ) {
    // print("Showing Ad")
    
    if (asInterstitial?.isReady ?? false) && !(asInterstitial?.hasBeenUsed ?? true) {
      // Present ad
      DispatchQueue.main.async {
        let viewController = UIApplication.shared.keyWindow!.rootViewController as! UIViewController
        self.asInterstitial?.present(rootViewController: viewController)
        resolve(nil)
      }
    } else {
      reject("E_AD_NOT_READY", "Ad is not ready.", nil);
    }

  }

  @objc
  func isReady(_ callback: RCTResponseSenderBlock) {
    // print("Is Ready?")
    callback([asInterstitial?.isReady ?? false])

//    callback(@[[NSNumber numberWithBool:[_interstitial isReady]]]);
  }
  
  @objc
  override static func requiresMainQueueSetup() -> Bool {
    return true
  }
  
  //MARK: - ASInterstitialDelegate functions
  // Notify when ad is succesfully received
  func interstitialDidReceiveAd(_ asInterstitial: ASInterstitial) {
    // print("interstitialDidReceiveAd")
    self.sendEvent(withName: kEventAdLoaded, body: nil)
    if ( requestAdResolve != nil ){
      requestAdResolve!(nil)
    }
  }
  
  // Notify when ad is failed to receive, check ASAdRequestError types and console logs for details
  func interstitialDidFailToReceiveAd(_ asInterstitial: ASInterstitial, error: ASAdRequestError) {
    // print("interstitialDidFailToReceiveAd \(error.localizedDescription)")
    self.sendEvent(withName: kEventAdFailedToLoad, body: error.localizedDescription)
    if (requestAdReject != nil){
      requestAdReject!("E_AD_REQUEST_FAILED", error.localizedDescription, error);
    }
  }
  
  // Notify when ad will be presented screen on fullscreen
  func interstitialWillPresentScreen(_ asInterstitial: ASInterstitial) {
    // print("interstitialWillPresentScreen")
    self.sendEvent(withName:kEventAdOpened, body:nil);

  }
  
  // Notify when ad will dismiss from screen
  func interstitialWillDismissScreen(_ asInterstitial: ASInterstitial) {
    // print("interstitialWillDismissScreen")
//    self.sendEvent(withName:kEventAdOpened, body:nil);
  }
  
  // Notify when ad dismissed from screen
  func interstitialDidDismissScreen(_ asInterstitial: ASInterstitial) {
    // print("interstitialDidDismissScreen")
    self.sendEvent(withName:kEventAdClosed, body:nil);
  }
  
  // Notify when ad is clicked and another app will be opened
  func interstitialWillLeaveApplication(_ asInterstitial: ASInterstitial) {
    // print("interstitialWillLeaveApplication")
    self.sendEvent(withName:kEventAdLeftApplication, body:nil);
  }
}
