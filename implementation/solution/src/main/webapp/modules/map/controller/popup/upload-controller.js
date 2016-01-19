'use strict';

/**
 *
 * @param $scope
 * @param $log
 * @param $location
 */
function UploadPopUpController($scope, $modalInstance, $filter, $importService, $translate, layer, attribute, attributes) {


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

    angular.forEach($scope.attributes, function(attr, index){

      var photoAlbumId = false;
      var attributesByLayer = {};

      if(attr.type == 'PHOTO_ALBUM') {

        attributesByLayer = attr;
        $scope.attributesByLayer.push(attr);
      }

      if(attr.attribute && attr.attribute.type == 'PHOTO_ALBUM') {
        attr.attribute.markerAttribute = {id: attr.id};

        attributesByLayer = attr.attribute;

        attribute.files = attribute.files ? attribute.files : [];

        if(attr.photoAlbum){

          markerService.listPhotosByPhotoAlbumId(attr.photoAlbum.id, {
            callback: function (result) {

              if(result.content.length) {

                attributesByLayer.files = attributesByLayer.files ? attributesByLayer.files : [];

                angular.forEach(result.content, function (photo) {

                  var file = $filter('filter')(attributesByLayer.files, {id: photo.id})[0];
                  var index = attributesByLayer.files.indexOf(file);

                  //photo.src = photo.image;
                  //photo.name = photo.description;

                  if (attribute.removePhotosIds.indexOf(photo.id) == -1 && index == -1) {
                    attributesByLayer.files.push(photo);
                  }

                  if (index != -1) {
                    attributesByLayer.files[index].src = photo.image;
                  }

                  /*if (attribute.removePhotosIds.indexOf(photo.id) == -1) {
                    photo.src = photo.image;
                    photo.name = photo.description;
                    attributesByLayer.files.push(photo);
                  }*/

                });

              }
              $scope.$apply();
            },
            errorHandler: function (message, exception) {
              $scope.message = {type: "error", text: message};
              $scope.$apply();
            }
          });
        }

        $scope.attributesByLayer.push(attributesByLayer);
      }
    });

    if($scope.attribute.markerAttribute && $scope.attribute.markerAttribute.id) {

      $scope.setAttribute($scope.attribute);

    }
  };

  $scope.onSuccess = function (files) {

    $scope.attribute.files = files;
    $scope.$apply();

  };

  $scope.onError = function(msg){

    //console.log(msg);

    $scope.msg = {
      type: "danger",
      text: $translate(msg)
    };
    $scope.fadeMsg();

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

      //$scope.attribute.files = [];
      angular.forEach($scope.attribute.files, function(file){
        if(file.id) {
          file.image = null;
          file.src = null;
        }
      });

      markerService.findPhotoAlbumByAttributeMarkerId(attribute.markerAttribute.id, null, {

        callback: function (result) {

          //$scope.attribute.files = [];

          angular.forEach(result.content, function (photo) {

            var file = $filter('filter')($scope.attribute.files, {id: photo.id})[0];
            var index = $scope.attribute.files.indexOf(file);

            //photo.src  = photo.image;
            //photo.name = photo.description;

            if ($scope.attribute.removePhotosIds.indexOf(photo.id) == -1 && index == -1) {
              $scope.attribute.files.push(photo);
            }

            if (index != -1){
              $scope.attribute.files[index].src = photo.image;
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

  $scope.fadeMsg = function(){
    $("span.error").show();

    setTimeout(function () {
      $("span.error").fadeOut();
    }, 5000);
  };

};