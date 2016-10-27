'use strict';

function CreateUserPopUpController( $scope, $modalInstance, $state, $importService, $translate ) {

	$importService("loginService");
	
    $scope.msg = null;

    /*-------------------------------------------------------------------
     * 		 				 	ATTRIBUTES
     *-------------------------------------------------------------------*/
    $scope.currentEntity = new User();
    $scope.confirmPassword = null;

    /*-------------------------------------------------------------------
     * 		 				 	  NAVIGATIONS
     *-------------------------------------------------------------------*/
    $scope.initialize = function(){
    	
    };

    /*-------------------------------------------------------------------
     * 		 				 	  BEHAVIORS
     *-------------------------------------------------------------------*/
    $scope.createAccount = function(){
    
    	if ( !$scope.form('form_create_account').$valid ){
			$scope.msg = {type:"danger", text: $translate('admin.users.The-highlighted-fields-are-required') , dismiss:true};
			return;
		} else if ($scope.currentEntity.password != $scope.currentEntity.confirmPassword ){
			$scope.msg = null;
			return
		}

    	loginService.insertUser( $scope.currentEntity, {
			callback : function() {
				$scope.msg = {type:"success", text: $translate('admin.users.User-successfully-inserted') + '!', dismiss:true};
                $("form[name='form_login']").find("[name='email']").val($scope.currentEntity.email);
                $("form[name='form_login']").find("[name='password']").val($scope.currentEntity.password);
                $("form[name='form_login']").submit();
			},
			errorHandler : function(message, exception) {
				$scope.msg = {type:"danger", text: message, dismiss:true};
				$scope.$apply();
			}
		});
    }

    $scope.form = function( formName )
    {
        if ( !formName )
            formName = "form";

       return $("form[name="+formName+"]").scope()[formName];
    };

    $scope.close = function( fechar ){
    	$scope.msg = null;
    };
   
    $scope.closePopUp = function(){
    	$state.go("authentication");
    	$modalInstance.close()
    }
};