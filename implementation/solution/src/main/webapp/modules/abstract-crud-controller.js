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
	 * Handler that listens to changes in URLs belonging to the State of the screen.
	 * Ex.: list, create, detail, edit
	 * 
	 * Whenever there is a change of URL if via button, manual URL Exchange, or 
	 * the advancing and back browser, this event is called and calls the initilize () that plays the role of front controller.
	 * 
	 */
    $scope.$on('$stateChangeSuccess', function(event, toState, toParams, fromState, fromParams) {
    	$log.info("$stateChangeSuccess");

        $scope.checkAccess();
        $rootScope.menuActive = $state.current.menu;
    	$scope.initialize(toState, toParams, fromState, fromParams);
        if (toState.url != '/detail/:id' && toState.url != "/list") {
            $scope.msg = null;
        }else if( $scope.msg != null && toState.url == "/list" && $scope.msg.type == 'danger'){
            $scope.msg = null;
        };
        
    });
    
    /**
     * Returns the instance of the form in the angular scope.
     * @param formName
     * @returns {*|Function|$scope.form|$scope.form|$scope.form|jQuery}
     */
    $scope.form = function( formName ) {
    	
        if ( !formName ) {
            formName = "form";
        }

        return $("form[name="+formName+"]").scope()[formName];
    };
    
    $scope.detectIE = function() {
        var ua = window.navigator.userAgent;
        var msie = ua.indexOf('MSIE ');
        var trident = ua.indexOf('Trident/');

        if (msie > 0) {
            // IE 10 or older => return version number
            return parseInt(ua.substring(msie + 5, ua.indexOf('.', msie)), 10);
        }

        if (trident > 0) {
            // IE 11 (or newer) => return version number
            var rv = ua.indexOf('rv:');
            return parseInt(ua.substring(rv + 3, ua.indexOf('.', rv)), 10);
        }

        // other browser
        return false;
    }

    /**
     * Remove messages by this method
     */
    $scope.close = function() {
        $scope.msg = null;
    };
    
    /**
     * Here we check if there are no messages to display,
     * If there is we include click event across the screen so that
     * the message is closed by clicking anywhere on the screen.
     */
    $scope.$watch('msg', function( newValue, oldValue ){
    	$timeout(function(){
	    	if( newValue && newValue.text && newValue.text.length > 0 ){
	    		angular.element( $('.htmlMain') ).bind('click', function(){
	    			angular.element( $('.alert') ).scope().$parent.msg = null;
	    			angular.element( $('.htmlMain') ).unbind('click');
	    			$scope.$apply();
	    		});
	    	}
    	});
    });
    
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