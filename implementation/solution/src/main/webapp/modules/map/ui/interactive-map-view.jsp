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
			<ul class="map-menu-items tool-items" id="menu-sidebar" style="padding:3px">
				<li ng-click="aumentarZoom()" title="<spring:message code="map.Zoom-in" />"><a href="#tabs-2">
						<div class="icon itaipu-icon-plus sidebar-icon"></div>
				</a></li>
				<li ng-click="diminuirZoom()" title="<spring:message code="map.Zoom-out" />"><a>
						<div class="icon itaipu-icon-minus sidebar-icon"></div>
				</a></li>
				<li ng-if="hasPermissionCalculoDistancia"  
					title="<spring:message code="map.Calculate-distance" />"
					ng-click="initializeDistanceCalc()"
					ng-class="{ferramenta_active : menu.fcDistancia}"><a>
						<div class="icon itaipu-icon-ruler-1 sidebar-icon"></div>
				</a></li>
				<li ng-if="hasPermissionCalculoArea" ng-click="initializeAreaCalc()"
					title="<spring:message code="map.Calculate-area" />"
					ng-class="{ferramenta_active : menu.fcArea}"><a>
						<div class="icon itaipu-icon-square sidebar-icon"></div>
				</a></li>

				<!-- Verificar... -->
				<li ng-click="initializeMarker()"
					title="<spring:message code="map.Perform-post" />"
					ng-class="{ferramenta_active : menu.fcMarker}"><a
					href="#tabs-1"> <span
						class="glyphicon glyphicon-map-marker sidebar-icon"></span>
				</a></li>

				<!--  <li ng-if="hasPermissionKML" ng-click=""><a>
						<div class="icon itaipu-icon-kml sidebar-icon"></div>
				</a></li>-->
			</ul>
		</div>

		<div id="sidebar-marker-detail-update" class="sidebar-style rui-resizable-left resizable-test-block" style="min-width: 384px" >

			<div class='rui-resizable-content' style="position: static;">
				<form name="sidebarMarkerDetail" ng-show="screen == 'detail'">
	
					<div style="position: absolute; top:0; right:0; left:0; bottom:0">
						<!-- <div class="sidebar-coloredbar"></div> -->
						<div style="height: 35px">
							<span ng-click="toggleSidebarMarkerDetailUpdate(300, 'closeButton');"
										class="icon itaipu-icon-close sidebar-close"
										title="<spring:message code="map.Close" />"
										></span>
							</div>			
						<accordion close-others="true" id="accordion-markers" class="accordion-popup accordion-caret">
				            <accordion-group ng-repeat="feature in features track by $index" ng-init="isOpen = $index == 0" is-open="isOpen" ng-class="{'min-height-accordion': feature.type == 'internal' }"> 
				            
				                <accordion-heading>
				                    <div style="cursor:pointer; padding: 10px 0;">
				                    	<i class="pull-left" ng-class="{'icon-chevron-down': isOpen, 'icon-chevron-right': !isOpen}"></i>
				                        <span ng-if="feature.type == 'internal'" ng-click="calculo()" >{{feature.feature.layer.title}} </span>
				                        <span ng-if="feature.type == 'external'">{{feature.feature.layer.titulo}}</span>
				                        
				                    </div>
				                </accordion-heading>
				                
				                
				                <span ng-if="feature.type == 'internal'">
					                
									<div id="tabs-2" ng-switch="LAYER_MENU_STATE" class="container" style="height: 100%; width: 100%; padding: 0;">
										
										<span
											style="float: left; margin-top: 12px; font-weight: bold; font-size: 18px;">{{
											marker.layer.title }}</span> <br style="clear: both;"> <br> <span
											style="float: left"><spring:message code="map.Created-by"/>: <b>{{ marker.user.name
												}}</b></span> <span style="float: right">{{ marker.created |
											date:'dd/MM/yyyy' }}</span>
										<hr>
				
										<button
											ng-if="(userMe.role == 'ADMINISTRATOR' || userMe.role == 'MODERATOR') || (marker.status == 'PENDING' && userMe.id == marker.user.id)"
											style="float: right;" 
											class="btn btn-default"
											title="<spring:message code="map.Delete"/>"
											ng-click="removeMarker()">
											<i class="itaipu-icon-delete"></i>
										</button>
										<button		
											ng-if="(userMe.role == 'ADMINISTRATOR' || userMe.role == 'MODERATOR') || (marker.status == 'PENDING' && userMe.id == marker.user.id)"
											style="float: right; margin-right: 5px" class="btn btn-default"
											ng-click="changeToScreen('update')"
											title="<spring:message code="map.Update"/>"
											>
											<i class="itaipu-icon-edit"></i>
										</button>
										<button
											ng-if="(userMe.role == 'ADMINISTRATOR' || userMe.role == 'MODERATOR') && (marker.status == 'ACCEPTED' || marker.status == 'PENDING')"
											style="float: right; margin-right: 5px; color: red;"
											ng-click="disableMarker()" class="btn btn-default"
											title="<spring:message code="map.Disable"/>"
											>
											<i class="glyphicon glyphicon-ban-circle"></i>
										</button>
										<button
											ng-if="(userMe.role == 'ADMINISTRATOR' || userMe.role == 'MODERATOR') && (marker.status == 'REFUSED' || marker.status == 'PENDING')"
											style="float: right; margin-right: 5px; color: #00981F"
											ng-click="enableMarker()" 
											class="btn btn-default"
											title="<spring:message code="map.Enable"/>"
											>
											<i class="glyphicon glyphicon-ok"></i>
										</button>
										<br> 
										<div style="text-align:center">
											<img ng-click="openImgModal()" ng-show="imgResult" class="marker-image" ng-src="{{ imgResult }}"
												style="width: 100%; height: 200px; margin-top: 12px; cursor: pointer;max-width:360px"> <br>
										</div>
										<br>
				
										<div style=" overflow: auto;">
											<div ng-repeat="markerAttribute in attributesByMarker track by $index" style="position: relative;margin-bottom:15px">
											
													<label ng-style="$index > 0 ? {'margin-top':'15px'} : '' " ng-if="!markerAttribute.value == ''">{{ markerAttribute.attribute.name }}</label> 
														<input
														type="number" name="number1"
														ng-if="markerAttribute.attribute.type == 'NUMBER' && !markerAttribute.value == '' "
														class="form-control" ng-model="markerAttribute.value"									
														required ng-disabled="true"
														> 
																						
														<input 
														name="date1" 
														ng-if="markerAttribute.attribute.type == 'DATE' && !markerAttribute.value == ''"
														class="form-control datepicker" ng-model="markerAttribute.value"									
														required 
														ng-disabled="true"
														>
				
													<div ng-if="markerAttribute.attribute.type == 'BOOLEAN' && !markerAttribute.value == ''">
														<input 
															ng-disabled="true" type="radio"
															ng-checked="markerAttribute.value == 'Yes'"
															ng-model="markerAttribute.value" 
															value="Yes"
															>
															<spring:message code="map.Yes" /> 
															
														<input
															ng-disabled="true" type="radio"
															ng-checked="markerAttribute.value == 'No'"
															ng-model="markerAttribute.value" 
															value="No"
															>
															<spring:message code="map.No" />
													</div>
				
													<input type="text"
														ng-if="markerAttribute.attribute.type == 'TEXT' && !markerAttribute.value == ''" 
														name="texto"
														class="form-control" ng-model="markerAttribute.value"
														required ng-disabled="true"> 
																		
											</div>
										</div>
									</div>
				               </span>
				               
				               <span ng-if="feature.type == 'external'">
					               <span ng-repeat="(key, value) in feature.feature.fields" >
					                   <b>{{key}}</b> - {{value}}
					                   </br>
					               </span>
				               </span>
				            </accordion-group>
				
				        </accordion>
	        
					</div>
				</form>
				
				<form name="sidebarMarkerUpdate" method="post" ng-show="screen == 'update'"
					default-button="buttonInsert" novalidate >
					
					<div style="position: absolute; top:0; right:0; left:0; bottom:0">
						<!-- <div class="sidebar-coloredbar"></div> -->
						<div>
							<button
								ng-if="(userMe.role == 'ADMINISTRATOR' || userMe.role == 'MODERATOR') || (marker.status == 'PENDING' && userMe.id == marker.user.id)"
								style="float: left; margin: 5px 0 0 5px" class="btn btn-default"
								ng-click="changeToScreen('detail')"
								title="<spring:message code="map.Back" />"						
								>							
								<
							</button>
								
							<span ng-click="toggleSidebarMarkerDetailUpdate(300, 'closeButton');"
								class="icon itaipu-icon-close sidebar-close"
								title="<spring:message code="map.Close" />"></span>
						</div>
						<div id="tabs-2" ng-switch="LAYER_MENU_STATE" class="container" style="overflow:auto;height:95%; width: 100%; padding: 10px;">
							<div class="sidebar-content-header"><spring:message code="map.Edit-post"/></div>
							
							<br style="clear: both;"> <br> 
							<label>Camada</label>
							<div style="margin-bottom:5px;">
								<select ng-change="listAttributesByLayerUpdate()"
									ng-disabled="selectLayerGroup"
									data-placeholder="Selecione uma camada" name="camada"
									ng-options="layer.layerTitle group by layer.group for layer in selectLayerGroup"
									ng-model="currentEntity.layer" chosen class="form-control"
									ng-class="{ngInvalid: sidebarMarker.camada.$error.required && sidebarMarker.$submitted}"
									>
									<option value=""></option>							
								</select> 
							</div>
							<span class="tooltip-validation"
								ng-show="sidebarMarker.$submitted && sidebarMarker.layer.$error.required"
								style="top: -20px"><spring:message code="map.Field-required"/></span> <br>
							
							<div ng-repeat="markerAttribute in attributesByMarker"
								ng-if="!showAttributesAlone"
								style="position: relative;margin-bottom:15px;">
	
								<ng-form name="ngSideMarker" default-button="buttonUpdate">
								<label  ng-style="$index > 0 ? {'margin-top':'15px'} : '' ">{{ markerAttribute.attribute.name }}</label> 
								
									<input
										type="number" 
										name="number1"
										ng-if="markerAttribute.attribute.type == 'NUMBER'"
										class="form-control"
										ng-model="markerAttribute.value"
										ng-class="{ngInvalid: ngSideMarker.$submitted && ngSideMarker.number1.$error.required}"
										ng-required="markerAttribute.attribute.required"
										> 
									
									<input								
										name="date1"
										ng-if="markerAttribute.attribute.type == 'DATE'"
										class="form-control datepicker" ng-model="markerAttribute.value"
										ng-class="{ngInvalid: ngSideMarker.$submitted && ngSideMarker.date1.$error.required}"
										required
										ng-required="markerAttribute.attribute.required"
										>								
	
								<div ng-if="markerAttribute.attribute.type == 'BOOLEAN'">
	
									<input 
										type="radio" 
										ng-checked="markerAttribute.value == 'Yes'"
										ng-model="markerAttribute.value" 
										value="Yes"
										>
										<spring:message code="map.Yes" /> 
										
									<input
										type="radio" 
										ng-checked="markerAttribute.value == 'No'"
										ng-model="markerAttribute.value" 
										value="No"
										>
										<spring:message code="map.No" />
										
								</div>
	
								<input type="text"
									ng-if="markerAttribute.attribute.type == 'TEXT'" name="texto"
									class="form-control" ng-model="markerAttribute.value"
									ng-class="{ ngInvalid: ngSideMarker.$submitted && ngSideMarker.texto.$error.required }"
									ng-required="markerAttribute.attribute.required"
									> 
									
									<span class="tooltip-validation"
										ng-show="  (ngSideMarker.texto.$error.required && ngSideMarker.$submitted)"
										style="top: 3px"><spring:message code="map.Field-required"/>
									</span> 
									
									<span
										class="tooltip-validation"
										ng-show="  (ngSideMarker.number1.$error.required && ngSideMarker.$submitted)"
										style="top: 3px" ><spring:message code="map.Field-required"/>
									</span> 
									
									<span
										class="tooltip-validation"
										ng-show="!(ngSideMarker.number1.$error.required && ngSideMarker.$submitted) && (ngSideMarker.number1.$error.number)"
										style="top: 3px"><spring:message code="map.Must-be-a-number"/>
									</span> 
									
									<span
										class="tooltip-validation"
										ng-show=" (ngSideMarker.date1.$error.required && ngSideMarker.$submitted)"
										style="top: 3px"><spring:message code="map.Field-required"/>
									</span> 
								<ng-form>
							
							</div>
	
							<div ng-repeat="attribute in attributesByLayer"
								ng-if="showAttributesAlone || showNewAttributes"
								style="position: relative">
								
								<ng-form name="ngSideMarker" default-button="buttonInsert">
								<label style="margin-top: 15px">{{ attribute.name }}</label> 
								
								<input type="number"
									name="number1" ng-if="attribute.type == 'NUMBER'"
									class="form-control" ng-model="attribute.value"
									ng-class="{ngInvalid:ngSideMarker.$submitted && ngSideMarker.number1.$error.required}"
									ng-required="attribute.required"
									> 
									
								<input
									name="date1" ng-if="attribute.type == 'DATE'"
									class="form-control datepicker" ng-model="attribute.value"
									ng-class="{ngInvalid: ngSideMarker.$submitted && ngSideMarker.date1.$error.required}"
									ng-required="attribute.required"
									>
	
