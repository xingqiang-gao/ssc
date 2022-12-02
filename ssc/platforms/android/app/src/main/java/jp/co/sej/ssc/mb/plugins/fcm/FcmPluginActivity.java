package jp.co.sej.ssc.mb.plugins.fcm;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;

import java.util.Map;
import java.util.HashMap;

/**
 * @author xingqianggao
 */
public class FcmPluginActivity extends Activity {
    private final static String TAG = "FcmPlugin";

    /**
     * this activity will be started if the user touches a notification that we own.
     * We send it's data off to the push plugin for processing.
     * If needed, we boot up the main activity to kickstart the application.
     *
     * @see android.app.Activity#onCreate(android.os.Bundle)
     */
    @SuppressLint("SuspiciousIndentation")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "==> FcmPluginActivity onCreate");

        Map<String, Object> data = new HashMap<>(16);
        if (getIntent().getExtras() != null) {
            Log.d(TAG, "==> USER TAPPED NOTFICATION");
            data.put("wasTapped", true);
            for (String key : getIntent().getExtras().keySet()) {
                String value = getIntent().getExtras().getString(key);
                Log.d(TAG, "\tKey: " + key + " Value: " + value);
                data.put(key, value);
            }
        }

        FcmPlugin.sendPushPayload(data);

        finish();

        forceMainActivityReload();
    }

    private void forceMainActivityReload() {
        PackageManager pm = getPackageManager();
        Intent launchIntent = pm.getLaunchIntentForPackage(getApplicationContext().getPackageName());
        startActivity(launchIntent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "==> FcmPluginActivity onResume");
        final NotificationManager notificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancelAll();
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG, "==> FcmPluginActivity onStart");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d(TAG, "==> FcmPluginActivity onStop");
    }

}