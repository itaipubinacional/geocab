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
#import "LayerDelegate.h"
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

@property (strong, nonatomic) CLLocationManager *locationManager;

@property (nonatomic) CGPoint location;
@property (nonatomic, strong) NSTimer *timer;
@property (retain, nonatomic) NSArray *layers;

@property (retain, nonatomic) UIActionSheet *actionSheet;

@property (strong, nonatomic) SelectLayerViewController *layerSelector;
@property (strong, nonatomic) UINavigationController *layerSelectorNavigator;
@property (strong, nonatomic) NSMutableArray *items;
@property (strong, nonatomic) NSArray *selectedLayers;

@property (weak, nonatomic) IBOutlet UIButton *addMyLocationButton;
@property (weak, nonatomic) IBOutlet UIButton *addAnotherLocationButton;

@property (weak, nonatomic) IBOutlet UIButton *changeMarkerButton;
@property (weak, nonatomic) IBOutlet UIButton *confirmMarkerButton;
@property (weak, nonatomic) IBOutlet UIButton *cancelMarkerButton;
@property (weak, nonatomic) IBOutlet UIButton *showMarkerOptionsButton;
@property (weak, nonatomic) IBOutlet UIView *markerOptionsOverlay;

@property (weak, nonatomic) IBOutlet UIButton *menuButton;

@property (weak, nonatomic) IBOutlet UILabel *hintLabel;

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
    
    //Configures the location manager to fetch the users location.
    if (nil == _locationManager)
        _locationManager = [[CLLocationManager alloc] init];
    
    _locationManager.delegate = self;
    _locationManager.desiredAccuracy = kCLLocationAccuracyKilometer;
    _locationManager.distanceFilter = 500;
    
    if (_actionSheet == nil)
        _actionSheet = [[UIActionSheet alloc] initWithTitle:nil delegate:self cancelButtonTitle:@"Cancelar" destructiveButtonTitle:nil otherButtonTitles:@"Adicionar minha localização", @"Adicionar outra localização",nil];
    [_actionSheet setTintColor:[UIColor blackColor]];
    
    
    [[UIApplication sharedApplication] setStatusBarHidden:YES];
    
    _selectedLayers = [NSMutableArray array];
    
    _layerSelector = [[SelectLayerViewController alloc] init];
    _layerSelector.delegate = self;
    _layerSelector.multipleSelection = YES;
    
    //Add marker buttons customization
    _addAnotherLocationButton.layer.borderColor = [UIColor lightGrayColor].CGColor;
    _addAnotherLocationButton.layer.borderWidth = 0.3;
    
    _addMyLocationButton.layer.borderWidth = 0.3;
    _addMyLocationButton.layer.borderColor = [UIColor lightGrayColor].CGColor;
    
    _changeMarkerButton.layer.borderWidth = 0.3;
    _changeMarkerButton.layer.borderColor = [UIColor lightGrayColor].CGColor;
    [_changeMarkerButton setBackgroundColor:[ControllerUtil colorWithHexString:@"828282"]];
    _changeMarkerButton.layer.cornerRadius = 3;
    
    [_menuButton.layer setShadowOffset:CGSizeMake(2, 2)];
    [_menuButton.layer setShadowColor:[[UIColor blackColor] CGColor]];
    [_menuButton.layer setShadowOpacity:0.5];
    
    _confirmMarkerButton.layer.borderWidth = 0.3;
    _confirmMarkerButton.layer.borderColor = [UIColor lightGrayColor].CGColor;
    [_confirmMarkerButton setBackgroundColor:[ControllerUtil colorWithHexString:@"00b7cd"]];
    _confirmMarkerButton.layer.cornerRadius = 3;
    
    [_markerOptionsOverlay setBackgroundColor:[[UIColor darkGrayColor] colorWithAlphaComponent:0.8]];
    
    [_addMyLocationButton addTarget:self action:@selector(addCurrentLocation:) forControlEvents:UIControlEventTouchUpInside];
    [_addAnotherLocationButton addTarget:self action:@selector(addSelectedLocation:) forControlEvents:UIControlEventTouchUpInside];
    [_cancelMarkerButton addTarget:self action:@selector(cancelMarkerRegistration:) forControlEvents:UIControlEventTouchUpInside];
    [_confirmMarkerButton addTarget:self action:@selector(confirmMarkerRegistration:) forControlEvents:UIControlEventTouchUpInside];
    [_changeMarkerButton addTarget:self action:@selector(changeMarkerRegistration:) forControlEvents:UIControlEventTouchUpInside];
    
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
    
    [_layerSelectorNavigator dismissViewControllerAnimated:NO completion:^{

    }];
}

