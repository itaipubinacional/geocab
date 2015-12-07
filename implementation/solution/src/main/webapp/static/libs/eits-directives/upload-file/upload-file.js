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
    templateUrl: 'static/libs/eits-directives/upload-file/upload-file.html',
    scope: {
      attribute: '=',
      onSuccess: '&'
    },
    link: function(scope, element, attrs){

      scope.fileSelected = {};
      scope.files = scope.attribute.files != undefined ? scope.attribute.files : [];

      element.on('load', function (event) {
        console.log('load');
      });

      angular.element(document).ready(function() {
        console.log('ready');
      });

      scope.loaded = function(){
        console.log('loaded');
      };

      scope.$watch('attribute.files', function(n, o){
          console.log('watch');
      });

      scope.$watch('attribute', function(newVal, oldVal){
        console.log('watch');
        scope.files = newVal.files != undefined ? newVal.files : [];

      });

      scope.$watch('scope', function(n, o){
        console.log('watch');
      });

      //============== DRAG & DROP =============
      // source for drag&drop: http://www.webappers.com/2011/09/28/drag-drop-file-upload-with-html5-javascript/
      var dropbox = document.getElementById("dropbox");
      scope.dropText = 'Drop files here...';

      // init event handlers
      function dragEnterLeave(evt) {
        evt.stopPropagation();
        evt.preventDefault();
        scope.$apply(function () {
          scope.dropText = 'Drop files here...';
          scope.dropClass = '';
        })
      };

      scope.setImage = function(file){
        scope.fileSelected = file;
        element.find('input[type="text"]').focus();
        //scope.file.description = file.name;
      };

      dropbox.addEventListener("dragenter", dragEnterLeave, false);
      dropbox.addEventListener("dragleave", dragEnterLeave, false);
      dropbox.addEventListener("dragover", function (evt) {
        evt.stopPropagation();
        evt.preventDefault();
        var clazz = 'not-available';
        var ok = evt.dataTransfer && evt.dataTransfer.types && evt.dataTransfer.types.indexOf('Files') >= 0;
        scope.$apply(function () {
          scope.dropText = ok ? 'Drop files here...' : 'Only files are allowed!';
          scope.dropClass = ok ? 'over' : 'not-available';
        })
      }, false);

      dropbox.addEventListener("drop", function (evt) {
        console.log('drop evt:', JSON.parse(JSON.stringify(evt.dataTransfer)));
        evt.stopPropagation();
        evt.preventDefault();
        scope.$apply(function () {
          scope.dropText = 'Drop files here...';
          scope.dropClass = '';
        });
        var files = evt.dataTransfer.files;

        if (files.length > 0) {
          for (var i = 0, file; file = files[i]; i++) {

            var reader = new FileReader();

            reader.onloadend = (function (readFile) {
              return function(e) {

                console.log(e.target.result);
                readFile.src = e.target.result;

                var fileToObj = angular.copy(readFile);
                scope.files.push(fileToObj);

                if(files.length == i) {
                  scope.fileSelected = scope.files[0];
                  scope.$apply();
                  scope.onSuccess({
                    files: scope.files
                  });
                }
              }
            })(file);

            reader.readAsDataURL(file);
          }
        }
      }, false);

      //============== DRAG & DROP =============

      scope.setFiles = function (element) {
        scope.$apply(function (scope) {
          console.log('files:', element.files);
          // Turn the FileList object into an Array
          var files = element.files;

          for (var i = 0, file; file = files[i]; i++) {

            var reader = new FileReader();

            reader.onloadend = (function (readFile) {
              return function (e) {

                console.log(e.target.result);
                readFile.src = e.target.result;

                var fileToObj = angular.copy(readFile);
                scope.files.push(fileToObj);

                if (files.length == i) {
                  scope.fileSelected = scope.files[0];
                  scope.$apply();
                  scope.onSuccess({
                    files: scope.files
                  });
                }
              }
            })(file);

            reader.readAsDataURL(file);
          }

          scope.progressVisible = false;

        });
      };

      if(!scope.fileSelected.name && scope.files.length > 0)
        scope.setImage(scope.files[0]);

      scope.uploadFile = function(){
        setTimeout( function () {
          angular.element('#files').trigger('click');
        }, 0)

      };
    }
  }
}]);
