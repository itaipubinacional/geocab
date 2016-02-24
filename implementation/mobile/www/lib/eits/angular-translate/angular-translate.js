/**
 *
 */
(function(window, angular, undefined) {
	'use strict';

	var translateModule = angular.module('eits-angular-translate', []);

	/**
	 *
	 */
	translateModule.provider('$translate', function() {

		//Attributes
		/**
		 *
		 */
		this.bundles = null;
		/**
		 *
		 */
		// this.url = "./bundles"; TODO
		this.url = "http://geocab.sbox.me/bundles";

		//Methods
		/**
		 *
		 */
		this.format = function (str, args) {
			return str.replace(new RegExp("{-?[0-9]+}", "g"), function(item) {
				var intVal = parseInt(item.substring(1, item.length - 1));
				var replace;
				if (intVal >= 0) {
					replace = args[intVal];
				} else if (intVal === -1) {
					replace = "{";
				} else if (intVal === -2) {
					replace = "}";
				} else {
					replace = "";
				}
				return replace;
			});
		};

		/**
		 *
		 */
		this.loadBundles = function() {

			console.log("Loading and caching bundles from URL: ", this.url);

	    	var options = {
				dataType: "json",
			    cache: true,
			    async: false,
			    url: this.url
			};

	    	var $self = this;
	    	$.ajax( options )
	    		.done(function( data ) {
	    			console.log( "Bundles loaded.");
	    			$self.bundles = data;
	    		})
	    		.fail( function( jqXHR, textStatus, exception ) {
	    			console.error("Error loading bundles.", "Reason: "+exception );
	    		}
	    	);
		}

		/**
		 * Provider get
		 */
	    this.$get = function( $rootScope ) {

				this.loadBundles(); //TODO Verificar a chamada

	    	if ( this.bundles == null ) throw new Error("Problems loading bundles. Please check the URL.");

	    	var $self = this;
	    	return function( code, args ) {
	    		Array.prototype.shift.apply(arguments); //shift(), removes the first element from the arguments

	    		if ( args ) {
	    			return $self.format($self.bundles[code], arguments);
	    		}
	    		else {
	    			return $self.bundles[code];
	    		}
	    	};
	    };

		//Getters and Setters
	    /**
	     *
	     */
	    this.useURL = function(url) {
	    	this.url = url;
	    	this.loadBundles();
	    };
	});

})(window, window.angular);
