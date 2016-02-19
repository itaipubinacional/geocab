(function (angular) {
  'use strict';

  /**
   *
   * @param $scope
   * @param $state
   */
  angular.module('application')
    .controller('AuthenticationController', function ($scope, $state, $http, $window, $ionicPopup, $API_ENDPOINT) {

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

      $scope.model.user.email = 'tbecker@itaipu.gov.br';
      $scope.model.user.password = 'itaipu';

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
                if(!localStorage.getItem('doneIntro')){

                    localStorage.setItem('doneIntro','true');
                    $state.go('intro');

                } else {

                    $state.go('map');

                }
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
    });

}(window.angular));
