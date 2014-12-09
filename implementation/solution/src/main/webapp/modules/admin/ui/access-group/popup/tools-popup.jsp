<!DOCTYPE html>
<html>

<div class="modal-content">

	<div class="modal-header">
		<button type="button" class="close" ng-click="close(true)"><span aria-hidden="true">&times;</span><span class="sr-only">Close</span></button>
	    <h3 class="modal-title">Associar Ferramentas</h3>
	</div>

	<div class="modal-body" ng-init="initialize();" style="overflow: visible">
	
		<div ng-include="assets/libs/eits-directives/alert/alert.html"></div>
		
        <form novalidate name="form" default-button="{{buttonInsert}}">
            <input type="text" ng-model="filter.filterText" class="form-control" placeholder="Pesquisar por nome ou descrição" style="width: 300px; float: left; margin-bottom: 10px;"/>
            <!--<input type="submit" class="btn btn-default" style="margin-left: 5px" value="Pesquisar" ng-disabled="currentPage == null" ng-click="list(data.filter, currentPage.pageable )"/>-->
        </form>

		<br style="clear: both"/>

		<div ng-show="showLoading" class="grid-loading"></div>
		<div ng-grid="gridOptions" style="height: 350px; border: 1px solid rgb(212,212,212);"></div>

		<div class="grid-elements-count" ng-show="ferramentas.length > 0" style="margin-top: 7px;">
			{{ferramentas.length}} registros
		</div>
        
	</div>
	<div class="modal-footer">
		<button id="buttonClose" class="btn btn-primary" ng-click="close(false)">Selecionar</button>
        <button class="btn btn-default" ng-click="close(true)">Fechar</button>
	</div>

</div>

</html>