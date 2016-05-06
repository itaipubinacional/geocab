(function (window, angular, undefined) {
  "use strict";

  //Start the AngularJS
  var projectModule = angular.module("map", ["ui.bootstrap", "angular-sortable-view", "ui.router", "ngGrid", "eits-broker", "eits-angular-translate", "ivh.treeview", "ivh.treeview-extend", 'eits-default-button', 'localytics.directives', 'ui-scaleSlider', 'angularBootstrapNavTree', 'eits-upload-file']);

  projectModule.config(function ($stateProvider, $urlRouterProvider, $importServiceProvider, $translateProvider) {
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

    //------
    //Scheduler
    //------
    //Resource Sheet
    //------
    $stateProvider.state('map', {
      url: "/",
      templateUrl: "./modules/map/ui/interactive-map-view.jsp",
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

  projectModule.directive('treeViewBox', function(ivhTreeviewMgr) {
    return {
      restrict: 'AE',
      require: '^ivhTreeview',
      template: [
        '<span class="tree-view-box">[',
        '<span ng-show="node.selected" class="x">x</span>',
        '<span ng-show="node.__ivhTreeviewIndeterminate" class="y">~</span>',
        '<span ng-hide="node.selected || node.__ivhTreeviewIndeterminate"> </span>',
        ']</span>',
      ].join(''),
      link: function(scope, element, attrs, ctrl) {
        element.on('click', function() {
          ivhTreeviewMgr.select(ctrl.root(), scope.node, !scope.node.selected);
          scope.$apply();
        });
      }
    };
  });

  /**
   *
   */
  angular.element(document).ready(function () {
    angular.bootstrap(document, ['map']);
  });

})(window, window.angular);
