//
//  AddNewPointViewController.m
//  geocab
//
//  Created by Henrique Lobato on 30/09/14.
//  Copyright (c) 2014 Itaipu. All rights reserved.
//

#import "AddNewMarkerViewController.h"
#import "LayerDelegate.h"
#import "MarkerDelegate.h"
#import "MarkerAttribute.h"
#import "Attribute.h"
#import "AttributeType.h"

@interface AddNewMarkerViewController()

@property (strong, nonatomic) FDTakeController *takeController;
@property (strong, nonatomic) SelectLayerViewController *selectLayerViewController;
@property (strong, nonatomic) UINavigationController *navigationCtrl;
@property (nonatomic, assign) int positionY;
@property (nonatomic, retain) NSArray *layerAttributes;

@property (strong, nonatomic) IBOutlet UIScrollView *scrollView;
@property (strong, nonatomic) IBOutlet UIImageView *imageView;
@property (weak, nonatomic) IBOutlet UIButton *imageButton;
@property (weak, nonatomic) IBOutlet UIButton *selectLayer;
@property (weak, nonatomic) IBOutlet UIView *dynamicFieldsView;
@property (weak, nonatomic) UITextField *activeTextField;

extern NSUserDefaults *defaults;

@end

@implementation AddNewMarkerViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    
    _selectLayerViewController = [[SelectLayerViewController alloc] init];
    _selectLayerViewController.delegate = self;
    
    if ( self.marker == nil ){
        self.marker = [[Marker alloc] init];
        self.marker.wktCoordenate = _wktCoordenate;
        
    } else {
        
        // Seleciona a layer em caso de edição do marker
        if ( self.marker.layer != nil )
        {
            [self generateAttributeFieldsByLayer: self.marker.layer];
            [self.selectLayer setTitle: self.marker.layer.title forState:UIControlStateNormal ];
            [self.selectLayer setTintColor: [UIColor blackColor]];
        }
        
    }
    
    _takeController = [[FDTakeController alloc] init];
    _takeController.delegate = self;
    
    //Navigation Bar
    self.navigationController.navigationBarHidden = NO;
    self.navigationItem.title = NSLocalizedString(@"marker.information", @"");;
    
    UIButton *closeButton = [UIButton buttonWithType:UIButtonTypeCustom];
    [closeButton setFrame:CGRectMake(10.0, 2.0, 20.0, 20.0)];
    [closeButton addTarget:self action:@selector(didFinish) forControlEvents:UIControlEventTouchUpInside];
    [closeButton setImage:[UIImage imageNamed:@"menu-close-btn.png"] forState:UIControlStateNormal];
    UIBarButtonItem *buttonItem = [[UIBarButtonItem alloc]initWithCustomView:closeButton];
    self.navigationItem.rightBarButtonItem = buttonItem;
    self.navigationItem.hidesBackButton = YES;

}

- (IBAction)takePhotoOrChoseFromLibrary:(id)sender {
    [self.takeController takePhotoOrChooseFromLibrary];
}

- (IBAction)selectLayer:(id)sender {
    
    if ( self.marker.id == nil || self.marker.id == 0 ){
        
        self.navigationCtrl = [[UINavigationController alloc] initWithRootViewController:_selectLayerViewController];
        
        [self presentViewController:_navigationCtrl animated:YES completion:^{}];
        
    }

}

