/**
 * 
 */
(function(window, angular, undefined) {
	'use strict';
	
	var brokerModule = angular.module('eits-broker', []);
	
	/**
	 * 
	 */
	brokerModule.provider('$importService', function() {

		/**
		 * 
		 */
		this.url = "./broker/interface";
		
		/**
		 * 
		 */
		this.options = {
			dataType: "script",
		    cache: true,
		    async: false,
		    url: null
		};
			
		/**
		 * 
		 */
	    this.$get = function( $rootScope ) {
	    	var $self = this;
	    	
	    	return function( service ) {
	    		$self.options.url = $self.url + "/"+service+".js";
		    	
		    	//Carrega dinamicamente o script
		    	$.ajax( $self.options )
		    		.fail( function( jqXHR, textStatus, exception ) {
		    			console.error("Error loading broker service: "+service, "Reason: "+exception );
		    		}
		    	);
		    	
		    	//Retorna a instancia para quem solicitou (via DI)
		    	return window[service];//Quando carregado o script, é aplicado no objeto window.
	    	};
	    };
	    
	    /**
	     * 
	     */
	    this.setBrokerURL = function(url) {
	    	this.url = url;
	    };
	});
	
})(window, window.angular);