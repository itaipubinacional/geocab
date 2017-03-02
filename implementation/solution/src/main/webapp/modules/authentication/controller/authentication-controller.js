'use strict';

function AuthenticationController( $scope, $injector, $log, $state, $timeout, $modal, $location, $importService , $translate) {

    /**
     * Include loginService class
     */
    $importService('loginService');

    /*-------------------------------------------------------------------
     * 		 				 	ATTRIBUTES
     *-------------------------------------------------------------------*/
    $scope.currentEntity = {};
    $scope.msg = null ;

    //if return error msg
    if($('#msg').val()){

        var msg = $('#msg').val();

        if( msg == 'User is disabled' ) msg = $translate('authentication.User-is-disabled');
        if( msg == 'Bad credentials' ) msg = $translate('authentication.Bad-credentials');

        $scope.msg = {type:"danger", text: msg, dismiss:true};
    }


    $scope.changeToInsert = function() {
        $log.info('changeToInsert');
        
        var dialog = $modal.open({
            templateUrl: 'modules/authentication/ui/authentication/popup/create-user-popup.jsp',
            controller: CreateUserPopUpController,
            windowClass: 'create-account',
            resolve: {
            }
        });
    };

    $scope.changeToForgetPassword = function(  ) {
        $log.info('changeToForgetPassword');

        var dialog = $modal.open({
            templateUrl: 'modules/authentication/ui/authentication/popup/forget-password-popup.jsp',
            controller:  ForgetPasswordPopUpController,
            windowClass: 'forget-password-modal',
            resolve: {
            }
        });
    };

    /*-------------------------------------------------------------------
     * 		 				 	  BEHAVIORS
     *-------------------------------------------------------------------*/
    $scope.login = function(){
        if ( !$scope.form_login.$valid )
           return;

        $('form[name="form_login"]').submit();
    }

    $scope.close = function( fechar ){
        $scope.msg = null;
    };

    $("#email").keyup(function(event){
        if(event.keyCode == 13){
            $("#enter").click();
        }
    });
    
    $("#password").keyup(function(event){
        if(event.keyCode == 13){
            $("#enter").click();
        }
    });
    
    if ( $('input[name=password]').val() != '' ){
    	$scope.form_login.setValid(true)
    }

};
