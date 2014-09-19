<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<html>
	<head>
		
		<title>Authentication</title>
		
		<!-- Styles -->
		<link rel="stylesheet" type="text/css" href="<c:url value="/static/style/authentication/authentication.css"/>" />
		<jsp:include page="../../default-styles.jsp"/>
		
		<!-- Scripts -->
		<jsp:include page="../../default-scripts.jsp"/> <!-- FIXME Deixar caminhos relativos.. -->
		
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
			<div class="title-short-login">MAPA SOCIAL</div>
			<div class="title-login"></div>
			<form method="post" action="./j_spring_security_check" name="form">
				<table>
					<tr>
						<td><label><spring:message code="authentication.email" /></label></td>
					</tr>
					<tr>	
						<td><input class="form-control" name="email" type="email"
							value="admin@geocab.com.br" required/></td>
					</tr>
					<tr>
						<td>
							<label><spring:message code="authentication.password" /></label>
							<a class="forget-password">Esqueci minha senha</a>
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
							class="btn-facebook">Efetuar login com facebook</button></td>
				</tr>
				<tr>
					<td><button type="button" id="enter-google" class="btn-google">Efetuar
							login com Google +</div></button></td>
				</tr>
				<tr>
					<td><a class="dont-have-account">Nao possui uma conta?</a><td>
				</tr>
			</table>
		</div>
	</div>
	
	
	
	
	
</body>
</html>