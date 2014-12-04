'use strict';

/**
 *
 * @param $scope
 * @param $log
 * @param $location
 */
function AccessGroupController($scope, $injector, $log, $state, $timeout, $modal, $location, $importService, $translate) {

    /**
     * Injeta os métodos, atributos e seus estados herdados de AbstractCRUDController.
     * @see AbstractCRUDController
     */
    $injector.invoke(AbstractCRUDController, this, {$scope: $scope});
    
    //$importService("accessGroupService");

    /*-------------------------------------------------------------------
     * 		 				 	EVENT HANDLERS
     *-------------------------------------------------------------------*/

    /**
     *  Handler que escuta toda vez que o usuário/programadamente faz o sorting na ng-grid.
     *  Quando o evento é disparado, configuramos o pager do spring-data
     *  e chamamos novamente a consulta, considerando também o estado do filtro (@see $scope.data.filter)
     */
    $scope.$on('ngGridEventSorted', function (event, sort) {

        // compara os objetos para garantir que o evento seja executado somente uma vez q não entre em loop
        if (!angular.equals(sort, $scope.gridOptions.sortInfo)) {
            $scope.gridOptions.sortInfo = angular.copy(sort);

            //Order do spring-data
            var order = new Order();
            order.direction = sort.directions[0].toUpperCase();
            order.property = sort.fields[0];

            //Sort do spring-data
            $scope.currentPage.pageable.sort = new Sort();
            $scope.currentPage.pageable.sort.orders = [ order ];

            $scope.listGruposAcessoByFilters($scope.data.filter, $scope.currentPage.pageable);
        }
    });

    /*-------------------------------------------------------------------
     * 		 				 	ATTRIBUTES
     *-------------------------------------------------------------------*/
    //STATES
    /**
     * Variável estática que representa
     * o estado de listagem de registros.
     */
    $scope.LIST_STATE = "access-group.list";
    /**
     * Variável estática que representa
     * o estado de detalhe de um registro.
     */
    $scope.DETAIL_STATE = "access-group.detail";
    /**
     * Variável estática que representa
     * o estado para a criação de registros.
     */
    $scope.INSERT_STATE = "access-group.create";
    /**
     * Variável estática que representa
     * o estado para a edição de registros.
     */
    $scope.UPDATE_STATE = "access-group.edit";
    /**
     * Variável que armazena o estado corrente da tela.
     * Esta variável deve SEMPRE estar de acordo com a URL
     * que está no browser.
     */
    $scope.currentState;

    /**
     *
     * @type {Array}
     */
    $scope.removerCamadas = [];

    /**
     *
     * @type {Array}
     */
    $scope.adicionarCamadas = [];

    /**
     *
     * @type {Array}
     */
    $scope.camadasSelecionadas = [];

    /**
     *
     * @type {Array}
     */
    $scope.camadasOriginais = [];

    /**
     *
     * @type {Array}
     */
    $scope.removerPesquisas = [];

    /**
     *
     * @type {Array}
     */
    $scope.adicionarPesquisas = [];

    /**
     *
     * @type {Array}
     */
    $scope.pesquisasSelecionadas = [];

    /**
     *
     * @type {Array}
     */
    $scope.pesquisasOriginais = [];

    /**
     *
     * @type {boolean}
     */
    $scope.usuarioTab = false;

    //DATA GRID
    /**
     * Variável estática coms os botões de ações da grid
     * O botão de editar navega via URL (sref) por que a edição é feita em outra página,
     * já o botão de excluir chama um método direto via ng-click por que não tem um estado da tela específico.
     */
    var GRID_ACTION_BUTTONS = '<div class="cell-centered">' +
        '<a ui-sref="grupo-acesso.editar({id:row.entity.id})" title="Editar" class="btn btn-mini"><i class="itaipu-icon-edit"></i></a>' +
        '<a ng-if="row.entity.id != 1" ng-click="changeToRemove(row.entity)" title="Excluir" class="btn btn-mini"><i class="itaipu-icon-delete"></i></a>' +
        '</div>';

    /**
     * Configurações gerais da ng-grid.
     * @see https://github.com/angular-ui/ng-grid/wiki/Configuration-Options
     */
    $scope.gridOptions = {
        data: 'currentPage.content',
        multiSelect: false,
        useExternalSorting: true,
        headerRowHeight: 45,
        rowHeight: 45,
        beforeSelectionChange: function (row, event) {
            //evita chamar a selecao, quando clicado em um action button.
            if ($(event.target).is("a") || $(event.target).is("i")) return false;
            $state.go($scope.DETAIL_STATE, {id: row.entity.id});
        },
        columnDefs: [
            {displayName: 'Nome', field: 'nome'},
            {displayName: 'Descrição', field: 'descricao', width: '55%'},
            {displayName: 'Ações', sortable: false, cellTemplate: GRID_ACTION_BUTTONS, width: '100px'}
        ]
    };

    var IMAGE_LEGENDA = '<div align="center" class="ngCellText" ng-cell-text ng-class="col.colIndex()">' +
        '<img style="width: 20px; height: 20px; border: solid 1px #c9c9c9;" ng-src="{{row.entity.legenda}}"/>' +
        '</div>';

    var GRID_ACTION_CAMADAS_BUTTONS = '<div class="cell-centered">' +
        '<a ng-click="removeCamada(row.entity)" ng-if="currentState != DETAIL_STATE" title="Excluir" class="btn btn-mini"><i class="itaipu-icon-delete"></i></a>' +
        '</div>';

    var GRID_ACTION_PESQUISAS_BUTTONS = '<div class="cell-centered">' +
        '<a ng-click="removePesquisa(row.entity)" ng-if="currentState != DETAIL_STATE" title="Excluir" class="btn btn-mini"><i class="itaipu-icon-delete"></i></a>' +
        '</div>';

    var GRID_ACTION_FERRAMENTAS_BUTTONS = '<div class="cell-centered">' +
        '<a ng-click="removeFerramenta(row.entity)" ng-if="currentState != DETAIL_STATE" title="Excluir" class="btn btn-mini"><i class="itaipu-icon-delete"></i></a>' +
        '</div>';

    var GRID_ACTION_USUARIOS_BUTTONS = '<div class="cell-centered">' +
        '<a ng-click="removeUsuario(row.entity)" ng-if="currentState != DETAIL_STATE" title="Excluir" class="btn btn-mini"><i class="itaipu-icon-delete"></i></a>' +
        '</div>';

    $scope.gridCamadas = {
        data: 'camadasSelecionadas',
        multiSelect: false,
        useExternalSorting: false,
        headerRowHeight: 45,
        rowHeight: 45,
        beforeSelectionChange: function (row, event) {
        },
        columnDefs: [
            {displayName: 'Simbologia', field: 'legenda', sortable: false, width: '120px', cellTemplate: IMAGE_LEGENDA},
            {displayName: 'Título', field: 'titulo'},
            {displayName: 'Nome', field: 'nome', width: '40%'},
            {displayName: 'Fonte de dados', field: 'fonteDados.nome'},
            {displayName: 'Grupo de camadas', field: 'grupoCamadas.nome'},
            {displayName: 'Ações', sortable: false, cellTemplate: GRID_ACTION_CAMADAS_BUTTONS, width: '100px'}
        ]
    };

    $scope.gridPesquisasPersonalizadas = {
        data: 'pesquisasSelecionadas',
        multiSelect: false,
        useExternalSorting: false,
        headerRowHeight: 45,
        rowHeight: 45,
        beforeSelectionChange: function (row, event) {
        },
        columnDefs: [
            {displayName: 'Nome', field: 'nome'},
            {displayName: 'Camada', field: 'camada.nome', width: '55%'},
            {displayName: 'Ações', sortable: false, cellTemplate: GRID_ACTION_PESQUISAS_BUTTONS, width: '100px'}
        ]
    };

    $scope.gridFerramentas = {
        data: 'currentEntity.ferramentas',
        multiSelect: false,
        useExternalSorting: false,
        headerRowHeight: 45,
        rowHeight: 45,
        beforeSelectionChange: function (row, event) {
        },
        columnDefs: [
            {displayName: 'Descrição', field: 'descricao'},
            {displayName: 'Nome', field: 'nome', width: '55%'},
            {displayName: 'Ações', sortable: false, cellTemplate: GRID_ACTION_FERRAMENTAS_BUTTONS, width: '100px'}
        ]
    };

    $scope.gridUsuarios = {
        data: 'currentEntity.usuarios',
        multiSelect: false,
        useExternalSorting: false,
        headerRowHeight: 45,
        rowHeight: 45,
        columnDefs: [
            {displayName: 'Nome Completo', field: 'nomeCompleto', width: '35%'},
            {displayName: 'Nome de Usuário', field: 'username'},
            {displayName: 'E-mail', field: 'email' },
            {displayName: 'Ações', sortable: false, cellTemplate: GRID_ACTION_USUARIOS_BUTTONS, width: '100px'}
        ]
    };

    /**
     * Variável que armazena o estado da paginação
     * para renderizar o pager e também para fazer as requisições das
     * novas páginas, contendo o estado do Sort incluído.
     *
     * @type PageRequest
     */
    $scope.currentPage;

    //FORM
    /**
     * Variável para armazenar atributos do formulário que
     * não cabem em uma entidade. Ex.:
     * @filter - Filtro da consulta
     */
    $scope.data = { filter: null, showFields: true, tipoGrupoAcesso: 'WMS' };
    /**
     * Armazena a entitidade corrente para edição ou detalhe.
     */
    $scope.currentEntity;

    /*-------------------------------------------------------------------
     * 		 				 	  NAVIGATIONS
     *-------------------------------------------------------------------*/
    /**
     * Método principal que faz o papel de front-controller da tela.
     * Ele é invocado toda vez que ocorre uma mudança de URL (@see $stateChangeSuccess),
     * quando isso ocorre, obtém o estado através do $state e chama o método inicial daquele estado.
     * Ex.: /list -> changeToList()
     *      /criar -> changeToInsert()
     *
     * Caso o estado não for encontrado, ele direciona para a listagem,
     * apesar que o front-controller do angular não deixa digitar uma URL inválida.
     */
    $scope.initialize = function (toState, toParams, fromState, fromParams) {
        var state = $state.current.name;

        /**
         * É necessario remover o atributo sortInfo pois o retorno de uma edição estava duplicando o valor do mesmo com o atributo Sort
         * impossibilitando as ordenações nas colunas da grid.
         */
        if ($scope.gridOptions.sortInfo) {
            delete $scope.gridOptions.sortInfo;
        }

        $scope.usuarioTab = false;

        $log.info("Starting the front controller.");

        switch (state) {
            case $scope.LIST_STATE:
            {
                $scope.changeToList();
            }
                break;
            case $scope.DETAIL_STATE:
            {
                $scope.changeToDetail($state.params.id);
            }
                break;
            case $scope.INSERT_STATE:
            {
                $scope.changeToInsert();
            }
                break;
            case $scope.UPDATE_STATE:
            {
                $scope.changeToUpdate($state.params.id);
            }
                break;
            default :
            {
                $state.go($scope.LIST_STATE);
            }
        }
    };

    /**
     * Realiza os procedimentos iniciais (prepara o estado)
     * para a tela de consulta e após isso, muda o estado para list.
     * @see LIST_STATE
     * @see $stateChangeSuccess
     *
     * Para mudar para este estado, deve-se primeiro carregar os dados da consulta.
     */
    $scope.changeToList = function () {
        $log.info("changeToList");

        var pageRequest = new PageRequest();
        pageRequest.size = 10;
        $scope.pageRequest = pageRequest;

        $scope.listGruposAcessoByFilters(null, pageRequest);
    };

    /**
     * Realiza os procedimentos iniciais (prepara o estado)
     * para a tela de inserção e após isso, muda o estado para insert.
     * @see INSERT_STATE
     * @see $stateChangeSuccess
     *
     * Para mudar para este estado, deve-se primeiro instanciar um novo currentEntity,
     * para limpar os campos e configurar valores defaults.
     */
    $scope.changeToInsert = function () {
        $log.info("changeToInsert");

        $scope.currentEntity = {};

        $scope.currentEntity.usuarios = [];

        $scope.removerCamadas = [];
        $scope.adicionarCamadas = [];
        $scope.camadasSelecionadas = [];
        $scope.camadasOriginais = [];
        $scope.removerPesquisas = [];
        $scope.adicionarPesquisas = [];
        $scope.pesquisasSelecionadas = [];
        $scope.pesquisasOriginais = [];

        //Para funcionar com o ng-model no radio button
        $scope.currentEntity.tipoGrupoAcesso = 'WMS';

        $scope.currentState = $scope.INSERT_STATE;

    };

    /**
     * Realiza os procedimentos iniciais (prepara o estado)
     * para a tela de edição e após isso, muda o estado para update.
     * @see UPDATE_STATE
     * @see $stateChangeSuccess
     *
     * Para mudar para este estado, deve-se primeiro obter via id
     * o registro pelo serviço de consulta e só então mudar o estado da tela.
     */
    $scope.changeToUpdate = function (id) {
        $log.info("changeToUpdate", id);

//        grupoAcessoService.findGrupoAcessoById($state.params.id, {
//            callback: function (result) {
//                $scope.currentEntity = result;
//                $scope.currentState = $scope.UPDATE_STATE;
//                $state.go($scope.UPDATE_STATE);
//                $scope.$apply();
//
//                $scope.loadCamadasById(result.id);
//                $scope.loadPesquisasById(result.id);
//            },
//            errorHandler: function (message, exception) {
//                $scope.msg = {type: "danger", text: message, dismiss: true};
//                $scope.$apply();
//            }
//        });
    };

    /**
     * Realiza os procedimentos iniciais (prepara o estado)
     * para a tela de detalhe e após isso, muda o estado para detail.
     * @see DETAIL_STATE
     * @see $stateChangeSuccess
     *
     * Para mudar para este estado, deve-se primeiro obter via id
     * o registro atualizado pelo serviço de consulta e só então mudar o estado da tela.
     * Caso o indentificador esteja inválido, retorna para o estado de listagem.
     */
    $scope.changeToDetail = function (id) {
        $log.info("changeToDetail", id);

        if (id == null || id == "" || id == 0) {
            $scope.msg = {type: "error", text: $scope.INVALID_ID_MESSAGE, dismiss: true};
            $scope.currentState = $scope.LIST_STATE;
            $state.go($scope.LIST_STATE);
            return;
        }

//        grupoAcessoService.findGrupoAcessoById(id, {
//            callback: function (result) {
//                $scope.currentEntity = result;
//                $scope.currentState = $scope.DETAIL_STATE;
//                $state.go($scope.DETAIL_STATE, {id: id});
//                $scope.$apply();
//
//                $scope.loadCamadasById(result.id);
//                $scope.loadPesquisasById(result.id);
//            },
//            errorHandler: function (message, exception) {
//                $scope.msg = {type: "danger", text: message, dismiss: true};
//                $scope.$apply();
//            }
//        });
    };

    /**
     * Realiza os procedimentos iniciais (prepara o estado)
     * para a tela de exclusão.
     *
     * Antes de excluir, o usuário notificado para confirmação
     * e só então o registro é excluido.
     * Após excluído, atualizamos a grid com estado de filtro, paginação e sorting.
     */
    $scope.changeToRemove = function (grupoAcesso) {
        $log.info("changeToRemove", grupoAcesso);

        var dialog = $modal.open({
            templateUrl: "assets/libs/eits-directives/dialog/dialog-template.html",
            controller: DialogController,
            windowClass: 'dialog-delete',
            resolve: {
                title: function () {
                    return "Exclusão de grupo de acesso";
                },
                message: function () {
                    return 'Tem certeza que deseja excluir o grupo de acesso "' + grupoAcesso.nome + '"? <br/>Esta operação não poderá mais ser desfeita.';
                },
                buttons: function () {
                    return [
                        {label: 'Excluir', css: 'btn btn-danger'},
                        {label: 'Cancelar', css: 'btn btn-default', dismiss: true}
                    ];
                }
            }
        });

        dialog.result.then(function (result) {

//            grupoAcessoService.removeGrupoAcesso(grupoAcesso.id, {
//                callback: function (result) {
//                    //caso o currentPage esteja null, configura o pager default
//                    if ($scope.currentPage == null) {
//                        $scope.changeToList();
//                        //caso não, usa o mesmo estado para carregar a listagem
//                    } else {
//                        $scope.listGruposAcessoByFilters($scope.data.filter, $scope.currentPage.pageable);
//                    }
//
//                    $scope.msg = {type: "success", text: 'O registro "' + grupoAcesso.nome + '" foi excluído com sucesso.', dismiss: true};
//                },
//                errorHandler: function (message, exception) {
//                    $scope.msg = {type: "danger", text: message, dismiss: true};
//                    $scope.$apply();
//                }
//            });
        });
    };

    /**
     * Configura o pageRequest conforme o componente visual pager
     * e chama o serviçoe de listagem, considerando o filtro corrente na tela.
     *
     * @see currentPage
     * @see data.filter
     */
    $scope.changeToPage = function (filter, pageNumber) {
        $scope.currentPage.pageable.page = pageNumber - 1;
        $scope.listGruposAcessoByFilters(filter, $scope.currentPage.pageable);
    };

    /*-------------------------------------------------------------------
     * 		 				 	  BEHAVIORS
     *-------------------------------------------------------------------*/

    /**
     * Realiza a consulta de registros, consirando filtro, paginação e sorting.
     * Quando ok, muda o estado da tela para list.
     *
     * @see data.filter
     * @see currentPage
     */
    $scope.listGruposAcessoByFilters = function (filter, pageRequest) {

//        grupoAcessoService.listGrupoAcessoByFilters(filter, pageRequest, {
//            callback: function (result) {
//                $scope.currentPage = result;
//                $scope.currentPage.pageable.pageNumber++;//Para fazer o bind com o pagination
//                $scope.currentState = $scope.LIST_STATE;
//                $state.go($scope.LIST_STATE);
//                $scope.$apply();
//            },
//            errorHandler: function (message, exception) {
//                $scope.msg = {type: "danger", text: message, dismiss: true};
//                $scope.$apply();
//            }
//        });
    };

    /**
     * Realiza a inserção de um novo registro
     * e no suscesso, modifica o estado da tela para o detail.
     */
    $scope.insertGrupoAcesso = function (grupoAcesso) {

        if (!$scope.form().$valid) {
            $scope.msg = {type: "danger", text: $scope.INVALID_FORM_MESSAGE, dismiss: true};
            return;
        }

//        grupoAcessoService.saveGrupoAcessoNomeDescricao(null, grupoAcesso.nome, grupoAcesso.descricao, {
//            callback: function (result) {
//                $scope.usuarioTab = true;
//                $scope.currentEntity = result;
//                $scope.msg = {type: "success", text: "Grupo de acesso inserido com sucesso!", dismiss: true};
//                $scope.$apply();
//            },
//            errorHandler: function (message, exception) {
//                $scope.msg = {type: "danger", text: message, dismiss: true};
//                $scope.$apply();
//            }
//        });
    };

    /**
     * Realiza a atualiza de um registro
     * e no sucesso modifica o estado da tela para o estado de detalhe
     */
    $scope.updateGrupoAcesso = function (grupoAcesso) {

        if (!$scope.form().$valid) {
            $scope.msg = {type: "danger", text: $scope.INVALID_FORM_MESSAGE, dismiss: true};
            return;
        }

//        grupoAcessoService.saveGrupoAcessoNomeDescricao(grupoAcesso.id, grupoAcesso.nome, grupoAcesso.descricao, {
//            callback: function (result) {
//                $scope.usuarioTab = true;
//                $scope.currentEntity = result;
//                $scope.msg = {type: "success", text: "Grupo de acesso atualizado com sucesso!", dismiss: true};
//                $scope.$apply();
//            },
//            errorHandler: function (message, exception) {
//                if (exception.message.indexOf("ConstraintViolationException") > -1) {
//                    message = "O campo Nome ou Endereço informado já existe, altere e tente novamente.";
//                }
//                $scope.msg = {type: "danger", text: message, dismiss: true};
//                $scope.$apply();
//            }
//        });
    };

    /**
     * Testa conexão se é WMS ou WFS
     */
    $scope.testaConexaoGrupoAcesso = function (grupoAcesso) {

        if (!$scope.form().endereco.$valid) {
            $scope.msg = {type: "danger", text: $scope.INVALID_FORM_MESSAGE, dismiss: true};
            return;
        }

//        grupoAcessoService.testaConexao(grupoAcesso.endereco, grupoAcesso.tipoGrupoAcesso, {
//            callback: function (result) {
//                if (result) {
//                    $scope.msg = {type: "success", text: "Conexão estabelecida com êxito.", dismiss: true};
//                }
//                else {
//                    $scope.msg = {type: "danger", text: "Não foi possível estabelecer conexão com o grupo de acesso geográficos.", dismiss: true};
//                }
//                $scope.$apply();
//            },
//            errorHandler: function (message, exception) {
//                $scope.msg = {type: "danger", text: message, dismiss: true};
//                $scope.$apply();
//            }
//        });
    };


    /**
     * Abre uma popup para selecionar a camada a ser associada.
     */
    $scope.associarCamada = function () {
        //Função responsável por chamar a popup de configurações de camada para associação.
        var dialog = $modal.open({
            templateUrl: 'modules/administrativo/ui/pesquisa-personalizada/popup/configuracoes-camadas-popup.html',
            controller: SelectConfiguracoesCamadasGrupoAcessoPopUpController,
            windowClass: 'xx-dialog',
            resolve: {
                camadasSelecionadas: function () {
                    return $scope.camadasSelecionadas;
                }
            }
        });

        dialog.result.then(function (result) {
            if (result) {
                $scope.camadasSelecionadas = $scope.camadasSelecionadas.concat(result);
                for (var i = 0; i < result.length; i++) {
                    var index = $scope.findByIdInArray($scope.camadasOriginais, result[i]);
                    if (index == -1) {
                        $scope.adicionarCamadas.push(result[i]);
                    }
                    var index2 = $scope.findByIdInArray($scope.removerCamadas, result[i]);
                    if (index2 > -1) {
                        $scope.removerCamadas.splice(index2, 1);
                    }
                }
            }
        });
    };

    /**
     * Abre uma popup para selecionar as pesquisas personalizadas a serem associadas.
     */
    $scope.associarPesquisas = function () {
        //Função responsável por chamar a popup de configurações de camada para associação.
        var dialog = $modal.open({
            templateUrl: 'modules/administrativo/ui/grupo-acesso/popup/pesquisas-personalizadas-popup.html',
            controller: SelectPesquisasPersonalizadasPopUpController,
            windowClass: 'xx-dialog',
            resolve: {
                pesquisasSelecionadas: function () {
                    return $scope.pesquisasSelecionadas;
                }
            }
        });

        dialog.result.then(function (result) {
            if (result) {
                $scope.pesquisasSelecionadas = $scope.pesquisasSelecionadas.concat(result);
                for (var i = 0; i < result.length; i++) {
                    var index = $scope.findByIdInArray($scope.pesquisasOriginais, result[i]);
                    if (index == -1) {
                        $scope.adicionarPesquisas.push(result[i]);
                    }
                    var index2 = $scope.findByIdInArray($scope.removerPesquisas, result[i]);
                    if (index2 > -1) {
                        $scope.removerPesquisas.splice(index2, 1);
                    }
                }
            }
        });
    };

    /**
     * Abre uma popup para selecionar as ferramentas a serem associadas.
     */
    $scope.associarFerramentas = function () {
        //Função responsável por chamar a popup de ferramentas para associação.
        var dialog = $modal.open({
            templateUrl: 'modules/administrativo/ui/grupo-acesso/popup/ferramentas-popup.html',
            controller: SelectFerramentasPopUpController,
            windowClass: 'xx-dialog',
            resolve: {
                ferramentasSelecionadas: function () {
                    return $scope.currentEntity.ferramentas;
                }
            }
        });

        dialog.result.then(function (result) {

            if (result != null) {

                $scope.ferramentasSelecionadas = result;

                for (var i = 0; i < $scope.ferramentasSelecionadas.length; i++) {
                    $scope.ferramentasSelecionadas[i].ordem = i;
                }
            }
            $scope.currentEntity.ferramentas = $scope.ferramentasSelecionadas;
        });
    };

    /**
     * Abre uma popup para selecionar as ferramentas a serem associadas.
     */
    $scope.associarUsuarios = function () {
        //Função responsável por chamar a popup de usuários para associação.
        var dialog = $modal.open({
            templateUrl: 'modules/administrativo/ui/grupo-acesso/popup/usuarios-ldap-popup.html',
            controller: SelectUsuariosPopUpController,
            windowClass: 'xx-dialog',
            resolve: {
                usuariosSelecionados: function () {
                    return $scope.currentEntity.usuarios;
                }
            }
        });

        dialog.result.then(function (result) {

            if (result != null) {
                $scope.currentEntity.usuarios.push(result);
            }
        });
    };

    /**
     * Limpa os campos
     */
    $scope.clearFields = function () {
        if (!$scope.data.showFields) {
            $scope.currentEntity.nomeUsuario = "";
            $scope.currentEntity.senha = "";
        }
    };

    /**
     *
     * @param entity
     */
    $scope.removeCamada = function (entity) {
        var index = $scope.camadasSelecionadas.indexOf(entity);
        var index2 = $scope.adicionarCamadas.indexOf(entity);
        var index3 = $scope.camadasOriginais.indexOf(entity);
        if (index > -1) {
            $scope.camadasSelecionadas.splice(index, 1);
        }
        if (index2 > -1) {
            $scope.adicionarCamadas.splice(index2, 1);
        }
        if (index3 > -1) {
            $scope.removerCamadas.push(entity);
        }
    };

    /**
     *
     * @param entity
     */
    $scope.removePesquisa = function (entity) {
        var index = $scope.pesquisasSelecionadas.indexOf(entity);
        var index2 = $scope.adicionarPesquisas.indexOf(entity);
        var index3 = $scope.pesquisasOriginais.indexOf(entity);
        if (index > -1) {
            $scope.pesquisasSelecionadas.splice(index, 1);
        }
        if (index2 > -1) {
            $scope.adicionarPesquisas.splice(index2, 1);
        }
        if (index3 > -1) {
            $scope.removerPesquisas.push(entity);
        }
    };

    /**
     *
     * @param entity
     */
    $scope.removeFerramenta = function (entity) {
        var index = $scope.currentEntity.ferramentas.indexOf(entity);
        if (index > -1) {
            $scope.currentEntity.ferramentas.splice(index, 1);
        };
    };

    /**
     *
     * @param entity
     */
    $scope.removeUsuario = function (entity) {
        var index = $scope.currentEntity.usuarios.indexOf(entity);
        if (index > -1) {
            $scope.currentEntity.usuarios.splice(index, 1);
        };
    };

    /**
     *
     * @param camadas
     */
    $scope.linkCamadas = function() {
//        grupoAcessoService.linkCamada($scope.adicionarCamadas, $scope.currentEntity.id, {
//            callback: function(result){
//                $scope.msg = {type: "success", text: "Alterações efetuadas com sucesso", dismiss: true};
//                $scope.adicionarCamadas = [];
//                $scope.$apply();
//            },
//            errorHandler: function(error){
//                $scope.msg = {type: "danger", text: "Erro: "+error, dismiss: true};
//            }
//        });
    };

    /**
     *
     * @param pesquisas
     */
    $scope.linkPesquisasPersonalizadas = function() {
//        grupoAcessoService.linkPesquisaPersonalizada($scope.adicionarPesquisas, $scope.currentEntity.id, {
//            callback: function(result){
//                $scope.msg = {type: "success", text: "Alterações efetuadas com sucesso", dismiss: true};
//                $scope.adicionarPesquisas = [];
//                $scope.$apply();
//            },
//            errorHandler: function(error){
//                $scope.msg = {type: "danger", text: "Erro: "+error, dismiss: true};
//            }
//        })
    }

    /**
     *
     * @param camada
     * @param grupoAcesso
     */
    $scope.unlinkCamadas = function() {
//        grupoAcessoService.unlinkCamada($scope.removerCamadas, $scope.currentEntity.id, {
//            callback: function(result){
//                $scope.msg = {type: "success", text: "Alterações efetuadas com sucesso", dismiss: true};
//                $scope.removerCamadas = [];
//                $scope.$apply();
//            },
//            errorHandler: function(error){
//                $scope.msg = {type: "danger", text: "Erro: "+error, dismiss: true};
//            }
//        });
    };

    /**
     *
     * @param pesquisa
     * @param grupoAcesso
     */
    $scope.unlinkPesquisaPersonalizada = function() {
//        grupoAcessoService.unlinkPesquisaPersonalizada($scope.removerPesquisas, $scope.currentEntity.id, {
//            callback: function(result){
//                $scope.msg = {type: "success", text: "Alterações efetuadas com sucesso", dismiss: true};
//                $scope.removerPesquisas = [];
//                $scope.$apply();
//            },
//            errorHandler: function(error){
//                $scope.msg = {type: "danger", text: "Erro: "+error, dismiss: true};
//            }
//        });
    };

    /**
     *
     */
    $scope.salvarCamadas = function () {
        if ($scope.adicionarCamadas.length > 0) {
            $scope.linkCamadas();
        }
        if ($scope.removerCamadas.length > 0) {
            $scope.unlinkCamadas();
        }
    }

    /**
     *
     */
    $scope.salvarPesquisas = function () {
        if ($scope.adicionarPesquisas.length > 0) {
            $scope.linkPesquisasPersonalizadas();
        }
        if ($scope.removerPesquisas.length > 0) {
            $scope.unlinkPesquisaPersonalizada();
        }
    }

    /**
     *
     * @param grupoId
     */
    $scope.loadCamadasById = function(grupoId){
//        grupoAcessoService.listCamadaByGrupoAcessoId(grupoId, {
//            callback: function(result){
//                $scope.camadasSelecionadas = result;
//                $scope.camadasOriginais = result.slice(0);
//                $scope.$apply();
//            },
//            errorHandler: function(error){
//                $log.error(error);
//            }
//        });
    };

    /**
     *
     * @param grupoId
     */
    $scope.loadPesquisasById = function(grupoId){
//        grupoAcessoService.listPesquisaPersonalizadaByGrupoAcessoId(grupoId, {
//            callback: function(result){
//                $scope.pesquisasSelecionadas = result;
//                $scope.pesquisasOriginais = result.slice(0);
//                $scope.$apply();
//            },
//            errorHandler: function(error){
//                $log.error(error);
//            }
//        });
    };

    /**
     *
     */
    $scope.updateUsuarios = function() {
//        grupoAcessoService.updateGrupoAcessoUsuarios($scope.currentEntity.id, $scope.currentEntity.usuarios, {
//            callback: function(result){
//                $scope.msg = {type: "success", text: "Alterações efetuadas com sucesso", dismiss: true};
//                $scope.$apply();
//            },
//            errorHandler: function(error){
//                $log.error(error);
//            }
//        });
    }

    /**
     *
     */
    $scope.updateFerramentas = function() {
//        grupoAcessoService.updateGrupoAcessoFerramentas($scope.currentEntity.id, $scope.currentEntity.ferramentas, {
//            callback: function(result){
//                $scope.msg = {type: "success", text: "Alterações efetuadas com sucesso", dismiss: true};
//                $scope.$apply();
//            },
//            errorHandler: function(error){
//                $log.error(error);
//            }
//        });
    }

    /**
     *
     */
    $scope.verificaTab = function(){
        if ($scope.currentEntity.id == null){

        } else {
            console.log("salvou")
        }
    }

    /**
     *
     */
    $scope.salvarAssociacoes = function(){
        $scope.salvarCamadas();
        $scope.salvarPesquisas();
        $scope.updateFerramentas();
        $scope.updateUsuarios();
    }

};