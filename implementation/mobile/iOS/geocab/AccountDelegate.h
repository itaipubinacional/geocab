//
//  AccountDelegate.h
//  geocab
//
//  Created by Henrique Lobato Zago on 14/10/14.
//  Copyright (c) 2014 Itaipu. All rights reserved.
//

#import "AbstractDelegate.h"

@interface AccountDelegate : AbstractDelegate

- (RKObjectMapping *) mapping;

- (void) loginWithEmail: (NSString *)email password: (NSString *)password successBlock: (void (^)(RKObjectRequestOperation *operation, RKMappingResult *result)) successBlock failureBlock: (void (^)(RKObjectRequestOperation *operation, NSError *error)) failureBlock;

- (void) socialAuthenticate: (NSString *)email name: (NSString *)name successBlock: (void (^)(RKObjectRequestOperation *operation, RKMappingResult *result)) successBlock failureBlock: (void (^)(RKObjectRequestOperation *operation, NSError *error)) failureBlock;

@end
