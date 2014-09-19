<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!-- HTML5 shim and Respond.js IE8 support of HTML5 elements and media queries -->
<!--[if lt IE 9]>
  <script src="https://oss.maxcdn.com/html5shiv/3.7.2/html5shiv.min.js"></script>
  <script src="https://oss.maxcdn.com/respond/1.4.2/respond.min.js"></script>
<![endif]-->
    
<!-- JQuery -->
<script type="text/javascript" src="<c:url value="/webjars/jquery/2.1.1/jquery.min.js"/>"></script> <!-- FIXME Tirar as versoes -->

<!-- AngularJS -->
<script type="text/javascript" src="<c:url value="/webjars/angularjs/1.2.18/angular.js"/>"></script>
<script type="text/javascript" src="<c:url value="/webjars/angular-ui-router/0.2.10/angular-ui-router.min.js"/>"></script>
<script type="text/javascript" src="<c:url value="/webjars/ng-grid/2.0.11/ng-grid.min.js"/>"></script>
<script type="text/javascript" src="<c:url value="/webjars/angular-ui-bootstrap/0.11.0/ui-bootstrap-tpls.min.js"/>"></script>

<!-- Bootstrap -->
<script type="text/javascript" src="<c:url value="/webjars/bootstrap/3.2.0/js/bootstrap.min.js"/>"></script>

<!-- DWR -->
<script type="text/javascript" src="<c:url value="/broker/engine.js"/>"></script>
<script type="text/javascript" src="<c:url value="/broker/util.js"/>"></script>


<script type="text/javascript" src="<c:url value="/static/js/eits/broker/eits-broker.js"/>"></script>
