(function (window, angular, undefined) {
  'use strict';

  //Start the AngularJS
  var module = angular.module('application', ['ngMessages', 'ionic', 'eits-ng', 'ionic-pullup', 'ionic.contrib.drawer', 'ngCordova', 'ngOpenFB' ,'eits.translate']);

  /**
   *
   */

  module.constant('$API_ENDPOINT', 'http://geocab.sbox.me');


  /**
   *
   */
  module.config(function ($stateProvider, $urlRouterProvider, $importServiceProvider, $sceDelegateProvider, $API_ENDPOINT, $translateProvider, $compileProvider) {


    $compileProvider.imgSrcSanitizationWhitelist(/^\s*(https?|ftp|mailto|file|tel|data):/);
    //-------
    //Broker configuration
    //-------
    $importServiceProvider.setBrokerURL($API_ENDPOINT + '/broker');

    //-------
    //Translate configuration
    //-------
    $translateProvider.useURL($API_ENDPOINT + '/bundles');

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
      abstract: true,
      url: "/map",
      template: '<ion-nav-view></ion-nav-view>',
      controller: 'MapController'
    }).state('map.index', {
      url: "/index",
      templateUrl: './views/map/map-index.html'
    }).state('map.gallery', {
      url: "/gallery",
      templateUrl: './views/map/gallery.html'
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
  module.run(function ($rootScope, $ionicPlatform, $state, $stateParams, $API_ENDPOINT, ngFB, $cordovaStatusbar) {


    $rootScope.$state = $state;
    $rootScope.$stateParams = $stateParams;
    $rootScope.$API_ENDPOINT = $API_ENDPOINT;

    $ionicPlatform.ready(function () {

      // Hide the accessory bar by default (remove this to show the accessory bar above the keyboard for form inputs
    	if (window.cordova && window.cordova.plugins && window.cordova.plugins.Keyboard) {

        cordova.plugins.Keyboard.hideKeyboardAccessoryBar(true);
        cordova.plugins.Keyboard.disableScroll(true);
      }
      // $cordovaStatusbar.overlaysWebView(true);
      // $cordovaStatusBar.style(2); //Black, transulcent
    });

    ngFB.init({appId: '801316929973059'});

   //

   // $cordovaStatusBar.style(1) //Light
   // $cordovaStatusBar.style(2) //Black, transulcent
   // $cordovaStatusBar.style(3) //Black, opaque

   // setTimeout(function() {
   //  $cordovaStatusbar.isVisible() ? $cordovaStatusbar.hide() : $cordovaStatusbar.show();
        // $cordovaStatusbar.overlaysWebView(false);
    // }, 300);

    // make it fullscreen on IOS so it has the correct header size.
    //ionic.Platform.fullScreen();

  });

  /**
   *
   */
  angular.element(document).ready(function () {
    angular.bootstrap(document, ['application']);
  });

})(window, window.angular);
