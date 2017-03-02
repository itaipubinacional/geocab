//
//  SideMenuTableViewController.m
//  geocab
//
//  Created by Henrique Lobato on 06/10/14.
//  Copyright (c) 2014 Itaipu. All rights reserved.
//

#import "SideMenuTableViewController.h"
#import "LayerDelegate.h"
#import "Layer.h"

@interface SideMenuTableViewController ()

@property (retain, nonatomic) NSArray *layers;
@property (retain, nonatomic) NSMutableArray *filteredLayers;
@property (retain, nonatomic) NSMutableArray *selectedLayers;
@property (weak, nonatomic) IBOutlet UISearchBar *searchBar;

@end

@implementation SideMenuTableViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    
    // Uncomment the following line to preserve selection between presentations.
     self.clearsSelectionOnViewWillAppear = NO;
    
    self.tableView.delegate = self;
    self.tableView.dataSource = self;
    
    self.searchBar.delegate = self;
    self.searchDisplayController.delegate = self;
    
    CGRect bounds = self.tableView.bounds;
    bounds.origin.y = bounds.origin.y + self.searchBar.bounds.size.height;
    self.tableView.bounds = bounds;
    
    [[NSNotificationCenter defaultCenter] addObserver:self selector:@selector(menuStateEventOcurred:) name:@"menuListener" object:nil];
    
    LayerDelegate *layerDelegate = [[LayerDelegate alloc] initWithUrl:@"layergroup/layers"];
    [layerDelegate list:^(RKObjectRequestOperation *operation, RKMappingResult *result) {
        
        _layers = [[result array] sortedArrayUsingDescriptors:@[[NSSortDescriptor sortDescriptorWithKey:@"name" ascending:YES]]];
        _filteredLayers = [_layers mutableCopy];
        _selectedLayers = [NSMutableArray arrayWithCapacity:[_layers count]];
        [self.tableView reloadData];
        
    } failBlock:^(RKObjectRequestOperation *operation, NSError *error) {
        
        UIAlertView *alert = [[UIAlertView alloc] initWithTitle:NSLocalizedString(@"error", @"")
                                                        message:NSLocalizedString(@"layer-fetch.error.message", @"")
                                                       delegate:nil
                                              cancelButtonTitle:@"OK"
                                              otherButtonTitles:nil];
        [alert show];
        
        
    } userName:@"admin@geocab.com.br" password:@"admin"];
}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

#pragma mark - Table view data source

- (NSInteger)numberOfSectionsInTableView:(UITableView *)tableView {
    // Return the number of sections.
    return 1;
}

- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section {
    // Return the number of rows in the section.
    return [_filteredLayers count];
}


- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath {
    UITableViewCell *cell = [self.tableView dequeueReusableCellWithIdentifier:@"cell" forIndexPath:indexPath];
    
    if (cell == nil) {
        cell = [[UITableViewCell alloc] init];
    }
    
    Layer *layer = (Layer*)[_filteredLayers objectAtIndex:indexPath.row];
    
    cell.textLabel.text = layer.title;
    cell.detailTextLabel.text = layer.name;
    cell.imageView.image = [UIImage imageWithData:[NSData dataWithContentsOfURL:[NSURL URLWithString:layer.legend]]];
    
    return cell;
}

- (CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath {
    return 50;
}

- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath
{
    // Update the delete button's title based on how many items are selected.
    [self updateSelectedLayers];
}

- (void)filterContentForSearchText:(NSString*)searchText scope:(NSString*)scope {
    // Update the filtered array based on the search text and scope.
    // Remove all objects from the filtered search array
    [self.filteredLayers removeAllObjects];
    // Filter the array using NSPredicate
    NSPredicate *predicate = [NSPredicate predicateWithFormat:@"SELF.title contains[c] %@",searchText];
    _filteredLayers = [NSMutableArray arrayWithArray:[_layers filteredArrayUsingPredicate:predicate]];
}

#pragma mark - UISearchDisplayController Delegate Methods
-(BOOL)searchDisplayController:(UISearchDisplayController *)controller shouldReloadTableForSearchString:(NSString *)searchString
{
    // Tells the table data source to reload when text changes
    [self filterContentForSearchText:searchString scope:
     [[self.searchDisplayController.searchBar scopeButtonTitles] objectAtIndex:[self.searchDisplayController.searchBar selectedScopeButtonIndex]]];
    // Return YES to cause the search result table view to be reloaded.
    return YES;
}

-(BOOL)searchDisplayController:(UISearchDisplayController *)controller shouldReloadTableForSearchScope:(NSInteger)searchOption
{
    // Tells the table data source to reload when scope bar selection changes
    [self filterContentForSearchText:self.searchDisplayController.searchBar.text scope:
     [[self.searchDisplayController.searchBar scopeButtonTitles] objectAtIndex:searchOption]];
    // Return YES to cause the search result table view to be reloaded.
    return YES;
}

- (void)searchDisplayController:(UISearchDisplayController *)controller didLoadSearchResultsTableView:(UITableView *)tableView
{
    tableView.rowHeight = 60;
}

- (void)searchBarTextDidEndEditing:(UISearchBar *)searchBar
{
    if (!self.searchDisplayController.isActive)
    {
        _filteredLayers = [NSMutableArray arrayWithArray:_layers];
    }
}

- (void)updateSelectedLayers
{
//    NSArray *selectedRows = [self.tableView indexPathsForSelectedRows];
//    [_selectedLayers removeAllObjects];
//    
//    for (NSIndexPath *index in selectedRows) {
//        [_selectedLayers addObject:[_filteredLayers objectAtIndex:(long)index.row]];
//    }
    
}

-(void)menuStateEventOcurred:(NSNotification *)notification
{

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
