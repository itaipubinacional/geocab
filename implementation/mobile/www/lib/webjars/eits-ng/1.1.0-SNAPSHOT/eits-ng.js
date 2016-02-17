(function () {
  'use strict';
  angular.module('eits-ng', ['eits.dwr-broker', 'eits.translate',]);
})();

(function (window, angular, undefined) {
  'use strict';
  var translateModule = angular.module('eits.translate', []);
  translateModule.filter('translate', function ($translate, $parse) {
    var translateFilter = function (code, args) {
      args = eval(args);
      return $translate(code, args);
    };
    translateFilter.$stateful = true;
    return translateFilter;
  });
  translateModule.provider('$translate', function () {
    this.bundles = null;
    this.url = "./bundles";
    this.format = function (str, args) {
      return str.replace(new RegExp("{-?[0-9]+}", "g"), function (item) {
        var intVal = parseInt(item.substring(1, item.length - 1));
        var replace;
        if (intVal >= 0) {
          replace = args[intVal];
        } else if (intVal === -1) {
          replace = "{";
        } else if (intVal === -2) {
          replace = "}";
        } else {
          replace = "";
        }
        return replace;
      });
    };
    this.loadBundles = function () {
      var options = {dataType: "json", cache: true, async: false, url: this.url};
      var $self = this;
      $.ajax(options).done(function (data) {
        $self.bundles = data;
      }).fail(function (jqXHR, textStatus, exception) {
        console.error("Error loading bundles.", "Reason: " + exception);
      });
    }
    this.$get = function ($rootScope) {
      if (this.bundles == null)throw new Error("Problems loading bundles. Please check the URL.");
      var $self = this;
      return function (code, args) {
        Array.prototype.shift.apply(arguments);
        if (args) {
          return $self.format($self.bundles[code], arguments);
        }
        else {
          return $self.bundles[code];
        }
      };
    };
    this.useURL = function (url) {
      this.url = url;
      this.loadBundles();
    };
  });
})(window, window.angular);

(function (window, angular, undefined) {
  'use strict';
  var brokerModule = angular.module('eits.dwr-broker', []);
  brokerModule.provider('$importService', function () {
    this.url = "./broker/interface";
    this.options = {dataType: "script", cache: true, async: false, url: null};
    this.$get = function () {
      var $self = this;
      return function (service) {
        $self.options.url = $self.url + "/" + service + ".js";
        $.ajax($self.options).fail(function (jqXHR, textStatus, exception) {
          console.error("Error loading broker service: " + service, "Reason: " + exception);
        });
        return window[service];
      };
    };
    this.setBrokerURL = function (url) {
      this.url = url;
    };
  });
})(window, window.angular);
