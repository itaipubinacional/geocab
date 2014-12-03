'use strict';

/**
 * 
 * @param $scope
 * @param $log
 * @param $location
 */
function SelectLayerGroupPopUpController($scope, layerGroups,currentLayerGroup, $modalInstance, $log, $location, $importService) {

	/*-------------------------------------------------------------------
	 * 		 				 	EVENT HANDLERS
	 *-------------------------------------------------------------------*/

	/*-------------------------------------------------------------------
	 * 		 				 	ATTRIBUTES
	 *-------------------------------------------------------------------*/

	$importService("layerGroupService");
	
	/**
	 * 
	 */
	$scope.msg = null;

	/**
	 * 
	 */
	$scope.currentEntity;

	/**
	 * 
	 */
	$scope.currentPage;

	/**
	 * 
	 */
	$scope.data = {
			filter : null
	};

	/**
	 * 
	 */
	$scope.groups = [];

	/*-------------------------------------------------------------------
	 * 		 				 	  NAVIGATIONS
	 *-------------------------------------------------------------------*/
	/**
	 * Main method that makes the role of front-controller of the screen.
	 * He is invoked whenever there is a change of URL (@see $stateChangeSuccess),
	 * When this occurs, gets the State via the $state and calls the initial method of that State.
	 * Ex.: /list -> changeToList()
	 *      /criar -> changeToInsert()
	 *
	 * If the State is not found, he directs to the listing,
	 * Although the front controller of angle won't let enter an invalid URL.
	 */
	$scope.initialize = function() 
	{

		$scope.listLayerGroups(layerGroups, null);
		
		
		
		if( currentLayerGroup )
		{
			$scope.currentGroup = currentLayerGroup;
			console.log(currentLayerGroup);
		}

	};



	/*-------------------------------------------------------------------
	 * 		 				 	  BEHAVIORS
	 *-------------------------------------------------------------------*/

	/**
	 * Performs the query records, consirando filter, paging and sorting.
	 * When ok, change the State of the screen to list.
	 *
	 * @see data.filter
	 * @see currentPage
	 */
	$scope.listLayerGroupByFilter = function( filtro, pageRequest ) {

		// If the filter field is empty
		filtro = filtro == "" ? null : filtro;

		layerGroupService.listGruposCamadas( filtro, pageRequest, {
			callback : function(result) {
				$scope.currentPage = result;
				$scope.currentPage.pageable.pageNumber++;//Para fazer o bind com o pagination
				$scope.$apply();

			},
			errorHandler : function(message, exception) {
				$scope.message = {type:"danger", text: message};
				$scope.$apply();
			}
		});
	};

	/**
	 * 
	 */
	$scope.my_tree_handler = function(branch) 
	{
		$scope.currentEntity = branch;
	};

	/**
	 * 
	 */
	$scope.listLayerGroups = function(layerGroups, layerGroupUpper)
	{
		for (var i = 0; i < layerGroups.length; i++) 
		{
			var data = new Object();
			data.children = [];

			if( layerGroupUpper == null )
			{
				data.id = layerGroups[i].id;
				data.label = layerGroups[i].name;
				$scope.groups.push(data);
			}
			else
			{
				data.id = layerGroups[i].id;
				data.label = layerGroups[i].name;
				layerGroupUpper.children.push(data);
			}

			$scope.listLayerGroups(layerGroups[i].layersGroup, data);
		}
	};

	/**
	 * 
	 */
	$scope.form = function( formName )
	{

		if ( !formName ) 
		{
			formName = "form";
		}

		return $("form[name="+formName+"]").scope()[formName];
	};

	/**
	 *
	 */
	 $scope.close = function( fechar )
	 {
		$scope.msg = null;

        if (fechar){

            $modalInstance.close(null);

        } else {
            if( $scope.currentEntity )
            {
                if( $scope.currentEntity.children.length > 0 )
                {
                    $modalInstance.close(null);
                }
                else
                {
                    $modalInstance.close($scope.currentEntity);
                }
            }
            else
            {
                $modalInstance.close(null);
            }
        }


	 };
};