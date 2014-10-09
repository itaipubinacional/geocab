<!DOCTYPE html>
<html>

<span id="marker-point" class="glyphicon glyphicon-map-marker sidebar-icon" style="display: none;"></span>

<section id="main-content" style="height: 100%">
    <div class="menu-sidebar-container" ng-mouseover="hideMousePosition()">

        <div>
            <ul class="map-menu-items tool-items" id="menu-sidebar">
                <li ng-click="aumentarZoom()">
                    <a href="#tabs-2">
                        <div class="icon itaipu-icon-plus sidebar-icon"></div>
                    </a>
                </li>
                <li ng-click="diminuirZoom()">
                    <a>
                        <div class="icon itaipu-icon-minus sidebar-icon"></div>
                    </a>
                </li>
                <li ng-if="hasPermissionCalculoDistancia" ng-click="initializeDistanceCalc()" ng-class="{ferramenta_active : menu.fcDistancia}">
                    <a>
                        <div class="icon itaipu-icon-ruler-1 sidebar-icon"></div>
                    </a>
                </li>
                <li ng-if="hasPermissionCalculoArea" ng-click="initializeAreaCalc()" ng-class="{ferramenta_active : menu.fcArea}">
                    <a>
                        <div class="icon itaipu-icon-square sidebar-icon"></div>
                    </a>
                </li>

                <!-- Verificar... -->
                <li ng-click="initializeMarker()" ng-class="{ferramenta_active : menu.fcMarker}">
                    <a href="#tabs-1">
                        <span class="glyphicon glyphicon-map-marker sidebar-icon"></span>
                    </a>
                </li>

                <li ng-if="hasPermissionKML" ng-click="">
                    <a>
                        <div class="icon itaipu-icon-kml sidebar-icon"></div>
                    </a>
                </li>
            </ul>
        </div>

		<div id="sidebar-marker-detail" class="sidebar-style">
		  	<form name="sidebarMarker">
		  		<div>
	               <div class="sidebar-coloredbar"></div>
	               <span ng-click="toggleSidebarMarkerDetail(300, 'closeButton');" class="icon itaipu-icon-close sidebar-close"></span>
					<div id="tabs-2" ng-switch="LAYER_MENU_STATE" class="container">
	                    <span style="float: left; margin-top: 12px; font-weight: bold; font-size: 18px;">{{ markerResultDetail.header.title }}</span>
                        <br style="clear: both;">
                        <br>
                        <span style="float: left">{{ markerResultDetail.header.layer }}</span> <span style="float: right">{{ markerResultDetail.header.date }}</span>
                        <hr>
                       
                       <button style="float: right;" class="btn btn-default" ng-click="removeMarker()"><i class="itaipu-icon-delete"></i></button>
                       <button style="float: right; margin-right: 5px" class="btn btn-default"><i class="itaipu-icon-edit"></i></button>
                       <br>
                       <img src="" style="width: 100%; height: 200px; margin-top: 12px;">
                       <br><br>
                      	
                       <p>Lorem ipsum dolor sit amet, consectetur adipiscing elit. Pellentesque sit amet urna eu nulla lacinia convallis. Morbi at gravida ligula, at sagittis quam</p>
                      	
                       <!-- <label>Foto</label> <input type="file" class="form-control" ng-model="currentEntity.photo"> -->
                       <!-- <label>Descrição</label> <textarea ng-model="currentEntity.description" class="form-control" style="height: 100px"></textarea> -->

	                </div>
                </div>
           	 </form>
           </div>

		  <div id="sidebar-marker-create" class="sidebar-style">
		  	<form name="sidebarMarker">
		  		<div>
	               <div class="sidebar-coloredbar"></div>
	               <span ng-click="clearFcMaker()" class="icon itaipu-icon-close sidebar-close"></span>
	
					<div id="tabs-2" ng-switch="LAYER_MENU_STATE" class="container">
	                   <div class="sidebar-content-header">Nova postagem</div>
                        <br style="clear: both;">
                        <br>
                       <label>Camada</label>
                       <select ng-model="currentEntity.layer" chosen class="form-control" ng-change="listAttributesByLayer(currentEntity.layer)">                       	
						  <optgroup ng-repeat="group in layersGroups" label="{{ group.name }}">
						    <option ng-repeat="layer in group.layers" value="{{ layer.id  }}">{{ layer.title }}</option>	    
						  </optgroup>
						</select>
                       <br>
                       <div ng-repeat="attribute in attributesByLayer">
                       		 <label>{{ attribute.name }}</label> 
                       		
                       		 <input type="number" ng-show="attribute.type == 'NUMBER'" class="form-control" ng-model="attribute.value">
                       		 <input type="date" ng-show="attribute.type == 'DATE'" class="form-control" ng-model="attribute.value">
                       		 
                       		 <div ng-show="attribute.type == 'BOOLEAN'">
                       		 		
                       		  		<input type="radio" name="boolean" ng-model="attribute.value" value="Yes">
                       		  		<input type="radio" name="boolean" ng-model="attribute.value" value="No">
                       		 </div>
                       		
                       		 <input type="text" ng-show="attribute.type == 'TEXT'" class="form-control" ng-model="attribute.value">
                       </div>
                      
                       <!-- <label>Foto</label> <input type="file" class="form-control" ng-model="currentEntity.photo"> -->
                       <!-- <label>Descrição</label> <textarea ng-model="currentEntity.description" class="form-control" style="height: 100px"></textarea> -->

    					<br>
    					<hr>
                       <button class="btn btn-default" ng-click="clearFcMaker()" style="float: left;"><span class="glyphicon glyphicon-picture"></span></button>
                       <button class="btn btn-primary" ng-click="insertMarker()" style="float: right">Enviar</button>
	                </div>
                </div>
           	 </form>
           </div>

        <div id="sidebar-tabs" style="float: left;">
            <ul class="map-menu-items tab-flag" id="menu-sidebar-2">
                <li id="menu-item-1" ng-click="toggleSidebarLayers(300, '#menu-item-1');" class="menu-item bg-inactive">
                    <a href="#tabs-1">
                        <div class="icon itaipu-icon-layers sidebar-icon"></div>
                    </a>
                </li>
            </ul>

            <div id="sidebar-layers" class="sidebar-style">
                <div class="sidebar-coloredbar"></div>
                <span ng-click="toggleSidebarLayers(300, 'closeButton')" class="icon itaipu-icon-close sidebar-close"></span>

                <div id="tabs-1" ng-switch="LAYER_MENU_STATE">
                    <div ng-switch-when="list">
                        <div id="layer-list">
                            <div>
                                <div class="sidebar-content-header">Camadas</div>
                                <br style="clear: both;">
                                <div class="form-item-horizontal radio" style="margin-left: 0;margin-top: 40px">
                                <input type="radio" id="osm"
                                ng-model="mapConf.type"
                                value="osm"
                                ng-click="initializeOSM()">
                                <label class="radio-label" for="osm"> Open Street View </label>
                            </div>
                            <br/>
                            <div class="form-item-horizontal radio" style="margin-left: 0;">
                                <input type="radio" id="googleMap"
                                ng-model="mapConf.type"
                                value="gmap"
                                ng-click="initializeGMAP()">
                                <label class="radio-label" for="googleMap"> Google Maps </label>
                            </div>
                            <br/>
                            <div class="form-item-horizontal radio" style="margin-left: 0;">
                            <input type="radio" id="mapQuest"
                                ng-model="mapConf.type"
                                value="mapQuest"
                                ng-click="initializeMapQuestOSM()">
                                <label class="radio-label" for="mapQuest"> MapQuest </label>
                            </div>
                        </div>
                    </div>

                <div style="overflow-x: auto;position: absolute;top: 210px;bottom: 0px;left: 20px;right: 0px;">

                <div ng-show="allLayers.length > 0">
                    <input type="text" ng-model="bagSearch" placeholder="Grupo ou layer" class="sidebar-content-search form-control"/>
                </div>

                <div id="tree"
                    ivh-treeview="allLayers"
                    ivh-fn="getSelectedNode"
                    ivh-treeview-label-attribute="'label'"
                    ivh-treeview-legend-attribute="'legenda'"
                    ivh-treeview-children-attribute="'children'"
                    ivh-treeview-filter="filter:bagSearch">
                </div>


                <br/>
            </div>
        </div>

        <div id="layer-legend-detail" ng-switch-when="legend_detail">
            <div class="sidebar-content-header" ng-click="exitLegendDetail()" style="cursor: pointer;">
                <span style="font-size: 17px;">&#x2190;</span>
                    Camadas
            </div>
            <br style="clear: both;">
            <div class="legend-detail-title">{{legendDetailTitle}}</div>
                <hr>
            <div class="legend-image-container">
                <img ng-src="{{legendDetailImage}}" style="padding: 10px;">
            </div>
        </div>

        </div>
    </div>
        </div>
    </div>
    <!-- Google Maps -->
    <div id="gmap" style="width: 0; height: 0"></div>

    <div id="typeMapQuest" ng-if="mapa.ativo == 'mapQuest_osm' || mapa.ativo == 'mapQuest_sat'" ng-mouseover="hideMousePosition()" style="position:absolute; top: 130px; right: 10px; z-index: 1;">
        <button type="button" ng-click="initializeMapQuestOSM()" class="btn btn-default btn-xs">OSM</button>
        <button type="button" ng-click="initializeMapQuestSAT()" class="btn btn-default btn-xs">SAT</button>
    </div>

    <!-- Openlayer Map -->
    <div id="olmap" style="position: relative; width:100%; height: 100%;top:0 !important">
        <div id="popup" class="ol-popup">
            <!--<a href="#" id="popup-closer" class="ol-popup-closer"></a>-->
            <div id="popup-content"></div>
        </div>
        <div id="info"></div>
    </div>
</section>

</html>
