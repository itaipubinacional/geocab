<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
	<span style="z-index: 1000;position:absolute;top:145px;left:503px;cursor:pointer" ng-click="changeToDetail(currentEntity)" class="icon itaipu-icon-close"></span>
	<div class="content-tab" style="width:35%; height: 100%; float:left; margin: 20px;" overflow="auto">
	
		<h3><spring:message code="admin.marker-moderation.History" /></h3>
		<hr>
		
		<div class="alert danger" style="text-align:center;height:75px">
			<span><b style="float:left"> {{ marker.user.name }} Usuario</b></span>						
			<span><b style="line-height:80px"> Recusado</b></span>						
			<span><b style="float:right"> {{ marker.created | date:'dd/MM/yyyy' }} 14/01/2015</b></span>					
			<span><i style="float:right;cursor:pointer;left:64px;top:40px" class="glyphicon glyphicon-chevron-up" ng-click="showFields(false)" ng-if="!hiding"></i></span>
			<span><i style="float:right;cursor:pointer;left:75px;top:40px" class="glyphicon glyphicon-chevron-down" ng-click="showFields(true)" ng-if="hiding"></i></span>
		</div>
		
		<div class="alert danger" style="text-align:center" ng-if="!hiding">
			<span><b> Algum motivo </b></span>						
		</div>
		
	</div>

</html>