<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
	<head>
		<title><spring:message code="authentication.Authentication" /></title>
		
		<!-- Styles -->
		<link rel="stylesheet" type="text/css" href="<c:url value="/static/style/authentication/authentication.css"/>" />
		<jsp:include page="../../default-styles.jsp"/>
		
		<!-- Scripts -->
		<jsp:include page="../../default-scripts.jsp"/>

		<style>
		.create-account .modal-dialog {
		  width: 330px;
		  top: 150;
		}
		
		.forget-password-modal .modal-dialog {
		  width: 330px;
		  top: 150;
		}
		
		</style>
	</head>
<body>
	
	<div class="container-fluid">

		<!-- content -->
		<div ui-view class="content"></div>
		<!-- /content -->

	</div>
	
	<!--
		Controllers 
	-->
    <!-- Main -->
    <script type="text/javascript" src="modules/authentication/authentication-main.js"></script>
	<script type="text/javascript" src="modules/authentication/controller/authentication-controller.js"></script>
	<script type="text/javascript" src="modules/authentication/controller/popup/create-user-popup-controller.js"></script>
	<script type="text/javascript" src="modules/authentication/controller/popup/forget-password-popup-controller.js"></script>
	
	<!--
		Directives 
	-->
	<script type="text/javascript" src="static/libs/eits-directives/default-button/default-button.js"></script>
	<script type="text/javascript" src="static/libs/eits-directives/auto-fill/auto-fill.js"></script>
	
</body>
</html>
















