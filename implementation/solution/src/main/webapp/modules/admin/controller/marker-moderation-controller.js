'use strict';

/**
 *
 * @param $scope
 * @param $log
 * @param $location
 */
function MarkerModerationController($scope, $injector, $log, $state, $timeout, $modal, $location, $importService, $translate) {

	/**
     * Inject the methods, attributes and its states inherited from AbstractCRUDController.
     * @see AbstractCRUDController
     */
    $injector.invoke(AbstractCRUDController, this, {$scope: $scope});
    
    $importService("markerModerationService");
    $importService("layerGroupService");
    $importService("markerService");
    $importService("accountService");
    $importService("motiveService");

	 /*-------------------------------------------------------------------
	  * 		 				 	CONSTANTS
	  *-------------------------------------------------------------------*/
     /**
      * Accept
      */
     $scope.ACCEPTED = "ACCEPTED";
     
     /**
      * Refused
      */
     $scope.REFUSED = "REFUSED";
     
     /**
      * Pending
      */
     $scope.PENDING = "PENDING";
     
    /*-------------------------------------------------------------------
     * 		 				 	ATTRIBUTES
     *-------------------------------------------------------------------*/
    //STATES
    /**
     * Static variable that represents
     * the State records list.
     */
    $scope.LIST_STATE = "marker-moderation.list";
    /**
     * Static variable that represents
     * detail of a State record.
     */
    $scope.DETAIL_STATE = "marker-moderation.detail";
    /**
     * Static variable that represents
     * the State for the creation of records.
     */
    $scope.INSERT_STATE = "marker-moderation.create";
    /**
     * Static variable that represents
     * the rule for editing records.
     */
    $scope.UPDATE_STATE = "marker-moderation.update";
    /**
     * Static variable that represents
     * the rule for editing records.
     */
    $scope.HISTORY_STATE = "marker-moderation.history";
    
    /**
     * Variable that stores the current state of the screen.
     * This variable shall ALWAYS conform to the URL
     * that is in the browser.
     */
    $scope.currentState;
    
    /**
     * Stores the current entity for editing or detail.
     */
    $scope.currentEntity;
    
    /**
     * visible
     */
    $scope.visible = false;
    
    /**
     * selected motive
     */
    $scope.selectedMotive;
    
    /**
     * filter
     */
    $scope.filter = {
    		'layer': null,
    		'status': null,
    		'dateStart': null,
    		'dateEnd': null,
    		'user': null
    };
    
    //FORM
    /**     
     * Variable that stores the query filter
     * @filter - query filter
     */
    $scope.data = {
        filter : null,
        allStatus: [],
        status: null,
        user: null
        
    };
    
    /**
     * select Marker tool
     * */
    $scope.selectMarkerTool = false;
    
    /**
     * selected features
     * */
    $scope.selectedFeatures = [];
    
    /**
     * All Features
     */
    $scope.features = [];
    
    /**
     * Responsible for controlling variable if the functionalities are active or not
     */
    $scope.menu = {
        selectMarker: false
    };
    
    /**
     * Markers Moderation
     */
    $scope.markersModeration = [];
    
    $scope.selectLayerGroup = [];
    
    /**
     * checks whether any research has been done
     */
    $scope.hasSearch = false;
    
  //DATA GRID
    /**
     * Static variable coms stock grid buttons
     * The Edit button navigates via URL (sref) why editing is done in another page,
     * Since the delete button calls a method directly via ng-click why does not have a specific screen state.
     */
    var GRID_ACTION_BUTTONS = '<div class="cell-centered button-action">' +
        '<a ng-click="changeToDetail(row.entity)" title="'+ $translate("admin.layer-config.Update") +'" class="btn btn-mini"><i style="color: #333; font-size: 18px" class="glyphicon glyphicon-eye-open"></i></a>' +
        '</div>';
    
    var IMAGE_MODERATION = '<div  class="cell-centered">' +
    '<a ng-if="row.entity.status == \'PENDING\' " class="icon-waiting-moderation"></a>'+
    '<a ng-if="row.entity.status == \'ACCEPTED\' " class="icon-accept-moderation"></a>'+
    '<a ng-if="row.entity.status == \'REFUSED\' " class="icon-refuse-moderation"></a>'+
    '</div>';
    
    $scope.gridOptions = {
			data: 'currentPage.content',
			multiSelect: false,
			useExternalSorting: true,
            headerRowHeight: 45,
            rowHeight: 45,
			beforeSelectionChange: function (row, event) {
				 
				//avoids call a selection , when clicked in a action button.
				if ( $(event.target).is("a") || $(event.target).is("i") ) return false;
				
				$scope.clearFeatures();
				
				if(row.selected) {
					$scope.gridOptions.selectRow(row.rowIndex, false);
					
					return false;
				} else {
					
					$scope.gridOptions.selectRow(row.rowIndex, true);
				}
				
				
				angular.forEach($scope.features, function(feature, index){
					var geometry = new ol.format.WKT().readGeometry(row.entity.location.coordinateString);
					if(ol.extent.equals(feature.extent, geometry.getExtent())){
						var marker = feature.feature.getProperties().marker;
						$scope.selectMarker(marker);
						
						 var pan = ol.animation.pan({
							    duration: 500,
							    source: /** @type {ol.Coordinate} */ ($scope.view.getCenter())
							  });
							  $scope.map.beforeRender(pan);
						
						$scope.view.setCenter(geometry.getCoordinates());
						
						angular.forEach($scope.selectedFeatures, function(selected, index){
							if(selected.marker.id == marker.id){
								selected.feature.push(feature.feature);
							}
						});
						
					}
				});
				
			},
			columnDefs: [
			             {displayName: $translate('admin.marker-moderation.Layer'), field:'layer.title'}, 
			             {displayName: $translate('admin.marker-moderation.Situation'), cellTemplate: IMAGE_MODERATION},
			             {displayName: $translate('Actions'), sortable:false, cellTemplate: GRID_ACTION_BUTTONS, width:'100px'}            
			             ]
	};
    
    /**
     * The configuration view of the map
     */
    $scope.view = new ol.View({
        center: ol.proj.transform([-54.1394, -24.7568], 'EPSG:4326', 'EPSG:3857'),
        zoom: 9,
        minZoom: 3
    });
    
    /*-------------------------------------------------------------------
     * 		 				 	  NAVIGATIONS
     *-------------------------------------------------------------------*/
    /**
     * Main method that makes the role of front-controller of the screen.
	 * He is invoked whenever there is a change of URL (@see $stateChangeSuccess),
	 * When this occurs, gets the State via the $state and calls the initial method of that State.
	 * Ex.: /list -> changeToList()
	 *      /create -> changeToInsert()
	 *
	 * If the State is not found, he directs to the listing,
	 * Although the front controller of angle won't let enter an invalid URL.
     */
    $scope.initialize = function (toState, toParams, fromState, fromParams) {
        
        /**
         * It is necessary to remove the sortInfo attribute because the return of an edition was doubling the value of the same with the Sort attribute
         * preventing the ordinations in the columns of the grid.
         */
    	
        $log.info("Starting the front controller.");

        $scope.changeToList();
         
        $scope.loadMap();
        
    };
  
    /**
     * Performs initial procedures (prepares the State)
     * for the query screen and after that, change the State to list.
     * @see LIST_STATE
     * @see $stateChangeSuccess
     *
     * To change to this State, one must first load the data from the query.
     */
    $scope.changeToList = function (markers) { 
        $log.info("changeToList");

        $scope.currentState = $scope.LIST_STATE;
        
        $scope.listAllInternalLayerGroups();
        $scope.listAllUsers();

        var pageRequest = new PageRequest();
        pageRequest.size = 10;
        $scope.pageRequest = pageRequest;        
        
        if(typeof markers == 'undefined'){
        	$scope.listMarkerByFilters(null, null, null, null, null, pageRequest);
        	$scope.listMarkerByFiltersMap(null, null, null, null);
        } else if( typeof markers.content != 'undefined' ) {
        	
        	var markersId = [];

            for ( var k = 0; k < markers.content.length; k++ ){
            	markersId.push(markers.content[k].id);
            }
        	
        	$scope.listMarkerByMarkers(markersId, pageRequest);
     
      		
        } else {
        	$scope.listMarkerByMarkers(markers, pageRequest);
        }
            
    };

    /**
     * Performs initial procedures (prepares the State)
     * for screen and after that, change the State to insert.
     * @see INSERT_STATE
     * @see $stateChangeSuccess
     *
     * To change to this State, you must first instantiate a new currentEntity,
     * to clear fields and configure defaults values.
     */
    $scope.changeToInsert = function () {
        $log.info("changeToInsert");

               
        $scope.currentState = $scope.INSERT_STATE;
    };

    /**
     * Performs initial procedures (prepares the State)
     * to the Edit screen and after that, change the State to update.
     * @see UPDATE_STATE
     * @see $stateChangeSuccess
     *
     * To change to this State, must first obtain via id
     * the query service record and only then change the State of the screen.
     */
    $scope.changeToUpdate = function (id) {

        $log.info("changeToUpdate", id);

        $scope.currentState = $scope.UPDATE_STATE;
    };

    /**
     * Performs initial procedures (prepares the State)
     * to the detail screen and after that, change the State to detail.
     * @see DETAIL_STATE
     * @see $stateChangeSuccess
     *
     * To change to this State, must first obtain via id
     * the updated record query service, and then change the State of the screen.
     * If the modifier is not valid, returns to the State of the listing.
     */
    $scope.changeToDetail = function (marker) {
        $log.info("changeToDetail", marker);
        
        $scope.drag = false;
        $scope.clearFeatures();
                
        var geometry = new ol.format.WKT().readGeometry(marker.location.coordinateString);
        
        $scope.map.getView().fitExtent(geometry.getExtent(), $scope.map.getSize());
        
        $scope.map.getView().setZoom(14);
        
        $scope.selectMarker(marker);
        
        angular.forEach($scope.features, function(feature, index){
        	var marker = feature.feature.getProperties().marker;
        	
        	if(ol.extent.equals(feature.extent, geometry.getExtent())){
				
				angular.forEach($scope.selectedFeatures, function(selected, index){
					if(selected.marker.id == marker.id){
						
						selected.feature.push(feature.feature);
					}
				});
				
				return false;
			}
		});
        
        $scope.selectMarker(marker);
		
		$scope.currentState = $scope.DETAIL_STATE;
        $scope.currentEntity = marker;
        
        $scope.listAttributesByMarker();

        
    };

    /**
     * Performs initial procedures (prepares the State)
     * for the delete screen.
     *
     * Before deleting the user notified for confirmation
     * and just so the record is deleted.
     * After deleted, update the grid with filter State, paging and sorting.
     */
    $scope.changeToRemove = function (layer) {
        $log.info("changeToRemove");

    };
    
    $scope.changeToHistory = function () {
        $log.info("changeToHistory");
        
        var pageRequest = new PageRequest();
        
        $scope.listMarkerModerationByMarker($scope.currentEntity.id, pageRequest);
        
    };
    
    /**
	 * Sets the pageRequest as visual pager component	 
	 * and call the list services, considering the current screen filter
	 *
	 * @see currentPage
	 * @see data.filter
	 */
	$scope.changeToPage = function( filter, pageNumber ) {
		$scope.currentPage.pageable.page = pageNumber-1;		
		$scope.listMarkerByFilters(null, null, null, null, null, $scope.currentPage.pageable);
				
	};
    
    /*-------------------------------------------------------------------
	 * 		 				 	  BEHAVIORS
	 *-------------------------------------------------------------------*/

    /**     
     * List all the internal layers
     */
    $scope.listAllInternalLayerGroups = function( filter ) {
 		   
 		   var pageRequest = new PageRequest();
 		   pageRequest.size = 8;

 		   var page = layerGroupService.listLayersByFilters( filter, pageRequest, {
 			   errorHandler : function(message, exception) {
 				   $scope.message = {type:"error", text: message};
 				   $scope.$apply();
 			   },
 			   async:false //USE ONLY IN AUTOCOMPLETE
 		   });
 		   
 		   //Simula um timeout para a exibição dos dados com 0 de delay.
 		   return $timeout( function () { return page ? page.content : []; }, 0);
 	    	
 	   
    	layerGroupService.listAllInternalLayerGroups({
     		callback : function(result) {
                 $scope.selectLayerGroup = [];
                 
                 angular.forEach(result, function(layer,index){
                 	
                 	$scope.selectLayerGroup.push({
                 		"layerTitle": layer.title,
                 		"layerId": layer.id,
                 		"layerIcon": layer.icon,
                 		"group": layer.layerGroup.name
                 	});
                 	
                 });
                 
                 $scope.$apply();
             },
             errorHandler : function(message, exception) {
                 $scope.message = {type:"error", text: message};
                 $scope.$apply();
             }
     	});
    	
    };

    /**     
     * List all the Users
     */
    $scope.listAllUsers = function(){
    	
    	accountService.listAllUsers({
    		callback : function(result){
    			$scope.selectUsers = [];
    			
    			angular.forEach(result, function(user,index){
                 	
                 	$scope.selectUsers.push({
                 		"name": user.name,
                 		"email": user.email,
                 		"userName": user.username,
                 	});
                 	
                 });
    			
                 $scope.apply();   
    		},
    		errorHandler : function(message, exception) {
                $scope.message = {type:"error", text: message};
                $scope.$apply();
            }
    	});
    	
    };
    
    /**
	 * Performs the query logs, considering filter, paging and sorting. 
	 * When ok, change the state of the screen to list.
	 * 
	 * @see data.filter
	 * @see currentPage
	 */
	$scope.listMarkerByFiltersMap = function( layer, status, dateStart, dateEnd, user ) {
		
		markerService.listMarkerByFiltersMap( layer, status, dateStart, dateEnd, user, {
			callback : function(result) {
				if($scope.features.length) {
					$scope.clearFeatures();
					$scope.removeLayers();
				}
				var markers = { 'content' : null };
				markers.content = result;
				$scope.buildVectorMarker(markers);			
				$scope.$apply();
			},
			errorHandler : function(message, exception) {
				$scope.msg = {type:"danger", text: message, dismiss:true};
				$scope.fadeMsg();
				$scope.$apply();
			}
		});
	};
    
	/**
	 * Performs the query logs, considering filter, paging and sorting. 
	 * When ok, change the state of the screen to list.
	 * 
	 * @see data.filter
	 * @see currentPage
	 */
	$scope.listMarkerByFilters = function( layer, status, dateStart, dateEnd, user, pageRequest ) {
		
		markerService.listMarkerByFilters( layer, status, dateStart, dateEnd, user, pageRequest, {
			callback : function(result) {
				
				$scope.currentPage = result;				
				$scope.currentPage.pageable.pageNumber++;
				$scope.currentState = $scope.LIST_STATE;
				$scope.$apply();
			},
			errorHandler : function(message, exception) {
				$scope.msg = {type:"danger", text: message, dismiss:true};
				$scope.fadeMsg();
				$scope.$apply();
			}
		});
	};
	
	/**
	 * Performs the query logs, considering filter, paging and sorting. 
	 * When ok, change the state of the screen to list.
	 * 
	 * @see data.filter
	 * @see currentPage
	 */
	$scope.listMarkerModerationByMarker = function( markerId, pageRequest ) {

		markerModerationService.listMarkerModerationByMarker( markerId, pageRequest, {
			callback : function(result) {
				$scope.markersModeration = result.content;
				$scope.currentState = $scope.HISTORY_STATE;
				$scope.$apply();
			},
			errorHandler : function(message, exception) {
				$scope.msg = {type:"danger", text: message, dismiss:true};
				$scope.fadeMsg();
				$scope.$apply();
			}
		});
	};
	
  /**
	* Calls the dialog to accept a marker
	*/
   $scope.approveMarker = function() {
   	
   	var dialog = $modal.open({
           templateUrl: "static/libs/eits-directives/dialog/dialog-template.html",
           controller: DialogController,
           windowClass: 'dialog-success',
           resolve: {
               title: function () {
                   return $translate('admin.marker-moderation.Confirm-approve');
               },
               message: function () {
                   return $translate('admin.marker-moderation.Are-you-sure-you-want-to-approve-this-marker')+' ? <br/>.';
               },
               buttons: function () {
                   return [
                       {label: $translate('admin.marker-moderation.Approve'), css: 'btn btn-success'},
                       {label: 'Cancelar', css: 'btn btn-default', dismiss: true}
                   ];
               }
           }
       });
   	
   	 dialog.result.then(function () {
   		 $scope.acceptMarkerModeration($scope.currentEntity.id);
     });
   	
   };
   
   /**
	* Update the status of the marker in the listView
	*/
   $scope.updateStatus = function() {
	   
	   for ( var k = 0; k < $scope.currentPage.content.length; k++ ){
		   if ( $scope.currentPage.content[k].id == $scope.currentEntity.id ){
			   $scope.currentPage.content[k] = $scope.currentEntity;
			   return;
		   }
	   } 
   }

   /*
	 * List motives of marker moderation
	 */
	$scope.listMotivesByMarkerModeration = function( markerModerationId ) {
		markerModerationService.listMotivesByMarkerModerationId( markerModerationId, {
			callback : function(result) {
				$scope.motiveMarkerModeration = result;
				$scope.$apply();
			},
			errorHandler : function(message, exception) {
				$scope.msg = {type:"danger", text: message, dismiss:true};
				$scope.fadeMsg();
				$scope.$apply();
			}
		});
	};
	
	$scope.refreshMap = function(markers){
		
		if($scope.features.length) {
			$scope.clearFeatures();
			$scope.removeLayers();
		}
		
		
		if ( $scope.hasSearch ){
			//if it was done some search, return the searched markers on the map
			$scope.buildVectorMarker(markers);
		}else {
			//else return all the markers
			$scope.listMarkerByFiltersMap(null, null, null, null);
		}
		
	}
	
	/**
	 * Accept status marker moderation
	 */
	$scope.acceptMarkerModeration = function( id ) {
		
		markerModerationService.acceptMarker( id, {
         callback : function(result) {
            console.log(result);
            $scope.currentEntity = result.marker;
      		$scope.updateStatus();
      		$scope.changeToList($scope.currentPage);
      		$scope.msg = {type:"success", text: $translate('admin.marker-moderation.Marker-successfully-approved') , dismiss:true};
      		$scope.$apply();
         },
         errorHandler : function(message, exception) {
             $scope.message = {type:"error", text: message};
             $scope.$apply();
         }
     });
	};
	
	/**
	 * Refuse status marker moderation
	 */
	$scope.refuseMarkerModeration = function( id, motive, description ) {
		
		markerModerationService.refuseMarker( id, motive, description, {
         callback : function(result) {
            console.log(result);
            $scope.currentEntity = result.marker;       
            $scope.updateStatus();
      		$scope.changeToList($scope.currentPage);
      		$scope.msg = {type:"danger", text: $translate('admin.marker-moderation.Marker-successfully-refused') , dismiss:true};
            $scope.$apply();
         },
         errorHandler : function(message, exception) {
             $scope.message = {type:"error", text: message};
             $scope.$apply();
         }
     });
	};

	
	/**
	 * Performs the query logs, considering filter, paging and sorting. 
	 * When ok, change the state of the screen to list.
	 * 
	 * @see data.filter
	 * @see currentPage
	 */
	$scope.listMarkerByMarkers = function( markers, pageRequest ) {

		markerService.listMarkerByMarkers( markers, pageRequest, {
			callback : function(result) {	
				if ( !$scope.drag ){
					$scope.refreshMap(result);
				}
				
				if ($scope.hasSearch || $scope.drag){
					$scope.currentPage = result;
					$scope.currentPage.pageable.pageNumber++;
				}

				$scope.currentState = $scope.LIST_STATE;
				$scope.$apply();
			},
			errorHandler : function(message, exception) {
				$scope.msg = {type:"danger", text: message, dismiss:true};
				$scope.fadeMsg();
				$scope.$apply();
			}
		});
	};

    /**
     * Load map
     */
    $scope.loadMap = function(){
    	 /**
         * Openlayers map configuration
         */
        $scope.olMapDiv = document.getElementById('olmap');
        $scope.map = new ol.Map({

            layers: [
                 new ol.layer.Tile({
                   source: new ol.source.OSM()
                 })
               ],
            target: $scope.olMapDiv,
            view: $scope.view
        });
        
        $scope.map.on('click', function(evt) {
        	var feature = $scope.map.forEachFeatureAtPixel(evt.pixel, function(feature, layer) {
 		        return feature;
 		      });
        	
        	if(feature) {
        		var marker = feature.getProperties().marker;
            	
            	$scope.changeToDetail(marker);
            	return false;
        	} else {
        		$scope.clearFeatures();
        	}
        	
    	});
        
        $scope.resolveDatePicker();
    };
    
    /**
     * Resolve date picker
     */
    $scope.resolveDatePicker = function(){
    	$timeout(function(){
			$('.datepicker').datepicker({ 
				dateFormat: 'dd/mm/yy',
				dayNames: ['Domingo','Segunda','Terça','Quarta','Quinta','Sexta','Sábado'],
			    dayNamesMin: ['D','S','T','Q','Q','S','S','D'],
			    dayNamesShort: ['Dom','Seg','Ter','Qua','Qui','Sex','S�b','Dom'],
			    monthNames: ['Janeiro','Fevereiro','Mar�o','Abril','Maio','Junho','Julho','Agosto','Setembro','Outubro','Novembro','Dezembro'],
			    monthNamesShort: ['Jan','Fev','Mar','Abr','Mai','Jun','Jul','Ago','Set','Out','Nov','Dez'],
			    nextText: 'Pr�ximo',
			    prevText: 'Anterior'
			});	

			$('.datepicker').mask("99/99/9999");
		}, 300);
    };
    
    /**
     * Build the vectors in the map
     */
    $scope.buildVectorMarker = function(markers){
    	$scope.drag = false;
    	var coordenates = [];
    	
		angular.forEach(markers.content, function(marker, index){
            
			/**
			 * Verify status
			 * */
			var statusColor = $scope.verifyStatusColor(marker.status);
			
			var dragBox = new ol.interaction.DragBox({
				  condition: function(){
				 	  return $scope.selectMarkerTool;
				  },
				  style: new ol.style.Style({
				    stroke: new ol.style.Stroke({
				      color: [0, 0, 255, 1]
				    })
				  })
			});
			
			dragBox.on('boxend', function(e) {
				
				  var extent = dragBox.getGeometry().getExtent();
				  var markers = [];
				
				  angular.forEach($scope.features, function(feature, index){
					    var marker = feature.feature.getProperties().marker;
					    $scope.selectMarker(marker);
					    
						var extentMarker = feature.extent;
						var feature = feature.feature;
						
						if(ol.extent.containsExtent(extent, extentMarker)){
							markers.push(marker.id);
							
							angular.forEach($scope.selectedFeatures, function(selected, index){
								if(selected.marker.id == marker.id){
									selected.feature.push(feature);
								}
							});
							
						}
				  });
				  
				  if(markers.length)
					  $scope.changeToList(markers);
				  
				  $scope.drag = true;
			});
			
			
			dragBox.on('boxstart', function(e) {
				$scope.clearFeatures();
			});

			$scope.map.addInteraction(dragBox);
			
			var geometry = new ol.format.WKT().readGeometry(marker.location.coordinateString);
		    var feature = new ol.Feature({
               geometry: geometry,
               marker: marker,
            });
          
			var fill = new ol.style.Fill({
				   color: statusColor,
					width: 4.53
				 });
			var stroke = new ol.style.Stroke({
				   color: '#3399CC',
				   width: 1.25
				 });
		   
			var source = new ol.source.Vector({ features: [feature] });
            var layer = new ol.layer.Vector({
               source: source,
               style: new ol.style.Style(
            		   {
        			     image: new ol.style.Circle({
        			       fill: fill,
        			       stroke: stroke,
        			       radius: 10,
        			     }),
        			     fill: fill,
        			     stroke: stroke
        			   }
               ),
               maxResolution: minScaleToMaxResolution(marker.layer.minimumScaleMap),
               minResolution: maxScaleToMinResolution(marker.layer.maximumScaleMap)
            });
            
            source.addFeatures(source);
            
            coordenates.push(geometry.getCoordinates());
            
            $scope.features.push({'feature': feature, "extent": source.getExtent(), 'layer': layer});
            
            $scope.map.addLayer(layer);
		});
		
		var extent = new ol.extent.boundingExtent(coordenates);
		
		$scope.map.getView().fitExtent(extent, $scope.map.getSize());
		
    };
    
     /**
	    Converts the value scale stored in the db to open layes zoom format
	  */
	 var minScaleToMaxResolution = function ( minScaleMap ){
	
	     switch (minScaleMap){
	         case 'UM500km':
	             return 78271.51696402048;
	         case 'UM200km':
	             return 78271.51696402048;
	         case 'UM100km':
	             return 4891.96981025128;
	         case 'UM50km':
	             return 2445.98490512564;
	         case 'UM20km':
	             return 1222.99245256282;
	         case 'UM10km':
	             return 611.49622628141;
	         case 'UM5km':
	             return 152.8740565703525;
	         case 'UM2km':
	             return 76.43702828517625;
	         case 'UM1km':
	             return 38.21851414258813;
	         case 'UM500m':
	             return 19.109257071294063;
	         case 'UM200m':
	             return 9.554628535647032;
	         case 'UM100m':
	             return 4.777314267823516;
	         case 'UM50m':
	             return 2.388657133911758;
	         case 'UM20m':
	             return 1.194328566955879;
	         default :
	             return 78271.51696402048;
	     }
	 };
	
	 /**
	  Converts the value scale stored in the db to open layes zoom forma
	  */
	 var maxScaleToMinResolution = function ( maxScaleMap ){
	
	     switch (maxScaleMap){
	         case 'UM500km':
	             return 19567.87924100512;
	         case 'UM200km':
	             return 4891.96981025128;
	         case 'UM100km':
	             return 2445.98490512564;
	         case 'UM50km':
	             return 1222.99245256282;
	         case 'UM20km':
	             return 305.748113140705;
	         case 'UM10km':
	             return 152.8740565703525;
	         case 'UM5km':
	             return 76.43702828517625;
	         case 'UM2km':
	             return 38.21851414258813;
	         case 'UM1km':
	             return 19.109257071294063;
	         case 'UM500m':
	             return 9.554628535647032;
	         case 'UM200m':
	             return 4.777314267823516;
	         case 'UM100m':
	             return 2.388657133911758;
	         case 'UM50m':
	             return 1.194328566955879;
	         case 'UM20m':
	             return 0.0005831682455839253;
	         default :
	             return 0.0005831682455839253;
	     }
	 };

	 /**
	  * Calls the modal to refuse a marker
	  */
	 $scope.refuseMarker = function() {
		 
		if ( $scope.currentEntity.status != 'REFUSED' ){
			
			var dialog = $modal.open({
	            templateUrl: "modules/admin/ui/marker-moderation/popup/refuse-marker.jsp",
	            controller: RefuseMarkerController,
	            windowClass: 'dialog-delete',
	            resolve: {
	                motive: function () {
	                    return $scope.motive;
	                }
	            }
	        });
	    	
	    	dialog.result.then(function (result) {
	    		
	    		$scope.refuseMarkerModeration($scope.currentEntity.id, result.motive, result.description );
	        });
			
		}
    };
    
	 /**
	  * Calls the dialog to accept a marker
	  */
    $scope.approveMarker = function() {
    	
    	if ($scope.currentEntity.status != 'ACCEPTED') {
    	
    		var dialog = $modal.open({
                templateUrl: "static/libs/eits-directives/dialog/dialog-template.html",
                controller: DialogController,
                windowClass: 'dialog-success',
                resolve: {
                    title: function () {
                        return $translate('admin.marker-moderation.Confirm-approve');
                    },
                    message: function () {
                        return $translate('admin.marker-moderation.Are-you-sure-you-want-to-approve-this-marker')+' ? <br/>.';
                    },
                    buttons: function () {
                        return [
                            {label: $translate('admin.marker-moderation.Approve'), css: 'btn btn-success'},
                            {label: 'Cancelar', css: 'btn btn-default', dismiss: true}
                        ];
                    }
                }
            });
        	
        	 dialog.result.then(function () {

        		 $scope.acceptMarkerModeration($scope.currentEntity.id);
             });
    		
    	}
    	
    };

    
    /**
	  * Lists the marker attributes
	  */
    $scope.listAttributesByMarker = function(){
    	
    	$scope.attributesByLayer = [];
		$scope.showNewAttributes = false;
    	
    	markerService.listAttributeByMarker($scope.currentEntity.id, {
		  callback : function(result) {
			  $scope.attributesByMarker = result;   
			  
			  layerGroupService.listAttributesByLayer($scope.currentEntity.layer.id,{
	          		callback : function(result) {
	          			$scope.attributesByLayer = [];
	          			
	          			angular.forEach(result, function(attribute, index){
		          				
	          					var exist = false;
	          					
	          					angular.forEach($scope.attributesByMarker, function(attributeByMarker, index){
	          					
		          					if(attributeByMarker.attribute.id == attribute.id){
		          						exist = true;
		          					}
		          				});
		          				
		          				if( !exist ) {
		          					$scope.attributesByLayer.push(attribute);
		          					$scope.showNewAttributes = true;
		          				}
		          				
		          			});
	          			
	                      $scope.$apply();
	                  },
	                  errorHandler : function(message, exception) {
	                      $scope.message = {type:"error", text: message};
	                      $scope.$apply();
	                  }
	          	});
			  
			  angular.forEach(result,function(markerAttribute,index){
				if (markerAttribute.attribute.type == "NUMBER") {
					markerAttribute.value = parseInt(markerAttribute.value);
				}  
			  });
			  
			 
			  $scope.$apply();
			 
          },
          errorHandler : function(message, exception) {
              $scope.message = {type:"error", text: message};
              $scope.$apply();
          }
    	});
    	
    	markerService.findImgByMarker($scope.currentEntity.id, {
   		 callback : function(result) {
   			 
   			 $scope.imgResult = result;
             },
             errorHandler : function(message, exception) {
                 $scope.message = {type:"error", text: message};
                 $scope.$apply();
             }
      	});
    	
    };
    
    /**
     * Return the translated status of the marker
     */
    $scope.translateStatus = function(id){
    	
    	if ($scope.markersModeration[id].status == 'PENDING'){
    		return $translate('admin.marker-moderation.PENDING');
    	} 
    	if ($scope.markersModeration[id].status == 'REFUSED'){
    		return $translate('admin.marker-moderation.REFUSED');
    	} 
    	if ($scope.markersModeration[id].status == 'ACCEPTED'){
    		return $translate('admin.marker-moderation.APPROVED');
    	} 
    };
    
      /**
     * Verify status
     */    
    $scope.verifyStatusColor = function(status) {
    	var statusColor;
		if(status == $scope.REFUSED) {
			statusColor = "#ba0000";
		} else if(status == $scope.ACCEPTED) {
			statusColor = "#09ba00";
		} else {
			statusColor = "#edad09";
		}
		return statusColor;
    };
    
    $scope.selectMarker = function(marker){
    	/**
		 * Verify status
		 * */
		var statusColor = $scope.verifyStatusColor(marker.status);
    	
    	var style = new ol.style.Style({
            image: new ol.style.Circle({
              radius: 10,
              fill: new ol.style.Fill({
                color: statusColor
              }),
              stroke: new ol.style.Stroke({
            	color: '#3399CC',
                width: 3.5
              })
            }),
            zIndex: 100000
          });
    	
    	var select = new ol.interaction.Select({style:style});
		$scope.map.addInteraction(select);

		$scope.selectedFeatures.push({'marker': marker, 'feature': select.getFeatures()});
    };
    
    $scope.eventMarkerTool = function(){
    	$scope.selectMarkerTool = $scope.menu.selectMarker = ($scope.selectMarkerTool == true) ? false : true;
    	
    };
   
    /**
     * Function that decreases the zoom map
     */
    $scope.eventDecreaseZoom = function (){
        $scope.map.getView().setZoom($scope.map.getView().getZoom() - 1);
    };

    /**
     * Function that increases the zoom map
     */
    $scope.eventIncreaseZoom = function (){
        $scope.map.getView().setZoom($scope.map.getView().getZoom() + 1);
    };
    
    /**
     * Filter
     */
    $scope.bindFilter = function() {
    	var pageRequest = new PageRequest();
        pageRequest.size = 10;
        $scope.pageRequest = pageRequest;
        
        if($scope.filter.status == "")
        	$scope.filter.status = null;
        if($scope.filter.user != null)
        	var userEmail = $scope.filter.user.email;
        
    	$scope.listMarkerByFilters( $scope.filter.layer, $scope.filter.status, $scope.filter.dateStart, $scope.filter.dateEnd, userEmail, pageRequest );
    	$scope.listMarkerByFiltersMap($scope.filter.layer, $scope.filter.status, $scope.filter.dateStart, $scope.filter.dateEnd, userEmail);
    	$scope.hasSearch = true;
    };
    
    $scope.clearFilters = function(){
    	
    	var pageRequest = new PageRequest();
        pageRequest.size = 10;
        $scope.pageRequest = pageRequest;
        
        $scope.filter.layer = null;
        $scope.filter.status = null;     
        $scope.filter.dateStart= null;
        $scope.filter.dateEnd= null;
        $scope.filter.user= null;
        
        $scope.listMarkerByFilters( null, null, null, null, null, pageRequest );
        $scope.listMarkerByFiltersMap( null, null, null, null, null);
        $scope.hasSearch = false;
    	
    };
    
    $scope.clearFeatures = function(){
    	if($scope.selectedFeatures.length) {
	        angular.forEach($scope.selectedFeatures, function(feature, index){
				feature.feature.clear();
			});
	        angular.forEach($scope.selectedFeatures, function(feature, index){
	        	 $scope.selectedFeatures.splice(index, 1);
			});
	       
        }
    };
    
    $scope.removeLayers = function() {
    	angular.forEach($scope.features, function(feature, index){
    		$scope.map.removeLayer(feature.layer);
    	});
    };
}
