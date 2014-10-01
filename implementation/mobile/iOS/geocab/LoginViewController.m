//
//  LoginViewController.m
//  geocab
//
//  Created by Vinicius Ramos Kawamoto on 17/09/14.
//  Copyright (c) 2014 Itaipu. All rights reserved.
//

#import "LoginViewController.h"
#import "GTLPlusConstants.h"
#import <GooglePlus/GooglePlus.h>

@implementation LoginViewController

@synthesize signInButton, fbLoginView;

- (void)viewDidLoad {
    [super viewDidLoad];
    
    GPPSignIn *signIn = [GPPSignIn sharedInstance];
    
    signIn.clientID = [[NSBundle mainBundle] objectForInfoDictionaryKey:@"kClientId"];
    signIn.scopes = [NSArray arrayWithObjects:kGTLAuthScopePlusLogin,
                     nil];
    signIn.shouldFetchGooglePlusUser = YES;
    signIn.shouldFetchGoogleUserEmail = YES;
    signIn.shouldFetchGoogleUserID = YES;
    signIn.delegate = self;
    
    self.fbLoginView.delegate = self;
    self.fbLoginView.readPermissions = @[@"public_profile", @"email", @"user_friends"];
}

//Method to make the keyboard disappear when touch happens out of the text field
- (void)touchesBegan:(NSSet *)touches withEvent:(UIEvent *)event {
    [self.view endEditing:YES];
}


- (IBAction)btLoginGoogle:(id)sender {
    
}

- (IBAction)btLoginFacebook:(id)sender {
    UIAlertView *alert = [[UIAlertView alloc] initWithTitle:@"Teste"
                                                    message:@"Teste facebook"
                                                   delegate:nil
                                          cancelButtonTitle:@"OK"
                                          otherButtonTitles:nil];
    [alert show];

}

- (void)finishedWithAuth:(GTMOAuth2Authentication *)auth error:(NSError *)error {
    //NSLog(@"Received error %@ and auth object %@", error, auth);
    if (error) {
        NSLog(@"Error %@", error);
    } else {
        NSLog(@"auth: %@", auth);
        NSLog(@"user email: %@", [[GPPSignIn sharedInstance] userEmail]);
        NSLog(@"google user: %@", [[GPPSignIn sharedInstance] googlePlusUser]);
    }
    
}

- (void)loginViewFetchedUserInfo:(FBLoginView *)loginView user:(id<FBGraphUser>)user {
    NSLog(@"FB - user name: %@", user.name);
}

- (void)presentSignInViewController: (UIViewController *)viewController {
    [[self navigationController] pushViewController:viewController animated:YES];
}

@end
