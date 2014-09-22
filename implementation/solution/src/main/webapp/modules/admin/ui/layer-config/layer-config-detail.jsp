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
                <b class="detail-label">Fonte de dados geográficos</b>
                <br>
                <span class="detail-value">{{currentEntity.fonteDados.nome}}</span>
            </div>
            <br>

            <div class="form-item">
                <b class="detail-label">Camada</b>
                <br>
                <span class="detail-value">{{currentEntity.nome}}</span>
            </div>
            <br>

            <div class="form-item">
                <b class="detail-label">Título</b>
                <br>
                <span class="detail-value">{{currentEntity.titulo}}</span>
            </div>
            <br>

			<div class="form-item">
                <b class="detail-label">Simbologia</b>
                <br>
                <img style="border: solid 1px #c9c9c9;" ng-src="{{currentEntity.legenda}}"/>
            </div>
            <br>
            
			<div class="form-item"  ng-if="currentEntity.grupoCamadas != null" style="width: 500px;">
                <b class="detail-label">Grupo de Camada</b>
                <br>
                <span class="detail-value">{{currentEntity.grupoCamadas.nome}}</span>
            </div>
            <br>

            <div class="form-item">
                <b class="detail-label" style="margin-bottom: 10px; display: inline-block;">Escala de visualização</b>
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
                       ng-disabled="true"> <label>Inicia habilitada no mapa</label>
            </div>

            <br />

            <div class="form-item position-relative" style="width: 300px;"
                 ng-if="currentState">
                <input type="checkbox" id="grupo" style="width: 20px;"
                       ng-model="currentEntity.sistema"
                       ng-disabled="currentState == DETAIL_STATE"> <label>Disponível no menu de camadas</label>

            </div>

            <hr>

            <h5 style="font-weight: bold">Grupo de Acesso</h5>
            <div class="form-item position-relative radio" style="width: 300px;">
                <input type="radio" id="publico"
                                                    ng-model="data.tipoAcesso" style="width: 20px;"
                                                    ng-disabled="currentState == DETAIL_STATE" value="PUBLICO">
                <label class="radio-label" style="position: relative; top: -2px;"
                       for="publico">Publico </label> <br /> <input type="radio"
                                                                    id="grupos" style="width: 20px;" ng-model="data.tipoAcesso"
                                                                    ng-disabled="currentState == DETAIL_STATE" value="GRUPOS"> <label
                    style="position: relative; top: -2px;" for="grupos"
                    class="radio-label"> Grupos </label>
            </div>

            <div class="form-item position-relative"
                 ng-if="data.tipoAcesso == 'GRUPOS'" style="width: 100%;">
                <div ng-grid="gridAcessoOptions"
                     style="height: 180px; border: 1px solid rgb(212, 212, 212);"></div>
        </div>
    </form>
</div>
</html>
