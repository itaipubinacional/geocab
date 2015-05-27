//
//  AccountDelegate.m
//  geocab
//
//  Created by Henrique Lobato Zago on 14/10/14.
//  Copyright (c) 2014 Itaipu. All rights reserved.
//

#import "AccountDelegate.h"
#import "User.h"

@implementation AccountDelegate

- (RKObjectMapping *) mapping
{
    RKObjectMapping *mapping = [RKObjectMapping mappingForClass:[User class]];
    [mapping addAttributeMappingsFromDictionary: [User generateDictionary]];
    
    return mapping;
}



- (void) loginWithEmail: (NSString *)email password: (NSString *)password successBlock: (void (^)(RKObjectRequestOperation *operation, RKMappingResult *result)) successBlock failureBlock: (void (^)(RKObjectRequestOperation *operation, NSError *error)) failureBlock
{
    NSString *credentials = [[email stringByAppendingString:@":"] stringByAppendingString:password];
    NSData *credentialsData = [credentials dataUsingEncoding:NSUTF8StringEncoding];
    NSString *credentialsEncoded = [credentialsData base64EncodedStringWithOptions:0];
    
    NSDictionary *params = [[NSDictionary alloc] initWithObjectsAndKeys:credentialsEncoded, @"credentials", nil];
    
    RKResponseDescriptor *responseDescriptor = [RKResponseDescriptor responseDescriptorWithMapping:self.mapping method:RKRequestMethodPOST pathPattern:nil keyPath:nil statusCodes:nil];
    
    [[RKObjectManager sharedManager] addResponseDescriptor:responseDescriptor];
    [[RKObjectManager sharedManager] postObject:nil path:@"check" parameters:params success:successBlock failure:failureBlock];
}

- (void) socialAuthenticate: (NSString *)email name: (NSString *)name successBlock: (void (^)(RKObjectRequestOperation *operation, RKMappingResult *result)) successBlock failureBlock: (void (^)(RKObjectRequestOperation *operation, NSError *error)) failureBlock
{
    NSDictionary *params = [[NSDictionary alloc] initWithObjectsAndKeys:email, @"email", name, @"name", nil];
    
    RKResponseDescriptor *responseDescriptor = [RKResponseDescriptor responseDescriptorWithMapping:self.mapping method:RKRequestMethodPOST pathPattern:nil keyPath:nil statusCodes:nil];
    
    [[RKObjectManager sharedManager] addResponseDescriptor:responseDescriptor];
    [[RKObjectManager sharedManager] postObject:nil path:@"" parameters:params success:successBlock failure:failureBlock];
}

@end

