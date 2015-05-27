//
//  MarkerAttribute.h
//  geocab
//
//  Created by Henrique Lobato Zago on 23/10/14.
//  Copyright (c) 2014 Itaipu. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "Marker.h"
#import "Attribute.h"

@interface MarkerAttribute : NSObject

@property (nonatomic, retain) NSNumber *id;
@property (nonatomic, retain) NSString *value;
@property (nonatomic, retain) Marker *marker;
@property (nonatomic, retain) Attribute *attribute;

+ (NSDictionary *) generateDictionary;

@end
