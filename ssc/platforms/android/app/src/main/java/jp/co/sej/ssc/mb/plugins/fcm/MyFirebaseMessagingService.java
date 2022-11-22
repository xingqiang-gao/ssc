package jp.co.sej.ssc.mb.plugins.fcm;

import android.content.Intent;
import android.util.Log;
import androidx.annotation.NonNull;
import java.util.Map;
import java.util.HashMap;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "FCMPlugin";

    /**
     * Called when message is received.
     *
     * @param remoteMessage Object representing the message received from Firebase Cloud Messaging.
     */
    // [START receive_message]
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Log.i(TAG, FCMPlugin.getLogForNow("==> MyFirebaseMessagingService onMessageReceived"));
        Map<String, Object> data = new HashMap<String, Object>();
		if( remoteMessage.getNotification() != null){
            String title = remoteMessage.getNotification().getTitle();
            String body = remoteMessage.getNotification().getBody();
			Log.i(TAG, FCMPlugin.getLogForNow("Notification Title: " + title));
			Log.i(TAG, FCMPlugin.getLogForNow("Notification Body: " + body));
            data.put("Notification Title",title);
            data.put("Notification Body",body);
		}
		for (String key : remoteMessage.getData().keySet()) {
                Object value = remoteMessage.getData().get(key);
                Log.i(TAG, FCMPlugin.getLogForNow("Key: " + key + " Value: " + value));
				data.put(key, value);
        }

        FCMPlugin.sendPushPayload( data );
    }

    @Override
    public void onNewToken(@NonNull String token) {
        Log.d(TAG, "FCM Refreshed token: " + token);
        FCMPlugin.sendTokenRefresh( token );
    }
}
