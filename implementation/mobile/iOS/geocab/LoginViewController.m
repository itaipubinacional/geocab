//
//  LoginViewController.m
//  geocab
//
//  Created by Vinicius Ramos Kawamoto on 17/09/14.
//  Copyright (c) 2014 Itaipu. All rights reserved.
//

#import "LoginViewController.h"
#import <GoogleSignIn/GoogleSignIn.h>
#import "AppDelegate.h"
#import "User.h"
#import "ControllerUtil.h"
#import "AccountDelegate.h"
#import "MBProgressHUD.h"

@interface LoginViewController ()

@property (weak, nonatomic) IBOutlet UITextField *username;
@property (weak, nonatomic) IBOutlet UITextField *password;
@property (copy) GIDSignIn *signIn;
@property (weak, nonatomic) IBOutlet UIButton *standardLoginButton;

extern NSUserDefaults *defaults;
extern User *loggedUser;

@end

@implementation LoginViewController

@synthesize signInButton, fbLoginView;

- (void)viewDidLoad {
    [super viewDidLoad];
    
    _signIn = [GIDSignIn sharedInstance];
	_signIn.shouldFetchBasicProfile = YES;
    _signIn.clientID = [[NSBundle mainBundle] objectForInfoDictionaryKey:@"kClientId"];
    _signIn.scopes = @[ @"https://www.googleapis.com/auth/plus.login" ];
    _signIn.delegate = self;
    _signIn.uiDelegate = self;
    
    [self.navigationController setNavigationBarHidden:YES animated:NO];
    
    _standardLoginButton.backgroundColor = [ControllerUtil colorWithHexString:@"27a7c6"];
    
    _username.delegate = self;
    _password.delegate = self;
    
    self.fbLoginView.delegate = self;
    self.fbLoginView.readPermissions = @[@"public_profile", @"email", @"user_friends"];
    
    for (id obj in self.fbLoginView.subviews) {
        if ([obj isKindOfClass:[UILabel class]]) {
            UILabel *loginLabel = obj;
            loginLabel.text = @"Facebook";
        }
    }
    
    [[GIDSignIn sharedInstance] signInSilently];
}

-(void)viewWillDisappear:(BOOL)animated{
    [super viewWillDisappear:animated];
    self.fbLoginView.delegate = nil;
}

//Method to make the keyboard disappear when touch happens out of the text field
- (void) touchesBegan:(NSSet *)touches withEvent:(UIEvent *)event
{
    UITouch *touch = [[event allTouches] anyObject];
    if ([self.username isFirstResponder] && [touch view] != self.username)
    {
        [self.username resignFirstResponder];
    }
    else if ([self.password isFirstResponder] && [touch view] != self.password)
    {
        [self.password resignFirstResponder];
    }
}

- (IBAction)login:(id)sender {
    
    if ( [ControllerUtil verifyInternetConection] && self.isFormValid ) {
        [MBProgressHUD showHUDAddedTo:self.view animated:YES];
        
        User *authUser = [[User alloc] init];
        [authUser setEmail:_username.text];
        [authUser setPassword:_password.text];
        [authUser setBasicAuthorization];
        
        AccountDelegate *accountDelegate = [[AccountDelegate alloc] initWithUrl:@"authentication/"];
        [accountDelegate userWithEmail:authUser successBlock:^(RKObjectRequestOperation *operation, RKMappingResult *result) {
            
            User *loggedUser = [[User alloc] init];
            loggedUser = [[result array] objectAtIndex:0];
            loggedUser.password = authUser.password;
            loggedUser.credentials = authUser.credentials;
            [defaults setObject:@"basic" forKey:@"auth"];
            [self authenticateUser:loggedUser];
            
        } failureBlock:^(RKObjectRequestOperation *operation, NSError *error) {
            [MBProgressHUD hideHUDForView:self.view animated:YES];
            UIAlertView *errorMessage = [[UIAlertView alloc] initWithTitle:NSLocalizedString(@"error", @"") message:NSLocalizedString(@"login.error.message", @"") delegate:self cancelButtonTitle:@"OK" otherButtonTitles:nil];
            [errorMessage show];
        }];
        
    }
}

- (void) authenticateUser:(User*) user {

    [defaults setObject:user.name forKey:@"name"];
    [defaults setObject:user.email forKey:@"email"];
    [defaults setObject:user.password forKey:@"password"];
    [defaults setObject:user.id forKey:@"userId"];
    [defaults setObject:user.role forKey:@"userRole"];
    
    loggedUser = user;
    [defaults synchronize];
    [MBProgressHUD hideHUDForView:self.view animated:YES];
    [self performSegueWithIdentifier:@"loginToMainSegue" sender:self];
}

