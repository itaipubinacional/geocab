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
#import <GoogleOpenSource/GoogleOpenSource.h>
#import "AppDelegate.h"
#import "User.h"
#import "ControllerUtil.h"
#import "AccountDelegate.h"

@interface LoginViewController ()

@property (weak, nonatomic) IBOutlet UITextField *username;
@property (weak, nonatomic) IBOutlet UITextField *password;
@property (copy) GPPSignIn *signIn;
@property (weak, nonatomic) IBOutlet UIButton *standardLoginButton;

extern NSUserDefaults *defaults;
extern User *loggedUser;

@end

@implementation LoginViewController

@synthesize signInButton, fbLoginView;

- (void)viewDidLoad {
    [super viewDidLoad];
    
    _signIn = [GPPSignIn sharedInstance];
    
    [self.navigationController setNavigationBarHidden:YES animated:NO];
    
    _signIn.clientID = [[NSBundle mainBundle] objectForInfoDictionaryKey:@"kClientId"];
    _signIn.scopes = [NSArray arrayWithObjects:kGTLAuthScopePlusLogin,
                     nil];
    _signIn.shouldFetchGooglePlusUser = YES;
    _signIn.shouldFetchGoogleUserEmail = YES;
    _signIn.shouldFetchGoogleUserID = YES;
    _signIn.delegate = self;
    
    _standardLoginButton.backgroundColor = [ControllerUtil colorWithHexString:@"27a7c6"];
    
    //[self.signInButton setStyle:(GPPSignInButtonStyle)];
    
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
        AccountDelegate *accountDelegate = [[AccountDelegate alloc] initWithUrl:@"authentication/"];
        [accountDelegate loginWithEmail:_username.text password:_password.text successBlock:^(RKObjectRequestOperation *operation, RKMappingResult *result) {
            User *loggedUser = [[User alloc] init];
            loggedUser = [[result array] objectAtIndex:0];
            [loggedUser setPassword:_password.text];
            [self authenticateUser:loggedUser];
        } failureBlock:^(RKObjectRequestOperation *operation, NSError *error) {
            UIAlertView *errorMessage = [[UIAlertView alloc] initWithTitle:NSLocalizedString(@"error", @"") message:NSLocalizedString(@"login.error.message", @"") delegate:self cancelButtonTitle:@"OK" otherButtonTitles:nil];
            [errorMessage show];
        }];
        
    }
}

- (void) authenticateUser:(User*) user {

    [defaults setObject:user.name forKey:@"name"];
    [defaults setObject:user.email forKey:@"email"];
    [defaults setObject:user.id forKey:@"userId"];
    [defaults setObject:user.role forKey:@"userRole"];
    
    NSString *password = [_password.text isEqualToString:@""] ? user.password : _password.text;
    [defaults setObject:password forKey:@"password"];
    loggedUser = user;
    [defaults synchronize];
    
    [self performSegueWithIdentifier:@"loginToMainSegue" sender:self];
}

- (BOOL)isFormValid {
    if ([_username.text isEqual:@""] || [_password.text isEqual:@""]) {
        UIAlertView *alert = [[UIAlertView alloc] initWithTitle:@"Erro"
                                                        message:@"Verifique se os campos estão preenchidos corretamente."
                                                       delegate:nil
                                              cancelButtonTitle:@"OK"
                                              otherButtonTitles:nil];
        [alert show];
        return NO;
    }
    
    return YES;
}

//Google plus login callback
- (void)finishedWithAuth:(GTMOAuth2Authentication *)auth error:(NSError *)error {
    if (error) {
        NSLog(@"Received error %@ and auth object %@", error, auth);
    } else {
        
        [[[GPPSignIn sharedInstance] plusService] executeQuery:[GTLQueryPlus queryForPeopleGetWithUserId:_signIn.userID] completionHandler:^(GTLServiceTicket *ticket, GTLPlusPerson *person, NSError *error)
         {
             [self clearTextInputs];
             AccountDelegate *accountDelegate = [[AccountDelegate alloc] initWithUrl:@"authentication/create"];
             [accountDelegate socialAuthenticate:[[GPPSignIn sharedInstance] userEmail] name:person.displayName successBlock:^(RKObjectRequestOperation *operation, RKMappingResult *result) {
                 User *loggedUser = (User*)[result firstObject];
                 loggedUser.password = @"none";
                 [self authenticateUser:loggedUser];
             } failureBlock:^(RKObjectRequestOperation *operation, NSError *error) {
                 NSLog(@"Google plus login error");
             }];
         }];
    }
    
}

//Facebook login callback
- (void)loginViewFetchedUserInfo:(FBLoginView *)loginView user:(id<FBGraphUser>)user {
    [self clearTextInputs];
    AccountDelegate *accountDelegate = [[AccountDelegate alloc] initWithUrl:@"authentication/create"];
    [accountDelegate socialAuthenticate:[user objectForKey:@"email"] name:user.name successBlock:^(RKObjectRequestOperation *operation, RKMappingResult *result) {
        User *loggedUser = (User*)[result firstObject];
        loggedUser.password = @"none";
        [self authenticateUser:loggedUser];
    } failureBlock:^(RKObjectRequestOperation *operation, NSError *error) {
        NSLog(@"Facebook login error");
    }];

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
