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

    /*-------------------------------------------------------------------
     * 		 				 	  NAVIGATIONS
     *-------------------------------------------------------------------*/
    $scope.initialize = function( toState, toParams, fromState, fromParams ) {
        $log.info('Starting the front controller. Authentication');
    };

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
        if ( !$scope.form_login.$valid ) {
            $scope.msg = {type:'danger', text: $scope.INVALID_FORM_MESSAGE, dismiss:true};
            return;
        }

        $('form[name="form_login"]').submit();
    }

};
