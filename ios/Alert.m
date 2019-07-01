//
//  Alert.m
//  RNBridgeTest
//
//  Created by Olcay Ay on 1.07.2019.
//  Copyright © 2019 Facebook. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "React/RCTBridgeModule.h"

@interface RCT_EXTERN_MODULE(Alert, NSObject)

RCT_EXTERN_METHOD(
  show:(NSString *)title
  message:(NSString *)message
  positiveBtnText:(NSString *)positiveBtnText
  negativeBtnText:(NSString *)negativeBtnText
  onPositiveCallback:(RCTResponseSenderBlock)onPositiveCallback
  onNegativeCallback:(RCTResponseSenderBlock)onNegativeCallback
)

@end
