(function(window, angular, undefined) {
	"use strict";
			
	//Start the AngularJS
	var projectModule = angular.module("admin", ["ui.bootstrap", "ui.router", "ngGrid", "eits-broker", "eits-angular-translate", "ui.tree",'angularBootstrapNavTree', 'ui-scaleSlider', 'localytics.directives','eits-default-button']);
	
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
			menu: "layer-group"
		});
		
		//Layer config
		$stateProvider.state('layer-config', {
			url : "/layer-config",
			templateUrl : "modules/admin/ui/layer-config/layer-config-view.jsp",
			controller : LayerConfigController
		})
		.state('layer-config.detail', {
			url: "/detail/:id",
			menu: "layer-config"
		})
		.state('layer-config.list', {
			url: "/list",
			menu: "layer-config"
		})
		.state('layer-config.create', {
			url : "/create",
			menu: "layer-config"
		})
		.state('layer-config.update', {
			url : "/update/:id",
			menu: "layer-config"
		});

		//Custom Search
		$stateProvider.state('custom-search', {
            url : "/custom-search",
            templateUrl : "modules/admin/ui/custom-search/custom-search-view.jsp",
            controller : CustomSearchController
        })
        .state('custom-search.list', {
                    url: "/list",
                    menu: "custom-search"
        })
        .state('custom-search.detail', {
                    url: "/detail/:id",
                    menu: "custom-search"
        })
        .state('custom-search.create', {
            url : "/create",
            menu: "custom-search"
        })
        .state('custom-search.update', {
            url : "/update/:id",
            menu: "custom-search"
        });
		
		//ACCESS GROUP
		$stateProvider.state('access-group', {
            url : "/access-group",
            templateUrl : "modules/admin/ui/access-group/access-group-view.jsp",
            controller : AccessGroupController
        })
        .state('access-group.detail', {
            url: "/detail/:id",
            menu: "access-group"
        })
        .state('access-group.list', {
            url: "/list",
            menu: "access-group"
        })
        .state('access-group.create', {
            url : "/create",
            menu: "access-group"
        })
        .state('access-group.update', {
            url : "/update/:id",
            menu: "access-group"
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




