//
//  MarkerAttribute.m
//  geocab
//
//  Created by Henrique Lobato Zago on 23/10/14.
//  Copyright (c) 2014 Itaipu. All rights reserved.
//

#import "MarkerAttribute.h"

@implementation MarkerAttribute

@synthesize id, value, marker, attribute;

+ (NSDictionary*) generateDictionary {
    
    return @{
             @"id"     : @"id",
             @"value"  : @"value"
            };
    
}

@end
