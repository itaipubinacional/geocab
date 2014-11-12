'use strict';

/**
 * 
 * @param $scope
 * @param $log
 * @param $location
 */
function MorePopupController($scope, $injector,$modalInstance, $state, currentEntity, $importService ) {

    $importService("layerGroupService");

	$scope.msg = null;

	/*-------------------------------------------------------------------
	 * 		 				 	ATTRIBUTES
	 *-------------------------------------------------------------------*/

    /*-------------------------------------------------------------------
     * 		 				 	ATTRIBUTES
     *-------------------------------------------------------------------*/
    //STATES
    /**
     *
     */
    $scope.NORMAL_STATE = "grupo-camadas.normal";
    /**
     *
     */
    $scope.CONFIRM_STATE = "grupo-camadas.confirm";

    /**
     *
     */
	$scope.currentEntity;

    /**
     *
     */
    $scope.currentState;

    /**
     *
     * @type {boolean}
     */
	var isEqual = false;

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
		$(document).keyup(function(e) {
			  if (e.keyCode == 27) { 
					$("#preview").remove();
			  }   
		});
		
		layerGroupService.listLayersIcons({
            callback: function (result) {
            
            	$scope.totalLayers = [];
            	
            	angular.forEach(result, function(layer, index){
            		var allowSave = true;
            		switch(layer){
	            		case 'default_yellow.png':
	            			allowSave = false;
	            			break;
	            		case 'default_white.png':
	            			allowSave = false;
	            			break;
	            		case 'default_red.png':
	            			allowSave = false;
	            			break;
	            		case 'default_pink.png':
	            			allowSave = false;
	            			break;
	            		case 'default_green.png':
	            			allowSave = false;
	            			break;
	            		case 'default_blue.png':
	            			allowSave = false;
	            			break;
            		}
            		
            		if(allowSave)
            			$scope.totalLayers.push(layer);
            	});
            	
            	$scope.currentPage = {};
            	$scope.currentPage.total = $scope.totalLayers.length;
            	$scope.currentPage.size = 125;
            	$scope.currentPage.totalPages = Math.floor($scope.currentPage.total / $scope.currentPage.size);
            	$scope.currentPage.pageable = {};
            	$scope.currentPage.pageable.pageNumber = 1;
            	
            	$scope.layerIcons = $scope.totalLayers.slice(0, $scope.currentPage.size);
                $scope.$apply();
                $scope.imagePreview();
            },
            errorHandler: function (message, exception) {
                $scope.msg = {type: "danger", text: message, dismiss: true};
                $scope.$apply();
            }
        });
		
		$scope.currentEntity = currentEntity;
		$scope.currentEntity.iconTemporary = $scope.currentEntity.icon;
	};

	/*-------------------------------------------------------------------
	 * 		 				 	  BEHAVIORS
	 *-------------------------------------------------------------------*/

	$scope.changeToPage = function(filters, page) {
		page = page - 1;
		var offset = page * $scope.currentPage.size;
		$scope.layerIcons = $scope.totalLayers.slice( offset, offset + $scope.currentPage.size );
		$scope.$apply();
		$scope.imagePreview();
	}

	$scope.imagePreview = function(){	
		/* CONFIG */
			
			$scope.xOffset = 10;
			$scope.yOffset = 30;
			
		/* END CONFIG */
		$("img.preview").hover(function(e){
			$("body").append("<p id='preview' style='position: fixed; z-index: 10000000'><img style='width: 70px; height: 70px' src='"+ $(this).attr("src") +"' alt='Image preview' /></p>");								 
			$("#preview")
				.css("top",($(this).position().top + $scope.xOffset) + "px")
				.css("left",(e.pageX - $scope.yOffset) + "px")
				.fadeIn("fast");						
	    },
		function(){
			this.title = '';	
			$("#preview").remove();
	    });	
		$("img.preview").mousemove(function(e){
			$("#preview")
				.css("top",($(this).position().top + $scope.xOffset) + "px")
				.css("left",(e.pageX - $scope.yOffset) + "px");
		});			
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
    $scope.closeConfirm = function()
    {
        $scope.msg = null;
        $scope.currentEntity.nome = '';
        $scope.currentState = $scope.NORMAL_STATE;
    };

    /**
	 *
	 */
	$scope.save = function() 
	{
		$scope.currentEntity.icon = $scope.currentEntity.iconTemporary;
		$scope.msg = null;
		$modalInstance.close(null);
	};
    
	/**
	 *
	 */
	$scope.close = function() 
	{
		$scope.msg = null;
		$modalInstance.close(null);
	};
};