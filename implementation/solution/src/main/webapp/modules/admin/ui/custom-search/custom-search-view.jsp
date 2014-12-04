<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<!-- Fonte de dados - Main View -->
<div>
	<div class="navbar">

        <!--Mensagens-->
        <div class="msg" ng-include="'static/libs/eits-directives/alert/alert.html'"></div>

    	<!-- Barra de Controle -->
        <div class="navbar-inner navbar-container">
            <div ng-switch on="currentState" class="navbar-title">
                <span ng-switch-when="custom-search.list"><spring:message code="admin.custom-search.Custom-search-list"/></span>
                <span ng-switch-when="pesquisa-personalizada.detalhe">DETALHE DE PESQUISA PERSONALIZADA</span>
                <span ng-switch-when="pesquisa-personalizada.criar">NOVA PESQUISA PERSONALIZADA</span>
                <span ng-switch-when="pesquisa-personalizada.editar">ALTERAÃ‡ÃƒO DE PESQUISA PERSONALIZADA</span>
                <span ng-switch-default>PESQUISAS PERSONALIZADAS - Carregando...</span>
            </div>

            <!-- State Listar -->
            <button ng-show="currentState == LIST_STATE" style="float: right;"
                class="btn btn-primary"
                ui-sref="pesquisa-personalizada.criar">Nova pesquisa personalizada
            </button>

            <!-- State Detalhe -->
            <button ng-show="currentState == DETAIL_STATE" style="float: left; margin-right: 15px; min-width: 40px;"
                    class="btn btn-default"
                    ui-sref="custom-search.list"><span class="icon itaipu-icon-arrow-left"></span>
            </button>
            
            <button ng-show="currentState == DETAIL_STATE" style="float: right;"
                class="btn btn-danger"
                ng-click="changeToRemove(currentEntity)">Excluir
            </button>
            <button ng-show="currentState == DETAIL_STATE" style="float: right;"
                class="btn btn-primary"
                ui-sref="pesquisa-personalizada.editar( {id:currentEntity.id} )">Alterar
            </button>

            <!-- State Criar | Editar -->
            <button ng-show="currentState == INSERT_STATE || currentState == UPDATE_STATE" style="float: left; margin-right: 15px; min-width: 40px;"
                    class="btn btn-default"
                    ui-sref="custom-search.list"><span class="icon itaipu-icon-arrow-left"></span>
            </button>
			
            <!-- State Criar -->
            <button ng-show="currentState == INSERT_STATE" style="float: right;"
                class="btn btn-success"
                id="buttonInsert"
                ng-click="insertPesquisaPersonalizada(currentEntity)">Salvar
            </button>
            <!-- State Editar -->
            <button ng-show="currentState == UPDATE_STATE" style="float: right;"
                class="btn btn-success"
                id="buttonUpdate"
                ng-click="updatePesquisaPersonalizada(currentEntity)">Salvar
            </button>
        </div>
    </div>
    
    <!-- Partial views dos states -->
	<div ng-switch on="currentState">
        <div ng-switch-when="custom-search.list">
        	<div ng-include="'modules/admin/ui/custom-search/custom-search-list.jsp'"></div>
        </div>
        <div ng-switch-when="custom-search.detail">
        	<div ng-include="'modules/admin/ui/custom-search/custom-search-detail.jsp'"></div>
        </div>
        <div ng-switch-when="custom-search.create">
        	<div ng-include="'modules/admin/ui/custom-search/custom-search-form.jsp'"></div>	
        </div> 
        <div ng-switch-when="custom-search.update">
        	<div ng-include="modules/admin/ui/custom-search/custom-search-form.jsp'"></div>
        </div>
        <div ng-switch-default>
        	<div ng-include="'modules/loading.html'"></div>
        </div>
    </div>
</div>
</html>