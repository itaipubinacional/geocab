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
        <h3 class="modal-title"><spring:message code="admin.marker-moderation.Refuse-marker" /></h3>
    </div>
    <div class="modal-body" ng-init="initialize();" style="overflow: visible">
        <div ng-include="'assets/libs/eits-directives/alert/alert.html'"></div>

        <form novalidate name="form_refuse_marker" ng-model="form_refuse_marker" default-button="refuseButton" style="margin-bottom: 10px; margin-top: 10px;" class="refuseMarker">        
        	
        	<div class="form-item" style="width:100%">
        		<label class="detail-label" required><spring:message code="admin.marker-moderation.Motive" /></label>
	        	<div style="position:relative;">
	        		<select name="select" class="form-control" ng-model="data.motive" style="margin-bottom: 15px"
	        				ng-options="motive.name for motive in motives"
							ng-class="{ngInvalid: form_refuse_marker.$submitted && form_refuse_marker.select.$error.required }"
	        		   		required>
	        		   		<option value="">Selecione o motivo</option>
	        		</select>
	        	</div>
        	
	        	 <span ng-show="form_refuse_marker.$submitted && form_refuse_marker.select.$error.required " class="tooltip-validation" style="right:16px;top:28px"><spring:message code="admin.users.Field-required" /></span>
	        	
	        	 <label class="form-item"><spring:message code="admin.marker-moderation.Description" /></label>
				 <textarea rows="5" cols="100" ng-model="data.description"></textarea>
				 
				 <span  ng-show="form_add_attribute.$submitted && form_add_attribute.select.$error.required" class="tooltip-validation" style="right:16px;top:62px;"><spring:message code="admin.users.Field-required" /></span>
			</div>
			
     
        </form>
        
       
        
       
        
        <br style="clear: both"/>

    </div>

    <div class="modal-footer">
        <button class="btn btn-danger" ng-click="refuse()" id="refuseButton"><spring:message code="admin.marker-moderation.Refuse" /></button>
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