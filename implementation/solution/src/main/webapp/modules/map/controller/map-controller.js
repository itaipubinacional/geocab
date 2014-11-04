'use strict';

/**
 * 
 * @param $scope
 * @param $log
 * @param $location
 */
function MapController( $scope, $injector, $log, $state, $timeout, $modal, $location, $http , $importService, $translate) {
	
	/**
	 * Injeta os métodos, atributos e seus estados herdados de AbstractCRUDController.
	 * @see AbstractCRUDController
	 */
	$injector.invoke(AbstractCRUDController, this, {$scope: $scope});

	/**
	 * Include services
	 */
	$importService("layerGroupService");
	$importService("markerService");
	
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
     * MapQuest SATÉLITE
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
     * Variável que armazena o tipo do mapa selecionado pelo usuário - GMAP ou OSM
     * Irá conter também o módulo selecionado - Terreno, Satélite, Híbrido, etc.
     */
    $scope.mapConf = {
        type : $scope.MAP_TYPE_OSM,
        ativo : null,
        modo : null
    };


    /**
     * Variável que armazena as configurações do google map
     */
    $scope.mapGoogleOptions = {};

    /**
     * Variável que representa o google map
     */
    $scope.mapGoogle = {};

    /**
     * Variável que representa o mapa openlayers
     */
    $scope.map = {};

    /**
     * Variável responsável por controlar se as funcionalidades estão ativas ou não
     */
    $scope.menu = {
        fcDistancia : false,
        fcArea : false,
        fcKml :  false,
        fcMarker: false
    };

    /**
     * Variável que armazena a lista dos grupos de camadas com o perfil de acesso do usuário
     * @type {Array}
     */
    $scope.allLayers = [];

    /**
     * Variável que armazena todas as camadas selecionada pelo usuário
     * @type {Array}
     */
    $scope.layers = [];
    
    /**
     * Variável que armazena as camadas kml ativas no mapa
     */
    $scope.kmlLayers = [];

    /**
     * Variável que armazena todas as camadas internas selecionada pelo usuário
     * @type {Array}
     */
    $scope.internalLayers = [];
  
    /**
    *
    */
    $scope.allLayersKML = []
    
    /**
     * Variável que armazena a camada interna que está sendo criada
     * @type {Array}
     */
    $scope.currentCreatingInternalLayer;
    
    /**
     * Variável que armazena a o slider que está ativo
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
    $scope.features = [];

    /*-------------------------------------------------------------------
     * 		            PERMISSÕES PARA AS FERRAMENTAS
     *-------------------------------------------------------------------*/

    //Permissão cálculo distância
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
    
    //Permissão cálculo área
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

    //Permissão cálculo área
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
     * 		 				 	 VARIÁVEIS DE CONFIGURAÇÃO MAPA
     *-------------------------------------------------------------------*/


    /**
     * Configuração da view do mapa
     */
    $scope.view = new ol.View({
        center: ol.proj.transform([-54.1394, -24.7568], 'EPSG:4326', 'EPSG:3857'),
        zoom: 9,
        minZoom: 3
    })

    // eventos da view
    $scope.view.on('change:center', function() {

        // Muda a view do google map se o mapa ativo for o gmap
        if ( $scope.mapConf.active == $scope.MAP_TYPE_GMAP ){
            var center = ol.proj.transform($scope.view.getCenter(), 'EPSG:3857', 'EPSG:4326');
            $scope.mapGoogle.setCenter(new google.maps.LatLng(center[1], center[0]));
        }

    });
    
    $scope.view.on('change:resolution', function() {

        // Muda a view do google map se o mapa ativo for o gmap
        if ( $scope.mapConf.active == $scope.MAP_TYPE_GMAP ){
            $scope.mapGoogle.setZoom($scope.view.getZoom());
        }

    });


    /**
     * Configuração da camada de fundo - OSM
     */
    $scope.rasterOSM = new ol.layer.Tile({
        source: new ol.source.OSM()
        //source: new ol.source.MapQuest({layer: 'osm'})
    });

    /**
     * Configuração da camada de fundo - MAPQUEST OSM
     */
    $scope.rasterMapQuestOSM = new ol.layer.Tile({
        source: new ol.source.MapQuest({layer: 'osm'})
    });

    /**mapConf.type
     * Configuração da camada de fundo - MAPQUEST SAT
     */
    $scope.rasterMapQuestSAT = new ol.layer.Tile({
        source: new ol.source.MapQuest({layer: 'sat'})
    });


    // Interação de rotate
    $scope.dragRotateAndZoom = new ol.interaction.DragRotateAndZoom();


    /**
     * Configuração de controle de posição de mouse
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


	/*-------------------------------------------------------------------
	 * 		 				 	  NAVIGATIONS
	 *-------------------------------------------------------------------*/
	/**
     * Método executado ao entrar na controller, apór carregar o DOM da página
     *
	 */
	$scope.initialize = function( toState, toParams, fromState, fromParams ) {
		
		$("#sidebar-marker-create, #sidebar-marker-detail-update").css("max-width", parseInt($(window).width()) - 68 );
		
		$("#sidebar-marker-create, #sidebar-marker-detail-update").resize(function() { 
			$(".menu-sidebar-container").css("right",parseInt($(this).css("width")) + 5); 
		});
		
		
		/**
		 * Caso não existe uma nav bar
		 * */
		/*if( !$("#navbar-administrator").length ) {
			$(".sidebar-style").css("top","60px");
		}*/
		
        /**
         * Configuração do mapa openlayers
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

            // rotação
            interactions: ol.interaction.defaults({
                dragPan: false
            }).extend([
                    $scope.dragRotateAndZoom,
                    dragAndDropInteraction,
                    new ol.interaction.DragPan({kinetic: null})
                ]),

            target: $scope.olMapDiv,
            view: $scope.view
        });

        
        
        // adiciona layer do OSM
        $scope.map.addLayer($scope.rasterOSM);
        $scope.rasterOSM.setVisible(false);

        // adiciona layer do MapQuest OSM
        $scope.map.addLayer($scope.rasterMapQuestOSM);
        $scope.rasterMapQuestOSM.setVisible(false);

        // adiciona layer do MapQuest OSM
        $scope.map.addLayer($scope.rasterMapQuestSAT);
        $scope.rasterMapQuestSAT.setVisible(false);
        
        // Registra as postagens no mapa
        //$scope.loadMarkers();

        /**
         * Configuração do mapa GMAP
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

//        // setar o mapGoogleOptions
//        $scope.mapGoogle = new google.maps.Map(document.getElementById("gmap"), $scope.mapGoogleOptions);

        // exibe coordenadas do mouse
        enableMouseCoordinates();


        $scope.LAYER_MENU_STATE = 'list';

        $scope.enableTools();

        $scope.listPublishedLayersGroup();

        // inicializa mapa
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
         * Evento de click para solicitar ao geoserver as informações das camadas da coordenada clicada
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

                listAllFeatures(listUrls);
                
                $scope.screen = 'detail';
               
            }

        	/* if click on the marker */
        	if( feature ){
        		if( typeof feature.getProperties().marker != "undefined" ) {
	        		$scope.screen = 'detail';
	        		
					if( $scope.screenMarkerOpenned ) {
						$scope.clearFcMarker();
					}
				        		
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
     * Funcão que faz requisição ao geo server para trazer as features
     * @param url
     * @param posicao
     */
    var listAllFeatures = function (listUrls){

    	layerGroupService.listAllFeatures(listUrls, {
            callback: function (result) {
            	
                for (var i=0; i < result.length; i++){

                    var feature = {
                        layer : $scope.layers[i],
                        campos : {}
                    };

                    angular.forEach(JSON.parse(result[i]).features, function(value, key) {
                        angular.forEach(value.properties, function(value, key) {

                            try {
                                feature.campos[decodeURIComponent( escape(key))] = decodeURIComponent( escape(value));
                            }
                            catch(e) {
                                feature.campos[key] = value;
                            }

                        });

                        var insere = false;
                        for (var propriedade in feature.campos) {
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
    $scope.enableTools = function(){
    	$scope.hasPermissionCalculoDistancia = true;
    	$scope.hasPermissionCalculoArea = true;
    	$scope.hasPermissionKML = true;
        enableFileKML();
    }

    /**
     *
     */
    $scope.listPublishedLayersGroup = function(){

        //Lista os os grupos de camadas e camadas publicados de acordo com o perfil de acesso do usuário
        layerGroupService.listLayerGroupUpperPublished( {
            callback: function (result) {

                var parseNode = function( node ) {
                    var item = {};

                    item.label =  !!node.nodes ? node.name : node.title;
                    item.name =  !!node.nodes ? '' : node.name;
                    item.legenda =  !!node.nodes ? '' : node.legend;
                    item.selected =  !!node.nodes ? '' : node.startEnabled;
                    item.fonteDadosEndereco =  !!node.nodes ? '' : node.dataSource.url;
                    item.value = node.id;
                    item.type = !!node.nodes ? 'grupo' : 'camada';

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

                //$scope.toggleSidebar(300, '#menu-item-1');

                // Abre a tree de camadas
//                $timeout(function(){
//                    $('#tree').find('.ivh-treeview-node-collapsed').removeClass('ivh-treeview-node-collapsed');
//                });

                $scope.$apply();
            },
            errorHandler: function (message, exception) {
                $scope.msg = {type: "danger", text: message, dismiss: true};
                $scope.$apply();
            }
        });

    }

    /**
     * Formata a url com o nome da camada para cada fonte de dados
     * @param node
     * @returns {{name: string, url: string}}
     */
    $scope.formatUrl = function(node, isPesquisa) {

        if( isPesquisa )
        {
            var index = node.nome.indexOf(":");
            var enderecoFonteDados = node.fonteDados.endereco.lastIndexOf("geoserver/");
            var tipoCamada = node.nome.substring(0,index);
            var nomeCamada = node.nome.substring(index+1,node.nome.length);
            var urlFormatada = node.fonteDados.endereco.substring(0, enderecoFonteDados+10)+tipoCamada+'/wms';
        }
        else
        {
            var index = node.name.indexOf(":");
            var enderecoFonteDados = node.fonteDadosEndereco.lastIndexOf("geoserver/");
            var tipoCamada = node.name.substring(0,index);
            var nomeCamada = node.name.substring(index+1,node.name.length);
            var urlFormatada = node.fonteDadosEndereco.substring(0, enderecoFonteDados+10)+tipoCamada+'/wms';
        }

        return {'name': nomeCamada, 'url': urlFormatada};

    }

  

    /**
     * Trata a seleção e desseleção de uma cada da tree
     * @param node
     */
    $scope.getSelectedNode = function(node){
    	
    	/* Check if it is an internal layer */
    	if(node.fonteDadosEndereco == null) {
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
                    source: wmsSource
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
                    //Adiciona na lista cada camada selecionada
                    $scope.layers.push({'wmsLayer': wmsLayer, 'wmsSource': wmsSource, "name":node.name, "titulo":node.label});

                    //Adiciona as camadas selecionadas no mapa
                    $scope.map.addLayer(wmsLayer);
                }
            }
            else
            {
            	for(var i=0; i < $scope.layers.length; i++)
                {
                    if( $scope.layers[i].name == node.name )
                    {
                        //Remove as camadas desselecionadas pelo usuário
                        $scope.map.removeLayer($scope.layers[i].wmsLayer);
                        $scope.layers.splice(i,1);
                    }
                }
            }
        }


    }
    
    /**
     * Trata a seleção e desseleção da tree de camadas kml
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

    /*-------------------------------------------------------------------
     * 		 			CONFIGURAÇÃO DO GOOGLE MAP
     *-------------------------------------------------------------------*/

    /**
     * Método que inicializa o mapa Google Maps e suas configurações
     */
    $scope.initializeGMAP = function initializeGMAP() {

        // só executa se o mata ativo no momento não for o google maps
        if ( $scope.mapConf.active != $scope.MAP_TYPE_GMAP ){


            // caso mapa ativo for MAP QUEST OSM
            if ( $scope.mapConf.active == $scope.MAP_TYPE_MAPQUEST_OSM ){
                $scope.rasterMapQuestOSM.setVisible(false);
            }

            // caso mapa ativo for MAP QUEST SAT
            if ( $scope.mapConf.active == $scope.MAP_TYPE_MAPQUEST_SAT ){
                $scope.rasterMapQuestSAT.setVisible(false);
            }

            // caso mapa ativo for MAP QUEST OSM
            if ( $scope.mapConf.active == $scope.MAP_TYPE_OSM ){
                $scope.rasterOSM.setVisible(false);
            }

            //ajusta css da div gmap - para mostra-la
            $("#gmap").css({"width":"100%","height":"100%"})

            // setar o mapGoogleOptions
            $scope.mapGoogle = new google.maps.Map(document.getElementById("gmap"), $scope.mapGoogleOptions);


            $scope.olMapDiv.parentNode.removeChild($scope.olMapDiv);
            $scope.mapGoogle.controls[google.maps.ControlPosition.RIGHT_TOP].push($scope.olMapDiv)

            // remove a interação de rotate
            $scope.map.removeInteraction($scope.dragRotateAndZoom);

            // tira a rotação
            $scope.map.getView().setRotation(0);


            $scope.mapConf.active = $scope.MAP_TYPE_GMAP;

            // setar view do google maps
            $scope.view.setCenter($scope.view.getCenter());
            $scope.view.setZoom($scope.view.getZoom());


        }

    }


    /**
     * Método que inicializa o mapa Open Street Map e suas configurações
     */
    $scope.initializeOSM = function initializeOSM() {

        // só executa se o mapa ativo no memento não for o OSM
        if ( $scope.mapConf.active != $scope.MAP_TYPE_OSM ){

           if ( $scope.mapConf.active == $scope.MAP_TYPE_GMAP ){

               //ajusta css da div gmap - para não mostra-la
               $("#gmap").css({"width":"0","height":"0"})

               // Retira o elemento olmap da div do google maps
               var element = document.getElementById("main-content");
               $scope.olMapDiv.style.position = "relative";
               $scope.olMapDiv.style.top = "0";
               element.appendChild($scope.olMapDiv);

               // adiciona interação de rotate
               $scope.map.addInteraction($scope.dragRotateAndZoom);
           }

            // caso mapa ativo for MAP QUEST OSM
            if ( $scope.mapConf.active == $scope.MAP_TYPE_MAPQUEST_OSM ){
                $scope.rasterMapQuestOSM.setVisible(false);
            }

            // caso mapa ativo for MAP QUEST SAT
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

        // só executa se o mapa ativo no memento não for o OSM
        if ( $scope.mapConf.active != $scope.MAP_TYPE_MAPQUEST_OSM ){

            // caso mapa ativo for google maps
            if ( $scope.mapConf.active == $scope.MAP_TYPE_GMAP ){

                //ajusta css da div gmap - para não mostra-la
                $("#gmap").css({"width":"0","height":"0"})

                // Retira o elemento olmap da div do google maps
                var element = document.getElementById("main-content");
                $scope.olMapDiv.style.position = "relative";
                $scope.olMapDiv.style.top = "0";
                element.appendChild($scope.olMapDiv);

                // adiciona interação de rotate
                $scope.map.addInteraction($scope.dragRotateAndZoom);
            }

            // caso mapa ativo for OSM
            if ( $scope.mapConf.active == $scope.MAP_TYPE_OSM ){
                $scope.rasterOSM.setVisible(false);
            }

            // caso mapa ativo for MAPQUEST SAT
            if ( $scope.mapConf.active == $scope.MAP_TYPE_MAPQUEST_SAT ){
                $scope.rasterMapQuestSAT.setVisible(false);
            }

            // seta o mapa do map quest osm
            $scope.rasterMapQuestOSM.setVisible(true);

            $scope.mapConf.active = $scope.MAP_TYPE_MAPQUEST_OSM;

        }
    }


    /**
     *
     */
    $scope.initializeMapQuestSAT = function() {

        // só executa se o mapa ativo no memento não for o OSM
        if ( $scope.mapConf.active != $scope.MAP_TYPE_MAPQUEST_SAT ){

            // caso mapa ativo for google maps
            if ( $scope.mapConf.active == $scope.MAP_TYPE_GMAP ){

                //ajusta css da div gmap - para não mostra-la
                $("#gmap").css({"width":"0","height":"0"})

                // Retira o elemento olmap da div do google maps
                var element = document.getElementById("main-content");
                $scope.olMapDiv.style.position = "relative";
                $scope.olMapDiv.style.top = "0";
                element.appendChild($scope.olMapDiv);

                // adiciona interação de rotate
                $scope.map.addInteraction($scope.dragRotateAndZoom);
            }

            // caso mapa ativo for OSM
            if ( $scope.mapConf.active == $scope.MAP_TYPE_OSM ){
                $scope.rasterOSM.setVisible(false);
            }

            // caso mapa ativo for MAP QUEST OSM
            if ( $scope.mapConf.active == $scope.MAP_TYPE_MAPQUEST_OSM ){
                $scope.rasterMapQuestOSM.setVisible(false);
            }


            // seta o mapa do map quest osm
            $scope.rasterMapQuestSAT.setVisible(true);

            $scope.mapConf.active = $scope.MAP_TYPE_MAPQUEST_SAT;

        }
    }



    /*-------------------------------------------------------------------
     * 		 		FUNCIONALIDADE EXIBIR COORDENADAS DO MOUSE
     *-------------------------------------------------------------------*/


    /**
     * Método responsável por exibir no mapa as coordenadas do ponteiro do mouse
     */
    function enableMouseCoordinates() {

        /**
         * Variável que possui o elemento que contém o tooltip
         */
        var info = $('#info');

        /**
         * Método que mostra as coordenadas do mouse no mapa
         */
        var displayCoordinateMouse = function(pixel) {

            info.html("<p>" + formatCoordinate($scope.mousePositionControl.l) + "</p>");
            info.css("display","block");

        };
        
        /**
         * Método que formata a coordenada do mouse
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
         * Evento para exibir coordenada do mouse
         */
        $($scope.map.getViewport()).on('mousemove', function(evt) {
        	displayCoordinateMouse($scope.map.getEventPixel(evt.originalEvent));
        });
    }


    /**
     * Função que esconde a posição do mouse
     */
    $scope.hideMousePosition = function (){
        var info = $('#info');
        info.tooltip('hide');
    }


    /*-------------------------------------------------------------------
     * 		 	    FUNCIONALIDADE CALCULAR DISTÂNCIA E ÁREA
     *-------------------------------------------------------------------*/


    /**
     * Método que calcula a distância de pontos no mapa interativo
     */
    $scope.initializeDistanceCalc = function () {

    	if($scope.menu.fcMarker){
    		$scope.clearFcMarker();
    	}
    	
        // verifica se alguma funcionalidade ja está ativa
        if ($scope.menu.fcDistancia || $scope.menu.fcArea){

            // se funcionalidade esta ativa é necessário sair da funcionalidade
            $scope.map.removeInteraction(draw);
            source.clear();
            $scope.map.removeLayer(vector);
            $('#popup').css("display","none");
            sketch = null;


        }

        // se funcionalidade esta ativa : desativa e sai
        if ($scope.menu.fcDistancia){

            $scope.menu.fcDistancia = false;
            return;

        } else {

            // ativa funcionalidade e desativa as outras para só ter uma ativa por vez
            $scope.menu = {
                fcDistancia : true,
                fcArea : false,
                fcKml :  false,
                fcMarker: false
            };

            // adiciona a camada de medição no mapa
            $scope.map.addLayer(vector);

            // adiciona o evento no mapa
            $($scope.map.getViewport()).on('mousemove', mouseMoveHandler);

            // inicializa a a interação
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
        	
            // ativa funcionalidade e desativa as outras para só ter uma ativa por vez
            $scope.menu = {
                fcDistancia : false,
                fcArea : false,
                fcKml :  false,
                fcMarker: true
            };
         
        }
    }
    
    /**
     * Método que calcula a área de pontos no mapa interativo
     */
    $scope.initializeAreaCalc = function () {

    	if($scope.menu.fcMarker){
    		$scope.clearFcMarker();
    	}

        // verifica se alguma funcionalidade ja está ativa
        if ($scope.menu.fcArea || $scope.menu.fcDistancia || $scope.menu.fcMarker){

            // se funcionalidade esta ativa é necessário sair da funcionalidade
            $scope.map.removeInteraction(draw);
            source.clear();
            $scope.map.removeLayer(vector);
            $('#popup').css("display","none");
            sketch = null;

        }

        // se funcionalidade esta ativa : desativa e sai
        if ($scope.menu.fcArea){

            $scope.menu.fcArea = false;
            return;

        } else {

            // ativa funcionalidade e desativa as outras para só ter uma ativa por vez
            $scope.menu = {
                fcDistancia : false,
                fcArea : true,
                fcKml :  false,
                fcMarker: false
            };

            // adiciona a camada de medição no mapa
            $scope.map.addLayer(vector);

            // adiciona o evento no mapa
            $($scope.map.getViewport()).on('mousemove', mouseMoveHandler);

            // inicializa a a interação
            addInteraction( 'Polygon' );
        }

    }

    // VARIÁVEIS
    /**
     * Source da camada de medição
     */
    var source = new ol.source.Vector();

    /**
     * Configuração da camada de medição
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
     * Variável que conterá a instância do ol.interaction.Draw
     */
    var draw;


    // EVENTOS
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


    // FUNÇÕES
    /**
     * Método que adiciona uma interação do usuário no mapa
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

                // limpa as marcações antigas
                source.clear();

            }, this);

        draw.on('drawend',
            function(evt) {
                // unset sketch
                sketch = null;

            }, this);
    }


    /**
     * Método que gera o formato de saída de medição da distância
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
     * Método que gera o formato de entrada de medição da área
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
     * 		 			FUNCIONALIDADE KML
     *-------------------------------------------------------------------*/


    /**
     * Método que permiti arrastar um arquivo KML para o mapa interativo
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
     * 		 			FUNCIONALIDADE KML
     *-------------------------------------------------------------------*/

    //Formatos permitidos a serem arrastados no mapa
    var dragAndDropInteraction = new ol.interaction.DragAndDrop({
        formatConstructors: [
            ol.format.KML
        ]
    });


    /**
     * Método que permiti arrastar um arquivo KML para o mapa interativo
     */
    function enableFileKML()
    {
        //Controla o drag and drop do arquivo KML no mapa
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

            //Redireciona para o ponto que o arquivo KML é arrastado
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

            // seleciona a ultima pesquisa
            item.children[item.children.length-1].selected = true;

            // seleciona o grupo pai
            var selectItemPai = true;
            for( var i in item.children )
            {
                if (item.children[i].selected != true){
                    selectItemPai = false
                }
            }

            // seleciona o grupo pai
            if (selectItemPai) item.selected = true;

            $scope.allLayersKML = [];
            $scope.allLayersKML.push(item);



        });


    }

    /*-------------------------------------------------------------------
     * 		 			COMPORTAMENTO DA SIDEBAR
     *-------------------------------------------------------------------*/

    /**
     * Função que gerencia a Sidebar
     * @param time Tempo execução da animação.
     * @param element Nome do elemento que está chamando a função.
     */
    $scope.toggleSidebarMenu = function (time, element){
    	
    	/**
    	 * Caso a aba do marker estiver aberta, feche ele e espere para abrir a nova.
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
     * Função que gerencia a Sidebar
     * @param time Tempo execução da animação.
     * @param element Nome do elemento que está chamando a função.
     */
    $scope.toggleSidebarMarkerCreate = function (time, element){
    	$scope.imgResult = "";
    	$scope.$apply();
    	
    	if(element == "closeButton") {
            $scope.screenMarkerOpenned = false;
        }
    	
    	/**
    	 * Caso a aba do marker estiver aberta, feche ele e espere para abrir a nova.
    	 * */
    	if($scope.slideActived == '#sidebar-layers') {
    		$scope.toggleSidebar(time, 'closeButton', '#sidebar-layers');
    		
    		$timeout(function(){
        		$scope.toggleSidebar(time, element, '#sidebar-marker-create');
        	}, 400)
    	} else {
    		
    		$scope.toggleSidebar(time, element, '#sidebar-marker-create');
    	}
    	
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
    	/* Lista para o edit*/
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
    	 * Caso a aba do marker estiver aberta, feche ele e espere para abrir a nova.
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
    	
    };
    
    $scope.clearDetailMarker = function() {
    	$scope.toggleSidebar(0, 'closeButton', '#sidebar-marker-detail-update');
    }
    
    $scope.toggleSidebar = function (time, element, slide){
	    time = time != null ? 300 : time;
	    
	    //Verifica se a animação é para abrir ou fechar a sidebar pela posição atual dela.
	    var closed = $('.menu-sidebar-container').css('right') == '3px';
	
	    //Verifica se o usuário clicou num botão que está ativo e a barra está amostra, se é para abrir ou se o clique partiu do botão de fechar.
	    if ((element == $scope.lastActive && !closed) || (closed) || (element == "closeButton")) {
	
	        //Gerencia a classe 'bg-inactive' que ativa e desativa os botões.
	        if (closed) {
	            if ($(element).hasClass('bg-inactive')) $(element).removeClass('bg-inactive');
	        } else {
	            $(".menu-item").addClass("bg-inactive");
	        }
	        //Executa a animação.
	        
	        $(slide).toggle('slide', { direction: 'right' }, time);
	        $('.menu-sidebar-container').animate({
	            'right' : closed ? '20%' : '3px'
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
     * 		 			MENU LATERAL DO MAPA
     *-------------------------------------------------------------------*/

    /**
     * Função que diminui zoom do mapa
     */
    $scope.diminuirZoom = function (){
        $scope.map.getView().setZoom($scope.map.getView().getZoom() - 1);
    }

    /**
     * Função que aumenta zoom do mapa
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
    		 $scope.msg = {type: "danger", text: "preencha os campos obrigatorios", dismiss: true};
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
//    	var iconStyle = new ol.style.Style({
//            image: new ol.style.Icon(({
//                anchor: [0.5, 1],
//                anchorXUnits: 'fraction',
//                anchorYUnits: 'fraction',
//                src: $scope.currentEntity.layer.layerIcon
//            }))
//        });
//    	$scope.currentCreatingInternalLayer.setStyle(iconStyle);
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
    		callback(layerId);
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
			                   source: new ol.source.Vector({ features: [iconFeature] })
			               });
			
			               layer.setStyle(iconStyle);
			
			               $scope.map.addLayer(layer);
			               
			               $scope.internalLayers.push({"layer": layer, "id": layerId});
		     			});
		
		     			
		              
	/*
	     			angular.forEach(result, function(marker, index){
	                   var iconFeature = new ol.Feature({
	                       geometry: new ol.geom.Point([marker.latitude ,marker.longitude]),
	                       marker: marker,
	                   });	
	                  
	                   icons.push(iconFeature);
	     			});
	
	     			
	               var layer = new ol.layer.Vector({
	                   source: new ol.source.Vector({ features: icons })
	               });
	
	               layer.setStyle(iconStyle);
	
	               $scope.map.addLayer(layer);
	               $scope.internalLayers.push({"layer": layer, "id": layerId});
	               */
	               
	               
	               
	               $scope.$apply();
	 		
		          },
		          errorHandler : function(message, exception) {
		              $scope.message = {type:"error", text: message};
		              $scope.$apply();
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
    
};
/**
 * Função responsável por carregar a foto do usuário na tela no momento em que foi selecionada
 */


