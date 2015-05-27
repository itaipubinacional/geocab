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

- (void) downloadMarkerAttributePhoto: (NSNumber *) markerId success: (void(^)(AFHTTPRequestOperation *operation, id responseObject)) success fail: (void(^)(AFHTTPRequestOperation *operation, NSError *error)) fail login:(NSString*)login password:(NSString*)password
{
    NSString *urlString = [[self.baseUrl stringByAppendingString:[markerId stringValue]] stringByAppendingString:@"/download"];
    
    NSURL* url = [NSURL URLWithString:urlString];
    RKObjectManager* objectManager = [RKObjectManager managerWithBaseURL:url];
    [objectManager.HTTPClient setAuthorizationHeaderWithUsername:login password:password];
    
    NSURLRequest *request = [objectManager requestWithObject:nil method:RKRequestMethodGET path:@"" parameters:nil];
    
    NSMutableURLRequest *downloadRequest = [objectManager requestWithObject:request method:RKRequestMethodGET path:urlString parameters:nil];
    AFHTTPRequestOperation *requestOperation = [[AFImageRequestOperation alloc] initWithRequest:downloadRequest];
    
    [requestOperation setCompletionBlockWithSuccess:success failure:fail];
    [objectManager.HTTPClient enqueueHTTPRequestOperation:requestOperation];
}

- (void) uploadMarkerAttributePhoto:(NSNumber *)markerId image:(UIImage *)image login:(NSString*)login password:(NSString*)password
{
    // the server url to which the image (or the media) is uploaded.
    NSString *urlString = [[self.baseUrl stringByAppendingString:[markerId stringValue]] stringByAppendingString:@"/uploadphoto"];
    NSURL* requestURL = [NSURL URLWithString:urlString];    
    
    // random string, that will not repeat in post data, to separate post data fields.
    NSString *BoundaryConstant = @"----------V2ymHFg03ehbqgZCaKO6jy";
    
    // string constant for the post parameter 'file'.
    NSString* FileParamConstant = @"file";
    
    // create request
    NSMutableURLRequest *request = [[NSMutableURLRequest alloc] init];
    NSString *credentials = [[login stringByAppendingString:@":"] stringByAppendingString:password];
    NSData *credentialsData = [credentials dataUsingEncoding:NSUTF8StringEncoding];
    NSString *credentialsEncoded = [credentialsData base64EncodedStringWithOptions:0];
    [request setValue:credentialsEncoded forHTTPHeaderField:@"Authorization"];
    
    [request setCachePolicy:NSURLRequestReloadIgnoringLocalCacheData];
    [request setHTTPShouldHandleCookies:NO];
    [request setTimeoutInterval:30];
    [request setHTTPMethod:@"POST"];
    
    // set Content-Type in HTTP header
    NSString *contentType = [NSString stringWithFormat:@"multipart/form-data; boundary=%@", BoundaryConstant];
    [request setValue:contentType forHTTPHeaderField: @"Content-Type"];
    
    // post body
    NSMutableData *body = [NSMutableData data];
    
    // add image data
    NSData *imageData = UIImageJPEGRepresentation(image, 1.0);
    if (imageData) {
        [body appendData:[[NSString stringWithFormat:@"--%@\r\n", BoundaryConstant] dataUsingEncoding:NSUTF8StringEncoding]];
        [body appendData:[[NSString stringWithFormat:@"Content-Disposition: form-data; name=\"%@\"; filename=\"marker-foto.jpg\"\r\n", FileParamConstant] dataUsingEncoding:NSUTF8StringEncoding]];
        [body appendData:[@"Content-Type: image/jpeg\r\n\r\n" dataUsingEncoding:NSUTF8StringEncoding]];
        [body appendData:imageData];
        [body appendData:[[NSString stringWithFormat:@"\r\n"] dataUsingEncoding:NSUTF8StringEncoding]];
    }
    
    [body appendData:[[NSString stringWithFormat:@"--%@--\r\n", BoundaryConstant] dataUsingEncoding:NSUTF8StringEncoding]];
    
    // setting the body of the post to the reqeust
    [request setHTTPBody:body];
    
    // set the content-length
    NSString *postLength = [NSString stringWithFormat:@"%d", [body length]];
    [request setValue:postLength forHTTPHeaderField:@"Content-Length"];
    
    // set URL
    [request setURL:requestURL];
    NSOperationQueue *queue = [[NSOperationQueue alloc] init];
    [NSURLConnection sendAsynchronousRequest:request queue:queue completionHandler:nil];
    
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
    [manager postObject:marker path:@"/marker/" parameters:nil success:successBlock failure:nil];
    
}

- (void) update: (void (^)(RKObjectRequestOperation *operation, RKMappingResult *result)) successBlock userName:(NSString*)userName password:(NSString*)password marker:(Marker*)marker
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
    [manager putObject:marker path:@"/marker/" parameters:nil success:successBlock failure:nil];
    
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

	[NSURLConnection sendSynchronousRequest:request returningResponse:&response error:&error];
}

- (void) approve:(NSString*)userName password:(NSString*)password markerId:(NSNumber*)markerId
{
    NSURLResponse * response = nil;
    NSError * error = nil;
    NSURL *url = [NSURL URLWithString: [NSString stringWithFormat:@"%@/%@/approve", self.baseUrl, markerId]];
    
    NSMutableURLRequest *request = [NSMutableURLRequest requestWithURL:url cachePolicy:NSURLRequestUseProtocolCachePolicy timeoutInterval:60.0];
    
    [request setHTTPMethod:@"PUT"];
    [request setValue:@"text/plain" forHTTPHeaderField:@"Accept"];
    [request setValue:@"text/plain" forHTTPHeaderField:@"Content-Type"];
    
    [NSURLConnection sendSynchronousRequest:request returningResponse:&response error:&error];
}

- (void) refuse:(NSString*)userName password:(NSString*)password markerId:(NSNumber*)markerId
{
    NSURLResponse * response = nil;
    NSError * error = nil;
    NSURL *url = [NSURL URLWithString: [NSString stringWithFormat:@"%@/%@/refuse", self.baseUrl, markerId]];
    
    NSMutableURLRequest *request = [NSMutableURLRequest requestWithURL:url cachePolicy:NSURLRequestUseProtocolCachePolicy timeoutInterval:60.0];
    
    [request setHTTPMethod:@"PUT"];
    [request setValue:@"text/plain" forHTTPHeaderField:@"Accept"];
    [request setValue:@"text/plain" forHTTPHeaderField:@"Content-Type"];
    
    [NSURLConnection sendSynchronousRequest:request returningResponse:&response error:&error];
}

@end
