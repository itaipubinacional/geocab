'use strict';

/**
 *
 * @param $scope
 * @param $log
 * @param $location
 */
function LayerGroupController( $scope, $injector, $log, $state, $timeout, $modal, $location ) {
    /**
     * Injeta os métodos, atributos e seus estados herdados de AbstractCRUDController.
     * @see AbstractCRUDController
     */
    $injector.invoke(AbstractCRUDController, this, {$scope: $scope});

    /*-------------------------------------------------------------------
     * 		 				 	EVENT HANDLERS
     *-------------------------------------------------------------------*/


    /*-------------------------------------------------------------------
     * 		 				 	ATTRIBUTES
     *-------------------------------------------------------------------*/
    //STATES
    /**
     * Variável estática que representa
     * o estado de listagem de registros.
     */
    $scope.LIST_STATE = "layer-group.list";

    /**
     * 
     */
    $scope.currentState;

    //FORM
    /**
     * Variável para armazenar atributos do formulário que
     * não cabem em uma entidade. Ex.:
     * @filter - Filtro da consulta
     */
    $scope.data = { filter:null };
    /**
     * Armazena a entitidade corrente para edição ou detalhe.
     */
    $scope.currentEntity;

    /**
     * Controla se é necessário salvar a ordenação antes de criar ou editar grupo
     */
    var isNeedSave = false;

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

        $log.info("Starting the front controller.");
        
        /* Na inicialização do menu de camadas não é necessário validar
         * se a ordenação foi salva, pois os campos não foram ordenados*/
    	isNeedSave = false;
    	
    	$scope.currentState = $scope.LIST_STATE;
        $state.go( $scope.LIST_STATE );
        
        //$scope.changeToList();

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

        $scope.listLayerGroup();
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
    $scope.listLayerGroup = function() {

        grupoCamadasService.listSuperiores( {
            callback : function(result) {
                $scope.currentPage = result;
                $scope.currentState = $scope.LIST_STATE;
                $state.go( $scope.LIST_STATE );
                $scope.$apply();
            },
            errorHandler : function(message, exception) {
                $scope.message = {type:"error", text: message};
                $scope.$apply();
            }
        });
    };

    /**
     * Save all nodes
     *
     */
    $scope.saveGrupoCamadas = function( ) {

        grupoCamadasService.saveAllGrupoCamadas( getRootNodesScope().$nodesScope.$modelValue, {
            callback : function() {
                $scope.listLayerGroup();
                isNeedSave = false;
                $scope.msg = {type:"success", text: "Ordenação do rascunho de grupo de camadas foi salva com sucesso!", dismiss:true};
                $scope.$apply();
            },
            errorHandler : function(message, exception) {
                console.error( message, exception );
                $scope.message = {type:"error", text: message};
                $scope.$apply();
            }
        });
    };

    $scope.publishGrupoCamadas = function() 
    {
        grupoCamadasService.publicarGrupoCamadas( getRootNodesScope().$nodesScope.$modelValue, {
            callback : function() {
                $scope.listLayerGroup();
                isNeedSave = false;
                $scope.msg = {type:"success", text: "Grupo de camadas publicado com sucesso!", dismiss:true};
                $scope.$apply();
            },
            errorHandler : function(message, exception) {
                console.error( message, exception );
                $scope.message = {type:"error", text: message};
                $scope.$apply();
            }
        });

	};

    /**
     *
     * UI TREE
     *
     */

    /**
     * Remover um item da tree
     *
     * @param scope
     */
    $scope.removeItem = function(scope)
    {

        if( scope.$modelValue.nodes && scope.$modelValue.nodes.length > 0 && scope.$modelValue.camadas && scope.$modelValue.camadas.length > 0)
        {
            $scope.msg = {type:"danger", text: "Não é possivel remover grupos de camadas que possuem camadas!", dismiss:true};
            return;
        } else
        if( scope.$modelValue.nodes && scope.$modelValue.nodes.length > 0 )
        {
            $scope.msg = {type:"danger", text: "Não é possivel remover grupos de camadas que possuem filhos!", dismiss:true};
            return;
        }

        else
        {
            var dialog = $modal.open( {
                templateUrl: "assets/libs/eits-directives/dialog/dialog-template.html",
                controller: DialogController,
                windowClass: 'dialog-delete',
                resolve: {
                    title: function(){return "Exclusão de grupo de camadas";},
                    message: function(){return 'Tem certeza que deseja excluir o grupo de camadas "<b>'+scope.$modelValue.nome+'</b>"? <br/>Esta operação não poderá mais ser desfeita.';},
                    buttons: function(){return [ {label:'Excluir', css:'btn btn-danger'}, {label:'Cancelar', css:'btn btn-default', dismiss:true} ];}
                }
            });

            dialog.result.then( function(result) {

                grupoCamadasService.removeGrupoCamadas( scope.$modelValue.id, {
                    callback : function(result) {
                        scope.remove();
                        $scope.$apply();
                    },
                    errorHandler : function(message, exception) {
                        console.error( message, exception );
                        $scope.message = {type:"error", text: message};
                        $scope.$apply();
                    }
                });

            });

        }
    };

    /**
     * Adiciona um novo grupo de camadas
     */
    $scope.newGrupoCamadas = function () {

        /**
         * Se houver alguma mensagem sendo exibida,
         */
//        if ($scope.msg != null) return;

        if( isNeedSave )
        {
            $scope.msg = null;
            $scope.msg = {type:"danger", text: "Por favor, salve a ordenação antes de criar ou editar um grupo", dismiss:true};
            return;
        }

        var dialog = $modal.open( {
            keyboard: false,
            templateUrl: 'modules/administrativo/ui/grupo-camadas/popup/grupo-camadas-popup.html',
            controller: GrupoCamadasPopUpController,
            scope: $scope,
            resolve: {
                grupos: function(){return getRootNodesScope().$nodesScope.$modelValue;},
                item: function(){return null;}
            }
        });

        dialog.result.then( function(result) 
        {
            if( result == null )
            {
                return;
            }
            
            grupoCamadasService.insertGrupoCamadas( result, {
                callback : function(result) {
                    result.nodes = [];
                    
                    getRootNodesScope().$nodesScope.$modelValue.unshift(result);
                    
                    grupoCamadasService.saveAllParentGrupoCamadas( getRootNodesScope().$nodesScope.$modelValue, {
                        callback : function() {
                            $scope.$apply();
                        },
                        errorHandler : function(message, exception) {
                            console.error( message, exception );
                            $scope.message = {type:"error", text: message};
                            $scope.$apply();
                        }
                    });
                    
                    $scope.$apply();
                },
                errorHandler : function(message, exception) {
                    console.error( message, exception );
                    $scope.message = {type:"error", text: message};
                    $scope.$apply();
                }
            });
        });
    };

    /**
     * Editar um nome de um item da tree
     * @param scope
     */
    $scope.editItem = function(scope) {

        var dialog = $modal.open({
            keyboard: false,
            templateUrl: 'modules/administrativo/ui/grupo-camadas/popup/grupo-camadas-popup.html',
            controller: GrupoCamadasPopUpController,
            scope: $scope,
            resolve: {
                grupos: function(){return scope.$parentNodesScope.$modelValue;},
                item: function(){return scope.$modelValue;},
                isNeedSave: function(){return isNeedSave;}
            }

        });

        dialog.result.then( function(result) 
        {

            if( result == null )
            {
                return;
            }

            scope.$modelValue.nome = result.nome;

            grupoCamadasService.updateGrupoCamadas( scope.$modelValue, {
                callback : function(result) {
                    result.nodes = [];

                    grupoCamadasService.saveAllParentGrupoCamadas( getRootNodesScope().$nodesScope.$modelValue, {
                        callback : function() {
                            $scope.$apply();
                        },
                        errorHandler : function(message, exception) {
                            console.error( message, exception );
                            $scope.message = {type:"error", text: message};
                            $scope.$apply();
                        }
                    });

                    $scope.$apply();
                },
                errorHandler : function(message, exception) {
                    console.error( message, exception );
                    $scope.message = {type:"error", text: message};
                    $scope.$apply();
                }
            });
        });
    };

    /**
     * Função para mostrar a tree caso haja itens
     * @param item
     * @returns {boolean}
     */
    $scope.visible = function(item) {

        if ($scope.query && $scope.query.length > 0 && item.title.indexOf($scope.query) == -1)
        {
            return false;
        }

        return true;
    };

    /**
     * Função para controlar as ações da tree, como a ação de aceitar o drop, a função para controlar o momento antes do drop e o momento que é feito o drop do item
     * @type {{accept: accept, beforeDrop: beforeDrop, dropped: dropped}}
     */
    $scope.treeOptions = {

        accept: function(sourceNode, destNodes, destIndex) {

        	for (var i = 0; i < destNodes.$modelValue.length; i++)
            {
                if( ( destNodes.$modelValue[i].nodes != null && sourceNode.$modelValue.nodes == null ) || ( sourceNode.$modelValue.nodes != null && destNodes.$modelValue[0].nodes == null ))
                {
                    //Caso o usuário não tenha permissão não aparece nem a possibilidade para arrastar um item
                    return false;
                }
            }
            return true;
        },
        beforeDrop: function(event) {
        	
        	//Se tivermos alguma mensagem aparecendo, ela será removida.
        	$scope.msg = null;
        	
            var sourceNode = event.source.nodeScope;
            var destNodes = event.dest.nodesScope;

            for( var i= 0; i < destNodes.$modelValue.length; i++)
            {
                if( destNodes.$modelValue[i].nodes && destNodes.$modelValue[i].nome == sourceNode.$modelValue.nome && destNodes.$modelValue[i].id != sourceNode.$modelValue.id )
                {
                    $scope.msg = {type:"danger", text: "Já existe um grupo com este nome no mesmo nível", dismiss:true};
                    event.source.nodeScope.$$apply = false;
                }
            }
        },
        dropped: function(event) {

            var sourceNode = event.source.nodeScope;
            var destNodes = event.dest.nodesScope;

            isNeedSave = true;

            if( destNodes.$nodeScope == null )
            {
                sourceNode.$modelValue.grupoCamadasSuperior = null;
            }
            else
            {
                if( destNodes.$nodeScope )
                {
                    sourceNode.$modelValue.grupoCamadasSuperior = destNodes.$nodeScope.$modelValue;
                }

                if( sourceNode.$modelValue.nodes == null )
                {
                    if ( destNodes.$nodeScope.$modelValue.camadas == null )
                    {
                        destNodes.$nodeScope.$modelValue.camadas = new Array();
                    }

                    if( destNodes.$nodeScope.$modelValue.camadas.length == 0 )
                    {
                        destNodes.$nodeScope.$modelValue.camadas.push( sourceNode.$modelValue );
                        destNodes.$nodeScope.$modelValue.gruposCamadas = null;
                    }
                    else if( destNodes.$nodeScope.$modelValue.camadas.length > 0 )
                    {
                        for ( var i = 0; i < destNodes.$nodeScope.$modelValue.camadas.length; i++ )
                        {
                            if( destNodes.$nodeScope.$modelValue.camadas[i].id == sourceNode.$modelValue.id )
                            {
                                return;
                            }
                        }
                        destNodes.$nodeScope.$modelValue.camadas.push( sourceNode.$modelValue );
                    }
                }
                else
                {
                    if ( destNodes.$nodeScope.$modelValue.gruposCamadas == null )
                    {
                        destNodes.$nodeScope.$modelValue.gruposCamadas = new Array();
                    }

                    if( destNodes.$nodeScope.$modelValue.gruposCamadas.length == 0 )
                    {
                        destNodes.$nodeScope.$modelValue.gruposCamadas.push( sourceNode.$modelValue );
                        destNodes.$nodeScope.$modelValue.camadas = null;
                    }
                    else if( destNodes.$nodeScope.$modelValue.gruposCamadas.length > 0 )
                    {
                        for ( var i = 0; i < destNodes.$nodeScope.$modelValue.gruposCamadas.length; i++ )
                        {
                            if( destNodes.$nodeScope.$modelValue.gruposCamadas[i].id == sourceNode.$modelValue.id )
                            {
                                return;
                            }
                        }
                        destNodes.$nodeScope.$modelValue.gruposCamadas.push( sourceNode.$modelValue );
                    }
                }
            }
        }
    };

    /**
     * Retorna a lista completa da tree
     */
    var getRootNodesScope = function() 
    {
        return angular.element(document.getElementById("tree-root")).scope();
    };

};

