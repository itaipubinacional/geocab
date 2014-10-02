<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>

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
			<div class="login-logo"></div>
			<div class="title-short-login"><spring:message code="authentication.Geocab" /></div>
			<div class="title-login"></div>
			<form method="post" action="./j_spring_security_check" name="form">
				<table>
					<tr>
						<td><label><spring:message code="authentication.Email" /></label></td>
					</tr>
					<tr>	
						<td><input class="form-control" name="email" type="email"
							value="admin@geocab.com.br" required/></td>
					</tr>
					<tr>
						<td>
							<label><spring:message code="authentication.Password" /></label>
							<a class="forget-password"><spring:message code="authentication.Forget-password" /></a>
						</td>
					</tr>
					<tr>
						<td><input class="form-control" name="password" type="password" 
							value="admin" required/></td>
					</tr>
					<tr>
						<td><input type="submit" value="Entrar" id="enter" class="btn-enter" /></td>
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
			</table>
		</div>
	</div>


   	
   	<form id="facebookSignin" action="<c:url value="/signin/facebook"/>" method="POST">
   		<input type="hidden" name="scope" value="email,public_profile"/>
				<button type="submit" class="facebook-btn">
					<i data-icon="m" class="icon"></i>
				</button>
	</form>
</html>
