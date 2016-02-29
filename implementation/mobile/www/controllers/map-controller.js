(function(angular) {
  'use strict';

  /**
   *
   * @param $scope
   * @param $state
   */
  angular.module('application')
    .controller('MapController', function($rootScope, $scope, $translate, $state, $document, $importService, $ionicGesture,
      $ionicPopup, $ionicSideMenuDelegate, $timeout, $cordovaDatePicker, $cordovaGeolocation,
      $filter, $log, $location, $ionicNavBarDelegate, $cordovaCamera, $ionicLoading,
      $cordovaToast, $ionicModal, $ionicSlideBoxDelegate, $ionicActionSheet) {

      /**
       *
       */
      $timeout(function() {
        $importService("accountService");
        $importService("layerGroupService");
        $importService("markerService");
        $importService("accountService");
        $importService("markerModerationService");
        $importService("motiveService");
      });

      //----STATES
      /**
       *
       */
      $scope.INDEX = "map.index";
      $scope.SHOW_GALLERY = "map.gallery";
      /**

       /*-------------------------------------------------------------------
       *              ATTRIBUTES
       *-------------------------------------------------------------------*/

      $scope.map = {};
      $scope.currentFeature = '';
      $scope.direction = '';
      $scope.isNewMarker = false;
      $scope.isDragStart = false;
      $scope.isDrawerOpen = false;
      $scope.allInternalLayerGroups = [];
      $scope.layers = [];
      $scope.newMarker = {};
      $scope.dragPan = true;

      $scope.photosSelected = 0;
      $scope.selectedPhoto = {};
      $scope.editPhoto = false;

      $scope.userMe = {};
      $scope.selectedPhotoAlbumAttribute = {};

      $scope.attributeIndex = '';

      $scope.pullUpHandle = angular.element(document.getElementsByTagName('ion-pull-up-handle'));

      $scope.pullUpHeight = 100;

      /**
       * Setting the background layer - OSM
       */
      $scope.rasterOSM = new ol.layer.Tile({
        source: new ol.source.OSM()
      });

      $scope.view = new ol.View({
        center: ol.proj.transform([-54.1394, -24.7568], 'EPSG:4326', 'EPSG:3857'),
        zoom: 9,
        minZoom: 3
      });

      /**
       *
       */
      $scope.model = {
        user: null,
        marker: null
      };

      $scope.showMarkerDetails = false;

      $scope.currentEntity = {};

      $scope.isNewMarker = false;

      /*-------------------------------------------------------------------
       * 		 				 	  HANDLERS
       *-------------------------------------------------------------------*/

      $scope.goBack = function() {
        $ionicNavBarDelegate.back();
      };

      $scope.setImagePath = function(image) {
        if (angular.isDefined(image) && image.match(/broker/)) {
          return $rootScope.$API_ENDPOINT + image.match(/\/broker.*/)[0];
        } else {
          return "data:image/png;base64," + image;
        }
      };

      $scope.getMarkerStatus = function(status) {
        return $translate('map.' + status.charAt(0).toUpperCase() + status.toLowerCase().slice(1));
      };

      $scope.clearNewMarker = function() {
        $scope.map.removeLayer($scope.currentCreatingInternalLayer);
        $scope.currentCreatingInternalLayer = {};
        $scope.minimizeFooter();
      };

      $scope.expandFooter = function() {
        $rootScope.$broadcast('expandFooter');
      };

      $scope.minimizeFooter = function() {
        $rootScope.$broadcast('minimizeFooter');
      };

      $scope.footerExpand = function() {

        //$log.debug('pullUpHeight: ' + $scope.pullUpHeight);
        //$log.debug('Footer expanded');

        $scope.listAllInternalLayerGroups();

        $scope.showMarkerDetails = true;

        if (!$scope.currentEntity.id && !$scope.currentEntity.layer) {

          $scope.currentEntity.layer = $scope.allInternalLayerGroups[0];
          $scope.listAttributesByLayer($scope.currentEntity.layer);

        } else {

          $scope.getLastPhotoByMarkerId($scope.currentEntity.id);

        }

        $scope.$apply();
      };

      $scope.footerCollapse = function() {
        //$log.debug('Footer collapsed');
        $scope.showMarkerDetails = false;
      };

      $scope.footerMinimize = function() {
        $log.debug('Footer minimize');
        //$log.debug($scope.pullUpHeight);
        $scope.showMarkerDetails = false;
        $scope.imgResult = '';

        if ($state.current.name == 'map.gallery')
          $scope.minimizeFooter();
      };

      $scope.getLastPhotoByMarkerId = function(markerId) {

        markerService.lastPhotoByMarkerId(markerId, {
          callback: function(result) {

            $scope.imgResult = result.image;
            $scope.$apply();

          },
          errorHandler: function(message, exception) {

            $scope.imgResult = null;
            $log.debug(message);
            $scope.$apply();
          }
        });

      };

      /*
       * GALLERY
       */
      $scope.getPhotosByAttribute = function(attribute, reload) {

        if (angular.equals($scope.selectedPhotoAlbumAttribute, {}) || reload === true) {

          if(angular.isDefined(attribute.photoAlbum))
            attribute.photoAlbum = null;

          $scope.selectedPhotoAlbumAttribute = attribute;
        }

        var attr = $filter('filter')($scope.currentEntity.markerAttribute, {
          id: attribute.id
        })[0];

        $scope.attributeIndex = $scope.currentEntity.markerAttribute.indexOf(attr);

        markerService.findPhotoAlbumByAttributeMarkerId(attribute.id, null, {
          callback: function(result) {

            attribute.photoAlbum = result.content[0].photoAlbum;
            attribute.photoAlbum.photos = result.content;

            $scope.photos = result.content;

            $scope.$apply();

            //$log.debug($scope.currentEntity);
          },
          errorHandler: function(message, exception) {
            $log.debug(message);
            $scope.$apply();
          }
        });

      };

      $scope.onHold = function(evt) {

        //$log.debug('onHold');

        $scope.isDisabled = false;

        $scope.clearShadowFeature($scope.currentFeature);
        $scope.currentFeature = '';

        $scope.currentEntity = {};
        $scope.pullUpHeight = 100;
        angular.element(document.getElementsByTagName('ion-pull-up-handle')).height($scope.pullUpHeight + 'px');

        angular.element(document.getElementsByTagName('ion-pull-up-handle')).css('top', '-' + $scope.pullUpHeight + 'px');

        $scope.clearNewMarker();

        $scope.isNewMarker = true;

        $scope.currentEntity = new Marker();

        var coordinate = $scope.map.getCoordinateFromPixel([evt.gesture.center.pageX, evt.gesture.center.pageY]);
        var transformed_coordinate = ol.proj.transform(coordinate, 'EPSG:900913', 'EPSG:4326');

        $scope.longitude = transformed_coordinate[0];
        $scope.latitude = transformed_coordinate[1];

        var iconStyle = new ol.style.Style({
          image: new ol.style.Icon({
            anchor: [0.5, 1],
            anchorXUnits: 'fraction',
            anchorYUnits: 'fraction',
            src: $rootScope.$API_ENDPOINT + '/static/images/new_marker.png'
          }),
          zIndex: 2
        });

        var iconFeature = new ol.Feature({
          geometry: new ol.geom.Point([coordinate[0], coordinate[1]])
        });

        var layer = new ol.layer.Vector({
          source: new ol.source.Vector({
            features: [iconFeature]
          })
        });

        layer.setStyle(iconStyle);

        $scope.currentCreatingInternalLayer = layer;
        $scope.map.addLayer(layer);

        $scope.currentEntity.latitude = coordinate[0];
        $scope.currentEntity.longitude = coordinate[1];

        $scope.setMarkerCoordinatesFormat();

        $scope.currentEntity.status = 'PENDING';

      };

      $timeout(function() {
        $scope.map = new ol.Map({
          controls: [],
          interactions: ol.interaction.defaults({
            dragPan: $scope.dragPan,
            mouseWheelZoom: true
          }),
          target: 'map',
          view: $scope.view
        });

        $scope.map.addLayer($scope.rasterOSM);
        $scope.rasterOSM.setVisible(true);

        $scope.map.on('pointerdrag', function(event, data) {

          if ($scope.isDrawerOpen) {
            event.preventDefault();
            $scope.toggleDrawer();
            $scope.$apply(function() {
              $scope.dragPan = false;
            });
          }

          if (event.pixel[0] < 40 || $scope.isDragStart) {

            if ($scope.direction === 'right') {
              event.preventDefault();

              $scope.$apply(function() {
                $scope.dragPan = false;
              });

            } else {

              $scope.$apply(function() {
                $scope.dragPan = true;
              });
            }
          }

        });

        /**
         * Click event to prompt the geoserver the information layer of the clicked coordinate
         */
        $scope.map.on('click', function(evt) {

          if (!$scope.isNewMarker) {
            $scope.clearNewMarker();
            $scope.currentEntity = {};
            $scope.clearShadowFeature($scope.currentFeature);
            $scope.currentFeature = '';
            $scope.$apply();
          }

          //$log.debug('openlayers.map.singleclick');

          //$log.debug($scope.isNewMarker);

          if ($scope.isDrawerOpen) {

            $scope.toggleDrawer();

          } else {

            var feature = $scope.map.forEachFeatureAtPixel(evt.pixel, function(feature, olLayer) {
              if (angular.isDefined(feature.getProperties().marker)) {
                return feature;
              } else {
                $scope.currentEntity = {};
              }
            });

            if (angular.isDefined(feature) && !$scope.isNewMarker) {

              $scope.selectedPhotoAlbumAttribute = {};

              $scope.isDisabled = true;

              $scope.currentEntity = feature.getProperties().marker;

              // if(($scope.userMe.role != 'ADMINISTRATOR' && $scope.currentEntity.status != 'PENDING' && $scope.currentEntity.user.id == $scope.userMe.id)
              //   || ($scope.userMe.role == 'ADMINISTRATOR' && ($scope.currentEntity.status == 'SAVED' || $scope.currentEntity.status == 'REFUSED' || $scope.currentEntity.status == 'CANCELED'))) {

              if (($scope.currentEntity.status == 'SAVED' || $scope.currentEntity.status == 'REFUSED' || $scope.currentEntity.status == 'CANCELED') && ($scope.currentEntity.user.id == $scope.userMe.id)) {
                $scope.isDisabled = false;
              }

              var iconStyle = new ol.style.Style({
                image: new ol.style.Icon(({
                  anchor: [0.5, 1],
                  anchorXUnits: 'fraction',
                  anchorYUnits: 'fraction',
                  src: $rootScope.$API_ENDPOINT + '/' + $scope.currentEntity.layer.icon
                }))
              });

              var shadowType = 'default';
              if (!$scope.currentEntity.layer.icon.match(/default/))
                shadowType = 'collection';

              var shadowStyle = $scope.setShadowMarker(shadowType);

              feature.setStyle([iconStyle, shadowStyle]);

              var geometry = feature.getGeometry();
              var coordinate = geometry.getCoordinates();

              var transformed_coordinate = ol.proj.transform(coordinate, 'EPSG:900913', 'EPSG:4326');
              $scope.latitude = transformed_coordinate[1];
              $scope.longitude = transformed_coordinate[0];

              $scope.setMarkerCoordinatesFormat();

              $scope.currentFeature = feature;
              $scope.pullUpHeight = 70;

              //$log.debug($scope.currentEntity);
              //$log.debug($scope.showMarkerDetails);

              markerService.listAttributeByMarker($scope.currentEntity.id, {

                callback: function(result) {

                  $scope.currentEntity.markerAttribute = result;

                  angular.forEach($scope.currentEntity.markerAttribute, function(markerAttribute, index) {

                    markerAttribute.name = markerAttribute.attribute.name;
                    markerAttribute.type = markerAttribute.attribute.type;

                    if (markerAttribute.attribute.type == "NUMBER") {
                      markerAttribute.value = parseInt(markerAttribute.value);
                    }

                    if (markerAttribute.attribute.type == 'PHOTO_ALBUM')
                      $scope.getPhotosByAttribute(markerAttribute, index);

                  });

                  layerGroupService.listAttributesByLayer($scope.currentEntity.layer.id, {

                    callback: function(result) {

                      $scope.attributesByLayer = [];

                      angular.forEach(result, function(attribute, index) {

                        var exist = false;

                        angular.forEach($scope.currentEntity.markerAttribute, function(attributeByMarker, index) {

                          if (attributeByMarker.attribute.id == attribute.id) {
                            exist = true;
                          }
                        });

                        if (!exist) {

                          $scope.currentEntity.markerAttribute.push({
                            attribute: attribute,
                            marker: $scope.currentEntity
                          });
                          $scope.attributesByLayer.push(attribute);

                        }

                      });

                      //localStorage.setItem('currentEntity', angular.toJson($scope.currentEntity));

                      $scope.$apply();
                    },
                    errorHandler: function(message, exception) {
                      $log.debug(message);
                      $scope.$apply();
                    }
                  });

                  $scope.$apply();

                },
                errorHandler: function(message, exception) {
                  $log.debug(message);
                  $scope.$apply();
                }
              });


              $scope.$apply();

            }

            if ($scope.isNewMarker) {
              $scope.isNewMarker = false;
            }
          }

        });
      });


      $ionicGesture.on('tap', function(e) {
        $scope.$apply(function() {
          //$log.debug('tap');
        });

      }, $document);

      $scope.onDragStart = function(state) {
        $scope.isDragStart = true;
        $scope.dragPan = false;
        $scope.listAllInternalLayerGroups();
      };

      $scope.onDragEnd = function(state) {
        $scope.isDrawerOpen = state;
        $scope.isDragStart = false;
        $scope.dragPan = state;
      };

      $scope.toggleDrawer = function() {
        $rootScope.$broadcast('toggleDrawer');
        $scope.listAllInternalLayerGroups();
        $scope.isDrawerOpen = !$scope.isDrawerOpen;
        $scope.isDragStart = false;
      };

      $ionicGesture.on('drag', function(e) {
        $scope.$apply(function() {
          $scope.direction = e.gesture.direction;
        });

      }, $document);

      $scope.clearShadowFeature = function(feature) {

        if (feature)
          feature.setStyle(feature.getStyle()[0]);

      };

      $scope.setShadowMarker = function(type) {

        if (!type) {
          type = 'default';
          if (($scope.currentEntity.layer && !$scope.currentEntity.layer.layerIcon.match(/default/)) || ($scope.currentEntity.layer && $scope.currentEntity.layer.icon && !$scope.currentEntity.layer.icon.match(/default/)))
            type = 'collection';
        }

        var anchor = [];
        anchor['collection'] = [0.50, 0.86];
        anchor['default'] = [0.49, 0.83];
        anchor['marker'] = [0.48, 0.73];

        return new ol.style.Style({
          image: new ol.style.Icon({
            anchor: anchor[type],
            anchorXUnits: 'fraction',
            anchorYUnits: 'fraction',
            src: $rootScope.$API_ENDPOINT + '/static/images/' + type + '_shadow.png'
          }),
          zIndex: 1
        });
      };

      $scope.setMarkerCoordinatesFormat = function() {
        if ($scope.coordinatesFormat == 'DEGREES_DECIMAL') {
          $scope.formattedLatitude = $scope.latitude.toFixed(6);
          $scope.formattedLongitude = $scope.longitude.toFixed(6);
        } else {
          $scope.formattedLatitude = $scope.convertDDtoDMS($scope.latitude, true);
          $scope.formattedLongitude = $scope.convertDDtoDMS($scope.longitude, false);
        }
      };

      $scope.convertDDtoDMS = function(coordinate, latitude) {
        var valCoordinate, valDeg, valMin, valSec, result;
        valCoordinate = Math.abs(coordinate);
        valDeg = Math.floor(valCoordinate);
        result = valDeg + "° ";
        valMin = Math.floor((valCoordinate - valDeg) * 60);
        result += valMin + "′ ";
        valSec = Math.round((valCoordinate - valDeg - valMin / 60) * 3600 * 1000) / 1000;
        result += valSec + '″ ';
        if (latitude)
          result += coordinate < 0 ? 'S' : 'N';
        if (!latitude)
          result += coordinate < 0 ? 'W' : 'O';
        return result;
      };

      $scope.toggleLayer = function(layer) {

        if ($filter('filter')($scope.allInternalLayerGroups, {
            visible: true
          }).length > 3) {

          layer.visible = false;

          $cordovaToast.showShortBottom($translate('mobile.map.Maximum-selections')).then(function(success) {
            // success
          }, function(error) {
            // error
          });

        } else {

          var layerExits = false; // Used to verify if the layer has been requested before

          angular.forEach($scope.map.getLayers(), function(group) {

            if (group instanceof ol.layer.Group) {
              var prop = group.getProperties();

              if (prop.id == layer.id) {
                layerExits = true;
                group.setVisible(layer.visible);
              }
            }
          });

          if (layer.visible && !layerExits) {

            markerService.listMarkerByLayer(layer.id, {
              callback: function(result) {

                if (result.length > 0) {

                  var iconPath = $rootScope.$API_ENDPOINT + '/' + result[0].layer.icon;

                  var iconStyle = new ol.style.Style({
                    image: new ol.style.Icon(({
                      anchor: [0.5, 1],
                      anchorXUnits: 'fraction',
                      anchorYUnits: 'fraction',
                      src: iconPath
                    }))
                  });

                  var markers = [];
                  angular.forEach(result, function(marker, index) {

                    var iconFeature = new ol.Feature({
                      geometry: new ol.format.WKT().readGeometry(marker.location.coordinateString),
                      marker: marker
                    });

                    iconFeature.setStyle(iconStyle);

                    var vectorSource = new ol.source.Vector({
                      features: [iconFeature]
                    });

                    var vectorLayer = new ol.layer.Vector({
                      source: vectorSource,
                      layer: layer.id
                    });

                    markers.push(vectorLayer);

                  });

                  var group = new ol.layer.Group({
                    layers: markers,
                    id: layer.id
                  });

                  $scope.map.addLayer(group);

                  $scope.$apply();
                }

              },
              errorHandler: function(message, exception) {
                $log.debug(message);
                $scope.$apply();
              }
            });

          } else {
            $scope.map.removeLayer(layer.layer);
          }
        }
      };

      /**
       *
       */
      $scope.listAllInternalLayerGroups = function() {

        if ($scope.allInternalLayerGroups.length == 0) {
          layerGroupService.listLayersByFilters(null, null, {
            callback: function(result) {
              $scope.allInternalLayerGroups = result.content;

              $scope.allInternalLayerGroups[2].visible = true;
              $scope.toggleLayer($scope.allInternalLayerGroups[2]);

              $scope.$apply();
            },
            errorHandler: function(message, exception) {
              $ionicPopup.alert({
                title: 'Opss...',
                template: message
              });

              $scope.$apply();
            }
          });
        }
      };

      /**
       *
       */
      $scope.listAttributesByLayer = function(layer, reload) {

        $scope.selectedPhotoAlbumAttribute = {};

        if (!$scope.currentEntity.markerAttribute || $scope.currentEntity.markerAttribute.length == 0 || reload) {

          $scope.currentEntity.markerAttribute = [];

          layerGroupService.listAttributesByLayer(layer.id, {
            callback: function(result) {

              angular.forEach(result, function(layerAttribute, index) {

                var attribute = new Attribute();

                attribute.id = layerAttribute.id;

                layerAttribute.id = null;
                layerAttribute.photoAlbum = null;

                layerAttribute.attribute = attribute;

                $scope.currentEntity.markerAttribute.push(layerAttribute);

                if (layerAttribute.type == 'PHOTO_ALBUM' && angular.equals($scope.selectedPhotoAlbumAttribute, {})) {
                  $scope.selectedPhotoAlbumAttribute = layerAttribute;
                  $scope.attributeIndex = index;
                }

              });

              //$scope.currentEntity.markerAttribute = result;

              $scope.$apply();
            },
            errorHandler: function(message, exception) {
              $ionicPopup.alert({
                title: 'Opss...',
                template: message
              });

              $scope.$apply();
            }
          });
        }
      };

      /**
       *
       */
      $scope.getGPSPosition = function() {
        var posOptions = {
          timeout: 10000,
          enableHighAccuracy: true
        };
        $cordovaGeolocation
          .getCurrentPosition(posOptions)
          .then(function(position) {
            var lat = position.coords.latitude;
            var long = position.coords.longitude;

            $ionicPopup.alert({
              title: 'GPS funcionando',
              template: lat + ' ' + long
            });
          }, function(err) {
            $log.debug(err);
          });
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
      }, 5000);

      $scope.removeAllSelectedLayers = function() {

        angular.forEach($scope.allInternalLayerGroups, function(group) {
          if (group.visible) {
            group.visible = false;
            $scope.toggleLayer(group);
          }
        });

      };

      /**
       * Prepara o estado, retira o password criptografado do usuário
       */
      $scope.logout = function() {
        $scope.toggleDrawer();
        $scope.removeAllSelectedLayers();

        localStorage.removeItem('userEmail');
        localStorage.removeItem('token');
        $location.path($rootScope.$API_ENDPOINT + "/j_spring_security_logout");

        $scope.userMe = {};
        $rootScope.model = {
          form: null,
          user: {
            email: 'test_prognus@mailinator.com', //TODO --------- R  E  M  O  V  E  R
            password: 'admin' //TODO --------- R  E  M  O  V  E  R
          },
          errorMsg: {
            title: $translate('Error'),
            subTitle: $translate('authentication.Authentication'),
            template: $translate('authentication.Bad-credentials') + ', ' + $translate('or') + ' ' + $translate('authentication.User-is-disabled')
          }
        };

        //Realiza o logout do google plus
        window.plugins.googleplus.disconnect(
          function(msg) {
            console.log(msg);
          }
        );
      };

    });

}(window.angular));
