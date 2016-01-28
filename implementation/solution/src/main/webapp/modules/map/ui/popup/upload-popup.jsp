<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<div class="modal-content">
  <div ng-include="static/libs/eits-directives/alert/alert.html"></div>
	<div class="modal-header">
	  <h3 class="modal-title"><spring:message code="photos.Insert-Photos"/></h3>
	</div>
	<div class="modal-body" ng-init="initialize();" style="overflow: hidden;max-height: 550px; min-height: 500px;">
    <div>
      <div class="col-md-9" style="padding: 0">
        <upload-file on-success="onSuccess(files)" on-error="onError(msg)" attribute="attribute"></upload-file>
      </div>
      <div id="sidenav" class="col-md-3" style="padding: 0">
        <ul>
          <li ng-repeat="attr in attributesByLayer" ng-click="setAttribute(attr)"
              ng-class="{'active': attr.name == attribute.name}">{{ attr.name }} ({{ attr.files ? attr.files.length : 0 }})</li>
        </ul>
      </div>
    </div>
	</div>
  <div class="modal-footer">
    <span style="background-color: initial" class="error">{{ msg.text }}</span>
    <a href="#" ng-click="clearFiles()"><spring:message code="photos.Clear-All"/></a>
    <button class="btn btn-default" ng-click="removeChecked()"><spring:message code="photos.Remove-Selected"/></button>
    <button id="buttonClose" class="btn btn-primary" ng-click="close(false)"><spring:message code="photos.Continue"/></button>
  </div>
</div>
</html>