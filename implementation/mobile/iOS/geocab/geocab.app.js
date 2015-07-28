
var geocabapp = function(){

	/**
	 * Atributos
	 */
	var layersAdd = [];
	var markersAdd = [];
	var view;
	var map;
	var nativeInterface;
    var assetsPath = '';
    var i18n = false;
	
	/**
	 * Inicializa atributos
	 */		
	var loadAttributes = function(mapTarget){
	
		view = new ol.View({
			center: ol.proj.transform([-54.1394, -24.7568], 'EPSG:4326', 'EPSG:3857'),
			zoom: 9
		});
		
		map = new ol.Map({
			target: mapTarget,
			layers: [ new ol.layer.Tile({ source: new ol.source.OSM() }) ],
			interactions: ol.interaction.defaults({altShiftDragRotate:false, pinchRotate:false, keyboard: false}),
			view: view,
			control: ol.control.defaults({rotate: false})
		});		
			
	};
	
	/**
	 * Inicializa event handlers
	 */		
	var loadEventHandlers = function(){
		
		map.on('click', function(evt) {

			// Verifica se foi selecionado algum marker
			var markerFeature = map.forEachFeatureAtPixel(evt.pixel, function(feature, layer) {
				return feature;
			});
			
			var markerId = markerFeature ? markerFeature.getProperties().markerId : null;
			var layersUrl = [];
			
			// Se a quantidade de camadas for maior que zero
			if ( layersAdd.length > 0) {
				for (var i in layersAdd) {
					var url = layersAdd[i].wmsSource.getGetFeatureInfoUrl(
						evt.coordinate, view.getResolution(), view.getProjection(), {
							'INFO_FORMAT': 'application/json'
						}
					);
					layersUrl.push(decodeURIComponent(url));
				}
			}

			nativeInterface.showLayerMarker(markerId, layersUrl);
			
		});		
		
	};
    
    var formatUrl = function(url, name) {
        
        var index = name.indexOf(":");
        var dataSourceAddress = url.lastIndexOf("ows?");
        var layerType = name.substring(0,index);
        var layerName = name.substring(index+1,name.length);
        var formattedUrl = url.substring(0, dataSourceAddress)+layerType+'/wms';
        
        return {'name': layerName, 'url': formattedUrl};
        
    };
	
	return {
		
		/**
		 * Inicializa componentes
		 */
		init : function(mapTarget, nativeObject){
			loadAttributes(mapTarget);
			loadEventHandlers();
			nativeInterface = nativeObject;
		},
		
		getNativeInterface : function(){
			return nativeInterface;
		},
		
		getLayersAdd : function(){
			return layersAdd;
		},		
		
        setAssetsPath : function(path){
            assetsPath = path;
        },
        
        getAssetsPath : function(){
            return assetsPath;
        },	

		getElementFromEvent : function(event){
			return $(event.target).closest('div[class^="marker-info-box"]');
		},
        
        getElementFromMap : function(){
            return $("#map").find(".marker-t");
        },
        
        isEmpty : function(value){
            return (typeof value === "undefined" || value == null
                    || value == '(null)' || value.length === 0);
        },
		
		/**
		 * Mostra ou esconde uma camada no mapa
		 */
		showLayer : function(url, id, name, title) {
            
            var item = formatUrl(url, name);
            
            var wmsSource = new ol.source.TileWMS({
                url: item.url,
                params: { 'LAYERS': item.name },
            });

            var wmsLayer = new ol.layer.Tile({
                source: wmsSource
            });

            // Adiciona a camada ao mapa
            map.addLayer(wmsLayer);

            // Adiciona a camada a lista global
            layersAdd.push({
                'wmsLayer': wmsLayer,
                'wmsSource': wmsSource,
                'id': id,
                'title': title
            });
				
		},

		/**
		 * Mostra um marcador no mapa de acordo com sua localização
		 */
		addMarker : function(marker) {
		
            if ( typeof marker === 'string' )
				marker = JSON.parse(marker);

			var layerIcon = this.getAssetsPath() + marker.layer.icon.substring(marker.layer.icon.lastIndexOf('/')+1);
            
			var iconFeature = new ol.Feature({
				geometry: new ol.format.WKT().readGeometry(marker.wktCoordenate),
				markerId: marker.id
			});

            var iSize = [32, 37];
            
            if( layerIcon.indexOf('default_') >= 0){
                iSize = [32, 32];
            }
            
			var iconStyle = new ol.style.Style({
				image: new ol.style.Icon(({
					size: iSize,
					src: layerIcon
				}))
			});

			var vectorSource = new ol.source.Vector({
				features: [iconFeature]
			});

			// adiciona o recurso na camada vetor  e aplica o estilo para toda a camada
			var vectorLayer = new ol.layer.Vector({
				source: vectorSource,
				style: iconStyle
			});

			// Adiciona a camada vetor na lista do mapa
			map.addLayer(vectorLayer);

			// Adiciona o marker a lista global
			markersAdd.push({
				vectorLayer : vectorLayer,
				marker : marker
			});

		},
        
        /**
         * Adiciona marcadores ao mapa
         */
        addMarkers : function(markers) {
            
            if ( typeof markers === 'string' )
                markers = JSON.parse(markers);
            
            for (pos in markers ) {
                this.addMarker(markers[pos]);
            }
            
        },
		
		/**
		 * Esconde marcador do mapa
		 */	
		closeMarker : function(markerId) {
			for (i in markersAdd) {
				if (markersAdd[i].marker.id == markerId) {
					map.removeLayer(markersAdd[i].vectorLayer);
                    markersAdd.splice(i, 1);
                    break;
				}
			}
		},
        
        /**
         * Esconde layer do mapa
         */
        closeLayer : function(layerId) {
            // Percorre para remover a camada que foi desmarcada
            for (i in layersAdd) {
                if (layersAdd[i].id == layerId) {
                    map.removeLayer(layersAdd[i].wmsLayer);
                    layersAdd.splice(i, 1);
                }
            }
            var i = markersAdd.length
            while (i--) {
                if (markersAdd[i].marker.layer.id == layerId) {
                    map.removeLayer(markersAdd[i].vectorLayer);
                    markersAdd.splice(i, 1);
                }
            }
        },
		
		/**
		 * Busca um marker pelo id
		 */				
		findMarker : function(markerId){
			for (i in markersAdd) {
				if (markersAdd[i].marker.id == markerId) {
					return markersAdd[i].marker;
				}
			}
			return null;
		},
		
		/**
		 * Aproxima a área de visualização
		 */		
		zoomToArea : function(latitude, longitude) {
			var pan = ol.animation.pan({ source: view.getCenter() });
			var coordinates = ol.proj.transform([longitude, latitude], 'EPSG:4326', 'EPSG:3857');
			map.beforeRender(pan);
			view.setCenter(coordinates);
			view.setZoom(18);
		},

		/**
		 * Mudança de estados
		 */	
		changeToActionState : function(){
			$("#action-state").show();
			$("#add-state").hide();
		},
		
		changeToAddState : function(){
			$("#action-state").hide();
			$("#add-state").show();
		},		
		
		changeToSaveState : function(){
			var coordinates = new ol.format.WKT().writeGeometry( new ol.geom.Point(map.getView().getCenter()) );
			nativeInterface.changeToAddMarker(coordinates);
		},

		hideStates : function(){
			$("#action-state").hide();
			$("#add-state").hide();
		},
        
        initialize_i18n : function(messages, locale){
            if ( i18n )
                return;
            
            var tokens = messages.split(";");
            
            $.each(tokens, function( index, value ) {
            	var values = value.split(":");
            	$( ".i18n."+values[0] ).replaceWith( values[1] );
            });
            
            if ( locale != 'pt' && locale != 'en' ){
                locale = 'pt';
            }
            
            $(".i18n.image").each(function(){
                var newSrc = $(this).attr('src') + locale.toLowerCase() + ".png";
            	$(this).attr('src', newSrc);
        	});
            
            i18n = true;
        }
	
	};

}();