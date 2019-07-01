//
//  AlertModule.m
//  RNBridgeTest
//
//  Created by Olcay Ay on 1.07.2019.
//  Copyright © 2019 Facebook. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "AlertModule.h"
#import <React/RCTLog.h>

@implementation AlertModule

RCT_EXPORT_MODULE();

// public void showAlert(String title, String message, String positiveBtnText, String negativeBtnText, Callback onPositiveCallback, Callback onNegativeCallback) {

RCT_EXPORT_METHOD(
                  showAlert:(NSString *)title
                  message:(NSString *)message
                  positiveBtnText:(NSString *)positiveBtnText
                  negativeBtnText:(NSString *)negativeBtnText
                  onPositiveCallback:(RCTResponseSenderBlock)onPositiveCallback
                  onNegativeCallback:(RCTResponseSenderBlock)onNegativeCallback )
{
//  UIAlertView *alert = [[UIAlertView alloc] initWithTitle:title
//                                                  message:message
//                                                 delegate:self
//                                        cancelButtonTitle:negativeBtnText
//                                        otherButtonTitles:positiveBtnText,nil];
//  [alert show];
  UIAlertController *alertController = [UIAlertController
                                        alertControllerWithTitle: title message:message preferredStyle: UIAlertControllerStyleAlert];
  
  UIAlertAction *okAction = [UIAlertAction actionWithTitle: positiveBtnText style: UIAlertActionStyleDefault handler: ^(UIAlertAction *action)
                             {
                               onPositiveCallback(@[[NSNull null]]);
                             }];
  [alertController addAction: okAction];
  
  UIAlertAction *cancelAction = [UIAlertAction actionWithTitle: negativeBtnText style: UIAlertActionStyleDefault handler: ^(UIAlertAction *action)
                                 {
                                   onNegativeCallback(@[[NSNull null]]);
                                 }];
  [alertController addAction: cancelAction];
  
  UIViewController *root = [[[[UIApplication sharedApplication] delegate] window] rootViewController];

  [root presentViewController:alertController animated:YES completion:nil];
}

@end
