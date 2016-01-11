'use strict';

/**
 * 
 * @param $scope
 * @param $log
 * @param $location
 */
function AssociateAttributeImportPopUpController($scope, $injector,$modalInstance, $state, layer, markerAttributes, $importService) {

	$importService("layerGroupService");

	$scope.msg = null;

	/*-------------------------------------------------------------------
	 * 		 				 	ATTRIBUTES
	 *-------------------------------------------------------------------*/

	$scope.layer = layer;
	$scope.markerAttributes = markerAttributes;

	$scope.attributesByLayer = [];

	layerGroupService.listAttributesByLayer($scope.layer.layerId, {
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
	});

	$scope.setMarkerAttribute = function(index, markerAttribute) {

		if($scope.attributesByLayer[index].type != markerAttribute.match(/\((.*)\)/)[1]) {
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