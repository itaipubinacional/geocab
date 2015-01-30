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
        <button type="button" class="close" ng-click="close(true)"><span aria-hidden="true">&times;</span><span class="sr-only"><spring:message code="Close" /></span></button>
        <h3 class="modal-title"><spring:message code="admin.layer-config.Associate-access-group" /></h3>
    </div>
    <div class="modal-body" ng-init="initialize();" style="overflow: visible">
        <div ng-include="'assets/libs/eits-directives/alert/alert.html'"></div>

        <form novalidate name="form" default-button="{{buttonInsert}}" style="margin-bottom: 10px; margin-top: 10px;">
            <input type="text" ng-model="data.filter" name="nome" style="width: 300px; float: left;"
                   placeholder="Filtrar nome" class="form-control"/>
            <input type="submit" class="btn btn-default" style="margin-left: 5px" value="Pesquisar" ng-disabled="currentPage == null" ng-click="listGruposAcessosByFilter(data.filter, currentPage.pageable )"/>
        </form>
        <br style="clear: both"/>

        <div ng-show="showLoading" class="grid-loading"></div>
        <div ng-grid="gridOptions" style="height: 335px; border: 1px solid rgb(212,212,212); margin-top: 10px;"></div>

        <div class="gridFooterDiv">
            <pagination style="text-align: center;"
                        total-items="currentPage.total" rotate="false"
                        items-per-page="currentPage.size"
                        max-size="currentPage.totalPages"
                        ng-change="changeToPage(data.filter, currentPage.pageable.pageNumber)"
                        ng-model="currentPage.pageable.pageNumber" boundary-links="true"
                        previous-text="‹" next-text="›" first-text="«" last-text="»">
            </pagination>
        </div>

        <div class="grid-elements-count" ng-show="currentPage.totalElements > 0">
            {{currentPage.numberOfElements}} <spring:message code="of" /> {{currentPage.totalElements}} items
        </div>

    </div>

    <div class="modal-footer">
        <button id="buttonClose" class="btn btn-primary" ng-click="close(true)"><spring:message code="Select" /></button>
        <button class="btn btn-default" ng-click="close(false)"><spring:message code="Close" /></button>
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