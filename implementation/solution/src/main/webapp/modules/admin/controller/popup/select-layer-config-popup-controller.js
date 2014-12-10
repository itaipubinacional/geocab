'use strict';

/**
 *
 * @param $scope
 * @param $modalInstance
 * @constructor
 */
function SelectLayerConfigPopUpController( $scope, $modalInstance, dataSource, selectedLayer, $importService ) {

    /*-------------------------------------------------------------------
     * 		 				 	EVENTS
     *-------------------------------------------------------------------*/

	 $importService("layerGroupService");
    /**
     *  Handler que escuta toda vez que o usuï¿½rio/programadamente faz o sorting na ng-grid.
     *  Quando o evento ï¿½ disparado, configuramos o pager do spring-data
     *  e chamamos novamente a consulta, considerando tambï¿½m o estado do filtro (@see $scope.data.filter)
     */
    $scope.$on('ngGridEventSorted', function(event, sort) {

        // compara os objetos para garantir que o evento seja executado somente uma vez que nï¿½o entre em loop
        if ( !angular.equals(sort, $scope.gridOptions.sortInfo) ) {
            $scope.gridOptions.sortInfo = angular.copy(sort);

            //Order do spring-data
            var order = new Order();
            order.direction = sort.directions[0].toUpperCase();
            order.property = sort.fields[0];

            //Sort do spring-data
            $scope.currentPage.pageable.sort = new Sort();
            $scope.currentPage.pageable.sort.orders = [ order ];

            $scope.listByFilters( $scope.currentEntity.filter, $scope.currentEntity.dataSource.id, $scope.currentPage.pageable );
        }
    });

    /*-------------------------------------------------------------------
     * 		 				 	ATTRIBUTES
     *-------------------------------------------------------------------*/

    $scope.selectedEntity = null;

    /**
     * Handler que captura os eventos de marcaï¿½ï¿½o
     * da grid
     * @param rows
     */
//    function toogleSelection (row) {
//        entity[configuracaoCamadaProperty] = row.entity;
//        $scope.close();
//    };

    var IMAGE_LEGENDA = '<div align="center" class="ngCellText" ng-cell-text ng-class="col.colIndex()">' +
        '<img ng-if="row.entity.dataSource.url" style="width: 20px; height: 20px; border: solid 1px #c9c9c9;" ng-src="{{row.entity.legend}}"/>' +
    	'<img ng-if="!row.entity.dataSource.url" style="width: 20px; height: 20px; border: solid 1px #c9c9c9;" ng-src="{{row.entity.icon}}"/>' +
        '</div>';

    /**
     * Configurações gerais da ng-grid.
     * @see https://github.com/angular-ui/ng-grid/wiki/Configuration-Options
     */
    $scope.gridOptions = {
        data: 'currentPage.content',
        multiSelect: false,
        useExternalSorting: true,
        headerRowHeight: 45,
        rowHeight: 50,
        selectedItems: [],
        afterSelectionChange: function(row, event){
            $scope.selectedEntity = row.entity;
        },
        columnDefs: [
            {displayName:'Simbologia', field:'legend', sortable:false, width: '120px', cellTemplate: IMAGE_LEGENDA},
            {displayName:'TÃ­tulo', field: 'title'},
            {displayName:'Nome', field:'name'}
        ]
    };


    /**
     * Variï¿½vel que armazena o estado da paginaï¿½ï¿½o
     * para renderizar o pager e tambï¿½m para fazer as requisiï¿½ï¿½es das
     * novas pï¿½ginas, contendo o estado do Sort incluï¿½do.
     *
     * @type PageRequest
     */
    $scope.currentPage;

    /**
     * Variï¿½vel para armazenar atributos do formulï¿½rio que
     * nï¿½o cabem em uma entidade. Ex.:
     * @filter - Filtro da consulta
     */
    $scope.data = {
        filter : null,
        dataSource : null
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
     * Realiza a consulta ao exibir a pop-up
     */
    $scope.initialize = function() {

        var pageRequest = new PageRequest();
        pageRequest.size = 6;
        $scope.pageRequest = pageRequest;

        var order = new Order();
        order.direction = 'ASC';
        order.property = 'id';

        // seta a fonte de dados
        $scope.data.dataSource = dataSource;

        $scope.pageRequest.sort = new Sort();
        $scope.pageRequest.sort.orders = [ order ];

        $scope.listByFilters( null, $scope.data.dataSource.id, pageRequest );
    };

    /**
     * Configura o pageRequest conforme o componente visual pager
     * e chama o serviï¿½o de listagem, considerando o filtro corrente na tela.
     *
     * @see currentPage
     * @see data.filter
     */
    $scope.changeToPage = function( filter, pageNumber ) {
        $scope.currentPage.pageable.page = pageNumber-1;
        $scope.listByFilters( filter, $scope.data.dataSource.id, $scope.currentPage.pageable );
        $scope.showLoading = false;
    };

    /**
     * Funï¿½ï¿½o responsï¿½vel por fechar a pop sem executar outras aï¿½ï¿½es
     */
    $scope.close = function (fechar) {

        if (fechar) {
            $modalInstance.close();
        } else {
            $modalInstance.close($scope.selectedEntity);
        }

    };

    /**
     * Realiza a consulta de registros, considerando filtro, paginaï¿½ï¿½o e sorting.
     * Quando ok, muda o estado da tela para list.
     *
     * @see data.filter
     * @see currentPage
     */
    $scope.listByFilters = function( filter, dataSourceId, pageRequest ) {

        $scope.showLoading = true;

        layerGroupService.listLayersByFilters( filter, dataSourceId ,pageRequest, {
            callback : function(result) {
                $scope.currentPage = result;
                $scope.currentPage.pageable.pageNumber++;//Para fazer o bind com o pagination
                $scope.showLoading = false;
                $scope.$apply();

                //Funï¿½ï¿½o responsï¿½vel por marcar os registros que jï¿½ estavam marcados antes da abertura da pop-up

                if (selectedLayer != null) {
                    angular.forEach( $scope.currentPage.content, function(data, index) {
                        if ( data.id == selectedLayer.id ){
                            $scope.gridOptions.selectRow(index, true);
                        }

                    });
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