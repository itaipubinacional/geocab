"use strict";
/**
 * Exemplo:
 * 1 - Sendo paises uma lista, ou uma chamada para uma função que retorne uma lista de objetos.
 * 2 - selected é o valor que receberá o dado na controller
 * 
 * <input auto-complete autocomplete="off" type="text" ng-model="selected" typeahead="pais as pais.nome for pais in paises | filter:{nome: $viewValue} | limitTo:8">
 */
angular.module("eits-autocomplete", ['ui.bootstrap'])
.directive('autoComplete', function ($compile) {
	return {
		restrict: 'A',
		link: function postLink(scope, element, attrs){
			
			$(element[0]).unbind('blur').on('blur', function(){
				
				var selected = eval("scope."+$(this).attr("ng-model"));
				
				if ( typeof selected != 'object' ) {
					
					var element = $(this); 
					eval("scope."+$(this).attr("ng-model")+" = null");
                    scope.$apply();
					setTimeout(function(){
						var e = jQuery.Event("keydown");

                        /**
                         * Versão 0.1
                         * Simula o ESC para fechar o combo
                         */
	            		//e.which = 27;
	            		//element.trigger(e);

                        /**
                         * Versão 0.2
                         * Simula o blur para fechar o combo
                         */
                        //element.blur();

					}, 100);
				}
			});
		}
	};
})
.directive('typeahead', function () {
    return {
        link: function postLink( scope, element, attrs ){
            $(element).focus(function()
            {
                $(this).next().css('maxWidth', ( element.width() + 12) + 'px' );
                $(this).unbind('focus');
            });
        }
    }
});
