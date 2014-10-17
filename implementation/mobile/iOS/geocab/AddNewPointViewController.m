//
//  AddNewPointViewController.m
//  geocab
//
//  Created by Henrique Lobato on 30/09/14.
//  Copyright (c) 2014 Itaipu. All rights reserved.
//

#import "AddNewPointViewController.h"
#import "Marker.h"

@interface AddNewPointViewController()

@property (strong, nonatomic) IBOutlet UITableView *layerTableView;
@property (strong, nonatomic) FDTakeController *takeController;
@property (weak, nonatomic) IBOutlet UIImageView *imageView;
@property (strong, nonatomic) SelectLayerViewController *selectLayerViewController;
@property (strong, nonatomic) UINavigationController *navigationController;
@property (strong, nonatomic) Marker *NewMarker;
@property (weak, nonatomic) IBOutlet UILabel *layerTitle;
@property (weak, nonatomic) IBOutlet UIButton *imageButton;
@property (strong, nonatomic) IBOutlet UIScrollView *scrollView;

@end

@implementation AddNewPointViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    
    _layerTableView.delegate = self;
    _layerTableView.dataSource = self;
    
    _scrollView.delegate = self;
    
    [_layerTableView.layer setBorderWidth:0.3];
    [_layerTableView.layer setBorderColor:[UIColor lightGrayColor].CGColor];
    
    _selectLayerViewController = [[SelectLayerViewController alloc] init];
    _selectLayerViewController.delegate = self;
    
    [_pointDescription.layer setBorderWidth:0.3];
    [_pointDescription.layer setBorderColor:[UIColor lightGrayColor].CGColor];
    [_pointDescription.layer setCornerRadius:5];
    
    [_imageButton.layer setBorderWidth:0.3];
    [_imageButton.layer setBorderColor:[UIColor lightGrayColor].CGColor];
    [_imageButton.layer setCornerRadius:5];
        
    _NewMarker = [[Marker alloc] init];
    
    _takeController = [[FDTakeController alloc] init];
    _takeController.delegate = self;
}

- (IBAction)takePhotoOrChoseFromLibrary:(id)sender {
    [self.takeController takePhotoOrChooseFromLibrary];
}

- (IBAction)selectLayer:(id)sender {
    
    self.navigationController = [[UINavigationController alloc] initWithRootViewController:_selectLayerViewController];
    
    [self presentViewController:_navigationController animated:YES completion:^{
    }];
}

- (IBAction)saveMarker:(id)sender {
    
}

- (void)didEndSelecting:(Layer *)selectedLayer{
    _NewMarker.layer = selectedLayer;
    _layerTitle.text = _NewMarker.layer.title;
//    _layerImage.image = [UIImage imageWithData:[NSData dataWithContentsOfURL:[NSURL URLWithString:_NewMarker.layer.legend]]];
    
    [_navigationController dismissViewControllerAnimated:YES completion:^{
        
    }];
}

-(void)cancelledSelecting {
    [_navigationController dismissViewControllerAnimated:YES completion:^{
        
    }];
}

- (id)initWithNibName:(NSString *)nibNameOrNil bundle:(NSBundle *)nibBundleOrNil
{
    self = [super initWithNibName:nibNameOrNil bundle:nibBundleOrNil];
    if (self) {
        // Custom initialization
    }
    return self;
}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section {
    return 1;
}

- (UITableViewCell*)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath {
    static NSString *CellIdentifier = @"layerCell";
    
    UITableViewCell *cell = [_layerTableView dequeueReusableCellWithIdentifier:CellIdentifier forIndexPath:indexPath];
    
    if (cell == nil) {
        cell = [[UITableViewCell alloc] init];
    }
    
    UIButton *selectLayerButton = (UIButton*)[cell viewWithTag:1001];
    [selectLayerButton addTarget:self action:@selector(selectLayer:) forControlEvents:UIControlEventTouchUpInside];

    
    return cell;
}

-(void)tableView:(UITableView *)_tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath {
    [self selectLayer:self];
}

- (void)takeController:(FDTakeController *)controller gotPhoto:(UIImage *)photo withInfo:(NSDictionary *)info {
    [self.imageView setImage:photo];
}

@end