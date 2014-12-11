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
        <button type="button" class="close" ng-click="close(true)"><span aria-hidden="true">&times;</span><span class="sr-only"><spring:message code="admin.custom-search.Close"/></span></button>
        <h3 class="modal-title"><spring:message code="admin.custom-search.Add-fields"/></h3>
    </div>
    <div class="modal-body" ng-init="initialize();" style="overflow: visible">
        <div class="msg" ng-include="'static/libs/eits-directives/alert/alert.html'"></div>

        <form novalidate name="form" default-button="{{buttonInsert}}" style="margin-bottom: 10px; margin-top: 10px;">
            <input type="text" ng-model="filterOptions.filterText" name="name" style="width: 300px; float: left;"
                   placeholder="<spring:message code="admin.custom-search.Filter-fields"/>" class="form-control"
                   required="true"
                   ng-class="{ ngInvalid: form.name.$error.required && (form.$submitted || form.name.$dirty) }"/>
        </form>

        <br style="clear: both"/>

        <div ng-show="showLoading" class="grid-loading"></div>
        <div ng-grid="gridOptions" style="height: 335px; border: 1px solid rgb(212,212,212); margin-top: 10px;"></div>

        <div style="float: right; margin-top: 10px; color: darkgray; font-size: 14px;" ng-show="layers.length > 0">
            {{fields.length}} <spring:message code="admin.custom-search.Itens"/>
        </div>

    </div>



    <div class="modal-footer">
        <button id="buttonClose" ng-disabled="gridOptions.selectedItems.length == 0" class="btn btn-primary" ng-click="close(false)"><spring:message code="admin.custom-search.Select"/></button>
        <button class="btn btn-default" ng-click="close(true)"><spring:message code="admin.custom-search.Close"/></button>
    </div>
</div>

<script type="text/javascript">
    /**
     * JQuery usado para nao permitir que a popup seja aberta novamente toda vez que o usuario teclar enter
     */
    $(document).ready(function () {
        $("#hide").focus();
        $("#hide").hide();
    });
</script>

</html>