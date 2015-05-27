//
//  Marker.h
//  geocab
//
//  Created by Henrique Lobato on 06/10/14.
//  Copyright (c) 2014 Itaipu. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "Layer.h"
#import "StatusMarker.h"
#import "User.h"

@interface Marker : NSObject

@property (nonatomic, retain) NSNumber *id;
@property (nonatomic, retain) NSNumber *created;
@property (nonatomic, retain) NSString *wktCoordenate;
@property (nonatomic, retain) NSData *imageData;
@property (nonatomic, retain) UIImage *imageUI;
@property (nonatomic, retain) NSString *status;
@property (strong, nonatomic) Layer *layer;
@property (nonatomic, retain) NSMutableArray *markerAttributes;
@property (nonatomic, retain) User *user;

+ (NSDictionary *) generateDictionary;
+ (NSString *) formatDate:(NSDate *)date;
+ (NSString *) dateStrToTimeStamp:(NSString *) dateStr;
+ (Marker *)fromJSONString:(NSString *)jsonString;
- (NSString *)toJSONString;
    
@end
