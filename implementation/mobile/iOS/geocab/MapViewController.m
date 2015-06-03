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
#import "FileDelegate.h"
#import "LayerDelegate.h"
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

UIActivityIndicatorView *indicator;

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
    
    // Loading
    indicator = [[UIActivityIndicatorView alloc]initWithActivityIndicatorStyle:UIActivityIndicatorViewStyleGray];
    [indicator setBackgroundColor:[UIColor lightTextColor]];
    indicator.frame = CGRectMake(0.0, 0.0, 45.0, 45.0);
    indicator.center = self.view.center;
    [self.view addSubview:indicator];
    [indicator bringSubviewToFront:self.view];
    [UIApplication sharedApplication].networkActivityIndicatorVisible = TRUE;
    
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
    
    context[@"changeToUpdateMarker"] = ^(NSString *markerJson, NSString *base64Image) {
        
        dispatch_async(dispatch_get_main_queue(), ^{
            
            self.currentMarker = [Marker fromJSONString:markerJson];
            
            if ( [base64Image length] > 0 )
            {
                NSURL *url = [NSURL URLWithString:base64Image];
                NSData *imageData = [NSData dataWithContentsOfURL:url];
                self.currentMarker.imageData = imageData;
            }
            
            self.currentMarker.markerAttributes = self.currentMarkerAttributes;
            
            [self performSegueWithIdentifier:@"addNewMarkerSegue" sender:self];
            
        });
        
    };
    
    context[@"changeToApproveMarker"] = ^(NSString *markerJson) {
        
        Marker *marker = [Marker fromJSONString:markerJson];
        
        MarkerDelegate *markerDelegate = [[MarkerDelegate alloc] initWithUrl:@"marker"];
        [markerDelegate approve:[defaults objectForKey:@"email"] password:[defaults objectForKey:@"password"] markerId:marker.id];
        
        marker.status = @"ACCEPTED";
        
        NSString *functionCall = [NSString stringWithFormat:@"geocabapp.marker.loadActions('%@')", [marker toJSONString]];
        [_webView stringByEvaluatingJavaScriptFromString:functionCall];
        
    };
    
    context[@"changeToRefuseMarker"] = ^(NSString *markerJSON, NSString *motiveMarkerJSON) {

        Marker *marker = [Marker fromJSONString:markerJSON];
        MotiveMarkerModeration *motiveMarkerModeration = [MotiveMarkerModeration fromJSONString:motiveMarkerJSON];
        
        MarkerDelegate *markerDelegate = [[MarkerDelegate alloc] initWithUrl:@"marker"];
        [markerDelegate refuse:[defaults objectForKey:@"email"] password:[defaults objectForKey:@"password"] markerId:marker.id motiveMarkerModeration:motiveMarkerModeration];
        
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
    
    context[@"showLayerMarker"] = ^(NSNumber *markerId, NSArray *layersUrl) {
        
        // Verifica se existem camadas sendo mostradas
        if ( layersUrl != nil && [layersUrl count] > 0 )
        {
            LayerDelegate *layerDelegate = [[LayerDelegate alloc] initWithUrl:@""];
            
            [layerDelegate listProperties:^(RKObjectRequestOperation *operation, RKMappingResult *result) {
                
                NSString *response = operation.HTTPRequestOperation.responseString;
                response = [response stringByReplacingOccurrencesOfString:@"\"{" withString:@"{"];
                response = [response stringByReplacingOccurrencesOfString:@"}\"" withString:@"}"];                
                
                if ( [markerId longValue] > 0 ){
                    [self loadMarker:markerId layersProperties:response];
                    
                } else {
                    NSString *functionCall = [NSString stringWithFormat:@"geocabapp.marker.show('','','','%@')", response];
                    [_webView stringByEvaluatingJavaScriptFromString:functionCall];
                }
                
            } userName:[defaults objectForKey:@"email"] password:[defaults objectForKey:@"password"] dataSource:layersUrl];
            
        }
        // Caso não, mostra apenas o marker
        else if ( markerId > 0 )
        {
			[self loadMarker:markerId layersProperties:nil];
        }

    };
    
    context[@"showOpenMenuButton"] = ^() {
        
        dispatch_async(dispatch_get_main_queue(), ^{
            self.menuButton.hidden = NO;
        });
        
    };
    
    context[@"closeOpenMenuButton"] = ^() {
        
        dispatch_async(dispatch_get_main_queue(), ^{
            self.menuButton.hidden = YES;
        });
        
    };
    
}

