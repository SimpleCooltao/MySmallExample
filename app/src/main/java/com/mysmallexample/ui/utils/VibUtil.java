package com.mysmallexample.ui.utils;

import android.app.Activity;
import android.app.Service;
import android.os.Vibrator;


public class VibUtil {
    /**
     * 震动
     *
     * @param activity     context
     * @param milliseconds 时间
     */
    public static void Vibrate(Activity activity, long milliseconds) {
        Vibrator vib = (Vibrator) activity.getSystemService(Service.VIBRATOR_SERVICE);
        vib.vibrate(milliseconds);
    }

    public static void Vibrate(Activity activity, long[] pattern, boolean isRepeat) {
        Vibrator vib = (Vibrator) activity.getSystemService(Service.VIBRATOR_SERVICE);
        vib.vibrate(pattern, isRepeat ? 1 : -1);
    }
}
