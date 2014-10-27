//
//  Attribute.h
//  geocab
//
//  Created by Henrique Lobato Zago on 23/10/14.
//  Copyright (c) 2014 Itaipu. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "AttributeType.h"
#import "Layer.h"

@interface Attribute : NSObject

@property (nonatomic, retain) NSNumber *id;
@property (nonatomic, retain) NSString *name;
@property (nonatomic) enum AttributeType type;
@property (nonatomic) BOOL required;
@property (nonatomic) BOOL attributeDefault;
@property (nonatomic, retain) Layer *layer;

@end
