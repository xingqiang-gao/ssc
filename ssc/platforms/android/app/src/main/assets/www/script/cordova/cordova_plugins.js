cordova.define("cordova/plugin_list", function (require, exports, module) {
  module.exports = [
    {
      id: "FcmTooken.FcmTooken",
      file: "plugins/FcmTooken/www/FcmTooken.js",
      pluginId: "FcmTooken",
      clobbers: ["cordova.plugins.FcmTooken"],
    },
    {
      id: "cordova-plugin-battery-status.battery",
      file: "plugins/cordova-plugin-battery-status/www/battery.js",
      pluginId: "cordova-plugin-battery-status",
      clobbers: ["navigator.battery"],
    },
    {
      id: "cordova-plugin-fcm.FCMPlugin",
      file: "plugins/cordova-plugin-fcm/www/FCMPlugin.js",
      pluginId: "cordova-plugin-fcm",
      clobbers: ["FCMPlugin"],
    },
    {
      id: "AppSendContent.AppSendContent",
      file: "plugins/AppSendContent/www/AppSendContent.js",
      pluginId: "AppSendContent",
      clobbers: ["cordova.plugins.AppSendContent"],
    },
    {
      id: "SscLogManager.SscLogManager",
      file: "plugins/SscLogManager/www/SscLogManager.js",
      pluginId: "SscLogManager",
      clobbers: ["SscNativeLogManager"],
    },
    {
      id: "BackgroundHandle.BackgroundHandle",
      file: "plugins/BackgroundHandle/www/BackgroundHandle.js",
      pluginId: "BackgroundHandle",
      clobbers: ["cordova.plugins.BackgroundHandle"],
    },
    {
      id: "NotificationSound.NotificationSound",
      file: "plugins/NotificationSound/www/NotificationSound.js",
      pluginId: "NotificationSound",
      clobbers: [
        "cordova.plugins.NotificationSound"
      ]
    }
  ];
  module.exports.metadata = {
    FcmTooken: "1.00",
    "cordova-plugin-battery-status": "2.0.3",
    "cordova-plugin-fcm": "2.1.2",
    AppSendContent: "1.0.0",
    SscLogManager: "1.0.0",
    BackgroundHandle: "1.0.0",
    NotificationSound: "1.0.0"
  };
});
