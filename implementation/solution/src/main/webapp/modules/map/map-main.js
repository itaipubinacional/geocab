(function(window, angular, undefined) {
	"use strict";
			
	//Start the AngularJS
	var projectModule = angular.module("map", ["ui.bootstrap", "ui.router", "ngGrid", "eits-broker"]);
	
	projectModule.config( function( $stateProvider , $urlRouterProvider, $importServiceProvider ) {
		//-------
		//Broker configuration
		//-------
		$importServiceProvider.setBrokerURL("broker/interface");
		
		//-------
		//URL Router
		//-------
		
		//HOME
        $urlRouterProvider.otherwise("/");
        
        //------
        //Scheduler
        //------    
        //Resource Sheet
        //------
        $stateProvider.state('home', {
        	url : "/",
			template: '<h2>Body</h2>',
			controller : function ( $state, $importService ){
				console.log( $importService("accountService") );
			}
        });
	});
	
	projectModule.run( function( $rootScope, $state, $stateParams ) {
		$rootScope.$state = $state;
	    $rootScope.$stateParams = $stateParams;
	});
	
	/**
	 * 
	 */
	angular.element(document).ready( function() {
		angular.bootstrap(document, ['map']);
	});
	
})(window, window.angular);