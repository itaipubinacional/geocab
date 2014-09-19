'use strict';

/**
 * 
 */
angular.module("eits-default-button", []).directive('defaultButton', [ function() {
    return {
        restrict : 'A',
        link     : function(scope, element, attr) {
            attr.$observe('defaultButton',function() {
                var button = $("#" + attr.defaultButton);
                if ( !button ) return; //nothing todo

                button.bind('click',function() {

                    scope.$apply(function(){
                        scope[attr.name].$submitted = true;
                    });
                });
            });
        }
    };
}]);