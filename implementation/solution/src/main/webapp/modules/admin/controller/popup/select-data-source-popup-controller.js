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
     * Configurações gerais da ng-grid.
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
     * Realiza a consulta ao exibir a pop-up
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
     * Configura o pageRequest conforme o componente visual pager
     * e chama o serviço de listagem, considerando o filtro corrente na tela.
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
     * Função responsável por fechar a pop sem executar outras ações
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
	 * Realiza a consulta de registros, consirando filtro, paginação e sorting.
	 * Quando ok, muda o estado da tela para list.
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

                //Função responsável por marcar os registros que já estavam marcados antes da abertura da pop-up
                var item = fonteDadoSelecionado;
                if (item) {
                    angular.forEach( $scope.currentPage.content, function(data, index) {
                        if ( data.id == item.id ){
                            $scope.gridOptions.selectRow(index, true);
                        }
                    });
                }

                //Função responsável por marcar os items que já estavam marcados antes do método $scope.listGruposAcessosByFilter ser chamado
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