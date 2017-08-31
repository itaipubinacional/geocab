<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<style>
	.icon{
		padding-right:3px;
	}
	
	table.table{
	
	}
	
</style>

<script>
	$("input[type=radio]").attr("disabled","true");
	
	
	
</script>
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
            
            <div ng-if="!currentEntity.dataSource.url" ng-grid="gridAttributesDetail" style="height: 320px; border: 1px solid rgb(212,212,212);"></div>

	                      	
           	<br>

			<div class="form-item">
                <b class="detail-label"><spring:message code="admin.layer-config.Symbology"/></b>
                <br>
                <img ng-if="currentEntity.dataSource.url"style="border: solid 1px #c9c9c9;" ng-src="{{currentEntity.legend}}"/>
                <img ng-if="!currentEntity.dataSource.url && currentEntity.dataSource.id " src="<c:url value="{{ currentEntity.icon }}"/>">
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

            <div ng-if="!currentEntity.dataSource.url" class="form-item position-relative" style="width: 300px;"
                 ng-if="currentState">
                <input type="checkbox" id="grupo" style="width: 20px;"
                       ng-model="currentEntity.enabled"
                       ng-disabled="currentState == DETAIL_STATE"> <label>Disponível para receber postagens</label>

            </div>
			
			<hr style="border-color: #d9d9d9"/>

            <label class="detail-label">Grupo de Acesso</label>

            <br/>
         

            <br/>

            <div class="form-item position-relative"  style="width: 100%;">
                <div ng-grid="gridAccessOptions" style="height: 300px; width:100%; border: 1px solid rgb(212, 212, 212);"></div>
            </div>
     
        </div>
    </form>
</div>
</html>
