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
                <button type="button" class="close" ng-click="close(true)"><span aria-hidden="true">&times;</span><span class="sr-only"></span></button>
                <h3 class="modal-title">
                  Importar SHP
                  <spring:message code="photos.Insert-Photos" />
                </h3>
              </div>
              <div class="modal-body">

                <p>Alguns valores de pontos não corresponderam a opções de múltipla escolha</p>

                <div ng-repeat="markerAttribute in markerAttributes">
                  {{ markerAttribute.layerAttribute.name }} não deu com

                  <span ng-repeat="attributeValue in markerAttribute.attributeValues">
                    {{attributeValue}}
                  </span>

                </div>
              </div>
              <div class="modal-footer">
                <button class="btn btn-default" ng-click="close(false)">Cancelar</button>
                <button class="btn btn-default" ng-click="close(true)">Continuar assim mesmo</button>
              </div>
            </div>

            </html>