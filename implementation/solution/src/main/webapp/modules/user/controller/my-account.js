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

	$scope.backgroundMap = [];

	$scope.backgroundMap.type = [];

	$scope.backgroundMap.type.GOOGLE_MAP_TERRAIN = false;
	$scope.backgroundMap.type.GOOGLE_SATELLITE_LABELS = false;
	
	/*-------------------------------------------------------------------
	 * 		 				 	  NAVIGATIONS
	 *-------------------------------------------------------------------*/
	/**
	 * Main method that makes the role of front-controller screen.
	 * It is invoked whenever there is a change of URL (@see $stateChangeSuccess),
	 * Ex.: /list -> changeToList()
	 *      /create -> changeToInsert()
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

					$scope.setBackgroundMap(result.backgroundMap);

    			$scope.$apply();
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
		
		$scope.flag = 0;
	};

	$scope.setType = function(newVal){
		angular.forEach(Object.keys(newVal), function(type, index){
			if(newVal[type]) {
				$scope.currentEntity.backgroundMap = type;
				$scope.setBackgroundMap(type);
			}
		});
	};

	$scope.$watch('backgroundMap.subType', function(newVal, oldVal){

		$scope.backgroundMap.type.GOOGLE_MAP_TERRAIN = false;
		$scope.backgroundMap.type.GOOGLE_SATELLITE_LABELS = false;

		if(newVal != oldVal) {
			$scope.currentEntity.backgroundMap = newVal;
			$scope.setBackgroundMap(newVal);
		}
	});

	$scope.$watch('backgroundMap.map', function(newVal, oldVal){
		if(newVal != oldVal) {
			$scope.currentEntity.backgroundMap = newVal;
			$scope.setBackgroundMap(newVal);
		}
	});

	$scope.setBackgroundMap = function(backgroundMap){

		if(backgroundMap.match(/GOOGLE/i))
			$scope.backgroundMap.map = 'GOOGLE';

		if(backgroundMap.match(/MAP_QUEST/i))
			$scope.backgroundMap.map = 'MAP_QUEST';

		if(backgroundMap.match(/MAP_QUEST|MAP_QUEST_OSM/i) && backgroundMap != 'MAP_QUEST_SAT') {
			$scope.currentEntity.backgroundMap = 'MAP_QUEST_OSM';
			$scope.backgroundMap.subType = 'MAP_QUEST_OSM';
		}

		if(backgroundMap.match(/MAP_QUEST_SAT/i))
			$scope.backgroundMap.subType = 'MAP_QUEST_SAT';

		if(backgroundMap.match(/GOOGLE|GOOGLE_MAP/i))
			$scope.backgroundMap.subType = 'GOOGLE_MAP';

		if(backgroundMap.match(/GOOGLE_SATELLITE/i))
			$scope.backgroundMap.subType = 'GOOGLE_SATELLITE';

		if(backgroundMap == 'GOOGLE_MAP_TERRAIN')
			$scope.backgroundMap.typeTerrain = true;

		if(backgroundMap == 'GOOGLE_SATELLITE_LABELS')
			$scope.backgroundMap.typeLabels = true;

		console.log($scope.currentEntity.backgroundMap);

	};
	
	/**
	 * Boot the state users.update 
	 */
	$scope.changeToUpdate = function( id ) {
		$log.info("changeToDetail", id);
		
		$scope.currentState = $scope.UPDATE_STATE;
	};

	$scope.changeForm = function(form){
		$scope.currentState = form;
	};
	
	$scope.updateUser = function() {
		
		if(!$scope.form('form').$valid){
			$scope.msg = {type:"danger", text: $translate("admin.users.The-highlighted-fields-are-required") , dismiss:true};
			$scope.fadeMsg();
			return;
		}

		if($scope.currentEntity.newPassword == ""){
			$scope.currentEntity.newPassword = null;
		}
		
//		if($scope.currentEntity.newPassword != $scope.currentEntity.repeatNewPassword) {
//			$scope.msg = {type:"danger", text: "As senhas não coincidem" + '!', dismiss:true};
//			return false;
//		}
		//delete $scope.currentEntity.repeatNewPassword;
		
		accountService.updateUserAuthenticated($scope.currentEntity, {
    		callback : function(result) {
    			result.password = null;
    			$scope.currentEntity = result;
    			$scope.msg = {type:"success", text: $translate("admin.user.Successfully-updated-informations") + '!', dismiss:true};
    			$scope.fadeMsg();
    			$scope.$apply();
            },
            errorHandler : function(message, exception) {
                $scope.msg = {type:"danger", text: message};
                $scope.$apply();
            }
    	});
		
	}
	
	$scope.fadeMsg = function(){
		$("div.msg").show();
		  
		setTimeout(function(){
		  	$("div.msg").fadeOut();
		 }, 3000);
	}
	
	$scope.passwordRequired = function(){
		if( $('#newPassword').val() != '' ){
			return true;
		}
	}
	
	
	$('#buttonUpdate').click(function(){
		$scope.flag = 1;
	}) 
			
	$(document).click(function() {
		if($scope.flag == 1){
			$scope.flag = 0;
		} else {
			$("div.msg").css("display","none");
			$scope.sim = 1;
		}
	});
	
};
