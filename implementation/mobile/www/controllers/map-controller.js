(function (angular) {
  'use strict';

  /**
   *
   * @param $scope
   * @param $state
   */
  angular.module('application')
    .controller('MapController', function ($rootScope, $scope, $state, $importService, $ionicPopup, $ionicSideMenuDelegate, Camera, $timeout, $cordovaDatePicker, $cordovaGeolocation) {

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
      /**
       *
       */
      $scope.model = {
        user: null,
        layers: null,
        marker: null
      };

      $scope.toggleLeftSideMenu = function () {
        $ionicSideMenuDelegate.toggleLeft();
      };

      $scope.showMarkerDetails = false;

      //$scope.markerDetail = {};

      $scope.currentEntity = {};

      $scope.isNewMarker = false;

      $scope.layers = {
        name: 'OpenStreetMap',
        active: true,
        source: {
          type: 'OSM'
        }
      };

      var style = {
        image: {
          icon: {
            anchor: [0.5, 1],
            anchorXUnits: 'fraction',
            anchorYUnits: 'fraction',
            opacity: 0.90,
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
            opacity: 0.90,
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
          map: ['singleclick', 'click'],
          //markers: [ 'change', 'change:layerGroup', 'change:size', 'change:target', 'change:view', 'click', 'dblclick', 'moveend', 'pointerdrag', 'pointermove', 'postcompose', 'postrender', 'precompose', 'propertychange', 'singleclick' ]
        }
      };

      $scope.center = {
        lon: -54.1394,
        lat: -24.7568,
        projection: 'EPSG:4326',
        zoom: 9,
        minZoom: 3
      };

      $scope.markers = [
        {
          id: 2,
          name: 'London',
          lat: -25.290638,
          lon: -54.062496,
          projection: 'EPSG:4326'
        },
        {
          id: 1,
          name: 'Bath',
          lat: -25.181322,
          lon: -54.271236,
          style: style,
          projection: 'EPSG:4326'
          /*label: {
           message: 'Finisterre',
           show: false,
           showOnMouseOver: true
           }*/
        },
        {
          id: 3,
          name: 'Canterbury',
          lat: -24.960028,
          lon: -54.299608,
          style: custom_style,
          projection: 'EPSG:4326'
          /*label: {
           message: 'Santiago de Compostela',
           show: false,
           showOnMouseOver: true
           }*/
        }
      ];

      $scope.mouseclickposition = {};


      /*-------------------------------------------------------------------
       * 		 				  	POST CONSTRUCT
       *-------------------------------------------------------------------*/
      /**
       *
       */
      $scope.findUserById = function () {

        accountService.findUserById(1, {
          callback: function (result) {

            $scope.model.user = result;

            $ionicPopup.alert({
              title: 'Serviço executado com sucesso',
              template: ':D'
            });

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
      /*-------------------------------------------------------------------
       * 		 				 	  HANDLERS
       *-------------------------------------------------------------------*/

      /**
       *
       */
      $scope.listAllInternalLayerGroups = function () {
        layerGroupService.listAllInternalLayerGroups({
          callback: function (result) {

            if(!$scope.currentEntity.layer)
              $scope.model.layers = result;

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
      $scope.listAttributesByLayer = function (layer) {

        layerGroupService.listAttributesByLayer(layer.id, {
          callback: function (result) {

            $scope.currentEntity.layer = layer;

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

        if ($scope.isNewMarker) {

          $scope.isNewMarker = false;

          $scope.$apply(function (scope) {
            if (data) {
              var p = ol.proj.transform([data.coord[0], data.coord[1]], data.projection, 'EPSG:4326');
              scope.mouseClickMap = p[0] + ', ' + p[1];

              var newMarker = {
                name: 'Novo ponto',
                lat: p[1],
                lon: p[0],
                style: custom_style,
                projection: 'EPSG:4326'
              };

              $scope.markers.push(newMarker);

              $scope.currentEntity = newMarker;

            }
          });

        } else {

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
          markerAttribute.marker = {layer: {id: $scope.currentEntity.layer.id}};
          $scope.currentEntity.markerAttribute[index] = markerAttribute;

        });

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
      }

    });

}(window.angular));
