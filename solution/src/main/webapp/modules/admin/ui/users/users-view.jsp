<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<!-- Users - Main View -->
<div>
	<div class="navbar">

        <!--Message -->
        <div class ="msg"ng-include="'static/libs/eits-directives/alert/alert.html'"></div>

    	<!-- Barra de Controle -->
        <div class="navbar-inner navbar-container">
            <div ng-switch on="currentState" class="navbar-title">
                <span ng-switch-when="users.list"><spring:message code="admin.users.LIST-OF-USERS"/></span>
                <span ng-switch-when="users.detail"><spring:message code="admin.users.DETAIL-OF-USER"/></span>
                <span ng-switch-when="users.create"><spring:message code="admin.users.NEW-USER"/></span>
                <span ng-switch-when="users.update"><spring:message code="admin.users.UPDATE-USER"/></span>
                <span ng-switch-default><spring:message code="admin.users.LOADING-USERS"/>...</span>
            </div>

            <!-- State List -->
            <button ng-show="currentState == LIST_STATE" style="float: right;"
                class="btn btn-primary"
                ui-sref="users.create"
                title="<spring:message code="admin.users.New-user"/>">
                <spring:message code="admin.users.New-user"/>                
            </button>

            <!-- State Detail -->
			<button ng-show="currentState == DETAIL_STATE" style="float: left; margin-right: 15px; min-width: 40px;"
				class="btn btn-default"
				ui-sref="users.list"><span class="icon itaipu-icon-arrow-left"></span>
            </button>
              <button ng-show="currentState == DETAIL_STATE && currentEntity.enabled " style="float: right;"
                class="btn btn-danger"
                ng-click="changeToDisable(currentEntity)"><spring:message code="admin.users.Disable"/>
            </button>
            <button ng-show="currentState == DETAIL_STATE && !currentEntity.enabled " style="float: right;"
                class="btn btn-default"
                ng-click="changeToEnable(currentEntity)"><spring:message code="admin.users.Enable"/>
            </button>
            <button ng-show="currentState == DETAIL_STATE " style="float: right;"
                class="btn btn-primary"
                ui-sref="users.update( {id:currentEntity.id} )"><spring:message code="admin.users.Update"/>
            </button>

            <!-- State Create | Edit -->
            <button ng-show="currentState == INSERT_STATE || currentState == UPDATE_STATE" style="float: left; margin-right: 15px; min-width: 40px;"
                class="btn btn-default"
                title="<spring:message code="admin.users.Back" />"
                ui-sref="users.list"><span class="icon itaipu-icon-arrow-left"></span>
            </button>
            <!-- State Create -->
            <button ng-show="currentState == INSERT_STATE" style="float: right;"
                class="btn btn-success"
                id="buttonInsert"
                title="<spring:message code="admin.users.Save"/>"
                ng-click="insertUser(currentEntity)"><spring:message code="admin.users.Save"/>
            </button>
            <!-- State Edit -->
            <button ng-show="currentState == UPDATE_STATE" style="float: right;"
                class="btn btn-success"
                id="buttonUpdate"
                title="<spring:message code="admin.users.Save"/>"
                ng-click="updateUser()"><spring:message code="admin.users.Save"/>
            </button>
        </div>
    </div>
    
    <!-- Partial views of states -->
	<div ng-switch on="currentState">
        <div ng-switch-when="users.list">
        	<div ng-include="'modules/admin/ui/users/users-list.jsp'"></div>
        </div>
        <div ng-switch-when="users.detail">
        	<div ng-include="'modules/admin/ui/users/users-detail.jsp'"></div>
        </div>
        <div ng-switch-when="users.create">
        	<div ng-include="'modules/admin/ui/users/users-form.jsp'"></div>	
        </div> 
        <div ng-switch-when="users.update">
        	<div ng-include="'modules/admin/ui/users/users-form.jsp'"></div>
        </div>
        <div ng-switch-default>
        	<div ng-include="'modules/common/loading.jsp'"></div>
        </div>
    </div>
</div>
</html>