- (void) webViewDidFinishLoad:(UIWebView *)webView
{
    JSContext *context = [_webView valueForKeyPath:@"documentView.webView.mainFrame.javaScriptContext"];
    context[@"confirmToProceed"] = ^(NSString *param1, NSString *param2) {
        [_webView stringByEvaluatingJavaScriptFromString:@"unbindTouchEvent()"];
        NSString *functionCall = [NSString stringWithFormat:@"addPoint(%@, %@, true)", param1, param2];
        [_webView stringByEvaluatingJavaScriptFromString:functionCall];
        [self hideNewMarkerButtons];
        [self showMarkerOptions];
        
        if (!_hintLabel.hidden) [UIView animateWithDuration:1  delay:1.5 options: UIViewAnimationCurveEaseInOut animations:^{ _hintLabel.hidden = true;} completion:nil];
        if (!_showMarkerOptionsButton.hidden) [UIView animateWithDuration:1  delay:1.5 options: UIViewAnimationCurveEaseInOut animations:^{ _showMarkerOptionsButton.hidden = true;} completion:nil];
        
        _location.x = [param1 floatValue];
        _location.y = [param2 floatValue];
    };
    
    context[@"getMarkerAtributes"] = ^(NSNumber *markerId, NSString *markerName, NSString *user, NSString *date) {
        
        MarkerDelegate *markerDelegate = [[MarkerDelegate alloc] initWithUrl:@"marker"];
        
        [markerDelegate listAttributesById:^(RKObjectRequestOperation *operation, RKMappingResult *result) {
            
            NSString *html = @"";
            
            html = [html stringByAppendingString:@"<div data-role=\"collapsible\" data-collapsed=\"false\">"];
            html = [html stringByAppendingString:[NSString stringWithFormat:@"<h1>%@</h1>", markerName]];
            html = [html stringByAppendingString:@"<div>"];
            html = [html stringByAppendingString:[NSString stringWithFormat:@"<div>%@: %@</div>", NSLocalizedString(@"created-by", @""), user]];
            html = [html stringByAppendingString:[NSString stringWithFormat:@"<div>%@</div><br>", date]];
            NSArray *array = [result array];
            for (MarkerAttribute *markerAtrribute in array) {
                
                NSString *htmlClass;
                html = [html stringByAppendingString:[NSString stringWithFormat:@"<div class=\"%@\"> <h4>%@</h4> <p>%@</p></div>", htmlClass, markerAtrribute.attribute.name, markerAtrribute.value]];
            }
            html = [html stringByAppendingString:[NSString stringWithFormat:@"<img id=\"marker-image-%@\" class=\"layer-content-image loading-gif\" alt=\"\" src=\"ajax-loader.gif\"/>", markerId]];
            html = [html stringByAppendingString:@"</div>"];
            html = [html stringByAppendingString:@"</div>"];
            
            [_webView stringByEvaluatingJavaScriptFromString:[NSString stringWithFormat:@"loadPopupContent('%@')", html]];
            [self loadMarkerImageOnHtml:markerId];
            
        } userName:[defaults objectForKey:@"email"] password:[defaults objectForKey:@"password"] markerId:markerId];
    };
    
    context[@"getExternalLayerAttributes"] = ^(NSString *urlString, NSString *title, BOOL collapsed) {
        // Prepare the link that is going to be used on the GET request
        NSURL * url = [[NSURL alloc] initWithString:urlString];
        NSURLRequest *urlRequest = [NSURLRequest requestWithURL:url];
        
        NSData *urlData;
        NSURLResponse *response;
        NSError *error;
        
        urlData = [NSURLConnection sendSynchronousRequest:urlRequest returningResponse:&response error:&error];
        
        NSDictionary * object = [NSJSONSerialization JSONObjectWithData:urlData options:0 error:&error];
        
        NSArray *features = [object objectForKey:@"features"];
        if ([features count] > 0) {
            NSDictionary *properties = [(NSDictionary*)[features firstObject] objectForKey:@"properties"];
            
            NSString *html = @"";
            
            html = [html stringByAppendingString:@"<div data-role=\"collapsible\" data-collapsed=\"false\">"];
            html = [html stringByAppendingString:[NSString stringWithFormat:@"<h1>%@</h1>", title]];
            html = [html stringByAppendingString:@"<div>"];
            
            for (NSString* key in properties) {
                
                html = [html stringByAppendingString:[NSString stringWithFormat:@"<div class=\"layer-content-text\"> <h4>%@</h4> <p>%@</p></div>", key, [properties objectForKey:key]]];
                
            }
            html = [html stringByAppendingString:@"</div>"];
            html = [html stringByAppendingString:@"</div>"];
            
            html = [html stringByReplacingOccurrencesOfString:@"\'" withString:@"\\'"];
        
            NSLog(@"%@",html);
            
            [_webView stringByEvaluatingJavaScriptFromString:[NSString stringWithFormat:@"loadPopupContent('%@')", html]];
        } else {
            [_webView stringByEvaluatingJavaScriptFromString:@"cancelLoading()"];
        }
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
            NSArray *markers = [result array];
            
            for (Marker *marker in markers) {
                NSRange position = [layer.icon rangeOfString:@"/" options:NSBackwardsSearch];
                NSString *iconName = [layer.icon substringWithRange:NSMakeRange(position.location+1, layer.icon.length - (position.location + 1))];
                
                NSTimeInterval seconds = [marker.created doubleValue] / 1000;
                NSDate *createdDate = [NSDate dateWithTimeIntervalSince1970:seconds];
                
                NSDateFormatter *dateFormat = [[NSDateFormatter alloc] init];
                [dateFormat setDateFormat:@"dd/MM/YYYY"];
                
                NSString *functionCall = [NSString stringWithFormat:@"showMarker(%@, %@, %@, '%@', '%@', '%@', '%@', true)", marker.latitude, marker.longitude, marker.id, marker.layer.name, iconName, marker.user.name, [dateFormat stringFromDate:createdDate]];
                [_webView stringByEvaluatingJavaScriptFromString:functionCall];
            }
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
    if (layer.dataSource.url != nil) {
        NSRange index = [layer.name rangeOfString:@":"];
        NSRange position = [layer.dataSource.url rangeOfString:@"geoserver/" options:NSBackwardsSearch];
        NSString *typeLayer = [layer.name substringWithRange:NSMakeRange(0, index.location)];
        
        NSString *urlFormated = [NSString stringWithFormat:@"%@%@/wms", [layer.dataSource.url substringWithRange:NSMakeRange(0, position.location+10)],typeLayer ];
        
        NSString *functionCall = [NSString stringWithFormat:@"showLayer('%@', '%@', '%@', false)", urlFormated , layer.name, layer.title];
        [_webView stringByEvaluatingJavaScriptFromString:functionCall];
    } else {
        
        NSString *functionCall = [NSString stringWithFormat:@"showMarker(null, null, null, '%@', null, null, null, false)", layer.name];
        [_webView stringByEvaluatingJavaScriptFromString:functionCall];
        
    }
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

-(IBAction)addNewPoint:(id)sender {
    if (!_addMyLocationButton.hidden) {
        [self hideNewMarkerButtons];
    } else {
        [self showNewMarkerButtons];
    }
    
    
    [self hideMarkerOptions];
}

-(void)showNewMarkerButtons {
    if (_addMyLocationButton.hidden) {
        [UIView animateWithDuration:1
                              delay:1.5
                            options: UIViewAnimationCurveEaseInOut
                         animations:^{
                             _addMyLocationButton.hidden = false;
                             _addAnotherLocationButton.hidden = false;
                         } 
                         completion:nil];
    }
}

-(void)hideNewMarkerButtons {
    if (!_addMyLocationButton.hidden) {
        [UIView animateWithDuration:1
                              delay:1.5
                            options: UIViewAnimationCurveEaseInOut
                         animations:^{
                             _addMyLocationButton.hidden = true;
                             _addAnotherLocationButton.hidden = true;
                         }
                         completion:nil];
    }
}

- (void) showMarkerOptions {
    if (_confirmMarkerButton.hidden) {
        [UIView animateWithDuration:1
                              delay:1.5
                            options: UIViewAnimationCurveEaseInOut
                         animations:^{
                             _markerOptionsOverlay.hidden = false;
                             _confirmMarkerButton.hidden = false;
                             _changeMarkerButton.hidden = false;
                             _cancelMarkerButton.hidden = false;
                         }
                         completion:nil];
    }
        
}

- (void) hideMarkerOptions {
    if (!_confirmMarkerButton.hidden) {
        [UIView animateWithDuration:1
                              delay:1.5
                            options: UIViewAnimationCurveEaseInOut
                         animations:^{
                             _markerOptionsOverlay.hidden = true;
                             _confirmMarkerButton.hidden = true;
                             _changeMarkerButton.hidden = true;
                             _cancelMarkerButton.hidden = true;
                         }
                         completion:nil];
    }
}

- (void) loadMarkerImageOnHtml:(NSNumber*)markerId {
    
    MarkerDelegate *markerDelegate = [[MarkerDelegate alloc] initWithUrl:@"files/markers/"];
    
    [markerDelegate downloadMarkerAttributePhoto:markerId success:^(AFHTTPRequestOperation *operation, id responseObject) {
        NSString *embbedImageString = responseObject != nil ? [NSString stringWithFormat:[NSString stringWithFormat:@"<img id=\"marker-image-%@\" class=\"layer-content-image\" alt=\"\" src=\"data:image/jpeg;base64,%@\" />", markerId, [operation.responseData base64EncodedStringWithOptions:0]]] : @"";
        
        [_webView stringByEvaluatingJavaScriptFromString:[NSString stringWithFormat:@"loadImageContent(%@, '%@')", markerId, embbedImageString]];
    } fail:^(AFHTTPRequestOperation *operation, NSError *error) {
        NSLog(@"Error downloading image");
        [_webView stringByEvaluatingJavaScriptFromString:[NSString stringWithFormat:@"loadImageContent(%@, '')", markerId]];
    } login:[defaults objectForKey:@"email"] password:[defaults objectForKey:@"password"]];
}

- (void)loadWebView
{
    NSString *path = [[NSBundle mainBundle] pathForResource:@"index" ofType:@"html" inDirectory:@"/"];
    [_webView loadRequest:[NSURLRequest requestWithURL:[NSURL fileURLWithPath:path]]];
}

- (void)locationManager:(CLLocationManager *)manager didUpdateLocations:(NSArray *)locations {
    CLLocation *location = [locations lastObject];
    NSDate *eventDate = location.timestamp;
    NSTimeInterval howRecent = [eventDate timeIntervalSinceNow];
    if (abs(howRecent) < 15.0) {
        NSString *functionCall = [NSString stringWithFormat:@"addPoint(%.5f, %.5f, false)", location.coordinate.latitude, location.coordinate.longitude];
        [_webView stringByEvaluatingJavaScriptFromString:functionCall];
        
        _location.x = location.coordinate.latitude;
        _location.y = location.coordinate.longitude;
        
        [_locationManager stopUpdatingLocation];
        [self hideNewMarkerButtons];
        [self showMarkerOptions];
        if (!_hintLabel.hidden) [UIView animateWithDuration:1  delay:1.5 options: UIViewAnimationCurveEaseInOut animations:^{ _hintLabel.hidden = true;} completion:nil];
        if (!_showMarkerOptionsButton.hidden) [UIView animateWithDuration:1  delay:1.5 options: UIViewAnimationCurveEaseInOut animations:^{ _showMarkerOptionsButton.hidden = true;} completion:nil];
    }
}

- (void)prepareForSegue:(UIStoryboardSegue *)segue sender:(id)sender {
    if ([segue.identifier isEqualToString:@"addNewMarkerSegue"]) {
        
//        NSLog(@"%.5f  %.5f", _locationManager.location.coordinate.latitude, _locationManager.location.coordinate.longitude);
        
        AddNewMarkerViewController *addNewMarkerViewController = (AddNewMarkerViewController*) segue.destinationViewController;
        addNewMarkerViewController.latitude = _location.x;
        addNewMarkerViewController.longitude = _location.y;
    }
}

-(IBAction)addCurrentLocation:(id)sender {
    if (SYSTEM_VERSION_GREATER_THAN_OR_EQUAL_TO(@"8.0")){
        [_locationManager requestWhenInUseAuthorization];
    }
    [_locationManager startUpdatingLocation];
}

-(IBAction)addSelectedLocation:(id)sender {
    [self hideNewMarkerButtons];
    [self hideMarkerOptions];
    [UIView animateWithDuration:1  delay:1.5 options: UIViewAnimationCurveEaseInOut animations:^{ _hintLabel.hidden = !_hintLabel.hidden;} completion:nil];
    [_webView stringByEvaluatingJavaScriptFromString:@"bindTouchEvent()"];
}

-(void)actionSheet:(UIActionSheet *)actionSheet clickedButtonAtIndex:(NSInteger)buttonIndex {
    if (buttonIndex == 0) {
        if (SYSTEM_VERSION_GREATER_THAN_OR_EQUAL_TO(@"8.0")){
            [_locationManager requestWhenInUseAuthorization];
        }
        [_locationManager startUpdatingLocation];
    }
}

- (IBAction)cancelMarkerRegistration:(id)sender {
    [_webView stringByEvaluatingJavaScriptFromString:@"removeMarker()"];
    [self hideMarkerOptions];
    [self hideNewMarkerButtons];
    if (_showMarkerOptionsButton.hidden) [UIView animateWithDuration:1  delay:1.5 options: UIViewAnimationCurveEaseInOut animations:^{ _showMarkerOptionsButton.hidden = false;} completion:nil];
}

- (IBAction)changeMarkerRegistration:(id)sender {
    [self hideMarkerOptions];
    [self hideNewMarkerButtons];
    
    [UIView animateWithDuration:1  delay:1.5 options: UIViewAnimationCurveEaseInOut animations:^{ _hintLabel.hidden = false; } completion:nil];
    if (!_showMarkerOptionsButton.hidden) [UIView animateWithDuration:1  delay:1.5 options: UIViewAnimationCurveEaseInOut animations:^{ _showMarkerOptionsButton.hidden = true; } completion:nil];
    
    [_webView stringByEvaluatingJavaScriptFromString:@"bindTouchEvent()"];
}

- (IBAction)confirmMarkerRegistration:(id)sender {
    [self performSegueWithIdentifier:@"addNewPointSegue" sender:self];
    [self hideMarkerOptions];
    [self hideNewMarkerButtons];
    
    [UIView animateWithDuration:1  delay:1.5 options: UIViewAnimationCurveEaseInOut animations:^{ _showMarkerOptionsButton.hidden = false; } completion:nil];
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
//            UINavigationController *navigationController = [[UIStoryboard storyboardWithName:@"Main" bundle:[NSBundle mainBundle]] instantiateViewControllerWithIdentifier:@"navigationController"];
//            [[[[UIApplication sharedApplication] delegate] window] setRootViewController:navigationController];
            
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
