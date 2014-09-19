<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<!-- Data source - Form -->
<div>
    <form novalidate name="form"
          default-button="{{ (currentState == INSERT_STATE) && 'buttonInsert' || 'buttonUpdate' }}">

        <div class="content-tab">            
            <div class="form-item position-relative" style="width: 300px;">
                <label class="detail-label" required><spring:message code="Name"/></label>
                <input name="name" type="text" class="form-control"
                       ng-model="currentEntity.name"
                       placeholder="<spring:message code="admin.datasource.Enter-your-name"/>"
                       required maxlength="144" ng-minlength="1"
                       ng-class="{ ngInvalid: form.name.$error.required && (form.$submitted || form.name.$dirty) }"
                       autofocus
                       ng-hover>
                <span ng-show="form.name.$error.required && (form.$submitted || form.name.$dirty)"
                      class="tooltip-validation"><spring:message code="Name"/> <spring:message code="required"/></span>
            </div>
            <br>

            <div class="form-item position-relative" style="width: 500px;">
                <label class="detail-label" required><spring:message code="Address"/></label>
                <input name="address" type="text" class="form-control"
                       ng-model="currentEntity.url"
                       numbers-only
                       maxlength="255"
                       placeholder="http://"
                       required ng-minlength="1"
                       ng-class="{ ngInvalid: form.address.$error.required && (form.$submitted || form.address.$dirty) }"
                       ng-hover>
                <span ng-show="form.address.$error.required && (form.$submitted || form.address.$dirty)"
                      class="tooltip-validation"><spring:message code="Address"/> <spring:message code="required"/></span>
            </div>
            <br>

            <div>
                <input id="authenticationRequired" type="checkbox"
                       ng-model="data.showFields"
                       ng-change="clearFields()" ng-checked="data.showFields">
                <label for="authenticationRequired" title="Authentication required"><spring:message code="Authentication"/> <spring:message code="required"/></label>
            </div>
            <br ng-if="data.show"/>

            <div class="form-item position-relative" ng-if="data.showFields" style="width: 200px;">
                <label class="detail-label" required><spring:message code="Username"/></label>
                <input name="username" type="text" class="form-control"
                       ng-model="currentEntity.login"
                       placeholder="<spring:message code="admin.datasource.Enter-your-username"/>"
                       required maxlength="144" ng-minlength="1"
                       ng-class="{ ngInvalid: form.username.$error.required && (form.$submitted || form.username.$dirty) }"
                       ng-hover>
				<span ng-show="form.username.$error.required && (form.$submitted || form.username.$dirty)"
                      class="tooltip-validation"><spring:message code="Username"/> <spring:message code="required"/></span>
            </div>

            <br/>
            
            <input type="text" style="display: none"/>

            <div class="form-item position-relative" ng-if="data.showFields" style="width: 200px;">
                <label class="detail-label" required><spring:message code="Password"/></label>
                <input name="password" type="password" class="form-control"
                       placeholder="<spring:message code="admin.datasource.Enter-your-password"/>"
                       ng-model="currentEntity.password"
                       required maxlength="144"
                       ng-class="{ ngInvalid: form.password.$error.required && (form.$submitted || form.password.$dirty) }"
                       ng-hover>
				<span ng-show="form.password.$error.required && (form.$submitted || form.password.$dirty)"
                      class="tooltip-validation"><spring:message code="Password"/> <spring:message code="required"/></span>
            </div>
        </div>
    </form>
</div>
</html>
