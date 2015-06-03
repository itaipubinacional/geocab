//
//  MarkerDelegate.h
//  geocab
//
//  Created by Henrique Lobato Zago on 22/10/14.
//  Copyright (c) 2014 Itaipu. All rights reserved.
//

#import "AbstractDelegate.h"
#import "Marker.h"
#import "MotiveMarkerModeration.h"

@interface MarkerDelegate : AbstractDelegate

- (void) list: (void (^)(RKObjectRequestOperation *operation, RKMappingResult *result)) successBlock failBlock: (void (^)(RKObjectRequestOperation *operation, NSError *error)) failBlock userName:(NSString*)userName password:(NSString*)password layerId: (NSNumber*) layerId;

- (void) listAttributesById: (void (^)(RKObjectRequestOperation *operation, RKMappingResult *result)) successBlock userName:(NSString*)userName password:(NSString*)password markerId: (NSNumber*) markerId;

- (void) insert: (void (^)(RKObjectRequestOperation *operation, RKMappingResult *result)) successBlock userName:(NSString*)userName password:(NSString*)password marker:(Marker*)marker;

- (void) update: (void (^)(RKObjectRequestOperation *operation, RKMappingResult *result)) successBlock userName:(NSString*)userName password:(NSString*)password marker:(Marker*)marker;

- (void) remove:(NSString*)userName password:(NSString*)password markerId:(NSNumber*)markerId;

- (void) approve:(NSString*)userName password:(NSString*)password markerId:(NSNumber*)markerId;

- (void) refuse:(NSString*)userName password:(NSString*)password markerId:(NSNumber*)markerId motiveMarkerModeration:(MotiveMarkerModeration *)motiveMarkerModeration;

- (void) listMotives: (void (^)(RKObjectRequestOperation *operation, RKMappingResult *result)) successBlock failBlock: (void (^)(RKObjectRequestOperation *operation, NSError *error)) failBlock userName:(NSString*)userName password:(NSString*)password;

@end
