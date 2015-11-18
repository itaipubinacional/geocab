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
        <h3 class="modal-title"><spring:message code="admin.layer-config.Add-attribute" /></h3>
    </div>
    <div class="modal-body" ng-init="initialize();" style="overflow: visible">
        <div ng-include="'assets/libs/eits-directives/alert/alert.html'"></div>

        <form novalidate name="form_add_attribute" default-button="buttonInsertAdd" style="margin-bottom: 10px; margin-top: 10px;" class="addAttribute">        
        
        	<div style="position:relative;">
        	<input type="text" 
        		   name="name"
        		   class="form-control"
        		   ng-model="currentEntity.name" 
        		   placeholder="<spring:message code="admin.layer-config.Attribut-name" />" 
        		   style="margin-bottom: 23px"
        		   ng-class="{ngInvalid: form_add_attribute.$submitted && form_add_attribute.name.$error.required }"
        		   maxlength="255"
        		   required
        		   >
        	</div>
        	
        	 <span  ng-show="form_add_attribute.$submitted  && form_add_attribute.name.$error.required" class="tooltip-validation" style="right:16px"><spring:message code="admin.users.Field-required" /></span>
        	
        	<div style="position:relative;">
			<select name="select" class="form-control" ng-model="currentEntity.type"  style="margin-bottom: 15px" 
								  ng-class="{ngInvalid: form_add_attribute.$submitted && form_add_attribute.select.$error.required }"
        		   				  required
        		   				  >
				<option value="" disabled selected style="display:none" ><spring:message code="admin.layer-config.Attribute-type" /></option>
				<option value="TEXT"><spring:message code="admin.layer-config.Text" /></option>
				<option value="NUMBER"><spring:message code="admin.layer-config.Number" /></option>
				<option value="DATE"><spring:message code="admin.layer-config.Date" /></option>
				<option value="BOOLEAN"><spring:message code="admin.layer-config.Boolean" /></option>
			</select>
			</div>
			
			 <span  ng-show="form_add_attribute.$submitted && form_add_attribute.select.$error.required" class="tooltip-validation" style="right:16px;top:62px;"><spring:message code="admin.users.Field-required" /></span>
			
			<input  type="checkbox" ng-model="currentEntity.required">
			<spring:message code="Required" /> 
     
        </form>
        
       
        
       
        
        <br style="clear: both"/>

    </div>

    <div class="modal-footer">
        <button id="buttonInsertAdd" ng-disabled="gridOptions.selectedItems.length == 0" title="<spring:message code="admin.layer-config.Register" />" class="btn btn-primary" ng-click="addAttribute()"><spring:message code="admin.layer-config.Register" /></button>
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