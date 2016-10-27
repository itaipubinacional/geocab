'use strict';

/**
 * 
 * @param $scope
 * @param $log
 * @param $location
 */
function RefuseMarkerController($scope, $injector,$modalInstance, $state, $importService, motive ) {
	
	
	$importService("motiveService");

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
    $scope.NORMAL_STATE = "marker-moderation.normal";
    /**
     *
     */
    $scope.CONFIRM_STATE = "marker-moderation.confirm";

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
	
	/**
	 * 
	 */
	$scope.data = {'motive': null, 'description': ""};

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
		$scope.listMotives();
	};

	/*-------------------------------------------------------------------
	 * 		 				 	  BEHAVIORS
	 *-------------------------------------------------------------------*/
	
	/**
	 * 
	 */
    $scope.listMotives = function () {

        motiveService.listMotives( {
            callback: function (result) {
                $scope.motives = result;
                $scope.$apply();
            },
            errorHandler: function (message, exception) {
                $scope.msg = {type: "danger", text: message, dismiss: true};
                $scope.$apply();
            }
        });
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
    $scope.closeConfirm = function()
    {
        $scope.msg = null;
        $scope.currentEntity.nome = '';
        $scope.currentState = $scope.NORMAL_STATE;
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
	$scope.close = function() 
	{
		$scope.msg = null;
		$modalInstance.close(null);
	};
	
	$scope.refuse = function()
	{
		if ( !$scope.form('form_refuse_marker').$valid ){
			return;
		}
		
		$modalInstance.close($scope.data);
	};
	
};