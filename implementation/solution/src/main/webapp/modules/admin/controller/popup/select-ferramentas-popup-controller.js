'use strict';

/**
 *
 * @param $scope
 * @param $modalInstance
 * @constructor
 */
function SelectFerramentasPopUpController( $scope, $modalInstance, ferramentasSelecionadas, $log ) {

    /*-------------------------------------------------------------------
     * 		 				 	EVENTS
     *-------------------------------------------------------------------*/

    /**
     *  Handler that listens to each time the user makes the sorting in tables programmatically/ng-grid.
     *  When the event is fired, we configure the pager of the spring-date
     *  and we call again the query, considering also the filter State (@see $scope. date. filter)
     */
    $scope.$on('ngGridEventSorted', function(event, sort) {

        // compares the objects to ensure that the event is run only once does not loop
        if ( !angular.equals(sort, $scope.gridOptions.sortInfo) ) {
            $scope.gridOptions.sortInfo = angular.copy(sort);

            //Order of spring-data
            var order = new Order();
            order.direction = sort.directions[0].toUpperCase();
            order.property = sort.fields[0];

            //Sort of spring-data
//            $scope.currentPage.pageable.sort = new Sort();
//            $scope.currentPage.pageable.sort.orders = [ order ];

            $scope.list();
        }
    });

    /*-------------------------------------------------------------------
     * 		 				 	ATTRIBUTES
     *-------------------------------------------------------------------*/

    $scope.selectedEntity = null;

    $scope.gridSelectedItems = [];

    /**
     * Handler that captures the events marking
     * da grid
     * @param rows
     */
//    function toogleSelection (row) {
//        entity[configuracaoCamadaProperty] = row.entity;
//        $scope.close();
//    };

    var IMAGE_LEGENDA = '<div align="center" class="ngCellText" ng-cell-text ng-class="col.colIndex()">' +
        '<img style="width: 20px; height: 20px; border: solid 1px #c9c9c9;" ng-src="{{row.entity.legenda}}"/>' +
        '</div>';

    /**
     * General Settings da ng-grid.
     * @see https://github.com/angular-ui/ng-grid/wiki/Configuration-Options
     */
    $scope.gridOptions = {
        data: 'ferramentas',
        multiSelect: true,
        useExternalSorting: true,
        headerRowHeight: 45,
        showSelectionCheckbox: true,
        filterOptions: $scope.data,
        selectedItems: $scope.gridSelectedItems,
        rowHeight: 50,
        afterSelectionChange: function(row, event){
            $scope.selectedEntity = row.entity;
        },
        columnDefs: [
            {displayName:'Descrição', field:'descricao'},
            {displayName:'Nome', field: 'nome'}

        ]
    };

    /**
     * Variable that stores the State of the paging
     * to render the pager and also to make requisitions of
     * new pages, containing the State of the Sort included.
     *
     * @type PageRequest
     */
    $scope.currentPage;

    /**
     * Variable to store the form attributes that
     * not fit on an entity. Ex.:
     * @filter - Query Filter
     */
    $scope.data = {
        filterText : ''
    };

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

        var order = new Order();
        order.direction = 'ASC';
        order.property = 'id';

        $scope.data = {
            filterText: ''
        }

        $scope.pageRequest.sort = new Sort();
        $scope.pageRequest.sort.orders = [ order ];

        $scope.list();
    };

    /**
     * Configures the pageRequest as the visual component pager
     * and calls the listing service, considering the current filter on the screen.
     *
     * @see currentPage
     * @see data.filter
     */
    $scope.changeToPage = function( filter, pageNumber ) {
//        $scope.currentPage.pageable.page = pageNumber-1;
//        $scope.list();
        $scope.showLoading = false;
    };

    /**
     * Function responsible for closing the pop without performing other actions
     */
    $scope.close = function( fechar )
    {
        $scope.msg = null;

        if (fechar)
        {
            $modalInstance.close();

        } else
        {
            $modalInstance.close($scope.gridOptions.selectedItems);
        }

    };

    /**
     * Performs the query records, whereas filter, paging and sorting.
     * When ok, change the State of the screen to list.
     *
     * @see data.filter
     * @see currentPage
     */
    $scope.list = function( filter ) {

        $scope.showLoading = true;

        grupoAcessoService.listFerramentas( {
            callback : function(result) {
                $scope.ferramentas = result;
                $scope.showLoading = false;
                $scope.$apply();

                //Function responsible for marking the records that were already tagged prior to the opening of pop-up

                if (ferramentasSelecionadas != null) {
                    var items = ferramentasSelecionadas;
                    if (items.length > 0) {
                        angular.forEach( $scope.ferramentas, function(data, index) {
                            data.ordem = null;
                            angular.forEach( items, function(item) {
                                if ( data.nome == item.nome ){
                                    data.rotulo = item.rotulo;
                                    data.ordem = item.ordem;
                                    $scope.gridOptions.selectItem(index, true);
                                }
                            });
                        });
                    }
                }


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
};