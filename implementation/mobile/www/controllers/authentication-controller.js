(function (angular) {
  'use strict';

/**
 *
 * @param $scope
 * @param $state
 */
angular.module('application')
  .controller('AuthenticationController', function ($importService, $timeout, $scope, $state, $http, $window, $ionicPopup, $API_ENDPOINT, ngFB, $ionicLoading) {


    /*-------------------------------------------------------------------
     * 		 				 	ATTRIBUTES
     *-------------------------------------------------------------------*/
    /**
     *
     */
    $scope.model = {
      form: null,
      user: {}
    };
    
    /*-------------------------------------------------------------------
    *                POST CONSTRUCT
    *-------------------------------------------------------------------*/
    if(localStorage.getItem('userEmail')){
      $scope.model.user.email  = localStorage.getItem('userEmail');
      $state.go('map');
    };
    /**
     *
    */
    $timeout(function () {
      $importService("loginService");
    });

    $scope.model.user.email = 'test_prognus@mailinator.com'; //TODO lembrar de retirar
    $scope.model.user.password = 'admin';

    /*-------------------------------------------------------------------
     * 		 				 	  HANDLERS
     *-------------------------------------------------------------------*/
    /**
     *
     */
    $scope.loginHandler = function () {

      if ($scope.model.form.$invalid) {
        $ionicPopup.alert({
          title: 'Opss...',
          subTitle: 'Os campos estão inválidos.',
          template: 'Por favor verifique e tente novamente.'
        });

      } else {

        var config = {
          headers: {'Content-Type': 'application/x-www-form-urlencoded; charset=UTF-8'}
        };


        $http.post($API_ENDPOINT + "/j_spring_security_check", $.param($scope.model.user), config)
          .success(function (data, status, headers, config) {
            $scope.loginSuccess();
          })
          .error(function (data, status, headers, config) {
            $scope.loginFailed();
          }
        );
      }
    };

    /**
     *
     */
    $scope.fbLogin = function () {
      //Realiza a autenticação
      ngFB.login({scope: 'email,public_profile,user_friends'})
      .then(function (response) {
        if (response.status === 'connected') {
          //Requisita da api do facebook as informações do usuário
          ngFB.api({
            path: '/me',
            params: {fields: 'id,name,email'}
          }).then(function (user) {
              //Valida o access token provido pelo facebook no back-end, o back-end devolve a sessão do usuário
              $http.get($API_ENDPOINT + "/login/facebook/" +user.email + "/" + response.authResponse.accessToken)
                .success(function (data, status, headers, config) {
                  $scope.model.user.email = user.email;
                  $state.go('home');
                  // $scope.verifyUser();
                })
                .error(function (data, status, headers, config) {
                  console.log(data);
                }
              );
            },
            function (error) {
              $scope.loginFailed();
            });
        } else {
          $scope.loginFailed();
        }
      });
    };

    // This method is executed when the user press the "Sign in with Google" button
    /**
       *
    */
    $scope.googleSignIn = function() {
      $ionicLoading.show({
        template: 'Logging in...' //TODO translate
      });

      window.plugins.googleplus.login(
        {},
        function (user_data) {
          $scope.model.user.email = user_data.email;
          $ionicLoading.hide();
          $scope.verifyUser();
        },
        function (msg) {
          $ionicLoading.hide();
          $scope.loginFailed();
        }
      );
    };

    /**
    * Verifying user existence in database
    */
    $scope.verifyUser = function () {
      loginService.findUserByEmail( $scope.model.user.email, {
        callback: function (result) {
          $scope.model.user = result;
          //Deleta a criptografia do password
          delete $scope.model.user.password;
          if (result && result.enabled) {
            $scope.loginSuccess();
          } else {
            $scope.loginFailed();
          }
          $scope.$apply();
        },
        errorHandler: function (message, exception) {
          $scope.loginFailed();
          $scope.$apply();
        }
      });
    }

    /**
      *
    */
    $scope.loginSuccess = function () {
      localStorage.setItem('userEmail', $scope.model.user.email);
      $state.go('intro');
    };

    /**
      *
    */
    $scope.loginFailed = function () {
      $ionicPopup.alert({
        title: 'Opss...',
        subTitle: 'Não foi possível autenticar.', //TODO traduzir
        template: 'Verifique seu usuário e tente novamente' ////TODO utilizar as mensagens providas pelos callbacks de erros
      });
    };

  });

}(window.angular));
