"use strict";

angular.module("eits-hover", []).directive('ngHover', [ function() {
	var HOVER_CLASS = "ng-hovered";
	return {
		restrict : 'A',
		require : 'ngModel',
		link : function(scope, element, attrs, ctrl) {
			ctrl.$hovered = false;

            if ( element.is('select') ){

                element.on('change', function(evt) {
                    element.parent().next().hide();
                }).on('mouseover', function(evt) {
                    if ( ctrl.$error.required ){
                        element.parent().next().show();
                    }
                }).on('mouseleave', function(evt) {
                    element.parent().next().hide();
                });
            }
            else{
                element.on('mouseover', function(evt) {

                    element.addClass(HOVER_CLASS);
                    scope.$apply(function() {
                        ctrl.$hovered = true;
                    });

                }).on('mouseleave', function(evt) {

                    element.removeClass(HOVER_CLASS);
                    scope.$apply(function() {
                        ctrl.$hovered = false;
                    });

                });
            }
		}
	};
} ]);