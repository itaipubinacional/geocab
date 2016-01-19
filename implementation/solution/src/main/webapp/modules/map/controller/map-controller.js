'use strict';

/**
 *
 * @param $scope
 * @param $log
 * @param $location
 */
function MapController($scope, $injector, $log, $state, $timeout, $modal, $location, $http, $importService, $filter, $translate) {

  /**
   *
   * Injects the methods, attributes and inherited states of AbstractCRUDController States.
   * @see AbstractCRUDController
   */
  $injector.invoke(AbstractCRUDController, this, {$scope: $scope});

  /**
   * Include services
   */
  $importService("layerGroupService");
  $importService("markerService");
  $importService("customSearchService");
  $importService("markerModerationService");
  $importService("accountService");
  $importService("shapeFileService");

  /*-------------------------------------------------------------------
   * 		 				 	EVENT HANDLERS
   *-------------------------------------------------------------------*/

  /*-------------------------------------------------------------------
   * 		 				 	ATTRIBUTES
   *-------------------------------------------------------------------*/

  // CONSTANTES


  /**
   * Google Maps
   */
  $scope.MAP_TYPE_GMAP = 'gmap'

  /**
   * Open Street Maps
   */
  $scope.MAP_TYPE_OSM = 'osm'

  /**
   * MapQuest OSM
   */
  $scope.MAP_TYPE_MAPQUEST_OSM = 'mapQuest_osm'

  /**
   * MapQuest SATELITE
   */
  $scope.MAP_TYPE_MAPQUEST_SAT = 'mapQuest_sat'

  /**
   *
   * @type {string}
   */
  $scope.LAYER_MENU_STATE = '';

  /**
   *
   * @type {string}
   */
  $scope.LAYER_MENU_LIST = 'list';

  /**
   *
   * @type {string}
   */
  $scope.LAYER_MENU_LEGEND_DETAIL = 'legend_detail';

  /**
   * */
  $scope.attributesByLayer = [];

  $scope.attributesByMarkerOnHover = [];

  /*
   *
   * */
  $scope.screenMarkerOpenned = false;

  $scope.screenSelectMarkerOpenned = false;

  $scope.marker = {};

  $scope.coordinatesFormat = '';

  /**
   * User credentials
   * */
  $scope.userMe;

  /**
   * Variable that stores the type of the map selected by the user - GMAP ou OSM
   * Will also contain the selected module - Warren, Satellite, Hybrid, etc.
   */
  $scope.mapConf = {
    type: $scope.MAP_TYPE_OSM,
    ativo: null,
    modo: null
  };


  /**
   * Variable that stores the settings from google map
   */
  $scope.mapGoogleOptions = {};

  /**
   * Variable that represents o google map
   */
  $scope.mapGoogle = {};

  /**
   * Variable representing the openlayers map
   */
  $scope.map = {};

  /**
   * Responsible for controlling variable if the functionalities are active or not
   */
  $scope.menu = {
    fcDistancia: false,
    fcArea: false,
    fcKml: false,
    fcMarker: false,
    fcSelect: false
  };

  /**
   * select Marker tool
   * */
  $scope.selectMarkerTool = false;

  $scope.selectedMarkers = [];

  /**
   * Variable that stores the list of layer groups with user access profile
   * @type {Array}
   */
  $scope.allLayers = [];

  /**
   * Variable that stores all layers selected by the user
   * @type {Array}
   */
  $scope.layers = [];

  /**
   * Variable that stores the active kml layers on a map
   */
  $scope.kmlLayers = [];

  /**
   * Variable that stores all the inner layers selected by the user
   * @type {Array}
   */
  $scope.internalLayers = [];

  /**
   * Variable that stores all the inner layers selected by the user Search
   * @type {Array}
   */
  $scope.internalLayersSearch = [];

  /**
   *
   */
  $scope.allLayersKML = [];

  $scope.importMarkers = [];
  $scope.importLayers = [];

  $scope.exportMarkers = [];
  $scope.exportLayers = [];

  /**
   * Variable that stores the inner layer being created
   * @type {Array}
   */
  $scope.currentCreatingInternalLayer;

  /**
   * Variable that stores the slider that is active
   * @type {Array}
   */
  $scope.slideActived;

  /**
   *
   */
  $scope.customSearches;

  /**
   *
   */
  $scope.currentCustomSearch = {};

  /**
   *
   */
  $scope.currentLayerField = {};

  /**
   *
   * @type {Array}
   */
  $scope.searchs = [];

  /**
   *
   * @type {Array}
   */
  $scope.allSearchs = [];

  /**
   *
   */
  $scope.allLayersSearches = [];

  /**
   *
   * @type {Array}
   */
  $scope.features = [];

  $scope.currentEntity = {};
  /*-------------------------------------------------------------------
   * 		            PERMISSIONS FOR THE TOOLS
   *-------------------------------------------------------------------*/

  //Allowed distance calculation
  /**
   *
   * @type {boolean}
   */
  $scope.hasPermissionCalculoDistancia = false;
  /**
   *
   * @type {number}
   */
  $scope.PERMISSION_CALCULO_DISTANCIA = 1;

  //Permission area calculation
  /**
   *
   * @type {boolean}
   */
  $scope.hasPermissionCalculoArea = false;
  /**
   *
   * @type {number}
   */
  $scope.PERMISSION_CALCULO_AREA = 2;

  //Permission KML
  /**
   *
   * @type {boolean}
   */
  $scope.hasPermissionKML = false;
  /**
   *
   * @type {number}
   */
  $scope.PERMISSION_KML = 3;

  //Permission SHP
  /**
   *
   * @type {boolean}
   */
  $scope.hasPermissionSHP = false;
  /**
   *
   * @type {number}
   */
  $scope.PERMISSION_SHP = 4;

  /**
   * Variable used to internal layer
   * @type {number}
   * */
  $scope.searchId = 1;

  $scope.backgroundMap = [];

  $scope.backgroundMap.type = [];

  $scope.backgroundMap.type.GOOGLE_MAP_TERRAIN = false;
  $scope.backgroundMap.type.GOOGLE_SATELLITE_LABELS = false;

  $scope.isLoading = false;
  /*-------------------------------------------------------------------
   * 		 				 	 CONFIGURATION VARIABLES MAP
   *-------------------------------------------------------------------*/

  /**
   * The configuration view of the map
   */
  $scope.view = new ol.View({
    center: ol.proj.transform([-54.1394, -24.7568], 'EPSG:4326', 'EPSG:3857'),
    zoom: 9,
    minZoom: 3
  });

  // view events
  $scope.view.on('change:center', function () {

    // Changes the view of the google map if the map is active the gmap
    if ($scope.mapConf.active == $scope.MAP_TYPE_GMAP) {
      var center = ol.proj.transform($scope.view.getCenter(), 'EPSG:3857', 'EPSG:4326');
      $scope.mapGoogle.setCenter(new google.maps.LatLng(center[1], center[0]));
    }

  });

  $scope.view.on('change:resolution', function () {

    //Changes the view of the google map if the map is active the gmap
    if ($scope.mapConf.active == $scope.MAP_TYPE_GMAP) {
      $scope.mapGoogle.setZoom($scope.view.getZoom());
    }

  });


  /**
   * Setting the background layer - OSM
   */
  $scope.rasterOSM = new ol.layer.Tile({
    source: new ol.source.OSM()
    //source: new ol.source.MapQuest({layer: 'osm'})
  });

  /**
   * Setting the background layer - MAPQUEST OSM
   */
  $scope.rasterMapQuestOSM = new ol.layer.Tile({
    source: new ol.source.MapQuest({layer: 'osm'})
  });

  /**mapConf.type
   * Setting the background layer - MAPQUEST SAT
   */
  $scope.rasterMapQuestSAT = new ol.layer.Tile({
    source: new ol.source.MapQuest({layer: 'sat'})
  });


  // Rotate Interaction
  $scope.dragRotateAndZoom = new ol.interaction.DragRotateAndZoom();


  /**
   * Setting the mouse position control
   */
  $scope.mousePositionControl = new ol.control.MousePosition({
    coordinateFormat: ol.coordinate.createStringXY(4),
    projection: 'EPSG:4326',
    // comment the following two lines to have the mouse position
    // be placed within the map.
    className: 'custom-mouse-position',
//            target: document.getElementById('info'),
    undefinedHTML: '&nbsp;'
  });

  $scope.firstTime = true;

  $scope.overlay = new ol.Overlay({
    element: container
  });

  /*-------------------------------------------------------------------
   * 		 				 	  NAVIGATIONS
   *-------------------------------------------------------------------*/
  /**
   * Method executed upon entering the controller, apór load the DOM of the page
   *
   */
  $scope.initialize = function (toState, toParams, fromState, fromParams) {


    /**
     * Menu setting
     */
    $("#sidebar-marker-create, #sidebar-marker-detail-update, #sidebar-layers").css("max-width", parseInt($(window).width()) - 68);
    $("#sidebar-marker-create, #sidebar-marker-detail-update, #sidebar-layers").resize(function () {
      if (!$scope.firstTime)
        $(".menu-sidebar-container").css("right", parseInt($(this).css("width")) + 5);

      $scope.firstTime = false;
    });


    /**
     * If doesn't have a nav bar
     * */
    if (!$("#navbar-administrator").length && !$("#navbar-user").length) {
      $(".sidebar-style").css("top", "60px");
    }

    /**
     * Openlayers map configuration
     */
    $scope.olMapDiv = document.getElementById('olmap');
    $scope.map = new ol.Map({

//            controls: ol.control.defaults({
//                attributionOptions: /** @type {olx.control.AttributionOptions} */ ({
//                    collapsible: false
//                })
//            }).extend([$scope.mousePositionControl, new ol.control.FullScreen()]),

      controls: ol.control.defaults().extend([
        new ol.control.ScaleLine(), $scope.mousePositionControl
      ]),


      // rotation
      interactions: ol.interaction.defaults({
        dragPan: false
      }).extend([
//                    $scope.dragRotateAndZoom,
        dragAndDropInteraction,
        new ol.interaction.DragPan({kinetic: null})
      ]),

      target: $scope.olMapDiv,
      view: $scope.view,
      overlays: [$scope.overlay]
    });


    // add OSM layer
    $scope.map.addLayer($scope.rasterOSM);
    $scope.rasterOSM.setVisible(false);

    // add  MapQuest OSM layer
    $scope.map.addLayer($scope.rasterMapQuestOSM);
    $scope.rasterMapQuestOSM.setVisible(false);

    // add MapQuest layer OSM
    $scope.map.addLayer($scope.rasterMapQuestSAT);
    $scope.rasterMapQuestSAT.setVisible(false);

    // Registers the posts on a map
    //$scope.loadMarkers();

    /**
     * Map configuration GMAP
     */
    $scope.mapGoogleOptions = {
      mapTypeControlOptions: {
        mapTypeIds: [google.maps.MapTypeId.ROADMAP,
          google.maps.MapTypeId.SATELLITE,
          google.maps.MapTypeId.HYBRID,
          google.maps.MapTypeId.TERRAIN],
        style: google.maps.MapTypeControlStyle.HORIZONTAL_BAR,
        position: google.maps.ControlPosition.RIGHT_TOP
      },
      zoomControl: false,
      panControl: false,
      scaleControl: false,
      keyboardShortcuts: false,
      draggable: false,
      disableDoubleClickZoom: true,
      scrollwheel: false,
      heading: 90
    };

//        set the mapGoogleOptions
//        $scope.mapGoogle = new google.maps.Map(document.getElementById("gmap"), $scope.mapGoogleOptions);

    // Displays mouse coordinates
    enableMouseCoordinates();


    $scope.LAYER_MENU_STATE = 'list';

    $scope.listToolsByUser();

    $scope.listPublishedLayersGroup();

    // Initializes map
    if ($scope.mapConf.type == $scope.MAP_TYPE_OSM) {
      $scope.initializeOSM();
    } else if ($scope.mapConf.type == $scope.MAP_TYPE_GMAP) {
      $scope.initializeGMAP();
      $scope.mapGoogle.setMapTypeId("hybrid");
    }


    $(function () {
      $("#sidebar-tabs").tabs({}).addClass("ui-tabs-vertical ui-helper-clearfix");
      $("#sidebar-tabs li").removeClass("ui-corner-top ui-widget-content").addClass("ui-corner-left");
    });

    $scope.setType = function(type, status) {
      if(status) {
        if (type == 'GOOGLE_SATELLITE_LABELS') {
          $scope.currentEntity.backgroundMap = type;
        } else {
          $scope.currentEntity.backgroundMap = type;
        }
      } else {
        $scope.currentEntity.backgroundMap = $scope.backgroundMap.subType;
      }
      $scope.initializeGMAP();
    };

    $scope.setBackgroundMap = function(backgroundMap){

      $scope.currentEntity.backgroundMap = backgroundMap;

      if(backgroundMap.match(/GOOGLE/i)) {
        $scope.mapConf.type = 'gmap';
        $scope.backgroundMap.map = 'GOOGLE';
      }

      if(backgroundMap.match(/MAP_QUEST/i)) {
        $scope.mapConf.type = 'mapQuest';
        $scope.backgroundMap.map = 'MAP_QUEST';
      }

      if(backgroundMap.match(/OPEN_STREET_MAP/i)) {
        $scope.mapConf.type = 'osm';
        $scope.backgroundMap.map = 'OPEN_STREET_MAP';
        $scope.initializeOSM();
      }

      if(backgroundMap.match(/MAP_QUEST|MAP_QUEST_OSM/i) && backgroundMap != 'MAP_QUEST_SAT') {
        $scope.currentEntity.backgroundMap = 'MAP_QUEST_OSM';
        $scope.backgroundMap.subType = 'MAP_QUEST_OSM';
        $scope.initializeMapQuestOSM();
      }

      if(backgroundMap.match(/MAP_QUEST_SAT/i)) {
        $scope.backgroundMap.subType = 'MAP_QUEST_SAT';
        $scope.initializeMapQuestSAT();
      }

      if(backgroundMap == 'GOOGLE_MAP' && backgroundMap != 'GOOGLE_SATELLITE') {
        $scope.backgroundMap.type.GOOGLE_SATELLITE_LABELS = false;
        $scope.backgroundMap.subType = 'GOOGLE_MAP';
        $scope.initializeGMAP();
      }

      if(backgroundMap == 'GOOGLE_SATELLITE') {
        $scope.backgroundMap.type.GOOGLE_MAP_TERRAIN = false;
        $scope.backgroundMap.subType = 'GOOGLE_SATELLITE';
        $scope.initializeGMAP();
      }

      if(backgroundMap == 'GOOGLE_MAP_TERRAIN') {
        $scope.backgroundMap.subType = 'GOOGLE_MAP';
        $scope.backgroundMap.type.GOOGLE_MAP_TERRAIN = true;
        $scope.initializeGMAP();
      }

      if(backgroundMap == 'GOOGLE_SATELLITE_LABELS') {
        $scope.backgroundMap.subType = 'GOOGLE_SATELLITE';
        $scope.backgroundMap.type.GOOGLE_SATELLITE_LABELS = true;
        $scope.initializeGMAP();
      }

    };

    $scope.convertDMSToDD = function(coordinate) {

      var coordinate = coordinate.split(/[^\d\w\.]+/);
      var dd = Number(coordinate[0]) + Number(coordinate[1])/60 + Number(coordinate[2])/(60*60);

      var direction = coordinate[3];
      if (direction == "S" || direction == "W") {
        dd = dd * -1;
      } // Don't do anything for N or E
      return dd;
    };

    $scope.convertDDtoDMS = function(coordinate, latitude){
      var valCoordinate, valDeg, valMin, valSec, result;

      valCoordinate = Math.abs(coordinate);

      valDeg = Math.floor(valCoordinate);
      result = valDeg + "° ";

      valMin = Math.floor((valCoordinate - valDeg) * 60);
      result += valMin + "′ ";

      valSec = Math.round((valCoordinate - valDeg - valMin / 60) * 3600 * 1000) / 1000;
      result += valSec + '″ ';

      if(latitude)
        result += coordinate < 0 ? 'S' : 'N';

      if(!latitude)
        result += coordinate < 0 ? 'W' : 'O';

      return result;
    };

    $scope.setMarkerCoordinates = function(){

      var formattedLatitude  = $scope.formattedLatitude.toString();
      var formattedLongitude = $scope.formattedLongitude.toString();

      var regEx = '';

      if($scope.coordinatesFormat != 'DECIMAL_DEGREES') {

        regEx = /^\d\d{0,1}°\s?\d\d{0,1}[′|']\s?\d\d{0,1}\.\d+?[″|"]\s?[N|S|W|O]?$/;

        if(regEx.test(formattedLatitude) && regEx.test(formattedLongitude)) {
          formattedLatitude  = $scope.convertDMSToDD(formattedLatitude);
          formattedLongitude = $scope.convertDMSToDD(formattedLongitude);
        }
      }

      regEx = /\d{2}[.|,]\d{6}/;

      if(regEx.test(formattedLatitude) && regEx.test(formattedLongitude)) {

        console.log(formattedLatitude);
        console.log(formattedLongitude);

        formattedLatitude  = parseFloat(formattedLatitude);
        formattedLongitude = parseFloat(formattedLongitude);

        $scope.latitude  = formattedLatitude;
        $scope.longitude = formattedLongitude;

        var iconStyle = new ol.style.Style({
          image: new ol.style.Icon(({
            anchor: [0.5, 1],
            anchorXUnits: 'fraction',
            anchorYUnits: 'fraction',
            src: ''
          }))
        });

        $scope.currentCreatingInternalLayer.setStyle(iconStyle);

        $scope.map.removeLayer($scope.currentCreatingInternalLayer);

        //$scope.clearFcMarker();

        if($scope.currentEntity.layer && $scope.currentEntity.layer.layerIcon == undefined) {
          $scope.currentEntity.layer.layerIcon = $scope.currentEntity.layer.icon;
        }

        var icon = 'static/images/marker.png';
        if($scope.currentEntity.layer) {
          icon = $scope.currentEntity.layer.layerIcon;
        }

        var iconStyle = new ol.style.Style({
          image: new ol.style.Icon(({
            anchor: [0.5, 1],
            anchorXUnits: 'fraction',
            anchorYUnits: 'fraction',
            src: icon
          }))
        });

        var shadowStyle = $scope.setShadowMarker();

        var olCoordinates = ol.proj.transform([formattedLongitude, formattedLatitude], 'EPSG:4326', 'EPSG:900913');
        console.log(olCoordinates);

        $scope.currentEntity.latitude  = olCoordinates[0];
        $scope.currentEntity.longitude = olCoordinates[1];

        var iconFeature = new ol.Feature({
          geometry: new ol.geom.Point([olCoordinates[0], olCoordinates[1]])
        });

        var layer = new ol.layer.Vector({
          source: new ol.source.Vector({features: [iconFeature]})
        });

        layer.setStyle([iconStyle, shadowStyle]);

        $scope.currentCreatingInternalLayer = layer;
        $scope.map.addLayer(layer);

        var zoom = $scope.map.getView().getZoom();

        var extent = layer.getSource().getExtent();
        $scope.map.getView().fitExtent(extent, $scope.map.getSize());

        $scope.map.getView().setZoom(zoom);

        //$scope.setMarkerCoordinatesFormat();
      }

    };

    $scope.setShadowMarker = function(type) {

      if(!type) {
        type = 'default';
        if (($scope.currentEntity.layer && !$scope.currentEntity.layer.layerIcon.match(/default/)) || ($scope.currentEntity.layer && $scope.currentEntity.layer.icon && !$scope.currentEntity.layer.icon.match(/default/)))
          type = 'collection';
      }

      var anchor = [];
      anchor['collection'] = [0.50, 0.86];
      anchor['default']    = [0.49, 0.83];
      anchor['marker']     = [0.48, 0.73];

      return new ol.style.Style({
        image: new ol.style.Icon({
          anchor: anchor[type],
          anchorXUnits: 'fraction',
          anchorYUnits: 'fraction',
          src: 'static/images/' + type + '_shadow.png'
        }),
        zIndex: 1
      });
    };

    $scope.setMarkerCoordinatesFormat = function() {
      if ($scope.coordinatesFormat == 'DEGREES_DECIMAL') {
        $scope.formattedLatitude  = $scope.latitude.toFixed(6);
        $scope.formattedLongitude = $scope.longitude.toFixed(6);
      } else {
        $scope.formattedLatitude  = $scope.convertDDtoDMS($scope.latitude, true);
        $scope.formattedLongitude = $scope.convertDDtoDMS($scope.longitude, false);
      }
    };

    /**
     * Click event to prompt the geoserver the information layer of the clicked coordinate
     */
    $scope.map.on('click', function (evt) {

      if ($scope.slideActived == '#sidebar-select-marker') {
        $scope.closeSelectMarker();
      }

      if ($scope.menu.fcMarker && $scope.screenMarkerOpenned) {

        $scope.clearShadowCreatingInternalLayer();

        var coord = evt.coordinate;
        var transformed_coordinate = ol.proj.transform(coord, 'EPSG:900913', 'EPSG:4326');
        //console.log(transformed_coordinate);

        $scope.longitude = transformed_coordinate[0];
        $scope.latitude  = transformed_coordinate[1];

        $scope.clearFcMarker(false);

        $scope.screenMarkerOpenned = true;

        //$scope.toggleSidebarMarkerCreate(300);

        var iconStyle = new ol.style.Style({
          image: new ol.style.Icon({
            anchor: [0.5, 1],
            anchorXUnits: 'fraction',
            anchorYUnits: 'fraction',
            src: 'static/images/marker.png'
          }),
          zIndex: 2
        });

        var shadowStyle = $scope.setShadowMarker('marker');

        var iconFeature = new ol.Feature({
          geometry: new ol.geom.Point([evt.coordinate[0], evt.coordinate[1]])
        });

        var layer = new ol.layer.Vector({
          source: new ol.source.Vector({
            features: [iconFeature]
          })
        });

        layer.setStyle([iconStyle, shadowStyle]);

        $scope.currentCreatingInternalLayer = layer;
        $scope.map.addLayer(layer);
        $scope.$apply();

        $scope.currentEntity.latitude  = evt.coordinate[0];
        $scope.currentEntity.longitude = evt.coordinate[1];

        $scope.setMarkerCoordinatesFormat();

        layerGroupService.listAllInternalLayerGroups({
          callback: function (result) {
            $scope.selectLayerGroup = [];

            angular.forEach(result, function (layer, index) {

              $scope.selectLayerGroup.push({
                "layerTitle": layer.title,
                "layerId": layer.id,
                "layerIcon": layer.icon,
                "group": layer.layerGroup.name
              });

            });

            $scope.currentState = $scope.LIST_STATE;

            $scope.$apply();
          },
          errorHandler: function (message, exception) {
            $scope.message = {type: "error", text: message};
            $scope.$apply();
          }
        });

        return false;
      }


      /* get the feature click to open marker detail */
      var feature = $scope.map.forEachFeatureAtPixel(evt.pixel, function (feature, layer) {
        return feature;
      });

      /*It is used to check if the user has clicked on the map or on the feature.*/
      $scope.feature = feature;

      if (($scope.layers.length > 0 && !$scope.menu.fcArea && !$scope.menu.fcDistancia) || feature) {
        $scope.features = [];
      }

      var hasSearch = $scope.allSearchs.length ? $scope.allSearchs[0].children.length > 0 : false;

      if (($scope.layers.length > 0 || hasSearch ) && !$scope.menu.fcArea && !$scope.menu.fcDistancia) {

        var listUrls = [];

        for (var i = 0; i < $scope.layers.length; i++) {
          var url = $scope.layers[i].wmsSource.getGetFeatureInfoUrl(
            evt.coordinate, $scope.view.getResolution(), $scope.view.getProjection(),
            {'INFO_FORMAT': 'application/json'});

          listUrls.push(decodeURIComponent(url));
        }

        if ($scope.screenMarkerOpenned) {
          $scope.clearFcMarker();
        }

        listAllFeatures(listUrls);

        $scope.screen = 'detail';

      }

      /* if click on the marker */
      if (feature) {

        if (typeof feature.getProperties().marker != "undefined") {

          $scope.clearAllSelectedMarkers();

          $scope.clearShadowCreatingInternalLayer();

          if ($scope.screenMarkerOpenned) {
            $scope.clearFcMarker();
          }

          $scope.currentCreatingInternalLayer = feature;
          $scope.screen = 'detail';

          $scope.marker = feature.getProperties().marker;

          var iconStyle = new ol.style.Style({
            image: new ol.style.Icon(({
              anchor: [0.5, 1],
              anchorXUnits: 'fraction',
              anchorYUnits: 'fraction',
              src: $scope.marker.layer.icon
            }))
          });

          var shadowType = 'default';
          if (!$scope.marker.layer.icon.match(/default/))
            shadowType = 'collection';

          var shadowStyle = $scope.setShadowMarker(shadowType);

          feature.setStyle([iconStyle, shadowStyle]);

          $scope.features.push({"feature": $scope.marker, "type": "internal"});

          var geometry = feature.getGeometry();
          var coordinate = geometry.getCoordinates();

          var transformed_coordinate = ol.proj.transform(coordinate, 'EPSG:900913', 'EPSG:4326');

          $scope.latitude = transformed_coordinate[1];
          $scope.longitude = transformed_coordinate[0];

          $scope.setMarkerCoordinatesFormat();

          if ($scope.features.length > 0) {
            $timeout(function () {
              $scope.toggleSidebarMarkerDetailUpdate(300);
            }, 400)
          }

          if ($scope.features.length == 1) {
            $timeout(function () {

              $(".min-height-accordion .panel-collapse .panel-body").removeAttr("style")
            }, 100)
          }

          $("div.msgMap").css("display", "none");
        }
      }

    });

    /**
     * authenticated user
     * */
    accountService.getUserAuthenticated({
      callback : function(result) {
        $scope.userMe = result;
        $scope.setBackgroundMap(result.backgroundMap);
        $scope.coordinatesFormat = result.coordinates;
        $scope.$apply();
      },
      errorHandler : function(message, exception) {
        $scope.message = {type:"error", text: message};
        $scope.$apply();
      }
    });

    /*markerService.getUserMe({
      callback: function (result) {
        $scope.userMe = result;
      },
      errorHandler: function (message, exception) {
        $scope.message = {type: "error", text: message};
        $scope.$apply();
      }
    });*/

  };

  $scope.changeToScreen = function (screen) {
    $scope.screen = screen;
  }

  $scope.objectKeys = function (obj) {
    return Object.keys(obj);
  }

  /**
   * Function that makes request to geo server to bring the features
   * @param url
   * @param posicao
   */
  var listAllFeatures = function (listUrls) {

    layerGroupService.listAllFeatures(listUrls, {
      callback: function (result) {

        for (var i = 0; i < result.length; i++) {

          var feature = {
            layer: $scope.layers[i],
            fields: {}
          };

          try {
            angular.forEach(JSON.parse(result[i]).features, function (value, key) {
              angular.forEach(value.properties, function (value, key) {

                try {
                  feature.fields[decodeURIComponent(escape(key))] = decodeURIComponent(escape(value));
                }
                catch (e) {
                  feature.fields[key] = value;
                }

              });

              var insere = false;
              for (var propriedade in feature.fields) {
                insere = true;
                break;
              }

              if (insere) {

                if ($scope.features.length) {
                  var alreadyExistLayer = false;

                  angular.forEach($scope.features, function (value, key) {

                    if (value.feature.layer.name == feature.layer.name) {
                      alreadyExistLayer = true;
                    }

                  });

                  if (!alreadyExistLayer) {
                    $scope.features.push({"feature": feature, "type": "external"});
                  }

                } else {
                  $scope.features.push({"feature": feature, "type": "external"});
                }

              }

              if ($scope.features.length > 0) {

                $timeout(function () {
                  $scope.toggleSidebarMarkerDetailUpdate(300);

                  //.panel-collapse 
                  $('.min-height-accordion').find('.panel-body').css('height',
                    parseInt($('#sidebar-marker-detail-update').height()) -
                    parseInt(( ( $scope.features.length) * 37 ) + 40) + 'px'
                  );
                }, 400)

              }

              if ($scope.features.length > 1) {
                $timeout(function () {
                  $(".min-height-accordion .panel-collapse .panel-body").css("min-height", "300px")
                }, 700)
              }

            });
          } catch (e) {
            continue;
          }

        }


        if ($("#sidebar-layers").css("display") == 'none' && $('.menu-sidebar-container').css('right') != '3px') {

          if ($scope.menu.fcMarker) {
            $scope.clearFcMarker();
          } else if (!$scope.feature) {
            $scope.clearDetailMarker();
          }
        }


        $scope.$apply();

      },
      errorHandler: function (message, exception) {
        $scope.msg = {type: "danger", text: message, dismiss: true};
        $scope.$apply();
      }
    });
  }


  /**
   *
   */
  $scope.listToolsByUser = function () {

    layerGroupService.listToolsByUser({
      callback: function (result) {

        for (var i = 0; i < result.length; i++) {
          if (result[i].id == $scope.PERMISSION_CALCULO_DISTANCIA) {
            $scope.hasPermissionCalculoDistancia = true;
          }
          else if (result[i].id == $scope.PERMISSION_CALCULO_AREA) {
            $scope.hasPermissionCalculoArea = true;
          }
          else if (result[i].id == $scope.PERMISSION_KML) {
            $scope.hasPermissionKML = true;
            enableFileKML();
          }
          else if (result[i].id == $scope.PERMISSION_SHP) {
            $scope.hasPermissionSHP = true;
          }

        }

        if ($scope.hasPermissionKML == false) {
          $("#menu-item-3").remove();
          $("#tabs-3").remove();
        }

        if ($scope.hasPermissionSHP == false) {
          $("#menu-item-4").remove();
          $("#tabs-4").remove();
        }

        $scope.$apply();
      },
      errorHandler: function (message, exception) {
        $scope.msg = {type: "danger", text: message, dismiss: true};
        $scope.$apply();
      }
    });

  }

  /**
   *
   */
  $scope.listPublishedLayersGroup = function () {

    //Lists the groups of layers and layers published according to user access profile
    layerGroupService.listLayerGroupUpperPublished({
      callback: function (result) {

        var parseNode = function (node) {
          var item = {};

          item.id = (!!node.nodes ? 'grupo' : 'layer') + '_' + node.id.toString();
          item.label = !!node.nodes ? node.name : node.title;
          item.name = !!node.nodes ? '' : node.name;
          item.legenda = !!node.nodes ? '' : node.legend;
          item.selected = !!node.nodes ? '' : node.startEnabled;
          item.dataSourceUrl = !!node.nodes ? '' : node.dataSource.url;
          item.value = node.id;
          item.type = !!node.nodes ? 'grupo' : 'layer';

          item.maximumScaleMap = !!node.nodes ? '' : node.maximumScaleMap;
          item.minimumScaleMap = !!node.nodes ? '' : node.minimumScaleMap;

          if (item.selected) {
            $scope.getSelectedNode(item);
          }

          item.children = [];

          if (!!node.nodes) {
            for (var i = 0; i < node.nodes.length; ++i) {
              item.children.push(parseNode(node.nodes[i]));
//                            if( true === node.nodes[i].startVisible ) {
//                                item.children.push(parseNode(node.nodes[i]));
//                            }
            }
          }
          return item;
        }

        $scope.allLayers = [];

        for (var i = 0; i < result.length; ++i) {
          $scope.allLayers.push(parseNode(result[i]))
        }

        if ($scope.allLayers[0]) {
          for (var i in $scope.allLayers[0].children) {
            if ($scope.allLayers[0].children.length == 1 && $scope.allLayers[0].children[i].selected) {
              $scope.allLayers[0].selected = true;
              $scope.allLayers[0].children[i].selected = true;
            }
            else if (!$scope.allLayers[0].children[i].selected) {
              $scope.allLayers[0].selected = false;
            }
            else {
              $scope.allLayers[0].selected = true;
            }
          }
        }


        $scope.$apply();
      },
      errorHandler: function (message, exception) {
        $scope.msg = {type: "danger", text: message, dismiss: true};
        $scope.$apply();
      }
    });

  }

  /**
   * Formats the url with the name of the layer for each data source
   * @param node
   * @returns {{name: string, url: string}}
   */
  $scope.formatUrl = function (node, isSearch) {

    if (isSearch) {
      var index = node.name.indexOf(":");
      var dataSourceAddress = node.dataSource.url.lastIndexOf("ows?");

      var layerType = node.name.substring(0, index);
      var layerName = node.name.substring(index + 1, node.name.length);
      var formattedUrl = node.dataSource.url.substring(0, dataSourceAddress) + layerType + '/wms';
    }
    else {
      var index = node.name.indexOf(":");
      var dataSourceAddress = node.dataSourceUrl.lastIndexOf("ows?");
      var layerType = node.name.substring(0, index);
      var layerName = node.name.substring(index + 1, node.name.length);
      var formattedUrl = node.dataSourceUrl.substring(0, dataSourceAddress) + layerType + '/wms';
    }

    return {'name': layerName, 'url': formattedUrl};

  }


  /**
   * Treat the selection and deselection of each of the tree
   * @param node
   */
  $scope.getSelectedNode = function (node) {

    if (typeof node == 'undefined' || node.search) return false;

    /* Check if it is an internal layer */
    if (typeof node.dataSourceUrl != 'undefined' && node.dataSourceUrl == null) {
      if (node.selected) {
        $scope.addInternalLayer(node.value);
      } else {
        $scope.removeInternalLayer(node.value);
      }
      return;
    }

    if (node && node.type == 'layer' && !node.search) {
      if (node.selected) {

        var item = $scope.formatUrl(node, false);

        var wmsOptions = {
          url: item.url,
          params: {
            'LAYERS': item.name
          }
        };

        if (node.dataSourceUrl.match(/&authkey=(.*)/)) {
          wmsOptions.url += "?" + node.dataSourceUrl.match(/&authkey=(.*)/)[0];
        }

        var wmsSource = new ol.source.TileWMS(wmsOptions);

        var wmsLayer = new ol.layer.Tile({
          source: wmsSource,
          maxResolution: minEscalaToMaxResolutionn(node.minimumScaleMap),
          minResolution: maxEscalaToMinResolutionn(node.maximumScaleMap)
        });

        $scope.layers.push({'wmsLayer': wmsLayer, 'wmsSource': wmsSource, "name": node.name, "titulo": node.label});

        //Adds the selected layers in the map
        $scope.map.addLayer(wmsLayer);
      }
      else {
        for (var i = 0; i <= $scope.layers.length; i++) {
          if (i == $scope.layers.length) {
            if ($scope.layers[i - 1].name == node.name && $scope.layers[i - 1].searchId == undefined) {
              $scope.map.removeLayer($scope.layers[i - 1].wmsLayer);
              $scope.layers.splice(i - 1, 1);
            }
          } else {
            if ($scope.layers[i].name == node.name && $scope.layers[i].searchId == undefined) {
              //Removes the user-desselecionadas layers
              $scope.map.removeLayer($scope.layers[i].wmsLayer);
              $scope.layers.splice(i, 1);
            }
          }
        }
      }
    }
  }

  $scope.getSelectedSearchNode = function (node) {

    if (typeof node == 'undefined') return false;

    if (node && node.type == 'layer' && $scope.allSearchs[0] && node.searchId >= 0) {

      if (node.selected) {

        for (var i = 0; i < $scope.allSearchs[0].children.length; i++) {
          if ($scope.allSearchs[0].children[i].name == node.name) {
            //Is internal layer
            if ($scope.allSearchs[0].children[i].search.layer.dataSource.url == null) {

              //$scope.addInternalLayerSearch($scope.allSearchs[0].children[i].pesquisa.searchId);
              $scope.addInternalLayerSearch(i);
            } else {
              $scope.map.removeLayer(node.wmsLayer);

              $scope.layers.push({
                'wmsLayer': $scope.allSearchs[0].children[i].wmsLayer,
                'wmsSource': $scope.allSearchs[0].children[i].wmsSource,
                "name": node.search.layer.name,
                "titulo": node.search.layer.title,
                'searchId': node.searchId
              });

              //Add in the list each selected layer
              $scope.map.addLayer($scope.allSearchs[0].children[i].wmsLayer);
            }
          }
        }
      }
      else {

        for (var i = 0; i < $scope.layers.length; i++) {
          if ($scope.layers[i].searchId == node.searchId) {
            //Is external layer
            $scope.map.removeLayer($scope.layers[i].wmsLayer);

            //retirar
            $scope.layers.splice(i, 1);
          }
        }

        for (var i = 0; i < $scope.allSearchs[0].children.length; i++) {
          if ($scope.allSearchs[0].children[i].name == node.name) {
            //Remove layers selected by user 

            //Is internal layer
            if ($scope.allSearchs[0].children[i].search.layer.dataSource.url == null) {

              //$scope.removeInternalLayerSearch($scope.allSearchs[0].children[i].pesquisa.searchId);
              $scope.removeInternalLayerSearch(i, $scope.allSearchs[0].children[i].search.layer.id);
            }

          }
        }
      }
    }
  }

  /**
   * Treat the selection and deselection of each of the kml tree
   * @param node
   */
  $scope.getSelectedKMLNode = function (node) {


    if (node && node.type == 'kml' && $scope.allLayersKML[0]) {

      if (node.selected) {

        for (var i = 0; i < $scope.allLayersKML[0].children.length; i++) {
          if ($scope.allLayersKML[0].children[i].name == node.name) {
            $scope.map.removeLayer(node.layer);
            $scope.map.addLayer($scope.allLayersKML[0].children[i].layer);
          }
        }
      }
      else {
        for (var i = 0; i < $scope.allLayersKML[0].children.length; i++) {
          if ($scope.allLayersKML[0].children[i].name == node.name) {
            //Remove as camadas desselecionadas pelo usuário
            $scope.map.removeLayer($scope.allLayersKML[0].children[i].layer);

          }
        }
      }
    }

  }


  /**
   Converts the value scale stored in the db to open layes zoom format
   */
  var minEscalaToMaxResolutionn = function (minEscalaMapa) {

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
      default :
        return 78271.51696402048;
    }
  }

  /**
   Converts the value scale stored in the db to open layes zoom forma
   */
  var maxEscalaToMinResolutionn = function (maxEscalaMapa) {

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
      default :
        return 0.0005831682455839253;
    }
  }

  /*-------------------------------------------------------------------
   * 		 			GOOGLE MAPS CONFIGURATION
   *-------------------------------------------------------------------*/

  /**
   * Create an overlay to anchor the popup to the map.
   */

  var container = document.getElementById('popup1');
  var content = document.getElementById('popup-content1');
  //var closer = document.getElementById('popup-closer');

  /**
   * Add a click handler to hide the popup.
   * @return {boolean} Don't follow the href.
   */
  /*closer.onclick = function() {
    container.style.display = 'none';
    closer.blur();
    return false;
  };*/

  var displayFeatureInfo = function(pixel) {

    var feature = $scope.map.forEachFeatureAtPixel(pixel, function(feature, layer) {
      return feature;
    });

    if (feature) {
      if (typeof feature.getProperties().marker != "undefined") {

        var geometry = feature.getGeometry();
        var coordinate = geometry.getCoordinates();
        var coordinatePixel = $scope.map.getPixelFromCoordinate(coordinate);

        $scope.overlay.setPosition(coordinate);

        $scope.markerOnHover = feature.getProperties().marker;

        if(!$('#popup1').is(':visible')) {

          container.style.display = 'block';
          $('#popup1').css('z-index', -1);

          markerService.listAttributeByMarker($scope.markerOnHover.id, {
            callback: function (result) {

              $scope.attributesByMarkerOnHover = result;
              $scope.$apply();

              angular.forEach($scope.attributesByMarkerOnHover, function(attribute){
                  if(attribute.attribute.visible)
                    $('#popup1').css('z-index', 0);
              });

              var left = coordinatePixel[0] - $('#popup1').outerWidth() / 2;
              var top = (coordinatePixel[1] - $('#popup1').outerHeight()) - 30;

              $('#popup1').css('left', left);
              $('#popup1').css('top', top);
              $('#popup1').css('bottom', 'initial');

              $('.ol-popup1:after').css('left', $('#popup1').outerWidth() / 2);

            },
            errorHandler: function (message, exception) {
              $scope.message = {type: "error", text: message};
              $scope.$apply();
            }
          });
        }

      }
    } else {

      $('#popup1').hide();
      $scope.attributesByMarkerOnHover = [];

    }
  };

  $scope.addEventListenerPointerMove = function() {
    /* POINTER MOVE LISTENER */
    $scope.map.on('pointermove', function (evt) {
      if (evt.dragging) {
        return;
      }
      var pixel = $scope.map.getEventPixel(evt.originalEvent);
      displayFeatureInfo(pixel);
    });
  };

  /**
   * Method that initializes the Google Maps map and its settings
   */
  $scope.initializeGMAP = function initializeGMAP() {

    // only runs if the currently active forest is not google maps
    if ($scope.mapConf.active != $scope.MAP_TYPE_GMAP) {


      // case map active MAP QUEST OSM
      if ($scope.mapConf.active == $scope.MAP_TYPE_MAPQUEST_OSM) {
        $scope.rasterMapQuestOSM.setVisible(false);
      }

      // If map QUEST MAP SAT active
      if ($scope.mapConf.active == $scope.MAP_TYPE_MAPQUEST_SAT) {
        $scope.rasterMapQuestSAT.setVisible(false);
      }

      // case map active MAP QUEST OSM
      if ($scope.mapConf.active == $scope.MAP_TYPE_OSM) {
        $scope.rasterOSM.setVisible(false);
      }

      //adjust div css gmap-to show it
      $("#gmap").css({"width": "100%", "height": "100%"})

      // set the mapGoogleOptions
      $scope.mapGoogle = new google.maps.Map(document.getElementById("gmap"), $scope.mapGoogleOptions);

      $scope.olMapDiv.parentNode.removeChild($scope.olMapDiv);
      $scope.mapGoogle.controls[google.maps.ControlPosition.RIGHT_TOP].push($scope.olMapDiv)

      // Removes the interaction of rotate
      $scope.map.removeInteraction($scope.dragRotateAndZoom);

      // Remove the rotation
      $scope.map.getView().setRotation(0);


      $scope.mapConf.active = $scope.MAP_TYPE_GMAP;

      // set the view from google maps
      $scope.view.setCenter($scope.view.getCenter());
      $scope.view.setZoom($scope.view.getZoom());
    }

    //$scope.mapGoogle.setMapTypeId('hybrid');

    if($scope.currentEntity.backgroundMap == 'GOOGLE_MAP')
      $scope.mapGoogle.setMapTypeId('roadmap');

    if($scope.currentEntity.backgroundMap == 'GOOGLE_MAP_TERRAIN')
      $scope.mapGoogle.setMapTypeId('terrain');

    if($scope.currentEntity.backgroundMap == 'GOOGLE_SATELLITE')
      $scope.mapGoogle.setMapTypeId('satellite');

    if($scope.currentEntity.backgroundMap == 'GOOGLE_SATELLITE_LABELS')
      $scope.mapGoogle.setMapTypeId('hybrid');

    $scope.addEventListenerPointerMove();

    setTimeout(function(){
      $('.gmnoprint:eq(4)').hide();
    }, 1000);

  };

  /**
   * Method that initializes the Open Street Map map and its settings
   */
  $scope.initializeOSM = function initializeOSM() {

    // only runs if the map currently active is not the OSM
    if ($scope.mapConf.active != $scope.MAP_TYPE_OSM) {

      if ($scope.mapConf.active == $scope.MAP_TYPE_GMAP) {

        //adjust div css gmap-not to show it
        $("#gmap").css({"width": "0", "height": "0"})

        // Removes the element from the olmap div of google maps
        var element = document.getElementById("main-content");
        $scope.olMapDiv.style.position = "relative";
        $scope.olMapDiv.style.top = "0";
        element.appendChild($scope.olMapDiv);

        // Adds interaction to rotate
        $scope.map.addInteraction($scope.dragRotateAndZoom);
      }

      // case map active MAP QUEST OSM
      if ($scope.mapConf.active == $scope.MAP_TYPE_MAPQUEST_OSM) {
        $scope.rasterMapQuestOSM.setVisible(false);
      }

      // If map QUEST MAP SAT active
      if ($scope.mapConf.active == $scope.MAP_TYPE_MAPQUEST_SAT) {
        $scope.rasterMapQuestSAT.setVisible(false);
      }

      //$scope.map.addLayer($scope.raster);
      $scope.rasterOSM.setVisible(true);

      $scope.mapConf.active = $scope.MAP_TYPE_OSM;

      $scope.addEventListenerPointerMove();

    }
  };

  /**
   *
   */
  $scope.initializeMapQuestOSM = function () {

    // only runs if the map active in memento is not the OSM
    if ($scope.mapConf.active != $scope.MAP_TYPE_MAPQUEST_OSM) {

      // If active map for google maps
      if ($scope.mapConf.active == $scope.MAP_TYPE_GMAP) {

        //adjust div css gmap-not to show it
        $("#gmap").css({"width": "0", "height": "0"})

        // Removes the element from the olmap div of google maps
        var element = document.getElementById("main-content");
        $scope.olMapDiv.style.position = "relative";
        $scope.olMapDiv.style.top = "0";
        element.appendChild($scope.olMapDiv);

        // Adds interaction to rotate
        $scope.map.addInteraction($scope.dragRotateAndZoom);
      }

      // If active map for OSM
      if ($scope.mapConf.active == $scope.MAP_TYPE_OSM) {
        $scope.rasterOSM.setVisible(false);
      }

      // If active map for MAPQUEST SAT
      if ($scope.mapConf.active == $scope.MAP_TYPE_MAPQUEST_SAT) {
        $scope.rasterMapQuestSAT.setVisible(false);
      }

      // sets the map map quest osm
      $scope.rasterMapQuestOSM.setVisible(true);

      $scope.mapConf.active = $scope.MAP_TYPE_MAPQUEST_OSM;

      $scope.addEventListenerPointerMove();

    }
  };


  /**
   *
   */
  $scope.initializeMapQuestSAT = function () {

    // only runs if the map active in memento is not the OSM
    if ($scope.mapConf.active != $scope.MAP_TYPE_MAPQUEST_SAT) {

      // If active map is google maps
      if ($scope.mapConf.active == $scope.MAP_TYPE_GMAP) {

        //adjust div css gmap-not to show it
        $("#gmap").css({"width": "0", "height": "0"})

        // Removes the element from the olmap div of google maps
        var element = document.getElementById("main-content");
        $scope.olMapDiv.style.position = "relative";
        $scope.olMapDiv.style.top = "0";
        element.appendChild($scope.olMapDiv);

        // Adds interaction to rotate
        $scope.map.addInteraction($scope.dragRotateAndZoom);
      }

      // If active map is OSM
      if ($scope.mapConf.active == $scope.MAP_TYPE_OSM) {
        $scope.rasterOSM.setVisible(false);
      }

      // case map active is MAP QUEST OSM
      if ($scope.mapConf.active == $scope.MAP_TYPE_MAPQUEST_OSM) {
        $scope.rasterMapQuestOSM.setVisible(false);
      }


      // set the map to map quest osm
      $scope.rasterMapQuestSAT.setVisible(true);

      $scope.mapConf.active = $scope.MAP_TYPE_MAPQUEST_SAT;

      $scope.addEventListenerPointerMove();
    }
  };


  /*-------------------------------------------------------------------
   * 		 		FUNCTIONALITY VIEW MOUSE COORDINATESE
   *-------------------------------------------------------------------*/


  /**
   * Responsible method to display on the map the mouse pointer coordinates
   */
  function enableMouseCoordinates() {

    /**
     * Variable that has the element that contains the tooltip
     */
    var info = $('#info');

    /**
     * Method that shows the mouse coordinates on the map
     */
    var displayCoordinateMouse = function (pixel) {

      info.html("<p>" + formatCoordinate($scope.mousePositionControl.l) + "</p>");
      info.css("display", "block");

    };

    /**
     * Method that formats the coordinate of the mouse
     */
    var formatCoordinate = function (coord) {

      if($scope.coordinatesFormat == 'DEGREES_DECIMAL') {
        return coord.split(',').reverse().join(', ');
      } else {
        return ol.coordinate.toStringHDMS(coord.split(',').map(Number));
      }

      /*var posVirgula = coord.indexOf(",");

      var part1 = coord.slice(0, posVirgula);
      var part2 = coord.slice(posVirgula + 2);

      var posPonto = part1.indexOf(".");
      var latitude = part1.slice(0, posPonto) + "°" + part1.slice(posPonto + 1, posPonto + 3) + "'" + part1.slice(posPonto + 3) + '"';

      posPonto = part2.indexOf(".");
      var longitude = part2.slice(0, posPonto) + "°" + part2.slice(posPonto + 1, posPonto + 3) + "'" + part2.slice(posPonto + 3) + '"';

      return latitude + ", " + longitude;*/

    }

    /**
     * Events to display coordinate of the mouse
     */
    $($scope.map.getViewport()).on('mousemove', function (evt) {
      displayCoordinateMouse($scope.map.getEventPixel(evt.originalEvent));
    });
  }


  /**
   * Function that hides the mouse position
   */
  $scope.hideMousePosition = function () {
    var info = $('#info');
    info.tooltip('hide');
  };

  $scope.closeSelectMarker = function() {

    $scope.toggleSidebar(300, '', '#sidebar-select-marker');
    $scope.screenSelectMarkerOpenned = false;
    $scope.menu.fcSelect = false;
    $scope.selectMarkerTool = false;

  };

  $scope.showMarkerDetail = function(marker) {
    console.log(marker);

    $scope.marker = marker;

    $scope.screen = 'detail';
    $scope.features.push({"feature": $scope.marker, "type": "internal"});

    $scope.closeSelectMarker();

    $timeout(function(){
      $scope.toggleSidebarMarkerDetailUpdate(300);
    }, 400);

  };

  $scope.clearShadowCreatingInternalLayer = function() {

    if (!$scope.currentCreatingInternalLayer && $scope.currentCreatingInternalLayer != undefined && $scope.marker != undefined && $scope.marker.layer != undefined) {

      var iconStyle = new ol.style.Style({
        image: new ol.style.Icon(({
          anchor: [0.5, 1],
          anchorXUnits: 'fraction',
          anchorYUnits: 'fraction',
          src: $scope.marker.layer.icon
        }))
      });
      $scope.currentCreatingInternalLayer.setStyle(iconStyle);
    }
  };

  $scope.clearAllSelectedMarkers = function() {

    angular.forEach($scope.markers, function(marker){

      var iconStyle = new ol.style.Style({
        image: new ol.style.Icon(({
          anchor: [0.5, 1],
          anchorXUnits: 'fraction',
          anchorYUnits: 'fraction',
          src: marker.layer.icon
        }))
      });

      marker.feature.setStyle(iconStyle);

    });

    $scope.selectedMarkers = [];

  };

  $scope.initializeSelectionTool = function () {

    /*if($scope.menu.fcSelect) {
      $scope.menu.fcSelect = false;
      $scope.selectMarkerTool = false;
    }*/

    if($scope.screenSelectMarkerOpenned) {

      $scope.closeSelectMarker();

    } else {

      if (!$scope.screenSelectMarkerOpenned && $scope.selectedMarkers.length) {

        $scope.screenSelectMarkerOpenned = true;

        $timeout(function() {
          $scope.toggleSidebar(300, '', '#sidebar-select-marker');
        }, 400);

      }

      $scope.selectMarkerTool = $scope.menu.fcSelect = ($scope.selectMarkerTool == true) ? false : true;

      if ($("#sidebar-marker-detail-update").css("display") == 'block') {
        $scope.clearDetailMarker();
      }

      if ($scope.menu.fcMarker) {

        $scope.clearFcMarker(true);
        $scope.menu.fcMarker = false;

      } else {

        $('li.menu-item').each(function (index) {

          if ($(this).hasClass('ui-state-active') && !$(this).hasClass('bg-inactive')) {
            console.log($(this).attr('id'));
            $scope.toggleSidebarMenu(300, '#' + $(this).attr('id'));
          }

        });
      }

      $scope.menu = {
        fcDistancia: false,
        fcArea: false,
        fcKml: false,
        fcMarker: false,
        fcSelect: true
      };

      var dragBox = new ol.interaction.DragBox({
        condition: function () {
          return $scope.selectMarkerTool;
        },
        style: new ol.style.Style({
          stroke: new ol.style.Stroke({
            color: [0, 0, 255, 1]
          })
        })
      });

      dragBox.on('boxend', function (e) {

        $scope.clearAllSelectedMarkers();

        if (!$scope.screenSelectMarkerOpenned) {
          $scope.toggleSidebar(300, '', '#sidebar-select-marker');
          $scope.screenSelectMarkerOpenned = true;
        }

        var extent = dragBox.getGeometry().getExtent();
        $scope.markers = [];

        angular.forEach($scope.internalLayers, function (feature, index) {

          var geometry = feature.feature.getGeometry();
          var coordinate = geometry.getCoordinates();

          var transformed_coordinate = ol.proj.transform(coordinate, 'EPSG:900913', 'EPSG:4326');

          $scope.latitude = transformed_coordinate[1];
          $scope.longitude = transformed_coordinate[0];

          $scope.setMarkerCoordinatesFormat();

          var marker = feature.feature.getProperties().marker;
          var extentMarker = feature.extent;

          var feature = feature.feature;

          marker.coordinate = $scope.formattedLatitude + ' ' + $scope.formattedLongitude;
          marker.feature = feature;

          if (ol.extent.containsExtent(extent, extentMarker)) {

            var iconStyle = new ol.style.Style({
              image: new ol.style.Icon(({
                anchor: [0.5, 1],
                anchorXUnits: 'fraction',
                anchorYUnits: 'fraction',
                src: marker.layer.icon
              }))
            });

            var shadowType = 'default';
            if (!marker.layer.icon.match(/default/))
              shadowType = 'collection';

            var shadowStyle = $scope.setShadowMarker(shadowType);

            feature.setStyle([iconStyle, shadowStyle]);

            var layer = $filter('filter')($scope.selectedMarkers, {id: marker.layer.id})[0];
            var index = $scope.selectedMarkers.indexOf(layer);

            if(index == -1) {
              marker.layer.markers = [];
              marker.layer.markers.push(marker);

              $scope.selectedMarkers.push(marker.layer);
            } else {

              $scope.selectedMarkers[index].markers.push(marker);
            }

            $scope.markers.push(marker);

            $scope.$apply();
          }
        });

        if ($scope.markers.length) {
          $scope.dragMarkers = $scope.markers;
        }

        $scope.drag = true;

        console.log($scope.selectedMarkers);

      });

      dragBox.on('boxstart', function (e) {

        $scope.clearShadowCreatingInternalLayer();
        //$scope.clearFeatures();
        console.log('boxstart');
      });

      $scope.map.addInteraction(dragBox);
    }

  };

  /*-------------------------------------------------------------------
   * 		 	    FUNCTIONALITY TO CALCULATE DISTANCE AND AREA
   *-------------------------------------------------------------------*/


  /**
   * Method that calculates the distance of points on interactive map
   */
  $scope.initializeDistanceCalc = function () {

    if ($scope.menu.fcMarker) {
      $scope.clearFcMarker();
    } else if ($("#sidebar-layers").css("display") == 'none' && $('.menu-sidebar-container').css('right') != '3px') {
      $scope.clearDetailMarker();
    }


    // checks whether any functionality is already active
    if ($scope.menu.fcDistancia || $scope.menu.fcArea) {

      // If this functionality is active is necessary to leave the funcionality
      $scope.map.removeInteraction(draw);
      source.clear();
      $scope.map.removeLayer(vector);
      $('#popup').css("display", "none");
      sketch = null;


    }

    //If this functionality is active: deactivates and leave caso mapa ativo for o google maps
    if ($scope.menu.fcDistancia) {

      $scope.menu.fcDistancia = false;
      return;

    } else {

      // active functionality and disables the other to only have one active at a time
      $scope.menu = {
        fcDistancia: true,
        fcArea: false,
        fcKml: false,
        fcMarker: false,
        fcSelect: false
      };

      // add the measuring layer on a map
      $scope.map.addLayer(vector);

      // adds the event on the map
      $($scope.map.getViewport()).on('mousemove', mouseMoveHandler);

      // initializes the interaction
      addInteraction('LineString');
    }
  };

  $scope.initializeMarker = function () {

    $scope.imgResult = null;

    if ($scope.slideActived == '#sidebar-select-marker') {
      $scope.closeSelectMarker();
    }

    /*if ($scope.slideActived == '#sidebar-marker-detail-update') {
      $scope.toggleSidebar(300, '', '#sidebar-marker-detail-update');
    }*/

    if ($("#sidebar-marker-detail-update").css("display") == 'block') {
      $scope.clearDetailMarker();
    }

    if ($scope.menu.fcMarker) {

      $scope.clearFcMarker(true);
      $scope.menu.fcMarker = false;

    } else {

      $('li.menu-item').each(function(index){

        if($(this).hasClass('ui-state-active') && !$(this).hasClass('bg-inactive')){
          console.log($(this).attr('id'));
          $scope.toggleSidebarMenu(300, '#' + $(this).attr('id'));
        }

      });

      $scope.screenMarkerOpenned = true;

      $scope.map.removeInteraction(draw);
      source.clear();
      $scope.map.removeLayer(vector);
      $('#popup').css("display", "none");
      sketch = null;

      layerGroupService.listAllInternalLayerGroups({
        callback: function (result) {
          $scope.selectLayerGroup = [];

          angular.forEach(result, function (layer, index) {

            $scope.selectLayerGroup.push({
              "layerTitle": layer.title,
              "layerId": layer.id,
              "layerIcon": layer.icon,
              "group": layer.layerGroup.name
            });

          });

          $scope.currentState = $scope.LIST_STATE;

          $scope.$apply();
        },
        errorHandler: function (message, exception) {
          $scope.message = {type: "error", text: message};
          $scope.$apply();
        }
      });

      $timeout(function(){
        $scope.toggleSidebar(300, '', '#sidebar-marker-create');
      }, 400);

      $scope.currentEntity = new Marker();

      // active functionality and disables the other to only have one active at a time
      $scope.menu = {
        fcDistancia: false,
        fcArea: false,
        fcKml: false,
        fcMarker: true,
        fcSelect: false
      };
    }
  };

  /**
   * Method that calculates the area of points on interactive map
   */
  $scope.initializeAreaCalc = function () {

    if ($scope.menu.fcMarker) {
      $scope.clearFcMarker();
    } else if ($("#sidebar-layers").css("display") == 'none' && $('.menu-sidebar-container').css('right') != '3px') {
      $scope.clearDetailMarker();
    }

    // checks whether any functionality is already active
    if ($scope.menu.fcArea || $scope.menu.fcDistancia || $scope.menu.fcMarker) {

      // If this functionality is necessary to leave the active functionality
      $scope.map.removeInteraction(draw);
      source.clear();
      $scope.map.removeLayer(vector);
      $('#popup').css("display", "none");
      sketch = null;

    }

    // If this functionality turns on: deactivates and goes out
    if ($scope.menu.fcArea) {

      $scope.menu.fcArea = false;
      return;

    } else {

      // activates and deactivates the other functionality to just have an active at a time
      $scope.menu = {
        fcDistancia: false,
        fcArea: true,
        fcKml: false,
        fcMarker: false,
        fcSelect: false
      };

      // Add the layer of measurement on the map
      $scope.map.addLayer(vector);

      // Add event on a map
      $($scope.map.getViewport()).on('mousemove', mouseMoveHandler);

      // Initializes the interaction
      addInteraction('Polygon');
    }

  };


  /**
   * Source of measuring layer
   */
  var source = new ol.source.Vector();

  /**
   * Measuring layer configuration
   */
  var vector = new ol.layer.Vector({
    source: source,
    style: new ol.style.Style({
      fill: new ol.style.Fill({
        color: 'rgba(255, 255, 255, 0.2)'
      }),
      stroke: new ol.style.Stroke({
        color: '#ffcc33',
        width: 2
      }),
      image: new ol.style.Circle({
        radius: 7,
        fill: new ol.style.Fill({
          color: '#ffcc33'
        })
      })
    })
  });


  /**
   * Currently drawed feature
   * @type {ol.Feature}
   */
  var sketch;

  /**
   * Variable that will contain the instance of ol.interaction.Draw
   */
  var draw;


  // EVENTS
  /**
   * handle pointer move
   * @param {Event} evt
   */
  var mouseMoveHandler = function (evt) {
    if (sketch && ( $scope.menu.fcArea || $scope.menu.fcDistancia )) {
      var output;
      var geom = (sketch.getGeometry());
      if (geom instanceof ol.geom.Polygon) {
        output = formatArea(/** @type {ol.geom.Polygon} */ (geom));

      } else if (geom instanceof ol.geom.LineString) {
        output = formatLength(/** @type {ol.geom.LineString} */ (geom));
      }

      $('#popup-content').html("<p>" + output + "</p>");
      $('#popup').css("display", "block");
    }
  };


  /**
   * Method that adds a user interaction on a map
   */
  function addInteraction(type) {
    // tipos : 'Polygon' e 'LineString'
    draw = new ol.interaction.Draw({
      source: source,
      type: /** @type {ol.geom.GeometryType} */ (type)
    });
    $scope.map.addInteraction(draw);

    draw.on('drawstart',
      function (evt) {
        // set sketch
        sketch = evt.feature;

        // clean the ancient markings
        source.clear();

      }, this);

    draw.on('drawend',
      function (evt) {
        // unset sketch
        sketch = null;

      }, this);
  }


  /**
   * Method that generates the output format of distance measurement
   */
  var formatLength = function (line) {
    var length = Math.round(line.getLength() * 100) / 100;
    var output;
    if (length > 100) {
      output = (Math.round(length / 1000 * 100) / 100) +
        ' ' + 'km';
    } else {
      output = (Math.round(length * 100) / 100) +
        ' ' + 'm';
    }
    return output;
  };


  /**
   * Method that generates the input format of measuring area
   */
  var formatArea = function (polygon) {
    var area = polygon.getArea();
    var output;
    if (area > 10000) {
      output = (Math.round(area / 1000000 * 100) / 100) +
        ' ' + 'km<sup>2</sup>';
    } else {
      output = (Math.round(area * 100) / 100) +
        ' ' + 'm<sup>2</sup>';
    }
    return output;
  };

  /*-------------------------------------------------------------------
   * 		 			CUSTOM SEARCH FUNCTIONALITY
   *-------------------------------------------------------------------*/
  $scope.listCustomSearchByUser = function () {

    // resets the fields of the last selected search
    $scope.currentCustomSearch.layer = null;
    $scope.currentCustomSearch.layerFields = {};
    $scope.searchMsg = null;

    // List custom searches based on user access profile
    customSearchService.listCustomSearchsByUser({
      callback: function (result) {

        $scope.customSearchs = result;

        $scope.toggleSidebarMenu(300, '#menu-item-2');

        $scope.$apply();

      },
      errorHandler: function (message, exception) {

        $scope.toggleSidebarMenu(300, '#menu-item-2');

        $scope.msg = {type: "danger", text: message, dismiss: true};
        $scope.$apply();
      }
    });

  }

  /**
   * The custom search receive the selected value
   * @param customSearch
   */
  $scope.selectCustomSearch = function (customSearch) {
    $scope.currentCustomSearch = customSearch;

    $timeout(function () {
      $('.datepicker').datepicker({
        dateFormat: 'dd/mm/yy',
        dayNames: ['Domingo', 'Segunda', 'Terça', 'Quarta', 'Quinta', 'Sexta', 'Sábado'],
        dayNamesMin: ['D', 'S', 'T', 'Q', 'Q', 'S', 'S', 'D'],
        dayNamesShort: ['Dom', 'Seg', 'Ter', 'Qua', 'Qui', 'Sex', 'Sáb', 'Dom'],
        monthNames: ['Janeiro', 'Fevereiro', 'Março', 'Abril', 'Maio', 'Junho', 'Julho', 'Agosto', 'Setembro', 'Outubro', 'Novembro', 'Dezembro'],
        monthNamesShort: ['Jan', 'Fev', 'Mar', 'Abr', 'Mai', 'Jun', 'Jul', 'Ago', 'Set', 'Out', 'Nov', 'Dez'],
        nextText: 'Próximo',
        prevText: 'Anterior'
      });

      $('.datepicker').mask("99/99/9999");
    });
  };


  /**
   *  List the layers of custom search fields
   */
  $scope.listFieldsLayersSearch = function () {

    $scope.hasSearch = null;
    $scope.hasSearch = true;
    // deselect the old research and remove the map
    angular.forEach($scope.searchs, function (search, index) {

      if (search.search.layer.name != $scope.currentCustomSearch.layer.name) {
        return true;
      }

      $scope.map.removeLayer(search.wmsLayer);
      ///$scope.allSearchs[0].children[index].selected = false;

      if ($scope.currentCustomSearch.name == $scope.allSearchs[0].children[index].search.name) {
        for (var i = 0; i < $scope.layers.length; i++) {
          if ($scope.allSearchs[0].children[index].searchId == $scope.layers[i].searchId) {
            $scope.layers.splice(i, 1);
          }
        }
        $scope.allSearchs[0].children[index].selected = false;
      } else {
        if ($scope.allSearchs[0].children[index].selected == true) {
          $scope.allSearchs[0].children[index].selected = true;
        } else {
          $scope.allSearchs[0].children[index].selected = false;
        }
      }

    });

    //If external layer...
    if ($scope.currentCustomSearch.layer.dataSource.url != null) {
      var item = $scope.formatUrl($scope.currentCustomSearch.layer, true);

      var wmsOptions = {url: item.url, params: {'LAYERS': $scope.currentCustomSearch.layer.name}};

      if ($scope.currentCustomSearch.layer.dataSource.token) {
        wmsOptions.url += "?&authkey=" + $scope.currentCustomSearch.layer.dataSource.token;
      }

      var wmsSource = new ol.source.TileWMS(wmsOptions);

      var wmsLayer = new ol.layer.Tile({
        source: wmsSource
      });

      $scope.map.addLayer(wmsLayer);

      $scope.layers.push({
        'wmsLayer': wmsLayer,
        'wmsSource': wmsSource,
        "name": $scope.currentCustomSearch.layer.name,
        "titulo": $scope.currentCustomSearch.layer.title,
        'searchId': ($scope.allSearchs.length ? $scope.allSearchs[0].children.length : 0)
      });

    } else {
      //If internal layer...

      $scope.removeInternalLayer($scope.currentCustomSearch.layer.id, function (layerId) {
        $scope.hasSearch = false;
        var fields = $scope.currentCustomSearch.layerFields;

        for (var field in fields) {
          if ($scope.currentCustomSearch.layerFields[field].type == 'BOOLEAN') {
            $scope.currentCustomSearch.layerFields[field].value = $($scope.isChecked()).val();
          } else
            $scope.currentCustomSearch.layerFields[field].value = $("#item_" + field).val();
        }

        markerService.listMarkerByLayerFilters($scope.currentCustomSearch.layer.id, {
          callback: function (results) {

            $scope.markersByLayer = results;

            $scope.allFieldEmpty = true;
            angular.forEach($scope.currentCustomSearch.layerFields, function (field, index) {
              if (field.value != "" && field.value != undefined) {
                $scope.allFieldEmpty = false;
              }
            });

            if (!$scope.allFieldEmpty) {

              angular.forEach(results, function (result, index) {
                $scope.canRemoveMarker = null;


                angular.forEach($scope.currentCustomSearch.layerFields, function (field, index) {

                  if ($scope.canRemoveMarker == true) return false;

                  //Se campo colocado na pesquisa realmente possuir um valor, ele deve ser processado!
                  //If the field in the search has a value, it need to be proccesed 
                  var enter = false;

                  angular.forEach(result.markerAttribute, function (markerAttribute, index) {

                    if (field.attributeId == markerAttribute.attribute.id && $scope.canRemoveMarker != true) {
                      enter = true;

                      if (field.value != "" && field.value != undefined) {
                        if (markerAttribute.value.toUpperCase().indexOf(field.value.toUpperCase()) != -1) {
                          $scope.canRemoveMarker = false;
                        } else {
                          $scope.canRemoveMarker = true;
                          return false;
                        }
                      }
                    }

                  });

                  //Se caso o valor não for processado dentro do foreach acima, e caso esse valor campo não esteja vazio, deve-se remover o marcador do mapa.
                  //Case the value isn't processed inside the foreach above, and case this value isn't empty, so the marker need to be removed of the map. 
                  if (!enter && (field.value != "" && field.value != undefined)) {
                    $scope.canRemoveMarker = true;
                  }

                });

                if ($scope.canRemoveMarker) {
                  delete $scope.markersByLayer[index];
                }
              });

            }

            var normalizeArray = $scope.markersByLayer;
            $scope.markersByLayer = [];

            angular.forEach(normalizeArray, function (value, index) {

              if (value != undefined) {
                $scope.markersByLayer.push(value);
              }

            });

            var iconPath = "static/images/marker.png";

            if ($scope.markersByLayer.length > 0) {
              iconPath = $scope.markersByLayer[0].layer.icon;
            }

            var iconStyle = new ol.style.Style({
              image: new ol.style.Icon(({
                anchor: [0.5, 1],
                anchorXUnits: 'fraction',
                anchorYUnits: 'fraction',
                src: iconPath

              }))
            });

            angular.forEach($scope.markersByLayer, function (marker, index) {
              var iconFeature = new ol.Feature({
                geometry: new ol.format.WKT().readGeometry(marker.location.coordinateString),
                marker: marker
              });

              var layer = new ol.layer.Vector({
                source: new ol.source.Vector({features: [iconFeature]}),
                maxResolution: minEscalaToMaxResolutionn(marker.layer.minimumScaleMap),
                minResolution: maxEscalaToMinResolutionn(marker.layer.maximumScaleMap)
              });

              layer.setStyle(iconStyle);

              $scope.map.addLayer(layer);

              var location = new ol.format.WKT().readGeometry(marker.location.coordinateString);

              $scope.internalLayers.push({
                "layer": layer,
                "id": layerId,
                "location": location,
                'searchId': $scope.allSearchs[0].children.length - 1
              });
              $scope.internalLayersSearch.push({
                "layer": layer,
                "id": layerId,
                "layerId": layerId,
                "searchId": $scope.allSearchs[0].children.length - 1,
                "location": location
              });
            });

            $scope.searchId++;

            $scope.$apply();


          },
          errorHandler: function (message, exception) {

            $scope.msg = {type: "danger", text: message, dismiss: true};
            $scope.$apply();
          }
        });
      })


      $scope.searchs.push({'search': $.extend([], $scope.currentCustomSearch)});

      var item = {};
      item.id = 'results';
      item.label = 'Resultado pesquisas';
      item.type = 'grupo';

      item.children = [];


      var lastSearchName = "";
      for (var i = 0; i < $scope.searchs.length; ++i) {

        $scope.searchs[i].id = 'pesquisa_' + (i + 1).toString();
        $scope.searchs[i].searchId = i;
        $scope.searchs[i].label = "Pesquisa " + (i + 1);
        $scope.searchs[i].type = 'layer';
        $scope.searchs[i].name = "pesquisa" + (i + 1);


        if ($scope.searchs[i].search.layer != null) {
          $scope.allLayersSearches[i] = $scope.searchs[i].search.layer;
        } else {
          $scope.searchs[i].search.layer = $scope.allLayersSearches[i];
        }

        item.children.push($scope.searchs[i]);
        lastSearchName = "Pesquisa " + (i + 1);
      }

      // seleciona a ultima pesquisa
      item.children[item.children.length - 1].selected = true;

      // seleciona o grupo pai caso so tenha uma pesquisa
      if (item.children.length == 1) item.selected = true;

      // Abre a tree de pesquisas
      $timeout(function () {
        $('#tree-pesquisas').find('.ivh-treeview-node-collapsed').removeClass('ivh-treeview-node-collapsed');
      });

      $scope.allSearchs = [];
      $scope.allSearchs.push(item);
      $scope.searchMsg = lastSearchName + ' - Realizada com sucesso';

      $("#alertPesquisa").show();

      setTimeout(function () {
        $("#alertPesquisa").fadeOut();
      }, 2000);

      return false;
    }

    var filterParams = {'CQL_FILTER': null};
    var filterList = '';
    var firstTime = true;

    var fields = $scope.currentCustomSearch.layerFields;

    var conectorLike;
    for (var i = 0; i < fields.length; i++) {

      fields[i].searchValue = '';

      if ($("#item_" + i).val() != '') {
        fields[i].searchValue = $("#item_" + i).val();

        if (fields[i].tipo != "INT") {
          conectorLike = true;
        } else {
          conectorLike = false;
        }
        if (firstTime) {
          if (conectorLike) {
            filterList = filterList.concat('strToLowerCase(' + '\"' + fields[i].name + '\"' + ') LIKE ' + "'%" + $("#item_" + i).val().toLowerCase() + "%'");
          } else {
            filterList = filterList.concat('strToLowerCase(' + '\"' + fields[i].name + '\"' + ') = ' + "'" + $("#item_" + i).val().toLowerCase() + "'");
          }

          firstTime = false;
        }
        else {
          if (conectorLike) {
            filterList = filterList.concat(' AND strToLowerCase(' + '\"' + fields[i].name + '\"' + ') LIKE ' + "'%" + $("#item_" + i).val().toLowerCase() + "%'");
          } else {
            filterList = filterList.concat(' AND strToLowerCase(' + '\"' + fields[i].name + '\"' + ') = ' + "'" + $("#item_" + i).val().toLowerCase() + "'");
          }

        }
      }
    }

    filterParams.CQL_FILTER = filterList;

    if (filterList != '') {
      wmsSource.updateParams(filterParams);
    }

    $scope.searchs.push({
      'wmsLayer': wmsLayer,
      'wmsSource': wmsSource,
      'search': $.extend([], $scope.currentCustomSearch)
    });

    var item = {};
    item.label = 'Resultado pesquisas';
    item.type = 'grupo';

    item.children = [];

    var lastSearchName;
    for (var i = 0; i < $scope.searchs.length; ++i) {
      $scope.searchs[i].id = 'pesquisa_' + (i + 1).toString();
      $scope.searchs[i].label = "Pesquisa " + (i + 1);
      $scope.searchs[i].type = 'layer';
      $scope.searchs[i].name = "pesquisa" + (i + 1);
      $scope.searchs[i].searchId = i;


      if ($scope.searchs[i].search.layer != null) {
        $scope.allLayersSearches[i] = $scope.searchs[i].search.layer;

      } else {
        $scope.searchs[i].search.layer = $scope.allLayersSearches[i];
      }

      item.children.push($scope.searchs[i]);
      lastSearchName = "Pesquisa " + (i + 1);
    }

    // seleciona a ultima pesquisa
    item.children[item.children.length - 1].selected = true;

    // seleciona o grupo pai caso so tenha uma pesquisa
    if (item.children.length == 1) item.selected = true;

    // Abre a tree de pesquisas
    $timeout(function () {
      $('#tree-pesquisas').find('.ivh-treeview-node-collapsed').removeClass('ivh-treeview-node-collapsed');
    });

    $scope.allSearchs = [];
    $scope.allSearchs.push(item);
    $scope.searchMsg = lastSearchName + ' - Realizada com sucesso'

    $("#alertPesquisa").show();

    setTimeout(function () {
      $("#alertPesquisa").fadeOut();
    }, 2000)


  }

  /*-------------------------------------------------------------------
   * 		 			KML FUNCTIONALITY
   *-------------------------------------------------------------------*/


  /**
   * Method that allowed drag a KML file to the interactive map
   */
