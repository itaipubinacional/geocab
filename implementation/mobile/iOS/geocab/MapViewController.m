//
//  MapViewController.m
//  geocab
//
//  Created by Vinicius Ramos Kawamoto on 18/09/14.
//  Copyright (c) 2014 Itaipu. All rights reserved.
//

#import "MapViewController.h"

@interface MapViewController ()

@property (strong, nonatomic) CLLocationManager *locationManager;

@property (nonatomic) CGPoint location;
@property (nonatomic, strong) NSTimer *timer;

@property (retain, nonatomic) UIActionSheet *actionSheet;

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
    
    //Configures the touch and hold event for adding a new point
    /*UILongPressGestureRecognizer *gesture = [[UILongPressGestureRecognizer alloc] initWithTarget:self action:@selector(handleGesture:)];
     gesture.minimumPressDuration = 0.1;
     gesture.allowableMovement = 600;
     [self.view addGestureRecognizer:gesture];*/
    
    if (_actionSheet == nil)
        _actionSheet = [[UIActionSheet alloc] initWithTitle:nil delegate:self cancelButtonTitle:@"Cancelar" destructiveButtonTitle:nil otherButtonTitles:@"Adicionar minha localização", @"Adicionar outra localização",nil];
    //[_actionSheet setBackgroundColor:[UIColor whiteColor]];
    [_actionSheet setTintColor:[UIColor blackColor]];
    
    
}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

- (void)handleGesture:(UIGestureRecognizer *) gesture {
    
    self.location = [gesture locationInView:self.webView];
    
    if (gesture.state == UIGestureRecognizerStateBegan) {
        self.timer = [NSTimer scheduledTimerWithTimeInterval:1 target:self selector:@selector(createPoint:) userInfo:nil repeats:YES];
    } else if (gesture.state == UIGestureRecognizerStateCancelled ||
               gesture.state == UIGestureRecognizerStateFailed ||
               gesture.state == UIGestureRecognizerStateEnded ||
               gesture.state == UIGestureRecognizerStateChanged) {
        [self.timer invalidate];
        self.timer = nil;
    }
    
    //[self someMethod:self.location];
}

- (void)createPoint:(NSTimer *)timer {
    [_actionSheet showInView:self.view];
}

- (IBAction)callJavascript:(id)sender {
    
    [_webView stringByEvaluatingJavaScriptFromString:@"caralegal()"];
    
}

-(IBAction)addNewPoint:(id)sender {
    [_actionSheet showInView:self.view];
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

        [self performSegueWithIdentifier:@"addNewPointSegue" sender:self];
    }
}

- (void)prepareForSegue:(UIStoryboardSegue *)segue sender:(id)sender {
    if ([segue.identifier isEqualToString:@"addNewPointSegue"]) {
        
    }
}

-(void)actionSheet:(UIActionSheet *)actionSheet clickedButtonAtIndex:(NSInteger)buttonIndex {
    if (buttonIndex == 0) {
        //[_locationManager startUpdatingLocation];
        [self performSegueWithIdentifier:@"addNewPointSegue" sender:self];
    }
}

-(void)viewDidAppear:(BOOL)animated {
    [super viewDidAppear:NO];
}

-(void)viewWillAppear:(BOOL)animated {
    [super viewWillAppear:NO];
}

@end
