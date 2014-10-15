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
@property (weak, nonatomic) IBOutlet UILabel *layerName;
@property (weak, nonatomic) IBOutlet UIImageView *layerImage;

@end

@implementation AddNewPointViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    
    _pointDescription.layer.borderWidth = 0.5f;
    _pointDescription.layer.borderColor = [UIColor grayColor].CGColor;
    _pointDescription.layer.cornerRadius = 5;
    
    _pointName.layer.borderWidth = 0.5f;
    _pointName.layer.borderColor = [UIColor grayColor].CGColor;
    _pointName.layer.cornerRadius = 5;
    
    _layerTableView.delegate = self;
    _layerTableView.dataSource = self;
    
    //[self.navigationController setNavigationBarHidden:NO];
    
    _selectLayerViewController = [[SelectLayerViewController alloc] init];
    _selectLayerViewController.delegate = self;
    
    _layerTitle.text = @"Select a layer";
    _layerName.text = @"";
    
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
    _layerName.text = _NewMarker.layer.name;
    _layerImage.image = [UIImage imageWithData:[NSData dataWithContentsOfURL:[NSURL URLWithString:_NewMarker.layer.legend]]];
    
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

- (void)takeController:(FDTakeController *)controller gotPhoto:(UIImage *)photo withInfo:(NSDictionary *)info {
    [self.imageView setImage:photo];
}

@end