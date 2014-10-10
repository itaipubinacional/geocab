//
//  AddNewPointViewController.h
//  geocab
//
//  Created by Henrique Lobato on 30/09/14.
//  Copyright (c) 2014 Itaipu. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "FDTakeController.h"
#import <CoreLocation/CoreLocation.h>
#import "SelectLayerViewController.h"

@interface AddNewPointViewController: UIViewController <UITableViewDelegate, UITableViewDataSource, FDTakeDelegate, UIGestureRecognizerDelegate, LayerSelectDelegate>

@property (weak, nonatomic) IBOutlet UITextField *pointName;
@property (weak, nonatomic) IBOutlet UITextView *pointDescription;

@property (nonatomic) CLLocationDegrees latitude;
@property (nonatomic) CLLocationDegrees longitude;

@end
