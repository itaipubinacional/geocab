<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>

	<!-- OpenLayers 3  -->
	<link rel="stylesheet" href="static/libs/openlayers/ol.css" type="text/css">
	
	<!-- OpenLayers 3 -->
	<script src="static/libs/openlayers/ol.js" type="text/javascript"></script>
	
	<!-- Google Maps -->
	<script type="text/javascript" src="http://maps.googleapis.com/maps/api/js?client=gme-itaipubinacional&sensor=false&channel=geocab"></script>

	<!-- Posting evaluation - List -->
	<div style="width:30%;float:left">
	        
		<!-- Filter Bar -->
		<div class="search-div">
			<form>
				<input type="text" ng-model="data.filter" class="form-control" title="<spring:message code="admin.users.Search"/>" placeholder="<spring:message code="admin.users.Name-or-E-mail"/>" style="float:left; width:300px"/>
				<input type="submit" value="<spring:message code="admin.users.Search"/>" class="btn btn-default" ng-disabled="currentPage == null"
				       ng-click=""
				       title="<spring:message code="admin.users.Search"/>"/>
		    </form>
		</div>
		
		<div ng-grid="gridOptions" style="height: 499px;border: 1px solid rgb(212,212,212);"></div>					
		
		<div class="gridFooterDiv">
		       <pagination style="text-align: center"
		                   total-items="currentPage.total" rotate="false"
		                   items-per-page="currentPage.size"
		                   max-size="currentPage.totalPages"
		                   ng-change="changeToPage(data.filter, currentPage.pageable.pageNumber)"
		                   ng-model="currentPage.pageable.pageNumber" boundary-links="true"
		                   previous-text="‹" next-text="›" first-text="«" last-text="»">
		       </pagination>
		</div> 	
		 
		    <div class="grid-elements-count">
		        {{currentPage.numberOfElements}} <spring:message code="admin.users.of"/> {{currentPage.totalElements}} <spring:message code="admin.users.items"/>
		    </div>
	
	</div>
	
	<!-- Map -->
	<div style="width:70%;float:right">
	
		<!-- Openlayer Map -->
		<div id="olmap"
				style="height: 499px; width:50%"> 
			<div id="popup" class="ol-popup">
				<div id="popup-content"></div>
			</div>
			<div id="info"></div>
		</div>
	
	</div>
	
	
	
	
	
	
	
	
	
	
	
	
	
	
</html>