"use strict"; 
/**
 *
 */
angular.module('auto-fill', []).directive('autoFillSync', function() {
    return {
        restrict: "A",
        require: "?ngModel",
        link: function(scope, element, attrs, ngModel) {
            setInterval(function() {
                var prev_val = '';
                if ( !angular.isUndefined( attrs.xAutoFillPrevVal ) ) {
                    prev_val = attrs.xAutoFillPrevVal;
                }
                if ( element.val() != prev_val ) {
                    if ( !angular.isUndefined( ngModel ) ) {
                        if ( !( element.val() == '' && ngModel.$pristine ) ) {
                            attrs.xAutoFillPrevVal = element.val();
                            scope.$apply(function() {
                                ngModel.$setViewValue(element.val());
                            });
                        }
                    }
                    else {
                        element.trigger('input');
                        element.trigger('change');
                        element.trigger('keyup');
                        attrs.xAutoFillPrevVal = element.val();
                    }
                }
            }, 300);
        }
    };
});