'use strict';

/**
 * 
 * @param $scope
 * @param $log
 * @param $location
 */
function AddAttributeImportPopUpController($scope, $injector,$modalInstance, $state, attributes ) {


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

	$scope.attributes = [
		{
			"name": "Atributo 1",
			"type": 'TEXT',
			"required": true,
			"showAttribute": true,
		},
		{
			"name": "Atributo 2",
			"type": 'TEXT',
			"required": true,
			"showAttribute": true,
		}
		];

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

		$scope.attributes.push({name: ''});

	};

	$scope.removeAttribute = function(index) {
		$scope.attributes.splice(index, 1);
	};

	/**
	 * Close popup
	 */
	$scope.fechaPopup = function () 
	{
		$modalInstance.close();
	};

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