(function(angular) {
  'use strict';

  /**
   *
   * @param $scope
   * @param $state
   */
  angular.module('application')
    .controller('GalleryController', function($rootScope, $scope, $translate, $state, $timeout, $log, $cordovaCamera, $ionicLoading,
                                              $cordovaToast, $ionicModal, $ionicSlideBoxDelegate, $ionicActionSheet, $ionicPopup,
                                              $ionicHistory, $ionicPlatform, $cordovaFile) {

      var errorHandler = function (fileName, e) {
        var msg = '';

        switch (e.code) {
          case FileError.QUOTA_EXCEEDED_ERR:
            msg = 'Storage quota exceeded';
            break;
          case FileError.NOT_FOUND_ERR:
            msg = 'File not found';
            break;
          case FileError.SECURITY_ERR:
            msg = 'Security error';
            break;
          case FileError.INVALID_MODIFICATION_ERR:
            msg = 'Invalid modification';
            break;
          case FileError.INVALID_STATE_ERR:
            msg = 'Invalid state';
            break;
          default:
            msg = 'Unknown error';
            break;
        }

        $log.debug('Error (' + fileName + '): ' + msg);
      };

      $scope.convertImgToBase64URL = function (fileName, onSuccess) {
        var pathToFile = fileName;
        window.resolveLocalFileSystemURL(pathToFile, function (fileEntry) {
          fileEntry.file(function (file) {
            var reader = new FileReader();

            reader.onloadend = function (e) {
              onSuccess(this.result);
            };

            reader.readAsDataURL(file);
          }, errorHandler.bind(null, fileName));
        }, errorHandler.bind(null, fileName));
      };

      var makeId = function() {
        var text = '';
        var possible = 'ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789';

        for (var i = 0; i < 5; i++) {
          text += possible.charAt(Math.floor(Math.random() * possible.length));
        }
        return text;
      };

      $scope.renameFile = function(file, onSuccess) {

        var file = file.match(/(.*)\.png/)[0];

        var filePath = file.split('/');
        var fileName = filePath[filePath.length - 1];
        filePath = file.replace(fileName, '').slice(0,-1);

        var newFileName = makeId();

        $cordovaFile.copyFile(filePath, fileName, filePath, newFileName + '.png').then(function(success) {

          fileName = filePath + '/' + newFileName + '.png';

          onSuccess(fileName);

        });

      };

      $rootScope.$on('camera:result', function(event, data){

        $scope.renameFile(data, function(fileName){

          var selectedPhotoAlbumAttribute = angular.fromJson(localStorage.selectedPhotoAlbumAttribute);

          angular.forEach($scope.currentEntity.markerAttribute, function(markerAttribute){

            if(markerAttribute.attribute.id == selectedPhotoAlbumAttribute.attribute.id) {

              var photo = {};
              photo.image       = fileName;
              photo.name        = markerAttribute.name + '.png';
              photo.description = markerAttribute.name;
              photo.mimeType    = 'image/png';

              $scope.convertImgToBase64URL(fileName, function (data) {

                photo.source = data.split(';base64,')[1];
                photo.contentLength = photo.source.length;

                if (!markerAttribute.photoAlbum) {
                  markerAttribute.photoAlbum = {};
                  markerAttribute.photoAlbum.photos = [];
                }

                markerAttribute.photoAlbum.photos.push(photo);

                $rootScope.$broadcast('loading:hide');

                $cordovaToast.showShortBottom('Foto salva').then(function (success) {}, function (error) {});

              });
            }

          });

        });

      });

      $scope.$on('$ionicView.beforeEnter', function (event, viewData) {
        if(navigator && navigator.splashscreen) navigator.splashscreen.hide();

        $log.debug('beforeEnter');
        viewData.enableBack = true;
      });

      $ionicPlatform.registerBackButtonAction(function(e){
         $state.go($scope.MAP_MARKER);
      }, 100);

      $scope.hasSelectedPhotos = false;

      $scope.takePhoto = function() {

        var options = {
          quality: 100,
          destinationType: Camera.DestinationType.FILE_URI,
          sourceType: Camera.PictureSourceType.CAMERA,
          allowEdit: false,
          targetWidth: 640,
          targetHeight: 480,
          encodingType: Camera.EncodingType.PNG,
          popoverOptions: CameraPopoverOptions,
          saveToPhotoAlbum: true,
          correctOrientation: true
        };

        $cordovaCamera.getPicture(options).then(function(imageData) {

          /*$ionicLoading.show({
            template: 'Salvando foto',
            duration: 2000
          });*/

          //$rootScope.$broadcast('loading:show');

          $scope.renameFile(imageData, function(fileName){

            var photo = new Photo();
            photo.image = fileName;
            photo.name = $scope.selectedPhotoAlbumAttribute.name + '.png';
            photo.description = $scope.selectedPhotoAlbumAttribute.name;
            photo.contentLength = imageData.length;
            photo.mimeType = 'image/png';

            $scope.convertImgToBase64URL(fileName, function (data) {

              photo.source = data.split(';base64,')[1];

              if (!$scope.selectedPhotoAlbumAttribute.photoAlbum) {
                $scope.selectedPhotoAlbumAttribute.photoAlbum = new PhotoAlbum();
                $scope.selectedPhotoAlbumAttribute.photoAlbum.photos = [];
              }

              $scope.selectedPhotoAlbumAttribute.photoAlbum.photos.push(photo);

              $rootScope.$broadcast('loading:hide');

              $cordovaToast.showShortBottom('Foto salva').then(function (success) {}, function (error) {});

            });

          });

        }, function(err) {
          $log.debug(err);
          $rootScope.$broadcast('loading:hide');
        });
      };

      $scope.getPhoto = function() {

        var options = {
          quality: 100,
          destinationType: Camera.DestinationType.FILE_URI,
          sourceType: Camera.PictureSourceType.PHOTOLIBRARY,
          allowEdit: false,
          targetWidth: 640,
          targetHeight: 480,
          encodingType: Camera.EncodingType.PNG,
          popoverOptions: CameraPopoverOptions,
          correctOrientation: true
        };

        $cordovaCamera.getPicture(options).then(function(imageData) {

          /*$ionicLoading.show({
            template: 'Salvando foto',
            duration: 2000
          });*/

          $scope.renameFile(imageData, function(fileName){

            var photo = new Photo();
            photo.image = fileName;
            photo.name = $scope.selectedPhotoAlbumAttribute.name + '.png';
            photo.description = $scope.selectedPhotoAlbumAttribute.name;
            photo.contentLength = imageData.length;
            photo.mimeType = 'image/jpeg';

            $scope.convertImgToBase64URL(fileName, function (data) {

              photo.source = data.split(';base64,')[1];

              if (!$scope.selectedPhotoAlbumAttribute.photoAlbum) {
                $scope.selectedPhotoAlbumAttribute.photoAlbum = new PhotoAlbum();
                $scope.selectedPhotoAlbumAttribute.photoAlbum.photos = [];
              }

              $scope.selectedPhotoAlbumAttribute.photoAlbum.photos.push(photo);

              $rootScope.$broadcast('loading:hide');

              $cordovaToast.showShortBottom('Foto salva').then(function (success) {}, function (error) {});

            });

          });

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

      $scope.openModal = function(index) {
        $ionicSlideBoxDelegate.slide(index);
        $scope.selectedPhoto = $scope.selectedPhotoAlbumAttribute.photoAlbum.photos[index];
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

        event.stopPropagation();

        $scope.hasSelectedPhotos = false;

        photo.selected = !photo.selected;

        angular.forEach($scope.selectedPhotoAlbumAttribute.photoAlbum.photos, function(photo){
          if(photo.selected) {
            $scope.hasSelectedPhotos = true;
          }
        });

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
