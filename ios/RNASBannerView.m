#import "RNASBannerView.h"

#if __has_include(<React/RCTBridgeModule.h>)
#import <React/RCTBridgeModule.h>
#import <React/UIView+React.h>
#import <React/RCTLog.h>
#else
#import "RCTBridgeModule.h"
#import "UIView+React.h"
#import "RCTLog.h"
#endif

@implementation RNASBannerView {
    ASBannerView *_bannerView;
}

- (void)dealloc {
    _bannerView.delegate = nil;
}

- (instancetype)initWithFrame:(CGRect)frame {
    if ((self = [super initWithFrame:frame])) {
        super.backgroundColor = [UIColor clearColor];
        UIWindow *keyWindow = [[UIApplication sharedApplication] keyWindow];
        UIViewController *rootViewController = [keyWindow rootViewController];
        _bannerView = [[ASBannerView alloc] initWithAdSize:ASAdSize.asAdSizeBanner];
        _bannerView.adUnitID = _adUnitID;
        _bannerView.gadAdUnitID = _gadAdUnitID;
        _bannerView.delegate = self;
        _bannerView.rootViewController = rootViewController;
        [self addSubview:_bannerView];
    }
    return self;
}

#pragma clang diagnostic push
#pragma clang diagnostic ignored "-Wobjc-missing-super-calls"
- (void)insertReactSubview:(UIView *)subview atIndex:(NSInteger)atIndex {
    RCTLogError(@"RNASBannerView cannot have subviews");
}
#pragma clang diagnostic pop

- (void)loadBanner {
    if(self.onSizeChange) {
      CGSize size =  CGSizeMake(320, 50);
//      CGSize size = CGSizeFromGADAdSize(_bannerView.adSize);
        if(!CGSizeEqualToSize(size, self.bounds.size)) {
            self.onSizeChange(@{
                                @"width": @(size.width),
                                @"height": @(size.height)
                                });
        }
    }

  ASAdRequest *request = [[ASAdRequest alloc] init];
  request.testDevices = _testDevices;
  [_bannerView loadAdWithAdRequest:request];
}

- (void)setTestDevices:(NSArray *)testDevices {
  _testDevices = testDevices;
}

-(void)layoutSubviews {
    [super layoutSubviews];
    _bannerView.frame = self.bounds;
}

# pragma mark ASBannerViewDelegate
- (void)adViewDidReceiveAd:(ASBannerView * _Nonnull)asBannerView {
   if (self.onAdLoaded) {
       self.onAdLoaded(@{});
   }
}

- (void)adViewDidFailToReceiveAd:(ASBannerView * _Nonnull)asBannerView error:(ASAdRequestError * _Nonnull)error {
    if (self.onAdFailedToLoad) {
        self.onAdFailedToLoad(@{ @"error": @{ @"message": [error localizedDescription] } });
    }
}

- (void)adViewWillLeaveApplication:(ASBannerView * _Nonnull)asBannerView {
    if (self.onAdLeftApplication) {
        self.onAdLeftApplication(@{});
    }
}
@end
