//
//  AbstractDelegate.m
//  geocab
//
//  Created by Henrique Lobato on 06/10/14.
//  Copyright (c) 2014 Itaipu. All rights reserved.
//

#import "AbstractDelegate.h"

@implementation AbstractDelegate

- (id)initWithUrl:(NSString *)url
{
    self = [super init];
    
    if (self)
    {
        self.baseUrl = [@"http://192.168.20.122:8080/geocab/" mutableCopy];
        [self.baseUrl appendString:url];
        
        RKObjectManager *objectManager = [RKObjectManager managerWithBaseURL:[NSURL URLWithString:self.baseUrl]];
        [RKObjectManager setSharedManager:objectManager];
        
    }
    
    return self;
}

@end
