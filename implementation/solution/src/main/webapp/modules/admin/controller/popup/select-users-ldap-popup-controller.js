'use strict';

/**
 * 
 * @param $scope
 * @param $log
 * @param $location
 */
function SelectUsersPopUpController($scope, $modalInstance , usersSelected, $log, $importService, $injector) {


	$injector.invoke(AbstractCRUDController, this, {$scope: $scope});
	
	$scope.msg = null;
	
	$importService("accountService");

	/*-------------------------------------------------------------------
	 * 		 				 	ATTRIBUTES
	 *-------------------------------------------------------------------*/

	/**
	 * 
	 */
	$scope.currentEntity = [];

    /**
     *
     * @type {null}
     */
    $scope.selectedEntity = null;

    /**
    *
    * @type {Array}
    */
   $scope.gridSelectedItems = [];
	
	/**
	 * 
	 */
	$scope.data = {
	        filter: ''
	      };

    /**
     *
     * @type {boolean}
     */
    $scope.showLoading = false;
	/*-------------------------------------------------------------------
	 * 		 				 	  NAVIGATIONS
	 *-------------------------------------------------------------------*/
	/**
	 * Main method that makes the role of front-controller of the screen.
	 * He is invoked whenever there is a change of URL (@see $stateChangeSuccess),
	 * When this occurs, gets the State via the $state and calls the initial method of that State.
	 * Ex.: /list -> changeToList()
	 *      /create -> changeToInsert()
	 *
	 * If the State is not found, he directs to the listing,
	 * Although the front controller of angle won't let enter an invalid URL.
	 */
	$scope.initialize = function() 
	{
        
        $scope.currentEntity = usersSelected.slice(0);
        
        var pageRequest = new PageRequest();
        pageRequest.size = 6;
        $scope.pageRequest = pageRequest;

        var order = new Order();
        order.direction = 'ASC';
        order.property = 'id';

        $scope.pageRequest.sort = new Sort();
        $scope.pageRequest.sort.orders = [ order ];
     
        $scope.listUsersByFilters(null, $scope.pageRequest);
        
	};

	/*-------------------------------------------------------------------
	 * 		 				 	  BEHAVIORS
	 *-------------------------------------------------------------------*/
	
	/**
     * Performs the query records, considering filter, paging and sorting.
     * When ok, change the State of the screen to list.
     *
     * @see data.filter
     * @see currentPage
     */
    $scope.listUsersByFilters = function( filter, pageRequest ) {

    	$scope.currentEntity = $scope.gridOptions.selectedItems.length > 0 ? $scope.gridOptions.selectedItems.slice(0): $scope.currentEntity;
        $scope.gridOptions.selectedItems.length = 0;
        
        $scope.showLoading = true;

        accountService.listUsersByFilters( filter, pageRequest, {
            callback : function(result) {
            	$scope.currentPage = result;
                $scope.currentPage.pageable.pageNumber++;//Para fazer o bind com o pagination
                $scope.showLoading = false;
                $scope.$apply();

                //Função responsável por marcar os registros na grid que já estavam marcados
                if ($scope.currentEntity) {
                    angular.forEach( $scope.currentEntity, function(data, index) {
                        var i = $scope.findByIdInArray($scope.currentPage.content, data);
                        if (i > -1) {
                            $scope.gridOptions.selectItem(i, true);
                        }
                    });
                };

                $scope.showLoading = false;
                $scope.$apply();

            },
            errorHandler : function(message, exception) {
                $scope.message = {type:"error", text: message};
                $scope.showLoading = false;
                $scope.$apply();
            }
        });
    };

    $scope.filterSelectedUsers = function () {
        var index;
        for (var i = 0; i < $scope.usersSelected.length; i++) {
            index = $scope.users.indexOf($scope.usersSelected[i]);
            if (index > -1) {
                $scope.users.splice(index, 1);
            };
        };
    };

	/**
	 * Close popup
	 */
	$scope.fechaPopup = function () 
	{
		$modalInstance.close($scope.currentEntity);
	};

	$scope.form = function( formName )
	{

		if ( !formName ) 
		{
			formName = "form";
		}

		return $("form[name="+formName+"]").scope()[formName];
	};

    $scope.findUsername = function(string, list) {
        for (var index = 0; index < list.length; index++) {
            if (list[index].username == string) return index
        }
        return -1;
    }
    
    $scope.gridOptions = {
        data: 'currentPage.content',
        multiSelect: true,
        useExternalSorting: true,
        headerRowHeight: 45,
        filterOptions: $scope.filterOptions,
        showSelectionCheckbox: true,
        selectedItems: $scope.gridSelectedItems,
        rowHeight: 45,
        enableRowSelection: true,
        afterSelectionChange: function (rowItem, event) {
        	
            if (rowItem.length > 0) {
                var i;
                for (var rowItemIndex = 0; rowItemIndex < rowItem.length; rowItemIndex++) {
                    if (rowItem[rowItemIndex].selected){
                        i = $scope.findByIdInArray($scope.currentEntity, rowItem[rowItemIndex].entity);
                        if (i == -1)
                            $scope.currentEntity.push(rowItem[rowItemIndex].entity);
                    } else {
                        i = $scope.findByIdInArray($scope.currentEntity, rowItem[rowItemIndex].entity);
                        if (i > -1)
                            $scope.currentEntity.splice(i, 1);
                    }
                }
            } else {
                var i;
                if (rowItem.selected){
                    i = $scope.findByIdInArray($scope.currentEntity, rowItem.entity);
                    if (i == -1)
                        $scope.currentEntity.push(rowItem.entity);
                } else {
                    i = $scope.findByIdInArray($scope.currentEntity, rowItem.entity);
                    if (i > -1)
                        $scope.currentEntity.splice(i, 1);
                }
            }
        },
        columnDefs: [
            {displayName:'Nome Completo', field:'name', width: '30%'},
            {displayName:'Nome de usuário', field:'username', width: '30%'},
            {displayName:'E-mail', field: 'email', width:'40%'}
        ]
    };
    
    $scope.changeToPage = function (filter, pageNumber) {
        $scope.currentPage.pageable.page = pageNumber - 1;
        $scope.listUsersByFilters(filter, $scope.currentPage.pageable);
        $scope.showLoading = false;
    };
   

    /**
	 *
	 */
	$scope.close = function( fechar )
	{
		$scope.msg = null;

        // checks whether the user has selected the option to close or select from the pop up
        if (fechar){
            $modalInstance.close();
        } else {
            $modalInstance.close($scope.currentEntity);
        }

	};
};