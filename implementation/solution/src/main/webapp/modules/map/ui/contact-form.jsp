<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page import="nl.captcha.Captcha" %>



<!DOCTYPE html>
<html>
<style>
</style>
<!-- My account - Update -->
<div>

	<!--Mensagens-->
    <div class="alert" ng-class="{'alert-dismissable': msg.dismiss, 'danger' : (msg.type == 'danger'), 'info' : (msg.type == 'info'), 'warning' : (msg.type == 'warning'), 'success' : (msg.type == 'success')}" ng-show="msg != null">
	    <button type="button" class="close" ng-click="close()" aria-hidden="true">&times;</button> 
	    {{msg.text}}
	</div>
    <form name="form" novalidate default-button="buttonUpdate">
        <div class="content-tab">
      		
      		
            <div class="form-item position-relative" style="width: 300px;">
                <label class="detail-label required" ><spring:message code="admin.users.Name"/></label>
                <input name="name" type="text" class="form-control"
                       ng-model="currentEntity.name"
                       placeholder="<spring:message code='admin.users.Name'/>"
                       required maxlength="144" ng-minlength="1"
                       ng-class="{ ngInvalid: form.name.$error.required && (form.$submitted || form.name.$dirty) }"
                       autofocus
                       autocomplete="off"
                       ng-hover>

                <span ng-show="form.name.$error.required && (form.$submitted || form.name.$dirty)"
                      class="tooltip-validation"><spring:message code="admin.users.Name"/> <spring:message code="admin.users.required"/></span>
            </div>
            
        </div>
    </form>
    
    
  
  
</div>
</html>