<!-- 								<div ng-if="attribute.type == 'BOOLEAN'" id="radioBoolean" class="boolean-required" > -->
	
<!-- 									<input type="radio" name="boolean" ng-model="attribute.value" -->
<%-- 										value="Yes" ng-required="attribute.required" ><spring:message code="map.Yes" />  --%>
										
<!-- 									<input type="radio" name="boolean" ng-model="attribute.value"  -->
<%-- 										value="No" ng-required="attribute.required"><spring:message code="map.No" />  --%>
<!-- 								</div> -->
								
								<div ng-if="attribute.type == 'BOOLEAN'" ng-required="attribute.required"  >
									<div class="required-boolean" >
										<input type="radio" name="boolean{{ $index }}" class="boolean-1 boolean" ng-model="attribute.value"
											value="Yes" onClick="isBooleanChecked(this)" ><spring:message code="map.Yes" /> 
											
										<input type="radio" name="boolean{{ $index }}" class="boolean-2 boolean" ng-model="attribute.value" 
											value="No" onClick="isBooleanChecked(this)"><spring:message code="map.No" /> 
									</div>
								</div>
	
								<input type="text" ng-if="attribute.type == 'TEXT'" name="texto"
									class="form-control" ng-model="attribute.value"
									ng-class="{ ngInvalid: ngSideMarker.$submitted && ngSideMarker.texto.$error.required }"
									ng-required="attribute.required"
									> 
									
									<span
									class="tooltip-validation"
									ng-show=" (ngSideMarker.texto.$error.required && ngSideMarker.$submitted) "
									style="top: 3px"><spring:message code="map.Field-required"/>
									</span> 
									
									<span
									class="tooltip-validation"
									ng-if=" (ngSideMarker.texto.$error.required && ngSideMarker.$submitted) "
									style="top: 3px"><spring:message code="map.Field-required"/>
									</span> 								
									
									<span
									class="tooltip-validation"
									ng-show="  (ngSideMarker.number1.$error.required && ngSideMarker.$submitted)"
									style="top: 3px"><spring:message code="map.Field-required"/>
									</span> 
									
									<span
									class="tooltip-validation"
									ng-show="!(ngSideMarker.number1.$error.required && ngSideMarker.$submitted) && (ngSideMarker.number1.$error.number)"
									style="top: 3px"><spring:message code="map.Must-be-a-number"/>
									</span> 
									
									<span
									class="tooltip-validation"
									ng-show=" (ngSideMarker.date1.$error.required && ngSideMarker.$submitted)"
									style="top: 3px"><spring:message code="map.Field-required"/>
									</span> 
									
									<ng-form>
							</div>
							<!-- <label>Foto</label> <input type="file" class="form-control" ng-model="currentEntity.photo"> -->
							<!-- <label>Descrição</label> <textarea ng-model="currentEntity.description" class="form-control" style="height: 100px"></textarea> -->
	
							<br>
							 <img  ng-show="imgResult" class="marker-image" ng-src="{{ imgResult }}"
								style="width: 100%; height: 200px; margin-top: 12px;"> <br>
							<br>
							
							<hr>
	
							<input type="file" id="upload-input" style="display: none;"
								accept="image/*"
								onchange="angular.element(this).scope().setPhotoMarker(this)" />
	
							<button class="btn btn-default"
								onclick="angular.element('#upload-input').click();"
								style="float: left;"
								title="<spring:message code="map.Picture"/>"
								>
								<span class="glyphicon glyphicon-picture"></span>
							</button>
	
							<button 
								id="buttonUpdate"
						   		class="btn btn-primary"
								ng-click="updateMarker()" 
								style="float: right"
								title="<spring:message code="map.Submit" />"
								>
								
								<spring:message code="map.Submit" />
								
							</button>
						</div>
					</div>
				</form>
			</div>
			<div class='rui-resizable-handle' style="background: #0077bf; width: 3px"></div>
		</div>

		<div id="sidebar-marker-create" style="min-width: 384px" class="sidebar-style rui-resizable-left resizable-test-block">
		
			<div class='rui-resizable-content' style="position: static;">
	  
				<form name="sidebarMarker" method="post"
					default-button="buttonInsert" novalidate>
					
						<!--  <div class="sidebar-coloredbar"></div>-->
						<span ng-click="clearFcMarker()"
							style="z-index: 10000"
							class="icon itaipu-icon-close sidebar-close"
							title="<spring:message code="map.Close" />"
							></span>
						<div style="position: absolute; left: 0; right: 0; bottom: 0; top: 0">
						<div id="tabs-2" ng-switch="LAYER_MENU_STATE" style="overflow:auto; width: auto" class="container">
							<div class="sidebar-content-header"><spring:message code="map.New-post" /></div>
							<br style="clear: both;"> <br> <label><spring:message code="map.Layer"/></label>
	
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
								style="top: -20px"><spring:message code="map.Field-required"/></span> <br>
	
							<div ng-repeat="attribute in attributesByLayer track by $index"
								style="position: relative">
								
								<ng-form name="ngSideMarker" default-button="buttonInsert">
								<label style="margin-top: 15px">{{ attribute.name }}</label> 
								
								<input type="number"
									name="number1" ng-if="attribute.type == 'NUMBER'"
									class="form-control" ng-model="attribute.value"
									ng-class="{ngInvalid:ngSideMarker.$submitted && ngSideMarker.number1.$error.required}"
									ng-required="attribute.required"
									> 
									
								<input
									name="date1" ng-if="attribute.type == 'DATE'"
									class="form-control datepicker" ng-model="attribute.value"
									ng-class="{ngInvalid: ngSideMarker.$submitted && ngSideMarker.date1.$error.required}"
									ng-required="attribute.required"
									>
	
								<div ng-if="attribute.type == 'BOOLEAN'" ng-required="attribute.required"  >
									<div class="required-boolean" >
										<input type="radio" name="boolean{{ $index }}" class="boolean-1 boolean" ng-model="attribute.value"
											value="Yes" onClick="isBooleanChecked(this)" ><spring:message code="map.Yes" /> 
											
										<input type="radio" name="boolean{{ $index }}" class="boolean-2 boolean" ng-model="attribute.value" 
											value="No" onClick="isBooleanChecked(this)"><spring:message code="map.No" /> 
									</div>
								</div>
	
								<input type="text" ng-if="attribute.type == 'TEXT'" name="texto"
									class="form-control" ng-model="attribute.value"
									ng-class="{ ngInvalid: ngSideMarker.$submitted && ngSideMarker.texto.$error.required }"
									ng-required="attribute.required"
									> 
									
									
									
									<span
									class="tooltip-validation"
									ng-show=" (ngSideMarker.texto.$error.required && ngSideMarker.$submitted) "
									style="top: 3px"><spring:message code="map.Field-required"/>
									</span> 
									
									<span
									class="tooltip-validation"
									ng-show="  (ngSideMarker.number1.$error.required && ngSideMarker.$submitted)"
									style="top: 3px"><spring:message code="map.Field-required"/>
									</span> 
									
									<span
									class="tooltip-validation"
									ng-show="!(ngSideMarker.number1.$error.required && ngSideMarker.$submitted) && (ngSideMarker.number1.$error.number)"
									style="top: 3px"><spring:message code="map.Must-be-a-number"/>
									</span> 
									
									<span
									class="tooltip-validation"
									ng-show=" (ngSideMarker.date1.$error.required && ngSideMarker.$submitted)"
									style="top: 3px"><spring:message code="map.Field-required"/>
									</span> 
									
									<ng-form>
							</div>
	
	
							<!-- <label>Foto</label> <input type="file" class="form-control" ng-model="currentEntity.photo"> -->
							<!-- <label>Descrição</label> <textarea ng-model="currentEntity.description" class="form-control" style="height: 100px"></textarea> -->
	
							<img class="marker-image" ng-show="imgResult" style="width: 100%; height: 200px; margin-top: 12px;"> <br>
							<hr>
	
							<input type="file" id="upload-input" style="display: none;"
								accept="image/*"
								onchange="angular.element(this).scope().setPhotoMarker(this)" />
	
							<button class="btn btn-default"
								onclick="angular.element('#upload-input').click();"
								style="float: left;"
								title="<spring:message code="map.Picture" />"
								>
								<span class="glyphicon glyphicon-picture"></span>
							</button>
	
							<button 
								id="buttonInsert" 
								class="btn btn-primary"
								ng-click="insertMarker()" 
								style="float: right"
								title="<spring:message code="map.Submit" />"
								>
								<spring:message code="map.Submit" />					
							</button>
						</div>
					</div>
				</form>
			</div>
		
  			<div class='rui-resizable-handle' style="background: #0077bf; width: 3px"></div>
		</div>
  
		<div id="sidebar-tabs" style="float: left;">
			<ul class="map-menu-items tab-flag" id="menu-sidebar-2" style="padding:6px">
				<li id="menu-item-1"
					title="<spring:message code="map.Layer-menu" />"
					ng-click="toggleSidebarMenu(300, '#menu-item-1');"
					class="menu-item bg-inactive">
					<a href="#tabs-1">
						<div class="icon itaipu-icon-layers sidebar-icon"></div>
					</a>
				</li>
				<li class="menu-item" id="menu-item-2" ng-click="listCustomSearchByUser()" title="Pesquisa Personalizada">
                    <a href="#tabs-2">
                        <div class="icon itaipu-icon-zoom sidebar-icon"></div>
                    </a>
                </li>
				<li class="menu-item" id="menu-item-3" ng-click="toggleSidebarMenu(300, '#menu-item-3');" title="<spring:message code="map.KML-enabled" />">
                    <a href="#tabs-3">
                        <div class="icon itaipu-icon-kml sidebar-icon"></div>
                    </a>
                </li>
			</ul>

			<div id="sidebar-layers" class="sidebar-style rui-resizable-left resizable-test-block" style="min-width: 384px" >
				<!--  <div class="sidebar-coloredbar"></div> -->
				
					<div class='rui-resizable-content' style="position: static;">
					
						<span style="z-index: 1000;" ng-click="toggleSidebarMenu(300, 'closeButton')"
						class="icon itaipu-icon-close sidebar-close"></span>
	
						<div id="tabs-1" ng-switch="LAYER_MENU_STATE" style="position: absolute; top:0; right:0; left:0; bottom:0">
							<div ng-switch-when="list">
								<div id="layer-list">
									<div>
										<div class="sidebar-content-header"><spring:message code="map.Layers"/></div>
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
											placeholder="<spring:message code="map.Group-or-layer"/>"
											class="sidebar-content-search form-control" />
									</div>
		
									<div id="tree" ivh-treeview="allLayers" ivh-fn="getSelectedNode"
										ivh-treeview-label-attribute="'label'"
										ivh-treeview-legend-attribute="'legenda'"
										ivh-treeview-children-attribute="'children'"
										ivh-treeview-id-attribute="'id'"
										ivh-treeview-filter="filter:bagSearch">
									</div>
									
									<div class="sidebar-content-header" ng-if="allSearchs.length > 0" style="margin: 30px 0px;"><spring:message code="map.Searchs"/></div>
                                    <br style="clear: both;">
                                    <div id="tree-pesquisas"
                                         ivh-treeview="allSearchs"
                                         ivh-fn="getSelectedSearchNode"
                                         ivh-treeview-label-attribute="'label'"	
                                         ivh-treeview-children-attribute="'children'">
                                    </div>
		
		
									<br />
								</div>
							</div>
		
							<div id="layer-legend-detail" ng-switch-when="legend_detail">
								<div class="sidebar-content-header" ng-click="exitLegendDetail()"
									style="cursor: pointer;">
									<span style="font-size: 17px;">&#x2190;</span><spring:message code="map.Layers"/>
								</div>
								<br style="clear: both;">
								<div class="legend-detail-title">{{legendDetailTitle}}</div>
								<hr>
								<div class="legend-image-container">
									<img ng-src="{{legendDetailImage}}" style="padding: 10px;">
								</div>
							</div>
		
						</div>
						<div id="tabs-2">

                            <div class="sidebar-content-header"><spring:message code="admin.access-group.Custom-Searchs" /></div>
                            <br style="clear: both; ">
                            <div class="form-item position-relative" style="width: 100%; margin-top: 30px; margin-bottom: 5px">
                                <select class="sidebar-content-search form-control" style="margin-bottom: 0; margin-top: 0"
                                        ng-model="currentCustomSearch"
                                        ng-change="selectCustomSearch(currentCustomSearch)"
                                        ng-options="search.name for search in customSearchs"
                                        ng-required="true" ng-hover>
                                    <option value=""><spring:message code="admin.custom-search.Select" />...</option>
                                </select>
                            </div>

                            <div id="alertPesquisa" ng-show="searchMsg != null" class="alert info" style="display: block;font-size: 11px;height: 40px;margin-top: 0px;margin-bottom: 0px;">
                                {{searchMsg}}
                            </div>
                            <hr style="border-color: #d9d9d9; position: absolute;top: 155px;right: 15px;left: 15px;"/>

                            <button class="btn btn-primary" ng-disabled="currentCustomSearch == null || currentCustomSearch.layer == null"
                                    style="width: 90px; height: 30px; position: absolute;top: 190px;" ng-click="listFieldsLayersSearch()"><spring:message code="admin.custom-search.Search" /></button>
                            <br/>
                            <div style="overflow-y: auto;position: absolute;top: 250px;bottom: 0px;left: 20px;right: 0px;">
                                <div ng-repeat="search in currentCustomSearch.layerFields" style="width: 90%;">
                                	<input ng-if="search.type != 'BOOLEAN'" id="item_{{$index}}" placeholder="{{search.label ? search.label : search.name}}" ng-class="{datepicker: search.type == 'DATE' }" type="text" class="form-control" maxlength="40">                                	                                	                                	
									
									<div ng-if="search.type == 'BOOLEAN'"  >
										<label>{{search.label ? search.label : search.name}}</label></br>						
										<input type="radio" name="boolean{{ $index }}" class="boolean-1 boolean yes"  
											value="Yes"  /><spring:message code="map.Yes" /> 
											
										<input type="radio" name="boolean{{ $index }}" class="boolean-2 boolean no" 
											value="No"  /><spring:message code="map.No" /> 
																					
									</div>