//    function enableFileKML()
//    {
//        //Controla o drag and drop do arquivo KML no mapa
//        dragAndDropInteraction.on('addfeatures', function(event) {
//            var vectorSource = new ol.source.Vector({
//                features: event.features,
//                projection: event.projection
//            });
//            $scope.map.getLayers().push(new ol.layer.Vector({
//                source: vectorSource
//            }));
//
//            //Redireciona para o ponto que o arquivo KML é arrastado
//            var view = $scope.map.getView();
//            view.fitExtent(
//                vectorSource.getExtent(), ($scope.map.getSize()));
//        });
//
//    }

  /*-------------------------------------------------------------------
   * 		 			KML FUNCTIONALITY
   *-------------------------------------------------------------------*/

  //Allowed formats to be dragged on a map
  var dragAndDropInteraction = new ol.interaction.DragAndDrop({
    formatConstructors: [
      ol.format.KML
    ]
  });


  /**
   * Method that allowed drag a KML file to the interactive map
   */
  function enableFileKML() {
    //Controls the drag and drop the KML file on a map
    dragAndDropInteraction.on('addfeatures', function (event) {
      var vectorSource = new ol.source.Vector({
        features: event.features,
        projection: event.projection
      });

      var kmlLayer = new ol.layer.Vector({
        source: vectorSource
      });

      $scope.kmlLayers.push({layer: kmlLayer})

      $scope.map.getLayers().push(kmlLayer);

      //Redirects to the point that the KML file is dragged
      var view = $scope.map.getView();
      view.fitExtent(
        vectorSource.getExtent(), ($scope.map.getSize()));

      var item = {};
      item.id = 'kmlLayers'
      item.label = 'Camadas KML';
      item.type = 'kml';

      item.children = [];

      for (var i = 0; i < $scope.kmlLayers.length; ++i) {
        $scope.kmlLayers[i].id = (i + 1).toString();
        $scope.kmlLayers[i].label = "Camada " + (i + 1);
        $scope.kmlLayers[i].type = 'kml';
        $scope.kmlLayers[i].name = "Camada" + (i + 1);

        item.children.push($scope.kmlLayers[i]);
      }

      // selects the last search
      item.children[item.children.length - 1].selected = true;

      // Select the parent group
      var selectItemPai = true;
      for (var i in item.children) {
        if (item.children[i].selected != true) {
          selectItemPai = false
        }
      }

      // Select the parent group
      if (selectItemPai) item.selected = true;

      $scope.allLayersKML = [];
      $scope.allLayersKML.push(item);


    });


  }

  /*-------------------------------------------------------------------
   * 		 			BEHAVIOR OF THE SIDEBAR
   *-------------------------------------------------------------------*/

  /**
   * Function that manages the Sidebar
   * @param time Time animation plays.
   * @param element Element name that is calling the function.
   */
  $scope.toggleSidebarMenu = function (time, element) {

    /*if ($scope.slideActived == '#sidebar-layers') {
      $scope.toggleSidebar(time, 'closeButton', '#sidebar-layers');

      $timeout(function () {
        $scope.toggleSidebar(time, element, '#sidebar-marker-create');
      }, 400)
    } else {

      $scope.toggleSidebar(time, element, '#sidebar-marker-create');
    }*/
    if($('#menu-item-4').css("display") != 'block') {
      $scope.clearAllSelectedMarkers();

      $scope.marker = {};
    }

    if ($("#sidebar-marker-detail-update").css("display") == 'block') {

      $scope.clearDetailMarker();

      $timeout(function () {
        $scope.toggleSidebar(time, element, '#sidebar-layers');
      }, 400)
    }

    if ($("#sidebar-select-marker").css("display") == 'block') {

      $scope.closeSelectMarker();

      $timeout(function () {
        $scope.toggleSidebar(time, element, '#sidebar-layers');
      }, 400)
    }

    /**
     * If the marker tab is open, close it and wait to open the new.
     * */
    if ($scope.menu.fcMarker) {
      $scope.clearFcMarker(true);

      $timeout(function () {
        $scope.toggleSidebar(time, element, '#sidebar-layers');
      }, 400)

    } else {

      $scope.toggleSidebar(time, element, '#sidebar-layers');
    }


  };


  /**
   * Function that manages the Sidebar
   * @param time Execution time of the animation.
   * @param element Name of the element that is calling the function.
   */
  $scope.toggleSidebarMarkerCreate = function (time, element) {

    $scope.attributesByLayer = [];
    $scope.imgResult = "";
    //$scope.$apply();

    if (element == "closeButton") {
      $scope.screenMarkerOpenned = false;
    }

    /**
     * If the marker tab is open, close it and wait to open the new.
     * */
    /*if ($scope.slideActived == '#sidebar-layers') {
      $scope.toggleSidebar(time, 'closeButton', '#sidebar-layers');

      $timeout(function () {
        $scope.toggleSidebar(time, element, '#sidebar-marker-create');
      }, 400)
    } else {

      $scope.toggleSidebar(time, element, '#sidebar-marker-create');
    }*/

    $scope.resolveDatepicker();

  };

  $scope.toggleSidebarMarkerDetailUpdate = function (time, element) {
    $scope.currentEntity = $scope.marker;

    if (element == "closeButton") {
      $scope.screenMarkerOpenned = false;
      $scope.toggleSidebar(time, 'closeButton', '#sidebar-marker-detail-update');
      return;
    }

    if (typeof $scope.marker != "undefined") {
      markerService.findImgByMarker($scope.marker.id, {
        callback: function (result) {

          $scope.imgResult = result;
        },
        errorHandler: function (message, exception) {
          $scope.message = {type: "error", text: message};
          $scope.$apply();
        }
      });

      $scope.attributesByLayer = [];
      $scope.showNewAttributes = false;

      markerService.listAttributeByMarker($scope.marker.id, {
        callback: function (result) {
          $scope.attributesByMarker = result;

          layerGroupService.listAttributesByLayer($scope.marker.layer.id, {
            callback: function (result) {
              $scope.attributesByLayer = [];

              angular.forEach(result, function (attribute, index) {

                var exist = false;

                angular.forEach($scope.attributesByMarker, function (attributeByMarker, index) {

                  if (attributeByMarker.attribute.id == attribute.id) {
                    exist = true;
                  }
                });

                if (!exist) {
                  $scope.attributesByLayer.push(attribute);
                  $scope.showNewAttributes = true;
                }

              });

              $scope.$apply();
            },
            errorHandler: function (message, exception) {
              $scope.message = {type: "error", text: message};
              $scope.$apply();
            }
          });

          angular.forEach(result, function (markerAttribute, index) {
            if (markerAttribute.attribute.type == "NUMBER") {
              markerAttribute.value = parseInt(markerAttribute.value);
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
    /* List for the edit */
    layerGroupService.listAllInternalLayerGroups({
      callback: function (result) {
        // $scope.layersGroups = result;

        $scope.selectLayerGroup = [];

        angular.forEach(result, function (layer, index) {

          $scope.selectLayerGroup.push($.extend(layer, {
            "layerTitle": layer.title,
            "layerId": layer.id,
            "group": layer.layerGroup.name
          }));

        })

        markerService.listAttributeByMarker($scope.currentEntity.id, {
          callback: function (result) {
            $scope.attributesByMarker = result;

            angular.forEach(result, function (markerAttribute, index) {
              if (markerAttribute.attribute.type == "NUMBER") {
                markerAttribute.value = parseInt(markerAttribute.value);
              }
            })

            angular.forEach($scope.selectLayerGroup, function (layer, index) {
              if (layer.layerId == $scope.currentEntity.layer.id) {
                layer.created = $scope.currentEntity.layer.created;
                $scope.currentEntity.layer = layer;

                return false;
              }
            })

            $scope.$apply();
          },
          errorHandler: function (message, exception) {
            $scope.message = {type: "error", text: message};
            $scope.$apply();
          }
        });

      },
      errorHandler: function (message, exception) {
        $scope.message = {type: "error", text: message};
        $scope.$apply();
      }
    });

    /**
     * If the marker tab is open, close it and wait to open the new.
     * */

    if ($scope.slideActived == '#sidebar-marker-detail-update') {
      $(".panel-body").height($("#sidebar-marker-detail-update").height() - 68 - 30);
      return
    }

    if ($scope.slideActived == '#sidebar-layers') {
      //If menu layer or search is open, close it and open marker detail
      $scope.toggleSidebar(time, 'closeButton', '#sidebar-layers');

      $("#sidebar-marker-detail-update").height($("#sidebar-layers").height());

      $timeout(function () {
        $scope.toggleSidebar(time, '', '#sidebar-marker-detail-update');
      }, 400);

    } else {
      $scope.toggleSidebar(time, '', '#sidebar-marker-detail-update');
    }
    /*
     if ( $('#sidebar-marker-detail').css("display") == 'none' ){

     }*/
    $scope.resolveDatepicker();
    $(".panel-body").height($("#sidebar-marker-detail-update").height() - 68 - 30);
  };

  $scope.clearDetailMarker = function () {
    $scope.toggleSidebar(0, 'closeButton', '#sidebar-marker-detail-update');
  };

  $scope.toggleSidebar = function (time, element, slide) {

    time = 300;

    //Checks whether the animation is to open or close the sidebar by her current position.
    var closed = $('.menu-sidebar-container').css('right') == '3px';

    // Checks whether the user has clicked a button that is active and the bar's sample, if it is open or if the click left of the close button.
    if ((element == $scope.lastActive && !closed) || (closed) || (element == "closeButton")) {

      //Manages the class ' bg-inactive ' which activates and deactivates the buttons.
      if (closed) {
        if ($(element).hasClass('bg-inactive')) $(element).removeClass('bg-inactive');
      } else {
        $(".menu-item").addClass("bg-inactive");
      }
      //Performs the animation

      $(slide).toggle('slide', {direction: 'right'}, time);
      $('.menu-sidebar-container').animate({
        'right': closed ? $(slide).width() : '3px'
      }, time);
    } else {
      if ($(element).hasClass('bg-inactive')) $(element).removeClass('bg-inactive');
    }
    $scope.lastActive = element;

    $scope.slideActived = element == 'closeButton' ? '' : slide;
  }

  /*-------------------------------------------------------------------
   * 		 			SIDE MENU MAP
   *-------------------------------------------------------------------*/

  /**
   * Function that decreases the zoom map
   */
  $scope.diminuirZoom = function () {
    $scope.map.getView().setZoom($scope.map.getView().getZoom() - 1);
  }

  /**
   * Function that increases the zoom map
   */
  $scope.aumentarZoom = function () {
    $scope.map.getView().setZoom($scope.map.getView().getZoom() + 1);
  }

  /**
   *
   */
  $scope.showMapInfo = function (features) {
    var dialog = $modal.open({
      templateUrl: 'modules/map/ui/popup/map-info-popup.jsp',
      controller: MapInfoPopUpController,
      resolve: {
        features: function () {
          return features
        }
      }
    });
  }

  /**
   *
   * @param camada
   */
  $scope.showLegendDetail = function (treeItem) {
    $scope.legendDetailTitle = treeItem.label;
    $scope.legendDetailImage = treeItem.legenda;
    $scope.LAYER_MENU_STATE = 'legend_detail';
  }

  /**
   *
   */
  $scope.exitLegendDetail = function () {
    $scope.LAYER_MENU_STATE = 'list';
  };

  $scope.clearFcMarker = function (close) {

    if ($scope.screenMarkerOpenned && close) {
      //$timeout(function(){
        $scope.toggleSidebar(300, '', '#sidebar-marker-create');
      //}, 400);
      $scope.menu.fcMarker = false;
    }

    $scope.currentEntity = new Marker();

    $scope.currentCreatingInternalLayer = {};

    $scope.map.removeLayer($scope.currentCreatingInternalLayer);

    $scope.screenMarkerOpenned = false;
    $scope.attributesByLayer = [];
  };

  $scope.updateMarker = function () {

    if (!$scope.form('sidebarMarkerUpdate').$valid) {
      $scope.msg = {type: "danger", text: $translate("admin.users.The-highlighted-fields-are-required"), dismiss: true};
      return;
    }

    if ($scope.currentEntity.layer == null) {
      var layer = new Layer();
      layer.id = $scope.currentEntity.layer;
      $scope.currentEntity.layer = layer;
    }

    angular.forEach($scope.attributesByMarker, function (attribute) {

      if (attribute.value == null) {
        attribute.value = "";
      }

      if(attribute.attribute.files) {

        console.log(attribute.attribute.files);

        angular.forEach(attribute.attribute.files, function(file, index){

          if(!file.id) {
            var photo = new Photo();
            var img = file.src.split(';base64,');
            photo.source = img[1];
            photo.name = file.name;
            photo.description = file.description;
            photo.contentLength = file.size;
            photo.mimeType = file.type;

            attribute.attribute.files[index] = photo;
          }
        });

        if(!attribute.photoAlbum) {
          var photoAlbum = new PhotoAlbum();
          photoAlbum.photos = new Array();

          attribute.photoAlbum = photoAlbum;
          attribute.photoAlbum.photos = attribute.attribute.files;

        } else {
          attribute.photoAlbum.photos = attribute.attribute.files;
        }
      }
    });

    $scope.currentEntity.markerAttribute = $scope.attributesByMarker;

    angular.forEach($scope.attributesByLayer, function (val, ind) {

      var attribute = new Attribute();
      attribute.id = val.id;

      var markerAttribute = new MarkerAttribute();
      if (val.value != "" && val.value != undefined) {
        markerAttribute.value = val.value;
      } else {
        markerAttribute.value = "";
      }

      markerAttribute.attribute = attribute;
      markerAttribute.marker = $scope.currentEntity;
      $scope.currentEntity.markerAttribute.push(markerAttribute);

    });

    if($scope.currentEntity.latitude == null){
      var olCoordinates = ol.proj.transform([$scope.longitude, $scope.latitude], 'EPSG:4326', 'EPSG:900913');
      $scope.currentEntity.latitude  = olCoordinates[0];
      $scope.currentEntity.longitude = olCoordinates[1];
    }

    $scope.currentEntity.wktCoordenate = new ol.format.WKT().writeGeometry(new ol.geom.Point([$scope.currentEntity.latitude, $scope.currentEntity.longitude]));
    $scope.currentEntity.location.coordinateString = $scope.currentEntity.wktCoordenate;

    /* Remove image to update */
    angular.forEach($scope.currentEntity.markerAttribute, function(markerAttribute){
      if(markerAttribute.photoAlbum) {
        angular.forEach(markerAttribute.photoAlbum.photos, function(photo) {

          if (markerAttribute.attribute.removePhotosIds) {
            var index = markerAttribute.attribute.removePhotosIds.indexOf(photo.id);

            if (index != -1)
              delete markerAttribute.photoAlbum.photos[index];
          }

          delete photo.image;
        })
      }
    });

    markerService.updateMarker($scope.currentEntity, {
      callback: function (result) {

        $scope.toggleSidebarMarkerDetailUpdate(300, 'closeButton')

        $scope.msg = {type: "success", text: $translate("map.Mark-updated-succesfully"), dismiss: true};
        $("div.msgMap").show();

        setTimeout(function () {
          $("div.msgMap").fadeOut();
        }, 5000);

        $scope.$apply();
      },
      errorHandler: function (message, exception) {
        $scope.message = {type: "error", text: message};
        $scope.$apply();
      }
    });

  };

  $scope.insertMarker = function (status) {

    $scope.isLoading = true;

    if (!$scope.isBooleanValid()) {
      return false;
    }
    if (!$scope.form('sidebarMarker').$valid) {

      return;
    }

    var layer = new Layer();
    layer.id = $scope.currentEntity.layer.layerId;
    $scope.currentEntity.layer = layer;

    $scope.currentEntity.markerAttribute = [];

    angular.forEach($scope.attributesByLayer, function (val, ind) {

      var attribute = new Attribute();
      attribute.id = val.id;

      var markerAttribute = new MarkerAttribute();
      if (val.value != "" && val.value != undefined) {
        markerAttribute.value = val.value;
      } else {
        markerAttribute.value = "";
      }

      if(val.files) {

        attribute.type = "PHOTO_ALBUM";

        var photoAlbum = new PhotoAlbum();
        photoAlbum.photos = new Array();

        angular.forEach(val.files, function(file){
          var photo = new Photo();
          var img = file.src.split(';base64,');
          photo.source = img[1];
          photo.name = file.name;
          photo.description = file.description;
          photo.contentLength = file.size;
          photo.mimeType = file.type;
          photoAlbum.photos.push(photo);
        });

        markerAttribute.photoAlbum = photoAlbum;

      }

      markerAttribute.attribute = attribute;
      markerAttribute.marker = $scope.currentEntity;
      $scope.currentEntity.markerAttribute.push(markerAttribute);

    });

    $scope.currentEntity.wktCoordenate = new ol.format.WKT().writeGeometry(new ol.geom.Point([$scope.currentEntity.latitude, $scope.currentEntity.longitude]));

    $scope.currentEntity.status = status;

    markerService.insertMarker( $scope.currentEntity, {
      callback: function (result) {

        $scope.isLoading = false;

        $scope.map.removeLayer($scope.currentCreatingInternalLayer);

        $scope.removeInternalLayer($scope.currentEntity.layer.id, function (layerId) {
          $scope.addInternalLayer(layerId);
        });

        $scope.clearFcMarker(true);

        $scope.msg = {type: "success", text: $translate("map.Mark-inserted-succesfully"), dismiss: true};
        $("div.msgMap").show();

        setTimeout(function () {
          $("div.msgMap").fadeOut();
        }, 5000);

        $scope.$apply();
      },
      errorHandler: function (message, exception) {

        $scope.isLoading = false;

        $scope.msg = {type: "danger", text: message};

        $("div.msgMap").show();

        setTimeout(function () {
          $("div.msgMap").fadeOut();
        }, 5000);

        $scope.$apply();
      }
    });

  }

  $scope.showAttributesAlone = false;
  $scope.showNewAttributes = false;
  $scope.listAttributesByLayerUpdate = function () {

    var iconStyle = new ol.style.Style({
      image: new ol.style.Icon(({
        anchor: [0.5, 1],
        anchorXUnits: 'fraction',
        anchorYUnits: 'fraction',
        src: $scope.currentEntity.layer.icon
      }))
    });
    $scope.currentCreatingInternalLayer.setStyle(iconStyle);

    $scope.showAttributesAlone = true;

    if ($scope.attributesByMarker.length > 0) {
      if ($scope.attributesByMarker[0].marker.layer.id == $scope.currentEntity.layer.layerId) {
        $scope.showAttributesAlone = false;

        layerGroupService.listAttributesByLayer($scope.currentEntity.layer.layerId, {
          callback: function (result) {
            $scope.attributesByLayer = [];

            angular.forEach(result, function (attribute, index) {

              var exist = false;

              angular.forEach($scope.attributesByMarker, function (attributeByMarker, index) {

                if (attributeByMarker.attribute.id == attribute.id) {
                  exist = true;
                }
              });

              if (!exist) {
                $scope.attributesByLayer.push(attribute);
                $scope.showNewAttributes = true;
              }

            });

            $scope.$apply();
          },
          errorHandler: function (message, exception) {
            $scope.message = {type: "error", text: message};
            $scope.$apply();
          }
        });

        return false;
      }
    }

    layerGroupService.listAttributesByLayer($scope.currentEntity.layer.layerId, {
      callback: function (result) {
        $scope.attributesByLayer = result;
        $scope.$apply();
      },
      errorHandler: function (message, exception) {
        $scope.message = {type: "error", text: message};
        $scope.$apply();
      }
    });
  }

  $scope.listAttributesByLayer = function () {

    var iconStyle = new ol.style.Style({
      image: new ol.style.Icon(({
        anchor: [0.5, 1],
        anchorXUnits: 'fraction',
        anchorYUnits: 'fraction',
        src: $scope.currentEntity.layer.layerIcon
      }))
    });

    var shadowStyle = $scope.setShadowMarker();

    $scope.currentCreatingInternalLayer.setStyle([iconStyle, shadowStyle]);

    layerGroupService.listAttributesByLayer($scope.currentEntity.layer.layerId, {
      callback: function (result) {
        $scope.attributesByLayer = result;
        $scope.$apply();
      },
      errorHandler: function (message, exception) {
        $scope.message = {type: "error", text: message};
        $scope.$apply();
      }
    });
  }

  $scope.removeInternalLayer = function (layerId, callback) {
    var callBackHasExecuted = false;
    var internalLayers = $.extend([], $scope.internalLayers);

    angular.forEach(internalLayers, function (value, index) {
      if ($scope.hasSearch) {
        if (value.id == layerId || value.layerId == layerId) {
          $scope.map.removeLayer(value.layer);
          var len = $scope.internalLayers.length;

          for (var i = 0; i < len; i++) {
            if (value.id == $scope.internalLayers[i].id) {
              $scope.internalLayers.splice(i, 1);
              return;
            }
          }

          if (typeof callback != 'undefined' && !callBackHasExecuted) {
            callback(value.id);
            callBackHasExecuted = true;
          }
        }
      } else {
        if ((value.id == layerId || value.layerId == layerId) && value.searchId == undefined) {
          $scope.map.removeLayer(value.layer);
          var len = $scope.internalLayers.length;

          for (var i = 0; i < len; i++) {
            if (value.id == $scope.internalLayers[i].id) {
              $scope.internalLayers.splice(i, 1);
              return;
            }
          }

          if (typeof callback != 'undefined' && !callBackHasExecuted) {
            callback(value.id);
            callBackHasExecuted = true;
          }
        }
      }
    });

    if (!callBackHasExecuted) {
      if (typeof callback != 'undefined') {
        callback(layerId);
      }
    }
  };

  $scope.addInternalLayer = function (layerId) {

    markerService.listMarkerByLayer(layerId, {
      callback: function (result) {

        var iconPath = "static/images/marker.png";

        if (result.length > 0) {
          iconPath = result[0].layer.icon
        }

        var iconStyle = new ol.style.Style({
          image: new ol.style.Icon(({
            anchor: [0.5, 1],
            anchorXUnits: 'fraction',
            anchorYUnits: 'fraction',
            src: iconPath

          }))
        });

        var icons = [];

        angular.forEach(result, function (marker, index) {

          //$scope.exportMarkers.push(marker);
          /* var iconFeature = new ol.Feature({
           geometry: new ol.geom.Point([marker.latitude ,marker.longitude]),
           marker: marker,
           });*/

          var iconFeature = new ol.Feature({
            geometry: new ol.format.WKT().readGeometry(marker.location.coordinateString),
            marker: marker
          });

          var source = new ol.source.Vector({features: [iconFeature]});

          var layer = new ol.layer.Vector({
            source: new ol.source.Vector({features: [iconFeature]}),

            maxResolution: minEscalaToMaxResolutionn(marker.layer.minimumScaleMap),
            minResolution: maxEscalaToMinResolutionn(marker.layer.maximumScaleMap)
          });

          layer.setStyle(iconStyle);

          $scope.map.addLayer(layer);

          $scope.internalLayers.push({"layer": layer, "id": layerId, "feature": iconFeature, "extent": source.getExtent()});
        });

        $scope.$apply();

      },
      errorHandler: function (message, exception) {
        $scope.message = {type: "error", text: message};
        $scope.$apply();
      }
    });

  }

  $scope.addInternalLayerSearch = function (searchId) {

    var enterIn = true;
    angular.forEach($scope.internalLayersSearch, function (internalLayerSearch, index) {
      // verifica qual é a pesquisa selecionada...
      if (internalLayerSearch.searchId == searchId) {


        if (enterIn) {
          var internalLayers = $.extend([], $scope.internalLayers);
          $scope.internalLayers = [];

          //Percorre a lista das layers existentes no mapa...
          angular.forEach(internalLayers, function (internalLayer, index) {
            if (internalLayer.id == internalLayerSearch.layerId &&
              (internalLayer.searchId == internalLayerSearch.searchId || internalLayer.searchId == undefined)) {
              $scope.map.removeLayer(internalLayer.layer);

            } else {
              $scope.internalLayers.push(internalLayer);
            }
          });
          enterIn = false;
        }


        $scope.map.addLayer(internalLayerSearch.layer);
        $scope.internalLayers.push(internalLayerSearch);
      }
    });

    $scope.$apply();

  }

  $scope.removeInternalLayerSearch = function (searchId, layerId) {
    var internalLayersSearch = $.extend([], $scope.internalLayersSearch);

    angular.forEach(internalLayersSearch, function (internalLayerSearch, index) {
      if (internalLayerSearch.searchId == searchId) {
        $scope.map.removeLayer(internalLayerSearch.layer);

        angular.forEach($scope.internalLayers, function (internalLayer, index) {
          if (internalLayerSearch.searchId == internalLayer.searchId) {
            $scope.internalLayers.splice(index, 1);
            return false;
          }

        });
      }
    });

  }


  $scope.removeMarker = function () {

    var dialog = $modal.open({
      templateUrl: "static/libs/eits-directives/dialog/dialog-template.html",
      controller: DialogController,
      windowClass: 'dialog-enable',
      resolve: {
        title: function () {
          return $translate("map.Delete-mark")
        },
        message: function () {
          return $translate("map.Are-you-sure-you-want-to-delete-the-mark") + " ?"
        },
        buttons: function () {
          return [{
            label: $translate("layer-group-popup.Delete"),
            css: 'btn btn-danger'
          }, {label: $translate("admin.users.Cancel"), css: 'btn btn-default', dismiss: true}];
        }
      }
    });

    dialog.result.then(function (result) {

      markerService.removeMarker($scope.marker.id, {
        callback: function (result) {
          //$scope.map.removeOverlay($scope.markerDetail.overlay);


          $scope.removeInternalLayer($scope.marker.layer.id, function (layerId) {
            $scope.addInternalLayer(layerId);
          })

          $scope.features = [];

          $scope.toggleSidebarMarkerDetailUpdate(300, 'closeButton');

          $scope.msg = {type: "success", text: $translate("map.Mark-was-successfully-deleted"), dismiss: true};
          $("div.msgMap").show();

          setTimeout(function () {
            $("div.msgMap").fadeOut();
          }, 5000);

        },
        errorHandler: function (message, exception) {
          $scope.message = {type: "error", text: message};
          $scope.$apply();
        }
      });

    });

  };

  $scope.setPhotoMarker = function (element) {

    if (element.value == "") {
      $scope.imgResult = null;
      $scope.$apply();
      return false;
    }

    if (!(/\.(gif|jpg|jpeg|bmp|png)$/i).test(element.value) && element.value != "") {
      $("#upload-input").val("");
      $scope.msg = {text: $translate("map.The-selected-file-is-invalid"), type: "danger", dismiss: true};
      $("div.msgMap").show();

      setTimeout(function () {
        $("div.msgMap").fadeOut();
      }, 3500);
      $scope.$apply();
      return false;
    }

    $('#files')[0].files = element.files;

    $scope.currentEntity.image = element;
    $scope.readURL(element);
  };

  $scope.enableMarker = function () {

    var dialog = $modal.open({
      templateUrl: "static/libs/eits-directives/dialog/dialog-template.html",
      controller: DialogController,
      windowClass: 'dialog-success',
      resolve: {
        title: function () {
          return $translate('admin.marker-moderation.Confirm-approve');
        },
        message: function () {
          return $translate('admin.marker-moderation.Are-you-sure-you-want-to-approve-this-marker') + ' ? <br/>.';
        },
        buttons: function () {
          return [
            {label: $translate('admin.marker-moderation.Approve'), css: 'btn btn-success'},
            {label: 'Cancelar', css: 'btn btn-default', dismiss: true}
          ];
        }
      }
    });

    dialog.result.then(function () {

      markerModerationService.acceptMarker($scope.marker.id, {
        callback: function (result) {
          $scope.marker.status = "ACCEPTED";

          $scope.msg = {type: "success", text: $translate("map.Mark-was-successfully-enabled"), dismiss: true};
          $("div.msgMap").show();

          setTimeout(function () {
            $("div.msgMap").fadeOut();
          }, 5000);

          $scope.$apply();
        },
        errorHandler: function (message, exception) {
          $scope.message = {type: "error", text: message};
          $scope.$apply();
        }
      });

    });

  };

  $scope.disableMarker = function () {

    var dialog = $modal.open({
      templateUrl: "modules/admin/ui/marker-moderation/popup/refuse-marker.jsp",
      controller: RefuseMarkerController,
      windowClass: 'dialog-delete',
      resolve: {
        motive: function () {
          return $scope.motive;
        }
      }
    });


    dialog.result.then(function (result) {
      $scope.refuseMarkerModeration($scope.currentEntity.id, result.motive, result.description);
    });
  };

  $scope.refuseMarkerModeration = function (id, motive, description) {

    markerModerationService.refuseMarker(id, motive, description, {
      callback: function (result) {
        console.log(result);
        $scope.marker.status = "REFUSED";
        $scope.msg = {type: "success", text: $translate("map.Mark-was-successfully-disabled"), dismiss: true};
        $("div.msgMap").show();

        setTimeout(function () {
          $("div.msgMap").fadeOut();
        }, 5000);
        $scope.$apply();
      },
      errorHandler: function (message, exception) {
        $scope.message = {type: "error", text: message};
        $scope.$apply();
      }
    });
  };

  $scope.readURL = function (input) {

    if (input.files && input.files[0] && input.files[0].size < 2000000) {
      var reader = new FileReader();

      reader.onload = function (e) {
        console.log(e);
        $scope.imgResult = e.target.result;
        $scope.$apply();
        $('.marker-image').attr('src', e.target.result);
      };
      reader.readAsDataURL(input.files[0]);

    }
  };

  $scope.showGallery = function (attributesByMarker) {

    var dialog = $modal.open({
      templateUrl: 'modules/map/ui/popup/img-popup.jsp',
      controller: ImgPopUpController,
      windowClass: 'gallery-modal-window',
      resolve: {
        attributesByMarker: function(){
          return attributesByMarker;
        }
      }
    });
  };

  $scope.isBooleanValid = function () {

    $scope.ok = true;
    $.each($(".boolean").parent().parent(), function (index, value) {
      if ($(this).attr("required")) {


        if (!$(this).find(".boolean-1").is(":checked") && !$(this).find(".boolean-2").is(":checked")) {
          $scope.ok = false;

          $(this).find(".required-boolean").css("border", "1px solid red");
          $(this).find(".required-boolean").css("border-radius", "5px");
          $(this).prepend('<span class="tooltip-validation"style="top: 3px">' + $translate("map.Field-required") + '</span>');
        }


      }
    });

    return $scope.ok;
  };

  $scope.resolveDatepicker = function () {

    $scope.$watch('attributesByLayer', function (oldValue, newValue) {
      $timeout(function () {
        $('.datepicker').datepicker({
          dateFormat: 'dd/mm/yy',
          dayNames: ['Domingo', 'Segunda', 'Terça', 'Quarta', 'Quinta', 'Sexta', 'Sábado'],
          dayNamesMin: ['D', 'S', 'T', 'Q', 'Q', 'S', 'S', 'D'],
          dayNamesShort: ['Dom', 'Seg', 'Ter', 'Qua', 'Qui', 'Sex', 'Sáb', 'Dom'],
          monthNames: ['Janeiro', 'Fevereiro', 'Março', 'Abril', 'Maio', 'Junho', 'Julho', 'Agosto', 'Setembro', 'Outubro', 'Novembro', 'Dezembro'],
          monthNamesShort: ['Jan', 'Fev', 'Mar', 'Abr', 'Mai', 'Jun', 'Jul', 'Ago', 'Set', 'Out', 'Nov', 'Dez'],
          nextText: 'Próximo',
          prevText: 'Anterior'
        });

        $('.datepicker').mask("99/99/9999");
      }, 200);
    });
    $scope.$watch('screen', function (oldValue, newValue) {
      $timeout(function () {
        $('.datepicker').datepicker({
          dateFormat: 'dd/mm/yy',
          dayNames: ['Domingo', 'Segunda', 'Terça', 'Quarta', 'Quinta', 'Sexta', 'Sábado'],
          dayNamesMin: ['D', 'S', 'T', 'Q', 'Q', 'S', 'S', 'D'],
          dayNamesShort: ['Dom', 'Seg', 'Ter', 'Qua', 'Qui', 'Sex', 'Sáb', 'Dom'],
          monthNames: ['Janeiro', 'Fevereiro', 'Março', 'Abril', 'Maio', 'Junho', 'Julho', 'Agosto', 'Setembro', 'Outubro', 'Novembro', 'Dezembro'],
          monthNamesShort: ['Jan', 'Fev', 'Mar', 'Abr', 'Mai', 'Jun', 'Jul', 'Ago', 'Set', 'Out', 'Nov', 'Dez'],
          nextText: 'Próximo',
          prevText: 'Anterior'
        });

        $('.datepicker').mask("99/99/9999");
      }, 200);
    })

  };

  $scope.getPhotosByAttribute = function(attribute, index){

    var pageable = {
      size: 1,
      page: 0,
      sort: {//Sort
        orders: [
          {direction: 'DESC', property: 'created'}
        ]
      }
    };

    markerService.findPhotoAlbumByAttributeMarkerId(attribute.id, pageable, {
      callback: function (result) {
        /*$(filter)('filter')($scope.attributesByMarker, {id: attribute.id})[0].photoAlbum.photos = result;
        $(filter)('filter')($scope.attributesByMarker, {id: attribute.id})[0].photoAlbum = new PhotoAlbum();*/

        $scope.attributesByMarker[index].photoAlbum = result.content[0].photoAlbum;
        $scope.attributesByMarker[index].photoAlbum.photos = result.content;

        $scope.imgResult = result.content[0].image;

        $scope.$apply();
      },
      errorHandler: function (message, exception) {
        $scope.message = {type: "error", text: message};
        $scope.$apply();
      }
    })

  };

  $scope.toggleSidebarMarkerDetailUpdate = function (time, element) {
    $scope.currentEntity = $scope.marker;

    if (element == "closeButton") {
      $scope.screenMarkerOpenned = false;
      $scope.toggleSidebar(time, 'closeButton', '#sidebar-marker-detail-update');

      $scope.clearShadowCreatingInternalLayer();
      return;
    }

    if (typeof $scope.marker != "undefined") {
      markerService.findImgByMarker($scope.marker.id, {
        callback: function (result) {

          $scope.imgResult = result;
        },
        errorHandler: function (message, exception) {
          $scope.message = {type: "error", text: message};
          $scope.$apply();
        }
      });

      $scope.attributesByLayer = [];
      $scope.showNewAttributes = false;

      markerService.listAttributeByMarker($scope.marker.id, {
        callback: function (result) {
          $scope.attributesByMarker = result;

          layerGroupService.listAttributesByLayer($scope.marker.layer.id, {
            callback: function (result) {
              $scope.attributesByLayer = [];

              angular.forEach(result, function (attribute, index) {

                var exist = false;

                angular.forEach($scope.attributesByMarker, function (attributeByMarker, index) {

                  if (attributeByMarker.attribute.id == attribute.id) {
                    exist = true;

                    if(attributeByMarker.attribute.type == 'PHOTO_ALBUM')
                      $scope.getPhotosByAttribute(attributeByMarker, index);

                  }

                });

                if (!exist) {
                  $scope.attributesByLayer.push(attribute);
                  $scope.showNewAttributes = true;
                }

              });

              $scope.$apply();
            },
            errorHandler: function (message, exception) {
              $scope.message = {type: "error", text: message};
              $scope.$apply();
            }
          });

          angular.forEach(result, function (markerAttribute, index) {
            if (markerAttribute.attribute.type == "NUMBER") {
              markerAttribute.value = parseInt(markerAttribute.value);
            }
          })


          $scope.$apply();

        },
        errorHandler: function (message, exception) {
          $scope.message = {type: "error", text: message};
          $scope.$apply();
        }
      });
    }

    layerGroupService.listAllInternalLayerGroups({
      callback: function (result) {

        $scope.selectLayerGroup = [];

        angular.forEach(result, function (layer, index) {

          $scope.selectLayerGroup.push($.extend(layer, {
            "layerTitle": layer.title,
            "layerId": layer.id,
            "group": layer.layerGroup.name
          }));

        })

        markerService.listAttributeByMarker($scope.currentEntity.id, {
          callback: function (result) {
            $scope.attributesByMarker = result;

            angular.forEach(result, function (markerAttribute, index) {
              if (markerAttribute.attribute.type == "NUMBER") {
                markerAttribute.value = parseInt(markerAttribute.value);
              }
            })

            angular.forEach($scope.selectLayerGroup, function (layer, index) {
              if (layer.layerId == $scope.currentEntity.layer.id) {
                layer.created = $scope.currentEntity.layer.created;
                $scope.currentEntity.layer = layer;

                return false;
              }
            })

            $scope.$apply();
          },
          errorHandler: function (message, exception) {
            $scope.message = {type: "error", text: message};
            $scope.$apply();
          }
        });

      },
      errorHandler: function (message, exception) {
        $scope.message = {type: "error", text: message};
        $scope.$apply();
      }
    });

    if ($scope.slideActived == '#sidebar-marker-detail-update') {
      $(".panel-body").height($("#sidebar-marker-detail-update").height() - 68 - 30);
      return
    }

    if ($scope.slideActived == '#sidebar-layers') {
      $scope.toggleSidebar(time, 'closeButton', '#sidebar-layers');

      $timeout(function () {
        $scope.toggleSidebar(time, '', '#sidebar-marker-detail-update');
      }, 400);

    } else {
      $scope.toggleSidebar(time, '', '#sidebar-marker-detail-update');
    }
    $scope.resolveDatepicker();
    $(".panel-body").height($("#sidebar-marker-detail-update").height() - 68 - 30);
  };

  $scope.isChecked = function () {
    if ($(".yes").is(':checked')) {
      return ".yes";
    }

    if ($(".no").is(':checked')) {
      return ".no";
    }
  };

  /* EXPORTAR IMPORTAR SHAPEFILE */

  $scope.shapeFile = {};
  $scope.shapeFile.form = {};

  //$scope.shapeFile.layerType = 'new';
  $scope.shapeFile.layerType = 'layer';

  $scope.isImport = false;
  $scope.isExport = false;

  var uploadButton = angular.element('#upload');

  $scope.clickUpload = function(){
    angular.element('#upload').trigger('click');
  };

  layerGroupService.listAllInternalLayerGroups({
    callback: function (result) {
      $scope.selectLayerGroup = [];

      angular.forEach(result, function (layer, index) {

        $scope.selectLayerGroup.push({
          "layerTitle": layer.title,
          "layerId": layer.id,
          "layerIcon": layer.icon,
          "group": layer.layerGroup.name
        });
      });
      $scope.$apply();
    },
    errorHandler: function (message, exception) {
      $scope.message = {type: "error", text: message};
      $scope.$apply();
    }
  });

  $scope.setAction = function (type) {

    if (type == 'import') {

      $scope.isImport = true;
      $scope.isExport = false;

      $scope.shapeFile.form.icon = 'static/icons/default_blue.png';

      /**
       * @type {Array}
       * */
      $scope.attributes = [];

      /**
       *
       * @type {Array}
       */
      $scope.selectedGroups = [];

      /**
       *
       * @type {Array}
       */
      $scope.originalGroups = [];

      /**
       *
       * @type {Array}
       */
      $scope.removeGroups = [];

      /**
       *
       * @type {Array}
       */
      $scope.addGroups = [];



    } else {

      $scope.clearImportMarkers();

      $scope.isImport = false;
      $scope.isExport = true;

      $scope.currentPage = {};

      $scope.currentPage.pageable = null;

      /**
       * filter
       */
      $scope.shapeFile.filter = {
        'layer': null,
        'status': null,
        'dateStart': null,
        'dateEnd': null,
        'user': null
      };

      $scope.listAllUsers();

      /*layerGroupService.listLayersByFilters( null, null, {

        callback: function (result) {

          $scope.shapeFile.layers = result.content;

          $scope.$apply();
        },
        errorHandler: function (message, exception) {
          $scope.message = {type: "error", text: message};
          $scope.$apply();
        }
      });*/

      layerGroupService.listAllInternalLayerGroups({
        callback: function (result) {
          $scope.selectLayerGroup = [];

          angular.forEach(result, function (layer, index) {

            $scope.selectLayerGroup.push({
              "layerTitle": layer.title,
              "layerId": layer.id,
              "layerIcon": layer.icon,
              "group": layer.layerGroup.name
            });

          })

          $scope.currentState = $scope.LIST_STATE;

          $scope.$apply();
        },
        errorHandler: function (message, exception) {
          $scope.message = {type: "error", text: message};
          $scope.$apply();
        }
      });

      $scope.resolveDatePicker();

    }
  };

  //DATA GRID
  /**
   * Static variable coms stock grid buttons
   * The Edit button navigates via URL (sref) why editing is done in another page,
   * Since the delete button calls a method directly via ng-click why does not have a specific screen state.
   */
  var GRID_ACTION_BUTTONS = '<div class="cell-centered button-action">' +
    '<a ui-sref="layer-config.update({id:row.entity.id})"  " title="'+ $translate("admin.layer-config.Update") +'" class="btn btn-mini"><i class="itaipu-icon-edit"></i></a>' +
    '<a ng-click="changeToRemove(row.entity)" title="'+ $translate("admin.layer-config.Delete") +'" class="btn btn-mini"><i class="itaipu-icon-delete"></i></a>' +
    '</div>';

  var LAYER_TYPE_NAME = '<div class="ngCellText ng-scope col4 colt4">' +
    '<span ng-if="!row.entity.dataSource.url" ng-cell-text="" class="ng-binding">Camada interna</span>' +
    '<span ng-if="row.entity.dataSource.url" ng-cell-text="" class="ng-binding">{{ row.entity.name }}</span>' +
    '</div>';


  var MARKER_BUTTONS = '<div  class="cell-centered">' +
    '<a ng-if="!row.entity.dataSource.url && row.entity.enabled == false" class="btn btn-mini"><i style="font-size: 16px; color: red" class="glyphicon glyphicon-ban-circle"></i></a>'+
    '<a ng-if="!row.entity.dataSource.url && row.entity.enabled == true" class="btn btn-mini"><i style="font-size: 16px; color: green" class="glyphicon glyphicon-ok"></i></a>'+
    '<a ng-if="row.entity.dataSource.url" class="btn btn-mini"><i style="font-size: 16px; color: blue" class="glyphicon glyphicon glyphicon-minus"></i></a>'+
    '</div>';

  var IMAGE_LEGEND = '<div class="ngCellText" ng-cell-text ng-class="col.colIndex()">' +
    '<img ng-if="row.entity.dataSource.url" style="width: 20px; height: 20px; border: solid 1px #c9c9c9;" ng-src="{{row.entity.legend}}"/>' +
    '<img ng-if="!row.entity.dataSource.url" style="width: 20px; height: 20px; border: solid 1px #c9c9c9;" ng-src="{{row.entity.icon}}"/>' +
    '</div>';

  /**
   * General settings in the ng-grid.
   * @see https://github.com/angular-ui/ng-grid/wiki/Configuration-Options
   */
  $scope.gridOptions = {
    data: 'currentPage.content',
    multiSelect: false,
    useExternalSorting: true,
    headerRowHeight: 45,
    rowHeight: 45,
    beforeSelectionChange: function (row, event) {
      //avoids calling the selecao, when clicked in an action button.
      if ($(event.target).is("a") || $(event.target).is("i")) return false;
      $state.go($scope.DETAIL_STATE, {id: row.entity.id});
    },
    columnDefs: [
      {displayName: 'Postagem', sortable: false, cellTemplate: MARKER_BUTTONS, width: '6%'},
      {displayName: $translate('admin.layer-config.Symbology'), field:'legend', sortable:false, width: '6%', cellTemplate: IMAGE_LEGEND},
      {displayName: $translate('Title'), field: 'title', width: '19%'},
      //{displayName: $translate('Layer'), field: 'name', width: '19%'},
      {displayName: $translate('Layer'), cellTemplate: LAYER_TYPE_NAME, width: '19%'},
      {displayName: $translate('admin.datasource.Data-Source'), field: 'dataSource.name', width: '30%'},
      {displayName: $translate('admin.layer-config.Layer-group'), field: 'layerGroup.name', width: '13%'},
      {displayName: $translate('Actions'), sortable: false, cellTemplate: GRID_ACTION_BUTTONS, width: '7%'}
    ]
  };

  var GRID_ACTION_ACCESS_BUTTONS = '<div class="cell-centered">' +
    '<a ng-click="removeAccessGroup(row.entity)" ng-if="currentState != DETAIL_STATE" title="Excluir" class="btn btn-mini"><i class="itaipu-icon-delete"></i></a>' +
    '</div>';

  /**
   * General settings of ng-grid.
   * @see https://github.com/angular-ui/ng-grid/wiki/Configuration-Options
   */

  $scope.gridAccessOptions = {
    data: 'selectedGroups',
    multiSelect: false,
    headerRowHeight: 45,
    rowHeight: 45,
    beforeSelectionChange: function (row, event) {
      return false;
    },
    columnDefs: [
      {displayName:$translate('Name'), field:'name'},
      {displayName:$translate('Description'), field:'description'},
      {displayName: '',sortable: false, cellTemplate: GRID_ACTION_ACCESS_BUTTONS, width: '100px'}
    ]
  };

  var GRID_ACTION_ATTRIBUTES_BUTTONS = '<div class="cell-centered">' +
    '<a ng-if="!row.entity.attributeDefault" ng-click="updateAttribute(row.entity)" ng-if="currentState != DETAIL_STATE" title="Update" class="btn btn-mini"><i class="itaipu-icon-edit"></i></a>' +
    '<a ng-if="!row.entity.attributeDefault" ng-click="removeAttribute(row.entity)" ng-if="currentState != DETAIL_STATE" title="Excluir" class="btn btn-mini"><i class="itaipu-icon-delete"></i></a>' +
    '</div>';

  var TYPE_COLUMN = '<div class="ngCellText ng-scope col2 colt2">' +
    '<span ng-cell-text="" class="ng-binding" ng-if="row.entity.type == \'DATE\'" >'+ $translate("admin.layer-config.DATE") +'</span>' +
    '<span ng-cell-text="" class="ng-binding" ng-if="row.entity.type == \'BOOLEAN\'" >'+ $translate("admin.layer-config.BOOLEAN") +'</span>' +
    '<span ng-cell-text="" class="ng-binding" ng-if="row.entity.type == \'TEXT\'" >'+ $translate("admin.layer-config.TEXT") +'</span>' +
    '<span ng-cell-text="" class="ng-binding" ng-if="row.entity.type == \'NUMBER\'" >'+ $translate("admin.layer-config.NUMBER") +'</span>' +
    '</div>';

  var REQUIRED_COLUMN = '<div class="ngCellText ng-scope col2 colt2">' +
    '<span ng-cell-text="" class="ng-binding" ng-if="row.entity.required == false" >'+ $translate("admin.layer-config.false") +'</span>' +
    '<span ng-cell-text="" class="ng-binding" ng-if="row.entity.required == true" >'+ $translate("admin.layer-config.true") +'</span>' +
    '</div>';

  /**
   * Configurações gerais da ng-grid.
   * @see https://github.com/angular-ui/ng-grid/wiki/Configuration-Options
   */
  $scope.gridAttributes = {
    data: 'attributes',
    useExternalSorting: false,
    multiSelect: false,
    headerRowHeight: 45,
    rowHeight: 45,
    beforeSelectionChange: function(row) {
      row.changed = true;
      return true;
    },
    afterSelectionChange: function (row, event) {
      if (row.changed){
        $scope.currentAttribute = row.entity;
        row.changed = false;
      }
    },
    columnDefs: [
      {displayName: $translate('Name'), field: 'name', width: '30%'},
      {displayName: $translate('Type'), field: 'type', cellTemplate:TYPE_COLUMN ,  width: '30%'},
      {displayName: $translate('Required'),field: 'required', sortable: false, cellTemplate: REQUIRED_COLUMN},
//            	'<div>' +
//                '<input type="checkbox" disabled="disabled" ng-checked="row.entity.required" >' +
//                '</div>', width: '30%'},
      {displayName: '', sortable: false, cellTemplate: GRID_ACTION_ATTRIBUTES_BUTTONS, width: '10%'}
    ]
  };


  /**
   * Configurações gerais da ng-grid.
   * @see https://github.com/angular-ui/ng-grid/wiki/Configuration-Options
   */
  $scope.gridAttributesDetail = {
    data: 'attributes',
    useExternalSorting: false,
    multiSelect: false,
    headerRowHeight: 45,
    rowHeight: 45,
    beforeSelectionChange: function (row, event) {
      //evita chamar a selecao, quando clicado em um action button.
      /*f ( $(event.target).is("a") || $(event.target).is("i") ) return false;
       $state.go($scope.DETAIL_STATE, {id:row.entity.id});*/
    },
    columnDefs: [
      {displayName: $translate('Name'), field: 'name', width: '33%'},
      {displayName: $translate('Type'),  cellTemplate: TYPE_COLUMN ,  width: '33%'},
      {displayName: $translate('Required'), cellTemplate: REQUIRED_COLUMN,  width: '33%'},
    ]
  };

  /**
   * Variable that stores the State of the paging
   * to render the pager and also to make requisitions of
   * new pages, containing the State of the Sort included.
   *
   * @type PageRequest
   */
  $scope.currentPage;

  /**
   * Filter
   */
  $scope.bindFilter = function() {
    var pageRequest = new PageRequest();
    pageRequest.size = 10;
    $scope.pageRequest = pageRequest;

    if($scope.shapeFile.filter.status == "")
      $scope.shapeFile.filter.status = null;
    if($scope.shapeFile.filter.user != null)
      var userEmail = $scope.shapeFile.filter.user.email;
    if ($scope.shapeFile.filter.dateStart == "")
      $scope.shapeFile.filter.dateStart = null;
    if ($scope.shapeFile.filter.dateEnd == "")
      $scope.shapeFile.filter.dateEnd = null;

    $scope.listMarkerByFilters( $scope.shapeFile.filter.layer, $scope.shapeFile.filter.status, $scope.shapeFile.filter.dateStart, $scope.shapeFile.filter.dateEnd, userEmail, pageRequest );
    $scope.listMarkerByFiltersMap($scope.shapeFile.filter.layer, $scope.shapeFile.filter.status, $scope.shapeFile.filter.dateStart, $scope.shapeFile.filter.dateEnd, userEmail);
    $scope.dragMarkers = null;
    $scope.hasSearch = true;
  };

  /**
   * Performs the query logs, considering filter, paging and sorting.
   * When ok, change the state of the screen to list.
   *
   * @see data.filter
   * @see currentPage
   */
  $scope.listMarkerByFilters = function( layer, status, dateStart, dateEnd, user, pageRequest ) {

    markerService.listMarkerByFilters( layer, status, dateStart, dateEnd, user, pageRequest, {
      callback : function(result) {

        $scope.currentPage = result;
        $scope.currentPage.pageable.pageNumber++;
        $scope.currentState = $scope.LIST_STATE;
        $scope.$apply();
      },
      errorHandler : function(message, exception) {
        $scope.msg = {type:"danger", text: message, dismiss:true};
        $scope.fadeMsg();
        $scope.$apply();
      }
    });
  };

  /**
   * Performs the query logs, considering filter, paging and sorting.
   * When ok, change the state of the screen to list.
   *
   * @see data.filter
   * @see currentPage
   */
  $scope.listMarkerByFiltersMap = function( layer, status, dateStart, dateEnd, user ) {

    markerService.listMarkerByFiltersMap( layer, status, dateStart, dateEnd, user, {
      callback : function(result) {
        if($scope.features.length) {
          $scope.clearFeatures();
          $scope.removeLayers();
        }
        var markers = { 'content' : null };
        markers.content = result;

        $scope.buildVectorMarker(markers);
        $scope.$apply();
      },
      errorHandler : function(message, exception) {
        $scope.msg = {type:"danger", text: message, dismiss:true};
        $scope.fadeMsg();
        $scope.$apply();
      }
    });
  };

  $scope.clearFilters = function(){

    $scope.shapeFile.filter.layer = null;
    $scope.shapeFile.filter.status = null;
    $scope.shapeFile.filter.dateStart= null;
    $scope.shapeFile.filter.dateEnd= null;
    $scope.shapeFile.filter.user = null;

  };

  /**
   * List all the Users
   */
  $scope.listAllUsers = function() {

    accountService.listAllUsers({
      callback: function (result) {
        $scope.selectUsers = [];

        angular.forEach(result, function (user, index) {

          $scope.selectUsers.push({
            "name": user.name,
            "email": user.email,
            "userName": user.username,
          });

        });

        $scope.$apply();
      },
      errorHandler: function (message, exception) {
        $scope.message = {type: "error", text: message};
        $scope.$apply();
      }
    });
  };

  /**
   *
   */
  $scope.selectDataSource = function () {
    var dialog = $modal.open({
      templateUrl: "modules/admin/ui/layer-config/popup/data-source-popup.jsp",
      controller: SelectDataSourcePopUpController,
      resolve: {
        dataSourceSelected: function () {
          return $scope.shapeFile.form.dataSource;
        }
      }
    });

    dialog.result.then(function (result) {

      // assigns the selected data

      if( $scope.shapeFile.form.dataSource && $scope.shapeFile.form.dataSource.id != result.id )
      {
        $scope.shapeFile.form.dataSource = result;
        $scope.shapeFile.form.title = null;
        $scope.shapeFile.form.name = null;
      }
      else
      {
        $scope.shapeFile.form.dataSource = result;
      }

    });
  };

  /*-------------------------------------------------------------------
   *                 POPUP - CONFIGURAÇÕES DE CAMADA
   *-------------------------------------------------------------------*/
  $scope.selectLayerConfig = function () {

    //Função responsável por chamar a popup de configurações de camada para associação.
    var dialog = $modal.open({
      templateUrl: 'modules/admin/ui/custom-search/popup/layer-config-popup.jsp',
      controller: SelectLayerConfigPopUpController,
      windowClass: 'xx-dialog',
      resolve: {
        dataSource : function () {
          return $scope.shapeFile.form.dataSource;
        },
        selectedLayer : function () {
          return $scope.shapeFile.form.layer;
        }
      }
    });

    dialog.result.then(function (result) {

      if (result) {
        $scope.shapeFile.form.layer = result;
      }

    });

  };

  /**
   * Add attribute
   * */
  $scope.addAttribute = function() {

    var dialog = $modal.open({
      templateUrl: "modules/admin/ui/layer-config/popup/add-attribute-import-popup.jsp",
      controller: AddAttributeImportPopUpController,
      windowClass: 'xx-dialog',
      resolve: {
        markerAttributes: function () {
          return $scope.importMarkers[0].markerAttribute;
        }
      }
    });

    dialog.result.then(function (result) {

      $scope.attributesByLayer = result.attributesByLayer;

    });
  };

  /**
   * Associate attribute
   * */
  $scope.associateAttribute = function() {
    var dialog = $modal.open({
      templateUrl: "modules/admin/ui/layer-config/popup/associate-attribute-import-popup.jsp",
      controller: AssociateAttributeImportPopUpController,
      windowClass: 'xx-dialog',
      resolve: {
        layer: function () {
          return $scope.shapeFile.form.layer;
        },
        markerAttributes: function () {
          return $scope.importMarkers[0].markerAttribute;
        }
      }
    });

    dialog.result.then(function (result) {

      $scope.attributesByLayer = result.attributesByLayer;

    });
  };

  $scope.moreIcons = function() {
    var dialog = $modal.open({
      templateUrl: "modules/admin/ui/layer-config/popup/more-icons-popup.jsp",
      controller: MorePopupController,
      windowClass: 'xx-dialog',
      resolve: {
        currentState: function(){
          return "layer-config.create";
        },
        currentEntity: function () {
          return $scope.shapeFile.form;
        }
      }
    });

    dialog.result.then(function (result) {

      if (result) {

        $scope.shapeFile.form.layerIcon = result.icon;

        angular.forEach($scope.importLayers, function (layer, index) {

          var iconStyle = new ol.style.Style({
            image: new ol.style.Icon(({
              anchor: [0.5, 1],
              anchorXUnits: 'fraction',
              anchorYUnits: 'fraction',
              src: $scope.shapeFile.form.layerIcon
            }))
          });

          layer.setStyle(iconStyle);

        });
      }
    });
  };

  /**
   *
   */
  $scope.selectLayerGroupPopup = function () {

    var request = {
      callback: function (result) {

        var dialog = $modal.open({
          templateUrl: "modules/admin/ui/layer-config/popup/layer-group-popup.jsp",
          controller: SelectLayerGroupPopUpController,
          windowClass: 'xx-dialog grupo-camada-dialog',
          resolve: {
            layerGroups: function () {
              return result;
            },
            currentLayerGroup: function () {
              return $scope.shapeFile.form.layerGroup;
            }
          }
        });

        dialog.result.then(function (result) {

          if (result) {
            $scope.shapeFile.form.layerGroup = result;
            $scope.shapeFile.form.layerGroup.name = result.label;
          }

        });

      },
      errorHandler: function (message, exception) {
        $scope.message = {type: "error", text: message};
        $scope.$apply();
      }
    };

    // checks if is internal layer
    if($scope.shapeFile.form.dataSource.url == null){
      layerGroupService.listLayersGroupUpper(request);
    } else {
      layerGroupService.listSupervisorsFilter($scope.shapeFile.form.name, $scope.shapeFile.form.dataSource.id, request);
    }

  };

  $scope.selectAccessGroups = function() {
    var dialog = $modal.open({
      templateUrl: "modules/admin/ui/custom-search/popup/access-group-popup.jsp",
      controller: SelectAccessGroupPopUpController,
      resolve: {
        selectedGroups : function () {
          return $scope.selectedGroups;
        }
      }
    });

    dialog.result.then(function (result) {

      if (result) {
        for (var i = 0; i < result.length; i++) {
          var index = $scope.findByIdInArray($scope.selectedGroups, result[i]);
          var index2 = $scope.findByIdInArray($scope.originalGroups, result[i]);
          var index3 = $scope.findByIdInArray($scope.removeGroups, result[i]);

          //Identifica se marcou novos registros
          if (index == -1 && index2 == -1) {
            var indexAdd = $scope.findByIdInArray($scope.addGroups, result[i]);
            if (indexAdd == -1)
              $scope.addGroups.push(result[i]);
          }

          if (index3 > -1) {
            $scope.removeGroups.splice(index3, 1);
          }

        }
        for (var i = 0; i < $scope.selectedGroups.length; i++) {

          var index = $scope.findByIdInArray(result, $scope.selectedGroups[i]);

          if (index == -1) {
            var index2 = $scope.findByIdInArray($scope.addGroups, $scope.selectedGroups[i]);
            var index3 = $scope.findByIdInArray($scope.removeGroups, $scope.selectedGroups[i]);
            var index4 = $scope.findByIdInArray($scope.originalGroups, $scope.selectedGroups[i]);

            if (index2 > -1){
              var indexAdd = $scope.findByIdInArray($scope.removeGroups, $scope.selectedGroups[i]);
              if (indexAdd > -1)
                $scope.adicionarGrupos.splice(indexAdd, 1);
            }
            if (index3 == -1 && index4 > -1) {
              $scope.removeGroups.push($scope.selectedGroups[i]);
            }

          }
        }
        $scope.selectedGroups = result;
      }

    });
  };

  $scope.exportShapeFile= function (){

    $scope.isLoading = true;

	  shapeFileService.exportShapeFile( $scope.exportMarkers, {
         callback: function (result) {

           $scope.isLoading = false;

              $('body').append('<a id="export-download" href="' + result + '"></a>');
              $('#export-download')[0].click();
           $('#export-download').remove();

           $scope.$apply();
         },
         errorHandler: function (message, exception) {
          alert(message);
          $scope.$apply();
         }
      });
    };

  $scope.insertMarkers = function () {

    $scope.isLoading = true;

    var importMarkers = [];

    angular.forEach($scope.importMarkers, function(marker){

      console.log(marker);

      $scope.currentEntity = marker;

      var markerAttributes = marker.markerAttribute;

      var layer = new Layer();
      layer.id = $scope.shapeFile.form.layer.id;
      $scope.currentEntity.layer = layer;

      $scope.currentEntity.markerAttribute = [];

      angular.forEach($scope.attributesByLayer, function (val, ind) {

        var attribute = new Attribute();
        attribute.id = val.id;

        var markerAttribute = new MarkerAttribute();

        angular.forEach(markerAttributes, function(attr){
          if(attr.attribute.name == val.name && attr.attribute.type == val.type) {
            markerAttribute.value = attr.value;
          }
        });

        markerAttribute.attribute = attribute;
        markerAttribute.marker = $scope.currentEntity;
        $scope.currentEntity.markerAttribute.push(markerAttribute);

      });

      $scope.currentEntity.wktCoordenate = $scope.currentEntity.location.coordinateString;

      $scope.currentEntity.status = 'SAVED';

      importMarkers.push($scope.currentEntity);

      /*markerService.insertMarker( $scope.currentEntity, {
        callback: function (result) {

          console.log(result);

          $scope.$apply();
        },
        errorHandler: function (message, exception) {
          $scope.message = {type: "error", text: message};
          $scope.$apply();
        }
      });*/

      //$scope.toggleSidebarMenu(300, 'closeButton');

    });

    markerService.insertMarker( importMarkers, {
      callback: function (result) {

        console.log('Imported');

        $scope.isLoading = false;

        $scope.$apply();

      }, errorHandler: function (message, exception) {
        $scope.message = {type: "error", text: message};
        $scope.$apply();
      }
    });

    $scope.toggleSidebarMenu(300, 'closeButton');

  };

  /**
   * Performs the insertion of a new record
   * and in the success, modifies the State of the screen for the detail.
   */
  $scope.importShapeFile = function () {

    if($scope.shapeFile.layerType == 'new') {

      $scope.shapeFile.form.minimumScaleMap = 'UM' + $scope.layers.values[0].substring(2);
      $scope.shapeFile.form.maximumScaleMap = 'UM' + $scope.layers.values[1].substring(2);

      if (!$scope.form().$valid) {
        $scope.msg = {
          type: "danger",
          text: $translate("admin.layer-config.The-highlighted-fields-are-required"),
          dismiss: true
        };
        $scope.fadeMsg();
        return;
      }

      $scope.shapeFile.form.name = $scope.shapeFile.form.title;

      /*for (var k = 0; k < $scope.currentPage.content.length; k++) {
        if ($scope.currentEntity.title.toUpperCase() == $scope.currentPage.content[k].title.toUpperCase()) {
          $scope.msg = {
            type: "danger",
            text: $translate('admin.layer-config.The-field-name-already-exists,-change-and-try-again'),
            dismiss: true
          };
          $scope.fadeMsg();
          return;
        }
      }*/

      if (($scope.shapeFile.form.dataSource.url == null) && ($scope.shapeFile.form.icon == undefined)) {
        $scope.msg = {type: "danger", text: $translate("admin.layer-config.Choose-an-icon"), dissmiss: true};
        $scope.fadeMsg();
        return;
      }

      if ($scope.shapeFile.form.legend == null) {

        var attributes = [];
        angular.forEach($scope.attributesByLayer, function (value, index) {

          value.attribute.layer = $scope.shapeFile.form;

          if(!value.attribute.required) value.attribute.required = false;
          if(!value.attribute.visible) value.attribute.visible = false;

          attributes.push(value.attribute);

        });

        $scope.shapeFile.form.attributes = attributes;
      }

      layerGroupService.insertLayer($scope.shapeFile.form, {
        callback: function (result) {

          $scope.msg = {
            type: "success",
            text: $translate("admin.layer-config.The-layer-has-been-created-successfully") + "!",
            dismiss: true
          };

          $scope.shapeFile.form.layer = result;

          $scope.attributesByLayer = [];

          layerGroupService.listAttributesByLayer(result.id, {
            callback: function (result) {

              angular.forEach(result, function(attribute){

                if(attribute.type != 'PHOTO_ALBUM') {
                  attribute.option = attribute.name + ' (' + attribute.type + ')';
                  $scope.attributesByLayer.push(attribute);
                }

              });

              $scope.insertMarkers();

              $scope.$apply();
            },
            errorHandler: function (message, exception) {
              $scope.message = {type: "error", text: message};
              $scope.$apply();
            }
          });

          $scope.$apply();
          $scope.saveGroups();
        },
        errorHandler: function (message, exception) {
          $scope.msg = {type: "danger", text: message, dismiss: true};
          $scope.$apply();
        }
      });
    } else {

      angular.forEach($scope.attributesByLayer, function(attribute){

        if(attribute.required && attribute.option == '') {
          $scope.msg = {type: "danger", text: $translate("admin.layer-config.Assign-the-required-fields"), dissmiss: true};
          $scope.fadeMsg();
          return;
        }

      });

      $scope.insertMarkers();
    }

  };

  /**
   *
   */
  $scope.saveGroups = function() {
    if ($scope.addGroups.length > 0) {
      $scope.linkGroups();
    }
    if ($scope.removeGroups.length > 0) {
      $scope.unlinkGroups();
    }
  }

  /**
   *
   */
  $scope.linkGroups = function() {
    layerGroupService.linkAccessGroup($scope.addGroups, $scope.shapeFile.form.layer.id, {
      callback: function(){
        $scope.addGroups = [];
        $scope.$apply();
      },
      errorHandler: function(error){
        $log.error(error);
      }
    })
  };

  /**
   *
   */
  $scope.unlinkGroups = function() {
    layerGroupService.unlinkAccessGroup($scope.removeGroups, $scope.currentEntity.id, {
      callback: function(){
        $scope.removeGroups = [];
        $scope.$apply();
      },
      errorHandler: function(error){
        $log.error(error);
      }
    });
  };

  $scope.fadeMsg = function(){
    $("div.msg").show();

    setTimeout(function(){
      $("div.msg").fadeOut();
    }, 5000);
  };

  $scope.shapeFileFilter = function() {

    $scope.internalLayers.forEach(function (layer) {

      $scope.map.removeLayer(layer.layer);

    });

    $scope.exportMarkers = [];
    $scope.exportLayers = [];

    var layer = null;
    var userEmail = null;

    if ($scope.shapeFile.filter.status == "")
      $scope.shapeFile.filter.status = null;
    if ($scope.shapeFile.filter.user != null)
      userEmail = $scope.shapeFile.filter.user.email;
    if ($scope.shapeFile.filter.dateStart == "")
      $scope.shapeFile.filter.dateStart = null;
    if ($scope.shapeFile.filter.dateEnd == "")
      $scope.shapeFile.filter.dateEnd = null;
    if ($scope.shapeFile.filter.layer != null)
      layer = $scope.shapeFile.filter.layer.layerTitle;

    markerService.listMarkerByFilters(layer, $scope.shapeFile.filter.status, $scope.shapeFile.filter.dateStart, $scope.shapeFile.filter.dateEnd, userEmail, null, {
      callback: function (result) {

        $scope.internalLayers.forEach(function(layer) {
          var marker = layer.feature.getProperties().marker;

          var index = $filter('filter')(result.content, {id: marker.id})[0];

          if(index) {
            $scope.map.addLayer(layer.layer);
            $scope.exportLayers.push(layer);
            $scope.exportMarkers.push(marker);
          }

        });

        var coordinates = [];
        var extent = '';

        angular.forEach(result.content, function(marker){

          var geometry = new ol.format.WKT().readGeometry(marker.location.coordinateString);
          coordinates.push(geometry.getCoordinates());
          extent = new ol.extent.boundingExtent(coordinates);

        });

        var zoom = $scope.map.getView().getZoom();
        $scope.map.getView().fitExtent(extent, $scope.map.getSize());
        $scope.map.getView().setZoom(zoom);

        $scope.$apply();

      },
      errorHandler: function (message, exception) {
        $scope.msg = {type: "danger", text: message, dismiss: true};
        $scope.fadeMsg();
        $scope.$apply();
      }
    });

  };

  $scope.clearImportMarkers = function() {

    $scope.importMarkers = [];
    $scope.testFiles = [];
    $('#upload')[0].val = '';
    data = [];

    angular.forEach($scope.importLayers, function (layer, index) {
      $scope.map.removeLayer(layer);
    });

  };

  /* TEST */
  $scope.setMarkerAttribute = function(index, markerAttribute) {

    if($scope.attributesByLayer[index].type != markerAttribute.match(/\((.*)\)/)[1]) {
      $scope.attributesByLayer[index].option = '';
    }

  };

  $scope.setImportLayer = function() {

    /* TEST */
    $scope.attributesByLayer = [];

    $scope.markerAttributes = $scope.importMarkers[0].markerAttribute;

    layerGroupService.listAttributesByLayer($scope.shapeFile.form.layer.layerId, {
      callback: function (result) {

        angular.forEach(result, function(attribute){

          if(attribute.type != 'PHOTO_ALBUM') {
            attribute.option = attribute.name + ' (' + attribute.type + ')';
            $scope.attributesByLayer.push(attribute);
          }

        });

        $scope.$apply();
      },
      errorHandler: function (message, exception) {
        $scope.message = {type: "error", text: message};
        $scope.$apply();
      }
    });

    angular.forEach($scope.importLayers, function (layer, index) {

      if(!$scope.shapeFile.form.layer.layerIcon)
        $scope.shapeFile.form.layer.layerIcon = $scope.shapeFile.form.layer.icon;

      var iconStyle = new ol.style.Style({
        image: new ol.style.Icon(({
          anchor: [0.5, 1],
          anchorXUnits: 'fraction',
          anchorYUnits: 'fraction',
          src: $scope.shapeFile.form.layer.layerIcon
        }))
      });

      layer.setStyle(iconStyle);

      //$scope.associateAttribute();

    });
  };

  $scope.testFiles = [];

  var data = [];

  $scope.$watch('testFiles', function(newVal, oldVal){

    if(newVal.length == 3) {
      shapeFileService.importShapeFile(data, {
        callback: function (result) {

          //console.log(result);

          $scope.isLoading = false;

          $scope.clearImportMarkers();

          $scope.importMarkers = result;

          var coordinates = [];
          var extent = '';

          angular.forEach($scope.importMarkers, function (marker, index) {

            var iconStyle = new ol.style.Style({
              image: new ol.style.Icon({
                anchor: [0.5, 1],
                anchorXUnits: 'fraction',
                anchorYUnits: 'fraction',
                src: 'static/icons/default_blue.png'
              }),
              zIndex: 2
            });

            var geometry = new ol.format.WKT().readGeometry(marker.location.coordinateString);

            var iconFeature = new ol.Feature({
              geometry: new ol.format.WKT().readGeometry(marker.location.coordinateString),
              marker: marker
            });

            var layer = new ol.layer.Vector({
              source: new ol.source.Vector({features: [iconFeature]})
            });

            layer.setStyle(iconStyle);

            coordinates.push(geometry.getCoordinates());

            $scope.importLayers.push(layer);

            $scope.map.addLayer(layer);

            extent = new ol.extent.boundingExtent(coordinates);

          });

          var zoom = $scope.map.getView().getZoom();

          $scope.map.getView().fitExtent(extent, $scope.map.getSize());

          $scope.map.getView().setZoom(zoom);

          $scope.$apply();
        },
        errorHandler: function (message, exception) {

          $scope.clearImportMarkers();

          alert(message);
          $scope.$apply();
        }
      });
    }

  }, true);

  $scope.onFileChange = function(input){

    $scope.setAction('import');

    if (!(/\.(shp|dbf|shx)$/i).test(input.value)){

      console.log('File type error');
      return false;
    }

    if (input.files) {

      $scope.isLoading = true;
      $scope.$apply();

      var files = input.files;

      for (var i = 0, file; file = files[i]; i++) {

        var reader = new FileReader();

        reader.onloadend = (function (readFile) {
          return function (e) {

            var base64 = e.target.result.split('base64,');
            //var base64 = e.target.result;
            var type = readFile.name.substr(readFile.name.length - 3);

            $scope.testFiles.push(readFile.name);

            data.push({type: type.toUpperCase(), source: base64[1], contentLength: readFile.size, name: readFile.name});

          }
        })(file);

        reader.readAsDataURL(file);
      }
    }
  };

  /* Upload Photos */

  $scope.showUpload = function(attribute, attributes){

    var dialog = $modal.open({
      templateUrl: "modules/map/ui/popup/upload-popup.jsp",
      controller: UploadPopUpController,
      size: 'lg',
      resolve: {
        layer: function(){
          return $scope.currentEntity.layer;
        },
        attribute: function(){
          return attribute;
        },
        attributes: function(){
          return attributes
        }
      }
    });


    dialog.result.then(function (result) {

      if(attribute.attribute) {
        angular.forEach(result, function (attribute) {
          if (attribute.attribute.type == 'PHOTO_ALBUM')
            attribute.photoAlbum.photos = attribute.attribute.files;
        });
      }

      $scope.attributesByMarker = result;
      console.log(result);

    });

  };

  //$scope.showGallery();

  /**
   * Resolve date picker
   */
  $scope.resolveDatePicker = function () {
    $timeout(function () {
      $('.datepicker').datepicker({
        dateFormat: 'dd/mm/yy',
        dayNames: ['Domingo', 'Segunda', 'Terça', 'Quarta', 'Quinta', 'Sexta', 'Sábado'],
        dayNamesMin: ['D', 'S', 'T', 'Q', 'Q', 'S', 'S', 'D'],
        dayNamesShort: ['Dom', 'Seg', 'Ter', 'Qua', 'Qui', 'Sex', 'Sáb', 'Dom'],
        monthNames: ['Janeiro', 'Fevereiro', 'Março', 'Abril', 'Maio', 'Junho', 'Julho', 'Agosto', 'Setembro', 'Outubro', 'Novembro', 'Dezembro'],
        monthNamesShort: ['Jan', 'Fev', 'Mar', 'Abr', 'Mai', 'Jun', 'Jul', 'Ago', 'Set', 'Out', 'Nov', 'Dez'],
        nextText: 'Próximo',
        prevText: 'Anterior'
      });

      $('.datepicker').mask("99/99/9999");
    }, 300);
  };



};

function isBooleanChecked(that) {
  $(that).parent().css("border", "0");
  $(that).parent().parent().find("span.tooltip-validation").remove();
};