'use strict';

(function(window, angular, undefined) {
			
	//Start the AngularJS
	var projectModule = angular.module('authentication', ['ui.bootstrap', 'ui.router', 'ngGrid', 'eits-broker', 'eits-angular-translate','angularBootstrapNavTree', 'ui-scaleSlider','eits-default-button']);
	
	projectModule.config( function( $stateProvider , $urlRouterProvider, $importServiceProvider, $translateProvider ) {
		//-------
		//Broker configuration
		//-------
		$importServiceProvider.setBrokerURL('broker/interface');
		
		//-------
		//Translate configuration
		//-------
		$translateProvider.useURL('./bundles');

		//authentication
        $urlRouterProvider.otherwise('/');

		$stateProvider.state('authentication', {
			url : '/',
			templateUrl : 'modules/authentication/ui/authentication/authentication.jsp',
			controller : AuthenticationController
		});
	});
	
	projectModule.run( function( $rootScope, $state, $stateParams, $translate ) {

		$rootScope.$state = $state;
	    $rootScope.$stateParams = $stateParams;
	});

	angular.element(document).ready( function() {
		angular.bootstrap(document, ['authentication']);
	});
	
})(window, window.angular);
