//
//  LayerTableViewCell.h
//  geocab
//
//  Created by Henrique Lobato on 09/10/14.
//  Copyright (c) 2014 Itaipu. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "Layer.h"

@interface LayerTableViewCell : UITableViewCell

@property (strong, nonatomic) Layer *layer;
@property (weak, nonatomic) IBOutlet UIImageView *legendImage;
@property (weak, nonatomic) IBOutlet UILabel *layerTitle;

@end
