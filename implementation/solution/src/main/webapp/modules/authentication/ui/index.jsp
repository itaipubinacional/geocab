<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title>Authentication</title>
	</head>
	<body>
		<form method="post" action="./j_spring_security_check">
		    <table>
		        <tr>
		            <td><spring:message code="authentication.email"/>:</td>
		            <td><input name="email" type="email" value="admin@geocab.com.br"/></td>
		        </tr>
		        <tr>
		            <td><spring:message code="authentication.password"/>:</td>
		            <td><input name="password" type="password" value="admin"/></td>
		        </tr>
		        <tr>
		            <td>
		                <input type="submit" value="Begin" />
		            </td>
		        </tr>
		    </table>
		</form>
	</body>
</html>