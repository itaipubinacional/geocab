<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<!-- Fonte de dados - Main View -->
<div>
	<div class="navbar">

        <!--Mensagens-->
        <div class="msg" ng-include="'static/libs/eits-directives/alert/alert.html'"></div>

    	<!-- Control Bar -->
        <div class="navbar-inner navbar-container">
            <div ng-switch on="currentState" class="navbar-title">
                <span ng-switch-when="access-group.list"><spring:message code="admin.access-group.List-Access-Group"/></span>
                <span ng-switch-when="access-group.detail"><spring:message code="admin.access-group.Access-Group-detail"/></span>
                <span ng-switch-when="access-group.create"><spring:message code="admin.access-group.New-Access-Group"/></span>
                <span ng-switch-when="access-group.update"><spring:message code="admin.access-group.Change-Access-Group"/></span>
                <span ng-switch-default><spring:message code="admin.access-group.ACCESS-GROUP-Loading"/></span>
            </div>

            <!-- State Listar -->
            <button ng-show="currentState == LIST_STATE" style="float: right;"
                class="btn btn-primary"
                ui-sref="access-group.create"><spring:message code="admin.access-group.New-Access-Group"/>
            </button>

            <!-- State Detalhe -->
			<button ng-show="currentState == DETAIL_STATE" style="float: left; margin-right: 15px; min-width: 40px;"
				class="btn btn-default"
				ui-sref="access-group.list"><span class="icon itaipu-icon-arrow-left"></span>
            </button>
            <button ng-show="currentState == DETAIL_STATE && currentEntity.id != 1" style="float: right;"
                class="btn btn-danger"
                ng-click="changeToRemove(currentEntity)"><spring:message code="admin.access-group.Delete"/>
            </button>
            <button ng-show="currentState == DETAIL_STATE" style="float: right;"
                class="btn btn-primary"
                ui-sref="access-group.update( {id:currentEntity.id} )"><spring:message code="admin.access-group.Update"/>
            </button>

            <!-- State Criar | Editar -->
            <button ng-show="currentState == INSERT_STATE || currentState == UPDATE_STATE" style="float: left; margin-right: 15px; min-width: 40px;"
                class="btn btn-default"
                ui-sref="access-group.list"><span class="icon itaipu-icon-arrow-left"></span>
            </button>
        </div>
    </div>
    
    <!-- Partial views dos states -->
	<div ng-switch on="currentState">
        <div ng-switch-when="access-group.list">
        	<div ng-include="'modules/admin/ui/access-group/access-group-list.jsp'"></div>
        </div>
        <div ng-switch-when="access-group.detail">
        	<div ng-include="'modules/admin/ui/access-group/access-group-detail.jsp'"></div>
        </div>
        <div ng-switch-when="access-group.create">
        	<div ng-include="'modules/admin/ui/access-group/access-group-form.jsp'"></div>	
        </div> 
        <div ng-switch-when="access-group.update">
        	<div ng-include="'modules/admin/ui/access-group/access-group-form.jsp'"></div>
        </div>
        <div ng-switch-default>
        	<div ng-include="'modules/common/loading.jsp'"></div>
        </div>
    </div>
</div>
</html>