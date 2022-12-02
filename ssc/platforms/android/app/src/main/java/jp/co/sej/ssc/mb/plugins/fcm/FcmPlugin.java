package jp.co.sej.ssc.mb.plugins.fcm;

import static jp.co.sej.ssc.mb.ConstantsDictionary.PLUGINS_GET_TOKEN;
import static jp.co.sej.ssc.mb.ConstantsDictionary.PLUGINS_READY;
import static jp.co.sej.ssc.mb.ConstantsDictionary.PLUGINS_REGISTER_NOTIFICATION;
import static jp.co.sej.ssc.mb.ConstantsDictionary.PLUGINS_SUBSCRIBE_TO_TOPIC;
import static jp.co.sej.ssc.mb.ConstantsDictionary.PLUGINS_UNSUBSCRIBE_FROM_TOPIC;

import org.apache.cordova.CordovaWebView;
import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CordovaInterface;

import android.annotation.SuppressLint;
import android.util.Log;

import androidx.annotation.NonNull;

import org.apache.cordova.PluginResult;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

/**
 * @author xingqianggao
 */
public class FcmPlugin extends CordovaPlugin {

    private static CallbackContext initCallbackContext;

    private static final String TAG = "FcmPlugin";

    public static CordovaWebView gWebView;
    public static String notificationCallBack = "FcmPlugin.onNotificationReceived";
    public static String tokenRefreshCallBack = "FcmPlugin.onTokenRefreshReceived";
    public static Boolean notificationCallBackReady = false;
    public static Map<String, Object> lastPush = null;

    public FcmPlugin() {
    }

    @Override
    public void initialize(CordovaInterface cordova, CordovaWebView webView) {
        super.initialize(cordova, webView);
        gWebView = webView;
        Log.d(TAG, "==> FcmPlugin initialize");
        FirebaseMessaging.getInstance().subscribeToTopic("android");
        FirebaseMessaging.getInstance().subscribeToTopic("all");
    }

    @Override
    public boolean execute(final String action, final JSONArray args, final CallbackContext callbackContext) throws JSONException {
        Log.d(TAG, "==> FcmPlugin execute: " + action);

        try {
            // READY //
            if (PLUGINS_READY.equals(action)) {
                //
                initCallbackContext = callbackContext;
                return true;
            }
            // GET TOKEN //
            else if (PLUGINS_GET_TOKEN.equals(action)) {
                try {
                    FirebaseMessaging.getInstance().setAutoInitEnabled(true);
                    FirebaseMessaging.getInstance().getToken()
                            .addOnCompleteListener(task -> {
                                if (!task.isSuccessful()) {
                                    Log.w(TAG, "Fetching FCM registration token failed", task.getException());
                                    callbackContext.error("Get token failed:" + task.getException().getMessage());
                                    return;
                                }
                                // Get new FCM registration token
                                String token = task.getResult();
                                Log.i(TAG, getLogForNow("FCM Token:" + token));
                                callbackContext.success(token);
                            });
                } catch (Exception e) {
                    callbackContext.error(e.getMessage());
                }
            }
            // NOTIFICATION CALLBACK REGISTER //
            else if (PLUGINS_REGISTER_NOTIFICATION.equals(action)) {
                notificationCallBackReady = true;
                cordova.getActivity().runOnUiThread(() -> {
                    if (lastPush != null) {
                        FcmPlugin.sendPushPayload(lastPush);
                        lastPush = null;
                        callbackContext.success();
                    }
                });
            }
            // UN/SUBSCRIBE TOPICS //
            else if (PLUGINS_SUBSCRIBE_TO_TOPIC.equals(action)) {
                cordova.getThreadPool().execute(() -> {
                    try {
                        FirebaseMessaging.getInstance().subscribeToTopic(args.getString(0));
                        callbackContext.success();
                    } catch (Exception e) {
                        callbackContext.error(e.getMessage());
                    }
                });
            } else if (PLUGINS_UNSUBSCRIBE_FROM_TOPIC.equals(action)) {
                cordova.getThreadPool().execute(() -> {
                    try {
                        FirebaseMessaging.getInstance().unsubscribeFromTopic(args.getString(0));
                        callbackContext.success();
                    } catch (Exception e) {
                        callbackContext.error(e.getMessage());
                    }
                });
            } else {
                callbackContext.error("Method not found");
                return false;
            }
        } catch (Exception e) {
            Log.d(TAG, "ERROR: onPluginAction: " + e.getMessage());
            callbackContext.error(e.getMessage());
            return false;
        }
        return true;
    }

    public static void sendPushPayload(Map<String, Object> payload) {
        Log.i(TAG, getLogForNow("FcmPlugin sendPushPayload"));
        try {
            JSONObject jo = new JSONObject();
            for (String key : payload.keySet()) {
                jo.put(key, payload.get(key));
                Log.d(TAG, "\tpayload: " + key + " => " + payload.get(key));
            }
            if (initCallbackContext != null) {
                PluginResult dataResult = new PluginResult(PluginResult.Status.OK, "FCM_MESSAGE_CALLBACK" + "(" + jo + ")");
                dataResult.setKeepCallback(true);
                initCallbackContext.sendPluginResult(dataResult);
            }
        } catch (Exception e) {
            Log.d(TAG, "\tERROR sendPushToView. SAVED NOTIFICATION: " + e.getMessage());
            lastPush = payload;
        }
    }

    public static void sendTokenRefresh(String token) {
        Log.d(TAG, "==> FcmPlugin sendRefreshToken");
        try {
            String callBack = "javascript:" + tokenRefreshCallBack + "('" + token + "')";
            gWebView.sendJavascript(callBack);
        } catch (Exception e) {
            Log.d(TAG, "\tERROR sendRefreshToken: " + e.getMessage());
        }
    }

    @SuppressLint("SimpleDateFormat")
    public static String getLogForNow(String log) {
        Date d = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss.SSS");
        return "TimeStamp: " + sdf.format(d) + "  " + log;
    }

    @Override
    public void onDestroy() {
        gWebView = null;
        notificationCallBackReady = false;
    }
} 
