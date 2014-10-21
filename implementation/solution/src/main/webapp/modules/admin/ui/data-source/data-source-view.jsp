<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<!-- Data source - Main View -->
<div>
	<div class="navbar">

        <!--Message -->
        <div class="msg" ng-include="'static/libs/eits-directives/alert/alert.html'"></div>

    	<!-- Bar control -->
        <div class="navbar-inner navbar-container">
            <div ng-switch on="currentState" class="navbar-title">
                <span ng-switch-when="data-source.list"><spring:message code="admin.datasource.LIST-OF-GEOGRAPHIC-DATA-SOURCE"/></span>
                <span ng-switch-when="data-source.detail"><spring:message code="admin.datasource.DETAIL-OF-GEOGRAPHIC-DATA-SOURCE"/></span>
                <span ng-switch-when="data-source.create"><spring:message code="admin.datasource.NEW-GEOGRAPHIC-DATA-SOURCE"/></span>
                <span ng-switch-when="data-source.update"><spring:message code="admin.datasource.UPDATE-GEOGRAPHIC-DATA-SOURCE"/></span>
                <span ng-switch-default><spring:message code="admin.datasource.GEOGRAPHIC-DATA-SOURCE"/> - <spring:message code="Loading"/>...</span>
            </div>

            <!-- State List -->
            <button ng-if="currentState == LIST_STATE" style="float: right;"
                class="btn btn-primary"
                ui-sref="data-source.create"><spring:message code="admin.datasource.New-Data-Source"/>
            </button>

            <!-- State Detail -->
			<button ng-if="currentState == DETAIL_STATE" style="float: left; margin-right: 15px; min-width: 40px;"
				class="btn btn-default"
				ui-sref="data-source.list"><span class="icon itaipu-icon-arrow-left"></span>
            </button>
            <button ng-if="currentState == DETAIL_STATE" style="float: right;"
                class="btn btn-danger"
                ng-click="changeToRemove(currentEntity)"><spring:message code="Remove"/>
            </button>
            <button ng-if="currentState == DETAIL_STATE" style="float: right;"
                class="btn btn-primary"
                ui-sref="data-source.update( {id:currentEntity.id} )"><spring:message code="Update"/>
            </button>

            <!-- State Create | Update -->
            <button ng-if="currentState == INSERT_STATE || currentState == UPDATE_STATE" style="float: left; margin-right: 15px; min-width: 40px;"
                class="btn btn-default"
                ui-sref="data-source.list"><span class="icon itaipu-icon-arrow-left"></span>
            </button>
			<button ng-if="currentState == (INSERT_STATE || currentState == UPDATE_STATE) && isUrlChecked()" style="float: right;"
				class="btn btn-warning"
				ng-click="testDataSourceConnection(currentEntity)"><spring:message code="admin.datasource.Connection-test"/>
            </button>
            <!-- State Create -->
            <button ng-if="currentState == INSERT_STATE" style="float: right;"
                class="btn btn-success"
                id="buttonInsert"
                ng-click="insertDataSource()"><spring:message code="Save"/>
            </button>
            
            <!-- State Update -->
            <button ng-if="currentState == UPDATE_STATE" style="float: right;"
                class="btn btn-success"
                id="buttonUpdate"
                ng-click="updateDataSource()"><spring:message code="Save"/>
            </button>
        </div>
    </div>
    
    <!-- Partial views of states -->
	<div ng-switch on="currentState">
        <div ng-switch-when="data-source.list">
        	<div ng-include="'modules/admin/ui/data-source/data-source-list.jsp'"></div>
        </div>
        <div ng-switch-when="data-source.detail">
        	<div ng-include="'modules/admin/ui/data-source/data-source-detail.jsp'"></div>
        </div>
        <div ng-switch-when="data-source.create">
        	<div ng-include="'modules/admin/ui/data-source/data-source-form.jsp'"></div>	
        </div> 
        <div ng-switch-when="data-source.update">
        	<div ng-include="'modules/admin/ui/data-source/data-source-form.jsp'"></div>
        </div>
        <div ng-switch-default>
        	<div ng-include="'modules/loading.html'"></div>
        </div>
    </div>
</div>
</html>