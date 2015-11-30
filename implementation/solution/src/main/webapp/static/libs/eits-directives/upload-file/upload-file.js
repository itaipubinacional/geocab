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

      scope.files = scope.attribute.files != undefined ? scope.attribute.files : [];

      var inputFiles = document.getElementById('files');
      //============== DRAG & DROP =============
      // source for drag&drop: http://www.webappers.com/2011/09/28/drag-drop-file-upload-with-html5-javascript/
      var dropbox = document.getElementById("dropbox");
      scope.dropText = 'Drop files here...';

      // init event handlers
      function dragEnterLeave(evt) {
        evt.stopPropagation()
        evt.preventDefault()
        scope.$apply(function () {
          scope.dropText = 'Drop files here...'
          scope.dropClass = ''
        })
      };

      dropbox.addEventListener("dragenter", dragEnterLeave, false)
      dropbox.addEventListener("dragleave", dragEnterLeave, false)
      dropbox.addEventListener("dragover", function (evt) {
        evt.stopPropagation()
        evt.preventDefault()
        var clazz = 'not-available'
        var ok = evt.dataTransfer && evt.dataTransfer.types && evt.dataTransfer.types.indexOf('Files') >= 0
        scope.$apply(function () {
          scope.dropText = ok ? 'Drop files here...' : 'Only files are allowed!'
          scope.dropClass = ok ? 'over' : 'not-available'
        })
      }, false);

      dropbox.addEventListener("drop", function (evt) {
        console.log('drop evt:', JSON.parse(JSON.stringify(evt.dataTransfer)));
        evt.stopPropagation();
        evt.preventDefault();
        scope.$apply(function () {
          scope.dropText = 'Drop files here...'
          scope.dropClass = ''
        });
        var files = evt.dataTransfer.files;

        //document.getElementById('files').files = files;

        if (files.length > 0) {
          for (var i = 0, file; file = files[i]; i++) {

            //scope.files.push(files[i]);
            var reader = new FileReader();

            reader.onloadend = (function (readFile) {
              return function(e) {

                console.log(e.target.result);
                readFile.src = e.target.result;

                scope.files.push(readFile);

                if(files.length == i) {
                  scope.$apply();
                  scope.onSuccess({
                    files: scope.files
                  });
                }
              }
            })(file);

            reader.readAsDataURL(file);
          }

          console.log(scope.files);
        }
      }, false);

      //============== DRAG & DROP =============

      scope.setFiles = function (element) {
        scope.$apply(function (scope) {
          console.log('files:', element.files);
          // Turn the FileList object into an Array
          scope.files = []
          for (var i = 0; i < element.files.length; i++) {
            scope.files.push(element.files[i])
          }
          scope.progressVisible = false
        });
      };

      scope.uploadFile = function () {
        var fd = new FormData();
        for (var i in scope.files) {
          fd.append("uploadedFile", scope.files[i])
        }
        var xhr = new XMLHttpRequest()
        xhr.upload.addEventListener("progress", uploadProgress, false)
        xhr.addEventListener("load", uploadComplete, false)
        xhr.addEventListener("error", uploadFailed, false)
        xhr.addEventListener("abort", uploadCanceled, false)
        xhr.open("POST", "/fileupload")
        scope.progressVisible = true
        xhr.send(fd)
      };

      function uploadProgress(evt) {
        scope.$apply(function () {
          if (evt.lengthComputable) {
            scope.progress = Math.round(evt.loaded * 100 / evt.total)
          } else {
            scope.progress = 'unable to compute'
          }
        });
      }

      function uploadComplete(evt) {
        /* This event is raised when the server send back a response */
        alert(evt.target.responseText)
      }

      function uploadFailed(evt) {
        alert("There was an error attempting to upload the file.")
      }

      function uploadCanceled(evt) {
        scope.$apply(function () {
          scope.progressVisible = false
        });
        alert("The upload has been canceled by the user or the browser dropped the connection.")
      }

    }
  }
}]);
