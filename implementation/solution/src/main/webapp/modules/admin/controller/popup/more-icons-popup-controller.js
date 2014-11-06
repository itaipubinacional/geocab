'use strict';

/**
 * 
 * @param $scope
 * @param $log
 * @param $location
 */
function MorePopupController($scope, $injector,$modalInstance, $state, currentEntity, $importService ) {

    $importService("layerGroupService");

	$scope.msg = null;

	/*-------------------------------------------------------------------
	 * 		 				 	ATTRIBUTES
	 *-------------------------------------------------------------------*/

    /*-------------------------------------------------------------------
     * 		 				 	ATTRIBUTES
     *-------------------------------------------------------------------*/
    //STATES
    /**
     *
     */
    $scope.NORMAL_STATE = "grupo-camadas.normal";
    /**
     *
     */
    $scope.CONFIRM_STATE = "grupo-camadas.confirm";

    /**
     *
     */
	$scope.currentEntity;

    /**
     *
     */
    $scope.currentState;

    /**
     *
     * @type {boolean}
     */
	var isEqual = false;

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
		layerGroupService.listLayersIcons({
            callback: function (result) {
            
            	var layers = [];
            	
            	angular.forEach(result, function(layer, index){
            		var allowSave = true;
            		switch(layer){
	            		case 'default_yellow.png':
	            			allowSave = false;
	            			break;
	            		case 'default_white.png':
	            			allowSave = false;
	            			break;
	            		case 'default_red.png':
	            			allowSave = false;
	            			break;
	            		case 'default_pink.png':
	            			allowSave = false;
	            			break;
	            		case 'default_green.png':
	            			allowSave = false;
	            			break;
	            		case 'default_blue.png':
	            			allowSave = false;
	            			break;
            		}
            		
            		if(allowSave)
            			layers.push(layer);
            	});
            	
            	$scope.layerIcons = layers;
                $scope.$apply();
            },
            errorHandler: function (message, exception) {
                $scope.msg = {type: "danger", text: message, dismiss: true};
                $scope.$apply();
            }
        });
		
		$scope.currentEntity = currentEntity;
	};

	/*-------------------------------------------------------------------
	 * 		 				 	  BEHAVIORS
	 *-------------------------------------------------------------------*/


	/**
	 * 
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
     */
    $scope.closeConfirm = function()
    {
        $scope.msg = null;
        $scope.currentEntity.nome = '';
        $scope.currentState = $scope.NORMAL_STATE;
    };

	/**
	 *
	 */
	$scope.close = function() 
	{
		currentEntity = $scope.currentEntity;
		$scope.msg = null;
		$modalInstance.close(null);
	};
};