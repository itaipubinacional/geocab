//
//  LoginViewController.h
//  geocab
//
//  Created by Vinicius Ramos Kawamoto on 17/09/14.
//  Copyright (c) 2014 Itaipu. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "GPPSignIn.h"
#import <FacebookSDK/FacebookSDK.h>

@class GPPSignInButton;

@interface LoginViewController : UIViewController <GPPSignInDelegate, FBLoginViewDelegate>

@property (retain, nonatomic) IBOutlet GPPSignInButton *signInButton;
@property (weak, nonatomic) IBOutlet FBLoginView *fbLoginView;

@end
