'use strict';

/**
 *.
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
     * Método principal que faz o papel de front-controller da tela.
     * Ele é invocado toda vez que ocorre uma mudança de URL (@see $stateChangeSuccess),
     * quando isso ocorre, obtém o estado através do $state e chama o método inicial daquele estado.
     *
     * Caso o estado não for encontrado, ele direciona para a listagem,
     * apesar que o front-controller do angular não deixa digitar uma URL inválida.
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