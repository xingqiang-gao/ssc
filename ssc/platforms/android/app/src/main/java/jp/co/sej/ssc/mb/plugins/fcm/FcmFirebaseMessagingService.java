package jp.co.sej.ssc.mb.plugins.fcm;

import android.util.Log;

import androidx.annotation.NonNull;

import java.util.Map;
import java.util.HashMap;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import jp.co.sej.ssc.mb.plugins.ns.NotificationSound;

/**
 * @author xingqianggao
 */
public class FcmFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "FcMPlugin";

    /**
     * Called when message is received.
     * [START receive_message]
     *
     * @param remoteMessage Object representing the message received from Firebase Cloud Messaging.
     */
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Log.i(TAG, FcmPlugin.getLogForNow("==> FcmFirebaseMessagingService onMessageReceived"));
        Map<String, Object> data = new HashMap<>(16);
        if (remoteMessage.getNotification() != null) {
            String title = remoteMessage.getNotification().getTitle();
            String body = remoteMessage.getNotification().getBody();
            Log.i(TAG, FcmPlugin.getLogForNow("Notification Title: " + title));
            Log.i(TAG, FcmPlugin.getLogForNow("Notification Body: " + body));
            data.put("Notification Title", title);
            data.put("Notification Body", body);
        }
        for (String key : remoteMessage.getData().keySet()) {
            Object value = remoteMessage.getData().get(key);
            Log.i(TAG, FcmPlugin.getLogForNow("Key: " + key + " Value: " + value));
            data.put(key, value);
        }

        NotificationSound.soundNotification(getApplicationContext());
        FcmPlugin.sendPushPayload(data);
    }

    @Override
    public void onNewToken(@NonNull String token) {
        Log.d(TAG, "FCM Refreshed token: " + token);
        FcmPlugin.sendTokenRefresh(token);
    }


}
