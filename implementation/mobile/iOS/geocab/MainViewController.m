//
//  MainViewController.m
//  geocab
//
//  Created by Henrique Lobato on 03/10/14.
//  Copyright (c) 2014 Itaipu. All rights reserved.
//

#import "MainViewController.h"
#import "User.h"

@interface MainViewController ()

extern NSUserDefaults *defaults;
extern User *loggedUser;

@end

@implementation MainViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    // Do any additional setup after loading the view.
    
    if ( [defaults objectForKey:@"auth"] != NULL )
    {
        loggedUser = [[User alloc] init];
        loggedUser.name = [defaults objectForKey:@"name"];
        loggedUser.email = [defaults objectForKey:@"email"];
        loggedUser.password = [defaults objectForKey:@"password"];
        loggedUser.role = [defaults objectForKey:@"userRole"];
        loggedUser.id = [defaults objectForKey:@"userId"];
        [loggedUser setBasicAuthorization];
        
        [self performSegueWithIdentifier:@"mainSegue" sender:self];
    }
    else
    {
        [self performSegueWithIdentifier:@"loginSegue" sender:nil];
    }
}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

/*
#pragma mark - Navigation

// In a storyboard-based application, you will often want to do a little preparation before navigation
- (void)prepareForSegue:(UIStoryboardSegue *)segue sender:(id)sender {
    // Get the new view controller using [segue destinationViewController].
    // Pass the selected object to the new view controller.
}
*/

@end
