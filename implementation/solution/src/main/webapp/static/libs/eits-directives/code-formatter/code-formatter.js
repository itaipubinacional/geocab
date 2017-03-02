"use strict";

angular.module('eits-code-formatter', []).
    filter('codeFormatter', function() {
        return function(input, p) {
            var out = "";
            if ( p ) {
                if( p == true )
                {
                    for (var i = 0, lenght = input.length; i < lenght; i++) {
                        out += input.charAt(i);
                        if ( i == 6 || i == 8 ) {
                            out += ".";
                        }
                    }
                }
                else if( p == "acaoOrcamentaria" )
                {
                    for (var i = 0, lenght = input.length; i < lenght; i++) {
                        out += input.charAt(i);
                        if ( i == 3 ) {
                            out += ".";
                        }
                    }
                }
            }
            else {
                if( input ==  undefined ) return;

                for (var i = 0, lenght = input.length; i < lenght; i++) {
                    out += input.charAt(i);
                    //Coloca '.' a cada 3 caracteres com exceção do último
                    if ((( ( i + 1 ) % 3 ) === 0 ) && ( i != ( lenght - 1 ))) {
                        out += ".";
                    }
                }
            }
            return out;
        }
    });