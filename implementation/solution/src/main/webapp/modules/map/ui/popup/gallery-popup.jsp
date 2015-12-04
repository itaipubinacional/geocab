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

    <!--<tabset>
      <tab heading="Enviar arquivos">
        <div></div>
      </tab>
      <tab heading="Fotos do atributo">
        <div></div>
      </tab>
    </tabset>-->

    <div>
      <div class="col-md-10">

        <!--<div id="photos">

          <div class="form-item position-relative">
            <label class="detail-label" required>
              Descrição
            </label>
            <input name="title" type="text" class="form-control"
                   ng-model="shapeFile.form.title"
                   placeholder="Informe a descrição da foto"
                   maxlength="144" ng-minlength="1"
                   ng-hover
                   required
                   ng-class="{ngInvalid:form.title.$error.required && (form.$submitted || form.title.$dirty) }"/>
                      <span ng-show="form.title.$error.required && (form.$submitted || form.title.$dirty) "
                            class="tooltip-validation"><spring:message code="admin.layer-config.Title-required"/></span>
          </div>

          <div class="row">
            <div class="col-xs-6 col-md-3">
              <div class="thumbnail">
                <img src="..." alt="...">
                <div class="caption">
                  <p>Checkbox</p>
                  <p>Thumbnail label</p>
                </div>
              </div>
            </div>
          </div>

        </div>-->

        <!--<div ng-if="attribute.files.length" style="width: 100%;float: left;">
          <div class="row">
            <div style="float:left;margin:5px; width: 140px; height: 130px" ng-repeat="file in attribute.files">
              <div class="thumbnail">
                <img style="max-width: 125px;max-height: 125px;" ng-src="{{ file.src }}" alt="{{ file.name }}">
                <div class="caption">
                  <p>{{ file.name}}</p>
                </div>
              </div>
            </div>
          </div>
        </div>-->
        <upload-file on-success="onSuccess(files)" attribute="attribute"></upload-file>

      </div>
      <div id="sidenav" class="col-md-2">
        <ul ng-repeat="attr in attributesByLayer">
          <li ng-click="setAttribute(attr)" ng-class="{'active': attr.name == attribute.name}"> {{ attr.name }} </li>
        </ul>
      </div>
    </div>

	</div>

  <div class="modal-footer">
    <a href="#" ng-click="clearFiles()">Limpar todos</a>
    <button class="btn btn-default" ng-click="removeChecked()">Remover selecionados</button>
    <button id="buttonClose" class="btn btn-primary" ng-click="close(false)">Continuar</button>
    <!--<button class="btn btn-default" ng-click="close(true)">Fechar</button>-->
  </div>
</div>


</html>