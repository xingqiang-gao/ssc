package jp.co.sej.ssc.mb;

import android.app.Application;


import jp.co.sej.ssc.mb.plugins.SscLogManager.log4j2android.AndroidLog4jHelper;

public class SejApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        if (BuildConfig.DEBUG) {
            AndroidLog4jHelper.initialise(this.getApplicationContext(), R.raw.log4j_debug);
        } else {
            AndroidLog4jHelper.initialise(this.getApplicationContext(), R.raw.log4j_release);
        }

    }
}
