cordova.define("BackgroundHandle.BackgroundHandle", function(require, exports, module) {
var exec = require('cordova/exec');

exports.init = function (arg0, error) {
    exec(
      function success(result){
        if (result) {
          eval(result);
        }

    }, error, 'BackgroundHandle', 'init', [arg0]);
};

exports.doTask = function (arg0, success, error) {
  exec(success, error, 'BackgroundHandle', 'doTask', [arg0]);
};

});
