//
//  Layer.m
//  geocab
//
//  Created by Henrique Lobato on 01/10/14.
//  Copyright (c) 2014 Itaipu. All rights reserved.
//

#import "Layer.h"

@implementation Layer

@synthesize id, name, title, legend, icon, selected, dataSource, created;

+ (NSDictionary*) generateDictionary {
    
    return @{
             @"id"        : @"id",
             @"name"      : @"name",
             @"title"     : @"title",
             @"legend"    : @"legend",
             @"icon"      : @"icon",
             @"created"   : @"created"
             };
    
}

@end