(function (angular) {
  'use strict';

  /**
   *
   * @param $scope
   * @param $state
   */
  angular.module('application')
    .controller('MapController', function ($rootScope, $scope, $state, $document, $importService, $ionicGesture, $ionicPopup, $ionicSideMenuDelegate, Camera, $timeout, $cordovaDatePicker, $cordovaGeolocation, $filter) {


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
        console.log('onDragStart');
        $scope.isDrawerOpen = !$scope.isDrawerOpen;
        $scope.isDragStart = true;
        $scope.defaults.interactions.dragPan = false;

        $scope.listAllInternalLayerGroups();
      };

      $scope.onDragEnd = function (event) {
        console.log('onDragEnd');
        $scope.isDrawerOpen = !$scope.isDrawerOpen;
        $scope.isDragStart = false;
        $scope.defaults.interactions.dragPan = true;
      };

      $scope.toggleDrawer = function () {
        console.log('toggleDrawer');

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

        /*console.log($scope.isDragStart);
         console.log(data.event.pixel);
         console.log($scope.defaults.interactions.dragPan);
         console.log($scope.direction);*/

        if (data.event.pixel[0] < 40 || !$scope.defaults.interactions.dragPan || $scope.isDragStart) {

          //console.log(data.event.pixel);

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

              var iconPath = '/static/images/marker.png';

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

                var newMarker = {
                  layer: layer,
                  id: layer.id,
                  lat: coordinates[1],
                  lon: coordinates[0],
                  style: iconStyle,
                  projection: 'EPSG:4326'
                };

                addLayer.markers.push(newMarker);

                //console.log(newMarker);
                //$scope.internalLayers.push({"layer": layer, "id": layer.id, "feature": iconFeature, "extent": source.getExtent()});

              });

              $scope.layers.push(addLayer);

              $scope.$apply();

            },
            errorHandler: function (message, exception) {
              $scope.message = {type: "error", text: message};
              $scope.$apply();
            }
          });

        } else {

          $timeout(function(){
            $scope.layers[indexOf].visible = layer.visible;
          });

          //$scope.layers.splice(indexOf, 1);

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

        layerGroupService.listAttributesByLayer(layer.id, {
          callback: function (result) {

            $scope.currentEntity.layer = {id: layer.id};

            angular.forEach(result, function(layerAttributes){

              var attribute = new Attribute();

              attribute.id = layerAttributes.id;

              layerAttributes.id = null;
              layerAttributes.photoAlbum = null;

              layerAttributes.attribute = attribute;

            });

            $scope.currentEntity.markerAttribute = result;

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
            console.log(err);
          });
      };

      $scope.$on('openlayers.map.singleclick', function (event, data) {

        console.log('openlayers.map.singleclick');

        console.log($scope.isNewMarker);

        if($scope.isDrawerOpen) {

          $scope.toggleDrawer();

        } else {

          if ($scope.isNewMarker) {

            $scope.isNewMarker = false;

            $scope.$apply(function (scope) {
              if (data) {
                var p = ol.proj.transform([data.coord[0], data.coord[1]], data.projection, 'EPSG:4326');

                var newMarker = {
                  name: 'Novo ponto',
                  lat: p[1],
                  lon: p[0],
                  style: scope.newMarkerStyle,
                  projection: 'EPSG:4326'
                };

                scope.newMarker = newMarker;

                scope.currentEntity = newMarker;

              }
            });

            $scope.newMarker.style = $scope.newMarkerStyle;

          } else {

            if($scope.newMarker.lat != '') {
              $scope.$apply(function(){
                $scope.currentEntity = {};
                $scope.newMarker = {};
              });
            }

            var map = data.event.map;
            var pixel = data.event.pixel;
            var feature = map.forEachFeatureAtPixel(pixel, function (feature, olLayer) {
              if (angular.isDefined(feature.getProperties().marker.name)) {
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
            $scope.$apply(function (scope) {
              if (data) {
                var p = ol.proj.transform([data.coord[0], data.coord[1]], data.projection, 'EPSG:4326');
                scope.mouseClickMap = p[0] + ', ' + p[1];
              } else {
                scope.mouseClickVector = '';
              }
            });
          }
        }

      });

      $scope.$on('markers.click', function (event, feature) {
        console.log('markers.click');
        $scope.$apply(function () {
          if (feature) {
            $scope.currentEntity = feature.getProperties().marker;
            console.log($scope.currentEntity);
          }
        });
      });

      $scope.centerOnMe = function () {
        console.log("Centering");
        if (!$scope.map) {
          return;
        }

        $scope.loading = $ionicLoading.show({
          content: 'Getting current location...',
          showBackdrop: false
        });

        navigator.geolocation.getCurrentPosition(function (pos) {
          console.log('Got pos', pos);
          $scope.map.setCenter(new google.maps.LatLng(pos.coords.latitude, pos.coords.longitude));
          $scope.loading.hide();
        }, function (error) {
          alert('Unable to get location: ' + error.message);
        });
      };

      $scope.clearMarkerDetail = function () {
        console.log('clearMarkerDetail');
      };

      $scope.footerExpand = function () {
        console.log('Footer expanded');

        $scope.listAllInternalLayerGroups();

        $scope.showMarkerDetails = true;
        $scope.$apply();
      };

      $scope.footerCollapse = function () {
        console.log('Footer collapsed');
        $scope.showMarkerDetails = false;
        $scope.$apply();
      };

      $scope.footerMinimize = function () {
        console.log('Footer minimize');
        $scope.showMarkerDetails = false;
      };

      $scope.onHold = function () {
        $scope.isNewMarker = true;
        console.log('onHold');
      };

      $scope.getPhoto = function () {
        Camera.getPicture().then(function (imageURI) {

          $scope.currentEntity.image = imageURI;
          console.log(imageURI);

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

    });

}(window.angular));
