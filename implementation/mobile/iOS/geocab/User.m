//
//  User.m
//  geocab
//
//  Created by Henrique Lobato on 02/10/14.
//  Copyright (c) 2014 Itaipu. All rights reserved.
//

#import "User.h"

@implementation User

@synthesize id, name, email, password, credentials, enabled, role, created, updated, username, authorities, accountNonExpired, accountNonLocked;

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

- (void)setAccessTokenAuthorization:(NSString *)accessToken provider: (NSString *)provider {
    
    NSString *authStr = [NSString stringWithFormat:@"%@:%@:%@", self.email, accessToken, provider];
    NSData *authData = [authStr dataUsingEncoding:NSUTF8StringEncoding];
    NSString *authValue = [NSString stringWithFormat:@"access_token %@", [authData base64EncodedStringWithOptions:0]];
    
    self.credentials = authValue;
    
};

- (void)setBasicAuthorization {
    
    NSString *authStr = [NSString stringWithFormat:@"%@:%@", self.email, self.password];
    NSData *authData = [authStr dataUsingEncoding:NSUTF8StringEncoding];
    NSString *authValue = [NSString stringWithFormat:@"Basic %@", [authData base64EncodedStringWithOptions:0]];
    
    self.credentials = authValue;
    
};

@end
