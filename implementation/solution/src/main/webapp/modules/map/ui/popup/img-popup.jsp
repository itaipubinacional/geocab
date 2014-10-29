<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags"  %>
<!DOCTYPE html>
<html>

<div class="modal-content" style="width: 676px;">

	<!-- <span class="icon itaipu-icon-close sidebar-close" ng-click="close(true)"></span> -->

	<div class="modal-body" ng-init="initialize();" style="overflow-y: auto; max-height: 550px;">

       <img ng-src="{{ img }}" style="width: 640px; height: 480px;"> 
        
	</div>
</div>

</html>