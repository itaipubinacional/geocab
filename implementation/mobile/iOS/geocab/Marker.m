//
//  Marker.m
//  geocab
//
//  Created by Henrique Lobato on 06/10/14.
//  Copyright (c) 2014 Itaipu. All rights reserved.
//

#import "Marker.h"
#import "MarkerAttribute.h"
#import "MarkerDelegate.h"

@implementation Marker

@synthesize id, created, wktCoordenate, layer, status, imageUI, imageData, markerAttributes, user;

+ (NSDictionary*) generateDictionary {
    
    return @{
             @"id"               : @"id",
             @"created"          : @"created",
             @"wktCoordenate"    : @"wktCoordenate",
             @"status"           : @"status"
            };
    
}

+ (NSString *)dateStrToTimeStamp:(NSString *) dateStr {
    
    // Converter string para date object
    NSDateFormatter *dateFormat = [[NSDateFormatter alloc] init];
    [dateFormat setDateFormat:@"dd/MM/yyyy"];
    NSDate *date = [dateFormat dateFromString:dateStr];
	return [NSString stringWithFormat:@"%ld", (long)[date timeIntervalSince1970]];
    
}

+ (NSString *)formatDate:(NSDate *)date {
    
    NSDateFormatter *dateFormat = [[NSDateFormatter alloc] init];
    [dateFormat setDateFormat:@"dd/MM/yyyy"];
    return [dateFormat stringFromDate:date];
    
}

- (NSString *)toJSONString {
    
    MarkerDelegate *delegate = [[MarkerDelegate alloc] init];
    RKObjectMapping *mapping = [delegate mapping];
    
    // MarkerAttribute
    RKObjectMapping *mappingMarkerAttr = [RKObjectMapping requestMapping];
    [mappingMarkerAttr addAttributeMappingsFromDictionary: [MarkerAttribute generateDictionary]];
    [mapping addPropertyMapping:[RKRelationshipMapping relationshipMappingFromKeyPath:@"markerAttributes" toKeyPath:@"markerAttributes" withMapping:mappingMarkerAttr]];
    
    // Attribute
    RKObjectMapping *mappingAttr = [RKObjectMapping requestMapping];
    [mappingAttr addAttributeMappingsFromDictionary: [Attribute generateDictionary]];
    [mappingMarkerAttr addPropertyMapping:[RKRelationshipMapping relationshipMappingFromKeyPath:@"attribute" toKeyPath:@"attribute" withMapping:mappingAttr]];
    
    RKRequestDescriptor *requestDescriptor = [RKRequestDescriptor requestDescriptorWithMapping:mapping.inverseMapping objectClass:[Marker class] rootKeyPath:nil];
    
    NSError *error;
    NSDictionary *parameters = [RKObjectParameterization parametersWithObject:self requestDescriptor:requestDescriptor error:&error];
    
    // Serialize the object to JSON
    NSData *JSON = [RKMIMETypeSerialization dataFromObject:parameters MIMEType:RKMIMETypeJSON error:&error];
    NSString *jsonString = [[NSString alloc] initWithBytes:[JSON bytes]
                                                    length:[JSON length] encoding:NSUTF8StringEncoding];
    return jsonString;
    
}

+ (Marker *)fromJSONString:(NSString *)jsonString {
    
    Marker *marker = [[Marker alloc] init];
    NSString* MIMEType = @"application/json";
    NSError* error;
    NSData *data = [jsonString dataUsingEncoding:NSUTF8StringEncoding];
    
    id parsedData = [RKMIMETypeSerialization objectFromData:data MIMEType:MIMEType error:&error];
    
    MarkerDelegate *markerDelegate = [[MarkerDelegate alloc] init];
    NSDictionary *mappingsDictionary = @{ [NSNull null] : [markerDelegate mapping]};
    RKMapperOperation *mapper = [[RKMapperOperation alloc] initWithRepresentation:parsedData mappingsDictionary:mappingsDictionary];
    mapper.targetObject = marker;
    [mapper execute:nil];
    
    return marker;
    
}

@end
