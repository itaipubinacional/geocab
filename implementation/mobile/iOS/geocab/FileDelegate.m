//
//  FileDelegate.m
//  geocab
//
//  Created by Joaz on 22/10/14.
//  Copyright (c) 2014 Itaipu. All rights reserved.
//

#import "FileDelegate.h"

@implementation FileDelegate

- (void) downloadMarkerAttributePhoto: (NSNumber *) markerId success: (void(^)(AFHTTPRequestOperation *operation, id responseObject)) success fail: (void(^)(AFHTTPRequestOperation *operation, NSError *error)) fail login:(NSString*)login password:(NSString*)password
{
    NSString *urlString = [[self.baseUrl stringByAppendingString:[markerId stringValue]] stringByAppendingString:@"/download"];
    
    NSURL* url = [NSURL URLWithString:urlString];
    RKObjectManager* objectManager = [RKObjectManager managerWithBaseURL:url];
    [objectManager.HTTPClient setAuthorizationHeaderWithUsername:login password:password];
    
    NSURLRequest *request = [objectManager requestWithObject:nil method:RKRequestMethodGET path:@"" parameters:nil];
    
    NSMutableURLRequest *downloadRequest = [objectManager requestWithObject:request method:RKRequestMethodGET path:urlString parameters:nil];
    AFHTTPRequestOperation *requestOperation = [[AFImageRequestOperation alloc] initWithRequest:downloadRequest];
    
    [requestOperation setCompletionBlockWithSuccess:success failure:fail];
    [objectManager.HTTPClient enqueueHTTPRequestOperation:requestOperation];
}

- (void) uploadMarkerAttributePhoto:(NSNumber *)markerId image:(UIImage *)image login:(NSString*)login password:(NSString*)password handler: (void(^)(NSURLResponse *response, NSData *data, NSError *connectionError)) handler
{
    // the server url to which the image (or the media) is uploaded.
    NSString *urlString = [self.baseUrl stringByAppendingString:@"marker/"];
    urlString = [urlString stringByAppendingString:[markerId stringValue]];
    urlString = [urlString stringByAppendingString:@"/upload"];
    NSURL* requestURL = [NSURL URLWithString:urlString];
    
    // random string, that will not repeat in post data, to separate post data fields.
    NSString *BoundaryConstant = @"----------V2ymHFg03ehbqgZCaKO6jy";
    
    // string constant for the post parameter 'file'.
    NSString* FileParamConstant = @"file";
    
    // create request
    NSMutableURLRequest *request = [[NSMutableURLRequest alloc] init];
    NSString *credentials = [[login stringByAppendingString:@":"] stringByAppendingString:password];
    NSData *credentialsData = [credentials dataUsingEncoding:NSUTF8StringEncoding];
    NSString *credentialsEncoded = [credentialsData base64EncodedStringWithOptions:0];
    [request setValue:credentialsEncoded forHTTPHeaderField:@"Authorization"];
    
    [request setCachePolicy:NSURLRequestReloadIgnoringLocalCacheData];
    [request setHTTPShouldHandleCookies:NO];
    [request setTimeoutInterval:30];
    [request setHTTPMethod:@"POST"];
    
    // set Content-Type in HTTP header
    NSString *contentType = [NSString stringWithFormat:@"multipart/form-data; boundary=%@", BoundaryConstant];
    [request setValue:contentType forHTTPHeaderField: @"Content-Type"];
    
    // post body
    NSMutableData *body = [NSMutableData data];
    
    // add image data
    NSData *imageData = UIImageJPEGRepresentation(image, 1.0);
    if (imageData) {
        [body appendData:[[NSString stringWithFormat:@"--%@\r\n", BoundaryConstant] dataUsingEncoding:NSUTF8StringEncoding]];
        [body appendData:[[NSString stringWithFormat:@"Content-Disposition: form-data; name=\"%@\"; filename=\"marker-foto.jpg\"\r\n", FileParamConstant] dataUsingEncoding:NSUTF8StringEncoding]];
        [body appendData:[@"Content-Type: image/jpeg\r\n\r\n" dataUsingEncoding:NSUTF8StringEncoding]];
        [body appendData:imageData];
        [body appendData:[[NSString stringWithFormat:@"\r\n"] dataUsingEncoding:NSUTF8StringEncoding]];
    }
    
    [body appendData:[[NSString stringWithFormat:@"--%@--\r\n", BoundaryConstant] dataUsingEncoding:NSUTF8StringEncoding]];
    
    // setting the body of the post to the reqeust
    [request setHTTPBody:body];
    
    // set the content-length
    NSString *postLength = [NSString stringWithFormat:@"%d", [body length]];
    [request setValue:postLength forHTTPHeaderField:@"Content-Length"];
    
    // set URL
    [request setURL:requestURL];
    NSOperationQueue *queue = [[NSOperationQueue alloc] init];
    [NSURLConnection sendAsynchronousRequest:request queue:queue completionHandler:handler];
    
}

@end
