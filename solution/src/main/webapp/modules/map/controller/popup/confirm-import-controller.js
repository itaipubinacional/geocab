'use strict';

function ConfirmImportPopUpController($scope, $modalInstance, $importService, $translate, markerAttributes) {



  /*-------------------------------------------------------------------
   * 		 				 	ATTRIBUTES
   *-------------------------------------------------------------------*/

  /**
   *
   */
  $scope.markerAttributes = markerAttributes;

  /*-------------------------------------------------------------------
   * 		 				 	  BEHAVIORS
   *-------------------------------------------------------------------*/

  /**
   *
   */
  $scope.close = function( continueImport ) {

    $modalInstance.close( continueImport );

  };


};
