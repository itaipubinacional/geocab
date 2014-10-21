//
//  SideMenuTableViewController.m
//  geocab
//
//  Created by Henrique Lobato on 06/10/14.
//  Copyright (c) 2014 Itaipu. All rights reserved.
//

#import "SelectLayerViewController.h"
#import "LayerDelegate.h"
#import "Layer.h"
#import "LayerTableViewCell.h"
#import "ControllerUtil.h"
#import "User.h"

@interface SelectLayerViewController ()

@property (retain, nonatomic) NSArray *layers;
@property (nonatomic, retain) NSMutableDictionary *sections;
@property (nonatomic, retain) UITableView *tableView;

extern User *loggedUser;

@end

@implementation SelectLayerViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    
    self.tableView.delegate = self;
    self.tableView.dataSource = self;
    
    //Navigation Bar
    self.navigationItem.title = NSLocalizedString(@"Layers", @"");
    
    self.tableView = [[UITableView alloc] initWithFrame:CGRectMake(0, 0, self.view.bounds.size.width, self.view.bounds.size.height)];
    [self.view addSubview:self.tableView];
    
    if (self.multipleSelection) {
        UIButton *button1=[UIButton buttonWithType:UIButtonTypeCustom];
        [button1 setFrame:CGRectMake(10.0, 2.0, 20.0, 20.0)];
        [button1 addTarget:self action:@selector(syncLayersAndLoadTable:) forControlEvents:UIControlEventTouchUpInside];
        [button1 setImage:[UIImage imageNamed:@"sync-button.png"] forState:UIControlStateNormal];
        UIBarButtonItem *button = [[UIBarButtonItem alloc]initWithCustomView:button1];
        self.navigationItem.leftBarButtonItem = button;
        
        UIBarButtonItem *buttonItem = [[UIBarButtonItem alloc] initWithTitle:@"X" style:UIBarButtonItemStylePlain target:self action:@selector(didFinish)];
        self.navigationItem.rightBarButtonItem = buttonItem;
        
        UIButton *logoutButton = [UIButton buttonWithType:UIButtonTypeRoundedRect];
        logoutButton.frame = CGRectMake(0, self.view.bounds.size.height-44, 320, 44);
        logoutButton.titleLabel.font = [UIFont systemFontOfSize:13];
        logoutButton.backgroundColor = [UIColor lightGrayColor];
        logoutButton.tintColor = [UIColor whiteColor];
        
        [logoutButton addTarget:self action:@selector(logoutMethodCall) forControlEvents:UIControlEventTouchUpInside];
        
        [logoutButton setTitle:@"Logout" forState:UIControlStateNormal];
        [self.view addSubview:logoutButton];
        
    } else {
        UIButton *button1=[UIButton buttonWithType:UIButtonTypeCustom];
        [button1 setFrame:CGRectMake(10.0, 2.0, 20.0, 20.0)];
        [button1 addTarget:self action:@selector(syncLayersAndLoadTable:) forControlEvents:UIControlEventTouchUpInside];
        [button1 setImage:[UIImage imageNamed:@"sync-button.png"] forState:UIControlStateNormal];
        UIBarButtonItem *button = [[UIBarButtonItem alloc]initWithCustomView:button1];
        self.navigationItem.rightBarButtonItem = button;
        
        self.navigationItem.leftBarButtonItem = [[UIBarButtonItem alloc] initWithBarButtonSystemItem:UIBarButtonSystemItemCancel target:self action:@selector(didCancel)];
    }
    
    
    
    LayerDelegate *layerDelegate = [[LayerDelegate alloc] initWithUrl:@"layergroup/layers"];
    [layerDelegate list:^(RKObjectRequestOperation *operation, RKMappingResult *result) {
        
        _layers = [[result array] sortedArrayUsingDescriptors:@[[NSSortDescriptor sortDescriptorWithKey:@"name" ascending:YES]]];
        [self arrangeArrayInSections:_layers];
        [self.tableView reloadData];
        
    } userName:loggedUser.email password:loggedUser.password];
    
    
}

-(void)arrangeArrayInSections:(NSArray*) array {
    
    self.sections = [[NSMutableDictionary alloc] init];
    BOOL found;
    for (Layer *layer in array)
    {
        NSString *c = [[layer title] substringToIndex:1];
        found = NO;
        
        for (NSString *str in [self.sections allKeys])
        {
            if ([str isEqualToString:c])
            {
                found = YES;
            }
        }
        if (!found)
        {
            [self.sections setValue:[[NSMutableArray alloc] init] forKey:c];
        }
    }
    for (Layer *layer in self.layers)
    {
        [[self.sections objectForKey:[[layer title] substringToIndex:1]] addObject:layer];
    }
    for (NSString *key in [self.sections allKeys])
    {
        [[self.sections objectForKey:key] sortUsingDescriptors:[NSArray arrayWithObject:[NSSortDescriptor sortDescriptorWithKey:@"title" ascending:YES]]];
    }
}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

- (IBAction)syncLayersAndLoadTable:(id)sender {
    LayerDelegate *layerDelegate = [[LayerDelegate alloc] initWithUrl:@"layergroup/layers"];
    [layerDelegate list:^(RKObjectRequestOperation *operation, RKMappingResult *result) {
        
        _layers = [[result array] sortedArrayUsingDescriptors:@[[NSSortDescriptor sortDescriptorWithKey:@"name" ascending:YES]]];
        [self.tableView reloadData];
        
    } userName:@"admin@geocab.com.br" password:@"admin"];
}

