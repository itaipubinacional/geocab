(function (angular) {
  'use strict';

  /**
   *
   * @param $scope
   * @param $state
   */
  angular.module('application')
    .controller('MapController', function ($rootScope, $scope, $translate, $state, $document, $importService, $ionicGesture, $ionicPopup, $ionicSideMenuDelegate, Camera, $timeout, $cordovaDatePicker, $cordovaGeolocation, $filter, $log) {


      /**
       *
       */
      $timeout(function () {
        $importService("accountService");
        $importService("layerGroupService");
        $importService("markerService");
      });


      /*-------------------------------------------------------------------
       * 		 				 	ATTRIBUTES
       *-------------------------------------------------------------------*/

      $scope.direction = '';
      $scope.isNewMarker = false;
      $scope.isDragStart = false;
      $scope.isDrawerOpen = false;
      $scope.allInternalLayerGroups = [];
      $scope.layers = [];
      $scope.newMarker = {};

      $scope.pullUpHeight = 90;

      /**
       * Setting the background layer - OSM
       */
      $scope.rasterOSM = new ol.layer.Tile({
        source: new ol.source.OSM()
        //source: new ol.source.MapQuest({layer: 'osm'})
      });

      $scope.view = new ol.View({
        center: ol.proj.transform([-54.1394, -24.7568], 'EPSG:4326', 'EPSG:3857'),
        zoom: 9,
        minZoom: 3
      });

      $scope.map = new ol.Map({
        interactions: ol.interaction.defaults({
          dragPan: true,
          mouseWheelZoom: true
        }),
        target: 'map',
        view: $scope.view
      });

      $scope.map.addLayer($scope.rasterOSM);
      $scope.rasterOSM.setVisible(true);

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

      $scope.newMarkerStyle = {
        image: {
          icon: {
            anchor: [0.5, 1],
            anchorXUnits: 'fraction',
            anchorYUnits: 'fraction',
            opacity: 1,
            src: window.location.origin + '/static/images/default_red.png'
          }
        }
      };

      var style = {
        image: {
          icon: {
            anchor: [0.5, 1],
            anchorXUnits: 'fraction',
            anchorYUnits: 'fraction',
            opacity: 1,
            src: 'http://openlayers.org/en/v3.7.0/examples/data/icon.png'
          }
        }
      };

      var custom_style = {
        image: {
          icon: {
            anchor: [0.5, 1],
            anchorXUnits: 'fraction',
            anchorYUnits: 'fraction',
            opacity: 1,
            src: 'http://tombatossals.github.io/angular-openlayers-directive/examples/images/map-marker.png'
          }
        }
      };

      $scope.defaults = {
        interactions: {
          mouseWheelZoom: true
        },
        controls: {
          zoom: false,
          rotate: false
        },
        events: {
          map: ['singleclick', 'pointerdrag']
        }
      };

      $scope.center = {
        lon: -54.1394,
        lat: -24.7568,
        projection: 'EPSG:4326',
        zoom: 9,
        minZoom: 3
      };

      /*-------------------------------------------------------------------
       * 		 				 	  HANDLERS
       *-------------------------------------------------------------------*/

      $scope.onDragStart = function (event) {
        $log.debug('onDragStart');
        $scope.isDrawerOpen = !$scope.isDrawerOpen;
        $scope.isDragStart = true;
        $scope.defaults.interactions.dragPan = false;

        $scope.listAllInternalLayerGroups();
      };

      $scope.onDragEnd = function (event) {
        $log.debug('onDragEnd');
        $scope.isDrawerOpen = !$scope.isDrawerOpen;
        $scope.isDragStart = false;
        $scope.defaults.interactions.dragPan = true;
      };

      $scope.toggleDrawer = function () {
        $log.debug('toggleDrawer');

        $rootScope.$broadcast('toggleDrawer');

        $scope.listAllInternalLayerGroups();

        $scope.isDrawerOpen = !$scope.isDrawerOpen;

      };

      $ionicGesture.on('drag', function (e) {
        $scope.$apply(function () {
          $scope.direction = e.gesture.direction;
        });

      }, $document);

      $scope.$on('openlayers.map.pointerdrag', function (event, data) {

        /*$log.debug($scope.isDragStart);
         $log.debug(data.event.pixel);
         $log.debug($scope.defaults.interactions.dragPan);
         $log.debug($scope.direction);*/

        if (data.event.pixel[0] < 40 || !$scope.defaults.interactions.dragPan || $scope.isDragStart) {

          //$log.debug(data.event.pixel);

          if ($scope.direction === 'right') {
            data.event.preventDefault();

            $scope.$apply(function () {
              $scope.defaults.interactions.dragPan = false;
            });

          } else {

            $scope.$apply(function () {
              $scope.defaults.interactions.dragPan = true;
            });
          }

        }

      });

      $scope.toggleLayer = function (layer) {

        var indexOf = $scope.layers.indexOf($filter('filter')($scope.layers, {id: layer.id})[0]);

        if($filter('filter')($scope.layers, {id: layer.id}).length == 0) {
          var addLayer = {id: layer.id, name: layer.name, visible: true, markers: []};

          markerService.listMarkerByLayer(layer.id, {
            callback: function (result) {

              /*var iconPath = '/static/images/marker.png';

              if (result.length > 0) {
                iconPath = '/' + result[0].layer.icon
              }

              var iconStyle = {
                image: {
                  icon: {
                    anchor: [0.5, 1],
                    anchorXUnits: 'fraction',
                    anchorYUnits: 'fraction',
                    src: $rootScope.$API_ENDPOINT + iconPath
                  }
                }
              };

              angular.forEach(result, function (marker, index) {

                var latlon = marker.location.coordinateString.match(/\((.*)\s(.*)\)/);
                var coordinates = ol.proj.transform([latlon[1], latlon[2]], 'EPSG:3857', 'EPSG:4326');

                marker.lat = coordinates[1];
                marker.lon = coordinates[0];
                marker.style = iconStyle;
                marker.projection = 'EPSG:4326';
                marker.layer = layer;

                addLayer.markers.push(marker);

              });

              $scope.layers.push(addLayer);*/

              var iconPath = "static/images/marker.png";

              if (result.length > 0) {
                iconPath = $rootScope.$API_ENDPOINT + '/'  + result[0].layer.icon
              }

              var iconStyle = new ol.style.Style({
                image: new ol.style.Icon(({
                  anchor: [0.5, 1],
                  anchorXUnits: 'fraction',
                  anchorYUnits: 'fraction',
                  src: iconPath
                }))
              });

              angular.forEach(result, function (marker, index) {

                var iconFeature = new ol.Feature({
                  geometry: new ol.format.WKT().readGeometry(marker.location.coordinateString),
                  marker: marker
                });

                var source = new ol.source.Vector({features: [iconFeature]});

                var layer = new ol.layer.Vector({
                  source: new ol.source.Vector({features: [iconFeature]})
                });

                layer.setStyle(iconStyle);

                $scope.map.addLayer(layer);
              });

              $scope.$apply();

            },
            errorHandler: function (message, exception) {
              $scope.message = {type: "error", text: message};
              $scope.$apply();
            }
          });

        } else {

          $scope.layers.splice(indexOf, 1);

        }

      };

      /**
       *
       */
      $scope.listAllInternalLayerGroups = function () {

        if($scope.allInternalLayerGroups.length == 0) {
          layerGroupService.listAllInternalLayerGroups({
            callback: function (result) {
              $scope.allInternalLayerGroups = result;

              $scope.toggleLayer($scope.allInternalLayerGroups[0]);

              $scope.allInternalLayerGroups[0].visible = true;

              $scope.$apply();
            },
            errorHandler: function (message, exception) {
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
      $scope.listAttributesByLayer = function (layer) {

        $scope.currentEntity.markerAttribute = [];

        layerGroupService.listAttributesByLayer(layer.id, {
          callback: function (result) {

            angular.forEach(result, function(layerAttributes){

              var attribute = new Attribute();

              attribute.id = layerAttributes.id;

              layerAttributes.id = null;
              layerAttributes.photoAlbum = null;

              layerAttributes.attribute = attribute;

              $scope.currentEntity.markerAttribute.push(layerAttributes);

            });

            //$scope.currentEntity.markerAttribute = result;

            $scope.$apply();
          },
          errorHandler: function (message, exception) {
            $ionicPopup.alert({
              title: 'Opss...',
              template: message
            });

            $scope.$apply();
          }
        });
      };

      /**
       *
       */
      $scope.getGPSPosition = function () {
        var posOptions = {timeout: 10000, enableHighAccuracy: true};
        $cordovaGeolocation
          .getCurrentPosition(posOptions)
          .then(function (position) {
            var lat = position.coords.latitude;
            var long = position.coords.longitude;

            $ionicPopup.alert({
              title: 'GPS funcionando',
              template: lat + ' ' + long
            });
          }, function (err) {
            $log.debug(err);
          });
      };

      $scope.$on('openlayers.map.singleclick', function (event, data) {

        $scope.footerMinimize();

        $log.debug('openlayers.map.singleclick');

        $log.debug($scope.isNewMarker);

        if($scope.isDrawerOpen) {

          $scope.toggleDrawer();

        } else {

          if ($scope.isNewMarker) {


            $scope.isNewMarker = false;

            $scope.pullUpHeight = 90;

            $scope.$apply(function (scope) {
              if (data) {

                scope.pullUpHeight = 90;

                var p = ol.proj.transform([data.coord[0], data.coord[1]], data.projection, 'EPSG:4326');

                var newMarker = {
                  name: 'Novo ponto',
                  lat: p[1],
                  lon: p[0],
                  projection: 'EPSG:4326',
                  visible: true
                };

                scope.newMarker = newMarker;

                scope.currentEntity = newMarker;

              }
            });

          } else {

            if(angular.isDefined($scope.newMarker.lat)) {
              $scope.newMarker.visible = false;
              $scope.currentEntity = {};

              $scope.footerMinimize();
              $scope.$apply();
            }

            var map = data.event.map;
            var pixel = data.event.pixel;
            var feature = map.forEachFeatureAtPixel(pixel, function (feature, olLayer) {
              if (angular.isDefined(feature.getProperties().marker)) {
                return feature;
              } else {
                $scope.currentEntity = {};
              }
            });
            if (angular.isDefined(feature)) {
              $scope.$broadcast('markers.click', feature, data.event);
              return;
            } else {
              $scope.currentEntity = {};
            }

          }
        }

        $log.debug('pullUpHeight: ' + $scope.pullUpHeight);

      });

      $scope.$on('markers.click', function (event, feature) {
        $log.debug('markers.click');
        $scope.$apply(function () {
          if (feature) {
            $scope.pullUpHeight = 60;
            $scope.currentEntity = feature.getProperties().marker;
            $log.debug($scope.currentEntity);
          }
        });
      });

      $scope.centerOnMe = function () {
        $log.debug("Centering");
        if (!$scope.map) {
          return;
        }

        $scope.loading = $ionicLoading.show({
          content: 'Getting current location...',
          showBackdrop: false
        });

        navigator.geolocation.getCurrentPosition(function (pos) {
          $log.debug('Got pos', pos);
          $scope.map.setCenter(new google.maps.LatLng(pos.coords.latitude, pos.coords.longitude));
          $scope.loading.hide();
        }, function (error) {
          alert('Unable to get location: ' + error.message);
        });
      };

      $scope.clearMarkerDetail = function () {
        $log.debug('clearMarkerDetail');
      };

      $scope.footerExpand = function () {

        $log.debug('pullUpHeight: ' + $scope.pullUpHeight);

        $log.debug('Footer expanded');

        $scope.listAllInternalLayerGroups();

        $scope.showMarkerDetails = true;

        $scope.currentEntity.layer = $scope.allInternalLayerGroups[0];
        $scope.listAttributesByLayer($scope.currentEntity.layer);

        /*if(!$scope.currentEntity.layer) {
          $timeout(function(){
            $scope.currentEntity.layer = $scope.currentEntity.layer;
          }, 500);
        }*/

        if(angular.isDefined($scope.currentEntity.id)) {

          markerService.listAttributeByMarker($scope.currentEntity.id, {
            callback: function (result) {

              $scope.currentEntity.markerAttribute = result;

              angular.forEach($scope.currentEntity.markerAttribute, function (markerAttribute, index) {

                markerAttribute.type = markerAttribute.attribute.type;

                if (markerAttribute.attribute.type == "NUMBER") {
                  markerAttribute.value = parseInt(markerAttribute.value);
                }
              });


              layerGroupService.listAttributesByLayer($scope.currentEntity.layer.id, {
                callback: function (result) {

                  $scope.attributesByLayer = [];

                  angular.forEach(result, function (attribute, index) {

                    var exist = false;

                    angular.forEach($scope.currentEntity.markerAttribute, function (attributeByMarker, index) {

                      if (attributeByMarker.attribute.id == attribute.id) {
                        exist = true;
                      }
                    });

                    if (!exist) {
                      $scope.currentEntity.markerAttribute.push({attribute: attribute, marker: $scope.currentEntity});
                      $scope.attributesByLayer.push(attribute);
                      $scope.showNewAttributes = true;
                    }

                  });

                  $scope.$apply();
                },
                errorHandler: function (message, exception) {
                  $log.debug(message);
                  $scope.$apply();
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

        $scope.$apply();
      };

      $scope.footerCollapse = function () {
        $log.debug('Footer collapsed');
        $scope.showMarkerDetails = false;
        //$scope.$apply();
      };

      $scope.footerMinimize = function () {
        $log.debug('Footer minimize');
        $scope.showMarkerDetails = false;
      };

      $scope.onHold = function () {

        $scope.pullUpHeight = 90;
        $scope.isNewMarker = true;

        $scope.currentEntity = {};
        $scope.footerCollapse();

        $log.debug('onHold');
      };

      $scope.getPhoto = function () {
        Camera.getPicture().then(function (imageURI) {

          $scope.currentEntity.image = imageURI;
          $log.debug(imageURI);

        }, function (err) {
          console.err(err);
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

      $scope.showDatePicker = function (attribute) {
        $cordovaDatePicker.show(options).then(function (date) {
          var month = date.getMonth() > 10 ? date.getMonth() + 1 : '0' + (date.getMonth() + 1);
          attribute.value = date.getDate() + '/' + month + '/' + date.getFullYear();
          //alert(date);
        });
      };

      $scope.save = function() {

        $scope.currentEntity.wktCoordenate = "POINT(-5984271.452126796 -2761057.316881751)";


        angular.forEach($scope.currentEntity.markerAttribute, function(attr, index){

          delete attr.attributeDefault;
          delete attr.layer;
          delete attr.name;
          delete attr.type;
          delete attr.orderAttribute;
          delete attr.required;
          delete attr.temporaryId;
          delete attr.updated;
          delete attr.visible;

          var attribute = new Attribute();
          attribute.id = attr.attribute.id;

          var markerAttribute = new MarkerAttribute();
          if (markerAttribute.value != "" && markerAttribute.value != undefined) {
            markerAttribute.value = val.value;
          } else {
            markerAttribute.value = "";
          }

          markerAttribute.attribute = attribute;
          markerAttribute.marker = {layer: {id: $scope.currentEntity.layer.id}, marker: $scope.currentEntity.markerAttribute};
          $scope.currentEntity.markerAttribute[index] = markerAttribute;

        });

        $scope.currentEntity.markerModeration = null;
        $scope.currentEntity.status = "SAVED";

        markerService.insertMarker( $scope.currentEntity, {
          callback: function (result) {

            $scope.isLoading = false;
            $scope.$apply();
          },
          errorHandler: function (message, exception) {

            $scope.isLoading = false;
            $scope.$apply();
          }
        });
      };

      /**
      * Prepara o estado, retira o password criptografado do usuário
      */
      $scope.logout = function(){
        localStorage.removeItem('userEmail');
        $state.go('authentication.login');
      };

      $timeout(function(){
        $scope.listAllInternalLayerGroups();
      }, 1500);

    });

}(window.angular));
