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

  $scope.setAttribute = function (attribute) {

    $scope.currentAttribute = attribute;
    /*angular.forEach(attribute.photoAlbumIds, function (photoAlbumId) {

      markerService.listPhotosByPhotoAlbumId(photoAlbumId, $scope.pageable, {

        callback: function (result) {

          angular.forEach(result.content, function (photo, index) {
            if (index == 0)
              $scope.setCurrentPhoto(photo);
            $scope.photos.push(photo);
          });

          $scope.$apply();
        },
        errorHandler: function (message, exception) {
          $scope.message = {type: "error", text: message};
          $scope.$apply();
        }

      });


    });*/
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

    angular.forEach(attributesByMarker, function (markerAttribute, index) {
      if (markerAttribute.attribute.type == 'PHOTO_ALBUM') {

        //$scope.attributes[index] = markerAttribute.attribute;

        angular.forEach(markerAttribute.marker.markerAttribute, function (attribute, index) {

          if (attribute.photoAlbum != null) {

            markerService.listPhotosByPhotoAlbumId(attribute.photoAlbum.id, $scope.pageable, {

              callback: function (result) {

                $scope.attributes.push(result);

                //if (index == 0) {
                  $scope.setAttribute(result);
                  $scope.$apply();
                //}

              },
              errorHandler: function (message, exception) {
                $scope.message = {type: "error", text: message};
                $scope.$apply();
              }

            });

          }
        });

      }
    });

    //$scope.setAttribute($scope.attributes[0]);

  };

  $scope.setCurrentPhoto = function (photo) {

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
  /*-------------------------------------------------------------------
   * 		 				 	  BEHAVIORS
   *-------------------------------------------------------------------*/

  /**
   *
   */
  $scope.close = function () {
    $scope.msg = null;
    $modalInstance.close(null);
  };


};