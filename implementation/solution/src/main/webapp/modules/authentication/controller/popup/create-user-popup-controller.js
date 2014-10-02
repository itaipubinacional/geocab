'use strict';

/**
 *
 * @param $scope
 * @param $modalInstance
 * @constructor
 */
function CreateUserPopUpController( $scope, $modalInstance, $state, $importService, $translate ) {


	$importService("loginService");
	
    $scope.msg = null;

    /*-------------------------------------------------------------------
     * 		 				 	ATTRIBUTES
     *-------------------------------------------------------------------*/

    /**
     *
     */
    $scope.currentEntity;

    


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
    	$scope.currentEntity = new User();
    };

    /*-------------------------------------------------------------------
     * 		 				 	  BEHAVIORS
     *-------------------------------------------------------------------*/

    $scope.createAccount = function(){
    	
    	if($scope.currentEntity.name === null){
    		$scope.msg = {type:"danger", text: $translate("admin.users.Field-name-is-required") + '!', dismiss:true};
    		$scope.apply();   
    		return;
    	} 
    	
    	if($scope.currentEntity.email === null){
    		$scope.msg = {type:"danger", text:  $translate("admin.users.Field-email-is-required") + '!', dismiss:true};
    		$scope.apply(); 
    		return;
    	}
    	
    	
    	if($scope.currentEntity.password === null){
    		$scope.msg = {type:"danger", text: $translate("admin.users.Field-password-is-required") + '!', dismiss:true};
    		$scope.apply();   
    		return;
    	} 
    	
    	if($scope.currentEntity.confirmPassword === null){
    		$scope.msg = {type:"danger", text: $translate("admin.users.Field-confirm-password-is-required") + '!', dismiss:true};
    		$scope.apply();
    		return;
    	} 
    	
    	
//    	if($scope.currentEntity.password !== $scope.currentEntity.confirmPassword){
//    		$scope.msg = {type:"danger", text: "As senhas precisam ser iguais" + '!', dismiss:true};
//    		$scope.apply();
//    		return;
//    	} 
    	
    	
//    	if ( !$scope.form.$valid ) {    		
//			$scope.msg = {type:"danger", text: "hu3hu3uh" , dismiss:true};
//			$scope.$apply();
//			return;
//		}
    	
    	console.log($scope.currentEntity.confirmPassword);

    	loginService.insertUser( $scope.currentEntity, {
			callback : function() {
				$scope.msg = {type:"success", text: $translate('admin.users.User-successfully-inserted') + '!', dismiss:true};
				$scope.$apply();
			},
			errorHandler : function(message, exception) {
				$scope.msg = {type:"danger", text: message, dismiss:true};
				$scope.$apply();
			}
		});
    }

    /**
     * Sai da popup
     */
    $scope.fechaPopup = function ()
    {
        $modalInstance.close($scope.currentEntity);
    };

//    $scope.form = function( formName )
//    {
//
//        if ( !formName )
//        {
//            formName = "form";
//        }
//
//        return $("form[name="+formName+"]").scope()[formName];
//    };
    
    /**
     *
     */
    $scope.close = function( fechar )
    {

    	$state.go("authentication");
    	$modalInstance.close();
        /*if (fechar)
        {
            $modalInstance.close();

        } else
        {
            var selectedItems = $scope.gridOptions.selectedItems;

            for(var i=0; i< selectedItems.length; i++)
            {
                if( selectedItems[i].ordem == null )
                {
                    selectedItems[i].ordem = camposCamadaExistentes.length;
                    camposCamadaExistentes.push(selectedItems[i])
                }
            }

            selectedItems.sort(function(a, b)
            {
                return a.ordem - b.ordem;
            });

            $modalInstance.close(selectedItems);
        }*/

    };
};