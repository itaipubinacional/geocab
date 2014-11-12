﻿'use strict';

/**
 *
 * @param $scope
 * @param $log
 * @param $location
 */
function LayerConfigController($scope, $injector, $log, $state, $timeout, $modal, $location, $importService, $translate) {
    /**
     * Inject the methods, attributes and its states inherited from AbstractCRUDController.
     * @see AbstractCRUDController
     */
    $injector.invoke(AbstractCRUDController, this, {$scope: $scope});

    $importService("layerGroupService");
    
    /*-------------------------------------------------------------------
     * 		 				 	EVENT HANDLERS
     *-------------------------------------------------------------------*/

    /**
     * Handler that listen every time that an user do the sorting at ng-grid.
     * When the event is displayed, it is set the pager of spring-data.
     * And so it call the query again, considering too the filter state (@see $scope.data.filter)
     */
    $scope.$on('ngGridEventSorted', function (event, sort) {

        if(event.targetScope.gridId != $scope.gridOptions.gridId)
        {
            return;
        }

        if (!angular.equals(sort, $scope.gridOptions.sortInfo)) {
            $scope.gridOptions.sortInfo = angular.copy(sort);

            var order = new Order();
            order.direction = sort.directions[0].toUpperCase();
            order.property = sort.fields[0];

            $scope.currentPage.pageable.sort = new Sort();
            $scope.currentPage.pageable.sort.orders = [ order ];

            $scope.listLayersByFilters($scope.data.filter, $scope.currentPage.pageable);
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
    $scope.LIST_STATE = "layer-config.list";
    /**
     * Variável estática que representa
     * o estado de detalhe de um registro.
     */
    $scope.DETAIL_STATE = "layer-config.detail";
    /**
     * Variável estática que representa
     * o estado para a criação de registros.
     */
    $scope.INSERT_STATE = "layer-config.create";
    /**
     * Variável estática que representa
     * o estado para a edição de registros.
     */
    $scope.UPDATE_STATE = "layer-config.update";
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
    $scope.removeGroups = [];

    /**
     *
     * @type {Array}
     */
    $scope.addGroups = [];

    /**
     *
     * @type {Array}
     */
    $scope.selectedGroups = [];

    /**
     *
     * @type {Array}
     */
    $scope.originalGroups = [];
    
    /**
     * @type {Array}
     * */
    $scope.attributes = [];

    //DATA GRID
    /**
     * Variável estática coms os botões de ações da grid
     * O botão de editar navega via URL (sref) por que a edição é feita em outra página,
     * já o botão de excluir chama um método direto via ng-click por que não tem um estado da tela específico.
     */
    var GRID_ACTION_BUTTONS = '<div class="cell-centered button-action">' +
        '<a ui-sref="layer-config.update({id:row.entity.id})"  " title="'+ $translate("admin.layer-config.Update") +'" class="btn btn-mini"><i class="itaipu-icon-edit"></i></a>' +
        '<a ng-click="changeToRemove(row.entity)" title="'+ $translate("admin.layer-config.Delete") +'" class="btn btn-mini"><i class="itaipu-icon-delete"></i></a>' +
        '</div>';
    
    var LAYER_TYPE_NAME = '<div class="ngCellText ng-scope col4 colt4">' +
    '<span ng-if="!row.entity.dataSource.url" ng-cell-text="" class="ng-binding">Camada interna</span>' +
    '<span ng-if="row.entity.dataSource.url" ng-cell-text="" class="ng-binding">{{ row.entity.name }}</span>' +
    '</div>';

    
    var MARKER_BUTTONS = '<div  class="cell-centered">' +
    '<a ng-if="!row.entity.dataSource.url && row.entity.enabled == false" class="btn btn-mini"><i style="font-size: 16px; color: red" class="glyphicon glyphicon-ban-circle"></i></a>'+
    '<a ng-if="!row.entity.dataSource.url && row.entity.enabled == true" class="btn btn-mini"><i style="font-size: 16px; color: green" class="glyphicon glyphicon-ok"></i></a>'+
    '<a ng-if="row.entity.dataSource.url" class="btn btn-mini"><i style="font-size: 16px; color: blue" class="glyphicon glyphicon glyphicon-minus"></i></a>'+
    '</div>';
    
    var IMAGE_LEGEND = '<div class="ngCellText" ng-cell-text ng-class="col.colIndex()">' +
	'<img ng-if="row.entity.dataSource.url" style="width: 20px; height: 20px; border: solid 1px #c9c9c9;" ng-src="{{row.entity.legend}}"/>' +
	'<img ng-if="!row.entity.dataSource.url" style="width: 20px; height: 20px; border: solid 1px #c9c9c9;" ng-src="{{row.entity.icon}}"/>' +
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
            {displayName: 'Postagem', sortable: false, cellTemplate: MARKER_BUTTONS, width: '6%'},
            {displayName: $translate('admin.layer-config.Symbology'), field:'legend', sortable:false, width: '6%', cellTemplate: IMAGE_LEGEND},
            {displayName: $translate('Title'), field: 'title', width: '19%'},
            //{displayName: $translate('Layer'), field: 'name', width: '19%'},
            {displayName: $translate('Layer'), cellTemplate: LAYER_TYPE_NAME, width: '19%'},
            {displayName: $translate('admin.datasource.Data-Source'), field: 'dataSource.name', width: '30%'},
            {displayName: $translate('admin.layer-config.Layer-group'), field: 'layerGroup.name', width: '13%'},
            {displayName: $translate('Actions'), sortable: false, cellTemplate: GRID_ACTION_BUTTONS, width: '7%'}
        ]
    };

    var GRID_ACTION_ACESSO_BUTTONS = '<div class="cell-centered">' +
        '<a ng-click="removeGrupoAcesso(row.entity)" ng-if="currentState != DETAIL_STATE" title="Excluir" class="btn btn-mini"><i class="itaipu-icon-delete"></i></a>' +
        '</div>';

    /**
     * Configurações gerais da ng-grid.
     * @see https://github.com/angular-ui/ng-grid/wiki/Configuration-Options
     */
    $scope.gridAcessoOptions = {
        data: 'selectedGroups',
        useExternalSorting: false,
        multiSelect: false,
        headerRowHeight: 45,
        rowHeight: 45,
        beforeSelectionChange: function (row, event) {
            //evita chamar a selecao, quando clicado em um action button.
        	if ( $(event.target).is("a") || $(event.target).is("i") ) return false;
				$state.go($scope.DETAIL_STATE, {id:row.entity.id});
        },
        columnDefs: [
            {displayName: $translate('Name'), field: 'name'},
            {displayName: $translate('Description'), field: 'description'},
            {displayName: '', sortable: false, cellTemplate: GRID_ACTION_ACESSO_BUTTONS, width: '100px'}
        ]
    };
    
    var GRID_ACTION_ATTRIBUTES_BUTTONS = '<div class="cell-centered">' +
    '<a ng-if="!row.entity.attributeDefault" ng-click="updateAttribute(row.entity)" ng-if="currentState != DETAIL_STATE" title="Update" class="btn btn-mini"><i class="itaipu-icon-edit"></i></a>' +
    '<a ng-if="!row.entity.attributeDefault" ng-click="removeAttribute(row.entity)" ng-if="currentState != DETAIL_STATE" title="Excluir" class="btn btn-mini"><i class="itaipu-icon-delete"></i></a>' +
    '</div>';
    
    var TYPE_COLUMN = '<div class="ngCellText ng-scope col2 colt2">' +
    '<span ng-cell-text="" class="ng-binding" ng-if="row.entity.type == \'DATE\'" >'+ $translate("admin.layer-config.DATE") +'</span>' +
    '<span ng-cell-text="" class="ng-binding" ng-if="row.entity.type == \'BOOLEAN\'" >'+ $translate("admin.layer-config.BOOLEAN") +'</span>' +
    '<span ng-cell-text="" class="ng-binding" ng-if="row.entity.type == \'TEXT\'" >'+ $translate("admin.layer-config.TEXT") +'</span>' +
    '<span ng-cell-text="" class="ng-binding" ng-if="row.entity.type == \'NUMBER\'" >'+ $translate("admin.layer-config.NUMBER") +'</span>' +
    '</div>';
    
    var REQUIRED_COLUMN = '<div class="ngCellText ng-scope col2 colt2">' +
    '<span ng-cell-text="" class="ng-binding" ng-if="row.entity.required == false" >'+ $translate("admin.layer-config.false") +'</span>' +
    '<span ng-cell-text="" class="ng-binding" ng-if="row.entity.required == true" >'+ $translate("admin.layer-config.true") +'</span>' +
    '</div>';
    
    /**
     * Configurações gerais da ng-grid.
     * @see https://github.com/angular-ui/ng-grid/wiki/Configuration-Options
     */
    $scope.gridAttributes = {
        data: 'attributes',
        useExternalSorting: false,
        multiSelect: false,
        headerRowHeight: 45,
        rowHeight: 45,
        beforeSelectionChange: function (row, event) {
            //evita chamar a selecao, quando clicado em um action button.
        	/*if ( $(event.target).is("a") || $(event.target).is("i") ) return false;
				$state.go($scope.DETAIL_STATE, {id:row.entity.id});*/
        },
        columnDefs: [
            {displayName: $translate('Name'), field: 'name', width: '30%'},
            {displayName: $translate('Type'), cellTemplate:TYPE_COLUMN ,  width: '30%'},
            {displayName: $translate('Required'),field: 'required', sortable: false, cellTemplate: REQUIRED_COLUMN}, 
//            	'<div>' +
//                '<input type="checkbox" disabled="disabled" ng-checked="row.entity.required" >' +
//                '</div>', width: '30%'},
            {displayName: '', sortable: false, cellTemplate: GRID_ACTION_ATTRIBUTES_BUTTONS, width: '10%'}
        ]
    };
    
    
    /**
     * Configurações gerais da ng-grid.
     * @see https://github.com/angular-ui/ng-grid/wiki/Configuration-Options
     */
    $scope.gridAttributesDetail = {
        data: 'attributes',
        useExternalSorting: false,
        multiSelect: false,
        headerRowHeight: 45,
        rowHeight: 45,
        beforeSelectionChange: function (row, event) {
            //evita chamar a selecao, quando clicado em um action button.
        	/*f ( $(event.target).is("a") || $(event.target).is("i") ) return false;
				$state.go($scope.DETAIL_STATE, {id:row.entity.id});*/
        },
        columnDefs: [
            {displayName: $translate('Name'), field: 'name', width: '33%'},
            {displayName: $translate('Type'),  cellTemplate: TYPE_COLUMN ,  width: '33%'},
            {displayName: $translate('Required'), cellTemplate: REQUIRED_COLUMN,  width: '33%'},
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

    /**
     *
     */
    $scope.layers = {};

    //FORM
    /**
     * Variável para armazenar atributos do formulário que
     * não cabem em uma entidade. Ex.:
     * @filter - Filtro da consulta
     */
    $scope.data =
    {
        filter: null,
        tipoAcesso: 'PUBLICO',
        nomeGrupoCamadas: null,
        selects: { fontesDados: null }
    };
    /**
     * Armazena a entitidade corrente para edição ou detalhe.
     */
    $scope.currentEntity = {sistema: false, habilitada: false};

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
        
        $scope.currentState = $scope.LIST_STATE;

        var pageRequest = new PageRequest();
        pageRequest.size = 10;
        $scope.pageRequest = pageRequest;

        $scope.listLayersByFilters(null, pageRequest);
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

        $scope.layers = {};
        
        /**
         * Attributes default
         * */
        /*var attribute = new Attribute();
        attribute.name = "Título";
        attribute.type = "TEXT";
        attribute.required = "true";
        attribute.attributeDefault = true;*/
         
        $scope.attributes = [];
        //$scope.attributes.push(attribute);
        
        $scope.originalGroups = [];
        $scope.selectedGroups = [];
        $scope.addGroups = [];
        $scope.removeGroups = [];

        $scope.currentEntity = new Object();
        
        $scope.currentEntity.icon = 'static/icons/default_blue.png';

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

        layerGroupService.findLayerById(id, {
            callback: function (result) {
                $scope.currentEntity = result;
                $scope.attributes = result.attributes;
                $scope.layers.values = {};
                $scope.layers.values[0] = '1:'+$scope.currentEntity.minimumScaleMap.substring(2);
                $scope.layers.values[1] = '1:'+$scope.currentEntity.maximumScaleMap.substring(2);
                $scope.currentState = $scope.UPDATE_STATE;
                $state.go($scope.UPDATE_STATE);
                $scope.$apply();
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

        layerGroupService.findLayerById(id, {
            callback: function (result) {
                $scope.currentEntity = result;
                $scope.attributes = result.attributes;
                $scope.layers.values = {};
                $scope.layers.values[0] = '1:'+$scope.currentEntity.minimumScaleMap.substring(2);
                $scope.layers.values[1] = '1:'+$scope.currentEntity.maximumScaleMap.substring(2);
                $scope.currentState = $scope.DETAIL_STATE;
                $state.go($scope.DETAIL_STATE, {id: id});
                $scope.$apply();
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
    $scope.changeToRemove = function (layer) {
        $log.info("changeToRemove", layer);

        var dialog = $modal.open({
            templateUrl: "static/libs/eits-directives/dialog/dialog-template.html",
            controller: DialogController,
            windowClass: 'dialog-delete',
            resolve: {
                title: function () {
                    return "Exclusão de camada";
                },
                message: function () {
                    return $translate('admin.layer-config.Are-you-sure-you-want-to-delete-the-layer')+' "' + layer.name + '"? <br/>'+$translate('admin.datasource.This-operation-can-not-be-undone')+'.';
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

        	layerGroupService.removeLayer(layer.id, {
                callback: function (result) {
                    //caso o currentPage esteja null, configura o pager default
                    if ($scope.currentPage == null) {
                        $scope.changeToList();
                        //caso não, usa o mesmo estado para carregar a listagem
                    } else {
                        $scope.listLayersByFilters($scope.data.filter, $scope.currentPage.pageable);
                    }

                    $scope.msg = {type: "success", text: $translate('admin.datasource.The-register')+' "'+ layer.name + '" '+$translate('admin.datasource.was-successfully-deleted')+'.', dismiss: true};
                         			  
      			  	$scope.fadeMsg();
                },
                errorHandler: function (message, exception) {
                    $scope.msg = {type: "danger", text: message, dismiss: true};
                    $scope.fadeMsg();                   
                    
                    $scope.$apply();
                }
            });
        });
    };

    /**
     * Configura o pageRequest conforme o coponente visual pager
     * e chama o serviçoe de listagem, considerando o filtro corrente na tela.
     *
     * @see currentPage
     * @see data.filter
     */
    $scope.changeToPage = function (filter, pageNumber) {
        $scope.currentPage.pageable.page = pageNumber - 1;
        $scope.listLayersByFilters(filter, $scope.currentPage.pageable);
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
    $scope.listLayersByFilters = function (filter, pageRequest) {

        layerGroupService.listLayersByFilters(filter, pageRequest, {
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
     * Realiza a inserção de um novo registro
     * e no suscesso, modifica o estado da tela para o detail.
     */
    $scope.insertLayer = function (layer) {

    	layer.minimumScaleMap = 'UM'+$scope.layers.values[0].substring(2);
    	layer.maximumScaleMap = 'UM'+$scope.layers.values[1].substring(2);
        
        if (!$scope.form().$valid) {
            $scope.msg = {type: "danger", text: $translate("admin.layer-config.The-highlighted-fields-are-required"), dismiss: true};
            $scope.fadeMsg();
            return;
        }
        
        if( ($scope.currentEntity.dataSource.url == null) && ($scope.currentEntity.icon == undefined) ){
        	$scope.msg = {type:"danger", text:$translate("admin.layer-config.Choose-an-icon"),dissmiss:true };
        	$scope.fadeMsg();
        	return;
        }
        
        if ( layer.legend == null ) {
        	layer.name = layer.title;
            
            angular.forEach($scope.attributes, function(value, index){
            	value.layer = layer;
            })
            
            layer.attributes = $scope.attributes;	
        }
        
        layerGroupService.insertLayer(layer, {
            callback: function (result) {
				$scope.currentState = $scope.LIST_STATE;
                $scope.currentEntity = result;
				$state.go($scope.LIST_STATE);
                $scope.msg = {type: "success", text: $translate("admin.layer-config.The-layer-has-been-created-successfully")+"!", dismiss: true};
                $scope.fadeMsg();
                $scope.$apply();
                $scope.saveGroups();
            },
            errorHandler: function (message, exception) {
                $scope.msg = {type: "danger", text: message, dismiss: true};
                $scope.fadeMsg();
                $scope.$apply();
            }
        });
    };

    /**
     * Realiza a atualiza de um registro
     * e no suscesso, modifica o estado da tela para o detail.
     */
    $scope.updateLayer = function (layer) {

    	layer.minimumScaleMap = 'UM'+$scope.layers.values[0].substring(2);
    	layer.maximumScaleMap = 'UM'+$scope.layers.values[1].substring(2);

        if (!$scope.form().$valid) {
            $scope.msg = {type: "danger", text: $scope.INVALID_FORM_MESSAGE, dismiss: true};
            return;
        }

        layer.name = layer.title;
        
        angular.forEach($scope.attributes, function(value, index){
        	value.layer = layer;
        })
        
        layer.attributes = $scope.attributes;
        

        layerGroupService.updateLayer(layer, {
            callback: function () {
				$scope.currentState = $scope.LIST_STATE;
                $scope.saveGroups();
				$state.go($scope.LIST_STATE);
                $scope.msg = {type: "success", text: $translate("admin.layer-config.The-layer-has-been-updated-successfully")+"!", dismiss: true};
                $scope.$apply();
            },
            errorHandler: function (message, exception) {
                $scope.msg = {type: "danger", text: message, dismiss: true};
                $scope.$apply();
            }
        });
    };

    /**
     * 
     */
    $scope.selectLayerGroup = function () {

        layerGroupService.listLayersGroupUpper({
            callback: function (result) {

                var dialog = $modal.open({
                    templateUrl: "modules/admin/ui/layer-config/popup/layer-group-popup.jsp",
                    controller: SelectLayerGroupPopUpController,
                    windowClass: 'xx-dialog grupo-camada-dialog',
                    resolve: {
                    	layerGroups: function () {
                            return result;
                        },
                        currentLayerGroup: function () {
                            return $scope.currentEntity.layerGroup;
                        }
                    }
                });

                dialog.result.then(function (result) {

                    if (result) {
                        $scope.currentEntity.layerGroup = result;
                        $scope.currentEntity.layerGroup.name = result.label;
                    }

                });

            },
            errorHandler: function (message, exception) {
                $scope.message = {type: "error", text: message};
                $scope.$apply();
            }
        });
    };

    /**
     *
     */
    $scope.selectDataSource = function () {
        var dialog = $modal.open({
            templateUrl: "modules/admin/ui/layer-config/popup/data-source-popup.jsp",
            controller: SelectDataSourcePopUpController,
            resolve: {
                dataSourceSelected: function () {
                    return $scope.currentEntity.dataSource;
                }
            }
        });

        dialog.result.then(function (result) {

            // atribui os dados selecionados

            if( $scope.currentEntity.dataSource && $scope.currentEntity.dataSource.id != result.id )
            {
                $scope.currentEntity.dataSource = result;
                $scope.currentEntity.title = null;
                $scope.currentEntity.name = null;
            }
            else
            {
                $scope.currentEntity.dataSource = result;
            }

        });
    };

    /**
     *
     */
    $scope.selectLayer = function () {
        var dialog = $modal.open({
            templateUrl: "modules/admin/ui/layer-config/popup/layers-popup.jsp",
            controller: SelectLayersPopUpController,
            windowClass: 'xx-dialog',
            resolve: {
                currentEntity: function () {
                    return $scope.currentEntity;
                }
            }
        });

        dialog.result.then(function (result) {

            if (result) {
                $scope.currentEntity.name = result.name;
                $scope.currentEntity.title = result.title;
                $scope.currentEntity.legend = result.legend;
            }

        });
    };

    /**
     *
     */
    $scope.clearFields = function () {
        if (!$scope.data.showFields) {
            $scope.currentEntity.nameUser = "";
            $scope.currentEntity.password = "";
        }
    };

    /**
     *
     */
    $scope.close = function () {
        $scope.msg = null;
    };

    /**
     *
     */
    $scope.saveGroups = function() {
        if ($scope.addGroups.length > 0) {
            $scope.linkGrupos();
        }
        if ($scope.removeGroups.length > 0) {
            $scope.unlinkGroups();
        }
    }

    /**
     *
     */
    $scope.linkGroups = function() {
    	layerGroupService.linkAccessGroup($scope.addGroups, $scope.currentEntity.id, {
            callback: function(){
                $scope.addGroups = [];
                $scope.$apply();
            },
            errorHandler: function(error){
                $log.error(error);
            }
        })
    };

    /**
    *
    */
    $scope.unlinkGroups = function() {
        layerGroupService.unlinkGrupoAcesso($scope.removeGroups, $scope.currentEntity.id, {
            callback: function(){
                $scope.removeGroups = [];
                $scope.$apply();
            },
            errorHandler: function(error){
                $log.error(error);
            }
        });
    };
    
  
    $scope.moreIcons = function() {
    	var dialog = $modal.open({
            templateUrl: "modules/admin/ui/layer-config/popup/more-icons-popup.jsp",
            controller: MorePopupController,
            windowClass: 'xx-dialog',
            resolve: {
            	currentEntity: function () {
                    return $scope.currentEntity;
                }
            }
        });

        dialog.result.then(function (result) {

            if (result) {
                $scope.currentEntity.name = result.name;
                $scope.currentEntity.title = result.title;
                $scope.currentEntity.legend = result.legend;
            }

        });
    }
    
    /**
     * Update attribute
     * */
    $scope.updateAttribute = function(attribute) {
    	
    	var dialog = $modal.open({
            templateUrl: "modules/admin/ui/layer-config/popup/update-attribute-popup.jsp",
            controller: UpdateAttributePopUpController,
            windowClass: 'xx-dialog',
            resolve: {
                attributes: function () {
                    return {'many' : $scope.attributes, 'single': attribute};
                }
            }
        });

        dialog.result.then(function (result) {

            if (result) {
                $scope.currentEntity.name = result.name;
                $scope.currentEntity.title = result.title;
                $scope.currentEntity.legend = result.legend;
            }

        });
    }
    
    /**
     * Add attribute
     * */
    $scope.addAttribute = function() {
    	var dialog = $modal.open({
            templateUrl: "modules/admin/ui/layer-config/popup/add-attribute-popup.jsp",
            controller: AddAttributePopUpController,
            windowClass: 'xx-dialog',
            resolve: {
                attributes: function () {
                    return $scope.attributes;
                }
            }
        });

        dialog.result.then(function (result) {

            if (result) {
                $scope.currentEntity.name = result.name;
                $scope.currentEntity.title = result.title;
                $scope.currentEntity.legend = result.legend;
            }

        });
    }
    
    /**
     * Remove attribute
     * */
    $scope.removeAttribute = function(row){
    	var index = $scope.attributes.indexOf(row);

        $scope.attributes.splice(index, 1);
    }
    
    $scope.fadeMsg = function(){
    	$("div.msg").show();
		  
    	setTimeout(function(){
	  		$("div.msg").fadeOut();
	  	}, 5000);
    }
    
//    $(document).click(function() {
//    	$("div.msg").hide();
//    });
    
};
