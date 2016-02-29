(function (angular) {
  'use strict';

  /**
   *
   * @param $scope
   * @param $state
   */
  angular.module('application')
    .controller('GalleryController', function ($rootScope, $scope, $translate, $state, $document, $importService, $ionicGesture,
                                           $ionicPopup, $ionicSideMenuDelegate, $timeout, $cordovaDatePicker, $cordovaGeolocation,
                                           $filter, $log, $location, $ionicNavBarDelegate, $cordovaCamera, $ionicLoading,
                                           $cordovaToast, $ionicModal, $ionicSlideBoxDelegate, $ionicActionSheet) {

      /* GALLERY */

      $scope.addMedia = function () {
        $scope.hideSheet = $ionicActionSheet.show({
          buttons: [
            {text: 'Tirar foto'},
            {text: 'Galeria'}
          ],
          titleText: 'Adicionar imagens',
          cancelText: 'Cancelar',
          buttonClicked: function (index) {
            $log.debug(index);
            if(index == 1)
              $scope.getPhoto();
            else
              $scope.takePhoto();
          }
        });
      };

      $scope.selectPhoto = function(photo) {

        $scope.selectedPhoto = photo;
        photo.selected = !photo.selected;
        $scope.photosSelected = photo.selected ? $scope.photosSelected + 1 : $scope.photosSelected - 1;

      };

      $ionicModal.fromTemplateUrl('views/modal/gallery-modal.html', {
        scope: $scope,
        animation: 'slide-in-up'
      }).then(function(modal) {
        $scope.modal = modal;
      });

      $scope.openModal = function(index) {
        $ionicSlideBoxDelegate.slide(index);
        $scope.modal.show();
      };

      $scope.closeModal = function() {
        $scope.modal.hide();
      };

      // Cleanup the modal when we're done with it!
      $scope.$on('$destroy', function() {
        $scope.modal.remove();
      });
      // Execute action on hide modal
      $scope.$on('modal.hide', function() {
        // Execute action
      });
      // Execute action on remove modal
      $scope.$on('modal.removed', function() {
        // Execute action
      });
      $scope.$on('modal.shown', function() {
        console.log('Modal is shown!');
      });

      // Call this functions if you need to manually control the slides
      $scope.next = function() {
        $ionicSlideBoxDelegate.next();
      };

      $scope.previous = function() {
        $ionicSlideBoxDelegate.previous();
      };

      $scope.goToSlide = function(index) {
        $scope.modal.show();
        $ionicSlideBoxDelegate.slide(index);
      };

      // Called each time the slide changes
      $scope.slideChanged = function(index) {
        $scope.slideIndex = index;
        $scope.selectedPhoto = $scope.selectedPhotoAlbumAttribute.photoAlbum.photos[index];
        $scope.showEditableDescription = false;
      };

      $scope.deletePhoto = function(photo) {
        photo.deleted = true;
      };

      $scope.restorePhoto = function(photo) {
        photo.deleted = false;
      };

      $scope.editPhoto = function(state) {
        $scope.showEditableDescription = state;
      };

    });

}(window.angular));
