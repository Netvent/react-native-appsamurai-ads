//
//  RNAppSamuraiRewarded.swift
//  RNBridgeTest
//
//  Created by Olcay Ay on 9.07.2019.
//  Copyright © 2019 Facebook. All rights reserved.
//

import Foundation
import UIKit
import AppSamuraiAdSDK


@objc(RNAppSamuraiRewarded)
class RNAppSamuraiRewarded: RCTEventEmitter, ASRewardBasedVideoAdDelegate {
  
  private var asRewardBasedVideoAd: ASRewardBasedVideoAd?
  private var adUnitIDs: [String: String] = [:]
  private var testDevices: Array<String> = []
  private var requestAdResolve: RCTPromiseResolveBlock?
  private var requestAdReject: RCTPromiseRejectBlock?
  
  let kEventAdLoaded = "rewardedVideoAdLoaded";
  let kEventAdFailedToLoad = "rewardedVideoAdFailedToLoad";
  let kEventAdOpened = "rewardedVideoAdOpened";
  let kEventAdClosed = "rewardedVideoAdClosed";
  let kEventAdLeftApplication = "rewardedVideoAdLeftApplication";
  let kEventRewarded = "rewardedVideoAdRewarded";
  let kEventVideoStarted = "rewardedVideoAdVideoStarted";
  let kEventVideoCompleted = "rewardedVideoAdVideoCompleted";
  
  /// - Returns: all supported events
  @objc open override func supportedEvents() -> [String] {
    return [
      kEventAdLoaded,
      kEventAdFailedToLoad,
      kEventAdOpened,
      kEventAdClosed,
      kEventAdLeftApplication,
      kEventRewarded,
      kEventVideoStarted,
      kEventVideoCompleted
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
    
    if ( asRewardBasedVideoAd == nil ){
      self.requestAdResolve = resolve
      self.requestAdReject = reject
      
      var asadUnitID: String? = nil
      var gadAdUnitID: String? = nil
      for (adnetwork, adUnitID) in adUnitIDs {
        print("\(adnetwork): \(adUnitID)")
        if ( adnetwork == AdNetwork.APPSAMURAI.rawValue ) {
          asadUnitID = adUnitID
        } else if ( adnetwork == AdNetwork.GOOGLE.rawValue ){
          gadAdUnitID = adUnitID
        }
      }
      
      // print("APPSAMURAI: \(asadUnitID) GOOGLE: \(gadAdUnitID)")

      if ( asadUnitID != nil ){
        asRewardBasedVideoAd = ASRewardBasedVideoAd(adUnitID: asadUnitID!, gadAdUnitID: gadAdUnitID)
        // delegate is used to receive ad events
        asRewardBasedVideoAd?.delegate = self
        let adRequest = ASAdRequest()
        if ( !testDevices.isEmpty ){
          adRequest.testDevices = testDevices
        }
        // Load ad with request
        asRewardBasedVideoAd?.loadAd(adRequest: adRequest)
      }
    } else {
      reject("E_AD_ALREADY_LOADED", "Ad is already loaded.", nil)
    }
  }
  
  @objc
  func showAd(_ resolve: @escaping RCTPromiseResolveBlock, rejecter reject: RCTPromiseRejectBlock ) {
    // print("Showing Ad")
    
    if (asRewardBasedVideoAd?.isReady ?? false) {
      // Present ad
      DispatchQueue.main.async {
        let viewController = UIApplication.shared.keyWindow!.rootViewController as! UIViewController
        self.asRewardBasedVideoAd?.present(rootViewController: viewController)
        resolve(nil)
      }
    } else {
      reject("E_AD_NOT_READY", "Ad is not ready.", nil);
    }
    
  }
  
  @objc
  func isReady(_ callback: RCTResponseSenderBlock) {
    // print("Is Ready?")
    callback([asRewardBasedVideoAd?.isReady ?? false])
    
    //    callback(@[[NSNumber numberWithBool:[_interstitial isReady]]]);
  }
  
  @objc
  override static func requiresMainQueueSetup() -> Bool {
    return true
  }
  
  //MARK: - ASRewardBasedVideoAdDelegate functions
  // Notify when ad is succesfully received
  func rewardBasedVideoAdDidReceive(_ asRewardBasedAd: ASRewardBasedVideoAd) {
    // print("RNAppSamuraiRewarded: rewardBasedVideoAdDidReceive")
    self.sendEvent(withName: kEventAdLoaded, body: nil)
    if ( requestAdResolve != nil ){
      requestAdResolve!(nil)
    }
  }
  
  // Notify when ad is failed to receive, check ASAdRequestError types and console logs for details
  func rewardBasedVideoAdDidFailToReceiveAd(_ asRewardBasedAd: ASRewardBasedVideoAd, error: ASAdRequestError) {
    // print("RNAppSamuraiRewarded: rewardBasedVideoAdDidFailToReceiveAd \(error.localizedDescription)")
    self.sendEvent(withName: kEventAdFailedToLoad, body: error.localizedDescription)
    if (requestAdReject != nil){
      requestAdReject!("E_AD_REQUEST_FAILED", error.localizedDescription, error);
    }
  }
  
  // Notify when ad is opened
  func rewardBasedVideoAdDidOpen(_ asRewardBasedAd: ASRewardBasedVideoAd) {
    // print("RNAppSamuraiRewarded: rewardBasedVideoAdDidOpen")
    self.sendEvent(withName:kEventAdOpened, body:nil);
  }
  
  // Notify when ad is closed
  func rewardBasedVideoAdDidClose(_ asRewardBasedAd: ASRewardBasedVideoAd) {
    // print("RNAppSamuraiRewarded: rewardBasedVideoAdDidClose")
    self.sendEvent(withName:kEventAdClosed, body:nil);
  }
  
  // Notify when video ad playback is started
  func rewardBasedVideoAdStartPlaying(_ asRewardBasedAd: ASRewardBasedVideoAd) {
    // print("RNAppSamuraiRewarded: rewardBasedVideoAdStartPlaying")
    self.sendEvent(withName:kEventVideoStarted, body:nil);
  }
  
  // Notify when video ad playback is completed
  func rewardBasedVideoAdCompletePlaying(_ asRewardBasedAd: ASRewardBasedVideoAd) {
    // print("RNAppSamuraiRewarded: rewardBasedVideoAdCompletePlaying")
    self.sendEvent(withName:kEventVideoCompleted, body:nil);
  }
  
  // Notify when ad reward is succesfully received
  func rewardBasedVideoAdDidReward(_ asRewardBasedAd: ASRewardBasedVideoAd) {
    // print("RNAppSamuraiRewarded: rewardBasedVideoAdDidReward")
    self.sendEvent(withName:kEventRewarded, body:nil);
  }
  
  // Notify when ad is clicked and another app will be opened
  func rewardBasedVideoAdWillLeaveApplication(_ asRewardBasedAd: ASRewardBasedVideoAd) {
    // print("RNAppSamuraiRewarded: rewardBasedVideoAdWillLeaveApplication")
    self.sendEvent(withName:kEventAdLeftApplication, body:nil);
  }
}
