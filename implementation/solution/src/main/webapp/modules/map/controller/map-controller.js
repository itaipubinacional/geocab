'use strict';

/**
 * 
 * @param $scope
 * @param $log
 * @param $location
 */
function MapController( $scope, $injector, $log, $state, $timeout, $modal, $location, $http , $importService, $translate ) {
	
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
        $scope.loadMarkers();

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

            if ($scope.layers.length > 0 && !$scope.menu.fcArea && !$scope.menu.fcDistancia){

                // zera os valores
                $scope.features = [];

                var listUrls = [];

                for(var i in $scope.layers )
                {
                    var url = $scope.layers[i].wmsSource.getGetFeatureInfoUrl(
                        evt.coordinate, $scope.view.getResolution(), $scope.view.getProjection(),
                        {'INFO_FORMAT': 'application/json'});


                    listUrls.push(decodeURIComponent(url));
                }

                listAllFeatures(listUrls);
            }


            if( $scope.menu.fcMarker && !$scope.screenMarkerOpenned ) {
            	$scope.screenMarkerOpenned = true;
                $scope.toggleSidebarMarkerCreate(300, '#menu-item-1');

                $("#marker-point").css('display','inherit');
                
                $scope.marker = new ol.Overlay({
                    position: evt.coordinate,
                    positioning: 'center-center',
                    element: document.getElementById('marker-point'),
                    stopEvent: false
                });
                $scope.map.addOverlay($scope.marker);
                
                $scope.currentEntity.latitude = evt.coordinate[0];
                $scope.currentEntity.longitude = evt.coordinate[1];
                
                layerGroupService.listAllLayerGroups({
            		callback : function(result) {
                        //$scope.layersGroups = result;
                        $scope.selectLayerGroup = [];
                        
                        angular.forEach(result, function(group,index){
                        	
                        	angular.forEach(group.layers, function(layer, index){
                        		$scope.selectLayerGroup.push({
                            		"layerTitle": layer.title,
                            		"layerId": layer.id,
                            		"group": group.name
                            	});
                        	})
                        	
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
                
            }



        });

    };

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
                            $scope.features.push(feature);
                        }

                    });
                }

                $scope.$apply();


                // inicializa popup
                if ($scope.features.length > 0){
                    $scope.showMapInfo($scope.features);
                }


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
    	$scope.hasPermissionCalculoDistancia = true;
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

                for(var i in $scope.layers)
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
                for(var i in $scope.layers)
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
    		$scope.clearFcMaker(true);
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
    	
    	if($("#sidebar-marker-detail").css("display") == 'block') {
    		$scope.clearDetailMarker();
    	}
    	
    	 $scope.map.removeInteraction(draw);
         source.clear();
         $scope.map.removeLayer(vector);
         $('#popup').css("display","none");
         sketch = null;
         
         
    	if ($scope.menu.fcMarker){

            $scope.menu.fcMarker = false;
            $scope.toggleSidebarMarkerCreate(300, 'closeButton');
            $scope.map.removeOverlay($scope.marker);
            $("body").prepend('<div id="marker-point" class="marker-point" style="display: none;"></div>');
            $scope.screenMarkerOpenned = false;
            return;

        } else {

        	$("body").prepend('<span id="marker-point" class="marker-point glyphicon glyphicon-map-marker" style="display: none;"></span>');
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
    		$scope.clearFcMaker(true);
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
            $scope.map.getLayers().push(new ol.layer.Vector({
                source: vectorSource
            }));

            //Redireciona para o ponto que o arquivo KML é arrastado
            var view = $scope.map.getView();
            view.fitExtent(
                vectorSource.getExtent(), ($scope.map.getSize()));
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
    $scope.toggleSidebarLayers = function (time, element){
    	
    	$scope.toggleSidebar(time, element, '#sidebar-layers');
    	
    };


    /**
     * Função que gerencia a Sidebar
     * @param time Tempo execução da animação.
     * @param element Nome do elemento que está chamando a função.
     */
    $scope.toggleSidebarMarkerCreate = function (time, element){
    	if(element == "closeButton") {
            $scope.screenMarkerOpenned = false;
        }
    	
    	$scope.toggleSidebar(time, element, '#sidebar-marker-create');
    };
    
    $scope.toggleSidebarMarkerUpdate = function (time, element){
    	$scope.currentEntity = $scope.markerDetail.data;
    	
    	if(element == "closeButton") {
            $scope.screenMarkerOpenned = false;
        }
    
    	layerGroupService.listAllLayerGroups({
    		callback : function(groups) {
               // $scope.layersGroups = result;
                
                $scope.selectLayerGroup = [];
                
                angular.forEach(groups, function(group,index){
                	
                	angular.forEach(group.layers, function(layer, index){
                		
                
                		$scope.selectLayerGroup.push({
                    		"layerTitle": layer.title,
                    		"layerId": layer.id,
                    		"group": group.name
                    	});
                	})
                	
                })
                
                
                
                markerService.findAttributeByMarker($scope.currentEntity.id, {
	       			 callback : function(result) {
	       				$scope.attributesByMarker = result;
	       				
	       				/*
	       				
	       				
	       				
	       				 angular.forEach($scope.layersGroups, function(value, index){
	       					angular.forEach(value.layers, function(val, ind){
	       						if( val.id == result[0].marker.layer.id ) {
	       							val.selected = true;
	       							$scope.currentEntity.layer = val;
	           					} else {
	           						val.selected = false;
	           					}
	       					});
	       				 });*/
	       				
	       			
	       				
                        angular.forEach($scope.selectLayerGroup, function(layer,index){
                        		if( layer.layerId == result[0].marker.layer.id ) {
	       							$scope.currentEntity.layer = layer;
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
    	
    	$scope.toggleSidebar(time, element, '#sidebar-marker-update');
    };
    
    $scope.toggleSidebarMarkerDetail = function (time, element, marker){
    	
    	
    	
    	markerService.findAttributeByMarker($scope.markerDetail.data.id, {
		  callback : function(result) {
			  $scope.markerResultDetail = result;
			  $scope.markerResultDetail.header = {};
			  angular.forEach(result, function(val, ind){
				  
				  if(val.attribute.type == "TEXT" && val.attribute.name == "Title") {
					  $scope.markerResultDetail.header.title = val.value;
					  var date = new Date(val.marker.created);
					  $scope.markerResultDetail.header.date = (date.getDate() < 10 ? "0" : "") + date.getDate() + "/" + (date.getMonth() < 9 ? "0" : "") + (date.getMonth() + 1) + "/" + date.getFullYear();
					  $scope.markerResultDetail.header.layer = val.attribute.layer.name;
				  }
				  
			  })
			  $scope.$apply();
			 
          },
          errorHandler : function(message, exception) {
              $scope.message = {type:"error", text: message};
              $scope.$apply();
          }
    	});
    	
    	if(element == "closeButton") {
            $scope.screenMarkerOpenned = false;
            $(".marker-point").css("color","#0077bf");
            $scope.toggleSidebar(time, element, '#sidebar-marker-detail');
        }
    	
    	if( $('.menu-sidebar-container').css('right') != '3px') {
    		//$scope.toggleSidebar(0, 'closeButton', '#sidebar-marker-detail');
    		/*
    		 * TODO: Colocar um loading...
    		 * */
    		$scope.toggleSidebar(time, 'closeButton', '#sidebar-marker-detail');
    	}
    	
    	$scope.toggleSidebar(time, element, '#sidebar-marker-detail');
    	
    };
    
    $scope.clearDetailMarker = function() {
    	$(".marker-point").css("color","#0077bf");
    	$scope.toggleSidebar(0, 'closeButton', '#sidebar-marker-detail');
    }
    
    $scope.toggleSidebar = function (time, element, slide){
	    time = time != null ? 300 : time;
	
	    //Verifica se a animação é para abrir ou fechar a sidebar pela posição atual dela.
	    var opening = $('.menu-sidebar-container').css('right') == '3px';
	
	    //Verifica se o usuário clicou num botão que está ativo e a barra está amostra, se é para abrir ou se o clique partiu do botão de fechar.
	    if ((element == $scope.lastActive && !opening) || (opening) || (element == "closeButton")) {
	
	        //Gerencia a classe 'bg-inactive' que ativa e desativa os botões.
	        if (opening) {
	            if ($(element).hasClass('bg-inactive')) $(element).removeClass('bg-inactive');
	        } else {
	            $(".menu-item").addClass("bg-inactive");
	        }
	        //Executa a animação.
	        $(slide).toggle('slide', { direction: 'right' }, time);
	        if(slide != "#sidebar-marker-update") {
		        $('.menu-sidebar-container').animate({
		            'right' : opening ? '20%' : '3px'
		        }, time);
	        }
	    } else {
	        if ($(element).hasClass('bg-inactive')) $(element).removeClass('bg-inactive');
	    }
	    $scope.lastActive = element;
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
    
    $scope.clearFcMaker = function(removeOverlay) {
    	$scope.currentEntity = new Marker();
    	
    	$scope.menu.fcMarker = false;
    	
    	if($scope.screenMarkerOpenned)
    		$scope.toggleSidebarMarkerCreate(300, 'closeButton');

    	if( removeOverlay ) {
    		$scope.map.removeOverlay($scope.marker);
    	}
    		
        
        $scope.screenMarkerOpenned = false;
        $scope.attributesByLayer = [];
    }
    
    $scope.updateMarker = function(){
    	/*
    	 * TODO: Verificar se todo o formário foi preenchido.
    	 * */
    	
    	/*if (!$scope.form('sidebarMarkerUpdate').$valid){
    		 $scope.msg = {type: "danger", text: "preencha", dismiss: true};
    		return;
    	}*/
    	
    	if( $scope.currentEntity.layer == null ) {
    		var layer = new Layer();
        	layer.id = $scope.currentEntity.layer;
        	$scope.currentEntity.layer = layer;	
    	}
    	
    	
    	$scope.currentEntity.markerAttribute = $scope.attributesByMarker;
    		
    	markerService.updateMarker($scope.currentEntity,{
      		callback : function(result) {
      		      			
      			$scope.toggleSidebarMarkerUpdate(300, 'closeButton')
      			
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
    	/*
    	 * TODO: Verificar se todo o formário foi preenchido.
    	 * */
    	
    	if (!$scope.form('sidebarMarker').$valid){
    		 $scope.msg = {type: "danger", text: "preencha", dismiss: true};
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
    		markerAttribute.value = val.value;
    		markerAttribute.attribute = attribute
    		markerAttribute.marker = $scope.currentEntity;
    		$scope.currentEntity.markerAttribute.push(markerAttribute);
    		
    	});
    	
    	//$scope.currentEntity.markerAttribute = $scope.attributesByLayer;
    	
    	markerService.insertMarker($scope.currentEntity,{
      		callback : function(result) {
      			  $scope.clearFcMaker(false);
      			        			  
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
    
    $scope.listAttributesByLayer = function(){
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
    
    $scope.loadMarkers = function(){
    	markerService.listAll({
      		callback : function(result) {
      			
      			angular.forEach(result, function(val, ind){
      				$("body").append('<span id="marker-point-'+ind+'" class="marker-point glyphicon glyphicon-map-marker"></span>');
      			
    	  			var marker = new ol.Overlay({
    	                position: [val.latitude, val.longitude],
    	                positioning: 'center-center',
    	                element: document.getElementById('marker-point-'+ind),
    	                stopEvent: false
    	            });
    	  			
    	            $scope.map.addOverlay(marker);
    	            
    	            $("#marker-point-"+ind).dblclick(function(event){
    	            	$(".marker-point").css("color","#0077bf");
    	            	event.stopPropagation();
    	            	$scope.markerDetail = {data: val, overlay: marker};
    	            	$scope.toggleSidebarMarkerDetail(300, '#menu-item-1');
    	            	$(this).css("color","#FF0000");
    	  			})
    	  			
    	            
    	            $scope.$apply();
      			})
      			
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

    	markerService.removeMarker($scope.markerDetail.data.id, {
      		  callback : function(result) {
      			$scope.map.removeOverlay($scope.markerDetail.overlay);
      			
      			
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
    	 console.log(element);
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
    		
    		markerService.enableMarker($scope.markerDetail.data.id, {
  			  callback : function(result) {
  				console.log(result);
  				
  				
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
    		
    		markerService.disableMarker($scope.markerDetail.data.id, {
			  callback : function(result) {
				console.log(result);
				
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
};

