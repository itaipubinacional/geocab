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
		this.url = "./broker";
		/**
		 * 
		 */
		this.interfaceUrl = this.url+"/interface";
		
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
	    this.$get = function() {
	    	var $self = this;
	    	
	    	//Quando carregado o script, é aplicado no objeto window (window[service]).
	    	return function( service ) {
	    		$self.options.url = $self.interfaceUrl + "/"+service+".js";
		    	
		    	//Carrega dinamicamente o script
		    	$.ajax( $self.options )
		    		.fail( function( jqXHR, textStatus, exception ) {
		    			console.error("Error loading broker service: "+service, "Reason: "+exception );
		    		}
		    	);
		    	
		    	//Atribui o path do broker
		    	setTimeout(function() {//setTimeout para evitar problemas de mobile
		    		window[service]._path = $self.url;
		    	}, 300);
		    	
		    	//Retorna a instancia para quem solicitou (via DI)
		    	return window[service];
	    	};
	    };
	    
	    /**
	     * 
	     */
	    this.setBrokerURL = function(url) {
	    	this.url = url;
	    	this.interfaceUrl = this.url+"/interface";
	    	
	    	//http://directwebremoting.org/dwr/documentation/browser/xdomain.html
	    	window.pathToDwrServlet = this.url;
	    };
	});
	
})(window, window.angular);