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

@interface SelectLayerViewController ()

@property (retain, nonatomic) NSArray *layers;
@property (nonatomic, retain) NSMutableDictionary *sections;

@end

@implementation SelectLayerViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    
    self.clearsSelectionOnViewWillAppear = NO;
    
    self.tableView.delegate = self;
    self.tableView.dataSource = self;
    
    self.navigationItem.title = NSLocalizedString(@"Layers", @"");
    
    UIButton *button1=[UIButton buttonWithType:UIButtonTypeCustom];
    [button1 setFrame:CGRectMake(10.0, 2.0, 20.0, 20.0)];
    [button1 addTarget:self action:@selector(syncLayersAndLoadTable:) forControlEvents:UIControlEventTouchUpInside];
    [button1 setImage:[UIImage imageNamed:@"sync-button.png"] forState:UIControlStateNormal];
    UIBarButtonItem *button = [[UIBarButtonItem alloc]initWithCustomView:button1];
    self.navigationItem.rightBarButtonItem = button;
    
    self.navigationItem.leftBarButtonItem = [[UIBarButtonItem alloc] initWithBarButtonSystemItem:UIBarButtonSystemItemCancel target:self action:@selector(didCancel)];
    
    LayerDelegate *layerDelegate = [[LayerDelegate alloc] initWithUrl:@"layergroup/layers"];
    [layerDelegate list:^(RKObjectRequestOperation *operation, RKMappingResult *result) {
        
        _layers = [[result array] sortedArrayUsingDescriptors:@[[NSSortDescriptor sortDescriptorWithKey:@"name" ascending:YES]]];
        [self arrangeArrayInSections:_layers];
        [self.tableView reloadData];
        
    } userName:@"admin@geocab.com.br" password:@"admin"];
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
    return [[self.sections valueForKey:[[[self.sections allKeys] sortedArrayUsingSelector:@selector(localizedCaseInsensitiveCompare:)] objectAtIndex:section]] count];
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
    
    cell.layerTitle.text = layer.title;
    cell.legendImage.image = [UIImage imageWithData:[NSData dataWithContentsOfURL:[NSURL URLWithString:layer.legend]]];
//    cell.layer = [layer copy];
    
    return cell;
}

- (CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath {
    return 50;
}

#pragma mark - UITableView Delegate

-(void)tableView:(UITableView *)_tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath {
    // Which item?
    Layer * item = (Layer*)[[self.sections valueForKey:[[[self.sections allKeys] sortedArrayUsingSelector:@selector(localizedCaseInsensitiveCompare:)] objectAtIndex:indexPath.section]] objectAtIndex:indexPath.row];
    
    if (self.multipleSelection) {
        item.selected = !item.selected;
        
        // Update UI
        [_tableView deselectRowAtIndexPath:indexPath animated:YES];
        [_tableView cellForRowAtIndexPath:indexPath].accessoryType = item.selected ? UITableViewCellAccessoryCheckmark : UITableViewCellAccessoryNone;
        
        // Delegate callback
        if (item.selected) {
//            if ([_delegate respondsToSelector:@selector(didEndSelecting:)]) [_delegate didEndSelecting:item];
        } else {
            //        if ([delegate respondsToSelector:@selector(selectorDidDeselectItem:)]) [delegate selectorDidDeselectItem:item];
            //        if (selectorMode==KNSelectorModeSelected) {
            //            [_tableView deleteRowsAtIndexPaths:[NSArray arrayWithObject:indexPath] withRowAnimation:UITableViewRowAnimationAutomatic];
            //        }
        }
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

/*
 // Override to support conditional editing of the table view.
 - (BOOL)tableView:(UITableView *)tableView canEditRowAtIndexPath:(NSIndexPath *)indexPath {
 // Return NO if you do not want the specified item to be editable.
 return YES;
 }
 */

/*
 // Override to support editing the table view.
 - (void)tableView:(UITableView *)tableView commitEditingStyle:(UITableViewCellEditingStyle)editingStyle forRowAtIndexPath:(NSIndexPath *)indexPath {
 if (editingStyle == UITableViewCellEditingStyleDelete) {
 // Delete the row from the data source
 [tableView deleteRowsAtIndexPaths:@[indexPath] withRowAnimation:UITableViewRowAnimationFade];
 } else if (editingStyle == UITableViewCellEditingStyleInsert) {
 // Create a new instance of the appropriate class, insert it into the array, and add a new row to the table view
 }
 }
 */

/*
 // Override to support rearranging the table view.
 - (void)tableView:(UITableView *)tableView moveRowAtIndexPath:(NSIndexPath *)fromIndexPath toIndexPath:(NSIndexPath *)toIndexPath {
 }
 */

/*
 // Override to support conditional rearranging of the table view.
 - (BOOL)tableView:(UITableView *)tableView canMoveRowAtIndexPath:(NSIndexPath *)indexPath {
 // Return NO if you do not want the item to be re-orderable.
 return YES;
 }
 */

/*
 #pragma mark - Navigation
 
 // In a storyboard-based application, you will often want to do a little preparation before navigation
 - (void)prepareForSegue:(UIStoryboardSegue *)segue sender:(id)sender {
 // Get the new view controller using [segue destinationViewController].
 // Pass the selected object to the new view controller.
 }
 */

@end
