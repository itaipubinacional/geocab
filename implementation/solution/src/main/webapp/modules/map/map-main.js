(function (window, angular, undefined) {
  "use strict";

  //Start the AngularJS
  var projectModule = angular.module("map", ["ui.bootstrap", "angular-sortable-view", "ui.router", "ngGrid", "eits-broker", "eits-angular-translate", "ivh.treeview", "ivh.treeview-extend", 'eits-default-button', 'localytics.directives', 'ui-scaleSlider', 'angularBootstrapNavTree', 'eits-upload-file']);

  projectModule.config(function ($stateProvider, $urlRouterProvider, $importServiceProvider, $translateProvider, ivhTreeviewOptionsProvider) {

    ivhTreeviewOptionsProvider.set({
      defaultSelectedState: false,
      validate: true,
      // Twisties can be images, custom html, or plain text
      twistieCollapsedTpl: '<span class="ivh-treeview-toggle ivh-treeview-toggle-right icon itaipu-icon-arrow-right"></span>',
      twistieExpandedTpl: '<span class="ivh-treeview-toggle ivh-treeview-toggle-down icon itaipu-icon-arrow-down" style="color: #0077bf"></span>',
      twistieLeafTpl: '<span style="margin-left: 18px"></span>'
    });

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
          '<span class="tree-view" ng-class="{checked: node.selected, indeterminate: node.__ivhTreeviewIndeterminate}">',
          '</span>'
      ].join(''),
      link: function(scope, element, attrs, ctrl) {
        element.on('click', function() {
          ivhTreeviewMgr.select(ctrl.root(), scope.node, !scope.node.selected);
          ctrl.onCbChange(scope.node);
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
