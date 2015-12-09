<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags"  %>
<!DOCTYPE html>
<html>

<div class="modal-content" id="gallery">

	<div class="modal-header">
		<h3 class="modal-title">Visualizar imagens</h3>
	</div>
	<!-- <span class="icon itaipu-icon-close sidebar-close" ng-click="close(true)"></span> -->

	<div class="modal-body" ng-init="initialize();" style="overflow-y: auto;">


		<div>
			<div class="col-md-10" style="padding: 0">

				<h4>Descrição</h4>
				<p>Texto descrição</p>

				<div class="current-photo">
					<img ng-src="{{ currentPhoto.image }}">
				</div>

				<div class="gallery-thumbnails">
					<img ng-click="setCurrentPhoto(photo)" ng-repeat="photo in photos" ng-src="{{ photo.image }}">
				</div>

			</div>
			<div id="sidenav" class="col-md-2" style="padding: 0">
				<ul>
					<li ng-repeat="attr in attributesByLayer" ng-click="setAttribute(attr)"
							ng-class="{'active': attr.name == attribute.name}"> {{ attr.name }} ({{ attr.files ? attr.files.length : 0 }})</li>
				</ul>
			</div>
		</div>


        
	</div>
</div>

</html>