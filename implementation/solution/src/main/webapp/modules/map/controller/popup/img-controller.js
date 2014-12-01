'use strict';

/**
 *
 * @param $scope
 * @param $log
 * @param $location
 */
function ImgPopUpController($scope, $modalInstance, $log, img) {

    /*-------------------------------------------------------------------
     * 		 				 	ATTRIBUTES
     *-------------------------------------------------------------------*/

    /**
     *
     * @type {null}
     */
    $scope.msg = null;

    /**
     *
     */
    $scope.currentEntity;

    /**
     *
     */
    $scope.features = [];


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
        $scope.img = img;
    };

    /*-------------------------------------------------------------------
     * 		 				 	  BEHAVIORS
     *-------------------------------------------------------------------*/

    /**
     *
     */
    $scope.close = function()
    {
        $scope.msg = null;
        $modalInstance.close(null);
    };
    
    
};