//
//  MotiveMarkerModeration.h
//  geocab
//
//  Created by Joaz on 28/05/15.
//  Copyright (c) 2015 Itaipu. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "Motive.h"

@interface MotiveMarkerModeration : NSObject

@property (nonatomic, retain) NSNumber *id;
@property (nonatomic, retain) NSString *description;
@property (nonatomic, retain) Motive *motive;

+ (NSDictionary *) generateDictionary;
+ (MotiveMarkerModeration *)fromJSONString:(NSString *)jsonString;

@end