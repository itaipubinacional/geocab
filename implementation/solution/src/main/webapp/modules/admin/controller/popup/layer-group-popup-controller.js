'use strict';

/**
 * 
 * @param $scope
 * @param $log
 * @param $location
 */
function LayerGroupPopUpController($scope, $injector,$modalInstance, layerGroups, item, $state, $translate) {


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
    $scope.NORMAL_STATE = "layer-group.normal";
    /**
     *
     */
    $scope.CONFIRM_STATE = "layer-group.confirm";

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

		if( item )
		{
			$scope.currentEntity = new LayerGroup();
			$scope.currentEntity.id = item.id;
			$scope.currentEntity.name = item.name;
		}
		else
		{
			$scope.currentEntity = new LayerGroup();
			$scope.currentEntity.layers = null;
			$scope.currentEntity.layerGroup = null;
			$scope.currentEntity.layerGroupUpper = null;
		}

        $scope.currentState = $scope.NORMAL_STATE;
	};

	/*-------------------------------------------------------------------
	 * 		 				 	  BEHAVIORS
	 *-------------------------------------------------------------------*/
	
	$scope.listLayersGroup = function(layersGroup, layerGroupUpper)
	{
		
		if( layersGroup == null )
		{
			layersGroup = [];
		}
		
		for (var i = 0; i < layersGroup.length; i++) 
		{
			var data = new Object();
			data.children = [];

			if( layerGroupUpper )
			{
				if( layersGroup[i].name.toUpperCase() == $scope.currentEntity.name.toUpperCase() && layersGroup[i].id != $scope.currentEntity.id )
				{
					isEqual =  true;
				}
				
				layerGroupUpper.children.push(data);
			}
				
			$scope.listLayersGroup(layersGroup[i].layerGroup, data);
		}
		
		return isEqual;
	};


	/**
	 * Sai da popup
	 */
	$scope.closePopup = function () 
	{
		if ( !$scope.form().$valid ) 
		{
			$scope.msg = {type:"danger", text: $translate("layer-group-popup.Please-enter-a-name-for-the-group") , dismiss:true};
			return;
		}
		
		$scope.listLayersGroup(layerGroups, null);

		if( layerGroups )
		{

			for( var i= 0; i < layerGroups.length; i++)
			{
				if( layerGroups[i].name.toUpperCase() == $scope.currentEntity.name.toUpperCase() && layerGroups[i].id != $scope.currentEntity.id )
				{
					$scope.msg = {type:"danger", text:  $translate("layer-group-popup.Already-have-a-group-with-this-name-at-the-same-level") , dismiss:true};
					return;
				}
			}
			
			if( isEqual == true )
			{
				$scope.msg = {type:"warning", text: $translate("layer-group-popup.Already-exists-a-group-with-this-name-on-another-level-Want-to-save-anyway-?"), dismiss:true};
                $scope.currentState = $scope.CONFIRM_STATE;
				isEqual = false;
				return;
			}
		}
		$modalInstance.close($scope.currentEntity);
	};

    /**
     * Confirma o nome do grupo e fecha popup
     */
    $scope.closePopupConfirm = function ()
    {
        if (!$scope.form().$valid) {
            $scope.msg = {type: "danger", text: $translate("layer-group-popup.Please-enter-a-name-for-the-group"), dismiss: true};
            return;
        }

        $modalInstance.close($scope.currentEntity);
    }

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
        $scope.currentEntity.name = '';
        $scope.currentState = $scope.NORMAL_STATE;
    };

	/**
	 *
	 */
	$scope.close = function() 
	{
		$scope.msg = null;
		$modalInstance.close(null);
	};
};