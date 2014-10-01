//
//  AddNewPointViewController.m
//  geocab
//
//  Created by Henrique Lobato on 30/09/14.
//  Copyright (c) 2014 Itaipu. All rights reserved.
//

#import "AddNewPointViewController.h"

@implementation AddNewPointViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    
    _pointDescription.layer.borderWidth = 0.5f;
    _pointDescription.layer.borderColor = [UIColor grayColor].CGColor;
    _pointDescription.layer.cornerRadius = 5;
    
    _pointName.layer.borderWidth = 0.5f;
    _pointName.layer.borderColor = [UIColor grayColor].CGColor;
    _pointName.layer.cornerRadius = 5;
}

- (id)initWithNibName:(NSString *)nibNameOrNil bundle:(NSBundle *)nibBundleOrNil
{
    self = [super initWithNibName:nibNameOrNil bundle:nibBundleOrNil];
    if (self) {
        // Custom initialization
    }
    return self;
}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

@end