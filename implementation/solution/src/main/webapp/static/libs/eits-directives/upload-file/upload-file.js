'use strict';

/**
 *
 * @param $scope
 * @param $modalInstance
 */
angular.module("eits-upload-file", []).directive('uploadFile', [function(){

  return {
    restrict: 'E',
    priority: -1000,
    templateUrl: 'static/libs/eits-directives/upload-file/upload-file.jsp',
    scope: {
      attribute: '=',
      onSuccess: '&',
      onError: '&',
      onLoading: '&'
    },
    link: function(scope, element, attrs){

      scope.isLoading = false;
      scope.over = false;
      scope.fileSelected = {};
      scope.files = scope.attribute.files != undefined ? scope.attribute.files : [];

      scope.maxSize = 2;
      scope.formats = ['image/jpeg', 'image/jpg', 'image/png'];

      element.on('load', function (event) {
        console.debug('load');
      });

      scope.loaded = function(){
        console.debug('loaded');
      };

      scope.setFile = function(file){
        scope.fileSelected = file;
        element.find('input[type="text"]').focus();
      };

      scope.toggleCheckbox = function(file){
        scope.fileSelected = file;
        element.find('input[type="text"]').focus();
      };

      scope.$watch('attribute.files', function(newVal, oldVal){
        scope.files = [];
        scope.files = newVal != undefined ? newVal : [];
        if(newVal)
          scope.setFile(newVal[0]);
      });

      scope.$watch('attribute', function(newVal, oldVal){
        scope.files = newVal.files != undefined ? newVal.files : [];

      });

      scope.isValidFormat = function (fileFormat) {
        if (scope.formats.indexOf(fileFormat) != -1)
          return true;

        scope.isLoading = false;
        scope.onError({msg: 'Invalid-format-only-jpg-png'});
        scope.$apply();
      };

      scope.isValidSize = function (fileSize) {
        var maxSize = (scope.maxSize * 1024) * 1024;
        if (maxSize > fileSize)
          return true;

        scope.isLoading = false;
        scope.onError({msg: 'Invalid-size-max-2-mb'});
        scope.$apply();
      };

      scope.isValidName = function (fileName) {
        var maxLength = 60;
        if (fileName.length < maxLength)
          return true;

        scope.isLoading = false;
        scope.onError({msg: 'Invalid-size-name-max-60-characters'});
        scope.$apply();
      };

      //============== DRAG & DROP =============
      // source for drag&drop: http://www.webappers.com/2011/09/28/drag-drop-file-upload-with-html5-javascript/
      var dropbox = document.getElementById("dropbox");

      dropbox.addEventListener('dragend', function( event ) {

        scope.$apply(function () {
          scope.over = false;
        });

      }, false);

      dropbox.addEventListener('dragover', function (event) {

        event.preventDefault();

        var hasFiles = event.dataTransfer && event.dataTransfer.types && event.dataTransfer.types.indexOf('Files') >= 0;

        if(hasFiles) {
          scope.$apply(function () {
            scope.over = true;
          });
        }
      }, false);

      dropbox.addEventListener('dragenter', function(event){

        scope.$apply(function () {
          scope.over = true;
        });

      }, false);

      dropbox.addEventListener('dragleave', function(event){

        scope.$apply(function () {
          scope.over = false;
        });

      }, false);

      dropbox.addEventListener('drop', function (event) {

        scope.onLoading({isLoading: true});

        event.preventDefault();

        var files = event.dataTransfer.files;

        if (files.length > 0) {

          scope.over = false;
          scope.isLoading = true;

          scope.$apply();

          for (var i = 0, file; file = files[i]; i++) {

            var reader = new FileReader();

            reader.onloadend = (function (readFile) {
              return function(e) {

                readFile.src = e.target.result;

                var fileToObj = angular.copy(readFile);
                scope.files.push(fileToObj);

                if(scope.files.length == i) {
                  scope.fileSelected = scope.files[0];
                  scope.isLoading = false;
                  scope.$apply();
                  scope.onSuccess({
                    files: scope.files
                  });

                }
              }
            })(file);

            if(scope.isValidFormat(file.type) && scope.isValidSize(file.size) && scope.isValidName(file.name))
              reader.readAsDataURL(file);
          }
        }
      }, false);

      scope.setFiles = function (element) {

        scope.onLoading({isLoading: true});

        scope.$apply(function (scope) {

          // Turn the FileList object into an Array
          var files = element.files;

          if (files.length > 0) {

            scope.over = false;
            scope.isLoading = true;

            for (var i = 0, file; file = files[i]; i++) {

              var reader = new FileReader();

              reader.onloadend = (function (readFile) {
                return function (e) {

                  readFile.src = e.target.result;

                  var fileToObj = angular.copy(readFile);
                  scope.files.push(fileToObj);

                  if (scope.files.length == i) {
                    scope.fileSelected = scope.files[0];
                    scope.isLoading = false;
                    scope.$apply();
                    scope.onSuccess({
                      files: scope.files
                    });

                  }
                }
              })(file);

              if (scope.isValidFormat(file.type) && scope.isValidSize(file.size) && scope.isValidName(file.name))
                reader.readAsDataURL(file);
            }
          }

        });
      };

      if(!scope.fileSelected.name && scope.files.length > 0)
        scope.setFile(scope.files[0]);

      scope.uploadFile = function(){
        setTimeout( function () {
          angular.element('#files').trigger('click');
        }, 0);

      };
    }
  }
}]);
