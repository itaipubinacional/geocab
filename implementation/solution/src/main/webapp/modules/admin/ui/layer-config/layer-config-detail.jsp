<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<!-- Layer config - Detail -->
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
            
            <div ng-grid="gridAttributesDetail" style="height: 320px; border: 1px solid rgb(212,212,212);"></div>

			<label class="detail-label" style="margin: 15px 0 5px 0;" required>Ícone</label>
            
            <table style="text-align: center; background: #E6E6E6;">
            
           		<tr>
           			<td><img src="<c:url value="/static/icons/1.png"/>" width="25" height="25"></td>
           			<td><img src="<c:url value="/static/icons/2.png"/>" width="25" height="25"></td>
           			<td><img src="<c:url value="/static/icons/3.png"/>" width="25" height="25"></td>
           			<td><img src="<c:url value="/static/icons/4.png"/>" width="25" height="25"></td>
           			<td><img src="<c:url value="/static/icons/5.png"/>" width="25" height="25"></td>
           		</tr>
           		<tr>
           			<td><input type="radio" value="/static/icons/1.png" ng-disabled="true" ng-checked="currentEntity.icon == '/static/icons/1.png'" name="layerIcon" ng-model="currentEntity.icon"></td>
           			<td><input type="radio" value="/static/icons/2.png" ng-disabled="true" ng-checked="currentEntity.icon == '/static/icons/2.png'" name="layerIcon" ng-model="currentEntity.icon"></td>
           			<td><input type="radio" value="/static/icons/3.png" ng-disabled="true" ng-checked="currentEntity.icon == '/static/icons/3.png'" name="layerIcon" ng-model="currentEntity.icon"></td>
           			<td><input type="radio" value="/static/icons/4.png" ng-disabled="true" ng-checked="currentEntity.icon == '/static/icons/4.png'" name="layerIcon" ng-model="currentEntity.icon"></td>
           			<td><input type="radio" value="/static/icons/5.png" ng-disabled="true" ng-checked="currentEntity.icon == '/static/icons/5.png'" name="layerIcon" ng-model="currentEntity.icon"></td>
           		</tr>
           		
           	</table>
           	<br>

			<div class="form-item">
                <b class="detail-label"><spring:message code="admin.layer-config.Symbology"/></b>
                <br>
                <img ng-if="currentEntity.dataSource.url"style="border: solid 1px #c9c9c9;" ng-src="{{currentEntity.legend}}"/>
                <img style="border: 1px solid #000" ng-if="!currentEntity.dataSource.url && currentEntity.dataSource.id " src="<c:url value="/static/images/symbology.image"/>">
            </div>
            <br>
            
			<div class="form-item"  ng-if="currentEntity.grupoCamadas != null" style="width: 500px;">
                <b class="detail-label"><spring:message code="admin.layer-config.Layer-group"/></b>
                <br>
                <span class="detail-value">{{currentEntity.layerGroup.name}}</span>
            </div>
            <br>

            <div class="form-item">
                <b class="detail-label" style="margin-bottom: 10px; display: inline-block;"><spring:message code="admin.layer-config.Viewing-scale"/></b>
                <br>
                <div class="position-relative" scale-slider slider-disabled="true" ng-model="layers" style="width: 350px;">
                </div>
                <div style="width: 350px;">
                    <label style="float: left">{{layers.values[0]}}</label>
                    <label style="float: right;">{{layers.values[1]}}</label>
                </div>
            </div>
            <br>

            <div class="form-item position-relative" style="width: 300px;"
                 ng-if="currentState">
                <input type="checkbox" style="width: 20px;"
                       ng-model="currentEntity.startEnabled"
                       ng-disabled="true"> <label><spring:message code="admin.layer-config.Start-allowed-in-map"/></label>
            </div>

            <br />

            <div class="form-item position-relative" style="width: 300px;"
                 ng-if="currentState">
                <input type="checkbox" id="grupo" style="width: 20px;"
                       ng-model="currentEntity.startVisible"
                       ng-disabled="currentState == DETAIL_STATE"> <label><spring:message code="admin.layer-config.Available-in-the-layers-menu"/></label>

            </div>
            
            <br />

            <div class="form-item position-relative" style="width: 300px;"
                 ng-if="currentState">
                <input type="checkbox" id="grupo" style="width: 20px;"
                       ng-model="currentEntity.enabled"
                       ng-disabled="currentState == DETAIL_STATE"> <label>Disponível para receber postagens</label>

            </div>

     
        </div>
    </form>
</div>
</html>
