<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<!-- Fonte de Dados - Detail -->
<div>
    <form>
        <div class="content-tab">
            <div class="form-item" style="width: 500px;">
                <b class="detail-label"><spring:message code="admin.access-group.Name"/></b>
                <br>
                <span class="detail-value">{{currentEntity.name}}</span>
            </div>
            <br>

            <div class="form-item">
                <b class="detail-label"><spring:message code="admin.access-group.Description"/></b>
                <br>
                <span class="detail-value">{{currentEntity.description}}</span>
            </div>
            <br>

            <tabset>
                <tab heading="<spring:message code="admin.access-group.User"/>" ng-if="currentEntity.id != 1">
                    <div ng-grid="gridUsers" style="height: 300px; width: 100%; border: 1px solid rgb(212,212,212); margin-top: 20px;">
                    </div>
                </tab>
                <tab heading="<spring:message code="admin.access-group.Layers"/>">
                    <div ng-grid="gridLayers" style="height: 300px; width: 100%; border: 1px solid rgb(212,212,212); margin-top: 20px;">
                    </div>
                </tab>
                <tab heading="<spring:message code="admin.access-group.Custom-Searchs"/>">
                    <div ng-grid="gridCustomSearch" style="height: 300px; width: 100%; border: 1px solid rgb(212,212,212); margin-top: 20px;">
                    </div>
                </tab>
                <tab heading="Ferramentas">
                    <div ng-grid="gridTools" style="height: 300px; width: 100%; border: 1px solid rgb(212,212,212); margin-top: 20px;">
                    </div>
                </tab>
                
            </tabset>
        </div>
    </form>
</div>
</html>
