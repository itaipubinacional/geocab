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

	/*layerGroupService.listAttributesByLayer($scope.layer.layerId, {
		callback: function (result) {

			//console.log(result);

			angular.forEach(result, function(attribute){

				//console.log(attribute);

				if(attribute.type != 'PHOTO_ALBUM') {
					attribute.option = attribute.name + ' (' + attribute.type + ')';
					$scope.attributesByLayer.push(attribute);
				}

			});

			//$scope.layer.attributes = result;

			$scope.$apply();
		},
		errorHandler: function (message, exception) {
			$scope.message = {type: "error", text: message};
			$scope.$apply();
		}
	});*/

	/*$scope.setOption = function(index, attribute){

		var option = attribute.option;

		$timeout(function(){

			$scope.attributesByLayer[index].option = option;

			$scope.$apply();

		}, 100);

		$log.debug(attribute);
	};*/

	$scope.setMarkerAttribute = function(index, markerAttribute) {

		if(markerAttribute.option != null && $scope.attributesByLayer[index].type != markerAttribute.option.match(/\((.*)\)/)[1]) {
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
