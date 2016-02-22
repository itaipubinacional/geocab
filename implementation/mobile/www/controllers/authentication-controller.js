(function (angular) {
  'use strict';

  /**
   *
   * @param $scope
   * @param $state
   */
  angular.module('application')
    .controller('AuthenticationController', function ($scope, $state, $http, $window, $ionicPopup, $API_ENDPOINT, ngFB, $ionicLoading) {

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

      $scope.model.user.email = 'test_prognus@mailinator.com';
      $scope.model.user.password = 'admin';


      /*-------------------------------------------------------------------
       * 		 				 	  HANDLERS
       *-------------------------------------------------------------------*/
      /**
       *
       */

    


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
                $ionicPopup.alert({
                  title: 'Opss...',
                  subTitle: 'Não foi possível autenticar.',
                  template: (data && data.message) ? data.message : data
                });
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
            $scope.loginSuccess();
          } else {
            $ionicPopup.alert({
              title: 'Facebook login failed',
              template: ':('
            });
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
          // For the purpose of this example I will store user data on local storage

          console.log(user_data);

          $ionicLoading.hide();
          $scope.loginSuccess();
        },
        function (msg) {
          $ionicLoading.hide();
        }
      );
    };

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

  });

}(window.angular));
