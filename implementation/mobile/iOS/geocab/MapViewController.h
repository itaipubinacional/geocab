//
//  MapViewController.h
//  geocab
//
//  Created by Vinicius Ramos Kawamoto on 18/09/14.
//  Copyright (c) 2014 Itaipu. All rights reserved.
//

#import <UIKit/UIKit.h>
#import <JavaScriptCore/JavaScriptCore.h>
#import <CoreLocation/CoreLocation.h>
#import "KNMultiItemSelector.h"

@interface MapViewController : UIViewController <UIWebViewDelegate, CLLocationManagerDelegate, UIActionSheetDelegate, KNMultiItemSelectorDelegate>
@property (weak, nonatomic) IBOutlet UIWebView *webView;


@end
