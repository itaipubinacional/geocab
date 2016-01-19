'use strict';

/**
 *
 * @param $scope
 * @param $log
 * @param $location
 */
function AddAttributeImportPopUpController($scope, $injector, $modalInstance, $state, markerAttributes ) {


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


	$scope.markerAttributes = markerAttributes;

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

		$scope.markerAttributes.push({name: ''});

	};

	$scope.removeAttribute = function(index) {
		$scope.markerAttributes.splice(index, 1);
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
	$scope.close = function()
	{
		$scope.msg = null;
		$modalInstance.close({attributesByLayer: $scope.markerAttributes});
	};
};