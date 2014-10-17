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
#import "MFSideMenu.h"
#import "AppDelegate.h"
#import "User.h"
#import "ControllerUtil.h"
#import "AccountDelegate.h"

@interface LoginViewController ()

@property (retain, nonatomic) MFSideMenuContainerViewController *menuContainerViewController;
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
    
    self.menuContainerViewController.panMode = MFSideMenuPanModeNone;
    
    _signIn = [GPPSignIn sharedInstance];
    
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
    
    [self.password setReturnKeyType:UIReturnKeyDone];
    
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
    if (self.isFormValid) {
        AccountDelegate *accountDelegate = [[AccountDelegate alloc] initWithUrl:@"authentication/"];
        [accountDelegate loginWithEmail:_username.text password:_password.text successBlock:^(RKObjectRequestOperation *operation, RKMappingResult *result) {
            User *loggedUser = [[User alloc] init];
            loggedUser = [[result array] objectAtIndex:0];
            [self authenticateUser:loggedUser];
        } failureBlock:^(RKObjectRequestOperation *operation, NSError *error) {
            UIAlertView *errorMessage = [[UIAlertView alloc] initWithTitle:@"Error" message:[error localizedDescription] delegate:self cancelButtonTitle:@"OK" otherButtonTitles:nil];
            [errorMessage show];
        }];
        
    }
}

- (void) authenticateUser:(User*) user {
    [defaults setObject:user.email forKey:@"email"];
    [defaults setObject:user.name forKey:@"name"];
    //[defaults setObject:[[[result array] objectAtIndex:0] id] forKey:@"userId"];
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
             //Prints null in both
             User *loggedUser = [[User alloc] init];
             loggedUser.email = [[GPPSignIn sharedInstance] userEmail];
             loggedUser.name = person.displayName;
             
             [self authenticateUser:loggedUser];
         }];
    }
    
}

//Facebook login callback
- (void)loginViewFetchedUserInfo:(FBLoginView *)loginView user:(id<FBGraphUser>)user {
    User *loggedUser = [[User alloc] init];
    loggedUser.name = user.name;
    loggedUser.email = [user objectForKey:@"email"];
    
    [self authenticateUser:loggedUser];
}

- (void)presentSignInViewController: (UIViewController *)viewController {
    [[self navigationController] pushViewController:viewController animated:YES];
}

- (void) viewWillAppear:(BOOL)animated {
    AppDelegate *delegate = (AppDelegate *)[UIApplication sharedApplication].delegate;
    delegate.container.panMode = MFSideMenuPanModeNone;
    
    self.navigationItem.hidesBackButton = YES;
    self.navigationItem.leftBarButtonItem = nil;
}

@end
