'use strict';

/**
 *
 * @param $scope
 * @param $modalInstance
 * @constructor
 */
function SelectConfigLayerAccessGroupPopUpController( $scope, $modalInstance, selectedLayers, $log, $injector, $importService ) {

    $injector.invoke(AbstractCRUDController, this, {$scope: $scope});

    /*-------------------------------------------------------------------
     * 		 				 	EVENTS
     *-------------------------------------------------------------------*/

    $importService("layerGroupService");
    
    /**
     *  Handler que escuta toda vez que o usuário/programadamente faz o sorting na ng-grid.
     *  Quando o evento é disparado, configuramos o pager do spring-data
     *  e chamamos novamente a consulta, considerando também o estado do filtro (@see $scope.data.filter)
     */
    $scope.$on('ngGridEventSorted', function(event, sort) {

        if(event.targetScope.gridId != $scope.gridOptions.gridId)
        {
            return;
        }

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

            $scope.listByFilters( $scope.data.filter, null, $scope.currentPage.pageable );
        }
    });

    /*-------------------------------------------------------------------
     * 		 				 	ATTRIBUTES
     *-------------------------------------------------------------------*/

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
     * @type {null}
     */
    $scope.itensMarcados = [];

    /**
     * Handler que captura os eventos de marcação
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
        multiSelect: true,
        useExternalSorting: true,
        headerRowHeight: 45,
        showSelectionCheckbox: true,
        selectedItems: $scope.gridSelectedItems,
        rowHeight: 50,
        afterSelectionChange: function(rowItem){
            if (rowItem.length > 0) {
                var i;
                for (var rowItemIndex = 0; rowItemIndex < rowItem.length; rowItemIndex++) {
                    if (rowItem[rowItemIndex].selected){
                        i = $scope.findByIdInArray($scope.itensMarcados, rowItem[rowItemIndex].entity);
                        if (i == -1)
                            $scope.itensMarcados.push(rowItem[rowItemIndex].entity);
                    } else {
                        i = $scope.findByIdInArray($scope.itensMarcados, rowItem[rowItemIndex].entity);
                        if (i > -1)
                            $scope.itensMarcados.splice(i, 1);
                    }
                }
            } else {
                var i;
                if (rowItem.selected){
                    i = $scope.findByIdInArray($scope.itensMarcados, rowItem.entity);
                    if (i == -1)
                        $scope.itensMarcados.push(rowItem.entity);
                } else {
                    i = $scope.findByIdInArray($scope.itensMarcados, rowItem.entity);
                    if (i > -1)
                        $scope.itensMarcados.splice(i, 1);
                }
            }
        },
        columnDefs: [
            {displayName:'Simbologia', field:'legend', sortable:false, width: '120px', cellTemplate: IMAGE_LEGENDA},
            {displayName:'Título', field: 'title'},
            {displayName:'Nome', field:'name'}
        ]
    };

    /**
     * Variável que armazena o estado da paginação
     * para renderizar o pager e também para fazer as requisições das
     * novas páginas, contendo o estado do Sort incluído.
     *
     * @type PageRequest
     */
    $scope.currentPage;

    /**
     * Variável para armazenar atributos do formulário que
     * não cabem em uma entidade. Ex.:
     * @filter - Filtro da consulta
     */
    $scope.data = {
        filter : null
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

        $scope.itensMarcados = selectedLayers.slice(0);

        var pageRequest = new PageRequest();
        pageRequest.size = 6;
        $scope.pageRequest = pageRequest;

        var order = new Order();
        order.direction = 'ASC';
        order.property = 'id';

        $scope.pageRequest.sort = new Sort();
        $scope.pageRequest.sort.orders = [ order ];

        $scope.listByFilters( null, null, $scope.pageRequest );
    };

    /**
     * Configura o pageRequest conforme o componente visual pager
     * e chama o serviço de listagem, considerando o filtro corrente na tela.
     *
     * @see currentPage
     * @see data.filter
     */
    $scope.changeToPage = function( filter, pageNumber ) {
        $scope.currentPage.pageable.page = pageNumber-1;
        $scope.listByFilters( filter, null, $scope.currentPage.pageable );
        $scope.showLoading = false;
    };

    /**
     * Função responsável por fechar a pop sem executar outras ações
     */
    $scope.close = function( fechar )
    {
        $scope.msg = null;

        if (fechar != null)
        {
            $modalInstance.close(fechar);

        } else
        {
            $modalInstance.close($scope.itensMarcados);
        }

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

    /**
     * Realiza a consulta de registros, considerando filtro, paginação e sorting.
     * Quando ok, muda o estado da tela para list.
     *
     * @see data.filter
     * @see currentPage
     */
    $scope.listByFilters = function( filter, fonteDadosId, pageRequest ) {

        $scope.showLoading = true;

        layerGroupService.listLayersConfigurationByFilter( filter, null, pageRequest, {
            callback : function(result) {
                $scope.currentPage = result;
                $scope.currentPage.pageable.pageNumber++;//Para fazer o bind com o pagination
                $scope.showLoading = false;
                $scope.$apply();

                //Função responsável por marcar os registros na grid que já estavam marcados
                if ($scope.itensMarcados) {
                    angular.forEach( $scope.itensMarcados, function(data, index) {
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
};