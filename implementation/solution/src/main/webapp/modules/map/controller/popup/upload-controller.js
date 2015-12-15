'use strict';

/**
 *
 * @param $scope
 * @param $log
 * @param $location
 */
function UploadPopUpController($scope, $modalInstance, $filter, $importService, layer, attribute, attributes) {


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

  $scope.attributes = attributes;

  $scope.attributesByLayer = [];

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

    angular.forEach($scope.attributes, function(attr){
      if(attr.type == 'PHOTO_ALBUM')
        $scope.attributesByLayer.push(attr);

      if(attr.attribute && attr.attribute.type == 'PHOTO_ALBUM') {
        attr.attribute.markerAttribute = {id: attr.id};
        $scope.attributesByLayer.push(attr.attribute);
      }
    });

    if($scope.attribute.markerAttribute && $scope.attribute.markerAttribute.id) {

      $scope.setAttribute($scope.attribute);

    }
  };

  $scope.onSuccess = function (files) {

    console.log(files);

    $scope.attribute.files = files;

    $scope.$apply();

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

    $scope.attribute.removePhotosIds = $scope.removePhotosIds;
  };

  $scope.clearFiles = function () {
    if ($scope.attribute.files.length) {
      var i = $scope.attribute.files.length;
      while (i--) {
        if($scope.attribute.files[i].id) {
          $scope.removePhotosIds.push($scope.attribute.files[i].id);
        }
        $scope.attribute.files.splice(i, 1);
      }
      $scope.attribute.removePhotosIds = $scope.removePhotosIds;
    }
  };

  $scope.setAttribute = function (attribute) {

    $scope.attribute = attribute;

    $scope.attribute.removePhotosIds = attribute.removePhotosIds ? attribute.removePhotosIds : [];

    if(attribute.markerAttribute && attribute.markerAttribute.id) {

      $scope.attribute.files = [];

      markerService.findPhotoAlbumByAttributeMarkerId(attribute.markerAttribute.id, null, {

        callback: function (result) {

          $scope.attribute.files = [];

          angular.forEach(result.content, function (photo) {

            if ($scope.attribute.removePhotosIds.indexOf(photo.id) == -1) {
              photo.src = photo.image;
              photo.name = photo.description;
              $scope.attribute.files.push(photo);
            }

          });

          $scope.$apply();

        },
        errorHandler: function (message, exception) {
          $scope.message = {type: "error", text: message};
          $scope.$apply();
        }
      });
    }

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

    $modalInstance.close($scope.attributes);

  };

};