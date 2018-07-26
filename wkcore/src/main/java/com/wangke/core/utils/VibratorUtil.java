package com.wangke.core.utils;

import android.app.Service;
import android.content.Context;
import android.os.Vibrator;



/**
 * Created by wk on 2017/12/22.
 */

public class VibratorUtil {
    //震动milliseconds毫秒
    public static void vibrate(final Context context, long milliseconds) {
        Vibrator vib = (Vibrator) context.getSystemService(Service.VIBRATOR_SERVICE);
        vib.vibrate(milliseconds);
        LogUtil.e("VibratorUtil", "milliseconds");
    }
    //以pattern[]方式震动
    public static void vibrate(final Context context, long[] pattern, int repeat){
        Vibrator vib = (Vibrator) context.getSystemService(Service.VIBRATOR_SERVICE);
        vib.vibrate(pattern,repeat);
        LogUtil.e("VibratorUtil", "pattern[]");

    }
    //取消震动
    public static void virateCancle(final Context context){
        Vibrator vib = (Vibrator) context.getSystemService(Service.VIBRATOR_SERVICE);
        vib.cancel();
        LogUtil.e("VibratorUtil", "virateCancle");

    }

}
