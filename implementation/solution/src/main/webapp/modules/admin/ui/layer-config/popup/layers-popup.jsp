<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<div class="modal-content">
    <div class="modal-header">
        <button type="button" class="close" ng-click="close(true)"><span aria-hidden="true">&times;</span><span class="sr-only">Close</span></button>
        <h3 class="modal-title"><spring:message code="admin.layer-config.Associate-layer"/></h3>
    </div>
    <div class="modal-body" ng-init="initialize();" style="overflow: visible">
        <div ng-include="'static/libs/eits-directives/alert/alert.html'"></div>

        <form novalidate name="form" default-button="{{buttonInsert}}" style="margin-bottom: 10px; margin-top: 10px;">
            <input type="text" ng-model="filterOptions.filterText" name="nome" style="width: 300px; float: left;"
                   placeholder="<spring:message code="admin.layer-config.Filter-by-name-or-title"/>" class="form-control"
                   required="true"
                   ng-class="{ ngInvalid: form.nome.$error.required && (form.$submitted || form.nome.$dirty) }"/>
        </form>

        <br style="clear: both"/>

        <div ng-show="showLoading" class="grid-loading"></div>
        <div class="grid-align" ng-grid="gridOptions" style="height: 335px; border: 1px solid rgb(212,212,212); margin-top: 10px;"></div>
    </div>

    <div class="grid-elements-count" style="margin: -14px 20px 0 0;">{{numberOfRegisters}} <spring:message code="admin.layer-config.Register"/>(s)</div>

    <div class="modal-footer">
        <button id="buttonClose" ng-disabled="selectedEntity == null" class="btn btn-primary" ng-click="close(false)">Selecionar</button>
        <button class="btn btn-default" ng-click="close(true)"><spring:message code="map.Close" /></button>
    </div>
</div>

<script type="text/javascript">
    /**
     * Jquery usado para nao permitir que a popup seja aberta novamente toda vez que o usuario teclar enter
     */
    $(document).ready(function () {
        $("#hide").focus();
        $("#hide").hide();
    });
</script>

</html>