- (IBAction)saveMarker:(id)sender {
    if ([self validateForm]) {
        
        if ( self.marker.markerAttributes == nil )
	        self.marker.markerAttributes = [[NSMutableArray alloc] init];
        
        for ( Attribute *attribute in self.layerAttributes ){
            
            // Percore a lista de atributos do marker para bindar o valor em caso de edição            
            bool find = false;
            
            for ( MarkerAttribute *markerAttr in self.marker.markerAttributes ){
                
                if ( [markerAttr.attribute.name isEqualToString:attribute.name]){
                    markerAttr.value = [attribute getViewComponentValue];
                    find = true;
                    break;
                }
                
            }
            
            if ( !find ){
                MarkerAttribute *markerAttribute = [[MarkerAttribute alloc] init];
                markerAttribute.attribute = attribute;
                markerAttribute.value = [attribute getViewComponentValue];
                [self.marker.markerAttributes addObject:markerAttribute];
            }
            
        }
        
        MarkerDelegate *markerDelegate = [[MarkerDelegate alloc] initWithUrl:@"marker/"];
        
        // Chamada ao serviço para persistir o marker
        if ( self.marker.id == nil || self.marker.id == 0 ){
            
            self.marker.status = @"PENDING";
            self.marker.wktCoordenate = self.wktCoordenate;
            
            [markerDelegate insert:^(RKObjectRequestOperation *operation, RKMappingResult *result) {
                
                Marker *markerResponse = [[result array] objectAtIndex: 0];
                
                NSString *functionCall = [NSString stringWithFormat:@"geocabapp.addMarker('%@')", operation.HTTPRequestOperation.responseString];
                [_webView stringByEvaluatingJavaScriptFromString:functionCall];
                [_webView stringByEvaluatingJavaScriptFromString:@"geocabapp.changeToActionState()"];
                
                // Faz upload da imagem do marker
                if ( self.marker.imageUI != nil )
                    [markerDelegate uploadMarkerAttributePhoto: markerResponse.id image: self.marker.imageUI login:[defaults objectForKey:@"email"] password:[defaults objectForKey:@"password"]];
                
                [self didFinish];
                
            } userName:[defaults objectForKey:@"email"] password:[defaults objectForKey:@"password"] marker:self.marker];
            
        } else {
            
            [markerDelegate update:^(RKObjectRequestOperation *operation, RKMappingResult *result) {
                
                Marker *markerResponse = [[result array] objectAtIndex: 0];
                
                // Faz upload da imagem do marker
                if ( self.marker.imageUI != nil )
                    [markerDelegate uploadMarkerAttributePhoto: self.marker.id image: self.marker.imageUI login:[defaults objectForKey:@"email"] password:[defaults objectForKey:@"password"]];
                
                // Lista os atributos atualizados do marker
                [markerDelegate listAttributesById:^(RKObjectRequestOperation *operation, RKMappingResult *result) {
                    
                    NSMutableArray *markerAttributes = [[result array] mutableCopy];
                    markerResponse.markerAttributes = markerAttributes;
                    
                    MarkerDelegate *delegate = [[MarkerDelegate alloc] initWithUrl:@"files/markers/"];
                    
                    // Faz o download da imagem se houver
                    [delegate downloadMarkerAttributePhoto:self.marker.id success:^(AFHTTPRequestOperation *operation, id responseObject) {
                        
                        // Converte a imagem para base64
                        NSString *imageBase64 = responseObject != nil ? [NSString stringWithFormat:@"data:image/jpeg;base64,%@", [operation.responseData base64EncodedStringWithOptions:0]] : @"";
                        
						// Atualiza o panel com as informações do marker
                        NSString *functionCall = [NSString stringWithFormat:@"geocabapp.marker.loadAttributes('%@','%@')", [markerResponse toJSONString], imageBase64];
                        
                        [_webView stringByEvaluatingJavaScriptFromString:functionCall];
                        
                        [self didFinish];
                        
                    } fail:^(AFHTTPRequestOperation *operation, NSError *error) {
                        
                        NSString *functionCall = [NSString stringWithFormat:@"geocabapp.marker.loadAttributes('%@','')", [markerResponse toJSONString]];
                        
                        [_webView stringByEvaluatingJavaScriptFromString:functionCall];
                        
                        [self didFinish];
                        
                    } login:[defaults objectForKey:@"email"] password:[defaults objectForKey:@"password"]];
                    
                    
                } userName:[defaults objectForKey:@"email"] password:[defaults objectForKey:@"password"] markerId:self.marker.id];
                
                
            } userName:[defaults objectForKey:@"email"] password:[defaults objectForKey:@"password"] marker:self.marker];
            
        }

    }
}

