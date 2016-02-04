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
  	<button type="button" class="close" ng-click="close(true)"><span aria-hidden="true">&times;</span><span class="sr-only"></span></button>
    <h3 class="modal-title"><spring:message code="photos.Visualize-Photos"/></h3>
  </div>
  <!-- <span class="icon itaipu-icon-close sidebar-close" ng-click="close(true)"></span> -->

  <div class="modal-body" ng-init="initialize();" style="overflow-y: auto;">
    <div>
      <div style="float:left;padding: 0;border-right: 1px solid #ccc">
        <!--<h4>Descrição</h4>-->

        <div style="margin: 0 45px">
          <p style="float: left">{{ currentPhoto.description }}</p>
          <span style="float: right">{{ pageable.page + 1 }} <spring:message code="of"/> {{ currentAttribute.totalPages }}</span>
        </div>
        <div class="current-photo" style="clear: both">
          <div class="left-nav" ng-click="previousPhoto()"><i class="icon-chevron-left icon-large"></i></div>
          <img ng-src="{{ currentPhoto.image }}">
          <div class="right-nav" ng-click="nextPhoto()"><i class="icon-chevron-right icon-large"></i></div>
        </div>
        <!--<div>
          <a href="" ng-click="previousPhoto()">Prev</a>
          <a href="" ng-click="nextPhoto()">Next</a>
        </div>-->
        <div class="gallery-thumbnails">
          <!--<div class="left-nav" ng-click="previousPage()"><i class="icon-chevron-left icon-large"></i></div>-->
          <img ng-click="setCurrentPhoto(photo, $index)" ng-repeat="photo in currentAttribute.content"
               ng-src="{{ photo.image }}">
          <!--<div class="right-nav" ng-click="nextPage()"><i class="icon-chevron-right icon-large"></i></div>-->
        </div>
        <!--<div>
          <a href="" ng-click="previousPage()">Prev</a>
          <a href="" ng-click="nextPage()">Next</a>
        </div>-->
      </div>
      <div id="sidenav" class="col-md-2" style="padding: 0">
        <ul>
          <li ng-repeat="attr in attributes" ng-click="setAttribute(attr, true)"
              ng-class="{'active': currentAttribute.content[0].photoAlbum.markerAttribute.attribute.name == attributes[$index].content[0].photoAlbum.markerAttribute.attribute.name}">
            {{ attributes[$index].content[0].photoAlbum.markerAttribute.attribute.name }} ({{ attributes[$index].total }})
          </li>
        </ul>
      </div>
    </div>
  </div>
</div>
</html>