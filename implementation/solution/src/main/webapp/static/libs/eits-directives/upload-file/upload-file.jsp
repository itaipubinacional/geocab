<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags"  %>
<!DOCTYPE html>
<html>
<div id="dropbox" ng-class="{'over': over}">
  <div ng-if="!files.length && !isLoading" class="row dropbox" draggable="true">
    <h3><spring:message code="photos.Drag-And-Drop-The-Files-To-Upload"/></h3>
    <h4><spring:message code="or"/></h4>
    <input type="button" class="btn btn-primary" ng-click="uploadFile()" value="<spring:message code="photos.Select-Files"/>"/>
    <p><spring:message code="photos.MaxSize-File-To-Upload"/></p>
  </div>
  <div style="width: 100%; text-align: center">
    <i ng-show="isLoading" class="loading" style="height: 50px;width: 50px"></i>
  </div>
  <div ng-if="files.length" style="width: 100%;float: left">
    <div class="row" ng-if="!isLoading">
      <div class="col-md-8">
        <div class="form-group" style="width:100%;margin-bottom: 10px; padding-right: 10px">
          <label class="detail-label">
            <spring:message code="Description"/>
          </label>
          <input name="description" type="text" class="form-control" ng-model="fileSelected.description"
                 maxlength="60" ng-maxlength="60"/>
        </div>
      </div>
      <div class="col-md-3" style="margin-top: 23px;">
        <div class="form-group">
          <input type="button" class="btn btn-primary" ng-click="uploadFile()" value="<spring:message code="photos.Select-Files"/>"/>
        </div>
      </div>
    </div>
    <div style="float: left;overflow: auto;height: 440px;">
      <div class="files" ng-repeat="file in files" class="dropbox" title="{{ file.description ? file.description : file.name }}">
        <div ng-click="setFile(file)" class="thumbnail" ng-class="{'active': file.$$hashKey == fileSelected.$$hashKey}">
          <img ng-src="{{ file.src }}" alt="{{ file.name }}">
          <div class="caption">
            <label ng-click="toggleCheckbox(file);$event.stopPropagation();">
              <input type="checkbox" ng-model="file.checked" name="file_{{ $index }}">
              <span ng-if="file.description">
                {{ file.description | limitTo: 30 }} {{ file.description.length > 30 ? '...' : '' }}
              </span>
              <span ng-if="file.name && !file.description">
                {{ file.name | limitTo: 30 }} {{ file.name.length > 30 ? '...' : '' }}
              </span>
            </label>
          </div>
        </div>
      </div>
    </div>
  </div>
  <div ng-if="over && files.length" id="dropbox-over-msg">
    <span><spring:message code="photos.Drop-The-Files-To-Upload"/> <spring:message code="in"/> <br/><b>{{ attribute.name }}</b></span>
  </div>
  <input type="file" id="files" multiple onchange="angular.element(this).scope().setFiles(this)" style="text-indent:999999px; color:#fff"/>
</div>
</html>