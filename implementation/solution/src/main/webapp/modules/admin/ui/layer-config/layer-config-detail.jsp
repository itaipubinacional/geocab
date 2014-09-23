<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<!-- Fonte de Dados - Detail -->
<div>
    <form>
        <div class="content-tab">
            <div class="form-item">
                <b class="detail-label"><spring:message code="admin.layer-config.Geographic-Data-Source"/></b>
                <br>
                <span class="detail-value">{{currentEntity.dataSource.name}}</span>
            </div>
            <br>

            <div class="form-item">
                <b class="detail-label"><spring:message code="Layer"/></b>
                <br>
                <span class="detail-value">{{currentEntity.name}}</span>
            </div>
            <br>

            <div class="form-item">
                <b class="detail-label"><spring:message code="Title"/></b>
                <br>
                <span class="detail-value">{{currentEntity.title}}</span>
            </div>
            <br>

			<div class="form-item">
                <b class="detail-label"><spring:message code="admin.layer-config.Symbology"/></b>
                <br>
                <img style="border: solid 1px #c9c9c9;" ng-src="{{currentEntity.legenda}}"/>
            </div>
            <br>
            
			<div class="form-item"  ng-if="currentEntity.grupoCamadas != null" style="width: 500px;">
                <b class="detail-label"><spring:message code="admin.layer-config.Layer-Group"/></b>
                <br>
                <span class="detail-value">{{currentEntity.layerGroup.name}}</span>
            </div>
            <br>

            <div class="form-item">
                <b class="detail-label" style="margin-bottom: 10px; display: inline-block;"><spring:message code="admin.layer-config.Viewing-scale"/></b>
                <br>
                <div class="position-relative" scale-slider slider-disabled="true" ng-model="escalas" style="width: 350px;">
                </div>
                <div style="width: 350px;">
                    <label style="float: left">{{escalas.values[0]}}</label>
                    <label style="float: right;">{{escalas.values[1]}}</label>
                </div>
            </div>
            <br>

            <div class="form-item position-relative" style="width: 300px;"
                 ng-if="currentState">
                <input type="checkbox" style="width: 20px;"
                       ng-model="currentEntity.habilitada"
                       ng-disabled="true"> <label><spring:message code="admin.layer-config.Start-allowed-in-map"/></label>
            </div>

            <br />

            <div class="form-item position-relative" style="width: 300px;"
                 ng-if="currentState">
                <input type="checkbox" id="grupo" style="width: 20px;"
                       ng-model="currentEntity.sistema"
                       ng-disabled="currentState == DETAIL_STATE"> <label><spring:message code="admin.layer-config.Available-in-the-layers-menu"/></label>

            </div>

            <hr>

            <h5 style="font-weight: bold"><spring:message code="admin.layer-config.Access-Group"/></h5>
            <div class="form-item position-relative radio" style="width: 300px;">
                <input type="radio" id="publico"
                                                    ng-model="data.tipoAcesso" style="width: 20px;"
                                                    ng-disabled="currentState == DETAIL_STATE" value="PUBLICO">
                <label class="radio-label" style="position: relative; top: -2px;"
                       for="publico"><spring:message code="Public"/> </label> <br /> <input type="radio"
                                                                    id="grupos" style="width: 20px;" ng-model="data.tipoAcesso"
                                                                    ng-disabled="currentState == DETAIL_STATE" value="GRUPOS"> <label
                    style="position: relative; top: -2px;" for="grupos"
                    class="radio-label"> <spring:message code="Groups"/> </label>
            </div>

            <div class="form-item position-relative"
                 ng-if="data.tipoAcesso == 'GRUPOS'" style="width: 100%;">
                <div ng-grid="gridAcessoOptions"
                     style="height: 180px; border: 1px solid rgb(212, 212, 212);"></div>
        </div>
    </form>
</div>
</html>
