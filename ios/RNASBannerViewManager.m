#import "RNASBannerViewManager.h"
#import "RNASBannerView.h"
#import "RCTConvert+ASAdSize.h"

#if __has_include(<React/RCTBridge.h>)
#import <React/RCTBridge.h>
#import <React/RCTUIManager.h>
#import <React/RCTEventDispatcher.h>
#else
#import "RCTBridge.h"
#import "RCTUIManager.h"
#import "RCTEventDispatcher.h"
#endif

@implementation RNASBannerViewManager

RCT_EXPORT_MODULE();

- (UIView *)view
{
    return [RNASBannerView new];
}

RCT_EXPORT_METHOD(loadBanner:(nonnull NSNumber *)reactTag)
{
    [self.bridge.uiManager addUIBlock:^(__unused RCTUIManager *uiManager, NSDictionary<NSNumber *, RNASBannerView *> *viewRegistry) {
        RNASBannerView *view = viewRegistry[reactTag];
        if (![view isKindOfClass:[RNASBannerView class]]) {
            RCTLogError(@"Invalid view returned from registry, expecting RNASBannerView, got: %@", view);
        } else {
            [view loadBanner];
        }
    }];
}

RCT_REMAP_VIEW_PROPERTY(adSize, _bannerView.adSize, ASAdSize)
RCT_REMAP_VIEW_PROPERTY(adUnitID, _bannerView.adUnitID, NSString)

RCT_EXPORT_VIEW_PROPERTY(testDevices, NSArray)

RCT_EXPORT_VIEW_PROPERTY(onSizeChange, RCTBubblingEventBlock)
RCT_EXPORT_VIEW_PROPERTY(onAdLoaded, RCTBubblingEventBlock)
RCT_EXPORT_VIEW_PROPERTY(onAdFailedToLoad, RCTBubblingEventBlock)
RCT_EXPORT_VIEW_PROPERTY(onAdOpened, RCTBubblingEventBlock)
RCT_EXPORT_VIEW_PROPERTY(onAdClosed, RCTBubblingEventBlock)
RCT_EXPORT_VIEW_PROPERTY(onAdLeftApplication, RCTBubblingEventBlock)

@end
