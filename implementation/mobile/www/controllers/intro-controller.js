(function(angular) {
  'use strict';

  /**
   *
   * @param $scope
   * @param $state
   */
  angular.module('application')
    .controller('IntroController', function($rootScope, $scope, $state, $ionicSlideBoxDelegate, $ionicPlatform) {


      /*-------------------------------------------------------------------
       * 		 				 	ATTRIBUTES
       *-------------------------------------------------------------------*/


      /*-------------------------------------------------------------------
       * 		 				  	POST CONSTRUCT
       *-------------------------------------------------------------------*/

      $ionicPlatform.registerBackButtonAction(function(e){
        $scope.previous();
      }, 100);
      /*-------------------------------------------------------------------
       * 		 				 	  HANDLERS
       *-------------------------------------------------------------------*/

      // Called to navigate to the main app
      $scope.startApp = function() {
        $state.go('map.index');
      };

      $scope.next = function() {
        $ionicSlideBoxDelegate.next();
      };

      $scope.previous = function() {
        if ($ionicSlideBoxDelegate.currentIndex()) {
          $ionicSlideBoxDelegate.previous();
        }else{
          ionic.Platform.exitApp();
        }
      };

      // Called each time the slide changes
      $scope.slideChanged = function(index) {
        if (index == 2) {
          localStorage.setItem('doneIntro', true);
        };
        $scope.slideIndex = index;
      };

      $scope.navSlide = function(index) {
        $ionicSlideBoxDelegate.slide(index, 500);
      }
    });

}(window.angular));
