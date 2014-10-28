//
//  MarkerDelegate.m
//  geocab
//
//  Created by Henrique Lobato Zago on 22/10/14.
//  Copyright (c) 2014 Itaipu. All rights reserved.
//

#import "MarkerDelegate.h"
#import "Marker.h"
#import "MarkerAttribute.h"
#import "Attribute.h"
#import "LayerDelegate.h"

@implementation MarkerDelegate

- (RKObjectMapping *) mapping
{
    RKObjectMapping *mapping = [RKObjectMapping mappingForClass:[Marker class]];
    [mapping addAttributeMappingsFromDictionary:@{
                                                  @"id"               : @"id",
                                                  @"image"            : @"image",
                                                  @"latitude"         : @"latitude",
                                                  @"longitude"        : @"longitude",
                                                  @"legend"           : @"legend",
                                                  @"status"           : @"status"
                                                  }];
    
    LayerDelegate *layerDelegate = [[LayerDelegate alloc] init];
    
    [mapping addPropertyMapping:[RKRelationshipMapping relationshipMappingFromKeyPath:@"layer" toKeyPath:@"layer" withMapping:[layerDelegate mapping]]];
    
    return mapping;
}

- (void) list: (void (^)(RKObjectRequestOperation *operation, RKMappingResult *result)) successBlock userName:(NSString*)userName password:(NSString*)password layerId: (NSNumber*) layerId
{
    RKResponseDescriptor *responseDescriptor = [RKResponseDescriptor responseDescriptorWithMapping:self.mapping method:RKRequestMethodGET pathPattern:nil keyPath:@"" statusCodes:nil];
    
    NSURL* url = [NSURL URLWithString:self.baseUrl];
    RKObjectManager* objectManager = [RKObjectManager managerWithBaseURL:url];
    [objectManager.HTTPClient setAuthorizationHeaderWithUsername:userName password:password];
    
    NSURLRequest *request = [objectManager requestWithObject:nil method:RKRequestMethodGET path:[NSString stringWithFormat:@"%@/markers", layerId] parameters:nil];
    
    RKObjectRequestOperation *objectRequestOperation = [[RKObjectRequestOperation alloc] initWithRequest:request responseDescriptors:@[ responseDescriptor ]];
    
    [objectRequestOperation setCompletionBlockWithSuccess:successBlock failure:nil];
    [objectRequestOperation start];
}

- (void) listAttributesById: (void (^)(RKObjectRequestOperation *operation, RKMappingResult *result)) successBlock userName:(NSString*)userName password:(NSString*)password markerId: (NSNumber*) markerId
{
    RKObjectMapping *mapping = [RKObjectMapping mappingForClass:[MarkerAttribute class]];
    [mapping addAttributeMappingsFromDictionary:@{
                                                  @"id"     : @"id",
                                                  @"value"  : @"value"
                                                  }];
    
    LayerDelegate *layerDelegate = [[LayerDelegate alloc] init];
    [mapping addPropertyMapping:[RKRelationshipMapping relationshipMappingFromKeyPath:@"layer" toKeyPath:@"layer" withMapping:[layerDelegate mapping]]];
    [mapping addPropertyMapping:[RKRelationshipMapping relationshipMappingFromKeyPath:@"marker" toKeyPath:@"marker" withMapping:self.mapping]];
    
    RKObjectMapping *attributeMapping = [RKObjectMapping mappingForClass:[Attribute class]];
    [attributeMapping addAttributeMappingsFromDictionary:@{
                                                  @"id"       : @"id",
                                                  @"name"     : @"name",
                                                  @"type"     : @"type",
                                                  @"required" : @"required"
                                                  }];
    
    [mapping addPropertyMapping:[RKRelationshipMapping relationshipMappingFromKeyPath:@"attribute" toKeyPath:@"attribute" withMapping:attributeMapping]];
    
    RKResponseDescriptor *responseDescriptor = [RKResponseDescriptor responseDescriptorWithMapping:mapping method:RKRequestMethodGET pathPattern:nil keyPath:@"" statusCodes:nil];
    
    NSURL* url = [NSURL URLWithString:self.baseUrl];
    RKObjectManager* objectManager = [RKObjectManager managerWithBaseURL:url];
    [objectManager.HTTPClient setAuthorizationHeaderWithUsername:userName password:password];
    
    NSURLRequest *request = [objectManager requestWithObject:nil method:RKRequestMethodGET path:[NSString stringWithFormat:@"%@/markerattributes", markerId] parameters:nil];
    
    RKObjectRequestOperation *objectRequestOperation = [[RKObjectRequestOperation alloc] initWithRequest:request responseDescriptors:@[ responseDescriptor ]];
    
    [objectRequestOperation setCompletionBlockWithSuccess:successBlock failure:nil];
    [objectRequestOperation start];
}

- (void) downloadMarkerAttributePhoto: (NSNumber *) markerId success: (void(^)(AFHTTPRequestOperation *operation, id responseObject)) success login:(NSString*)login password:(NSString*)password
{
    NSString *urlString = [[self.baseUrl stringByAppendingString:[markerId stringValue]] stringByAppendingString:@"/download"];
    
    NSURL* url = [NSURL URLWithString:urlString];
    RKObjectManager* objectManager = [RKObjectManager managerWithBaseURL:url];
    [objectManager.HTTPClient setAuthorizationHeaderWithUsername:login password:password];
    
    NSURLRequest *request = [objectManager requestWithObject:nil method:RKRequestMethodGET path:@"" parameters:nil];
    
    NSMutableURLRequest *downloadRequest = [objectManager requestWithObject:request method:RKRequestMethodGET path:urlString parameters:nil];
    AFHTTPRequestOperation *requestOperation = [[AFImageRequestOperation alloc] initWithRequest:downloadRequest];
    
    [requestOperation setCompletionBlockWithSuccess:success failure:nil];
    [objectManager.HTTPClient enqueueHTTPRequestOperation:requestOperation];
}

@end
