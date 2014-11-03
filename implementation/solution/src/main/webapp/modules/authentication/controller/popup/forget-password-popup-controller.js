'use strict';

/**
 *
 * @param $scope
 * @param $modalInstance
 * @constructor
 */
function ForgetPasswordPopUpController( $scope, $modalInstance, $state, $importService, $translate, $timeout ) {


	$importService("loginService");
	
    $scope.msg = null;

    /*-------------------------------------------------------------------
     * 		 				 	ATTRIBUTES
     *-------------------------------------------------------------------*/

    /**
     *
     */
    $scope.currentEntity;
    
    $scope.confirmPassword;
    

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
    	$scope.confirmPassword=null;
    };

    /*-------------------------------------------------------------------
     * 		 				 	  BEHAVIORS
     *-------------------------------------------------------------------*/
   
    $scope.forgetPassword = function(){
    	if ( !$scope.form('form_forget_password').$valid ) 
    	{    		    		    	
			$scope.msg = {type:"danger", text: $translate('admin.users.The-highlighted-fields-are-required') , dismiss:true};
			$scope.$apply();
			return;
		} 

    	$scope.msg = {type:"info", text: "Aguarde um momento" + '...', dismiss:true};
    	
    	loginService.recoverPassword($scope.currentEntity, {
            callback: function (result) {
            	$scope.msg = {type:"success", text: "Uma nova senha foi enviada ao e-mail" + '!', dismiss:true};
            	$scope.$apply();
            	$timeout(function(){
            		$scope.fechaPopup();
            	}, 5000);
            },
            errorHandler: function (message, exception) {
                $scope.msg = {type: "danger", text: message, dismiss: true};
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
   
    $scope.closePopUp = function(){
    	$state.go("authentication");
    	$modalInstance.close();
    }
    
};