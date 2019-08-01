//
//  RNAppSamuraiRewarded.m
//  RNAppSamuraiAds
//
//  Created by Levent ORAL on 1.08.2019.
//  Copyright © 2019 Facebook. All rights reserved.
//

#import "RNAppSamuraiRewarded.h"

#if __has_include(<React/RCTUtils.h>)
#import <React/RCTUtils.h>
#else
#import "RCTUtils.h"
#endif

static NSString *const kEventAdLoaded = @"rewardedVideoAdLoaded";
static NSString *const kEventAdFailedToLoad = @"rewardedVideoAdFailedToLoad";
static NSString *const kEventAdOpened = @"rewardedVideoAdOpened";
static NSString *const kEventAdClosed = @"rewardedVideoAdClosed";
static NSString *const kEventAdLeftApplication = @"rewardedVideoAdLeftApplication";
static NSString *const kEventRewarded = @"rewardedVideoAdRewarded";
static NSString *const kEventVideoStarted = @"rewardedVideoAdVideoStarted";
static NSString *const kEventVideoCompleted = @"rewardedVideoAdVideoCompleted";

@implementation RNAppSamuraiRewarded
{
    ASRewardBasedVideoAd *asRewardBasedVideoAd;
    NSString *_adUnitID;
    NSArray *_testDevices;
    RCTPromiseResolveBlock _requestAdResolve;
    RCTPromiseRejectBlock _requestAdReject;
}

- (dispatch_queue_t)methodQueue
{
    return dispatch_get_main_queue();
}

+ (BOOL)requiresMainQueueSetup
{
    return NO;
}

RCT_EXPORT_MODULE();

- (NSArray<NSString *> *)supportedEvents
{
    return @[
             kEventRewarded,
             kEventAdLoaded,
             kEventAdFailedToLoad,
             kEventAdOpened,
             kEventAdClosed,
             kEventVideoStarted,
             kEventVideoCompleted,
             kEventAdLeftApplication ];
}

RCT_EXPORT_METHOD(setAdUnitID:(NSString *)adUnitID)
{
    _adUnitID = adUnitID;
}

RCT_EXPORT_METHOD(setTestDevices:(NSArray *)testDevices)
{
    _testDevices = testDevices;
}

RCT_EXPORT_METHOD(requestAd:(RCTPromiseResolveBlock)resolve rejecter:(RCTPromiseRejectBlock)reject)
{
    _requestAdResolve = nil;
    _requestAdReject = nil;
    
    if (_asRewardBasedVideoAd == nil) {
        _requestAdResolve = resolve;
        _requestAdReject = reject;
        
        _asRewardBasedVideoAd = [[ASRewardBasedVideoAd alloc] initWithAdUnitID:adUnitID];
        _asRewardBasedVideoAd.delegate = self;
        
        ASAdRequest *adRequest = [[ASAdRequest alloc] init];
        request.testDevices = _testDevices;
        [_asInterstitial loadAdWithAdRequest:adRequest];
    } else {
        reject(@"E_AD_ALREADY_LOADED", @"Ad is already loaded.", nil);
    }
}

RCT_EXPORT_METHOD(showAd:(RCTPromiseResolveBlock)resolve rejecter:(RCTPromiseRejectBlock)reject)
{
    if ([_asRewardBasedVideoAd isReady]) {
        [_asRewardBasedVideoAd presentWithRootViewController:[UIApplication sharedApplication].delegate.window.rootViewController];
        resolve(nil);
    }
    else {
        reject(@"E_AD_NOT_READY", @"Ad is not ready.", nil);
    }
}

RCT_EXPORT_METHOD(isReady:(RCTResponseSenderBlock)callback)
{
    callback(@[[NSNumber numberWithBool:[_asRewardBasedVideoAd isReady]]]);
}

#pragma ASRewardBasedVideoAdDelegate functions
- (void)rewardBasedVideoAdDidReceive:(ASRewardBasedVideoAd * _Nonnull)asRewardBasedAd {
    [self sendEventWithName:kEventAdLoaded body:nil];
    _requestAdResolve(nil);
}

- (void)rewardBasedVideoAdDidFailToReceiveAd:(ASRewardBasedVideoAd * _Nonnull)asRewardBasedAd error:(ASAdRequestError * _Nonnull)error {
    NSDictionary *jsError = RCTJSErrorFromCodeMessageAndNSError(@"E_AD_REQUEST_FAILED", error.localizedDescription, error);
    [self sendEventWithName:kEventAdFailedToLoad body:jsError];
    
    _requestAdReject(@"E_AD_REQUEST_FAILED", error.localizedDescription, error);
}

- (void)rewardBasedVideoAdDidOpen:(ASRewardBasedVideoAd * _Nonnull)asRewardBasedAd {
    [self sendEventWithName:kEventAdOpened body:nil];
}

- (void)rewardBasedVideoAdDidClose:(ASRewardBasedVideoAd * _Nonnull)asRewardBasedAd {
    [self sendEventWithName:kEventAdClosed body:nil];
}

- (void)rewardBasedVideoAdStartPlaying:(ASRewardBasedVideoAd * _Nonnull)asRewardBasedAd {
    [self sendEventWithName:kEventVideoStarted body:nil];
}

- (void)rewardBasedVideoAdCompletePlaying:(ASRewardBasedVideoAd * _Nonnull)asRewardBasedAd {
    [self sendEventWithName:kEventVideoCompleted body:nil];
}

- (void)rewardBasedVideoAdDidReward:(ASRewardBasedVideoAd * _Nonnull)asRewardBasedAd {
    [self sendEventWithName:kEventRewarded body:nil];
}

- (void)rewardBasedVideoAdWillLeaveApplication:(ASRewardBasedVideoAd * _Nonnull)asRewardBasedAd {
    [self sendEventWithName:kEventAdLeftApplication body:nil];
}

@end
