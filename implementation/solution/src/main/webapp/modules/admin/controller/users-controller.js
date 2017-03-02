'use strict';

/**
 * 
 * @param $scope
 * @param $log
 * @param $location
 */
function UsersController( $scope, $injector, $log, $state, $timeout, $modal, $location, $importService , $translate) {
	
	
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
	/**
	 * Static variable
	 * View list of Users
	 */
	$scope.LIST_STATE = "users.list";
	/**
	 * Static variable
	 * View details of User
	 */
	$scope.DETAIL_STATE = "users.detail";
	/**
	 * Static variable
	 * View insert a new User
	 */
	$scope.INSERT_STATE = "users.create";
	/**
	 * Static variable
	 * View edit a User
	 */
	$scope.UPDATE_STATE = "users.update";
	
	/**
	 * This variable store the current view state
	 * This variable should ALWAYS be in agreement with the URL in browser.
	 */
	$scope.currentState;

	//DATA GRID
	/**
	 * Static variable coms buttons shares of grid
	 * The edit button navigates via URL (SREF) why the editing is done on another page
	 * now the delete button a direct method calls via click-ng why not have a specific status screen.
	 */
    var GRID_ACTION_BUTTONS = '<div class="cell-centered">' +
	'<a ui-sref="users.update({id:row.entity.id})" title="'+$translate("admin.users.Edit")+'" class="btn btn-mini"><i class="itaipu-icon-edit"></i></a>'+
	'<a ng-show="row.entity.enabled == true" ng-click="changeToDisable(row.entity)" title="'+$translate("admin.users.Disable")+'" style="color:red" class="btn btn-mini glyphicon glyphicon-ban-circle"></a>'+
	'<a ng-show="row.entity.enabled == false" ng-click="changeToEnable(row.entity)" title="'+$translate("admin.users.Enable")+'"  style="color:green" class="btn btn-mini glyphicon glyphicon-ok"></a>'+
	'</div>';
   
    var GRID_STATUS_FORMATER = ' <div class="ngCellText " ng-class="col.colIndex()"><span class="ng-binding" ng-cell-text="">'+
    						   '{{row.entity.enabled == true && "'+$translate("admin.users.Active")+'" || "'+$translate("admin.users.Inactive")+'"}}</span></div>';
    
    var GRID_ACCESS_PROFILE_FORMATER = ' <div class="ngCellText " ng-class="col.colIndex()"><span class="ng-binding" ng-cell-text="">'+
    						   '{{row.entity.role == "ADMINISTRATOR" && "'+$translate("admin.users.Administrator")+'" || "" }}'+
    						   '{{row.entity.role == "USER" && "'+$translate("admin.users.User")+'" || ""}}'+
    						   '{{row.entity.role == "MODERATOR" && "'+$translate("admin.users.Moderator")+'" || ""}}'+
    						   '</span></div>';
    
	/**
	 * ng-grid configurations. 
	 * @see https://github.com/angular-ui/ng-grid/wiki/Configuration-Options
	 */
	$scope.gridOptions = { 
			data: 'currentPage.content',
			multiSelect: false,
			useExternalSorting: true,
            headerRowHeight: 45,
            rowHeight: 45,
			beforeSelectionChange: function (row, event) {
				//don't select line if a button was clicked
				if ( $(event.target).is("a") || $(event.target).is("i") ) return false;
				$state.go($scope.DETAIL_STATE, {id:row.entity.id});
			},
			columnDefs: [
			             {displayName:$translate("admin.users.Name"), field:'name'},
			             {displayName:$translate("admin.users.E-mail"), field:'email'},
			             {displayName:$translate("admin.users.Access-profile"), field:'role' , width: '200px', cellTemplate: GRID_ACCESS_PROFILE_FORMATER},
			             {displayName:$translate("admin.users.Status"), field:'enabled' , width: '100px' , cellTemplate: GRID_STATUS_FORMATER },
			             {displayName:$translate("admin.users.Actions"), sortable:false, cellTemplate: GRID_ACTION_BUTTONS, width:'100px'}
			             ]
	};

	
	
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
	 *      /create -> changeToInsert()
	 *      
	 * If the state is not found, it directs you to the list
	 */
	$scope.initialize = function( toState, toParams, fromState, fromParams ) {
		var state = $state.current.name;
		
		$log.info('Starting the front controller. Users');

		switch (state) {
			case $scope.LIST_STATE: {
				$scope.changeToList();
			}
			break;
			case $scope.DETAIL_STATE: {
				$scope.changeToDetail( $state.params.id );
			}
			break;
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
	 * Boot the state users.list
	 */
	$scope.changeToList = function() {
		$log.info("changeToList");
	
		var pageRequest = new PageRequest();
		pageRequest.size = 10;
		$scope.pageRequest = pageRequest;
		$scope.listUsersByFilters( null, pageRequest );
	};
	
	/**
	 * Boot the state users.insert 
	 */
	$scope.changeToInsert = function() {
		
		$log.info("changeToInsert");

		//Clear current entity
		$scope.currentEntity = {};
		
		
		//Default role value
		$scope.currentEntity.role = 'ADMINISTRATOR';
		
		$scope.currentState = $scope.INSERT_STATE;
		
		
	};
	
	/**
	 * Boot the state users.detail 
	 */
	$scope.changeToDetail = function( id ) {
		$log.info("changeToDetail", id);

		if ( id == null || id == "" || id == 0 ) {
			$scope.msg = {type:"error", text: $scope.INVALID_ID_MESSAGE, dismiss:true};
			$scope.currentState = $scope.LIST_STATE;
			$state.go($scope.LIST_STATE);
			return;
		}
		
		accountService.findUserById( id, {
			callback : function(result) {
				$scope.currentEntity = result;
				$log.info("view user: ", result);
				$scope.currentState = $scope.DETAIL_STATE;
				$state.go($scope.DETAIL_STATE, {id:id});
				$scope.$apply();
			},
			errorHandler : function(message, exception) {
				$scope.msg = {type:"danger", text: message, dismiss:true};
				$scope.$apply();
			}
		});
	};
	
	
	
	/**
	 * Open the confirmation dialog to disable user
	 * if confirm redirect to users.list
	 */
	$scope.changeToDisable = function( user ) {
		$log.info("changeToDisable", user);
	
		var dialog = $modal.open( {
			templateUrl: "static/libs/eits-directives/dialog/dialog-template.html",
			controller: DialogController,
			windowClass: 'dialog-disable',
			resolve: {
				title: function(){return $translate("admin.users.Disable-user");},
				message: function(){return $translate("admin.users.Are-you-sure-you-want-to-disable-the-user") + ' "'+user.name+'"? ';},
				buttons: function(){return [ {label: $translate("admin.users.Disable") , css:'btn btn-warning'}, {label: $translate("admin.users.Cancel"), css:'btn btn-default', dismiss:true} ];}
			}
		});

        dialog.result.then( function(result) {

        	accountService.disableUser( user.id, {
				callback : function(result) {
					//if currentPage is null, configure the pager default
					if ( $scope.currentPage == null ) {
						$scope.changeToList();
						//else, use the same estate to load the listing
					} else {
						$scope.listUsersByFilters($scope.data.filter, $scope.currentPage.pageable);
					}

					$scope.msg = {type: "success", text: $translate('admin.users.The-record-was-successfully-inactivated')+'.', dismiss:true};
					
					$scope.fadeMsg();
										
				},
				errorHandler : function(message, exception) {
					$scope.msg = {type:"danger", text: message, dismiss:true};
					$scope.fadeMsg();
					$scope.$apply();					
				}
			});
		});
	};
	
	/**
	 * Open the confirmation dialog to disable user
	 * if confirm redirect to users.list
	 */
	$scope.changeToEnable = function( user ) {
		$log.info("changeToEnable", user);

		var dialog = $modal.open( {
			templateUrl: "static/libs/eits-directives/dialog/dialog-template.html",
			controller: DialogController,
			windowClass: 'dialog-enable',
			resolve: {
				title: function(){return $translate('admin.users.Enable-user');},
				message: function(){return $translate('admin.users.Are-you-sure-you-want-to-enable-the-user') + ' "'+user.name+'"? ';},
				buttons: function(){return [ {label:$translate("admin.users.Enable"), css:'btn btn-primary'}, {label: $translate("admin.users.Cancel"), css:'btn btn-default', dismiss:true} ];}
			}
		});

        dialog.result.then( function(result) {

        	accountService.enableUser( user.id, {
				callback : function(result) {
					if ( $scope.currentPage == null ) { //if current page is null, set default pager
						$scope.changeToList();
					} else {//if not, keep the current pager
						$scope.listUsersByFilters($scope.data.filter, $scope.currentPage.pageable);
					}

					$scope.msg = {type: "success", text: $translate('admin.users.The-record-was-successfully-enabled') + '.', dismiss:true};
				
					$scope.fadeMsg();
				},
				errorHandler : function(message, exception) {
					$scope.msg = {type:"danger", text: message, dismiss:true};
					$scope.fadeMsg();
					$scope.$apply();
				}
			});
		});
	};
	
	/**
	 * Boot the state users.update 
	 */
	$scope.changeToUpdate = function( id ) {
		$log.info("changeToUpdate", id);

		accountService.findUserById( $state.params.id, {
			callback : function(result) {
				
				$scope.currentEntity = result;
				$scope.currentEntity.password = ''; //clear password. if not set, not update.
				
				$scope.currentState = $scope.UPDATE_STATE;
				$state.go($scope.UPDATE_STATE);
				$scope.$apply();
			},
			errorHandler : function(message, exception) {
				$scope.msg = {type:"danger", text: message, dismiss:true};
				$scope.fadeMsg();
				$scope.$apply();
			}
		}); 
	};

	/*-------------------------------------------------------------------
	 * 		 				 	  BEHAVIORS
	 *-------------------------------------------------------------------*/

	/**
	 * Insert a new User.
	 * If success change state to users.list
	 */
	$scope.insertUser = function( user ) {

		if ( !$scope.form().$valid ) {
			$scope.msg = {type:"danger", text: $scope.INVALID_FORM_MESSAGE, dismiss:true};
			$scope.fadeMsg();
			return;
		}

		accountService.insertUser( user, {
			callback : function() {
				$scope.currentState = $scope.LIST_STATE;
				$state.go($scope.LIST_STATE);
				$scope.msg = {type:"success", text: $translate('admin.users.User-successfully-inserted') + '!', dismiss:true};
				$scope.fadeMsg();
				$scope.$apply();
			},
			errorHandler : function(message, exception) {
				$scope.msg = {type:"danger", text: message, dismiss:true};
				$scope.fadeMsg();
				$scope.$apply();
			}
		});
	};
	
	/**
	 * Change the page of ng-grid 
	 * 
	 * @see currentPage
	 * @see data.filter 
	 */
	$scope.changeToPage = function( filter, pageNumber ) {
		$scope.currentPage.pageable.page = pageNumber-1;
		$scope.listUsersByFilters( filter, $scope.currentPage.pageable );
	};

	
	/**
	 * Search Users considering filtering, paging and sorting.
	 * If success change state to users.list
	 * 
	 * @see data.filter
	 * @see currentPage
	 */
	$scope.listUsersByFilters = function( filter, pageRequest ) {
				
		accountService.listUsersByFilters( filter, pageRequest, {
			callback : function(result) {
				$scope.currentPage = result;
				$scope.currentPage.pageable.pageNumber++;
				$scope.currentState = $scope.LIST_STATE;
				$state.go( $scope.LIST_STATE );
				$scope.$apply();
			
			},
			errorHandler : function(message, exception) {
				$scope.msg = {type:"danger", text: message, dismiss:true};
				$scope.fadeMsg();
				$scope.$apply();
			}
		});
	};
	
	/**
	 * Update User
	 * if success redirect to users.list
	 */
	$scope.updateUser = function() {

		if ( !$scope.form().$valid ) {
			$scope.msg = {type:"danger", text: $scope.INVALID_FORM_MESSAGE, dismiss:true};
			$scope.fadeMsg();
			return;
		}

		accountService.updateUser( $scope.currentEntity , {
			callback : function() {
				$scope.currentState = $scope.LIST_STATE;
				$state.go($scope.LIST_STATE);
				$scope.msg = {type:"success", text: $translate('admin.users.User-updated-successfully')+ '!', dismiss:true};
				$scope.fadeMsg();
				$scope.$apply();
			},
			errorHandler : function(message, exception) {
				$scope.msg = {type:"danger", text: message, dismiss:true};
				$scope.$apply();
			}
		});
	};
	
	/*
	 * return the translated state of the user
	 */
	$scope.stateTranslated= function(){
		if ($scope.currentEntity.enabled){
			return $translate("admin.users.Active");
		} else {
			return $translate("admin.users.Inactive");
		}
	}
	
	/*
	 * return the translated role of the user
	 */
	$scope.roleTranslated= function(){
		if($scope.currentEntity.role == "ADMINISTRATOR"){
			return $translate("admin.users.Administrator") ;
		}
		
		if($scope.currentEntity.role == "MODERATOR"){
			return $translate("admin.users.Moderator");
		}
		
		if($scope.currentEntity.role == "USER"){
			return $translate("admin.users.User")
		}
	}
	
	$scope.fadeMsg = function(){
		$("div.msg").show();
		  
		  	setTimeout(function(){
		  		$("div.msg").fadeOut();
		  	}, 3000);
	};
	
	$(document).click(function() {
		$("div.msg").css("display","none");
    });
};
