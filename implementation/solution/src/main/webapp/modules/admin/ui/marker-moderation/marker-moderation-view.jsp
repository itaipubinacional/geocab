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
			position : static;
		}
	</style>
	
	<!--Message -->
    <div class="msg" style="position:absolute;text-align:center; width:57%;top:13%;left:40%; z-index:1000" ng-include="'static/libs/eits-directives/alert/alert.html'"></div>
	
	<!-- OpenLayers 3  -->
	<link rel="stylesheet" href="static/libs/openlayers/ol.css" type="text/css">
	
	<!-- CSS do mapa -->
    <link href="static/style/map/style.css" type="text/css" rel="stylesheet">
	
	<!-- OpenLayers 3 -->
	<script src="<c:url value="/static/libs/openlayers/ol.js"/>"></script>
	
	<!-- Google Maps -->
	<script type="text/javascript" src="http://maps.googleapis.com/maps/api/js?client=gme-itaipubinacional&sensor=false&channel=geocab"></script>

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
			<li ng-click="eventIncreaseZoom()" title="<spring:message code="map.Zoom-in" />"><a href="#tabs-2">
					<div class="icon itaipu-icon-plus sidebar-icon"></div>
			</a></li>
			<li ng-click="eventDecreaseZoom()" title="<spring:message code="map.Zoom-out" />"><a>
					<div class="icon itaipu-icon-minus sidebar-icon"></div>
			</a></li>
			<li ng-click="eventMarkerTool()"
				ng-class="{ferramenta_active : menu.selectMarker}"
				title="<spring:message code="admin.marker-moderation.Select-marker" />"><a>
					<div class="select-moderation sidebar-icon" style="width: 37px; height: 20px;"></div>
			</a></li>
		</ul>
	</div>

	<!-- Map -->
	<div style="position : absolute;top:116px;left : 38%;right: 0;bottom: 0;">
			<!-- Openlayer Map -->
			<div id="olmap" style="position : absolute;top : 0;left : 0;right: 0;bottom: 0;"> 
				<div id="popup" class="ol-popup">
					<div id="popup-content"></div>
				</div>
				<div id="info"></div>
			</div>
	</div>
	
</html>