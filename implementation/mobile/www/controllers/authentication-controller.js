(function (angular) {
  'use strict';

/**
 *
 * @param $scope
 * @param $state
 */
angular.module('application')
  .controller('AuthenticationController', function ($rootScope, $importService, $timeout, $scope, $state, $http, $window,
                                                    $ionicPopup, $API_ENDPOINT, ngFB, $ionicLoading, $translate, $ionicPlatform,
                                                    $ionicHistory) {


    $timeout(function() {
      $importService("accountService");
    });

    /*-------------------------------------------------------------------
     *              ATTRIBUTES
     *-------------------------------------------------------------------*/
    /**
     *
     */
    $rootScope.model = {
      form: null,
      user: {
        email : '',
        password : ''
      },
      errorMsg : {
              title : $translate('Error'),
              subTitle : $translate('authentication.Authentication'),
              template  : $translate('authentication.Bad-credentials') + ', ' + $translate('or') + ' ' + $translate('authentication.User-is-disabled')
            }
      };

    /*-------------------------------------------------------------------
     *                HANDLERS
     *-------------------------------------------------------------------*/

     /**
      * authenticated user
      * */
     $scope.getUserAuthenticated = function() {

       $timeout(function() {
         accountService.getUserAuthenticated({
           callback: function(result) {

             $rootScope.$broadcast('userMe', result);
             $rootScope.userMe = result;
             $scope.coordinatesFormat = result.coordinates;
             $scope.$apply();
           },
           errorHandler: function(message, exception) {
             $log.debug(message);
             $scope.$apply();
           }
         });
       });
     };

    /**
     *
     */
    $scope.loginHandler = function (form) {

      if (!form.$valid) {

        $scope.isFormSubmit = true;

      } else {

        $scope.isFormSubmit = false;

        $scope.login('/login', $scope.model.user);
      }

    };

    /**
     * Facebook login
     */
    $scope.fbLogin = function () {
      $ionicLoading.show({
        template: $translate('authentication.Logging-in')
      });
      //Realiza a autenticação
      ngFB.login({scope: 'email'})
      .then(function (response) {
        if (response.status === 'connected') {
          //Requisita da api do facebook as informações do usuário
          ngFB.api({
            path: '/me',
            params: {fields: 'id,name,email'}
          }).then(function (user) {
              $scope.login('/login/facebook',  {'email' : user.email, 'token' : response.authResponse.accessToken});
            },
            function (error) {
              $scope.loginFailed();
            });
        } else {
          $scope.loginFailed();
        }
      },function (error) {
          //Se o usuário clicou em voltar, ou seja, cancelou o login via facebook
          if(error.status == 'user_cancelled'){
            $ionicLoading.hide();
          }else {
            $scope.loginFailed();
          }
        });
    };

    /**
     * This method is executed when the user press the "Sign in with Google" button  *
    */
    $scope.googleSignIn = function() {
      $ionicLoading.show({
        template: $translate('authentication.Logging-in')
      });

      window.plugins.googleplus.login(
        {
          'offline': true, // optional, used for Android only - if set to true the plugin will also return the OAuth access token ('oauthToken' param), that can be used to sign in to some third party services that don't accept a Cross-client identity token (ex. Firebase)
        },
        function (user) {
          $scope.login('/login/google', {'email': user.email, 'token' : user.oauthToken});
        },
        function (error) {
          //Se o usuário clicou em voltar, ou seja, cancelou o login via facebook
          if(error == 'user cancelled'){
            $ionicLoading.hide();
          }else {
            $scope.loginFailed();
          }
        }
      );
    };

    /**
     * Realiza o login
    */
    $scope.login = function(url, user){
      //Valida o token provido pelo facebook no back-end, o back-end devolve a sessão do usuário
      $http.post($API_ENDPOINT + url, user)
        .success(function (data, status, headers, config) {
          $scope.model.user = data;
          $scope.loginSuccess();
      }).error(function (data, status, headers, config) {
          $scope.loginFailed();
      });
    };


    /**
      *
    */
    $scope.loginSuccess = function () {
      $ionicLoading.hide();
      localStorage.setItem('token', $scope.model.user.token);
      localStorage.setItem('userEmail', $scope.model.user.email);
      if (localStorage.getItem('doneIntro')) {
        $state.go('map.index');
        //$scope.getUserAuthenticated();
      } else {
        $state.go('authentication.intro');
      }
    };

    /**
      *
    */
    $scope.loginFailed = function () {
      $ionicLoading.hide();
      localStorage.removeItem('token', $scope.model.user.token);
      localStorage.removeItem('userEmail', $scope.model.user.email);
      $ionicPopup.alert($scope.model.errorMsg);
    };

    /*-------------------------------------------------------------------
     *                POST CONSTRUCT
     *-------------------------------------------------------------------*/

    /**
     * Remove a splashscreen quando a tela de login for carregada
    */
    $scope.$on('$ionicView.loaded', function() {
      ionic.Platform.ready( function() {
        if(navigator && navigator.splashscreen) navigator.splashscreen.hide();
      });
    });

    /**
     * token handler
    */
    if(localStorage.getItem('token')){
      $scope.model.user.email = localStorage.getItem('userEmail');
      $scope.login('/login/geocab', {'email' : $scope.model.user.email, 'token' : localStorage.getItem('token')});
    }

    if(localStorage.getItem('userEmail')){
      $scope.model.user.email = localStorage.getItem('userEmail');
    }

    //Handler de BACK
    $ionicPlatform.registerBackButtonAction(function(e){
      if ($state.$current.name == 'map.index' || $state.$current.name == 'authentication.login') {
        ionic.Platform.exitApp();
      } else {
        $ionicHistory.goBack();
      }
    }, 100);

  });

}(window.angular));
