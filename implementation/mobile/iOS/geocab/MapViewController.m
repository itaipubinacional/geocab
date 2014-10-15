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
    _addMyLocationButton.layer.borderColor = [UIColor lightGrayColor].CGColor;
    _addAnotherLocationButton.layer.borderWidth = 0.3;
    _addMyLocationButton.layer.borderWidth = 0.3;
    
    [_addMyLocationButton addTarget:self action:@selector(addCurrentLocation:) forControlEvents:UIControlEventTouchUpInside];
    [_addAnotherLocationButton addTarget:self action:@selector(addSelectedLocation:) forControlEvents:UIControlEventTouchUpInside];
}

- (void) didEndMultipleSelecting:(NSArray *)selectedLayers {
    _selectedLayers = selectedLayers;
    
    [_layerSelectorNavigator dismissViewControllerAnimated:YES completion:^{
        
    }];
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
    
    //[_sideMenu toggleLeftSideMenuCompletion:^{}];
}

-(IBAction)addNewPoint:(id)sender {
    [self toogleButtons];
}

-(void)toogleButtons {
    [UIView animateWithDuration:1
                          delay:1.5
                        options: UIViewAnimationCurveEaseInOut
                     animations:^{
                         _addMyLocationButton.hidden = !_addMyLocationButton.hidden;
                         _addAnotherLocationButton.hidden = !_addAnotherLocationButton.hidden;
                     } 
                     completion:nil];
}

- (void) webViewDidFinishLoad:(UIWebView *)webView
{
    JSContext *context = [_webView valueForKeyPath:@"documentView.webView.mainFrame.javaScriptContext"];
    context[@"submitButton"] = ^(NSString *param1) {
        //NSLog([NSString stringWithFormat:@"Clicou no botão: %@", param1]);
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
//        NSString *functionCall = [NSString stringWithFormat:@"addPoint(%.5f, %.5f)", location.coordinate.latitude, location.coordinate.longitude];
//        [_webView stringByEvaluatingJavaScriptFromString:functionCall];
        
        [_locationManager stopUpdatingLocation];
        [self toogleButtons];
        [self performSegueWithIdentifier:@"addNewPointSegue" sender:self];
    }
}

- (void)prepareForSegue:(UIStoryboardSegue *)segue sender:(id)sender {
    if ([segue.identifier isEqualToString:@"addNewPointSegue"]) {
        
        NSLog(@"%.5f  %.5f", _locationManager.location.coordinate.latitude, _locationManager.location.coordinate.longitude);
        
//        AddNewPointViewController *addNewPointViewController = (AddNewPointViewController*) segue.destinationViewController;
//        addNewPointViewController.latitude = _location.x;
    }
}

-(IBAction)addCurrentLocation:(id)sender {
    if (SYSTEM_VERSION_GREATER_THAN_OR_EQUAL_TO(@"8.0")){
        [_locationManager requestWhenInUseAuthorization];
    }
    [_locationManager startUpdatingLocation];
}

-(IBAction)addSelectedLocation:(id)sender {
    [self toogleButtons];
}

-(void)actionSheet:(UIActionSheet *)actionSheet clickedButtonAtIndex:(NSInteger)buttonIndex {
    if (buttonIndex == 0) {
        if (SYSTEM_VERSION_GREATER_THAN_OR_EQUAL_TO(@"8.0")){
            [_locationManager requestWhenInUseAuthorization];
        }
        [_locationManager startUpdatingLocation];
    }
}

- (void)locationManager:(CLLocationManager *)manager didFailWithError:(NSError *)error {
    NSLog(@"didFailWithError: %@", error);
    
    UIAlertView *errorAlert = [[UIAlertView alloc] initWithTitle:@"Erro de GPS" message:@"Houve um erro ao tentar obter sua localização" delegate:self cancelButtonTitle:@"OK" otherButtonTitles:nil];
    
    [errorAlert show];
}

- (void)viewWillAppear:(BOOL)animated {
    [self.navigationController setNavigationBarHidden:YES animated:animated];
    //_sideMenu.panMode = MFSideMenuPanModeDefault;
    [super viewWillAppear:animated];
}

//- (void)viewWillDisappear:(BOOL)animated {
//    [super viewWillDisappear:animated];
//    
//}

-(void)viewDidDisappear:(BOOL)animated {
    [super viewDidDisappear:animated];
    [self.navigationController setNavigationBarHidden:NO animated:animated];
}

@end
