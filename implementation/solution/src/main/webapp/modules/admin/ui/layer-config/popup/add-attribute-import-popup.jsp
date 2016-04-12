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
            <table class="ng-grid table">
                <thead>
                <tr>
                    <th></th>
                    <th>Nome</th>
                    <th>Tipo</th>
                    <th>Obrigatório</th>
                    <th>Exibir Atributo</th>
                    <th>Ações</th>
                </tr>
                </thead>
                <tbody sv-root sv-part="markerAttributes">
                    <tr sv-element ng-repeat="attribute in markerAttributes">
                        <td sv-handle class="reorder"></td>
                        <td><input type="text" class="form-control" ng-model="attribute.attribute.name"></td>
                        <td>
                            <select name="select" class="form-control" ng-model="attribute.attribute.type"
                                    ng-class="{ngInvalid: form_add_attribute.$submitted && form_add_attribute.select.$error.required }"
                                    required>
                            <option value="" disabled selected style="display:none" ><spring:message code="admin.layer-config.Attribute-type" /></option>
                            <option value="TEXT"><spring:message code="admin.layer-config.Text" /></option>
                            <option value="NUMBER"><spring:message code="admin.layer-config.Number" /></option>
                            <option value="DATE"><spring:message code="admin.layer-config.Date" /></option>
                            <option value="BOOLEAN"><spring:message code="admin.layer-config.Boolean" /></option>
                            <option value="PHOTO_ALBUM"><spring:message code="admin.layer-config.Photo-album" /></option>
                        </select>
                        </td>
                        <td style="text-align: center"><input type="checkbox" ng-model="attribute.attribute.required"></td>
                        <td style="text-align: center"><input type="checkbox" ng-model="attribute.attribute.visible"></td>
                        <td>
                            <a ng-click="removeAttribute($index)" title="Excluir" class="btn btn-mini"><i class="itaipu-icon-delete"></i></a>
                        </td>
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