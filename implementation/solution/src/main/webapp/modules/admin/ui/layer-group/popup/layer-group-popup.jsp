<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
    <div class="modal-content">
    
        <div class="modal-header">
            <h3 class="modal-title"><spring:message code="layer-group-popup.Insert-a-name-for-the-layer-group"/></h3>
        </div>
        <div class="modal-body" ng-init="initialize();" style="overflow: visible">
			<div class="msg" ng-include="'static/libs/eits-directives/alert/alert.html'" ></div>

			<form novalidate name="form" default-button="buttonInsert"
				style="margin-bottom: 10px; margin-top: 10px;">

				<div class="form-item position-relative" style="width: 350px;">
					</br> <input type="text" ng-model="currentEntity.name" name="name"
						style="width: 300px;"
						placeholder='<spring:message code="layer-group-popup.Inform-the-name" />'
						class="form-control" required="true" maxlength="144"
						ng-class="{ ngInvalid: form.name.$error.required && (form.$submitted || form.nome.$dirty) }" />

					<span class="tooltip-validation"
						ng-show=" form.name.$error.required && (form.$submitted || form.nome.$dirty)" style="right:50px; top:-3px"><spring:message code="layer-group-popup.Name-required" /></span>
				</div>

			</form>
		</div>

        <div class="modal-footer">
            <button id="buttonInsert" ng-show="currentState == NORMAL_STATE" class="btn btn-primary" title="<spring:message code="layer-group-popup.Save" />" ng-click="closePopup()"><spring:message code="layer-group-popup.Save" /></button>
            <button id="buttonClose" ng-show="currentState == NORMAL_STATE" class="btn btn-default" title="<spring:message code="layer-group-popup.Close" />" ng-click="close()"><spring:message code="layer-group-popup.Close" /></button>

            <button id="buttonConfirmInsert" ng-show="currentState == CONFIRM_STATE" class="btn btn-primary" title="<spring:message code="layer-group-popup.Yes"/>" ng-click="closePopupConfirm()"><spring:message code="layer-group-popup.Yes"/></button>
            <button id="buttonConfirmClose" ng-show="currentState == CONFIRM_STATE" class="btn btn-default"  title="<spring:message code="layer-group-popup.No"/>" ng-click="closeConfirm()"><spring:message code="layer-group-popup.No"/></button>
        </div>
    </div>

<script type="text/javascript">
    /**
     * Jquery used to not allow the popup to open again every time the User press enter
     */
    $(document).ready(function(){
        $("#hide").focus();
        $("#hide").hide();
    });
</script>

</html>