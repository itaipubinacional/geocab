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
      $cordovaToast, $ionicModal, $ionicSlideBoxDelegate, $ionicActionSheet) {

      $scope.getPhoto = function() {
        Camera.getPicture().then(function(imageURI) {

          $scope.currentEntity.image = imageURI;
          //$log.debug(imageURI);

        }, function(err) {
          //$log.debug.err(err);
        });
      };

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

          //$log.debug(form);

        } else {

          $scope.isFormSubmit = false;

          if ($scope.currentEntity.id) {

            var olCoordinates = ol.proj.transform([$scope.longitude, $scope.latitude], 'EPSG:4326', 'EPSG:900913');
            $scope.currentEntity.wktCoordenate = new ol.format.WKT().writeGeometry(new ol.geom.Point([olCoordinates[0], olCoordinates[1]]));

            angular.forEach($scope.currentEntity.markerAttribute, function(attribute, index) {
              if (attribute.type == 'PHOTO_ALBUM' && attribute.photoAlbum != null) {
                angular.forEach(attribute.photoAlbum.photos, function(photo, index) {
                  delete photo.image;

                  if (photo.deleted)
                    delete attribute.photoAlbum.photos[index];
                });
              }
            });

            markerService.updateMarker($scope.currentEntity, {
              callback: function(result) {

                $scope.isLoading = false;

                $scope.clearNewMarker();

                $scope.currentEntity.layer.visible = false;
                $scope.toggleLayer($scope.currentEntity.layer);
                $scope.currentEntity.layer.visible = true;
                $scope.toggleLayer($scope.currentEntity.layer);

                $scope.currentEntity = {};

                $scope.clearShadowFeature($scope.currentFeature);
                $scope.currentFeature = '';
                $scope.minimizeFooter();

                $cordovaToast.showShortBottom($translate('map.Mark-updated-succesfully')).then(function(success) {
                  // success
                }, function(error) {
                  // error
                });

                $scope.$apply();
              },
              errorHandler: function(message, exception) {

                $scope.isLoading = false;
                $log.debug(message);

                $scope.$apply();
              }
            });

          } else {

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

            //$log.debug($scope.currentEntity);

            markerService.insertMarker($scope.currentEntity, {
              callback: function(result) {

                $scope.isLoading = false;
                $scope.clearNewMarker();

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

                if (angular.isDefined(internalLayer.visible) && internalLayer.visible) {

                  angular.forEach($scope.map.getLayers(), function(group) {

                    if (group instanceof ol.layer.Group) {
                      var prop = group.getProperties();

                      if (prop.id == $scope.currentEntity.layer.id) {

                        var innerLayers = prop.layers.getArray();
                        //group.setVisible(layer.visible);

                        var iconFeature = new ol.Feature({
                          geometry: new ol.format.WKT().readGeometry($scope.currentEntity.wktCoordenate),
                          marker: $scope.currentEntity
                        });

                        iconFeature.setStyle(iconStyle);

                        var vectorSource = new ol.source.Vector({
                          features: [iconFeature]
                        });

                        var vectorLayer = new ol.layer.Vector({
                          source: vectorSource
                        });

                        innerLayers.push(vectorLayer);

                      }
                    }
                  });
                } else {

                  internalLayer.visible = true;
                  $scope.toggleLayer(internalLayer);

                }

                $scope.currentEntity = {};
                $scope.currentFeature = '';
                $scope.minimizeFooter();

                $cordovaToast.showShortBottom($translate('map.Mark-inserted-succesfully')).then(function(success) {}, function(error) {});

                $scope.$apply();
              },
              errorHandler: function(message, exception) {
                $log.debug(message);
                $scope.isLoading = false;
                $scope.$apply();
              }
            });
          }
        }
      };

      /**
       * authenticated user
       * */
      $timeout(function() {
        accountService.getUserAuthenticated({
          callback: function(result) {
            $scope.userMe = result;
            $scope.coordinatesFormat = result.coordinates;
            $scope.$apply();
          },
          errorHandler: function(message, exception) {
            $log.debug(message);
            $scope.$apply();
          }
        });
      }, 2000);

      $scope.removeAllSelectedLayers = function() {

        angular.forEach($scope.allInternalLayerGroups, function(group) {
          if (group.visible) {
            group.visible = false;
            $scope.toggleLayer(group);
          }
        });

      };

      $timeout(function() {
        $scope.listAllInternalLayerGroups();
      }, 1000);

      $scope.getCurrentEntity = function() {

        $scope.currentEntity = angular.fromJson(localStorage.getItem('currentEntity'));
        //$log.debug($scope.currentEntity);

        $timeout(function() {
          $scope.attributeIndex = 1;

          $scope.selectedPhotoAlbumAttribute = $scope.currentEntity.markerAttribute[$scope.attributeIndex];

          $scope.getPhotosByAttribute($scope.currentEntity.markerAttribute[$scope.attributeIndex], $scope.attributeIndex);
        }, 1000);

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

        markerModerationService.refuseMarker(id, motive, description, {
          callback: function(result) {

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

      $scope.approveMarker = function() {

        var confirmPopup = $ionicPopup.confirm({
          title: $translate('admin.marker-moderation.Confirm-approve'),
          template: $translate('admin.marker-moderation.Are-you-sure-you-want-to-approve-this-marker') + '?',
          buttons: [{
            text: 'Cancelar'
          }, {
            text: $translate('admin.marker-moderation.Approve'),
            type: 'button-positive'
          }]
        });

        confirmPopup.then(function(res) {
          if (res) {
            $scope.acceptMarkerModeration($scope.currentEntity.id);
          }
        });

      };

      $scope.listMotives = function() {

        motiveService.listMotives({
          callback: function(result) {
            $scope.motives = result;
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
          buttons: [{
            text: $translate('layer-group-popup.Close')
          }, {
            text: $translate('admin.marker-moderation.Confirm-cancel'),
            type: 'button-positive'
          }]
        });

        confirmPopup.then(function(res) {
          if (res) {
            $scope.cancelMarkerModeration($scope.currentEntity.id);
          }
        });

      };

    });

}(window.angular));
