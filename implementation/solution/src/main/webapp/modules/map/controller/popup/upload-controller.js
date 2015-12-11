'use strict';

/**
 *
 * @param $scope
 * @param $log
 * @param $location
 */
function UploadPopUpController($scope, $modalInstance, $log, $filter, layer, attribute, attributesByLayer) {

  /*-------------------------------------------------------------------
   * 		 				 	ATTRIBUTES
   *-------------------------------------------------------------------*/

  /**
   *
   * @type {null}
   */
  $scope.msg = null;

  $scope.filter = $filter;
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
  $scope.initialize = function () {
    console.log('initialize');
  };

  $scope.onSuccess = function (files) {

    console.log(files);

    $scope.attribute.files = files;

  };

  $scope.removeChecked = function () {

    var i = $scope.attribute.files.length;

    while (i--) {
      var file = $scope.attribute.files[i];

      if (file.checked) {
        $scope.attribute.files.splice(i, 1);
      }
    }
  };

  $scope.clearFiles = function () {
    if ($scope.attribute.files.length) {
      var i = $scope.attribute.files.length;
      while (i--) {
        $scope.attribute.files.splice(i, 1);
      }
    }
  };

  $scope.setAttribute = function (attribute) {
    $scope.attribute = attribute;
  };


  /*-------------------------------------------------------------------
   * 		 				 	  BEHAVIORS
   *-------------------------------------------------------------------*/

  /**
   *
   */
  $scope.close = function (fechar) {
    // verifica se o usuário selecionou a opção de fechar ou selecionar na pop up
    if (fechar) {
      $modalInstance.close();
    } else {
      $modalInstance.close($scope.attributesByLayer);
    }
  };


};