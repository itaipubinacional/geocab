<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<!-- Grupo de camadas - Main View -->
<div>
     <div class="navbar">

         <!--Mensagens-->
         <div ng-include="'static/libs/eits-directives/alert/alert.html'" style="margin-bottom: 15px"></div>

         <div class="navbar-inner navbar-container">
			<div ng-switch on="currentState" class="navbar-title" style="float: left; width: 180px;">
			    <span ng-switch-when="layer-group.list"><spring:message code="layer-group-view.LAYER-GROUP" /></span>
			    <span ng-switch-default><spring:message code="layer-group-view.LAYER-GROUP-Loading" /></span>
			</div>
			
			<button ng-show="currentState == LIST_STATE" style="float: right; margin-bottom: 15px;" ng-click="publishLayerGroup()"
			        class="btn btn-warning"><spring:message code="layer-group-view.Post" />
			</button>
			
			<button ng-show="currentState == LIST_STATE" style="float: right; margin-bottom: 15px;" ng-click="saveLayerGroup()"
			        class="btn btn-success"><spring:message code="layer-group-view.Save-ordering"/>
			</button>
             
			<button ng-show="currentState == LIST_STATE" style="float: left; margin-bottom: 15px; width: 122px;" ng-click="newLayerGroup()"
				class="btn btn-default"><div class="icon itaipu-icon-folder" style="float: left;margin-top: 2px;margin-right: 4px;"></div>
				<spring:message code="layer-group-view.New-group"/>
			</button>

         </div>
     </div>
    
    <!-- Partial views dos states -->
	<div ng-switch on="currentState">
        <div ng-switch-when="layer-group.list">
        	<div ng-include="'modules/admin/ui/layer-group/layer-group-list.jsp'"></div>
        </div>
    </div>
</div>
</html>