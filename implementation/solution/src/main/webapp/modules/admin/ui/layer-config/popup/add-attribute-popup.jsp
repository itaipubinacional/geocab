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
        <button type="button" class="close" ng-click="close(true)"><span aria-hidden="true">&times;</span><span class="sr-only">Close</span></button>
        <h3 class="modal-title">Adicionar atributo</h3>
    </div>
    <div class="modal-body" ng-init="initialize();" style="overflow: visible">
        <div ng-include="'assets/libs/eits-directives/alert/alert.html'"></div>

        <form novalidate name="form" default-button="buttonInsert" style="margin-bottom: 10px; margin-top: 10px;">
        	<input type="text" class="form-control" ng-model="currentEntity.name" placeholder="Nome do atributo">
			<select class="form-control" ng-model="currentEntity.type" placeholder="Tipo do atributo">
				<option value="TEXT">Text</option>
				<option value="NUMBER">Number</option>
				<option value="DATE">Date</option>
				<option value="BOOLEAN">Boolean</option>
			</select>
			Required <input class="form-control" type="checkbox" ng-model="currentEntity.required">
        </form>
        <br style="clear: both"/>

    </div>

    <div class="modal-footer">
        <button id="buttonInsert" ng-disabled="gridOptions.selectedItems.length == 0" class="btn btn-primary" ng-click="addAttribute()">Cadastrar</button>
        <button class="btn btn-default" ng-click="close(true)">Fechar</button>
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