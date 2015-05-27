//
//  LayerDelegate.m
//  geocab
//
//  Created by Henrique Lobato on 06/10/14.
//  Copyright (c) 2014 Itaipu. All rights reserved.
//

#import "LayerDelegate.h"
#import "Layer.h"
#import "Attribute.h"

@implementation LayerDelegate

- (RKObjectMapping *) mapping
{
    RKObjectMapping *mapping = [RKObjectMapping mappingForClass:[Layer class]];
    [mapping addAttributeMappingsFromDictionary: [Layer generateDictionary]];
    
    RKObjectMapping *dataSourceMapping = [RKObjectMapping mappingForClass:[DataSource class]];
    [dataSourceMapping addAttributeMappingsFromDictionary:@{
                                                            @"name"        : @"name",
                                                            @"url"         : @"url",
                                                            @"login"       : @"login",
                                                            @"password"    : @"password",
                                                            }];
    
    [mapping addPropertyMapping:[RKRelationshipMapping relationshipMappingFromKeyPath:@"dataSource" toKeyPath:@"dataSource" withMapping:dataSourceMapping]];
    
    return mapping;
}

- (void) list: (void (^)(RKObjectRequestOperation *operation, RKMappingResult *result)) successBlock failBlock: (void (^)(RKObjectRequestOperation *operation, NSError *error)) failBlock userName:(NSString*)userName password:(NSString*)password
{
    RKResponseDescriptor *responseDescriptor = [RKResponseDescriptor responseDescriptorWithMapping:self.mapping method:RKRequestMethodGET pathPattern:nil keyPath:@"" statusCodes:nil];
    
    NSURL* url = [NSURL URLWithString:self.baseUrl];
    RKObjectManager* objectManager = [RKObjectManager managerWithBaseURL:url];
    [objectManager.HTTPClient setAuthorizationHeaderWithUsername:userName password:password];
    
    NSURLRequest *request = [objectManager requestWithObject:nil method:RKRequestMethodGET path:@"" parameters:nil];
    
    RKObjectRequestOperation *objectRequestOperation = [[RKObjectRequestOperation alloc] initWithRequest:request responseDescriptors:@[ responseDescriptor ]];
    
    [objectRequestOperation setCompletionBlockWithSuccess:successBlock failure:failBlock];
    [objectRequestOperation start];
}

- (void) listAttributesById: (void (^)(RKObjectRequestOperation *operation, RKMappingResult *result)) successBlock userName:(NSString*)userName password:(NSString*)password layerId: (NSNumber*) layerId
{
    RKObjectMapping *mapping = [RKObjectMapping mappingForClass:[Attribute class]];
    [mapping addAttributeMappingsFromDictionary:@{
                                                           @"id"       : @"id",
                                                           @"name"     : @"name",
                                                           @"type"     : @"type",
                                                           @"required" : @"required"
                                                           }];
    
    RKResponseDescriptor *responseDescriptor = [RKResponseDescriptor responseDescriptorWithMapping:mapping method:RKRequestMethodGET pathPattern:nil keyPath:@"" statusCodes:nil];
    
    NSURL* url = [NSURL URLWithString:self.baseUrl];
    RKObjectManager* objectManager = [RKObjectManager managerWithBaseURL:url];
    [objectManager.HTTPClient setAuthorizationHeaderWithUsername:userName password:password];
    
    NSURLRequest *request = [objectManager requestWithObject:nil method:RKRequestMethodGET path:[NSString stringWithFormat:@"%@/layerattributes", layerId] parameters:nil];
    
    RKObjectRequestOperation *objectRequestOperation = [[RKObjectRequestOperation alloc] initWithRequest:request responseDescriptors:@[ responseDescriptor ]];
    
    [objectRequestOperation setCompletionBlockWithSuccess:successBlock failure:nil];
    [objectRequestOperation start];
}

@end
