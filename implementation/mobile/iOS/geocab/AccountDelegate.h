//
//  AccountDelegate.h
//  geocab
//
//  Created by Henrique Lobato Zago on 14/10/14.
//  Copyright (c) 2014 Itaipu. All rights reserved.
//

#import "AbstractDelegate.h"
#import "User.h"

@interface AccountDelegate : AbstractDelegate

- (RKObjectMapping *) mapping;

- (void) userWithEmail: (User *)user successBlock: (void (^)(RKObjectRequestOperation *operation, RKMappingResult *result)) successBlock failureBlock: (void (^)(RKObjectRequestOperation *operation, NSError *error)) failureBlock;

@end
