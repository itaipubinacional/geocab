<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<div class="modal-content">
    <div class="modal-body" ng-init="initialize();" style="overflow: visible">
   			
  		<div class="alreadyHaveAccount"><spring:message code="authentication.Already-have-an-account"/>
			<a class="enter" ng-click="close()"><spring:message code="authentication.Enter"/></a>
		</div>
		<div class="line" ><hr></hr></div>
		
		<!--Message -->
        <div ng-include="'static/libs/eits-directives/alert/alert.html'"></div>
        
        
        <form name="form" method="post" action="./j_spring_security_check"  default-button="buttonInsert">
					<table>
						<tr>
							<td><label required><spring:message code="authentication.Name" /></label></td>
						</tr>
						<tr>
							<td><input 
							 class="form-control" 
							 ng-model="currentEntity.name" 
							 name="name" 
							 ng-class="{ ngInvalid: form.name.$error.required && (form.$submitted || form.name.$dirty) }"
							 type="text" 
							 required
							  ng-hover
							 ></td>
						</tr>
						<tr>
							<td><label required><spring:message code="authentication.Email" /></label></td>
						</tr>
						<tr>
							<td><input 
							class="form-control" 
							ng-model="currentEntity.email" 
							name="email" 
							ng-class="{ ngInvalid: form.email.$error.required && (form.$submitted || form.email.$dirty) }"
							type="email" 
							required
							ng-hover
							>
							</td>
						</tr>
						<tr>
							<td><label required><spring:message code="authentication.Password" /></label></td>
						</tr>
						<tr>
							<td><input 
							class="form-control" 
							ng-model="currentEntity.password" 
							name="password" 
							ng-class="{ ngInvalid: form.password.$error.required && (form.$submitted || form.password.$dirty) }"
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
							<td><input 
							class="form-control" 
							ng-model="currentEntity.confirmPassword" 
							name="confirmPassword" 
							ng-class="{ ngInvalid: form.confirmPassword.$error.required && (form.$submitted || form.confirmPassword.$dirty) }"
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
