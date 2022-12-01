cordova.define("NotificationSound.NotificationSound", function (require, exports, module) {
    var exec = require('cordova/exec');

    exports.soundNotification = function (arg0, success, error) {
        exec(success, error, 'NotificationSound', 'soundNotification', [arg0]);
    };
    exports.vibratorNotification = function (arg0, success, error) {
        exec(success, error, 'NotificationSound', 'vibratorNotification', [arg0]);
    };
    exports.vibratorStop = function (arg0, success, error) {
        exec(success, error, 'NotificationSound', 'vibratorStop', [arg0]);
    };

});
