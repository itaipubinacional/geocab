//
//  User.h
//  geocab
//
//  Created by Henrique Lobato on 02/10/14.
//  Copyright (c) 2014 Itaipu. All rights reserved.
//

#import <Foundation/Foundation.h>

@interface User : NSObject

@property (retain, nonatomic) NSNumber *id;
@property (retain, nonatomic) NSString *name;
@property (retain, nonatomic) NSString *email;
@property (retain, nonatomic) NSString *password;

@end
