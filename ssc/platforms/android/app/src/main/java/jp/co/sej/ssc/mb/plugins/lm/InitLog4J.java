package jp.co.sej.ssc.mb.plugins.lm;

import android.app.Application;


import jp.co.sej.ssc.mb.BuildConfig;
import jp.co.sej.ssc.mb.R;

/**
 * @author wangshiyu
 */
public class InitLog4J extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        if (BuildConfig.DEBUG) {
            Log4jHelper.initialise(this.getApplicationContext(), R.raw.log4j_debug);
        } else {
            Log4jHelper.initialise(this.getApplicationContext(), R.raw.log4j_release);
        }

    }
}
