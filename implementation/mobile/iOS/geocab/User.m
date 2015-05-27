//
//  User.m
//  geocab
//
//  Created by Henrique Lobato on 02/10/14.
//  Copyright (c) 2014 Itaipu. All rights reserved.
//

#import "User.h"

@implementation User

@synthesize id, name, email, password, enabled, role, created, updated, username, authorities, accountNonExpired, accountNonLocked;

+ (NSDictionary*) generateDictionary {
    
    return @{
             @"id"          : @"id",
             @"name"        : @"name",
             @"created"     : @"created",
             @"updated"     : @"updated",
             @"email"       : @"email",
             @"enabled"     : @"enabled",
             @"role"        : @"role",
             @"password"    : @"password",
             @"authorities" : @"authorities"
             };
    
}

@end
