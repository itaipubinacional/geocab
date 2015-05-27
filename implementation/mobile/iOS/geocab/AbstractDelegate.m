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
    RKLogConfigureByName("RestKit/Network", RKLogLevelTrace);
    
    self = [super init];
    
    if (self)
    {
        #warning Verificar endereço base
        self.baseUrl = [@"http://geocab.sbox.me/" mutableCopy];
        [self.baseUrl appendString:url];
        
        RKObjectManager *objectManager = [RKObjectManager managerWithBaseURL:[NSURL URLWithString:self.baseUrl]];
        [RKObjectManager setSharedManager:objectManager];
        
    }
    
    return self;
}

@end
