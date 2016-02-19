(function (angular) {
  'use strict';

  /**
   *
   * @param $scope
   * @param $state
   */
  angular.module('application')
    .controller('HomeController', function ($rootScope, $scope, $state, $importService, $ionicPopup, $timeout) {

      /**
       *
       */

      $timeout(function () {
        $importService("accountService");
        $importService("layerGroupService");
      });


      /*-------------------------------------------------------------------
       * 		 				 	ATTRIBUTES
       *-------------------------------------------------------------------*/
      /**
       *
       */
      $scope.model = {
        user: null
      };

      /*-------------------------------------------------------------------
       * 		 				  	POST CONSTRUCT
       *-------------------------------------------------------------------*/
      /**
       *
       */
      $scope.findUserById = function () {

        accountService.findUserById(1, {
          callback: function (result) {

            $scope.model.user = result;

            $ionicPopup.alert({
              title: 'Serviço executado com sucesso',
              template: ':D'
            });

            $scope.$apply();
          },
          errorHandler: function (message, exception) {
            $ionicPopup.alert({
              title: 'Opss...',
              template: message
            });

            $scope.$apply();
          }
        });

      };

      $scope.findLayers = function () {

        layerGroupService.listAllInternalLayerGroups({
          callback: function (result) {

            $scope.model.user = result;

            $ionicPopup.alert({
              title: 'Serviço executado com sucesso',
              template: ':D'
            });

            $scope.$apply();
          },
          errorHandler: function (message, exception) {
            $ionicPopup.alert({
              title: 'Opss...',
              template: message
            });

            $scope.$apply();
          }
        });

      };

      /*-------------------------------------------------------------------
       * 		 				 	  HANDLERS
       *-------------------------------------------------------------------*/
    });

}(window.angular));
