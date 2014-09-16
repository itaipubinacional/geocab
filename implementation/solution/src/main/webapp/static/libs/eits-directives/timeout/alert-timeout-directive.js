"use strict";
/**
* Directiva que fecha o alert após um intervalo de milisegundos
* Parâmetro do intervalo -> alert-expire=""
* Para que o alert não feche, o "time" deverá ser 0.
* 
* Exemplo:
* <alert alert-expire="message.time" ng-show="$parent.message.text.length > 0" type="message.type" close="message = ''"/>{{message.text}}</alert>
*
 */

var timeoutAlertDirective;

angular.module("eits-alert", ['ui.bootstrap'])
.directive('alert', function ($timeout) {
	return {
		restrict: 'EA',
		scope: {
			  alertExpire: '='
		},
		link: function postLink(scope, element, attrs){
			scope.$watch('$parent.message', function(context){
                var type = scope.type;

                clearTimeout( timeoutAlertDirective );

                if (scope.alertExpire == 0 || (scope.alertExpire == undefined && type == 'error')) {
                    return 0;
                } else if (context != undefined && context != '') {
                    var time = scope.alertExpire? scope.alertExpire: 4000;
                    timeoutAlertDirective = setTimeout(function(){
                        scope.close();
                        scope.$apply();
                    }, time);
                }
			});
		}
	};
});