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

        <div>
          <a href="" ng-click="previousPhoto()">Prev</a>
          <a href="" ng-click="nextPhoto()">Next</a>
          <span>{{ pageable.page + 1 }} de {{ currentAttribute.totalPages }}</span>
        </div>
        <div class="current-photo">
          <img ng-src="{{ currentPhoto.image }}">
        </div>

        <div class="gallery-thumbnails">
          <img ng-click="setCurrentPhoto(photo, $index)" ng-repeat="photo in currentAttribute.content"
               ng-src="{{ photo.image }}">
        </div>

      </div>
      <div id="sidenav" class="col-md-2" style="padding: 0">
        <div>
          <a href="" ng-click="previousPage()">Prev</a>
          <a href="" ng-click="nextPage()">Next</a>
          <span>{{ pageable.page + 1 }} de {{ currentAttribute.totalPages }}</span>
        </div>
        <ul>
          <li ng-repeat="attr in attributes" ng-click="setAttribute(attr, true)"
              ng-class="{'active': currentAttribute.content[0].photoAlbum.markerAttribute.attribute.name == attributes[$index].content[0].photoAlbum.markerAttribute.attribute.name}"> {{ attributes[$index].content[0].photoAlbum.markerAttribute.attribute.name }} ({{ currentAttribute.total }})
          </li>
        </ul>
      </div>
    </div>


  </div>
</div>

</html>