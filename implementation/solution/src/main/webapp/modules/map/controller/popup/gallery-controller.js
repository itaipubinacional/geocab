'use strict';

/**
 *
 * @param $scope
 * @param $log
 * @param $location
 */
function GalleryPopUpController($scope, $modalInstance, $log, layer, attribute, attributesByLayer) {

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
    $scope.layer = layer;

    $scope.attribute = attribute;

    $scope.attributesByLayer = attributesByLayer;


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

    };

    $scope.onSuccess = function(files) {

      console.log(files);

      $scope.attribute.files = files;

        $scope.attributesByLayer[0].files = files;
    };


    /*-------------------------------------------------------------------
     * 		 				 	  BEHAVIORS
     *-------------------------------------------------------------------*/

    /**
     *
     */
    $scope.close = function(fechar)
    {
        // verifica se o usuário selecionou a opção de fechar ou selecionar na pop up
        if (fechar){
            $modalInstance.close();
        } else {
            $modalInstance.close($scope.attributesByLayer);
        }
    };
    
    
};