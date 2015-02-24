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
    
    $importService("accessGroupService");

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

            $scope.listAccessGroupByFilters($scope.data.filter, $scope.currentPage.pageable);
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
    $scope.UPDATE_STATE = "access-group.update";
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
    * @type {Array}
    */
   $scope.removeUsers = [];

   /**
    *
    * @type {Array}
    */
   $scope.addUsers = [];

   /**
    *
    * @type {Array}
    */
   $scope.selectedUsers = [];

   /**
    *
    * @type {Array}
    */
   $scope.originalUsers = [];

    /**
     *
     * @type {boolean}
     */
    $scope.userTab = false;

    //DATA GRID
    /**
     * Variável estática coms os botões de ações da grid
     * O botão de editar navega via URL (sref) por que a edição é feita em outra página,
     * já o botão de excluir chama um método direto via ng-click por que não tem um estado da tela específico.
     */
    var GRID_ACTION_BUTTONS = '<div class="cell-centered">' +
        '<a ui-sref="access-group.update({id:row.entity.id})" title="'+$translate('admin.access-group.Update')+'" class="btn btn-mini"><i class="itaipu-icon-edit"></i></a>' +
        '<a ng-if="row.entity.id != 1" ng-click="changeToRemove(row.entity)" title="'+$translate('admin.access-group.Delete')+'" class="btn btn-mini"><i class="itaipu-icon-delete"></i></a>' +
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
            {displayName: $translate('admin.access-group.Name'), field: 'name'},
            {displayName: $translate('admin.access-group.Description'), field: 'description', width: '55%'},
            {displayName: $translate('admin.access-group.Actions'), sortable: false, cellTemplate: GRID_ACTION_BUTTONS, width: '100px'}
        ]
    };

    var IMAGE_SUBTITLE = '<div align="center" class="ngCellText" ng-cell-text ng-class="col.colIndex()">' +
        '<img ng-if="row.entity.dataSource.url" style="width: 20px; height: 20px; border: solid 1px #c9c9c9;" ng-src="{{row.entity.legend}}"/>' +
    	'<img ng-if="!row.entity.dataSource.url" style="width: 20px; height: 20px; border: solid 1px #c9c9c9;" ng-src="{{row.entity.icon}}"/>' +
        '</div>';
    
    var IMAGE_SUBTITLE_SEARCH = '<div align="center" class="ngCellText" ng-cell-text ng-class="col.colIndex()">' +
    '<img ng-if="row.entity.layer.dataSource.url" style="width: 20px; height: 20px; border: solid 1px #c9c9c9;" ng-src="{{row.entity.layer.legend}}"/>' +
	'<img ng-if="!row.entity.layer.dataSource.url" style="width: 20px; height: 20px; border: solid 1px #c9c9c9;" ng-src="{{row.entity.layer.icon}}"/>' +
    '</div>';

    var GRID_ACTION_LAYER_BUTTONS = '<div class="cell-centered">' +
        '<a ng-click="removeLayer(row.entity)" ng-if="currentState != DETAIL_STATE" title="'+$translate('admin.access-group.Delete')+'" class="btn btn-mini"><i class="itaipu-icon-delete"></i></a>' +
        '</div>';

    var GRID_ACTION_SEARCH_BUTTONS = '<div class="cell-centered">' +
        '<a ng-click="removeSearch(row.entity)" ng-if="currentState != DETAIL_STATE" title="'+$translate('admin.access-group.Delete')+'" class="btn btn-mini"><i class="itaipu-icon-delete"></i></a>' +
        '</div>';
    
    var GRID_ACTION_TOOLS_BUTTONS = '<div class="cell-centered">' +
    '<a ng-click="removeTool(row.entity)" ng-if="currentState != DETAIL_STATE" title="'+$translate('admin.access-group.Delete')+'" class="btn btn-mini"><i class="itaipu-icon-delete"></i></a>' +
    '</div>';

    var GRID_ACTION_USER_BUTTONS = '<div class="cell-centered">' +
        '<a ng-click="removeUser(row.entity)" ng-if="currentState != DETAIL_STATE" title="'+$translate('admin.access-group.Delete')+'" class="btn btn-mini"><i class="itaipu-icon-delete"></i></a>' +
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
            {displayName: $translate('admin.access-group.Symbology'), field: 'subtitle', sortable: false, width: '120px', cellTemplate: IMAGE_SUBTITLE},
            {displayName: $translate('admin.access-group.Title'), field: 'title'},
            {displayName: $translate('admin.access-group.Name'), field: 'name', width: '40%'},
            {displayName: $translate('admin.access-group.Data-source'), field: 'dataSource.name'},
            {displayName: $translate('admin.access-group.Layer-group'), field: 'layerGroup.name'},
            {displayName: $translate('admin.access-group.Actions'), sortable: false, cellTemplate: GRID_ACTION_LAYER_BUTTONS, width: '100px'}
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
			{displayName: $translate('admin.access-group.Symbology'), field: 'subtitle', sortable: false, width: '10%', cellTemplate: IMAGE_SUBTITLE_SEARCH},
			{displayName: $translate('admin.access-group.Name'), field: 'name', width: '20%'},
			{displayName: $translate('admin.access-group.Layer-title'), field: 'layer.title', width: '20%'},
			{displayName: $translate('admin.access-group.Layer-name'), field: 'layer.name', width: '20%'},
			{displayName: $translate('admin.access-group.Data-source'), field: 'layer.dataSource.name', width: '20%'},
			{displayName: $translate('admin.access-group.Actions'), sortable: false, cellTemplate: GRID_ACTION_SEARCH_BUTTONS, width: '10%'}
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
                {displayName: $translate('admin.access-group.Description'), field: 'description'},
                {displayName: $translate('admin.access-group.Name'), field: 'name', width: '55%'},
                {displayName: $translate('admin.access-group.Actions'), sortable: false, cellTemplate: GRID_ACTION_TOOLS_BUTTONS, width: '100px'}
            ]
        };
    
    $scope.gridUsers = {
        data: 'selectedUsers',
        multiSelect: false,
        useExternalSorting: false,
        headerRowHeight: 45,
        rowHeight: 45,
        columnDefs: [
            {displayName: $translate('admin.access-group.Full-name'), field: 'name', width: '35%'},
            {displayName: $translate('admin.access-group.User-name'), field: 'username'},
            {displayName: $translate('admin.access-group.Actions'), sortable: false, cellTemplate: GRID_ACTION_USER_BUTTONS, width: '100px'}
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
    $scope.data = { filter: null, showFields: true, accessGroupType: 'WMS' };
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

        $scope.listAccessGroupByFilters(null, pageRequest);
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

        $scope.removeLayers= [];
        $scope.addLayers = [];
        $scope.selectedLayers = [];
        $scope.originalLayers = [];
        $scope.removeSearchs = [];
        $scope.addSearchs = [];
        $scope.selectedSearchs = [];
        $scope.originalSearchs = [];
        $scope.removeUsers = [];
        $scope.addUsers = [];
        $scope.selectedUsers = [];
        $scope.originalUsers = [];

        //Para funcionar com o ng-model no radio button
        $scope.currentEntity.accessGroupType = 'WMS';

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

        accessGroupService.findAccessGroupById($state.params.id, {
            callback: function (result) {
            	$scope.selectedUsers = result.users;
                $scope.currentEntity =  result;
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
     * para a tela de exclusão.
     *
     * Antes de excluir, o usuário notificado para confirmação
     * e só então o registro é excluido.
     * Após excluído, atualizamos a grid com estado de filtro, paginação e sorting.
     */
    $scope.changeToRemove = function (accessGroup) {
        $log.info("changeToRemove", accessGroup);

        var dialog = $modal.open({
            templateUrl: "static/libs/eits-directives/dialog/dialog-template.html",
            controller: DialogController,
            windowClass: 'dialog-delete',
            resolve: {
                title: function () {
                    return $translate("admin.access-group.Access-group-exclusion");
                },
                message: function () {
                    return $translate("admin.access-group.Are-you-sure-you-want-to-delete-the-access-group")  + ' "'+ accessGroup.name + '" ' + '? <br/>' + $translate("This-operation-can-not-be-undone");
                },
                buttons: function () {
                    return [
                        {label: $translate("admin.access-group.Delete"), css: 'btn btn-danger'},
                        {label: $translate("admin.access-group.Cancel"), css: 'btn btn-default', dismiss: true}
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
                        //caso não, usa o mesmo estado para carregar a listagem
                    } else {
                        $scope.listAccessGroupByFilters($scope.data.filter, $scope.currentPage.pageable);
                    }

                    $scope.msg = {type: "success", text:  $translate("admin.access-group.The-register") + ' "'+ accessGroup.name + '" ' + $translate("admin.access-group.Was-successfully-deleted") + '.', dismiss: true};
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
     * e chama o serviçoe de listagem, considerando o filtro corrente na tela.
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
     * Realiza a consulta de registros, consirando filtro, pagina��o e sorting.
     * Quando ok, muda o estado da tela para list.
     *
     * @see data.filter
     * @see currentPage
     */
    $scope.listAccessGroupByFilters = function (filter, pageRequest) {

        accessGroupService.listAccessGroupByFilters(filter, pageRequest, {
            callback: function (result) {
                $scope.currentPage = $scope.selectedUsers = result;
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
     * Realiza a inser��o de um novo registro
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
                $scope.msg = {type: "success", text: $translate("admin.access-group.Access-group-successfully-inserted") + ' !', dismiss: true};
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
                $scope.msg = {type: "success", text: $translate("admin.access-group.Access-group-successfully-updated") + ' !', dismiss: true};
                $scope.$apply();
            },
            errorHandler: function (message, exception) {
                if (exception.message.indexOf("ConstraintViolationException") > -1) {
                    message = $translate("admin.access-group.Informed-name-or-address-field-already-exists-change-and-try-again") + '.';
                }
                $scope.msg = {type: "danger", text: message, dismiss: true};
                $scope.$apply();
            }
        });
    };

    /**
     * Testa conex�o se � WMS ou WFS
     */
    $scope.testAccessGroupConnection = function (accessGroup) {

        if (!$scope.form().endereco.$valid) {
            $scope.msg = {type: "danger", text: $scope.INVALID_FORM_MESSAGE, dismiss: true};
            return;
        }

        accessGroupService.testaConexao(accessGroup.url, accessGroup.accessGroupType, {
            callback: function (result) {
                if (result) {
                    $scope.msg = {type: "success", text: $translate('admin.access-group.Connection-established-with-success'), dismiss: true};
                }
                else {
                    $scope.msg = {type: "danger", text: $translate('admin.access-group.Could-not-connect-to-the-geographical-access-group') , dismiss: true};
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
        //Fun��o respons�vel por chamar a popup de configura��es de camada para associa��o.
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
        //Fun��o respons�vel por chamar a popup de configura��es de camada para associa��o.
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
        //Fun��o respons�vel por chamar a popup de ferramentas para associa��o.
        var dialog = $modal.open({
            templateUrl: 'modules/admin/ui/access-group/popup/tools-popup.jsp',
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
        //Fun��o respons�vel por chamar a popup de usu�rios para associa��o.
        var dialog = $modal.open({
            templateUrl: 'modules/admin/ui/access-group/popup/users-popup.jsp',
            controller: SelectUsersPopUpController,
            windowClass: 'xx-dialog',
            resolve: {
                usersSelected: function () {
                    return $scope.selectedUsers;
                }
            }
        });

        dialog.result.then(function (result) {
        	
            if (result) {
                for (var i = 0; i < result.length; i++) {
                    var index = $scope.findByIdInArray($scope.selectedUsers, result[i]);
                    var index2 = $scope.findByIdInArray($scope.originalUsers, result[i]);
                    var index3 = $scope.findByIdInArray($scope.removeUsers, result[i]);

                    //Identifica se marcou novos registros
                    if (index == -1 && index2 == -1) {
                        var indexAdd = $scope.findByIdInArray($scope.addUsers, result[i]);
                        if (indexAdd == -1)
                            $scope.addUsers.push(result[i]);
                    }

                    if (index3 > -1) {
                        $scope.removeUsers.splice(index3, 1);
                    }
                }

                for (var i = 0; i < $scope.selectedUsers.length; i++) {

                    var index = $scope.findByIdInArray(result, $scope.selectedUsers[i]);

                    if (index == -1) {
                        var index2 = $scope.findByIdInArray($scope.addUsers, $scope.selectedUsers[i]);
                        var index3 = $scope.findByIdInArray($scope.removeUsers, $scope.selectedUsers[i]);
                        var index4 = $scope.findByIdInArray($scope.originalUsers, $scope.selectedUsers[i]);

                        if (index2 > -1){
                            var indexAdd = $scope.findByIdInArray($scope.removeUsers, $scope.selectedUsers[i]);
                            if (indexAdd > -1)
                                $scope.addUsers.splice(indexAdd, 1);
                        }
                        if (index3 == -1 && index4 > -1) {
                            $scope.removeUsers.push($scope.selectedUsers[i]);
                        }

                    }
                }
                
                $scope.selectedUsers = $scope.currentEntity.users = result;
                
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
   $scope.removeUser = function (entity) {
       var index = $scope.selectedUsers.indexOf(entity);
       var index2 = $scope.addUsers.indexOf(entity);
       var index3 = $scope.originalUsers.indexOf(entity);
       if (index > -1) {
           $scope.selectedUsers.splice(index, 1);
       }
       if (index2 > -1) {
           $scope.addUsers.splice(index2, 1);
       }
       if (index3 > -1) {
           $scope.removeUsers.push(entity);
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
                $scope.msg = {type: "success", text: $translate('admin.access-group.update-has-been-completed-successfully'), dismiss: true};
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
                $scope.msg = {type: "success", text: $translate('admin.access-group.update-has-been-completed-successfully'), dismiss: true};
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
                $scope.msg = {type: "success", text: $translate('admin.access-group.update-has-been-completed-successfully'), dismiss: true};
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
        accessGroupService.unlinkCustomSearch($scope.removeSearchs, $scope.currentEntity.id, {
            callback: function(result){
                $scope.msg = {type: "success", text: $translate('admin.access-group.update-has-been-completed-successfully'), dismiss: true};
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
                $scope.selectedSearchs = result;
                $scope.originalSearchs = result.slice(0);
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
                $scope.msg = {type: "success", text: $translate('admin.access-group.update-has-been-completed-successfully'), dismiss: true};
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
                $scope.msg = {type: "success", text: $translate('admin.access-group.update-has-been-completed-successfully'), dismiss: true};
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