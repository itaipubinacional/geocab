<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<!-- Grupo de camadas - Main View -->
<div>
     <div class="navbar">

         <!--Mensagens-->
         <div ng-include="'static/libs/eits-directives/alert/alert.html'" style="margin-bottom: 15px"></div>

         <div class="navbar-inner navbar-container">
			<div ng-switch on="currentState" class="navbar-title" style="float: left; width: 180px;">
			    <span ng-switch-when="grupo-camadas.listar">GRUPO DE CAMADAS</span>
			    <span ng-switch-default>GRUPO DE CAMADAS - Carregando...</span>
			</div>
			
			<button ng-show="currentState == LIST_STATE" style="float: right; margin-bottom: 15px;" ng-click="publishGrupoCamadas()"
			        class="btn btn-warning">Publicar
			</button>
			
			<button ng-show="currentState == LIST_STATE" style="float: right; margin-bottom: 15px;" ng-click="saveGrupoCamadas()"
			        class="btn btn-success">Salvar ordenaÃ§Ã£o
			</button>
             
			<button ng-show="currentState == LIST_STATE" style="float: left; margin-bottom: 15px; width: 122px;" ng-click="newGrupoCamadas()"
				class="btn btn-default"><div class="icon itaipu-icon-folder" style="float: left;margin-top: 2px;margin-right: 4px;"></div>Novo grupo
			</button>

         </div>
     </div>
    
    <!-- Partial views dos states -->
	<div ng-switch on="currentState">
        <div ng-switch-when="layer-group.list">
        	<div ng-include="'modules/admin/ui/layer-group/layer-group-list.jsp'"></div>
        </div>
    </div>
</div>
</html>