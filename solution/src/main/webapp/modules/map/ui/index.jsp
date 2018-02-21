<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags"  %>
<!DOCTYPE html>
<html xmlns:ng="http://angularjs.org">

	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title>GeoITAIPU Sustentável</title>

		<!-- Styles -->
		<jsp:include page="../../default-styles.jsp"/>

		<!-- Scripts -->
		<jsp:include page="../../default-scripts.jsp"/>


		<script src="<c:url value="/static/libs/right/right-src.js"/>"></script>
		<script src="<c:url value="/static/libs/right/right-resizable.js"/>"></script>

		<!-- OpenLayers 3  -->
	    <link rel="stylesheet" href="<c:url value="/static/libs/openlayers/ol.css"/>" type="text/css">

	    <!-- Treeview -->
	    <link rel="stylesheet" href="<c:url value="/static/libs/angular-treeview/ivh-treeview.min.css"/>" type="text/css">

	    <!-- CSS do mapa -->
	    <link href="<c:url value="/static/style/map/style.css"/>" type="text/css" rel="stylesheet">

		<!-- Controllers -->
		<!-- Main -->
		<script type="text/javascript" src="<c:url value="/modules/map/map-main.js"/>"></script>
   		<script type="text/javascript" src="<c:url value="/modules/abstract-crud-controller.js"/>"></script>
		<script type="text/javascript" src="<c:url value="/modules/map/controller/map-controller.js"/>"></script>
		<script type="text/javascript" src="<c:url value="/modules/map/controller/contact-controller.js"/>"></script>
		<script type="text/javascript" src="<c:url value="/modules/map/controller/popup/map-info-controller.js"/>"></script>
		<script type="text/javascript" src="<c:url value="/modules/map/controller/popup/img-controller.js"/>"></script>
		<script type="text/javascript" src="<c:url value="/modules/map/controller/popup/upload-controller.js"/>"></script>
		<script type="text/javascript" src="<c:url value="/modules/map/controller/popup/confirm-import-controller.js"/>"></script>
		<script type="text/javascript" src="<c:url value="/modules/admin/controller/popup/refuse-marker-controller.js"/>"></script>
		<script type="text/javascript" src="<c:url value="/modules/admin/controller/popup/more-icons-popup-controller.js"/>"></script>
		<script type="text/javascript" src="<c:url value="/modules/admin/controller/popup/select-internal-data-source-popup-controller.js"/>"></script>
		<script type="text/javascript" src="<c:url value="/modules/admin/controller/popup/select-access-group-popup-controller.js"/>"></script>
		<script type="text/javascript" src="<c:url value="/modules/admin/controller/popup/select-layer-config-popup-controller.js"/>"></script>
		<script type="text/javascript" src="<c:url value="/modules/admin/controller/popup/select-layer-group-popup-controller.js"/>"></script>
		<script type="text/javascript" src="<c:url value="/modules/admin/controller/popup/add-attribute-import-popup-controller.js"/>"></script>
		<script type="text/javascript" src="<c:url value="/modules/admin/controller/popup/associate-attribute-import-popup-controller.js"/>"></script>

   		<!-- Treeview -->
		<script type="text/javascript" src="<c:url value="/static/libs/angular-treeview/ivh-treeview.min.js"/>"></script>

		<script src="<c:url value="/static/libs/angular-treeview/ivh-treeview.extend.js"/>" type="text/javascript"></script>

		<script src="<c:url value="/static/libs/angular-sortable-view/src/angular-sortable-view.js"/>" type="text/javascript"></script>

		<!-- OpenLayers 3 v3.0.0 -->
		<script src="<c:url value="/static/libs/openlayers/ol.js"/>" type="text/javascript"></script>

		<!-- Google Maps -->
		<script type="text/javascript" src="https://maps.googleapis.com/maps/api/js?client=gme-itaipubinacional&sensor=false&channel=geocab"></script>

		<script type="text/javascript" src="<c:url value="/static/libs/eits-directives/default-button/default-button.js"/>"></script>

	 	<style>
	        .popup{
	            background:white;
	            width:250px;
	            height:200px;
	            border-radius:5px;
	        }
			.ol-scale-line {
				background: #ffffff;
				color: #000;
				left: 80px;
			}
		
			.ol-scale-line-inner{
				color: #333; 
				border-color: #333
			}
			
	    </style>
	</head>

	<body class="overflow-hidden container-fluid" style="height: 100%;">
			<header>
				<jsp:include page="../../common/header.jsp"/>
			</header>

			<security:authorize ifAnyGranted="ADMINISTRATOR">
				<style>.map-content{position: absolute; bottom: 0; top: 116px; left: 0; right: 0;}</style>
			</security:authorize>

			<security:authorize ifAnyGranted="USER,MODERATOR">
				<sec:authorize access="principal.password != 'no password'">
					<style>.map-content{position: absolute; bottom: 0; top: 116px; left: 0; right: 0;}</style>
				</sec:authorize>
				<sec:authorize access="principal.password == 'no password'">
					<style>.map-content{position: absolute; bottom: 0; top: 60px; left: 0; right: 0;}</style>
				</sec:authorize>
			</security:authorize>

			<security:authorize access="!isAuthenticated()">
				<style>.map-content{position: absolute; bottom: 0; top: 60px; left: 0; right: 0;}</style>
			</security:authorize>

			<!-- content -->
			<div ui-view></div>
			<!-- /content -->

			<!-- content -->
			<!--<security:authorize ifAnyGranted="ADMINISTRATOR">
			    <div  id="sb-site"  ng-include="'modules/map/ui/interactive-map-view.jsp'" style="position: absolute; bottom: 0; top: 116px; left: 0; right: 0;" ng-controller="MapController"></div>
			</security:authorize>

			<security:authorize ifAnyGranted="USER,MODERATOR">
				<sec:authorize access="principal.password != 'no password'">
			    	<div  id="sb-site" ng-include="'modules/map/ui/interactive-map-view.jsp'" style="position: absolute; bottom: 0; top: 116px; left: 0; right: 0;" ng-controller="MapController"></div>
				</sec:authorize>
				<sec:authorize access="principal.password == 'no password'">
			    	<div  id="sb-site" ng-include="'modules/map/ui/interactive-map-view.jsp'" style="position: absolute; bottom: 0; top: 60px; left: 0; right: 0;" ng-controller="MapController"></div>
				</sec:authorize>
			</security:authorize>

			<security:authorize access="!isAuthenticated()">
				<div  id="sb-site" ng-include="'modules/map/ui/interactive-map-view.jsp'" style="position: absolute; bottom: 0; top: 60px; left: 0; right: 0;" ng-controller="MapController"></div>
			</security:authorize>-->

			<!-- /content -->

			<footer></footer>
		</div>
	</body>
</html>
