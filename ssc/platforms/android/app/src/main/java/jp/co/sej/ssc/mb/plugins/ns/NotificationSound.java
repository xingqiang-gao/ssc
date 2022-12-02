package jp.co.sej.ssc.mb.plugins.ns;


import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.os.VibratorManager;
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
 *
 * @author wangning
 */
public class NotificationSound extends CordovaPlugin {

    private static CallbackContext initCallbackContext;
    private static final String TAG = "NotificationSound";
    public static int current;
    private Vibrator vibrator;

    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
        switch (action) {
            case "init":
                initCallbackContext = callbackContext;
                Log.i(TAG, getLogForNow("NotificationSound init success."));
                return true;
            case "soundNotification":
                soundNotification(cordova.getContext());
                return true;
            case "vibratorNotification":
                int second = args.getInt(0);
                this.vibratorNotification(cordova.getContext(), second, callbackContext);
                return true;
            case "vibratorStop":
                this.vibratorStop();
                return true;
            default:
        }
        return false;
    }

    public static void soundNotification(Context context) {
        AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        MediaPlayer mediaPlayer = MediaPlayer.create(context, R.raw.beep);
        mediaPlayer.setOnCompletionListener(new CompletionListener(audioManager));
        current = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        if (current == 0) {
            audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, 8, 0);
            mediaPlayer.start();
        } else {
            mediaPlayer.start();
        }
    }

    private void vibratorNotification(Context context, int second, CallbackContext callbackContext) {
        VibrationEffect vibrationEffect = VibrationEffect.createOneShot(second, VibrationEffect.DEFAULT_AMPLITUDE);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.S) {
            VibratorManager vibratorManager = (VibratorManager) context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE);
            vibrator = vibratorManager.getDefaultVibrator();
        } else {
            vibrator = (Vibrator) context.getSystemService(Service.VIBRATOR_SERVICE);
        }
        vibrator.vibrate(vibrationEffect);
        callbackContext.success();
    }

    private void vibratorStop() {
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
