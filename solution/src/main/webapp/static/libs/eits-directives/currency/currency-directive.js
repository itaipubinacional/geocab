"use strict";

angular.module('eits-currency', [])
.directive('currencyInput', function($parse) {
    return {
        require: 'ngModel',
        restrict: 'A',
        link: function(scope, element, attrs, ngModelController) {

            scope.decimal = attrs.decimal == undefined ? 2 : attrs.decimal;

            /**
             * Formata inputValue como valor monetário
             */
            function formatCurrency ( inputValue ) {

                var inputVal = ( typeof  inputValue == 'number') ? new String( inputValue.toFixed(scope.decimal) ) : inputValue;

                //Remove tudo que não for número e em seguida os zeros a esquerda
                if (inputVal != null){
                    inputVal = inputVal.replace(/\D/g, '').replace(/^0*/,'');

                    //Adiciona zero enquanto o valor for menor que 3
                    while (inputVal.length < ( parseInt(scope.decimal) + 1)){
                        inputVal = '0' + inputVal;
                    }
                    //Valor antes da vírgula
                    var intPart = inputVal.substr(0, inputVal.length - scope.decimal);
                    //Valor depois da vírgula
                    var decPart = inputVal.substr(inputVal.length - scope.decimal, scope.decimal);

                    //Adiciona . a cada 3 números da parte inteira
                    if (intPart.length > 3) {
                        var intDiv = Math.floor(intPart.length / 3);
                        while (intDiv > 0) {
                            var lastComma = intPart.indexOf(".");
                            if (lastComma < 0) {
                                lastComma = intPart.length;
                            }
                            if (lastComma - 3 > 0) {
                                intPart = intPart.slice(0, lastComma - 3) + "." + intPart.slice(lastComma - 3 + Math.abs(0));
                            }
                            intDiv--;
                        }
                    }

                    var result = intPart;
                        result += decPart.length > 0 ? "," + decPart : "";
                    return result;
                }
            }

            /**
             * Formata o valor existente no modelo antes de enviar para a visualização
             */
            ngModelController.$formatters.unshift(function( valueFromModel ) {
                if (typeof valueFromModel === 'undefined') {
                    return;
                }
                //O retorno dessa função será passado para o campo de texto
                return formatCurrency(valueFromModel);
            });

            /**
             * Formata o valor antes de gravá-lo no modelo e altera o texto da visulização
             */
            ngModelController.$parsers.push(function ( valueToModel ) {
                if( valueToModel == undefined ) return;
                //Remove letras
                var result= formatCurrency( valueToModel );
                //Formata o valor digitado
                element[0].value = formatCurrency( valueToModel);
                //Remove os pontos e vírgula
                result = String( result).replace(/[\.\,]/g,"");
                //Adiciona um . para separar os decimais
                result = result.substr( 0, result.length - scope.decimal ) + "." + result.substr( result.length - scope.decimal );
                return result;
            });
        }
    };
});