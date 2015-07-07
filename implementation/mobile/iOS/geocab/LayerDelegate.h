//
//  LayerDelegate.h
//  geocab
//
//  Created by Henrique Lobato on 06/10/14.
//  Copyright (c) 2014 Itaipu. All rights reserved.
//

#import "AbstractDelegate.h"
#import "DataSource.h"

@interface LayerDelegate : AbstractDelegate

- (void) list: (void (^)(RKObjectRequestOperation *operation, RKMappingResult *result)) successBlock failBlock: (void (^)(RKObjectRequestOperation *operation, NSError *error)) failBlock;

- (void) listAttributesById: (void (^)(RKObjectRequestOperation *operation, RKMappingResult *result)) successBlock layerId: (NSNumber*) layerId;

- (void) listProperties: (void (^)(RKObjectRequestOperation *operation, RKMappingResult *result)) successBlock dataSource:(NSArray*)dataSource;

@end