- (void)didEndSelecting:(Layer *)selectedLayer{
    self.marker.layer = selectedLayer;
    
    [self generateAttributeFieldsByLayer: selectedLayer];
    
    [self.selectLayer setTitle: self.marker.layer.title forState:UIControlStateNormal ];
    
    [_navigationCtrl dismissViewControllerAnimated:YES completion:^{}];
}

-(void)cancelledSelecting {

    [_navigationCtrl dismissViewControllerAnimated:YES completion:^{}];
    
}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

- (BOOL)validateForm {
    
    // Percorre a lista de atributos da camada
    for (Attribute *attribute in self.layerAttributes) {
        
        NSString *fieldValue = [attribute getViewComponentValue];
        
        if ( attribute.required && (fieldValue == nil || [fieldValue isEqualToString:@""]) ) {
            
            NSString *message = [NSString stringWithFormat:@"Preencha o campo %@", attribute.name];
            
            UIAlertView *alert = [[UIAlertView alloc] initWithTitle:@"Dado não informado" message: message delegate:self cancelButtonTitle:@"OK" otherButtonTitles:nil];
            [alert show];
            
            return NO;
        }
    }
    
    return YES;
}

/**
 * Gera os atributos na aplicação de acordo com a camada
 * @param layer
 */
- (void)generateAttributeFieldsByLayer:(Layer *)layer {
    
    LayerDelegate *layerDelegate = [[LayerDelegate alloc] initWithUrl:@"layergroup"];
    
    [layerDelegate listAttributesById:^(RKObjectRequestOperation *operation, RKMappingResult *result) {
        
        self.layerAttributes = [result array];
    	[[self.dynamicFieldsView subviews] makeObjectsPerformSelector:@selector(removeFromSuperview)];
        self.dynamicFieldsView.translatesAutoresizingMaskIntoConstraints = NO;
        
        self.positionY = 0;
        int labelHight = 30;
        int fieldHeight = 50;
        
        // Percorre a lista de atributos da camada
        for (Attribute *attribute in self.layerAttributes) {
            
            NSString *markerValue = @"";
            
            // Percore a lista de atributos do marker para bindar o valor em caso de edição
            for ( MarkerAttribute *markerAttribute in self.marker.markerAttributes ){
                if ( [markerAttribute.attribute.name isEqualToString:attribute.name] ){
                    markerValue = markerAttribute.value;
                    break;
                }
            }
            
            // Label
            UILabel *uiLabel = [[UILabel alloc] init];
            uiLabel.text = [attribute.name capitalizedString];
            uiLabel.textAlignment =  NSTextAlignmentLeft;
            uiLabel.translatesAutoresizingMaskIntoConstraints = NO;
            [_dynamicFieldsView addSubview:uiLabel];
            
            // Posicionamento
			[self loadLeftTopPositionConstraints:uiLabel positionY:self.positionY];
            self.positionY += labelHight;
            
            // Verifica se e o ultimo atributo para adicionar constraint e determinar o height da superview
            if ( attribute == [ self.layerAttributes lastObject ] ) {
				[self.dynamicFieldsView addConstraint:[NSLayoutConstraint constraintWithItem:self.dynamicFieldsView attribute:NSLayoutAttributeBottom relatedBy:NSLayoutRelationGreaterThanOrEqual toItem:uiLabel attribute:NSLayoutAttributeBottom multiplier:1 constant:50.0]];
            }
        
            if ( [attribute.type isEqual:@"TEXT"] ){
                
                UITextField *uiTextField = [self generateTextField];
                [uiTextField setKeyboardType: UIKeyboardTypeDefault];
                
                // Adiciona o campo a view dinamica
                [_dynamicFieldsView addSubview:uiTextField];
                
                // Posicionamento
                [self loadTextFieldPositionConstraints:uiTextField positionY:self.positionY];
                self.positionY += fieldHeight;
                
                // Em caso de edicao preenche o valor
                uiTextField.text = markerValue;
                attribute.viewComponent = uiTextField;
                
            } else if ( [attribute.type isEqual:@"NUMBER"] ){
                
                UITextField *uiTextField = [self generateTextField];
                [uiTextField setKeyboardType:UIKeyboardTypeNumberPad];
                
                // Adiciona o campo a view dinamica
                [_dynamicFieldsView addSubview:uiTextField];
                
                // Posicionamento
                [self loadTextFieldPositionConstraints:uiTextField positionY:self.positionY];
                self.positionY += fieldHeight;
                
                // Em caso de edicao preenche o valor
                uiTextField.text = markerValue;
                attribute.viewComponent = uiTextField;
                
            } else if ( [attribute.type isEqual:@"BOOLEAN"] ){
                
		    	UISwitch *uiSwitch = [[UISwitch alloc] init];
                uiSwitch.translatesAutoresizingMaskIntoConstraints = NO;
                
                // Adiciona o campo a view dinamica
                [_dynamicFieldsView addSubview:uiSwitch];
                
                // Posicionamento
                [self loadLeftTopPositionConstraints:uiSwitch positionY:self.positionY];
                self.positionY += fieldHeight;
                
                // Em caso de edicao preenche o valor
                if ( [markerValue isEqualToString:@"Yes"] )
					[uiSwitch setOn:true];
                      
                attribute.viewComponent = uiSwitch;
                
            } else if ( [attribute.type isEqual:@"DATE"] ){
                
                UITextField *uiTextField = [self generateTextField];
                UIDatePicker *uiDatePicker = [[UIDatePicker alloc] init];
                
                [uiDatePicker setMaximumDate:[NSDate date]];
                [uiDatePicker setDatePickerMode:UIDatePickerModeDate];
                [uiDatePicker addTarget:self action:@selector(datePicked:) forControlEvents:UIControlEventValueChanged];
                uiTextField.inputView = uiDatePicker;
                
                // Adiciona o campo a view dinamica
                [_dynamicFieldsView addSubview:uiTextField];
                
                // Posicionamento
                [self loadTextFieldPositionConstraints:uiTextField positionY:self.positionY];
                self.positionY += fieldHeight;
                
                // Em caso de edicao preenche o valor
                if ( ![markerValue isEqualToString:@""] ){
                    
                    double timestampval;
                    
                    if ( [markerValue length] == 13 ){
                    	timestampval = [markerValue doubleValue]/1000;
                    } else {
                        timestampval = [markerValue doubleValue];
                    }
                
                    NSTimeInterval timestamp = (NSTimeInterval)timestampval;
                    NSDate *updatetimestamp = [NSDate dateWithTimeIntervalSince1970:timestamp];
                    uiTextField.text = [Marker formatDate:updatetimestamp];
                }

                attribute.viewComponent = uiTextField;
            }
        }
        
    } userName:[defaults objectForKey:@"email"] password:[defaults objectForKey:@"password"] layerId:layer.id];
    
    
}

