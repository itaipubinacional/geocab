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
#import "Marker.h"

@interface AddNewMarkerViewController: UIViewController <FDTakeDelegate, LayerSelectDelegate, UIScrollViewDelegate, UITextFieldDelegate, UITextViewDelegate>

@property (nonatomic, retain) NSString *wktCoordenate;
@property (weak, nonatomic) UIWebView *webView;
@property (strong, nonatomic) Marker *marker;

@end
