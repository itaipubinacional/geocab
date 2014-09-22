﻿'use strict';

/**
 * 
 * @param $scope
 * @param $log
 * @param $location
 */
function UsersController( $scope, $injector, $log, $state, $timeout, $modal, $location, $importService ) {
	
	/**
	 * Inject methods, attributes and states inherited of the AbstractCRUDController 
	 * @see AbstractCRUDController
	 */
	$injector.invoke(AbstractCRUDController, this, {$scope: $scope});
	
	/**
	 * Include accountService class
	 */
	$importService("accountService");
	
	
	/*-------------------------------------------------------------------
	 * 		 				 	EVENT HANDLERS
	 *-------------------------------------------------------------------*/
	
	/**
	 *  Handler que escuta toda vez que o usuário/programadamente faz o sorting na ng-grid.
	 *  Quando o evento é disparado, configuramos o pager do spring-data 
	 *  e chamamos novamente a consulta, considerando também o estado do filtro (@see $scope.data.filter)
	 */
	$scope.$on('ngGridEventSorted', function(event, sort) {

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

			$scope.listUsersByFilters( $scope.data.filter, $scope.currentPage.pageable );
		}
	});

	/*-------------------------------------------------------------------
	 * 		 				 	ATTRIBUTES
	 *-------------------------------------------------------------------*/
	//STATES
	
	/**
	 * Static variable
	 * View list of Users
	 */
	$scope.LIST_STATE = "users.list";
	/**
	 * Static variable
	 * View details of User
	 */
	$scope.DETAIL_STATE = "users.detail";
	/**
	 * Static variable
	 * View insert a new User
	 */
	$scope.INSERT_STATE = "users.create";
	/**
	 * Static variable
	 * View edit a User
	 */
	$scope.UPDATE_STATE = "users.edit";
	
	/**
	 * This variable store the current view state
	 * This variable should ALWAYS be in agreement with the URL in browser.
	 */
	$scope.currentState;

	//DATA GRID
	/**
	 * Variável estática coms os botões de ações da grid
	 * O botão de editar navega via URL (sref) por que a edição é feita em outra página,
	 * já o botão de excluir chama um método direto via ng-click por que não tem um estado da tela específico.
	 */
    var GRID_ACTION_BUTTONS = '<div class="cell-centered">' +
	'<a ui-sref="fonte-dados.editar({id:row.entity.id})" title="Editar" class="btn btn-mini"><i class="itaipu-icon-edit"></i></a>'+
	'<a ng-click="changeToRemove(row.entity)" title="Excluir" class="btn btn-mini"><i class="itaipu-icon-delete"></i></a>'+
	'</div>';
   
    //TODO:Tranlate Ativo/Inativo
    var GRID_STATUS_FORMATER = ' <div class="ngCellText " ng-class="col.colIndex()"><span class="ng-binding" ng-cell-text="">'+
    						   '{{row.entity.enabled == true && "Active" || "Inactive"}}</span></div>';
    
	/**
	 * ng-grid configurations. 
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
			             {displayName:'Name', field:'name'},
			             {displayName:'E-mail', field:'email'},
			             {displayName:'Type', field:'role' , width: '200px'},
			             {displayName:'Status', field:'enabled' , width: '100px' , cellTemplate: GRID_STATUS_FORMATER },
			             {displayName:'Action', sortable:false, cellTemplate: GRID_ACTION_BUTTONS, width:'100px'}
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
	$scope.data = { filter:null };
	
	$scope.currentEntity = {};
	
	
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
		
		$log.info('Starting the front controller. Users');

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
	 * Boot the state users.list
	 * 
	 * @see LIST_STATE
	 * @see $stateChangeSuccess
	 * 
	 */
	$scope.changeToList = function() {
		$log.info("changeToList");
	
		var pageRequest = new PageRequest();
		pageRequest.size = 10;
		$scope.pageRequest = pageRequest;
		$scope.listUsersByFilters( null, pageRequest );
	};
	
	/**
	 * Boot the state users.insert 
	 * 
	 * @see INSERT_STATE
	 * @see $stateChangeSuccess
	 * 
	 */
	$scope.changeToInsert = function() {
		$log.info("changeToInsert");

		//Clear current entity
		$scope.currentEntity = {};
		
		//Default role value
		$scope.currentEntity.role = 'ADMINISTRATOR';
		
		$scope.currentState = $scope.INSERT_STATE;
	};
	
	/**
	 * Boot the state users.detail 
	 * 
	 * @see INSERT_STATE
	 * @see $stateChangeSuccess
	 * 
	 */
	$scope.changeToDetail = function( id ) {
		$log.info("changeToDetail", id);

		if ( id == null || id == "" || id == 0 ) {
			$scope.msg = {type:"error", text: $scope.INVALID_ID_MESSAGE, dismiss:true};
			$scope.currentState = $scope.LIST_STATE;
			$state.go($scope.LIST_STATE);
			return;
		}
		

//		fonteDadosService.findFonteDadosById( id, {
//			callback : function(result) {
//				$scope.currentEntity = result;
//				$scope.currentState = $scope.DETAIL_STATE;
//				$state.go($scope.DETAIL_STATE, {id:id});
//				$scope.$apply();
//			},
//			errorHandler : function(message, exception) {
//				$scope.msg = {type:"danger", text: message, dismiss:true};
//				$scope.$apply();
//			}
//		});
	};

	/*-------------------------------------------------------------------
	 * 		 				 	  BEHAVIORS
	 *-------------------------------------------------------------------*/

	/**
	 * Insert a new User.
	 * If sucess change state to users.list
	 */
	$scope.insertUser = function( user ) {

		if ( !$scope.form().$valid ) {
			$scope.msg = {type:"danger", text: $scope.INVALID_FORM_MESSAGE, dismiss:true};
			return;
		}

		accountService.insertUser( user, {
			callback : function() {
				$scope.currentState = $scope.LIST_STATE;
				$state.go($scope.LIST_STATE);
				$scope.msg = {type:"success", text: "Usuario inserido com sucesso!", dismiss:true};
				$scope.$apply();
			},
			errorHandler : function(message, exception) {
				$scope.msg = {type:"danger", text: message, dismiss:true};
				$scope.$apply();
			}
		});
	};
	
	/**
	 * Change the page of ng-grid 
	 * 
	 * @see currentPage
	 * @see data.filter 
	 */
	$scope.changeToPage = function( filter, pageNumber ) {
		$scope.currentPage.pageable.page = pageNumber-1;
		$scope.listUsersByFilters( filter, $scope.currentPage.pageable );
	};

	
	/**
	 * Search Users considering filtering, paging and sorting.
	 * If sucess change state to users.list
	 * 
	 * @see data.filter
	 * @see currentPage
	 */
	$scope.listUsersByFilters = function( filter, pageRequest ) {
				
		accountService.listUsersByFilters( filter, pageRequest, {
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
};

=======
﻿'use strict';

/**
 * 
 * @param $scope
 * @param $log
 * @param $location
 */
function UsersController( $scope, $injector, $log, $state, $timeout, $modal, $location, $importService , $translate) {
	
	/**
	 * Inject methods, attributes and states inherited of the AbstractCRUDController 
	 * @see AbstractCRUDController
	 */
	$injector.invoke(AbstractCRUDController, this, {$scope: $scope});
	
	/**
	 * Include accountService class
	 */
	$importService("accountService");
	
	
	/*-------------------------------------------------------------------
	 * 		 				 	EVENT HANDLERS
	 *-------------------------------------------------------------------*/
	
	/**
	 *  Handler que escuta toda vez que o usuário/programadamente faz o sorting na ng-grid.
	 *  Quando o evento é disparado, configuramos o pager do spring-data 
	 *  e chamamos novamente a consulta, considerando também o estado do filtro (@see $scope.data.filter)
	 */
	$scope.$on('ngGridEventSorted', function(event, sort) {

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

			$scope.listUsersByFilters( $scope.data.filter, $scope.currentPage.pageable );
		}
	});

	/*-------------------------------------------------------------------
	 * 		 				 	ATTRIBUTES
	 *-------------------------------------------------------------------*/
	//STATES
	
	/**
	 * Static variable
	 * View list of Users
	 */
	$scope.LIST_STATE = "users.list";
	/**
	 * Static variable
	 * View details of User
	 */
	$scope.DETAIL_STATE = "users.detail";
	/**
	 * Static variable
	 * View insert a new User
	 */
	$scope.INSERT_STATE = "users.create";
	/**
	 * Static variable
	 * View edit a User
	 */
	$scope.UPDATE_STATE = "users.update";
	
	/**
	 * This variable store the current view state
	 * This variable should ALWAYS be in agreement with the URL in browser.
	 */
	$scope.currentState;

	//DATA GRID
	/**
	 * Variável estática coms os botões de ações da grid
	 * O botão de editar navega via URL (sref) por que a edição é feita em outra página,
	 * já o botão de excluir chama um método direto via ng-click por que não tem um estado da tela específico.
	 */
    var GRID_ACTION_BUTTONS = '<div class="cell-centered">' +
	'<a ui-sref="users.update({id:row.entity.id})" title="'+$translate("admin.users.Edit")+'" class="btn btn-mini"><i class="itaipu-icon-edit"></i></a>'+
	'<a ng-click="changeToDisable(row.entity)" title="'+$translate("admin.users.Disable")+'" class="btn btn-mini"><i class="itaipu-icon-delete"></i></a>'+
	'</div>';
   
    //TODO:Tranlate Ativo/Inativo
    var GRID_STATUS_FORMATER = ' <div class="ngCellText " ng-class="col.colIndex()"><span class="ng-binding" ng-cell-text="">'+
    						   '{{row.entity.enabled == true && "'+$translate("admin.users.Active")+'" || "'+$translate("admin.users.Inactive")+'"}}</span></div>';
    
    //TODO:Tranlate Ativo/Inativo
    var GRID_STATUS_FORMATER2 = ' <div class="ngCellText " ng-class="col.colIndex()"><span class="ng-binding" ng-cell-text="">'+
    						   '{{row.entity.role == "ADMINISTRATOR" && "'+$translate("admin.users.Administrator")+'" || "" }}'+
    						   '{{row.entity.role == "USER" && "'+$translate("admin.users.User")+'" || ""}}'+
    						   '{{row.entity.role == "MODERATOR" && "'+$translate("admin.users.Moderator")+'" || ""}}'+
    						   '</span></div>';
    
	/**
	 * ng-grid configurations. 
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
			             {displayName:$translate("admin.users.Name"), field:'name'},
			             {displayName:$translate("admin.users.E-mail"), field:'email'},
			             {displayName:$translate("admin.users.Access-profile"), field:'role' , width: '200px', cellTemplate: GRID_STATUS_FORMATER2},
			             {displayName:$translate("admin.users.Status"), field:'enabled' , width: '100px' , cellTemplate: GRID_STATUS_FORMATER },
			             {displayName:$translate("admin.users.Actions"), sortable:false, cellTemplate: GRID_ACTION_BUTTONS, width:'100px'}
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
	$scope.data = { filter:null };
	
	$scope.currentEntity = {};
	
	
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
		
		$log.info('Starting the front controller. Users');

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
	 * Boot the state users.list
	 * 
	 * @see LIST_STATE
	 * @see $stateChangeSuccess
	 * 
	 */
	$scope.changeToList = function() {
		$log.info("changeToList");
	
		var pageRequest = new PageRequest();
		pageRequest.size = 10;
		$scope.pageRequest = pageRequest;
		$scope.listUsersByFilters( null, pageRequest );
	};
	
	/**
	 * Boot the state users.insert 
	 * 
	 * @see INSERT_STATE
	 * @see $stateChangeSuccess
	 * 
	 */
	$scope.changeToInsert = function() {
		$log.info("changeToInsert");

		//Clear current entity
		$scope.currentEntity = {};
		
		//Default role value
		$scope.currentEntity.role = 'ADMINISTRATOR';
		
		$scope.currentState = $scope.INSERT_STATE;
	};
	
	/**
	 * Boot the state users.detail 
	 * 
	 * @see INSERT_STATE
	 * @see $stateChangeSuccess
	 * 
	 */
	$scope.changeToDetail = function( id ) {
		$log.info("changeToDetail", id);

		if ( id == null || id == "" || id == 0 ) {
			$scope.msg = {type:"error", text: $scope.INVALID_ID_MESSAGE, dismiss:true};
			$scope.currentState = $scope.LIST_STATE;
			$state.go($scope.LIST_STATE);
			return;
		}
		
		accountService.findUserById( id, {
			callback : function(result) {
				$scope.currentEntity = result;
				$log.info("view user: ", result);
				$scope.currentState = $scope.DETAIL_STATE;
				$state.go($scope.DETAIL_STATE, {id:id});
				$scope.$apply();
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
	$scope.changeToDisable = function( user ) {
		$log.info("changeToDisable", user);

		var dialog = $modal.open( {
			templateUrl: "static/libs/eits-directives/dialog/dialog-template.html",
			controller: DialogController,
			windowClass: 'dialog-disable',
			resolve: {
				title: function(){return $translate("admin.users.Disable-user");},
				message: function(){return $translate("admin.users.Are-you-sure-you-want-to-disable-the-user") + ' "'+user.name+'"? ';},
				buttons: function(){return [ {label: $translate("admin.users.Disable") , css:'btn btn-warning'}, {label: $translate("admin.users.Cancel"), css:'btn btn-default', dismiss:true} ];}
			}
		});

        dialog.result.then( function(result) {

        	accountService.disableUser( user.id, {
				callback : function(result) {
					//caso o currentPage esteja null, configura o pager default
					if ( $scope.currentPage == null ) {
						$scope.changeToList();
						//caso não, usa o mesmo estado para carregar a listagem
					} else {
						$scope.listUsersByFilters($scope.data.filter, $scope.currentPage.pageable);
					}

					$scope.msg = {type: "success", text: $translate('admin.users.The-record-was-successfully-inactivated')+'.', dismiss:true};
				},
				errorHandler : function(message, exception) {
					$scope.msg = {type:"danger", text: message, dismiss:true};
					$scope.$apply();
				}
			});
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
	$scope.changeToEnable = function( user ) {
		$log.info("changeToEnable", user);

		var dialog = $modal.open( {
			templateUrl: "static/libs/eits-directives/dialog/dialog-template.html",
			controller: DialogController,
			windowClass: 'dialog-enable',
			resolve: {
				title: function(){return $translate('admin.users.Enable-user');},
				message: function(){return $translate('admin.users.Are-you-sure-you-want-to-enable-the-user') + ' "'+user.name+'"? ';},
				buttons: function(){return [ {label:$translate("admin.users.Enable"), css:'btn btn-primary'}, {label: $translate("admin.users.Cancel"), css:'btn btn-default', dismiss:true} ];}
			}
		});

        dialog.result.then( function(result) {

        	accountService.enableUser( user.id, {
				callback : function(result) {
					//caso o currentPage esteja null, configura o pager default
					if ( $scope.currentPage == null ) {
						$scope.changeToList();
						//caso não, usa o mesmo estado para carregar a listagem
					} else {
						$scope.listUsersByFilters($scope.data.filter, $scope.currentPage.pageable);
					}

					$scope.msg = {type: "success", text: $translate('admin.users.The-record-was-successfully-enabled') + '.', dismiss:true};
				},
				errorHandler : function(message, exception) {
					$scope.msg = {type:"danger", text: message, dismiss:true};
					$scope.$apply();
				}
			});
		});
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

		accountService.findUserById( $state.params.id, {
			callback : function(result) {
				
				$scope.currentEntity = result;
				$scope.currentEntity.password = ''; //clear password. if not set, not update.
				
				$scope.currentState = $scope.UPDATE_STATE;
				$state.go($scope.UPDATE_STATE);
				$scope.$apply();
			},
			errorHandler : function(message, exception) {
				$scope.msg = {type:"danger", text: message, dismiss:true};
				$scope.$apply();
			}
		}); 
	};

	/*-------------------------------------------------------------------
	 * 		 				 	  BEHAVIORS
	 *-------------------------------------------------------------------*/

	/**
	 * Insert a new User.
	 * If sucess change state to users.list
	 */
	$scope.insertUser = function( user ) {

		if ( !$scope.form().$valid ) {
			$scope.msg = {type:"danger", text: $scope.INVALID_FORM_MESSAGE, dismiss:true};
			return;
		}

		accountService.insertUser( user, {
			callback : function() {
				$scope.currentState = $scope.LIST_STATE;
				$state.go($scope.LIST_STATE);
				$scope.msg = {type:"success", text: $translate('admin.users.User-successfully-inserted') + '!', dismiss:true};
				$scope.$apply();
			},
			errorHandler : function(message, exception) {
				$scope.msg = {type:"danger", text: message, dismiss:true};
				$scope.$apply();
			}
		});
	};
	
	/**
	 * Change the page of ng-grid 
	 * 
	 * @see currentPage
	 * @see data.filter 
	 */
	$scope.changeToPage = function( filter, pageNumber ) {
		$scope.currentPage.pageable.page = pageNumber-1;
		$scope.listUsersByFilters( filter, $scope.currentPage.pageable );
	};

	
	/**
	 * Search Users considering filtering, paging and sorting.
	 * If sucess change state to users.list
	 * 
	 * @see data.filter
	 * @see currentPage
	 */
	$scope.listUsersByFilters = function( filter, pageRequest ) {
				
		accountService.listUsersByFilters( filter, pageRequest, {
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
	 * Realiza a atualização de um registro
	 * e no sucesso modifica o estado da tela para o estado de detalhe
	 */
	$scope.updateUser = function() {

		if ( !$scope.form().$valid ) {
			$scope.msg = {type:"danger", text: $scope.INVALID_FORM_MESSAGE, dismiss:true};
			return;
		}

		accountService.updateUser( $scope.currentEntity , {
			callback : function() {
				$scope.currentState = $scope.LIST_STATE;
				$state.go($scope.LIST_STATE);
				$scope.msg = {type:"success", text: $translate('admin.users.User-updated-successfully')+ '!', dismiss:true};
				$scope.$apply();
			},
			errorHandler : function(message, exception) {
				$scope.msg = {type:"danger", text: message, dismiss:true};
				$scope.$apply();
			}
		});
	};
};
