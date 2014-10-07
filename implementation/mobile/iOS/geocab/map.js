$(document).ready(function(){
	var layer = new ol.layer.Image({
		source: new ol.source.ImageWMS({
			url: 'http://172.17.6.112/geoserver/bdgeo/wms',
			params: {'LAYERS': 'v_programa_239_zoneamento_faixa_protecao'},
			serverType: 'geoserver'
		})
	 });
                  
	var iconFeature = new ol.Feature({
		geometry: new ol.geom.Point([-25.515946, -54.585558]),
		name: 'Null Island',
		population: 4000,
		rainfall: 500
	});

	var iconStyle = new ol.style.Style({
		image: new ol.style.Icon(/** @type {olx.style.IconOptions} */ ({
			anchor: [0.5, 46],
			anchorXUnits: 'fraction',
			anchorYUnits: 'pixels',
			//size: [20, 20],
			scale: 0.2,
			src: 'http://icons.iconarchive.com/icons/icons-land/vista-map-markers/256/Map-Marker-Marker-Outside-Chartreuse-icon.png'
		}))
	});

	iconFeature.setStyle(iconStyle);

	var vectorSource = new ol.source.Vector({
		features: [iconFeature]
	});

	var vectorLayer = new ol.layer.Vector({
		source: vectorSource
	});

	var map = new ol.Map({
		target: 'map',
		layers: [
			new ol.layer.Tile({
							  //source: new ol.source.MapQuest({layer: 'sat'})
							  source: new ol.source.OSM()
							  })
		, vectorLayer],
		view: new ol.View({
			center: ol.proj.transform([37.41, 8.82], 'EPSG:4326', 'EPSG:3857'),
			zoom: 4
		}),
                                       
    });
                  
    map.getView().setCenter(ol.projection.transform(new ol.Coordinate(-111, 46), 'EPSG:4326', 'EPSG:3857'));
    map.addLayer(layer);
    
    function addPointToMap (lat, lon) {
    	window.alert('aqui');
    	var newPoint = new ol.Feature({
			geometry: new ol.geom.Point([lat, lon]),
			name: 'Null Island',
			population: 4000,
			rainfall: 500
		});
		newPoint.setStyle(iconStyle);
		
		var pointLayer = ol.layer.Vector({
				source: [ol.source.Vector({
				features: [newPoint]
			})]
		});
		
		map.addLayer(pointLayer);
    }
})
