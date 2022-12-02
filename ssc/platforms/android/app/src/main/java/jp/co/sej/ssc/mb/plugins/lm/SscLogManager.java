package jp.co.sej.ssc.mb.plugins.lm;

import static jp.co.sej.ssc.mb.ConstantsDictionary.LOG_MANAGER_DEBUG;
import static jp.co.sej.ssc.mb.ConstantsDictionary.LOG_MANAGER_ERROR;
import static jp.co.sej.ssc.mb.ConstantsDictionary.LOG_MANAGER_INFO;
import static jp.co.sej.ssc.mb.ConstantsDictionary.LOG_MANAGER_WARN;

import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CallbackContext;

import org.json.JSONArray;
import org.json.JSONException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * This class echoes a string called from JavaScript.
 * @author wangshiyu
 */
public class SscLogManager extends CordovaPlugin {

    private static final Logger logger = LogManager.getRootLogger();

    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
        if (LOG_MANAGER_DEBUG.equals(action)) {
            String message = args.getString(0);
            cordova.getThreadPool().execute(() -> {
                try {
                    logger.debug(message);
                    callbackContext.success();
                } catch (Exception e) {
                    callbackContext.error("Logger exception:" + e);
                }
            });
            return true;
        } else if (LOG_MANAGER_INFO.equals(action)) {
            String message = args.getString(0);
            cordova.getThreadPool().execute(() -> {
                try {
                    logger.info(message);
                    callbackContext.success();
                } catch (Exception e) {
                    callbackContext.error("Logger exception:" + e);
                }
            });
            return true;
        } else if (LOG_MANAGER_ERROR.equals(action)) {
            String message = args.getString(0);
            cordova.getThreadPool().execute(() -> {
                try {
                    logger.error(message);
                    callbackContext.success();
                } catch (Exception e) {
                    callbackContext.error("Logger exception:" + e);
                }
            });
            return true;
        } else if (LOG_MANAGER_WARN.equals(action)) {
            String message = args.getString(0);
            cordova.getThreadPool().execute(() -> {
                try {
                    logger.warn(message);
                    callbackContext.success();
                } catch (Exception e) {
                    callbackContext.error("Logger exception:" + e);
                }
            });
            return true;
        }
        return false;
    }
}