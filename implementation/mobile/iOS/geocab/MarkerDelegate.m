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
#import "Motive.h"
#import "LayerDelegate.h"
#import "AccountDelegate.h"

@implementation MarkerDelegate

- (RKObjectMapping *) mapping
{
    RKObjectMapping *mapping = [RKObjectMapping mappingForClass:[Marker class]];
    [mapping addAttributeMappingsFromDictionary: [Marker generateDictionary]];
    
    LayerDelegate *layerDelegate = [[LayerDelegate alloc] init];
    AccountDelegate *accountDelegate = [[AccountDelegate alloc] init];
    
    [mapping addPropertyMapping:[RKRelationshipMapping relationshipMappingFromKeyPath:@"layer" toKeyPath:@"layer" withMapping:[layerDelegate mapping]]];
    
    [mapping addPropertyMapping:[RKRelationshipMapping relationshipMappingFromKeyPath:@"user" toKeyPath:@"user" withMapping:[accountDelegate mapping]]];
    
    return mapping;
}

- (void) list: (void (^)(RKObjectRequestOperation *operation, RKMappingResult *result)) successBlock failBlock: (void (^)(RKObjectRequestOperation *operation, NSError *error)) failBlock userName:(NSString*)userName password:(NSString*)password layerId: (NSNumber*) layerId
{
    RKResponseDescriptor *responseDescriptor = [RKResponseDescriptor responseDescriptorWithMapping:self.mapping method:RKRequestMethodGET pathPattern:nil keyPath:@"" statusCodes:nil];
    
    NSURL* url = [NSURL URLWithString:self.baseUrl];
    RKObjectManager* objectManager = [RKObjectManager managerWithBaseURL:url];
    [objectManager.HTTPClient setAuthorizationHeaderWithUsername:userName password:password];
    
    NSURLRequest *request = [objectManager requestWithObject:nil method:RKRequestMethodGET path:[NSString stringWithFormat:@"%@/markers", layerId] parameters:nil];
    
    RKObjectRequestOperation *objectRequestOperation = [[RKObjectRequestOperation alloc] initWithRequest:request responseDescriptors:@[ responseDescriptor ]];
    
    [objectRequestOperation setCompletionBlockWithSuccess:successBlock failure:failBlock];
    [objectRequestOperation start];
}

