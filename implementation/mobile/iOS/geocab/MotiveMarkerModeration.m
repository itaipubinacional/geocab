//
//  MotiveMarkerModeration.m
//  geocab
//
//  Created by Joaz on 28/05/15.
//  Copyright (c) 2015 Itaipu. All rights reserved.
//

#import "MotiveMarkerModeration.h"
#import "MarkerDelegate.h"

@implementation MotiveMarkerModeration

@synthesize id, description, motive;

+ (NSDictionary*) generateDictionary {
    
    return @{
             @"id"          : @"id",
             @"description" : @"description"
             };
    
}

+ (MotiveMarkerModeration *)fromJSONString:(NSString *)jsonString {
    
    MotiveMarkerModeration *motiveMarker = [[MotiveMarkerModeration alloc] init];
    NSString* MIMEType = @"application/json";
    NSError* error;
    NSData *data = [jsonString dataUsingEncoding:NSUTF8StringEncoding];
    
    id parsedData = [RKMIMETypeSerialization objectFromData:data MIMEType:MIMEType error:&error];
    
    RKObjectMapping *requestMapping = [RKObjectMapping mappingForClass:[MotiveMarkerModeration class]];
    [requestMapping addAttributeMappingsFromDictionary: [MotiveMarkerModeration generateDictionary]];
    
    RKObjectMapping *mappingMotive = [RKObjectMapping mappingForClass:[Motive class]];
    [mappingMotive addAttributeMappingsFromDictionary: [Motive generateDictionary]];
    [requestMapping addPropertyMapping:[RKRelationshipMapping relationshipMappingFromKeyPath:@"motive" toKeyPath:@"motive" withMapping:mappingMotive]];
    
    NSDictionary *mappingsDictionary = @{ [NSNull null] : requestMapping};
    
    RKMapperOperation *mapper = [[RKMapperOperation alloc] initWithRepresentation:parsedData mappingsDictionary:mappingsDictionary];
    
    mapper.targetObject = motiveMarker;
    
    [mapper execute:nil];
    
    return motiveMarker;
    
}

@end
