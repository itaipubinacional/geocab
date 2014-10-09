//
//  AbstractDelegate.h
//  geocab
//
//  Created by Henrique Lobato on 06/10/14.
//  Copyright (c) 2014 Itaipu. All rights reserved.
//

#import <Foundation/Foundation.h>
#import <RestKit/RestKit.h>

@interface AbstractDelegate : NSObject

@property (nonatomic, retain) NSMutableString *baseUrl;

- (id) initWithUrl: (NSString *) url;
- (RKObjectMapping *) mapping;
- (void) list: (void (^)(RKObjectRequestOperation *operation, RKMappingResult *result)) successBlock;

@end
