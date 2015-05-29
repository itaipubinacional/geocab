//
//  Motive.h
//  geocab
//
//  Created by Joaz on 28/05/15.
//  Copyright (c) 2015 Itaipu. All rights reserved.
//

#import <Foundation/Foundation.h>

@interface Motive : NSObject

@property (nonatomic, retain) NSNumber *id;
@property (nonatomic, retain) NSString *name;

+ (NSDictionary *) generateDictionary;

@end