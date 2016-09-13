<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>

<style>
    .content {
        margin: 0;
        width: 100%;
        position: static;
    }
</style>

<!--Message -->
<div class="msg" style="position:absolute;text-align:center; width:57%;left:40%; z-index:1000"
     ng-include="'static/libs/eits-directives/alert/alert.html'"></div>

<!-- OpenLayers 3  -->
<link rel="stylesheet" href="static/libs/openlayers/ol.css" type="text/css">

<!-- CSS do mapa -->
<link href="static/style/map/style.css" type="text/css" rel="stylesheet">

<!-- OpenLayers 3 -->
<script src="<c:url value='/static/libs/openlayers/ol.js'/>"></script>

<!-- Google Maps -->
<script type = "text/javascript" src = "http://maps.googleapis.com/maps/api/js?client=gme-itaipubinacional&sensor=false&channel=geocab" ></script>

<!-- Partial views of states -->
<div ng-switch on="currentState">
    <div ng-switch-when="marker-moderation.list">
        <div ng-include="'modules/admin/ui/marker-moderation/marker-moderation-list.jsp'"></div>
    </div>
    <div ng-switch-when="marker-moderation.detail">
        <div ng-include="'modules/admin/ui/marker-moderation/marker-moderation-detail.jsp'"></div>
    </div>
    <div ng-switch-when="marker-moderation.create">
        <div ng-include="'modules/admin/ui/marker-moderation/marker-moderation-form.jsp'"></div>
    </div>
    <div ng-switch-when="marker-moderation.update">
        <div ng-include="'modules/admin/ui/marker-moderation/marker-moderation-form.jsp'"></div>
    </div>
    <div ng-switch-when="marker-moderation.history">
        <div ng-include="'modules/admin/ui/marker-moderation/marker-moderation-history.jsp'"></div>
    </div>
    <div ng-switch-default>
        <div ng-include="'modules/common/loading.jsp'"></div>
    </div>
</div>

  <div class="menu-sidebar-container" style="right: 5px" ng-mouseover="hideMousePosition()">

      
	<ul class="map-menu-items tool-items" id="menu-sidebar" style="padding:3px">
        <li ng-click="eventIncreaseZoom()" title="<spring:message code='map.Zoom-in'/>">
            <a>
                <div class="icon itaipu-icon-zoom-in sidebar-icon"></div>
            </a>
        </li>
        <li ng-click="eventDecreaseZoom()" title="<spring:message code='map.Zoom-out'/>">
            <a>
                <div class="icon itaipu-icon-zoom-out sidebar-icon"></div>
            </a>
        </li>
        <li ng-click="eventMarkerTool()"
            ng-class="{ferramenta_active : menu.selectMarker}"
            title="<spring:message code='admin.marker-moderation.Select-marker'/>">
            <a>
                <div class="select-moderation sidebar-icon" style="width: 37px; height: 20px;"></div>
            </a>
        </li>
        <li title="<spring:message code='map.Calculate-distance' />"
            ng-click="initializeDistanceCalc()"
            ng-class="{ferramenta_active : menu.fcDistancia}">
            <a>
                <div class="icon itaipu-icon-ruler sidebar-icon"></div>
            </a>
        </li>
        <li ng-click="initializeAreaCalc()"
            title="<spring:message code='map.Calculate-area'/>"
            ng-class="{ferramenta_active : menu.fcArea}">
            <a>
                <div class="icon itaipu-icon-square sidebar-icon"></div>
            </a>
        </li>
        
    </ul>



      <div id="sidebar-tabs" style="float: left;">
      
        <ul class="map-menu-items tab-flag" id="menu-sidebar-2" style="padding:9px">
       		<li 	class="menu-item" id="menu-item-3" ng-click="toggleSidebarMenu(300, '#menu-item-3');" 
	       		title="<spring:message code="map.KML-enabled" />" 
	       		style="padding-bottom: 37px; padding-top: 17px;"
	       		ng-class="{ferramenta_active : menu.fcKml}">
	          <a>
	            <div class="icon itaipu-icon-kml sidebar-icon"></div>
	          </a>
          </li>
        </ul>

        <div id="sidebar-layers" class="sidebar-style rui-resizable-left resizable-test-block" style="min-width: 384px" >
          <!--  <div class="sidebar-coloredbar"></div> -->

          <div class='rui-resizable-content' style="position: static;">

              <span style="z-index: 1000;" ng-click="toggleSidebarMenu(300, 'closeButton')"
                    class="icon itaipu-icon-close sidebar-close"></span>
                    
             <div id="tabs-3" style="position: absolute; top:0; right:0; left:0; bottom:0; padding: 13px 19px 0px 19px; box-shadow: rgb(153, 153, 153) 0px 0px 6px 0px, rgb(153, 153, 153) -3px 3px 3px -3px">

              <div class="sidebar-content-header"><spring:message code="map.KML-files"/></div>
              <br style="clear: both; ">

              <div id="msgKml" ng-if="allLayersKML.length == 0" class="alert info" style="margin-top: 40px;text-align: center;">
<%--                 <spring:message code="map.None-KML-file-enabled"/> --%>
                {{kmlLabel}}
              </div>
              <div style="overflow: auto;top: 110px;bottom: 0px;left: 20px;right: 0px;">
                <div id="tree-kml-marker-moderation"
                     ivh-treeview="allLayersKML"
                     ivh-fn="getSelectedKMLNode"
                     ivh-treeview-label-attribute="'label'"
                     ivh-treeview-children-attribute="'children'"
                    >
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>

    </div>



<!-- Map -->
<div style="position: absolute;top:116px;left: 38%; right: 0;bottom: 0; z-index: 0">
    <!-- Openlayer Map -->
    <div id="olmap" style="position : absolute;top : 0;left : 0;right: 0;bottom: 0;" class="loading">
        <div id="popup" class="ol-popup">
            <div id="popup-content"></div>
        </div>
        <div id="info" style="height: 30px; margin: 0"></div>
    </div>
</div>

</html>