<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>    
    <div class="content-tab" style="width:36%; height: 100%; float:left; margin: 20px;">
    	<span style="z-index: 1000;float:right;cursor:pointer" ng-click="changeToDetail(currentEntity)" class="icon itaipu-icon-close"></span>
    	<div style="overflow:auto;height:78vh;width:59vh">

	        <h3><spring:message code="admin.marker-moderation.History" /></h3>
	        <hr>
	         
	        <div ng-repeat="markerModeration in markersModeration " >
	         
	            <div class="{{markerModeration.status == 'PENDING' ? 'alert warning' : markerModeration.status == 'ACCEPTED' ? 'alert success' : 'alert danger'}}" style="text-align:center;height:75px">                                                                 
	                 
	                <div>
	                    <span><b style="float:left"> {{ currentEntity.user.name }}</b></span>           
	                </div>
	 
	                <div style="position:relative">      
	                    <span><b style="position:absolute;top:25px;left:13px" > {{ $index + 1 }}</b></span>
	                </div>                                                                                                                                                
	                 
	                <div style="position:relative">                                       
	                    <span><b  style="position:absolute;top:15px;left:45%" > {{ translateStatus($index) }}</b></span>                        
	                </div>                                        
	                 
	                <div style="position:relative">
	                    <span><b style="float:right;position:absolute;top:0px;right:0px"> {{ currentEntity.created | date:'dd/MM/yyyy' }}</b></span>                                                
	                </div>
	                     
	                <div>             
	                    <span ng-show="visible && markerModeration.status == 'REFUSED'" id="up-arrow" ><i style="float:right;cursor:pointer;left:-12px;top:40px" class="glyphicon glyphicon-chevron-up" ng-click="visible = false" ></i></span>
	                    <span ng-show="!visible && markerModeration.status == 'REFUSED'"><i style="float:right;cursor:pointer;top:40px" class="glyphicon glyphicon-chevron-down" ng-click="visible = true; listMotivesByMarkerModeration(markerModeration.id)" ></i></span>
	                </div>
	                                                 
	            </div>
	 
	            <div class="alert danger" style="text-align:center" ng-if="visible && markerModeration.status == 'REFUSED'">
	                <span><b> {{motiveMarkerModeration[0].motive.name}} - {{motiveMarkerModeration[0].description}} </b></span>                      
	            </div>
	             
	        </div>
        </div>
    </div>
 
</html>