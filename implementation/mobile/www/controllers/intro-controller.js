/**
 * Created by boz on 18/02/16.
 */
(function (angular) {
    'use strict';

    /**
     *
     * @param $scope
     * @param $state
     */
    angular.module('application')
        .controller('IntroController', function ($rootScope, $scope, $state, $ionicSlideBoxDelegate) {


            /*-------------------------------------------------------------------
             * 		 				 	ATTRIBUTES
             *-------------------------------------------------------------------*/


            /*-------------------------------------------------------------------
             * 		 				  	POST CONSTRUCT
             *-------------------------------------------------------------------*/


            /*-------------------------------------------------------------------
             * 		 				 	  HANDLERS
             *-------------------------------------------------------------------*/

            // Called to navigate to the main app
            $scope.startApp = function () {
                $state.go('map');
            };

            $scope.next = function () {
                $ionicSlideBoxDelegate.next();
            };

            $scope.previous = function () {
                $ionicSlideBoxDelegate.previous();
            };

            // Called each time the slide changes
            $scope.slideChanged = function (index) {
                $scope.slideIndex = index;
            };

            $scope.navSlide = function (index) {
                $ionicSlideBoxDelegate.slide(index, 500);
            }
        });

}(window.angular));
