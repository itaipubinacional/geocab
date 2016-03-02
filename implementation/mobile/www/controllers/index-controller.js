(function(angular) {
  'use strict';

  /**
   *
   * @param $scope
   * @param $state
   */
  angular.module('application')
    .controller('IndexController', function($rootScope, $scope, $translate, $state, $document, $importService, $ionicGesture,
      $ionicPopup, $ionicSideMenuDelegate, $timeout, $cordovaDatePicker, $cordovaGeolocation,
      $filter, $log, $location, $ionicNavBarDelegate, $cordovaCamera, $ionicLoading,
      $cordovaToast, $ionicModal) {

      var options = {
        date: new Date(),
        mode: 'date', // or 'time'
        minDate: new Date() - 10000,
        allowOldDates: true,
        allowFutureDates: false,
        doneButtonLabel: 'DONE',
        doneButtonColor: '#F2F3F4',
        cancelButtonLabel: 'CANCEL',
        cancelButtonColor: '#000000'
      };

      $scope.showDatePicker = function(attribute) {
        $cordovaDatePicker.show(options).then(function(date) {
          var month = date.getMonth() > 10 ? date.getMonth() + 1 : '0' + (date.getMonth() + 1);
          attribute.value = date.getDate() + '/' + month + '/' + date.getFullYear();
          //alert(date);
        });
      };


      $scope.saveMarker = function(form) {

        if (!form.$valid) {

          $scope.isFormSubmit = true;

        } else {

          $scope.isFormSubmit = false;

          if ($scope.currentEntity.id) {

            var isValid = true;

            angular.forEach($scope.currentEntity.markerAttribute, function(attribute, index) {

              if (attribute.type == 'PHOTO_ALBUM' && attribute.required && attribute.photoAlbum == null) {

                $scope.selectedPhotoAlbumAttribute = attribute;
                $state.go($scope.SHOW_GALLERY);

                isValid = false;

                $cordovaToast.showShortBottom($translate('photos.Insert-Photos-in-attribute', attribute.name)).then(function(success) {}, function(error) {});
              }

              if (isValid && attribute.type == 'PHOTO_ALBUM' && attribute.photoAlbum != null) {

                angular.forEach(attribute.photoAlbum.photos, function(photo, index) {

                  delete photo.image;

                  if (photo.deleted)
                    delete attribute.photoAlbum.photos[index];
                });
              }
            });

            if (isValid) {

              $rootScope.$broadcast('loading:show');

              $scope.currentEntity.status = 'PENDING';

              var olCoordinates = ol.proj.transform([$scope.longitude, $scope.latitude], 'EPSG:4326', 'EPSG:900913');

              $scope.currentEntity.wktCoordenate = new ol.format.WKT().writeGeometry(new ol.geom.Point([olCoordinates[0], olCoordinates[1]]));

              markerService.updateMarker($scope.currentEntity, {

                callback: function(result) {

                  $scope.currentEntity.layer.visible = false;
                  $scope.toggleLayer($scope.currentEntity.layer);
                  $scope.currentEntity.layer.visible = true;
                  $scope.toggleLayer($scope.currentEntity.layer);

                  $scope.clearNewMarker();

                  $scope.clearShadowFeature($scope.currentFeature);
                  $scope.currentFeature = '';
                  $scope.minimizeFooter();

                  $rootScope.$broadcast('loading:hide');

                  $cordovaToast.showShortBottom($translate('map.Mark-updated-succesfully')).then(function(success) {
                    // success
                  }, function(error) {
                    // error
                  });

                  $scope.$apply();
                },
                errorHandler: function(message, exception) {

                  $rootScope.$broadcast('loading:hide');
                  $log.debug(message);

                  $scope.$apply();
                }
              });
            }

          } else {

            var isValid = true;

            angular.forEach($scope.currentEntity.markerAttribute, function(attribute, index) {

              if (attribute.type == 'PHOTO_ALBUM' && attribute.required && attribute.photoAlbum == null) {

                $scope.selectedPhotoAlbumAttribute = attribute;
                $state.go($scope.SHOW_GALLERY);

                isValid = false;

                $cordovaToast.showShortBottom($translate('photos.Insert-Photos-in-attribute', attribute.name)).then(function(success) {}, function(error) {});
              }

            });

            if (isValid) {

              $rootScope.$broadcast('loading:show');

              var layer = new Layer();
              layer.id = $scope.currentEntity.layer.id;
              $scope.currentEntity.layer = layer;

              var attributes = $scope.currentEntity.markerAttribute;
              $scope.currentEntity.markerAttribute = [];

              angular.forEach(attributes, function(attr, ind) {

                var attribute = new Attribute();
                attribute.id = attr.attribute.id;

                var markerAttribute = new MarkerAttribute();
                if (attr.value != "" && attr.value != undefined) {
                  markerAttribute.value = attr.value;
                } else {
                  markerAttribute.value = "";
                }

                if (attr.type == 'PHOTO_ALBUM') {

                  attribute.type = 'PHOTO_ALBUM';

                  var photoAlbum = new PhotoAlbum();
                  photoAlbum.photos = new Array();

                  if (angular.isObject(attr.photoAlbum)) {
                    angular.forEach(attr.photoAlbum.photos, function(file) {

                      var photo = new Photo();
                      photo.source = file.image;
                      photo.name = file.name;
                      photo.description = file.description;
                      photo.contentLength = file.contentLength;
                      photo.mimeType = file.mimeType;
                      photoAlbum.photos.push(photo);

                    });
                  }

                  markerAttribute.photoAlbum = photoAlbum;
                }

                markerAttribute.attribute = attribute;
                markerAttribute.marker = $scope.currentEntity;
                $scope.currentEntity.markerAttribute.push(markerAttribute);

              });

              var olCoordinates = ol.proj.transform([$scope.longitude, $scope.latitude], 'EPSG:4326', 'EPSG:900913');
              $scope.currentEntity.wktCoordenate = new ol.format.WKT().writeGeometry(new ol.geom.Point([olCoordinates[0], olCoordinates[1]]));

              markerService.insertMarker($scope.currentEntity, {
                callback: function(result) {

                  var internalLayer = $filter('filter')($scope.allInternalLayerGroups, {
                    id: $scope.currentEntity.layer.id
                  })[0];

                  var iconPath = $rootScope.$API_ENDPOINT + '/' + internalLayer.icon;

                  var iconStyle = new ol.style.Style({
                    image: new ol.style.Icon(({
                      anchor: [0.5, 1],
                      anchorXUnits: 'fraction',
                      anchorYUnits: 'fraction',
                      src: iconPath
                    }))
                  });

                  if(internalLayer.visible && internalLayer.visible != undefined) {

                    internalLayer.visible = false;
                    $scope.toggleLayer(internalLayer);
                    internalLayer.visible = true;
                    $scope.toggleLayer(internalLayer);

                  } else {

                    internalLayer.visible = true;
                    $scope.toggleLayer(internalLayer);

                  }

                  $rootScope.$broadcast('loading:hide');
                  $scope.clearNewMarker();
                  $scope.minimizeFooter();

                  $cordovaToast.showShortBottom($translate('map.Mark-inserted-succesfully')).then(function(success) {}, function(error) {});

                },
                errorHandler: function(message, exception) {
                  $log.debug(message);
                  $rootScope.$broadcast('loading:hide');
                  $scope.$apply();
                }
              });
            }
          }
        }
      };

      $scope.removeAllSelectedLayers = function() {

        angular.forEach($scope.allInternalLayerGroups, function(group) {
          if (group.visible) {
            group.visible = false;
            $scope.toggleLayer(group);
          }
        });

      };

      $scope.getCurrentEntity = function() {

        $scope.currentEntity = angular.fromJson(localStorage.getItem('currentEntity'));
        //$log.debug($scope.currentEntity);

        $timeout(function() {
          $scope.attributeIndex = 1;

          $scope.selectedPhotoAlbumAttribute = $scope.currentEntity.markerAttribute[$scope.attributeIndex];

          $scope.getPhotosByAttribute($scope.currentEntity.markerAttribute[$scope.attributeIndex], $scope.attributeIndex);
        }, 1000);

      };

      $scope.verifyStatus = function(){
        if (($scope.currentEntity.status == 'SAVED' || $scope.currentEntity.status == 'REFUSED' || $scope.currentEntity.status == 'CANCELED')
          && ($scope.currentEntity.user.id == $scope.userMe.id)) {
          $scope.isDisabled = false;
        }
      };


      $scope.$on('$stateChangeSuccess', function(event, toState, toParams, fromState, fromParams) {

        switch ($state.current.name) {
          case $scope.SHOW_GALLERY:
            {
              //$scope.getCurrentEntity();
              break;
            }
        }
      });

      /* MARKER MODERATIONS */

      /**
       * Accept marker
       */
      $scope.acceptMarkerModeration = function(id) {

        markerModerationService.acceptMarker(id, {
          callback: function(result) {

            $scope.currentEntity.status = result.marker.status;

            $cordovaToast.showShortBottom($translate('admin.marker-moderation.Marker-successfully-approved')).then(function(success) {
              // success
            }, function(error) {
              // error
            });

            $scope.$apply();
          },
          errorHandler: function(message, exception) {
            $log.debug(message);
            $scope.$apply();
          }
        });
      };

      /**
       * Refuse status marker moderation
       */
      $scope.refuseMarkerModeration = function(id, motive, description) {

        description = angular.isDefined(description) ? description : '';

        markerModerationService.refuseMarker(id, motive, description, {
          callback: function(result) {

            $scope.currentEntity.status = result.marker.status;

            $scope.verifyStatus();

            $cordovaToast.showShortBottom($translate('admin.marker-moderation.Marker-successfully-refused')).then(function(success) {
              // success
            }, function(error) {
              // error
            });


            $scope.$apply();
          },
          errorHandler: function(message, exception) {
            $log.debug(message);
            $scope.$apply();
          }
        });
      };

      /**
       * Cancel marker
       */
      $scope.cancelMarkerModeration = function(id) {
        markerModerationService.cancelMarkerModeration(id, {
          callback: function(result) {

            $scope.currentEntity.status = result.status;

            $cordovaToast.showShortBottom($translate('admin.marker-moderation.Marker-successfully-canceled')).then(function(success) {
              // success
            }, function(error) {
              // error
            });

            $scope.$apply();
          },
          errorHandler: function(message, exception) {
            $log.debug(message);
            $scope.$apply();
          }
        });
      };

      $scope.removeMarkerModeration = function() {
        markerService.removeMarker($scope.currentEntity.id, {
          callback: function(result) {

            var layer = $filter('filter')($scope.allInternalLayerGroups, {id: $scope.currentEntity.layer.id})[0];

            layer.visible = false;
            $scope.toggleLayer(layer);
            layer.visible = true;
            $scope.toggleLayer(layer);

            $scope.minimizeFooter();
            $scope.clearNewMarker();

            $cordovaToast.showShortBottom($translate("map.Mark-was-successfully-deleted")).then(function(success) {
            }, function(error) {
            });

            $scope.minimizeFooter();
            $scope.apply();

          },
          errorHandler: function(message, exception) {
            $scope.msg = {
              type: "error",
              text: message
            };
            $scope.$apply();
          }
        });
      };

      $scope.approveMarker = function() {

        var confirmPopup = $ionicPopup.confirm({
          title: $translate('admin.marker-moderation.Confirm-approve'),
          template: $translate('admin.marker-moderation.Are-you-sure-you-want-to-approve-this-marker') + '?',
          cancelText: $translate('Close'),
          okText: $translate('admin.marker-moderation.Approve')
        });

        confirmPopup.then(function(res) {
          console.log(res);
          if (res) {
            $scope.acceptMarkerModeration($scope.currentEntity.id);
          }
        });

      };

      $scope.removeMarker = function() {

        var confirmPopup = $ionicPopup.confirm({
          title: $translate("map.Delete-mark"),
          template: $translate("map.Are-you-sure-you-want-to-delete-the-mark") + '?',
          cancelText: $translate("admin.users.Cancel"),
          okText: $translate("layer-group-popup.Delete")
        });

        confirmPopup.then(function(res) {
          console.log(res);
          if (res) {
            $scope.removeMarkerModeration($scope.currentEntity.id);
          }
        });
      }

      $scope.listMotives = function() {

        motiveService.listMotives({
          callback: function(result) {
            $scope.motives = result;
            $scope.refuse = {motive : result[0]};
            $scope.$apply();
          },
          errorHandler: function(message, exception) {
            $log.debug(message);
            $scope.$apply();
          }
        });
      };

      $ionicModal.fromTemplateUrl('views/modal/marker-moderation-modal.html', {
        scope: $scope,
        animation: 'slide-in-up'
      }).then(function(modal) {

        $scope.refuseMarkerModal = modal;
      });

      $scope.refuseMarker = function() {
        $scope.listMotives();
        $scope.refuseMarkerModal.show();
      };

      $scope.closeRefuseMarkerModal = function() {
        $scope.refuseMarkerModal.hide();
      };

      $scope.confirmRefuseMarkerModal = function(refuse) {
        $scope.refuseMarkerModal.hide();
        $scope.refuseMarkerModeration($scope.currentEntity.id, refuse.motive, refuse.description);
      };

      $scope.cancelMarker = function() {

        var confirmPopup = $ionicPopup.confirm({
          title: $translate('admin.marker-moderation.Confirm-cancel'),
          template: $translate('admin.marker-moderation.Are-you-sure-you-want-to-cancel-this-marker') + '?',
          cancelText: $translate('Close'),
          okText: $translate('admin.marker-moderation.Confirm-cancel')
        });

        confirmPopup.then(function(res) {
          if (res) {
            $scope.cancelMarkerModeration($scope.currentEntity.id);
          }
        });

      };

    });

}(window.angular));
