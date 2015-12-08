'use strict';

/**
 *
 * @param $scope
 * @param $log
 * @param $location
 */
function ImgPopUpController($scope, $modalInstance, $log, photoAlbumIds, $importService) {

    $importService("markerService");

    /*-------------------------------------------------------------------
     * 		 				 	ATTRIBUTES
     *-------------------------------------------------------------------*/

    /**
     *
     * @type {null}
     */
    $scope.msg = null;

    /**
     *
     */
    $scope.currentEntity;


    $scope.currentPhoto = {};

    /**
     *
     */
    $scope.photos = [];


    /*-------------------------------------------------------------------
     * 		 				 	  NAVIGATIONS
     *-------------------------------------------------------------------*/
    /**
     * Main method that makes the role of front-controller of the screen.
     * He is invoked whenever there is a change of URL (@see $stateChangeSuccess),
     * When this occurs, gets the State via the $state and calls the initial method of that State.
     *
     * If the State is not found, he directs to the listing,
     * Although the front controller of Angular won't let enter an invalid URL.
     */
    $scope.initialize = function()
    {
        $scope.photoAlbumIds = photoAlbumIds;

        angular.forEach(photoAlbumIds, function (photoAlbumId) {

            markerService.listPhotosByPhotoAlbumId(photoAlbumId, {

                callback: function (result) {
                    console.log(result);

                    angular.forEach(result.content, function(photo, index){
                        if(index == 0)
                            $scope.currentPhoto = photo;
                        $scope.photos.push(photo);
                    });

                    $scope.$apply();
                },
                errorHandler: function (message, exception) {
                    $scope.message = {type: "error", text: message};
                    $scope.$apply();
                }

            });


        });


    };

    $scope.setCurrentPhoto = function(photo){

        markerService.findPhotoById(photo.id, {
            callback: function (result) {
                console.log(result);
                $scope.currentPhoto = result;
                $scope.$apply();
            },
            errorHandler: function (message, exception) {
                $scope.message = {type: "error", text: message};
                $scope.$apply();
            }
        });

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
        $modalInstance.close(null);
    };
    
    
};