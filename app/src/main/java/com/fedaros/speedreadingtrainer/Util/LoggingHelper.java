package com.fedaros.speedreadingtrainer.Util;

import android.util.Log;

/**
 * Created by Shoshaa on 1912//16.
 */

public class LoggingHelper {

    private boolean loggingSwitch;

    public LoggingHelper(boolean logging){
        this.loggingSwitch = logging;
    }

    public void LogDebug(String logTag, String message){
        if (loggingSwitch){
            Log.d(logTag, message);
        }
    }

    public void LogError(String logTag, String message){
        if (loggingSwitch){
            Log.e(logTag, message);
        }
    }
}
