'use strict';

/**
 *
 * @param $scope
 * @param $modalInstance
 * @constructor
 */
function AdicionarCamposPopUpController( $scope, $modalInstance, camada, camposCamadaExistentes ) {


    $scope.msg = null;

    /*-------------------------------------------------------------------
     * 		 				 	ATTRIBUTES
     *-------------------------------------------------------------------*/

    /**
     *
     */
    $scope.currentEntity;

    /**
     *
     */
    $scope.filterOptions = {
        filterText: ''
    };

    /**
     *
     * @type {boolean}
     */
    $scope.showLoading = true;

    $scope.camadas = {};

    /**
     * Define a linha que vai estar selecionada ao abrir a grid.
     * @type {Array}
     */
    $scope.gridSelectedItems = [];

    $scope.gridOptions = {
        data: 'camadas',
        multiSelect: true,
        showSelectionCheckbox: true,
        headerRowHeight: 45,
        filterOptions: $scope.filterOptions,
        selectedItems: $scope.gridSelectedItems,
        rowHeight: 45,
        enableRowSelection: true,
        afterSelectionChange: function (row, event) {

        },
        columnDefs: [
            {displayName:'Nome', field:'nome', width:'250px'},
            {displayName:'Tipo', field: 'tipoGeoServer'}
        ]
    };
    /*-------------------------------------------------------------------
     * 		 				 	  NAVIGATIONS
     *-------------------------------------------------------------------*/
    /**
     * Método principal que faz o papel de front-controller da tela.
     * Ele é invocado toda vez que ocorre uma mudança de URL (@see $stateChangeSuccess),
     * quando isso ocorre, obtém o estado através do $state e chama o método inicial daquele estado.
     * Ex.: /list -> changeToList()
     *      /criar -> changeToInsert()
     *
     * Caso o estado não for encontrado, ele direciona para a listagem,
     * apesar que o front-controller do angular não deixa digitar uma URL inválida.
     */
    $scope.initialize = function()
    {
        $scope.camada = camada;
        $scope.listCamposCamadasByFilters($scope.camada);
    };

    /*-------------------------------------------------------------------
     * 		 				 	  BEHAVIORS
     *-------------------------------------------------------------------*/

    /**
     * Realiza a consulta de registros, consirando filtro, paginação e sorting.
     * Quando ok, muda o estado da tela para list.
     *
     * @see data.filter
     * @see currentPage
     */
    $scope.listCamposCamadasByFilters = function( camada ) {
        grupoCamadasService.listCamposCamadasByFilter(camada, {
            callback: function (result) {
                $scope.camadas = result;
                $scope.showLoading = false;
                $scope.$apply();

                // função que seleciona na grid os campos que já estavam selecionados anteriormente
                var items = camposCamadaExistentes;
                if (items.length > 0) {
                    angular.forEach( $scope.camadas, function(data, index) {
                        data.ordem = null;
                        angular.forEach( items, function(item) {
                            if ( data.nome == item.nome ){
                                data.rotulo = item.rotulo;
                                data.ordem = item.ordem;
                                $scope.gridOptions.selectItem(index, true);
                            }
                        });
                    });
                }

                $scope.$apply();

            }, errorHandler : function(message, exception) {
                $log.error("mesage", message);
                $log.error("exception", message);
                $scope.showLoading = false;
                $scope.$apply();
            }
        });
    };



    /**
     * Sai da popup
     */
    $scope.fechaPopup = function ()
    {
        $modalInstance.close($scope.currentEntity);
    };

    $scope.form = function( formName )
    {

        if ( !formName )
        {
            formName = "form";
        }

        return $("form[name="+formName+"]").scope()[formName];
    };

    /**
     *
     */
    $scope.close = function( fechar )
    {

        if (fechar)
        {
            $modalInstance.close();

        } else
        {
            var selectedItems = $scope.gridOptions.selectedItems;

            for(var i=0; i< selectedItems.length; i++)
            {
                if( selectedItems[i].ordem == null )
                {
                    selectedItems[i].ordem = camposCamadaExistentes.length;
                    camposCamadaExistentes.push(selectedItems[i])
                }
            }

            selectedItems.sort(function(a, b)
            {
                return a.ordem - b.ordem;
            });

            $modalInstance.close(selectedItems);
        }

    };
};