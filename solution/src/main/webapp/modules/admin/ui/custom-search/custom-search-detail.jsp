<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<!-- Fonte de dados - Form -->
<div>

    <form novalidate name="form"
          default-button="{{ (currentState == INSERT_STATE) && 'buttonInsert' || 'buttonUpdate' }}">

        <div class="content-tab">

            <div class="form-item position-relative" style="width: 250px; margin-bottom: 15px;">
                <b class="detail-label"><spring:message code="admin.custom-search.Name" /></b>
                <br>
                <span class="detail-value">{{currentEntity.name}}</span>
            </div>
            <br/>

            <div class="form-item position-relative" style="width: 250px; margin-bottom: 15px;">
                <b class="detail-label"><spring:message code="admin.custom-search.Data-source" /></b>
                <br>
                <span class="detail-value">{{currentEntity.layer.dataSource.name}}</span>
            </div>
            <br/>

            <div class="form-item position-relative">
                <b class="detail-label"><spring:message code="admin.custom-search.Layer-name" /></b>
                <br>
                <span class="detail-value">{{currentEntity.layer.name}}</span>
            </div>
            <br/>

            <div class="form-item position-relative">
                <b class="detail-label"><spring:message code="admin.custom-search.Layer-title" /></b>
                <br>
                <span class="detail-value">{{currentEntity.layer.title}}</span>
            </div>
            <br/>

            <div class="form-item position-relative">
                <b class="detail-label"><spring:message code="admin.custom-search.Symbology" /></b>
                <br>
                <img style="border: solid 1px #c9c9c9;" ng-if="currentEntity.layer.dataSource.url != null" ng-src="{{currentEntity.layer.legend}}"/>
                    <img style="border: solid 1px #c9c9c9;" ng-if="currentEntity.layer.dataSource.url == null" ng-src="{{currentEntity.layer.icon}}"/>
            </div>
            <br/>

            <hr>

            <tabset>
                <tab heading="<spring:message code="admin.custom-search.Layer-fields" />">
                    <div ng-grid="gridlayersOptions" style="height: 335px; border: 1px solid rgb(212,212,212); margin-top: 40px;"></div>
                </tab>
                <tab heading="<spring:message code="admin.custom-search.Access" />">

                    <div class="form-item position-relative" style="width: 100%;">
                        <div ng-grid="gridGroupOptions" style="height: 300px; border: 1px solid rgb(212,212,212); margin-top: 40px;">
                   
                        </div>
                    </div>

                </tab>
            </tabset>
        </div>
    </form>
</div>
</html>
