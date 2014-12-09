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

    $scope.gridLayers = {
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
        data: 'selectedSearchs',
        multiSelect: false,
        useExternalSorting: false,
        headerRowHeight: 45,
        rowHeight: 45,
        beforeSelectionChange: function (row, event) {
        },
        columnDefs: [
			{displayName: 'Symbology', field: 'subtitle', sortable: false, width: '10%', cellTemplate: IMAGE_SUBTITLE_SEARCH},
			{displayName: 'Name', field: 'name', width: '20%'},
			{displayName: 'Layer\'s Title', field: 'layer.title', width: '20%'},
			{displayName: 'Layer\'s Name', field: 'layer.name', width: '20%'},
			{displayName: 'Data source', field: 'layer.dataSource.name', width: '20%'},
			{displayName: 'Actions', sortable: false, cellTemplate: GRID_ACTION_SEARCH_BUTTONS, width: '10%'}
        ]
    };  

    $scope.gridTools = {
            data: 'currentEntity.tools',
            multiSelect: false,
            useExternalSorting: false,
            enableSorting: true,
            headerRowHeight: 45,
            rowHeight: 45,
            beforeSelectionChange: function (row, event) {
            },
            columnDefs: [
                {displayName: 'DescriÁ„o', field: 'description'},
                {displayName: 'Nome', field: 'name', width: '55%'},
                {displayName: 'AÁıes', sortable: false, cellTemplate: GRID_ACTION_TOOLS_BUTTONS, width: '100px'}
            ]
        };
    
    $scope.gridUsers = {
        data: 'currentEntity.users',
        multiSelect: false,
        useExternalSorting: false,
        headerRowHeight: 45,
        rowHeight: 45,
        columnDefs: [
            {displayName: 'Full name', field: 'name', width: '35%'},
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
    $scope.data = { filter: null, showFields: true, accessGroupType: 'WMS' };
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

        $scope.removeLayers= [];
        $scope.addLayers = [];
        $scope.selectedLayers = [];
        $scope.originalLayers = [];
        $scope.removeSearchs = [];
        $scope.addSearchs = [];
        $scope.selectedSearchs = [];
        $scope.originalSearchs = [];

        //Para funcionar com o ng-model no radio button
        $scope.currentEntity.accessGroupType = 'WMS';

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

                $scope.loadLayersById(result.id);
                $scope.loadSearchsById(result.id);
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

                $scope.loadLayersById(result.id);
                $scope.loadSearchsById(result.id);
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
    $scope.changeToRemove = function (accessGroup) {
        $log.info("changeToRemove", accessGroup);

        var dialog = $modal.open({
            templateUrl: "static/libs/eits-directives/dialog/dialog-template.html",
            controller: DialogController,
            windowClass: 'dialog-delete',
            resolve: {
                title: function () {
                    return "Exclus√£o de grupo de acesso";
                },
                message: function () {
                    return 'Tem certeza que deseja excluir o grupo de acesso "' + accessGroup.name + '"? <br/>Esta opera√ß√£o n√£o poder√° mais ser desfeita.';
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

            accessGroupService.removeAccessGroup(accessGroup.id, {
                callback: function (result) {
                    //caso o currentPage esteja null, configura o pager default
                    if ($scope.currentPage == null) {
                        $scope.changeToList();
                        //caso n√£o, usa o mesmo estado para carregar a listagem
                    } else {
                        $scope.listAccessGroupByFilters($scope.data.filter, $scope.currentPage.pageable);
                    }

                    $scope.msg = {type: "success", text: 'O registro "' + accessGroup.name + '" foi exclu√≠do com sucesso.', dismiss: true};
                },
                errorHandler: function (message, exception) {
                    $scope.msg = {type: "danger", text: message, dismiss: true};
                    $scope.$apply();
                }
            });
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
        $scope.listAccessGroupByFilters(filter, $scope.currentPage.pageable);
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
    $scope.testAccessGroupConnection = function (accessGroup) {

        if (!$scope.form().endereco.$valid) {
            $scope.msg = {type: "danger", text: $scope.INVALID_FORM_MESSAGE, dismiss: true};
            return;
        }

        accessGroupService.testaConexao(accessGroup.url, accessGroup.accessGroupType, {
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
    $scope.associateLayer = function () {
        //FunÁ„o respons·vel por chamar a popup de configuraÁıes de camada para associaÁ„o.
        var dialog = $modal.open({
            templateUrl: 'modules/admin/ui/custom-search/popup/layer-config-popup.jsp',
            controller: SelectConfigLayerAccessGroupPopUpController,
            windowClass: 'xx-dialog',
            resolve: {
                selectedLayers: function () {
                    return $scope.selectedLayers;
                }
            }
        });

        dialog.result.then(function (result) {
            if (result) {
                for (var i = 0; i < result.length; i++) {
                    var index = $scope.findByIdInArray($scope.selectedLayers, result[i]);
                    var index2 = $scope.findByIdInArray($scope.originalLayers, result[i]);
                    var index3 = $scope.findByIdInArray($scope.removeLayers, result[i]);

                    //Identifica se marcou novos registros
                    if (index == -1 && index2 == -1) {
                        var indexAdd = $scope.findByIdInArray($scope.addLayers, result[i]);
                        if (indexAdd == -1)
                            $scope.addLayers.push(result[i]);
                    }

                    if (index3 > -1) {
                        $scope.removeLayer.splice(index3, 1);
                    }

                }
                for (var i = 0; i < $scope.selectedLayers.length; i++) {

                    var index = $scope.findByIdInArray(result, $scope.selectedLayers[i]);

                    if (index == -1) {
                        var index2 = $scope.findByIdInArray($scope.addLayers, $scope.selectedLayers[i]);
                        var index3 = $scope.findByIdInArray($scope.removeLayers, $scope.selectedLayers[i]);
                        var index4 = $scope.findByIdInArray($scope.originalLayers, $scope.selectedLayers[i]);

                        if (index2 > -1){
                            var indexAdd = $scope.findByIdInArray($scope.removeLayers, $scope.selectedLayers[i]);
                            if (indexAdd > -1)
                                $scope.addLayers.splice(indexAdd, 1);
                        }
                        if (index3 == -1 && index4 > -1) {
                            $scope.removeLayers.push($scope.selectedLayers[i]);
                        }

                    }
                }
                $scope.selectedLayers = result;
            }
        });
    };

    /**
     * Abre uma popup para selecionar as pesquisas personalizadas a serem associadas.
     */
    $scope.associateSearch = function () {
        //FunÁ„o respons·vel por chamar a popup de configuraÁıes de camada para associaÁ„o.
        var dialog = $modal.open({
            templateUrl: 'modules/admin/ui/access-group/popup/custom-search-popup.jsp',
            controller: SelectCustomSearchPopUpController,
            windowClass: 'xx-dialog',
            resolve: {
                selectedSearchs: function () {
                    return $scope.selectedSearchs;
                }
            }
        });

        dialog.result.then(function (result) {
            if (result) {
                for (var i = 0; i < result.length; i++) {
                    var index = $scope.findByIdInArray($scope.selectedSearchs, result[i]);
                    var index2 = $scope.findByIdInArray($scope.originalSearchs, result[i]);
                    var index3 = $scope.findByIdInArray($scope.removeSearchs, result[i]);

                    //Identifica se marcou novos registros
                    if (index == -1 && index2 == -1) {
                        var indexAdd = $scope.findByIdInArray($scope.addSearchs, result[i]);
                        if (indexAdd == -1)
                            $scope.addSearchs.push(result[i]);
                    }

                    if (index3 > -1) {
                        $scope.removeSearchs.splice(index3, 1);
                    }
                }

                for (var i = 0; i < $scope.selectedSearchs.length; i++) {

                    var index = $scope.findByIdInArray(result, $scope.selectedSearchs[i]);

                    if (index == -1) {
                        var index2 = $scope.findByIdInArray($scope.addSearchs, $scope.selectedSearchs[i]);
                        var index3 = $scope.findByIdInArray($scope.removeSearchs, $scope.selectedSearchs[i]);
                        var index4 = $scope.findByIdInArray($scope.originalSearchs, $scope.selectedSearchs[i]);

                        if (index2 > -1){
                            var indexAdd = $scope.findByIdInArray($scope.removeSearchs, $scope.selectedSearchs[i]);
                            if (indexAdd > -1)
                                $scope.addSearchs.splice(indexAdd, 1);
                        }
                        if (index3 == -1 && index4 > -1) {
                            $scope.removeSearchs.push($scope.selectedSearchs[i]);
                        }

                    }
                }
                $scope.selectedSearchs = result;
            }
        });
    };

    /**
     * Abre uma popup para selecionar as ferramentas a serem associadas.
     */
    $scope.associateTools = function () {
        //FunÁ„o respons·vel por chamar a popup de ferramentas para associaÁ„o.
        var dialog = $modal.open({
            templateUrl: 'modules/admin/ui/access-group/popup/tools-popup.html',
            controller: SelectToolsPopUpController,
            windowClass: 'xx-dialog',
            resolve: {
                selectedTools: function () {
                    return $scope.currentEntity.tools;
                }
            }
        });

        dialog.result.then(function (result) {

            if (result != null) {

                $scope.selectedTools = result;

                for (var i = 0; i < $scope.selectedTools.length; i++) {
                    $scope.selectedTools[i].orderLayer = i;
                }

                $scope.currentEntity.tools = $scope.selectedTools;
            }

        });
    };

    /**
     * Abre uma popup para selecionar as ferramentas a serem associadas.
     */
    $scope.associateUsers = function () {
        //FunÁ„o respons·vel por chamar a popup de usu·rios para associaÁ„o.
        var dialog = $modal.open({
            templateUrl: 'modules/admin/ui/access-group/popup/users-popup.jsp',
            controller: SelectUsersPopUpController,
            windowClass: 'xx-dialog',
            resolve: {
                usersSelected: function () {
                    return $scope.currentEntity.users;
                }
            }
        });

        dialog.result.then(function (result) {

            if (result != null) {
                $scope.currentEntity.users.push(result);
            }
        });
    };

    /**
     * Clear fields
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
    $scope.removeLayer = function (entity) {
        var index = $scope.findByIdInArray($scope.selectedLayers, entity);
        var index2 = $scope.findByIdInArray($scope.addLayers, entity);
        var index3 = $scope.findByIdInArray($scope.originalLayers, entity);
        if (index > -1) {
            $scope.selectedLayers.splice(index, 1);
        }
        if (index2 > -1) {
            $scope.addLayers.splice(index2, 1);
        }
        if (index3 > -1) {
            $scope.removeLayers.push(entity);
        }
    };

    /**
     *
     * @param entity
     */
    $scope.removeSearch = function (entity) {
        var index = $scope.selectedSearchs.indexOf(entity);
        var index2 = $scope.addSearchs.indexOf(entity);
        var index3 = $scope.originalSearchs.indexOf(entity);
        if (index > -1) {
            $scope.selectedSearchs.splice(index, 1);
        }
        if (index2 > -1) {
            $scope.addSearchs.splice(index2, 1);
        }
        if (index3 > -1) {
            $scope.removeSearchs.push(entity);
        }
    };

    /**
     *
     * @param entity
     */
    $scope.removeTool = function (entity) {
        var index = $scope.currentEntity.tools.indexOf(entity);
        if (index > -1) {
            $scope.currentEntity.tools.splice(index, 1);
        };
    };

    /**
     *
     * @param entity
     */
    $scope.removeUser = function (entity) {
        var index = $scope.currentEntity.users.indexOf(entity);
        if (index > -1) {
            $scope.currentEntity.users.splice(index, 1);
        };
    };

    /**
     *
     * @param camadas
     */
    $scope.linkLayers = function() {
        accessGroupService.linkLayer($scope.addLayers, $scope.currentEntity.id, {
            callback: function(result){
                $scope.msg = {type: "success", text: "AlteraÁıes efetuadas com sucesso", dismiss: true};
                $scope.addLayers = [];
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
    $scope.linkCustomSearch = function() {
        accessGroupService.linkCustomSearch($scope.addSearchs, $scope.currentEntity.id, {
            callback: function(result){
                $scope.msg = {type: "success", text: "AlteraÁıes efetuadas com sucesso", dismiss: true};
                $scope.addSearchs = [];
                $scope.$apply();
            },
            errorHandler: function(error){
                $scope.msg = {type: "danger", text: "Erro: "+error, dismiss: true};
            }
        })
    }

    /**
	*
	*/
    $scope.unlinkLayers = function() {
        accessGroupService.unlinkLayer($scope.removeLayers, $scope.currentEntity.id, {
            callback: function(result){
                $scope.msg = {type: "success", text: "AlteraÁıes efetuadas com sucesso", dismiss: true};
                $scope.removeLayers = [];
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
    $scope.unlinkCustomSearch = function() {
        accessGroupService.unlinkCustomSearch($scope.removerPesquisas, $scope.currentEntity.id, {
            callback: function(result){
                $scope.msg = {type: "success", text: "AlteraÁıes efetuadas com sucesso", dismiss: true};
                $scope.removeSearchs = [];
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
    $scope.saveLayers = function () {
        if ($scope.addLayers.length > 0) {
            $scope.linkLayers();
        }
        if ($scope.removeLayers.length > 0) {
            $scope.unlinkLayers();
        }
    }

    /**
     *
     */
    $scope.saveSearchs = function () {
        if ($scope.addSearchs.length > 0) {
            $scope.linkCustomSearch();
        }
        if ($scope.removeSearchs.length > 0) {
            $scope.unlinkCustomSearch();
        }
    }

    /**
     *
     * @param grupoId
     */
    $scope.loadLayersById = function(grupoId){
        accessGroupService.listLayerByAccessGroupId(grupoId, {
            callback: function(result){
                $scope.selectedLayers = result;
                $scope.originalLayers = result.slice(0);
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
    $scope.loadSearchsById = function(grupoId){
        accessGroupService.listCustomSearchByAccessGroupId(grupoId, {
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
    $scope.updateUsers = function() {
        accessGroupService.updateAccessGroupUsers($scope.currentEntity.id, $scope.currentEntity.users, {
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
    $scope.updateTools = function() {
        accessGroupService.updateAccessGroupTools($scope.currentEntity.id, $scope.currentEntity.tools, {
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
    $scope.saveAssotiations= function(){
        $scope.saveLayers();
        $scope.saveSearchs();
        $scope.updateTools();
        $scope.updateUsers();
    }

};