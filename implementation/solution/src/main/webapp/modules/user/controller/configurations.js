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
	
	$scope.GENERAL_CONFIGURATION_STATE = "general-configuration-state";

	/**
	 * This variable store the current view state
	 * This variable should ALWAYS be in agreement with the URL in browser.
	 */
	$scope.currentState = $scope.UPDATE_STATE;

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
    			$scope.userLogged = result;
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

		$scope.flag = 0;
	};

	$scope.setType = function(type, status) {
		if(status) {
			if (type == 'GOOGLE_SATELLITE_LABELS') {
				$scope.currentEntity.backgroundMap = type;
			} else {
				$scope.currentEntity.backgroundMap = type;
			}
		} else {
			$scope.currentEntity.backgroundMap = $scope.backgroundMap.subType;
		}
	};

	$scope.setBackgroundMap = function(backgroundMap){

    $scope.currentEntity.backgroundMap = backgroundMap;

		if(backgroundMap.match(/GOOGLE/i))
			$scope.backgroundMap.map = 'GOOGLE';

		if(backgroundMap.match(/MAP_QUEST/i))
			$scope.backgroundMap.map = 'MAP_QUEST';

		if(backgroundMap.match(/OPEN_STREET_MAP/i))
			$scope.backgroundMap.map = 'OPEN_STREET_MAP';

		if(backgroundMap.match(/MAP_QUEST|MAP_QUEST_OSM/i) && backgroundMap != 'MAP_QUEST_SAT') {
			$scope.currentEntity.backgroundMap = 'MAP_QUEST_OSM';
			$scope.backgroundMap.subType = 'MAP_QUEST_OSM';
		}

		if(backgroundMap.match(/MAP_QUEST_SAT/i))
			$scope.backgroundMap.subType = 'MAP_QUEST_SAT';

		if(backgroundMap == 'GOOGLE_MAP' && backgroundMap != 'GOOGLE_SATELLITE') {
      $scope.backgroundMap.type.GOOGLE_SATELLITE_LABELS = false;
      $scope.backgroundMap.subType = 'GOOGLE_MAP';
    }

		if(backgroundMap == 'GOOGLE_SATELLITE') {
      $scope.backgroundMap.type.GOOGLE_MAP_TERRAIN = false;
      $scope.backgroundMap.subType = 'GOOGLE_SATELLITE';
    }

		if(backgroundMap == 'GOOGLE_MAP_TERRAIN') {
      $scope.backgroundMap.subType = 'GOOGLE_MAP';
      $scope.backgroundMap.type.GOOGLE_MAP_TERRAIN = true;
    }

		if(backgroundMap == 'GOOGLE_SATELLITE_LABELS') {
      $scope.backgroundMap.subType = 'GOOGLE_SATELLITE';
      $scope.backgroundMap.type.GOOGLE_SATELLITE_LABELS = true;
    }

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

	$scope.changeState = function(state){
		$scope.currentState = state;
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
