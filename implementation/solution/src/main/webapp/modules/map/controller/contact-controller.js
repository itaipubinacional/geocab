'use strict';

/**
 * 
 * @param $scope
 * @param $log
 * @param $location
 */
function ContactController( $scope, $injector, $log, $state, $timeout, $modal, $location, $importService , $translate) {


	/**
	 * Inject methods, attributes and states inherited of the AbstractCRUDController
	 * @see AbstractCRUDController
	 */
	$injector.invoke(AbstractCRUDController, this, {$scope: $scope});
	
	/**
	 * Include contactService class
	 */
	$importService("contactService");
	
	
	/*-------------------------------------------------------------------
	 * 		 				 	EVENT HANDLERS
	 *-------------------------------------------------------------------*/

	
	/*-------------------------------------------------------------------
	 * 		 				 	ATTRIBUTES
	 *-------------------------------------------------------------------*/

	$scope.contactForm = {};



	/**
	 *
	 */
	$scope.sendForm = function(){

		if ( !$scope.form.$valid ) {
			$scope.msg = {type:"danger", text: $scope.INVALID_FORM_MESSAGE, dismiss:true};
			$scope.fadeMsg();
		}else{
			console.log("TESTES");

			contactService.contactUs( $scope.contactForm, {
				callback : function(result) {

					console.log(result);
					$scope.$apply();
				},
				errorHandler : function(message, exception) {
					$scope.message = {type:"error", text: message};
					$scope.$apply();
				}
			});

		}

	};


	$scope.initialize = function( toState, toParams, fromState, fromParams ) {
		
		 /**
		 * authenticated user
		 * */
		contactService.getLoggedUser({
    		callback : function(result) {
    			//$scope.contactForm = result;

					$scope.contactForm.email = 'admin@email.com';
					$scope.contactForm.name = 'Subject';
					$scope.contactForm.subject = 'Subject';
					$scope.contactForm.message = 'Message';
					$scope.contactForm.isBot = false;

    			$scope.$apply();
            },
            errorHandler : function(message, exception) {
                $scope.message = {type:"error", text: message};
                $scope.$apply();
            }
    	});

	};

	$scope.fadeMsg = function(){
		$("div.msg").show();
		  
		setTimeout(function(){
		  	$("div.msg").fadeOut();
		 }, 3000);
	}

	
};
