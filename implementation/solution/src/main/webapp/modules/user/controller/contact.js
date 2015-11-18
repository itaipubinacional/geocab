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
	 * Include accountService class
	 */
	$importService("accountService");
	
	
	/*-------------------------------------------------------------------
	 * 		 				 	EVENT HANDLERS
	 *-------------------------------------------------------------------*/

	
	/*-------------------------------------------------------------------
	 * 		 				 	ATTRIBUTES
	 *-------------------------------------------------------------------*/
	
	
	//STATES

	
	/**
	 * Store current User entity for update
	 */
	$scope.contactForm = {};
	
	
	/*-------------------------------------------------------------------
	 * 		 				 	  NAVIGATIONS
	 *-------------------------------------------------------------------*/
	/**
	 *
	 */
	$scope.sendForm = function(){
		console.log("");
	};


	$scope.initialize = function( toState, toParams, fromState, fromParams ) {
		var state = $state.current.name;
		
		 /**
		 * authenticated user
		 * */
		accountService.getUserAuthenticated({
    		callback : function(result) {
    			$scope.currentEntity = result;
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
