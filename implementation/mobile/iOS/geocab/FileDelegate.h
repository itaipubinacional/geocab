//
//  FileDelegate.h
//  geocab
//
//  Created by Joaz on 22/10/14.
//  Copyright (c) 2014 Itaipu. All rights reserved.
//

#import "AbstractDelegate.h"
#import "Marker.h"
#import "MotiveMarkerModeration.h"

@interface FileDelegate : AbstractDelegate

- (void) downloadMarkerAttributePhoto: (NSNumber *) markerId success: (void(^)(AFHTTPRequestOperation *operation, id responseObject)) success fail: (void(^)(AFHTTPRequestOperation *operation, NSError *error)) fail;

- (void) uploadMarkerAttributePhoto:(NSNumber *)markerId image:(UIImage *)image;

@end
