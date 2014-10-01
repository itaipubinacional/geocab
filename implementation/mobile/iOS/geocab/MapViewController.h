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

@interface MapViewController : UIViewController <UIWebViewDelegate, CLLocationManagerDelegate, UIActionSheetDelegate>
@property (weak, nonatomic) IBOutlet UIWebView *webView;


@end
