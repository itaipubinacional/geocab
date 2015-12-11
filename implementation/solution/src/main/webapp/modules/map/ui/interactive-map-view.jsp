<%@ page language="java" contentType="text/html; charset=UTF-8"
pageEncoding="UTF-8"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="sec"
uri="http://www.springframework.org/security/tags"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags"  %>
<!DOCTYPE html>
<html>

<div id="sb-site" class="map-content">

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
          <security:authorize access="isAuthenticated()">
            <li ng-click="initializeMarker()"
                title="<spring:message code="map.Perform-post" />"
            ng-class="{ferramenta_active : menu.fcMarker}"><a
              href="#tabs-1"> <span
              class="glyphicon glyphicon-map-marker sidebar-icon"></span>
          </a></li>
          </security:authorize>

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
              <accordion close-others="true" id="accordion-markers" class="accordion-popup accordion-caret" heightStyle="content">
                <accordion-group ng-repeat="feature in features track by $index" ng-init="isOpen = $index == 0" is-open="isOpen" ng-class="{'min-height-accordion': feature.type == 'internal' || feature.type == 'external' }">

                  <accordion-heading>
                    <div style="cursor:pointer; padding: 10px 0;">
                      <i class="pull-left" ng-class="{'icon-chevron-down': isOpen, 'icon-chevron-right': !isOpen}"></i>
                      <span ng-if="feature.type == 'internal'" style="overflow:auto" ng-click="calculo()" >{{feature.feature.layer.title}} </span>
                      <span ng-if="feature.type == 'external'" style="overflow:auto" ng-click="calculo()">{{feature.feature.layer.titulo}}</span>

                    </div>
                  </accordion-heading>


                          <span ng-if="feature.type == 'internal'">

                    <div id="tabs-2" ng-switch="LAYER_MENU_STATE" class="container" style="height: 100%; width: 100%; padding: 0;">

                      <span style="float: left; margin-top: 12px; font-weight: bold; font-size: 18px;">{{marker.layer.title }}</span>
                      <br style="clear: both;"> <br>
                      <span style="float: left"><spring:message code="map.Created-by"/>: <b>{{ marker.user.name}}</b></span>
                      <span style="float: right">{{ marker.created |date:'dd/MM/yyyy' }}</span>
                      <br>
                      <span style="float: left" ng-if="marker.status == 'PENDING'"><spring:message code="map.Status"/>: <b><spring:message code="map.Pending"/></b></span>
                      <span style="float: left" ng-if="marker.status == 'REFUSED'"><spring:message code="map.Status"/>: <b><spring:message code="map.Refused"/></b></span>
                      <span style="float: left" ng-if="marker.status == 'ACCEPTED'"><spring:message code="map.Status"/>: <b><spring:message code="map.Approved"/></b></span>
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
                          ng-if="(marker.status != 'ACCEPTED' && userMe.id == marker.user.id)"
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
                          title="<spring:message code="map.Refuse"/>"
                      >
                      <i class="glyphicon glyphicon-ban-circle"></i>
                      </button>
                      <button
                          ng-if="(userMe.role == 'ADMINISTRATOR' || userMe.role == 'MODERATOR') && (marker.status == 'REFUSED' || marker.status == 'PENDING')"
                          style="float: right; margin-right: 5px; color: #00981F"
                          ng-click="enableMarker()"
                          class="btn btn-default"
                          title="<spring:message code="map.Approve"/>"
                      >
                      <i class="glyphicon glyphicon-ok"></i>
                      </button>
                      <br>
                      <div style="text-align:center">
                        <img ng-click="openImgModal(attributesByMarker)" ng-show="imgResult" class="marker-image" ng-src="{{ imgResult }}"
                             style="width: 100%;margin-top: 12px;cursor: pointer;max-width:360px"> <br>
                      </div>
                      <br>

                      <div style=" overflow: auto;">
                        <div ng-repeat="markerAttribute in attributesByMarker track by $index" style="position: relative;margin-bottom:15px">

                          <!--<button ng-if="markerAttribute.attribute.type == 'PHOTO_ALBUM'" class="btn btn-default"
                                  onclick="angular.element('#upload-input').click();"
                                  style="float: left;"
                                  title="<spring:message code='map.Picture'/>"><span class="glyphicon glyphicon-picture"></span>
                          </button>-->

                          <label ng-style="$index > 0 ? {'margin-top':'15px'} : '' " ng-if="!markerAttribute.value == '' || markerAttribute.value == '0'">{{ markerAttribute.attribute.name }}</label>

                          <input
                              type="number" name="number1"
                              ng-if="(markerAttribute.attribute.type == 'NUMBER' && !markerAttribute.value == '') || markerAttribute.value == '0' "
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

                          <div
                              ng-if="markerAttribute.attribute.type == 'TEXT' && !markerAttribute.value == ''"
                              name="texto"
                              class="form-control" ng-bind="markerAttribute.value"
                              required
                              style="resize: none;max-height: 127px;min-height: 30px;height: auto;"
                              ng-disabled="true" style="resize:none">
                          </div>


                        </div>
                      </div>
                    </div>
                         </span>

                         <span ng-if="feature.type == 'external'">
                           <span ng-repeat="key in objectKeys(feature.feature.fields)" >
                               <b>{{key}}</b> - {{feature.feature.fields[key]}}
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

                    <button ng-if="markerAttribute.attribute.type == 'PHOTO_ALBUM'" class="btn btn-default"
                            onclick="angular.element('#upload-input').click();"
                            style="float: left;"
                            title="<spring:message code='map.Picture'/>"><span class="glyphicon glyphicon-picture"></span>
                    </button>

                    <label  ng-style="$index > 0 ? {'margin-top':'15px'} : '' ">{{ markerAttribute.attribute.name }}</label>

                    <input
                        type="number"
                        name="number1"
                        ng-if="markerAttribute.attribute.type == 'NUMBER'"
                        class="form-control"
                        ng-model="markerAttribute.value"
                        maxlength="255"
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
                           maxlength="255"
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

                    {{ attribute.type }}
                    <button ng-if="attribute.type == 'PHOTO_ALBUM'" class="btn btn-default"
                            onclick="angular.element('#upload-input').click();"
                            style="float: left;"
                            title="<spring:message code='map.Picture'/>"><span class="glyphicon glyphicon-picture"></span>
                    </button>

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

                <br style="clear: both;"> <br>

                <label><spring:message code="map.Layer"/></label>

                <!-- no-results-text="Nenhum registro encontrado com" -->

                <select ng-change="listAttributesByLayer()"
                        data-placeholder="Selecione uma camada" name="camada"
                        ng-options="layer.layerTitle group by layer.group for layer in selectLayerGroup"
                        ng-model="currentEntity.layer" chosen class="form-control"
                        ng-class="{ngInvalid: sidebarMarker.camada.$error.required }"
                        required>
                  <option value=""></option>
                </select>

                <span class="tooltip-validation"
                      ng-show="sidebarMarker.$submitted && sidebarMarker.layer.$error.required"
                      style="top: -20px"><spring:message code="map.Field-required"/></span>

                <br style="clear: both;"> <br>

                <span><b><spring:message code='map.Input-format'/></b></span>
                <i class="icon-question-sign icon-large" tooltip-placement="right"
                   tooltip="Selecione o formato de entrada para as coordenadas"></i>

                <br style="clear: both;">

                <div class="form-item-horizontal radio" style="margin-left: 0; margin-top: 15px">
                  <input type="radio" id="DMS" ng-change="setMarkerCoordinatesFormat()" ng-model="coordinatesFormat" value="DEGREES_MINUTES_SECONDS"
                         name="DMS">
                  <label class="radio-label" for="DMS"> <spring:message code='admin.users.coordinatesDMS'/> </label>
                </div>

                <br />

                <div class="form-item-horizontal radio" style="margin-left: 0;">
                  <input type="radio" id="DD" ng-change="setMarkerCoordinatesFormat()" ng-model="coordinatesFormat" value="DEGREES_DECIMAL"
                         name="DD">
                  <label class="radio-label" for="DD"> <spring:message code='admin.users.coordinatesDD'/> </label>
                </div>

                <br style="clear: both;">

                <span ><b><spring:message code='map.Coordinates'/></b></span>

                <i class="icon-question-sign icon-large" tooltip-placement="right"
                   tooltip="<spring:message code='map.Tips-coordinate'/>"></i>

                <br style="clear: both;">

                <div class="form-item position-relative" style="width:100%;margin:10px 0; padding-right: 10px">
                  <label class="detail-label" required>Latitude</label>
                  <input type="text" name="latitude" ng-change="setMarkerCoordinates()"
                         class="form-control" ng-model="formattedLatitude">
                </div>

                <br />

                <div class="form-item position-relative" style="width:100%;margin-bottom: 10px; padding-right: 10px">
                  <label class="detail-label" required>Longitude</label>
                  <input type="text" name="longitude" ng-change="setMarkerCoordinates()"
                         class="form-control" ng-model="formattedLongitude">
                </div>

                <div ng-repeat="attribute in attributesByLayer track by $index"
                     style="float: left;width: 100%;" class="form-group">

                  <ng-form name="ngSideMarker" default-button="buttonInsert">

                    <button ng-if="attribute.type == 'PHOTO_ALBUM'" class="btn btn-default"
                            ng-click="showUpload(attribute)"
                            style="float: left;margin-right: 5px"
                            title="<spring:message code='map.Picture'/>"><span class="glyphicon glyphicon-picture"></span>
                    </button>

                    <label style="margin-top: 10px">{{ attribute.name }}</label>

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
                           ng-required="attribute.required" maxlength="255"
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

                <img class="marker-image" ng-if="imgResult" style="width: 100%; height: 200px; margin-top: 12px;"> <br>
                <hr>

                <div ng-if="currentEntity.layer">
                  <!--<input type="file" id="upload-input" style="display: none;"
                         accept="image/*"
                         onchange="angular.element(this).scope().setPhotoMarker(this)" />

                  <button  class="btn btn-default"
                           onclick="angular.element('#upload-input').click();"
                           style="float: left;"
                           title="<spring:message code="map.Picture" />"
                  >
                  <span class="glyphicon glyphicon-picture"></span>
                  </button>-->

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
            </div>
          </form>
        </div>

        <div class='rui-resizable-handle' style="background: #0077bf; width: 3px"></div>
      </div>

      <div id="sidebar-tabs" style="float: left;">
        <ul class="map-menu-items tab-flag" id="menu-sidebar-2" style="padding:6px">

          <li class="menu-item bg-inactive" id="menu-item-5" ng-click="toggleSidebarMenu(300, '#menu-item-5');" title="<spring:message code='map.Background'/>">
          <a href="#tabs-5">
            <div style="font-size:25px" class="icon itaipu-icon-globe-world sidebar-icon"></div>
          </a>
          </li>

          <li id="menu-item-1"
              title="<spring:message code='map.Layer-menu' />" ng-click="toggleSidebarMenu(300, '#menu-item-1');" class="menu-item">
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

          <li class="menu-item" id="menu-item-4" ng-click="toggleSidebarMenu(300, '#menu-item-4');" title="<spring:message code="map.SHP-enabled" />">
          <a href="#tabs-4">
            <div style="font-size:25px" class="icon itaipu-icon-shp sidebar-icon"></div>
          </a>
          </li>
        </ul>

        <div id="sidebar-layers" class="sidebar-style rui-resizable-left resizable-test-block" style="min-width: 384px" >
          <!--  <div class="sidebar-coloredbar"></div> -->

          <div class='rui-resizable-content' style="position: static;">

              <span style="z-index: 1000;" ng-click="toggleSidebarMenu(300, 'closeButton')"
                    class="icon itaipu-icon-close sidebar-close"></span>

            <div id="tabs-5" ng-switch="LAYER_MENU_STATE" style="position: absolute; top:0; right:0; left:0; bottom:0">
              <div ng-switch-when="list">
                <div id="layer-list">
                  <div>
                    <div class="sidebar-content-header"><spring:message code="map.Background"/></div>
                    <br style="clear: both;">
                    <div class="form-item-horizontal radio"
                         style="margin-left: 0; margin-top: 40px">
                      <input type="radio" id="osm" ng-model="mapConf.type"
                             value="osm" ng-click="setBackgroundMap('OPEN_STREET_MAP')"> <label
                        class="radio-label" for="osm"> Open Street View </label>
                    </div>
                    <br />
                    <div class="form-item-horizontal radio" style="margin-left: 0;">
                      <input type="radio" id="googleMap" ng-model="mapConf.type"
                             value="gmap" ng-click="setBackgroundMap('GOOGLE_MAP')"> <label
                        class="radio-label" for="googleMap"> Google Maps </label>
                    </div>
                    <br />
                    <div class="form-item-horizontal radio" style="margin-left: 0;">
                      <input type="radio" id="mapQuest" ng-model="mapConf.type"
                             value="mapQuest" ng-click="setBackgroundMap('MAP_QUEST_OSM')"> <label
                        class="radio-label" for="mapQuest"> MapQuest </label>
                    </div>

                    <hr style="border-color: #d9d9d9;"/>

                    <div style="margin-top: 12px" ng-if="mapConf.type == 'gmap'">

                      <div>
                        <div class="form-item-horizontal radio" style="margin-left: 0;">
                          <input type="radio" id="Map" ng-click="setBackgroundMap('GOOGLE_MAP')" ng-model="backgroundMap.subType" value="GOOGLE_MAP"
                                 name="Map">
                          <label class="radio-label" for="Map"> <spring:message code='admin.users.Map'/> </label>
                        </div>

                        <div class="form-item-horizontal radio" style="margin-left: 0;">
                          <input type="radio" id="Satellite" ng-click="setBackgroundMap('GOOGLE_SATELLITE')" ng-model="backgroundMap.subType" value="GOOGLE_SATELLITE"
                                 name="Satellite">
                          <label class="radio-label" for="Satellite"> <spring:message code='admin.users.Satellite'/> </label>
                        </div>
                      </div>

                      <div style="margin-left: 30px" ng-if="backgroundMap.subType == 'GOOGLE_MAP'">
                        <label><input ng-change="setType('GOOGLE_MAP_TERRAIN', backgroundMap.type.GOOGLE_MAP_TERRAIN)" name="GOOGLE_MAP_TERRAIN" type="checkbox"
                                      ng-model="backgroundMap.type.GOOGLE_MAP_TERRAIN" value="GOOGLE_MAP_TERRAIN">
                          <spring:message code='admin.users.Terrain'/>
                        </label>
                      </div>
                      <div style="margin-left: 130px" ng-if="backgroundMap.subType == 'GOOGLE_SATELLITE'">
                        <label><input ng-change="setType('GOOGLE_SATELLITE_LABELS', backgroundMap.type.GOOGLE_SATELLITE_LABELS)" name="GOOGLE_SATELLITE_LABELS" type="checkbox" style="margin-left: 20px "
                                      ng-model="backgroundMap.type.GOOGLE_SATELLITE_LABELS" value="GOOGLE_SATELLITE_LABELS">
                          <spring:message code='admin.users.Labels'/>
                        </label>
                      </div>
                    </div>

                    <div style="margin-top: 12px;" ng-if="mapConf.type == 'mapQuest'">

                      <div class="form-item-horizontal radio" style="margin-left: 0;">
                        <input type="radio" id="OSM" ng-click="setBackgroundMap('MAP_QUEST_OSM')" ng-model="backgroundMap.subType" value="MAP_QUEST_OSM"
                               name="OSM">
                        <label class="radio-label" for="OSM"> OSM </label>
                      </div>

                      <div class="form-item-horizontal radio" style="margin-left: 0;">
                        <input type="radio" id="SAT" ng-click="setBackgroundMap('MAP_QUEST_SAT')" ng-model="backgroundMap.subType" value="MAP_QUEST_SAT"
                               name="SAT">
                        <label class="radio-label" for="SAT"> SAT </label>
                      </div>

                    </div>

                  </div>

                  </div>

              </div>
            </div>
            <div id="tabs-1" ng-switch="LAYER_MENU_STATE" style="position: absolute; top:0; right:0; left:0; bottom:0">
              <div ng-switch-when="list">

                <div>

                  <div class="sidebar-content-header"><spring:message code="map.Layers"/></div>

                  <br style="clear: both;">

                  <div ng-show="allLayers.length > 0">
                    <input maxlength="144" type="text" ng-model="bagSearch"
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
                      style="width: 90px; position: absolute;top: 190px;" ng-click="listFieldsLayersSearch()"><spring:message code="admin.custom-search.Search" /></button>
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
              <div style="overflow: auto;top: 110px;bottom: 0px;left: 20px;right: 0px;">
                <div id="tree-kml"
                     ivh-treeview="allLayersKML"
                     ivh-fn="getSelectedKMLNode"
                     ivh-treeview-label-attribute="'label'"
                     ivh-treeview-children-attribute="'children'"
                    >
                </div>
              </div>
            </div>

            <div id="tabs-4" style="min-width: 338px; position: absolute; top:0; right:0; left:0; bottom:0;">

              <div class="sidebar-content-header"><spring:message code="map.SHP-export-import"/></div>
              <br style="clear: both; ">

              <div>

                <button style="margin: 10px 0;" class="btn btn-primary"
                        ng-click="setAction('export')">
                  <i class="icon-upload icon-large"></i>
                  <spring:message code="admin.shape-file.Export" />
                </button>

                <button class="btn btn-primary"
                        ng-click="clickUpload()">
                  <i class="icon-download icon-large"></i>
                  <spring:message code="admin.shape-file.Import" /></button>

                <input style="display:none" id="upload" multiple="true" type="file" name="upload[]" onchange="angular.element(this).scope().onFileChange(this)">
                <input style="display:none" id="upload1" multiple="true" type="file" name="upload[]">
                <input style="display:none" id="upload2" multiple="true" type="file" name="upload[]">

                <hr style="border-color: #d9d9d9;"/>

              </div>
              <div ng-if="isExport">

                <p>Filtros</p>

                <div>
                  <select ng-change="listAttributesByLayer()"
                          data-placeholder="Selecione uma camada" name="camada"
                          ng-options="layer.layerTitle group by layer.group for layer in selectLayerGroup"
                          ng-model="shapeFile.filter.layer" chosen class="form-control"
                          ng-class="{ngInvalid: sidebarMarker.camada.$error.required }"
                          required>
                    <option value=""></option>
                  </select>
                </div>

                <div style="margin-top:10px;">

                  <select class="form-control" ng-model="shapeFile.filter.status" style="width:100%;">
                    <option value="" ng-selected="true"><spring:message code="admin.marker-moderation.All-status" /></option>
                    <option value="PENDING"><spring:message code="admin.marker-moderation.Pending" /></option>
                    <option value="ACCEPTED"><spring:message code="admin.marker-moderation.Approved" /></option>
                    <option value="REFUSED"><spring:message code="admin.marker-moderation.Refused"/></option>
                  </select>
                </div>

                <div style="margin-top:10px;float: left">
                  <input ng-model="shapeFile.filter.dateStart" class="form-control datepicker" style="float: left;width:130px;margin-right:10px" placeholder="<spring:message code='admin.marker-moderation.Beginning'/>"/>
                  <input ng-model="shapeFile.filter.dateEnd" class="form-control datepicker" style="float: left;width:130px;margin-right:10px" placeholder="<spring:message code='admin.marker-moderation.Ending'/>"/>
                </div>

                <div ng-if="userMe.role == 'ADMINISTRATOR'" style="float:left;width:100%;margin-top:10px;">
                  <select data-placeholder="<spring:message code='admin.marker-moderation.Users'/>" name="user"
                          ng-options="user.email for user in selectUsers"
                          ng-model="shapeFile.filter.user" chosen class="form-control">
                  <option value=""><spring:message code="admin.marker-moderation.All-users"/></option>
                  </select>
                </div>

                <div style="float: left;margin-top: 10px">
                  <span ng-click="clearFilters()">Limpar Filtros</span>
                  <input type="button" style="margin-right:5px" ng-click="bindFilter()" value="<spring:message code='Filter'/>"
                         title="<spring:message code='Search'/>" class="btn btn-default"/>
                </div>

                <div style="float: left;clear: both">
                  <button ng-click="exportShapeFile()" type="button" style="margin: 6px 0 20px 0;" class="btn btn-success">Exportar</button>
                </div>

              </div>
              <!-- #export-shape-file -->

              <div style="text-align: left;float:left; width: 100%" ng-if="isImport">

                <form novalidate name="form">
                  <div class="form-item-horizontal radio">
                    <input type="radio" id="layer" ng-model="shapeFile.layerType"
                           value="layer" ng-change="setLayerType()"> <label
                      class="radio-label" for="layer"> Camada existente</label>
                  </div>

                  <div class="form-item-horizontal radio">
                    <input type="radio" id="new-layer" ng-model="shapeFile.layerType"
                           value="new" ng-change="setLayerType()"> <label
                      class="radio-label" for="new-layer"> Nova camada</label>
                  </div>

                  <div class="form-item position-relative">
                    <label class="detail-label" required><spring:message code="admin.datasource.Data-Source"/></label>
                    <div class="input-group position-relative">
                      <input name="dataSource" type="text" disabled class="form-control"
                             ng-model="shapeFile.form.dataSource.name"
                             placeholder="<spring:message code='admin.layer-config.Enter-the-data-source' />" maxlength="144"
                             ng-minlength="1"
                             ng-hover
                             required
                             ng-class="{ngInvalid:form.dataSource.$error.required && (form.$submitted || form.dataSource.$dirty)}">
                        <span class="input-group-btn">
                            <button style="height: 34px" ng-click="selectDataSource()" title="<spring:message code='admin.layer-config.Enter-the-data-source' />" class="btn btn-default"
                                    type="button" ng-disabled="currentEntity.id != null">
                              <i class="icon-plus-sign icon-large"></i>
                            </button>
                        </span>
                    </div>
                    <span ng-show="form.dataSource.$error.required && (form.$submitted || form.dataSource.$dirty)" class="tooltip-validation"><spring:message code="admin.datasource.Data-Source"/> <spring:message code="required"/></span>
                  </div>

                  <div ng-if="shapeFile.layerType != 'new'" class="form-item position-relative">
                    <label class="detail-label" required><spring:message code="admin.custom-search.Layer"/></label>
                    <div class="input-group">
                      <input name="layerGroup" type="text" class="form-control"
                             ng-model="shapeFile.form.layer.name"
                             disabled
                             placeholder="<spring:message code='admin.custom-search.Enter-the-Layer'/>"
                             maxlength="144"
                             ng-minlength="1"
                             ng-hover
                             required>
                        <span class="input-group-btn">
                            <button style="height: 34px" ng-click="selectLayerConfig()" class="btn btn-default" type="button"
                                    ng-disabled="shapeFile.form.dataSource == null">
                              <i class="icon-plus-sign icon-large"></i>
                            </button>
                        </span>
                    </div>
                    <span ng-show="form.layerGroup.$error.required && form.$submitted"
                          class="tooltip-validation"><spring:message code="admin.custom-search.Layer-required"/></span>
                  </div>

                  <div ng-if="shapeFile.layerType == 'new'" class="form-item position-relative" style="width:100%;margin-bottom: 10px; padding-right: 10px">
                    <label class="detail-label" required>
                      <spring:message code="Title"/>
                    </label>
                    <input name="title" type="text" class="form-control"
                           ng-model="shapeFile.form.title"
                           placeholder="Informe o título"
                           maxlength="144" ng-minlength="1"
                           ng-hover
                           required
                           ng-class="{ngInvalid:form.title.$error.required && (form.$submitted || form.title.$dirty) }"/>
                      <span ng-show="form.title.$error.required && (form.$submitted || form.title.$dirty) "
                            class="tooltip-validation"><spring:message code="admin.layer-config.Title-required"/></span>
                  </div>

                  <div style="margin-bottom: 10px">
                    <button ng-click="addAttribute()" title="<spring:message code='admin.layer-config.Add-attributes' />"
                            class="btn btn-primary" style="margin-bottom: 5px">
                      <spring:message code="admin.layer-config.Add-attributes"/>
                    </button>
                  </div>

                  <div ng-if="shapeFile.layerType == 'new'">
                    <div>
                      <div style="float: left;">
                        <img ng-src="{{ shapeFile.form.icon}}"/>
                      </div>
                      <button class="btn btn-primary" ng-click="moreIcons()" >
                        <spring:message code="admin.layer-config.More-icons"/>
                      </button>
                    </div>
                    <div style="margin-top: 10px" class="form-item position-relative">
                      <label class="detail-label" required><spring:message code="admin.layer-config.Layer-group"/> </label>
                      <div class="input-group">
                        <input name="layerGroup" type="text" disabled class="form-control"
                               ng-model="shapeFile.form.layerGroup.name"
                               placeholder="<spring:message code='admin.layer-config.Enter-the-layer-group' />"
                               maxlength="144"
                               ng-minlength="1"
                               required
                               ng-class="{ ngInvalid:form.layerGroup.$error.required && (form.$submitted || form.layerGroup.$dirty)}" class="tooltip-validation}">
                          <span class="input-group-btn">
                              <button style="height: 34px" ng-click="selectLayerGroup()" class="btn btn-default"
                                      title="<spring:message code='admin.layer-config.Enter-the-layer-group' />"
                                      type="button"
                                      ng-disabled="shapeFile.form.dataSource == null">
                                  <i class="icon-plus-sign icon-large"></i>
                            </button>
                          </span>
                      </div>
                      <span ng-show="form.layerGroup.$error.required && (form.$submitted || form.layerGroup.$dirty)" class="tooltip-validation"><spring:message code="admin.layer-config.Layer-group"/>  <spring:message code="required"/> </span>
                    </div>

                    <div style="float: left; width: 100%;padding: 0 20px 0 5px;">
                      <div class="position-relative" scale-slider ng-model="layers"></div>
                      <div style="width: 350px;">
                        <label style="float: left">{{layers.values[0]}}</label>
                        <label style="float: right;">{{layers.values[1]}}</label>
                      </div>
                    </div>

                    <div class="form-item position-relative" style="width: 300px;">
                      <label><input type="checkbox" id="grupo" style="width: 20px;"
                             ng-model="shapeFile.form.startEnabled"> <spring:message code="admin.layer-config.Start-allowed-in-map"/></label>
                    </div>

                    <div class="form-item position-relative" style="width: 300px;">
                      <label><input type="checkbox" style="width: 20px;"
                             ng-model="shapeFile.form.startVisible"> <spring:message code="admin.layer-config.Available-in-the-layers-menu"/></label>
                    </div>

                    <div class="form-item position-relative" style="width: 300px;">
                      <label><input type="checkbox" style="width: 20px;"
                             ng-model="shapeFile.form.enabled"> <spring:message code="admin.layer-config.Available-to-receive-posts"/></label>
                    </div>

                    <div>
                      <button ng-click="selectAccessGroups()" type="button" style="margin: 6px 0 20px 0;" class="btn btn-primary">Associar grupo</button>
                    </div>

                  </div>

                  <div>
                    <button ng-click="importShapeFile()" type="button" style="margin: 6px 0 20px 0;" class="btn btn-success">Salvar</button>
                  </div>

                </form>
              </div>
              <!-- #import-shape-file -->

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

</div>

</html>



