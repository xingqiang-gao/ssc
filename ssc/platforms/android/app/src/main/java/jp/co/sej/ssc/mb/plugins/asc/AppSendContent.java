package jp.co.sej.ssc.mb.plugins.asc;

import static jp.co.sej.ssc.mb.ConstantsDictionary.PLUGINS_INSERT;
import static jp.co.sej.ssc.mb.ConstantsDictionary.PLUGINS_QUERY;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CallbackContext;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * This class echoes a string called from JavaScript.
 *
 * @author xingqianggao
 */
public class AppSendContent extends CordovaPlugin {
    private final String URL = "content://jp.co.sej.ssc.mb.SejContent/content";

    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
        if (PLUGINS_INSERT.equals(action)) {
            this.insert(args, callbackContext);
            return true;
        } else if (PLUGINS_QUERY.equals(action)) {
            this.query(args, callbackContext);
            return true;
        }
        return false;
    }

    private void insert(JSONArray args, CallbackContext callbackContext) {
        try {
            JSONObject param = args.getJSONObject(0);

            ContentValues values = new ContentValues();
            values.put("key1", param.getString("key"));
            values.put("value1", param.getString("value"));

            Uri uri = cordova.getContext().getContentResolver().insert(
                    Uri.parse(URL), values);

            callbackContext.success("success");
        } catch (Exception e) {
            callbackContext.error(e.getMessage());
        }
    }

    @SuppressLint("Range")
    private void query(JSONArray args, CallbackContext callbackContext) {
        try {
            if (args != null && args.length() > 0) {
                JSONObject param = args.getJSONObject(0);
                String id = param.getString("ID");
                JSONObject ret = new JSONObject();

                ContentResolver cr = cordova.getContext().getContentResolver();
                Uri uri = Uri.parse(URL);
                Cursor cursor = cr.query(uri, null, null, null, null);
                while (cursor.moveToNext()) {
                    String key = cursor.getString(cursor.getColumnIndex("key1"));
                    String value = cursor.getString(cursor.getColumnIndex("value1"));
                    ret.put(key, value);
                }
                Log.i("result:", ret.toString());
                callbackContext.success(ret);
                cursor.close();
            } else {
                callbackContext.error("Expected one non-empty string argument.");
            }
        } catch (Exception e) {
            callbackContext.error(e.getMessage());
        }
    }
}
