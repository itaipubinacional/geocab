//
//  main.m
//  geocab
//
//  Created by Henrique Lobato on 17/09/14.
//  Copyright (c) 2014 Itaipu. All rights reserved.
//

#import <UIKit/UIKit.h>

#import "AppDelegate.h"
#import "User.h"

User *loggedUser;
NSUserDefaults *defaults;

int main(int argc, char * argv[])
{
    defaults = [NSUserDefaults standardUserDefaults];
    
    @autoreleasepool {
        return UIApplicationMain(argc, argv, nil, NSStringFromClass([AppDelegate class]));
    }
}
