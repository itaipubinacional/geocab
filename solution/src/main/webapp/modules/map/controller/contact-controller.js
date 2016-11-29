'use strict';

/**
 * 
 * @param $scope
 * @param $log
 * @param $location
 */
function ContactController($scope, $injector, $log, $state, $timeout, $modal,
		$location, $importService, $translate) {

	/**
	 * Inject methods, attributes and states inherited of the
	 * AbstractCRUDController
	 * 
	 * @see AbstractCRUDController
	 */
	$injector.invoke(AbstractCRUDController, this, {
		$scope : $scope
	});

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
	
	$scope.initialize = function(toState, toParams, fromState, fromParams) {

		/**
		 * authenticated user
		 */
		contactService.getLoggedUser({
			callback : function(result) {
				if(result){
					$scope.user = result;
					$scope.contactForm.email = result.email;
					$scope.contactForm.name = result.name;	
				}
				
				$scope.$apply();
			},
			errorHandler : function(message, exception) {
				$scope.message = {
					type : "error",
					text : message
				};
				$scope.$apply();
			}
		});

	};

	/**
	 * 
	 */
	$scope.sendForm = function() {

		if (!$scope.form.$valid) {
			$scope.msg = {
				type : "danger",
				text : $scope.INVALID_FORM_MESSAGE,
				dismiss : true
			};
			$scope.fadeMsg();
		} else {

			contactService.contactUs($scope.contactForm, {
				callback : function(result) {
					$scope.msg = {
						type : "success",
						text : 'Sua mensagem foi enviada com sucesso!',
						dismiss : true
					};
					$scope.fadeMsg();
					$scope.$apply();
				},
				errorHandler : function(message, exception) {
					$scope.msg = {
						type : "danger",
						text : message,
						dismiss : true
					};
					$scope.fadeMsg();
					$scope.$apply();
				}
			});

		}

	};

	$scope.fadeMsg = function(){
		$("div.msg").show();
	}


};
