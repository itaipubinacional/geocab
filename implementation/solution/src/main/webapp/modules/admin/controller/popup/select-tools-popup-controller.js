'use strict';

/**
 *
 * @param $scope
 * @param $modalInstance
 * @constructor
 */
function SelectToolsPopUpController( $scope, $modalInstance, selectedTools, $log, $importService ) {

    /*-------------------------------------------------------------------
     * 		 				 	EVENTS
     *-------------------------------------------------------------------*/
	
	$importService("accessGroupService");

    /*-------------------------------------------------------------------
     * 		 				 	ATTRIBUTES
     *-------------------------------------------------------------------*/

    $scope.selectedEntity = null;

    $scope.gridSelectedItems = [];

    /**
     * Variável para armazenar atributos do formulário que
     * não cabem em uma entidade. Ex.:
     * @filter - Filtro da consulta
     */
    $scope.filter = {
        filterText: ''
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

        $scope.list();
    };

    /**
     * Realiza a consulta de registros, considerando filtro, paginação e sorting.
     * Quando ok, muda o estado da tela para list.
     *
     * @see filterOptions.filter
     * @see currentPage
     */
    $scope.list = function( filter ) {

        $scope.showLoading = true;

        accessGroupService.listTools( {
            callback : function(result) {
                $scope.tools = result;
                $scope.showLoading = false;
                $scope.$apply();

                //Função responsável por marcar os registros que já estavam marcados antes da abertura da pop-up

                if (selectedTools != null) {
                    var items = selectedTools;
                    if (items.length > 0) {
                        angular.forEach( $scope.tools, function(data, index) {
                            data.ordem = null;
                            angular.forEach( items, function(item) {
                                if ( data.name == item.name ){
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


    /**
     * Configura o pageRequest conforme o componente visual pager
     * e chama o serviço de listagem, considerando o filtro corrente na tela.
     *
     * @see currentPage
     * @see filterOptions.filter
     */
    $scope.changeToPage = function( filter, pageNumber ) {
//        $scope.currentPage.pageable.page = pageNumber-1;
//        $scope.list();
        $scope.showLoading = false;
    };


    /**
     * Configurações gerais da ng-grid.
     * @see https://github.com/angular-ui/ng-grid/wiki/Configuration-Options
     */
    $scope.gridOptions = {
        data: 'tools',
        multiSelect: true,
        enableSorting: true,
        useExternalSorting: false,
        headerRowHeight: 45,
        showSelectionCheckbox: true,
        selectedItems: $scope.gridSelectedItems,
        rowHeight: 50,
        filterOptions: $scope.filter,
        columnDefs: [
            {displayName:'Descrição', field:'description'},
            {displayName:'Nome', field: 'name'}

        ]
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


};