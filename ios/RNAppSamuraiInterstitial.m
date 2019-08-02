//
//  RNAppSamuraiInterstitial.m
//  RNAppSamuraiAds
//
//  Created by Levent ORAL on 31.07.2019.
//  Copyright © 2019 Facebook. All rights reserved.
//

#import "RNAppSamuraiInterstitial.h"

#if __has_include(<React/RCTUtils.h>)
#import <React/RCTUtils.h>
#else
#import "RCTUtils.h"
#endif

static NSString *const kEventAdLoaded = @"interstitialAdLoaded";
static NSString *const kEventAdFailedToLoad = @"interstitialAdFailedToLoad";
static NSString *const kEventAdOpened = @"interstitialAdOpened";
static NSString *const kEventAdClosed = @"interstitialAdClosed";
static NSString *const kEventAdLeftApplication = @"interstitialAdLeftApplication";

@implementation RNAppSamuraiInterstitial
{
    ASInterstitial *_asInterstitial;
    NSString *_adUnitID;
    NSString *_gadAdUnitID;
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
             kEventAdLoaded,
             kEventAdFailedToLoad,
             kEventAdOpened,
             kEventAdClosed,
             kEventAdLeftApplication ];
}

RCT_EXPORT_METHOD(setAdUnitID:(NSString *)adUnitID)
{
    _adUnitID = adUnitID;
}

RCT_EXPORT_METHOD(setGADAdUnitID:(NSString *)gadAdUnitID)
{
    _gadAdUnitID = gadAdUnitID;
}

RCT_EXPORT_METHOD(setTestDevices:(NSArray *)testDevices)
{
    _testDevices = testDevices;
}

RCT_EXPORT_METHOD(requestAd:(RCTPromiseResolveBlock)resolve rejecter:(RCTPromiseRejectBlock)reject)
{
    _requestAdResolve = nil;
    _requestAdReject = nil;

    if ([_asInterstitial hasBeenUsed] || _asInterstitial == nil) {
        _requestAdResolve = resolve;
        _requestAdReject = reject;

        if (_gadAdUnitID != nil) {
            _asInterstitial = [[ASInterstitial alloc] initWithAdUnitID:_adUnitID gadAdUnitID:_gadAdUnitID];
        } else {
            _asInterstitial = [[ASInterstitial alloc] initWithAdUnitID:_adUnitID];
        }
        _asInterstitial.delegate = self;

        ASAdRequest *adRequest = [[ASAdRequest alloc] init];
        adRequest.testDevices = _testDevices;
        [_asInterstitial loadAdWithAdRequest:adRequest];
    } else {
        reject(@"E_AD_ALREADY_LOADED", @"Ad is already loaded.", nil);
    }
}

RCT_EXPORT_METHOD(showAd:(RCTPromiseResolveBlock)resolve rejecter:(RCTPromiseRejectBlock)reject)
{
    if ([_asInterstitial isReady]) {
        [_asInterstitial presentWithRootViewController:[UIApplication sharedApplication].delegate.window.rootViewController];
        resolve(nil);
    }
    else {
        reject(@"E_AD_NOT_READY", @"Ad is not ready.", nil);
    }
}

RCT_EXPORT_METHOD(isReady:(RCTResponseSenderBlock)callback)
{
    callback(@[[NSNumber numberWithBool:[_asInterstitial isReady]]]);
}

#pragma ASInterstitialDelegate functions
- (void)interstitialDidReceiveAd:(ASInterstitial *_Nonnull)asInterstitial
{
    [self sendEventWithName:kEventAdLoaded body:nil];
    _requestAdResolve(nil);
}

- (void)interstitialDidFailToReceiveAd:(ASInterstitial *_Nonnull)asInterstitial error:(ASAdRequestError *_Nonnull)error
{
    NSDictionary *jsError = RCTJSErrorFromCodeMessageAndNSError(@"E_AD_REQUEST_FAILED", error.localizedDescription, error);
    [self sendEventWithName:kEventAdFailedToLoad body:jsError];

    _requestAdReject(@"E_AD_REQUEST_FAILED", error.localizedDescription, error);
}

- (void)interstitialWillPresentScreen:(ASInterstitial *_Nonnull)asInterstitial
{
    [self sendEventWithName:kEventAdOpened body:nil];
}

- (void)interstitialWillDismissScreen:(ASInterstitial *_Nonnull)asInterstitial
{
}

- (void)interstitialDidDismissScreen:(ASInterstitial *_Nonnull)asInterstitial
{
    [self sendEventWithName:kEventAdClosed body:nil];
}

- (void)interstitialWillLeaveApplication:(ASInterstitial *_Nonnull)asInterstitial
{
    [self sendEventWithName:kEventAdLeftApplication body:nil];
}

@end
