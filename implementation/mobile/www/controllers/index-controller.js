(function(angular) {
  'use strict';

  /**
   *
   * @param $scope
   * @param $state
   */
  angular.module('application')
    .controller('IndexController', function($scope, $translate, $state, $ionicPopup, $ionicPopover, $filter, $log, $cordovaToast, $ionicModal) {

      // Configura o popover
      $ionicPopover.fromTemplateUrl('marker-view-options-menu.html', {
          scope: $scope
      }).then(function(popover) {
          $scope.popover = popover;
      });

      $scope.openPopover = function($event) {
        $scope.popover.show($event);
      };

      $scope.closePopover = function() {
        $scope.popover.hide();
      };

      //Cleanup the popover when we're done with it!
      $scope.$on('$destroy', function() {
        $scope.popover.remove();
      });

      // Execute action on hide popover
      $scope.$on('popover.hidden', function() {
        // Execute action
      });

      // Execute action on remove popover
      $scope.$on('popover.removed', function() {
        // Execute action
      });


      var options = {
        date: new Date(),
        mode: 'date', // or 'time'
        minDate: new Date() - 10000,
        allowOldDates: true,
        allowFutureDates: false,
        doneButtonLabel: 'DONE',
        doneButtonColor: '#F2F3F4',
        cancelButtonLabel: 'CANCEL',
        cancelButtonColor: '#000000'
      };

      $scope.showDatePicker = function(attribute) {
        $cordovaDatePicker.show(options).then(function(date) {
          var month = date.getMonth() > 10 ? date.getMonth() + 1 : '0' + (date.getMonth() + 1);
          attribute.value = date.getDate() + '/' + month + '/' + date.getFullYear();
          //alert(date);
        });
      };

      $scope.removeAllSelectedLayers = function() {

        angular.forEach($scope.allInternalLayerGroups, function(group) {
          if (group.visible) {
            group.visible = false;
            $scope.toggleLayer(group);
          }
        });

      };

      $scope.verifyStatus = function(){
        if (($scope.currentEntity.status == 'SAVED' || $scope.currentEntity.status == 'REFUSED' || $scope.currentEntity.status == 'CANCELED')
          && ($scope.currentEntity.user.id == $scope.userMe.id)) {
          $scope.isDisabled = false;
        }
      };

      /* MARKER MODERATIONS */

      /**
       * Accept marker
       */
      $scope.acceptMarkerModeration = function(id) {

        markerModerationService.acceptMarker(id, {
          callback: function(result) {

            $scope.currentEntity.status = result.marker.status;

            $state.go( $scope.MAP_INDEX );

            $scope.$apply();

            $cordovaToast.showShortBottom($translate('admin.marker-moderation.Marker-successfully-approved')).then(function(success) {
              // success
            }, function(error) {
              // error
            });
          },
          errorHandler: function(message, exception) {
            $log.debug(message);
            $scope.$apply();
          }
        });
      };

      /**
       * Refuse status marker moderation
       */
      $scope.refuseMarkerModeration = function(id, motive, description) {

        description = angular.isDefined(description) ? description : '';

        markerModerationService.refuseMarker(id, motive, description, {
          callback: function(result) {

            $scope.currentEntity.status = result.marker.status;

            $scope.verifyStatus();

            $state.go( $scope.MAP_INDEX );

            $scope.$apply();

            $cordovaToast.showShortBottom($translate('admin.marker-moderation.Marker-successfully-refused')).then(function(success) {
              // success
            }, function(error) {
              // error
            });
          },
          errorHandler: function(message, exception) {
            $log.debug(message);
            $scope.$apply();
          }
        });
      };

      /**
       * Cancel marker
       */
      $scope.cancelMarkerModeration = function(id) {
        markerModerationService.cancelMarkerModeration(id, {
          callback: function(result) {

            $scope.currentEntity.status = result.status;

            $state.go( $scope.MAP_INDEX );

            $scope.$apply();

            $cordovaToast.showShortBottom($translate('admin.marker-moderation.Marker-successfully-canceled')).then(function(success) {
              // success
            }, function(error) {
              // error
            });

          },
          errorHandler: function(message, exception) {
            $log.debug(message);
            $scope.$apply();
          }
        });
      };

      $scope.removeMarkerModeration = function() {
        markerService.removeMarker($scope.currentEntity.id, {
          callback: function(result) {

            var layer = $filter('filter')($scope.allInternalLayerGroups, {id: $scope.currentEntity.layer.id})[0];

            layer.visible = false;
            $scope.toggleLayer(layer);
            layer.visible = true;
            $scope.toggleLayer(layer);

            $scope.clearNewMarker();

            $state.go( $scope.MAP_INDEX );

            $scope.apply();

            $cordovaToast.showShortBottom($translate("map.Mark-was-successfully-deleted")).then(function(success) {
            }, function(error) {
            });

          },
          errorHandler: function(message, exception) {
            $scope.msg = {
              type: "error",
              text: message
            };
            $scope.$apply();
          }
        });
      };

      $scope.approveMarker = function() {
        $scope.closePopover();
        var confirmPopup = $ionicPopup.confirm({
          title: $translate('admin.marker-moderation.Confirm-approve'),
          template: $translate('admin.marker-moderation.Are-you-sure-you-want-to-approve-this-marker') + '?',
          cancelText: $translate('Close'),
          okText: $translate('admin.marker-moderation.Approve')
        });

        confirmPopup.then(function(res) {
          $log.debug(res);
          if (res) {
            $scope.acceptMarkerModeration($scope.currentEntity.id);
          }
        });

      };

      $scope.removeMarker = function() {
        $scope.closePopover();
        var confirmPopup = $ionicPopup.confirm({
          title: $translate("map.Delete-mark"),
          template: $translate("map.Are-you-sure-you-want-to-delete-the-mark") + '?',
          cancelText: $translate("admin.users.Cancel"),
          okText: $translate("layer-group-popup.Delete")
        });

        confirmPopup.then(function(res) {
          $log.debug(res);
          if (res) {
            $scope.removeMarkerModeration($scope.currentEntity.id);
          }
        });
      };

      $scope.listMotives = function() {

        motiveService.listMotives({
          callback: function(result) {
            $scope.motives = result;
            $scope.refuse = {motive : result[0]};
            $scope.$apply();
          },
          errorHandler: function(message, exception) {
            $log.debug(message);
            $scope.$apply();
          }
        });
      };

      $ionicModal.fromTemplateUrl('views/modal/marker-moderation-modal.html', {
        scope: $scope,
        animation: 'slide-in-up'
      }).then(function(modal) {

        $scope.refuseMarkerModal = modal;
      });

      $scope.refuseMarker = function() {
        $scope.closePopover();
        $scope.listMotives();
        $scope.refuseMarkerModal.show();
      };

      $scope.closeRefuseMarkerModal = function() {
        $scope.refuseMarkerModal.hide();
      };

      $scope.confirmRefuseMarkerModal = function(refuse) {
        $scope.refuseMarkerModal.hide();
        $scope.refuseMarkerModeration($scope.currentEntity.id, refuse.motive, refuse.description);
      };

      $scope.cancelMarker = function() {
        $scope.closePopover();
        var confirmPopup = $ionicPopup.confirm({
          title: $translate('admin.marker-moderation.Confirm-cancel'),
          template: $translate('admin.marker-moderation.Are-you-sure-you-want-to-cancel-this-marker') + '?',
          cancelText: $translate('Close'),
          okText: $translate('map.Confirm')
        });

        confirmPopup.then(function(res) {
          if (res) {
            $scope.cancelMarkerModeration($scope.currentEntity.id);
          }
        });

      };

    });

}(window.angular));
