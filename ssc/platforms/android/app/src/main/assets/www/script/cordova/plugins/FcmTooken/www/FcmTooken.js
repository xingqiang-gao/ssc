cordova.define("FcmTooken.FcmTooken", function(require, exports, module) {
var exec = require('cordova/exec');

exports.getTooken = function (successCb, failureCb) {
    exec(successCb, failureCb, 'FcmTooken', 'getTooken', []);
};

});
