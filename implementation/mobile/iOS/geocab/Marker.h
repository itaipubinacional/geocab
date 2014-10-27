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
#import <CoreLocation/CoreLocation.h>

@interface Marker : NSObject

@property (nonatomic, retain) NSNumber *id;
@property (nonatomic, retain) NSString *latitude;
@property (nonatomic, retain) NSString *longitude;
@property (nonatomic, retain) NSData *image;
@property (strong, nonatomic) Layer *layer;
@property (nonatomic) enum StatusMarker status;
@property (nonatomic, retain) NSArray *markerAtrributes;

@end
