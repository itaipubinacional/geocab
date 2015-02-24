<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>

</head>
<div class="modal-content">
    <div class="modal-body" ng-init="initialize();" style="overflow: visible">
   			
		<!--Message -->
        <div ng-include="'static/libs/eits-directives/alert/alert.html'"></div>
        
        
        <form name="form_forget_password" method="post" action="./j_spring_security_check"  default-button="buttonForget" novalidate>
					<table>					
						<tr>							
							<td>
								<label required><spring:message code="authentication.Email" /></label>								
							</td>
						</tr>
						<tr>
							<td><span ng-show="form_forget_password.email.$error.required && form_forget_password.$submitted" class="tooltip-validation create"><spring:message code="admin.users.Field-required" /></span></td>							
						</tr>
						<tr>
							<td><span ng-show="form_forget_password.$submitted && form_forget_password.email.$error.email" class="tooltip-validation create"><spring:message code="admin.users.The-email-is-not-valid" /></span></td>
						</tr>
						<tr>												
							<td><input 
							class="form-control ng-invalid" 
							ng-model="currentEntity.email" 
							name="email" 							
							ng-class="{ ngInvalid: form_forget_password.$submitted && (form_forget_password.email.$error.required ) }"
							type="email" 
							required
							ng-hover
							>
							</td>
						</tr>						
								
						<tr>
							<td><input type="button" id="buttonForget" ng-click="forgetPassword()" value='<spring:message code="authentication.Rescue-password"></spring:message>' id="createAccount" class="btn-enter" /></td>
						</tr>
					</table>				
				</form>
    </div>
</div>

</html>
