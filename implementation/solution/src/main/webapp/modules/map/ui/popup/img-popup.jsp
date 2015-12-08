<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags"  %>
<!DOCTYPE html>
<html>

<div class="modal-content" style="width: 676px;">

	<!-- <span class="icon itaipu-icon-close sidebar-close" ng-click="close(true)"></span> -->

	<div class="modal-body" ng-init="initialize();" style="overflow-y: auto; max-height: 550px;">


		<div>
			<div class="col-md-10" style="padding: 0">


				<div style="height: 400px; width: 100%">
					<img ng-src="{{ currentPhoto.image }}">
				</div>

				<div class="gallery-thumbnails">
					<img ng-click="setCurrentPhoto(photo)" ng-repeat="photo in photos" ng-src="{{ photo.image }}" style="max-width: 125px; max-height: 125px;">
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