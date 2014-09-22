(function(window, angular, undefined) {
	"use strict";
			
	//Start the AngularJS
	var projectModule = angular.module("admin", ["ui.bootstrap", "ui.router", "ngGrid", "eits-broker", "eits-angular-translate"]);
	
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
        $stateProvider.state('home', {
        	url : "/",
			template: '<h2>Body</h2>',
			controller : function ( $state, $importService ){
				console.log( $importService("accountService") );
			}
        });
        
      //Users
		$stateProvider.state('users', {
			url : "/users",
			templateUrl : "modules/admin/ui/users/users-view.jsp",
			controller : UsersController
		}).state('users.list', {
			url: "/list",
			menu: "users"
		}).state('users.create', {
			url : "/create",
			menu: "users"
		}).state('users.detail', {
			url: "/defail/:id",
			menu: "users"
		}).state('users.update', {
			url : "/update/:id",
			menu: "users"
		});

      //Data source
		$stateProvider.state('data-source', {
			url : "/data-source",
			templateUrl : "modules/admin/ui/data-source/data-source-view.jsp",
			controller : DataSourceController
		})
		.state('data-source.detail', {
			url: "/detail/:id",
			menu: "data-source"
		})
		.state('data-source.list', {
			url: "/list",
			menu: "data-source"
		})
		.state('data-source.create', {
			url : "/create",
			menu: "data-source"
		})
		.state('data-source.update', {
			url : "/update/:id",
			menu: "data-source"
		});
		
		//Layer group
		$stateProvider.state('layer-group', {
			url : "/layer-group",
			templateUrl : "modules/admin/ui/layer-group/layer-group-view.jsp",
			controller : LayerGroupController
		})
		.state('layer-group.list', {
			url: "/list",
			menu: "data-source"
		});
		
		//Layers
		$stateProvider.state('layers', {
			url : "/layers",
			templateUrl : "modules/admin/ui/layers/dlayers-view.html",
			controller : DataSourceController
		})
		.state('layers.detail', {
			url: "/defail/:id",
			menu: "data-source"
		})
		.state('layers.list', {
			url: "/list",
			menu: "data-source"
		})
		.state('layers.create', {
			url : "/create",
			menu: "data-source"
		})
		.state('layers.update', {
			url : "/update/:id",
			menu: "data-source"
		});

		
	});
	
	projectModule.run( function( $rootScope, $state, $stateParams, $translate ) {
		
		console.log( $translate("admin.datasource.Name-or-address") );
		
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