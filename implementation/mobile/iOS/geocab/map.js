$(document).ready(function(){
                  var layer = new ol.layer.Image({
                                                 source: new ol.source.ImageWMS({
                                                                                url: 'http://172.17.6.112/geoserver/bdgeo/wms',
                                                                                params: {'LAYERS': 'v_programa_239_zoneamento_faixa_protecao'},
                                                                                serverType: 'geoserver'
                                                                                })
                                                 });
                  
                  var map = new ol.Map({
                                       target: 'map',
                                       layers: [
                                                new ol.layer.Tile({
                                                                  //source: new ol.source.MapQuest({layer: 'sat'})
                                                                  source: new ol.source.OSM()
                                                                  })
                                                ],
                                       view: new ol.View({
                                                         center: ol.proj.transform([37.41, 8.82], 'EPSG:4326', 'EPSG:3857'),
                                                         zoom: 4
                                                         }),
                                       
    });
                  
    map.getView().setCenter(ol.projection.transform(new ol.Coordinate(-111, 46), 'EPSG:4326', 'EPSG:3857'));
    map.addLayer(layer);
})
