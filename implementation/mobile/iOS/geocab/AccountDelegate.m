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



- (void) userWithEmail: (User *)user successBlock: (void (^)(RKObjectRequestOperation *operation, RKMappingResult *result)) successBlock failureBlock: (void (^)(RKObjectRequestOperation *operation, NSError *error)) failureBlock
{
    NSDictionary *params = @{@"username":user.email};
    
    RKResponseDescriptor *responseDescriptor = [RKResponseDescriptor responseDescriptorWithMapping:self.mapping method:RKRequestMethodPOST pathPattern:nil keyPath:nil statusCodes:nil];
    
    [[RKObjectManager sharedManager].HTTPClient setDefaultHeader:@"Authorization" value:user.credentials];
    [[RKObjectManager sharedManager] addResponseDescriptor:responseDescriptor];
    [[RKObjectManager sharedManager] postObject:nil path:@"user" parameters:params success:successBlock failure:failureBlock];
}

@end

