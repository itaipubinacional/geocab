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
   			
  		<div class="alreadyHaveAccount"><spring:message code="authentication.Already-have-an-account"/>
			<a class="enter" ng-click="closePopUp()"><spring:message code="authentication.Enter"/></a>
		</div>
		<div class="line" ><hr></hr></div>
		
		<!--Message -->
        <div ng-include="'static/libs/eits-directives/alert/alert.html'"></div>
        
        <form name="form_create_account" method="post" action="./j_spring_security_check"  default-button="buttonInsert" novalidate autocomplete="off">
					<table>					
						<tr>
							<td><label required><spring:message code="authentication.Name" /></label>
						</td>				
						</tr>
						<tr>
							<td><span ng-show="form_create_account.name.$error.required &&  form_create_account.$submitted" class="tooltip-validation create"><spring:message code="admin.users.Field-required" /></span>
						</tr>
						<tr>
							<td>
							<input 
							 id="popupName"
							 class="form-control" 
							 ng-model="currentEntity.name" 
							 name="name" 
							 ng-class="{ ngInvalid: form_create_account.$submitted && form_create_account.name.$error.required}"
							 type="text" 
							 required
							 ></td>																	
						</tr>					
						
						<tr>							
							<td>
								<label required><spring:message code="authentication.Email" /></label>								
							</td>
						</tr>
						<tr>
							<td><span ng-show="form_create_account.email.$error.required && form_create_account.$submitted" class="tooltip-validation create"><spring:message code="admin.users.Field-required" /></span></td>							
						</tr>
						<tr>
							<td><span ng-show="form_create_account.email.$error.email && form_create_account.$submitted " class="tooltip-validation create"><spring:message code="admin.users.The-email-is-not-valid" /></span></td>
						</tr>
						<tr>												
							<td><input 
							id="popupEmail"
							class="form-control" 
							ng-model="currentEntity.email" 
							name="email" 							
							type="email"
							ng-class="{ ngInvalid: form_create_account.$submitted && form_create_account.email.$error.required }"			
							required
							
							>
							</td>
						</tr>						
						<tr>
							<td>
								<label required><spring:message code="authentication.Password" /></label>							
							</td>
						</tr>
						<tr>
							<td><span ng-show="form_create_account.password.$error.required && form_create_account.$submitted" class="tooltip-validation create"><spring:message code="admin.users.Field-required" /></span></td>
						</tr>
						<tr>
							<td><input 
							id="popupPassword"
							class="form-control" 
							ng-model="currentEntity.password" 
							name="password" 
							ng-class="{ ngInvalid:form_create_account.password.$error.required && (form_create_account.$submitted ) }"
							type="password" 
							required
							ng-hover
							>
							</td>
						</tr>	
						<tr>
							<td><label><spring:message code="authentication.Confirm-password" /></label></td>
						</tr>
						<tr>
							<td><span ng-show="(form_create_account.confirmPassword.$error.required && form_create_account.$submitted) 
													&& !((form_create_account.confirmPassword.$dirty || form_create_account.password.$dirty ) 
													&& (currentEntity.password != confirmPassword))" 
													class="tooltip-validation create"><spring:message code="admin.users.Field-required" 
													/></span></td>
						</tr>
						<tr>
							<td><span ng-show=" form_create_account.$submitted && currentEntity.password != currentEntity.confirmPassword" 
												class="tooltip-validation create"><spring:message code="admin.user.The-password-fields-must-be-equal" 
												/></span><td>
						</tr>
						<tr>
							<td><input 
							id="popupConfirm"
							class="form-control" 
							ng-model="currentEntity.confirmPassword" 
							name="confirmPassword" 
							ng-class="{ngInvalid: ((form_create_account.$submitted && form_create_account.confirmPassword.$error.required) || currentEntity.password != currentEntity.confirmPassword)  }"
							type="password" 
							required
							ng-hover
							></td>						
						</tr>			
						<tr>
							<td><input type="button" id="buttonInsert" ng-click="createAccount()" value='<spring:message code="authentication.Create-an-account"></spring:message>' id="createAccount" class="btn-enter" /></td>
						</tr>
					</table>				
				</form>
    </div>
</div>

</html>
