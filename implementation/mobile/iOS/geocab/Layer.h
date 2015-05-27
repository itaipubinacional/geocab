//
//  Layer.h
//  geocab
//
//  Created by Henrique Lobato on 01/10/14.
//  Copyright (c) 2014 Itaipu. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "DataSource.h"

@interface Layer: NSObject

@property (retain, nonatomic) NSNumber *id;
@property (retain, nonatomic) NSString *name;
@property (retain, nonatomic) NSNumber *created;
@property (retain, nonatomic) NSString *title;
@property (retain, nonatomic) NSString *legend;
@property (retain, nonatomic) NSString *icon;
@property (retain, nonatomic) DataSource *dataSource;
@property BOOL selected;

+ (NSDictionary*) generateDictionary;
    
@end
