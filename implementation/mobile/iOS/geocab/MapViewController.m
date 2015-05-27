//
//  MapViewController.m
//  geocab
//
//  Created by Vinicius Ramos Kawamoto on 18/09/14.
//  Copyright (c) 2014 Itaipu. All rights reserved.
//

#import "MapViewController.h"
#import "AppDelegate.h"
#import "AddNewMarkerViewController.h"
#import "MarkerDelegate.h"
#import "Layer.h"
#import "User.h"
#import "Marker.h"
#import "MarkerAttribute.h"
#import "AttributeType.h"
#import "ControllerUtil.h"
#import "LoginViewController.h"
#import <FacebookSDK/FacebookSDK.h>

#define SYSTEM_VERSION_GREATER_THAN_OR_EQUAL_TO(v)  ([[[UIDevice currentDevice] systemVersion] compare:v options:NSNumericSearch] != NSOrderedAscending)

@interface MapViewController ()

@property (nonatomic, strong) NSTimer *timer;
@property (retain, nonatomic) NSArray *layers;
@property (nonatomic, retain) NSString *wktCoordenate;

@property (strong, nonatomic) SelectLayerViewController *layerSelector;
@property (strong, nonatomic) UINavigationController *layerSelectorNavigator;
@property (strong, nonatomic) NSMutableArray *items;
@property (strong, nonatomic) NSArray *selectedLayers;
@property (strong, nonatomic) Marker *currentMarker;
@property (strong, nonatomic) NSMutableArray *currentMarkerAttributes;

@property (weak, nonatomic) IBOutlet UIButton *menuButton;

extern NSUserDefaults *defaults;

@end

@implementation MapViewController

- (id)initWithNibName:(NSString *)nibNameOrNil bundle:(NSBundle *)nibBundleOrNil
{
    self = [super initWithNibName:nibNameOrNil bundle:nibBundleOrNil];
    if (self) {
        // Custom initialization
    }
    return self;
}

- (void)viewDidLoad
{
    [super viewDidLoad];
    
    _webView.delegate = self;
    _webView.scrollView.bounces = NO;
    _webView.scrollView.scrollEnabled = NO;
    
    [self loadWebView];
    
    [[UIApplication sharedApplication] setStatusBarHidden:YES];
    
    _selectedLayers = [NSMutableArray array];
    
    _layerSelector = [[SelectLayerViewController alloc] init];
    _layerSelector.delegate = self;
    _layerSelector.multipleSelection = YES;
    
    //Add marker buttons customization
    [_menuButton.layer setShadowOffset:CGSizeMake(2, 2)];
    [_menuButton.layer setShadowColor:[[UIColor blackColor] CGColor]];
    [_menuButton.layer setShadowOpacity:0.5];
    
    _currentMarker = [[Marker alloc] init];
    
    [ControllerUtil verifyInternetConection];
}

- (void)viewWillAppear:(BOOL)animated {
    [self.navigationController setNavigationBarHidden:YES animated:NO];
    [super viewWillAppear:animated];
}

-(void)viewDidDisappear:(BOOL)animated {
    [super viewDidDisappear:animated];
}

- (void) didEndMultipleSelecting:(NSArray *)selectedLayers {
    _selectedLayers = selectedLayers;
    
    CATransition *transition = [CATransition animation];
    transition.duration = 0.3;
    transition.timingFunction = [CAMediaTimingFunction functionWithName:kCAMediaTimingFunctionEaseInEaseOut];
//    transition.type = kCATransitionPush;
    transition.type = kCATransitionFromRight;
    [self.layerSelectorNavigator.view.window.layer addAnimation:transition forKey:nil];
    
    [_layerSelectorNavigator dismissViewControllerAnimated:NO completion:^{}];
}

