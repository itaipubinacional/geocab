//
//  ControllerUtil.h
//  geocab
//
//  Created by Henrique Lobato on 07/10/14.
//  Copyright (c) 2014 Itaipu. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "Reachability.h"

@interface ControllerUtil : NSObject

+(UIColor*)colorWithHexString:(NSString*)hex;
+ (BOOL)verifyInternetConection;

@end
