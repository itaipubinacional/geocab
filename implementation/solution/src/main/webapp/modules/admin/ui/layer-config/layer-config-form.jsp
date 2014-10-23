<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<!-- Layer config - Form -->
<div>

    <form novalidate name="form"
          default-button="{{ (currentState == INSERT_STATE) && 'buttonInsert' || 'buttonUpdate' }}">

        <div class="content-tab">

            <div class="form-item position-relative" style="width: 350px;">
                <label class="detail-label" required><spring:message code="admin.datasource.Data-Source"/></label>
                <div class="input-group position-relative">
                    <input name="dataSource" type="text" disabled class="form-control"
                           ng-model="currentEntity.dataSource.name"
                           placeholder="Informe a fonte de dados" maxlength="144"
                           ng-minlength="1"
                           ng-hover
                           required
                           ng-class="{ngInvalid:form.dataSource.$error.required && (form.$submitted || form.dataSource.$dirty)}"
                           >
                    <span class="input-group-btn">
                        <button ng-click="selectDataSource()" class="btn btn-default"
                                type="button" ng-disabled="currentEntity.id != null">
                            <i class="icon-plus-sign icon-large"></i>
                        </button>
                    </span>
                </div>

                <span ng-show="form.dataSource.$error.required && (form.$submitted || form.dataSource.$dirty)" class="tooltip-validation"><spring:message code="admin.datasource.Data-Source"/> <spring:message code="required"/></span>
            </div>


            <!-- Camada externa -->
            <span ng-if="currentEntity.dataSource.url">
                <div  class="position-relative" style="width: 350px;">
                    <label class="detail-label" required><spring:message code="Layer"/></label>

                <div class="input-group">

                <input name="layer" type="text" disabled class="form-control"
                    ng-model="currentEntity.name"
                    placeholder="Informe a camada"
                    maxlength="144" ng-minlength="1"
                    ng-hover
                    required />
                <span class="input-group-btn">
                    <button ng-click="selectLayer()"
                        ng-disabled="currentEntity.dataSource == null || currentEntity.id != null"
                        class="btn btn-default" type="button">
                        <i class="icon-plus-sign icon-large"></i>
                    </button>
                </span>
                </div>
                    <span ng-show="form.layer.$error.required && (form.$submitted || form.layer.$dirty)" class="tooltip-validation"><spring:message code="Layer"/> <spring:message code="required"/></span>
                </div>

                <div class="form-item position-relative" ng-if="currentEntity.name" style="margin: 20px 0;">

                <label class="detail-label" required><spring:message code="Title"/></label>

                <div class="position-relative input-group" style="width: 350px;">
         
                <input name="title" type="text" class="form-control"
                    ng-model="currentEntity.title"
                    placeholder="Informe o título"
                    maxlength="144" ng-minlength="1"
                    ng-hover
                    required>
                </div>
                
                <br/>

                <label class="detail-label"><spring:message code="admin.layer-config.Symbology"/></label>

                <div class="position-relative input-group" style="width: 350px;">
                <img style="border: solid 1px #c9c9c9;" ng-src="{{currentEntity.legend}}"/>
                </div>

                </div>
            </span>

            <!-- Camada interna -->
           
                <span ng-if="!currentEntity.dataSource.url && currentEntity.dataSource.id ">
                <br/>
                <div class="position-relative input-group" style="width: 350px;">
                	<label class="detail-label" required><spring:message code="Title"/></label>
                
                	<input name="title" type="text" class="form-control"
                    	ng-model="currentEntity.title"
                    	placeholder="Informe o título"
                    	maxlength="144" ng-minlength="1"
                    	ng-hover
                    	required 
                    	ng-class="{ngInvalid:form.title.$error.required && (form.$submitted || form.title.$dirty) }"
                    	/>
           
           			 <span ng-show="form.title.$error.required && (form.$submitted || form.title.$dirty) " class="tooltip-validation"><spring:message code="admin.layer-config.Title-required" /></span>
                </div>
                
                <br/>
                
            <div>
	            <button ng-click="addAttribute()"  class="btn btn-primary" style="margin-bottom: 5px">Adicionar atributos</button>
	            <div ng-grid="gridAttributes" style="height: 320px; border: 1px solid rgb(212,212,212);"></div>
	            
	            <label class="detail-label" style="margin: 15px 0 5px 0;" required>Escolha um ícone</label>
	            
	            <table style="text-align: center; background: #E6E6E6;">
	            
	           		<tr>
	           			<td><img src="<c:url value="/static/icons/1.png"/>" width="25" height="25"></td>
	           			<td><img src="<c:url value="/static/icons/2.png"/>" width="25" height="25"></td>
	           			<td><img src="<c:url value="/static/icons/3.png"/>" width="25" height="25"></td>
	           			<td><img src="<c:url value="/static/icons/4.png"/>" width="25" height="25"></td>
	           			<td><img src="<c:url value="/static/icons/5.png"/>" width="25" height="25"></td>
	           		</tr>
	           		<tr>
	           			<td><input type="radio" value="static/icons/1.png" ng-checked="currentEntity.icon == 'static/icons/1.png'" name="layerIcon" ng-model="currentEntity.icon"></td>
	           			<td><input type="radio" value="static/icons/2.png" ng-checked="currentEntity.icon == 'static/icons/2.png'" name="layerIcon" ng-model="currentEntity.icon"></td>
	           			<td><input type="radio" value="static/icons/3.png" ng-checked="currentEntity.icon == 'static/icons/3.png'" name="layerIcon" ng-model="currentEntity.icon"></td>
	           			<td><input type="radio" value="static/icons/4.png" ng-checked="currentEntity.icon == 'static/icons/4.png'" name="layerIcon" ng-model="currentEntity.icon"></td>
	           			<td><input type="radio" value="static/icons/5.png" ng-checked="currentEntity.icon == 'static/icons/5.png'" name="layerIcon" ng-model="currentEntity.icon"></td>
	           		</tr>
	           		
	           	</table>
            
            
            </div>
            
            </span>
            
            

            <br/>
            <div class="form-item position-relative" style="width: 350px;">
                <label class="detail-label" required><spring:message code="admin.layer-config.Layer-group"/> </label>
                <div class="input-group">
                    <input name="layerGroup" type="text" disabled class="form-control"
                           ng-model="currentEntity.layerGroup.name"
                           placeholder="Informe o grupo de camada"
                           maxlength="144" ng-minlength="1"
                           ng-hover
                           required
                           ng-class="{ng-invalid:form.layerGroup.$error.required && (form.$submitted || form.layerGroup.$dirty)" class="tooltip-validation}"                           
                           >
                    <span class="input-group-btn">
                        <button ng-click="selectLayerGroup()" class="btn btn-default"
                                type="button">
                            <i class="icon-plus-sign icon-large"></i>
                        </button>
                    </span>
                </div>

                <span ng-show="form.layerGroup.$error.required && (form.$submitted || form.layerGroup.$dirty)" class="tooltip-validation"><spring:message code="admin.layer-config.Layer-group"/>  <spring:message code="required"/> </span>
            </div>

            <br/>

            <div class="position-relative" scale-slider ng-model="layers" style="width: 350px;"></div>
            <div style="width: 350px;">
                <label style="float: left">{{layers.values[0]}}</label>
                <label style="float: right;">{{layers.values[1]}}</label>
            </div>

            <br/>

            <div class="form-item position-relative" style="width: 300px;"
                 ng-if="currentState">
                <input type="checkbox" id="grupo" style="width: 20px;"
                       ng-model="currentEntity.startEnabled"> <label><spring:message code="admin.layer-config.Start-allowed-in-map"/></label>

            </div>

            <br/>

            <div class="form-item position-relative" style="width: 300px;"
                 ng-if="currentState">
                <input type="checkbox" style="width: 20px;"
                       ng-model="currentEntity.startVisible"
                       ng-disabled="currentState == DETAIL_STATE"> <label><spring:message code="admin.layer-config.Available-in-the-layers-menu"/></label>

            </div>


       </div>
    </form>
</div>
</html>
