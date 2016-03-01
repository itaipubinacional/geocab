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
      $cordovaToast, $http) {

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
      $scope.allLayers = [];
      $scope.layers = [];
      $scope.newMarker = {};
      $scope.dragPan = true;
      $scope.layers = [];

      $scope.showMarkerDetails = false;
      $scope.showWMSDetails = false;

      $scope.currentEntity = {};
      $scope.isNewMarker = false;

      $scope.currentWMS = {};

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



      /*-------------------------------------------------------------------
       * 		 				 	  HANDLERS
       *-------------------------------------------------------------------*/

      $scope.goBack = function() {
        $ionicNavBarDelegate.back();
      };

      $scope.setImagePath = function(image) {
        //$log.debug(image);
        if (image != null && image != '') {
          if (image.match(/broker/)) {
            return $rootScope.$API_ENDPOINT + image.match(/\/broker.*/)[0];
          } else {
            return "data:image/png;base64," + image;
          }
        }
      };



      $scope.getMarkerStatus = function(status) {
        return $translate('map.' + status.charAt(0).toUpperCase() + status.toLowerCase().slice(1));
      };

      $scope.clearNewMarker = function() {
        $scope.map.removeLayer($scope.currentCreatingInternalLayer);
        $scope.currentCreatingInternalLayer = {};
        $scope.currentEntity = {};
        $scope.currentFeature = '';
      };

      $scope.footerWMSExpand = function() {
        $scope.showWMSDetails = true;
      };

      $scope.expandFooter = function() {
        $rootScope.$broadcast('expandFooter');
      };

      $scope.minimizeFooter = function() {
        $rootScope.$broadcast('minimizeFooter');
      };

      $scope.footerExpand = function() {

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

        if (attribute.photoAlbum != null) {
          angular.forEach(attribute.photoAlbum.photos, function(photo) {
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

        markerService.findPhotoAlbumByAttributeMarkerId(attribute.id, null, {
          callback: function(result) {

            if (attribute.photoAlbum != null) {
              angular.forEach(result.content, function(photo) {

                var photoAttr = $filter('filter')(attribute.photoAlbum.photos, {
                  id: photo.id
                })[0];

                if (photoAttr) {
                  photoAttr.image = photo.image;
                }

              });
            } else {
              attribute.photoAlbum = result.content[0].photoAlbum;
              attribute.photoAlbum.photos = result.content;
            }
            // angular.forEach(attribute.photoAlbum.photos, function(photo){
            // });

            // attribute.photoAlbum = result.content[0].photoAlbum;
            // attribute.photoAlbum.photos = result.content;
            // $scope.photos = result.content;

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

        $scope.listAllLayers();
        $scope.listAllInternalLayerGroups();
        $scope.getUserAuthenticated();

        //$log.debug('onHold');

        $scope.currentWMS = {};

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

          //$scope.minimizeFooter();

          $scope.currentWMS = {};

          if (!$scope.isNewMarker) {
            $scope.currentEntity = {};
            $scope.clearShadowFeature($scope.currentFeature);
            $scope.clearNewMarker();
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

            if(!angular.isDefined(feature) && !$scope.isNewMarker) {
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

              $scope.selectedPhotoAlbumAttribute = {};

              $scope.isDisabled = true;

              $scope.currentEntity = feature.getProperties().marker;

              if (($scope.currentEntity.status == 'SAVED' || $scope.currentEntity.status == 'REFUSED' || $scope.currentEntity.status == 'CANCELED')
                && ($scope.currentEntity.user.id == $scope.userMe.id)) {
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

                      $rootScope.$broadcast('loading:hide');

                      $scope.attributesByLayer = [];

                      angular.forEach(result, function(attribute, index) {

                        var exist = false;

                        angular.forEach($scope.currentEntity.markerAttribute, function(attributeByMarker, index) {

                          if (attributeByMarker.attribute.id == attribute.id) {
                            exist = true;
                          }
                        });

                        if (!exist) {

                          /*attribute.attribute = attribute;
                          attribute.marker = $scope.currentEntity;

                          attribute.id = null;*/

                          $scope.currentEntity.markerAttribute.push({attribute: attribute, marker: $scope.currentEntity, type: attribute.type, name: attribute.name});

                        }

                      });

                      //localStorage.setItem('currentEntity', angular.toJson($scope.currentEntity));

                      $scope.$apply();
                    },
                    errorHandler: function(message, exception) {
                      $log.debug(message);
                      $rootScope.$broadcast('loading:hide');
                      $scope.$apply();
                    }
                  });

                  $scope.$apply();

                },
                errorHandler: function(message, exception) {
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
        //$scope.listAllInternalLayerGroups();
      };

      $scope.onDragEnd = function(state) {

        $scope.isDrawerOpen = state == undefined ? $scope.isDrawerOpen : state;
        $scope.isDragStart = false;
        $scope.dragPan = state;
      };

      $scope.toggleDrawer = function() {
        $rootScope.$broadcast('toggleDrawer');
        //$scope.listAllInternalLayerGroups();
        $scope.isDrawerOpen = !$scope.isDrawerOpen;
        $scope.isDragStart = false;

        $scope.listAllLayers();
        $scope.listAllInternalLayerGroups();
        $scope.getUserAuthenticated();

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

      $scope.minEscalaToMaxResolutionn = function(minEscalaMapa) {

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
      $scope.maxEscalaToMinResolutionn = function(maxEscalaMapa) {

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

      $scope.getFeatureProperties = function(url, layer) {

        if(url != undefined && url != '') {
          $rootScope.$broadcast('loading:show');

          $http({
            method: 'GET',
            url: url
          }).then(function successCallback(response) {

            $rootScope.$broadcast('loading:hide');

            //$log.debug(response);

            if(response.data.features.length != 0) {

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

        if ($filter('filter')($scope.allLayers, {
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

            if (group instanceof ol.layer.Tile) {

              if (group.getProperties().layer && group.getProperties().layer.id == layer.id) {
                layerExits = true;
                group.setVisible(layer.visible);
              }
            }
          });

          if (layer.visible && !layerExits) {

            $rootScope.$broadcast('loading:show');

            if (layer.dataSource.url != null) {

              var wmsOptions = {
                url: layer.dataSource.url.split("ows?")[0] + 'wms',
                params: {
                  'LAYERS': layer.name
                },
                serverType: 'geoserver',
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

                    $cordovaToast.showShortBottom('Nenhum ponto encontrado').then(function(success) {
                      // success
                    }, function(error) {
                      // error
                    });

                  }

                },
                errorHandler: function(message, exception) {
                  $log.debug(message);
                  $rootScope.$broadcast('loading:hide');
                  $scope.$apply();
                }
              });
            }

          } else {
            $scope.map.removeLayer(layer.layer);
          }
        }
      };

      $scope.listAllLayers = function() {

        if ($scope.allLayers.length == 0) {

          $rootScope.$broadcast('loading:show');

          layerGroupService.listLayersByFilters(null, null, {
            callback: function(result) {
              $scope.allLayers = result.content;
              $scope.$apply();
            },
            errorHandler: function(message, exception) {
              $log.debug(message);
              $rootScope.$broadcast('loading:hide');
              $scope.$apply();
            }
          });
        }

      };

      /**
       *
       */
      $scope.listAllInternalLayerGroups = function() {

        if ($scope.allInternalLayerGroups.length == 0) {
          layerGroupService.listAllInternalLayerGroups({
            callback: function(result) {
              $scope.allInternalLayerGroups = result;
              $log.debug($scope.allInternalLayerGroups);

              $rootScope.$broadcast('loading:hide');

              $scope.$apply();
            },
            errorHandler: function(message, exception) {
              $log.debug(message);
              $rootScope.$broadcast('loading:hide');
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
              $log.debug(message);
              $scope.$apply();
            }
          });
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

      /**
       * Prepara o estado, retira o password criptografado do usuário
       */
      $scope.logout = function() {
        $scope.toggleDrawer();
        $scope.removeAllSelectedLayers();

        localStorage.removeItem('userEmail');
        localStorage.removeItem('token');
        $location.path($rootScope.$API_ENDPOINT + "/j_spring_security_logout");

        //Realiza o logout do google plus
        window.plugins.googleplus.disconnect(
          function(msg) {
            console.log(msg);
          }
        );
      };

      /**
       * authenticated user
       * */
      $scope.getUserAuthenticated = function() {
        if(angular.equals($scope.userMe, {})) {
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
        }
      };

      $scope.$on('userMe', function(event, data){

        $scope.allInternalLayerGroups = [];
        $scope.allLayers = [];

        $scope.userMe = data;

      });

      $scope.onDragStart = function() {
        $scope.listAllLayers();
        $scope.listAllInternalLayerGroups();
        $scope.getUserAuthenticated();
      }

    });

}(window.angular));
