//
//  AddNewPointViewController.m
//  geocab
//
//  Created by Henrique Lobato on 30/09/14.
//  Copyright (c) 2014 Itaipu. All rights reserved.
//

#import "AddNewPointViewController.h"

@interface AddNewPointViewController()

@property (strong, nonatomic) IBOutlet UITableView *layerTableView;
@property (strong, nonatomic) FDTakeController *takeController;
@property (weak, nonatomic) IBOutlet UIImageView *imageView;
//@property (strong, nonatomic) UITapGestureRecognizer *gestureRecognizer;

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
    
    _takeController = [[FDTakeController alloc] init];
    _takeController.delegate = self;
}

- (IBAction)takePhotoOrChoseFromLibrary:(id)sender {
    [self.takeController takePhotoOrChooseFromLibrary];
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
    
    cell.textLabel.text = @"Camada";
    
    return cell;
}

- (void)takeController:(FDTakeController *)controller gotPhoto:(UIImage *)photo withInfo:(NSDictionary *)info {
    [self.imageView setImage:photo];
}

@end