'use strict';

/**
 *
 * @param $scope
 * @param $modalInstance
 * @constructor
 */
function SelectDataSourcePopUpController( $scope, $modalInstance, dataSourceSelected, $injector, $importService ) {

    $injector.invoke(AbstractCRUDController, this, {$scope: $scope});

    $importService("dataSourceService");
    
    /*-------------------------------------------------------------------
     * 		 				 	EVENTS
     *-------------------------------------------------------------------*/

    /**
     *  Handler that listens to each time the user makes the sorting in tables programmatically/ng-grid.
     *  When the event is fired, we configure the pager of the spring-date
     *  and we call again the query, considering also the filter State (@see $scope. date. filter)
     */
    $scope.$on('ngGridEventSorted', function(event, sort) {

        // compara os objetos para garantir que o evento seja executado somente uma vez que não entre em loop
        if ( !angular.equals(sort, $scope.gridOptions.sortInfo) ) {
            $scope.gridOptions.sortInfo = angular.copy(sort);

            //Order do spring-data
            var order = new Order();
            order.direction = sort.directions[0].toUpperCase();
            order.property = sort.fields[0];

            //Sort do spring-data
            $scope.currentPage.pageable.sort = new Sort();
            $scope.currentPage.pageable.sort.orders = [ order ];

            $scope.itensMarcados = $scope.gridOptions.selectedItems.slice(0);
            $scope.gridOptions.selectedItems.length = 0;

            $scope.listDataSourceByFilters( $scope.data.filter, $scope.currentPage.pageable );
        }
    });

    /**
     * 
     */
    $scope.selectedEntity = null;

    /**
     *
     * @type {{filterText: string}}
     */
    $scope.filterOptions = {
        filterText: ''
    };

    /**
     *
     * @type {null}
     */
    $scope.itensMarcados = null;

    /**
     * Handler que captura os eventos de marcação
     * da grid
     * @param rows
     */
//    function toogleSelection (row) {
//        entity[configuracaoCamadaProperty] = row.entity;
//        $scope.close();
//    };

    /*-------------------------------------------------------------------
     * 		 				 	ATTRIBUTES
     *-------------------------------------------------------------------*/
    /**
     * General Settings da ng-grid.
     * @see https://github.com/angular-ui/ng-grid/wiki/Configuration-Options
     */
    $scope.gridOptions = {
        data: 'currentPage.content',
        multiSelect: false,
        rowHeight: 45,
        headerRowHeight: 45,
        useExternalSorting: true,
        selectedItems: [],
        afterSelectionChange: function(row, event){
            $scope.selectedEntity = row.entity;
        },
        columnDefs: [
            {displayName:'Name', field:'name'}
        ]
    };

    /**
    * Variable that stores the state of paging
    * to render the pager and also to make requisitions of
    * new pages, containing the State of the Sort included
    *
    * @type PageRequest
    */
    $scope.currentPage;

    /**
     * Variable to store the form attributes that
     * not fit on an entity. Ex.:
     * @filter - Query filter
     */
    $scope.data = { filter:null };
    
    /**
    *
    * @type {boolean}
    */
   $scope.showLoading = true;
   
   /**
    * 
    */
   $scope.currentEntity;

    /*-------------------------------------------------------------------
     * 		 				 	  BEHAVIORS
     *-------------------------------------------------------------------*/
    /**
     * Performs the query when displaying a pop-up
     */
    $scope.initialize = function() {

        var pageRequest = new PageRequest();
        pageRequest.size = 6;
        $scope.pageRequest = pageRequest;

        /*var order = new Order();
        order.direction = 'ASC';
        order.property = 'id';

        $scope.pageRequest.sort = new Sort();
        $scope.pageRequest.sort.orders = [ order ];*/

        $scope.listDataSourceByFilters( null, pageRequest );
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
        $scope.listDataSourceByFilters( filter, $scope.currentPage.pageable );
        $scope.showLoading = false;
    };

    /**
     * Function responsible for closing the pop without performing other actions
     */
    $scope.close = function ( fechar ) {

        // verifica se o usuário selecionou a opção de fechar ou selecionar na pop up
        if (fechar){
            $modalInstance.close();
        } else {
            $modalInstance.close($scope.selectedEntity);
        }

    };
    
    /**
	 * Performs the query records, consirando filter, paging and sorting.
	 * When ok, change the State of the screen to list.
	 * 
	 * @see data.filter
	 * @see currentPage
	 */
	$scope.listDataSourceByFilters = function( filter, pageRequest ) {
		
		$scope.showLoading = true;
		
		dataSourceService.listDataSourceByFilters( filter, pageRequest, {
			callback : function(result) {
				$scope.currentPage = result;
				$scope.currentPage.pageable.pageNumber++;//Para fazer o bind com o pagination
                $scope.$apply();
				
				$scope.showLoading = false;

                //Function responsible for marking the records that were already tagged prior to the opening of pop-up
                var item = dataSourceSelected;
                if (item) {
                    angular.forEach( $scope.currentPage.content, function(data, index) {
                        if ( data.id == item.id ){
                            $scope.gridOptions.selectRow(index, true);
                        }
                    });
                }

                //Function responsible for mark the items that were already marked before the method $scope. listGruposAcessosByFilter be called
                if ($scope.itensMarcados) {
                    angular.forEach( $scope.itensMarcados, function(data, index) {
                        var i = $scope.findByIdInArray($scope.currentPage.content, data);
                        if (i > -1) {
                            $scope.gridOptions.selectItem(i, true);
                        }
                    });
                }

				$scope.$apply();
				
			},
			errorHandler : function(message, exception) {
				$scope.msg = {type:"danger", text: message, dismiss:true};
				$scope.$apply();
			}
		});
		
	};

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

};