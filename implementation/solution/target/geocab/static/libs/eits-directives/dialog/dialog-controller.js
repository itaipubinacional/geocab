'use strict';

/**
 * 
 * @param $scope
 * @param $modalInstance
 */
function DialogController($scope, $modalInstance, title, message, buttons) {

	/*-------------------------------------------------------------------
	 * 		 				 	ATTRIBUTES
	 *-------------------------------------------------------------------*/
	/**
	 * 
	 */
	$scope.title = title;
	/**
	 * 
	 */
	$scope.message = message;

	/**
	 * 
	 */
	$scope.buttons = [ {
			label: "Confirmar",
			css: "btn btn-warning"
		}, {
			label: "Cancelar",
			css: "btn",
			dismiss: true
		} 
	];
	
	if ( buttons != null ) {
		
		//Verificarmos se todos os botoes tem style
		angular.forEach(buttons, function(button, key) {
			if ( button.css == null ) {
				button.css = "btn";
			}
		});
		$scope.buttons = buttons;
	}
	
	/*-------------------------------------------------------------------
	 * 		 				 	HANDLERS
	 *-------------------------------------------------------------------*/
	/**
	 * 
	 */
	$scope.clickHandler = function( button ) {
		
		if ( button.dismiss ) {
            $modalInstance.dismiss();
		}
		else {
			$modalInstance.close();
		}
	};
};
