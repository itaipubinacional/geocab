//
//  DataSource.h
//  geocab
//
//  Created by Henrique Lobato Zago on 14/10/14.
//  Copyright (c) 2014 Itaipu. All rights reserved.
//

#import <Foundation/Foundation.h>

@interface DataSource : NSObject

@property (retain, nonatomic) NSString *name;
@property (retain, nonatomic) NSString *url;
@property (retain, nonatomic) NSString *login;
@property (retain, nonatomic) NSString *password;

@end