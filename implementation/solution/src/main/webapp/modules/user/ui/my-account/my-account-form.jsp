<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<!-- My account - Update -->
<div>

	<!--Mensagens-->
        <div class="msg" ng-include="'static/libs/eits-directives/alert/alert.html'" ></div>
    <form>
        <div class="content-tab">
      
            <div class="form-item position-relative" style="width: 300px;">
                <label class="detail-label" required><spring:message code="admin.users.Name"/></label>
                <input name="name" type="text" class="form-control"
                       ng-model="currentEntity.name"
                       placeholder="<spring:message code="admin.users.Name"/>"
                       required maxlength="144" ng-minlength="1"
                       ng-class="{ ngInvalid: form.name.$error.required && (form.$submitted || form.name.$dirty) }"
                       autofocus
                       ng-hover>

                <span ng-show="form.name.$error.required && (form.$submitted || form.name.$dirty)"
                      class="tooltip-validation"><spring:message code="admin.users.Name"/> <spring:message code="admin.users.required"/></span>
            </div>
            <br>           
            <div class="form-item position-relative" style="width: 300px;">
                <label class="detail-label" required><spring:message code="admin.users.E-mail"/></label>
                <input name="address" type="email" id="address" class="form-control"
                       ng-model="currentEntity.email"
                       maxlength="255"
                       placeholder="<spring:message code="admin.users.E-mail"/>"
                       required ng-minlength="1"
                       ng-class="{ ngInvalid: form.address.$error.required && (form.$submitted || form.address.$dirty) }"
                       ng-hover
                       autocomplete="off"
                      />
                <span ng-show="form.address.$error.required && (form.$submitted || form.address.$dirty)"
                      class="tooltip-validation"><spring:message code="admin.users.E-mail"/> <spring:message code="admin.users.required"/></span>
            </div>
  			<br>
  			<div class="form-item position-relative" style="width: 300px;">
                <label class="detail-label" required><spring:message code="admin.users.Password"/></label>
                <input name="password" type="password" id="password" class="form-control"
                       ng-model="currentEntity.newPassword"
                       maxlength="255"
                       placeholder="<spring:message code="admin.users.Password"/>"
                       required ng-minlength="1"
                       ng-class="{ ngInvalid: form.address.$error.required && (form.$submitted || form.address.$dirty) }"
                       ng-hover
                       autocomplete="off"
                      />
                <span ng-show="form.address.$error.required && (form.$submitted || form.address.$dirty)"
                      class="tooltip-validation"><spring:message code="admin.users.E-mail"/> <spring:message code="admin.users.required"/></span>
            </div>
  			<br>
  			<div class="form-item position-relative" style="width: 300px;">
                <label class="detail-label" required><spring:message code="admin.users.Repeat-The-Password"/></label>
                <input name="password" type="password" id="password" class="form-control"
                       ng-model="currentEntity.repeatNewPassword"
                       maxlength="255"
                       placeholder="<spring:message code="admin.users.Repeat-The-Password"/>"
                       required ng-minlength="1"
                       ng-class="{ ngInvalid: form.address.$error.required && (form.$submitted || form.address.$dirty) }"
                       ng-hover
                       autocomplete="off"
                      />
                <span ng-show="form.address.$error.required && (form.$submitted || form.address.$dirty)"
                      class="tooltip-validation"><spring:message code="admin.users.E-mail"/> <spring:message code="admin.users.required"/></span>
            </div>
  			<br>
            
            
        </div>
    </form>
</div>
</html>
