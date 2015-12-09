'use strict';

/**
 *
 * @param $scope
 * @param $log
 * @param $location
 */
function ImgPopUpController($scope, $modalInstance, $log, attributesByMarker, $importService) {

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
  $scope.currentEntity;


  $scope.currentPhoto = {};

  $scope.photoIndex = 0;

  $scope.attributes = [];

  /**
   *
   */
  $scope.photos = [];

  $scope.currentAttribute = {};

  $scope.pageable = {
    size: 5,
      page: 0,
      sort: {//Sort
      orders: [
        {direction: 'DESC', property: 'created'}
      ]
    }
  };

  /*-------------------------------------------------------------------
   * 		 				 	  BEHAVIORS
   *-------------------------------------------------------------------*/

  $scope.setCurrentPhoto = function (photo, index) {

    $scope.photoIndex = index;

    markerService.findPhotoById(photo.id, {
      callback: function (result) {
        console.log(result);
        $scope.currentPhoto = result;
        $scope.$apply();
      },
      errorHandler: function (message, exception) {
        $scope.message = {type: "error", text: message};
        $scope.$apply();
      }
    });


  };

  $scope.setAttribute = function (attribute, reload) {

    if(reload) {
      markerService.listPhotosByPhotoAlbumId(attribute.content[0].photoAlbum.id, $scope.pageable, {

        callback: function (result) {

          $scope.currentAttribute = result;

          $scope.setCurrentPhoto(result.content[0], 0);
          $scope.$apply();

        },
        errorHandler: function (message, exception) {
          $scope.message = {type: "error", text: message};
          $scope.$apply();
        }

      });
    } else {

      $scope.currentAttribute = attribute;
      $scope.setCurrentPhoto(attribute.content[0], 0);
    }

  };

  $scope.nextPhoto = function(){

    if($scope.currentAttribute.content[$scope.photoIndex + 1]){

      $scope.setCurrentPhoto($scope.currentAttribute.content[$scope.photoIndex + 1], $scope.photoIndex + 1);

    }

  };

  $scope.previousPhoto = function(){

    if($scope.photoIndex - 1 >= 0 && $scope.currentAttribute.content[$scope.photoIndex - 1]){

      $scope.setCurrentPhoto($scope.currentAttribute.content[$scope.photoIndex - 1], $scope.photoIndex - 1);

    }

  };

  $scope.nextPage = function(){


  };

  $scope.previousPage = function(){

  };

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

    $scope.attributesByMarker = attributesByMarker;

    angular.forEach(attributesByMarker[0].marker.markerAttribute, function (markerAttribute, index) {
      if (markerAttribute.attribute.type == 'PHOTO_ALBUM') {

        if (markerAttribute.photoAlbum != null) {

          console.log(markerAttribute.photoAlbum.id);

          markerService.listPhotosByPhotoAlbumId(markerAttribute.photoAlbum.id, $scope.pageable, {

            callback: function (result) {

              $scope.attributes.push(result);

              if(!$scope.currentAttribute.content) {
                $scope.setAttribute(result, false);
                $scope.$apply();
              }
            },
            errorHandler: function (message, exception) {
              $scope.message = {type: "error", text: message};
              $scope.$apply();
            }

          });

        }
      }
    });

  };


  /**
   *
   */
  $scope.close = function () {
    $scope.msg = null;
    $modalInstance.close(null);
  };


};