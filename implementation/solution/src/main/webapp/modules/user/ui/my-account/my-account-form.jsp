<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<style>
</style>
<!-- My account - Update -->
<div>

	<!--Mensagens-->
        <div class="msg" ng-include="'static/libs/eits-directives/alert/alert.html'" ></div>
    <form name="form" novalidate default-button="buttonUpdate">
        <div class="content-tab">
      		
      		
            <div class="form-item position-relative" style="width: 300px;">
                <label class="detail-label" ><spring:message code="admin.users.Name"/></label>
                <input name="name" type="text" class="form-control"
                       ng-model="currentEntity.name"
                       placeholder="<spring:message code="admin.users.Name"/>"
                       required maxlength="144" ng-minlength="1"
                       ng-class="{ ngInvalid: form.name.$error.required && (form.$submitted || form.name.$dirty) }"
                       autofocus
                       autocomplete="off"
                       ng-hover>

                <span ng-show="form.name.$error.required && (form.$submitted || form.name.$dirty)"
                      class="tooltip-validation"><spring:message code="admin.users.Name"/> <spring:message code="admin.users.required"/></span>
            </div>
  			<br>
  			<div class="form-item position-relative" style="width: 300px;">
                <label class="detail-label" required><spring:message code="admin.users.Password"/></label>
                <input name="password" type="password" id="password" class="form-control"
                       ng-model="currentEntity.newPassword"
                       maxlength="255"
                       placeholder="<spring:message code="admin.users.Password"/>"
                       ng-minlength="1"
                       ng-class="{ ngInvalid: form.password.$error.required && (form.$submitted) }"
                       autocomplete="off"
                      />
                <span ng-show="form.password.$error.required && (form.$submitted)"
                      class="tooltip-validation"><spring:message code="admin.users.Password"/> <spring:message code="Required"/></span>
            </div>
            
  			<br>
  			<div class="form-item position-relative" style="width: 300px;">
                <label class="detail-label" required><spring:message code="admin.users.Repeat-The-Password"/></label>
                <input name="rePassword" type="password" id="rePassword" class="form-control"
                       ng-model="currentEntity.repeatNewPassword"
                       maxlength="255"
                       placeholder="<spring:message code="admin.users.Repeat-The-Password"/>"
                       ng-required=" passwordRequired() "
                       ng-minlength="1"
                       ng-class="{ ngInvalid: (form.$submitted) && form.rePassword.$error.required }"
                       ng-hover
                       autocomplete="off"
                      />
                <span ng-show="form.rePassword.$error.required && (form.$submitted)"
                      class="tooltip-validation"><spring:message code="admin.users.Password"/> <spring:message code="Required"/></span>
                
                <span ng-show=" !( form.rePassword.$error.required && (form.$submitted) ) && (form.$submitted) && (currentEntity.newPassword != currentEntity.repeatNewPassword)" 
												class="tooltip-validation create"><spring:message code="admin.user.The-password-fields-must-be-equal"/></span>
                
             
               
                
            </div>
  			<br>
            
            
        </div>
    </form>
    
    
  
  
</div>
</html>
