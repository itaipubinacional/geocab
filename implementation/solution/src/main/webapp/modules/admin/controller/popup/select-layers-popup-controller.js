'use strict';

/**
 * 
 * @param $scope
 * @param $log
 * @param $location
 */
function SelectLayersPopUpController($scope, $modalInstance, currentEntity, $log, $location) {


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
	 * Main method that makes the role of front-controller of the screen.
	 * He is invoked whenever there is a change of URL (@see $stateChangeSuccess),
	 * When this occurs, gets the State via the $state and calls the initial method of that State.
	 * Ex.: /list -> changeToList()
	 *      /criar -> changeToInsert()
	 *
	 * If the State is not found, he directs to the listing,
	 * Although the front controller of angle won't let enter an invalid URL.a.
	 */
	$scope.initialize = function() 
	{
        $scope.numeroDeRegistros = 0;

        $scope.currentEntity = currentEntity;
        $scope.selectedItemsGrid = [{
            nome:currentEntity.nome,
            titulo:currentEntity.titulo
        }]
		$scope.listExternalLayerByFilters(currentEntity.dataSource);
	};

	/*-------------------------------------------------------------------
	 * 		 				 	  BEHAVIORS
	 *-------------------------------------------------------------------*/
	
	/**
     * Performs the query records, consirando filter, paging and sorting.
     * When ok, change the State of the screen to list.
     *
     * @see data.filter
     * @see currentPage
     */
    $scope.listExternalLayerByFilters = function( dataSource ) {
    	
        layerGroupService.listExternalLayersByFilters( dataSource, {
            callback : function(result) {
            	$scope.layers = result;
                $scope.numberOfRegisters = result.length;
                $scope.showLoading = false;
                $scope.$apply();

                //Function responsible for marking the records that were already tagged prior to the opening of pop-up

                if (currentEntity.name) {
                    angular.forEach( $scope.layers, function(data, index) {
                        if ( data.name == currentEntity.name ){
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
	 * Close popup
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
	'<img style="width: 20px; height: 20px; border: solid 1px #c9c9c9;" ng-src="{{row.entity.legend}}"/>' +
	'</div>';

	/**
     * Handler that captures the events marking
     * the grid
     * @param rows
     */
    $scope.toogleSelection = function(row, event) {
    	$scope.currentEntity = row.entity;
        $scope.selectedEntity = row.entity;
    };

    $scope.gridOptions = {
        data: 'layers',
        multiSelect: false,
        headerRowHeight: 45,
        filterOptions: $scope.filterOptions,
        rowHeight: 45,
        enableRowSelection: true,
        afterSelectionChange: function (row, event) {
            $scope.toogleSelection(row, event);
        },
        columnDefs: [
            {displayName:'Simbologia', field:'legend', sortable:false, width: '100px', cellTemplate: IMAGE_LEGENDA},       
            {displayName:'Título', field: 'title', width:'280px'},
            {displayName:'Nome', field:'name'}
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