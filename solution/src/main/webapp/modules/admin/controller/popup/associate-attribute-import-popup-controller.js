'use strict';

/**
 *
 * @param $scope
 * @param $log
 * @param $location
 */
function AssociateAttributeImportPopUpController($scope, $log, $timeout, $injector, $modalInstance, $state, layer, layerAttributes, markerAttributes, $importService) {

	$importService("layerGroupService");

	$scope.msg = null;

	/*-------------------------------------------------------------------
	 * 		 				 	ATTRIBUTES
	 *-------------------------------------------------------------------*/

	$scope.layer = layer;
	$scope.markerAttributes = markerAttributes;

	$scope.attributesByLayer = layerAttributes;


	/*$scope.setOption = function(index, attribute){

		var option = attribute.option;

		$timeout(function(){

			$scope.attributesByLayer[index].option = option;

			$scope.$apply();

		}, 100);

		$log.debug(attribute);
	};*/

	$scope.setMarkerAttribute = function(index, markerAttribute) {
		
		var attributeTypeImport = markerAttribute.option.match(/\((.*)\)/)[1];
		var attributeTypeLayer = $scope.attributesByLayer[index].type;
		
		if(markerAttribute.option != null && attributeTypeLayer != "MULTIPLE_CHOICE" && attributeTypeLayer != attributeTypeImport) {
			$scope.attributesByLayer[index].option = '';
		}

	};

	/*-------------------------------------------------------------------
	 * 		 				 	  BEHAVIORS
	 *-------------------------------------------------------------------*/

	/**
	 *
	 */
	$scope.close = function()
	{
		$scope.msg = null;
		$modalInstance.close({attributesByLayer: $scope.attributesByLayer});
	};
};
