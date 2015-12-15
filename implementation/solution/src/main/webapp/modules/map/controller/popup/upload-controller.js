'use strict';

/**
 *
 * @param $scope
 * @param $log
 * @param $location
 */
function UploadPopUpController($scope, $modalInstance, $filter, $importService, layer, attribute, attributesByLayer) {


  $importService("markerService");


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

  $scope.removePhotosIds = [];
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

    if($scope.attribute.markerAttribute && $scope.attribute.markerAttribute.id) {

      $scope.setAttribute($scope.attribute);

    }
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
        if(file.id)
          $scope.removePhotosIds.push(file.id);
        $scope.attribute.files.splice(i, 1);
      }
    }
  };

  $scope.clearFiles = function () {
    if ($scope.attribute.files.length) {
      var i = $scope.attribute.files.length;
      while (i--) {
        if($scope.attribute.file.id)
          $scope.removePhotosIds.push($scope.attribute.file[i].id);
        $scope.attribute.files.splice(i, 1);
      }
    }
  };

  $scope.setAttribute = function (attribute) {

    $scope.attribute = attribute;

    var files = attribute.files ? attribute.files : [];

    markerService.findPhotoAlbumByAttributeMarkerId(attribute.markerAttribute.id, null, {

      callback: function (result) {

        $scope.attribute.files = [];


        angular.forEach(result.content, function (photo) {

          //photo.delete = false;

          /*if(!files.length)
            photo.delete = true;
          */
          if($filter('filter')(files, {id: photo.id})[0]) {
            photo.delete = false;
          } else {
            photo.delete = true;
          }

          if(!files.length)
            photo.delete = false;

          photo.src = photo.image;
          photo.name = photo.description;
          $scope.attribute.files.push(photo);

        });

        //$scope.attribute = $scope.attribute;

        $scope.$apply();

      },
      errorHandler: function (message, exception) {
        $scope.message = {type: "error", text: message};
        $scope.$apply();
      }

    });

  };


  /*-------------------------------------------------------------------
   * 		 				 	  BEHAVIORS
   *-------------------------------------------------------------------*/

  /**
   *
   */
  $scope.close = function () {

    angular.forEach($scope.attribute.files, function(file){
        file.image = null;
    });

    $modalInstance.close($scope.removePhotosIds);

  };

};