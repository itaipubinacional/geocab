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

        <div>
            <table>
                <thead>
                <tr>
                    <th>Nome</th>
                    <th>Tipo</th>
                    <th>Obrigatório</th>
                    <th>Exibir Atributo</th>
                    <th>Ações</th>
                </tr>
                </thead>
                <tbody ui-sortable ng-model="attributes">
                    <tr ng-repeat="attribute in markerAttributes">
                        <td><input type="text" ng-model="attribute.attribute.name"></td>
                        <td>
                            <select name="select" class="form-control" ng-model="attribute.attribute.type"  style="margin-bottom: 15px"
                                    ng-class="{ngInvalid: form_add_attribute.$submitted && form_add_attribute.select.$error.required }"
                                    required
                            >
                            <option value="" disabled selected style="display:none" ><spring:message code="admin.layer-config.Attribute-type" /></option>
                            <option value="TEXT"><spring:message code="admin.layer-config.Text" /></option>
                            <option value="NUMBER"><spring:message code="admin.layer-config.Number" /></option>
                            <option value="DATE"><spring:message code="admin.layer-config.Date" /></option>
                            <option value="BOOLEAN"><spring:message code="admin.layer-config.Boolean" /></option>
                            <option value="PHOTO_ALBUM"><spring:message code="admin.layer-config.Photo-album" /></option>
                        </select>
                        </td>
                        <td><input type="checkbox" value="attribute.required" ng-value="attribute.required"></td>
                        <td><input type="checkbox" value="attribute.visible" ng-value="attribute.visible"></td>
                        <td><button ng-click="removeAttribute($index)" value="Remove">Remove</button></td>
                    </tr>
                </tbody>
            </table>
        </div>

        <div>
            <button title="<spring:message code="admin.layer-config.Add-attribute" />" class="btn btn-primary" ng-click="addAttribute()"><spring:message code="admin.layer-config.Add-attribute" /></button>
        </div>

      <br style="clear: both"/>

    </div>

    <div class="modal-footer">
        <button class="btn btn-default" title="<spring:message code="admin.layer-config.Continue" />" ng-click="close(true)"><spring:message code="admin.layer-config.Continue" /></button>
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