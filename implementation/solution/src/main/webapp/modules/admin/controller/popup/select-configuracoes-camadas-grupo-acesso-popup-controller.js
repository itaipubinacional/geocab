'use strict';

/**
 *
 * @param $scope
 * @param $modalInstance
 * @constructor
 */
function SelectConfiguracoesCamadasGrupoAcessoPopUpController( $scope, $modalInstance, camadasSelecionadas, $log ) {

    /*-------------------------------------------------------------------
     * 		 				 	EVENTS
     *-------------------------------------------------------------------*/

    /**
     *  Handler que escuta toda vez que o usuário/programadamente faz o sorting na ng-grid.
     *  Quando o evento é disparado, configuramos o pager do spring-data
     *  e chamamos novamente a consulta, considerando também o estado do filtro (@see $scope.data.filter)
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

            $scope.listByFilters( $scope.data.filter, $scope.data.fonteDados.id, $scope.currentPage.pageable );
        }
    });

    /*-------------------------------------------------------------------
     * 		 				 	ATTRIBUTES
     *-------------------------------------------------------------------*/

    $scope.selectedEntity = null;

    $scope.gridSelectedItems = [];

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
        '<img style="width: 20px; height: 20px; border: solid 1px #c9c9c9;" ng-src="{{row.entity.legenda}}"/>' +
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
        afterSelectionChange: function(row, event){
            $scope.selectedEntity = row.entity;
        },
        columnDefs: [
            {displayName:'Simbologia', field:'legenda', sortable:false, width: '120px', cellTemplate: IMAGE_LEGENDA},
            {displayName:'Título', field: 'titulo'},
            {displayName:'Nome', field:'nome'}
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

        var pageRequest = new PageRequest();
        pageRequest.size = 6;
        $scope.pageRequest = pageRequest;

        var order = new Order();
        order.direction = 'ASC';
        order.property = 'id';

        $scope.pageRequest.sort = new Sort();
        $scope.pageRequest.sort.orders = [ order ];

        $scope.listByFilters( null, pageRequest );
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
        $scope.listByFilters( filter, $scope.currentPage.pageable );
        $scope.showLoading = false;
    };

    /**
     * Função responsável por fechar a pop sem executar outras ações
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
    $scope.listByFilters = function( filter, pageRequest ) {

        $scope.showLoading = true;

        grupoCamadasService.listConfiguracoesCamadasByFilter( filter, null, pageRequest, {
            callback : function(result) {
                $scope.currentPage = result;
                $scope.currentPage.pageable.pageNumber++;//Para fazer o bind com o pagination
                $scope.showLoading = false;
                $scope.$apply();

                //Função responsável por marcar os registros que já estavam marcados antes da abertura da pop-up


                //Função responsável por retirar os registros que já estavam marcados antes da abertura da pop-up
                if (camadasSelecionadas) {
                    angular.forEach( camadasSelecionadas, function(data, index) {
                        var i = $scope.findName(data.nome, $scope.currentPage.content);
                        if (i > -1) {
                            $scope.currentPage.content.splice(i, 1);
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