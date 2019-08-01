#if __has_include(<React/RCTView.h>)
#import <React/RCTView.h>
#else
#import "RCTView.h"
#endif

@import AppSamuraiAdSDK;

@class RCTEventDispatcher;

@interface RNASBannerView : RCTView <ASBannerViewDelegate>

@property (nonatomic, copy) NSArray *testDevices;
@property (nonatomic, copy) NSString *adUnitID;
@property (nonatomic, copy) NSString *gadAdUnitID;

@property (nonatomic, copy) RCTBubblingEventBlock onSizeChange;
@property (nonatomic, copy) RCTBubblingEventBlock onAdLoaded;
@property (nonatomic, copy) RCTBubblingEventBlock onAdFailedToLoad;
@property (nonatomic, copy) RCTBubblingEventBlock onAdLeftApplication;

- (void)loadBanner;

@end
