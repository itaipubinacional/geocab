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
#import "User.h"

@implementation LayerDelegate

extern User *loggedUser;

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

- (void) list: (void (^)(RKObjectRequestOperation *operation, RKMappingResult *result)) successBlock failBlock: (void (^)(RKObjectRequestOperation *operation, NSError *error)) failBlock {
    RKResponseDescriptor *responseDescriptor = [RKResponseDescriptor responseDescriptorWithMapping:self.mapping method:RKRequestMethodGET pathPattern:nil keyPath:@"" statusCodes:nil];
    
    NSURL* url = [NSURL URLWithString:self.baseUrl];
    RKObjectManager* objectManager = [RKObjectManager managerWithBaseURL:url];
    [objectManager.HTTPClient setDefaultHeader:@"Authorization" value: loggedUser.credentials];
    
    NSURLRequest *request = [objectManager requestWithObject:nil method:RKRequestMethodGET path:@"" parameters:nil];
    
    RKObjectRequestOperation *objectRequestOperation = [[RKObjectRequestOperation alloc] initWithRequest:request responseDescriptors:@[ responseDescriptor ]];
    
    [objectRequestOperation setCompletionBlockWithSuccess:successBlock failure:failBlock];
    [objectRequestOperation start];
}

- (void) listAttributesById: (void (^)(RKObjectRequestOperation *operation, RKMappingResult *result)) successBlock layerId: (NSNumber*) layerId
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
    [objectManager.HTTPClient setDefaultHeader:@"Authorization" value: loggedUser.credentials];
    
    NSURLRequest *request = [objectManager requestWithObject:nil method:RKRequestMethodGET path:[NSString stringWithFormat:@"%@/layerattributes", layerId] parameters:nil];
    
    RKObjectRequestOperation *objectRequestOperation = [[RKObjectRequestOperation alloc] initWithRequest:request responseDescriptors:@[ responseDescriptor ]];
    
    [objectRequestOperation setCompletionBlockWithSuccess:successBlock failure:nil];
    [objectRequestOperation start];
}

- (void) listProperties: (void (^)(RKObjectRequestOperation *operation, RKMappingResult *result)) successBlock dataSource:(NSArray*)dataSource
{
    NSMutableArray *dataSourceArray = [NSMutableArray array];
    for (NSString *url in dataSource) {
        DataSource *dataSource = [[DataSource alloc] init];
        dataSource.url = url;
        [dataSourceArray addObject:dataSource];
    }

    // Response Mapeamentos
    NSIndexSet *statusCodes = RKStatusCodeIndexSetForClass(RKStatusCodeClassSuccessful);
    RKObjectMapping *responseMapping = [RKObjectMapping mappingForClass:[DataSource class]];
    RKResponseDescriptor *responseDescriptor = [RKResponseDescriptor responseDescriptorWithMapping:responseMapping method:RKRequestMethodAny pathPattern:nil keyPath:@"" statusCodes:statusCodes];
    
    // Request Mapeamentos
    RKObjectMapping *requestMapping = [RKObjectMapping requestMapping];
    [requestMapping addAttributeMappingsFromDictionary: @{@"url":@"url"}];
    
    // Request
    RKRequestDescriptor *requestDescriptor = [RKRequestDescriptor requestDescriptorWithMapping:requestMapping objectClass:[DataSource class] rootKeyPath:nil method:RKRequestMethodAny];
    
    // Configurações da requisição
    NSURL* url = [NSURL URLWithString:self.baseUrl];
    RKObjectManager *manager = [RKObjectManager managerWithBaseURL:url];
    [manager.HTTPClient setDefaultHeader:@"Authorization" value: loggedUser.credentials];
    [manager setRequestSerializationMIMEType:RKMIMETypeJSON];
    [manager addRequestDescriptor:requestDescriptor];
    [manager addResponseDescriptor:responseDescriptor];
    
    // POST to create
    [manager postObject:dataSourceArray path:@"layergroup/layerproperties" parameters:nil success:successBlock failure:nil];
}

@end
