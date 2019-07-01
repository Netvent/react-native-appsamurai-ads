//
//  CalendarManager.m
//  RNBridgeTest
//
//  Created by Olcay Ay on 1.07.2019.
//  Copyright © 2019 Facebook. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "CalendarManager.h"
#import <React/RCTLog.h>

@implementation CalendarManager

RCT_EXPORT_MODULE();

RCT_EXPORT_METHOD(addEvent:(NSString *)name location:(NSString *)location)
{
  RCTLogInfo(@"RCTLog: Pretending to create an event %@ at %@", name, location);
  NSLog(@"NSLOG: Pretending to create an event %@ at %@", name, location);
  
  UIAlertView *alert = [[UIAlertView alloc] initWithTitle:@"Do you want to say hello?"
                                                  message:@"More info..."
                                                 delegate:self
                                        cancelButtonTitle:@"Cancel"
                                        otherButtonTitles:@"Say Hello",nil];
  [alert show];
}

RCT_EXPORT_METHOD(showAlert:(NSString *)title message:(NSString *)message  positiveBtnText:(NSString *)positiveBtnText  negativeBtnText:(NSString *)negativeBtnText )
{
  UIAlertView *alert = [[UIAlertView alloc] initWithTitle:title
                                                  message:message
                                                 delegate:self
                                        cancelButtonTitle:negativeBtnText
                                        otherButtonTitles:positiveBtnText,nil];
  [alert show];
}

@end
