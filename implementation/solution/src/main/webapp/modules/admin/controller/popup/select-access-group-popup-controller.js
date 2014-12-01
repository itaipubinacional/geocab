'use strict';

/**
 * 
 * @param $scope
 * @param $log
 * @param $location
 */
function SelectAccessGroupPopUpController($scope, $modalInstance, $log, gruposSelecionados) {

	/*-------------------------------------------------------------------
	 * 		 				 	EVENT HANDLERS
	 *-------------------------------------------------------------------*/


    /**
     *  Handler that listens to each time the user makes the sorting in tables programmatically/ng-grid.
     *  When the event is fired, we configure the pager of the spring-date
     *  and we call again the query, considering also the filter State (@see $scope. date. filter)
     */
    $scope.$on('ngGridEventSorted', function (event, sort) {

        if(event.targetScope.gridId != $scope.gridOptions.gridId)
        {
            return;
        }

        // compara os objetos para garantir que o evento seja executado somente uma vez q não entre em loop
        if (!angular.equals(sort, $scope.gridOptions.sortInfo)) {
            $scope.gridOptions.sortInfo = angular.copy(sort);

            //Order do spring-data
            var order = new Order();
            order.direction = sort.directions[0].toUpperCase();
            order.property = sort.fields[0];

            //Sort do spring-data
            $scope.currentPage.pageable.sort = new Sort();
            $scope.currentPage.pageable.sort.orders = [ order ];

            $scope.listGruposAcessosByFilter($scope.data.filter, $scope.currentPage.pageable);
        }
    });


	/*-------------------------------------------------------------------
	 * 		 				 	ATTRIBUTES
	 *-------------------------------------------------------------------*/


    /**
     * Variable to store the form attributes
     * not fit on an entity. Ex.:
     * @filter - Query filterv
     */
    $scope.data = { filter:null };

	/**
	 * 
	 */
	$scope.currentPage;

	/**
	 * 
	 */
	$scope.grupos = [];

    /**
     *a
     * @type {{data: string, multiSelect: boolean, useExternalSorting: boolean, headerRowHeight: number, filterOptions: ($scope.filterOptions|*), rowHeight: number, enableRowSelection: boolean, afterSelectionChange: afterSelectionChange, columnDefs: *[]}}
     */
    $scope.gridOptions = {
        data: 'currentPage.content',
        multiSelect: true,
        showSelectionCheckbox: true,
        useExternalSorting: true,
        headerRowHeight: 45,
        keepLastSelected: false,
        rowHeight: 45,
        selectedItems: [],
        enableRowSelection: true,
        columnDefs: [
            {displayName: 'Nome', field: 'nome'},
            {displayName: 'Descrição', field: 'descricao'}
        ]
    };

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
        var pageRequest = new PageRequest();
        pageRequest.size = 6;
        $scope.pageRequest = pageRequest;

		$scope.listGruposAcessosByFilter($scope.data.filter, $scope.pageRequest);


    };

    /**
     * Configures the pageRequest as the visual component pager
     * and calls the listing service, considering the current filter on the screen.
     *
     * @see currentPage
     * @see data.filter
     */
    $scope.changeToPage = function( filter, pageNumber ) {
        $scope.currentPage.pageable.page = pageNumber-1;
        $scope.listGruposAcessosByFilter( filter, $scope.currentPage.pageable );
        $scope.showLoading = false;
    };

	/*-------------------------------------------------------------------
	 * 		 				 	  BEHAVIORS
	 *-------------------------------------------------------------------*/

    /**
     *
     * @param string
     * @param list
     * @returns {number}
     */
    $scope.findName = function(string, list) {
        for (var index = 0; index < list.length; index++) {
            if (list[index].nome == string) return index
        }
        return -1;
    }

	/**
	 * Performs the query records, considering  filter, paging and sorting.
	 * When ok, change the screen state to list
	 *
	 * @see data.filter
	 * @see currentPage
	 */
	$scope.listGruposAcessosByFilter = function( filtro, pageRequest ) {

        grupoAcessoService.listGrupoAcessoByFilters( filtro, pageRequest, {
			callback : function(result) {
				$scope.currentPage = result;
				$scope.currentPage.pageable.pageNumber++;
                $scope.$apply();

                $scope.showLoading = false;

                
                if (gruposSelecionados) {
                    angular.forEach( gruposSelecionados, function(data, index) {
                        var i = $scope.findName(data.nome, $scope.currentPage.content);
                        if (i > -1) {
                            $scope.currentPage.content.splice(i, 1);
                        }
                    });
                };

                $scope.$apply();


			},
			errorHandler : function(message, exception) {
				$scope.message = {type:"danger", text: message};
				$scope.$apply();
			}
		});
	};

	$scope.my_tree_handler = function(branch) 
	{
		$scope.currentEntity = branch;
	};

	$scope.form = function( formName )
	{

		if ( !formName ) 
		{
			formName = "form";
		}

		return $("form[name="+formName+"]").scope()[formName];
	};

	/**
	 *
	 */
	 $scope.close = function( fechar )
	 {
         if (fechar) {
             $modalInstance.close();
         } else {
             $modalInstance.close($scope.gridOptions.selectedItems);
         }

	 };
};