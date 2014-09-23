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
	$scope.grupos = [];

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
	 * Realiza a consulta de registros, consirando filtro, paginação e sorting.
	 * Quando ok, muda o estado da tela para list.
	 *
	 * @see data.filter
	 * @see currentPage
	 */
	$scope.listLayerGroupByFilter = function( filtro, pageRequest ) {

		// Caso o campo filtro esteja vazio
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
				data.label = layerGroups[i].nome;
				$scope.grupos.push(data);
			}
			else
			{
				data.id = layerGroups[i].id;
				data.label = layerGroups[i].nome;
				layerGroupUpper.children.push(data);
			}

			$scope.listLayerGroups(layerGroups[i].layerGroups, data);
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