'use strict';

/**
 *
 * @param $scope
 * @param $log
 * @param $location
 */
	function CustomSearchController( $scope, $injector, $log, $state, $timeout, $modal, $location, $importService, $translate ) {
	/**
	 * Injeta os métodos, atributos e seus estados herdados de AbstractCRUDController.
	 * @see AbstractCRUDController
	 */
	$injector.invoke(AbstractCRUDController, this, {$scope: $scope});

	 $importService("customSearchService");
	
	/*-------------------------------------------------------------------
	 * 		 				 	EVENT HANDLERS
	 *-------------------------------------------------------------------*/

	/**
	 *  Handler que escuta toda vez que o usuário/programadamente faz o sorting na ng-grid.
	 *  Quando o evento é disparado, configuramos o pager do spring-data
	 *  e chamamos novamente a consulta, considerando também o estado do filtro (@see $scope.data.filter)
	 */
	$scope.$on('ngGridEventSorted', function(event, sort) {

        if(event.targetScope.gridId != $scope.gridOptions.gridId)
        {
            return;
        }

		// compara os objetos para garantir que o evento seja executado somente uma vez q não entre em loop
		if ( !angular.equals(sort, $scope.gridOptions.sortInfo) ) {
			$scope.gridOptions.sortInfo = angular.copy(sort);

			//Order do spring-data
			var order = new Order();
			order.direction = sort.directions[0].toUpperCase();
			order.property = sort.fields[0];

			//Sort do spring-data
			$scope.currentPage.pageable.sort = new Sort();
			$scope.currentPage.pageable.sort.orders = [ order ];

			$scope.listCustomSearchByFilters( $scope.data.filter, $scope.currentPage.pageable );
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
	$scope.LIST_STATE = "custom-search.list";
	/**
	 * Variável estática que representa
	 * o estado de detalhe de um registro.
	 */
	$scope.DETAIL_STATE = "custom-search.detail";
	/**
	 * Variável estática que representa
	 * o estado para a criação de registros.
	 */
	$scope.INSERT_STATE = "custom-search.create";
	/**
	 * Variável estática que representa
	 * o estado para a edição de registros.
	 */
	$scope.UPDATE_STATE = "custom-search.update";
	/**
	 * Variável que armazena o estado corrente da tela.
	 * Esta variável deve SEMPRE estar de acordo com a URL
	 * que está no browser.
	 */
	$scope.currentState;

    /**
     * Variável auxiliar
     * @type {{dataSource: null}}
     */
    $scope.data = {
        dataSource : null
    }

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
     *
     */
    $scope.selectedFields = {};

    /**
     *
     * @type {{filterText: string}}
     */
    $scope.filterOptions = {
        filterText: ''
    };
	/**
	 * Variável estática coms os botões de ações da grid
	 * O botão de editar navega via URL (sref) por que a edição é feita em outra página,
	 * já o botão de excluir chama um método direto via ng-click por que não tem um estado da tela específico.
	 */
    var GRID_ACTION_BUTTONS = '<div class="cell-centered">' +
	'<a ui-sref="custom-search.update({id:row.entity.id})" title="'+ $translate('admin.custom-search.Update') +'" class="btn btn-mini"><i class="itaipu-icon-edit"></i></a>'+
	'<a ng-click="changeToRemove(row.entity)" title="'+ $translate('admin.custom-search.Delete') +'" class="btn btn-mini"><i class="itaipu-icon-delete"></i></a>'+
	'</div>';
    
    var IMAGE_LEGEND = '<div class="ngCellText" ng-cell-text ng-class="col.colIndex()">' +
	'<img ng-if="row.entity.layer.dataSource.url" style="width: 20px; height: 20px; border: solid 1px #c9c9c9;" ng-src="{{row.entity.layer.legend}}"/>' +
	'<img ng-if="!row.entity.layer.dataSource.url" style="width: 20px; height: 20px; border: solid 1px #c9c9c9;" ng-src="{{row.entity.layer.icon}}"/>' +
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
				if ( $(event.target).is("a") || $(event.target).is("i") ) return false;
				$state.go($scope.DETAIL_STATE, {id:row.entity.id});
			},
			columnDefs: [
			             {displayName: $translate('admin.custom-search.Symbology') , field:'layer.legend', sortable:false, width: '120px', cellTemplate: IMAGE_LEGEND},
			             {displayName: $translate('admin.custom-search.Custom-search'), field:'name'}, 
			             {displayName: $translate('admin.custom-search.Layer-title'), field:'layer.title'},
			             {displayName: $translate('admin.custom-search.Layer-name'), field:'layer.name'},
			             {displayName: $translate('admin.custom-search.Data-source'), field:'layer.dataSource.name'},
			             {displayName: $translate('admin.custom-search.Actions'), sortable:false, cellTemplate: GRID_ACTION_BUTTONS, width:'100px'}
			             ]
	};

	var GRID_ACTION_ACCESS_BUTTONS = '<div class="cell-centered">' +
	'<a ng-click="removeAccessGroup(row.entity)" title="Excluir" class="btn btn-mini"><i class="itaipu-icon-delete"></i></a>'+
	'</div>';

    var GRID_FIELDS_INPUT = '<div class="cell-centered">' +
        '<input class="form-control" maxlength="144" ng-disabled="currentState == DETAIL_STATE" style="width: 70%; margin-top: -3px;" type="text" ng-model="row.entity.rotulo" placeholder="'+$translate("admin.custom-search.Label")+'">'+
        '</div>';

    var GRID_ACTION_FIELDS_BUTTONS = '<div class="cell-centered">' +
        '<a ng-click="removeCampo(row.entity)" ng-if="currentState != DETAIL_STATE" title="Excluir" class="btn btn-mini"><i class="itaipu-icon-delete"></i></a>' +
        '</div>';

    /**
     *
     * @type {{data: string, multiSelect: boolean, useExternalSorting: boolean, headerRowHeight: number, rowHeight: number, beforeSelectionChange: beforeSelectionChange, columnDefs: {displayName: string, field: string}[]}}
     */
	$scope.gridAccessOptions = {
			data: 'selectedGroups',
			multiSelect: false,
            headerRowHeight: 45,
            rowHeight: 45,
			beforeSelectionChange: function (row, event) {
            return false;
        },
			columnDefs: [
				 {displayName: $translate("Nome"), field:'name'},
				 {displayName: $translate("Description"), field:'description'}				 
			]
	};

    /**
     *
     * @type {{data: string, multiSelect: boolean, useExternalSorting: boolean, headerRowHeight: number, rowHeight: number, beforeSelectionChange: beforeSelectionChange, columnDefs: *[]}}
     */
    $scope.gridlayersOptions = {
        data: 'selectedFields',
        multiSelect: false,
        headerRowHeight: 45,
        rowHeight: 45,
        beforeSelectionChange: function(row) {
            row.changed = true;
            return true;
        },
        afterSelectionChange: function (row, event) {
            if (row.changed){
                $scope.currentlayerField = row.entity;
                row.changed = false;
            }
        },
        columnDefs: [
            {displayName: $translate("admin.custom-search.Name"), field: 'name'},
            {displayName: $translate("Type"), field: 'type'},
            {displayName: $translate("admin.custom-search.Label"), sortable:false, cellTemplate: GRID_FIELDS_INPUT},
            {displayName:'', sortable:false, cellTemplate: GRID_ACTION_FIELDS_BUTTONS, width:'100px'}
        ]
    };

    /**
     *
     * @type {{data: string, multiSelect: boolean, useExternalSorting: boolean, headerRowHeight: number, rowHeight: number, beforeSelectionChange: beforeSelectionChange, columnDefs: *[]}}
     */
    $scope.gridGroupOptions = {
        data: 'selectedGroups',
        multiSelect: false,
        headerRowHeight: 45,
        rowHeight: 45,
        beforeSelectionChange: function (row, event) {
            return false;
        },
        columnDefs: [
            {displayName: $translate("Nome"), field: 'name'},
            {displayName: $translate("Description"), field: 'description'},
            {displayName: '', sortable: false, cellTemplate: GRID_ACTION_ACCESS_BUTTONS, width: '100px'}
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
	$scope.data =
	{
		filter:null,
		selects : { CustomSearch: null }
	};
	/**
	 * Armazena a entitidade corrente para edição ou detalhe.
	 */
	$scope.currentEntity;

    /**
     *
     */
    $scope.currentLayerField = null;

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
	$scope.initialize = function( toState, toParams, fromState, fromParams ) {
		var state = $state.current.name;
		/**
		 * É necessario remover o atributo sortInfo pois o retorno de uma edição estava duplicando o valor do mesmo com o atributo Sort
		 * impossibilitando as ordenações nas colunas da grid.
		 */
		if( $scope.gridOptions.sortInfo ){
			delete $scope.gridOptions.sortInfo;
		}

        $scope.currentCampoCamada = null;

		$log.info("Starting the front controller.");

		switch (state) {
			case $scope.LIST_STATE: {
				$scope.changeToList();
			}
			break;
			case $scope.DETAIL_STATE: {
				$scope.changeToDetail( $state.params.id );
			}
			break;
			case $scope.INSERT_STATE: {
				$scope.changeToInsert();
			}
			break;
			case $scope.UPDATE_STATE: {
				$scope.changeToUpdate( $state.params.id );
			}
			break;
			default : {
				$state.go( $scope.LIST_STATE );
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
	$scope.changeToList = function() {
		$log.info("changeToList");

		var pageRequest = new PageRequest();
		pageRequest.size = 10;
		$scope.pageRequest = pageRequest;

		$scope.listCustomSearchByFilters( null, pageRequest );
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
	$scope.changeToInsert = function() {
		$log.info("changeToInsert");

		$scope.currentEntity = new Object();

        $scope.data.dataSource = null;

        $scope.originalGroups = [];
        $scope.selectedGroups = [];
        $scope.addGroups = [];
        $scope.removeGroups = [];
	   
	    $scope.selectedFields = [];

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
	$scope.changeToUpdate = function( id ) {
		$log.info("changeToUpdate", id);

		customSearchService.findCustomSearchById( $state.params.id, {
			callback : function(result) {
				$scope.currentEntity = result;
                $scope.selectedFields = result.layerFields;
                $scope.data.dataSource = $scope.currentEntity.layer.dataSource;
				$scope.currentState = $scope.UPDATE_STATE;
				$state.go($scope.UPDATE_STATE);
				$scope.$apply();

                $scope.loadlayerGroups(result.id);
			},
			errorHandler : function(message, exception) {
				$scope.msg = {type:"danger", text: message, dismiss:true};
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
	$scope.changeToDetail = function( id ) {
		$log.info("changeToDetail", id);

		if ( id == null || id == "" || id == 0 ) {
			$scope.msg = {type:"error", text: $scope.INVALID_ID_MESSAGE, dismiss:true};
			$scope.currentState = $scope.LIST_STATE;
			$state.go($scope.LIST_STATE);
			return;
		}

		customSearchService.findCustomSearchById( id, {
			callback : function(result) {
				$scope.currentEntity = result;
                $scope.selectedFields = result.layerFields;
				$scope.currentState = $scope.DETAIL_STATE;
				$state.go($scope.DETAIL_STATE, {id:id});
				$scope.$apply();

                $scope.loadlayerGroups(result.id);
			},
			errorHandler : function(message, exception) {
				$scope.msg = {type:"danger", text: message, dismiss:true};
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
	$scope.changeToRemove = function( CustomSearch ) {
		$log.info("changeToRemove", CustomSearch);

		var dialog = $modal.open( {
			templateUrl: "static/libs/eits-directives/dialog/dialog-template.html",
			controller: DialogController,
			windowClass: 'dialog-delete',
			resolve: {
				title: function(){return $translate("admin.custom-search.Custom-search-exclusion");},
				message: function(){return $translate("admin.custom-search.Are-you-sure-you-want-to-delete-the-layer-group") +' ' + CustomSearch.name + ' ' + '? <br/> ' + $translate("This-operation-can-not-be-undone");},
				buttons: function(){return [ {label:$translate("admin.custom-search.Delete"), css:'btn btn-danger'}, {label:$translate("admin.custom-search.Cancel"), css:'btn btn-default', dismiss:true} ];}
			}
		});



        dialog.result.then( function(result) {

			customSearchService.removeCustomSearch( CustomSearch.id, {
				callback : function(result) {
					//caso o currentPage esteja null, configura o pager default
					if ( $scope.currentPage == null ) {
						$scope.changeToList();
						//caso não, usa o mesmo estado para carregar a listagem
					} else {
						$scope.listCustomSearchByFilters($scope.data.filter, $scope.currentPage.pageable);
					}

					$scope.msg = {type: "success", text: $translate("admin.custom-search.The-register") + ' " '+CustomSearch.name+'" ' + $translate("admin.custom-search.Was-successfully-deleted"), dismiss:true};
				},
				errorHandler : function(message, exception) {
					$scope.msg = {type:"danger", text: message, dismiss:true};
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
	$scope.changeToPage = function( filter, pageNumber ) {
		$scope.currentPage.pageable.page = pageNumber-1;
		$scope.listCustomSearchByFilters( filter, $scope.currentPage.pageable );
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
	$scope.listCustomSearchByFilters = function( filter, pageRequest ) {

		customSearchService.listCustomSearchByFilters( filter, pageRequest, {
			callback : function(result) {
				$scope.currentPage = result;
				$scope.currentPage.pageable.pageNumber++;//Para fazer o bind com o pagination
				$scope.currentState = $scope.LIST_STATE;
				$state.go( $scope.LIST_STATE );
				$scope.$apply();
			},
			errorHandler : function(message, exception) {
				$scope.msg = {type:"danger", text: message, dismiss:true};
				$scope.$apply();
			}
		});
	};

	/**
	 * Realiza a inserção de um novo registro
	 * e no suscesso, modifica o estado da tela para o detail.
	 */
	$scope.insertCustomSearch = function( customSearch ) {

		if ( !$scope.form().$valid ) {
			$scope.msg = {type:"danger", text: $scope.INVALID_FORM_MESSAGE, dismiss:true};
			return;
		}

		customSearch.layerFields = $scope.selectedFields;
        
		customSearchService.insertCustomSearch( customSearch, {
			callback : function(result) {        				
				$scope.currentState = $scope.LIST_STATE;
				$scope.currentEntity.id = result;
				$state.go($scope.LIST_STATE);
				$scope.msg = {type:"success", text: "Pesquisa personalizada inserida com sucesso!", dismiss:true};
				$scope.$apply();
				$scope.saveGroups();
			},
			errorHandler : function(message, exception) {
				$scope.msg = {type:"danger", text: message, dismiss:true};
				$scope.$apply();
			}
		});
	};

	/**
	 * Realiza a atualiza de um registro
	 * e no suscesso, modifica o estado da tela para o detail.
	 */
	$scope.updateCustomSearch = function( CustomSearch ) {

		if ( !$scope.form().$valid ) {
			$scope.msg = {type:"danger", text: $scope.INVALID_FORM_MESSAGE, dismiss:true};
			return;
		}

        CustomSearch.layerGroups = $scope.selectedGroups;
        CustomSearch.layerFields = $scope.selectedFields;

		customSearchService.updateCustomSearch( CustomSearch, {
			callback : function() {
                $scope.saveGroups();
				$scope.currentState = $scope.LIST_STATE;
				$state.go($scope.LIST_STATE);
				$scope.msg = {type:"success", text: $translate("admin.custom-search.Custom-search-successfully-updated") + ' !', dismiss:true};

				$scope.$apply();
			},
			errorHandler : function(message, exception) {
				if (exception.message.indexOf("ConstraintViolationException") > -1){
					message = $translate("admin.custom-search.The-field-name-already-exists-change-and-try-again") + '.';
				}
				$scope.msg = {type:"danger", text: message, dismiss:true};
				$scope.$apply();
			}
		});
	};

    /**
     *
     */
    $scope.selectAccessGroup = function () {
        var dialog = $modal.open({
            templateUrl: "modules/admin/ui/custom-search/popup/access-group-popup.jsp",
            controller: SelectAccessGroupPopUpController,
            resolve: {
                selectedGroups : function () {
                    return $scope.selectedGroups;
                }
            }
        });

        dialog.result.then(function (result) {
            $log.log(result);

//            if (result != null && result.length > 0) {
//                $scope.selectedGroups = $scope.selectedGroups.concat(result);
//                for (var i = 0; i < result.length; i++) {
//                    var index = $scope.findByIdInArray($scope.originalGroups, result[i]);
//                    if (index == -1) {
//                        $scope.addGroups.push(result[i]);
//                    }
//                    var index2 = $scope.findByIdInArray($scope.removeGroups, result[i]);
//                    if (index2 > -1) {
//                        $scope.removeGroups.splice(index2, 1);
//                    }
//                }
//            }
//            
            if (result) {
                for (var i = 0; i < result.length; i++) {
                    var index = $scope.findByIdInArray($scope.selectedGroups, result[i]);
                    var index2 = $scope.findByIdInArray($scope.originalGroups, result[i]);
                    var index3 = $scope.findByIdInArray($scope.removeGroups, result[i]);

                    //Identifica se marcou novos registros
                    if (index == -1 && index2 == -1) {
                        var indexAdd = $scope.findByIdInArray($scope.addGroups, result[i]);
                        if (indexAdd == -1)
                            $scope.addGroups.push(result[i]);
                    }

                    if (index3 > -1) {
                        $scope.removeGroups.splice(index3, 1);
                    }

                }
                for (var i = 0; i < $scope.selectedGroups.length; i++) {

                    var index = $scope.findByIdInArray(result, $scope.selectedGroups[i]);

                    if (index == -1) {
                        var index2 = $scope.findByIdInArray($scope.addGroups, $scope.selectedGroups[i]);
                        var index3 = $scope.findByIdInArray($scope.removeGroups, $scope.selectedGroups[i]);
                        var index4 = $scope.findByIdInArray($scope.originalGroups, $scope.selectedGroups[i]);

                        if (index2 > -1){
                            var indexAdd = $scope.findByIdInArray($scope.removeGroups, $scope.selectedGroups[i]);
                            if (indexAdd > -1)
                                $scope.addGroups.splice(indexAdd, 1);
                        }
                        if (index3 == -1 && index4 > -1) {
                            $scope.removeGroups.push($scope.selectedGroups[i]);
                        }

                    }
                }
                $scope.selectedGroups = result;
            }
        });
    }

    /**
     *
     * @param entity
     */
    $scope.removeAccessGroup = function (entity) {
        var index = $scope.selectedGroups.indexOf(entity);
        var index2 = $scope.addGroups.indexOf(entity);
        var index3 = $scope.originalGroups.indexOf(entity);
        if (index > -1) {
            $scope.selectedGroups.splice(index, 1);
        }
        if (index2 > -1) {
            $scope.addGroups.splice(index2, 1);
        }
        if (index3 > -1) {
            $scope.removeGroups.push(entity);
        }
    };

    /**
     *
     * @param entity
     */
    $scope.removeCampo = function (entity) {
        var index = $scope.selectedFields.indexOf(entity);
        if (index > -1) {
            $scope.selectedFields.splice(index, 1);
        }

        for(var i = 0; i < $scope.selectedFields.length; i++)
        {
            $scope.selectedFields[i].order = i;
        }
    }

    /**
     *
     */
    $scope.linkGroups = function() {
        customSearchService.linkAccessGroup($scope.addGroups, $scope.currentEntity.id, {
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
     * @param pesquisa
     * @param grupoAcesso
     */
    $scope.unlinkGroups = function() {
        customSearchService.unlinkAccessGroup($scope.removeGroups, $scope.currentEntity.id, {
            callback: function(result){
                $scope.removeGroups = [];
                $scope.$apply();
            },
            errorHandler: function(error){
                $log.error(error);
            }
        });
    };

    /*-------------------------------------------------------------------
     *                 POPUP - CONFIGURAÇÕES DE CAMADA
     *-------------------------------------------------------------------*/
    $scope.selectLayerConfig = function () {

        //Função responsável por chamar a popup de configurações de camada para associação.
        var dialog = $modal.open({
            templateUrl: 'modules/admin/ui/custom-search/popup/layer-config-popup.jsp',
            controller: SelectLayerConfigPopUpController,
            windowClass: 'xx-dialog',
            resolve: {
                dataSource : function () {
                    return $scope.data.dataSource;
                },
                selectedLayer : function () {
                    return $scope.currentEntity.layer;
                }
            }
        });

        dialog.result.then(function (result) {

            if (result) {
                $scope.currentEntity.layer = result;
            }

        });

    };

    /*-------------------------------------------------------------------
     *                 POPUP - FONTE DE DADOS
     *-------------------------------------------------------------------*/
    $scope.selectDataSource = function () {
        var dialog = $modal.open({
            templateUrl: "modules/admin/ui/layer-config/popup/data-source-popup.jsp",
            controller: SelectDataSourcePopUpController,
            resolve: {
                dataSourceSelected : function () {
                    return $scope.currentEntity.dataSource;
                }
            }
        });

        dialog.result.then(function (result) {
            if (result != null) {
                $scope.data.dataSource = result;
                if ($scope.currentEntity.layer != null && $scope.currentEntity.layer.dataSource.id != result.id){
                    $scope.currentEntity.layer = null;
                }
                $scope.$apply;
            }
        });
    };

    /**
     *
     */
    $scope.levelUp = function() {

        var rows = $scope.gridlayersOptions.ngGrid.data;

        for(var i = 0; i < rows.length; i++)
        {
            if( rows[i].name == $scope.currentLayerField.name && rows[i].type ==  $scope.currentLayerField.type )
            {
                if( rows[i].order > 0 )
                {
                    var currentRow = rows[i];
                    rows[i] = rows[i-1];
                    rows[i-1] = currentRow;
                    rows[i].order = i;
                    rows[i-1].order = i - 1;

                    $scope.selectedFields = rows;

                }
            }
        }
        $scope.apply;
    };

    /**
     *
     */
    $scope.levelDown = function() {
        var rows = $scope.gridlayersOptions.ngGrid.data;
        for(var i = 0; i < rows.length; i++)
        {
            if( rows[i].name == $scope.currentLayerField.name && rows[i].type ==  $scope.currentLayerField.type )
            {
                if( rows[i].order < rows.length - 1 )
                {
                    var currentRow = rows[i];
                    rows[i] = rows[i+1];
                    rows[i+1] = currentRow;
                    rows[i].order = i;
                    rows[i+1].order = i + 1;
                    $scope.selectedFields = rows;
                    return;
                }
            }
        }
        $scope.apply;
    }

    /*-------------------------------------------------------------------
     *                 POPUP - CAMPOS DE CAMADA
     *-------------------------------------------------------------------*/
    $scope.addFields = function () {
        //Função responsável por chamar a popup de configurações de camada para associação.
        var dialog = $modal.open({
            templateUrl: 'modules/admin/ui/custom-search/popup/add-fields-popup.jsp',
            controller: AddFieldsPopupController,
            resolve: {
                layer: function () {
                    return $scope.currentEntity.layer;
                },
                layerExistingFields: function () {
                    return $scope.selectedFields;
                }
            }
        });

        dialog.result.then(function (result) {

            if (result != null) {

                $scope.currentLayerField = null;

                $scope.selectedFields = result;

                for(var i = 0; i < $scope.selectedFields.length; i++)
                {
                    $scope.selectedFields[i].order = i;
                }
            }
        });
    };

    /**
     *
     * @param pesquisaId
     */
    $scope.loadlayerGroups = function(pesquisaId) {
        customSearchService.listAccessGroupBySearchId(pesquisaId, {
            callback: function(result) {
                $scope.selectedGroups = result;
                $scope.originalGroups = result.slice(0);

                $scope.$apply();
            },
            errorHandler: function(error) {
                $log.error(error);
            }
        })
    };

    /**
     *
     */
    $scope.saveGroups = function() {
        if ($scope.addGroups.length > 0) {
            $scope.linkGroups();
        }
        if ($scope.removeGroups.length > 0) {
            $scope.unlinkGroups();
        }
    }
};

