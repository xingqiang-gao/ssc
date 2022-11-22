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
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import org.apache.cordova.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import jp.co.sej.ssc.mb.plugins.fcm.FCMPlugin;

public class MainActivity extends CordovaActivity
{
    private final String TAG = "MainActivity";
    Intent intent = null;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i("onCreate:", "true");

        // enable Cordova apps to be started in the background
        intent = getIntent();
        Bundle extras = intent.getExtras();

        if(extras !=null) {
            String extras0 = intent.getExtras().getString("title");
            String extras1 = intent.getExtras().getString("body");
            Log.i(TAG, getLogForNow("onCreate Message Title: " + extras0));
            Log.i(TAG, getLogForNow("onCreate Message Body: " +extras1));
            Map<String, Object> data = new HashMap<String, Object>();
            data.put("title", extras0);
            data.put("body", extras1);

            Thread ft = new Thread(new FCMThread(data));
            ft.start();
        }

        if (extras != null && extras.getBoolean("cdvStartInBackground", false)) {
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
        if(intent.getExtras() !=null) {
            String extras0 = intent.getExtras().getString("title");
            String extras1 = intent.getExtras().getString("body");
            Log.i(TAG, getLogForNow("onStart Message Title: " + extras0));
            Log.i(TAG, getLogForNow("onStart Message Body: " + extras1));

        }
        appLinkProcess();
    }

    private void appLinkProcess(){
        Intent appLinkIntent = getIntent();
        String appLinkAction = appLinkIntent.getAction();
        Uri appLinkData = appLinkIntent.getData();
        if(appLinkData != null) {
            String path = appLinkData.getPath();
            String param = appLinkData.getQuery();
            Log.i("test path:", path);
            Log.i("test param:", param);
        }
    }

    @Override
    public void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if(intent.getExtras() !=null) {
            String extras0 = intent.getExtras().getString("title");
            String extras1 = intent.getExtras().getString("body");
            Log.i(TAG, getLogForNow("onNewIntent Msg Title:" + extras0));
            Log.i(TAG, getLogForNow("onNewIntent Msg Body:" + extras1));
            Map<String, Object> data = new HashMap<String, Object>();
            data.put("title", extras0);
            data.put("body", extras1);
            FCMPlugin.sendPushPayload( data );
        }
        appLinkProcess();
    }

    private String getLogForNow(String log){
        Date d = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss.SSS");
        return  "TimeStamp: " + sdf.format(d) +"  "+  log;
    }

    class FCMThread implements Runnable{
        private Map<String, Object> _data = null;
        public FCMThread(Map<String, Object> data){
            this._data = data;
        }

        @Override
        public void run() {
            try {
                Log.i("FCMThread:",  getLogForNow("running begin..."));
                sleep(60000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Log.i("FCMThread:", getLogForNow("running end..."));
            Log.i("FCMThread", getLogForNow("Send Message To Angular: " + this._data.toString()));
            FCMPlugin.sendPushPayload( this._data );
        }
    }
}
