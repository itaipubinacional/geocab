(function () {
    'use strict';

    /**
     * RangeSlider, allows user to define a range of values using a slider
     * Touch friendly.
     * @directive
     */
angular.module('ui-scaleSlider', [])
    .directive('scaleSlider', function() {
        return {
            scope: {
                ngModel: '=',
                sliderDisabled: '='
            },
            link: function(scope, elem, attrs) {
                $(elem).addClass('ui-scale-slider');
                if (!scope.scaleSlider) {
                    //scope.scaleSlider = [ '1:70M', '1:35M', '1:18M', '1:9M', '1:4M', '1:2M', '1:1M', '1:528K', '1:274K', '1:137K', '1:68K', '1:34K', '1:17K', '1:8561', '1:4280', '1:2140'];
                	scope.scaleSlider = [ '1:500km', '1:200km', '1:100km', '1:50km', '1:20km', '1:10km', '1:5km', '1:2km', '1:1km', '1:500m', '1:200m', '1:100m', '1:50m', '1:20m'];
                }
                var value1 = 0;
                var value2 = scope.scaleSlider.length-1;
                if (!scope.ngModel.values) {
                    scope.ngModel.values = [scope.scaleSlider[0], scope.scaleSlider[scope.scaleSlider.length-1]];
                } else {
                    value1 = scope.scaleSlider.indexOf(scope.ngModel.values[0]);
                    value2 = scope.scaleSlider.indexOf(scope.ngModel.values[1]);
                }
                if (attrs.sliderDisabled != "true") {
                    attrs.sliderDisabled = false;
                }
                else {
                    attrs.sliderDisabled = true;
                }
                $(elem).slider({
                    range: true,
                    disabled: attrs.sliderDisabled,
                    min: 0,
                    max: scope.scaleSlider.length-1,
                    values: [value1, value2],
                    slide: function( event, ui ) {
                        scope.ngModel.values = [scope.scaleSlider[ui.values[0]], scope.scaleSlider[ui.values[1]]];
                        scope.$apply();
                    }
                });
            }
        }
    });
}());