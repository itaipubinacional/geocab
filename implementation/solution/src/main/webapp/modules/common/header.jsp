<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags"  %>
<!DOCTYPE html>
<html>

<!-- header -->
<div id="header-page">
	<!-- navbar 1 -->
	<div class="navbar navbar-1" style="z-index: 1002;">
		<a class="pre" href="./">&nbsp;</a>
		<a class="logo" href="./">&nbsp;</a>
		<div class="nav-collapse collapse">
			<div class="left-side">
				<div style="float: left; margin-top: 23px">
					<span style="font-size: 17px"><b style="font-size: 17px">GeoCAB</b>
						- Cultivando Água Boa</span>
				</div>
			</div>

			<ul class="nav navbar-nav pull-right right-side">
				<li><a href="#" class="active box-separator"
					style="border: none; margin-top: 7px;"> <span
						style="color: #000000" > <security:authorize access="isAuthenticated()"> <sec:authentication  property="principal.name"/> </security:authorize> </span>
				</a></li>
				<li class="box-separator"></li>
				<li>
					<div class="user-logout">
						<security:authorize access="isAuthenticated()"> 
							<a href="<c:url value="/j_spring_security_logout"/>"><spring:message code="Logout" /></a>
						</security:authorize>
						<security:authorize access="!isAuthenticated()"> 
							<a href="<c:url value="/authentication"/>">Login</a>
						</security:authorize>
					</div>
				</li>
			</ul>
		</div>
	</div>
	<security:authorize access="isAuthenticated()">
		<security:authorize ifAnyGranted="USER, MODERATOR"> 
			<sec:authorize access="principal.password != 'no password'">
				<div id="navbar-user" class="navbar navbar-2" style="z-index: 1001;">
					<div class="navbar-inner border-radius-0">
			
						<div class="nav-collapse collapse">
							<ul class="nav navbar-nav">
														
								<li class="position-relative"><a href="./"
									style="width: 50px;" ng-class="{active: menuActive == null}"><span
										class="icon-mapa-interativo"></span></a></li>
			
								<li class="position-relative"><a
									href="user#/account"
									ng-class="{active: menuActive == 'my-account'}"
									style="width: 150px;">Minha conta</a></li>
									
									
			                </ul>
						</div>
					</div>
				</div>
			</sec:authorize>
		</security:authorize>
	
		<security:authorize ifAnyGranted="ADMINISTRATOR">
			<div id="navbar-administrator" class="navbar navbar-2" style="z-index: 1001;">
				<div class="navbar-inner border-radius-0"  style="padding-right: 0;">
		
					<div class="nav-collapse collapse">
						<ul class="nav navbar-nav">
													
							<li class="position-relative"><a href="./"
								style="width: 50px;" ng-class="{active: menuActive == null}"><span
									class="icon-mapa-interativo"></span></a></li>
		
							<li class="position-relative"><a
								href="admin#/users"
								ng-class="{active: menuActive == 'users'}"
								style="width: 150px;"><spring:message code="admin.users.Users"/></a></li>
								
							<li class="position-relative"><a
								href="admin#/data-source"
								ng-class="{active: menuActive == 'data-source'}"
								style="width: 150px;"><spring:message code="admin.datasource.Data-Source"/></label></a></li>
		
							<li class="position-relative"><a
								href="admin#/layer-group"
								ng-class="{active: menuActive == 'layer-group'}"
								style="width: 150px;"><spring:message code="admin.layer-group.Layer-group" /></a></li>
		
		                    <li class="position-relative"><a
		                            href="admin#/layer-config"
		                            ng-class="{active: menuActive == 'layer-config'}"
		                            style="width: 150px;"><spring:message code="admin.layer-config.Layers"/></a></li>
		                    
		                     <li class="position-relative"><a
		                            href="admin#/custom-search"
		                            ng-class="{active: menuActive == 'custom-search'}"
		                            style="width: 150px;"><spring:message code="admin.custom-search.Search"/></a></li>
		                            
		                     <li class="position-relative"><a
		                            href="admin#/access-group"
		                            ng-class="{active: menuActive == 'access-group'}"
		                            style="width: 150px;"><spring:message code="admin.access-group.Access-group"/></a></li>
		                            
		                     <li class="position-relative"><a
		                            href="admin#/marker-moderation"
		                            ng-class="{active: menuActive == 'marker-moderation'}"
		                            style="width: 150px;"><spring:message code="admin.marker-moderation.marker-moderation"/></a></li>
		                    
		                </ul>
		                <ul class="nav navbar-nav" style="float: right">
		                	<li class="position-relative"><a
									href="user#/account"
									ng-class="{active: menuActive == 'my-account'}"
									style="width: 150px;">Minha conta</a></li>
		                </ul>
		                
					</div>
				</div>
			</div>
		</security:authorize>	
	</security:authorize>	
</div>


</html>