<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>

<style>

	input.ng-invalid.ng-dirty{
	border:1px solid red;
	
}
</style>
<div class="modal-content">
    <div class="modal-header">
        <button type="button" class="close" ng-click="close(true)"><span aria-hidden="true">&times;</span><span class="sr-only"></span></button>
        <h3 ng-if="currentState == UPDATE_STATE || currentState == INSERT_STATE" class="modal-title"><spring:message code="admin.layer-config.Choose-an-icon" /></h3>
        <h3 ng-if="currentState == DETAIL_STATE" class="modal-title">Ícones</h3>
    </div>
    <div class="modal-body" ng-init="initialize();" style="overflow: visible">
        <div ng-include="'assets/libs/eits-directives/alert/alert.html'"></div> 
      			<div class="input-group">
				  <span class="input-group-addon" id="basic-addon1"><i class="icon-search icon-large"></i></span>
				  <input type="text" class="form-control" ng-model="iconFilter" placeholder="Filtre por nome" aria-describedby="basic-addon1">
				</div>    
           		<div style="text-align: center; margin-top:20px; overflow: auto; max-height: 450px; overflow-x: hidden;">
            		 <div style="display: inline-block;" > 
	            		<div  ng-repeat="icon in layerIcons | filter: iconFilter"  
	            			style="float: left; text-align: center; max-width: 100px; width: 100px;"  class="col-md-1"
	            			ng-style="currentEntity.iconTemporary == 'static/icons/' + icon ? {'border':'2px solid red', 'margin':'3px'} : {'margin':'5px'}" >
	            			<div class="clearfix visible-xs-block"></div>
		            		<label for="{{ icon }}" >
		            			<img src="<c:url value="/static/icons/{{ icon }}"/>" width="50" height="50" class="preview" title=""  > <br>
		            			<label ng-bind="getIconName( icon )" class="dynamic-text"></label>
		            			<input id="{{ icon }}" type="radio" value="static/icons/{{ icon }}" ng-checked="currentEntity.icon == 'static/icons/{{ icon }}'" name="layerIcon" style="display: none" ng-model="currentEntity.iconTemporary"> 
		            		</label>
	            		</div>
            		</div>
		       </div>

    </div>

    <div class="modal-footer"> 
    	<div ><a href="http://mapicons.nicolasmollet.com/" target="_blank"><img src="static/images/autor_icones.gif" style="float:left"></a></div>   
        <button ng-if="currentState == UPDATE_STATE || currentState == INSERT_STATE" class="btn btn-primary" title="<spring:message code="Save" />" ng-click="save()"><spring:message code="Save" /></button>
        <button class="btn btn-default" title="<spring:message code="admin.layer-config.Close" />" ng-click="close(true)"><spring:message code="admin.layer-config.Close" /></button>
    </div>
</div>

<script type="text/javascript">
    /**
     * Jquery usado para nao permitir que a popup seja aberta novamente toda vez que o usuario teclar enter
     */
    $(document).ready(function () {
        $("#hide").focus();
        $("#hide").hide();
    });
</script>

</html>