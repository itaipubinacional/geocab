//
//  SideMenuTableViewController.h
//  geocab
//
//  Created by Henrique Lobato on 06/10/14.
//  Copyright (c) 2014 Itaipu. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "Layer.h"
@protocol LayerSelectDelegate <NSObject>
@required
- (void)didEndSelecting:(Layer*) selectedLayer;
- (void)cancelledSelecting;
- (void)didEndMultipleSelecting:(NSArray*) selectedLayers;
- (void)didCheckedLayer:(Layer*) layer;
- (void)didUnheckedLayer:(Layer*) layer;
@end

@interface SelectLayerViewController : UITableViewController <UITableViewDataSource, UITableViewDelegate>

{
    // Delegate to respond back
    id <LayerSelectDelegate> _delegate;
    
}
@property (nonatomic,strong) id delegate;

@property BOOL multipleSelection;
@property (nonatomic, readonly) NSArray * selectedItems;

@end
