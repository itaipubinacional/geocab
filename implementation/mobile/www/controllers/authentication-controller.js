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

    /**
     *
    */
    $timeout(function () {
      $importService("loginService");
    });

    $scope.model.user.email = 'test_prognus@mailinator.com';
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
    }

    /**
     *
     */
    $scope.fbLogin = function () {
      ngFB.login({scope: 'email,public_profile,user_friends'})
      .then(function (response) {
        if (response.status === 'connected') {
          ngFB.api({
            path: '/me',
            params: {fields: 'id,name,email'}
          }).then(
            function (user) {
              $scope.model.user.email = user.email;
              $scope.verifyUser();
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
        template: 'Logging in...'
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
          if (result) {
            $scope.loginSuccess();
          }else{
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
      if(!localStorage.getItem('doneIntro')){
        localStorage.setItem('doneIntro','true');
        $state.go('intro');
      } else {
        $state.go('map');
      }
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