<!--                                     <div class="form-group" ng-if="search.type == 'DATETIME'"> -->
<!--                                         <label>{{ search.label ? search.label : search.name }}</label> -->
<!--                                         <div class="input-group input-daterange" id="item_{{$index}}" date-picker> -->
<!--                                             <input type="text" class="form-control" name="start" /> -->
<!--                                             <span class="input-group-addon">-</span> -->
<!--                                             <input type="text" class="form-control" name="end" /> -->
<!--                                         </div> -->
<!--                                     </div> -->
<!--                                     <input ng-if="search.type == 'STRING'" id="item_{{$index}}" placeholder="{{search.label ? search.label : search.name}}" type="text" class="form-control" maxlength="40"> -->
<!--                                     <div class="form-group row" ng-if="search.type == 'NUMBER'" id="item_{{$index}}"> -->
<!--                                         <div class="col-sm-5"> -->
<!--                                             <select class="form-control" style="padding: 4px"> -->
<!--                                                 <option value="=">=</option> -->
<!--                                                 <option value=">">></option> -->
<%--                                                 <option value="<"><</option> --%>
<!--                                                 <option value=">=">>=</option> -->
<%--                                                 <option value="<="><=</option> --%>
<!--                                                 <option value="!=">!=</option> -->
<!--                                                 <option value="entre">Entre</option> -->
<!--                                                 <option value="somente">Somente</option> -->
<!--                                             </select> -->
<!--                                         </div> -->
<!--                                         <div class="col-sm-7"> -->
<!--                                             <input placeholder="{{search.label ? search.label : search.name}}" type="text" class="form-control" maxlength="40"> -->
<!--                                         </div> -->
<!--                                     </div> -->
                                    </br>
                                </div>
                            </div>
                        </div>
						<div id="tabs-3" style="position: absolute; top:0; right:0; left:0; bottom:0">
		
		                    <div class="sidebar-content-header"><spring:message code="map.KML-files"/></div>
		                    <br style="clear: both; ">
		
		                    <div id="msgKml" ng-if="allLayersKML.length == 0" class="alert info" style="margin-top: 40px;text-align: center">
		                    	<spring:message code="map.None-KML-file-enabled"/>
		                    </div>
		
		                    <div style="overflow-x: auto;position: absolute;top: 110px;bottom: 0px;left: 20px;right: 0px;">
		                        <div id="tree-kml"
		                             ivh-treeview="allLayersKML"
		                             ivh-fn="getSelectedKMLNode"
		                             ivh-treeview-label-attribute="'label'"
		                             ivh-treeview-children-attribute="'children'">
		                        </div>
		                    </div>
		
		                </div>
	              </div>
	              <div class='rui-resizable-handle' style="background: #0077bf; width: 3px"></div>
			</div>
		</div>

	</div>

	<!-- Google Maps -->
	<div id="gmap" style="width: 0; height: 0"></div>

	<div id="typeMapQuest"
		ng-if="mapConf.active == 'mapQuest_osm' || mapConf.active == 'mapQuest_sat'"
		ng-mouseover="hideMousePosition()"
		style="position: absolute; top: 20px; right: 10px; z-index: 1;">
		<button type="button" ng-click="initializeMapQuestOSM()"
			class="btn btn-default btn-xs">OSM</button>
		<button type="button" ng-click="initializeMapQuestSAT()"
			class="btn btn-default btn-xs">SAT</button>
	</div>

	<!-- Openlayer Map -->
	<div id="olmap"
			style="position: absolute; top: 0; bottom: 0; left: 0; right: 0"> 
		<!-- style="position: relative; width: 100%; height: 100%; top: 0 !important"> -->
		<div id="popup" class="ol-popup">
			<!--<a href="#" id="popup-closer" class="ol-popup-closer"></a>-->
			<div id="popup-content"></div>
		</div>
		<div id="info"></div>
	</div>
</section>

</html>


