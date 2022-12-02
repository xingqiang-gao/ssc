package jp.co.sej.ssc.mb.plugins.bh;

import static jp.co.sej.ssc.mb.ConstantsDictionary.PLUGINS_DO_TASK;
import static jp.co.sej.ssc.mb.ConstantsDictionary.PLUGINS_INIT;

import android.annotation.SuppressLint;
import android.util.Log;

import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CallbackContext;

import org.apache.cordova.PluginResult;
import org.json.JSONArray;
import org.json.JSONException;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * This class echoes a string called from JavaScript.
 *
 * @author wangning
 */
public class BackgroundHandle extends CordovaPlugin {

    private static CallbackContext initCallbackContext;
    private static final String TAG = "BackgroundHandle";

    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
        if (PLUGINS_INIT.equals(action)) {
            initCallbackContext = callbackContext;
            Log.i(TAG, getLogForNow("BackgroundHandle init success."));
            return true;
        } else if (PLUGINS_DO_TASK.equals(action)) {
            this.doTask();
            return true;
        }
        return false;
    }

    private void doTask() {
        // todo...
        sendPushPayload("123");
    }

    public static void sendPushPayload(String msg) {
        Log.i(TAG, getLogForNow("BackgroundHandle sendPushPayload"));
        try {
            if (initCallbackContext != null) {
                PluginResult dataResult = new PluginResult(PluginResult.Status.OK, "BH_MESSAGE_CALLBACK" + "(" + msg + ")");
                dataResult.setKeepCallback(true);
                initCallbackContext.sendPluginResult(dataResult);
            }
        } catch (Exception e) {
            Log.d(TAG, "\tBackgroundHandle sendPushPayload: " + e.getMessage());
        }
    }

    @SuppressLint("SimpleDateFormat")
    public static String getLogForNow(String log) {
        Date d = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss.SSS");
        return "TimeStamp: " + sdf.format(d) + "  " + log;
    }
}
