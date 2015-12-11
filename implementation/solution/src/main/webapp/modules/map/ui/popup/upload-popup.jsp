<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<div class="modal-content">
	<div class="modal-header">
		<h3 class="modal-title">Inserir imagens</h3>
	</div>
	<div class="modal-body" ng-init="initialize();" style="overflow: auto;max-height: 550px; min-height: 500px">
    <div>
      <div class="col-md-10" style="padding: 0">
        <upload-file on-success="onSuccess(files)" attribute="attribute"></upload-file>
      </div>
      <div id="sidenav" class="col-md-2" style="padding: 0">
        <ul>
          <li ng-repeat="attr in attributesByLayer" ng-click="setAttribute(attr)"
              ng-class="{'active': attr.name == attribute.name}">{{ attr.name }} ({{ attr.files ? attr.files.length : 0 }})</li>
        </ul>
      </div>
    </div>
	</div>
  <div class="modal-footer">
    <a href="#" ng-click="clearFiles()">Limpar todos</a>
    <button class="btn btn-default" ng-click="removeChecked()">Remover selecionados</button>
    <button id="buttonClose" class="btn btn-primary" ng-click="close(false)">Continuar</button>
  </div>
</div>
</html>