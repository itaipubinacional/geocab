//
//  MapViewController.m
//  geocab
//
//  Created by Vinicius Ramos Kawamoto on 18/09/14.
//  Copyright (c) 2014 Itaipu. All rights reserved.
//

#import "MapViewController.h"
#import "AppDelegate.h"
#import "AddNewPointViewController.h"
#import "LayerDelegate.h"
#import "Layer.h"
#import "ControllerUtil.h"
#import "LoginViewController.h"

#define SYSTEM_VERSION_GREATER_THAN_OR_EQUAL_TO(v)  ([[[UIDevice currentDevice] systemVersion] compare:v options:NSNumericSearch] != NSOrderedAscending)

@interface MapViewController ()

@property (strong, nonatomic) CLLocationManager *locationManager;

@property (nonatomic) CGPoint location;
@property (nonatomic, strong) NSTimer *timer;
@property (retain, nonatomic) NSArray *layers;

@property (retain, nonatomic) UIActionSheet *actionSheet;

@property (weak, nonatomic) MFSideMenuContainerViewController *sideMenu;
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
    
    AppDelegate *delegate = (AppDelegate *)[UIApplication sharedApplication].delegate;
    _sideMenu = delegate.container;
    
    [self.navigationController.navigationItem.leftBarButtonItem setImage:[UIImage imageNamed:@"inc_menu_20.png"]];
    
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
}

- (void) didEndMultipleSelecting:(NSArray *)selectedLayers {
    _selectedLayers = selectedLayers;
    
    [_layerSelectorNavigator dismissViewControllerAnimated:YES completion:^{

    }];
}

- (void) didCheckedLayer:(Layer *)layer {
    NSRange index = [layer.name rangeOfString:@":"];
    NSRange position = [layer.dataSource.url rangeOfString:@"geoserver/" options:NSBackwardsSearch];
    NSString *typeLayer = [layer.name substringWithRange:NSMakeRange(0, index.location)];
    
    NSString *urlFormated = [NSString stringWithFormat:@"%@%@/wms", [layer.dataSource.url substringWithRange:NSMakeRange(0, position.location+10)],typeLayer ];
    
    NSString *functionCall = [NSString stringWithFormat:@"showLayer('%@', '%@', 'true')", urlFormated , layer.name];
    [_webView stringByEvaluatingJavaScriptFromString:functionCall];
}

- (void) didUnheckedLayer:(Layer *)layer {
    NSRange index = [layer.name rangeOfString:@":"];
    NSRange position = [layer.dataSource.url rangeOfString:@"geoserver/" options:NSBackwardsSearch];
    NSString *typeLayer = [layer.name substringWithRange:NSMakeRange(0, index.location)];
    
    NSString *urlFormated = [NSString stringWithFormat:@"%@%@/wms", [layer.dataSource.url substringWithRange:NSMakeRange(0, position.location+10)],typeLayer ];
    
    NSString *functionCall = [NSString stringWithFormat:@"showLayer('%@', '%@', 'false')", urlFormated , layer.name];
    [_webView stringByEvaluatingJavaScriptFromString:functionCall];
}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

- (IBAction)toggleMenu:(id)sender {
    
    _layerSelectorNavigator = [[UINavigationController alloc] initWithRootViewController:_layerSelector];
    _layerSelectorNavigator.modalTransitionStyle = UIModalPresentationNone;
    
    [self presentViewController:_layerSelectorNavigator animated:YES completion:^{

    }];
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
}

- (void)loadWebView
{
    NSString *path = [[NSBundle mainBundle] pathForResource:@"index" ofType:@"html"];
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
    if ([segue.identifier isEqualToString:@"addNewPointSegue"]) {
        
        NSLog(@"%.5f  %.5f", _locationManager.location.coordinate.latitude, _locationManager.location.coordinate.longitude);
        
        AddNewPointViewController *addNewPointViewController = (AddNewPointViewController*) segue.destinationViewController;
        addNewPointViewController.latitude = _location.x;
        addNewPointViewController.longitude = _location.y;
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
    
    
    UINavigationController *navigationController = [[UIStoryboard storyboardWithName:@"Main" bundle:[NSBundle mainBundle]] instantiateViewControllerWithIdentifier:@"navigationController"];
    [[[[UIApplication sharedApplication] delegate] window] setRootViewController:navigationController];
    
    defaults = [NSUserDefaults standardUserDefaults];
    NSDictionary * dict = [defaults dictionaryRepresentation];
    for (id key in dict) {
        
        //heck the keys if u need
        [defaults removeObjectForKey:key];
    }
    [defaults synchronize];
}

- (void)viewWillAppear:(BOOL)animated {
    [self.navigationController setNavigationBarHidden:YES animated:animated];
    [super viewWillAppear:animated];
}

-(void)viewDidDisappear:(BOOL)animated {
    [super viewDidDisappear:animated];
    [self.navigationController setNavigationBarHidden:NO animated:animated];
}

@end
