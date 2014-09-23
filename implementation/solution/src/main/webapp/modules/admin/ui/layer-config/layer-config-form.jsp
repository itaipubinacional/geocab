<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<!-- ConfiguraÃ§Ã£o de camada - Form -->
<div>

    <form novalidate name="form"
          default-button="{{ (currentState == INSERT_STATE) && 'buttonInsert' || 'buttonUpdate' }}">

        <div class="content-tab">

            <div class="form-item position-relative" style="width: 350px;">
                <label class="detail-label" required>Fonte Dados</label>
                <div class="input-group position-relative">
                    <input name="fonteDados" type="text" disabled class="form-control"
                           ng-model="currentEntity.dataSource.name"
                           placeholder="Informe a fonte de dados" maxlength="144"
                           ng-minlength="1"
                           ng-hover
                           required>
                    <span class="input-group-btn">
                        <button ng-click="selectDataSource()" class="btn btn-default"
                                type="button" ng-disabled="currentEntity.id != null">
                            <i class="icon-plus-sign icon-large"></i>
                        </button>
                    </span>
                </div>

                <span ng-show="form.dataSource.$error.required && (form.$submitted || form.dataSource.$dirty)" class="tooltip-validation">Fonte de dados obrigatÃ³rio</span>
            </div>

            <br/>

            <div class="position-relative" style="width: 350px;">
                <label class="detail-label" required>Camada</label>
                <div class="input-group">
                    <input name="camada" type="text" disabled class="form-control"
                           ng-model="currentEntity.name"
                           placeholder="Informe a camada"
                           maxlength="144" ng-minlength="1"
                           ng-hover
                           required>
                <span
                        class="input-group-btn">
                    <button ng-click="selectLayer()"
                            ng-disabled="currentEntity.dataSource == null || currentEntity.id != null"
                            class="btn btn-default" type="button">
                        <i class="icon-plus-sign icon-large"></i>
                    </button>
                </span>
                </div>

                <span ng-show="form.layer.$error.required && (form.$submitted || form.layer.$dirty)" class="tooltip-validation">Camada obrigatória</span>
            </div>

            <div class="form-item position-relative" ng-if="currentEntity.name" style="margin: 20px 0;">

                <label class="detail-label">Título</label>

                <div class="position-relative input-group" style="width: 350px;">
                    <input name="titulo" type="text" class="form-control"
                           ng-model="currentEntity.title"
                           placeholder="Informe o título"
                           maxlength="144" ng-minlength="1"
                           ng-hover
                           required>
                </div>
                <span ng-show="form.title.$error.required && (form.$submitted || form.title.$dirty)" class="tooltip-validation">Título obrigatório</span>

                <br/>

                <label class="detail-label">Simbologia</label>

                <div class="position-relative input-group" style="width: 350px;">
                    <img style="border: solid 1px #c9c9c9;" ng-src="{{currentEntity.legenda}}"/>
                </div>

            </div>

            <br/>

            <div class="form-item position-relative" style="width: 350px;">
                <label class="detail-label" required>Grupo de camadas</label>
                <div class="input-group">
                    <input name="layerGroup" type="text" disabled class="form-control"
                           ng-model="currentEntity.layerGroup.name"
                           placeholder="Informe o grupo de camada"
                           maxlength="144" ng-minlength="1"
                           ng-hover
                           required>
                    <span class="input-group-btn">
                        <button ng-click="selectLayerGroup()" class="btn btn-default"
                                type="button">
                            <i class="icon-plus-sign icon-large"></i>
                        </button>
                    </span>
                </div>

                <span ng-show="form.layerGroup.$error.required && (form.$submitted || form.layerGroup.$dirty)" class="tooltip-validation">Grupo de camada obrigatório</span>
            </div>

            <br/>

            <div class="position-relative" scale-slider ng-model="escalas" style="width: 350px;"></div>
            <div style="width: 350px;">
                <label style="float: left">{{escalas.values[0]}}</label>
                <label style="float: right;">{{escalas.values[1]}}</label>
            </div>

            <br/>

            <div class="form-item position-relative" style="width: 300px;"
                 ng-if="currentState">
                <input type="checkbox" id="grupo" style="width: 20px;"
                       ng-model="currentEntity.habilitada"> <label>Inicia habilitada no mapa</label>

            </div>

            <br/>

            <div class="form-item position-relative" style="width: 300px;"
                 ng-if="currentState">
                <input type="checkbox" style="width: 20px;"
                       ng-model="currentEntity.sistema"
                       ng-disabled="currentState == DETAIL_STATE"> <label>Disponível no menu de camadas</label>

            </div>

            <hr style="border-color: #d9d9d9"/>

            <label class="detail-label">Acesso</label>

            <br/>

            <button ng-click="selectGroupAccess()" type="button" style="margin: 6px 0 20px 0;" class="btn btn-primary">Associar grupo</button>

            <br/>

            <div class="form-item position-relative"  style="width: 100%;">
                <div ng-grid="gridAcessoOptions" style="height: 300px; width:100%; border: 1px solid rgb(212, 212, 212);"></div>
            </div>

       </div>
    </form>
</div>
</html>
