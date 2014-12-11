<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html xmlns:ng="http://angularjs.org">
	<sec:authorize access="isAnonymous()">
		<c:redirect url="./authentication"/>
    </sec:authorize>
    
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title>GeoCAB - Cultivando água boa </title>
		
		<!-- Styles -->
		<jsp:include page="../../default-styles.jsp"/>
		
		<!-- Scripts -->
		<jsp:include page="../../default-scripts.jsp"/> <!-- FIXME Deixar caminhos relativos.. -->
		
		<link href="<c:url value="/static/libs/angular-ui-tree/css/angular-ui-tree.min.css"/>" type="text/css" rel="stylesheet" />
		
		<script type="text/javascript" src="<c:url value="/static/libs/angular-ui-tree/js/angular-ui-tree.min.js"/>"></script>
		
		<!-- Controllers -->
		
		<!-- Main -->
		<script type="text/javascript" src="modules/admin/admin-main.js"></script>
	</head>
	
	<body>
	
	<!-- container -->
	<div class="container-fluid">
		<header>
				<jsp:include page="../../common/header.jsp"/>
		</header>

		<!-- content -->
		<div ui-view class="content"></div>
		<!-- /content -->
		
		<footer>
   		</footer>

	</div>
	
	<!-- Controllers -->
	<script type="text/javascript" src="modules/abstract-crud-controller.js"></script>
	<script type="text/javascript" src="modules/admin/controller/popup/select-tools-popup-controller.js"></script>
	<script type="text/javascript" src="modules/admin/controller/popup/select-config-layer-access-group-popup-controller.js"></script>
	<script type="text/javascript" src="modules/admin/controller/popup/select-users-ldap-popup-controller.js"></script>
	<script type="text/javascript" src="modules/admin/controller/popup/update-attribute-popup-controller.js"></script>
	<script type="text/javascript" src="modules/admin/controller/popup/add-attribute-popup-controller.js"></script>
	<script type="text/javascript" src="modules/admin/controller/popup/select-layer-group-popup-controller.js"></script>
	<script type="text/javascript" src="modules/admin/controller/popup/select-data-source-popup-controller.js"></script>
	<script type="text/javascript" src="modules/admin/controller/popup/select-access-group-popup-controller.js"></script>
	<script type="text/javascript" src="modules/admin/controller/popup/select-layer-config-popup-controller.js"></script>
	<script type="text/javascript" src="modules/admin/controller/popup/add-fields-popup-controller.js"></script>
	<script type="text/javascript" src="modules/admin/controller/popup/select-layers-popup-controller.js"></script>
	<script type="text/javascript" src="modules/admin/controller/popup/select-custom-search-popup-controller.js"></script>
	<script type="text/javascript" src="modules/admin/controller/popup/more-icons-popup-controller.js"></script>
	<script type="text/javascript" src="modules/admin/controller/data-source-controller.js"></script>
	<script type="text/javascript" src="modules/admin/controller/layer-group-controller.js"></script>
	<script type="text/javascript" src="modules/admin/controller/layer-config-controller.js"></script>
	<script type="text/javascript" src="modules/admin/controller/custom-search-controller.js"></script>
	<script type="text/javascript" src="modules/admin/controller/access-group-controller.js"></script>
	<script type="text/javascript" src="modules/admin/controller/popup/layer-group-popup-controller.js"></script>
	<script type="text/javascript" src="modules/admin/controller/users-controller.js"></script>
	<script type="text/javascript" src="static/libs/eits-directives/default-button/default-button.js"></script>
   		
	</body>
</html>
