<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<html>
	<head>
		
		<title><spring:message code="authentication.Authentication" /></title>
		
		<!-- Styles -->
		<link rel="stylesheet" type="text/css" href="<c:url value="/static/style/authentication/authentication.css"/>" />
		<jsp:include page="../../default-styles.jsp"/>
		
		<!-- Scripts -->
		<jsp:include page="../../default-scripts.jsp"/> <!-- FIXME Deixar caminhos relativos.. -->
		<script type="text/javascript" src="<c:url value="/static/style/authentication/register.js"/>"></script>
		
	</head>
<body>
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
					<td><button type="button" id="enter-facebook"
							class="btn-facebook"><spring:message code="authentication.Login-with-Facebook" /></button></td>
				</tr>
				<tr>
					<td><button type="button" id="enter-google" class="btn-google"><spring:message code="authentication.Login-with-Google-+" /></button></td>
				</tr>
				<tr>
					<td><a class="dont-have-account" id="register"><spring:message code="authentication.Dont-have-an-account" /></a><td>
				</tr>
			</table>
		</div>
	</div>
	
	<div class="register-overlay" style="display: none;">
		<div class="register-wrapper">
			<div class="border-login">
				<div class="border-left">
					<div class="border-red"></div>
					<div class="border-blue"></div>
				</div>
				<div class="border-right">
					<div class="border-yellow"></div>
					<div class="border-green"></div>
				</div>
				<a class="close">x</a>
			</div>
			<div class="container-register">
				<div class="alreadyHaveAccount"><spring:message code="authentication.Already-have-an-account"/>
					<a class="enter"><spring:message code="authentication.Enter"/></a>
				</div>
				<div class="line" ><hr></hr></div>
				<form method="post" action="./j_spring_security_check">
					<table>
						<tr>
							<td><label><spring:message code="authentication.Name" /></label></td>
						</tr>
						<tr>
							<td><input class="form-control" name="name" type="text" required></td>
						</tr>
						<tr>
							<td><label><spring:message code="authentication.Email" /></label></td>
						</tr>
						<tr>
							<td><input class="form-control" name="email" type="email" required></td>
						</tr>
						<tr>
							<td><label><spring:message code="authentication.Password" /></label></td>
						</tr>
						<tr>
							<td><input class="form-control" name="password" type="password" required></td>
						</tr>
						<tr>
							<td><label><spring:message code="authentication.Confirm-password" /></label></td>
						</tr>
						<tr>
							<td><input class="form-control" name="confirmPassword" type="password" required></td>
						</tr>
						<tr>
							<td><input type="submit" value='<spring:message code="authentication.Create-an-account"></spring:message>' id="createAccount" class="btn-enter" /></td>
						</tr>
					</table>				
				</form>
			</div>
		</div>
	</div>
	
	
</body>
</html>
















