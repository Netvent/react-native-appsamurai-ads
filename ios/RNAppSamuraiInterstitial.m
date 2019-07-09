//
//  RNAppSamuraiInterstitial.m
//  RNBridgeTest
//
//  Created by Olcay Ay on 8.07.2019.
//  Copyright © 2019 Facebook. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "React/RCTBridgeModule.h"
#import <React/RCTEventEmitter.h>


@interface RCT_EXTERN_MODULE(RNAppSamuraiInterstitial, RCTEventEmitter)

RCT_EXTERN_METHOD(supportedEvents)

RCT_EXTERN_METHOD(
  setAdUnitID:(NSString *)adUnitID
)

RCT_EXTERN_METHOD(
  setTestDevices:(NSArray *)testDevices
)

RCT_EXTERN_METHOD(
  requestAd:(RCTPromiseResolveBlock)resolve rejecter:(RCTPromiseRejectBlock)reject
)

RCT_EXTERN_METHOD(
  showAd:(RCTPromiseResolveBlock)resolve rejecter:(RCTPromiseRejectBlock)reject
)

RCT_EXTERN_METHOD(
  isReady:(RCTResponseSenderBlock)callback
)

@end
