'use strict';

/**
 *
 * @param $scope
 * @param $log
 * @param $location
 */
function LayerGroupController( $scope, $injector, $log, $state, $timeout, $modal, $location, $importService, $translate ) {
    /**
     * Injects the methods, attributes and their inherited state of AbstractCRUDController.
     * @see AbstractCRUDController
     */
    $injector.invoke(AbstractCRUDController, this, {$scope: $scope});
    
    $importService("layerGroupService");

    /*-------------------------------------------------------------------
     * 		 				 	EVENT HANDLERS
     *-------------------------------------------------------------------*/


    /*-------------------------------------------------------------------
     * 		 				 	ATTRIBUTES
     *-------------------------------------------------------------------*/
    //STATES
    /**
     * Static variable that represents 
     * The listing of state records.
     */
    $scope.LIST_STATE = "layer-group.list";

    /**
     * 
     */
    $scope.currentState;

    //FORM
    /**
     * Variable to store attributes of the form 
     * That do not fit into one entity. Ex.:
     * @filter - Query filter
     */
    $scope.data = { filter:null };
    
    
    /**
     * Stores the current entity for editing or detail.
     */
    $scope.currentEntity;

    /**
     * Controls if it's required to save the sorting before creating or editing group
     */
    var isNeedSave = false;

    /*-------------------------------------------------------------------
     * 		 				 	  NAVIGATIONS
     *-------------------------------------------------------------------*/
    
    /**
     * Main method that makes the role of front-controller screen.
	 * It is invoked whenever there is a change of URL (@see $stateChangeSuccess),
	 * Ex.: /list -> changeToList()
	 *      /create -> changeToInsert()
	 *      
	 * If the state is not found, it directs you to the list
     */
    $scope.initialize = function( toState, toParams, fromState, fromParams ) {
    	
    	var state = $state.current.name;

        $log.info("Starting the front controller.");
        
        /* At startup of the layers menu is not necessary to validate
         * If the ordering was saved because the fields were not ordered */
    	isNeedSave = false;
    	
    	$scope.currentState = $scope.LIST_STATE;
        $state.go( $scope.LIST_STATE );
        
        $scope.changeToList();

    };

    /**
     * 
     * Boot the state users.list
	 * @see LIST_STATE
	 * @see $stateChangeSuccess
	 * 
	 * To switch to this state, you must first load the data from the query.
     * 
     */
    $scope.changeToList = function() {
        $log.info("changeToList");

        $scope.listLayerGroup();
    };

    /*-------------------------------------------------------------------
     * 		 				 	  BEHAVIORS
     *-------------------------------------------------------------------*/

    /**
  	 * Performs the query logs, considering filter, paging and sorting. 
  	 * When ok, change the state of the screen to list.
     *
     * @see data.filter
     * @see currentPage
     */
    $scope.listLayerGroup = function() {

        layerGroupService.listLayersGroupUpper( {
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
    $scope.saveLayerGroup = function( ) {

    	layerGroupService.saveAllLayersGroup( getRootNodesScope().$nodesScope.$modelValue, {
            callback : function() {
                $scope.listLayerGroup();
                isNeedSave = false;
                $scope.msg = {type:"success", text: $translate("layer-group-view.Ordering-the-draft-layer-group-has-been-saved-successfully"), dismiss:true};
                $scope.fadeMsg();
                $scope.$apply();
            },
            errorHandler : function(message, exception) {
                console.error( message, exception );
                $scope.message = {type:"error", text: message};
                $scope.fadeMsg();
                $scope.$apply();
            }
        });
    };

    $scope.publishLayerGroup = function() 
    {
    	layerGroupService.publishLayerGroup( getRootNodesScope().$nodesScope.$modelValue, {
            callback : function() {
                $scope.listLayerGroup();
                isNeedSave = false;
                $scope.msg = {type:"success", text: $translate("layer-group-view.Layer-group-succesfully-published"), dismiss:true};
                $scope.fadeMsg();
                $scope.$apply();
            },
            errorHandler : function(message, exception) {
                console.error( message, exception );
                $scope.message = {type:"error", text: message};
                $scope.fadeMsg();
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
     * Remove an item from the tree
     *
     * @param scope
     */
    $scope.removeItem = function(scope)
    {
    	
    	
        if( scope.$modelValue.nodes && scope.$modelValue.nodes.length > 0 && scope.$modelValue.layers && scope.$modelValue.layers.length > 0)
        {
            $scope.msg = {type:"danger", text: $translate("layer-group-view.Is-not-possible-to-remove-layers-groups-that-have-layers"), dismiss:true};
            
            $scope.fadeMsg();
            return;
        } else
        if( scope.$modelValue.nodes && scope.$modelValue.nodes.length > 0 )
        {
            $scope.msg = {type:"danger", text: $translate("layer-group-view.Is-not-possible-to-remove-layers-groups-that-have-children"), dismiss:true};
            $scope.fadeMsg();
            return;
        }

        else
        {
            var dialog = $modal.open( {
                templateUrl: "static/libs/eits-directives/dialog/dialog-template.html",
                controller: DialogController,
                windowClass: 'dialog-delete',
                resolve: {
                    title: function(){return $translate("layer-group-popup.Layer-group-exclusion");},
                    message: function(){return $translate("layer-group-popup.Are-you-sure-you-want-to-delete-the-layer-group") +' "<b>'+scope.$modelValue.name+'</b>"? <br/>' + $translate("layer-group-popup.This-operation-can-not-be-undone");},
                    buttons: function(){return [ {label: $translate("layer-group-popup.Delete") , css:'btn btn-danger'}, {label: $translate("layer-group-popup.Cancel") , css:'btn btn-default', dismiss:true} ];}
                }
            });

            dialog.result.then( function(result) {

            	layerGroupService.removeLayerGroup( scope.$modelValue.id, {
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
     * Adds a new layer group
     */
    $scope.newLayerGroup = function () {

        /**
         * If there are any messages being displayed,
         */
//        if ($scope.msg != null) return;

        if( isNeedSave )
        {
            $scope.msg = null;
            $scope.msg = {type:"danger", text: $translate("layer-group-view.Please-save-the-ordination-before-creating-or-editing-a-group"), dismiss:true};
            $scope.fadeMsg();
            return;
        }

        var dialog = $modal.open( {
            keyboard: false,
            templateUrl: 'modules/admin/ui/layer-group/popup/layer-group-popup.jsp',
            controller: LayerGroupPopUpController,
            scope: $scope,
            resolve: {
            	layerGroups: function(){return getRootNodesScope().$nodesScope.$modelValue;},
                item: function(){return null;}
            }
        });

        dialog.result.then( function(result) 
        {
            if( result == null )
            {
                return;
            }
            
            layerGroupService.insertLayerGroup( result, {
                callback : function(result) {
                    result.nodes = [];
                    
                    getRootNodesScope().$nodesScope.$modelValue.unshift(result);
                    
                    layerGroupService.saveAllParentLayerGroup( getRootNodesScope().$nodesScope.$modelValue, {
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
     * Edit name of an item from the tree
     * @param scope
     */
    $scope.editItem = function(scope) {

        var dialog = $modal.open({
            keyboard: false,
            templateUrl: 'modules/admin/ui/layer-group/popup/layer-group-popup.jsp',
            controller: LayerGroupPopUpController,
            scope: $scope,
            resolve: {
            	layerGroups: function(){return scope.$parentNodesScope.$modelValue;},
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

            scope.$modelValue.name = result.name;

            layerGroupService.updateLayerGroup( scope.$modelValue, {
                callback : function(result) {
                    result.nodes = [];

                    layerGroupService.saveAllParentLayerGroup( getRootNodesScope().$nodesScope.$modelValue, {
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
     * Function to show the tree if it has items
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
     * Function to control the actions of the tree, 
     * as the action to accept the drop, 
  	 * function to control the time before the drop and the time that is done drop the item
     * 
     * @type {{accept: accept, beforeDrop: beforeDrop, dropped: dropped}}
     */
    $scope.treeOptions = {

        accept: function(sourceNode, destNodes, destIndex) {

        	for (var i = 0; i < destNodes.$modelValue.length; i++)
            {
                if( ( destNodes.$modelValue[i].nodes != null && sourceNode.$modelValue.nodes == null ) || ( sourceNode.$modelValue.nodes != null && destNodes.$modelValue[0].nodes == null ))
                {
                    //If the user does not have permission not appear nor the possibility to drag an ite
                    return false;
                }
            }
            return true;
        },
        beforeDrop: function(event) {
        	
        	//If we have a message popping up, it will be removed.
        	$scope.msg = null;
        	
            var sourceNode = event.source.nodeScope;
            var destNodes = event.dest.nodesScope;

            for( var i= 0; i < destNodes.$modelValue.length; i++)
            {
                if( !destNodes.$modelValue[i].nodes && destNodes.$modelValue[i].name == sourceNode.$modelValue.name && destNodes.$modelValue[i].id != sourceNode.$modelValue.id )
                {
                    $scope.msg = {type:"danger", text: $translate("layer-group-popup.Already-have-a-group-with-this-name-at-the-same-level"), dismiss:true};
                    $scope.fadeMsg();
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
                sourceNode.$modelValue.layerGroupUpper = null;
            }
            else
            {
                if( destNodes.$nodeScope )
                {
                    sourceNode.$modelValue.layerGroupUpper = destNodes.$nodeScope.$modelValue;
                }

                if( sourceNode.$modelValue.nodes == null )
                {
                    if ( destNodes.$nodeScope.$modelValue.layers == null )
                    {
                        destNodes.$nodeScope.$modelValue.layers = new Array();
                    }

                    if( destNodes.$nodeScope.$modelValue.layers.length == 0 )
                    {
                        destNodes.$nodeScope.$modelValue.layers.push( sourceNode.$modelValue );
                        destNodes.$nodeScope.$modelValue.layersGroup = null;
                    }
                    else if( destNodes.$nodeScope.$modelValue.layers.length > 0 )
                    {
                        for ( var i = 0; i < destNodes.$nodeScope.$modelValue.layers.length; i++ )
                        {
                            if( destNodes.$nodeScope.$modelValue.layers[i].id == sourceNode.$modelValue.id )
                            {
                                return;
                            }
                        }
                        destNodes.$nodeScope.$modelValue.layers.push( sourceNode.$modelValue );
                    }
                }
                else
                {
                    if ( destNodes.$nodeScope.$modelValue.layersGroup == null )
                    {
                        destNodes.$nodeScope.$modelValue.layersGroup = new Array();
                    }

                    if( destNodes.$nodeScope.$modelValue.layersGroup.length == 0 )
                    {
                        destNodes.$nodeScope.$modelValue.layersGroup.push( sourceNode.$modelValue );
                        destNodes.$nodeScope.$modelValue.layers = null;
                    }
                    else if( destNodes.$nodeScope.$modelValue.layersGroup.length > 0 )
                    {
                        for ( var i = 0; i < destNodes.$nodeScope.$modelValue.layersGroup.length; i++ )
                        {
                            if( destNodes.$nodeScope.$modelValue.layersGroup[i].id == sourceNode.$modelValue.id )
                            {
                                return;
                            }
                        }
                        destNodes.$nodeScope.$modelValue.layersGroup.push( sourceNode.$modelValue );
                    }
                }
            }
        }
    };

    /**
     * Returns the complete list of tree
     */
    var getRootNodesScope = function() 
    {
        return angular.element(document.getElementById("tree-root")).scope();
    };

    $scope.fadeMsg = function(){
		$("div.msg").show();
		
	  	setTimeout(function(){
	  		$("div.msg").fadeOut();
	  	}, 3000);
	}
			
	
    
};

