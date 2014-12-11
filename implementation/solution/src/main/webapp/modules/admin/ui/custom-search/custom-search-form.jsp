<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<!-- Fonte de dados - Form -->
<div>

    <form novalidate name="form"
          default-button="{{ (currentState == INSERT_STATE) && 'buttonInsert' || 'buttonUpdate' }}">

        <div class="content-tab">

            <div class="form-item position-relative" style="width: 250px; margin-bottom: 15px;">
                <label class="detail-label" required><spring:message code="admin.custom-search.Name"/></label>
                <input name="name" type="text" class="form-control"
                       ng-model="currentEntity.name"
                       placeholder="<spring:message code="admin.custom-search.Enter-the-name"/>"
                       required maxlength="144" ng-minlength="1"
                       ng-class="{ ngInvalid: form.name.$error.required && (form.$submitted || form.name.$dirty) }"
                       autofocus required
                       ng-hover>
                <span ng-show="form.name.$error.required && (form.$submitted || form.name.$dirty)"
                      class="tooltip-validation"><spring:message code="admin.custom-search.Name-required"/></span>
            </div>

            <br/>

            <div class="form-item position-relative" style="width: 350px;">
                <label class="detail-label" required><spring:message code="admin.custom-search.Data-source"/></label>
                <div class="input-group">
                    <input name="dataSource" type="text" disabled class="form-control"
                           ng-model="data.dataSource.name"
                           placeholder="<spring:message code="admin.custom-search.Enter-the-data-source"/>"
                           maxlength="144" ng-minlength="1"
                           ng-hover required>
                    <span class="input-group-btn">
                        <button ng-click="selectDataSource()" class="btn btn-default"
                                type="button"  ng-disabled="currentEntity.id != null">
                            <i class="icon-plus-sign icon-large"></i>
                        </button>
                    </span>
                </div>

                <span ng-show="form.dataSource.$error.required && (form.$submitted || form.dataSource.$dirty)"
                      class="tooltip-validation"><spring:message code="admin.custom-search.Data-source-required"/></span>
            </div>

            <br/>

            <div class="form-item position-relative" style="width: 350px;">
                <label class="detail-label" required><spring:message code="admin.custom-search.Layer"/></label>
                <div class="input-group">
                    <input name="layerGroup" type="text" class="form-control"
                           ng-model="currentEntity.layer.name"
                           disabled
                           placeholder="<spring:message code="admin.custom-search.Enter-the-Layer"/>"
                           maxlength="144" ng-minlength="1"
                           ng-hover>
                    <span class="input-group-btn">
                        <button ng-click="selectLayerConfig()" class="btn btn-default" type="button"
                                ng-disabled="data.dataSource == null || currentEntity.id != null">
                            <i class="icon-plus-sign icon-large"></i>
                        </button>
                    </span>
                </div>

            </div>

            <div ng-if="currentEntity.layer.name">

                <br/>

                <label class="detail-label"l><spring:message code="admin.custom-search.Title"/></label>

                <div class="position-relative input-group" style="width: 350px;">
                    <span>{{currentEntity.layer.title}}</span>
                </div>

                <br/>

                <label class="detail-label"><spring:message code="admin.custom-search.Symbology"/></label>

                <div class="position-relative input-group" style="width: 350px;">
                    <img style="border: solid 1px #c9c9c9;" ng-if="currentEntity.layer.dataSource.url != null" ng-src="{{currentEntity.layer.legend}}"/>
                    <img style="border: solid 1px #c9c9c9;" ng-if="currentEntity.layer.dataSource.url == null" ng-src="{{currentEntity.layer.icon}}"/>
                </div>

            </div>

            <hr>

            <tabset>
                <tab heading="<spring:message code="admin.custom-search.Layer-fields"/>">

                    <div style="margin-top: 20px;">
                        <button type="button" ng-click="subirNivel()" class="btn btn-default" ng-disabled="currentCampoCamada == null || camposSelecionados.length == 0 || camposSelecionados.length == 1 || currentCampoCamada.ordem == 0"
                                style="float: left;" >
                            <span class="glyphicon glyphicon-arrow-up"></span>
                        </button>

                        <button type="button" ng-click="descerNivel()" class="btn btn-default" ng-disabled="currentCampoCamada == null || camposSelecionados.length == 0 || camposSelecionados.length == 1 || currentCampoCamada.ordem == camposSelecionados.length - 1"
                                style="float: left; margin-left: 10px;">
                            <span class="glyphicon glyphicon-arrow-down"></span>
                        </button>

                    </div>

                    <button type="button" ng-click="addFields()" ng-disabled="currentEntity.layer.name == null"
                            style="float: right;" class="btn btn-primary"><spring:message code="admin.custom-search.Add-fields"/>
                    </button>
                    <br>

                    <div ng-grid="gridlayersOptions" style="height: 300px; width: 100%; border: 1px solid rgb(212,212,212); margin-top: 40px;">

                    </div>
                </tab>
                <tab heading="<spring:message code="admin.custom-search.Access"/>">

                    <button ng-click="selectAccessGroup()" type="button" style="margin: 6px 0 20px 0;" class="btn btn-primary"><spring:message code="admin.custom-search.Associate-group"/></button>

                    <br/>

                    <div class="form-item position-relative" style="height: 300px; width: 100%; border: 1px solid rgb(212,212,212);">
                        <div ng-grid="gridGroupOptions"
                             style="height: 300px; width:100%; border: 1px solid rgb(212, 212, 212);"></div>
                    </div>

                </tab>
            </tabset>
        </div>
    </form>
</div>
</html>
