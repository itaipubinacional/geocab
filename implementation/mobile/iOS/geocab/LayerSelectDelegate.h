//
//  LayerSelectDelegate.h
//  geocab
//
//  Created by Henrique Lobato on 09/10/14.
//  Copyright (c) 2014 Itaipu. All rights reserved.
//

#import <Foundation/Foundation.h>

@protocol LayerSelectDelegate <NSObject>
@required
- (void) doneSelecting;
@end

@interface LayerSelectDelegate : NSObject

{
    // Delegate to respond back
    id <LayerSelectDelegate> _delegate;
    
}
@property (nonatomic,strong) id delegate;

- (void)startProcess;

@end
