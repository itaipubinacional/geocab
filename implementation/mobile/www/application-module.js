(function(window, angular, undefined) {
  'use strict';

  //Start the AngularJS
  var module = angular.module('application', ['ngMessages', 'ionic', 'eits-ng', 'ngCordova', 'ngOpenFB', 'eits.translate']);

  /**
   * Desenvolvimento
   */
  module.constant('$API_ENDPOINT', 'http://geocab.sbox.me');
  /**
   * Homologação
   */
  //module.constant('$API_ENDPOINT', 'http://chi554a:8080/geocab');
  /**
   * Produção
   */
  // module.constant('$API_ENDPOINT', 'http://geocab.itaipu.gov.br');

  /**
   *
   */
  module.config(function($stateProvider, $urlRouterProvider, $importServiceProvider, $sceDelegateProvider, $API_ENDPOINT,
                         $translateProvider, $compileProvider, $httpProvider, $logProvider) {

    $logProvider.debugEnabled(true); //TODO Fixme;

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
    }).state('authentication.intro', {
      url: "/intro",
      controller: 'IntroController',
      templateUrl: './views/home/intro.html'
    });

    //MAP
    $stateProvider.state('map', {
      abstract: true,
      url: '/map',
      templateUrl: './views/templates/menu.html',
      controller: 'MapController'
    }).state('map.index', {
      url: '/index',
      views: {
        'menuContent': {
          templateUrl: './views/templates/map-index.html',
          controller: 'IndexController'
        }
      }
    }).state('map.marker', {
      url: '/marker',
      views: {
        'menuContent': {
          templateUrl: './views/templates/marker-view.html',
          controller: 'IndexController'
        }
      }
    }).state('map.wms', {
      url: '/wms',
      views: {
        'menuContent': {
          templateUrl: './views/templates/wms-view.html',
          controller: 'IndexController'
        }
      }
    }).state('map.gallery', {
      url: '/gallery',
      views: {
        'menuContent': {
          templateUrl: './views/map/gallery.html',
          controller: 'GalleryController'
        }
      }
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
  module.run(function($rootScope, $log, $http, $ionicPopup, $ionicPlatform, $state, $stateParams, $API_ENDPOINT, ngFB,
                      $cordovaStatusbar, $ionicLoading, $cordovaNetwork) {


    $rootScope.$state = $state;
    $rootScope.$stateParams = $stateParams;
    $rootScope.$API_ENDPOINT = $API_ENDPOINT;

    $rootScope.setUrl = function(url) {
      if (window.location.hostname.match(/localhost/))
        return $API_ENDPOINT + '/broker/' + url;
      return './lib/dwr/' + url;
    };

    $ionicPlatform.ready(function() {

      // Hide the accessory bar by default (remove this to show the accessory bar above the keyboard for form inputs
      if (window.cordova && window.cordova.plugins && window.cordova.plugins.Keyboard) {

        cordova.plugins.Keyboard.hideKeyboardAccessoryBar(true);
        cordova.plugins.Keyboard.disableScroll(true);
      }

      if (window.StatusBar) {
        // org.apache.cordova.statusbar required
        StatusBar.styleDefault();
      }

      $rootScope.showNetworkAlert = function() {
        var alertPopup = $ionicPopup.alert({
          title: 'Sem conexão',
          template: 'Por favor, verifique sua conexão com a internet e reinicie o aplicativo'
        });

        alertPopup.then(function() {

          ionic.Platform.exitApp();

        });
      };

      $rootScope.showServerAlert = function() {
        var alertPopup = $ionicPopup.alert({
          title: 'Sem conexão',
          template: 'Por favor, servidor não disponível. Tente novamente mais tarde'
        });

        alertPopup.then(function() {

          ionic.Platform.exitApp();

        });
      };

      // listen for Online event
      $rootScope.$on('$cordovaNetwork:online', function(event, networkState){
        $log.debug('online');
        var onlineState = networkState;
      });

      // listen for Offline event
      $rootScope.$on('$cordovaNetwork:offline', function(event, networkState){
        $log.debug('offline');

        $rootScope.showNetworkAlert();

      });

      if(navigator.connection) {
        var type = navigator.connection.type;
        $log.debug(type);

        var isOnline = $cordovaNetwork.isOnline();
        $log.debug(isOnline);

        var isOffline = $cordovaNetwork.isOffline();
        $log.debug(isOffline);

        if (isOffline) {
          if (navigator && navigator.splashscreen) {
            navigator.splashscreen.hide();
            $rootScope.$broadcast('$cordovaNetwork:offline');
          }
        }

        $http({
          method: 'GET',
          url: $API_ENDPOINT
        }).then(function successCallback(response) {

        }, function errorCallback(response) {

          if (navigator && navigator.splashscreen) {
            navigator.splashscreen.hide();
            $rootScope.showServerAlert();
          }

        });
      }

    });

    ngFB.init({
      appId: '1563968713815015'
    });

    $rootScope.$on('loading:show', function() {
      $ionicLoading.show({
        template: '<ion-spinner></ion-spinner>'
      });
    });

    $rootScope.$on('loading:hide', function() {
      $ionicLoading.hide();
    });

  });

  /**
   *
   */
  angular.element(document).ready(function() {
    angular.bootstrap(document, ['application']);
  });

})(window, window.angular);
