'use strict';

/**
 * 
 * @param $scope
 * @param $log
 * @param $location
 */
function MyAccountController( $scope, $injector, $log, $state, $timeout, $modal, $location, $importService , $translate) {
	
	
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
	
	/**
	 *  Handler that listens every time the user / programmatically makes sorting in grid-ng.
	 *  When the event is triggered, we set the pager's spring-data
	 *  and call the query again, also considering the state of the filter (@see $scope.data.filter)
	 */
	$scope.$on('ngGridEventSorted', function(event, sort) {

		//run only once
		if ( !angular.equals(sort, $scope.gridOptions.sortInfo) ) {
			$scope.gridOptions.sortInfo = angular.copy(sort);

				
			//Order do spring-data
			var order = new Order();
			order.direction = sort.directions[0].toUpperCase();
			order.property = sort.fields[0];

			//Sort do spring-data
			$scope.currentPage.pageable.sort = new Sort();
			$scope.currentPage.pageable.sort.orders = [ order ];

			$scope.listUsersByFilters( $scope.data.filter, $scope.currentPage.pageable );
		}
	});

	
	/*-------------------------------------------------------------------
	 * 		 				 	ATTRIBUTES
	 *-------------------------------------------------------------------*/
	
	
	//STATES
	
	$scope.UPDATE_STATE = "my-account.form";
	
	/**
	 * This variable store the current view state
	 * This variable should ALWAYS be in agreement with the URL in browser.
	 */
	$scope.currentState;
	
	/**
	 * Store the state of paging
	 * @type PageRequest
	 */
	$scope.currentPage;

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
		
		
		 /**
		 * authenticated user
		 * */
		accountService.getUserAuthenticated({
    		callback : function(result) {
    			$scope.currentEntity = result;
            },
            errorHandler : function(message, exception) {
                $scope.message = {type:"error", text: message};
                $scope.$apply();
            }
    	});
        
		
		$log.info('Starting the front controller. Users');

		switch (state) {
			case $scope.UPDATE_STATE: {
				$scope.changeToUpdate( $state.params.id );
			}
			default : {
				$state.go( $scope.UPDATE_STATE );
			}
		}
		
	};
	
	/**
	 * Boot the state users.update 
	 */
	$scope.changeToUpdate = function( id ) {
		$log.info("changeToDetail", id);
		
		$scope.currentState = $scope.UPDATE_STATE;
	};
	
	$scope.updateUser = function() {
		
		if($scope.currentEntity.newPassword != $scope.currentEntity.repeatNewPassword) {
			$scope.msg = {type:"danger", text: "As senhas não coincidem" + '!', dismiss:true};
			return false;
		}
		delete $scope.currentEntity.repeatNewPassword;
		
		accountService.updateUserAuthenticated($scope.currentEntity, {
    		callback : function(result) {
    			$scope.currentEntity = result;
    			$scope.msg = {type:"success", text: "Informações atualizadas com sucesso" + '!', dismiss:true};
    			$scope.$apply();
            },
            errorHandler : function(message, exception) {
                $scope.message = {type:"error", text: message};
                $scope.$apply();
            }
    	});
		
	}
	

};
