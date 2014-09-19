"use strict";

/**
 *
 */
angular.module('eits-numbers-only', []).directive('numbersOnly', function(){
    return {
        require: 'ngModel',
        priority: 1,
        link: function(scope, element, attrs, modelCtrl) {
            modelCtrl.$parsers.push(function (inputValue) {
                if (inputValue == undefined) return '';

                var transformedInput = inputValue.replace( new RegExp("[^0-9" + attrs.numbersOnly + "]") , '');

                if (transformedInput!=inputValue) {
                    modelCtrl.$setViewValue(transformedInput);
                    modelCtrl.$render();
                }

                return transformedInput;
            });
        }
    };
});