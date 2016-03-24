(function(angular) {
  'use strict';

  /**
   *
   * @param $scope
   * @param $state
   */
  angular.module('application')
    .controller('GalleryController', function($rootScope, $scope, $translate, $state, $timeout, $log, $cordovaCamera, $ionicLoading,
                                              $cordovaToast, $ionicModal, $ionicSlideBoxDelegate, $ionicActionSheet) {

      $rootScope.$on('camera:result', function(event, imageData){

        var selectedPhotoAlbumAttribute = angular.fromJson(localStorage.selectedPhotoAlbumAttribute);

        angular.forEach($scope.currentEntity.markerAttribute, function(markerAttribute){

          if(markerAttribute.attribute.id == selectedPhotoAlbumAttribute.attribute.id) {

            var photo = {};
            photo.name        = markerAttribute.name + '.png';
            photo.description = markerAttribute.name;
            photo.mimeType    = 'image/png';

            photo.source = imageData;
            photo.contentLength = photo.source.length;

            if (!markerAttribute.photoAlbum) {
              markerAttribute.photoAlbum = {};
              markerAttribute.photoAlbum.photos = [];
            }

            markerAttribute.photoAlbum.photos.push(photo);
            $rootScope.$broadcast('loading:hide');
            $cordovaToast.showShortBottom('Foto salva').then(function (success) {}, function (error) {});
          }
        });
      });

      $scope.$on('$ionicView.beforeEnter', function (event, viewData) {
        if(navigator && navigator.splashscreen) navigator.splashscreen.hide();

        $log.debug('beforeEnter');
        viewData.enableBack = true;
      });

      $scope.hasSelectedPhotos = false;

      $scope.takePhoto = function() {

        var options = {
          quality: 100,
          destinationType: Camera.DestinationType.DATA_URL,
          sourceType: Camera.PictureSourceType.CAMERA,
          allowEdit: false,
          targetWidth: 640,
          targetHeight: 480,
          encodingType: 1,
          popoverOptions: CameraPopoverOptions,
          saveToPhotoAlbum: true,
          correctOrientation: false,
          mediaType: 0
        };

        $cordovaCamera.getPicture(options).then(function(imageData) {

          var photo = new Photo();
          photo.name = $scope.selectedPhotoAlbumAttribute.name + '.png';
          photo.description = $scope.selectedPhotoAlbumAttribute.name;
          photo.contentLength = imageData.length;
          photo.mimeType = 'image/png';

          photo.source = imageData;

          if (!$scope.selectedPhotoAlbumAttribute.photoAlbum) {
            $scope.selectedPhotoAlbumAttribute.photoAlbum = new PhotoAlbum();
            $scope.selectedPhotoAlbumAttribute.photoAlbum.photos = [];
          }

          $scope.selectedPhotoAlbumAttribute.photoAlbum.photos.push(photo);
          $rootScope.$broadcast('loading:hide');
          $cordovaToast.showShortBottom('Foto salva').then(function (success) {}, function (error) {});

        }, function(err) {
          $log.debug(err);
          $rootScope.$broadcast('loading:hide');
        });
      };

      $scope.getPhoto = function() {

        var options = {
          quality: 100,
          destinationType: Camera.DestinationType.DATA_URL,
          sourceType: Camera.PictureSourceType.PHOTOLIBRARY,
          allowEdit: false,
          targetWidth: 640,
          targetHeight: 480,
          encodingType: 1,
          popoverOptions: CameraPopoverOptions,
          correctOrientation: false,
          mediaType: 0
        };

        $cordovaCamera.getPicture(options).then(function(imageData) {

          var photo = new Photo();
          photo.name = $scope.selectedPhotoAlbumAttribute.name + '.png';
          photo.description = $scope.selectedPhotoAlbumAttribute.name;
          photo.contentLength = imageData.length;
          photo.mimeType = 'image/jpeg';

          photo.source = imageData;

          if (!$scope.selectedPhotoAlbumAttribute.photoAlbum) {
            $scope.selectedPhotoAlbumAttribute.photoAlbum = new PhotoAlbum();
            $scope.selectedPhotoAlbumAttribute.photoAlbum.photos = [];
          }

          $scope.selectedPhotoAlbumAttribute.photoAlbum.photos.push(photo);
          $rootScope.$broadcast('loading:hide');
          $cordovaToast.showShortBottom('Foto salva').then(function (success) {}, function (error) {});

        }, function(err) {
          $log.debug(err);
          $rootScope.$broadcast('loading:hide');
        });
      };

      /* GALLERY */

      $scope.addMedia = function() {

        localStorage.setItem('currentEntity', angular.toJson($scope.currentEntity));
        localStorage.setItem('selectedPhotoAlbumAttribute', angular.toJson($scope.selectedPhotoAlbumAttribute));

        $scope.actionSheet = $ionicActionSheet.show({
          buttons: [{
            text: 'Tirar foto'
          }, {
            text: 'Galeria'
          }],
          titleText: 'Adicionar imagens',
          cancelText: 'Cancelar',
          buttonClicked: function(index) {
            $log.debug(index);
            if (index == 1)
              $scope.getPhoto();
            else
              $scope.takePhoto();
          }
        });

        $timeout(function() {
           $scope.actionSheet();
         }, 5000);
      };

      $ionicModal.fromTemplateUrl('views/modal/gallery-modal.html', {
        scope: $scope,
        animation: 'slide-in-up'
      }).then(function(modal) {
        $scope.modal = modal;
      });

      $scope.openModal = function(index, photo) {

        $log.debug($scope.hasSelectedPhotos);

        if ($scope.hasSelectedPhotos){

          photo.selected = !photo.selected;

          $scope.hasSelectedPhotos = false;

          angular.forEach($scope.selectedPhotoAlbumAttribute.photoAlbum.photos, function(photo){
            if(photo.selected) {
              $scope.hasSelectedPhotos = true;
            }

          });

        } else {
          $log.debug("openModal");
          $ionicSlideBoxDelegate.slide(index);
          $scope.selectedPhoto = $scope.selectedPhotoAlbumAttribute.photoAlbum.photos[index];
          $scope.modal.show();
        }
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
        $log.debug('Modal is shown!');
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


      $scope.selectPhoto = function(event, photo) {

        // event.stopPropagation();
        //

        $scope.hasSelectedPhotos = true;

        photo.selected = !photo.selected;

        // angular.forEach($scope.selectedPhotoAlbumAttribute.photoAlbum.photos, function(photo){
        //   if(photo.selected) {
        //     $scope.hasSelectedPhotos = true;
        //   }
        // });

      };

      $scope.deleteSelectedPhotos = function() {

        var i = $scope.selectedPhotoAlbumAttribute.photoAlbum.photos.length;

        while (i--) {
          var photo = $scope.selectedPhotoAlbumAttribute.photoAlbum.photos[i];

          if (photo.selected) {
            $scope.selectedPhotoAlbumAttribute.photoAlbum.photos.splice(i, 1);
          }
        }

        $scope.hasSelectedPhotos = false;

        angular.forEach($scope.selectedPhotoAlbumAttribute.photoAlbum.photos, function(photo){
          if(photo.selected) {
            $scope.hasSelectedPhotos = true;
          }
        });

      };

      $scope.showSavedPhotos = function() {

        for(var i = 0; i < $scope.currentEntity.markerAttribute.length; i++){
          if($scope.currentEntity.markerAttribute[i].type == 'PHOTO_ALBUM' && $scope.currentEntity.markerAttribute[i].photoAlbum != null && $scope.currentEntity.markerAttribute[i].photoAlbum.photos.length != 0) {

            $cordovaToast.showShortBottom('Fotos salvas').then(function(success) {
              // success
            }, function(error) {
              // error
            });

            break;
          }
        }

      };

    });

}(window.angular));