- (BOOL)isFormValid {
    if ([_username.text isEqual:@""] || [_password.text isEqual:@""]) {
        UIAlertView *alert = [[UIAlertView alloc] initWithTitle:NSLocalizedString(@"error", @"")
                                                        message:NSLocalizedString(@"verify-form-fields", @"")
                                                       delegate:nil
                                              cancelButtonTitle:@"OK"
                                              otherButtonTitles:nil];
        [alert show];
        return NO;
    }
    
    return YES;
}

//Google plus login callback
- (void)signIn:(GIDSignIn *)signIn didSignInForUser:(GIDGoogleUser *)user withError:(NSError *)error {
    if (error) {
        NSLog(@"Received error %@ and auth object %@", error, signIn);
    } else {
        
		[self clearTextInputs];
        [MBProgressHUD showHUDAddedTo:self.view animated:YES];
        
        NSString *accessToken = user.authentication.accessToken;
        User *authUser = [[User alloc] init];
        [authUser setEmail:user.profile.email];
        [authUser setAccessTokenAuthorization:accessToken provider:@"googleplus" ];
        
        AccountDelegate *accountDelegate = [[AccountDelegate alloc] initWithUrl:@"authentication/"];
		[accountDelegate userWithEmail:authUser successBlock:^(RKObjectRequestOperation *operation, RKMappingResult *result) {
                 
			User *loggedUser = (User*)[result firstObject];
            loggedUser.credentials = authUser.credentials;
            [self authenticateUser:loggedUser];
                 
		} failureBlock:^(RKObjectRequestOperation *operation, NSError *error) {
            [MBProgressHUD hideHUDForView:self.view animated:YES];
            UIAlertView *errorMessage = [[UIAlertView alloc] initWithTitle:NSLocalizedString(@"error", @"") message:NSLocalizedString(@"login.error.message", @"") delegate:self cancelButtonTitle:@"OK" otherButtonTitles:nil];
            [errorMessage show];
		}];
        
    }
    
}

//Facebook login callback
- (void)loginViewFetchedUserInfo:(FBLoginView *)loginView user:(id<FBGraphUser>)user {
    
    if ([self facebookCounter] > 0)
        return;
    else
    {
        self.facebookCounter++;
        [MBProgressHUD showHUDAddedTo:self.view animated:YES];
        
        dispatch_after(dispatch_time(DISPATCH_TIME_NOW, 1.5 * NSEC_PER_SEC), dispatch_get_current_queue(), ^{
            
            [self clearTextInputs];
            
            NSString *accessToken = [[[FBSession activeSession] accessTokenData] accessToken];
            User *authUser = [[User alloc] init];
            [authUser setEmail:[user objectForKey:@"email"]];
            [authUser setAccessTokenAuthorization:accessToken provider:@"facebook" ];
            
            AccountDelegate *accountDelegate = [[AccountDelegate alloc] initWithUrl:@"authentication/"];
            [accountDelegate userWithEmail:authUser successBlock:^(RKObjectRequestOperation *operation, RKMappingResult *result) {
                
                User *loggedUser = (User*)[result firstObject];
                loggedUser.credentials = authUser.credentials;
                [self authenticateUser:loggedUser];
                [self setFacebookCounter:0];
                
            } failureBlock:^(RKObjectRequestOperation *operation, NSError *error) {
                [MBProgressHUD hideHUDForView:self.view animated:YES];
                UIAlertView *errorMessage = [[UIAlertView alloc] initWithTitle:NSLocalizedString(@"error", @"") message:NSLocalizedString(@"login.error.message", @"") delegate:self cancelButtonTitle:@"OK" otherButtonTitles:nil];
                [errorMessage show];
            }];
            
        });
    }
}



- (void)presentSignInViewController: (UIViewController *)viewController {
    [[self navigationController] pushViewController:viewController animated:YES];
}

- (void) viewWillAppear:(BOOL)animated {
    [self.navigationController setNavigationBarHidden:YES animated:animated];
}

- (void) clearTextInputs {
    _username.text = @"";
    _password.text = @"";
}

- (BOOL)textFieldShouldReturn:(UITextField *)textField {
    
    [textField resignFirstResponder];
    
    if (textField == self.username) {
        [self.password becomeFirstResponder];
    }
    
    return YES;
}

@end
