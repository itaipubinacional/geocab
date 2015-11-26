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
	<div class="modal-body" ng-init="initialize();" style="max-height: 550px; min-height: 500px">

    <div>
      <div class="col-md-8">

        <div id="photos">

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

        </div>

        <div id="upload" style="text-align: center">

          <h2>Solte os arquivos em qualquer lugar para fazer o upload</h2>
          <h4>ou</h4>

          <button>Selecionar arquivos</button>

          <p>Tamanho máximo do arquivo de upload: 2 MB.</p>

        </div>


        <upload-file></upload-file>

      </div>
      <div id="sidenav" class="col-md-4">

        <ul>
          <li>Atributo 1</li>
          <li>Atributo 2</li>
        </ul>

      </div>
    </div>

	</div>

  <div class="modal-footer">
    <a href="">Limpar todos</a>
    <button id="buttonClose" class="btn btn-primary" ng-click="close(false)">Continuar</button>
    <button class="btn btn-default" ng-click="close(true)">Fechar</button>
  </div>
</div>


</html>