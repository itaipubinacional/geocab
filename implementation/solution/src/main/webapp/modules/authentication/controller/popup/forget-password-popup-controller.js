'use strict';

/**
 *
 * @param $scope
 * @param $modalInstance
 * @constructor
 */
function ForgetPasswordPopUpController( $scope, $modalInstance, $state, $importService, $translate, $timeout ) {


	$importService("loginService");
	
    $scope.msg = null;

    /*-------------------------------------------------------------------
     * 		 				 	ATTRIBUTES
     *-------------------------------------------------------------------*/

    /**
     *
     */
    $scope.currentEntity;
    
    $scope.confirmPassword;
    

    /*-------------------------------------------------------------------
     * 		 				 	  NAVIGATIONS
     *-------------------------------------------------------------------*/
    /**
     * Main method that makes the role of front-controller of the screen.
     * He is invoked whenever there is a change of URL (@see $stateChangeSuccess),
     * When this occurs, gets the State via the $state and calls the initial method of that State.
     *
     * If the State is not found, he directs to the listing,
     * Although the front controller of Angular won't let enter an invalid URL.
     */
    $scope.initialize = function()
    {
    	$scope.currentEntity = new User();
    	$scope.confirmPassword=null;
    };

    /*-------------------------------------------------------------------
     * 		 				 	  BEHAVIORS
     *-------------------------------------------------------------------*/
   
    $scope.forgetPassword = function(){
    	if ( !$scope.form('form_forget_password').$valid ) 
    	{    		    		    	
			$scope.msg = {type:"danger", text: $translate('admin.users.The-highlighted-fields-are-required') , dismiss:true};
			$scope.$apply();
			return;
		} 

    	$scope.msg = {type:"info", text: "Aguarde um momento" + '...', dismiss:true};
    	
    	loginService.recoverPassword($scope.currentEntity, {
            callback: function (result) {
            	$scope.msg = {type:"success", text: "Uma nova senha foi enviada ao e-mail" + '!', dismiss:true};
            	$scope.$apply();
            	$timeout(function(){
            		$scope.fechaPopup();
            	}, 5000);
            },
            errorHandler: function (message, exception) {
                $scope.msg = {type: "danger", text: message, dismiss: true};
                $scope.$apply();
            }
        });
    	
    }

    /**
     * Close popup
     */
    $scope.fechaPopup = function ()
    {
        $modalInstance.close($scope.currentEntity);
    };

    $scope.form = function( formName )
    {
    	
        if ( !formName )
        {
            formName = "form";
        }

       return $("form[name="+formName+"]").scope()[formName];
    };
    
    /**
     *
     */
    $scope.close = function( fechar )
    {

    	$scope.msg = null;
        

    };
   
    $scope.closePopUp = function(){
    	$state.go("authentication");
    	$modalInstance.close();
    }
    
};