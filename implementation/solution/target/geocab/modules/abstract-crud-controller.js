'use strict';

/**
 * 
 * @param $scope
 * @param $log
 * @param $state
 * @param $timeout
 * @param $dialog
 */
function AbstractCRUDController( $scope, $log, $state, $rootScope, $timeout ) {

    /*-------------------------------------------------------------------
     * 		 				 	ATTRIBUTES
     *-------------------------------------------------------------------*/

    $scope.INVALID_FORM_MESSAGE = "Os campos em destaque são de preenchimento obrigatório.";
    $scope.INVALID_ID_MESSAGE   = "Não foi possível abrir o detalhe do registro. O identificador é inválido.";
    $scope.ACCESS_DENIED        = "Acesso negado ao tentar realizar esta operação. Tente novamente ou contate o administrador do sistema.";

    /*-------------------------------------------------------------------
	 * 		 				 	EVENT HANDLERS
	 *-------------------------------------------------------------------*/
	/**
	 * Handler que escuta as mudanças de URLs pertecentes ao estado da tela.
	 * Ex.: listar, criar, detalhe, editar
	 * 
	 * Toda vez que ocorre uma mudança de URL se via botão, troca de URL manual, ou ainda 
	 * ao vançar e voltar do browser, este evento é chamado e chama o initilize() que faz o papel de front-controller.
	 * 
	 */
    $scope.$on('$stateChangeSuccess', function(event, toState, toParams, fromState, fromParams) {
    	$log.info("$stateChangeSuccess");

        $scope.checkAccess();
        $rootScope.menuActive = $state.current.menu;
    	$scope.initialize(toState, toParams, fromState, fromParams);
        if (toState.url != '/detalhe/:id' && toState.url != "/listar") {
            $scope.msg = null;
        }else if( $scope.msg != null && toState.url == "/listar" && $scope.msg.type == 'danger'){
            $scope.msg = null;
        };
        
    });
    
    /**
     * Retorna a instancia do form no escopo do angular.
     * @param formName
     * @returns {*|Function|$scope.form|$scope.form|$scope.form|jQuery}
     */
    $scope.form = function( formName ) {
    	
        if ( !formName ) {
            formName = "form";
        }

        return $("form[name="+formName+"]").scope()[formName];
    };

    /**
     * Removemos as mensagens por este método
     */
    $scope.close = function() {
        $scope.msg = null;
    };
    
    /**
     * Aqui verificamos se existem mensagens para exibição,
     * Caso houver incluimos evento de clique em toda a tela para que
     * a mensagem seja fechada clicando em qualquer lugar da tela.
     */
    /*$scope.$watch('msg', function( newValue, oldValue ){
    	$timeout(function(){
	    	if( newValue && newValue.text && newValue.text.length > 0 ){
	    		angular.element( $('.htmlMain') ).bind('click', function(){
	    			angular.element( $('.alert') ).scope().$parent.msg = null;
	    			angular.element( $('.htmlMain') ).unbind('click');
	    			$scope.$apply();
	    		});
	    	}
    	});
    });*/
    
    /**
     *
     * @param value
     * @param size
     */
    $scope.substr = function( value, size ){
        if( value.length > size ) {
            return value.substr(0, size) + "...";
        } else {
            return value;
        }

    }

    /**
     *  @param permission
     */
    $scope.checkAccess = function( /*permission, callback*/ ){
        if( arguments.length > 0 ){
            var permission = arguments[0];
            var callback   = arguments[1];

            if( !$rootScope.checkPermission( permission ) ) {
                localStorage.setItem( "error_permission", $scope.ACCESS_DENIED );
                $scope.currentState = 'dashboard';
                $state.go($scope.currentState);
            } else {
                if( callback != undefined && (typeof callback.success) == 'function' ) {
                    callback.success();
                }
            }
        }
        else if( localStorage.getItem( "error_permission" ) ) {
            var msg = localStorage.getItem( "error_permission");
            setTimeout(function(){ $scope.msg = {type:"error", text: msg }; $scope.$apply()  }, 1);
            localStorage.removeItem( "error_permission" );
        }
    }

    /**
     *
     * @param array
     * @param entity
     * @returns {number}
     */
    $scope.findByIdInArray = function(array, entity){
        for (var i = 0; i < array.length; i++) {
            if (array[i].id == entity.id) {
                return i;
            }
        }
        return -1;
    }
}