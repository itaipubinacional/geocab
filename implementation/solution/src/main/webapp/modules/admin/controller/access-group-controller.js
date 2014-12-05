'use strict';

/**
 *
 * @param $scope
 * @param $log
 * @param $location
 */
function AccessGroupController($scope, $injector, $log, $state, $timeout, $modal, $location, $importService, $translate) {

    /**
     * Injeta os m√©todos, atributos e seus estados herdados de AbstractCRUDController.
     * @see AbstractCRUDController
     */
    $injector.invoke(AbstractCRUDController, this, {$scope: $scope});
    
    $importService("accessGroupService");

    /*-------------------------------------------------------------------
     * 		 				 	EVENT HANDLERS
     *-------------------------------------------------------------------*/

    /**
     *  Handler que escuta toda vez que o usu√°rio/programadamente faz o sorting na ng-grid.
     *  Quando o evento √© disparado, configuramos o pager do spring-data
     *  e chamamos novamente a consulta, considerando tamb√©m o estado do filtro (@see $scope.data.filter)
     */
    $scope.$on('ngGridEventSorted', function (event, sort) {

        // compara os objetos para garantir que o evento seja executado somente uma vez q n√£o entre em loop
        if (!angular.equals(sort, $scope.gridOptions.sortInfo)) {
            $scope.gridOptions.sortInfo = angular.copy(sort);

            //Order do spring-data
            var order = new Order();
            order.direction = sort.directions[0].toUpperCase();
            order.property = sort.fields[0];

            //Sort do spring-data
            $scope.currentPage.pageable.sort = new Sort();
            $scope.currentPage.pageable.sort.orders = [ order ];

            $scope.listAccessGroupByFilters($scope.data.filter, $scope.currentPage.pageable);
        }
    });

    /*-------------------------------------------------------------------
     * 		 				 	ATTRIBUTES
     *-------------------------------------------------------------------*/
    //STATES
    /**
     * Vari√°vel est√°tica que representa
     * o estado de listagem de registros.
     */
    $scope.LIST_STATE = "access-group.list";
    /**
     * Vari√°vel est√°tica que representa
     * o estado de detalhe de um registro.
     */
    $scope.DETAIL_STATE = "access-group.detail";
    /**
     * Vari√°vel est√°tica que representa
     * o estado para a cria√ß√£o de registros.
     */
    $scope.INSERT_STATE = "access-group.create";
    /**
     * Vari√°vel est√°tica que representa
     * o estado para a edi√ß√£o de registros.
     */
    $scope.UPDATE_STATE = "access-group.update";
    /**
     * Vari√°vel que armazena o estado corrente da tela.
     * Esta vari√°vel deve SEMPRE estar de acordo com a URL
     * que est√° no browser.
     */
    $scope.currentState;

    /**
     *
     * @type {Array}
     */
    $scope.removeLayers = [];

    /**
     *
     * @type {Array}
     */
    $scope.addLayers = [];

    /**
     *
     * @type {Array}
     */
    $scope.selectedLayers = [];

    /**
     *
     * @type {Array}
     */
    $scope.originalLayers= [];

    /**
     *
     * @type {Array}
     */
    $scope.removeSearchs = [];

    /**
     *
     * @type {Array}
     */
    $scope.addSearchs = [];

    /**
     *
     * @type {Array}
     */
    $scope.selectedSearchs = [];

    /**
     *
     * @type {Array}
     */
    $scope.originalSearchs = [];

    /**
     *
     * @type {boolean}
     */
    $scope.userTab = false;

    //DATA GRID
    /**
     * Vari√°vel est√°tica coms os bot√µes de a√ß√µes da grid
     * O bot√£o de editar navega via URL (sref) por que a edi√ß√£o √© feita em outra p√°gina,
     * j√° o bot√£o de excluir chama um m√©todo direto via ng-click por que n√£o tem um estado da tela espec√≠fico.
     */
    var GRID_ACTION_BUTTONS = '<div class="cell-centered">' +
        '<a ui-sref="access-group.update({id:row.entity.id})" title="Editar" class="btn btn-mini"><i class="itaipu-icon-edit"></i></a>' +
        '<a ng-if="row.entity.id != 1" ng-click="changeToRemove(row.entity)" title="Excluir" class="btn btn-mini"><i class="itaipu-icon-delete"></i></a>' +
        '</div>';

    /**
     * Configura√ß√µes gerais da ng-grid.
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
            {displayName: 'Name', field: 'name'},
            {displayName: 'Description', field: 'description', width: '55%'},
            {displayName: 'Actions', sortable: false, cellTemplate: GRID_ACTION_BUTTONS, width: '100px'}
        ]
    };

    var IMAGE_SUBTITLE = '<div align="center" class="ngCellText" ng-cell-text ng-class="col.colIndex()">' +
        '<img style="width: 20px; height: 20px; border: solid 1px #c9c9c9;" ng-src="{{row.entity.subtitle}}"/>' +
        '</div>';
    
    var IMAGE_SUBTITLE_SEARCH = '<div align="center" class="ngCellText" ng-cell-text ng-class="col.colIndex()">' +
    '<img style="width: 20px; height: 20px; border: solid 1px #c9c9c9;" ng-src="{{row.entity.camada.legenda}}"/>' +
    '</div>';

    var GRID_ACTION_LAYER_BUTTONS = '<div class="cell-centered">' +
        '<a ng-click="removeLayer(row.entity)" ng-if="currentState != DETAIL_STATE" title="Delete" class="btn btn-mini"><i class="itaipu-icon-delete"></i></a>' +
        '</div>';

    var GRID_ACTION_SEARCH_BUTTONS = '<div class="cell-centered">' +
        '<a ng-click="removeSearch(row.entity)" ng-if="currentState != DETAIL_STATE" title="Delete" class="btn btn-mini"><i class="itaipu-icon-delete"></i></a>' +
        '</div>';
    
    var GRID_ACTION_TOOLS_BUTTONS = '<div class="cell-centered">' +
    '<a ng-click="removeFerramenta(row.entity)" ng-if="currentState != DETAIL_STATE" title="Excluir" class="btn btn-mini"><i class="itaipu-icon-delete"></i></a>' +
    '</div>';

    var GRID_ACTION_USER_BUTTONS = '<div class="cell-centered">' +
        '<a ng-click="removeUser(row.entity)" ng-if="currentState != DETAIL_STATE" title="Delete" class="btn btn-mini"><i class="itaipu-icon-delete"></i></a>' +
        '</div>';

    $scope.gridCamadas = {
        data: 'selectedLayers',
        multiSelect: false,
        useExternalSorting: false,
        headerRowHeight: 45,
        rowHeight: 45,
        beforeSelectionChange: function (row, event) {
        },
        columnDefs: [
            {displayName: 'Symbology', field: 'subtitle', sortable: false, width: '120px', cellTemplate: IMAGE_SUBTITLE},
            {displayName: 'Title', field: 'title'},
            {displayName: 'Name', field: 'name', width: '40%'},
            {displayName: 'Data source', field: 'dataSource.name'},
            {displayName: 'Layer group', field: 'layerGroup.name'},
            {displayName: 'Actions', sortable: false, cellTemplate: GRID_ACTION_LAYER_BUTTONS, width: '100px'}
        ]
    };

    $scope.gridCustomSearch = {
        data: 'selectedSearch',
        multiSelect: false,
        useExternalSorting: false,
        headerRowHeight: 45,
        rowHeight: 45,
        beforeSelectionChange: function (row, event) {
        },
        columnDefs: [
			{displayName: 'Symbology', field: 'subtitle', sortable: false, width: '120px', cellTemplate: IMAGE_SUBTITLE_SEARCH},
			{displayName: 'Name', field: 'name', width: '40%'},
			{displayName: 'Layer\'s Title', field: 'title'},
			{displayName: 'Layer\'s Name', field: 'layer.name', width: '55%'},
			{displayName: 'Data source', field: 'dataSource.name'},
			{displayName: 'Actions', sortable: false, cellTemplate: GRID_ACTION_SEARCH_BUTTONS, width: '100px'}
        ]
    };  

    $scope.gridFerramentas = {
            data: 'currentEntity.ferramentas',
            multiSelect: false,
            useExternalSorting: false,
            enableSorting: true,
            headerRowHeight: 45,
            rowHeight: 45,
            beforeSelectionChange: function (row, event) {
            },
            columnDefs: [
                {displayName: 'DescriÁ„o', field: 'descricao'},
                {displayName: 'Nome', field: 'nome', width: '55%'},
                {displayName: 'AÁıes', sortable: false, cellTemplate: GRID_ACTION_TOOLS_BUTTONS, width: '100px'}
            ]
        };
    
    $scope.gridUser = {
        data: 'currentEntity.user',
        multiSelect: false,
        useExternalSorting: false,
        headerRowHeight: 45,
        rowHeight: 45,
        columnDefs: [
            {displayName: 'Full name', field: 'fullName', width: '35%'},
            {displayName: 'User Name', field: 'username'},
            {displayName: 'Action', sortable: false, cellTemplate: GRID_ACTION_USER_BUTTONS, width: '100px'}
        ]
    };

    /**
     * Vari√°vel que armazena o estado da pagina√ß√£o
     * para renderizar o pager e tamb√©m para fazer as requisi√ß√µes das
     * novas p√°ginas, contendo o estado do Sort inclu√≠do.
     *
     * @type PageRequest
     */
    $scope.currentPage;

    //FORM
    /**
     * Vari√°vel para armazenar atributos do formul√°rio que
     * n√£o cabem em uma entidade. Ex.:
     * @filter - Filtro da consulta
     */
    $scope.data = { filter: null, showFields: true, tipoGrupoAcesso: 'WMS' };
    /**
     * Armazena a entitidade corrente para edi√ß√£o ou detalhe.
     */
    $scope.currentEntity;

    /*-------------------------------------------------------------------
     * 		 				 	  NAVIGATIONS
     *-------------------------------------------------------------------*/
    /**
     * M√©todo principal que faz o papel de front-controller da tela.
     * Ele √© invocado toda vez que ocorre uma mudan√ßa de URL (@see $stateChangeSuccess),
     * quando isso ocorre, obt√©m o estado atrav√©s do $state e chama o m√©todo inicial daquele estado.
     * Ex.: /list -> changeToList()
     *      /criar -> changeToInsert()
     *
     * Caso o estado n√£o for encontrado, ele direciona para a listagem,
     * apesar que o front-controller do angular n√£o deixa digitar uma URL inv√°lida.
     */
    $scope.initialize = function (toState, toParams, fromState, fromParams) {
        var state = $state.current.name;

        /**
         * √â necessario remover o atributo sortInfo pois o retorno de uma edi√ß√£o estava duplicando o valor do mesmo com o atributo Sort
         * impossibilitando as ordena√ß√µes nas colunas da grid.
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
     * para a tela de consulta e ap√≥s isso, muda o estado para list.
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

        $scope.listAccessGroupByFilters(null, pageRequest);
    };

    /**
     * Realiza os procedimentos iniciais (prepara o estado)
     * para a tela de inser√ß√£o e ap√≥s isso, muda o estado para insert.
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
     * para a tela de edi√ß√£o e ap√≥s isso, muda o estado para update.
     * @see UPDATE_STATE
     * @see $stateChangeSuccess
     *
     * Para mudar para este estado, deve-se primeiro obter via id
     * o registro pelo servi√ßo de consulta e s√≥ ent√£o mudar o estado da tela.
     */
    $scope.changeToUpdate = function (id) {
        $log.info("changeToUpdate", id);

        accessGroupService.findAccessGroupById($state.params.id, {
            callback: function (result) {
                $scope.currentEntity = result;
                $scope.currentState = $scope.UPDATE_STATE;
                $state.go($scope.UPDATE_STATE);
                $scope.$apply();

                $scope.loadCamadasById(result.id);
                $scope.loadPesquisasById(result.id);
            },
            errorHandler: function (message, exception) {
                $scope.msg = {type: "danger", text: message, dismiss: true};
                $scope.$apply();
            }
        });
    };

    /**
     * Realiza os procedimentos iniciais (prepara o estado)
     * para a tela de detalhe e ap√≥s isso, muda o estado para detail.
     * @see DETAIL_STATE
     * @see $stateChangeSuccess
     *
     * Para mudar para este estado, deve-se primeiro obter via id
     * o registro atualizado pelo servi√ßo de consulta e s√≥ ent√£o mudar o estado da tela.
     * Caso o indentificador esteja inv√°lido, retorna para o estado de listagem.
     */
    $scope.changeToDetail = function (id) {
        $log.info("changeToDetail", id);

        if (id == null || id == "" || id == 0) {
            $scope.msg = {type: "error", text: $scope.INVALID_ID_MESSAGE, dismiss: true};
            $scope.currentState = $scope.LIST_STATE;
            $state.go($scope.LIST_STATE);
            return;
        }

        accessGroupService.findAccessGroupById(id, {
            callback: function (result) {
                $scope.currentEntity = result;
                $scope.currentState = $scope.DETAIL_STATE;
                $state.go($scope.DETAIL_STATE, {id: id});
                $scope.$apply();

                $scope.loadCamadasById(result.id);
                $scope.loadPesquisasById(result.id);
            },
            errorHandler: function (message, exception) {
                $scope.msg = {type: "danger", text: message, dismiss: true};
                $scope.$apply();
            }
        });
    };

    /**
     * Realiza os procedimentos iniciais (prepara o estado)
     * para a tela de exclus√£o.
     *
     * Antes de excluir, o usu√°rio notificado para confirma√ß√£o
     * e s√≥ ent√£o o registro √© excluido.
     * Ap√≥s exclu√≠do, atualizamos a grid com estado de filtro, pagina√ß√£o e sorting.
     */
    $scope.changeToRemove = function (grupoAcesso) {
        $log.info("changeToRemove", grupoAcesso);

        var dialog = $modal.open({
            templateUrl: "assets/libs/eits-directives/dialog/dialog-template.html",
            controller: DialogController,
            windowClass: 'dialog-delete',
            resolve: {
                title: function () {
                    return "Exclus√£o de grupo de acesso";
                },
                message: function () {
                    return 'Tem certeza que deseja excluir o grupo de acesso "' + grupoAcesso.nome + '"? <br/>Esta opera√ß√£o n√£o poder√° mais ser desfeita.';
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

//            accessGroupService.removeGrupoAcesso(grupoAcesso.id, {
//                callback: function (result) {
//                    //caso o currentPage esteja null, configura o pager default
//                    if ($scope.currentPage == null) {
//                        $scope.changeToList();
//                        //caso n√£o, usa o mesmo estado para carregar a listagem
//                    } else {
//                        $scope.listGruposAcessoByFilters($scope.data.filter, $scope.currentPage.pageable);
//                    }
//
//                    $scope.msg = {type: "success", text: 'O registro "' + grupoAcesso.nome + '" foi exclu√≠do com sucesso.', dismiss: true};
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
     * e chama o servi√ßoe de listagem, considerando o filtro corrente na tela.
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
     * Realiza a consulta de registros, consirando filtro, paginaÁ„o e sorting.
     * Quando ok, muda o estado da tela para list.
     *
     * @see data.filter
     * @see currentPage
     */
    $scope.listAccessGroupByFilters = function (filter, pageRequest) {

        accessGroupService.listAccessGroupByFilters(filter, pageRequest, {
            callback: function (result) {
                $scope.currentPage = result;
                $scope.currentPage.pageable.pageNumber++;//Para fazer o bind com o pagination
                $scope.currentState = $scope.LIST_STATE;
                $state.go($scope.LIST_STATE);
                $scope.$apply();
            },
            errorHandler: function (message, exception) {
                $scope.msg = {type: "danger", text: message, dismiss: true};
                $scope.$apply();
            }
        });
    };

    /**
     * Realiza a inserÁ„o de um novo registro
     * e no suscesso, modifica o estado da tela para o detail.
     */
    $scope.insertAccessGroup = function () {

        if (!$scope.form().$valid) {
            $scope.msg = {type: "danger", text: $scope.INVALID_FORM_MESSAGE, dismiss: true};
            return;
        }

        accessGroupService.saveAccessGroupNameDescription(null, $scope.currentEntity.name, $scope.currentEntity.description, {
            callback: function (result) {
                $scope.usuarioTab = true;
                $scope.currentEntity = result;
                $scope.msg = {type: "success", text: "Grupo de acesso inserido com sucesso!", dismiss: true};
                $scope.$apply();
            },
            errorHandler: function (message, exception) {
                $scope.msg = {type: "danger", text: message, dismiss: true};
                $scope.$apply();
            }
        });
    };

    /**
     * Realiza a atualiza de um registro
     * e no sucesso modifica o estado da tela para o estado de detalhe
     */
    $scope.updateAccessGroup = function () {

        if (!$scope.form().$valid) {
            $scope.msg = {type: "danger", text: $scope.INVALID_FORM_MESSAGE, dismiss: true};
            return;
        }

        accessGroupService.saveAccessGroupNameDescription($scope.currentEntity.id, $scope.currentEntity.name, $scope.currentEntity.description, {
            callback: function (result) {
                $scope.usuarioTab = true;
                $scope.currentEntity = result;
                $scope.msg = {type: "success", text: "Grupo de acesso atualizado com sucesso!", dismiss: true};
                $scope.$apply();
            },
            errorHandler: function (message, exception) {
                if (exception.message.indexOf("ConstraintViolationException") > -1) {
                    message = "O campo Nome ou EndereÁo informado j· existe, altere e tente novamente.";
                }
                $scope.msg = {type: "danger", text: message, dismiss: true};
                $scope.$apply();
            }
        });
    };

    /**
     * Testa conex„o se È WMS ou WFS
     */
    $scope.testaConexaoGrupoAcesso = function (grupoAcesso) {

        if (!$scope.form().endereco.$valid) {
            $scope.msg = {type: "danger", text: $scope.INVALID_FORM_MESSAGE, dismiss: true};
            return;
        }

        accessGroupService.testaConexao(grupoAcesso.endereco, grupoAcesso.tipoGrupoAcesso, {
            callback: function (result) {
                if (result) {
                    $scope.msg = {type: "success", text: "Conex„o estabelecida com Íxito.", dismiss: true};
                }
                else {
                    $scope.msg = {type: "danger", text: "N„o foi possÌvel estabelecer conex„o com o grupo de acesso geogr·ficos.", dismiss: true};
                }
                $scope.$apply();
            },
            errorHandler: function (message, exception) {
                $scope.msg = {type: "danger", text: message, dismiss: true};
                $scope.$apply();
            }
        });
    };


    /**
     * Abre uma popup para selecionar a camada a ser associada.
     */
    $scope.associarCamada = function () {
        //FunÁ„o respons·vel por chamar a popup de configuraÁıes de camada para associaÁ„o.
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
                for (var i = 0; i < result.length; i++) {
                    var index = $scope.findByIdInArray($scope.camadasSelecionadas, result[i]);
                    var index2 = $scope.findByIdInArray($scope.camadasOriginais, result[i]);
                    var index3 = $scope.findByIdInArray($scope.removerCamadas, result[i]);

                    //Identifica se marcou novos registros
                    if (index == -1 && index2 == -1) {
                        var indexAdd = $scope.findByIdInArray($scope.adicionarCamadas, result[i]);
                        if (indexAdd == -1)
                            $scope.adicionarCamadas.push(result[i]);
                    }

                    if (index3 > -1) {
                        $scope.removerCamadas.splice(index3, 1);
                    }

                }
                for (var i = 0; i < $scope.camadasSelecionadas.length; i++) {

                    var index = $scope.findByIdInArray(result, $scope.camadasSelecionadas[i]);

                    if (index == -1) {
                        var index2 = $scope.findByIdInArray($scope.adicionarCamadas, $scope.camadasSelecionadas[i]);
                        var index3 = $scope.findByIdInArray($scope.removerCamadas, $scope.camadasSelecionadas[i]);
                        var index4 = $scope.findByIdInArray($scope.camadasOriginais, $scope.camadasSelecionadas[i]);

                        if (index2 > -1){
                            var indexAdd = $scope.findByIdInArray($scope.removerCamadas, $scope.camadasSelecionadas[i]);
                            if (indexAdd > -1)
                                $scope.adicionarCamadas.splice(indexAdd, 1);
                        }
                        if (index3 == -1 && index4 > -1) {
                            $scope.removerCamadas.push($scope.camadasSelecionadas[i]);
                        }

                    }
                }
                $scope.camadasSelecionadas = result;
            }
        });
    };

    /**
     * Abre uma popup para selecionar as pesquisas personalizadas a serem associadas.
     */
    $scope.associarPesquisas = function () {
        //FunÁ„o respons·vel por chamar a popup de configuraÁıes de camada para associaÁ„o.
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
                for (var i = 0; i < result.length; i++) {
                    var index = $scope.findByIdInArray($scope.pesquisasSelecionadas, result[i]);
                    var index2 = $scope.findByIdInArray($scope.pesquisasOriginais, result[i]);
                    var index3 = $scope.findByIdInArray($scope.removerPesquisas, result[i]);

                    //Identifica se marcou novos registros
                    if (index == -1 && index2 == -1) {
                        var indexAdd = $scope.findByIdInArray($scope.adicionarPesquisas, result[i]);
                        if (indexAdd == -1)
                            $scope.adicionarPesquisas.push(result[i]);
                    }

                    if (index3 > -1) {
                        $scope.removerPesquisas.splice(index3, 1);
                    }
                }

                for (var i = 0; i < $scope.pesquisasSelecionadas.length; i++) {

                    var index = $scope.findByIdInArray(result, $scope.pesquisasSelecionadas[i]);

                    if (index == -1) {
                        var index2 = $scope.findByIdInArray($scope.adicionarPesquisas, $scope.pesquisasSelecionadas[i]);
                        var index3 = $scope.findByIdInArray($scope.removerPesquisas, $scope.pesquisasSelecionadas[i]);
                        var index4 = $scope.findByIdInArray($scope.pesquisasOriginais, $scope.pesquisasSelecionadas[i]);

                        if (index2 > -1){
                            var indexAdd = $scope.findByIdInArray($scope.removerPesquisas, $scope.pesquisasSelecionadas[i]);
                            if (indexAdd > -1)
                                $scope.adicionarPesquisas.splice(indexAdd, 1);
                        }
                        if (index3 == -1 && index4 > -1) {
                            $scope.removerPesquisas.push($scope.pesquisasSelecionadas[i]);
                        }

                    }
                }
                $scope.pesquisasSelecionadas = result;
            }
        });
    };

    /**
     * Abre uma popup para selecionar as ferramentas a serem associadas.
     */
    $scope.associarFerramentas = function () {
        //FunÁ„o respons·vel por chamar a popup de ferramentas para associaÁ„o.
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

                $scope.currentEntity.ferramentas = $scope.ferramentasSelecionadas;
            }

        });
    };

    /**
     * Abre uma popup para selecionar as ferramentas a serem associadas.
     */
    $scope.associarUsuarios = function () {
        //FunÁ„o respons·vel por chamar a popup de usu·rios para associaÁ„o.
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
        var index = $scope.findByIdInArray($scope.camadasSelecionadas, entity);
        var index2 = $scope.findByIdInArray($scope.adicionarCamadas, entity);
        var index3 = $scope.findByIdInArray($scope.camadasOriginais, entity);
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
        accessGroupService.linkCamada($scope.adicionarCamadas, $scope.currentEntity.id, {
            callback: function(result){
                $scope.msg = {type: "success", text: "AlteraÁıes efetuadas com sucesso", dismiss: true};
                $scope.adicionarCamadas = [];
                $scope.$apply();
            },
            errorHandler: function(error){
                $scope.msg = {type: "danger", text: "Erro: "+error, dismiss: true};
            }
        });
    };

    /**
     *
     * @param pesquisas
     */
    $scope.linkPesquisasPersonalizadas = function() {
        accessGroupService.linkPesquisaPersonalizada($scope.adicionarPesquisas, $scope.currentEntity.id, {
            callback: function(result){
                $scope.msg = {type: "success", text: "AlteraÁıes efetuadas com sucesso", dismiss: true};
                $scope.adicionarPesquisas = [];
                $scope.$apply();
            },
            errorHandler: function(error){
                $scope.msg = {type: "danger", text: "Erro: "+error, dismiss: true};
            }
        })
    }

    /**
     *
     * @param camada
     * @param grupoAcesso
     */
    $scope.unlinkCamadas = function() {
        accessGroupService.unlinkCamada($scope.removerCamadas, $scope.currentEntity.id, {
            callback: function(result){
                $scope.msg = {type: "success", text: "AlteraÁıes efetuadas com sucesso", dismiss: true};
                $scope.removerCamadas = [];
                $scope.$apply();
            },
            errorHandler: function(error){
                $scope.msg = {type: "danger", text: "Erro: "+error, dismiss: true};
            }
        });
    };

    /**
     *
     * @param pesquisa
     * @param grupoAcesso
     */
    $scope.unlinkPesquisaPersonalizada = function() {
        accessGroupService.unlinkPesquisaPersonalizada($scope.removerPesquisas, $scope.currentEntity.id, {
            callback: function(result){
                $scope.msg = {type: "success", text: "AlteraÁıes efetuadas com sucesso", dismiss: true};
                $scope.removerPesquisas = [];
                $scope.$apply();
            },
            errorHandler: function(error){
                $scope.msg = {type: "danger", text: "Erro: "+error, dismiss: true};
            }
        });
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
        accessGroupService.listCamadaByGrupoAcessoId(grupoId, {
            callback: function(result){
                $scope.camadasSelecionadas = result;
                $scope.camadasOriginais = result.slice(0);
                $scope.$apply();
            },
            errorHandler: function(error){
                $log.error(error);
            }
        });
    };

    /**
     *
     * @param grupoId
     */
    $scope.loadPesquisasById = function(grupoId){
        accessGroupService.listPesquisaPersonalizadaByGrupoAcessoId(grupoId, {
            callback: function(result){
                $scope.pesquisasSelecionadas = result;
                $scope.pesquisasOriginais = result.slice(0);
                $scope.$apply();
            },
            errorHandler: function(error){
                $log.error(error);
            }
        });
    };

    /**
     *
     */
    $scope.updateUsuarios = function() {
        accessGroupService.updateGrupoAcessoUsuarios($scope.currentEntity.id, $scope.currentEntity.usuarios, {
            callback: function(result){
                $scope.msg = {type: "success", text: "AlteraÁıes efetuadas com sucesso", dismiss: true};
                $scope.$apply();
            },
            errorHandler: function(error){
                $log.error(error);
            }
        });
    }

    /**
     *
     */
    $scope.updateFerramentas = function() {
        accessGroupService.updateGrupoAcessoFerramentas($scope.currentEntity.id, $scope.currentEntity.ferramentas, {
            callback: function(result){
                $scope.msg = {type: "success", text: "AlteraÁıes efetuadas com sucesso", dismiss: true};
                $scope.$apply();
            },
            errorHandler: function(error){
                $log.error(error);
            }
        });
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