package com.wangke.core.utils;

import android.os.Handler;
import android.view.Gravity;
import android.widget.Toast;

import com.wangke.core.WkCoreLib;

/**
 * ToastUtils.java
 * 解决单个弹出的问题。
 * @author Wilson
 * @description toast显示工具
 * @date 2015/10/26
 * @modifier
 */
public class ToastUtils {

    public static Toast sToast;
    private static Handler handler = new Handler();
    private static Runnable runnable = new Runnable() {
        public void run() {
            sToast.cancel();
        }
    };
    public static void show(String msg, int duration){
        handler.removeCallbacks(runnable);
        if (sToast != null){
            sToast.setText(msg);
        }else{
            sToast = Toast.makeText(WkCoreLib.getInstance(),msg,duration);
        }
        sToast.setGravity(Gravity.BOTTOM, 0, 100);
        if(duration == Toast.LENGTH_LONG){
            duration = 3000;
        }else{
            duration = 1500;
        }
        handler.postDelayed(runnable, duration);
        sToast.show();
    }

    public static void show(int msg,int duration){
        show(WkCoreLib.getInstance().getResources().getString(msg), duration);
    }
    public static  void show(int msg){
        show(WkCoreLib.getInstance().getResources().getString(msg), Toast.LENGTH_LONG);
    }

    public static  void show(String msg){
        show(msg, Toast.LENGTH_LONG);
    }
}
