<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<!-- Layer config - Main View -->
<div>
    <div class="navbar">

        <!--Message-->
        <div class="msg" ng-include="'static/libs/eits-directives/alert/alert.html'"></div>

        <!-- Bar controller -->
        <div class="navbar-inner navbar-container">
            <div ng-switch on="currentState" class="navbar-title">
                <span ng-switch-when="layer-config.list"><spring:message code="admin.layer-config.LIST-OF-LAYERS"/></span>
                <span ng-switch-when="layer-config.detail"><spring:message code="admin.layer-config.LAYER-DETAIL"/></span>
                <span ng-switch-when="layer-config.create"><spring:message code="admin.layer-config.NEW-LAYER"/></span>
                <span ng-switch-when="layer-config.update"><spring:message code="admin.layer-config.UPDATE-LAYER"/></span>
                <span ng-switch-default><spring:message code="admin.layer-config.LIST-OF-LAYERS"/> - <spring:message code="Loading"/>...</span>
            </div>

            <!-- State List -->
            <button ng-if="currentState == LIST_STATE" style="float: right;"
                    class="btn btn-primary"
                    ui-sref="layer-config.create"><spring:message code="admin.layer-config.New-layer"/>
            </button>

            <!-- State Detail -->
            <button ng-if="currentState == DETAIL_STATE" style="float: left; margin-right: 15px; min-width: 40px;"
                    class="btn btn-default"
                    ui-sref="layer-config.list"><span class="icon itaipu-icon-arrow-left"></span>
            </button> 
            
            <button ng-if="currentState == DETAIL_STATE" style="float: right;"
                    class="btn btn-danger"
                    ng-click="changeToRemove(currentEntity)"><spring:message code="Remove"/>
            </button>
            <button ng-if="currentState == DETAIL_STATE" style="float: right;"
                    class="btn btn-primary"
                    ui-sref="layer-config.update( {id:currentEntity.id} )"><spring:message code="Update"/>
            </button>

            <!-- State Create | Update -->
            <button ng-if="currentState == INSERT_STATE || currentState == UPDATE_STATE"
                    style="float: left; margin-right: 15px; min-width: 40px;"
                    class="btn btn-default"
                    ui-sref="layer-config.list"
                    title="<spring:message code="admin.layer-config.Back"/>" >
                                        
                    <span class="icon itaipu-icon-arrow-left"></span>
                    
            </button>

            <!-- State Create -->
            <button ng-if="currentState == INSERT_STATE" style="float: right;"
                    class="btn btn-success"
                    id="buttonInsert"
                    ng-click="insertLayer(currentEntity)"><spring:message code="Save"/>
            </button>
            <!-- State Update -->
            <button ng-if="currentState == UPDATE_STATE" style="float: right;"
                    class="btn btn-success"
                    id="buttonUpdate"
                    ng-click="updateLayer(currentEntity)"><spring:message code="Save"/>
            </button>
        </div>
    </div>

    <!-- Partial views of states -->
    <div ng-switch on="currentState">
        <div ng-switch-when="layer-config.list">
            <div ng-include="'modules/admin/ui/layer-config/layer-config-list.jsp'"></div>
        </div>
        <div ng-switch-when="layer-config.detail">
            <div ng-include="'modules/admin/ui/layer-config/layer-config-detail.jsp'"></div>
        </div>
        <div ng-switch-when="layer-config.create">
            <div ng-include="'modules/admin/ui/layer-config/layer-config-form.jsp'"></div>
        </div>
        <div ng-switch-when="layer-config.update">
            <div ng-include="'modules/admin/ui/layer-config/layer-config-form.jsp'"></div>
        </div>
        <div ng-switch-default>
            <div ng-include="'modules/common/loading.jsp'"></div>
        </div>
    </div>
</div>
</html>