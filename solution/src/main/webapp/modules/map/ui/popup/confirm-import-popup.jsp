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
      <button type="button" class="close" ng-click="close(false)"><span aria-hidden="false">&times;</span><span class="sr-only"></span></button>
      <h3 class="modal-title">
        Confirmar importação SHP
      </h3>
    </div>
    <div class="modal-body">

      <h4>Alguns valores de pontos não corresponderam a opções de múltipla escolha</h4>

      <div ng-repeat="markerAttribute in markerAttributes">

        <h5>Atributo <b>"{{ markerAttribute.layerAttribute.name }}"</b> ( opções

          <span ng-repeat="option in markerAttribute.layerAttribute.options">
            <b>{{option.description}}
              <span ng-if="!$last"> / </span>
          </b>
          </span>
          )
        </h5>

        <h5>
          Não correspondeu aos valores:
        </h5>

        <h5 ng-repeat="attributeValue in markerAttribute.attributeValues track by $index">
          <b ng-if="$index === markerAttribute.attributeValues.indexOf(attributeValue)">
            {{attributeValue}}
          </b>
        </h5>

      </div>
    </div>
    <div class="modal-footer">
      <button class="btn btn-default" ng-click="close(false)">Cancelar</button>
      <button class="btn btn-default" ng-click="close(true)">Continuar assim mesmo</button>
    </div>
  </div>

  </html>