'use strict';

/**
 * 
 * @param $scope
 * @param $log
 * @param $location
 */
function AddAttributePopUpController($scope, $injector,$modalInstance, $state, attributes ) {


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
	 * Main method that makes the role of front-controller of the screen.
     * He is invoked whenever there is a change of URL (@see $stateChangeSuccess),
     * When this occurs, gets the State via the $state and calls the initial method of that State.
     *
     * If the State is not found, he directs to the listing,
     * Although the front controller of Angular won't let enter an invalid URL.
	 */
	$scope.initialize = function() 
	{
		$scope.currentEntity = new Attribute();
	};

	/*-------------------------------------------------------------------
	 * 		 				 	  BEHAVIORS
	 *-------------------------------------------------------------------*/

	$scope.addAttribute = function () {
		
		if( !$scope.form('form_add_attribute').$valid ){
			
			return;
		}
		
		if(!$scope.currentEntity.required) $scope.currentEntity.required = false;
		
		attributes.push($scope.currentEntity);
		$scope.close();
	}

	/**
	 * Close popup
	 */
	$scope.fechaPopup = function () 
	{
		if ( !$scope.form().$valid ) 
		{
			$scope.msg = {type:"danger", text: "Por favor digite um nome para o grupo", dismiss:true};
			return;
		}
		
		$scope.listGruposCamadas(grupos, null);

		if( grupos )
		{

			for( var i= 0; i < grupos.length; i++)
			{
				if( grupos[i].nome.toUpperCase() == $scope.currentEntity.nome.toUpperCase() && grupos[i].id != $scope.currentEntity.id )
				{
					$scope.msg = {type:"danger", text: "Já possui um grupo com este nome no mesmo nível", dismiss:true};
					return;
				}
			}
			
			if( isEqual == true )
			{
				$scope.msg = {type:"warning", text: "Já existe um grupo com este nome em outro nível. Deseja salvar mesmo assim?", dismiss:true};
                $scope.currentState = $scope.CONFIRM_STATE;
				isEqual = false;
				return;
			}
		}
		$modalInstance.close($scope.currentEntity);
	};

    /**
     * Confirms the name of the Group and close popup
     */
    $scope.fechaPopupConfirm = function ()
    {
        if (!$scope.form().$valid) {
            $scope.msg = {type: "danger", text: "Por favor digite um nome para o grupo", dismiss: true};
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
        $scope.currentEntity.nome = '';
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