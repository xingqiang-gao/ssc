cordova.define("AppSendContent.AppSendContent", function(require, exports, module) {
var exec = require('cordova/exec');

exports.insert = function (arg0, success, error) {
    exec(success, error, 'AppSendContent', 'insert', [arg0]);
};

exports.query = function (arg0, success, error) {
    exec(success, error, 'AppSendContent', 'query', [arg0]);
};

});
