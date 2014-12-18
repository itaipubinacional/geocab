'use strict';

/**
 * 
 * @param $scope
 * @param $log
 * @param $location
 */
function MapController( $scope, $injector, $log, $state, $timeout, $modal, $location, $http , $importService, $translate) {
	
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

    /*
    *
    * */
    $scope.screenMarkerOpenned = false;
    
    $scope.marker;
    
    /**
     * User credentials
     * */
    $scope.userMe;

    /**
     * Variable that stores the type of the map selected by the user - GMAP ou OSM
     * Will also contain the selected module - Warren, Satellite, Hybrid, etc.
     */
    $scope.mapConf = {
        type : $scope.MAP_TYPE_OSM,
        ativo : null,
        modo : null
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
        fcDistancia : false,
        fcArea : false,
        fcKml :  false,
        fcMarker: false
    };

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
    $scope.allLayersKML = []
    
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
     * @type {Array}
     */
    $scope.features = [];

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

    //Permission area calculation
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
    })

    // view events
    $scope.view.on('change:center', function() {

        // Changes the view of the google map if the map is active the gmap
        if ( $scope.mapConf.active == $scope.MAP_TYPE_GMAP ){
            var center = ol.proj.transform($scope.view.getCenter(), 'EPSG:3857', 'EPSG:4326');
            $scope.mapGoogle.setCenter(new google.maps.LatLng(center[1], center[0]));
        }

    });
    
    $scope.view.on('change:resolution', function() {

        //Changes the view of the google map if the map is active the gmap
        if ( $scope.mapConf.active == $scope.MAP_TYPE_GMAP ){
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

    /*-------------------------------------------------------------------
	 * 		 				 	  NAVIGATIONS
	 *-------------------------------------------------------------------*/
	/**
     * Method executed upon entering the controller, apór load the DOM of the page
     *
	 */
	$scope.initialize = function( toState, toParams, fromState, fromParams ) {
		
		
		/**
		 * Menu setting
		 */
			$("#sidebar-marker-create, #sidebar-marker-detail-update, #sidebar-layers").css("max-width", parseInt($(window).width()) - 68 );
			$("#sidebar-marker-create, #sidebar-marker-detail-update, #sidebar-layers").resize(function() { 
				if(!$scope.firstTime)
					$(".menu-sidebar-container").css("right",parseInt($(this).css("width")) + 5); 
				
				$scope.firstTime = false;
			});
		
		
		/**
		 * If doesn't have a nav bar
		 * */
		if( !$("#navbar-administrator").length && !$("#navbar-user").length ) {
			$(".sidebar-style").css("top","60px");
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

            controls: [
                $scope.mousePositionControl
            ],

            // rotation
            interactions: ol.interaction.defaults({
                dragPan: false
            }).extend([
//                    $scope.dragRotateAndZoom,
                    dragAndDropInteraction,
                    new ol.interaction.DragPan({kinetic: null})
                ]),

            target: $scope.olMapDiv,
            view: $scope.view
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
        if ( $scope.mapConf.type == $scope.MAP_TYPE_OSM ){
            $scope.initializeOSM();
        } else if ( $scope.mapConf.type == $scope.MAP_TYPE_GMAP ){
            $scope.initializeGMAP();
            $scope.mapGoogle.setMapTypeId("hybrid");
        }


        $(function () {
            $("#sidebar-tabs").tabs({
            }).addClass("ui-tabs-vertical ui-helper-clearfix");
            $("#sidebar-tabs li").removeClass("ui-corner-top ui-widget-content").addClass("ui-corner-left");
        });


        /**
         * Click event to prompt the geoserver the information layer of the clicked coordinat
         */
        $scope.map.on('click', function(evt) {
        	
        	
        	
        	 if( $scope.menu.fcMarker && !$scope.screenMarkerOpenned ) {
             	$scope.screenMarkerOpenned = true;
                 $scope.toggleSidebarMarkerCreate(300);

                 var iconStyle = new ol.style.Style({
                     image: new ol.style.Icon(({
                         anchor: [0.5, 1],
                         anchorXUnits: 'fraction',
                         anchorYUnits: 'fraction',
                         src: 'static/images/marker.png'
                     }))
                 });

                 var iconFeature = new ol.Feature({
                     geometry: new ol.geom.Point([  evt.coordinate[0] , evt.coordinate[1]])
                     
                 });	

                var layer = new ol.layer.Vector({
                     source: new ol.source.Vector({ features: [iconFeature] })
                 });

                 layer.setStyle(iconStyle);
                 
                 $scope.currentCreatingInternalLayer = layer;
                 $scope.map.addLayer(layer);
                 $scope.$apply();
                /*
                 $scope.marker = new ol.Overlay({
                     position: evt.coordinate,
                     positioning: 'center-center',
                     element: document.getElementById('marker-point'),
                     stopEvent: false
                 });
                 $scope.map.addOverlay($scope.marker);*/
                 
                 $scope.currentEntity.latitude = evt.coordinate[0];
                 $scope.currentEntity.longitude = evt.coordinate[1];
                 
                 layerGroupService.listAllInternalLayerGroups({
             		callback : function(result) {
                         //$scope.layersGroups = result;
                         $scope.selectLayerGroup = [];
                         
                         angular.forEach(result, function(layer,index){
                         	
                         	$scope.selectLayerGroup.push({
                         		"layerTitle": layer.title,
                         		"layerId": layer.id,
                         		"layerIcon": layer.icon,
                         		"group": layer.layerGroup.name
                         	});
                         	
                         })
                         
                         $scope.currentState = $scope.LIST_STATE;
                         $state.go( $scope.LIST_STATE );
                         $scope.$apply();
                     },
                     errorHandler : function(message, exception) {
                         $scope.message = {type:"error", text: message};
                         $scope.$apply();
                     }
             	});
              return false;   
             }
        	
        	 
        	
            /* get the feature click to open marker detail */
         	var feature = $scope.map.forEachFeatureAtPixel(evt.pixel, function(feature, layer) {
 		        return feature;
 		      });
         	
         	if(($scope.layers.length > 0 && !$scope.menu.fcArea && !$scope.menu.fcDistancia) || feature) {
                $scope.features = [];
         	}
         	
            if ($scope.layers.length > 0 && !$scope.menu.fcArea && !$scope.menu.fcDistancia){

                var listUrls = [];

                for(var i =0; i < $scope.layers.length; i++)
                {
                    var url = $scope.layers[i].wmsSource.getGetFeatureInfoUrl(
                        evt.coordinate, $scope.view.getResolution(), $scope.view.getProjection(),
                        {'INFO_FORMAT': 'application/json'});

                    listUrls.push(decodeURIComponent(url));
                }

                if( $scope.screenMarkerOpenned ) {
					$scope.clearFcMarker();
				}
                
                listAllFeatures(listUrls);
                
                $scope.screen = 'detail';
               
            }

        	/* if click on the marker */
        	if( feature ){
        		if( typeof feature.getProperties().marker != "undefined" ) {
        			if( $scope.screenMarkerOpenned ) {
						$scope.clearFcMarker();
					}
        			
        			$scope.currentCreatingInternalLayer = feature;
	        		$scope.screen = 'detail';
				        		
	        		$scope.marker = feature.getProperties().marker;
	        		$scope.features.push({"feature": $scope.marker, "type": "internal"});
        		}
        	}
        	
        	if( $scope.features.length > 0 ) {
                $timeout(function(){
        			$scope.toggleSidebarMarkerDetailUpdate(300);	
    	    	}, 400)
	    	} 
        	
    		
        	if($scope.features.length == 1) {
        		$timeout(function(){
        			
            		$(".min-height-accordion .panel-collapse .panel-body").removeAttr("style")
    	    	}, 100)
    	    	
        	}
           
        	
        	$("div.msgMap").css("display","none");

        });
        /**
		 * authenticated user
		 * */
        markerService.getUserMe({
    		callback : function(result) {
    			$scope.userMe = result;
            },
            errorHandler : function(message, exception) {
                $scope.message = {type:"error", text: message};
                $scope.$apply();
            }
    	});

    };
    
    $scope.changeToScreen = function(screen) {
    	$scope.screen = screen;
    }

    /**
     * Function that makes request to geo server to bring the features
     * @param url
     * @param posicao
     */
    var listAllFeatures = function (listUrls){

    	layerGroupService.listAllFeatures(listUrls, {
            callback: function (result) {
            	
                for (var i=0; i < result.length; i++){

                    var feature = {
                        layer : $scope.layers[i],
                        fields : {}
                    };

                    angular.forEach(JSON.parse(result[i]).features, function(value, key) {
                        angular.forEach(value.properties, function(value, key) {

                            try {
                                feature.fields[decodeURIComponent( escape(key))] = decodeURIComponent( escape(value));
                            }
                            catch(e) {
                                feature.fields[key] = value;
                            }

                        });

                        var insere = false;
                        for (var propriedade in feature.fields) {
                            insere = true;
                            break;
                        }

                        if (insere){
                            $scope.features.push({"feature": feature, "type":"external"});
                        }
                        
                        if( $scope.features.length > 0 ) {
                            
        	                $timeout(function(){
        	        			$scope.toggleSidebarMarkerDetailUpdate(300);
        	        			
        	        			//.panel-collapse 
        	        			$('.min-height-accordion').find('.panel-body').css('height', 
        	        															parseInt($('#sidebar-marker-detail-update').height()) - 
        	        															parseInt( ( ( $scope.features.length) * 37 ) + 40 ) + 'px'
        	        														  );
        	    	    	}, 400)
        	    	    	
                        }
                        
                        if($scope.features.length > 1) {
                        	$timeout(function(){
                            	$(".min-height-accordion .panel-collapse .panel-body").css("min-height","300px")
                        	}, 700)
                    	}

                    });
                    
                }
                
                if ( $scope.features.length <= 0 && $('.menu-sidebar-container').css('right') != '3px' ) {
        			
        			if($scope.menu.fcMarker){
        	    		$scope.clearFcMarker();
        	    	} else {
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
    $scope.listToolsByUser = function(){
    	
    	layerGroupService.listToolsByUser({
            callback: function (result) {

                for (var i = 0; i < result.length; i++)
                {
                    if(result[i].id == $scope.PERMISSION_CALCULO_DISTANCIA)
                    {
                        $scope.hasPermissionCalculoDistancia = true;
                    }
                    else if(result[i].id == $scope.PERMISSION_CALCULO_AREA)
                    {
                        $scope.hasPermissionCalculoArea = true;
                    }
                    else if(result[i].id == $scope.PERMISSION_KML)
                    {
                        $scope.hasPermissionKML = true;
                        enableFileKML();
                    }

                }

                if ($scope.hasPermissionKML == false){
                    $("#menu-item-3").remove();
                    $("#tabs-3").remove();
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
    $scope.listPublishedLayersGroup = function(){

        //Lists the groups of layers and layers published according to user access profile
        layerGroupService.listLayerGroupUpperPublished( {
            callback: function (result) {

                var parseNode = function( node ) {
                    var item = {};

                    item.label =  !!node.nodes ? node.name : node.title;
                    item.name =  !!node.nodes ? '' : node.name;
                    item.legenda =  !!node.nodes ? '' : node.legend;
                    item.selected =  !!node.nodes ? '' : node.startEnabled;
                    item.dataSourceUrl =  !!node.nodes ? '' : node.dataSource.url;
                    item.value = node.id;
                    item.type = !!node.nodes ? 'grupo' : 'camada';

                    item.maximumScaleMap = !!node.nodes ? '' : node.maximumScaleMap;
                    item.minimumScaleMap= !!node.nodes ? '' : node.minimumScaleMap;
                    
                    if( item.selected ){
                        $scope.getSelectedNode(item);
                    }

                    item.children = [];

                    if(  !!node.nodes ) {
                        for(var i =0; i < node.nodes.length; ++i) {
                            if( true === node.nodes[i].startVisible ) {
                                item.children.push(parseNode(node.nodes[i]));
                            }
                        }
                    }
                    return item;
                }

                $scope.allLayers = [];

                for(var i =0; i < result.length ; ++i){
                   $scope.allLayers.push( parseNode( result[i] ) )
                }

                if( $scope.allLayers[0] )
                {
                    for (var i in $scope.allLayers[0].children)
                    {
                        if ($scope.allLayers[0].children.length == 1 && $scope.allLayers[0].children[i].selected )
                        {
                            $scope.allLayers[0].selected = true;
                            $scope.allLayers[0].children[i].selected = true;
                        }
                        else if ( !$scope.allLayers[0].children[i].selected )
                        {
                            $scope.allLayers[0].selected = false;
                        }
                        else
                        {
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
    $scope.formatUrl = function(node, isPesquisa) {

        if( isPesquisa )
        {
            var index = node.name.indexOf(":");
            var dataSourceAddress = node.dataSource.url.lastIndexOf("geoserver/");
            var layerType = node.name.substring(0,index);
            var layerName = node.name.substring(index+1,node.name.length);
            var formattedUrl = node.dataSource.url.substring(0, dataSourceAddress+10)+layerType+'/wms';
        }
        else
        {
            var index = node.name.indexOf(":");
            var dataSourceAddress = node.dataSourceUrl.lastIndexOf("geoserver/");
            var layerType = node.name.substring(0,index);
            var layerName = node.name.substring(index+1,node.name.length);
            var formattedUrl = node.dataSourceUrl.substring(0, dataSourceAddress+10)+layerType+'/wms';
        }

        return {'name': layerName, 'url': formattedUrl};

    }

  

    /**
     * Treat the selection and deselection of each of the tree
     * @param node
     */
    $scope.getSelectedNode = function(node){
    	
    	if(typeof node == 'undefined' || node.pesquisa) return false;
    	
    	/* Check if it is an internal layer */
    	if(typeof node.dataSourceUrl != 'undefined' && node.dataSourceUrl == null) {
    		if( node.selected ) {
    			$scope.addInternalLayer(node.value);
    		} else {
    			$scope.removeInternalLayer(node.value);
    		}
    		return;
    	}
    	
        if( node && node.type == 'camada' && !node.pesquisa){
            if( node.selected ){

                var item = $scope.formatUrl(node,false);

                var wmsSource = new ol.source.TileWMS({
                    url: item.url,
                    params:{
                        'LAYERS': item.name
                    }
                });

                var wmsLayer = new ol.layer.Tile({
                	 source: wmsSource,
                     maxResolution: minEscalaToMaxResolutionn(node.minimumScaleMap),
                     minResolution: maxEscalaToMinResolutionn(node.maximumScaleMap)
                });

                var isAdded = false;

                for(var i=0; i < $scope.layers.length; i++)
                {
                    if($scope.layers[i].name == node.name)
                    {
                        isAdded = true;
                    }
                }

                if( !isAdded )
                {
                    //Add in the list each selected layer
                    $scope.layers.push({'wmsLayer': wmsLayer, 'wmsSource': wmsSource, "name":node.name, "titulo":node.label});

                    //Adds the selected layers in the map
                    $scope.map.addLayer(wmsLayer);
                }
            }
            else
            {
            	for(var i=0; i < $scope.layers.length; i++)
                {
                    if( $scope.layers[i].name == node.name )
                    {
                        //Removes the user-desselecionadas layers
                        $scope.map.removeLayer($scope.layers[i].wmsLayer);
                        $scope.layers.splice(i,1);
                    }
                }
            }
        }


    }
    
    $scope.getSelectedSearchNode = function(node){

    	if(typeof node == 'undefined') return false;
    	
        if( node && node.type == 'camada' && $scope.allSearchs[0]){

            if( node.selected ){

                for (var i = 0; i < $scope.allSearchs[0].children.length; i++)
                {
                    if($scope.allSearchs[0].children[i].name == node.name)
                    {
                    	//Is internal layer
                    	if( $scope.allSearchs[0].children[i].pesquisa.layer.dataSource.url == null ) {
                    		
                    		//$scope.addInternalLayerSearch($scope.allSearchs[0].children[i].pesquisa.searchId);
                    		$scope.addInternalLayerSearch(1);
                    	} else {
                    		$scope.map.removeLayer(node.wmsLayer);
                            $scope.map.addLayer($scope.allSearchs[0].children[i].wmsLayer);	
                    	}
                    }
                }
            }
            else
            {

                for (var i = 0; i < $scope.allSearchs[0].children.length; i++)
                {
                    if( $scope.allSearchs[0].children[i].name == node.name )
                    {
                    	//Remove layers selected by user 
                    	
                    	//Is internal layer
                    	if( $scope.allSearchs[0].children[i].pesquisa.layer.dataSource.url == null ) {
                    		
                    			//$scope.removeInternalLayerSearch($scope.allSearchs[0].children[i].pesquisa.searchId);
                    			$scope.removeInternalLayerSearch(1, $scope.allSearchs[0].children[i].pesquisa.layer.id);
                    	} else {
                    	
	                        //Is external layer
	                        $scope.map.removeLayer($scope.allSearchs[0].children[i].wmsLayer);
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
    $scope.getSelectedKMLNode = function(node){


        if( node && node.type == 'kml' && $scope.allLayersKML[0]){

            if( node.selected ){

            	for(var i=0; i < $scope.allLayersKML[0].children.length; i++)
                {
                    if($scope.allLayersKML[0].children[i].name == node.name)
                    {
                        $scope.map.removeLayer(node.layer);
                        $scope.map.addLayer($scope.allLayersKML[0].children[i].layer);
                    }
                }
            }
            else
            {
                for(var i=0; i < $scope.allLayersKML[0].children.length; i++)
                {
                    if( $scope.allLayersKML[0].children[i].name == node.name )
                    {
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
    var minEscalaToMaxResolutionn = function ( minEscalaMapa ){
 
        switch (minEscalaMapa){
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
    var maxEscalaToMinResolutionn = function ( maxEscalaMapa ){
 
        switch (maxEscalaMapa){
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
     * Method that initializes the Google Maps map and its settings
     */
    $scope.initializeGMAP = function initializeGMAP() {

        // only runs if the currently active forest is not google maps
        if ( $scope.mapConf.active != $scope.MAP_TYPE_GMAP ){


            // case map active MAP QUEST OSM
            if ( $scope.mapConf.active == $scope.MAP_TYPE_MAPQUEST_OSM ){
                $scope.rasterMapQuestOSM.setVisible(false);
            }

            // If map QUEST MAP SAT active
            if ( $scope.mapConf.active == $scope.MAP_TYPE_MAPQUEST_SAT ){
                $scope.rasterMapQuestSAT.setVisible(false);
            }

            // case map active MAP QUEST OSM
            if ( $scope.mapConf.active == $scope.MAP_TYPE_OSM ){
                $scope.rasterOSM.setVisible(false);
            }

            //adjust div css gmap-to show it
            $("#gmap").css({"width":"100%","height":"100%"})

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

    }


    /**
     * Method that initializes the Open Street Map map and its settings
     */
    $scope.initializeOSM = function initializeOSM() {

        // only runs if the map currently active is not the OSM
        if ( $scope.mapConf.active != $scope.MAP_TYPE_OSM ){

           if ( $scope.mapConf.active == $scope.MAP_TYPE_GMAP ){

               //adjust div css gmap-not to show it
               $("#gmap").css({"width":"0","height":"0"})

               // Removes the element from the olmap div of google maps
               var element = document.getElementById("main-content");
               $scope.olMapDiv.style.position = "relative";
               $scope.olMapDiv.style.top = "0";
               element.appendChild($scope.olMapDiv);

               // Adds interaction to rotate
               $scope.map.addInteraction($scope.dragRotateAndZoom);
           }

            // case map active MAP QUEST OSM
            if ( $scope.mapConf.active == $scope.MAP_TYPE_MAPQUEST_OSM ){
                $scope.rasterMapQuestOSM.setVisible(false);
            }

            // If map QUEST MAP SAT active
            if ( $scope.mapConf.active == $scope.MAP_TYPE_MAPQUEST_SAT ){
                $scope.rasterMapQuestSAT.setVisible(false);
            }


            //$scope.map.addLayer($scope.raster);
            $scope.rasterOSM.setVisible(true);

            $scope.mapConf.active = $scope.MAP_TYPE_OSM;

        }

    }


    /**
     *
     */
    $scope.initializeMapQuestOSM = function() {

        // only runs if the map active in memento is not the OSM
        if ( $scope.mapConf.active != $scope.MAP_TYPE_MAPQUEST_OSM ){

            // If active map for google maps
            if ( $scope.mapConf.active == $scope.MAP_TYPE_GMAP ){

                //adjust div css gmap-not to show it
                $("#gmap").css({"width":"0","height":"0"})

                // Removes the element from the olmap div of google maps
                var element = document.getElementById("main-content");
                $scope.olMapDiv.style.position = "relative";
                $scope.olMapDiv.style.top = "0";
                element.appendChild($scope.olMapDiv);

                // Adds interaction to rotate
                $scope.map.addInteraction($scope.dragRotateAndZoom);
            }

            // If active map for OSM
            if ( $scope.mapConf.active == $scope.MAP_TYPE_OSM ){
                $scope.rasterOSM.setVisible(false);
            }

            // If active map for MAPQUEST SAT
            if ( $scope.mapConf.active == $scope.MAP_TYPE_MAPQUEST_SAT ){
                $scope.rasterMapQuestSAT.setVisible(false);
            }

            // sets the map map quest osm
            $scope.rasterMapQuestOSM.setVisible(true);

            $scope.mapConf.active = $scope.MAP_TYPE_MAPQUEST_OSM;

        }
    }


    /**
     *
     */
    $scope.initializeMapQuestSAT = function() {

        // only runs if the map active in memento is not the OSM
        if ( $scope.mapConf.active != $scope.MAP_TYPE_MAPQUEST_SAT ){

            // If active map is google maps
            if ( $scope.mapConf.active == $scope.MAP_TYPE_GMAP ){

                //adjust div css gmap-not to show it
                $("#gmap").css({"width":"0","height":"0"})

                // Removes the element from the olmap div of google maps
                var element = document.getElementById("main-content");
                $scope.olMapDiv.style.position = "relative";
                $scope.olMapDiv.style.top = "0";
                element.appendChild($scope.olMapDiv);

                // Adds interaction to rotate
                $scope.map.addInteraction($scope.dragRotateAndZoom);
            }

            // If active map is OSM
            if ( $scope.mapConf.active == $scope.MAP_TYPE_OSM ){
                $scope.rasterOSM.setVisible(false);
            }

            // case map active is MAP QUEST OSM
            if ( $scope.mapConf.active == $scope.MAP_TYPE_MAPQUEST_OSM ){
                $scope.rasterMapQuestOSM.setVisible(false);
            }


            // set the map to map quest osm
            $scope.rasterMapQuestSAT.setVisible(true);

            $scope.mapConf.active = $scope.MAP_TYPE_MAPQUEST_SAT;

        }
    }



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
        var displayCoordinateMouse = function(pixel) {

            info.html("<p>" + formatCoordinate($scope.mousePositionControl.l) + "</p>");
            info.css("display","block");

        };
        
        /**
         * Method that formats the coordinate of the mouse
         */
        var formatCoordinate = function( coord ){

            var posVirgula = coord.indexOf(",");

            var part1 = coord.slice(0,posVirgula);
            var part2 = coord.slice(posVirgula+2);

            var posPonto = part1.indexOf(".");
            var latitude = part1.slice(0,posPonto) + "°" + part1.slice(posPonto+1, posPonto+3) + "'" + part1.slice(posPonto+3) + '"';

            posPonto = part2.indexOf(".");
            var longitude = part2.slice(0,posPonto) + "°" + part2.slice(posPonto+1, posPonto+3) + "'" + part2.slice(posPonto+3) + '"';

            return latitude + ", " + longitude;

        }
        
        /**
         * Events to display coordinate of the mouse
         */
        $($scope.map.getViewport()).on('mousemove', function(evt) {
        	displayCoordinateMouse($scope.map.getEventPixel(evt.originalEvent));
        });
    }


    /**
     * Function that hides the mouse position
     */
    $scope.hideMousePosition = function (){
        var info = $('#info');
        info.tooltip('hide');
    }


    /*-------------------------------------------------------------------
     * 		 	    FUNCTIONALITY TO CALCULATE DISTANCE AND AREA
     *-------------------------------------------------------------------*/


    /**
     * Method that calculates the distance of points on interactive map
     */
    $scope.initializeDistanceCalc = function () {

    	if($scope.menu.fcMarker){
    		$scope.clearFcMarker();
    	}
    	
        // checks whether any functionality is already active
        if ($scope.menu.fcDistancia || $scope.menu.fcArea){

            // If this functionality is active is necessary to leave the funcionality
            $scope.map.removeInteraction(draw);
            source.clear();
            $scope.map.removeLayer(vector);
            $('#popup').css("display","none");
            sketch = null;


        }

        //If this functionality is active: deactivates and leavecaso mapa ativo for o google maps
        if ($scope.menu.fcDistancia){

            $scope.menu.fcDistancia = false;
            return;

        } else {

        	// active functionality and disables the other to only have one active at a time
            $scope.menu = {
                fcDistancia : true,
                fcArea : false,
                fcKml :  false,
                fcMarker: false
            };

            // add the measuring layer on a map
            $scope.map.addLayer(vector);

            // adds the event on the map
            $($scope.map.getViewport()).on('mousemove', mouseMoveHandler);

            // initializes the interaction
            addInteraction( 'LineString' );
        }
    }


    $scope.initializeMarker = function () {
    	
    	if($("#sidebar-marker-detail-update").css("display") == 'block') {
    		$scope.clearDetailMarker();
    	}
    	
    	 $scope.map.removeInteraction(draw);
         source.clear();
         $scope.map.removeLayer(vector);
         $('#popup').css("display","none");
         sketch = null;
         
         
    	if ($scope.menu.fcMarker){

    		$scope.clearFcMarker();
            return;

        } else {
        	

        	//$("body").prepend('<span id="marker-point" class="marker-point glyphicon glyphicon-map-marker" style="display: none;"></span>');
        	$scope.currentEntity = new Marker();
        	
            // active functionality and disables the other to only have one active at a time
            $scope.menu = {
                fcDistancia : false,
                fcArea : false,
                fcKml :  false,
                fcMarker: true
            };
         
        }
    }
    
    /**
     * Method that calculates the area of points on interactive map
     */
    $scope.initializeAreaCalc = function () {

    	if($scope.menu.fcMarker){
    		$scope.clearFcMarker();
    	}

        // checks whether any functionality is already active
        if ($scope.menu.fcArea || $scope.menu.fcDistancia || $scope.menu.fcMarker){

            // If this functionality is necessary to leave the active functionality
            $scope.map.removeInteraction(draw);
            source.clear();
            $scope.map.removeLayer(vector);
            $('#popup').css("display","none");
            sketch = null;

        }

        // If this functionality turns on: deactivates and goes out
        if ($scope.menu.fcArea){

            $scope.menu.fcArea = false;
            return;

        } else {

            // activates and deactivates the other functionality to just have an active at a time
            $scope.menu = {
                fcDistancia : false,
                fcArea : true,
                fcKml :  false,
                fcMarker: false
            };

            // Add the layer of measurement on the map
            $scope.map.addLayer(vector);

            // Add event on a map
            $($scope.map.getViewport()).on('mousemove', mouseMoveHandler);

            // Initializes the interaction
            addInteraction( 'Polygon' );
        }

    }

  
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
    var mouseMoveHandler = function(evt) {
        if (sketch && ( $scope.menu.fcArea || $scope.menu.fcDistancia ) ) {
            var output;
            var geom = (sketch.getGeometry());
            if (geom instanceof ol.geom.Polygon) {
                output = formatArea(/** @type {ol.geom.Polygon} */ (geom));

            } else if (geom instanceof ol.geom.LineString) {
                output = formatLength( /** @type {ol.geom.LineString} */ (geom));
            }

            $('#popup-content').html("<p>" + output + "</p>");
            $('#popup').css("display","block");
        }
    };



    /**
     * Method that adds a user interaction on a map
     */
    function addInteraction( type ) {
        // tipos : 'Polygon' e 'LineString'
        draw = new ol.interaction.Draw({
            source: source,
            type: /** @type {ol.geom.GeometryType} */ (type)
        });
        $scope.map.addInteraction(draw);

        draw.on('drawstart',
            function(evt) {
                // set sketch
                sketch = evt.feature;

                // clean the ancient markings
                source.clear();

            }, this);

        draw.on('drawend',
            function(evt) {
                // unset sketch
                sketch = null;

            }, this);
    }


    /**
     * Method that generates the output format of distance measurement
     */
    var formatLength = function(line) {
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
    var formatArea = function(polygon) {
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
    $scope.listCustomSearchByUser = function(){

        // resets the fields of the last selected search
        $scope.currentCustomSearch.layer = null;
        $scope.currentCustomSearch.layerFields = {};
        $scope.searchMsg = null;

        // List custom searches based on user access profile
        customSearchService.listCustomSearchsByUser( {
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
     $scope.selectCustomSearch = function(customSearch) {
         $scope.currentCustomSearch = customSearch;
         
         $timeout(function(){
	         $('.datepicker').datepicker({ 
				dateFormat: 'dd/mm/yy',
				dayNames: ['Domingo','Segunda','Terça','Quarta','Quinta','Sexta','Sábado'],
			    dayNamesMin: ['D','S','T','Q','Q','S','S','D'],
			    dayNamesShort: ['Dom','Seg','Ter','Qua','Qui','Sex','Sáb','Dom'],
			    monthNames: ['Janeiro','Fevereiro','Março','Abril','Maio','Junho','Julho','Agosto','Setembro','Outubro','Novembro','Dezembro'],
			    monthNamesShort: ['Jan','Fev','Mar','Abr','Mai','Jun','Jul','Ago','Set','Out','Nov','Dez'],
			    nextText: 'Próximo',
			    prevText: 'Anterior'
			});	
	
			$('.datepicker').mask("99/99/9999");
         });
    };
    
    
    /**
     *  List the layers of custom search fields
     */
    // Variable used to internal layer
    $scope.searchId = 0;
    $scope.listFieldsLayersSearch = function(){

        // deselect the old research and remove the map
        for( var i = 0; i < $scope.searchs.length; i++ )
        {
            $scope.map.removeLayer($scope.searchs[i].wmsLayer);
            $scope.allSearchs[0].children[i].selected = false;
        }

        
        if($scope.currentCustomSearch.layer.dataSource.url != null ){
        	var item = $scope.formatUrl($scope.currentCustomSearch.layer, true);

			var wmsSource = new ol.source.TileWMS({
				url: item.url,
				params:{
					'LAYERS': $scope.currentCustomSearch.layer.name
				}
			});

			var wmsLayer = new ol.layer.Tile({
				source: wmsSource
			});

			 $scope.map.addLayer(wmsLayer);
		} else {
			
			$scope.searchId++;
			
			$scope.removeInternalLayer($scope.currentCustomSearch.layer.id, function(layerId){
				var fields = $scope.currentCustomSearch.layerFields;
				
				for(var field in fields) {
					if($scope.currentCustomSearch.layerFields[field].type == 'BOOLEAN'){
						$scope.currentCustomSearch.layerFields[field].value = $($scope.isChecked()).val() ;
					} else
						$scope.currentCustomSearch.layerFields[field].value = $("#item_" + field).val();
				}
				
				markerService.listMarkerByLayerFilters($scope.currentCustomSearch.layer.id, {
				    callback: function (results) {
				    	
				    	$scope.markersByLayer = results;
				    	
				    	$scope.allFieldEmpty = true;
				    	angular.forEach($scope.currentCustomSearch.layerFields, function(field, index){
				    		if(field.value != "" && field.value != undefined) {
				    			$scope.allFieldEmpty = false;
	    					}
				    	});
				    	
				    	if(!$scope.allFieldEmpty){
				    		
				    		angular.forEach(results, function(result, index){
				    			$scope.canRemoveMarker = null;
				    			
				    			angular.forEach(result.markerAttribute, function(markerAttribute, index){
				    				
				    				angular.forEach($scope.currentCustomSearch.layerFields, function(field, index){	    					
				    					
					    				if(field.attributeId == markerAttribute.attribute.id && $scope.canRemoveMarker != true ){
					    					
					    					if (field.value != "" && field.value != undefined) {
					    						if (markerAttribute.value.indexOf(field.value) != -1 ){
					    							$scope.canRemoveMarker = false;
					    						}else{
					    							$scope.canRemoveMarker = true;
					    							
					    						}
					    							
					    						
					    					}
					    				}
				    				
				    				});
				    			
				    			});
				    			
				    			if($scope.canRemoveMarker) {
				    				delete $scope.markersByLayer[index];
				    			}
				    		});
				    		
				    	}
			    		
				    	var normalizeArray = $scope.markersByLayer;
				    	$scope.markersByLayer = [];
				    	
			    		angular.forEach(normalizeArray, function(value, index){

				    		if(value != undefined){
				    			$scope.markersByLayer.push(value);
				    		} 
			    			
			    		});
				    	
		    			var iconPath = "static/images/marker.png";
						 
						if($scope.markersByLayer.length > 0) {
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
					
						angular.forEach($scope.markersByLayer, function(marker, index){
			                   var iconFeature = new ol.Feature({
			                       geometry: new ol.geom.Point([marker.latitude ,marker.longitude]),
			                       marker: marker,
			                   });	
			                  
			                   var layer = new ol.layer.Vector({
				                   source: new ol.source.Vector({ features: [iconFeature] }),
				                   
				                   maxResolution: minEscalaToMaxResolutionn(marker.layer.minimumScaleMap),
				                   minResolution: maxEscalaToMinResolutionn(marker.layer.maximumScaleMap)
				               });
				
				               layer.setStyle(iconStyle);
				
				               $scope.map.addLayer(layer);
				               
				               $scope.internalLayers.push({"layer": layer, "id": layerId});
				               $scope.internalLayersSearch.push({"layer": layer, "searchId": $scope.searchId});
			     			});
			
						$scope.$apply();
			    	
				    		
		            },
		            errorHandler: function (message, exception) {

		                $scope.msg = {type: "danger", text: message, dismiss: true};
		                $scope.$apply();
		            }
		        });
			})
			//$scope.addInternalLayer($scope.currentCustomSearch.layer.id);
			$scope.searchs.push({'pesquisa': $scope.currentCustomSearch});
			
			var item = {};
	        item.label = 'Resultado pesquisas';
	        item.type = 'grupo';
	        item.searchId = $scope.searchId;
	
	        item.children = [];
	
	        var lastSearchName;
	        for(var i =0; i < $scope.searchs.length ; ++i)
	        {
	
	            $scope.searchs[i].label = "Pesquisa "+ (i+1);
	            $scope.searchs[i].type = 'camada';
	            $scope.searchs[i].name = "pesquisa"+ (i+1);
	
	            item.children.push($scope.searchs[i]);
	            lastSearchName = "Pesquisa "+ (i+1);
	        }
	
	        // seleciona a ultima pesquisa
	        item.children[item.children.length-1].selected = true;
	
	        // seleciona o grupo pai caso so tenha uma pesquisa
	        if (item.children.length == 1) item.selected = true;
	
	        // Abre a tree de pesquisas
	        $timeout(function(){
	            $('#tree-pesquisas').find('.ivh-treeview-node-collapsed').removeClass('ivh-treeview-node-collapsed');
	        });
	
	        $scope.allSearchs = [];
	        $scope.allSearchs.push(item);
	        $scope.searchMsg = lastSearchName + ' - Realizada com sucesso'
	
	        $("#alertPesquisa").show();
	
	        setTimeout(function(){
	            $("#alertPesquisa").fadeOut();
	        }, 2000)
			
			
			return false;
		}

        var filterParams = {'CQL_FILTER' : null};
        var filterList = '';
        var firstTime = true;

        var fields = $scope.currentCustomSearch.layerFields;

        var formatDate = function(date) {
            var date = date.split('/');
            return date.reverse().join('-');
        }

        var conectorLike;
        for (var i = 0; i < fields.length; i++)
        { 
        	//estou aqui
            fields[i].searchValue = '';

            if( $("#item_"+i).val() != '' )
            {
                fields[i].searchValue = $("#item_"+i).val();
 
                if (fields[i].tipo != "INT"){
                    conectorLike = true;
                } else {
                    conectorLike = false;
                }
                if( firstTime )
                {
                    if (conectorLike){
                        filterList = filterList.concat('strToLowerCase(' + '\"' + fields[i].name + '\"' + ') LIKE ' + "'%" + $("#item_"+i).val().toLowerCase() + "%'");
                    } else {
                        filterList = filterList.concat('strToLowerCase(' + '\"' + fields[i].name + '\"' + ') = ' + "'" + $("#item_"+i).val().toLowerCase() + "'");
                    }
 
                    firstTime = false;
                }
                else
                {
                    if (conectorLike){
                        filterList = filterList.concat(' AND strToLowerCase(' + '\"' + fields[i].name + '\"' + ') LIKE ' + "'%" + $("#item_"+i).val().toLowerCase() + "%'");
                    } else {
                        filterList = filterList.concat(' AND strToLowerCase(' + '\"' + fields[i].name + '\"' + ') = ' + "'" + $("#item_"+i).val().toLowerCase() + "'");
                    }
 
                }
            }
        }
            
//            if( firstTime ) {
//                if ($("#item_" + i).val() != '' && fields[i].type == 'INT') {
//
//                    fields[i].searchValue = $("#item_" + i).val();
//                    filterList = filterList.concat('strToLowerCase(' + '\"' + fields[i].name + '\"' + ') = ' + "'%" + $("#item_" + i).val().toLowerCase() + "%'");
//
//                }
//
//                if (fields[i].type == 'NUMBER') {
//
//                    fields[i].searchValue = $("#item_" + i).val();
//
//                    var operatorType = $("#item_" + i + " select").val();
//                    var valueNumber = $("#item_" + i + " input").val();
//
//                    if ( operatorType == 'entre' ) {
//                        filterList = filterList.concat('strToLowerCase(' + '\"' + fields[i].name + '\"' + ') > ' + valueNumber + "'");
//                        filterList = filterList.concat('AND strToLowerCase(' + '\"' + fields[i].name + '\"' + ') <' + valueNumber + "'");
//                    } else if ( operatorType == 'somente' ) {
//                        filterList = filterList.concat('strToLowerCase(' + '\"' + fields[i].name + '\"' + ') IN (' + valueNumber + ")'");
//                    } else {
//                        filterList = filterList.concat('strToLowerCase(' + '\"' + fields[i].name + '\"' + ') ' + operatorType + ' ' + valueNumber + "'");
//                    }
//
//                }
//
//                if ($("#item_" + i).val() != '' && fields[i].type == 'STRING') {
//
//                    fields[i].searchValue = $("#item_" + i).val();
//                    filterList = filterList.concat('strToLowerCase(' + '\"' + fields[i].name + '\"' + ') LIKE ' + "'" + $("#item_" + i).val().toLowerCase() + "'");
//
//                }
//
//                if (fields[i].type == "DATETIME") {
//
//                    var startDate = $("#item_" + i + " input[name=startDate]").val();
//                    var endDate = $("#item_" + i + " input[name=endDate]").val();
//
//                    if(startDate != '')
//                        filterList = filterList.concat('strToLowerCase(' + '\"' + fields[i].name + '\"' + ') >= ' + "'" + formatDate(startDate) + "' ");
//                    if(endDate != '')
//                        filterList = filterList.concat('AND strToLowerCase(' + '\"' + fields[i].name + '\"' + ') <= ' + "'" + formatDate(endDate) + "'");
//                }
//
//                firstTime = false;
//
//            } else {
//
//                if ($("#item_" + i).val() != '' && fields[i].type == 'INT') {
//
//                    fields[i].searchValue = $("#item_" + i).val();
//                    filterList = filterList.concat(' AND strToLowerCase(' + '\"' + fields[i].name + '\"' + ') = ' + "'%" + $("#item_" + i).val().toLowerCase() + "%'");
//
//                }
//
//                if (fields[i].type == 'NUMBER') {
//
//                    fields[i].searchValue = $("#item_" + i).val();
//
//                    var operatorType = $("#item_" + i + " select").val();
//                    var valueNumber = $("#item_" + i + " input").val();
//
//                    if ( operatorType == 'entre' ) {
//                        filterList = filterList.concat(' AND strToLowerCase(' + '\"' + fields[i].name + '\"' + ') > ' + valueNumber + "'");
//                        filterList = filterList.concat(' AND strToLowerCase(' + '\"' + fields[i].name + '\"' + ') <' + valueNumber + "'");
//                    } else if ( operatorType == 'somente' ) {
//                        filterList = filterList.concat(' AND strToLowerCase(' + '\"' + fields[i].name + '\"' + ') IN (' + valueNumber + ")'");
//                    } else {
//                        filterList = filterList.concat(' AND strToLowerCase(' + '\"' + fields[i].name + '\"' + ') ' + operatorType + ' ' + valueNumber + "'");
//                    }
//
//                }
//
//                if ($("#item_" + i).val() != '' && fields[i].type == 'STRING') {
//
//                    fields[i].searchValue = $("#item_" + i).val();
//                    filterList = filterList.concat(' AND strToLowerCase(' + '\"' + fields[i].name + '\"' + ') LIKE ' + "'" + $("#item_" + i).val().toLowerCase() + "'");
//
//                }
//
//                if (fields[i].type == "DATETIME") {
//
//                    var startDate = $("#item_" + i + " input[name=startDate]").val();
//                    var endDate = $("#item_" + i + " input[name=endDate]").val();
//
//                    if(startDate != '')
//                        filterList = filterList.concat(' AND strToLowerCase(' + '\"' + fields[i].name + '\"' + ') >= ' + "'" + formatDate(startDate) + "' ");
//                    if(endDate != '')
//                        filterList = filterList.concat(' AND strToLowerCase(' + '\"' + fields[i].name + '\"' + ') <= ' + "'" + formatDate(endDate) + "'");
//                }
//
//            }
//
//        }

        filterParams.CQL_FILTER = filterList;

        if( filterList != '' )
        {
            wmsSource.updateParams(filterParams);
        }

        $scope.searchs.push({'wmsLayer': wmsLayer, 'wmsSource': wmsSource, 'pesquisa': $scope.currentCustomSearch});

        var item = {};
        item.label = 'Resultado pesquisas';
        item.type = 'grupo'

        item.children = [];

        var lastSearchName;
        for(var i =0; i < $scope.searchs.length ; ++i)
        {

            $scope.searchs[i].label = "Pesquisa "+ (i+1);
            $scope.searchs[i].type = 'camada';
            $scope.searchs[i].name = "pesquisa"+ (i+1);

            item.children.push($scope.searchs[i]);
            lastSearchName = "Pesquisa "+ (i+1);
        }

        // seleciona a ultima pesquisa
        item.children[item.children.length-1].selected = true;

        // seleciona o grupo pai caso so tenha uma pesquisa
        if (item.children.length == 1) item.selected = true;

        // Abre a tree de pesquisas
        $timeout(function(){
            $('#tree-pesquisas').find('.ivh-treeview-node-collapsed').removeClass('ivh-treeview-node-collapsed');
        });

        $scope.allSearchs = [];
        $scope.allSearchs.push(item);
        $scope.searchMsg = lastSearchName + ' - Realizada com sucesso'

        $("#alertPesquisa").show();

        setTimeout(function(){
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
    function enableFileKML()
    {
        //Controls the drag and drop the KML file on a map
        dragAndDropInteraction.on('addfeatures', function(event) {
            var vectorSource = new ol.source.Vector({
                features: event.features,
                projection: event.projection
            });

            var kmlLayer = new ol.layer.Vector({
                source: vectorSource
            });

            $scope.kmlLayers.push({layer : kmlLayer})

            $scope.map.getLayers().push(kmlLayer);

            //Redirects to the point that the KML file is dragged
            var view = $scope.map.getView();
            view.fitExtent(
                vectorSource.getExtent(), ($scope.map.getSize()));

            var item = {};
            item.label = 'Camadas KML';
            item.type = 'kml'

            item.children = [];

            for(var i =0; i < $scope.kmlLayers.length ; ++i)
            {

                $scope.kmlLayers[i].label = "Camada "+ (i+1);
                $scope.kmlLayers[i].type = 'kml';
                $scope.kmlLayers[i].name = "Camada"+ (i+1);

                item.children.push($scope.kmlLayers[i]);
            }

            // selects the last search
            item.children[item.children.length-1].selected = true;

            // Select the parent group
            var selectItemPai = true;
            for( var i in item.children )
            {
                if (item.children[i].selected != true){
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
    $scope.toggleSidebarMenu = function (time, element){
    	if($("#sidebar-marker-detail-update").css("display") == 'block') {
    		$scope.clearDetailMarker();
    		
    		$timeout(function(){
	    		$scope.toggleSidebar(time, element, '#sidebar-layers');
	    	}, 400)
    	}
    	/**
    	 * If the marker tab is open, close it and wait to open the new.
    	 * */
    	if( $scope.menu.fcMarker ) {
    		$scope.clearFcMarker();
    	
	    	$timeout(function(){
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
    $scope.toggleSidebarMarkerCreate = function (time, element){
    	$scope.imgResult = "";
    	$scope.$apply();
    	
    	if(element == "closeButton") {
            $scope.screenMarkerOpenned = false;
        }
    	
    	/**
    	 * If the marker tab is open, close it and wait to open the new.
    	 * */
    	if($scope.slideActived == '#sidebar-layers') {
    		$scope.toggleSidebar(time, 'closeButton', '#sidebar-layers');
    		
    		$timeout(function(){
        		$scope.toggleSidebar(time, element, '#sidebar-marker-create');
        	}, 400)
    	} else {
    		
    		$scope.toggleSidebar(time, element, '#sidebar-marker-create');
    	}
    	
    	$scope.resolveDatepicker();
    	
    };
    
    $scope.toggleSidebarMarkerDetailUpdate = function (time, element){
    	$scope.currentEntity = $scope.marker;
    	
    	if(element == "closeButton") {
            $scope.screenMarkerOpenned = false;
            $scope.toggleSidebar(time, 'closeButton', '#sidebar-marker-detail-update');
            return;
        }
    	
    	if( typeof $scope.marker != "undefined" ) {
	    	markerService.findImgByMarker($scope.marker.id, {
	 			 callback : function(result) {
	 				 
	 				 $scope.imgResult = result;
	 	          },
	 	          errorHandler : function(message, exception) {
	 	              $scope.message = {type:"error", text: message};
	 	              $scope.$apply();
	 	          }
		    	});
	    	
	    	$scope.attributesByLayer = [];
			$scope.showNewAttributes = false;
	    	
	    	markerService.listAttributeByMarker($scope.marker.id, {
			  callback : function(result) {
				  $scope.attributesByMarker = result;   
				  
				  layerGroupService.listAttributesByLayer($scope.marker.layer.id,{
		          		callback : function(result) {
		          			$scope.attributesByLayer = [];
		          			
		          			angular.forEach(result, function(attribute, index){
			          				
		          					var exist = false;
		          					
		          					angular.forEach($scope.attributesByMarker, function(attributeByMarker, index){
		          					
			          					if(attributeByMarker.attribute.id == attribute.id){
			          						exist = true;
			          					}
			          				});
			          				
			          				if( !exist ) {
			          					$scope.attributesByLayer.push(attribute);
			          					$scope.showNewAttributes = true;
			          				}
			          				
			          			});
		          			
		                      $scope.$apply();
		                  },
		                  errorHandler : function(message, exception) {
		                      $scope.message = {type:"error", text: message};
		                      $scope.$apply();
		                  }
		          	});
				  
				  angular.forEach(result,function(markerAttribute,index){
					if (markerAttribute.attribute.type == "NUMBER") {
						markerAttribute.value = parseInt(markerAttribute.value);
					}  
				  })
				  
				 
				  $scope.$apply();
				 
	          },
	          errorHandler : function(message, exception) {
	              $scope.message = {type:"error", text: message};
	              $scope.$apply();
	          }
	    	});
    	}
    	/* List for the edit */
    	layerGroupService.listAllInternalLayerGroups({
    		callback : function(result) {
               // $scope.layersGroups = result;
                
                $scope.selectLayerGroup = [];
                        
                angular.forEach(result, function(layer,index){
                	
                	$scope.selectLayerGroup.push($.extend(layer, {
                		"layerTitle": layer.title,
                		"layerId": layer.id,
                		"group": layer.layerGroup.name
                	}));
                	
                })
                
                markerService.listAttributeByMarker($scope.currentEntity.id, {
	       			 callback : function(result) {
	       				$scope.attributesByMarker = result;
	       				
	       				angular.forEach(result,function(markerAttribute,index){
	       					if (markerAttribute.attribute.type == "NUMBER") {
	       						markerAttribute.value = parseInt(markerAttribute.value);
	       					}  
	       				  })
	  
                        angular.forEach($scope.selectLayerGroup, function(layer,index){
                        		if( layer.layerId == $scope.currentEntity.layer.id ) {
                        			layer.created = $scope.currentEntity.layer.created;
	       							$scope.currentEntity.layer = layer;
	       							
	       							return false;
	           					}
                        })
	       				 
	       				 $scope.$apply();
	       	          },
	       	          errorHandler : function(message, exception) {
	       	              $scope.message = {type:"error", text: message};
	       	              $scope.$apply();
	       	          }
       	    	});
                
            },
            errorHandler : function(message, exception) {
                $scope.message = {type:"error", text: message};
                $scope.$apply();
            }
    	});    	
    	
    	/**
    	 * If the marker tab is open, close it and wait to open the new.
    	 * */
    	
    	if($scope.slideActived == '#sidebar-marker-detail-update') return;
    	
    	if($scope.slideActived == '#sidebar-layers') {
    		$scope.toggleSidebar(time, 'closeButton', '#sidebar-layers');
    		
    		$timeout(function(){
        		$scope.toggleSidebar(time, '', '#sidebar-marker-detail-update');
        	}, 400);
    		
    	} else {
    		$scope.toggleSidebar(time, '', '#sidebar-marker-detail-update');
    	}
    	/*
    	if ( $('#sidebar-marker-detail').css("display") == 'none' ){
    		
    	}*/
    	$scope.resolveDatepicker();
    	
    };
    
    $scope.clearDetailMarker = function() {
    	$scope.toggleSidebar(0, 'closeButton', '#sidebar-marker-detail-update');
    }
    
    $scope.toggleSidebar = function (time, element, slide){
	    time = time != null ? 300 : time;
	    
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
	        
	        $(slide).toggle('slide', { direction: 'right' }, time);
	        $('.menu-sidebar-container').animate({
	            'right' : closed ? $(slide).width() : '3px'
	        }, time);
	    } else {
	        if ($(element).hasClass('bg-inactive')) $(element).removeClass('bg-inactive');
	    }
	    $scope.lastActive = element;
	    
	    if(element == "closeButton"){
	    	$scope.slideActived = '';
	    } else {
	    	$scope.slideActived = slide;
	    }
	    
    }

    /*-------------------------------------------------------------------
     * 		 			SIDE MENU MAP
     *-------------------------------------------------------------------*/

    /**
     * Function that decreases the zoom map
     */
    $scope.diminuirZoom = function (){
        $scope.map.getView().setZoom($scope.map.getView().getZoom() - 1);
    }

    /**
     * Function that increases the zoom map
     */
    $scope.aumentarZoom = function (){
        $scope.map.getView().setZoom($scope.map.getView().getZoom() + 1);
    }

    /**
     *
     */
    $scope.showMapInfo = function(features) {
        var dialog = $modal.open({
            templateUrl: 'modules/map/ui/popup/map-info-popup.jsp',
            controller: MapInfoPopUpController,
            resolve : {
                features: function(){ return features }
            }
        });
    }

    /**
     *
     * @param camada
     */
    $scope.showLegendDetail = function(treeItem) {
        $scope.legendDetailTitle = treeItem.label;
        $scope.legendDetailImage = treeItem.legenda;
        $scope.LAYER_MENU_STATE = 'legend_detail';
    }

    /**
     *
     */
    $scope.exitLegendDetail = function (){
        $scope.LAYER_MENU_STATE = 'list';
    }
    
    $scope.clearFcMarker = function() {
    	$scope.currentEntity = new Marker();
    	
    	$scope.menu.fcMarker = false;
    	
    	if($scope.screenMarkerOpenned)
    		$scope.toggleSidebar('300', 'closeButton', $scope.slideActived);

    	$scope.map.removeLayer($scope.currentCreatingInternalLayer);
    	
        $scope.screenMarkerOpenned = false;
        $scope.attributesByLayer = [];
    }
    
    $scope.updateMarker = function(){
    	
    	if (!$scope.form('sidebarMarkerUpdate').$valid){
    		 $scope.msg = {type: "danger", text: $translate("admin.users.The-highlighted-fields-are-required"), dismiss: true};
    		return;
    	}
    	
    	if( $scope.currentEntity.layer == null ) {
    		var layer = new Layer();
        	layer.id = $scope.currentEntity.layer;
        	$scope.currentEntity.layer = layer;	
    	}
    	
    	var i=0;
    	
    	angular.forEach($scope.attributesByMarker, function(){
    		
    		if ($scope.attributesByMarker[i].value == null){
    			$scope.attributesByMarker[i].value = "";
    		}
    		
    		i++;    		
    	});

    	$scope.currentEntity.markerAttribute = $scope.attributesByMarker;
    	
    	angular.forEach($scope.attributesByLayer, function(val,ind){
    		
    		var attribute = new Attribute();
    		attribute.id = val.id;

    		var markerAttribute = new MarkerAttribute();
    		if (val.value != "" && val.value != undefined){
    			markerAttribute.value = val.value;
    		} else {
    			markerAttribute.value = "";
    		}
    		
    		markerAttribute.attribute = attribute
    		markerAttribute.marker = $scope.currentEntity;
    		$scope.currentEntity.markerAttribute.push(markerAttribute);
    		
    	});
    	
    	markerService.updateMarker($scope.currentEntity,{
      		callback : function(result) {
      		      			
      			$scope.toggleSidebarMarkerDetailUpdate(300, 'closeButton')
      			
      			 $scope.msg = {type: "success", text: $translate("map.Mark-updated-succesfully") , dismiss: true};      			  
     			  $("div.msgMap").show();
     			  
     			  setTimeout(function(){
     				  $("div.msgMap").fadeOut();
     			  }, 5000);
      			
                  $scope.$apply();
              },
              errorHandler : function(message, exception) {
        		  $scope.message = {type:"error", text: message};
                  $scope.$apply();
              }
      	});

    }
    
    $scope.insertMarker = function(){    	
    	if( !$scope.isBooleanValid() ) {
    		return false;
    	}
    	if (!$scope.form('sidebarMarker').$valid){
    		 
    		return;
    	}
    	
    	var layer = new Layer();
    	layer.id = $scope.currentEntity.layer.layerId;
    	$scope.currentEntity.layer = layer;
    	
    	$scope.currentEntity.markerAttribute = [];
    	angular.forEach($scope.attributesByLayer, function(val,ind){
    		
    		var attribute = new Attribute();
    		attribute.id = val.id;

    		var markerAttribute = new MarkerAttribute();
    		if (val.value != "" && val.value != undefined){
    			markerAttribute.value = val.value;
    		} else {
    			markerAttribute.value = "";
    		}
    		
    		markerAttribute.attribute = attribute
    		markerAttribute.marker = $scope.currentEntity;
    		$scope.currentEntity.markerAttribute.push(markerAttribute);
    		
    	});
    	
    	
    	markerService.insertMarker($scope.currentEntity,{
      		callback : function(result) {
      			  
      			  $scope.map.removeLayer($scope.currentCreatingInternalLayer);
      			  
      			  $scope.removeInternalLayer($scope.currentEntity.layer.id, function(layerId){
      				   $scope.addInternalLayer(layerId);
      			  })

      			$scope.clearFcMarker();
    			  
    			  $scope.msg = {type: "success", text: $translate("map.Mark-inserted-succesfully") , dismiss: true};      			  
    			  $("div.msgMap").show();
    			  
    			  setTimeout(function(){
    				  $("div.msgMap").fadeOut();
    			  }, 5000);
      			  
                  $scope.$apply();
              },
              errorHandler : function(message, exception) {
                  $scope.message = {type:"error", text: message};
                  $scope.$apply();
              }
      	});

    }
    
    $scope.showAttributesAlone = false;
    $scope.showNewAttributes = false;
    $scope.listAttributesByLayerUpdate = function(){
    	
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
    	
    	if($scope.attributesByMarker.length > 0) {
	    	if($scope.attributesByMarker[0].marker.layer.id == $scope.currentEntity.layer.layerId) {
	    		$scope.showAttributesAlone = false;
	    		
	    		layerGroupService.listAttributesByLayer($scope.currentEntity.layer.layerId,{
	          		callback : function(result) {
	          			$scope.attributesByLayer = [];
	          			
	          			angular.forEach(result, function(attribute, index){
		          				
	          					var exist = false;
	          					
	          					angular.forEach($scope.attributesByMarker, function(attributeByMarker, index){
	          					
		          					if(attributeByMarker.attribute.id == attribute.id){
		          						exist = true;
		          					}
		          				});
		          				
		          				if( !exist ) {
		          					$scope.attributesByLayer.push(attribute);
		          					$scope.showNewAttributes = true;
		          				}
		          				
		          			});
	          			
	                      $scope.$apply();
	                  },
	                  errorHandler : function(message, exception) {
	                      $scope.message = {type:"error", text: message};
	                      $scope.$apply();
	                  }
	          	});
	    		
	    		return false;
	    	}
    	}
    	 
    	  layerGroupService.listAttributesByLayer($scope.currentEntity.layer.layerId,{
      		callback : function(result) {
                  $scope.attributesByLayer = result;
                  $scope.$apply();
              },
              errorHandler : function(message, exception) {
                  $scope.message = {type:"error", text: message};
                  $scope.$apply();
              }
      	});
    }
    
    $scope.listAttributesByLayer = function(){
    	var iconStyle = new ol.style.Style({
            image: new ol.style.Icon(({
                anchor: [0.5, 1],
                anchorXUnits: 'fraction',
                anchorYUnits: 'fraction',
                src: $scope.currentEntity.layer.layerIcon
            }))
        });
    	$scope.currentCreatingInternalLayer.setStyle(iconStyle);
    	 
    	  layerGroupService.listAttributesByLayer($scope.currentEntity.layer.layerId,{
      		callback : function(result) {
                  $scope.attributesByLayer = result;
                  $scope.$apply();
              },
              errorHandler : function(message, exception) {
                  $scope.message = {type:"error", text: message};
                  $scope.$apply();
              }
      	});
    }
    
    $scope.removeInternalLayer = function(layerId, callback){
    	var callBackHasExecuted = false;
    	var internalLayers =  $.extend([], $scope.internalLayers);
    	angular.forEach(internalLayers, function(value, index){
			  if(value.id == layerId) {
				  $scope.map.removeLayer(value.layer);
				  $scope.internalLayers.splice(index, 1);
				 if(typeof callback != 'undefined' && !callBackHasExecuted) {
					 callback(value.id);
					 callBackHasExecuted = true;
				 }
			  }
		  });
    	
    	if( !callBackHasExecuted ) {
    		if(typeof callback != 'undefined') {
    			callback(layerId);
    		}
    	}
    }
    
    $scope.addInternalLayer = function( layerId ) {
    	
    	markerService.listMarkerByLayer(layerId, {
				 callback : function(result) {
					 
					var iconPath = "static/images/marker.png";
					 
					if(result.length > 0) {
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
					
					angular.forEach(result, function(marker, index){
		                   var iconFeature = new ol.Feature({
		                       geometry: new ol.geom.Point([marker.latitude ,marker.longitude]),
		                       marker: marker,
		                   });	
		                  
		                   
		                   var layer = new ol.layer.Vector({
			                   source: new ol.source.Vector({ features: [iconFeature] }),
			                   
			                   maxResolution: minEscalaToMaxResolutionn(marker.layer.minimumScaleMap),
			                   minResolution: maxEscalaToMinResolutionn(marker.layer.maximumScaleMap)
			               });
			
			               layer.setStyle(iconStyle);
			
			               $scope.map.addLayer(layer);
			               
			               $scope.internalLayers.push({"layer": layer, "id": layerId});
		     			});
		
		     			

	               
	               
	               
	               $scope.$apply();
	 		
		          },
		          errorHandler : function(message, exception) {
		              $scope.message = {type:"error", text: message};
		              $scope.$apply();
		          }
		});
    	
    } 
   
    $scope.addInternalLayerSearch = function( searchId ) {
    	
    	angular.forEach($scope.internalLayersSearch, function(internalLayer, index){
    		$scope.map.addLayer(internalLayer.layer);
    	});
    	$scope.$apply();
    	
    } 
    
    $scope.removeInternalLayerSearch = function(searchId, layerId){
    	var internalLayers =  $.extend([], $scope.internalLayers);
    	var internalLayersSearch =  $.extend([], $scope.internalLayersSearch);
    	
    	angular.forEach(internalLayers, function(value, index){
			  if(value.id == layerId) {
				  $scope.map.removeLayer(value.layer);
				 
			  }
		  });
    	
    	angular.forEach(internalLayersSearch, function(value, index){
			  if(value.searchId == searchId) {
				  $scope.map.removeLayer(value.layer);
				 
			  }
		  });
    	
    }
    
    
    $scope.removeMarker = function(){
    	
		var dialog = $modal.open( {
			templateUrl: "static/libs/eits-directives/dialog/dialog-template.html",
			controller: DialogController,
			windowClass: 'dialog-enable',
			resolve: {
				title: function(){return $translate("map.Delete-mark")},
				message: function(){return $translate("map.Are-you-sure-you-want-to-delete-the-mark") + " ?"},
				buttons: function(){return [ {label:$translate("layer-group-popup.Delete"), css:'btn btn-danger'}, {label: $translate("admin.users.Cancel"), css:'btn btn-default', dismiss:true} ];}
			}
		});
	    	
		dialog.result.then( function(result) {
	
	    	markerService.removeMarker($scope.marker.id, {
	      		  callback : function(result) {
	      			//$scope.map.removeOverlay($scope.markerDetail.overlay);
	      			  
	      			
		  			$scope.removeInternalLayer($scope.marker.layer.id, function(layerId){
	   				   	$scope.addInternalLayer(layerId);
	    			})
	      			
	    			$scope.features = [];
	    			
	    			$scope.toggleSidebarMarkerDetailUpdate(300, 'closeButton');  
	    			  
	      			$scope.msg = {type: "success", text: $translate("map.Mark-was-successfully-deleted"), dismiss: true};
	      			$("div.msgMap").show();
	      			  
	      			setTimeout(function(){
	      			  $("div.msgMap").fadeOut();
	      			}, 5000);
	      			
		          },
		          errorHandler : function(message, exception) {
		              $scope.message = {type:"error", text: message};
		              $scope.$apply();
		          }
	      	});
				
		});
    
    }
    
    $scope.setPhotoMarker = function(element) {
    	if (!(/\.(gif|jpg|jpeg|bmp|png)$/i).test(element.value)){
            $scope.msg = {text: $translate("map.The-selected-file-is-invalid"), type: "danger", dismiss: true};
            return false;
        }
    	
    	
    	$scope.currentEntity.image = element;
    	
    	$scope.readURL(element);
    }
    
    $scope.enableMarker = function() {
    	
    	var dialog = $modal.open( {
    		templateUrl: "static/libs/eits-directives/dialog/dialog-template.html",
    		controller: DialogController,
    		windowClass: 'dialog-enable',
    		resolve: {
    			title: function(){return $translate("map.Enable-mark")},
    			message: function(){return $translate("map.Are-you-sure-you-want-to-enable-the-mark") + " ?"},
    			buttons: function(){return [ {label:$translate("map.Enable"), css:'btn btn-success'}, {label: $translate("admin.users.Cancel"), css:'btn btn-default', dismiss:true} ];}
    		}
    	});
    	
    	
    	dialog.result.then(function(result) {
    		
    		markerService.enableMarker($scope.marker.id, {
  			  callback : function(result) {
  				$scope.marker.status = "ACCEPTED";
  				
  				$scope.msg = {type: "success", text: $translate("map.Mark-was-successfully-enabled"), dismiss: true};
      			$("div.msgMap").show();
      			
      			setTimeout(function(){
      			  $("div.msgMap").fadeOut();
      			}, 5000);
  				
  	          },
  	          errorHandler : function(message, exception) {
  	              $scope.message = {type:"error", text: message};
  	              $scope.$apply();
  	          }
  		});
    		
    	});
    	
    }
    
    $scope.disableMarker = function() {
    	
    	var dialog = $modal.open( {
    		templateUrl: "static/libs/eits-directives/dialog/dialog-template.html",
    		controller: DialogController,
    		windowClass: 'dialog-enable',
    		resolve: {
    			title: function(){return $translate("map.Disable-mark")},
    			message: function(){return $translate("map.Are-you-sure-you-want-to-disable-the-mark") + " ?"},
    			buttons: function(){return [ {label:$translate("map.Disable"), css:'btn btn-danger'}, {label: $translate("admin.users.Cancel"), css:'btn btn-default', dismiss:true} ];}
    		}
    	});
    	
    	dialog.result.then(function(result) {
    		
    		markerService.disableMarker($scope.marker.id, {
			  callback : function(result) {
				$scope.marker.status = "REFUSED";
				
				$scope.msg = {type: "success", text: $translate("map.Mark-was-successfully-disabled"), dismiss: true};
      			$("div.msgMap").show();
      			
      			setTimeout(function(){
      			  $("div.msgMap").fadeOut();
      			}, 5000);
				
	          },
	          errorHandler : function(message, exception) {
	              $scope.message = {type:"error", text: message};
	              $scope.$apply();
	          }
    		});
    		
    	});
    	
    }
    
    $scope.readURL = function(input){

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
    }
    
    $scope.openImgModal = function() {
    	var dialog = $modal.open({
            templateUrl: 'modules/map/ui/popup/img-popup.jsp',
            controller: ImgPopUpController,
            resolve : {
            	img: function(){ return $scope.imgResult }
            }
        });
    }
    
    $scope.isBooleanValid = function(){
    	
    	$scope.ok = true;
    	$.each($(".boolean").parent().parent(), function(index, value){  
    		if( $(this).attr("required") ){  
    			
    			
    			if( !$(this).find(".boolean-1").is(":checked") && !$(this).find(".boolean-2").is(":checked") ) {
    				$scope.ok = false;
    				
    				$(this).find(".required-boolean").css("border", "1px solid red");
    				$(this).find(".required-boolean").css("border-radius", "5px");
    				$(this).prepend('<span class="tooltip-validation"style="top: 3px">'+ $translate("map.Field-required") +'</span>') ; 
    			}
    			
    			  
    			}   
    		});
    	
    	return $scope.ok;
    }
    
    $scope.resolveDatepicker = function(){
    	
		$scope.$watch('attributesByLayer', function(oldValue, newValue){
			$timeout(function(){
				$('.datepicker').datepicker({ 
					dateFormat: 'dd/mm/yy',
					dayNames: ['Domingo','Segunda','Terça','Quarta','Quinta','Sexta','Sábado'],
				    dayNamesMin: ['D','S','T','Q','Q','S','S','D'],
				    dayNamesShort: ['Dom','Seg','Ter','Qua','Qui','Sex','Sáb','Dom'],
				    monthNames: ['Janeiro','Fevereiro','Março','Abril','Maio','Junho','Julho','Agosto','Setembro','Outubro','Novembro','Dezembro'],
				    monthNamesShort: ['Jan','Fev','Mar','Abr','Mai','Jun','Jul','Ago','Set','Out','Nov','Dez'],
				    nextText: 'Próximo',
				    prevText: 'Anterior'
				});	

				$('.datepicker').mask("99/99/9999");
			}, 200);
		})
		$scope.$watch('screen', function(oldValue, newValue){
			$timeout(function(){
				$('.datepicker').datepicker({ 
					dateFormat: 'dd/mm/yy',
					dayNames: ['Domingo','Segunda','Terça','Quarta','Quinta','Sexta','Sábado'],
				    dayNamesMin: ['D','S','T','Q','Q','S','S','D'],
				    dayNamesShort: ['Dom','Seg','Ter','Qua','Qui','Sex','Sáb','Dom'],
				    monthNames: ['Janeiro','Fevereiro','Março','Abril','Maio','Junho','Julho','Agosto','Setembro','Outubro','Novembro','Dezembro'],
				    monthNamesShort: ['Jan','Fev','Mar','Abr','Mai','Jun','Jul','Ago','Set','Out','Nov','Dez'],
				    nextText: 'Próximo',
				    prevText: 'Anterior'
				});		

				$('.datepicker').mask("99/99/9999");
			}, 200);
		})
	
    }
    
    $scope.isChecked = function(){
    	if($(".yes").is(':checked')){
    		return ".yes";
    	}
    	
    	if($(".no").is(':checked')){
    		return ".no";
    	}
    }
    
};



function isBooleanChecked(that){
	$(that).parent().css("border", "0");
	$(that).parent().parent().find("span.tooltip-validation").remove();
}

/**
 * Function responsible for loading the user photo on the screen at the time it was selected
 */


