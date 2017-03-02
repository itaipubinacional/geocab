'use strict';

/**
 * 
 * @param $scope
 * @param $log
 * @param $location
 */
function DataSourceController( $scope, $injector, $log, $state, $timeout, $modal, $location, $importService, $translate ) {
	
	/**
	 * Injects methods, attributes and their inherited state of AbstractCRUDController.
	 * @see AbstractCRUDController
	 */
	$injector.invoke(AbstractCRUDController, this, {$scope: $scope});
	
	$importService("dataSourceService");

	/*-------------------------------------------------------------------
	 * 		 				 	EVENT HANDLERS
	 *-------------------------------------------------------------------*/

	/**
	 * Handler listening whenever the user / programmatically does the sorting in grid-ng. 
     * When the event is fired, we set the pager's spring-data 
     * and call the query again, also considering the state of the filter(@see $scope.data.filter)
	 */
	$scope.$on('ngGridEventSorted', function(event, sort) {

		// compares the objects to ensure that the event runs only once not enter a loop
		if ( !angular.equals(sort, $scope.gridOptions.sortInfo) ) {
			$scope.gridOptions.sortInfo = angular.copy(sort);

			//Order spring-data
			var order = new Order();
			order.direction = sort.directions[0].toUpperCase();
			order.property = sort.fields[0];

			//Sort spring-data
			$scope.currentPage.pageable.sort = new Sort();
			$scope.currentPage.pageable.sort.orders = [ order ];

			$scope.listDataSourceByFilters( $scope.data.filter, $scope.currentPage.pageable );
		}
	});

	/*-------------------------------------------------------------------
	 * 		 				 	ATTRIBUTES
	 *-------------------------------------------------------------------*/
	//STATES
	/**
	 * Static variable that represents 
	 * the state list of records.
	 */
	$scope.LIST_STATE = "data-source.list";
	/**
	 * Static variable that represents
	 * detail the state of a record.
	 */
	$scope.DETAIL_STATE = "data-source.detail";
	/**
	 * Static variable that represents
	 * the state for the creation of records.
	 */
	$scope.INSERT_STATE = "data-source.create";
	/**
	 * Static variable that represents
	 * the state for editing records.
	 */
	$scope.UPDATE_STATE = "data-source.update";
	/**
	 * Variable that stores the current state of the screen
	 * This variable should ALWAYS be in agreement with the URL
	 * that is in the browser.
	 */
	$scope.currentState;

	//DATA GRID
	/**
	 * Static variable with buttons shares of grid
	 * The edit button navigates via URL (SREF) why the editing is done on another page
	 * now the delete button a direct method calls via click-ng why not have a specific status screen.
	 */
    var GRID_ACTION_BUTTONS = '<div class="cell-centered">' +
	'<a ui-sref="data-source.update({id:row.entity.id})" title="'+ $translate('admin.datasource.Update') +'" class="btn btn-mini"><i class="itaipu-icon-edit"></i></a>'+
	'<a ng-click="changeToRemove(row.entity)" title="'+ $translate('admin.datasource.Delete') +'" class="btn btn-mini"><i class="itaipu-icon-delete"></i></a>'+
	'</div>';

	/**
	 * Settings of ng-grid
	 * @see https://github.com/angular-ui/ng-grid/wiki/Configuration-Options
	 */
	$scope.gridOptions = { 
			data: 'currentPage.content',
			multiSelect: false,
			useExternalSorting: true,
            headerRowHeight: 45,
            rowHeight: 45,
			beforeSelectionChange: function (row, event) {
				//avoids calling include selecting, when clicked on a button action.
				if ( $(event.target).is("a") || $(event.target).is("i") ) return false;
				$state.go($scope.DETAIL_STATE, {id:row.entity.id});
			},
			columnDefs: [
			             {displayName: $translate('Name'), field:'name'},
			             {displayName: $translate('Address'), field:'url',  width:'55%'},
			             {displayName: $translate('Actions'), sortable:false, cellTemplate: GRID_ACTION_BUTTONS, width:'100px'}
			             ]
	};

	/**
	 * 
	 * Store the state of paging
	 * to render the pager and to make requisitions of new pages containing the state Sort included.
	 * @type PageRequest
	 * 
	 * @type PageRequest
	 */
	$scope.currentPage;

	//FORM
	/**
	 * 
	 * Store auxiliary information that dont't fit in an entity Ex:
	 * @filter - Search filter
	 * 
	 */
	$scope.data = { filter:null, showFields: false };
	/**
	 * Store current User entity for update
	 */
	$scope.currentEntity;
	
	
	/*-------------------------------------------------------------------
	 * 		 				 	  NAVIGATIONS
	 *-------------------------------------------------------------------*/
	/**
	 * 
	 * Main method that makes the role of front-controller screen.
	 * It is invoked whenever there is a change of URL (@see $stateChangeSuccess),
	 * Ex.: /list -> changeToList()
	 *      /create -> changeToInsert()
	 *      
	 * If the state is not found, it directs you to the list
	 * 
	 */
	$scope.initialize = function( toState, toParams, fromState, fromParams ) {
		var state = $state.current.name;
	
		/**
		 * It is necessary to remove the SortInfo attribute for the return of an issue was doubling the value of the same attribute with the Sort 
		 * Preventing the ordinations in the columns of the grid.
		 */
		if( $scope.gridOptions.sortInfo ){
			delete $scope.gridOptions.sortInfo;
		}

		$log.info("Starting the front controller.");

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
	 * 
	 * Boot the state users.list
	 * @see LIST_STATE
	 * @see $stateChangeSuccess
	 * 
	 * To switch to this state, you must first load the data from the query.
	 * 
	 */
	$scope.changeToList = function() {
		$log.info("changeToList");
		
		var pageRequest = new PageRequest();
		pageRequest.size = 6;
		$scope.pageRequest = pageRequest;

		$scope.listDataSourceByFilters( null, pageRequest );
		
		$scope.currentState = $scope.LIST_STATE;
	};

	/**
	 * 
	 * Boot the state users.insert 
	 * @see INSERT_STATE
	 * @see $stateChangeSuccess
	 * 
	 * To switch to this state, you must first instantiate a new currentEntity, 
	 * To clear the fields and set default values​​.	
	 * 
	 */
	$scope.changeToInsert = function() {
		$log.info("changeToInsert");

		$scope.currentEntity = new DataSource();
		/*
		$scope.currentEntity = new FonteDados();
		
		//To operate ng-model no radio button
		$scope.currentEntity.tipoFonteDados = 'WMS';*/

		$scope.currentState = $scope.INSERT_STATE;

	};

	/**
	 * Boot the state users.update
	 * @see UPDATE_STATE
	 * @see $stateChangeSuccess 
	 *
	 * To move to this state, must first obtain the ID 
	 * The record of the consultation service and only then change the state of the screen.
	 *
	 *
	 */
	$scope.changeToUpdate = function( id ) {
		$log.info("changeToUpdate", id);

		dataSourceService.findDataSourceById( $state.params.id, {
			callback : function(result) {
				$scope.currentEntity = result;
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

	/**
	 * 
	 * Boot the state users.detail 
	 * @see DETAIL_STATE
	 * @see $stateChangeSuccess
	 * 
	 * To switch to this state, one must first get through the 
	 * registration id updated by the consultation service and only then change the state of the screen. 
	 * If the identifier is invalid, returns to the state list.
	 * 
	 */
	$scope.changeToDetail = function( id ) {
		$log.info("changeToDetail", id);

		if ( id == null || id == "" || id == 0 ) {
			$scope.msg = {type:"error", text: $scope.INVALID_ID_MESSAGE, dismiss:true};
			
			$scope.currentState = $scope.LIST_STATE;
			$state.go($scope.LIST_STATE);
			return;
		}

		dataSourceService.findDataSourceById( id, {
			callback : function(result) {
				$scope.currentEntity = result;
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
	 * Boot the state users.remove
	 * 
	 * Before deleting the user notified for confirmation
	 * and then the record is excluded.
	 * Once deleted, update the grid with state filter, paging and sorting.
	 */
	$scope.changeToRemove = function( dataSource ) {
		$log.info("changeToRemove", dataSource);

		var dialog = $modal.open( {
			templateUrl: "static/libs/eits-directives/dialog/dialog-template.html",
			controller: DialogController,
			windowClass: 'dialog-delete',
			resolve: {
				title: function(){return $translate("admin.datasource.Deleting-data-source"); },
				message: function(){return $translate("admin.datasource.Are-you-sure-you-want-to-delete-the-data-source")+' "'+dataSource.name+'"? <br/>'+$translate("admin.datasource.This-operation-can-not-be-undone")+'.'; },
				buttons: function(){return [ {label: $translate("Remove") , css:'btn btn-danger'}, {label: $translate('admin.datasource.Cancel') , css:'btn btn-default', dismiss:true} ];}
			}
		});

        dialog.result.then( function(result) {

			dataSourceService.removeDataSource( dataSource.id, {
				callback : function(result) {
					//if the currentPage is null, configure the pager default
					if ( $scope.currentPage == null ) {
						$scope.changeToList();
						//else, use the same state to load the listing
					} else {
						$scope.listDataSourceByFilters($scope.data.filter, $scope.currentPage.pageable);
					}

					$scope.msg = {type: "success", text: $translate("admin.datasource.The-register") + ' "'+dataSource.name+'" '+$translate("admin.datasource.was-successfully-deleted")+'.', dismiss:true};
					
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
	 * Sets the pageRequest as the visual component and calls the pager listing service, 
	 * considering the current filter on the screen.
	 * 
	 * @see currentPage
	 * @see data.filter 
	 */
	$scope.changeToPage = function( filter, pageNumber ) {
		$scope.currentPage.pageable.page = pageNumber-1;
		$scope.listDataSourceByFilters( filter, $scope.currentPage.pageable );
	};

	/*-------------------------------------------------------------------
	 * 		 				 	  BEHAVIORS
	 *-------------------------------------------------------------------*/

	/**
	 * Performs the query logs, considering filter, paging and sorting. 
	 * When ok, change the state of the screen to list.
	 * 
	 * @see data.filter
	 * @see currentPage
	 */
	$scope.listDataSourceByFilters = function( filter, pageRequest ) {

		dataSourceService.listDataSourceByFilters( filter, pageRequest, {
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
	 * Insert a new Data Source
	 * If success, change to detail state.
	 */
	$scope.insertDataSource = function() {

		if ( !$scope.form().$valid ) {
			$scope.msg = {type:"danger", text: $scope.INVALID_FORM_MESSAGE, dismiss:true};
			$scope.fadeMsg();
			return;
		}

		dataSourceService.insertDataSource( $scope.currentEntity, {
			callback : function() {
				$scope.currentState = $scope.LIST_STATE;
				$state.go($scope.LIST_STATE);
				$scope.msg = {type:"success", text: $translate("admin.datasource.Geographic-data-source-successfully-inserted")+"!", dismiss:true};
				$scope.fadeMsg();
				$scope.$apply();
				$scope.data.showFieldUrl = null;
			},
			errorHandler : function(message, exception) {
				$scope.msg = {type:"danger", text: message, dismiss:true};
				$scope.fadeMsg();
				$scope.$apply();
			}
		});
	};

	/**
	 *Performs updates a record 
	 *and successfully modifies the state of the screen to state detail
	 */
	$scope.updateDataSource = function() {

		if ( !$scope.form().$valid ) {
			$scope.msg = {type:"danger", text: $scope.INVALID_FORM_MESSAGE, dismiss:true};
			$scope.fadeMsg();
			return;
		}

		dataSourceService.updateDataSource( $scope.currentEntity , {
			callback : function() {
			
				$scope.currentState = $scope.LIST_STATE;
				$state.go($scope.LIST_STATE);
				$scope.msg = {type:"success", text: $translate("admin.datasource.Geographic-data-source-successfully-updated")+"!", dismiss:true};
				$scope.fadeMsg();
				$scope.$apply();
			},
			errorHandler : function(message, exception) {
				if (exception.message.indexOf("ConstraintViolationException") > -1){
					message = $translate("admin.datasource.The-name-or-address-entered-field-already-exists,-change-and-try-again")+".";
				}
				$scope.msg = {type:"danger", text: message, dismiss:true};
				$scope.fadeMsg();
				$scope.$apply();
			}
		});
	};
	
	/**
	 * Test the connection
	 */
	$scope.testDataSourceConnection = function() {
		
		
		if ( $scope.currentState != $scope.DETAIL_STATE && !$scope.form().url.$valid ) {
			$scope.msg = {type:"danger", text: $scope.INVALID_FORM_MESSAGE, dismiss:true};
			$scope.fadeMsg();
			return;
		}

		dataSourceService.testConnection( $scope.currentEntity.url, {
			callback : function(result) {
				if(result){
					$scope.msg = {type:"success", text: $translate("admin.datasource.Connection-successfully-established"), dismiss:true};	
					$scope.fadeMsg();
				}
				else{
					$scope.msg = {type:"danger", text: $translate("admin.datasource.Could-not-connect-to-the-geographic-data-source")+".", dismiss:true};
					$scope.fadeMsg();
				}
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
	 * Clear the fields
	 */
	$scope.clearFields = function(){
		if( ! $('#authenticationRequired').is(':checked')){
			$scope.currentEntity.login = null;
			$scope.currentEntity.password = null;
		}
	};
	
	$scope.clearFieldUrl= function(){
		if(! $('#urlRequired').is(':checked')){
			$scope.currentEntity.url=null;
			$('#authenticationRequired').attr('checked',false);
			$scope.clearFields();
		}
	};

	$scope.externalDataSource;
	
	$scope.isUrlChecked = function(){
		if ( $('#urlRequired').is(':checked')  ) {
			return true;
		}
	}
	
	$scope.isUserChecked = function(){
		if( $('#authenticationRequired').is(':checked') ){
			return true;
		}
	}
	
	
	$scope.fadeMsg = function(){
		$("div.msg").show();
		  
		  	setTimeout(function(){
		  		$("div.msg").fadeOut();
		  	}, 3000);
	}
	
	$(document).click(function() {
		$("div.msg").css("display","none");
    });
	
};

