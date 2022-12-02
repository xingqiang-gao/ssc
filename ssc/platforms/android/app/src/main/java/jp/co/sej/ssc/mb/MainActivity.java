/*
       Licensed to the Apache Software Foundation (ASF) under one
       or more contributor license agreements.  See the NOTICE file
       distributed with this work for additional information
       regarding copyright ownership.  The ASF licenses this file
       to you under the Apache License, Version 2.0 (the
       "License"); you may not use this file except in compliance
       with the License.  You may obtain a copy of the License at

         http://www.apache.org/licenses/LICENSE-2.0

       Unless required by applicable law or agreed to in writing,
       software distributed under the License is distributed on an
       "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
       KIND, either express or implied.  See the License for the
       specific language governing permissions and limitations
       under the License.
 */

package jp.co.sej.ssc.mb;

import static java.lang.Thread.sleep;

import static jp.co.sej.ssc.mb.ConstantsDictionary.CDV_START_IN_BACKGROUND;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import com.google.common.util.concurrent.ThreadFactoryBuilder;

import org.apache.cordova.*;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import jp.co.sej.ssc.mb.plugins.fcm.FcmPlugin;

/**
 * @author gaoxingqiang
 */
public class MainActivity extends CordovaActivity {
    private final String TAG = "MainActivity";
    Intent intent = null;

    @SuppressWarnings("AlibabaThreadShouldSetName")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i("onCreate:", "true");

        // enable Cordova apps to be started in the background
        intent = getIntent();
        Bundle extras = intent.getExtras();

        if (extras != null) {
            String extras0 = intent.getExtras().getString("title");
            String extras1 = intent.getExtras().getString("body");
            Log.i(TAG, getLogForNow("onCreate Message Title: " + extras0));
            Log.i(TAG, getLogForNow("onCreate Message Body: " + extras1));
            Map<String, Object> data = new HashMap<>(16);
            data.put("title", extras0);
            data.put("body", extras1);

            fcmStart(data);
        }

        if (extras != null && extras.getBoolean(CDV_START_IN_BACKGROUND, false)) {
            moveTaskToBack(true);
        }
        appLinkProcess();
        // Set by <content src="index.html" /> in config.xml
        loadUrl(launchUrl);
        // ATTENTION: This was auto-generated to handle app links.
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.i("onStart:", "true");
        if (intent.getExtras() != null) {
            String extras0 = intent.getExtras().getString("title");
            String extras1 = intent.getExtras().getString("body");
            Log.i(TAG, getLogForNow("onStart Message Title: " + extras0));
            Log.i(TAG, getLogForNow("onStart Message Body: " + extras1));

        }
        appLinkProcess();
    }

    private void appLinkProcess() {
        Intent appLinkIntent = getIntent();
        String appLinkAction = appLinkIntent.getAction();
        Uri appLinkData = appLinkIntent.getData();
        if (appLinkData != null) {
            String path = appLinkData.getPath();
            String param = appLinkData.getQuery();
            Log.i("test path:", path);
            Log.i("test param:", param);
        }
    }

    @Override
    public void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (intent.getExtras() != null) {
            String extras0 = intent.getExtras().getString("title");
            String extras1 = intent.getExtras().getString("body");
            Log.i(TAG, getLogForNow("onNewIntent Msg Title:" + extras0));
            Log.i(TAG, getLogForNow("onNewIntent Msg Body:" + extras1));
            Map<String, Object> data = new HashMap<>(16);
            data.put("title", extras0);
            data.put("body", extras1);
            FcmPlugin.sendPushPayload(data);
        }
        appLinkProcess();
    }

    @SuppressLint("SimpleDateFormat")
    private String getLogForNow(String log) {
        Date d = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss.SSS");
        return "TimeStamp: " + sdf.format(d) + "  " + log;
    }

    class FcmThread implements Runnable {
        private final Map<String, Object> data;

        public FcmThread(Map<String, Object> data) {
            this.data = data;
        }

        @Override
        public void run() {
            try {
                Log.i("FcmThread:", getLogForNow("running begin..."));
                sleep(60000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Log.i("FcmThread:", getLogForNow("running end..."));
            Log.i("FcmThread", getLogForNow("Send Message To Angular: " + this.data.toString()));
            FcmPlugin.sendPushPayload(this.data);
        }
    }

    private void fcmStart(Map<String, Object> data) {
        ThreadFactory threadFactory = new ThreadFactoryBuilder().setNameFormat("singleThreadPool").build();
        ThreadPoolExecutor singleThreadPool = new ThreadPoolExecutor(
                1, 1, 0L,
                TimeUnit.MILLISECONDS, new LinkedBlockingQueue<>(), threadFactory);
        singleThreadPool.execute(new FcmThread(data));
        singleThreadPool.shutdown();
    }

}
