<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>

<span id="marker-point"
	class="glyphicon glyphicon-map-marker sidebar-icon"
	style="display: none;"></span>

<section id="main-content" style="height: 100%">
	<!--Message -->
	<div class="msgMap"
		ng-include="'static/libs/eits-directives/alert/alert.html'"></div>

	<div class="menu-sidebar-container" ng-mouseover="hideMousePosition()">

		<div>
			<ul class="map-menu-items tool-items" id="menu-sidebar">
				<li ng-click="aumentarZoom()"><a href="#tabs-2">
						<div class="icon itaipu-icon-plus sidebar-icon"></div>
				</a></li>
				<li ng-click="diminuirZoom()"><a>
						<div class="icon itaipu-icon-minus sidebar-icon"></div>
				</a></li>
				<li ng-if="hasPermissionCalculoDistancia"
					ng-click="initializeDistanceCalc()"
					ng-class="{ferramenta_active : menu.fcDistancia}"><a>
						<div class="icon itaipu-icon-ruler-1 sidebar-icon"></div>
				</a></li>
				<li ng-if="hasPermissionCalculoArea" ng-click="initializeAreaCalc()"
					ng-class="{ferramenta_active : menu.fcArea}"><a>
						<div class="icon itaipu-icon-square sidebar-icon"></div>
				</a></li>

				<!-- Verificar... -->
				<li ng-click="initializeMarker()"
					ng-class="{ferramenta_active : menu.fcMarker}"><a
					href="#tabs-1"> <span
						class="glyphicon glyphicon-map-marker sidebar-icon"></span>
				</a></li>

				<li ng-if="hasPermissionKML" ng-click=""><a>
						<div class="icon itaipu-icon-kml sidebar-icon"></div>
				</a></li>
			</ul>
		</div>

		<div id="sidebar-marker-detail-update" class="sidebar-style"  >
		
			<form name="sidebarMarkerDetail" ng-show="screen == 'detail'">
				<div style="height:650px;">
					<div class="sidebar-coloredbar"></div>
					<span ng-click="toggleSidebarMarkerDetailUpdate(300, 'closeButton');"
						class="icon itaipu-icon-close sidebar-close"></span>
					<div id="tabs-2" ng-switch="LAYER_MENU_STATE" class="container">
						<span
							style="float: left; margin-top: 12px; font-weight: bold; font-size: 18px;">{{
							marker.layer.title }}</span> <br style="clear: both;"> <br> <span
							style="float: left">Criado por: <b>{{ marker.user.name
								}}</b></span> <span style="float: right">{{ marker.layer.created |
							date:'dd/MM/yyyy' }}</span>
						<hr>

						<button
							ng-if="(userMe.role == 'ADMINISTRATOR' || userMe.role == 'MODERADOR') || (marker.status == 'PENDING' && userMe.id == marker.user.id)"
							style="float: right;" class="btn btn-default"
							ng-click="removeMarker()">
							<i class="itaipu-icon-delete"></i>
						</button>
						<button
							
							ng-if="(userMe.role == 'ADMINISTRATOR' || userMe.role == 'MODERADOR') || (marker.status == 'PENDING' && userMe.id == marker.user.id)"
							style="float: right; margin-right: 5px" class="btn btn-default"
							ng-click="changeToScreen('update')"
							>
							<i class="itaipu-icon-edit"></i>
						</button>
						<button
							ng-if="(userMe.role == 'ADMINISTRATOR' || userMe.role == 'MODERADOR') && (marker.status == 'ACCEPTED' || marker.status == 'PENDING')"
							style="float: right; margin-right: 5px; color: red;"
							ng-click="disableMarker()" class="btn btn-default">
							<i class="glyphicon glyphicon-ban-circle"></i>
						</button>
						<button
							ng-if="(userMe.role == 'ADMINISTRATOR' || userMe.role == 'MODERADOR') && (marker.status == 'REFUSED' || marker.status == 'PENDING')"
							style="float: right; margin-right: 5px; color: #00981F"
							ng-click="enableMarker()" class="btn btn-default">
							<i class="glyphicon glyphicon-ok"></i>
						</button>
						<br> <img class="marker-image" ng-src="{{ imgResult }}"
							style="width: 100%; height: 200px; margin-top: 12px;"> <br>
						<br>

						<div style=" overflow: auto; height: 320px;">
							<div ng-repeat="markerAttribute in attributesByMarker" style="position: relative;margin-bottom:15px">
							
									<label>{{ markerAttribute.attribute.name }}</label> 
										<input
										type="number" name="number1"
										ng-if="markerAttribute.attribute.type == 'NUMBER'"
										class="form-control" ng-model="markerAttribute.value"									
										required ng-disabled="true"
										> 
																		
										<input type="date"
										name="date1" ng-if="markerAttribute.attribute.type == 'DATE'"
										class="form-control" ng-model="markerAttribute.value"									
										required 
										ng-disabled="true"
										>

									<div ng-if="markerAttribute.attribute.type == 'BOOLEAN'">
										<input ng-disabled="true" type="radio"
											ng-checked="markerAttribute.value == 'Yes'"
											ng-model="markerAttribute.value" value="Yes"> 
											
											<input
											ng-disabled="true" type="radio"
											ng-checked="markerAttribute.value == 'No'"
											ng-model="markerAttribute.value" value="No">
									</div>

									<input type="text"
										ng-if="markerAttribute.attribute.type == 'TEXT'" name="texto"
										class="form-control" ng-model="markerAttribute.value"
										required ng-disabled="true"> 
														
							</div>
						</div>
					</div>
				</div>
			</form>
			
			<form name="sidebarMarkerUpdate" method="post" ng-show="screen == 'update'"
				default-button="buttonInsert" novalidate >
				<div style="height:650px">
					<div class="sidebar-coloredbar"></div>
					<button
							ng-if="(userMe.role == 'ADMINISTRATOR' || userMe.role == 'MODERADOR') || (marker.status == 'PENDING' && userMe.id == marker.user.id)"
							style="float: left; margin: 5px 0 0 5px" class="btn btn-default"
							ng-click="changeToScreen('detail')">
							<
						</button>
						
					<span ng-click="toggleSidebarMarkerDetailUpdate(300, 'closeButton');"
						class="icon itaipu-icon-close sidebar-close"></span>

					<div id="tabs-2" ng-switch="LAYER_MENU_STATE" class="container" style="overflow:auto;height:100%">
						<div class="sidebar-content-header">Editar postagem</div>
						
						<br style="clear: both;"> <br> 
						<label>Camada</label>
						
						<div style="margin-bottom:5px;">
							<select ng-change="listAttributesByLayer()"
								data-placeholder="Selecione uma camada" name="camada"
								ng-options="layer.layerTitle group by layer.group for layer in selectLayerGroup"
								ng-model="currentEntity.layer" chosen class="form-control"
								ng-class="{ngInvalid: sidebarMarker.camada.$error.required && sidebarMarker.$submitted}"
								required>
								<option value=""></option>							
							</select> 
						</div>
						<span class="tooltip-validation"
							ng-show="sidebarMarker.$submitted && sidebarMarker.layer.$error.required"
							style="top: -20px">Campo Obrigatório</span> <br>

						
						<div ng-repeat="markerAttribute in attributesByMarker"
							style="position: relative;margin-bottom:15px;">

							<ng-form name="ngSideMarker" default-button="buttonUpdate">
							<label>{{ markerAttribute.attribute.name }}</label> <input
								type="number" name="number1"
								ng-if="markerAttribute.attribute.type == 'NUMBER'"
								class="form-control" ng-model="markerAttribute.value"
								ng-class="{ngInvalid: ngSideMarker.$submitted && ngSideMarker.number1.$error.required}"
								ng-required="markerAttribute.required"
								> 
								
								<input
								type="date" name="date1"
								ng-if="markerAttribute.attribute.type == 'DATE'"
								class="form-control" ng-model="markerAttribute.value"
								ng-class="{ngInvalid: ngSideMarker.$submitted && ngSideMarker.date1.$error.required}"
								required
								ng-required="markerAttribute.required"
								>								

							<div ng-if="markerAttribute.attribute.type == 'BOOLEAN'">

								<input type="radio" ng-checked="markerAttribute.value == 'Yes'"
									ng-model="markerAttribute.value" value="Yes"> 
									
									<input
									type="radio" ng-checked="markerAttribute.value == 'No'"
									ng-model="markerAttribute.value" value="No">
							</div>

							<input type="text"
								ng-if="markerAttribute.attribute.type == 'TEXT'" name="texto"
								class="form-control" ng-model="markerAttribute.value"
								ng-class="{ ngInvalid: ngSideMarker.$submitted && ngSideMarker.texto.$error.required }"
								ng-required="markerAttribute.required"
								> 
								
								<span class="tooltip-validation"
									ng-show="  (ngSideMarker.texto.$error.required && ngSideMarker.$submitted)"
									style="top: 3px"> Campo Obrigatório
								</span> 
								
								<span
									class="tooltip-validation"
									ng-show="  (ngSideMarker.number1.$error.required && ngSideMarker.$submitted)"
									style="top: 3px" >Campo Obrigatório
								</span> 
								
								<span
									class="tooltip-validation"
									ng-show="!(ngSideMarker.number1.$error.required && ngSideMarker.$submitted) && (ngSideMarker.number1.$error.number)"
									style="top: 3px">Campo Obrigatório
								</span> 
								
								<span
									class="tooltip-validation"
									ng-show=" (ngSideMarker.date1.$error.required && ngSideMarker.$submitted)"
									style="top: 3px">Campo Obrigatório
								</span> 
							<ng-form>
						
						</div>

						<!-- <label>Foto</label> <input type="file" class="form-control" ng-model="currentEntity.photo"> -->
						<!-- <label>Descrição</label> <textarea ng-model="currentEntity.description" class="form-control" style="height: 100px"></textarea> -->

						<br>
						 <img class="marker-image" ng-src="{{ imgResult }}"
							style="width: 100%; height: 200px; margin-top: 12px;"> <br>
						<br>
						
						<hr>

						<input type="file" id="upload-input" style="display: none;"
							accept="image/*"
							onchange="angular.element(this).scope().setPhotoMarker(this)" />

						<button class="btn btn-default"
							onclick="angular.element('#upload-input').click();"
							style="float: left;">
							<span class="glyphicon glyphicon-picture"></span>
						</button>

						<button id="buttonUpdate" class="btn btn-primary"
							ng-click="updateMarker()" style="float: right">Enviar</button>
					</div>
				</div>
			</form>
		</div>

		<div id="sidebar-marker-create" class="sidebar-style">
			<form name="sidebarMarker" method="post"
				default-button="buttonInsert" novalidate>
				<div>
					<div class="sidebar-coloredbar"></div>
					<span ng-click="clearFcMarker()"
						class="icon itaipu-icon-close sidebar-close"></span>

					<div id="tabs-2" ng-switch="LAYER_MENU_STATE" class="container">
						<div class="sidebar-content-header">Nova postagem</div>
						<br style="clear: both;"> <br> <label>Camada</label>

						<!-- no-results-text="Nenhum registro encontrado com" -->

						<select ng-change="listAttributesByLayer()"
							data-placeholder="Selecione uma camada" name="camada"
							ng-options="layer.layerTitle group by layer.group for layer in selectLayerGroup"
							ng-model="currentEntity.layer" chosen class="form-control"
							ng-class="{ngInvalid: sidebarMarker.camada.$error.required }"
							required				
							>
							<option value=""></option>
						</select> 
						
						<span class="tooltip-validation"
							ng-show="sidebarMarker.$submitted && sidebarMarker.layer.$error.required"
							style="top: -20px">Campo Obrigatório</span> <br>

						<div ng-repeat="attribute in attributesByLayer"
							style="position: relative">
							
							<ng-form name="ngSideMarker" default-button="buttonInsert">
							<label>{{ attribute.name }}</label> 
							
							<input type="number"
								name="number1" ng-if="attribute.type == 'NUMBER'"
								class="form-control" ng-model="attribute.value"
								ng-class="{ngInvalid:ngSideMarker.$submitted && ngSideMarker.number1.$error.required}"
								ng-required="attribute.required"
								> 
								
							<input
								type="date" name="date1" ng-if="attribute.type == 'DATE'"
								class="form-control" ng-model="attribute.value"
								ng-class="{ngInvalid: ngSideMarker.$submitted && ngSideMarker.date1.$error.required}"
								ng-required="attribute.required"
								>

							<div ng-if="attribute.type == 'BOOLEAN'">

								<input type="radio" name="boolean" ng-model="attribute.value"
									value="Yes"> 
									
								<input type="radio" name="boolean"
									ng-model="attribute.value" value="No">
							</div>

							<input type="text" ng-if="attribute.type == 'TEXT'" name="texto"
								class="form-control" ng-model="attribute.value"
								ng-class="{ ngInvalid: ngSideMarker.$submitted && ngSideMarker.texto.$error.required }"
								ng-required="attribute.required"
								> 
								
								<span
								class="tooltip-validation"
								ng-show=" (ngSideMarker.texto.$error.required && ngSideMarker.$submitted) "
								style="top: 3px">Campo Obrigatório
								</span> 
								
								<span
								class="tooltip-validation"
								ng-show="  (ngSideMarker.number1.$error.required && ngSideMarker.$submitted)"
								style="top: 3px">Campo Obrigatório
								</span> 
								
								<span
								class="tooltip-validation"
								ng-show="!(ngSideMarker.number1.$error.required && ngSideMarker.$submitted) && (ngSideMarker.number1.$error.number)"
								style="top: 3px">Campo Obrigatório
								</span> 
								
								<span
								class="tooltip-validation"
								ng-show=" (ngSideMarker.date1.$error.required && ngSideMarker.$submitted)"
								style="top: 3px">Campo Obrigatório
								</span> 
								
								<ng-form>
						</div>


						<!-- <label>Foto</label> <input type="file" class="form-control" ng-model="currentEntity.photo"> -->
						<!-- <label>Descrição</label> <textarea ng-model="currentEntity.description" class="form-control" style="height: 100px"></textarea> -->

						<img class="marker-image"> <br>
						<hr>



						<input type="file" id="upload-input" style="display: none;"
							accept="image/*"
							onchange="angular.element(this).scope().setPhotoMarker(this)" />

						<button class="btn btn-default"
							onclick="angular.element('#upload-input').click();"
							style="float: left;">
							<span class="glyphicon glyphicon-picture"></span>
						</button>

						<button id="buttonInsert" class="btn btn-primary"
							ng-click="insertMarker()" style="float: right">Enviar</button>
					</div>
				</div>
			</form>
		</div>
		<div id="sidebar-tabs" style="float: left;">
			<ul class="map-menu-items tab-flag" id="menu-sidebar-2">
				<li id="menu-item-1"
					ng-click="toggleSidebarMenu(300, '#menu-item-1');"
					class="menu-item bg-inactive"><a href="#tabs-1">
						<div class="icon itaipu-icon-layers sidebar-icon"></div>
				</a></li>
			</ul>

			<div id="sidebar-layers" class="sidebar-style">
				<div class="sidebar-coloredbar"></div>
				<span ng-click="toggleSidebarMenu(300, 'closeButton')"
					class="icon itaipu-icon-close sidebar-close"></span>

				<div id="tabs-1" ng-switch="LAYER_MENU_STATE">
					<div ng-switch-when="list">
						<div id="layer-list">
							<div>
								<div class="sidebar-content-header">Camadas</div>
								<br style="clear: both;">
								<div class="form-item-horizontal radio"
									style="margin-left: 0; margin-top: 40px">
									<input type="radio" id="osm" ng-model="mapConf.type"
										value="osm" ng-click="initializeOSM()"> <label
										class="radio-label" for="osm"> Open Street View </label>
								</div>
								<br />
								<div class="form-item-horizontal radio" style="margin-left: 0;">
									<input type="radio" id="googleMap" ng-model="mapConf.type"
										value="gmap" ng-click="initializeGMAP()"> <label
										class="radio-label" for="googleMap"> Google Maps </label>
								</div>
								<br />
								<div class="form-item-horizontal radio" style="margin-left: 0;">
									<input type="radio" id="mapQuest" ng-model="mapConf.type"
										value="mapQuest" ng-click="initializeMapQuestOSM()"> <label
										class="radio-label" for="mapQuest"> MapQuest </label>
								</div>
							</div>
						</div>

						<div
							style="overflow-x: auto; position: absolute; top: 210px; bottom: 0px; left: 20px; right: 0px;">

							<div ng-show="allLayers.length > 0">
								<input type="text" ng-model="bagSearch"
									placeholder="Grupo ou layer"
									class="sidebar-content-search form-control" />
							</div>

							<div id="tree" ivh-treeview="allLayers" ivh-fn="getSelectedNode"
								ivh-treeview-label-attribute="'label'"
								ivh-treeview-legend-attribute="'legenda'"
								ivh-treeview-children-attribute="'children'"
								ivh-treeview-filter="filter:bagSearch"></div>


							<br />
						</div>
					</div>

					<div id="layer-legend-detail" ng-switch-when="legend_detail">
						<div class="sidebar-content-header" ng-click="exitLegendDetail()"
							style="cursor: pointer;">
							<span style="font-size: 17px;">&#x2190;</span> Camadas
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

	<div id="typeMapQuest"
		ng-if="mapa.ativo == 'mapQuest_osm' || mapa.ativo == 'mapQuest_sat'"
		ng-mouseover="hideMousePosition()"
		style="position: absolute; top: 130px; right: 10px; z-index: 1;">
		<button type="button" ng-click="initializeMapQuestOSM()"
			class="btn btn-default btn-xs">OSM</button>
		<button type="button" ng-click="initializeMapQuestSAT()"
			class="btn btn-default btn-xs">SAT</button>
	</div>

	<!-- Openlayer Map -->
	<div id="olmap"
		style="position: relative; width: 100%; height: 100%; top: 0 !important">
		<div id="popup" class="ol-popup">
			<!--<a href="#" id="popup-closer" class="ol-popup-closer"></a>-->
			<div id="popup-content"></div>
		</div>
		<div id="info"></div>
	</div>
</section>

</html>


