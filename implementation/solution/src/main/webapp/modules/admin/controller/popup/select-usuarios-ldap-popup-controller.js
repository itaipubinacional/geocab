'use strict';

/**
 * 
 * @param $scope
 * @param $log
 * @param $location
 */
function SelectUsuariosPopUpController($scope, $modalInstance , usuariosSelecionados, $log) {


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
	$scope.data = {
	        filter: ''
	      };

    /**
     *
     * @type {boolean}
     */
    $scope.showLoading = false;
	/*-------------------------------------------------------------------
	 * 		 				 	  NAVIGATIONS
	 *-------------------------------------------------------------------*/
	/**
	 * Main method that makes the role of front-controller of the screen.
	 * He is invoked whenever there is a change of URL (@see $stateChangeSuccess),
	 * When this occurs, gets the State via the $state and calls the initial method of that State.
	 * Ex.: /list -> changeToList()
	 *      /create -> changeToInsert()
	 *
	 * If the State is not found, he directs to the listing,
	 * Although the front controller of angle won't let enter an invalid URL.
	 */
	$scope.initialize = function() 
	{
        $scope.numeroDeRegistros = 0;
        $scope.usuariosSelecionados = usuariosSelecionados;
	};

	/*-------------------------------------------------------------------
	 * 		 				 	  BEHAVIORS
	 *-------------------------------------------------------------------*/
	
	/**
     * Performs the query records, considering filter, paging and sorting.
     * When ok, change the State of the screen to list.
     *
     * @see data.filter
     * @see currentPage
     */
    $scope.listUsuariosByFilters = function( filter ) {

        $scope.showLoading = true;

        usuarioService.listUsuariosByFilters( filter, {
            callback : function(result) {
            	$scope.usuarios = result;
                $scope.showLoading = false;

                //Function responsible for marking the records that were already tagged prior to the opening of pop-up
                if ($scope.usuariosSelecionados) {
                    angular.forEach( $scope.usuariosSelecionados, function(data, index) {
                        var i = $scope.findUsername(data.username, $scope.usuarios);
                        if (i > -1) {
                            $scope.usuarios.splice(i, 1);
                        }
                    });
                }

                $scope.$apply();

            },
            errorHandler : function(message, exception) {
                $scope.message = {type:"error", text: message};
                $scope.showLoading = false;
                $scope.$apply();
            }
        });
    };

    $scope.filterSelectedUsers = function () {
        var index;
        for (var i = 0; i < $scope.usuariosSelecionados.length; i++) {
            index = $scope.usuarios.indexOf($scope.usuariosSelecionados[i]);
            if (index > -1) {
                $scope.usuarios.splice(index, 1);
            };
        };
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

    $scope.findUsername = function(string, list) {
        for (var index = 0; index < list.length; index++) {
            if (list[index].username == string) return index
        }
        return -1;
    }
    
    var IMAGE_LEGENDA = '<div align="center" class="ngCellText" ng-cell-text ng-class="col.colIndex()">' +
	'<img style="width: 20px; height: 20px; border: solid 1px #c9c9c9;" ng-src="{{row.entity.legenda}}"/>' +
	'</div>';

	/**
     * Handler that captures the events marking
     * da grid
     * @param rows
     */
    $scope.toogleSelection = function(row, event) {
    	$scope.currentEntity = row.entity;
        $scope.selectedEntity = row.entity;
    };

    $scope.gridOptions = {
        data: 'usuarios',
        multiSelect: false,
        headerRowHeight: 45,
        filterOptions: $scope.filterOptions,
        rowHeight: 45,
        enableRowSelection: true,
        afterSelectionChange: function (row, event) {
            $scope.toogleSelection(row, event);
        },
        columnDefs: [
            {displayName:'Nome Completo', field:'nomeCompleto'},
            {displayName:'Nome de usuário', field:'username', width: '120px'},
            {displayName:'E-mail', field: 'email', width:'280px'}
        ]
    };

    /**
	 *
	 */
	$scope.close = function( fechar )
	{
		$scope.msg = null;

        // checks whether the user has selected the option to close or select from the pop up
        if (fechar){
            $modalInstance.close();
        } else {
            $modalInstance.close($scope.currentEntity);
        }

	};
};