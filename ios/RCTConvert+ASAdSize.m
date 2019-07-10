#import "RCTConvert+ASAdSize.h"

@implementation RCTConvert (ASAdSize)

+ (ASAdSize *)ASAdSize:(id)json
{
    NSString *adSize = [self NSString:json];
    if ([adSize isEqualToString:@"banner"]) {
        return ASAdSize.asAdSizeBanner;
    } else if ([adSize isEqualToString:@"mediumRectangle"]) {
        return ASAdSize.asAdSizeMediumRectangle;
    } else {
        return ASAdSize.asAdSizeInvalid;
    }
}

@end