- (void) webViewDidFinishLoad:(UIWebView *)webView
{
    JSContext *context = [_webView valueForKeyPath:@"documentView.webView.mainFrame.javaScriptContext"];
    
    context[@"changeToAddMarker"] = ^(NSString *coordenates) {
        
        self.currentMarker = nil;
        self.wktCoordenate = coordenates;
        [self performSegueWithIdentifier:@"addNewMarkerSegue" sender:self];
        
    };
    
    context[@"changeToUpdateMarker"] = ^(NSString *markerJson) {
        
        self.currentMarker = [Marker fromJSONString:markerJson];
            
        self.currentMarker.markerAttributes = self.currentMarkerAttributes;
        
        [self performSegueWithIdentifier:@"addNewMarkerSegue" sender:self];
        
    };
    
    context[@"changeToApproveMarker"] = ^(NSString *markerJson) {
        
        Marker *marker = [Marker fromJSONString:markerJson];
        
        MarkerDelegate *markerDelegate = [[MarkerDelegate alloc] initWithUrl:@"marker"];
        [markerDelegate approve:[defaults objectForKey:@"email"] password:[defaults objectForKey:@"password"] markerId:marker.id];
        
        marker.status = @"ACCEPTED";
        
        NSString *functionCall = [NSString stringWithFormat:@"geocabapp.marker.loadActions('%@')", [marker toJSONString]];
        [_webView stringByEvaluatingJavaScriptFromString:functionCall];
        
    };
    
    context[@"changeToRefuseMarker"] = ^(NSString *markerJson) {
        
        Marker *marker = [Marker fromJSONString:markerJson];
        
        MarkerDelegate *markerDelegate = [[MarkerDelegate alloc] initWithUrl:@"marker"];
        [markerDelegate refuse:[defaults objectForKey:@"email"] password:[defaults objectForKey:@"password"] markerId:marker.id];
        
        marker.status = @"REFUSED";
        
        NSString *functionCall = [NSString stringWithFormat:@"geocabapp.marker.loadActions('%@')", [marker toJSONString]];
        [_webView stringByEvaluatingJavaScriptFromString:functionCall];
        
    };
    
    context[@"changeToRemoveMarker"] = ^(NSString *markerJson) {
        
        Marker *marker = [Marker fromJSONString:markerJson];
        MarkerDelegate *markerDelegate = [[MarkerDelegate alloc] initWithUrl:@"marker"];
        [markerDelegate remove:[defaults objectForKey:@"email"] password:[defaults objectForKey:@"password"] markerId:marker.id];
        
		NSString *functionCall = [NSString stringWithFormat:@"geocabapp.closeMarker('%@')", marker.id];
		[_webView stringByEvaluatingJavaScriptFromString:functionCall];
        [_webView stringByEvaluatingJavaScriptFromString:@"geocabapp.marker.hide()"];
        
    };
    
    context[@"showMarker"] = ^(NSNumber *markerId) {
        
        dispatch_async(dispatch_get_main_queue(), ^{
            self.menuButton.hidden = YES;
        });
        
        MarkerDelegate *markerDelegate = [[MarkerDelegate alloc] initWithUrl:@"marker"];
        
        [markerDelegate listAttributesById:^(RKObjectRequestOperation *operation, RKMappingResult *result) {
            
            self.currentMarkerAttributes = [[result array] mutableCopy];
            
            NSString *userId = [defaults objectForKey:@"userId"];
            NSString *userRole = [defaults objectForKey:@"userRole"];
            NSString *markerAttributes = operation.HTTPRequestOperation.responseString;
            
            MarkerDelegate *delegate = [[MarkerDelegate alloc] initWithUrl:@"files/markers/"];
            
            [delegate downloadMarkerAttributePhoto:markerId success:^(AFHTTPRequestOperation *operation, id responseObject) {
                
				NSString *imageBase64 = responseObject != nil ? [NSString stringWithFormat:@"data:image/jpeg;base64,%@", [operation.responseData base64EncodedStringWithOptions:0]] : @"";
                
                NSString *functionCall = [NSString stringWithFormat:@"geocabapp.marker.showOptions('%@','%@','%@','%@','%@')", markerId, markerAttributes, imageBase64, userId, userRole];
                
                [_webView stringByEvaluatingJavaScriptFromString:functionCall];
                
            } fail:^(AFHTTPRequestOperation *operation, NSError *error) {
                
                NSString *functionCall = [NSString stringWithFormat:@"geocabapp.marker.showOptions('%@','%@','','%@','%@')", markerId, markerAttributes, userId, userRole];
                
                [_webView stringByEvaluatingJavaScriptFromString:functionCall];
                
            } login:[defaults objectForKey:@"email"] password:[defaults objectForKey:@"password"]];
            
            
        } userName:[defaults objectForKey:@"email"] password:[defaults objectForKey:@"password"] markerId:markerId];
    };
    
    context[@"showOpenMenuButton"] = ^() {
        
        dispatch_async(dispatch_get_main_queue(), ^{
            self.menuButton.hidden = NO;
        });
        
    };
    
}

