(function (angular) {
  'use strict';

  /**
   *
   * @param $scope
   * @param $state
   */
  angular.module('application')
    .controller('MapController', function ($rootScope, $scope, $translate, $state, $document, $importService, $ionicGesture,
                                           $ionicPopup, $ionicSideMenuDelegate, $timeout, $cordovaGeolocation, $filter, $log, $location, $cordovaCamera, $ionicLoading,
                                           $cordovaToast, $http, $ionicNavBarDelegate, $interval, $ionicPlatform) {

      /**
       *
       */
      $timeout(function () {
        $importService("accountService");
        $importService("layerGroupService");
        $importService("markerService");
        $importService("accountService");
        $importService("markerModerationService");
        $importService("motiveService");
      });

      $scope.parseDate = function (key, value) {

        if (key.match(/(created|updated)/) && !!value) {
          return new Date(value);
        }
        return value;

      };

      //----STATES
      /**
       *
       */
      $scope.MAP_INDEX = "map.index";
      $scope.MAP_GALLERY = "map.gallery";
      $scope.MAP_MARKER = "map.marker";

      $scope.MAP_WMS = "map.wms";

      /**
       /*-------------------------------------------------------------------
       *              ATTRIBUTES
       *-------------------------------------------------------------------*/

      $scope.map = {};
      $scope.currentFeature = '';
      $scope.isNewMarker = false;
      $scope.internalLayer = {};
      $scope.allInternalLayerGroups = [];
      $scope.allLayers = [];
      $scope.layers = [];
      $scope.newMarker = {};
      $scope.dragPan = true;
      $scope.layers = [];
      $scope.lastPhoto = {};

      $scope.lastCurrentEntity = {};

      $scope.currentEntity = localStorage.getItem('currentEntity') ? JSON.parse(localStorage.getItem('currentEntity'), $scope.parseDate) : {};
      $scope.isNewMarker = false;

      $scope.currentWMS = {};

      $scope.photosSelected = 0;
      $scope.selectedPhoto = {};
      $scope.editPhoto = false;

      $scope.userMe = {};
      $scope.selectedPhotoAlbumAttribute = localStorage.getItem('selectedPhotoAlbumAttribute') ? angular.fromJson(localStorage.getItem('selectedPhotoAlbumAttribute')) : {};

      $scope.attributeIndex = '';

      $scope.selectedLayers = localStorage.getItem('selectedLayers') ? angular.fromJson(localStorage.getItem('selectedLayers')) : [];

      $scope.lastCenterPosition = localStorage.getItem('lastCenterPosition') ? angular.fromJson(localStorage.getItem('lastCenterPosition')) : [-54.1394, -24.7568];


      /**
       *
       */
      $scope.model = {
        user: null,
        marker: null
      };

      /**
       * authenticated user
       * */
      $scope.getUserAuthenticated = function () {

        $log.debug('getUserAuthenticated');

        if (angular.equals($scope.userMe, {})) {

          $rootScope.$broadcast('loading:show');

          var intervalPromise = $interval(function () {

            if (angular.isDefined(accountService)) {
              accountService.getUserAuthenticated({
                callback: function (result) {
                  $scope.userMe = result;
                  $scope.coordinatesFormat = result.coordinates;

                  $scope.setMarkerCoordinatesFormat();

                  $rootScope.$broadcast('loading:hide');

                  $interval.cancel(intervalPromise);

                  $log.debug('getUserAuthenticated success');
                  $scope.$apply();
                },
                errorHandler: function (message, exception) {

                  $rootScope.$broadcast('loading:hide');

                  $log.debug(message);

                  $interval.cancel(intervalPromise);

                  localStorage.removeItem('lastRoute');
                  localStorage.removeItem('lastState');

                  $state.go('authentication.login');

                  $log.debug('getUserAuthenticated fail');

                  $ionicSideMenuDelegate.toggleLeft();

                  $scope.clearNewMarker();

                  $scope.$apply();
                }
              });
            }
          }, 500);

        }
      };

      $scope.$on('$ionicView.beforeEnter', function (event, viewData) {
        $log.debug('beforeEnter');
        viewData.enableBack = false;
      });

      $scope.loadSelectedLayers = function () {

        $log.debug('loadSelectedLayers');

        angular.forEach($scope.selectedLayers, function (layer) {
           $scope.toggleLayer(layer);
        });

      };

      $scope.showMarkerPosition = function () {

        if ($scope.coordinatesFormat == 'DEGREES_DECIMAL')
          return $scope.currentEntity.formattedLatitude + ', ' + $scope.currentEntity.formattedLongitude;
        return $scope.currentEntity.formattedLatitude + ' ' + $scope.currentEntity.formattedLongitude;

      };

      $scope.removeLastPhoto = function () {

        var hasPhoto = false;

        angular.forEach($scope.currentEntity.markerAttribute, function (attribute) {

          if (attribute.type == 'PHOTO_ALBUM' && attribute.photoAlbum != undefined) {

            angular.forEach(attribute.photoAlbum.photos, function (photo) {

              hasPhoto = true;

              if (photo.deleted && photo.id == $scope.lastPhoto.id) {
                $scope.imgResult = '';
              }
            });
          }
        });

        if (!hasPhoto) {
          $scope.imgResult = '';
        }
      };

      /**
       * LIST ALL INTERNAL LAYERS GROUPS
       */
      $scope.listAllInternalLayerGroups = function () {

        if ($scope.allInternalLayerGroups.length == 0) {

          $rootScope.$broadcast('loading:show');

          var intervalPromise = $interval(function () {

            if (angular.isDefined(layerGroupService)) {

              layerGroupService.listAllInternalLayerGroups({
                callback: function (result) {
                  $scope.allInternalLayerGroups = result;
                  $log.debug($scope.allInternalLayerGroups);

                  //$scope.currentEntity.layer = $scope.allInternalLayerGroups[0];
                  $rootScope.$broadcast('loading:hide');

                  if(!angular.equals($scope.currentEntity, {}) && !angular.equals($scope.currentEntity.layer, {})) {
                    angular.forEach(result, function (layer) {
                      if ($scope.currentEntity.layer && layer.id == $scope.currentEntity.layer.id) {
                        $scope.currentEntity.layer = layer;
                      }
                    });
                  }

                  $interval.cancel(intervalPromise);

                  $scope.$apply();
                },
                errorHandler: function (message, exception) {
                  $log.debug(message);

                  $interval.cancel(intervalPromise);

                  localStorage.setItem('lastState', $scope.MAP_INDEX);

                  $rootScope.$broadcast('loading:hide');

                  $scope.$apply();
                }
              });
            }
          }, 500);

        }
      };

      $scope.$on('$stateChangeSuccess', function (event, toState, toParams, fromState, fromParams) {

        $log.debug('$stateChangeSuccess');

        var userEmail = localStorage.getItem('userEmail');
        var token = localStorage.getItem('token');

        if (!!userEmail) {

          $http.post($scope.$API_ENDPOINT + '/login/geocab', {'email' : userEmail, 'token' : token})
            .success(function (data, status, headers, config) {

              $log.debug('user logged');

              $scope.userMe = data;
              $scope.coordinatesFormat = data.coordinates;

              $scope.setMarkerCoordinatesFormat();

              $rootScope.$broadcast('loading:hide');

              $log.debug('getUserAuthenticated success');

            })
            .error(function (data, status, headers, config) {
              $rootScope.$broadcast('loading:hide');

              $log.debug(data);

              localStorage.removeItem('lastRoute');
              localStorage.removeItem('lastState');

              $state.go('authentication.login');

              $log.debug('getUserAuthenticated fail');

              $ionicSideMenuDelegate.toggleLeft();

              $scope.clearNewMarker();
            });
        }

        if (navigator && navigator.splashscreen) navigator.splashscreen.hide();

        switch ($state.current.name) {
          case $scope.MAP_INDEX:
          {

            $ionicPlatform.ready(function() {
              $ionicPlatform.registerBackButtonAction(function (e) {
                ionic.Platform.exitApp();
              }, 100);
            });

            $scope.listAllLayers();

            $ionicNavBarDelegate.showBackButton(false);

            $timeout(function () {

              $log.debug($('canvas').length);

              if ($('canvas').length == 0 || angular.equals($scope.map, {})) {

                $scope.initializeMap();



              } else {

                $scope.map.updateSize();

                $scope.toggleLastLayer();

              }

            });
            break;
          }
          case $scope.MAP_GALLERY:
          {
            $ionicNavBarDelegate.showBackButton(true);

            $ionicPlatform.ready(function() {
              $ionicPlatform.registerBackButtonAction(function (e) {
                $state.go($scope.MAP_MARKER);
              }, 100);
            });

            angular.forEach($scope.currentEntity.markerAttribute, function (markerAttribute) {
              if (markerAttribute.attribute.id == $scope.selectedPhotoAlbumAttribute.attribute.id)
                $scope.selectedPhotoAlbumAttribute = markerAttribute;
            });

            $scope.getPhotosByAttribute($scope.selectedPhotoAlbumAttribute);
            //$scope.listAllLayers();
            //
            break;
          }
          case $scope.MAP_MARKER:
          {
            $scope.imgResult = '';

            $ionicNavBarDelegate.showBackButton(true);

            $ionicPlatform.ready(function() {
              $ionicPlatform.registerBackButtonAction(function (e) {
                $state.go($scope.MAP_INDEX);
              }, 100);
            });

            $scope.removeLastPhoto();
            //$scope.listAllLayers();
            $scope.listAllInternalLayerGroups();
            $scope.getLastPhotoByMarkerId($scope.currentEntity.id);

            break;
          }

        }
      });


      /*-------------------------------------------------------------------
       * 		 				 	  HANDLERS
       *-------------------------------------------------------------------*/

      $scope.toggleLeftSideMenu = function () {
        $ionicSideMenuDelegate.toggleLeft();

        $scope.listAllInternalLayerGroups();
        $scope.getUserAuthenticated();

      };

      $scope.viewWMS = function () {
        $state.go($scope.MAP_WMS);
      };

      $scope.setImagePath = function (photo) {
        //$log.debug(image);
        if (photo != null && photo != '') {
          if(typeof photo === 'string' && photo.match(/broker/)) {
            return $rootScope.$API_ENDPOINT + photo.match(/\/broker.*/)[0];
          } else {

            if(photo.image != null || photo.source != null) {
              if (photo.image != undefined && photo.image.match(/broker/)) {
                return $rootScope.$API_ENDPOINT + photo.image.match(/\/broker.*/)[0];
              } else {
                return "data:image/png;base64," + photo.source;
              }
            }
          }
        }
      };

      $scope.getMarkerStatus = function (status) {
        return $translate('map.' + status.charAt(0).toUpperCase() + status.toLowerCase().slice(1));
      };

      $scope.clearNewMarker = function () {
        //if(!angular.equals($scope.currentEntity, {})) {
          $scope.map.removeLayer($scope.currentCreatingInternalLayer);
          $scope.currentCreatingInternalLayer = {};

          if (!angular.isDefined($scope.currentEntity.layer) || $scope.currentEntity.id) {
            $scope.currentEntity = {};
          }

          $scope.currentFeature = '';
        //}
      };

      $scope.removeRecursiveAttributes = function(markerAttribute) {

        angular.forEach(markerAttribute, function (attribute, index) {
          if (markerAttribute[index].photoAlbum != null) {
            if(markerAttribute[index].photoAlbum.markerAttribute != null) {
              markerAttribute[index].photoAlbum.markerAttribute = {
                attribute: {id: attribute.attribute.id},
                id: markerAttribute[index].photoAlbum.markerAttribute.id
              };
            }
            angular.forEach(markerAttribute[index].photoAlbum.photos, function (photos, idx) {
              if(angular.isDefined(markerAttribute[index].photoAlbum.photos[idx].photoAlbum) && angular.isDefined(markerAttribute[index].photoAlbum.photos[idx].photoAlbum.photos)) {
                angular.forEach(markerAttribute[index].photoAlbum.photos[idx].photoAlbum.photos, function (albumPhotos) {
                  albumPhotos.photoAlbum = {
                    id: albumPhotos.photoAlbum.id,
                    markerAttribute: {
                      id: albumPhotos.photoAlbum.markerAttribute.id,
                      attribute: {id: albumPhotos.photoAlbum.markerAttribute.attribute.id}
                    }
                  };
                });
              }
            });
          }
        });
        return markerAttribute;
      };

      $scope.viewMarker = function () {

        $log.debug('viewMarker');

        $scope.imgResult = '';

        $state.go($scope.MAP_MARKER);

        $scope.listAllInternalLayerGroups();

        $scope.showMarkerDetails = true;

        if (!$scope.currentEntity.id && !$scope.currentEntity.layer) {

          $scope.currentEntity.layer = $scope.allInternalLayerGroups[0];
          $scope.listAttributesByLayer($scope.currentEntity.layer);

        } else {

          $scope.getLastPhotoByMarkerId($scope.currentEntity.id);

        }

      };

      $scope.getLastPhotoByMarkerId = function (markerId) {

        var intervalPromise = $interval(function () {

          if (angular.isDefined(markerService)) {

            $interval.cancel(intervalPromise);

            markerService.lastPhotoByMarkerId(markerId, {
              callback: function (result) {

                $scope.imgResult = result.image;
                $scope.lastPhoto = result;
                $scope.$apply();

              },
              errorHandler: function (message, exception) {

                $scope.imgResult = null;
                $log.debug(message);
                $scope.$apply();
              }
            });
          }
        }, 500);

      };

      /*
       * GALLERY
       */
      $scope.getPhotosByAttribute = function (attribute, reload) {

        if (angular.isDefined(attribute) && attribute != null) {
          if (attribute.photoAlbum != null) {
            angular.forEach(attribute.photoAlbum.photos, function (photo) {
              if (photo.id) {
                photo.image = null;
              }
            });
          }

          if (angular.equals($scope.selectedPhotoAlbumAttribute, {}) || reload === true) {
            $scope.selectedPhotoAlbumAttribute = attribute;
          }

          var attr = $filter('filter')($scope.currentEntity.markerAttribute, {
            id: attribute.id
          })[0];

          $scope.attributeIndex = $scope.currentEntity.markerAttribute.indexOf(attr);

          var intervalPromise = $interval(function () {

            if (angular.isDefined(markerService)) {

              $interval.cancel(intervalPromise);

              markerService.findPhotoAlbumByAttributeMarkerId(attribute.id, null, {
                callback: function (result) {

                  if (attribute.photoAlbum != null) {
                    angular.forEach(result.content, function (photo) {

                      if (angular.isDefined(attribute.photoAlbum.photos)) {
                        var photoAttr = $filter('filter')(attribute.photoAlbum.photos, {
                          id: photo.id
                        })[0];

                        if (photoAttr) {
                          photoAttr.image = photo.image;
                        }
                      }

                    });
                  } else {
                    attribute.photoAlbum = result.content[0].photoAlbum;
                    attribute.photoAlbum.photos = result.content;
                  }

                  $scope.$apply();

                },
                errorHandler: function (message, exception) {
                  $log.debug(message);
                  $scope.$apply();
                }
              });
            }
          }, 500);
        }
      };

      $scope.showNewMarker = function () {

        if(!angular.equals($scope.currentEntity, {})) {
          var iconStyle = new ol.style.Style({
            image: new ol.style.Icon({
              anchor: [0.5, 1],
              anchorXUnits: 'fraction',
              anchorYUnits: 'fraction',
              src: $rootScope.$API_ENDPOINT + '/static/images/new_marker.png'
            })
          });

          var iconFeature = new ol.Feature({
            geometry: new ol.geom.Point([$scope.currentEntity.latitude, $scope.currentEntity.longitude])
          });

          var layer = new ol.layer.Vector({
            source: new ol.source.Vector({
              features: [iconFeature]
            })
          });

          layer.setStyle(iconStyle);

          $scope.currentCreatingInternalLayer = layer;

          if(!$scope.currentEntity.id)
            $scope.map.addLayer(layer);

          //$scope.$apply();
        }
      };

      $scope.onHold = function (evt) {

        $scope.listAllLayers();
        $scope.listAllInternalLayerGroups();
        $scope.getUserAuthenticated();

        $scope.currentWMS = {};

        $scope.isDisabled = false;

        $scope.clearShadowFeature($scope.currentFeature);
        $scope.currentFeature = '';

        angular.element(document.getElementsByTagName('ion-pull-up-handle')).height($scope.pullUpHeight + 'px');

        angular.element(document.getElementsByTagName('ion-pull-up-handle')).css('top', '-' + $scope.pullUpHeight + 'px');

        $scope.clearNewMarker();

        $scope.isNewMarker = true;

        if (!angular.isDefined($scope.currentEntity.layer)) {
          $scope.currentEntity = new Marker();
        }

        var coordinate = $scope.map.getCoordinateFromPixel([evt.gesture.center.pageX, evt.gesture.center.pageY - 44]);
        var transformed_coordinate = ol.proj.transform(coordinate, 'EPSG:900913', 'EPSG:4326');

        $scope.currentEntity.lat = transformed_coordinate[1];
        $scope.currentEntity.long = transformed_coordinate[0];

        $scope.currentEntity.latitude = coordinate[0];
        $scope.currentEntity.longitude = coordinate[1];

        $scope.showNewMarker();

        $scope.setMarkerCoordinatesFormat();

        $scope.currentEntity.status = 'PENDING';

        $rootScope.$broadcast('loading:hide');

      };

      $scope.initializeMap = function () {

        $timeout(function(){
          $log.debug('initializeMap');
          /**
           * Setting the background layer - OSM
           */
          $scope.rasterOSM = new ol.layer.Tile({
            source: new ol.source.OSM()
          });

          $scope.view = new ol.View({
            center: ol.proj.transform($scope.lastCenterPosition, 'EPSG:4326', 'EPSG:3857'),
            zoom: 9,
            minZoom: 3
          });

          $scope.map = new ol.Map({
            controls: [],
            interactions: ol.interaction.defaults({
              dragPan: $scope.dragPan,
              mouseWheelZoom: true,
              pinchRotate: false
            }),
            target: 'map',
            view: $scope.view
          });

          $scope.map.addLayer($scope.rasterOSM);
          $scope.rasterOSM.setVisible(true);

          /**
           * Map event on Move end to save last center position
           */
          $scope.map.on('moveend', function (evt) {

            var center = ol.proj.transform($scope.view.getCenter(), 'EPSG:3857', 'EPSG:4326');
            //$scope.mapGoogle.setCenter(new google.maps.LatLng(center[1], center[0]));
            localStorage.setItem('lastCenterPosition', angular.toJson([center[0], center[1]]));

          });

          /**
           * Click event to prompt the geoserver the information layer of the clicked coordinate
           */
          $scope.map.on('click', function (evt) {

            $log.debug('openlayers.map.singleclick');

            /*if ($state.current.name != $scope.MAP_INDEX)
             $state.go($scope.MAP_INDEX);*/

            $scope.currentWMS = {};

            localStorage.removeItem('currentEntity');

            if (!$scope.isNewMarker) {
              $scope.currentEntity = {};
              $scope.clearShadowFeature($scope.currentFeature);
              $scope.clearNewMarker();
              $scope.currentFeature = '';
              $scope.$apply();
            }

            var feature = $scope.map.forEachFeatureAtPixel(evt.pixel, function (feature, olLayer) {
              if (angular.isDefined(feature.getProperties().marker)) {
                return feature;
              } else {
                $scope.currentEntity = {};
              }
            });

            if (!angular.isDefined(feature) && !$scope.isNewMarker) {
              angular.forEach($scope.layers, function (layer) {

                if (layer.wmsLayer.getVisible()) {
                  var url = layer.wmsSource.getGetFeatureInfoUrl(evt.coordinate, $scope.view.getResolution(), $scope.view.getProjection(), {
                    'INFO_FORMAT': 'application/json'
                  });

                  $scope.getFeatureProperties(decodeURIComponent(url), layer.wmsLayer.getProperties().layer);
                }
              });
            }

            if (angular.isDefined(feature) && !$scope.isNewMarker) {

              $rootScope.$broadcast('loading:show');

              $scope.imgResult = '';
              $scope.selectedPhotoAlbumAttribute = {};

              $scope.isDisabled = true;

              $scope.currentEntity = feature.getProperties().marker;

              if ($scope.currentEntity.status != 'ACCEPTED' && $scope.currentEntity.user.id == $scope.userMe.id) {
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
              $scope.currentEntity.lat = transformed_coordinate[1];
              $scope.currentEntity.long = transformed_coordinate[0];

              $scope.setMarkerCoordinatesFormat();

              $scope.currentFeature = feature;

              markerService.listAttributeByMarker($scope.currentEntity.id, {

                callback: function (result) {

                  $scope.currentEntity.markerAttribute = result;

                  angular.forEach($scope.currentEntity.markerAttribute, function (markerAttribute, index) {

                    markerAttribute.name = markerAttribute.attribute.name;
                    markerAttribute.type = markerAttribute.attribute.type;
                    markerAttribute.required = markerAttribute.attribute.required;

                    if (markerAttribute.attribute.type == "NUMBER") {
                      markerAttribute.value = parseInt(markerAttribute.value);
                    }

                    if (markerAttribute.attribute.type == 'PHOTO_ALBUM')
                      $scope.getPhotosByAttribute(markerAttribute, index);

                  });

                  layerGroupService.listAttributesByLayer($scope.currentEntity.layer.id, {

                    callback: function (result) {

                      $rootScope.$broadcast('loading:hide');

                      $scope.attributesByLayer = [];

                      angular.forEach(result, function (attribute, index) {

                        var exist = false;

                        angular.forEach($scope.currentEntity.markerAttribute, function (attributeByMarker, index) {

                          if (attributeByMarker.attribute.id == attribute.id) {
                            exist = true;
                          }
                        });

                        if (!exist) {

                          $scope.currentEntity.markerAttribute.push({
                            attribute: attribute,
                            marker: {id: $scope.currentEntity.id},
                            type: attribute.type,
                            name: attribute.name
                          });

                        }

                      });

                      $scope.$apply();
                    },
                    errorHandler: function (message, exception) {
                      $log.debug(message);
                      $rootScope.$broadcast('loading:hide');
                      $scope.$apply();
                    }
                  });

                  $scope.$apply();

                },
                errorHandler: function (message, exception) {
                  $log.debug(message);
                  $rootScope.$broadcast('loading:hide');
                  $scope.$apply();
                }
              });

              $scope.$apply();

            }

            if ($scope.isNewMarker) {
              $scope.isNewMarker = false;
            }

          });

          $timeout(function(){
            if(!angular.equals($scope.allLayers, []))
              $scope.toggleLastLayer();

            $scope.showNewMarker();

            if(!angular.equals($scope.allLayers, []))
              $scope.loadSelectedLayers();
          });
        });

      };

      $scope.clearShadowFeature = function (feature) {

        if (feature)
          feature.setStyle(feature.getStyle()[0]);

      };

      $scope.setShadowMarker = function (type) {

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

      $scope.setMarkerCoordinatesFormat = function () {
        if (angular.isDefined($scope.currentEntity.lat)) {
          if ($scope.coordinatesFormat == 'DEGREES_DECIMAL') {
            $scope.currentEntity.formattedLatitude = $scope.currentEntity.lat.toFixed(6);
            $scope.currentEntity.formattedLongitude = $scope.currentEntity.long.toFixed(6);
          } else {
            $scope.currentEntity.formattedLatitude = $scope.convertDDtoDMS($scope.currentEntity.lat, true);
            $scope.currentEntity.formattedLongitude = $scope.convertDDtoDMS($scope.currentEntity.long, false);
          }
        }
      };

      $scope.minEscalaToMaxResolutionn = function (minEscalaMapa) {

        switch (minEscalaMapa) {
          case 'UM500km':
            return 78271.51696402048;
          case 'UM200km':
            return 78271.51696402048;
          case 'UM100km':
            return 4891.96981025128;
          case 'UM50km':
            return 2445.98490512564;
          case 'UM20km':
            return 1222.99245256282;
          case 'UM10km':
            return 611.49622628141;
          case 'UM5km':
            return 152.8740565703525;
          case 'UM2km':
            return 76.43702828517625;
          case 'UM1km':
            return 38.21851414258813;
          case 'UM500m':
            return 19.109257071294063;
          case 'UM200m':
            return 9.554628535647032;
          case 'UM100m':
            return 4.777314267823516;
          case 'UM50m':
            return 2.388657133911758;
          case 'UM20m':
            return 1.194328566955879;
          default:
            return 78271.51696402048;
        }
      };

      /**
       Converts the value scale stored in the db to open layes zoom forma
       */
      $scope.maxEscalaToMinResolutionn = function (maxEscalaMapa) {

        switch (maxEscalaMapa) {
          case 'UM500km':
            return 19567.87924100512;
          case 'UM200km':
            return 4891.96981025128;
          case 'UM100km':
            return 2445.98490512564;
          case 'UM50km':
            return 1222.99245256282;
          case 'UM20km':
            return 305.748113140705;
          case 'UM10km':
            return 152.8740565703525;
          case 'UM5km':
            return 76.43702828517625;
          case 'UM2km':
            return 38.21851414258813;
          case 'UM1km':
            return 19.109257071294063;
          case 'UM500m':
            return 9.554628535647032;
          case 'UM200m':
            return 4.777314267823516;
          case 'UM100m':
            return 2.388657133911758;
          case 'UM50m':
            return 1.194328566955879;
          case 'UM20m':
            return 0.0005831682455839253;
          default:
            return 0.0005831682455839253;
        }
      };

      $scope.getFeatureProperties = function (url, layer) {

        if (url != undefined && url != '') {
          $rootScope.$broadcast('loading:show');

          $http({
            method: 'GET',
            url: url
          }).then(function successCallback(response) {

            $rootScope.$broadcast('loading:hide');

            //$log.debug(response);

            if (response.data.features.length != 0) {

              $scope.currentWMS.layer = layer;
              $scope.currentWMS.attributes = [];

              angular.forEach(response.data.features[0].properties, function (attribute, key) {

                $scope.currentWMS.attributes.push({name: key, value: attribute});

              });

              $scope.showWMSDetails = true;
            }

          }, function errorCallback(response) {
          });
        }

      };

      $scope.convertDDtoDMS = function (coordinate, latitude) {
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


      $scope.toggleLayer = function (layer, removeCurrentEntity) {

        $log.debug('toggleLayer');

        if(angular.isDefined(layer.id)) {
          if (removeCurrentEntity && !angular.equals($scope.currentEntity, {}) && $scope.currentEntity.id) {
            $scope.currentEntity = {};
          }

          if(angular.isDefined($filter('filter')($scope.allLayers, {id: layer.id})[0]))
            $filter('filter')($scope.allLayers, {id: layer.id})[0].visible = layer.visible;

          if ($filter('filter')($scope.allLayers, {visible: true}).length > 3) {

            layer.visible = false;

            $cordovaToast.showShortBottom($translate('mobile.map.Maximum-selections')).then(function (success) {
            }, function (error) {
            });

          } else {

            angular.forEach($scope.map.getLayers(), function (group) {

              if (group instanceof ol.layer.Group) {
                var prop = group.getProperties();

                if (prop.id == layer.id) {
                  $scope.map.removeLayer(group);
                  //group.setVisible(layer.visible);
                }
              }

              if (group instanceof ol.layer.Tile) {
                var prop = group.getProperties();
                if (prop.layer && prop.layer.id == layer.id) {
                  group.setVisible(layer.visible);
                  $scope.map.removeLayer(group);
                }
              }
            });

            if (layer.visible) {

              $rootScope.$broadcast('loading:show');

              var hasSelectedLayer = $filter('filter')($scope.selectedLayers, {id: layer.id})[0];

              if (!angular.isDefined(hasSelectedLayer)) {
                $scope.selectedLayers.push({id: layer.id, visible: true});
                localStorage.setItem('selectedLayers', angular.toJson($scope.selectedLayers));
              }

              layer = $filter('filter')($scope.allLayers, {id: layer.id})[0];

              if (layer.dataSource != null && layer.dataSource.url != null) {

                var wmsOptions = {
                  url: layer.dataSource.url.split("ows?")[0] + 'wms',
                  params: {
                    'LAYERS': layer.name
                  },
                  serverType: 'geoserver'
                };

                if (layer.dataSource.url.match(/&authkey=(.*)/))
                  wmsOptions.url += "?" + layer.dataSource.url.match(/&authkey=(.*)/)[0];

                var wmsSource = new ol.source.TileWMS(wmsOptions);

                var wmsLayer = new ol.layer.Tile({
                  layer: layer,
                  source: wmsSource,
                  maxResolution: $scope.minEscalaToMaxResolutionn(layer.minimumScaleMap),
                  minResolution: $scope.maxEscalaToMinResolutionn(layer.maximumScaleMap)
                });

                $scope.layers.push({wmsSource: wmsSource, wmsLayer: wmsLayer});

                $scope.map.addLayer(wmsLayer);

                $rootScope.$broadcast('loading:hide');

              } else {

                markerService.listMarkerByLayer(layer.id, {
                  callback: function (result) {

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
                      angular.forEach(result, function (marker, index) {

                        var icons = [];

                        icons.push(iconStyle);

                        var iconFeature = new ol.Feature({
                          geometry: new ol.format.WKT().readGeometry(marker.location.coordinateString),
                          marker: marker
                        });

                        if ($scope.lastCurrentEntity.id == marker.id) {

                          $scope.clearNewMarker();

                          $scope.currentFeature = iconFeature;

                          var shadowType = 'default';
                          if (!result[0].layer.icon.match(/default/))
                            shadowType = 'collection';

                          var shadowStyle = $scope.setShadowMarker(shadowType);

                          icons.push(shadowStyle);

                          $scope.map.getView().setCenter(iconFeature.getGeometry().getCoordinates());

                          $scope.currentEntity = {};
                          $scope.lastCurrentEntity = {};

                          localStorage.removeItem('currentEntity');
                          localStorage.removeItem('selectedPhotoAlbumAttribute');
                          localStorage.removeItem('lastState');
                          localStorage.removeItem('lastRoute');

                        }

                        iconFeature.setStyle(icons);

                        var vectorSource = new ol.source.Vector({
                          features: [iconFeature]
                        });

                        var vectorLayer = new ol.layer.Vector({
                          source: vectorSource,
                          layer: layer.id,
                          maxResolution: $scope.minEscalaToMaxResolutionn(marker.layer.minimumScaleMap),
                          minResolution: $scope.maxEscalaToMinResolutionn(marker.layer.maximumScaleMap)
                        });

                        markers.push(vectorLayer);

                      });

                      var group = new ol.layer.Group({
                        layers: markers,
                        id: layer.id
                      });

                      $scope.map.addLayer(group);

                      $rootScope.$broadcast('loading:hide');

                      $scope.$apply();
                    } else {

                      $rootScope.$broadcast('loading:hide');

                      $cordovaToast.showShortBottom('Nenhum ponto encontrado').then(function (success) {
                      }, function (error) {
                      });

                    }

                  },
                  errorHandler: function (message, exception) {
                    $log.debug(message);
                    $rootScope.$broadcast('loading:hide');

                    if (message.match(/Incomplete reply from server/ig))
                      $rootScope.$broadcast('$cordovaNetwork:offline');

                    $scope.$apply();
                  }
                });
              }

            } else {

              var selectedLayer = $filter('filter')($scope.selectedLayers, {id: layer.id})[0];

              $scope.selectedLayers.splice($scope.selectedLayers.indexOf(selectedLayer), 1);
              localStorage.setItem('selectedLayers', angular.toJson($scope.selectedLayers));

              //$scope.map.removeLayer(layer.layer);
            }
          }
        }
      };

      $scope.toggleLastLayer = function() {

        $log.debug('toggleLastLayer');

        if (angular.isDefined($scope.lastCurrentEntity) && angular.isDefined($scope.lastCurrentEntity.layer)) {
          $scope.internalLayer = $filter('filter')($scope.allLayers, {id: $scope.lastCurrentEntity.layer.id})[0];

          if (angular.isDefined($scope.internalLayer.visible)) {

            $scope.internalLayer.visible = false;
            $scope.toggleLayer($scope.internalLayer);
            $scope.internalLayer.visible = true;
            $scope.toggleLayer($scope.internalLayer);

          } else {

            $scope.internalLayer.visible = true;
            $scope.toggleLayer($scope.internalLayer, true);

          }

          $scope.internalLayer = {};

        }

      };

      $scope.listAllLayers = function () {

        $log.debug('listAllLayers');

        if ($scope.allLayers.length == 0) {

          $rootScope.$broadcast('loading:show');

          var intervalPromise = $interval(function () {

            if (angular.isDefined(layerGroupService)) {

              layerGroupService.listLayersByFilters(null, null, {
                callback: function (result) {
                  $scope.allLayers = result.content;

                  $rootScope.$broadcast('loading:hide');

                  $scope.toggleLastLayer();

                  $interval.cancel(intervalPromise);

                  $scope.loadSelectedLayers();

                  $scope.$apply();
                },
                errorHandler: function (message, exception) {
                  $log.debug(message);

                  $interval.cancel(intervalPromise);

                  $rootScope.$broadcast('loading:hide');

                  if (message.match(/Incomplete reply from server/ig))
                    $rootScope.$broadcast('$cordovaNetwork:offline');

                  $scope.$apply();
                }
              });
            }

          }, 500);

        }
      };

      /**
       *
       */
      $scope.listAttributesByLayer = function (layer, reload) {

        $rootScope.$broadcast('loading:show');

        $scope.selectedPhotoAlbumAttribute = {};

        if (!$scope.currentEntity.markerAttribute || $scope.currentEntity.markerAttribute.length == 0 || reload) {

          $scope.currentEntity.markerAttribute = [];

          layerGroupService.listAttributesByLayer(layer.id, {
            callback: function (result) {

              angular.forEach(result, function (layerAttribute, index) {

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
              $rootScope.$broadcast('loading:hide');

              //$scope.currentEntity.markerAttribute = result;

              $scope.$apply();
            },
            errorHandler: function (message, exception) {
              $rootScope.$broadcast('loading:hide');
              $log.debug(message);

              if (message.match(/Incomplete reply from server/ig))
                $rootScope.$broadcast('$cordovaNetwork:offline');


              $scope.$apply();
            }
          });
        }
      };

      $scope.removeAllLayers = function () {

        angular.forEach($scope.map.getLayers(), function (group) {

          if (group instanceof ol.layer.Group) {
            $scope.map.removeLayer(group);
          }

          if (group instanceof ol.layer.Tile) {
            $scope.map.removeLayer(group);
          }
        });

        /*angular.forEach($scope.allInternalLayerGroups, function (group) {
          if (group.visible) {
            group.visible = false;
            $scope.toggleLayer(group);
          }
        });*/

        $scope.map = {};

      };

      /**
       * Prepara o estado, retira o password criptografado do usuário
       */
      $scope.logout = function () {

        $ionicSideMenuDelegate.toggleLeft();

        $scope.userMe = {};

        /*$scope.allLayers = {};
         $scope.allInternalLayerGroups = {};*/

        $scope.removeAllLayers();

        localStorage.removeItem('userEmail');
        localStorage.removeItem('token');
        localStorage.removeItem('currentEntity');
        localStorage.removeItem('lastState');
        localStorage.removeItem('lastRoute');
        localStorage.removeItem('selectedLayers');
        localStorage.removeItem('lastCenterPosition');
        localStorage.removeItem('selectedPhotoAlbumAttribute');

        $('#map').html('');

        $scope.internalLayer = {};

        $http({
          method: 'GET',
          url: $rootScope.$API_ENDPOINT + '/j_spring_security_logout'
        }).then(function successCallback(response) {

          //$state.go('authentication.login');

          $location.path('/authentication/login');

        }, function errorCallback(response) {

          $log.debug(response);

        });

        //Realiza o logout do google plus
        window.plugins.googleplus.disconnect(
          function (msg) {
            $log.debug(msg);
          }
        );
      };

      $scope.$on('userMe', function (event, data) {

        $scope.allInternalLayerGroups = [];
        $scope.allLayers = [];

        $scope.userMe = data;

      });


      /* INDEX */

      $scope.saveMarker = function (form) {

        $scope.internalLayer = $filter('filter')($scope.allLayers, {id: $scope.currentEntity.layer.id})[0];

        if (!form.$valid) {

          $scope.isFormSubmit = true;

        } else {

          $scope.isFormSubmit = false;

          if ($scope.currentEntity.id) {

            var isValid = true;

            angular.forEach($scope.currentEntity.markerAttribute, function (attribute, index) {

              if (attribute.type == 'PHOTO_ALBUM' && attribute.required && (attribute.photoAlbum == null || attribute.photoAlbum.photos.length == 0)) {

                $scope.selectedPhotoAlbumAttribute = attribute;
                $state.go($scope.MAP_GALLERY);

                isValid = false;

                $cordovaToast.showShortBottom($translate('photos.Insert-Photos-in-attribute', attribute.name)).then(function (success) {
                }, function (error) {
                });
              }

              if (isValid && attribute.type == 'PHOTO_ALBUM' && attribute.photoAlbum != null) {

                angular.forEach(attribute.photoAlbum.photos, function (photo, index) {

                  delete photo.image;

                  if (photo.deleted)
                    delete attribute.photoAlbum.photos[index];
                });
              }
            });

            if (isValid) {

              $rootScope.$broadcast('loading:show');

              $scope.currentEntity.status = 'PENDING';

              var olCoordinates = ol.proj.transform([$scope.currentEntity.long, $scope.currentEntity.lat], 'EPSG:4326', 'EPSG:900913');

              $scope.currentEntity.wktCoordenate = new ol.format.WKT().writeGeometry(new ol.geom.Point([olCoordinates[0], olCoordinates[1]]));

              markerService.updateMarker($scope.currentEntity, {

                callback: function (result) {

                  $scope.lastCurrentEntity = result;

                  /*$scope.internalLayer.visible = false;
                  $scope.toggleLayer($scope.internalLayer);
                  $scope.internalLayer.visible = true;
                  $scope.toggleLayer($scope.internalLayer);*/

                  //$scope.clearNewMarker();

                  $scope.clearShadowFeature($scope.currentFeature);
                  $scope.currentFeature = '';

                  $state.go($scope.MAP_INDEX);
                  $rootScope.$broadcast('loading:hide');

                  $cordovaToast.showShortBottom($translate('map.Mark-updated-succesfully')).then(function (success) {
                  }, function (error) {
                  });

                  $scope.$apply();
                },
                errorHandler: function (message, exception) {

                  $log.debug(message);
                  $state.go($scope.MAP_INDEX);
                  $rootScope.$broadcast('loading:hide');

                  $scope.$apply();
                }
              });
            }

          } else {

            var isValid = true;

            angular.forEach($scope.currentEntity.markerAttribute, function (attribute, index) {

              if (attribute.type == 'PHOTO_ALBUM' && attribute.required && attribute.photoAlbum == null) {

                $scope.selectedPhotoAlbumAttribute = attribute;
                $state.go($scope.MAP_GALLERY);

                isValid = false;

                $cordovaToast.showShortBottom($translate('photos.Insert-Photos-in-attribute', attribute.name)).then(function (success) {
                }, function (error) {
                });
              }

            });

            if (isValid) {

              $rootScope.$broadcast('loading:show');

              var layer = new Layer();
              layer.id = $scope.currentEntity.layer.id;
              $scope.currentEntity.layer = layer;

              var attributes = $scope.currentEntity.markerAttribute;
              $scope.currentEntity.markerAttribute = [];

              angular.forEach(attributes, function (attr, ind) {

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

                    angular.forEach(attr.photoAlbum.photos, function (file) {

                      var photo = new Photo();
                      photo.source = file.source;
                      photo.name = file.name;
                      photo.description = file.description;
                      photo.contentLength = file.contentLength ? file.contentLength : file.source.length;
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

              var olCoordinates = ol.proj.transform([$scope.currentEntity.long, $scope.currentEntity.lat], 'EPSG:4326', 'EPSG:900913');
              $scope.currentEntity.wktCoordenate = new ol.format.WKT().writeGeometry(new ol.geom.Point([olCoordinates[0], olCoordinates[1]]));

              $log.debug('Insert current');
              $log.debug($scope.currentEntity);

              markerService.insertMarker($scope.currentEntity, {
                callback: function (result) {

                  $scope.lastCurrentEntity = result;



                  $rootScope.$broadcast('loading:hide');

                  $state.go($scope.MAP_INDEX);
                  $scope.$apply();

                  /*if ($scope.internalLayer.visible && $scope.internalLayer.visible != undefined) {

                   $scope.internalLayer.visible = false;
                   $scope.toggleLayer($scope.internalLayer);
                   $scope.internalLayer.visible = true;
                   $scope.toggleLayer($scope.internalLayer);

                   } else {

                   $scope.internalLayer.visible = true;
                   $scope.toggleLayer($scope.internalLayer);

                   }*/

                  $cordovaToast.showShortBottom($translate('map.Mark-inserted-succesfully')).then(function (success) {
                  }, function (error) {
                  });

                },
                errorHandler: function (message, exception) {
                  $log.debug(message);
                  $state.go($scope.MAP_INDEX);
                  $rootScope.$broadcast('loading:hide');
                  $scope.$apply();
                }
              });
            }
          }
        }
      };

      $scope.verifyStatus = function () {
        if (($scope.currentEntity.status == 'SAVED' || $scope.currentEntity.status == 'REFUSED' || $scope.currentEntity.status == 'CANCELED')
          && ($scope.currentEntity.user.id == $scope.userMe.id)) {
          $scope.isDisabled = false;
        }
      };

      $scope.setSelectValue = function (attribute) {
        if (attribute.value != undefined)
          return attribute.value;
        else if (attribute.required)
          return 'Yes';
        else
          return '';
      };

      $scope.nextInput = function(event) {
        $log.debug(event);
        var nextInput = angular.element(event.target).parent().parent().next().find('input');
        if(nextInput.length) {
          if(event.keyCode === 13) {
            nextInput.focus();
            event.preventDefault();
            event.stopPropagation();
          }
        } else {
          if (event.keyCode === 13 || event.keyCode === 9) {
            $timeout(function () {

              angular.element('#sendForm').trigger('click');

              angular.element('#search').blur();

            }, 100);
          }
        }
      };
    });

}(window.angular));
