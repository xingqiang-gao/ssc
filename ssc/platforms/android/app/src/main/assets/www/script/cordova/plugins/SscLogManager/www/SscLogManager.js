cordova.define(
  "SscLogManager.SscLogManager",
  function (require, exports, module) {
    var exec = require("cordova/exec");

    exports.info = function (arg0, success, error) {
      exec(success, error, "SscLogManager", "info", [arg0]);
    };

    exports.debug = function (arg0, success, error) {
      exec(success, error, "SscLogManager", "debug", [arg0]);
    };

    exports.error = function (arg0, success, error) {
      exec(success, error, "SscLogManager", "error", [arg0]);
    };

    exports.warn = function (arg0, success, error) {
      exec(success, error, "SscLogManager", "warn", [arg0]);
    };
  }
);
