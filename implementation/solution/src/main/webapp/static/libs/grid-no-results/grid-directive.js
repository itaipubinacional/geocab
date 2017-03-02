"use strict";

angular.module("grid-no-result", ['ngGrid']).directive('ngGrid', function() {

    return {
        link: function($scope, element, attrs) {

            $scope.$on("ngGridEventData", function(event) {

                var grid = $scope.$eval(attrs.ngGrid);

                if ($scope.$eval(grid.data) != undefined && $scope.$eval(grid.data).length == 0) {

                	var row = "<div ng-row=\"\" class=\"ngRow rowEmpty\" style=\"top: 0px; height: 45px;\">" +
				                    "<div class=\"ngCell\" style=\"width:100%;cursor: pointer;\">" +
				                        "<div ng-cell=\"\">" +
				                            "<div class=\"ngCellText\">" +
				                                "<span ng-cell-text=\"\" class=\"ng-binding\">Não existem registros.</span>" +
				                            "</div>" +
				                        "</div>" +
				                    "</div>" +
				                "</div>";


                    grid.ngGrid.$viewport.find('.ngCanvas').append(row);
                } else {

                    grid.ngGrid.$viewport.find('.rowEmpty').remove();
                  
                }

            });

            /**
             * Exibe uma linha que não possui registro quando é utiliazdo o filtro do angular sempre que o usuário digita
             */
            $scope.$on('ngGridEventRows', function() {

                if ($scope.gridOptions.ngGrid) {
                    if ($scope.gridOptions.ngGrid.filteredRows.length == 0) {
                        var row = "<div ng-row=\"\" class=\"ngRow rowEmpty\" style=\"top: 0px; height: 45px;\">" +
                            "<div class=\"ngCell\" style=\"width:100%;cursor: pointer;\">" +
                            "<div ng-cell=\"\">" +
                            "<div class=\"ngCellText\">" +
                            "<span ng-cell-text=\"\" class=\"ng-binding\">Não existem registros.</span>" +
                            "</div>" +
                            "</div>" +
                            "</div>" +
                            "</div>";


                        $scope.gridOptions.ngGrid.$viewport.find('.ngCanvas').append(row);
                    } else {
                        $scope.gridOptions.ngGrid.$viewport.find('.rowEmpty').remove();
                    }
                }


            });

        }
    }

})