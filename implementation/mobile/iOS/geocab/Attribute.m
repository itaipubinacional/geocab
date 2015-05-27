//
//  Attribute.m
//  geocab
//
//  Created by Henrique Lobato Zago on 23/10/14.
//  Copyright (c) 2014 Itaipu. All rights reserved.
//

#import "Attribute.h"
#import "Marker.h"

@implementation Attribute

@synthesize id, type, required, attributeDefault, layer, name, viewComponent;

- (NSString *)getViewComponentValue {
    
    if ([self.viewComponent class] == [UITextField class]){
        
        NSString *value = [((UITextField *)self.viewComponent) text];
        
        if ( [self.type isEqualToString:@"DATE"] )
            return [Marker dateStrToTimeStamp:value];
            
        return value;
        
    } else if ([self.viewComponent class] == [UISwitch class]){
        
        if ( [((UISwitch *)self.viewComponent) isOn] ){
            return @"Yes";
        }
        
        return @"No";
        
    }

    return nil;
}

+ (NSDictionary*) generateDictionary {
    
    return @{
             @"id"       : @"id",
             @"name"     : @"name",
             @"type"     : @"type",
             @"required" : @"required"
             };
    
}

@end
