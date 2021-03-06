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
        <h3 class="modal-title"><spring:message code="admin.layer-config.Associate-attributes" /></h3>
    </div>
    <div class="modal-body" ng-init="initialize();" style="overflow: visible">

        <div>

            <table class="ng-grid">
                <thead>
	                <tr>
	                    <th>Atributo da camada</th>
	                    <th>Atributo importado</th>
	                </tr>
                </thead>
                <tbody>
	                <tr ng-repeat="attribute in attributesByLayer">
	                    <td>{{ attribute.name }} ({{ attribute.type }}) <b ng-if="attribute.required">*</b></td>
	                    <td>
	                        <select data-placeholder="" name="attribute"
	                                ng-model="attribute.option" class="form-control"
	                                ng-class="{ngInvalid: attribute.required && attribute.option == ''}"
	                                ng-change="setMarkerAttribute($index, attribute)"
	                                ng-required="attribute.required">
	                            <option ng-selected="markerAttribute.option == attribute.option" ng-repeat="markerAttribute in markerAttributes" value="{{ markerAttribute.option }}" ng-value="markerAttribute.option">
                                    {{ markerAttribute.attribute.name }} ({{markerAttribute.attribute.type}})
                                </option>
	                        </select>
	                    </td>
	                </tr>
                </tbody>
            </table>
        </div>

        <br style="clear: both"/>

    </div>

    <div class="modal-footer">
        <button class="btn btn-primary" title="<spring:message code='admin.layer-config.Continue'/>" ng-click="close(true)"><spring:message code='admin.layer-config.Continue'/></button>
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
