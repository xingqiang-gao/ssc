package jp.co.sej.ssc.mb.plugins.SejLogManager.SejLogManager;

import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CallbackContext;

import org.json.JSONArray;
import org.json.JSONException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * This class echoes a string called from JavaScript.
 */
public class SejLogManager extends CordovaPlugin {

    private static final Logger logger = LogManager.getRootLogger();

    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
        if (action.equals("debug")) {
            String message = args.getString(0);
            cordova.getThreadPool().execute(new Runnable() {
                public void run() {
                    try {
                        logger.debug(message);
                        callbackContext.success();
                    } catch (Exception e) {
                        callbackContext.error("Logger exception:" + e.toString());
                    }
                }
            });
            return true;
        } else if (action.equals("info")) {
            String message = args.getString(0);
            cordova.getThreadPool().execute(new Runnable() {
                public void run() {
                    try {
                        logger.info(message);
                        callbackContext.success();
                    } catch (Exception e) {
                        callbackContext.error("Logger exception:" + e.toString());
                    }
                }
            });
            return true;
        } else if (action.equals("error")) {
            String message = args.getString(0);
            cordova.getThreadPool().execute(new Runnable() {
                public void run() {
                    try {
                        logger.error(message);
                        callbackContext.success();
                    } catch (Exception e) {
                        callbackContext.error("Logger exception:" + e.toString());
                    }
                }
            });
            return true;
        } else if (action.equals("warn")) {
            String message = args.getString(0);
            cordova.getThreadPool().execute(new Runnable() {
                public void run() {
                    try {
                        logger.warn(message);
                        callbackContext.success();
                    } catch (Exception e) {
                        callbackContext.error("Logger exception:" + e.toString());
                    }
                }
            });
            return true;
        }
        return false;
    }
}