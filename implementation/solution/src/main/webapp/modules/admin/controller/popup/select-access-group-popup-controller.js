'use strict';

/**
 * 
 * @param $scope
 * @param $log
 * @param $location
 */
function SelectAccessGroupPopUpController($scope, $modalInstance, $log, selectedGroups, $injector, $importService) {

	
	$importService("accessGroupService");
	
	
	/*-------------------------------------------------------------------
	 * 		 				 	EVENT HANDLERS
	 *-------------------------------------------------------------------*/

    $injector.invoke(AbstractCRUDController, this, {$scope: $scope});

    /**
     *  Handler que escuta toda vez que o usuário/programadamente faz o sorting na ng-grid.
     *  Quando o evento é disparado, configuramos o pager do spring-data
     *  e chamamos novamente a consulta, considerando também o estado do filtro (@see $scope.data.filter)
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
     * Variável para armazenar atributos do formulário que
     * não cabem em uma entidade. Ex.:
     * @filter - Filtro da consulta
     */
    $scope.data = { filter:null };

	/**
	 * 
	 */
	$scope.currentPage;

    /**
     *
     * @type {Array}
     */
	$scope.grupos = [];

    /**
     *
     * @type {null}
     */
    $scope.itensMarcados = [];

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
        afterSelectionChange: function (rowItem){
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
        enableRowSelection: true,
        columnDefs: [
            {displayName: 'Nome', field: 'name'},
            {displayName: 'Descrição', field: 'description'}
        ]
    };

	/*-------------------------------------------------------------------
	 * 		 				 	  NAVIGATIONS
	 *-------------------------------------------------------------------*/
	/**
	 * Método principal que faz o papel de front-controller da tela.
	 * Ele é invocado toda vez que ocorre uma mudança de URL (@see $stateChangeSuccess),
	 * quando isso ocorre, obtém o estado através do $state e chama o método inicial daquele estado.
	 * Ex.: /list -> changeToList()
	 *      /criar -> changeToInsert()
	 *
	 * Caso o estado não for encontrado, ele direciona para a listagem,
	 * apesar que o front-controller do angular não deixa digitar uma URL inválida.
	 */
	$scope.initialize = function() 
	{
        $scope.itensMarcados = selectedGroups.slice(0);

        var pageRequest = new PageRequest();
        pageRequest.size = 6;
        $scope.pageRequest = pageRequest;

		$scope.listGruposAcessosByFilter($scope.data.filter, $scope.pageRequest);


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
	 * Realiza a consulta de registros, consirando filtro, paginação e sorting.
	 * Quando ok, muda o estado da tela para list.
	 *
	 * @see data.filter
	 * @see currentPage
	 */
	$scope.listGruposAcessosByFilter = function( filtro, pageRequest ) {

        accessGroupService.listAccessGroupByFilters( filtro, pageRequest, {
			callback : function(result) {
				$scope.currentPage = result;
				$scope.currentPage.pageable.pageNumber++;//Para fazer o bind com o pagination
                $scope.$apply();

                $scope.showLoading = false;

                //Função responsável por retirar os registros que já estavam marcados antes da abertura da pop-up

                if ($scope.itensMarcados) {
                    angular.forEach( $scope.itensMarcados, function(data, index) {
                        var i = $scope.findByIdInArray($scope.currentPage.content, data);
                        if (i > -1) {
                            $scope.gridOptions.selectItem(i, true);
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

    /**
     *
     * @param branch
     */
	$scope.my_tree_handler = function(branch) 
	{
		$scope.currentEntity = branch;
	};

    /**
     *
     * @param formName
     * @returns {*}
     */
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
     * @param fechar
     */
	 $scope.close = function( fechar )
	 {
         if (fechar == false) {
             $modalInstance.close(fechar);
         } else {
             $modalInstance.close($scope.itensMarcados);
         }
	 };
};