-(UITextField *)generateTextField {
    
    UITextField *uiTextField = [[UITextField alloc] init];
    uiTextField.translatesAutoresizingMaskIntoConstraints = NO;
    uiTextField.borderStyle = UITextBorderStyleRoundedRect;
    uiTextField.contentVerticalAlignment = UIControlContentVerticalAlignmentCenter;
    uiTextField.textAlignment = NSTextAlignmentLeft;
    uiTextField.keyboardType = UIKeyboardTypeDecimalPad;
    uiTextField.clearButtonMode = UITextFieldViewModeAlways;
    uiTextField.returnKeyType = UIReturnKeyDone;
    uiTextField.delegate = self;
    
    return uiTextField;
    
}

-(void)loadTextFieldPositionConstraints:(UITextField *)textField positionY:(int)positionY {
    
    [self loadLeftTopPositionConstraints:textField positionY:positionY];
    
    NSDictionary *viewsDictionary = @{@"uiView":textField};
    NSArray *constraint_c = [NSLayoutConstraint constraintsWithVisualFormat:@"V:[uiView(40)]" options:0 metrics:nil views:viewsDictionary];

    [self.dynamicFieldsView addConstraints:constraint_c];
    [self.dynamicFieldsView addConstraint:[NSLayoutConstraint constraintWithItem:textField attribute:NSLayoutAttributeWidth relatedBy:NSLayoutRelationEqual toItem:self.dynamicFieldsView attribute:NSLayoutAttributeWidth multiplier:1 constant:0.0]];
    
}