- (void) didCheckedLayer:(Layer *)layer {
    if (layer.dataSource.url != nil) {
        NSRange index = [layer.name rangeOfString:@":"];
        NSRange position = [layer.dataSource.url rangeOfString:@"geoserver/" options:NSBackwardsSearch];
        NSString *typeLayer = [layer.name substringWithRange:NSMakeRange(0, index.location)];
        
        NSString *urlFormated = [NSString stringWithFormat:@"%@%@/wms", [layer.dataSource.url substringWithRange:NSMakeRange(0, position.location+10)],typeLayer ];
        
        NSString *functionCall = [NSString stringWithFormat:@"showLayer('%@', '%@', '%@', 'true')", urlFormated , layer.name, layer.title];
        [_webView stringByEvaluatingJavaScriptFromString:functionCall];
    } else {
        MarkerDelegate *markerDelegate = [[MarkerDelegate alloc] initWithUrl:@"marker/"];
        [markerDelegate list:^(RKObjectRequestOperation *operation, RKMappingResult *result) {

            NSString *markers = operation.HTTPRequestOperation.responseString;
			NSString *functionCall = [NSString stringWithFormat:@"geocabapp.addMarkers('%@')", markers];
			[_webView stringByEvaluatingJavaScriptFromString:functionCall];
            
        } failBlock:^(RKObjectRequestOperation *operation, NSError *error) {
            
            UIAlertView *alert = [[UIAlertView alloc] initWithTitle:NSLocalizedString(@"error", @"")
                                                            message:NSLocalizedString(@"layer-fetch.error.message", @"")
                                                           delegate:nil
                                                  cancelButtonTitle:@"OK"
                                                  otherButtonTitles:nil];
            [alert show];
            
            
        } userName:[defaults objectForKey:@"email"] password:[defaults objectForKey:@"password"] layerId:layer.id];
    }
}

- (void) didUnheckedLayer:(Layer *)layer {
    
    NSString *functionCall = [NSString stringWithFormat:@"geocabapp.closeMarker('%@')", layer.id];
    [_webView stringByEvaluatingJavaScriptFromString:functionCall];
    
}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

- (IBAction)toggleMenu:(id)sender {
    
    _layerSelectorNavigator = [[UINavigationController alloc] initWithRootViewController:_layerSelector];
    
    CATransition *transition = [CATransition animation];
    transition.duration = 0.3;
    transition.timingFunction = [CAMediaTimingFunction functionWithName:kCAMediaTimingFunctionEaseInEaseOut];
    transition.type = kCATransitionFromLeft;
    transition.fillMode = kCAFillModeBoth;
    [self.view.window.layer addAnimation:transition forKey:nil];

    
    [self presentViewController:_layerSelectorNavigator animated:NO completion:nil];
}

- (void)loadWebView
{
    NSString *path = [[NSBundle mainBundle] pathForResource:@"webview" ofType:@"html" inDirectory:@"/"];
    [_webView loadRequest:[NSURLRequest requestWithURL:[NSURL fileURLWithPath:path]]];
    _webView.scrollView.scrollEnabled = TRUE;
    _webView.scalesPageToFit = TRUE;
}

- (void)prepareForSegue:(UIStoryboardSegue *)segue sender:(id)sender {
    
    if ([segue.identifier isEqualToString:@"addNewMarkerSegue"]) {
        
        AddNewMarkerViewController *addNewMarkerViewController = (AddNewMarkerViewController*) segue.destinationViewController;
        addNewMarkerViewController.wktCoordenate = self.wktCoordenate;
        addNewMarkerViewController.webView = self.webView;
        addNewMarkerViewController.marker = self.currentMarker;
        
    }
    
}

- (void)locationManager:(CLLocationManager *)manager didFailWithError:(NSError *)error {
    NSLog(@"didFailWithError: %@", error);
    
    UIAlertView *errorAlert = [[UIAlertView alloc] initWithTitle:NSLocalizedString(@"gps.error.title", @"") message:NSLocalizedString(@"gps.error.message", @"") delegate:self cancelButtonTitle:@"OK" otherButtonTitles:nil];
    
    [errorAlert show];
}

-(void)logoutButtonPressed {
    
    UIAlertView *alert = [[UIAlertView alloc] initWithTitle:NSLocalizedString(@"logout-confirmation.title", @"")
                                                    message:NSLocalizedString(@"logout-confirmation.message", @"")
                                                   delegate:self
                                          cancelButtonTitle:NSLocalizedString(@"no", @"")
                                          otherButtonTitles:NSLocalizedString(@"yes", @""), nil];
    [alert show];
}

- (void)alertView:(UIAlertView *)alertView clickedButtonAtIndex:(NSInteger)buttonIndex
{
    switch(buttonIndex) {
        case 0:
            break;
        case 1: {
            
            defaults = [NSUserDefaults standardUserDefaults];
            NSDictionary * dict = [defaults dictionaryRepresentation];
            for (id key in dict) {
                
                //heck the keys if u need
                [defaults removeObjectForKey:key];
            }
            [defaults synchronize];
            
            if ([[FBSession activeSession] isOpen]) {
                [[FBSession activeSession] closeAndClearTokenInformation];
            }
            
            [_layerSelectorNavigator dismissViewControllerAnimated:NO completion:^{
               [self performSegueWithIdentifier:@"logoutSegue" sender:nil];
            }];
            break;
        }
    }
}

@end
