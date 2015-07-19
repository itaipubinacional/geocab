//
//  LoginViewController.h
//  geocab
//
//  Created by Vinicius Ramos Kawamoto on 17/09/14.
//  Copyright (c) 2014 Itaipu. All rights reserved.
//

#import <UIKit/UIKit.h>
#import <GoogleSignIn/GoogleSignIn.h>
#import <FacebookSDK/FacebookSDK.h>

@interface LoginViewController : UIViewController <GIDSignInDelegate, FBLoginViewDelegate, UITextFieldDelegate>

@property(weak, nonatomic) IBOutlet GIDSignInButton *signInButton;
@property (weak, nonatomic) IBOutlet FBLoginView *fbLoginView;
@property (nonatomic, assign) int facebookCounter;

@end
