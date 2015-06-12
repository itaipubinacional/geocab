//
//  Motive.m
//  geocab
//
//  Created by Joaz on 28/05/15.
//  Copyright (c) 2015 Itaipu. All rights reserved.
//

#import "Motive.h"

@implementation Motive

@synthesize id, name;

+ (NSDictionary*) generateDictionary {
    
    return @{
		@"id"     : @"id",
        @"name"   : @"name",
	};
}

@end

