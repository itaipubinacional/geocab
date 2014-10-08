'use strict';

	/**
	 * 
	 * @param $scope
	 * @param $log
	 * @param $location
	 */
	function UserController( $scope, $injector, $log, $state, $timeout, $modal, $location, $importService , $translate) {
		
		/**
		 * Inject methods, attributes and states inherited of the AbstractCRUDController 
		 * @see AbstractCRUDController
		 */
		$injector.invoke(AbstractCRUDController, this, {$scope: $scope});
		
		/**
		 * Include accountService class
		 */
		$importService("loginService");
		
		/*-------------------------------------------------------------------
		 * 		 				 	ATTRIBUTES
		 *-------------------------------------------------------------------*/
		
		
		//STATES
		/**
		 * Static variable
		 * View insert a new User
		 */
		$scope.INSERT_STATE = "authentication.create";
		/**
		 * Static variable
		 * View edit a User
		 */
		$scope.UPDATE_STATE = "authentication.update";
		
		/**
		 * This variable store the current view state
		 * This variable should ALWAYS be in agreement with the URL in browser.
		 */
		$scope.currentState;

		/**
		 * Store auxiliary information
		 * @filter - Search filter
		 */
		$scope.data = { filter:null };
		
		
		/**
		 * Store current User entity for update
		 */
		$scope.currentEntity = {};
		
		
		/*-------------------------------------------------------------------
		 * 		 				 	  NAVIGATIONS
		 *-------------------------------------------------------------------*/
		/**
		 * Main method that makes the role of front-controller screen.
		 * It is invoked whenever there is a change of URL (@see $stateChangeSuccess),
		 * Ex.: /list -> changeToList()
		 *      /criar -> changeToInsert()
		 *      
		 * If the state is not found, it directs you to the list
		 */
		$scope.initialize = function( toState, toParams, fromState, fromParams ) {
			var state = $state.current.name;
			
			$log.info('Starting the front controller. Authentication');

			switch (state) {
				case $scope.INSERT_STATE: {
					$scope.changeToInsert();
				}
				break;
				case $scope.UPDATE_STATE: {
					$scope.changeToUpdate( $state.params.id );
				}
				break;
				default : {
					$state.go( $scope.LIST_STATE );
				}
			}
		};
		
		/**
		 * Boot the state users.insert 
		 */
		$scope.changeToInsert = function() {
			$log.info("changeToInsert");
			
			var dialog = $modal.open({
                templateUrl: "modules/authentication/ui/user/popup/create-user-popup.jsp",
                controller: CreateUserPopUpController,
                windowClass: 'create-account',
                resolve: {
                }
            });

            dialog.result.then(function (result) {
            	$state.go("authentication");
            });
            
			
			$scope.currentState = $scope.INSERT_STATE;
		};

		
		
		/**
		 * Boot the state users.update 
		 */
		$scope.changeToUpdate = function(  ) {
//			$log.info("changeToUpdate", id);
//
//			accountService.findUserById( $state.params.id, {
//				callback : function(result) {
//					
//					$scope.currentEntity = result;
//					$scope.currentEntity.password = ''; //clear password. if not set, not update.
//					
//					$scope.currentState = $scope.UPDATE_STATE;
//					$state.go($scope.UPDATE_STATE);
//					$scope.$apply();
//				},
//				errorHandler : function(message, exception) {
//					$scope.msg = {type:"danger", text: message, dismiss:true};
//					$scope.$apply();
//				}
//			}); 
			
			
				$log.info("changeToUpdate");
				
				var dialog = $modal.open({
	                templateUrl: "modules/authentication/ui/user/popup/forget-password-popup.jsp",
	                controller:  ForgetPasswordPopUpController,
	                windowClass: 'forget-password-modal',
	                resolve: {
	                }
	            });

	            dialog.result.then(function (result) {
	            	$state.go("authentication");
	            });
	            
				
				$scope.currentState = $scope.UPDATE_STATE;
			
			
		};

		/*-------------------------------------------------------------------
		 * 		 				 	  BEHAVIORS
		 *-------------------------------------------------------------------*/
		
		$scope.createAccount = function( user ) {
			 
			if ( !$scope.form().$valid ) {
				$scope.msg = {type:"danger", text: $scope.INVALID_FORM_MESSAGE, dismiss:true};
				return;
			}

			accountService.insertUser( user, {
				callback : function() {
					$scope.currentState = $scope.LIST_STATE;
					$state.go($scope.LIST_STATE);
					$scope.msg = {type:"success", text: $translate('admin.users.User-successfully-inserted') + '!', dismiss:true};
					$scope.$apply();
				},
				errorHandler : function(message, exception) {
					$scope.msg = {type:"danger", text: message, dismiss:true};
					$scope.$apply();
				}
			});
		}
		
		
//		$scope.form = function( formName )
//	    {
//	    	console.log(formName);
//	        if ( !formName )
//	        {
//	            formName = "form";
//	        }
//
//	       return $("form[name="+formName+"]").scope()[formName];
//	    };
		

		
		$scope.login = function(){
			if ( !$scope.form('form_login').$valid ) {
				$scope.msg = {type:"danger", text: $scope.INVALID_FORM_MESSAGE, dismiss:true};
				return;
			}
			
			$("form[name='form_login']").submit();
			
		}
		
		
		
		 		
		
	};