-(void)loadLeftTopPositionConstraints:(UIView *)uiView positionY:(int)positionY {
    
    NSDictionary *viewsDictionary = @{@"uiView":uiView};
    NSString * vfl_v = [NSString stringWithFormat: @"V:|-%d-[uiView]", positionY];
    NSArray *constraint_a = [NSLayoutConstraint constraintsWithVisualFormat:vfl_v options:0 metrics:nil views:viewsDictionary];
    
    NSArray *constraint_b = [NSLayoutConstraint constraintsWithVisualFormat:@"H:|-0-[uiView]" options:0 metrics:nil views:viewsDictionary];
    
    [self.dynamicFieldsView addConstraints:constraint_a];
    [self.dynamicFieldsView addConstraints:constraint_b];
    
}

- (void)takeController:(FDTakeController *)controller gotPhoto:(UIImage *)photo withInfo:(NSDictionary *)info {
    
    self.marker.imageUI = photo;
    
    if ( self.imageView != nil )
	    [self.imageView removeFromSuperview];
    
    self.imageView = [[UIImageView alloc] init];
    self.imageView.translatesAutoresizingMaskIntoConstraints = NO;
    [self.imageView setImage:photo];
    [self.dynamicFieldsView addSubview:self.imageView];
    
    // Calcula a proporcao da imagem
    CGFloat width = CGRectGetWidth(self.dynamicFieldsView.bounds);
    float percent = (100 * width) / photo.size.width;
    float height = (photo.size.height * percent) / 100;
    
    // Define o posicionamento
    [self loadLeftTopPositionConstraints:self.imageView positionY:self.positionY+10];
    
    NSDictionary *viewsDictionary = @{@"uiView":self.imageView};
    
    NSString * heightVFL = [NSString stringWithFormat: @"V:[uiView(%f)]", height];
    NSArray *constraint_c = [NSLayoutConstraint constraintsWithVisualFormat:heightVFL options:0 metrics:nil views:viewsDictionary];
    [self.dynamicFieldsView addConstraints:constraint_c];
    
    NSArray *constraintBottom = [NSLayoutConstraint constraintsWithVisualFormat:@"V:[uiView]-10-|" options:0 metrics:nil views:viewsDictionary];
    [self.dynamicFieldsView addConstraints:constraintBottom];
    
    [self.dynamicFieldsView addConstraint:[NSLayoutConstraint constraintWithItem:self.imageView attribute:NSLayoutAttributeWidth relatedBy:NSLayoutRelationEqual toItem:self.dynamicFieldsView attribute:NSLayoutAttributeWidth multiplier:1 constant:0.0]];
    
}

- (void)datePicked:(UIDatePicker *)picker {
    
    self.activeTextField.text = [Marker formatDate:picker.date];
    
}

- (void)textFieldDidBeginEditing:(UITextField *)textField {
    CGPoint scrollPoint = CGPointMake(0, textField.frame.origin.y);
    [self.scrollView setContentOffset:scrollPoint animated:YES];
    self.activeTextField = textField;
}

- (void)textFieldDidEndEditing:(UITextField *)textField {
    [self.scrollView setContentOffset:CGPointZero animated:YES];
}

-(void)didFinish {
	[[self navigationController] popViewControllerAnimated: YES];
}


@end