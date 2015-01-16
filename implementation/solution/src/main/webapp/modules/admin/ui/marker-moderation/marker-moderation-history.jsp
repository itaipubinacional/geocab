<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>	
	<div class="content-tab" style="width:35%; height: 100%; float:left; margin: 20px;" overflow="auto">
		<span style="z-index: 1000;float:right;cursor:pointer" ng-click="changeToDetail(currentEntity)" class="icon itaipu-icon-close"></span>
		
		<h3><spring:message code="admin.marker-moderation.History" /></h3>
		<hr>
		
		<div ng-repeat="markerModeration in markersModeration " >
		
			<div class="{{markerModeration.status == 'PENDING' ? 'alert warning' : markerModeration.status == 'ACCEPTED' ? 'alert success' : 'alert danger'}}" style="text-align:center;height:75px">								
				<span><b ng-if="markerModeration.status == 'REFUSED'" style="position:relative;right:38%"> {{ $index + 1 }}</b></span>
				<span><b ng-if="markerModeration.status != 'REFUSED'" style="position:relative;right:37.5%"> {{ $index + 1 }}</b></span>
				
				<span><b style="float:left"> {{ currentEntity.user.name }}</b></span>																																						
				
				<span><b ng-if="markerModeration.status == 'REFUSED'" style="line-height:80px"> {{ translateStatus($index) }}</b></span>						
				<span><b ng-if="markerModeration.status != 'REFUSED'" style="line-height:80px;margin-right:27px"> {{ translateStatus($index) }}</b></span>						
													
				<span><b style="float:right"> {{ currentEntity.created | date:'dd/MM/yyyy' }}</b></span>													
								
				<span ng-show="visible && markerModeration.status == 'REFUSED'" id="up-arrow" ><i style="float:right;cursor:pointer;left:64px;top:40px" class="glyphicon glyphicon-chevron-up" ng-click="visible = false" ></i></span>
				<span ng-show="!visible && markerModeration.status == 'REFUSED'"><i style="float:right;cursor:pointer;left:75px;top:40px" class="glyphicon glyphicon-chevron-down" ng-click="visible = true" ></i></span>
												
			</div>

			<div class="alert danger" style="text-align:center" ng-if="visible && markerModeration.status == 'REFUSED'">
				<span><b> Algum motivo </b></span>						
			</div>
			
		</div>
		
	</div>

</html>