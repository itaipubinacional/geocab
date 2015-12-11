<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<!-- Users - Form -->
<div>
    <form novalidate name="form"
          default-button="{{ (currentState == INSERT_STATE) && 'buttonInsert' || 'buttonUpdate' }}" autocomplete="off">

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
                       maxlength="50"
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
            
            <input style="display:none" type="text" name="fakeusernameremembered"/>
			<input style="display:none" type="password" name="fakepasswordremembered"/>
            
            <div ng-if="currentState == UPDATE_STATE" class="form-item position-relative" style="width: 200px;">
                <label class="detail-label"><spring:message code="admin.users.Password"/></label>
                <input name="password" type="password" class="form-control"
                       placeholder="<spring:message code="admin.users.Password"/>"
                       ng-model="currentEntity.password"
                       maxlength="144"
                       ng-class="{ ngInvalid: form.password.$error.required && (form.$submitted || form.password.$dirty) }"
                       ng-hover
                       autocomplete="off">
            </div>            
            <div ng-if="currentState == INSERT_STATE" class="form-item position-relative" style="width: 200px;">
                <label class="detail-label" required><spring:message code="admin.users.Password"/></label>
                <input name="password" type="password" class="form-control"
                       placeholder="<spring:message code="admin.users.Password"/>"
                       ng-model="currentEntity.password"
                       required maxlength="144"
                       ng-class="{ ngInvalid: form.password.$error.required && (form.$submitted || form.password.$dirty) }"
                       ng-hover>
              	<span ng-show="form.password.$error.required && (form.$submitted || form.password.$dirty)"
                      class="tooltip-validation"><spring:message code="admin.users.Password"/> <spring:message code="admin.users.required"/></span>
            </div>
            <br />
            <div class="form-item position-relative">
            	<label class="detail-label" required><spring:message code="admin.users.Access-profile"/></label>
            	<div class="radio">
	            	<input id="role-adminstrator" type="radio" value="ADMINISTRATOR" ng-model="currentEntity.role" />
	                <label class="radio-label" for="role-adminstrator"><spring:message code="admin.users.Administrator"/></label>
					<br/>
	                <input id="role-moderator" type="radio" value="MODERATOR" ng-model="currentEntity.role" checked="checked"/>
	                <label class="radio-label" for="role-moderator"><spring:message code="admin.users.Moderator"/></label>
	                <br/>
	                <input id="role-user" type="radio" value="USER" ng-model="currentEntity.role" />
	                <label class="radio-label" for="role-user"><spring:message code="admin.users.Common-user"/></label>
            	</div>
            	
            </div>
        </div>
    </form>
</div>


</html>
