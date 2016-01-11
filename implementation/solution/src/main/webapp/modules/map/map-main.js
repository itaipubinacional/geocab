(function (window, angular, undefined) {
  "use strict";

  //Start the AngularJS
  var projectModule = angular.module("map", ["ui.bootstrap", "ui.sortable", "ui.router", "ngGrid", "eits-broker", "eits-angular-translate", "ivh.treeview", "ivh.treeview-extend", 'eits-default-button', 'localytics.directives', 'ui-scaleSlider', 'angularBootstrapNavTree', 'eits-upload-file']);

  projectModule.config(function ($stateProvider, $urlRouterProvider, $importServiceProvider, $translateProvider) {
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
    $stateProvider.state('map', {
      url: "/",
      templateUrl: "modules/map/ui/interactive-map-view.jsp",
      controller: MapController,
      menu: 'map'
    });

    $stateProvider.state('contact', {
      url: "/contact",
      templateUrl: "modules/map/ui/contact-view.jsp",
      controller: ContactController
    });

  });

  projectModule.run(function ($rootScope, $state, $stateParams) {
    $rootScope.$state = $state;
    $rootScope.$stateParams = $stateParams;
  });

  /**
   *
   */
  angular.element(document).ready(function () {
    angular.bootstrap(document, ['map']);
  });

})(window, window.angular);