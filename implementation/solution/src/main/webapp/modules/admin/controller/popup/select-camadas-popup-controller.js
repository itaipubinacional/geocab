'use strict';

/**
 * 
 * @param $scope
 * @param $log
 * @param $location
 */
function SelectCamadasPopUpController($scope, $modalInstance, currentEntity, $log, $location) {


	$scope.msg = null;

	/*-------------------------------------------------------------------
	 * 		 				 	ATTRIBUTES
	 *-------------------------------------------------------------------*/

	/**
	 * 
	 */
	$scope.currentEntity;

    /**
     *
     * @type {null}
     */
    $scope.selectedEntity = null;
	
	/**
	 * 
	 */
	$scope.filterOptions = {
	        filterText: ''
	      };

    /**
     *
     * @type {boolean}
     */
    $scope.showLoading = true;
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
        $scope.numeroDeRegistros = 0;

        $scope.currentEntity = currentEntity;
        $scope.selectedItemsGrid = [{
            nome:currentEntity.nome,
            titulo:currentEntity.titulo
        }]
		$scope.listCamadasExternasByFilters(currentEntity.fonteDados);
	};

	/*-------------------------------------------------------------------
	 * 		 				 	  BEHAVIORS
	 *-------------------------------------------------------------------*/
	
	/**
     * Realiza a consulta de registros, consirando filtro, paginação e sorting.
     * Quando ok, muda o estado da tela para list.
     *
     * @see data.filter
     * @see currentPage
     */
    $scope.listCamadasExternasByFilters = function( fonteDados ) {
        grupoCamadasService.listCamadasExternasByFilters( fonteDados, {
            callback : function(result) {
            	$scope.camadas = result;
                $scope.numeroDeRegistros = result.length;
                $scope.showLoading = false;
                $scope.$apply();

                //Função responsável por marcar os registros que já estavam marcados antes da abertura da pop-up

                if (currentEntity.nome) {
                    angular.forEach( $scope.camadas, function(data, index) {
                        if ( data.nome == currentEntity.nome ){
                            $scope.gridOptions.selectRow(index, true);
                        }

                    });
                }

            },
            errorHandler : function(message, exception) {
                $scope.message = {type:"error", text: message};
                $scope.showLoading = false;
                $scope.$apply();
            }
        });
    };


	/**
	 * Sai da popup
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


    
    var IMAGE_LEGENDA = '<div align="center" class="ngCellText" ng-cell-text ng-class="col.colIndex()">' +
	'<img style="width: 20px; height: 20px; border: solid 1px #c9c9c9;" ng-src="{{row.entity.legenda}}"/>' +
	'</div>';

	/**
     * Handler que captura os eventos de marcação
     * da grid
     * @param rows
     */
    $scope.toogleSelection = function(row, event) {
    	$scope.currentEntity = row.entity;
        $scope.selectedEntity = row.entity;
    };

    $scope.gridOptions = {
        data: 'camadas',
        multiSelect: false,
        headerRowHeight: 45,
        filterOptions: $scope.filterOptions,
        rowHeight: 45,
        enableRowSelection: true,
        afterSelectionChange: function (row, event) {
            $scope.toogleSelection(row, event);
        },
        columnDefs: [
            {displayName:'Simbologia', field:'legenda', sortable:false, width: '100px', cellTemplate: IMAGE_LEGENDA},       
            {displayName:'Título', field: 'titulo', width:'280px'},
            {displayName:'Nome', field:'nome'}
        ]
    };

    /**
	 *
	 */
	$scope.close = function( fechar )
	{
		$scope.msg = null;

        // verifica se o usuário selecionou a opção de fechar ou selecionar na pop up
        if (fechar){
            $modalInstance.close();
        } else {
            $modalInstance.close($scope.currentEntity);
        }

	};
};