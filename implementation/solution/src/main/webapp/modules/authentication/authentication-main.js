(function(window, angular, undefined) {
	"use strict";
			
	//Start the AngularJS
	var projectModule = angular.module("authentication", ["ui.bootstrap", "ui.router", "ngGrid", "eits-broker", "eits-angular-translate",'angularBootstrapNavTree', 'ui-scaleSlider','eits-default-button']);
	
	projectModule.config( function( $stateProvider , $urlRouterProvider, $importServiceProvider, $translateProvider ) {
		//-------
		//Broker configuration
		//-------
		$importServiceProvider.setBrokerURL("broker/interface");
		
		//-------
		//Translate configuration
		//-------
		$translateProvider.useURL('./bundles');
		
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
        /*$stateProvider.state('home', {
        	url : "/",
			template: '<h2>Body</h2>',
			controller : function ( $state, $importService ){
				console.log( $importService("accountService") );
			}
        });*/
        
      //Users
		$stateProvider.state('authentication', {
			url : "/",
			templateUrl : "modules/authentication/ui/user/user.jsp",
			controller : UserController
		});
		/*.state('authentication.create', {
			url : "register"
		}).state('authentication.update', {
			url : "/update/:id"
		});*/
		
	});
	
	projectModule.run( function( $rootScope, $state, $stateParams, $translate ) {
				
		$rootScope.$state = $state;
	    $rootScope.$stateParams = $stateParams;
	});
	
	/**
	 * 
	 */
	angular.element(document).ready( function() {
		angular.bootstrap(document, ['authentication']);
	});
	
})(window, window.angular);
