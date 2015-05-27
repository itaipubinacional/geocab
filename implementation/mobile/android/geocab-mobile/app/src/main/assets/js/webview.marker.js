
var webviewApp = function(){

	 var layersAdd = [];
     var markersAdd = [];
	 
	function showLayer(url, name, title, checked) {
		if (checked == 'true') {
			var wmsSource = new ol.source.TileWMS({
				url: url,
				params: {
					'LAYERS': name
				},
			});

			var wmsLayer = new ol.layer.Tile({
				source: wmsSource
			});

			map.addLayer(wmsLayer);

			layersAdd.push({
				'wmsLayer': wmsLayer,
				'wmsSource': wmsSource,
				'name': name,
				'title': title
			});
		} else {
			for (i in layersAdd) {
				if (layersAdd[i].name == name) {
					map.removeLayer(layersAdd[i].wmsLayer);
					layersAdd.splice(i, 1);
				}
			}
		}
	}

		function showMarker(wktCoordenate, markerId, markerUser, markerDate, name, icon) {

            console.log(wktCoordenate);

            var layerIcon = "file:///android_res/drawable/"+icon+".png";

            var iconFeature = new ol.Feature({
                geometry: new ol.format.WKT().readGeometry(wktCoordenate),
                layerName: name,
                markerId: markerId,
                markerUser: markerUser,
                markerDate: markerDate
            });

            var iconStyle = new ol.style.Style({
                image: new ol.style.Icon( /** @type {olx.style.IconOptions} */ ({
                    size: [32, 37],
                    src: 'file:///android_res/drawable/'+icon+'.png'
                }))
            });

            var vectorSource = new ol.source.Vector({
                features: [iconFeature]
            });

            //add the feature vector to the layer vector, and apply a style to whole layer
            var vectorLayer = new ol.layer.Vector({
                source: vectorSource,
                style: iconStyle
            });

            map.addLayer(vectorLayer);

            markersAdd.push({
                'vectorLayer': vectorLayer,
                'name': name
            });

        }	


};