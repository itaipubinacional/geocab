<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
    <c:if test="${!empty sessionScope.SPRING_SECURITY_LAST_EXCEPTION}">
        <input type="hidden" value="${sessionScope.SPRING_SECURITY_LAST_EXCEPTION.message}"  id="msg" />
    <c:remove scope="session" var="SPRING_SECURITY_LAST_EXCEPTION"/>
    </c:if>
    <div class="login-wrapper">
		<div class="border-login">
			<div class="border-left">
				<div class="border-red"></div>
				<div class="border-blue"></div>
			</div>
			<div class="border-right">
				<div class="border-yellow"></div>
				<div class="border-green"></div>
			</div>		
		</div>
		<div class="container-login">
			<div class="login-logo">
				<div class="login-logo-geocab"></div>
				<div class="login-logo-itaipu" ></div>
			</div>
			<div class="title-short-login"></div>
			<div class="title-login" style="display:none">
			</div>
            <!--Message -->
            <div ng-include="'static/libs/eits-directives/alert/alert.html'"></div>

			<form id="form_login" method="post" action="./j_spring_security_check" name="form_login" default-button="enter" novalidate >
				<table>
					<tr>
						<td><label><spring:message code="authentication.Email" /></label></td>
					</tr>
					<tr>
						<td><span ng-if="form_login.email.$error.required && form_login.$submitted" class="tooltip-validation create"><spring:message code="admin.users.Field-required" /></span></td>							
					</tr>
					<tr>
						<td><span ng-if="form_login.email.$error.email && form_login.$submitted" class="tooltip-validation create"><spring:message code="admin.users.The-email-is-not-valid" /></span></td>
					</tr>
					<tr>	
						<td><input class="form-control" 
								   id="email"
								   ng-model="currentEntity.email"
								   name="email" 
								   type="email"
								   ng-class="{ ngInvalid: form_login.email.$error.required && (form_login.$error.email || form_login.email.$dirty || form_login.$submitted) }"
								   required
								   auto-fill-sync/></td>
					</tr>
					<tr>
						<td>
							<label><spring:message code="authentication.Password" /></label>
							<a class="forget-password" ng-click="changeToForgetPassword()"><spring:message code="authentication.Forget-password" /></a>
						</td>
					</tr>
					<tr>
						<td><span ng-if="form_login.password.$error.required && (form_login.$submitted )" class="tooltip-validation create"><spring:message code="admin.users.Field-required" /></span></td>
					</tr>
					<tr>
						<td><input class="form-control" 
								   id="password"
								   ng-model="currentEntity.password"
								   name="password" 
								   type="password" 
								   required
								   ng-class="{ ngInvalid: form_login.password.$error.required && (form_login.$submitted ) }"
								   auto-fill-sync
								   /></td>
					</tr>
					<tr>
						<td><input type="button"  ng-click="login()" value='<spring:message code="authentication.Enter" />' id="enter" class="btn-enter" /></td>
					</tr>
				</table>
			</form>
			<table>
				<tr>
					<td class="line"><hr></hr></td>
				</tr>
				<tr>
					<td>
				    	<form id="facebookSignin" action="<c:url value="/signin/facebook"/>" method="POST">
				    		<input type="hidden" name="scope" value="email,public_profile"/>
	  						<button id="enter-facebook" type="submit" class="btn-facebook">
	  							<spring:message code="authentication.Login-with-Facebook" />
	  						</button>
						</form> 
					</td>
				</tr>
				<tr>
					<td>
						<form id="googleSignin" action="<c:url value="/signin/google"/>" method="POST">
	  						<input type="hidden" name="scope" value="profile email"/>
							<button id="enter-google" type="submit" class="btn-google">
								<spring:message code="authentication.Login-with-Google-+" />
							</button>
						</form>
					</td>
				</tr>
				<tr>
					<td><a class="dont-have-account" id="register"  ng-click="changeToInsert()"><spring:message code="authentication.Dont-have-an-account" /></a><td>
				</tr>
				<tr>
					<td><a style="margin: 0 0 0 100px" class="dont-have-account" id="register" href="${pageContext.request.contextPath != '' ? pageContext.request.contextPath: '/'}"><spring:message code="authentication.Show-map" /></a><td>
				</tr>
			</table>
		</div>
	</div>
	
</html>
