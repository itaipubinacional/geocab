//
//  User.h
//  geocab
//
//  Created by Henrique Lobato on 02/10/14.
//  Copyright (c) 2014 Itaipu. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "UserRole.h"

@interface User : NSObject

@property (retain, nonatomic) NSNumber *id;
@property (retain, nonatomic) NSString *name;
@property (retain, nonatomic) NSString *email;
@property (retain, nonatomic) NSDate *created;
@property (retain, nonatomic) NSDate *updated;
@property (retain, nonatomic) NSString *username;
@property (retain, nonatomic) NSString *password;
@property (retain, nonatomic) NSArray *authorities;
@property (retain, nonatomic) NSString *role;
@property (nonatomic) BOOL enabled;
@property (nonatomic) BOOL accountNonLocked;
@property (nonatomic) BOOL accountNonExpired;

+ (NSDictionary *) generateDictionary;

@end