- (void) loadMarker:(NSNumber *) markerId layersProperties: (NSString *)layersProperties {
    
    MarkerDelegate *markerDelegate = [[MarkerDelegate alloc] initWithUrl:@"marker"];
    
    [markerDelegate listAttributesById:^(RKObjectRequestOperation *operation, RKMappingResult *result) {
        
        self.currentMarkerAttributes = [[result array] mutableCopy];
        
        NSString *userId = [defaults objectForKey:@"userId"];
        NSString *userRole = [defaults objectForKey:@"userRole"];
        NSString *markerAttributes = operation.HTTPRequestOperation.responseString;
        markerAttributes = [markerAttributes stringByAddingPercentEscapesUsingEncoding:NSUTF8StringEncoding];
        
        FileDelegate *delegate = [[FileDelegate alloc] initWithUrl:@"files/marker/"];
        
        [delegate downloadMarkerAttributePhoto:markerId success:^(AFHTTPRequestOperation *operation, id responseObject) {
            
            NSString *imageBase64 = responseObject != nil ? [NSString stringWithFormat:@"data:image/jpeg;base64,%@", [operation.responseData base64EncodedStringWithOptions:0]] : @"";
            
            NSString *functionCall = [NSString stringWithFormat:@"geocabapp.marker.showOptions('%@','%@','%@','%@','%@','%@')", markerId, markerAttributes, imageBase64, userId, userRole, layersProperties];
            
            [_webView stringByEvaluatingJavaScriptFromString:functionCall];
            [self loadMotives:markerDelegate];
            
        } fail:^(AFHTTPRequestOperation *operation, NSError *error) {
            
            NSString *functionCall = [NSString stringWithFormat:@"geocabapp.marker.showOptions('%@','%@','','%@','%@','%@')", markerId, markerAttributes, userId, userRole, layersProperties];
            
            [_webView stringByEvaluatingJavaScriptFromString:functionCall];
            [self loadMotives:markerDelegate];
            
        } login:[defaults objectForKey:@"email"] password:[defaults objectForKey:@"password"]];
        
        
    } userName:[defaults objectForKey:@"email"] password:[defaults objectForKey:@"password"] markerId:markerId];
    
}

- (void) loadMotives:(MarkerDelegate *) markerDelegate {
    
    [markerDelegate listMotives:^(RKObjectRequestOperation *operation, RKMappingResult *result) {
        
        NSString *motives = operation.HTTPRequestOperation.responseString;
        NSString *functionCall = [NSString stringWithFormat:@"geocabapp.marker.loadMotives('%@')", motives];
        [_webView stringByEvaluatingJavaScriptFromString:functionCall];
        
    } failBlock: nil userName:[defaults objectForKey:@"email"] password:[defaults objectForKey:@"password"]];
    
}

- (void) didCheckedLayer:(Layer *)layer {
    if (layer.dataSource.url != nil) {

        NSString *functionCall = [NSString stringWithFormat:@"geocabapp.showLayer('%@','%@','%@','%@')", layer.dataSource.url , layer.id, layer.name, layer.title];
        [_webView stringByEvaluatingJavaScriptFromString:functionCall];
        
    } else {
        MarkerDelegate *markerDelegate = [[MarkerDelegate alloc] initWithUrl:@"marker/"];
        [markerDelegate list:^(RKObjectRequestOperation *operation, RKMappingResult *result) {

            [self didUnheckedLayer:layer]; // Para pontos adicionados recentemente
            
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
    
    NSString *functionCall = [NSString stringWithFormat:@"geocabapp.closeLayer('%@')", layer.id];
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
    _webView.scrollView.scrollEnabled = FALSE;
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
