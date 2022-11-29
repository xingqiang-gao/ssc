package jp.co.sej.ssc.mb.plugins.ns;


import static jp.co.sej.ssc.mb.R.*;

import android.annotation.SuppressLint;
import android.app.Service;
import android.media.MediaPlayer;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.util.Log;

import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CallbackContext;

import org.apache.cordova.PluginResult;
import org.json.JSONArray;
import org.json.JSONException;

import java.text.SimpleDateFormat;
import java.util.Date;

import jp.co.sej.ssc.mb.R;

/**
 * This class echoes a string called from JavaScript.
 */
public class NotificationSound extends CordovaPlugin {

    private static CallbackContext initCallbackContext;
    private static final String TAG = "NotificationSound";

    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
        switch (action) {
            case "init":
                initCallbackContext = callbackContext;
                Log.i(TAG, getLogForNow("NotificationSound init success."));
                return true;
            case "soundNotification":
                String message = args.getString(0);
                this.soundNotification(message, callbackContext);
                return true;
            case "vibratorNotification":
                int second = args.getInt(0);
                this.vibratorNotification(second, callbackContext);
                return true;
            case "vibratorStop":
                this.vibratorStop();
                return true;
        }
        return false;

    }

    private void soundNotification(String message, CallbackContext callbackContext) {
        MediaPlayer mediaPlayer = MediaPlayer.create(cordova.getActivity(), R.raw.beep);
        mediaPlayer.start();
    }

    private void vibratorNotification(int second, CallbackContext callbackContext) {
        Vibrator vibrator = (Vibrator) cordova.getActivity().getSystemService(Service.VIBRATOR_SERVICE);
        VibrationEffect vibrationEffect = VibrationEffect.createOneShot(second, VibrationEffect.DEFAULT_AMPLITUDE);
        vibrator.vibrate(vibrationEffect);
        callbackContext.success();
    }
    private void vibratorStop() {
        Vibrator vibrator = (Vibrator) cordova.getActivity().getSystemService(Service.VIBRATOR_SERVICE);
        vibrator.cancel();
    }

    public static void sendPushPayload(String msg) {
        Log.i(TAG, getLogForNow("NotificationSound sendPushPayload"));
        try {
            if (initCallbackContext != null) {
                PluginResult dataResult = new PluginResult(PluginResult.Status.OK, "NS_MESSAGE_CALLBACK" + "(" + msg + ")");
                dataResult.setKeepCallback(true);
                initCallbackContext.sendPluginResult(dataResult);
            }
        } catch (Exception e) {
            Log.d(TAG, "\tNotificationSound sendPushPayload: " + e.getMessage());
        }
    }

    @SuppressLint("SimpleDateFormat")
    public static String getLogForNow(String log) {
        Date d = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss.SSS");
        return "TimeStamp: " + sdf.format(d) + "  " + log;
    }
}