- (void) listAttributesById: (void (^)(RKObjectRequestOperation *operation, RKMappingResult *result)) successBlock userName:(NSString*)userName password:(NSString*)password markerId: (NSNumber*) markerId
{
    RKObjectMapping *mapping = [RKObjectMapping mappingForClass:[MarkerAttribute class]];
    [mapping addAttributeMappingsFromDictionary:[MarkerAttribute generateDictionary]];
    
    RKObjectMapping *attributeMapping = [RKObjectMapping mappingForClass:[Attribute class]];
    [attributeMapping addAttributeMappingsFromDictionary:[Attribute generateDictionary]];
    
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

- (void) insert: (void (^)(RKObjectRequestOperation *operation, RKMappingResult *result)) successBlock userName:(NSString*)userName password:(NSString*)password marker:(Marker*)marker
{
    // Response
    NSIndexSet *statusCodes = RKStatusCodeIndexSetForClass(RKStatusCodeClassSuccessful);
    RKObjectMapping *responseMapping = [self mapping];
    RKResponseDescriptor *markerDescriptor = [RKResponseDescriptor responseDescriptorWithMapping:responseMapping method:RKRequestMethodAny pathPattern:nil keyPath:@"" statusCodes:statusCodes];
    
    // Mapeamentos
    RKObjectMapping *requestMapping = [RKObjectMapping requestMapping];
	[requestMapping addAttributeMappingsFromDictionary: [Marker generateDictionary]];
    
    RKObjectMapping *mappingLayer = [RKObjectMapping requestMapping];
    [mappingLayer addAttributeMappingsFromDictionary: [Layer generateDictionary]];
    [requestMapping addPropertyMapping:[RKRelationshipMapping relationshipMappingFromKeyPath:@"layer" toKeyPath:@"layer" withMapping:mappingLayer]];
    
    // MarkerAttribute
    RKObjectMapping *mappingMarkerAttr = [RKObjectMapping requestMapping];
    [mappingMarkerAttr addAttributeMappingsFromDictionary: [MarkerAttribute generateDictionary]];
    [requestMapping addPropertyMapping:[RKRelationshipMapping relationshipMappingFromKeyPath:@"markerAttributes" toKeyPath:@"markerAttributes" withMapping:mappingMarkerAttr]];
    
    // Attribute
    RKObjectMapping *mappingAttr = [RKObjectMapping requestMapping];
    [mappingAttr addAttributeMappingsFromDictionary: [Attribute generateDictionary]];
    [mappingMarkerAttr addPropertyMapping:[RKRelationshipMapping relationshipMappingFromKeyPath:@"attribute" toKeyPath:@"attribute" withMapping:mappingAttr]];
    
    RKRequestDescriptor *requestDescriptor = [RKRequestDescriptor requestDescriptorWithMapping:requestMapping objectClass:[Marker class] rootKeyPath:nil method:RKRequestMethodAny];
    
    // Configurações da requisição
    NSURL* url = [NSURL URLWithString:self.baseUrl];
    RKObjectManager *manager = [RKObjectManager managerWithBaseURL:url];
    [manager.HTTPClient setAuthorizationHeaderWithUsername:userName password:password];
    [manager setRequestSerializationMIMEType:RKMIMETypeJSON];
    [manager addRequestDescriptor:requestDescriptor];
    [manager addResponseDescriptor:markerDescriptor];
    
    // POST to create
    [manager postObject:marker path:@"marker/" parameters:nil success:successBlock failure:nil];
    
}

- (void) update: (void (^)(RKObjectRequestOperation *operation, RKMappingResult *result)) successBlock userName:(NSString*)userName password:(NSString*)password marker:(Marker*)marker
{
    // Response
    NSIndexSet *statusCodes = RKStatusCodeIndexSetForClass(RKStatusCodeClassSuccessful);
    RKObjectMapping *responseMapping = [self mapping];
    RKResponseDescriptor *markerDescriptor = [RKResponseDescriptor responseDescriptorWithMapping:responseMapping method:RKRequestMethodAny pathPattern:nil keyPath:@"" statusCodes:statusCodes];
    
    // Mapeamentos
    RKObjectMapping *requestMapping = [RKObjectMapping requestMapping];
    [requestMapping addAttributeMappingsFromDictionary: @{
		@"id":  @"id",
		@"created" : @"created",
		@"wktCoordenate" : @"wktCoordenate",
        @"status" : @"status",
        @"imageToDelete" : @"imageToDelete",
	}];
    
    RKObjectMapping *mappingLayer = [RKObjectMapping requestMapping];
    [mappingLayer addAttributeMappingsFromDictionary: [Layer generateDictionary]];
    [requestMapping addPropertyMapping:[RKRelationshipMapping relationshipMappingFromKeyPath:@"layer" toKeyPath:@"layer" withMapping:mappingLayer]];
    
    RKObjectMapping *mappingUser = [RKObjectMapping requestMapping];
    [mappingUser addAttributeMappingsFromDictionary: @{@"id":@"id"}];
    [requestMapping addPropertyMapping:[RKRelationshipMapping relationshipMappingFromKeyPath:@"user" toKeyPath:@"user" withMapping:mappingUser]];
    
    // MarkerAttribute
    RKObjectMapping *mappingMarkerAttr = [RKObjectMapping requestMapping];
    [mappingMarkerAttr addAttributeMappingsFromDictionary: [MarkerAttribute generateDictionary]];
    [requestMapping addPropertyMapping:[RKRelationshipMapping relationshipMappingFromKeyPath:@"markerAttributes" toKeyPath:@"markerAttributes" withMapping:mappingMarkerAttr]];
    
    // Attribute
    RKObjectMapping *mappingAttr = [RKObjectMapping requestMapping];
    [mappingAttr addAttributeMappingsFromDictionary: [Attribute generateDictionary]];
    [mappingMarkerAttr addPropertyMapping:[RKRelationshipMapping relationshipMappingFromKeyPath:@"attribute" toKeyPath:@"attribute" withMapping:mappingAttr]];
    
    RKRequestDescriptor *requestDescriptor = [RKRequestDescriptor requestDescriptorWithMapping:requestMapping objectClass:[Marker class] rootKeyPath:nil method:RKRequestMethodAny];
    
    // Configurações da requisição
    NSURL* url = [NSURL URLWithString:self.baseUrl];
    RKObjectManager *manager = [RKObjectManager managerWithBaseURL:url];
    [manager.HTTPClient setAuthorizationHeaderWithUsername:userName password:password];
    [manager setRequestSerializationMIMEType:RKMIMETypeJSON];
    [manager addRequestDescriptor:requestDescriptor];
    [manager addResponseDescriptor:markerDescriptor];
    
    // POST to create
    [manager putObject:marker path:@"marker/" parameters:nil success:successBlock failure:nil];
    
}

- (void) remove:(NSString*)userName password:(NSString*)password markerId:(NSNumber*)markerId
{
    NSURLResponse * response = nil;
    NSError * error = nil;
    NSURL *url = [NSURL URLWithString: [NSString stringWithFormat:@"%@/%@", self.baseUrl, markerId]];
    
    NSMutableURLRequest *request = [NSMutableURLRequest requestWithURL:url cachePolicy:NSURLRequestUseProtocolCachePolicy timeoutInterval:60.0];
    
    [request setHTTPMethod:@"DELETE"];
    [request setValue:@"text/plain" forHTTPHeaderField:@"Accept"];
    [request setValue:@"text/plain" forHTTPHeaderField:@"Content-Type"];
    
    //Authorization
    NSString *credentials = [[userName stringByAppendingString:@":"] stringByAppendingString:password];
    NSData *credentialsData = [credentials dataUsingEncoding:NSUTF8StringEncoding];
    NSString *credentialsEncoded = [credentialsData base64EncodedStringWithOptions:0];
    [request setValue:credentialsEncoded forHTTPHeaderField:@"Authorization"];

	[NSURLConnection sendSynchronousRequest:request returningResponse:&response error:&error];
}

- (void) approve:(NSString*)userName password:(NSString*)password markerId:(NSNumber*)markerId
{
    NSURLResponse * response = nil;
    NSError * error = nil;
    NSURL *url = [NSURL URLWithString: [NSString stringWithFormat:@"%@/%@/approve", self.baseUrl, markerId]];
    
    NSMutableURLRequest *request = [NSMutableURLRequest requestWithURL:url cachePolicy:NSURLRequestUseProtocolCachePolicy timeoutInterval:60.0];
    
    [request setHTTPMethod:@"POST"];
    [request setValue:@"text/plain" forHTTPHeaderField:@"Accept"];
    [request setValue:@"text/plain" forHTTPHeaderField:@"Content-Type"];
    
    [NSURLConnection sendSynchronousRequest:request returningResponse:&response error:&error];
}

- (void) refuse:(NSString*)userName password:(NSString*)password markerId:(NSNumber*)markerId motiveMarkerModeration:(MotiveMarkerModeration *)motiveMarkerModeration;
{
    // Response
    NSIndexSet *statusCodes = RKStatusCodeIndexSetForClass(RKStatusCodeClassSuccessful);
    RKObjectMapping *responseMapping = [RKObjectMapping mappingForClass:[MotiveMarkerModeration class]];
    RKResponseDescriptor *responseDescriptor = [RKResponseDescriptor responseDescriptorWithMapping:responseMapping method:RKRequestMethodAny pathPattern:nil keyPath:@"" statusCodes:statusCodes];
    
    // Mapeamentos
    RKObjectMapping *requestMapping = [RKObjectMapping requestMapping];
    [requestMapping addAttributeMappingsFromDictionary: [MotiveMarkerModeration generateDictionary]];
    
    RKObjectMapping *mappingMotive = [RKObjectMapping requestMapping];
    [mappingMotive addAttributeMappingsFromDictionary: [Motive generateDictionary]];
    [requestMapping addPropertyMapping:[RKRelationshipMapping relationshipMappingFromKeyPath:@"motive" toKeyPath:@"motive" withMapping:mappingMotive]];
    
    RKRequestDescriptor *requestDescriptor = [RKRequestDescriptor requestDescriptorWithMapping:requestMapping objectClass:[MotiveMarkerModeration class] rootKeyPath:nil method:RKRequestMethodAny];
    
    // Configurações da requisição
    NSURL* url = [NSURL URLWithString:self.baseUrl];
    RKObjectManager *manager = [RKObjectManager managerWithBaseURL:url];
    [manager.HTTPClient setAuthorizationHeaderWithUsername:userName password:password];
    [manager setRequestSerializationMIMEType:RKMIMETypeJSON];
    [manager addRequestDescriptor:requestDescriptor];
    [manager addResponseDescriptor:responseDescriptor];
    
    // POST to create
    [manager postObject:motiveMarkerModeration path:[NSString stringWithFormat:@"%@/refuse", markerId] parameters:nil success:nil failure:nil];
}

- (void) listMotives: (void (^)(RKObjectRequestOperation *operation, RKMappingResult *result)) successBlock failBlock: (void (^)(RKObjectRequestOperation *operation, NSError *error)) failBlock userName:(NSString*)userName password:(NSString*)password
{
    RKObjectMapping *responseMapping = [RKObjectMapping mappingForClass:[Motive class]];
    RKResponseDescriptor *responseDescriptor = [RKResponseDescriptor responseDescriptorWithMapping:responseMapping method:RKRequestMethodGET pathPattern:nil keyPath:@"" statusCodes:nil];
    
    NSURL* url = [NSURL URLWithString:self.baseUrl];
    RKObjectManager* objectManager = [RKObjectManager managerWithBaseURL:url];
    [objectManager.HTTPClient setAuthorizationHeaderWithUsername:userName password:password];
    
    NSURLRequest *request = [objectManager requestWithObject:nil method:RKRequestMethodGET path:@"motives" parameters:nil];
    
    RKObjectRequestOperation *objectRequestOperation = [[RKObjectRequestOperation alloc] initWithRequest:request responseDescriptors:@[ responseDescriptor ]];
    
    [objectRequestOperation setCompletionBlockWithSuccess:successBlock failure:failBlock];
    [objectRequestOperation start];
}

@end
