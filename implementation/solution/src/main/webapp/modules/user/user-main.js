(function(window, angular, undefined) {
	"use strict";
			
	//Start the AngularJS
	var projectModule = angular.module("admin", ["ui.bootstrap", "ui.router", "ngGrid", "eits-broker", "eits-angular-translate", "ui.tree",'angularBootstrapNavTree', 'ui-scaleSlider', 'localytics.directives','eits-default-button']);
	
	projectModule.config( function( $stateProvider , $urlRouterProvider, $importServiceProvider, $translateProvider ) {
		//-------
		//Broker configuration
		//-------
		$importServiceProvider.setBrokerURL("./broker");
		
		//-------
		//Translate configuration
		//-------
		$translateProvider.useURL('./bundles');
		
		//-------
		//URL Router
		//-------
		
		//HOME
        $urlRouterProvider.otherwise("/");
                
      //Users
		$stateProvider.state('my-account', {
			url : "/account",
			templateUrl : "modules/user/ui/my-account/my-account-view.jsp",
			controller : MyAccountController
		}).state('my-preferences', {
			url : "/preferences",
			templateUrl : "modules/user/ui/my-account/my-preferences-form.jsp",

		}).state('my-account.form', {
			url: "/form",
			menu: "my-account"
		});
    	
	});


	
	projectModule.run( function( $rootScope, $state, $stateParams, $translate ) {
				
		$rootScope.$state = $state;
	    $rootScope.$stateParams = $stateParams;
	});
	
	/**
	 * 
	 */
	angular.element(document).ready( function() {
		angular.bootstrap(document, ['admin']);
	});
	
	
	
	
	
})(window, window.angular);




