<!DOCTYPE html>
<html>
<!-- Fonte de dados - Main View -->
<div>
    <div class="navbar">

        <!--Mensagens-->
        <div ng-include="'assets/libs/eits-directives/alert/alert.html'"></div>

        <!-- Barra de Controle -->
        <div class="navbar-inner navbar-container">
            <div ng-switch on="currentState" class="navbar-title">
                <span ng-switch-when="layer-config.list">LISTA DE CAMADAS</span>
                <span ng-switch-when="layer-config.detail">DETALHE DE CAMADA</span>
                <span ng-switch-when="layer-config.create">NOVA CAMADA</span>
                <span ng-switch-when="layer-config.update">ALTERAÇÃO DE CAMADA</span>
                <span ng-switch-default>LISTA DE CAMADAS - Carregando...</span>
            </div>

            <!-- State Listar -->
            <button ng-show="currentState == LIST_STATE" style="float: right;"
                    class="btn btn-primary"
                    ui-sref="layer-config.create">Nova camada
            </button>

            <!-- State Detalhe -->
            <button ng-show="currentState == DETAIL_STATE" style="float: left; margin-right: 15px; min-width: 40px;"
                    class="btn btn-default"
                    ui-sref="layer-config.list"><span class="icon itaipu-icon-arrow-left"></span>
            </button> 
            
            <button ng-show="currentState == DETAIL_STATE" style="float: right;"
                    class="btn btn-danger"
                    ng-click="changeToRemove(currentEntity)">Excluir
            </button>
            <button ng-show="currentState == DETAIL_STATE" style="float: right;"
                    class="btn btn-primary"
                    ui-sref="layer-config.update( {id:currentEntity.id} )">Alterar
            </button>

            <!-- State Criar | Editar -->
            <button ng-show="currentState == INSERT_STATE || currentState == UPDATE_STATE"
                    style="float: left; margin-right: 15px; min-width: 40px;"
                    class="btn btn-default"
                    ui-sref="layer-config.list"><span class="icon itaipu-icon-arrow-left"></span>
            </button>

            <!-- State Criar -->
            <button ng-show="currentState == INSERT_STATE" style="float: right;"
                    class="btn btn-success"
                    id="buttonInsert"
                    ng-click="insertCamada(currentEntity)">Salvar
            </button>
            <!-- State Editar -->
            <button ng-show="currentState == UPDATE_STATE" style="float: right;"
                    class="btn btn-success"
                    id="buttonUpdate"
                    ng-click="updateCamada(currentEntity)">Salvar
            </button>
        </div>
    </div>

    <!-- Partial views dos states -->
    <div ng-switch on="currentState">
        <div ng-switch-when="layer-config.list">
            <div ng-include="'modules/admin/ui/layer-config/layer-config-list.jsp'"></div>
        </div>
        <div ng-switch-when="layer-config.detail">
            <div ng-include="'modules/admin/ui/layer-config/layer-config-detail.jsp'"></div>
        </div>
        <div ng-switch-when="layer-config.create">
            <div ng-include="'modules/admin/ui/layer-config/layer-config-form.jsp'"></div>
        </div>
        <div ng-switch-when="layer-config.update">
            <div ng-include="'modules/admin/ui/layer-config/layer-config-form.jsp'"></div>
        </div>
        <div ng-switch-default>
            <div ng-include="'modules/common/loading.jsp'"></div>
        </div>
    </div>
</div>
</html>