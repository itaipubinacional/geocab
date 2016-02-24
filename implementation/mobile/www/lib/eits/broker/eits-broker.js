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
		this.configureServicePath = function( service ) {
			var $self = this;
			
			//setTimeout para evitar problemas de load time no mobile
			setTimeout(function() {
				if ( window[service] ) {
					window[service]._path = this.url;
				//se nao carregou ainda, espera o proximo ciclo
				} else { 
					$self.configureServicePath(service);
				}
	    	});
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
		    	
		    	$self.configureServicePath( service );		    			
		    	
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