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
		<title>Hello <sec:authentication property="principal.name"/></title>
		
		<!-- Styles -->
		<jsp:include page="../../default-styles.jsp"/>
		
		<!-- Scripts -->
		<jsp:include page="../../default-scripts.jsp"/> <!-- FIXME Deixar caminhos relativos.. -->
		
		<!-- Controllers -->
		
		<!-- Main -->
		<script type="text/javascript" src="modules/admin/admin-main.js"></script>
	</head>
	
	<body>
	
	<!-- container -->
	<div class="container-fluid">

		<!-- header -->
		<jsp:include page="../../common/header.jsp"/>
		<!-- /header -->
		

		<!-- content -->
		<div ui-view class="content"></div>
		<!-- /content -->

	</div>
	
		<!--
		Controllers 
		-->
		<script type="text/javascript" src="modules/abstract-crud-controller.js"></script>
   		<script type="text/javascript" src="modules/admin/controller/data-source-controller.js"></script>
   		<script type="text/javascript" src="modules/admin/controller/users-controller.js"></script>
   		
	</body>
</html>