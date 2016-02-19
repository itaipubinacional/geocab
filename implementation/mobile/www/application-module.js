(function (window, angular, undefined) {
  'use strict';

  //Start the AngularJS
  var module = angular.module('application', ['ngMessages', 'ionic', 'eits-ng', 'openlayers-directive', 'ionic-pullup', 'ionic.contrib.drawer', 'ngCordova', 'ngOpenFB']);

  /**
   *
   */

  module.constant('$API_ENDPOINT', 'http://geocab.sbox.me');


  /**
   *
   */
  module.config(function ($stateProvider, $urlRouterProvider, $importServiceProvider, $sceDelegateProvider, $API_ENDPOINT) {
    //-------
    //Broker configuration
    //-------
    $importServiceProvider.setBrokerURL($API_ENDPOINT + "/broker");

    //-------
    //Strict Contextual Escaping
    //-------
    $sceDelegateProvider.resourceUrlWhitelist([
      // Allow same origin resource loads.
      'self',
      // Allow loading from our assets domain. Notice the difference between * and **.
      $API_ENDPOINT + '/**'
    ]);


    //-------
    //URL Router
    //-------
    $urlRouterProvider.otherwise("/authentication/login");

    //AUTHENTICATION
    $stateProvider.state('authentication', {
      abstract: true,
      url: "/authentication",
      template: '<ion-nav-view></ion-nav-view>',
      controller: 'AuthenticationController'
    }).state('authentication.login', {
      url: "/login",
      templateUrl: './views/authentication/authentication-index.html'
    });

    //MAP
    $stateProvider.state('map', {
      url: "/map",
      controller: 'MapController',
      templateUrl: './views/map/map-index.html'
    });

    //HOME
    $stateProvider.state('home', {
      url: "/home",
      controller: 'HomeController',
      templateUrl: './views/home/home-index.html'
    });

    //INTRO
    $stateProvider.state('intro', {
      url: "/intro",
      controller: 'IntroController',
      templateUrl: './views/home/intro.html'
    });

  }).factory('Camera', ['$q', function($q) {

    return {
      getPicture: function(options) {
        var q = $q.defer();

        navigator.camera.getPicture(function(result) {
          // Do any magic you need
          q.resolve(result);
        }, function(err) {
          q.reject(err);
        }, options);

        return q.promise;
      }
    }
  }]);

  /**
   *
   */
  module.run(function ($rootScope, $ionicPlatform, $state, $stateParams, $API_ENDPOINT, ngFB) {

    $rootScope.$state = $state;
    $rootScope.$stateParams = $stateParams;
    $rootScope.$API_ENDPOINT = $API_ENDPOINT;

    $ionicPlatform.ready(function () {

      // Hide the accessory bar by default (remove this to show the accessory bar above the keyboard for form inputs
    	if (window.cordova && window.cordova.plugins && window.cordova.plugins.Keyboard) {
        cordova.plugins.Keyboard.hideKeyboardAccessoryBar(true);
        cordova.plugins.Keyboard.disableScroll(true);
      }

      if (window.StatusBar) {
        // org.apache.cordova.statusbar required
        StatusBar.styleDefault();
      }
    });
    
    ngFB.init({appId: '801316929973059'});

  });

  /**
   *
   */
  angular.element(document).ready(function () {
    angular.bootstrap(document, ['application']);
  });

})(window, window.angular);
