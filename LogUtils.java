package com.xy.hellonewworld;

import android.util.Log;

/**
 * Created by Administrator on 2018/4/1.
 */

public class LogUtils {
    private static final boolean DEBUG = true;
    private static final String TAG = "CameraUtils";

    public static void d(String log){
        if(DEBUG){
            Log.d(TAG,log);
        }
    }
    public static void i(String log){
        if(DEBUG){
            Log.i(TAG,log);
        }
    }
    public static void v(String log){
        if(DEBUG){
            Log.v(TAG,log);
        }
    }
    public static void w(String log){
        if(DEBUG){
            Log.w(TAG,log);
        }
    }
    public static void e(String log){
        if(DEBUG){
            Log.e(TAG,log);
        }
    }
}