#pragma mark - Table view data source

- (NSInteger)numberOfSectionsInTableView:(UITableView *)tableView {
    // Return the number of sections.
    return [[self.sections allKeys] count];
}

- (NSArray *)sectionIndexTitlesForTableView:(UITableView *)tableView {
    return [[self.sections allKeys] sortedArrayUsingSelector:@selector(localizedCaseInsensitiveCompare:)];
}

- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section {
    // Return the number of rows in the section.
//    return [[self.sections valueForKey:[[[self.sections allKeys] sortedArrayUsingSelector:@selector(localizedCaseInsensitiveCompare:)] objectAtIndex:section]] count];
    return 1;
}

- (NSString *)tableView:(UITableView *)tableView titleForHeaderInSection:(NSInteger)section
{
    return [[[self.sections allKeys] sortedArrayUsingSelector:@selector(localizedCaseInsensitiveCompare:)] objectAtIndex:section];
}

- (void)tableView:(UITableView *)tableView willDisplayHeaderView:(UIView *)view forSection:(NSInteger)section
{
    // Background color
    view.tintColor = [ControllerUtil colorWithHexString:@"e7e7e7"];
    // Text Color
    UITableViewHeaderFooterView *header = (UITableViewHeaderFooterView *)view;
    [header.textLabel setTextColor:[ControllerUtil colorWithHexString:@"5a5a5a"]];
    header.textLabel.font = [UIFont boldSystemFontOfSize:16.0f];
    
    CALayer* layer = view.layer;
    
    CALayer* bottomBorder = [CALayer layer];
    bottomBorder.borderColor = [ControllerUtil colorWithHexString:@"e7e7e7"].CGColor;
    bottomBorder.borderWidth = 1;
    bottomBorder.frame = CGRectMake(-1, layer.frame.size.height-1, layer.frame.size.width, 1);
    [bottomBorder setBorderColor:[ControllerUtil colorWithHexString:@"e7e7e7"].CGColor];
    [layer addSublayer:bottomBorder];
}

- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath {
    static NSString *cellId = @"LayerTableCell";
    
    LayerTableViewCell *cell = (LayerTableViewCell*)[tableView dequeueReusableCellWithIdentifier:cellId];
    
    if (cell == nil)
    {
        NSArray *nib = [[NSBundle mainBundle] loadNibNamed:@"LayerTableViewCell" owner:self options:nil];
        cell = (LayerTableViewCell*)[nib objectAtIndex:0];
    }
    
    Layer *layer = (Layer*) [[self.sections valueForKey:[[[self.sections allKeys] sortedArrayUsingSelector:@selector(localizedCaseInsensitiveCompare:)] objectAtIndex:indexPath.section]] objectAtIndex:indexPath.row];
    
    cell.accessoryType = layer.selected ? UITableViewCellAccessoryCheckmark : UITableViewCellAccessoryNone;
    
    cell.layerTitle.text = layer.title;
    cell.legendImage.image = [UIImage imageWithData:[NSData dataWithContentsOfURL:[NSURL URLWithString:layer.legend]]];
    
    return cell;
}

- (CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath {
    return 50;
}

#pragma mark - UITableView Delegate

-(void)tableView:(UITableView *)_tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath {
    // Which item?
    Layer * item = (Layer*)[[self.sections valueForKey:[[[self.sections allKeys] sortedArrayUsingSelector:@selector(localizedCaseInsensitiveCompare:)] objectAtIndex:indexPath.section]] objectAtIndex:indexPath.row];
    
    if (self.multipleSelection ) {
        if (!item.selected && [self numberOfSelectedItems] < 3) {
            item.selected = !item.selected;
            if ([_delegate respondsToSelector:@selector(didCheckedLayer:)]) [_delegate didCheckedLayer:item];
        } else if (item.selected){
            item.selected = !item.selected;
            if ([_delegate respondsToSelector:@selector(didUnheckedLayer:)]) [_delegate didUnheckedLayer:item];
        }
        
        // Update UI
        [_tableView deselectRowAtIndexPath:indexPath animated:YES];
        [_tableView cellForRowAtIndexPath:indexPath].accessoryType = item.selected ? UITableViewCellAccessoryCheckmark : UITableViewCellAccessoryNone;
    } else {
        if ([_delegate respondsToSelector:@selector(didEndSelecting:)]) [_delegate didEndSelecting:item];
    }
    
    
}

#pragma mark - Cancel or Done button event

-(void)didCancel {
    // Clear all selections
    for (Layer * i in self.selectedItems) {
        i.selected = NO;
    }
    if ([_delegate respondsToSelector:@selector(cancelledSelecting)]) [_delegate cancelledSelecting];
}

-(void)didFinish {
    // Delegate callback
    if ([_delegate respondsToSelector:@selector(didEndMultipleSelecting:)]) {
        [_delegate didEndMultipleSelecting:self.selectedItems];
    }
}

-(NSArray*)selectedItems {
    NSPredicate *pred = [NSPredicate predicateWithFormat:@"selected = YES"];
    return [_layers filteredArrayUsingPredicate:pred];
}

-(NSUInteger)numberOfSelectedItems {
    return [[_layers filteredArrayUsingPredicate:[NSPredicate predicateWithFormat:@"selected = YES"]] count];
}

-(void)logoutMethodCall {
    if ([_delegate respondsToSelector:@selector(logoutButtonPressed)]) [_delegate logoutButtonPressed];
}

@end
